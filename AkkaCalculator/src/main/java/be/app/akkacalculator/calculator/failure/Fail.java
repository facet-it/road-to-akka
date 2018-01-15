package be.app.akkacalculator.calculator.failure;

public class Fail {
    
    public static class Request {
        private final int failFactor;
        
        public Request(int failFactor) {
            this.failFactor = failFactor;
        }

        public int getFailFactor() {
            return failFactor;
        }
    }
    
    public static class Response {

        public String getMessage() {
            return "sorry, no failure this time";
        }
    }

}
