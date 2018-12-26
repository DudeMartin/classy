package classy;

public class CustomAttribute {

    public String name;
    public byte[] data;

    public CustomAttribute() {

    }

    CustomAttribute(Buffer data, String name, int length) {
        this.name = name;
        this.data = data.getBytes(length);
    }
}