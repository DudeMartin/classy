package org.classy;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ClassFile {

    public int minorVersion;
    public int majorVersion;
    public PoolItem[] constantPool;
    public Set<AccessFlag> accessFlags;
    public String name;
    public String superclassName;
    public List<String> interfaceNames;
    public List<FieldMember> fields;
    public List<MethodMember> methods;
    public String sourceFileName;
    public List<InnerClassMember> innerClasses;
    public String enclosingClassName;
    public String enclosingMethodName;
    public String enclosingMethodDescriptor;
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
            throw new IllegalArgumentException("Bad magic number.");
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
        accessFlags = AccessFlag.set(data.getUnsignedShort(), AccessFlag.classFlags);
        name = constantPool[constantPool[data.getUnsignedShort()].value].stringValue;
        superclassName = constantPool[constantPool[data.getUnsignedShort()].value].stringValue;
        int interfaceCount = data.getUnsignedShort();
        interfaceNames = new ArrayList<String>(interfaceCount);
        while (interfaceCount-- > 0) {
            interfaceNames.add(constantPool[constantPool[data.getUnsignedShort()].value].stringValue);
        }
        int fieldCount = data.getUnsignedShort();
        fields = new ArrayList<FieldMember>(fieldCount);
        while (fieldCount-- > 0) {
            fields.add(new FieldMember(constantPool, data));
        }
        int methodCount = data.getUnsignedShort();
        methods = new ArrayList<MethodMember>(methodCount);
        while (methodCount-- > 0) {
            methods.add(new MethodMember(constantPool, data));
        }
        for (int i = data.getUnsignedShort(); i > 0; i--) {
            String attributeName = constantPool[data.getUnsignedShort()].stringValue;
            int length = data.getInteger();
            if ("SourceFile".equals(attributeName)) {
                sourceFileName = constantPool[data.getUnsignedShort()].stringValue;
            } else if ("InnerClasses".equals(attributeName)) {
                int classCount = data.getUnsignedShort();
                innerClasses = new ArrayList<InnerClassMember>(classCount);
                while (classCount-- > 0) {
                    innerClasses.add(new InnerClassMember(constantPool, data));
                }
            } else if ("EnclosingMethod".equals(attributeName)) {
                enclosingClassName = constantPool[constantPool[data.getUnsignedShort()].value].stringValue;
                PoolItem methodItem = constantPool[data.getUnsignedShort()];
                enclosingMethodName = constantPool[methodItem.value].stringValue;
                enclosingMethodDescriptor = constantPool[(int) methodItem.longValue].stringValue;
            } else if ("SourceDebugExtension".equals(attributeName)) {
                sourceDebug = data.getString(length);
            } else if ("BootstrapMethods".equals(attributeName)) {
                int bootstrapMethodCount = data.getUnsignedShort();
                bootstrapMethods = new ArrayList<BootstrapMethodMember>(bootstrapMethodCount);
                while (bootstrapMethodCount-- > 0) {
                    bootstrapMethods.add(new BootstrapMethodMember(constantPool, data));
                }
            } else if ("Synthetic".equals(attributeName)) {
                accessFlags.add(AccessFlag.ACC_SYNTHETIC);
            } else if ("Deprecated".equals(attributeName)) {
                deprecated = true;
            } else if ("Signature".equals(attributeName)) {
                signature = SharedAttributes.readSignature(constantPool, data);
            } else if ("RuntimeVisibleAnnotations".equals(attributeName)) {
                visibleAnnotations = SharedAttributes.readAnnotations(constantPool, data);
            } else if ("RuntimeInvisibleAnnotations".equals(attributeName)) {
                invisibleAnnotations = SharedAttributes.readAnnotations(constantPool, data);
            } else if ("RuntimeVisibleTypeAnnotations".equals(attributeName)) {
                visibleTypeAnnotations = SharedAttributes.readTypeAnnotations(constantPool, data);
            } else if ("RuntimeInvisibleTypeAnnotations".equals(attributeName)) {
                invisibleTypeAnnotations = SharedAttributes.readTypeAnnotations(constantPool, data);
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