package be.app.makingactors;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;

/**
 * This is creating an Actor with a private constructor. Which works just fine! I like
 * this method the most, as clearly states the level of encapsulation.
 */
public class ActorCreation4 extends AbstractActor {
    
    private final LoggingAdapter log = Logging.getLogger(this.getContext().getSystem(), this);
    
    private final boolean someBoolean;
    private final int someInt;
    
    private ActorCreation4(boolean someBoolean, int someInt) {
        this.someBoolean = someBoolean;
        this.someInt = someInt;
    }
    
    // Good props. The create method of the Props has the order of the arguments right.
    public static Props props(int someInt, boolean someBoolean) {
        return Props.create(ActorCreation3.class, someBoolean, someInt);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().matchAny(this::talk).build();
    }
    
    private void talk(Object object) {
        System.out.println("i am an actor");
    }

}
