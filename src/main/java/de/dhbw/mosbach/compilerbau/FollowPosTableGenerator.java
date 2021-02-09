package de.dhbw.mosbach.compilerbau;

import de.dhbw.mosbach.compilerbau.ast.BinOpNode;
import de.dhbw.mosbach.compilerbau.ast.OperandNode;
import de.dhbw.mosbach.compilerbau.ast.SyntaxNode;
import de.dhbw.mosbach.compilerbau.ast.UnaryOpNode;
import de.dhbw.mosbach.compilerbau.visit.FollowposTableEntry;
import de.dhbw.mosbach.compilerbau.visit.Visitable;
import de.dhbw.mosbach.compilerbau.visit.Visitor;

import java.util.HashSet;
import java.util.Set;

@SuppressWarnings({"unused", "SameParameterValue"})
public class FollowPosTableGenerator implements Visitor {
    private Set<FollowposTableEntry> followPosTable = new HashSet<>();

    public FollowPosTableGenerator() {
    }

    public Set<FollowposTableEntry> getFollowPosTable() {
        return this.followPosTable;
    }

    public void generate(Visitable root) {
        this.followPosTable = new HashSet<>();
        DepthFirstIterator.traverse(root, this);
    }

    @Override
    public void visit(OperandNode node) {
        this.followPosTable.add(new FollowposTableEntry(node.position, node.symbol));
        // no need to initialize followpos Set because its default is an empty set.
    }

    @Override
    public void visit(BinOpNode node) {
        if (node.operator.equals("*") || node.operator.equals("+")) {
            for (int i : node.lastpos) {
                FollowposTableEntry entryAtPositionI = this.getEntryByPosition(i);
                if (entryAtPositionI == null) {
                    throw new RuntimeException("Could not find FollowposTableEntry with position " + i);
                }

                entryAtPositionI.followpos.addAll(node.firstpos);
            }
        } else if (node.operator.equals("°")) {
            // Konkatenation
            if (!(node.left instanceof SyntaxNode) || !(node.right instanceof SyntaxNode)) {
                throw new RuntimeException("Node is not a SyntaxNode");
            }

            for (int i : ((SyntaxNode) node.left).lastpos) {
                FollowposTableEntry entryAtPositionI = this.getEntryByPosition(i);
                if (entryAtPositionI == null) {
                    throw new RuntimeException("Could not find FollowposTableEntry with position " + i);
                }

                entryAtPositionI.followpos.addAll(((SyntaxNode) node.right).firstpos);
            }
        }
    }

    @Override
    public void visit(UnaryOpNode node) {
        if (!(node.subNode instanceof SyntaxNode)) {
            throw new RuntimeException("Sub node is not a SyntaxNode");
        }

        if (node.operator.equals("*") || node.operator.equals("+")) {
            for (int i : node.lastpos) {
                FollowposTableEntry entryAtPositionI = this.getEntryByPosition(i);
                if (entryAtPositionI == null) {
                    throw new RuntimeException("Could not find FollowposTableEntry with position " + i);
                }

                entryAtPositionI.followpos.addAll(((SyntaxNode) node.subNode).firstpos);
            }
        } else if (node.operator.equals("°")) {
            // Konkatenation

            for (int i : ((SyntaxNode) node.subNode).lastpos) {
                FollowposTableEntry entryAtPositionI = this.getEntryByPosition(i);
                if (entryAtPositionI == null) {
                    throw new RuntimeException("Could not find FollowposTableEntry with position " + i);
                }

                entryAtPositionI.followpos.addAll(((SyntaxNode) node.subNode).firstpos);
            }
        }
    }

    public FollowposTableEntry getEntryByPosition(int pos) {
        for (FollowposTableEntry entry : this.followPosTable) {
            if (entry.position == pos) return entry;
        }

        return null;
    }
}
