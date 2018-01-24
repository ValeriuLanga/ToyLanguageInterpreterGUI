package Model.Statements;

import Model.Exceptions.DivisionByZeroException;
import Model.Exceptions.UnknownOperationException;
import Model.Expressions.BooleanExpression;
import Model.Expressions.Expression;
import Model.ProgramState;

public class RepeatUntilStatement implements Statement {
    private Expression expression;
    private Statement statement;

    public RepeatUntilStatement(Expression expression, Statement statement) {
        this.expression = expression;
        this.statement = statement;
    }

    @Override
    public ProgramState execute(ProgramState currentState) throws DivisionByZeroException, UnknownOperationException {
        // create the following statement: stmt1;(while(!exp2) stmt1)

        Expression negatedBoolean = ((BooleanExpression)expression).NegateExpression();

        Statement transformedStatement = new CompoundStatement(
                statement,
                new WhileStatement(negatedBoolean,statement)
        );

        // push the new statement on the stack
        currentState.getExecutionStack().push(transformedStatement);
        return null;
    }

    @Override
    public String toString() {
        return "RepeatUntil( " + expression + " ) { "+ statement + " } ;";
    }

}
