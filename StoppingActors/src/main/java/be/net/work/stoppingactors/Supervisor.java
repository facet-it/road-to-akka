package be.net.work.stoppingactors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Kill;
import akka.actor.PoisonPill;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;

/**
 * A 'supervising' actor. This one will try and send either a stop message or use
 * the context/system to try and stop the 'unstoppable' actor. 
 */
public class Supervisor extends AbstractActor {
    
    private final LoggingAdapter logging = Logging.getLogger(getContext().getSystem(), this);
    private ActorRef unstoppable;
    
    public static Props props() {
        return Props.create(Supervisor.class);
    }

    @Override
    public void preStart() throws Exception {
        this.unstoppable = getContext().actorOf(Unstoppable.props(), "unstoppable");
    }
    

    /**
     * It needs to be said that for the sake of simplicity, I only use simple String
     * messages instead of the full type matching. So actually sending the String
     * 'killIt' will not work any given actor unless you write you custom 
     * functionality for it like I do here. 
     */
    @Override
    public Receive createReceive() {
        return receiveBuilder().matchEquals("go", this::go)
                               .matchEquals("stop", this::stopUnstoppable)
                               .matchEquals("forceStop", this::forceStop)
                               .matchEquals("killIt", this::killIt)
                               .matchEquals("poisonIt", this::poisonIt)
                               .build();
    }
    
    //simple message to activate the unstoppable actor
    private void go(String goMessage) {
        logging.info("let the beast go");
        unstoppable.tell("go", getSelf());
    }
    
    //here I try to stop the 'unstoppable' actor by sending a custom 'stop' 
    //stop message. I do this to clearly indicate that the message won't be
    //picked up by the unstoppable actor because it is still processing the 
    //previous message.
    private void stopUnstoppable(String stopMessage) {
        logging.info("telling to stop");
        unstoppable.tell("stop", getSelf());
    }
    
    //this is using the context/
    private void forceStop(String forceStopMessage) {
        logging.info("Force to stop");
        getContext().getSystem().stop(unstoppable);
    }
    
    //Same here: Killing an actor is based on sending a message, which will 
    //not work when the actor never stops processing a previous message
    private void killIt(String killItMessage) {
        logging.info("Sending a kill message");
        unstoppable.tell(Kill.getInstance(), ActorRef.noSender());
    }
    
    //Same here: Sending the poisonPill message will not work either, because...
    //it is a message and the 
    private void poisonIt(String poisonMessage) {
        logging.info("Sending a poison pill message");
        unstoppable.tell(PoisonPill.getInstance(), ActorRef.noSender());
    }

}
