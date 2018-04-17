package be.net.work.supervising.child;

import akka.actor.AbstractActor;
import akka.actor.OneForOneStrategy;
import akka.actor.Props;
import akka.actor.SupervisorStrategy;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.DeciderBuilder;
import be.net.work.supervising.NeedRestartException;
import be.net.work.supervising.NeedsStopException;
import be.net.work.supervising.SmallMistakeException;
import java.util.concurrent.TimeUnit;
import scala.PartialFunction;
import scala.concurrent.duration.Duration;

public class Child extends AbstractActor {
    private final LoggingAdapter logging = Logging.getLogger(getContext().getSystem(), this);
    
    private String currentState = "Initial state";
    
    public static Props props() {
        return Props.create(Child.class);
    }

    @Override
    public void postStop() throws Exception {
        logging.info("Just stopped...");
    }

    @Override
    public void preStart() throws Exception {
        logging.info("About to start...");
    }

    /**
     * This actor will use the escalate strategy when a child (a grand child of 
     * the original supervisor actor) throws an 'escalate exception'. 
     */
    @Override
    public SupervisorStrategy supervisorStrategy() {
        PartialFunction decider = DeciderBuilder.match(EscalateException.class, e -> SupervisorStrategy.escalate()).build();
        
        return new OneForOneStrategy(3, Duration.create(1, TimeUnit.MINUTES), decider);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().matchEquals("resume", message -> doResume())
                               .matchEquals("stop", message -> doStop())
                               .matchEquals("restart", message -> doRestart())
                               .matchEquals("showState", message -> showState())
                               .build();
    }
    
    private void doResume() throws SmallMistakeException {
        this.currentState = "will be resumed";
        throw new SmallMistakeException("woops");
    }
    
    private void doStop() throws NeedsStopException {
        this.currentState = "Will be stopped";
        throw new NeedsStopException("stop me!");
    }
    
    private void doRestart() throws NeedRestartException {
        this.currentState = "will be restarted";
        throw new NeedRestartException("restart me!");
    }
    
    private void showState() {
        logging.info(currentState);
    }
}
