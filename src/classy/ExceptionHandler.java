package classy;

import classy.instructions.Instruction;

public class ExceptionHandler {

    public Instruction start;
    public Instruction end;
    public Instruction handler;
    public String type;

    public ExceptionHandler() {

    }

    ExceptionHandler(PoolItem[] constantPool, Buffer data, Instruction[] instructions) {
        start = instructions[data.getUnsignedShort()];
        end = instructions[data.getUnsignedShort()];
        handler = instructions[data.getUnsignedShort()];
        int typeIndex = data.getUnsignedShort();
        type = (typeIndex == 0) ? null : constantPool[constantPool[typeIndex].value].stringValue;
    }
}