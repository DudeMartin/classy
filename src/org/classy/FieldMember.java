package org.classy;

import java.util.Set;

public class FieldMember extends ClassMember {

    public Object constantValue;

    public FieldMember() {

    }

    FieldMember(PoolItem[] constantPool, Buffer data) {
        super(constantPool, data);
    }

    protected Set<AccessFlag> sourceAccessFlags() {
        return AccessFlag.fieldFlags;
    }

    protected void readAttribute(PoolItem[] constantPool, Buffer data, String name, int length) {
        if ("ConstantValue".equals(name)) {
            PoolItem item = constantPool[data.getUnsignedShort()];
            switch (item.tag) {
                case PoolItem.CONSTANT_Long:
                    constantValue = item.longValue;
                    break;
                case PoolItem.CONSTANT_Float:
                case PoolItem.CONSTANT_Double:
                    constantValue = item.doubleValue;
                    break;
                case PoolItem.CONSTANT_Integer:
                    constantValue = item.value;
                    break;
                case PoolItem.CONSTANT_String:
                    constantValue = constantPool[item.value].stringValue;
                    break;
            }
        } else {
            data.offset += length;
        }
    }
}