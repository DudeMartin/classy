package org.classy.code;

public class ConstantPushInstruction extends Instruction {

    public Object constant;

    public ConstantPushInstruction(int opcode, Object constant) {
        super(opcode);
        this.constant = constant;
    }

    @Override
    public InstructionType getType() {
        return InstructionType.CONSTANT_PUSH;
    }
}