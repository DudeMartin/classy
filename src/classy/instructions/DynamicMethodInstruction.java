package classy.instructions;

import classy.BootstrapMethodMember;

public class DynamicMethodInstruction extends Instruction {

    public BootstrapMethodMember bootstrapMethod;
    public String name;
    public String descriptor;

    public DynamicMethodInstruction(BootstrapMethodMember bootstrapMethod, String name, String descriptor) {
        super(INVOKEDYNAMIC);
        this.bootstrapMethod = bootstrapMethod;
        this.name = name;
        this.descriptor = descriptor;
    }

    @Override
    public Type getType() {
        return Type.DYNAMIC_METHOD;
    }
}