package org.classy;

import java.util.Set;

public class MethodParameter {

    public String name;
    public Set<AccessFlag> accessFlags;

    public MethodParameter() {

    }

    MethodParameter(PoolItem[] constantPool, Buffer data) {
        int nameIndex = data.getUnsignedShort();
        name = (nameIndex == 0) ? null : constantPool[nameIndex].stringValue;
        accessFlags = AccessFlag.set(data.getUnsignedShort(), AccessFlag.PARAMETER_FLAGS);
    }
}