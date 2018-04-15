package be.net.work.supervising.child;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import be.net.work.supervising.NeedRestartException;
import be.net.work.supervising.NeedsStopException;
import be.net.work.supervising.SmallMistakeException;

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
