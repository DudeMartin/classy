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
        int count = data.getUnsignedShort();
        pairs = new ArrayList<ElementPair>(count);
        while (count-- > 0) {
            pairs.add(new ElementPair(constantPool[data.getUnsignedShort()].stringValue, new ElementPair.ElementValue(constantPool, data)));
        }
    }

    public static class ElementPair {

        public String name;
        public ElementValue value;

        public ElementPair(String name, ElementValue value) {
            this.name = name;
            this.value = value;
        }

        public static class ElementValue {

            public int tag;
            public int value;
            public long longValue;
            public double doubleValue;
            public String stringValue;
            public String[] enumConstant;
            public AnnotationMember nestedAnnotation;
            public List<ElementValue> elements;

            public ElementValue() {

            }

            ElementValue(PoolItem[] constantPool, Buffer data) {
                tag = data.getUnsignedByte();
                int value = 0;
                long longValue = 0;
                double doubleValue = 0;
                String stringValue = null;
                String[] enumConstant = null;
                AnnotationMember nestedAnnotation = null;
                List<ElementValue> elements = null;
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
                        int count = data.getUnsignedShort();
                        elements = new ArrayList<ElementValue>(count);
                        while (count-- > 0) {
                            elements.add(new ElementValue(constantPool, data));
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
    }
}