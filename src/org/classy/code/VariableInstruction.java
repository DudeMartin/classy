package org.classy.code;

public class VariableInstruction extends Instruction {

    public int index;

    public VariableInstruction(int opcode, int index) {
        super(opcode);
        this.index = index;
    }

    protected boolean isValid(int opcode) {
        return (opcode >= ILOAD && opcode <= ALOAD) || (opcode >= ISTORE && opcode <= ASTORE);
    }
}