package org.classy.instructions;

import org.classy.Reference;

public class MethodInstruction extends Instruction {

    public Reference method;

    public MethodInstruction(int opcode, Reference method) {
        super(opcode);
        this.method = method;
    }

    @Override
    public InstructionType getType() {
        return InstructionType.METHOD;
    }
}