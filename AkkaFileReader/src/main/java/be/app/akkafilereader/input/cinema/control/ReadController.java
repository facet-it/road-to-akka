package be.app.akkafilereader.input.cinema.control;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import be.app.akkafilereader.input.reader.FileReader;
import be.app.akkafilereader.input.reader.ReadFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class ReadController extends AbstractActor {
    private static final String identifier = "supervisor";
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    private final Path mainDirectory;
    
    private Map<String, ActorRef> readers = new HashMap<>();
    private Map<ActorRef, String> lastKnownFailures = new HashMap<>();
    
    Consumer<Path> handlePaths = path -> {
        String fileName = path.getFileName().toString(); 
        ActorRef reader = readers.get(fileName);
        if(reader != null) {
            reader.tell(new ReadFile.Request(fileName + System.currentTimeMillis(), path), getSelf());
        }
        else {
            reader = getContext().actorOf(FileReader.props(fileName), fileName);
            readers.put(fileName, reader);
            reader.tell(new ReadFile.Request(fileName + System.currentTimeMillis(), path), getSelf());
        }
    };
    
    public ReadController(Path mainDirectory) {
        this.mainDirectory = mainDirectory;
    }
    
    public static Props props(Path mainDirectory) {
        return Props.create(ReadController.class, mainDirectory);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().match(UpdatePrograms.Request.class, this::update)
                               .build();
    }
    
    private void update(UpdatePrograms.Request request) {
        //Files.walk :O never seen this before but looks cool! Carefull though, 
        //it seems to be working recursively
        try (Stream<Path> paths = Files.walk(mainDirectory)) {
            paths
            .filter(Files::isRegularFile)
            .forEach(handlePaths);
        }
        catch(IOException ioe) {
            log.error("Exception thrown in {}, reason: {}", identifier, ioe.getMessage());
        }
    }

    @Override
    public void postStop() throws Exception {
        log.info("{} has stopped", identifier);
    }

    @Override
    public void preStart() throws Exception {
        log.info("{} has started", identifier);
    }
}
