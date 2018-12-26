package org.classy.instructions;

import java.util.List;

public class TableSwitchInstruction extends Instruction {

    public int low;
    public int high;
    public Instruction defaultTarget;
    public List<Instruction> targets;

    public TableSwitchInstruction(int low, int high, Instruction defaultTarget, List<Instruction> targets) {
        super(TABLESWITCH);
        this.low = low;
        this.high = high;
        this.defaultTarget = defaultTarget;
        this.targets = targets;
    }

    @Override
    public Type getType() {
        return Type.TABLE_SWITCH;
    }
}