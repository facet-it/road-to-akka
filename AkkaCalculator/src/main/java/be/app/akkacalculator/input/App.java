package be.app.akkacalculator.input;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.pattern.Patterns;
import be.app.akkacalculator.calculator.factor.Factors;
import be.app.akkacalculator.calculator.factor.FactorsCalculator;
import be.app.akkacalculator.calculator.failure.Fail;
import be.app.akkacalculator.calculator.failure.FailingActor;
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
        ActorRef failure = system.actorOf(FailingActor.props(), "failing");
        
        //handleSuccessFullWithTransformResult(powers);
        //handleSuccessThenAskOtherActor(powers, factors);
        
        //handleFailureButWrong(failure);
        
        //handleFailureGoodWay(failure);
        //doHandleSuccessfullCase(failure);
        
        doHandleFailGracefullyCase(failure);
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
    
    // this is surely not the right way of doing it. So the supervising actor stops the failing 
    // actor automatically and restarts it soon after. Also, the actor just throws an exception, 
    // it does not handle the exception gracefully with a Status.failure message. 
    public static void handleFailureButWrong(ActorRef ref) {
        Future result = Patterns.ask(ref, "random message", 2000);
        CompletionStage stage = FutureConverters.toJava(result);
        
        stage.thenAccept(e -> System.out.println("some went wrong probably " + e));
    }
    
    /**
     * this is basically the way to handle actors who might fail. But this is still not
     * the best of examples since the failing actor has no real 'return type'. It just fails
     * with an exception, always. This makes it hard to actually understand the Bifunction that
     * is given as parameter in for the handle method. 
     * 
     * But it is still a cool example to see what actually happens when an Actor throws a
     * totally unexpected exception. I have set the timeout to 10 seconds (10000) and 
     * what basically happens is that the CompletionStage does not complete until timeout
     * because it expects a message. Check the output of the timeout! It is still good
     * to know though!
     * 
     * The second parameter is never printed. I do not know why that is :-/
     */
    public static void handleFailureGoodWay(ActorRef ref) {
        Future result = Patterns.ask(ref, "random message", 10000);
        CompletionStage stage = FutureConverters.toJava(result);
        // Mind the difference here: the good way is to use the 'handle' method
        stage.handle((first, second) -> {
            System.out.println("this is the first parameter " + first);
            System.out.println("this is the second parameter " + second);
            return null;
        });
    }
    
    /**
     * So this will not fail, but we are using the CompletionStage.handle method 
     * to learn more about the parameters that are returned. Also, since I know
     * somewhat expect a message back, I can actually make a typed CompletionStage. This
     * looks not so dirty nor hacky anymore ;-)
     */
    public static void doHandleSuccessfullCase(ActorRef ref) {
        Fail.Request nonFail = new Fail.Request(2);
        
        Future result = Patterns.ask(ref, nonFail, 10000);
        CompletionStage<Fail.Response> stage = FutureConverters.toJava(result);
        
        stage.handle((first, second) -> {
            System.out.println("this is the first parameter " + first);
            System.out.println("this is the second parameter " + second);
            return null;
        });
    }
    /**
     * From these examples we can see that the first parameter of handle is the
     * response we expect when there is no exception. The second parameter is the 
     * Status.fail message we sent back when an exception is thrown and handled 
     * gracefully.
     */
    public static void doHandleFailGracefullyCase(ActorRef ref) {
        Fail.Request doFail = new Fail.Request(3);
        
        Future result = Patterns.ask(ref, doFail, 2000);
        CompletionStage<Fail.Response> stage = FutureConverters.toJava(result);
        
        stage.handle((first, second) -> {
            System.out.println("this is the first parameter " + first);
            System.out.println("this is the second parameter " + second);
            return null;
        });
        
    }

}
