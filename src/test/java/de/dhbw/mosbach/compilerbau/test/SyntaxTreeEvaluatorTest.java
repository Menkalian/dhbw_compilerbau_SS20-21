package de.dhbw.mosbach.compilerbau.test;

import de.dhbw.mosbach.compilerbau.Parser;
import de.dhbw.mosbach.compilerbau.SyntaxTreeEvaluator;
import de.dhbw.mosbach.compilerbau.ast.BinOpNode;
import de.dhbw.mosbach.compilerbau.ast.OperandNode;
import de.dhbw.mosbach.compilerbau.visit.Visitable;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class SyntaxTreeEvaluatorTest {


    @Test
    public void test_getFirstPos(){
        SyntaxTreeEvaluator syntaxTreeEvaluator = new SyntaxTreeEvaluator();

        Visitable expectedTree = new OperandNode("D");
        expectedTree = new BinOpNode("|", expectedTree, new OperandNode("H"));
        expectedTree = new BinOpNode("|", expectedTree,
                new BinOpNode("°", new OperandNode("B"), new OperandNode("W"))
        );
        expectedTree = new BinOpNode("°", expectedTree, new OperandNode("#"));

        syntaxTreeEvaluator.placeholder(expectedTree);
    }

}
