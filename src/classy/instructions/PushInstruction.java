package classy.instructions;

public class PushInstruction extends Instruction {

    public int value;

    public PushInstruction(int opcode, int value) {
        super(opcode);
        this.value = value;
    }

    @Override
    public Type getType() {
        return Type.PUSH;
    }
}