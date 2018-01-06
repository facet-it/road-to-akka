/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.app.akkacalculator.calculator.power;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.testkit.javadsl.TestKit;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;


public class PowerCalculatorTest {
    private TestKit probe;
    private static ActorSystem system;
    private ActorRef calculator;
    
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
        calculator = system.actorOf(PowerCalculator.props(), "Power");
    }
    
    @Test
    public void itShouldRespondOnACalculatePowerRequest() throws Exception {
        //create a request
        CalculatePower.Request request = new CalculatePower.Request("test", 2, 3);
        //send it to the actor, set the testkit as sender so it can expect a message
        calculator.tell(request, probe.getRef());
        //expect the response
        CalculatePower.Response result = probe.expectMsgClass(CalculatePower.Response.class);
        
        assertEquals("test", result.getRequestId());
        assertEquals(8, result.getResult());
        
    }
    
}
