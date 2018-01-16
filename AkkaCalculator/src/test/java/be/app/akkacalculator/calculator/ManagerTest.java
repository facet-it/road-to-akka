/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.app.akkacalculator.calculator;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.testkit.javadsl.TestKit;
import be.app.akkacalculator.calculator.factor.Factors;
import be.app.akkacalculator.calculator.power.CalculatePower;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ManagerTest {
    
    private static ActorSystem system;
    private TestKit probe;
    private ActorRef ref;
    
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
        ref = system.actorOf(Manager.props(), "test");
    }

    
    @Test
    public void itShouldForwardCalculatePowerMessages() throws Exception {
        CalculatePower.Request request = new CalculatePower.Request("test", 2, 3);
        ref.tell(request, probe.getRef());
        
        probe.expectMsgClass(CalculatePower.Response.class);
        
        //we can check the last sender, which should be the PowerCalculater Actor. You
        //can' t seem to test which actor was the last, but it at least you can test
        //that it is not the same as the original sender
        ActorRef lastSender = probe.getLastSender();
        Assert.assertNotEquals(lastSender, ref);
    }
    
    @Test
    public void itShouldForwardFactorsMessages() throws Exception {
        Factors.Request request = new Factors.Request("test", 100);
        ref.tell(request, probe.getRef());
        
        probe.expectMsgClass(Factors.Response.class);
        
        //we can check the last sender, which should be the PowerCalculater Actor. You
        //can' t seem to test which actor was the last, but it at least you can test
        //that it is not the same as the original sender
        ActorRef lastSender = probe.getLastSender();
        Assert.assertNotEquals(lastSender, ref);
    }
    
}
