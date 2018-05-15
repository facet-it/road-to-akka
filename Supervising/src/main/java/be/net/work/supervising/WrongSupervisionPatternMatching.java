package be.net.work.supervising;

import akka.actor.AbstractActor;
import akka.actor.OneForOneStrategy;
import akka.actor.Props;
import akka.actor.SupervisorStrategy;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.DeciderBuilder;
import be.net.work.supervising.child.FailingChildOnStart;
import java.util.concurrent.TimeUnit;
import scala.PartialFunction;
import scala.concurrent.duration.Duration;

public class WrongSupervisionPatternMatching extends AbstractActor {
    
    private final LoggingAdapter logging = Logging.getLogger(getContext().getSystem(), this);
    
    public static Props props() {
        return Props.create(WrongSupervisionPatternMatching.class);
    }

    /**
     * Just a small test to check if the prestart hook is kicking in without a message being sent.
     * @throws Exception 
     */
    @Override
    public void preStart() throws Exception {
        logging.info("RestartSupervisor prestart hook kicking in....");
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().matchEquals("start", e -> startFailingChild()).build();
    }
    
    private void startFailingChild() {
        getContext().actorOf(FailingChildOnStart.props());
    }
    
    /**
     * This is an example of wrong pattern matching as a supervisor strategy. Because 
     * Exception is a superclass of the NeedRestartException, there is a match in the 
     * decider builder. Therefor, the stop supervisor strategy will be invoked instead
     * of the restart supervisor strategy. 
     */
    @Override
    public SupervisorStrategy supervisorStrategy() {
        PartialFunction strategy = DeciderBuilder.match(Exception.class, e -> SupervisorStrategy.stop())
                                                 .match(NeedRestartException.class, e -> SupervisorStrategy.restart())
                                                 .build();
        return new OneForOneStrategy(3, Duration.create(1, TimeUnit.MINUTES), strategy);
    }

}
