package org.classy.code;

public class VariableInstruction extends Instruction {

    public int index;

    public VariableInstruction(int opcode, int index) {
        super(opcode);
        this.index = index;
    }

    @Override
    public InstructionType getType() {
        return InstructionType.VARIABLE;
    }
}