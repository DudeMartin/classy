package org.classy;

public class BootstrapMethodMember {

    public boolean interfaceMethod;
    public String owner;
    public String name;
    public String descriptor;
    public Object[] arguments;

    public BootstrapMethodMember() {

    }

    BootstrapMethodMember(PoolItem[] constantPool, Buffer data) {
        PoolItem handleInformation = constantPool[data.getUnsignedShort()];
        interfaceMethod = (handleInformation.value == PoolItem.REF_invokeInterface);
        PoolItem targetInformation = constantPool[(int) handleInformation.longValue];
        owner = constantPool[constantPool[targetInformation.value].value].stringValue;
        PoolItem targetTypeInformation = constantPool[(int) targetInformation.longValue];
        name = constantPool[targetTypeInformation.value].stringValue;
        descriptor = constantPool[(int) targetTypeInformation.longValue].stringValue;
        int argumentCount = data.getUnsignedShort();
        arguments = new Object[argumentCount];
        for (int i = 0; i < argumentCount; i++) {
            arguments[i] = Shared.readConstant(constantPool, data.getUnsignedShort());
        }
    }
}