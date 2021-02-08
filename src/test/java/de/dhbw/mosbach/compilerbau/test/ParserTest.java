package de.dhbw.mosbach.compilerbau.test;

import de.dhbw.mosbach.compilerbau.Parser;
import de.dhbw.mosbach.compilerbau.ast.BinOpNode;
import de.dhbw.mosbach.compilerbau.ast.OperandNode;
import de.dhbw.mosbach.compilerbau.ast.UnaryOpNode;
import de.dhbw.mosbach.compilerbau.util.CompareUtils;
import de.dhbw.mosbach.compilerbau.visit.Visitable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ParserTest {
    @Test
    public void testEmptyRegEx () {
        String input = "#";
        Parser testParser = new Parser(input);

        Visitable expectedTree = new OperandNode("#");
        Visitable actualTree = testParser.start(null);

        Assertions.assertTrue(CompareUtils.astEquals(expectedTree, actualTree));
    }

    @Test
    public void testRegExpWithBrackets () {
        String input = "(b(1)G)#";
        Parser testParser = new Parser(input);

        // Manually build Tree
        Visitable expectedTree = new OperandNode("b");
        expectedTree = new BinOpNode("°", expectedTree, new OperandNode("1"));
        expectedTree = new BinOpNode("°", expectedTree, new OperandNode("G"));
        expectedTree = new BinOpNode("°", expectedTree, new OperandNode("#"));

        // Parse input
        Visitable actualTree = testParser.start(null);

        Assertions.assertTrue(CompareUtils.astEquals(expectedTree, actualTree));
    }

    @Test
    public void testRegExpWithConcatenation () {
        String input = "(aC2)#";
        Parser testParser = new Parser(input);

        // Manually build Tree
        Visitable expectedTree = new OperandNode("a");
        expectedTree = new BinOpNode("°", expectedTree, new OperandNode("C"));
        expectedTree = new BinOpNode("°", expectedTree, new OperandNode("2"));
        expectedTree = new BinOpNode("°", expectedTree, new OperandNode("#"));

        // Parse input
        Visitable actualTree = testParser.start(null);

        Assertions.assertTrue(CompareUtils.astEquals(expectedTree, actualTree));
    }

    @Test
    public void testRegExpWithAlternative () {
        String input = "(D|H|BW)#";
        Parser testParser = new Parser(input);

        // Manually build Tree
        Visitable expectedTree = new OperandNode("D");
        expectedTree = new BinOpNode("|", expectedTree, new OperandNode("H"));
        expectedTree = new BinOpNode("|", expectedTree,
                                     new BinOpNode("°", new OperandNode("B"), new OperandNode("W"))
        );
        expectedTree = new BinOpNode("°", expectedTree, new OperandNode("#"));

        // Parse input
        Visitable actualTree = testParser.start(null);

        Assertions.assertTrue(CompareUtils.astEquals(expectedTree, actualTree));
    }

    @Test
    public void testRegExpWithKleenHull () {
        String input = "(AHA*b*)#";
        Parser testParser = new Parser(input);

        // Manually build Tree
        Visitable expectedTree = new OperandNode("A");
        expectedTree = new BinOpNode("°", expectedTree, new OperandNode("H"));
        expectedTree = new BinOpNode("°", expectedTree, new UnaryOpNode("*", new OperandNode("A")));
        expectedTree = new BinOpNode("°", expectedTree, new UnaryOpNode("*", new OperandNode("b")));
        expectedTree = new BinOpNode("°", expectedTree, new OperandNode("#"));

        // Parse input
        Visitable actualTree = testParser.start(null);

        Assertions.assertTrue(CompareUtils.astEquals(expectedTree, actualTree));
    }

    @Test
    public void testRegExpWithPositiveHull () {
        String input = "(IN+F19+B)#";
        Parser testParser = new Parser(input);

        // Manually build Tree
        Visitable expectedTree = new OperandNode("I");
        expectedTree = new BinOpNode("°", expectedTree, new UnaryOpNode("+", new OperandNode("N")));
        expectedTree = new BinOpNode("°", expectedTree, new OperandNode("F"));
        expectedTree = new BinOpNode("°", expectedTree, new OperandNode("1"));
        expectedTree = new BinOpNode("°", expectedTree, new UnaryOpNode("+", new OperandNode("9")));
        expectedTree = new BinOpNode("°", expectedTree, new OperandNode("B"));
        expectedTree = new BinOpNode("°", expectedTree, new OperandNode("#"));

        // Parse input
        Visitable actualTree = testParser.start(null);

        Assertions.assertTrue(CompareUtils.astEquals(expectedTree, actualTree));
    }


    @Test
    public void testRegExpWithOption () {
        String input = "(OTT?O)#";
        Parser testParser = new Parser(input);

        // Manually build Tree
        Visitable expectedTree = new OperandNode("O");
        expectedTree = new BinOpNode("°", expectedTree, new OperandNode("T"));
        expectedTree = new BinOpNode("°", expectedTree, new UnaryOpNode("?", new OperandNode("T")));
        expectedTree = new BinOpNode("°", expectedTree, new OperandNode("O"));
        expectedTree = new BinOpNode("°", expectedTree, new OperandNode("#"));

        // Parse input
        Visitable actualTree = testParser.start(null);

        Assertions.assertTrue(CompareUtils.astEquals(expectedTree, actualTree));
    }

    @Test
    public void testRegExpWithAllOperators () {
        String input = "((T|D)e+s(d?)t1*02)#";
        Parser testParser = new Parser(input);

        // Manually build Tree
        Visitable expectedTree = new BinOpNode("|", new OperandNode("T"), new OperandNode("D"));
        expectedTree = new BinOpNode("°", expectedTree, new UnaryOpNode("+", new OperandNode("e")));
        expectedTree = new BinOpNode("°", expectedTree, new OperandNode("s"));
        expectedTree = new BinOpNode("°", expectedTree, new UnaryOpNode("?", new OperandNode("d")));
        expectedTree = new BinOpNode("°", expectedTree, new OperandNode("t"));
        expectedTree = new BinOpNode("°", expectedTree, new UnaryOpNode("*", new OperandNode("1")));
        expectedTree = new BinOpNode("°", expectedTree, new OperandNode("0"));
        expectedTree = new BinOpNode("°", expectedTree, new OperandNode("2"));
        expectedTree = new BinOpNode("°", expectedTree, new OperandNode("#"));

        // Parse input
        Visitable actualTree = testParser.start(null);

        Assertions.assertTrue(CompareUtils.astEquals(expectedTree, actualTree));
    }

    @Test
    public void testInvalidExpressionWithInputAfterHash () {
        String input = "(soivo)#oi?";
        Parser testParser = new Parser(input);
        Assertions.assertThrows(RuntimeException.class, () -> testParser.start(null));
    }

    @Test
    public void testNullInputIsInvalid () {
        Parser testParser = new Parser(null);
        Assertions.assertThrows(RuntimeException.class, () -> testParser.start(null));
    }

    @ParameterizedTest(name = "#{index} - Test SyntaxError with \"{0}\"")
    @MethodSource("inputProvider")
    public void testInvalidExpressionWithSyntaxErrors (String input) {
        Parser testParser = new Parser(input);
        Assertions.assertThrows(RuntimeException.class, () -> testParser.start(null));
    }

    private List<String> inputProvider () {
        return List.of(
                // Empty input
                "",
                // Invalid Characters after different operators/operands
                "(%def)#",
                "(in(f)%)#",
                "(te~st)#",
                "(a|[bc])#",
                "(no*}42)#",
                "(69+=BieG)#",
                "(71'?01)#",
                "(5EB`)#",
                "(!)#",
                // Missing Characters
                "(adf)",
                "(bc#",
                "zu)#",
                "(D34+8(doi*?)#",
                // Missplaced Characters
                "(Ab3l)1#"
        );
    }
}
