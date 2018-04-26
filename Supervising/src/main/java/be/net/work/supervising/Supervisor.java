package be.net.work.supervising;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.OneForOneStrategy;
import akka.actor.Props;
import akka.actor.SupervisorStrategy;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.DeciderBuilder;
import be.net.work.supervising.child.Child;
import be.net.work.supervising.child.EscalateException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import scala.PartialFunction;
import scala.concurrent.duration.Duration;

/**
 * This will be the supervising actor where we will override the default 
 * supervising strategies. 
 */
public class Supervisor extends AbstractActor {
    private final LoggingAdapter logging = Logging.getLogger(getContext().getSystem(), this);
    private final List<ActorRef> children = new ArrayList<>(4);
    
    public Supervisor() {
        for(int i = 0; i < 4; i++) {
            children.add(getContext().actorOf(Child.props(), "child-" + i));
        }
    }
    
    /**
     * Still considered to be best practice to have a static props factory on your
     * actor class. When actually creating the actor by passing it's props to the
     * system, at least it is clear to the reader which actor you are actually creating
     * and what it needs.
     */
    public static Props props() {
        return Props.create(Supervisor.class);
    }
    
    @Override
    public void postStop() throws Exception {
        logging.info("Supervisor just stopped...");
    }

    @Override
    public void preStart() throws Exception {
        logging.info("Supervisor about to start...");
    }

    /**
     * No escalation strategy here. I will put one in a child in order to see what
     * the actual supervisor will do with it. (Escalation strategy gets passed to 
     * the supervisor of the supervisor.
     * 
     * Mind the strategy for the Escalate exception, even though the child itself
     * will never directly throw such an exception. This exception is thrown in 
     * the grandchild, and the supervisor strategy of the child is 'escalate'. This
     * basically means that the child will pass the exception to it own supervisor, 
     * which is in this case the 'Supervisor actor' (This actor). 
     * 
     * If this actor does not have a strategy to handle this EscalateException. If 
     * not, then to the system it would look like the EscalateException was thrown 
     * in this actor, triggering its very own supervisor (user guardian actor) and
     * its default supervisor strategy will kick in, which is a restart in the case 
     * of Exception (EscalateException extends Exception class).
     */
    @Override
    public SupervisorStrategy supervisorStrategy() {
        PartialFunction decider = DeciderBuilder.match(SmallMistakeException.class, e -> SupervisorStrategy.resume())
                                    .match(NeedRestartException.class, e -> SupervisorStrategy.restart())
                                    .match(NeedsStopException.class, e -> SupervisorStrategy.stop())
                                    .match(EscalateException.class, e -> SupervisorStrategy.resume())
                                    .build();
        
        return new OneForOneStrategy(2, Duration.create(1.0, TimeUnit.MINUTES), decider);
    }
    
    

    @Override
    public Receive createReceive() {
        return receiveBuilder().matchEquals("go", message -> sendToAll()).build();
    }
    
    private void sendToAll() {
        sendRestart(children.get(0));
        sendStop(children.get(1));
        sendSmallMistake(children.get(2));
        sendEscalate(children.get(3));
    }
    
    private void sendRestart(ActorRef destination) {
        destination.tell("restart", getSelf());
        destination.tell("showState", getSelf());
    }
    
    private void sendStop(ActorRef destination) {
        destination.tell("stop", getSelf());
        destination.tell("showState", getSelf());
    }
    
    private void sendSmallMistake(ActorRef destination) {
        destination.tell("resume", getSelf());
        destination.tell("showState", getSelf());
    }
    
    private void sendEscalate(ActorRef destination) {
        destination.tell("escalate", getSelf());
        destination.tell("showState", getSelf());
    }
}
