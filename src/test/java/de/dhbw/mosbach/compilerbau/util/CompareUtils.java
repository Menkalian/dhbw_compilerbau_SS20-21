package de.dhbw.mosbach.compilerbau.util;

import de.dhbw.mosbach.compilerbau.Visitable;
import de.dhbw.mosbach.compilerbau.ast.BinOpNode;
import de.dhbw.mosbach.compilerbau.ast.OperandNode;
import de.dhbw.mosbach.compilerbau.ast.UnaryOpNode;

public class CompareUtils {
    public static boolean astEquals (Visitable v1, Visitable v2) {
        if (v1 == v2)
            return true;

        if (v1 == null)
            return false;

        if (v2 == null)
            return false;

        if (v1.getClass() != v2.getClass())
            return false;

        if (v1.getClass() == OperandNode.class) {
            OperandNode op1 = (OperandNode) v1;
            OperandNode op2 = (OperandNode) v2;
            return op1.position == op2.position && op1.symbol.equals(op2.symbol);
        }

        if (v1.getClass() == UnaryOpNode.class) {
            UnaryOpNode op1 = (UnaryOpNode) v1;
            UnaryOpNode op2 = (UnaryOpNode) v2;
            return op1.operator.equals(op2.operator) && astEquals(op1.subNode, op2.subNode);
        }

        if (v1.getClass() == BinOpNode.class) {
            BinOpNode op1 = (BinOpNode) v1;
            BinOpNode op2 = (BinOpNode) v2;
            return op1.operator.equals(op2.operator) &&
                   astEquals(op1.left, op2.left) &&
                   astEquals(op1.right, op2.right);
        }

        throw new IllegalStateException("Ungueltiger Knotentyp");
    }
}
