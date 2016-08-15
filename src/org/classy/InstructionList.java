package org.classy;

import org.classy.code.Instruction;

import java.util.ArrayList;
import java.util.List;

public class InstructionList extends ArrayList<Instruction> {

    private final MethodMember owner;

    InstructionList(MethodMember owner, List<Instruction> source) {
        this.owner = owner;
        addAll(source);
    }
}