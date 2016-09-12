package org.classy;

import org.classy.Reference.ReferenceType;
import org.classy.instructions.Instruction;

import java.util.ArrayList;
import java.util.List;

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
                return new Reference(ReferenceType.CLASS, null, constantPool[item.value].stringValue, null, 0);
            case PoolItem.CONSTANT_Fieldref:
            case PoolItem.CONSTANT_Methodref:
            case PoolItem.CONSTANT_InterfaceMethodref: {
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
                        throw new Error("Unreachable code.");
                }
                String owner = constantPool[constantPool[item.value].value].stringValue;
                String name = constantPool[constantPool[(int) item.longValue].value].stringValue;
                String descriptor = constantPool[(int) constantPool[(int) item.longValue].longValue].stringValue;
                return new Reference(type, owner, name, descriptor, 0);
            }
            case PoolItem.CONSTANT_MethodHandle:
                String owner = constantPool[constantPool[constantPool[(int) item.longValue].value].value].stringValue;
                String name = constantPool[constantPool[(int) constantPool[(int) item.longValue].longValue].value].stringValue;
                String descriptor = constantPool[(int) constantPool[(int) constantPool[(int) item.longValue].longValue].longValue].stringValue;
                return new Reference(ReferenceType.HANDLE, owner, name, descriptor, item.value);
            case PoolItem.CONSTANT_MethodType:
                return new Reference(ReferenceType.TYPE, null, null, constantPool[item.value].stringValue, 0);
        }
        throw new Error("Unreachable code.");
    }
}