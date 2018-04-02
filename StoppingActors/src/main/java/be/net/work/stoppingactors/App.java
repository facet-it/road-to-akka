package be.net.work.stoppingactors;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

public class App {
    
    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create();
        
        ActorRef supervisor = system.actorOf(Supervisor.props(), "supervisor");
        
        supervisor.tell("go", ActorRef.noSender());
        
        try{
            Thread.sleep(16000);
        }
        catch(InterruptedException ir) {
            ir.printStackTrace();
        }
        
        supervisor.tell("stop", ActorRef.noSender());
        
        try{
            Thread.sleep(16000);
        }
        catch(InterruptedException ir) {
            ir.printStackTrace();
        }
        
        supervisor.tell("forceStop", ActorRef.noSender());
        
        try{
            Thread.sleep(16000);
        }
        catch(InterruptedException ir) {
            ir.printStackTrace();
        }
        
        supervisor.tell("killIt", ActorRef.noSender());
        
        try{
            Thread.sleep(16000);
        }
        catch(InterruptedException ir) {
            ir.printStackTrace();
        }
        
        supervisor.tell("poisonIt", ActorRef.noSender());
    }

}
