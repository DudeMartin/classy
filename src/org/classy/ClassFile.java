package org.classy;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ClassFile {

    public int minorVersion;
    public int majorVersion;
    @Generated public PoolItem[] constantPool;
    public Set<AccessFlag> accessFlags;
    public String name;
    public String superclassName;
    public List<String> interfaceNames;
    public List<FieldMember> fields;
    public List<MethodMember> methods;
    public String sourceFileName;
    public List<InnerClassMember> innerClasses;
    public Reference enclosingMethod;
    public String sourceDebug;
    public List<BootstrapMethodMember> bootstrapMethods;
    public boolean deprecated;
    public String signature;
    public List<AnnotationMember> visibleAnnotations;
    public List<AnnotationMember> invisibleAnnotations;
    public List<TypeAnnotationMember> visibleTypeAnnotations;
    public List<TypeAnnotationMember> invisibleTypeAnnotations;
    public List<CustomAttribute> customAttributes;

    public ClassFile() {

    }

    public ClassFile(byte[] classData, int offset) {
        Buffer data = new Buffer(classData, offset);
        if (data.getInteger() != 0xCAFEBABE) {
            throw new RuntimeException("Bad magic number.");
        }
        minorVersion = data.getUnsignedShort();
        majorVersion = data.getUnsignedShort();
        int constantPoolLength = data.getUnsignedShort();
        constantPool = new PoolItem[constantPoolLength];
        for (int i = 1; i < constantPoolLength; i++) {
            constantPool[i] = new PoolItem(data);
            switch (constantPool[i].tag) {
                case PoolItem.CONSTANT_Long:
                case PoolItem.CONSTANT_Double:
                    i++;
                    break;
            }
        }
        accessFlags = AccessFlag.forMask(data.getUnsignedShort(), AccessFlag.CLASS_FLAGS);
        name = constantPool[constantPool[data.getUnsignedShort()].value].stringValue;
        int superclassIndex = data.getUnsignedShort();
        superclassName = (superclassIndex == 0) ? null : constantPool[constantPool[superclassIndex].value].stringValue;
        int count = data.getUnsignedShort();
        interfaceNames = new ArrayList<String>(count);
        while (count-- > 0) {
            interfaceNames.add(constantPool[constantPool[data.getUnsignedShort()].value].stringValue);
        }
        count = data.getUnsignedShort();
        fields = new ArrayList<FieldMember>(count);
        while (count-- > 0) {
            fields.add(new FieldMember(constantPool, data));
        }
        count = data.getUnsignedShort();
        methods = new ArrayList<MethodMember>(count);
        while (count-- > 0) {
            methods.add(new MethodMember(constantPool, data));
        }
        for (int i = data.getUnsignedShort(); i > 0; i--) {
            String attributeName = constantPool[data.getUnsignedShort()].stringValue;
            int length = data.getInteger();
            if ("SourceFile".equals(attributeName)) {
                sourceFileName = constantPool[data.getUnsignedShort()].stringValue;
            } else if ("InnerClasses".equals(attributeName)) {
                count = data.getUnsignedShort();
                innerClasses = new ArrayList<InnerClassMember>(count);
                while (count-- > 0) {
                    innerClasses.add(new InnerClassMember(constantPool, data));
                }
            } else if ("EnclosingMethod".equals(attributeName)) {
                enclosingMethod = new Reference();
                enclosingMethod.type = Reference.ReferenceType.METHOD;
                enclosingMethod.owner = constantPool[constantPool[data.getUnsignedShort()].value].stringValue;
                int methodIndex = data.getUnsignedShort();
                if (methodIndex != 0) {
                    enclosingMethod.name = constantPool[constantPool[methodIndex].value].stringValue;
                    enclosingMethod.name = constantPool[(int) constantPool[methodIndex].longValue].stringValue;
                }
            } else if ("SourceDebugExtension".equals(attributeName)) {
                sourceDebug = data.getString(length);
            } else if ("BootstrapMethods".equals(attributeName)) {
                count = data.getUnsignedShort();
                bootstrapMethods = new ArrayList<BootstrapMethodMember>(count);
                while (count-- > 0) {
                    bootstrapMethods.add(new BootstrapMethodMember(constantPool, data));
                }
            } else if ("Synthetic".equals(attributeName)) {
                accessFlags.add(AccessFlag.ACC_SYNTHETIC);
            } else if ("Deprecated".equals(attributeName)) {
                deprecated = true;
            } else if ("Signature".equals(attributeName)) {
                signature = constantPool[data.getUnsignedShort()].stringValue;
            } else if ("RuntimeVisibleAnnotations".equals(attributeName)) {
                visibleAnnotations = Shared.readAnnotations(constantPool, data);
            } else if ("RuntimeInvisibleAnnotations".equals(attributeName)) {
                invisibleAnnotations = Shared.readAnnotations(constantPool, data);
            } else if ("RuntimeVisibleTypeAnnotations".equals(attributeName)) {
                visibleTypeAnnotations = Shared.readTypeAnnotations(constantPool, data, this, null, null);
            } else if ("RuntimeInvisibleTypeAnnotations".equals(attributeName)) {
                invisibleTypeAnnotations = Shared.readTypeAnnotations(constantPool, data, this, null, null);
            } else {
                if (customAttributes == null) {
                    customAttributes = new ArrayList<CustomAttribute>(1);
                }
                customAttributes.add(new CustomAttribute(data, attributeName, length));
            }
        }
    }

    public ClassFile(byte[] classData) {
        this(classData, 0);
    }
}