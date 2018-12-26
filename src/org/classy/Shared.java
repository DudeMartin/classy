package org.classy;

import java.util.ArrayList;
import java.util.List;

import org.classy.SymbolicReference.Type;
import org.classy.instructions.Instruction;

class Shared {

    static List<AnnotationMember> readAnnotations(PoolItem[] constantPool, Buffer data) {
        int count = data.getUnsignedShort();
        List<AnnotationMember> annotations = new ArrayList<AnnotationMember>(count);
        while (count-- > 0) {
            annotations.add(new AnnotationMember(constantPool, data));
        }
        return annotations;
    }

    static List<TypeAnnotationMember> readTypeAnnotations(PoolItem[] constantPool,
                                                          Buffer data,
                                                          ClassFile classFile,
                                                          MethodMember method,
                                                          Instruction[] instructions) {
        int count = data.getUnsignedShort();
        List<TypeAnnotationMember> annotations = new ArrayList<TypeAnnotationMember>(count);
        while (count-- > 0) {
            annotations.add(new TypeAnnotationMember(constantPool, data, classFile, method, instructions));
        }
        return annotations;
    }

    static Object readConstant(PoolItem[] constantPool, int index) {
        PoolItem item = constantPool[index];
        switch (item.tag) {
            case PoolItem.CONSTANT_Utf8:
                return item.stringValue;
            case PoolItem.CONSTANT_Integer:
                return item.value;
            case PoolItem.CONSTANT_Float:
                return (float) item.doubleValue;
            case PoolItem.CONSTANT_Long:
                return item.longValue;
            case PoolItem.CONSTANT_Double:
                return item.doubleValue;
            case PoolItem.CONSTANT_String:
                return constantPool[item.value].stringValue;
            case PoolItem.CONSTANT_Class:
                SymbolicReference classReference = new SymbolicReference();
                classReference.type = Type.CLASS;
                classReference.name = constantPool[item.value].stringValue;
                return classReference;
            case PoolItem.CONSTANT_Fieldref:
            case PoolItem.CONSTANT_Methodref:
            case PoolItem.CONSTANT_InterfaceMethodref: {
                SymbolicReference methodReference = new SymbolicReference();
                switch (item.tag) {
                    case PoolItem.CONSTANT_Fieldref:
                        methodReference.type = Type.FIELD;
                        break;
                    case PoolItem.CONSTANT_Methodref:
                        methodReference.type = Type.METHOD;
                        break;
                    default:
                        methodReference.type = Type.INTERFACE_METHOD;
                        break;
                }
                methodReference.owner = constantPool[constantPool[item.value].value].stringValue;
                methodReference.name = constantPool[constantPool[(int) item.longValue].value].stringValue;
                methodReference.descriptor = constantPool[(int) constantPool[(int) item.longValue].longValue].stringValue;
                return methodReference;
            }
            case PoolItem.CONSTANT_MethodHandle:
                SymbolicReference handleReference = new SymbolicReference();
                handleReference.type = Type.HANDLE;
                handleReference.owner = constantPool[constantPool[constantPool[(int) item.longValue].value].value].stringValue;
                handleReference.name = constantPool[constantPool[(int) constantPool[(int) item.longValue].longValue].value].stringValue;
                handleReference.descriptor = constantPool[(int) constantPool[(int) constantPool[(int) item.longValue].longValue].longValue].stringValue;
                handleReference.kind = item.value;
                return handleReference;
            case PoolItem.CONSTANT_MethodType:
                SymbolicReference typeReference = new SymbolicReference();
                typeReference.type = Type.TYPE;
                typeReference.descriptor = constantPool[item.value].stringValue;
                return typeReference;
        }
        throw new Error("Unreachable code.");
    }
}