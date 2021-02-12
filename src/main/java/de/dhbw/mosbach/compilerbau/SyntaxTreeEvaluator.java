package de.dhbw.mosbach.compilerbau;

import de.dhbw.mosbach.compilerbau.ast.BinOpNode;
import de.dhbw.mosbach.compilerbau.ast.OperandNode;
import de.dhbw.mosbach.compilerbau.ast.SyntaxNode;
import de.dhbw.mosbach.compilerbau.ast.UnaryOpNode;
import de.dhbw.mosbach.compilerbau.visit.Visitable;
import de.dhbw.mosbach.compilerbau.visit.Visitor;

public class SyntaxTreeEvaluator implements Visitor {

    private int counter;
    public SyntaxTreeEvaluator(){
        this.counter = 1;
    }

    public void placeholder(Visitable root){
        DepthFirstIterator.traverse(root,this);
    }

    @Override
    public void visit (OperandNode node) {

        node.position = counter++;

        if(node.symbol!=null){
            node.nullable = false;
            node.firstpos.add(node.position);
            node.lastpos.add(node.position);
        }
        else{
            node.nullable = true;
            node.firstpos.add(null);
            node.lastpos.add(null);
        }


        //System.out.println(node.symbol +" : "+ node.firstpos + " ," + node.lastpos);

    }

    @Override
    public void visit (BinOpNode node) {

        SyntaxNode left,right;
        left = (SyntaxNode) node.left;
        right = (SyntaxNode) node.right;

        if(node.operator.equals("°")){


            if(left.nullable && right.nullable){
                node.nullable = true;
            }
            else {
                node.nullable = false;
            }

            if(left.nullable){
                node.firstpos.addAll(left.firstpos);
                node.firstpos.addAll(right.firstpos);
            }
            else {
                node.firstpos.addAll(left.firstpos);
            }

            if(right.nullable){
                node.lastpos.addAll(left.lastpos);
                node.lastpos.addAll(right.lastpos);
            }
            else {
                node.lastpos.addAll(right.lastpos);
            }

            //System.out.println(node.operator +" : "+ node.firstpos + " ," + node.lastpos);

        }

        if(node.operator.equals("|")){


            if(left.nullable || right.nullable){
                node.nullable = true;
            }
            else {
                node.nullable = false;
            }

            node.firstpos.addAll(left.firstpos);
            node.firstpos.addAll(right.firstpos);

            node.lastpos.addAll(left.lastpos);
            node.lastpos.addAll(right.lastpos);

            //System.out.println(node.operator +" : "+ node.firstpos + " ," + node.lastpos);

        }
    }

    @Override
    public void visit (UnaryOpNode node) {
        // TODO: 08.02.2021 (...)

        SyntaxNode sub = (SyntaxNode) node.subNode;

        if(node.operator.equals("*")){
            node.nullable = true;
            node.firstpos.addAll(sub.firstpos);
            node.lastpos.addAll(sub.lastpos);
        }
        if(node.operator.equals("+")){
            node.nullable = sub.nullable;
            node.firstpos.addAll(sub.firstpos);
            node.lastpos.addAll(sub.lastpos);
        }
        if(node.operator.equals("?")){
            node.nullable = true;
            node.firstpos.addAll(sub.firstpos);
            node.lastpos.addAll(sub.lastpos);
        }
    }
}
