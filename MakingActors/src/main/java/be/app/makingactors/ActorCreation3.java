package be.app.makingactors;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;

/**
 * So by now we know that we need a constructor. If the arguments match with was is given
 * to the props.
 */
public class ActorCreation3 extends AbstractActor {
    
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    
    private final boolean someBoolean;
    private final int someInt;
    
    public ActorCreation3(boolean someBoolean, int someInt) {
        this.someBoolean = someBoolean;
        this.someInt = someInt;
    }
    
    //This Props factory will not work. The order of the arguments is of importance!
    public static Props badProps(int someInt, boolean someBoolean) {
        //so here, the order in which the arguments appear is important. This means
        //that Akka will not scan for the type of the arguments, but just used the 
        //position of the props vararg to decide where to insert the aruments in the constructor
        return Props.create(ActorCreation3.class, someInt, someBoolean);
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
