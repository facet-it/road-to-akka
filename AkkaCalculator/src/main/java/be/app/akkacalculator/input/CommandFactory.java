package be.app.akkacalculator.input;

import be.app.akkacalculator.calculator.Calculus;
import java.util.List;
import java.util.stream.Collectors;
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
        int[] params = parseParameters(inputCommands[1]);
        
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
    
    private int[] parseParameters(String parameters) {
        String[] separatedParams = parameters.split(",");
        int[] params = new int[separatedParams.length];
        
        for(int i = 0; i < separatedParams.length; i++) {
            if(Character.isDigit(separatedParams[i].charAt(0))) {
                params[i] = Integer.parseInt(separatedParams[i]);
            }
        }
        
        return params;
    }

}
