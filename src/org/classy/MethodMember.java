package org.classy;

import org.classy.instructions.*;

import java.util.*;

public class MethodMember extends ClassMember {

    public int maxStack;
    public int maxLocals;
    public List<Instruction> instructions;
    public List<ExceptionHandler> exceptionHandlers;
    public List<LineNumber> lineNumbers;
    public List<LocalVariable> localVariables;
    public List<LocalVariable> localTypeVariables;
    @Generated public StackMapFrame[] stackMapTable;
    public List<TypeAnnotationMember> codeVisibleTypeAnnotations;
    public List<TypeAnnotationMember> codeInvisibleTypeAnnotations;
    public List<CustomAttribute> codeCustomAttributes;
    public List<String> exceptions;
    public List<List<AnnotationMember>> visibleParameterAnnotations;
    public List<List<AnnotationMember>> invisibleParameterAnnotations;
    public AnnotationMember.ElementValue defaultAnnotationValue;
    public List<MethodParameter> parameters;

    public MethodMember() {

    }

    MethodMember(PoolItem[] constantPool, Buffer data) {
        super(constantPool, data);
    }

    protected Set<AccessFlag> sourceAccessFlags() {
        return AccessFlag.METHOD_FLAGS;
    }

    protected void readAttribute(PoolItem[] constantPool, Buffer data, String name, int length) {
        if ("Code".equals(name)) {
            readCodeAttribute(constantPool, data);
        } else if ("Exceptions".equals(name)) {
            int count = data.getUnsignedShort();
            exceptions = new ArrayList<String>(count);
            while (count-- > 0) {
                exceptions.add(constantPool[constantPool[data.getUnsignedShort()].value].stringValue);
            }
        } else if ("RuntimeVisibleParameterAnnotations".equals(name)) {
            int count = data.getUnsignedByte();
            visibleParameterAnnotations = new ArrayList<List<AnnotationMember>>(count);
            while (count-- > 0) {
                visibleParameterAnnotations.add(Shared.readAnnotations(constantPool, data));
            }
        } else if ("RuntimeInvisibleParameterAnnotations".equals(name)) {
            int count = data.getUnsignedByte();
            invisibleParameterAnnotations = new ArrayList<List<AnnotationMember>>(count);
            while (count-- > 0) {
                invisibleParameterAnnotations.add(Shared.readAnnotations(constantPool, data));
            }
        } else if ("AnnotationDefault".equals(name)) {
            defaultAnnotationValue = new AnnotationMember.ElementValue(constantPool, data);
        } else if ("MethodParameters".equals(name)) {
            int count = data.getUnsignedByte();
            parameters = new ArrayList<MethodParameter>(count);
            while (count-- > 0) {
                parameters.add(new MethodParameter(constantPool, data));
            }
        } else {
            if (customAttributes == null) {
                customAttributes = new ArrayList<CustomAttribute>(1);
            }
            customAttributes.add(new CustomAttribute(data, name, length));
        }
    }

    private void readCodeAttribute(PoolItem[] constantPool, Buffer data) {
        maxStack = data.getUnsignedShort();
        maxLocals = data.getUnsignedShort();
        int codeLength = data.getInteger();
        Instruction[] codeInstructions = new Instruction[codeLength];
        int instructionCount = 0;
        for (int i = 0, codeStart = data.offset, instructionStart; i < codeLength; i += data.offset - instructionStart, instructionCount++) {
            instructionStart = data.offset;
            readInstruction(constantPool, data, codeStart, codeInstructions, i);
        }
        instructions = new ArrayList<Instruction>(instructionCount);
        for (int i = 0; i < codeLength; i++) {
            Instruction instruction = codeInstructions[i];
            if (instruction != null) {
                instructions.add(instruction);
            }
        }
        int count = data.getUnsignedShort();
        exceptionHandlers = new ArrayList<ExceptionHandler>(count);
        while (count-- > 0) {
            exceptionHandlers.add(new ExceptionHandler(constantPool, data, codeInstructions));
        }
        for (int i = data.getUnsignedShort(); i > 0; i--) {
            String attributeName = constantPool[data.getUnsignedShort()].stringValue;
            int length = data.getInteger();
            if ("LineNumberTable".equals(attributeName)) {
                count = data.getUnsignedShort();
                if (lineNumbers == null) {
                    lineNumbers = new ArrayList<LineNumber>(count);
                }
                while (count-- > 0) {
                    lineNumbers.add(new LineNumber(data, codeInstructions));
                }
            } else if ("LocalVariableTable".equals(attributeName)) {
                count = data.getUnsignedShort();
                if (localVariables == null) {
                    localVariables = new ArrayList<LocalVariable>(count);
                }
                while (count-- > 0) {
                    localVariables.add(new LocalVariable(constantPool, data, codeInstructions));
                }
            } else if ("LocalVariableTypeTable".equals(attributeName)) {
                count = data.getUnsignedShort();
                if (localTypeVariables == null) {
                    localTypeVariables = new ArrayList<LocalVariable>(count);
                }
                while (count-- > 0) {
                    localTypeVariables.add(new LocalVariable(constantPool, data, codeInstructions));
                }
            } else if ("StackMapTable".equals(attributeName)) {
                readStackMapTable(constantPool, data, codeInstructions);
            } else if ("RuntimeVisibleTypeAnnotations".equals(attributeName)) {
                codeVisibleTypeAnnotations = Shared.readTypeAnnotations(constantPool, data, null, this, codeInstructions);
            } else if ("RuntimeInvisibleTypeAnnotations".equals(attributeName)) {
                codeInvisibleTypeAnnotations = Shared.readTypeAnnotations(constantPool, data, null, this, codeInstructions);
            } else {
                if (codeCustomAttributes == null) {
                    codeCustomAttributes = new ArrayList<CustomAttribute>(1);
                }
                codeCustomAttributes.add(new CustomAttribute(data, attributeName, length));
            }
        }
    }

    private void readInstruction(PoolItem[] constantPool,
                                 Buffer data,
                                 int codeStart,
                                 Instruction[] instructions,
                                 int index) {
        int opcode = data.getUnsignedByte();
        if (instructions[index] == null) {
            switch (Instruction.TYPES.get(opcode)) {
                case NULLARY:
                    instructions[index] = new NullaryInstruction(opcode);
                    break;
                case PUSH:
                    instructions[index] = new PushInstruction(opcode, (opcode == Instruction.BIPUSH) ? data.getByte() : data.getShort());
                    break;
                case VARIABLE:
                    instructions[index] = new VariableInstruction(opcode, data.getUnsignedByte());
                    break;
                case INCREMENT:
                    instructions[index] = new IncrementInstruction(data.getUnsignedByte(), data.getByte());
                    break;
                case CONSTANT_PUSH:
                    instructions[index] = new ConstantPushInstruction(opcode, Shared.readConstant(constantPool, (opcode == Instruction.LDC) ? data.getUnsignedByte() : data.getUnsignedShort()));
                    break;
                case JUMP:
                    instructions[index] = new JumpInstruction(opcode, readTarget(constantPool, data, codeStart, instructions, index, (opcode == Instruction.GOTO_W || opcode == Instruction.JSR_W) ? data.getInteger() : data.getShort()));
                    break;
                case TABLE_SWITCH: {
                    data.offset += 3 - (index & 3);
                    Instruction defaultTarget = readTarget(constantPool, data, codeStart, instructions, index, data.getInteger());
                    int low = data.getInteger();
                    int high = data.getInteger();
                    int count = high - low + 1;
                    List<Instruction> targets = new ArrayList<Instruction>(count);
                    while (count-- > 0) {
                        targets.add(readTarget(constantPool, data, codeStart, instructions, index, data.getInteger()));
                    }
                    instructions[index] = new TableSwitchInstruction(low, high, defaultTarget, targets);
                    break;
                }
                case LOOKUP_SWITCH:
                    data.offset += 3 - (index & 3);
                    Instruction defaultTarget = readTarget(constantPool, data, codeStart, instructions, index, data.getInteger());
                    SortedMap<Integer, Instruction> targets = new TreeMap<Integer, Instruction>();
                    int count = data.getInteger();
                    while (count-- > 0) {
                        targets.put(data.getInteger(), readTarget(constantPool, data, codeStart, instructions, index, data.getInteger()));
                    }
                    instructions[index] = new LookupSwitchInstruction(defaultTarget, targets);
                    break;
                case FIELD:
                    instructions[index] = new FieldInstruction(opcode, (Reference) Shared.readConstant(constantPool, data.getUnsignedShort()));
                    break;
                case METHOD:
                    instructions[index] = new MethodInstruction(opcode, (Reference) Shared.readConstant(constantPool, data.getUnsignedShort()));
                    if (opcode == Instruction.INVOKEINTERFACE) {
                        data.offset += 2;
                    }
                    break;
                case DYNAMIC_METHOD:
                    instructions[index] = new DynamicMethodInstruction((Reference) Shared.readConstant(constantPool, data.getUnsignedShort()));
                    data.offset += 2;
                    break;
                case TYPE:
                    String type;
                    if (opcode == Instruction.NEWARRAY) {
                        int code = data.getUnsignedByte();
                        switch (code) {
                            case TypeInstruction.T_BOOLEAN:
                                type = "[Z";
                                break;
                            case TypeInstruction.T_CHAR:
                                type = "[C";
                                break;
                            case TypeInstruction.T_FLOAT:
                                type = "[F";
                                break;
                            case TypeInstruction.T_DOUBLE:
                                type = "[D";
                                break;
                            case TypeInstruction.T_BYTE:
                                type = "[B";
                                break;
                            case TypeInstruction.T_SHORT:
                                type = "[S";
                                break;
                            case TypeInstruction.T_INT:
                                type = "[I";
                                break;
                            case TypeInstruction.T_LONG:
                                type = "[J";
                                break;
                            default:
                                throw new RuntimeException(code + " is not a valid or supported primitive array type code.");
                        }
                    } else {
                        type = constantPool[constantPool[data.getUnsignedShort()].value].stringValue;
                    }
                    instructions[index] = new TypeInstruction(opcode, type);
                    break;
                case WIDE:
                    opcode = data.getUnsignedByte();
                    if (opcode == Instruction.IINC) {
                        instructions[index] = new IncrementInstruction(data.getUnsignedShort(), data.getShort());
                    } else {
                        instructions[index] = new VariableInstruction(opcode, data.getUnsignedShort());
                    }
                    break;
                case MULTIDIMENSIONAL_ARRAY:
                    instructions[index] = new MultidimensionalArrayInstruction(constantPool[constantPool[data.getUnsignedShort()].value].stringValue, data.getUnsignedByte());
                    break;
            }
        } else {
            switch (Instruction.TYPES.get(opcode)) {
                case PUSH:
                    data.offset += (opcode == Instruction.BIPUSH) ? 1 : 2;
                    break;
                case VARIABLE:
                    data.offset += 1;
                    break;
                case INCREMENT:
                case FIELD:
                    data.offset += 2;
                    break;
                case CONSTANT_PUSH:
                    data.offset += (opcode == Instruction.LDC) ? 1 : 2;
                    break;
                case JUMP:
                    data.offset += (opcode == Instruction.GOTO_W || opcode == Instruction.JSR_W) ? 4 : 2;
                    break;
                case TABLE_SWITCH:
                    data.offset += 7 - (index & 3);
                    data.offset += (-data.getInteger() + data.getInteger() + 1) * 4;
                    break;
                case LOOKUP_SWITCH:
                    data.offset += 7 - (index & 3);
                    data.offset += data.getInteger() * 8;
                    break;
                case METHOD:
                    data.offset += 2;
                    if (opcode == Instruction.INVOKEINTERFACE) {
                        data.offset += 2;
                    }
                    break;
                case DYNAMIC_METHOD:
                    data.offset += 4;
                    break;
                case TYPE:
                    data.offset += (opcode == Instruction.NEWARRAY) ? 1 : 2;
                    break;
                case WIDE:
                    data.offset += (data.getUnsignedByte() == Instruction.IINC) ? 4 : 2;
                    break;
                case MULTIDIMENSIONAL_ARRAY:
                    data.offset += 3;
                    break;
            }
        }
    }

    private Instruction readTarget(PoolItem[] constantPool,
                                   Buffer data,
                                   int codeStart,
                                   Instruction[] instructions,
                                   int index,
                                   int offset) {
        Instruction target = instructions[index + offset];
        if (target == null) {
            int current = data.offset;
            data.offset = codeStart + index + offset;
            readInstruction(constantPool, data, codeStart, instructions, index + offset);
            data.offset = current;
            return instructions[index + offset];
        }
        return target;
    }

    private void readTypeInformation(PoolItem[] constantPool,
                                      Buffer data,
                                      Instruction[] instructions,
                                      Object[] array,
                                      int index,
                                      int count) {
        while (count-- > 0) {
            int tag = data.getUnsignedByte();
            switch (tag) {
                case StackMapFrame.ITEM_Double:
                case StackMapFrame.ITEM_Long:
                    array[index] = tag;
                    break;
                case StackMapFrame.ITEM_Object:
                    array[index] = constantPool[constantPool[data.getUnsignedShort()].value].stringValue;
                    break;
                case StackMapFrame.ITEM_Uninitialized:
                    TypeInstruction newInstruction = (TypeInstruction) instructions[data.getUnsignedShort()];
                    array[index] = newInstruction.type;
                    break;
                default:
                    array[index] = tag;
                    break;
            }
            index++;
        }
    }

    private void readStackMapTable(PoolItem[] constantPool, Buffer data, Instruction[] instructions) {
        int frameCount = data.getUnsignedShort();
        stackMapTable = new StackMapFrame[frameCount];
        Object[] previous = StackMapFrame.EMPTY;
        for (int offset = 0, i = 0; i < frameCount; offset++, i++) {
            int type = data.getUnsignedByte();
            StackMapFrame frame = new StackMapFrame();
            frame.type = type;
            if (type == StackMapFrame.FULL_FRAME) {
                offset += data.getUnsignedShort();
                int count = data.getUnsignedShort();
                frame.locals = new Object[count];
                readTypeInformation(constantPool, data, instructions, frame.locals, 0, count);
                count = data.getUnsignedShort();
                frame.stack = new Object[count];
                readTypeInformation(constantPool, data, instructions, frame.stack, 0, count);
            } else if (type >= StackMapFrame.APPEND) {
                offset += data.getUnsignedShort();
                int toAppend = type - StackMapFrame.SAME_FRAME_EXTENDED;
                frame.locals = new Object[previous.length + toAppend];
                System.arraycopy(previous, 0, frame.locals, 0, previous.length);
                readTypeInformation(constantPool, data, instructions, frame.locals, previous.length, toAppend);
                frame.stack = StackMapFrame.EMPTY;
            } else if (type == StackMapFrame.SAME_FRAME_EXTENDED) {
                offset += data.getUnsignedShort();
                frame.locals = previous.clone();
                frame.stack = StackMapFrame.EMPTY;
            } else if (type >= StackMapFrame.CHOP) {
                offset += data.getUnsignedShort();
                frame.locals = new Object[previous.length + type - StackMapFrame.SAME_FRAME_EXTENDED];
                System.arraycopy(previous, 0, frame.locals, 0, frame.locals.length);
                frame.stack = StackMapFrame.EMPTY;
            } else if (type == StackMapFrame.SAME_LOCALS_1_STACK_ITEM_EXTENDED) {
                offset += data.getUnsignedShort();
                frame.locals = previous.clone();
                frame.stack = new Object[1];
                readTypeInformation(constantPool, data, instructions, frame.stack, 0, 1);
            } else if (type >= StackMapFrame.SAME_LOCALS_1_STACK_ITEM) {
                offset += type - StackMapFrame.SAME_LOCALS_1_STACK_ITEM;
                frame.locals = previous.clone();
                frame.stack = new Object[1];
                readTypeInformation(constantPool, data, instructions, frame.stack, 0, 1);
            } else {
                offset += type;
                frame.locals = previous.clone();
                frame.stack = StackMapFrame.EMPTY;
            }
            frame.start = instructions[offset];
            stackMapTable[i] = frame;
            previous = frame.locals;
        }
    }
}