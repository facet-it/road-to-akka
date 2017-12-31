package be.app.akkafilereader.input;

/*
    These type of messages between actors seem to be a returning thing, so 
    lets make a parent class out of it
*/
public class GenericAkkeMessage {
    
    private final String id;
    
    public GenericAkkeMessage(String id) {
        this.id = id;
    }
    
    public String getId() {
        return this.id;
    }

}
