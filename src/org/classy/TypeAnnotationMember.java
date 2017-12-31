package org.classy;

import org.classy.instructions.Instruction;

public class TypeAnnotationMember extends AnnotationMember {

    public TargetType target;
    public TypeInformation information;
    public TypePathStep[] path;

    public TypeAnnotationMember() {

    }

    TypeAnnotationMember(PoolItem[] constantPool,
                         Buffer data,
                         ClassFile classFile,
                         MethodMember method,
                         Instruction[] instructions) {
        target = TargetType.forValue(data.getUnsignedByte());
        information = new TypeInformation(data, classFile, method, instructions, target);
        int pathLength = data.getUnsignedByte();
        path = new TypePathStep[pathLength];
        for (int i = 0; i < pathLength; i++) {
            path[i] = new TypePathStep(data);
        }
        readAnnotation(constantPool, data);
    }

    public enum TargetType {

        CLASS_TYPE_PARAMETER (0x00),
        METHOD_TYPE_PARAMETER (0x01),
        CLASS_EXTENDS (0x10),
        CLASS_TYPE_PARAMETER_BOUND (0x11),
        METHOD_TYPE_PARAMETER_BOUND (0x12),
        FIELD (0x13),
        METHOD_RETURN (0x14),
        METHOD_RECEIVER (0x15),
        METHOD_FORMAL_PARAMETER (0x16),
        THROWS (0x17),
        LOCAL_VARIABLE (0x40),
        RESOURCE_VARIABLE (0x41),
        EXCEPTION_PARAMETER (0x42),
        INSTANCEOF (0x43),
        NEW (0x44),
        CONSTRUCTOR_REFERENCE (0x45),
        METHOD_REFERENCE (0x46),
        CAST (0x47),
        CONSTRUCTOR_INVOCATION_TYPE_ARGUMENT (0x48),
        METHOD_INVOCATION_TYPE_ARGUMENT (0x49),
        CONSTRUCTOR_REFERENCE_TYPE_ARGUMENT (0x4A),
        METHOD_REFERENCE_TYPE_ARGUMENT (0x4B);

        public final int typeValue;

        TargetType(int typeValue) {
            this.typeValue = typeValue;
        }

        public static TargetType forValue(int typeValue) {
            for (TargetType type : TargetType.values()) {
                if (typeValue == type.typeValue) {
                    return type;
                }
            }
            throw new IllegalArgumentException(typeValue + " is not a valid target type value.");
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
                                TargetType target) {
            int value = 0;
            int secondValue = 0;
            String stringValue = null;
            LocalVariableRange[] localVariableTable = null;
            ExceptionHandler exceptionHandler = null;
            Instruction instruction = null;
            switch (target) {
                case CLASS_TYPE_PARAMETER:
                case METHOD_TYPE_PARAMETER:
                case METHOD_FORMAL_PARAMETER:
                    value = data.getUnsignedByte();
                    break;
                case CLASS_EXTENDS:
                    int supertypeIndex = data.getUnsignedShort();
                    stringValue = (supertypeIndex == 65535) ? classFile.superclassName : classFile.interfaceNames.get(supertypeIndex);
                    break;
                case CLASS_TYPE_PARAMETER_BOUND:
                case METHOD_TYPE_PARAMETER_BOUND:
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
                case INSTANCEOF:
                case NEW:
                case CONSTRUCTOR_REFERENCE:
                case METHOD_REFERENCE:
                    instruction = instructions[data.getUnsignedShort()];
                    break;
                case CAST:
                case CONSTRUCTOR_INVOCATION_TYPE_ARGUMENT:
                case METHOD_INVOCATION_TYPE_ARGUMENT:
                case CONSTRUCTOR_REFERENCE_TYPE_ARGUMENT:
                case METHOD_REFERENCE_TYPE_ARGUMENT:
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
    }

    public static class TypePathStep {

        public TypePathKind pathKind;
        public int argumentIndex;

        public TypePathStep() {

        }

        private TypePathStep(Buffer data) {
            pathKind = TypePathKind.forValue(data.getUnsignedByte());
            argumentIndex = data.getUnsignedByte();
        }

        public enum TypePathKind {

            ARRAY (0),
            INNER_TYPE (1),
            WILDCARD (2),
            TYPE_ARGUMENT (3);

            public final int kindValue;

            TypePathKind(int kindValue) {
                this.kindValue = kindValue;
            }

            public static TypePathKind forValue(int kindValue) {
                if (kindValue < 0 || kindValue > 3) {
                    throw new IllegalArgumentException(kindValue + " is not a valid type path kind value.");
                }
                return TypePathKind.values()[kindValue];
            }
        }
    }
}