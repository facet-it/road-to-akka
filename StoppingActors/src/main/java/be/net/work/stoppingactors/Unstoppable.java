package be.net.work.stoppingactors;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;

public class Unstoppable extends AbstractActor{
    private final LoggingAdapter logging = Logging.getLogger(getContext().getSystem(), this);
    
    public static Props props() {
        return Props.create(Unstoppable.class);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().matchEquals("go", this::goWild)
                               .matchEquals("stop", null)
                               .build();
    }
    
    private void goWild(String message) {
        logging.info("message received");
        while(true) {
            logging.info("I am going all out!!!!");
            
            try{
                Thread.sleep(5000);
            }
            catch(InterruptedException ir) {
                ir.printStackTrace();
            }
        }
    }
    
    //this message should actually never be executed, because the 'go' message
    //never finishes and thus the next message never gets handled.
    private void stop(String stopMessage) {
        System.out.println("Oooh the stop message!");
        this.context().stop(getSelf());
    }

}
