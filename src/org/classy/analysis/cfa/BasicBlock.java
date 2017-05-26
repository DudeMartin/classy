package org.classy.analysis.cfa;

import org.classy.instructions.Instruction;

import java.util.ArrayList;
import java.util.List;

public class BasicBlock {

    final List<BasicBlock> successors = new ArrayList<BasicBlock>(2);
    final List<Instruction> instructions = new ArrayList<Instruction>();

    BasicBlock() {

    }

    public List<BasicBlock> successors() {
        return successors;
    }

    public List<Instruction> instructions() {
        return instructions;
    }
}