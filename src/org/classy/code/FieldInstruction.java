package org.classy.code;

public class FieldInstruction extends Instruction {

    public String owner;
    public String name;
    public String descriptor;

    public FieldInstruction(int opcode, String owner, String name, String descriptor) {
        super(opcode);
        this.owner = owner;
        this.name = name;
        this.descriptor = descriptor;
    }

    protected boolean isValid(int opcode) {
        return opcode >= GETSTATIC && opcode <= PUTFIELD;
    }
}