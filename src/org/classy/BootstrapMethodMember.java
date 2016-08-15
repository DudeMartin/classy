package org.classy;

public class BootstrapMethodMember {

    public boolean interfaceMethod;
    public String owner;
    public String name;
    public String descriptor;
    public Object[][] arguments;

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
        arguments = new Object[argumentCount][2];
        for (int i = 0; i < argumentCount; i++) {
            PoolItem argumentItem = constantPool[data.getUnsignedShort()];
            arguments[i][0] = argumentItem.tag;
            switch (argumentItem.tag) {
                case PoolItem.CONSTANT_String:
                case PoolItem.CONSTANT_Class:
                case PoolItem.CONSTANT_MethodType:
                    arguments[i][1] = constantPool[argumentItem.value].stringValue;
                    break;
                case PoolItem.CONSTANT_Integer:
                    arguments[i][1] = argumentItem.value;
                    break;
                case PoolItem.CONSTANT_Long:
                    arguments[i][1] = argumentItem.longValue;
                    break;
                case PoolItem.CONSTANT_Float:
                case PoolItem.CONSTANT_Double:
                    arguments[i][1] = argumentItem.doubleValue;
                    break;
                case PoolItem.CONSTANT_MethodHandle:
                    arguments[i][1] = new BootstrapMethodMember(constantPool, data);
                    break;
            }
        }
    }
}