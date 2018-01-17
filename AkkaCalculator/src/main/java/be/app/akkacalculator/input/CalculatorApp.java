package be.app.akkacalculator.input;

import be.app.akkacalculator.calculator.Calculus;
import java.util.Scanner;

public class CalculatorApp {
    
    public static void main(String[] args) {
        Calculus calculus = new Calculus();
        CommandFactory factory = new CommandFactory(calculus);
        
        System.out.println("Please use the following commands: ");
        System.out.println("do power of <base>,<power>");
        System.out.println("do factors of <base>");
        System.out.println("do exit");
        
        Scanner scanInput = new Scanner(System.in);
        String input = scanInput.nextLine();
        
        while(! input.equals(CommandFactory.EXIT)) {
            Command command = factory.parseCommandFrom(input);
            if(command != null) {
                command.doCommand();
            }
            
            input = scanInput.nextLine();
        }
        
        System.exit(0);
        
    }

}
