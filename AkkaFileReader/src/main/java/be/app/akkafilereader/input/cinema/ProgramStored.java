package be.app.akkafilereader.input.cinema;

public class ProgramStored {
    
    private final String requestId;
    private final boolean hasProgramChanged;
    
    public ProgramStored(String requestId, boolean hasProgramChanged) {
        this.requestId = requestId;
        this.hasProgramChanged = hasProgramChanged;
    }
    
    public String getRequestId(){
        return this.requestId;
    }
    
    public boolean hasProgramChanged() {
        return hasProgramChanged;
    }
}
