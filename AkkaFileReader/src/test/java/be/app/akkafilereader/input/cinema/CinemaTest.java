/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.app.akkafilereader.input.cinema;

import akka.actor.AbstractActor;
import akka.actor.Props;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Beheerder
 */
public class CinemaTest {
    
    public CinemaTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of props method, of class Cinema.
     */
    @Test
    public void testProps() {
        System.out.println("props");
        String name = "";
        Props expResult = null;
        Props result = Cinema.props(name);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of createReceive method, of class Cinema.
     */
    @Test
    public void testCreateReceive() {
        System.out.println("createReceive");
        Cinema instance = null;
        AbstractActor.Receive expResult = null;
        AbstractActor.Receive result = instance.createReceive();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of postStop method, of class Cinema.
     */
    @Test
    public void testPostStop() throws Exception {
        System.out.println("postStop");
        Cinema instance = null;
        instance.postStop();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of preStart method, of class Cinema.
     */
    @Test
    public void testPreStart() throws Exception {
        System.out.println("preStart");
        Cinema instance = null;
        instance.preStart();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
