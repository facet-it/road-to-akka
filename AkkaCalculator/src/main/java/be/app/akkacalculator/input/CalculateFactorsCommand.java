package be.app.akkacalculator.input;

import be.app.akkacalculator.calculator.Calculus;

public class CalculateFactorsCommand implements Command{
    
    private final Calculus calculus;
    private final int base;
    
    public CalculateFactorsCommand(Calculus calculus, int base) {
        this.calculus = calculus;
        this.base = base;
    }

    @Override
    public void doCommand() {
        calculus.calculateFactorsOf(base);
    }

}
