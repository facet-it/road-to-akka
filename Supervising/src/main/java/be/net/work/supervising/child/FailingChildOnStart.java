package be.net.work.supervising.child;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import be.net.work.supervising.NeedRestartException;

/**
 * This actor doesn't do much except failing right after it was started. This of
 * course for the sole purpose to test the supervision abilities of its parent.
 * @author Beheerder
 */
public class FailingChildOnStart extends AbstractActor {
    
    private final LoggingAdapter logging = Logging.getLogger(getContext().getSystem(), this);
    
    public static Props props() {
        return Props.create(FailingChildOnStart.class);
    }

    @Override
    public void preStart() throws Exception {
        logging.info("failing child on start started....");
        getSelf().tell("restart", ActorRef.noSender());
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().matchEquals("restart", e -> forceRestart()).build();
    }
    
    private void forceRestart() throws Exception {
        throw new NeedRestartException("cant handle this, restart me!!!");
    }
}
