package be.app.akkafilereader.input.cinema;

import java.util.Arrays;

public class StoreProgram {
    private final String[] program;
    private final String requestId;
    
    public StoreProgram(String requestId, String[] program) {
        this.requestId = requestId;
        this.program = program;
    }
    
    public String[] getProgram() {
        String[] programCopy = Arrays.copyOf(program, program.length);
        return programCopy;
    }
    
    public String getRequestId() {
        return this.requestId;
    }
}
