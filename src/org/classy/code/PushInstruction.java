package org.classy.code;

public class PushInstruction extends Instruction {

    public int value;

    public PushInstruction(int opcode, int value) {
        super(opcode);
        this.value = value;
    }

    @Override
    public InstructionType getType() {
        return InstructionType.PUSH;
    }
}