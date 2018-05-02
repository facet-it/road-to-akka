package be.net.work.supervising;

import akka.actor.AbstractActor;
import akka.actor.AllForOneStrategy;
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

/**
 * This actor is going to start a child, which will fail almost instantly. The supervision
 * strategy will be to restart. With this I want to check out the behaviour of the other
 * parameters when declaring the supervision strategy. For example, one can declare
 * that for this particular strategy, a restart can happen up to 10 times in a minute.
 * If that boundary is crossed, then the child actor is just stopped.
 * @author Beheerder
 */
public class TestRestartSupervisor extends AbstractActor {
    
    private final LoggingAdapter logging = Logging.getLogger(getContext().getSystem(), this);
    
    public static Props props() {
        return Props.create(TestRestartSupervisor.class);
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
     * This is where the 'magic' is happening. A one for one strategy is only
     * for the affected child. An AllForOneStrategy will affect all of the children, 
     * whether they are experiencing errors or not. Obviously, this type of strategy is
     * for very tightly coupled children.
     * 
     * In this case, we should retry 3 times before this actor is stopped. This only counts
     * for restarting. 
     * @return 
     */
    @Override
    public SupervisorStrategy supervisorStrategy() {
        PartialFunction strategy = DeciderBuilder.match(NeedRestartException.class, e -> SupervisorStrategy.restart()).build();
        return new OneForOneStrategy(3, Duration.create(1, TimeUnit.MINUTES), strategy);
    }
}
