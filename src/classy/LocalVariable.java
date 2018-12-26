package classy;

import classy.instructions.Instruction;

public class LocalVariable {

    public Instruction start;
    public Instruction end;
    public String name;
    public String type;
    public int index;

    public LocalVariable() {

    }

    LocalVariable(PoolItem[] constantPool, Buffer data, Instruction[] instructions) {
        int startIndex = data.getUnsignedShort();
        start = instructions[startIndex];
        int endIndex = startIndex + data.getUnsignedShort();
        end = (endIndex >= instructions.length) ? null : instructions[endIndex];
        name = constantPool[data.getUnsignedShort()].stringValue;
        type = constantPool[data.getUnsignedShort()].stringValue;
        index = data.getUnsignedShort();
    }
}