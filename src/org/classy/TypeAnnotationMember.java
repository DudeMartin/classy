package org.classy;

public class TypeAnnotationMember extends AnnotationMember {

    /*
     * Type annotation targets.
     */
    public static final int CLASS_PARAMETER_GENERIC                = 0x00;
    public static final int METHOD_PARAMETER_GENERIC               = 0x01;
    public static final int CLASS_SUPERTYPE                        = 0x10;
    public static final int CLASS_PARAMETER_GENERIC_BOUND          = 0x11;
    public static final int METHOD_PARAMETER_GENERIC_BOUND         = 0x12;
    public static final int FIELD_DECLARATION                      = 0x13;
    public static final int METHOD_RETURN                          = 0x14;
    public static final int METHOD_RECEIVER                        = 0x15;
    public static final int METHOD_FORMAL_PARAMETER                = 0x16;
    public static final int THROWS                                 = 0x17;
    public static final int LOCAL_VARIABLE                         = 0x40;
    public static final int RESOURCE_VARIABLE                      = 0x41;
    public static final int EXCEPTION_PARAMETER                    = 0x42;
    public static final int INSTANCEOF_EXPRESSION                  = 0x43;
    public static final int NEW_EXPRESSION                         = 0x44;
    public static final int CONSTRUCTOR_REFERENCE_EXPRESSION       = 0x45;
    public static final int METHOD_REFERENCE_EXPRESSION            = 0x46;
    public static final int CAST_EXPRESSION                        = 0x47;
    public static final int CONSTRUCTOR_ARGUMENT_GENERIC           = 0x48;
    public static final int METHOD_ARGUMENT_GENERIC                = 0x49;
    public static final int CONSTRUCTOR_REFERENCE_ARGUMENT_GENERIC = 0x4A;
    public static final int METHOD_REFERENCE_ARGUMENT_GENERIC      = 0x4B;

    /*
     * Type paths.
     */
    public static final int ARRAY_TYPE    = 0;
    public static final int NESTED_TYPE   = 1;
    public static final int WILDCARD_TYPE = 2;
    public static final int TYPE_ARGUMENT = 3;

    public int type;
    public TypeInformation information;
    public int[][] path;

    public TypeAnnotationMember() {

    }

    TypeAnnotationMember(PoolItem[] constantPool, Buffer data) {
        type = data.getUnsignedByte();
        if (type != FIELD_DECLARATION
                && type != METHOD_RETURN
                && type != METHOD_RECEIVER) {
            information = new TypeInformation(data, type);
        }
        int pathLength = data.getUnsignedByte();
        path = new int[pathLength][2];
        for (int i = 0; i < pathLength; i++) {
            path[i][0] = data.getUnsignedByte();
            path[i][1] = data.getUnsignedByte();
        }
        readAnnotation(constantPool, data);
    }

    public static final class TypeInformation {

        public final int index;
        public final int secondIndex;
        public final int[][] localVariableTable;

        public TypeInformation(int index, int secondIndex, int[][] localVariableTable) {
            this.index = index;
            this.secondIndex = secondIndex;
            this.localVariableTable = localVariableTable;
        }

        private TypeInformation(Buffer data, int type) {
            int index = 0;
            int secondIndex = 0;
            int[][] localVariableTable = null;
            switch (type) {
                case CLASS_PARAMETER_GENERIC:
                case METHOD_PARAMETER_GENERIC:
                case METHOD_FORMAL_PARAMETER:
                    index = data.getUnsignedByte();
                    break;
                case CLASS_SUPERTYPE:
                case THROWS:
                case EXCEPTION_PARAMETER:
                case INSTANCEOF_EXPRESSION:
                case NEW_EXPRESSION:
                case CONSTRUCTOR_REFERENCE_EXPRESSION:
                case METHOD_REFERENCE_EXPRESSION:
                    index = data.getUnsignedShort();
                    break;
                case CLASS_PARAMETER_GENERIC_BOUND:
                case METHOD_PARAMETER_GENERIC_BOUND:
                    index = data.getUnsignedByte();
                    secondIndex = data.getUnsignedByte();
                    break;
                case LOCAL_VARIABLE:
                case RESOURCE_VARIABLE:
                    int length = data.getUnsignedShort();
                    localVariableTable = new int[length][3];
                    for (int i = 0; i < length; i++) {
                        localVariableTable[i][0] = data.getUnsignedShort();
                        localVariableTable[i][1] = data.getUnsignedShort();
                        localVariableTable[i][2] = data.getUnsignedShort();
                    }
                    break;
                case CAST_EXPRESSION:
                case CONSTRUCTOR_ARGUMENT_GENERIC:
                case METHOD_ARGUMENT_GENERIC:
                case CONSTRUCTOR_REFERENCE_ARGUMENT_GENERIC:
                case METHOD_REFERENCE_ARGUMENT_GENERIC:
                    index = data.getUnsignedShort();
                    secondIndex = data.getUnsignedByte();
                    break;
            }
            this.index = index;
            this.secondIndex = secondIndex;
            this.localVariableTable = localVariableTable;
        }
    }
}