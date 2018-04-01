package be.app.makingactors;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

public class Test {
    
    public static void main(String[] args){
        ActorSystem system = ActorSystem.create("test");
        
        //This will throw an exception: 
        //ActorRef creation1 = system.actorOf(ActorCreation1.props(true, 100), "actorCreation1");
        
        ActorRef creation2 = system.actorOf(ActorCreation2.props(true, 100), "actorCreation2");
        //ActorRef creation3 = system.actorOf(ActorCreation3.badProps(100, true));
        
    }
    
    /**
     * When actors have a public constructor, you might be tempted to try and 
     * instantiate one without the use of the ActorSystem, just like that. Well, 
     * actors are supposed to be very encapsulated. The compiler will not complain
     * when you do so, but there will be an exception thrown at runtime!
     */
    public static void createActorWithoutTheSystem() {
        ActorCreation3 actor = new ActorCreation3(true, 100);
        
        actor.createReceive();
    }

}
