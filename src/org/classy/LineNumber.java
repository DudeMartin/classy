package org.classy;

import org.classy.instructions.Instruction;

public class LineNumber {

    public Instruction start;
    public int number;

    public LineNumber() {

    }

    LineNumber(Buffer data, Instruction[] instructions) {
        start = instructions[data.getUnsignedShort()];
        number = data.getUnsignedShort();
    }
}