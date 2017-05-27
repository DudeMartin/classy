package org.classy.analysis.cfa;

import org.classy.instructions.Instruction;

import java.util.ArrayList;
import java.util.List;

public class BasicBlock {

    public final List<BasicBlock> successors = new ArrayList<BasicBlock>(2);
    public final List<Instruction> instructions = new ArrayList<Instruction>();

    BasicBlock() {

    }
}