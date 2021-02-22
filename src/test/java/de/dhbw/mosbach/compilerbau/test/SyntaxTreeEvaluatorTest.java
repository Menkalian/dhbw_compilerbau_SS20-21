package de.dhbw.mosbach.compilerbau.test;


import de.dhbw.mosbach.compilerbau.SyntaxTreeEvaluator;
import de.dhbw.mosbach.compilerbau.ast.BinOpNode;
import de.dhbw.mosbach.compilerbau.ast.OperandNode;
import de.dhbw.mosbach.compilerbau.ast.UnaryOpNode;
import de.dhbw.mosbach.compilerbau.visit.Visitable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class SyntaxTreeEvaluatorTest {


    @Test
    public void testSyntaxTreeFromLecture(){
        // input: ((a|b)*abb)#
        //actualTree
        OperandNode node1 = new OperandNode("a");

        OperandNode node2 = new OperandNode("b");

        BinOpNode altNode = new BinOpNode("|", node1, node2);

        UnaryOpNode kleenNode = new UnaryOpNode("*", altNode);

        OperandNode node3 = new OperandNode("a");

        BinOpNode concatNode1 = new BinOpNode("°", kleenNode, node3);

        OperandNode node4 = new OperandNode("b");

        BinOpNode concatNode2 = new BinOpNode("°", concatNode1, node4);

        OperandNode node5 = new OperandNode("b");

        BinOpNode concatNode3 = new BinOpNode("°", concatNode2, node5);

        OperandNode node6 = new OperandNode("#");

        BinOpNode inputSyntaxTree = new BinOpNode("°", concatNode3, node6);


        //expectedTree
        node1 = new OperandNode("a");
        node1.position = 1;
        node1.firstpos.add(1);
        node1.lastpos.add(1);
        node1.nullable = false;

        node2 = new OperandNode("b");
        node2.position = 2;
        node2.firstpos.add(2);
        node2.lastpos.add(2);
        node2.nullable = false;

        altNode = new BinOpNode("|", node1, node2);
        altNode.firstpos.addAll(Arrays.asList(1, 2));
        altNode.lastpos.addAll(Arrays.asList(1, 2));
        altNode.nullable = false;

        kleenNode = new UnaryOpNode("*", altNode);
        kleenNode.firstpos.addAll(Arrays.asList(1, 2));
        kleenNode.lastpos.addAll(Arrays.asList(1, 2));
        kleenNode.nullable = true;

        node3 = new OperandNode("a");
        node3.position = 3;
        node3.firstpos.add(3);
        node3.lastpos.add(3);
        node3.nullable = false;

        concatNode1 = new BinOpNode("°", kleenNode, node3);
        concatNode1.firstpos.addAll(Arrays.asList(1, 2, 3));
        concatNode1.lastpos.add(3);
        concatNode1.nullable = false;

        node4 = new OperandNode("b");
        node4.position = 4;
        node4.firstpos.add(4);
        node4.lastpos.add(4);
        node4.nullable = false;

        concatNode2 = new BinOpNode("°", concatNode1, node4);
        concatNode2.firstpos.addAll(Arrays.asList(1, 2, 3));
        concatNode2.lastpos.add(4);
        concatNode2.nullable = false;

        node5 = new OperandNode("b");
        node5.position = 5;
        node5.firstpos.add(5);
        node5.lastpos.add(5);
        node5.nullable = false;

        concatNode3 = new BinOpNode("°", concatNode2, node5);
        concatNode3.firstpos.addAll(Arrays.asList(1, 2, 3));
        concatNode3.lastpos.add(5);
        concatNode3.nullable = false;

        node6 = new OperandNode("#");
        node6.position = 6;
        node6.firstpos.add(6);
        node6.lastpos.add(6);
        node6.nullable = false;

        BinOpNode expectedSyntaxTree = new BinOpNode("°", concatNode3, node6);
        expectedSyntaxTree.firstpos.addAll(Arrays.asList(1, 2, 3));
        expectedSyntaxTree.lastpos.add(6);
        expectedSyntaxTree.nullable = false;


        SyntaxTreeEvaluator syntaxTreeEvaluator = new SyntaxTreeEvaluator();
        syntaxTreeEvaluator.start(inputSyntaxTree);

        Assertions.assertTrue(equals(expectedSyntaxTree,inputSyntaxTree));
    }

    @Test
    public void testSyntaxTreeWithConcatenation(){
        //input: (abc)#

        //actualTree
        OperandNode node1 = new OperandNode("a");
        OperandNode node2 = new OperandNode("b");

        BinOpNode concatNode1 = new BinOpNode("°",node1,node2);

        OperandNode node3 = new OperandNode("c");

        BinOpNode concatNode2 = new BinOpNode("°",concatNode1,node3);

        OperandNode node4 = new OperandNode("#");

        BinOpNode inputSyntaxTree = new BinOpNode("°",concatNode2,node4);

        //expectedTree
        node1 = new OperandNode("a");
        node1.position = 1;
        node1.firstpos.add(1);
        node1.lastpos.add(1);
        node1.nullable = false;

        node2 = new OperandNode("b");
        node2.position = 2;
        node2.firstpos.add(2);
        node2.lastpos.add(2);
        node2.nullable = false;

        concatNode1 = new BinOpNode("°",node1,node2);
        concatNode1.firstpos.add(1);
        concatNode1.lastpos.add(2);
        concatNode1.nullable = false;

        node3 = new OperandNode("c");
        node3.position = 3;
        node3.firstpos.add(3);
        node3.lastpos.add(3);
        node3.nullable = false;

        concatNode2 = new BinOpNode("°",concatNode1,node3);
        concatNode2.firstpos.add(1);
        concatNode2.lastpos.add(3);
        concatNode2.nullable = false;


        node4 = new OperandNode("#");
        node4.position = 4;
        node4.firstpos.add(4);
        node4.lastpos.add(4);
        node4.nullable = false;

        BinOpNode expectedSyntaxTree = new BinOpNode("°",concatNode2,node4);
        expectedSyntaxTree.firstpos.add(1);
        expectedSyntaxTree.lastpos.add(4);
        expectedSyntaxTree.nullable = false;

        SyntaxTreeEvaluator syntaxTreeEvaluator = new SyntaxTreeEvaluator();
        syntaxTreeEvaluator.start(inputSyntaxTree);

        Assertions.assertTrue(equals(expectedSyntaxTree,inputSyntaxTree));

    }

    @Test
    public void testSyntaxTreeWithAlternative(){
        //input: (ab|c)#

        //actualTree
        OperandNode node1 = new OperandNode("a");

        OperandNode node2 = new OperandNode("b");

        BinOpNode concatNode1 = new BinOpNode("°",node1,node2);

        OperandNode node3 = new OperandNode("c");

        BinOpNode alternativeNode = new BinOpNode("|",concatNode1,node3);

        OperandNode node4 = new OperandNode("#");

        BinOpNode inputSyntaxTree = new BinOpNode("°",alternativeNode,node4);


        //expectedTree
        node1 = new OperandNode("a");
        node1.position = 1;
        node1.firstpos.add(1);
        node1.lastpos.add(1);
        node1.nullable = false;

        node2 = new OperandNode("b");
        node2.position = 2;
        node2.firstpos.add(2);
        node2.lastpos.add(2);
        node2.nullable = false;

        concatNode1 = new BinOpNode("°",node1,node2);
        concatNode1.firstpos.add(1);
        concatNode1.lastpos.add(2);
        concatNode1.nullable = false;

        node3 = new OperandNode("c");
        node3.position = 3;
        node3.firstpos.add(3);
        node3.lastpos.add(3);
        node3.nullable = false;

        alternativeNode = new BinOpNode("|",concatNode1,node3);
        alternativeNode.firstpos.addAll(Arrays.asList(1,3));
        alternativeNode.lastpos.addAll(Arrays.asList(2,3));
        alternativeNode.nullable = false;

        node4 = new OperandNode("#");
        node4.position = 4;
        node4.firstpos.add(4);
        node4.lastpos.add(4);
        node4.nullable = false;

        BinOpNode expectedSyntaxTree = new BinOpNode("°",alternativeNode,node4);
        expectedSyntaxTree.firstpos.addAll(Arrays.asList(1,3));
        expectedSyntaxTree.lastpos.add(4);
        expectedSyntaxTree.nullable = false;


        SyntaxTreeEvaluator syntaxTreeEvaluator = new SyntaxTreeEvaluator();
        syntaxTreeEvaluator.start(inputSyntaxTree);

        Assertions.assertTrue(equals(expectedSyntaxTree,inputSyntaxTree));

    }

    @Test
    public void testSyntaxTreeWithKleenHull(){
        //input: ((ab)*c)#

        //actualTree
        OperandNode node1 = new OperandNode("a");

        OperandNode node2 = new OperandNode("b");

        BinOpNode concatNode1 = new BinOpNode("°",node1,node2);

        UnaryOpNode kleenNode = new UnaryOpNode("*",concatNode1);

        OperandNode node3 = new OperandNode("c");

        BinOpNode concatNode2 = new BinOpNode("°",kleenNode,node3);

        OperandNode node4 = new OperandNode("#");

        BinOpNode inputSyntaxTree = new BinOpNode("°",concatNode2,node4);


        //expectedTree
        node1 = new OperandNode("a");
        node1.position = 1;
        node1.firstpos.add(1);
        node1.lastpos.add(1);
        node1.nullable = false;

        node2 = new OperandNode("b");
        node2.position = 2;
        node2.firstpos.add(2);
        node2.lastpos.add(2);
        node2.nullable = false;

        concatNode1 = new BinOpNode("°",node1,node2);
        concatNode1.firstpos.add(1);
        concatNode1.lastpos.add(2);
        concatNode1.nullable = false;

        kleenNode = new UnaryOpNode("*",concatNode1);
        kleenNode.firstpos.add(1);
        kleenNode.lastpos.add(2);
        kleenNode.nullable = true;

        node3 = new OperandNode("c");
        node3.position = 3;
        node3.firstpos.add(3);
        node3.lastpos.add(3);
        node3.nullable = false;

        concatNode2 = new BinOpNode("°",kleenNode,node3);
        concatNode2.firstpos.addAll(Arrays.asList(1,3));
        concatNode2.lastpos.add(3);
        concatNode2.nullable = false;

        node4 = new OperandNode("#");
        node4.position = 4;
        node4.firstpos.add(4);
        node4.lastpos.add(4);
        node4.nullable = false;

        BinOpNode expectedSyntaxTree = new BinOpNode("°",concatNode2,node4);
        expectedSyntaxTree.firstpos.addAll(Arrays.asList(1,3));
        expectedSyntaxTree.lastpos.add(4);
        expectedSyntaxTree.nullable = false;


        SyntaxTreeEvaluator syntaxTreeEvaluator = new SyntaxTreeEvaluator();
        syntaxTreeEvaluator.start(inputSyntaxTree);

        Assertions.assertTrue(equals(expectedSyntaxTree,inputSyntaxTree));
    }

    @Test
    public void testSyntaxTreeWithPositiveHull(){
        //input: (ab+c)#

        //actualTree
        OperandNode node1 = new OperandNode("a");

        OperandNode node2 = new OperandNode("b");

        BinOpNode concatNode1 = new BinOpNode("°",node1,node2);

        UnaryOpNode positiveNode = new UnaryOpNode("+",concatNode1);

        OperandNode node3 = new OperandNode("c");

        BinOpNode concatNode2 = new BinOpNode("°",positiveNode,node3);

        OperandNode node4 = new OperandNode("#");

        BinOpNode inputSyntaxTree = new BinOpNode("°",concatNode2,node4);


        //expectedTree
        node1 = new OperandNode("a");
        node1.position = 1;
        node1.firstpos.add(1);
        node1.lastpos.add(1);
        node1.nullable = false;

        node2 = new OperandNode("b");
        node2.position = 2;
        node2.firstpos.add(2);
        node2.lastpos.add(2);
        node2.nullable = false;

        concatNode1 = new BinOpNode("°",node1,node2);
        concatNode1.firstpos.add(1);
        concatNode1.lastpos.add(2);
        concatNode1.nullable = false;

        positiveNode = new UnaryOpNode("+",concatNode1);
        positiveNode.firstpos.add(1);
        positiveNode.lastpos.add(2);
        positiveNode.nullable = false;

        node3 = new OperandNode("c");
        node3.position = 3;
        node3.firstpos.add(3);
        node3.lastpos.add(3);
        node3.nullable = false;

        concatNode2 = new BinOpNode("°",positiveNode,node3);
        concatNode2.firstpos.add(1);
        concatNode2.lastpos.add(3);
        concatNode2.nullable = false;

        node4 = new OperandNode("#");
        node4.position = 4;
        node4.firstpos.add(4);
        node4.lastpos.add(4);
        node4.nullable = false;

        BinOpNode expectedSyntaxTree = new BinOpNode("°",concatNode2,node4);
        expectedSyntaxTree.firstpos.add(1);
        expectedSyntaxTree.lastpos.add(4);
        expectedSyntaxTree.nullable = false;

        SyntaxTreeEvaluator syntaxTreeEvaluator = new SyntaxTreeEvaluator();
        syntaxTreeEvaluator.start(inputSyntaxTree);

        Assertions.assertTrue(equals(expectedSyntaxTree,inputSyntaxTree));

    }

    @Test
    public void testSyntaxTreeWithOption(){
        //input: (ab?c)#

        //actualTree
        OperandNode node1 = new OperandNode("a");

        OperandNode node2 = new OperandNode("b");

        BinOpNode concatNode1 = new BinOpNode("°",node1,node2);

        UnaryOpNode optionNode = new UnaryOpNode("?",concatNode1);

        OperandNode node3 = new OperandNode("c");

        BinOpNode concatNode2 = new BinOpNode("°",optionNode,node3);

        OperandNode node4 = new OperandNode("#");

        BinOpNode inputSyntaxTree = new BinOpNode("°",concatNode2,node4);


        //expectedTree
        node1 = new OperandNode("a");
        node1.position = 1;
        node1.firstpos.add(1);
        node1.lastpos.add(1);
        node1.nullable = false;

        node2 = new OperandNode("b");
        node2.position = 2;
        node2.firstpos.add(2);
        node2.lastpos.add(2);
        node2.nullable = false;

        concatNode1 = new BinOpNode("°",node1,node2);
        concatNode1.firstpos.add(1);
        concatNode1.lastpos.add(2);
        concatNode1.nullable = false;

        optionNode = new UnaryOpNode("?",concatNode1);
        optionNode.firstpos.add(1);
        optionNode.lastpos.add(2);
        optionNode.nullable = true;

        node3 = new OperandNode("c");
        node3.position = 3;
        node3.firstpos.add(3);
        node3.lastpos.add(3);
        node3.nullable = false;

        concatNode2 = new BinOpNode("°",optionNode,node3);
        concatNode2.firstpos.addAll(Arrays.asList(1,3));
        concatNode2.lastpos.add(3);
        concatNode2.nullable = false;

        node4 = new OperandNode("#");
        node4.position = 4;
        node4.firstpos.add(4);
        node4.lastpos.add(4);
        node4.nullable = false;

        BinOpNode expectedSyntaxTree = new BinOpNode("°",concatNode2,node4);
        expectedSyntaxTree.firstpos.addAll(Arrays.asList(1,3));
        expectedSyntaxTree.lastpos.add(4);
        expectedSyntaxTree.nullable = false;


        SyntaxTreeEvaluator syntaxTreeEvaluator = new SyntaxTreeEvaluator();
        syntaxTreeEvaluator.start(inputSyntaxTree);

        Assertions.assertTrue(equals(expectedSyntaxTree,inputSyntaxTree));
    }



    private boolean equals(Visitable expected, Visitable visited)
    {
        if (expected == null && visited == null) return true;
        if (expected == null || visited == null) return false;
        if (expected.getClass() != visited.getClass()) return false;
        if (expected.getClass() == BinOpNode.class)
        {
            BinOpNode op1 = (BinOpNode) expected;
            BinOpNode op2 = (BinOpNode) visited;
            return op1.nullable.equals(op2.nullable) &&
                    op1.firstpos.equals(op2.firstpos) &&
                    op1.lastpos.equals(op2.lastpos) &&
                    equals(op1.left, op2.left) &&
                    equals(op1.right, op2.right);
        }
        if (expected.getClass() == UnaryOpNode.class)
        {
            UnaryOpNode op1 = (UnaryOpNode) expected;
            UnaryOpNode op2 = (UnaryOpNode) visited;
            return op1.nullable.equals(op2.nullable) &&
                    op1.firstpos.equals(op2.firstpos) &&
                    op1.lastpos.equals(op2.lastpos) &&
                    equals(op1.subNode, op2.subNode);
        }
        if (expected.getClass() == OperandNode.class)
        {
            OperandNode op1 = (OperandNode) expected;
            OperandNode op2 = (OperandNode) visited;
            return op1.nullable.equals(op2.nullable) &&
                    op1.firstpos.equals(op2.firstpos) &&
                    op1.lastpos.equals(op2.lastpos);
        }
        throw new IllegalStateException(
                String.format( "Beide Wurzelknoten sind Instanzen der Klasse %1$s !"
                + " Dies ist nicht erlaubt!",
                expected.getClass().getSimpleName())
        );
    }


}
