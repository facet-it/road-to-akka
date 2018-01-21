/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.app.makingactors;

import akka.actor.ActorSystem;
import akka.testkit.javadsl.TestKit;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Beheerder
 */
public class ActorCreation2Test {
    
    private TestKit probe;
    private static ActorSystem system;
    
    @BeforeClass
    public static void setUpClass() {
        system = ActorSystem.create();
    }
    
    @AfterClass
    public static void tearDownClass() {
        TestKit.shutdownActorSystem(system);
        system = null;
    }
    
    @Before
    public void setUp() {
        probe = new TestKit(system);
    }
    

    
    @Test
    public void InstantiatingActorWithoutCreateReceiveMethodThrowsException() throws Exception {
        system.actorOf(ActorCreation2.props(true, 100));
    }
    
}
