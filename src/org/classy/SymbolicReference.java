package org.classy;

public class SymbolicReference {

    public enum Type {

        CLASS,
        FIELD,
        METHOD,
        INTERFACE_METHOD,
        HANDLE,
        TYPE
    }

    public Type type;
    public String owner;
    public String name;
    public String descriptor;
    public int kind;
}