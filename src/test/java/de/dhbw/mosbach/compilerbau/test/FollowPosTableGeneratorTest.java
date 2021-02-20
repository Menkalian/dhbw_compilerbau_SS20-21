package de.dhbw.mosbach.compilerbau.test;

import de.dhbw.mosbach.compilerbau.FollowPosTableGenerator;
import de.dhbw.mosbach.compilerbau.ast.BinOpNode;
import de.dhbw.mosbach.compilerbau.ast.OperandNode;
import de.dhbw.mosbach.compilerbau.ast.UnaryOpNode;
import de.dhbw.mosbach.compilerbau.visit.FollowposTableEntry;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

public class FollowPosTableGeneratorTest {
    private FollowPosTableGenerator generator;
    private SortedMap<Integer, FollowposTableEntry> followposTableEntries;

    @BeforeEach
    public void init() {
        this.generator = new FollowPosTableGenerator();
        this.followposTableEntries = generator.getFollowposTableEntries();
    }

    @Test
    public void test01_createsCorrectFollowPosTableEntriesExampleVorlesung() {
        // input: "((a|b)*abb)#" , Vorlesungsunterlagen Kapitel 3, Seite 78

        // construct syntax tree manually:
        OperandNode node1 = new OperandNode("a");
        node1.position = 1;
        node1.firstpos.add(1);
        node1.lastpos.add(1);
        node1.nullable = false;

        OperandNode node2 = new OperandNode("b");
        node2.position = 2;
        node2.firstpos.add(2);
        node2.lastpos.add(2);
        node2.nullable = false;

        BinOpNode altNode = new BinOpNode("|", node1, node2);
        altNode.firstpos.addAll(Arrays.asList(1, 2));
        altNode.lastpos.addAll(Arrays.asList(1, 2));
        altNode.nullable = false;

        UnaryOpNode kleenNode = new UnaryOpNode("*", altNode);
        kleenNode.firstpos.addAll(Arrays.asList(1, 2));
        kleenNode.lastpos.addAll(Arrays.asList(1, 2));
        kleenNode.nullable = true;

        OperandNode node3 = new OperandNode("a");
        node3.position = 3;
        node3.firstpos.add(3);
        node3.lastpos.add(3);
        node3.nullable = false;

        BinOpNode concatNode1 = new BinOpNode("°", kleenNode, node3);
        concatNode1.firstpos.addAll(Arrays.asList(1, 2, 3));
        concatNode1.lastpos.add(3);
        concatNode1.nullable = false;

        OperandNode node4 = new OperandNode("b");
        node4.position = 4;
        node4.firstpos.add(4);
        node4.lastpos.add(4);
        node4.nullable = false;

        BinOpNode concatNode2 = new BinOpNode("°", concatNode1, node4);
        concatNode2.firstpos.addAll(Arrays.asList(1, 2, 3));
        concatNode2.lastpos.add(4);
        concatNode2.nullable = false;

        OperandNode node5 = new OperandNode("b");
        node5.position = 5;
        node5.firstpos.add(5);
        node5.lastpos.add(5);
        node5.nullable = false;

        BinOpNode concatNode3 = new BinOpNode("°", concatNode2, node5);
        concatNode3.firstpos.addAll(Arrays.asList(1, 2, 3));
        concatNode3.lastpos.add(5);
        concatNode3.nullable = false;

        OperandNode node6 = new OperandNode("#");
        node6.position = 6;
        node6.firstpos.add(6);
        node6.lastpos.add(6);
        node6.nullable = false;

        BinOpNode inputSyntaxTree = new BinOpNode("°", concatNode3, node6);
        inputSyntaxTree.firstpos.addAll(Arrays.asList(1, 2, 3));
        inputSyntaxTree.lastpos.add(6);
        inputSyntaxTree.nullable = false;

        // create expected FollowposTable
        FollowposTableEntry row1 = new FollowposTableEntry(1, "a");
        row1.followpos.addAll(Arrays.asList(1, 2, 3));

        FollowposTableEntry row2 = new FollowposTableEntry(2, "b");
        row2.followpos.addAll(Arrays.asList(1, 2, 3));

        FollowposTableEntry row3 = new FollowposTableEntry(3, "a");
        row3.followpos.add(4);

        FollowposTableEntry row4 = new FollowposTableEntry(4, "b");
        row4.followpos.add(5);

        FollowposTableEntry row5 = new FollowposTableEntry(5, "b");
        row5.followpos.add(6);

        FollowposTableEntry row6 = new FollowposTableEntry(6, "#");

        // add rows to SortedMap
        List<FollowposTableEntry> list = Arrays.asList(row1, row2, row3, row4, row5, row6);

        SortedMap<Integer, FollowposTableEntry> expectedTableEntries = new TreeMap<>();

        for (FollowposTableEntry i : list) {
            expectedTableEntries.put(i.position, i);
        }

        // generate actual table
        this.generator.generate(inputSyntaxTree);

        Assertions.assertEquals(expectedTableEntries, this.followposTableEntries);
    }

    @Test
    public void test02_createsCorrectFollowPosTableEntriesExampleSelbstdefiniert() {
        // input: "((dh)+b(w)*)#"
        // construct syntax tree manually:
        OperandNode node1 = new OperandNode("d");
        node1.position = 1;
        node1.firstpos.add(1);
        node1.lastpos.add(1);
        node1.nullable = false;

        OperandNode node2 = new OperandNode("h");
        node2.position = 2;
        node2.firstpos.add(2);
        node2.lastpos.add(2);
        node2.nullable = false;

        BinOpNode concatNode1 = new BinOpNode("°", node1, node2);
        concatNode1.firstpos.add(1);
        concatNode1.lastpos.add(2);
        concatNode1.nullable = false;

        UnaryOpNode positiveNode = new UnaryOpNode("+", concatNode1);
        positiveNode.firstpos.add(1);
        positiveNode.lastpos.add(2);
        positiveNode.nullable = false;

        OperandNode node3 = new OperandNode("b");
        node3.position = 3;
        node3.firstpos.add(3);
        node3.lastpos.add(3);
        node3.nullable = false;

        BinOpNode concatNode2 = new BinOpNode("°", positiveNode, node3);
        concatNode2.firstpos.add(1);
        concatNode2.lastpos.add(3);
        concatNode2.nullable = false;

        OperandNode node4 = new OperandNode("w");
        node4.position = 4;
        node4.firstpos.add(4);
        node4.lastpos.add(4);
        node4.nullable = false;

        UnaryOpNode kleenNode = new UnaryOpNode("*", node4);
        kleenNode.firstpos.add(4);
        kleenNode.lastpos.add(4);
        kleenNode.nullable = true;

        BinOpNode concatNode3 = new BinOpNode("°", concatNode2, kleenNode);
        concatNode3.firstpos.add(1);
        concatNode3.lastpos.addAll(Arrays.asList(3, 4));
        concatNode3.nullable = false;

        OperandNode node5 = new OperandNode("#");
        node5.position = 5;
        node5.firstpos.add(5);
        node5.lastpos.add(5);
        node5.nullable = false;

        BinOpNode inputSyntaxTree = new BinOpNode("°", concatNode3, node5);
        inputSyntaxTree.firstpos.add(1);
        inputSyntaxTree.lastpos.add(5);
        inputSyntaxTree.nullable = false;

        // create expected FollowposTable
        FollowposTableEntry row1 = new FollowposTableEntry(1, "d");
        row1.followpos.add(2);

        FollowposTableEntry row2 = new FollowposTableEntry(2, "h");
        row2.followpos.addAll(Arrays.asList(1, 3));

        FollowposTableEntry row3 = new FollowposTableEntry(3, "b");
        row3.followpos.addAll(Arrays.asList(4, 5));

        FollowposTableEntry row4 = new FollowposTableEntry(4, "w");
        row4.followpos.addAll(Arrays.asList(4, 5));

        FollowposTableEntry row5 = new FollowposTableEntry(5, "#");

        // add rows to SortedMap
        List<FollowposTableEntry> list = Arrays.asList(row1, row2, row3, row4, row5);

        SortedMap<Integer, FollowposTableEntry> expectedTableEntries = new TreeMap<>();

        for (FollowposTableEntry i : list) {
            expectedTableEntries.put(i.position, i);
        }

        // generate actual table
        this.generator.generate(inputSyntaxTree);

        Assertions.assertEquals(expectedTableEntries, this.followposTableEntries);
    }
}
