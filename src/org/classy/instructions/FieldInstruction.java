package org.classy.instructions;

import org.classy.Reference;

public class FieldInstruction extends Instruction {

    public Reference field;

    public FieldInstruction(int opcode, Reference field) {
        super(opcode);
        this.field = field;
    }

    @Override
    public InstructionType getType() {
        return InstructionType.FIELD;
    }
}