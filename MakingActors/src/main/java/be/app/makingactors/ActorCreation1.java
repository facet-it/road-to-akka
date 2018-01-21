package be.app.makingactors;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;

/**
 * Actors should be created using the Props mechanism. The props object lets us pass
 * the actor class we want to instantiate and all of the arguments it needs. It is best 
 * practise to have a static factory method for the props and keep them close to the class. Why you may 
 * ask? If you define the props ad hoc when you are creating an actor (which is possible)
 * but all of a sudden the constructor need to change, you only need to change the props in one
 * place, the same place as where the constructor changes. 
 * 
 * This class will not have a constructor for all of the fitting arguments, and 
 * instantiation even with props will fail. It will throw following exception on 
 * instantiation: 
 * 
 * Exception in thread "main" java.lang.IllegalArgumentException: no matching constructor found on 
 * class be.app.makingactors.ActorCreation1 for arguments [class java.lang.Boolean, class java.lang.Integer]
 */
public class ActorCreation1 extends AbstractActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    
    private boolean someBoolean;
    private int someInt;
    
    public static Props props(boolean someBoolean, int someInt) {
        return Props.create(ActorCreation1.class, someBoolean, someInt);
    }
    

    /*
    SIDENOTE: you always need to really implement the createReceive method or you 
    will not be able to create your actor. So even if we had a constructor, instatiating
    this would still fail. But when creating an actor, the constructor is checked first.
    */
    @Override
    public Receive createReceive() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void postStop() throws Exception {
        log.info("Stopped ActorCreation1");
    }

    @Override
    public void preStart() throws Exception {
        log.info("Started ActorCreation1");
    }
    
    

}
