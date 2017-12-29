/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.app.akkafilereader.input.cinema;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.testkit.javadsl.TestKit;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Beheerder
 */
public class CinemaTest {
    
    static ActorSystem system;

    @BeforeClass
    public static void setup() {
        system = ActorSystem.create();
    }

    @AfterClass
    public static void teardown() {
        TestKit.shutdownActorSystem(system);
        system = null;
    }
   
    @Test
    public void testSettingTheProgram() {
        TestKit probe = new TestKit(system);
        ActorRef cinema = system.actorOf(Cinema.props("test"));
        String[] program = {"13.00-15.00: movie", "15.30-17.00: other movie"};
        StoreProgram request = new StoreProgram("request1", program);
        cinema.tell(request, probe.getRef());
        
        ProgramStored reply = probe.expectMsgClass(ProgramStored.class);
        Assert.assertEquals("request1", reply.getRequestId());
    }
    
    @Test
    public void testCheckIfProgramHasChanged() {
        TestKit probe = new TestKit(system);
        ActorRef cinema = system.actorOf(Cinema.props("test"));
        String[] program = {"13.00-15.00: movie", "15.30-17.00: other movie"};
        StoreProgram request = new StoreProgram("request1", program);
        cinema.tell(request, probe.getRef());
        
        //First time, the program is changed from empty to the program
        ProgramStored reply = probe.expectMsgClass(ProgramStored.class);
        Assert.assertEquals("request1", reply.getRequestId());
        Assert.assertEquals(true, reply.hasProgramChanged());
        
        request = new StoreProgram("request2", program);
        cinema.tell(request, probe.getRef());
        
        //When sending the same program, it should not indicate change
        reply = probe.expectMsgClass(ProgramStored.class);
        Assert.assertEquals("request2", reply.getRequestId());
        Assert.assertEquals(false, reply.hasProgramChanged());
    }
    
    @Test
    public void testReadingProgram() {
        TestKit probe = new TestKit(system);
        ActorRef cinema = system.actorOf(Cinema.props("test"));
        String[] program = {"13.00-15.00: movie", "15.30-17.00: other movie"};
        StoreProgram storeProgram = new StoreProgram("storeRequest", program);
        
        //first set the program
        cinema.tell(storeProgram , probe.getRef());
        probe.expectMsgClass(ProgramStored.class);
        
        //then request for the program
        ReadProgram.ReadProgramRequest request = new ReadProgram.ReadProgramRequest("readRequest");
        cinema.tell(request, probe.getRef());
        
        ReadProgram.ReadProgramResponse response = probe.expectMsgClass(ReadProgram.ReadProgramResponse.class);
        
        Assert.assertEquals("readRequest", response.getRequestId());
        Assert.assertEquals(program.length, response.getProgram().length);
        Assert.assertEquals(program[0], response.getProgram()[0]);
    }
    
}
