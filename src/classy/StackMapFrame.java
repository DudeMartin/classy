package classy;

import classy.instructions.Instruction;

public class StackMapFrame {

    /*
     * Verification type information tags.
     */
    public static final int ITEM_Top = 0;
    public static final int ITEM_Integer = 1;
    public static final int ITEM_Float = 2;
    public static final int ITEM_Double = 3;
    public static final int ITEM_Long = 4;
    public static final int ITEM_Null = 5;
    public static final int ITEM_UninitializedThis = 6;
    public static final int ITEM_Object = 7;
    public static final int ITEM_Uninitialized = 8;

    /*
     * Stack frame types.
     */
    public static final int SAME = 0;
    public static final int SAME_LOCALS_1_STACK_ITEM = 64;
    public static final int SAME_LOCALS_1_STACK_ITEM_EXTENDED = 247;
    public static final int CHOP = 248;
    public static final int SAME_FRAME_EXTENDED = 251;
    public static final int APPEND = 252;
    public static final int FULL_FRAME = 255;

    public static final Object[] EMPTY = new Object[0];
    public int type;
    public Instruction start;
    public Object[] locals;
    public Object[] stack;
}