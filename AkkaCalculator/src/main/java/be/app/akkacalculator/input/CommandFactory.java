package be.app.akkacalculator.input;

import be.app.akkacalculator.calculator.Calculus;
import java.util.stream.Stream;

public class CommandFactory {
    
    public static final String EXIT = "exit";
    private final Calculus calculus;
    
    public CommandFactory(Calculus calculus) {
        this.calculus = calculus;
    }
    
    //also a bit shady. This needs to change
    public Command parseCommandFrom(String input) {
        String[] inputCommands = input.split("of");
        Integer[] params = parseParameters(inputCommands[1]);
        
        switch(inputCommands[0].trim()) {
            case "do power": 
                return new CalculatePowerCommand(calculus, params[0], params[1]);
            case "do factors": 
                return new CalculateFactorsCommand(calculus, params[0]);
            default: 
                System.out.println("Unknown command, please try again");
                return null;
        }
    }
    
    private Integer[] parseParameters(String parameters) {
        String[] separatedParams = parameters.split(",");
        int[] params = new int[separatedParams.length];
        
        //nailed it! :-)
        Integer[] result = Stream.of(separatedParams).map(String::trim)
                                                     .filter(item -> Character.isDigit(item.charAt(0)))
                                                     .map(Integer::parseInt)
                                                     .toArray(Integer[]::new);
        
        return result;
    }

}
