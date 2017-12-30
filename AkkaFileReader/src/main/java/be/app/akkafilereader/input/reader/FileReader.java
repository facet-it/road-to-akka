package be.app.akkafilereader.input.reader;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import be.app.akkafilereader.input.cinema.Cinema;
import be.app.akkafilereader.input.cinema.StoreProgram;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileReader extends AbstractActor{
    
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    private final String name;
    private ActorRef cinema;
    
    public FileReader(String name) {
        this.name = name;
    }
    
    public static Props props(String name) {
        return Props.create(FileReader.class, name);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().match(ReadFile.Request.class, this::readFile)
                               .build();
    }
    
    private void readFile(ReadFile.Request request) {
        Path filepath = request.getFilePath();
        
        try (InputStream input = Files.newInputStream(filepath);
            BufferedReader reader = new BufferedReader(new InputStreamReader(input))) {
            
            List<String> program = new ArrayList<>();
            
            String line = null;
            while((line = reader.readLine()) != null) {
                program.add(line);
            }
            
            StoreProgram storeProgram = new StoreProgram(this.name + System.currentTimeMillis(), program.toArray(new String[program.size()]));
            cinema.tell(storeProgram, getSelf());
        }
        catch(IOException ioe) {
                log.error("Exception while reading file in {}. Cause is {}", name, ioe.getMessage());
        }
    }

    @Override
    public void postStop() throws Exception {
        log.info("Stopped reader with name {}", name);
    }

    @Override
    public void preStart() throws Exception {
        log.info("Started reader with name {}", name);
        this.cinema = getContext().actorOf(Cinema.props(this.name));
        log.info("Created cinama with name {}", this.name);
    }
    
    

}
