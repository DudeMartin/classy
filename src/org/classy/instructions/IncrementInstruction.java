package org.classy.instructions;

public class IncrementInstruction extends VariableInstruction {

    public int delta;

    public IncrementInstruction(int index, int delta) {
        super(IINC, index);
        this.delta = delta;
    }

    @Override
    public InstructionType getType() {
        return InstructionType.INCREMENT;
    }
}