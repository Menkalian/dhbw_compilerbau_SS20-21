package de.dhbw.mosbach.compilerbau;

import de.dhbw.mosbach.compilerbau.ast.BinOpNode;
import de.dhbw.mosbach.compilerbau.ast.OperandNode;
import de.dhbw.mosbach.compilerbau.ast.SyntaxNode;
import de.dhbw.mosbach.compilerbau.ast.UnaryOpNode;
import de.dhbw.mosbach.compilerbau.visit.FollowposTableEntry;
import de.dhbw.mosbach.compilerbau.visit.Visitable;
import de.dhbw.mosbach.compilerbau.visit.Visitor;

import java.util.SortedMap;
import java.util.TreeMap;

public class FollowPosTableGenerator implements Visitor {
    private final SortedMap<Integer, FollowposTableEntry> followposTableEntries = new TreeMap<>();

    public FollowPosTableGenerator() {
    }

    public SortedMap<Integer, FollowposTableEntry> getFollowposTableEntries() {
        return followposTableEntries;
    }

    public void generate(Visitable root) {
        // clear table entries if they have been generated before (when table generator is used for multiple syntax trees)
        if (this.followposTableEntries.size() > 0) {
            this.followposTableEntries.clear();
        }

        DepthFirstIterator.traverse(root, this);
    }

    @Override
    public void visit(OperandNode node) {
        this.followposTableEntries.put(node.position, new FollowposTableEntry(node.position, node.symbol));
    }

    @Override
    public void visit(BinOpNode node) {
        // only | (alternative) and ° (konkatenation) possible
        // | (alternative) is not relevant
        if (node.operator.equals("°")) {
            if (!(node.left instanceof SyntaxNode) || !(node.right instanceof SyntaxNode)) {
                throw new RuntimeException("Node is not a SyntaxNode.");
            }

            for (int i : ((SyntaxNode) node.left).lastpos) {
                this.followposTableEntries.get(i).followpos.addAll(((SyntaxNode) node.right).firstpos);
            }
        }
    }

    @Override
    public void visit(UnaryOpNode node) {
        // only * (kleene operator), + (positive operator) and ? (option) possible.
        // ? (option) is not relevant
        if (node.operator.equals("*") || node.operator.equals("+")) {
            if (!(node.subNode instanceof SyntaxNode)) {
                throw new RuntimeException("Sub node is not a SyntaxNode.");
            }

            SyntaxNode subNode = (SyntaxNode) node.subNode;

            for (int i : subNode.lastpos) {
                this.followposTableEntries.get(i).followpos.addAll(subNode.firstpos);
            }
        }
    }
}
