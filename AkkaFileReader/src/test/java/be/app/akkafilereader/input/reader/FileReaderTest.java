/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package be.app.akkafilereader.input.reader;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.testkit.javadsl.TestKit;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Beheerder
 */
public class FileReaderTest {
    
    static ActorSystem system;
    private TestKit probe;
    private ActorRef fileReader;
    
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
        fileReader = system.actorOf(FileReader.props("test"));
    }

    @Test
    public void testFileReadWithExistingFile() throws Exception {
        Path path = Paths.get("src", "test", "resources", "KinepolisBrugge.txt"); 
        ReadFile.Request request = new ReadFile.Request("test1", path);
        
        fileReader.tell(request, probe.getRef());
        ReadFile.Response response = probe.expectMsgClass(ReadFile.Response.class);
        
        Assert.assertEquals("test1", response.getRequestId());
    }
    
    @Test
    public void getFailureWhenExceptionIsThrown() throws Exception {
        Path path = Paths.get("non-existing-file.txt"); 
        ReadFile.Request request = new ReadFile.Request("test1", path);
        
        fileReader.tell(request, probe.getRef());
        ReadFile.Failure response = probe.expectMsgClass(ReadFile.Failure.class);
        
        Assert.assertEquals("test1", response.getRequestId());
    }
    
}
