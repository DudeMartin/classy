package org.classy;

import org.classy.instructions.Instruction;

public class TypeAnnotationMember extends AnnotationMember {

    /*
     * Type annotation targets.
     */
    public static final int CLASS_PARAMETER_GENERIC = 0x00;
    public static final int METHOD_PARAMETER_GENERIC = 0x01;
    public static final int CLASS_SUPERTYPE = 0x10;
    public static final int CLASS_PARAMETER_GENERIC_BOUND = 0x11;
    public static final int METHOD_PARAMETER_GENERIC_BOUND = 0x12;
    public static final int FIELD_DECLARATION = 0x13;
    public static final int METHOD_RETURN = 0x14;
    public static final int METHOD_RECEIVER = 0x15;
    public static final int METHOD_FORMAL_PARAMETER = 0x16;
    public static final int THROWS = 0x17;
    public static final int LOCAL_VARIABLE = 0x40;
    public static final int RESOURCE_VARIABLE = 0x41;
    public static final int EXCEPTION_PARAMETER = 0x42;
    public static final int INSTANCEOF_EXPRESSION = 0x43;
    public static final int NEW_EXPRESSION = 0x44;
    public static final int CONSTRUCTOR_REFERENCE_EXPRESSION = 0x45;
    public static final int METHOD_REFERENCE_EXPRESSION = 0x46;
    public static final int CAST_EXPRESSION = 0x47;
    public static final int CONSTRUCTOR_ARGUMENT_GENERIC = 0x48;
    public static final int METHOD_ARGUMENT_GENERIC = 0x49;
    public static final int CONSTRUCTOR_REFERENCE_ARGUMENT_GENERIC = 0x4A;
    public static final int METHOD_REFERENCE_ARGUMENT_GENERIC = 0x4B;

    /*
     * Type paths.
     */
    public static final int ARRAY_TYPE = 0;
    public static final int NESTED_TYPE = 1;
    public static final int WILDCARD_TYPE = 2;
    public static final int TYPE_ARGUMENT = 3;

    public int type;
    public TypeInformation information;
    public int[][] path;

    public TypeAnnotationMember() {

    }

    TypeAnnotationMember(PoolItem[] constantPool,
                         Buffer data,
                         ClassFile classFile,
                         MethodMember method,
                         Instruction[] instructions) {
        type = data.getUnsignedByte();
        switch (type) {
            case FIELD_DECLARATION:
            case METHOD_RETURN:
            case METHOD_RECEIVER:
                break;
            default:
                information = new TypeInformation(data, classFile, method, instructions, type);
                break;
        }
        int length = data.getUnsignedByte();
        path = new int[length][2];
        for (int i = 0; i < length; i++) {
            path[i][0] = data.getUnsignedByte();
            path[i][1] = data.getUnsignedByte();
        }
        readAnnotation(constantPool, data);
    }

    @Generated
    public static class LocalVariableRange {

        public Instruction start;
        public Instruction end;
        public int index;

        public LocalVariableRange() {

        }

        private LocalVariableRange(Buffer data, Instruction[] instructions) {
            int startIndex = data.getUnsignedShort();
            start = instructions[startIndex];
            int endIndex = startIndex + data.getUnsignedShort();
            end = (endIndex >= instructions.length) ? null : instructions[endIndex];
            index = data.getUnsignedShort();
        }
    }

    public static class TypeInformation {

        public int value;
        public int secondValue;
        public String stringValue;
        public LocalVariableRange[] localVariableTable;
        public ExceptionHandler exceptionHandler;
        public Instruction instruction;

        public TypeInformation() {

        }

        private TypeInformation(Buffer data,
                                ClassFile classFile,
                                MethodMember method,
                                Instruction[] instructions,
                                int type) {
            int value = 0;
            int secondValue = 0;
            String stringValue = null;
            LocalVariableRange[] localVariableTable = null;
            ExceptionHandler exceptionHandler = null;
            Instruction instruction = null;
            switch (type) {
                case CLASS_PARAMETER_GENERIC:
                case METHOD_PARAMETER_GENERIC:
                case METHOD_FORMAL_PARAMETER:
                    value = data.getUnsignedByte();
                    break;
                case CLASS_SUPERTYPE:
                    int supertypeIndex = data.getUnsignedShort();
                    stringValue = (supertypeIndex == 65535) ? classFile.superclassName : classFile.interfaceNames.get(supertypeIndex);
                    break;
                case CLASS_PARAMETER_GENERIC_BOUND:
                case METHOD_PARAMETER_GENERIC_BOUND:
                    value = data.getUnsignedByte();
                    secondValue = data.getUnsignedByte();
                    break;
                case THROWS:
                    stringValue = method.exceptions.get(data.getUnsignedShort());
                    break;
                case LOCAL_VARIABLE:
                case RESOURCE_VARIABLE:
                    int count = data.getUnsignedShort();
                    localVariableTable = new LocalVariableRange[count];
                    for (int i = 0; i < count; i++) {
                        localVariableTable[i] = new LocalVariableRange(data, instructions);
                    }
                    break;
                case EXCEPTION_PARAMETER:
                    exceptionHandler = method.exceptionHandlers.get(data.getUnsignedShort());
                    break;
                case INSTANCEOF_EXPRESSION:
                case NEW_EXPRESSION:
                case CONSTRUCTOR_REFERENCE_EXPRESSION:
                case METHOD_REFERENCE_EXPRESSION:
                    instruction = instructions[data.getUnsignedShort()];
                    break;
                case CAST_EXPRESSION:
                case CONSTRUCTOR_ARGUMENT_GENERIC:
                case METHOD_ARGUMENT_GENERIC:
                case CONSTRUCTOR_REFERENCE_ARGUMENT_GENERIC:
                case METHOD_REFERENCE_ARGUMENT_GENERIC:
                    instruction = instructions[data.getUnsignedShort()];
                    value = data.getUnsignedByte();
                    break;
            }
            this.value = value;
            this.secondValue = secondValue;
            this.stringValue = stringValue;
            this.localVariableTable = localVariableTable;
            this.exceptionHandler = exceptionHandler;
            this.instruction = instruction;
        }
    }
}