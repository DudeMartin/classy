package org.classy.instructions;

import org.classy.SymbolicReference;

public class FieldInstruction extends Instruction {

    public SymbolicReference field;

    public FieldInstruction(int opcode, SymbolicReference field) {
        super(opcode);
        this.field = field;
    }

    @Override
    public Type getType() {
        return Type.FIELD;
    }
}