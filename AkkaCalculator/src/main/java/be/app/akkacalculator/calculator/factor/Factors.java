package be.app.akkacalculator.calculator.factor;

import java.util.Collections;
import java.util.List;

public class Factors {
    
    public static class Request{
        private final String requestId;
        private final int base;
        
        public Request(String requestId, int base) {
            this.base = base;
            this.requestId = requestId;
        }

        public String getRequestId() {
            return requestId;
        }

        public int getBase() {
            return base;
        }
    }
    
    public static class Response{
        private final String requestId;
        private final List<Integer> result;
        
        public Response(String requestId, List<Integer> result) {
            this.requestId = requestId;
            this.result = result;
        }

        public String getRequestId() {
            return requestId;
        }

        public List<Integer> getResult() {
            return Collections.unmodifiableList(result);
        }
    }

}
