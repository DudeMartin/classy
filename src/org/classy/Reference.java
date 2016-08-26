package org.classy;

public final class Reference {

    public enum ReferenceType {

        CLASS,
        FIELD,
        METHOD,
        INTERFACE_METHOD,
        HANDLE,
        TYPE
    }


    public final ReferenceType type;
    public final String owner;
    public final String name;
    public final String descriptor;

    public Reference(ReferenceType type,
                     String owner,
                     String name,
                     String descriptor) {
        this.type = type;
        this.owner = owner;
        this.name = name;
        this.descriptor = descriptor;
    }

    public static String toInternalName(String className) {
        return className.replace('.', '/');
    }
}