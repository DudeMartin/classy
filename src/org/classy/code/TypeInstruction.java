package org.classy.code;

public class TypeInstruction extends Instruction {

    public String descriptor;

    public TypeInstruction(int opcode, String descriptor) {
        super(opcode);
        this.descriptor = descriptor;
    }

    protected boolean isValid(int opcode) {
        return (opcode >= NEW && opcode <= ANEWARRAY)
                || opcode == CHECKCAST
                || opcode == INSTANCEOF;
    }
}