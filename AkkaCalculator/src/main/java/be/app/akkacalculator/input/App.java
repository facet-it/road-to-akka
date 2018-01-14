package be.app.akkacalculator.input;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.pattern.Patterns;
import be.app.akkacalculator.calculator.factor.Factors;
import be.app.akkacalculator.calculator.factor.FactorsCalculator;
import be.app.akkacalculator.calculator.power.CalculatePower;
import be.app.akkacalculator.calculator.power.PowerCalculator;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.function.Consumer;
import java.util.function.Function;
import scala.compat.java8.FutureConverters;
import scala.concurrent.Future;

/**
 * Class for testing async programming using Java and Akka
 * @author Beheerder
 */
public class App {
    
    public static Consumer<CalculatePower.Response> printer = response -> System.out.println(response.getResult());
    public static Function<CalculatePower.Response, String> powerConverter = response -> "the result of the power is " + response.getResult();
    //its easier to write then to read which is not so good
    public static Consumer<List<Integer>> printFactors = list -> list.stream().forEach(factor -> System.out.println("this is a factor: " + factor));
    
    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("Test");
        
        ActorRef powers = system.actorOf(PowerCalculator.props(), "powers");
        ActorRef factors = system.actorOf(FactorsCalculator.props(), "factors");
        
        //handleSuccessFullWithTransformResult(powers);
        handleSuccessThenAskOtherActor(powers, factors);
    }
    
    //handle a future and just consume the result
    public static void handlySuccessfullFuture(ActorRef ref) {
        CalculatePower.Request request = new CalculatePower.Request("test", 4, 8);
        /*
            Here we get a scala future as a return value. While we can work with that, 
            it is clunky as a java developer. That is why we need to transition them in 
            a CompletionStage.
        */
        Future result = Patterns.ask(ref, request, 2000);
        CompletionStage<CalculatePower.Response> stage = FutureConverters.toJava(result);
        
        //Lets print out the result using a consumer
        stage.thenAccept(printer);
    }
    
    //handle future, transform the result, consume the result
    public static void handleSuccessFullWithTransformResult(ActorRef ref) {
        CalculatePower.Request request = new CalculatePower.Request("test", 2, 10);
        Future result = Patterns.ask(ref, request, 2000);
        
        CompletionStage<CalculatePower.Response> stage = FutureConverters.toJava(result);
        
        //so here we are going to modify the result from the Actor to a string and then print it
        //do mind: converter is a function, takes a response, transforms it to a String
        stage.thenApply(powerConverter).thenAccept(System.out::println);
    }
    
    //We ask Actor for the power, then we will print out all of its factors
    public static void handleSuccessThenAskOtherActor(ActorRef first, ActorRef second) {
        CalculatePower.Request request = new CalculatePower.Request("test1", 5, 9);
        
        //a bit of a clunky function but it will do for now
        Function<CalculatePower.Response, CompletionStage<Factors.Response>> askFactors = response -> {
                int power = response.getResult();
                Factors.Request factorsRequest = new Factors.Request("ftest", power);
                Future factors = Patterns.ask(second, factorsRequest, 2000);
                return FutureConverters.toJava(factors);
        };
        
        Future result = Patterns.ask(first, request, 2000);
        CompletionStage<CalculatePower.Response> stage = FutureConverters.toJava(result);
        
        stage.thenCompose(askFactors).thenApply(factors -> factors.getResult()).thenAccept(printFactors);
    }

}
