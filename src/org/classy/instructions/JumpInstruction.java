package org.classy.instructions;

public class JumpInstruction extends Instruction {

    public Instruction target;

    public JumpInstruction(int opcode, Instruction target) {
        super(opcode);
        this.target = target;
    }

    @Override
    public InstructionType getType() {
        return InstructionType.JUMP;
    }
}