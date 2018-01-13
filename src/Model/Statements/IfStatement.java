package Model.Statements;

import Model.Exceptions.DivisionByZeroException;
import Model.Exceptions.NotExistingException;
import Model.Exceptions.UnknownOperationException;
import Model.ExecutionStack.ExecutionStackInterface;
import Model.Expressions.Expression;
import Model.Heap.HeapInterface;
import Model.ProgramState;
import Model.SymbolTable.SymbolTableInterface;

public class IfStatement implements Statement {
    private Expression ifStatement;
    private Statement thenStatement;
    private Statement elseStatement;

    public IfStatement(Expression ifStm, Statement thenStm, Statement elseStm) {
        ifStatement     = ifStm;
        thenStatement   = thenStm;
        elseStatement   = elseStm;
    }

    @Override
    public ProgramState execute(ProgramState currentState) throws DivisionByZeroException, UnknownOperationException, NotExistingException {
        SymbolTableInterface<String, Integer> symTable = currentState.getSymbolTable();
        ExecutionStackInterface<Statement> stack = currentState.getExecutionStack();
        HeapInterface<Integer, Integer> heap = currentState.getHeap();

        if(0 != ifStatement.eval(symTable, heap)) {
            stack.push(thenStatement);
        }
        else {
            stack.push(elseStatement);
        }

        return null;
    }

    public String toString() {
        return "IF(" + this.ifStatement + ") THEN(" + this.thenStatement + ")ELSE(" + this.elseStatement + ")";
    }
}
