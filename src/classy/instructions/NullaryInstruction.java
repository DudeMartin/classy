package classy.instructions;

public class NullaryInstruction extends Instruction {

    public NullaryInstruction(int opcode) {
        super(opcode);
    }

    @Override
    public Type getType() {
        return Type.NULLARY;
    }
}