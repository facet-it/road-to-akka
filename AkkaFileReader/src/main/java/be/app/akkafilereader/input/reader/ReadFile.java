package be.app.akkafilereader.input.reader;

import java.nio.file.Path;

/**
 * ReadFile request and response messages. 
 * I have a feeling that most Akka messages come in pairs (request and response)
 * and therefor I like this way of coding them better. In the akka examples, 
 * response messages are coded inside the Actor itself as public static classes.
 * 
 * I think don't think this is clean and doing it this way feels a lot cleaner. 
 */
public class ReadFile {
    
    public static class Request {
        private final String requestId;
        private final Path filePath;
        
        public Request(String requestId, Path filePath) {
            this.requestId = requestId;
            this.filePath = filePath;
        }
        
        public String getRequestId() {
            return this.requestId;
        }
        
        // I can safely do this, as Path implementations by Java are 
        // immutable
        public Path getFilePath() {
            return this.filePath;
        }
    }
    
    public static class Response{
        private final String requestId;
        
        public Response(String requestId) {
            this.requestId = requestId;
        }
        
        public String getRequestId() {
            return this.requestId;
        }
    }

}
