package de.dhbw.mosbach.compilerbau;

import de.dhbw.mosbach.compilerbau.ast.BinOpNode;
import de.dhbw.mosbach.compilerbau.ast.OperandNode;
import de.dhbw.mosbach.compilerbau.ast.UnaryOpNode;
import de.dhbw.mosbach.compilerbau.visit.Visitable;

@SuppressWarnings({"unused", "SameParameterValue"})
public class Parser {
    private int position;
    private final String eingabe;

    public Parser (String eingabe) {
        this.eingabe = eingabe;
        position = 0;
    }

    /**
     * Nichtterminal Start<br>
     * - Nur diese Methode ist oeffentlich!!<br>
     * - Nur in dieser Methode auf Eingabeende ueberpruefen !!!
     */
    public Visitable start (Visitable parameter) {
        char inputChar = eingabe.charAt(position);
        if (inputChar == '(') {
            match('(');
            Visitable subTree = regExp(null);
            match(')');
            match('#');
            assertEndOfInput();

            return new BinOpNode("°", subTree, new OperandNode("#"));
        } else if (inputChar == '#') {
            return new OperandNode("#");
        } else {
            throw new RuntimeException("Syntax error!");
        }
    }

    /**
     * Nichtterminal <b>RegExp</b>
     */
    private Visitable regExp (Visitable parameter) {
        char inputChar = eingabe.charAt(position);
        if (Character.isLetterOrDigit(inputChar) || inputChar == '(') {
            return regExpEnde(term(null));
        } else {
            throw new RuntimeException("Syntax error!");
        }
    }

    /**
     * Nichtterminal <b>RE'</b>
     */
    private Visitable regExpEnde (Visitable parameter) {
        char inputChar = eingabe.charAt(position);
        if (inputChar == '|') {
            Visitable expandedTree = new BinOpNode("|", parameter, term(null));
            return regExpEnde(expandedTree);
        } else if (inputChar == ')') {
            return parameter;
        } else {
            throw new RuntimeException("Syntax error!");
        }
    }

    /**
     * Nichtterminal <b>Term</b>
     */
    private Visitable term (Visitable parameter) {
        char inputChar = eingabe.charAt(position);
        if (Character.isLetterOrDigit(inputChar) || inputChar == '(') {
            if (parameter != null) {
                return term(new BinOpNode("°", parameter, factor(null)));
            }

            return term(factor(null));
        } else if (inputChar == '|' || inputChar == ')') {
            return parameter;
        } else {
            throw new RuntimeException("Syntax error!");
        }
    }

    /**
     * Nichtterminal <b>Factor</b>
     */
    private Visitable factor (Visitable parameter) {
        char inputChar = eingabe.charAt(position);
        if (Character.isLetterOrDigit(inputChar) || inputChar == '(') {
            return hOp(elem(null));
        } else {
            throw new RuntimeException("Syntax error!");
        }
    }

    /**
     * Nichtterminal <b>HOp</b>
     */
    private Visitable hOp (Visitable parameter) {
        char inputChar = eingabe.charAt(position);
        if (Character.isLetterOrDigit(inputChar) || inputChar == '(' || inputChar == '|' || inputChar == ')') {
            return parameter;
        } else if (inputChar == '*') {
            match('*');
            return new UnaryOpNode("*", parameter);
        } else if (inputChar == '+') {
            match('+');
            return new UnaryOpNode("+", parameter);
        } else if (inputChar == '?') {
            match('?');
            return new UnaryOpNode("?", parameter);
        } else {
            throw new RuntimeException("Syntax error!");
        }
    }

    /**
     * Nichtterminal <b>Elem</b>
     */
    private Visitable elem (Visitable parameter) {
        char inputChar = eingabe.charAt(position);
        if (Character.isLetterOrDigit(inputChar)) {
            return alphanum(null);
        } else if (inputChar == '(') {
            return regExp(null);
        } else {
            throw new RuntimeException("Syntax error!");
        }
    }

    /**
     * Nichtterminal <b>Alphanum</b>
     */
    private Visitable alphanum (Visitable parameter) {
        char inputChar = eingabe.charAt(position);

        if (!Character.isLetterOrDigit(inputChar)) {
            throw new RuntimeException("An alphanumeric symbol is a letter or a digit");
        }

        OperandNode symbolNode = new OperandNode(String.valueOf(inputChar));
        match(inputChar);

        return symbolNode;
    }

    /**
     * Tests the next symbol in the input matches the given input and consumes it
     *
     * @param symbol
     *         The character that should be next in input
     */
    private void match (char symbol) {
        if ((eingabe == null) || ("".equals(eingabe))) {
            throw new RuntimeException("Syntax error!");
        }
        if (position >= eingabe.length()) {
            throw new RuntimeException("End of input reached!");
        }
        if (eingabe.charAt(position) != symbol) {
            throw new RuntimeException("Syntax error!");
        }
        position++;
    }

    /**
     * 1. wird benoetigt bei der Regel Start -> '(' RegExp ')''#'<br>
     * 2. wird benoetigt bei der Regel Start -> '#'<br>
     * 3. wird sonst bei keiner anderen Regel benoetigt
     */
    private void assertEndOfInput () {
        if (position < eingabe.length()) {
            throw new RuntimeException("No end of input reached!");
        }
    }
}
