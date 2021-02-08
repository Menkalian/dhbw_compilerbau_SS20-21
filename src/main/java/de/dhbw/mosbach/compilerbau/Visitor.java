package de.dhbw.mosbach.compilerbau;

import de.dhbw.mosbach.compilerbau.ast.BinOpNode;
import de.dhbw.mosbach.compilerbau.ast.OperandNode;
import de.dhbw.mosbach.compilerbau.ast.UnaryOpNode;

public interface Visitor {
    void visit (OperandNode node);

    void visit (BinOpNode node);

    void visit (UnaryOpNode node);
}
