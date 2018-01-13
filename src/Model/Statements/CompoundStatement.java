package Model.Statements;

import Model.ExecutionStack.ExecutionStackInterface;
import Model.ProgramState;

public class CompoundStatement implements Statement {
    private Statement statement1, statement2;

    public CompoundStatement(Statement s1, Statement s2) {
        this.statement1 = s1;
        this.statement2 = s2;
    }

    @Override
    public ProgramState execute(ProgramState currentState) {
        ExecutionStackInterface<Statement> stack = currentState.getExecutionStack();

        stack.push(this.statement2);
        stack.push(this.statement1);

        return null;
    }

    @Override
    public String toString() {
        return "(" + statement1 + ";" + statement2 + ")";
    }
}
