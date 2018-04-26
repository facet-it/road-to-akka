package be.net.work.supervising;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

public class App {
    
    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create();
        ActorRef superVisor = system.actorOf(Supervisor.props(), "supervisor");
        
        superVisor.tell("go", ActorRef.noSender());
    }

}
