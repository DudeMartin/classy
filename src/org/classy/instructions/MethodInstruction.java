package org.classy.instructions;

import org.classy.SymbolicReference;

public class MethodInstruction extends Instruction {

    public SymbolicReference method;

    public MethodInstruction(int opcode, SymbolicReference method) {
        super(opcode);
        this.method = method;
    }

    @Override
    public InstructionType getType() {
        return InstructionType.METHOD;
    }
}