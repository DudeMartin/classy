package org.classy;

public class BootstrapMethodMember {

    public Reference reference;
    public Object[] arguments;

    public BootstrapMethodMember() {

    }

    BootstrapMethodMember(PoolItem[] constantPool, Buffer data) {
        reference = (Reference) Shared.readConstant(constantPool, data.getUnsignedShort());
        int argumentCount = data.getUnsignedShort();
        arguments = new Object[argumentCount];
        for (int i = 0; i < argumentCount; i++) {
            arguments[i] = Shared.readConstant(constantPool, data.getUnsignedShort());
        }
    }
}