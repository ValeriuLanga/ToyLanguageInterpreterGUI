package Model.Statements;

import Model.Exceptions.DivisionByZeroException;
import Model.Exceptions.UnknownOperationException;
import Model.Expressions.Expression;
import Model.ProgramState;


//
//  Written during seminary 11, on 11.jan.2018
//

public class ForStatement implements Statement {
    private Statement initialStatement;
    private Statement stepStatement;
    private Statement bodyStatement;
    private Expression condition;

    public ForStatement(Statement initialStatement, Expression condition, Statement stepStatement, Statement bodyStatement) {
        this.initialStatement = initialStatement;
        this.stepStatement = stepStatement;
        this.bodyStatement = bodyStatement;
        this.condition = condition;
    }

    @Override
    public ProgramState execute(ProgramState currentState) throws DivisionByZeroException, UnknownOperationException {
        // we transform the For Statement into a while
        Statement newStatement = new CompoundStatement(
              initialStatement,
              new WhileStatement(condition,
                      new CompoundStatement(bodyStatement, stepStatement)));

        // push the newly created while statement onto the stack
        currentState.getExecutionStack().push(newStatement);

        return null;
    }

    @Override
    public String toString()
    {
        return "for( " + initialStatement + "; " + condition + "; "+ stepStatement + " )" + bodyStatement + " ;";
    }

}
