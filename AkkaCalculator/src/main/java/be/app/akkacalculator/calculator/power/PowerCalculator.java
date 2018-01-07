package be.app.akkacalculator.calculator.power;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import java.util.HashMap;
import java.util.Map;

/**
 * Actor which will calculate the power of a given base. 
 * 
 * Every actor extends from AbstractActor, which is a Java 8 API given by Akka. There
 * is also UntypedActor, which is from an earlier version of Akka. The AbstractActor
 * gives us a template for creating our own actors.
 * 
 * Game plan for actors: 
 * 
 * 1) You shoud never want an instance of an Actor
 * 2) You will never create an actor instance yourself
 * 3) Actors are super encapsulated. They are the building blocks for parallel 
 * programming, so an actor does not want to share state with the rest of the 
 * world. 
 * 4) communicating with an actor happens through messages and messages only. This 
 * really enforces the encapsulation.
 */
public class PowerCalculator extends AbstractActor{
    private static final String NAME = "POWER";
    private final LoggingAdapter log = Logging.getLogger(this.getContext().getSystem(), this);
    
    //This will function as an improvised cache.
    private final Map<Power, Integer> cache = new HashMap<>();
    
    //The props is the mechanism used for creating actors. It is probably short 
    //for properties. The Props is what is passed to the actor system, so that
    //you never have to deal with an actor instance. You pass the classname to 
    //the props and all of it's constructor params. In this case, there are none.
    public static Props props() {
        return Props.create(PowerCalculator.class);
    }
    /**
     * The heart of the actor. This is where some sort of class matching is done
     * to identify how to react to certain requests. 
     */
    @Override
    public Receive createReceive() {
        return receiveBuilder().match(CalculatePower.Request.class, this::calculatePower)
                               .build();
    }
    
    private void calculatePower(CalculatePower.Request request) {
        int base = request.getBase();
        int power = request.getPower();
        Power powerOf = new Power(base, power);
        Integer result = cache.get(powerOf);
        
        if(result == null) {
            result = doCalculation(base, power);
            cache.put(powerOf, result);
        }
        
        getSender().tell(new CalculatePower.Response(request.getRequestId(), result), getSelf());
    }
    
    private int doCalculation(int base, int power) {
        int result = 1;
        
        for(int i=power; i > 0; i--) {
            result = result * base;
        }
        
        return result;
    }
    
    //For now, these don't do much. They are lifecycle hooks
    @Override
    public void postStop() throws Exception {
        log.info("{} has stopped working", NAME);
    }

    @Override
    public void preStart() throws Exception {
        log.info("{} has started", NAME);
    }
    
    
    private static class Power {
        final int base;
        final int power;
        
        Power(int base, int power) {
            this.power = power;
            this.base = base;
        }
    }

}
