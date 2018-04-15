package be.net.work.supervising;

import akka.actor.AbstractActor;
import akka.actor.OneForOneStrategy;
import akka.actor.Props;
import akka.actor.SupervisorStrategy;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.japi.pf.DeciderBuilder;
import java.util.concurrent.TimeUnit;
import scala.PartialFunction;
import scala.concurrent.duration.Duration;

/**
 * This will be the supervising actor where we will override the default 
 * supervising strategies. 
 */
public class Supervisor extends AbstractActor {
    private final LoggingAdapter logging = Logging.getLogger(getContext().getSystem(), this);
    
    /**
     * Still considered to be best practice to have a static props factory on your
     * actor class. When actually creating the actor by passing it's props to the
     * system, at least it is clear to the reader which actor you are actually creating
     * and what it needs.
     */
    public static Props props() {
        return Props.create(Supervisor.class);
    }

    /**
     * No escalation strategy here. I will put one in a child in order to see what
     * the actual supervisor will do with it. (Escalation strategy gets passed to 
     * the supervisor of the supervisor.
     */
    @Override
    public SupervisorStrategy supervisorStrategy() {
        PartialFunction decider = DeciderBuilder.match(SmallMistakeException.class, e -> SupervisorStrategy.resume())
                                    .match(NeedRestartException.class, e -> SupervisorStrategy.restart())
                                    .match(NeedsStopException.class, e -> SupervisorStrategy.stop())
                                    .build();
        
        return new OneForOneStrategy(2, Duration.create(1.0, TimeUnit.MINUTES), decider);
    }
    
    

    @Override
    public Receive createReceive() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
