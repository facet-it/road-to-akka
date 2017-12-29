package be.app.akkafilereader.input.cinema;

public class ReadProgram {
    
    public static class ReadProgramRequest {
        private final String requestId;
        
        public ReadProgramRequest(String requestId) {
            this.requestId = requestId;
        }
        
        public String getRequestId() {
            return this.requestId;
        }
    }
    
    public static class ReadProgramResponse{
        private final String requestId;
        private final String[] program;
        
        public ReadProgramResponse(String requestId, String[] program) {
            this.requestId = requestId;
            this.program = program;
        }
        
        public String getRequestId() {
            return this.requestId;
        }
        
        public String[] getProgram() {
            return this.program;
        }
    }

}
