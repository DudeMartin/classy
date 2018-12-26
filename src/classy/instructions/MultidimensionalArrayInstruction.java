package classy.instructions;

public class MultidimensionalArrayInstruction extends TypeInstruction {

    public int dimensions;

    public MultidimensionalArrayInstruction(String type, int dimensions) {
        super(MULTIANEWARRAY, type);
        this.dimensions = dimensions;
    }

    @Override
    public Type getType() {
        return Type.MULTIDIMENSIONAL_ARRAY;
    }
}