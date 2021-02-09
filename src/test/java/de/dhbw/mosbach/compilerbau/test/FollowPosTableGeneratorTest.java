package de.dhbw.mosbach.compilerbau.test;

import de.dhbw.mosbach.compilerbau.FollowPosTableGenerator;
import de.dhbw.mosbach.compilerbau.ast.OperandNode;
import de.dhbw.mosbach.compilerbau.visit.FollowposTableEntry;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Set;

public class FollowPosTableGeneratorTest {
    private FollowPosTableGenerator generator;
    private Set<FollowposTableEntry> followPosTable;

    @BeforeEach
    public void init() {
        this.generator = new FollowPosTableGenerator();
        this.followPosTable = generator.getFollowPosTable();
    }

    @Test
    public void test01_getsCorrectEntryByPosition() {
        FollowposTableEntry expectedEntry = new FollowposTableEntry(2, "*");

        Assertions.assertNull(this.generator.getEntryByPosition(1));

        this.generator.getFollowPosTable().addAll(Arrays.asList(
                new FollowposTableEntry(1, "+"),
                expectedEntry,
                new FollowposTableEntry(3, "°")));

        FollowposTableEntry getEntry = this.generator.getEntryByPosition(2);
        Assertions.assertNotNull(getEntry);
        Assertions.assertEquals(expectedEntry, getEntry);
    }

    @Test
    public void test02_createsNewEntryWhenVisitingOperandNode() {
        OperandNode node = new OperandNode("a");
        node.position = 1;

        this.generator.visit(node);
        Assertions.assertEquals(1, this.followPosTable.size());

        FollowposTableEntry newEntry = this.generator.getEntryByPosition(1);
        Assertions.assertNotNull(newEntry);
        Assertions.assertEquals(newEntry.symbol, "a");
        Assertions.assertEquals(newEntry.position, 1);
        Assertions.assertTrue(newEntry.followpos.isEmpty());
    }

//    @Test
//    public void test00_followPositionsAreStoredCorrectly() {
//        String input = "(a|b)*abb#"; // Vorlesungsunterlagen Kapitel 3, Seite 78
//
//        // construct syntax tree manually:
//        Visitable tree = new OperandNode("a");
//        tree = new BinOpNode("*", tree, new OperandNode("b"));
//        tree = new BinOpNode("°", tree, new OperandNode("a"));
//        tree = new BinOpNode("°", tree, new OperandNode("b"));
//        tree = new BinOpNode("°", tree, new OperandNode("b"));
//        tree = new BinOpNode("°", tree, new OperandNode("#"));
//
//        // generate FollowPosTable
//        this.generator.generate(tree);
//
//        FollowposTableEntry entry1 = this.generator.getEntryByPosition(1);
//        Assertions.assertNotNull(1);
//
//    }
}
