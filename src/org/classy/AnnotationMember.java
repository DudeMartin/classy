package org.classy;

import java.util.ArrayList;
import java.util.List;

public class AnnotationMember {

    public String type;
    public List<ElementPair> pairs;

    public AnnotationMember() {

    }

    AnnotationMember(PoolItem[] constantPool, Buffer data) {
        readAnnotation(constantPool, data);
    }

    protected final void readAnnotation(PoolItem[] constantPool, Buffer data) {
        type = constantPool[data.getUnsignedShort()].stringValue;
        int pairCount = data.getUnsignedShort();
        pairs = new ArrayList<ElementPair>(pairCount);
        while (pairCount-- > 0) {
            pairs.add(new ElementPair(constantPool[data.getUnsignedShort()].stringValue, new ElementValue(constantPool, data)));
        }
    }

    public static final class ElementValue {

        public final int tag;
        public final int value;
        public final long longValue;
        public final double doubleValue;
        public final String stringValue;
        public final String[] enumConstant;
        public final AnnotationMember nestedAnnotation;
        public final ElementValue[] elements;

        public ElementValue(int tag,
                            int value,
                            long longValue,
                            double doubleValue,
                            String stringValue,
                            String[] enumConstant,
                            AnnotationMember nestedAnnotation,
                            ElementValue[] elements) {
            this.tag = tag;
            this.value = value;
            this.longValue = longValue;
            this.doubleValue = doubleValue;
            this.stringValue = stringValue;
            this.enumConstant = enumConstant;
            this.nestedAnnotation = nestedAnnotation;
            this.elements = elements;
        }

        ElementValue(PoolItem[] constantPool, Buffer data) {
            tag = data.getUnsignedByte();
            int value = 0;
            long longValue = 0;
            double doubleValue = 0;
            String stringValue = null;
            String[] enumConstant = null;
            AnnotationMember nestedAnnotation = null;
            ElementValue[] elements = null;
            switch (tag) {
                case 'B':
                case 'C':
                case 'I':
                case 'S':
                case 'Z':
                    value = constantPool[data.getUnsignedShort()].value;
                    break;
                case 'D':
                    doubleValue = constantPool[data.getUnsignedShort()].doubleValue;
                    break;
                case 'J':
                    longValue = constantPool[data.getUnsignedShort()].longValue;
                    break;
                case 's':
                case 'c':
                    stringValue = constantPool[data.getUnsignedShort()].stringValue;
                    break;
                case 'e':
                    enumConstant = new String[] { constantPool[data.getUnsignedShort()].stringValue, constantPool[data.getUnsignedShort()].stringValue };
                    break;
                case '@':
                    nestedAnnotation = new AnnotationMember(constantPool, data);
                    break;
                case '[':
                    int elementCount = data.getUnsignedShort();
                    elements = new ElementValue[elementCount];
                    for (int i = 0; i < elementCount; i++) {
                        elements[i] = new ElementValue(constantPool, data);
                    }
                    break;
            }
            this.value = value;
            this.longValue = longValue;
            this.doubleValue = doubleValue;
            this.stringValue = stringValue;
            this.enumConstant = enumConstant;
            this.nestedAnnotation = nestedAnnotation;
            this.elements = elements;
        }
    }

    public static final class ElementPair {

        public final String name;
        public final ElementValue value;

        public ElementPair(String name, ElementValue value) {
            this.name = name;
            this.value = value;
        }
    }
}