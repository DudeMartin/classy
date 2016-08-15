package org.classy;

import java.util.ArrayList;
import java.util.List;

class SharedAttributes {

    static String readSignature(PoolItem[] constantPool, Buffer data) {
        return constantPool[data.getUnsignedShort()].stringValue;
    }

    static List<AnnotationMember> readAnnotations(PoolItem[] constantPool, Buffer data) {
        int count = data.getUnsignedShort();
        List<AnnotationMember> annotations = new ArrayList<AnnotationMember>(count);
        while (count-- > 0) {
            annotations.add(new AnnotationMember(constantPool, data));
        }
        return annotations;
    }

    static List<TypeAnnotationMember> readTypeAnnotations(PoolItem[] constantPool, Buffer data) {
        int count = data.getUnsignedShort();
        List<TypeAnnotationMember> annotations = new ArrayList<TypeAnnotationMember>(count);
        while (count-- > 0) {
            annotations.add(new TypeAnnotationMember(constantPool, data));
        }
        return annotations;
    }
}