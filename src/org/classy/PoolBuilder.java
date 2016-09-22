package org.classy;

import static org.classy.PoolItem.*;

class PoolBuilder {

    final Buffer encoded = new Buffer(1024);
    private int[] hashes = new int[16];
    private int index;

    int put(int tag,
            int value,
            long longValue,
            double doubleValue,
            String... stringValues) {
        int hash = 31 * tag;
        switch (tag) {
            case CONSTANT_Utf8:
                return putStringItem(tag, stringValues[0]);
            case CONSTANT_Integer:
                hash += value;
                break;
            case CONSTANT_Class:
            case CONSTANT_String:
            case CONSTANT_MethodType:
                hash += stringValues[0].hashCode();
                break;
            case CONSTANT_Float:
            case CONSTANT_Double:
                long bits = Double.doubleToLongBits(doubleValue);
                hash += bits ^ (bits >>> 32);
                break;
            case CONSTANT_Long:
                hash += longValue ^ (longValue >>> 32);
                break;
            case CONSTANT_Fieldref:
            case CONSTANT_Methodref:
            case CONSTANT_InterfaceMethodref:
                hash += stringValues[0].hashCode();
                hash = 31 * hash + stringValues[1].hashCode();
                hash = 31 * hash + stringValues[2].hashCode();
                break;
            case CONSTANT_NameAndType:
                hash += stringValues[0].hashCode();
                hash = 31 * hash + stringValues[1].hashCode();
                break;
            case CONSTANT_MethodHandle:
                hash += value;
                hash = 31 * hash + stringValues[0].hashCode();
                hash = 31 * hash + stringValues[1].hashCode();
                hash = 31 * hash + stringValues[2].hashCode();
                break;
            case CONSTANT_InvokeDynamic:
                hash += value;
                hash = 31 * hash + stringValues[0].hashCode();
                hash = 31 * hash + stringValues[1].hashCode();
                break;
        }
        int itemIndex = find(hash);
        if (itemIndex == -1) {
            encoded.putByte(tag);
            switch (tag) {
                case CONSTANT_Integer:
                case CONSTANT_Float:
                    encoded.putInteger(value);
                    break;
                case CONSTANT_Long:
                    encoded.putLong(longValue);
                    break;
                case CONSTANT_Double:
                    encoded.putLong(Double.doubleToLongBits(doubleValue));
                    break;
                case CONSTANT_Class:
                case CONSTANT_String:
                case CONSTANT_MethodType:
                    encoded.putShort(putStringItem(CONSTANT_Utf8, stringValues[0]));
                    break;
                case CONSTANT_Fieldref:
                case CONSTANT_Methodref:
                case CONSTANT_InterfaceMethodref:
                    encoded.putShort(putStringItem(CONSTANT_Class, stringValues[0]));
                    encoded.putShort(putStringItem(CONSTANT_NameAndType, stringValues[1], stringValues[2]));
                    break;
                case CONSTANT_NameAndType:
                    encoded.putShort(putStringItem(CONSTANT_Utf8, stringValues[0]));
                    encoded.putShort(putStringItem(CONSTANT_Utf8, stringValues[1]));
                    break;
                case CONSTANT_MethodHandle:
                    encoded.putByte(value);
                    int referenceTag;
                    switch (value) {
                        case REF_getField:
                        case REF_getStatic:
                        case REF_putField:
                        case REF_putStatic:
                            referenceTag = CONSTANT_Fieldref;
                            break;
                        case REF_invokeVirtual:
                        case REF_invokeStatic:
                        case REF_invokeSpecial:
                        case REF_newInvokeSpecial:
                            referenceTag = CONSTANT_Methodref;
                            break;
                        case REF_invokeInterface:
                            referenceTag = CONSTANT_InterfaceMethodref;
                            break;
                        default:
                            throw new RuntimeException(value + " is not a valid reference kind.");
                    }
                    encoded.putShort(putStringItem(referenceTag, stringValues[0], stringValues[1], stringValues[2]));
                    break;
                case CONSTANT_InvokeDynamic:
                    encoded.putShort(value);
                    encoded.putShort(putStringItem(CONSTANT_NameAndType, stringValues[0], stringValues[1]));
                    break;
            }
            return store(hash);
        }
        return itemIndex;
    }

    int putStringItem(int tag, String... values) {
        if (tag == CONSTANT_Utf8) {
            int hash = 31 * tag + values[0].hashCode();
            int stringIndex = find(hash);
            if (stringIndex == -1) {
                encoded.putByte(tag);
                encoded.putString(values[0]);
                return store(hash);
            }
            return stringIndex;
        }
        return put(tag, 0, 0, 0, values);
    }

    int putReferenceItem(Reference reference) {
        switch (reference.type) {
            case CLASS:
                return putStringItem(CONSTANT_Class, reference.name);
            case FIELD:
            case METHOD:
            case INTERFACE_METHOD:
                int itemTag;
                switch (reference.type) {
                    case FIELD:
                        itemTag = CONSTANT_Fieldref;
                        break;
                    case METHOD:
                        itemTag = CONSTANT_Methodref;
                        break;
                    case INTERFACE_METHOD:
                        itemTag = CONSTANT_InterfaceMethodref;
                        break;
                    default:
                        throw new Error("Unreachable code.");
                }
                return putStringItem(itemTag, reference.owner, reference.name, reference.descriptor);
            case HANDLE:
                return put(CONSTANT_MethodHandle, reference.kind, 0, 0, reference.owner, reference.name, reference.descriptor);
            case TYPE:
                return putStringItem(CONSTANT_MethodType, reference.descriptor);
            default:
                throw new NullPointerException("The provided reference does not specify a type.");
        }
    }

    private int find(int hash) {
        for (int i = 0; i < index; i++) {
            if (hashes[i] == hash) {
                return i;
            }
        }
        return -1;
    }

    private int store(int hash) {
        if (index == hashes.length) {
            int[] newHashes = new int[index * 2];
            System.arraycopy(hashes, 0, newHashes, 0, index);
            hashes = newHashes;
        }
        hashes[index++] = hash;
        return index;
    }
}