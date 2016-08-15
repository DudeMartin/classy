package org.classy.code;

public class NullaryInstruction extends Instruction {

    public NullaryInstruction(int opcode) {
        super(opcode);
    }

    protected boolean isValid(int opcode) {
        return opcode < Instruction.BIPUSH
                || (opcode > Instruction.ALOAD && opcode < Instruction.ISTORE)
                || (opcode > Instruction.ASTORE && opcode < Instruction.IINC)
                || (opcode > Instruction.IINC && opcode < Instruction.IFEQ)
                || (opcode > Instruction.LOOKUPSWITCH && opcode < Instruction.GETSTATIC)
                || (opcode > Instruction.ANEWARRAY && opcode < Instruction.CHECKCAST)
                || (opcode > Instruction.INSTANCEOF && opcode < Instruction.WIDE);
    }
}