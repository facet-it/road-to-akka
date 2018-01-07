/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.app.akkacalculator.calculator.factor;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.testkit.javadsl.TestKit;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class FactorsCalculatorTest {
    
    private static ActorSystem system;
    private TestKit probe;
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
        calculator = system.actorOf(FactorsCalculator.props(), "Factors");
    }

    
    @Test
    public void itShouldReturnFactorsOfANumber() throws Exception {
        Factors.Request request = new Factors.Request("test", 6);
        List<Integer> factors = Arrays.asList(6,3,2,1);
        List<Integer> expectedResult = Collections.unmodifiableList(factors);
        
        calculator.tell(request, probe.getRef());
        
        Factors.Response response = probe.expectMsgClass(Factors.Response.class);
        
        assertEquals("test", response.getRequestId());
        assertEquals(expectedResult, response.getResult());
    }
    
}
