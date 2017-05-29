package org.classy.analysis.cfa;

import org.classy.MethodMember;
import org.classy.instructions.Instruction;
import org.classy.instructions.JumpInstruction;
import org.classy.instructions.LookupSwitchInstruction;
import org.classy.instructions.TableSwitchInstruction;

import java.util.List;

public final class BlockGraph {

    private final BasicBlock[] targets;

    public BlockGraph(List<Instruction> instructions) {
        int instructionCount = instructions.size();
        targets = new BasicBlock[instructionCount];
        build(instructions, 0, instructionCount);
    }

    public BlockGraph(MethodMember method) {
        this(method.instructions);
    }

    public BasicBlock graph() {
        return targets[0];
    }

    private BasicBlock build(List<Instruction> instructions, int start, int end) {
        BasicBlock block = targets[start];
        if (block != null) {
            return block;
        }
        block = new BasicBlock();
        targets[start] = block;
        while (start < end) {
            Instruction instruction = instructions.get(start++);
            if (instruction == null) {
                throw new RuntimeException("Missing instruction.");
            }
            block.instructions.add(instruction);
            switch (instruction.getType()) {
                case JUMP:
                    JumpInstruction jumpInstruction = (JumpInstruction) instruction;
                    int targetIndex = checkTarget(instructions, jumpInstruction.target);
                    if (jumpInstruction.isConditional()) {
                        block.successors.add(build(instructions, start, targetIndex));
                    }
                    block.successors.add(build(instructions, targetIndex));
                    break;
                case LOOKUP_SWITCH:
                    LookupSwitchInstruction lookupSwitchInstruction = (LookupSwitchInstruction) instruction;
                    buildSwitchInstruction(instructions, block, lookupSwitchInstruction.targets.values(), lookupSwitchInstruction.defaultTarget);
                    break;
                case TABLE_SWITCH:
                    TableSwitchInstruction tableSwitchInstruction = (TableSwitchInstruction) instruction;
                    buildSwitchInstruction(instructions, block, tableSwitchInstruction.targets, tableSwitchInstruction.defaultTarget);
                    break;
                case NULLARY:
                    int opcode = instruction.getOpcode();
                    if (opcode >= Instruction.IRETURN && opcode <= Instruction.RETURN || opcode == Instruction.ATHROW) {
                        break;
                    }
                default:
                    continue;
            }
            break;
        }
        return block;
    }

    private BasicBlock build(List<Instruction> instructions, int start) {
        return build(instructions, start, instructions.size());
    }

    private void buildSwitchInstruction(List<Instruction> instructions,
                                        BasicBlock block,
                                        Iterable<Instruction> targets,
                                        Instruction defaultTarget) {
        int targetIndex;
        for (Instruction target : targets) {
            targetIndex = checkTarget(instructions, target);
            block.successors.add(build(instructions, targetIndex));
        }
        targetIndex = checkTarget(instructions, defaultTarget);
        block.successors.add(build(instructions, targetIndex));
    }

    private static int checkTarget(List<Instruction> instructions, Instruction target) {
        int index = instructions.indexOf(target);
        if (index == -1) {
            throw new RuntimeException("Missing jump target.");
        }
        return index;
    }
}