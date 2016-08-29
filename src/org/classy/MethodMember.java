package org.classy;

import org.classy.code.*;

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
                visibleParameterAnnotations[i] = Shared.readAnnotations(constantPool, data);
            }
        } else if ("RuntimeInvisibleParameterAnnotations".equals(name)) {
            int parameterCount = data.getUnsignedByte();
            invisibleParameterAnnotations = (List<AnnotationMember>[]) new ArrayList<?>[parameterCount];
            for (int i = 0; i < parameterCount; i++) {
                invisibleParameterAnnotations[i] = Shared.readAnnotations(constantPool, data);
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
                case NULLARY:
                    instructionList.add(new NullaryInstruction(opcode));
                    break;
                case PUSH:
                    instructionList.add(new PushInstruction(opcode, (opcode == Instruction.BIPUSH) ? data.getByte() : data.getShort()));
                    break;
                case VARIABLE:
                    instructionList.add(new VariableInstruction(opcode, data.getUnsignedByte()));
                    break;
                case CONSTANT_PUSH:
                    instructionList.add(new ConstantPushInstruction(opcode, Shared.readConstant(constantPool, (opcode == Instruction.LDC) ? data.getUnsignedByte() : data.getUnsignedShort())));
                    break;
            }
        }
    }
}