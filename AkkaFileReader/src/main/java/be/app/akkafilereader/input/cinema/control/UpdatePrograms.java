package be.app.akkafilereader.input.cinema.control;

import be.app.akkafilereader.input.GenericAkkeMessage;

public class UpdatePrograms {
    
    public static class Request extends GenericAkkeMessage{    
        public Request(String id) {
            super(id);
        }
    }
    
    public static class Response extends GenericAkkeMessage{   
        public Response(String id) {
            super(id);
        }
    }
    
    public static class Failure extends GenericAkkeMessage{ 
        private final String errorMessage;
        
        public Failure(String id, String errorMessage) {
            super(id);
            this.errorMessage = errorMessage;
        }
        
        public String getErrorMessage() {
            return errorMessage;
        }
    }
}
