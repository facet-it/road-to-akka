/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.app.akkafilereader.input.cinema.control;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.testkit.javadsl.TestKit;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Beheerder
 */
public class ReadControllerTest {
    
    static ActorSystem system;
    private TestKit probe;
    private ActorRef readController;
    
    @BeforeClass
    public static void setup() {
        system = ActorSystem.create();
    }

    @AfterClass
    public static void teardown() {
        TestKit.shutdownActorSystem(system);
        system = null;
    }
    
    @Before
    public void setUp() {
        probe = new TestKit(system);
        readController = system.actorOf(ReadController.props(Paths.get("src", "test", "resources")), "Supervisor");
    }
    
    @Test
    public void updatePrograms() {
        UpdatePrograms.Request request = new UpdatePrograms.Request("test");
        readController.tell(request, probe.getRef());
        
        UpdatePrograms.Response response = probe.expectMsgClass(UpdatePrograms.Response.class);
        Assert.assertEquals("test", response.getId());
    }
}
