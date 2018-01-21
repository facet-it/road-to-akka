package be.app.makingactors;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;

/**
 * Instantiating this actor will fail because there is no real implementation of
 * createReceive. 
 * 
 * It has to be noted that when creating a
 */
public class ActorCreation2 extends AbstractActor{
    
    private final LoggingAdapter log = Logging.getLogger(this.getContext().getSystem(), this);
    
    private final boolean someBoolean;
    private final int someInt;
    
    public static Props props(boolean someBoolean, int someInt) {
        return Props.create(ActorCreation2.class, someBoolean, someInt);
    }
    
    public ActorCreation2(boolean someBoolean, int someInt) {
        this.someBoolean = someBoolean;
        this.someInt = someInt;
    }

    @Override
    public Receive createReceive() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void postStop() throws Exception {
        log.info("Stopped ActorCreation2");
    }

    @Override
    public void preStart() throws Exception {
        log.info("Started ActorCreation2");
    }
}
