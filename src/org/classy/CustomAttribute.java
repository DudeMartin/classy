package org.classy;

public class CustomAttribute {

    public String name;
    public int length;
    public byte[] data;

    public CustomAttribute() {

    }

    CustomAttribute(Buffer data, String name, int length) {
        this.name = name;
        this.length = length;
        this.data = data.getBytes(length);
    }
}