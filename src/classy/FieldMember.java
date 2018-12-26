package classy;

import java.util.ArrayList;
import java.util.Set;

public class FieldMember extends ClassMember {

    public Object constantValue;

    public FieldMember() {

    }

    FieldMember(ClassFile ownerClass, Buffer data) {
        super(ownerClass, data);
    }

    protected Set<AccessFlag> sourceAccessFlags() {
        return AccessFlag.FIELD_FLAGS;
    }

    protected void readAttribute(ClassFile ownerClass, Buffer data, String name, int length) {
        if ("ConstantValue".equals(name)) {
            constantValue = Shared.readConstant(ownerClass.constantPool, data.getUnsignedShort());
        } else {
            if (customAttributes == null) {
                customAttributes = new ArrayList<CustomAttribute>(1);
            }
            customAttributes.add(new CustomAttribute(data, name, length));
        }
    }
}