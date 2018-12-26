package classy.instructions;

public class TypeInstruction extends Instruction {

    /*
     * Primitive array types.
     */
    public static final int T_BOOLEAN = 4;
    public static final int T_CHAR = 5;
    public static final int T_FLOAT = 6;
    public static final int T_DOUBLE = 7;
    public static final int T_BYTE = 8;
    public static final int T_SHORT = 9;
    public static final int T_INT = 10;
    public static final int T_LONG = 11;

    public String type;

    public TypeInstruction(int opcode, String type) {
        super(opcode);
        this.type = type;
    }

    @Override
    public Type getType() {
        return Type.TYPE;
    }
}