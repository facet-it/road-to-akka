package be.app.akkacalculator.calculator.power;

public class CalculatePower {
    
    public static class Request{
        private final String requestId;
        private final int base;
        private final int power;
        
        public Request(String requestId, int base, int power) {
            this.requestId = requestId;
            this.base = base;
            this.power = power;
        }

        public String getRequestId() {
            return requestId;
        }

        public int getBase() {
            return base;
        }

        public int getPower() {
            return power;
        }
    }
    
    public static class Response{
        private final String requestId;
        private final int result;
        
        public Response(String requestId, int result) {
            this.requestId = requestId;
            this.result = result;
        }

        public String getRequestId() {
            return requestId;
        }

        public int getResult() {
            return result;
        }
    }

}
