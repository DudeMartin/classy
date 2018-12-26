package classy.instructions;

import classy.SymbolicReference;

public class MethodInstruction extends Instruction {

    public SymbolicReference method;

    public MethodInstruction(int opcode, SymbolicReference method) {
        super(opcode);
        this.method = method;
    }

    @Override
    public Type getType() {
        return Type.METHOD;
    }
}