package org.classy;

import java.util.Set;

public class InnerClassMember {

    public String name;
    public String outerClassName;
    public String simpleName;
    public Set<AccessFlag> accessFlags;

    public InnerClassMember() {

    }

    InnerClassMember(PoolItem[] constantPool, Buffer data) {
        name = constantPool[constantPool[data.getUnsignedShort()].value].stringValue;
        int outerClassIndex = data.getUnsignedShort();
        outerClassName = (outerClassIndex == 0) ? null : constantPool[constantPool[outerClassIndex].value].stringValue;
        int simpleNameIndex = data.getUnsignedShort();
        simpleName = (simpleNameIndex == 0) ? null : constantPool[simpleNameIndex].stringValue;
        accessFlags = AccessFlag.set(data.getUnsignedShort(), AccessFlag.innerClassFlags);
    }
}