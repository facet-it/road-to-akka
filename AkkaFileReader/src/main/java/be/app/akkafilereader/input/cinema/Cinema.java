package be.app.akkafilereader.input.cinema;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import java.util.stream.Stream;

public class Cinema extends AbstractActor{
    
    private final String name;
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    private int hashedProgram;
    private String[] program = null;
    
    public Cinema(String name) {
        this.name = name;
    }
    
    //Don't know yet why exactly this is so important, but it seems to be used
    // to create and partially identify an actor
    public static Props props(String name){
        return Props.create(Cinema.class, name);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().match(StoreProgram.class, this::setProgram)
                               .build();
    }
    
    private void setProgram(StoreProgram storeMessage) {
        this.program = storeMessage.getProgram();
        int newHash = Stream.of(program).reduce("", String::concat).hashCode();
        if(newHash != this.hashedProgram) {
            getSender().tell(ProgramHasChanged.class, getSelf());
            this.hashedProgram = newHash;
        }
    }

    @Override
    public void postStop() throws Exception {
        log.info("Stopped actor with reference {}", this.name);
    }

    @Override
    public void preStart() throws Exception {
        log.info("Started actor with reference {}", this.name);
    }
}
