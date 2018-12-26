package org.classy;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

import org.classy.instructions.Instruction;

public class TypeAnnotationMember extends AnnotationMember {

    public TargetType targetType;
    public TypeInformation information;
    public PathStep[] path;

    public TypeAnnotationMember() {

    }

    TypeAnnotationMember(PoolItem[] constantPool,
                         Buffer data,
                         ClassFile classFile,
                         MethodMember method,
                         Instruction[] instructions) {
        targetType = TargetType.forTag(data.getUnsignedByte());
        information = new TypeInformation(data, classFile, method, instructions, targetType);
        int pathLength = data.getUnsignedByte();
        path = new PathStep[pathLength];
        for (int i = 0; i < pathLength; i++) {
            path[i] = new PathStep(data);
        }
        readAnnotation(constantPool, data);
    }

    public enum TargetType {

        CLASS_TYPE_PARAMETER(0x00),
        METHOD_TYPE_PARAMETER(0x01),
        CLASS_EXTENDS(0x10),
        CLASS_TYPE_PARAMETER_BOUND(0x11),
        METHOD_TYPE_PARAMETER_BOUND(0x12),
        FIELD(0x13),
        METHOD_RETURN(0x14),
        METHOD_RECEIVER(0x15),
        METHOD_FORMAL_PARAMETER(0x16),
        THROWS(0x17),
        LOCAL_VARIABLE(0x40),
        RESOURCE_VARIABLE(0x41),
        EXCEPTION_PARAMETER(0x42),
        INSTANCEOF(0x43),
        NEW(0x44),
        CONSTRUCTOR_REFERENCE(0x45),
        METHOD_REFERENCE(0x46),
        CAST(0x47),
        CONSTRUCTOR_INVOCATION_TYPE_ARGUMENT(0x48),
        METHOD_INVOCATION_TYPE_ARGUMENT(0x49),
        CONSTRUCTOR_REFERENCE_TYPE_ARGUMENT(0x4A),
        METHOD_REFERENCE_TYPE_ARGUMENT(0x4B);

        private static final Set<TargetType> VALUES = Collections.unmodifiableSet(EnumSet.allOf(TargetType.class));
        public final int tag;

        TargetType(int tag) {
            this.tag = tag;
        }

        public static TargetType forTag(int tag) {
            for (TargetType type : VALUES) {
                if (tag == type.tag) {
                    return type;
                }
            }
            throw new IllegalArgumentException(tag + " is not a valid target type tag.");
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

    public static class PathStep {

        public Kind kind;
        public int argumentIndex;

        public PathStep() {

        }

        private PathStep(Buffer data) {
            kind = Kind.forTag(data.getUnsignedByte());
            argumentIndex = data.getUnsignedByte();
        }

        public enum Kind {

            ARRAY(0),
            INNER_TYPE(1),
            WILDCARD(2),
            TYPE_ARGUMENT(3);

            private static final Set<Kind> VALUES = Collections.unmodifiableSet(EnumSet.allOf(Kind.class));
            public final int tag;

            Kind(int tag) {
                this.tag = tag;
            }

            public static Kind forTag(int tag) {
                for (Kind kind : VALUES) {
                    if (kind.tag == tag) {
                        return kind;
                    }
                }
                throw new IllegalArgumentException(tag + " is not a valid path step kind tag.");
            }
        }
    }
}