package org.classy;

public final class PoolItem {

    /*
     * Constant pool tags.
     */
    public static final int CONSTANT_Utf8 = 1;
    public static final int CONSTANT_Integer = 3;
    public static final int CONSTANT_Float = 4;
    public static final int CONSTANT_Long = 5;
    public static final int CONSTANT_Double = 6;
    public static final int CONSTANT_Class = 7;
    public static final int CONSTANT_String = 8;
    public static final int CONSTANT_Fieldref = 9;
    public static final int CONSTANT_Methodref = 10;
    public static final int CONSTANT_InterfaceMethodref = 11;
    public static final int CONSTANT_NameAndType = 12;
    public static final int CONSTANT_MethodHandle = 15;
    public static final int CONSTANT_MethodType = 16;
    public static final int CONSTANT_InvokeDynamic = 18;

    /*
     * Method handle kinds.
     */
    public static final int REF_getField = 1;
    public static final int REF_getStatic = 2;
    public static final int REF_putField = 3;
    public static final int REF_putStatic = 4;
    public static final int REF_invokeVirtual = 5;
    public static final int REF_invokeStatic = 6;
    public static final int REF_invokeSpecial = 7;
    public static final int REF_newInvokeSpecial = 8;
    public static final int REF_invokeInterface = 9;

    public final int tag;
    public final int value;
    public final long longValue;
    public final double doubleValue;
    public final String stringValue;

    PoolItem(Buffer data) {
        tag = data.getUnsignedByte();
        int value = 0;
        long longValue = 0;
        double doubleValue = 0;
        String stringValue = null;
        switch (tag) {
            case CONSTANT_Class:
            case CONSTANT_String:
            case CONSTANT_MethodType:
                value = data.getUnsignedShort();
                break;
            case CONSTANT_Fieldref:
            case CONSTANT_Methodref:
            case CONSTANT_InterfaceMethodref:
            case CONSTANT_NameAndType:
            case CONSTANT_InvokeDynamic:
                value = data.getUnsignedShort();
                longValue = data.getUnsignedShort();
                break;
            case CONSTANT_Integer:
                value = data.getInteger();
                break;
            case CONSTANT_Float:
                doubleValue = Float.intBitsToFloat(data.getInteger());
                break;
            case CONSTANT_Long: {
                long highBytes = data.getInteger();
                long lowBytes = data.getInteger() & 0xFFFFFFFFL;
                longValue = (highBytes << 32) | lowBytes;
                break;
            }
            case CONSTANT_Double:
                long highBytes = data.getInteger();
                long lowBytes = data.getInteger() & 0xFFFFFFFFL;
                doubleValue = Double.longBitsToDouble((highBytes << 32) | lowBytes);
                break;
            case CONSTANT_Utf8:
                stringValue = data.getString(data.getUnsignedShort());
                break;
            case CONSTANT_MethodHandle:
                value = data.getUnsignedByte();
                longValue = data.getUnsignedShort();
                break;
            default:
                throw new RuntimeException(tag + " is not a valid or supported tag.");
        }
        this.value = value;
        this.longValue = longValue;
        this.doubleValue = doubleValue;
        this.stringValue = stringValue;
    }
}