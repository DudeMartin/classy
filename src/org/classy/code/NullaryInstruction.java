package org.classy.code;

public class NullaryInstruction extends Instruction {

    public NullaryInstruction(int opcode) {
        super(opcode);
    }

    @Override
    public InstructionType getType() {
        return InstructionType.NULLARY;
    }
}