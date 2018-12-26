package classy.instructions;

public class VariableInstruction extends Instruction {

    public int index;

    public VariableInstruction(int opcode, int index) {
        super(opcode);
        this.index = index;
    }

    @Override
    public Type getType() {
        return Type.VARIABLE;
    }
}