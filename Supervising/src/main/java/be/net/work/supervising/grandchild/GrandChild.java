package be.net.work.supervising.grandchild;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import be.net.work.supervising.child.EscalateException;

public class GrandChild extends AbstractActor { 
    private final LoggingAdapter logging = Logging.getLogger(getContext().getSystem(), this);
    
    public static Props props() {
        return Props.create(GrandChild.class);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().matchEquals("escalate", message -> escalate())
                               .build();
    }
    
    private void escalate() throws EscalateException {
        logging.info("oooh boy, this needs escalation!!!");
        throw new EscalateException("can't deal with this");
    }

}
