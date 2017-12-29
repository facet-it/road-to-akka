package be.app.akkafilereader.input.cinema;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import java.util.Arrays;
import java.util.stream.Stream;

public class Cinema extends AbstractActor{
    
    private final String name;
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    private int hashedProgram;
    private String[] program = {};
    
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
                               .match(ReadProgram.ReadProgramRequest.class, this::readProgram)
                               .build();
    }
    
    private void setProgram(StoreProgram storeMessage) {
        int newHash = Stream.of(storeMessage.getProgram()).reduce("", String::concat).hashCode();
        if(newHash != this.hashedProgram) {
            this.hashedProgram = newHash;
            this.program = storeMessage.getProgram();
            getSender().tell(new ProgramStored(storeMessage.getRequestId(), true), getSelf());
        }
        else{
            getSender().tell(new ProgramStored(storeMessage.getRequestId(), false), getSelf());
        }
        
        log.info("{} has succesfully stored the program", getSelf());
    }
    
    private void readProgram(ReadProgram.ReadProgramRequest request) {
        String[] programCopy = Arrays.copyOf(this.program, program.length);
        ReadProgram.ReadProgramResponse response = new ReadProgram.ReadProgramResponse(request.getRequestId(), programCopy);
        
        getSender().tell(response, getSelf());
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
