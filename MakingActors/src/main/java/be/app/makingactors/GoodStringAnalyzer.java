package be.app.makingactors;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class GoodStringAnalyzer extends AbstractActor {
    
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    
    private boolean keepLatestRequest;
    private int maxStringLength;
    
    //Actors are supposed to be very encapsulated. You should never get an instance of
    //an actor and should never be able to invoke actor methods directly. The point is
    //to use a props factory. I suspect that when creating an Actor passing the props
    //to the system, the actor class is scanned for a suitable constructor and the 
    //instantiation properties are inserted via the props. This feels a bit 
    //contradictory if you use a public constructor. Because that would mean that any
    //other object can just instantiate the actor. 
    
    //So I'm trying a private constructor, in hopes that the 'scanning' process is
    //using reflection...
    
    /*
        It does use reflection! Even more, if you do not have a matching constructor, 
        it will throw an exception on starting: 
        Exception in thread "main" java.lang.IllegalArgumentException: no matching constructor found on class be.app.makingactors.GoodStringAnalyzer for arguments [class java.lang.Boolean, class java.lang.Integer]
    */
    public GoodStringAnalyzer(boolean keepLatestRequest, int maxStringLength) {
        this.keepLatestRequest = keepLatestRequest;
        this.maxStringLength = maxStringLength;
    }
    
    public static Props props(boolean keepLatestRequest, int maxStringLength) {
        return Props.create(GoodStringAnalyzer.class, keepLatestRequest, maxStringLength);
    }
    
    private String latestRequest = "";

    //You cannot create an actor if this create receive method is not implemented
    @Override
    public Receive createReceive() {
        return receiveBuilder().matchAny(this::talk).build();
    }
    
    private void talk(Object object) {
        System.out.println("i am an actor");
    }

    @Override
    public void postStop() throws Exception {
        log.info("actor has stopped");
    }

    @Override
    public void preStart() throws Exception {
        log.info("actor has started");
    }
    
    

}
