package be.app.akkacalculator.calculator;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.pattern.Patterns;
import be.app.akkacalculator.calculator.factor.Factors;
import be.app.akkacalculator.calculator.power.CalculatePower;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.function.Consumer;
import java.util.function.Function;
import scala.compat.java8.FutureConverters;
import scala.concurrent.Future;

public class Calculus {
    
    /**
     * this feels a bit bad. The calculus is now also responsible for handling the 
     * futures.... Following Single Responsibility, if the handling of the resonses 
     * should change, then this calculus needs to change as well, while it is only 
     * meant as a gateway for reaching out to Actors.
     */
    private final Consumer<CalculatePower.Response> printer = response -> System.out.println(response.getResult());
    private final Function<CalculatePower.Response, String> powerConverter = response -> "the result of the power is " + response.getResult();
    //its easier to write then to read which is not so good
    private final Consumer<List<Integer>> printFactors = list -> list.stream().forEach(factor -> System.out.println("this is a factor: " + factor));
    private final ActorRef manager;
    
    public Calculus() {
        ActorSystem system = ActorSystem.create("calculus");
        manager = system.actorOf(Manager.props());
    }
    
    public void calculatePowerOf(int base, int power) {
        String requestId = "id::" + System.currentTimeMillis();
        CalculatePower.Request request = new CalculatePower.Request(requestId, base, power);
        
        askPower(request).thenApply(powerConverter).thenAccept(System.out::println);
    }
    
    //trying to make a logical abstraction like in the book "learning akka"
    private CompletionStage<CalculatePower.Response> askPower(CalculatePower.Request request) {
        Future powerFuture = Patterns.ask(manager, request, 2000);
        return FutureConverters.toJava(powerFuture);
    }
    
    public void calculateFactorsOf(int base) {
        String requestId = "id::" + System.currentTimeMillis();
        Factors.Request request = new Factors.Request(requestId, base);
        
        askFactors(request).thenApply(response -> response.getResult()).thenAccept(printFactors);
    }
    
    private CompletionStage<Factors.Response> askFactors(Factors.Request request) {
        Future factorsFuture = Patterns.ask(manager, request, 2000);
        return FutureConverters.toJava(factorsFuture);
    }

}
