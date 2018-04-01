package be.net.work.stoppingactors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class Supervisor extends AbstractActor {
    
    private final LoggingAdapter logging = Logging.getLogger(getContext().getSystem(), this);
    private ActorRef unstoppable;
    
    public static Props props() {
        return Props.create(Supervisor.class);
    }

    @Override
    public void preStart() throws Exception {
        this.unstoppable = getContext().actorOf(Unstoppable.props());
    }
    

    @Override
    public Receive createReceive() {
        return receiveBuilder().matchEquals("go", this::go)
                               .matchEquals("stop", this::stopUnstoppable)
                               .matchEquals("forceStop", this::forceStop)
                               .build();
    }
    
    private void go(String goMessage) {
        logging.info("let the beast go");
        unstoppable.tell("go", getSelf());
    }
    
    private void stopUnstoppable(String stopMessage) {
        logging.info("telling to stop");
        unstoppable.tell("stop", getSelf());
    }
    
    private void forceStop(String forceStopMessage) {
        logging.info("Force to stop");
        getContext().getSystem().stop(unstoppable);
    }

}
