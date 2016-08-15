package org.classy.code;

public class VariableInstruction extends Instruction {

    public VariableInstruction(int opcode) {
        super(opcode);
    }

    protected boolean isValid(int opcode) {
        return false;
    }
}