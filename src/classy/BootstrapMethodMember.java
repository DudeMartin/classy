package classy;

public class BootstrapMethodMember {

    public SymbolicReference reference;
    public Object[] arguments;

    public BootstrapMethodMember() {

    }

    BootstrapMethodMember(PoolItem[] constantPool, Buffer data) {
        reference = (SymbolicReference) Shared.readConstant(constantPool, data.getUnsignedShort());
        int count = data.getUnsignedShort();
        arguments = new Object[count];
        for (int i = 0; i < count; i++) {
            arguments[i] = Shared.readConstant(constantPool, data.getUnsignedShort());
        }
    }
}