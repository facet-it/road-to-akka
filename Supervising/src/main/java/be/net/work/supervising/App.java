package be.net.work.supervising;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

public class App {
    
    public static void main(String[] args) {
        //generalSupervisingTest();
        
        //testRestartSupervising();
        
        testWrongPatternMatching();
    }
    
    /**
     * Testing all of the strategies using our own supervisor actor.
     */
    public static void generalSupervisingTest() {
        ActorSystem system = ActorSystem.create();
        ActorRef superVisor = system.actorOf(Supervisor.props(), "supervisor");
        
        superVisor.tell("go", ActorRef.noSender());
    }
    
    /**
     * Focus on testing the restart strategy
     */
    public static void testRestartSupervising() {
        ActorSystem system = ActorSystem.create();
        ActorRef restartSupervisor = system.actorOf(TestRestartSupervisor.props());
        
        restartSupervisor.tell("start", ActorRef.noSender());
    }
    
    /**
     * Focus on testing wrong pattern matching
     */
    public static void testWrongPatternMatching() {
        ActorSystem system = ActorSystem.create();
        ActorRef wrongpatternMatching = system.actorOf(WrongSupervisionPatternMatching.props());
        
        wrongpatternMatching.tell("start", ActorRef.noSender());
    }

}
