package org.classy.instructions;

public class ConstantPushInstruction extends Instruction {

    public Object constant;

    public ConstantPushInstruction(int opcode, Object constant) {
        super(opcode);
        this.constant = constant;
    }

    @Override
    public Type getType() {
        return Type.CONSTANT_PUSH;
    }
}