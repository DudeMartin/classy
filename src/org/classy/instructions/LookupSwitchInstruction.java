package org.classy.instructions;

import java.util.SortedMap;

public class LookupSwitchInstruction extends Instruction {

    public Instruction defaultTarget;
    public SortedMap<Integer, Instruction> targets;

    public LookupSwitchInstruction(Instruction defaultTarget, SortedMap<Integer, Instruction> targets) {
        super(LOOKUPSWITCH);
        this.defaultTarget = defaultTarget;
        this.targets = targets;
    }

    @Override
    public Type getType() {
        return Type.LOOKUP_SWITCH;
    }
}