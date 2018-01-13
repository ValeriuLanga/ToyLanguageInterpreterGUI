package Model.Statements;

import Model.Exceptions.DivisionByZeroException;
import Model.Exceptions.UnknownOperationException;
import Model.Expressions.Expression;
import Model.ProgramState;

public class WhileStatement implements Statement {

    private Expression expression;
    private Statement statement;

    public WhileStatement(Expression expression, Statement statement) {
        this.expression = expression;
        this.statement = statement;
    }

    @Override
    public ProgramState execute(ProgramState currentState) throws DivisionByZeroException, UnknownOperationException {
        int value = expression.eval(currentState.getSymbolTable(), currentState.getHeap());

        if(value != 0) {  // i.e. we can still go on
            // push both the while statement and the contained statement
            currentState.getExecutionStack().push(this);
            currentState.getExecutionStack().push(statement);
        }

        return null;
    }

    @Override
    public String toString() {
        return "While( " + expression + " ) { "+ statement + " } ;";
    }
}
