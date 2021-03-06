package be.app.akkacalculator.input;

import be.app.akkacalculator.calculator.Calculus;

public class CalculatePowerCommand implements Command{
    
    private final Calculus calculus;
    private final int base;
    private final int power;
    
    public CalculatePowerCommand(Calculus calculus, int base, int power) {
        this.calculus = calculus;
        this.base = base;
        this.power = power;
    }

    @Override
    public void doCommand() {
        calculus.calculatePowerOf(base, power);
    }

}
