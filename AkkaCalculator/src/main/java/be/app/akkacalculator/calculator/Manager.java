package be.app.akkacalculator.calculator;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import be.app.akkacalculator.calculator.factor.Factors;
import be.app.akkacalculator.calculator.factor.FactorsCalculator;
import be.app.akkacalculator.calculator.power.CalculatePower;
import be.app.akkacalculator.calculator.power.PowerCalculator;

public class Manager extends AbstractActor{
    
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    private ActorRef power;
    private ActorRef factors;
    
    public static Props props() {
        return Props.create(Manager.class);
    }

    /**
     * I know, I know, there is no handling for any random kind of message that might get through yet :-)
     * 
     * IMPORTANT NOTE
     * when forwarding messages, the original sender is automatically kept. This is awesome, 
     * as this means that I do not need to configure this manager to deal with the
     * responses of the forwarded messaged. So what happens when you forward is this: 
     * client object does managerRef.tell
     * managerActor receives and does childActor.forward
     * childactor handles message and does getSender.tell ------ > to client code
     * 
     * I hope that this actually makes sense when I read this in the future.
     */
    @Override
    public Receive createReceive() {
        return receiveBuilder().match(CalculatePower.Request.class, this::doPower)
                               .match(Factors.Request.class, this::doFactors)
                               .build();
    }
    
    private void doPower(CalculatePower.Request request) {
        power.forward(request, this.getContext());
    }
    
    private void doFactors(Factors.Request request) {
        factors.forward(request, this.getContext());
    }

    @Override
    public void postStop() throws Exception {
        log.info("Manager has stopped");
    }

    @Override
    public void preStart() throws Exception {
        log.info("Manager has started");
        power = this.getContext().actorOf(PowerCalculator.props(), "powers");
        factors = this.getContext().actorOf(FactorsCalculator.props(), "factors");
    }

}
