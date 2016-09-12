package org.classy.instructions;

public class MultidimensionalArrayInstruction extends TypeInstruction {

    public int dimensions;

    public MultidimensionalArrayInstruction(String type, int dimensions) {
        super(MULTIANEWARRAY, type);
        this.dimensions = dimensions;
    }

    @Override
    public InstructionType getType() {
        return InstructionType.MULTIDIMENSIONAL_ARRAY;
    }
}