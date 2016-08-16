package org.classy.code;

public class NullaryInstruction extends Instruction {

    public NullaryInstruction(int opcode) {
        super(opcode);
    }

    protected boolean isValid(int opcode) {
        return opcode < BIPUSH
                || (opcode > ALOAD && opcode < ISTORE)
                || (opcode > ASTORE && opcode < IINC)
                || (opcode > IINC && opcode < IFEQ)
                || (opcode > LOOKUPSWITCH && opcode < GETSTATIC)
                || (opcode > ANEWARRAY && opcode < CHECKCAST)
                || (opcode > INSTANCEOF && opcode < WIDE);
    }
}