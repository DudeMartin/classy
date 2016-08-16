package org.classy.code;

public class PushInstruction extends Instruction {

    public int value;

    public PushInstruction(int opcode, int value) {
        super(opcode);
        this.value = value;
    }

    protected boolean isValid(int opcode) {
        return opcode == BIPUSH || opcode == SIPUSH;
    }
}