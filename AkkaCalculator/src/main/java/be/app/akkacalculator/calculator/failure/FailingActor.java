package be.app.akkacalculator.calculator.failure;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.actor.Status;
import akka.event.Logging;
import akka.event.LoggingAdapter;

/**
 * This Actor will always throw an exception. The point is to learn to work
 * with futures that could fail. 
 * 
 * This is actually a bad example of a failing actor. The exception thrown really
 * is excetional. When expecting bad things that might happen, it is best better to
 * handle these situations by returning a message of type Status.Failure(new Exception("unknown message")). 
 * 
 * Why? Because when an object uses Patterns.ask to asynchronously deal with information 
 * coming from an actor, you need to handle the async call with CompletionStage.handle'. In the
 * case resembling this one, where the exception is not handled gracefully and just is thrown, 
 * the CompletionStage.handle will execute a function with null parameters, and it will 
 * wait for a returning message (which will never arrive, since we never send one back). 
 * 
 * 
 * I changed the failing actor so that we can look at what happens when it can either
 * fail or not.
 */
public class FailingActor extends AbstractActor{
    
    private final LoggingAdapter log = Logging.getLogger(this);
    
    public FailingActor() {}
    
    public static Props props() {
        return Props.create(FailingActor.class);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Fail.Request.class, this::handleFailMessage)
                .matchAny(e -> {log.info("bla");
                                                throw new Exception("Random exception");
                                              })
                .build();
    }
    
    private void handleFailMessage(Fail.Request request) {
        int failFactor = request.getFailFactor();
        
        if(failFactor %2 == 0) {
            getSender().tell(new Fail.Response(), getSelf());
        }
        else{
            getSender().tell(new Status.Failure(new Exception("Random exception")), getSelf());
        }
    }

    @Override
    public void postStop() throws Exception {
        log.info("Stopped the fail actor");
    }

    @Override
    public void preStart() throws Exception {
        log.info("Started the fail actor");
    }
}
