package de.dhbw.mosbach.compilerbau.ast;

import de.dhbw.mosbach.compilerbau.visit.Visitable;
import de.dhbw.mosbach.compilerbau.visit.Visitor;

public class OperandNode extends SyntaxNode implements Visitable {
    public int position;
    public String symbol;

    public OperandNode (String symbol) {
        position = -1;
        this.symbol = symbol;
    }

    @Override
    public void accept (Visitor visitor) {
        visitor.visit(this);
    }
}
