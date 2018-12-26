package classy.instructions;

public class JumpInstruction extends Instruction {

    public Instruction target;

    public JumpInstruction(int opcode, Instruction target) {
        super(opcode);
        this.target = target;
    }

    public boolean isConditional() {
        int opcode = getOpcode();
        return opcode != Instruction.GOTO
                && opcode != Instruction.JSR
                && opcode != Instruction.GOTO_W
                && opcode != Instruction.JSR_W;
    }

    @Override
    public Type getType() {
        return Type.JUMP;
    }
}