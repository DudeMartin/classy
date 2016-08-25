package org.classy;

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
        while (data.offset < codeEnd) {

        }
    }
}