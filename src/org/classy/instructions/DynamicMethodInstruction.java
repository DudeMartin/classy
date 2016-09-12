package org.classy.instructions;

import org.classy.Reference;

public class DynamicMethodInstruction extends MethodInstruction {

    public DynamicMethodInstruction(Reference method) {
        super(INVOKEDYNAMIC, method);
    }

    @Override
    public InstructionType getType() {
        return InstructionType.DYNAMIC_METHOD;
    }
}