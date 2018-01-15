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
     * I know, I know, there is no handling for any other kind of message that might get through yet :-)
     */
    @Override
    public Receive createReceive() {
        return receiveBuilder().match(CalculatePower.Request.class, this::doPower)
                               .match(Factors.Request.class, this::doFactors)
                               .match(CalculatePower.Response.class, this::answerPower)
                               .match(Factors.Response.class, this::answerFactors)
                               .build();
    }
    
    private void doPower(CalculatePower.Request request) {
        power.forward(request, this.getContext());
    }
    
    private void answerPower(CalculatePower.Response response) {
        getSender().tell(response, getSelf());
    }
    
    private void doFactors(Factors.Request request) {
        factors.forward(request, this.getContext());
    }
    
    private void answerFactors(Factors.Response response) {
        getSender().tell(response, getSelf());
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
