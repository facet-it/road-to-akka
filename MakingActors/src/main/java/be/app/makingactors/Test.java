package be.app.makingactors;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

public class Test {
    
    public static void main(String[] args){
        ActorSystem system = ActorSystem.create("test");
        
        //ActorRef test = system.actorOf(GoodStringAnalyzer.props(true, 100), "test");
        
        //just instatiating an actor... which is bad. What would happen?
        //And exception is thrown! How crazy is that!
        /*
            Exception in thread "main" akka.actor.ActorInitializationException: You cannot create an instance of [be.app.makingactors.GoodStringAnalyzer] explicitly using the constructor (new). You have to use one of the 'actorOf' factory methods to create a new actor. See the documentation.
        
        GoodStringAnalyzer analyzer = new GoodStringAnalyzer(true, 100);
        
        analyzer.createReceive();
                
        */
        
        //This will throw an exception: 
        //ActorRef creation1 = system.actorOf(ActorCreation1.props(true, 100), "actorCreation1");
        
        ActorRef creation2 = system.actorOf(ActorCreation2.props(true, 100), "actorCreation2");
        
    }

}
