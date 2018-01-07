package be.app.akkacalculator.calculator.factor;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class FactorsCalculator extends AbstractActor{
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), FactorsCalculator.class);
    private final String NAME = "Factors";
    
    //This will function as an improvised cache.
    private final Map<FactorsOf, List<Integer>> cache = new HashMap<>();
    
    public static Props props() {
        return Props.create(FactorsCalculator.class);
    }
    

    @Override
    public Receive createReceive() {
        return receiveBuilder().match(Factors.Request.class, this::calculateFactors)
                        .build();
    }
    
    private void calculateFactors(Factors.Request request) {
        int base = request.getBase();
        FactorsOf factorsOf = new FactorsOf(base);
        List<Integer> factors = cache.get(factorsOf);
        
        if(factors == null) {
            factors = calculate(base);
            cache.put(factorsOf, factors);
        }
        
        getSender().tell(new Factors.Response(request.getRequestId(), factors), getSelf());
    }
    
    private List<Integer> calculate(int base) {
        List<Integer> factors = new LinkedList<>();
        
        for(int i = base; i > 0; i--) {
            int rest = base%i; 
            if(rest == 0) {
                factors.add(i);
            }
        }
        
        return Collections.unmodifiableList(factors);
    }

    @Override
    public void postStop() throws Exception {
        log.info("{} has stopped working", NAME);
    }

    @Override
    public void preStart() throws Exception {
        log.info("{} has started", NAME);
    }
    
    

    private static class FactorsOf{
        private int base;
        
        public FactorsOf(int base) {
            this.base = base;
        }

        public int getBase() {
            return base;
        }
    }
}
