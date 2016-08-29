package org.classy.code;

public class IncrementInstruction extends VariableInstruction {

    public int delta;

    public IncrementInstruction(int opcode, int index, int delta) {
        super(opcode, index);
        this.delta = delta;
    }

    @Override
    public InstructionType getType() {
        return InstructionType.INCREMENT;
    }
}