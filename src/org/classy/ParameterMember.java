package org.classy;

import java.util.Set;

public class ParameterMember {

    public String name;
    public Set<AccessFlag> accessFlags;

    public ParameterMember() {

    }

    ParameterMember(PoolItem[] constantPool, Buffer data) {
        int nameIndex = data.getUnsignedShort();
        name = (nameIndex == 0) ? null : constantPool[nameIndex].stringValue;
        accessFlags = AccessFlag.set(data.getUnsignedShort(), AccessFlag.parameterFlags);
    }
}