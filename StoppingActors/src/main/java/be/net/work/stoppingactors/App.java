package be.net.work.stoppingactors;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

public class App {
    
    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create();
        
        ActorRef supervisor = system.actorOf(Supervisor.props());
        
        supervisor.tell("go", ActorRef.noSender());
        
        try{
            Thread.sleep(15000);
        }
        catch(InterruptedException ir) {
            ir.printStackTrace();
        }
        
        supervisor.tell("stop", ActorRef.noSender());
        
        try{
            Thread.sleep(15000);
        }
        catch(InterruptedException ir) {
            ir.printStackTrace();
        }
        
        supervisor.tell("forceStop", ActorRef.noSender());
    }

}
