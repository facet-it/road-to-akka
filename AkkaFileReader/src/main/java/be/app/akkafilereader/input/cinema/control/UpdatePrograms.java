package be.app.akkafilereader.input.cinema.control;

public class UpdatePrograms {
    
    public static class Request {
        private final String id;
        
        public Request(String id) {
            this.id = id;
        }
        
        public String getId() {
            return this.id;
        }
    }
}
