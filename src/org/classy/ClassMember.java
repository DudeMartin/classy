package org.classy;

import java.util.List;
import java.util.Set;

public abstract class ClassMember {

    public Set<AccessFlag> accessFlags;
    public String name;
    public String descriptor;
    public boolean deprecated;
    public String signature;
    public List<AnnotationMember> visibleAnnotations;
    public List<AnnotationMember> invisibleAnnotations;
    public List<TypeAnnotationMember> visibleTypeAnnotations;
    public List<TypeAnnotationMember> invisibleTypeAnnotations;
    public List<CustomAttribute> customAttributes;

    protected ClassMember() {

    }

    ClassMember(PoolItem[] constantPool, Buffer data) {
        accessFlags = AccessFlag.set(data.getUnsignedShort(), sourceAccessFlags());
        name = constantPool[data.getUnsignedShort()].stringValue;
        descriptor = constantPool[data.getUnsignedShort()].stringValue;
        for (int i = data.getUnsignedShort(); i > 0; i--) {
            String attributeName = constantPool[data.getUnsignedShort()].stringValue;
            int length = data.getInteger();
            if ("Synthetic".equals(attributeName)) {
                accessFlags.add(AccessFlag.ACC_SYNTHETIC);
            } else if ("Deprecated".equals(attributeName)) {
                deprecated = true;
            } else if ("Signature".equals(attributeName)) {
                signature = Shared.readSignature(constantPool, data);
            } else if ("RuntimeVisibleAnnotations".equals(attributeName)) {
                visibleAnnotations = Shared.readAnnotations(constantPool, data);
            } else if ("RuntimeInvisibleAnnotations".equals(attributeName)) {
                invisibleAnnotations = Shared.readAnnotations(constantPool, data);
            } else if ("RuntimeVisibleTypeAnnotations".equals(attributeName)) {
                visibleTypeAnnotations = Shared.readTypeAnnotations(constantPool, data);
            } else if ("RuntimeInvisibleTypeAnnotations".equals(attributeName)) {
                invisibleTypeAnnotations = Shared.readTypeAnnotations(constantPool, data);
            } else {
                readAttribute(constantPool, data, attributeName, length);
            }
        }
    }

    protected abstract Set<AccessFlag> sourceAccessFlags();
    protected abstract void readAttribute(PoolItem[] constantPool, Buffer data, String name, int length);
}