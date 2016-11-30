package org.classy;

public class Reference {

    public enum ReferenceType {

        CLASS,
        FIELD,
        METHOD,
        INTERFACE_METHOD,
        HANDLE,
        TYPE
    }

    public ReferenceType type;
    public String owner;
    public String name;
    public String descriptor;
    public int kind;

    public Reference() {

    }
}