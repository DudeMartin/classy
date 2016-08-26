package org.classy;

import org.classy.Reference.ReferenceType;
import org.classy.code.ConstantPushInstruction;
import org.classy.code.Instruction;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MethodMember extends ClassMember {

    public int maxStack;
    public int maxLocals;
    public List<String> exceptions;
    public List<AnnotationMember>[] visibleParameterAnnotations;
    public List<AnnotationMember>[] invisibleParameterAnnotations;
    public AnnotationMember.ElementValue defaultAnnotationValue;
    public List<ParameterMember> parameters;

    public MethodMember() {

    }

    MethodMember(PoolItem[] constantPool, Buffer data) {
        super(constantPool, data);
    }

    protected Set<AccessFlag> sourceAccessFlags() {
        return AccessFlag.methodFlags;
    }

    @SuppressWarnings("unchecked")
    protected void readAttribute(PoolItem[] constantPool, Buffer data, String name, int length) {
        if ("Code".equals(name)) {
            data.offset += length;
        } else if ("Exceptions".equals(name)) {
            int exceptionCount = data.getUnsignedShort();
            exceptions = new ArrayList<String>(exceptionCount);
            while (exceptionCount-- > 0) {
                exceptions.add(constantPool[constantPool[data.getUnsignedShort()].value].stringValue);
            }
        } else if ("RuntimeVisibleParameterAnnotations".equals(name)) {
            int parameterCount = data.getUnsignedByte();
            visibleParameterAnnotations = (List<AnnotationMember>[]) new ArrayList<?>[parameterCount];
            for (int i = 0; i < parameterCount; i++) {
                visibleParameterAnnotations[i] = SharedAttributes.readAnnotations(constantPool, data);
            }
        } else if ("RuntimeInvisibleParameterAnnotations".equals(name)) {
            int parameterCount = data.getUnsignedByte();
            invisibleParameterAnnotations = (List<AnnotationMember>[]) new ArrayList<?>[parameterCount];
            for (int i = 0; i < parameterCount; i++) {
                invisibleParameterAnnotations[i] = SharedAttributes.readAnnotations(constantPool, data);
            }
        } else if ("AnnotationDefault".equals(name)) {
            defaultAnnotationValue = new AnnotationMember.ElementValue(constantPool, data);
        } else if ("MethodParameters".equals(name)) {
            int parameterCount = data.getUnsignedByte();
            parameters = new ArrayList<ParameterMember>(parameterCount);
            while (parameterCount-- > 0) {
                parameters.add(new ParameterMember(constantPool, data));
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
        int codeStart = data.offset;
        int codeEnd = codeStart + codeLength;
        List<Instruction> instructionList = new ArrayList<Instruction>();
        while (data.offset < codeEnd) {
            int opcode = data.getUnsignedByte();
            switch (Instruction.TYPES.get(opcode)) {
                case CONSTANT_PUSH: {
                    int index = (opcode == Instruction.LDC) ? data.getUnsignedByte() : data.getUnsignedShort();
                    PoolItem item = constantPool[index];
                    Object constant = null;
                    switch (item.tag) {
                        case PoolItem.CONSTANT_Integer:
                            constant = item.value;
                            break;
                        case PoolItem.CONSTANT_Float:
                            constant = (float) item.doubleValue;
                            break;
                        case PoolItem.CONSTANT_Long:
                            constant = item.longValue;
                            break;
                        case PoolItem.CONSTANT_Double:
                            constant = item.doubleValue;
                            break;
                        case PoolItem.CONSTANT_String:
                            constant = constantPool[item.value].stringValue;
                            break;
                        case PoolItem.CONSTANT_Class:
                            constant = new Reference(ReferenceType.CLASS, null, Reference.toInternalName(constantPool[item.value].stringValue), null);
                            break;
                        case PoolItem.CONSTANT_Fieldref:
                        case PoolItem.CONSTANT_Methodref:
                        case PoolItem.CONSTANT_InterfaceMethodref:
                            ReferenceType type;
                            switch (item.tag) {
                                case PoolItem.CONSTANT_Fieldref:
                                    type = ReferenceType.FIELD;
                                    break;
                                case PoolItem.CONSTANT_Methodref:
                                    type = ReferenceType.METHOD;
                                    break;
                                case PoolItem.CONSTANT_InterfaceMethodref:
                                    type = ReferenceType.INTERFACE_METHOD;
                                    break;
                                default:
                                    throw new Error();
                            }
                            String owner      = constantPool[constantPool[item.value].value].stringValue;
                            String name       = constantPool[constantPool[(int) item.longValue].value].stringValue;
                            String descriptor = constantPool[(int) constantPool[(int) item.longValue].longValue].stringValue;
                            constant = new Reference(type, owner, name, descriptor);
                            break;
                        case PoolItem.CONSTANT_MethodHandle:
                            break;
                        case PoolItem.CONSTANT_MethodType:
                            constant = new Reference(ReferenceType.TYPE, null, null, constantPool[item.value].stringValue);
                            break;
                    }
                    instructionList.add(new ConstantPushInstruction(opcode, constant));
                    break;
                }
            }
        }
    }
}