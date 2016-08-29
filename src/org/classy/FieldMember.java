package org.classy;

import java.util.ArrayList;
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
            constantValue = Shared.readConstant(constantPool, data.getUnsignedShort());
        } else {
            if (customAttributes == null) {
                customAttributes = new ArrayList<CustomAttribute>(1);
            }
            customAttributes.add(new CustomAttribute(data, name, length));
        }
    }
}