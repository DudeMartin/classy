package org.classy.analysis.cfa;

import org.classy.instructions.Instruction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BasicBlock {

    final Collection<BasicBlock> successors = new ArrayList<BasicBlock>(2);
    final List<Instruction> instructions = new ArrayList<Instruction>();

    BasicBlock() {

    }

    public Iterable<BasicBlock> successors() {
        return successors;
    }

    public Iterable<Instruction> instructions() {
        return instructions;
    }
}