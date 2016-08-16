package org.classy.code;

public class ConstantPushInstruction extends Instruction {

    public Object constant;

    public ConstantPushInstruction(int opcode, Object constant) {
        super(opcode);
        this.constant = constant;
    }

    protected boolean isValid(int opcode) {
        return opcode >= LDC && opcode <= LDC2_W;
    }
}