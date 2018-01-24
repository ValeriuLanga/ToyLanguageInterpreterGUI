package Model.Statements;

import Model.Exceptions.DivisionByZeroException;
import Model.Exceptions.UnknownOperationException;
import Model.ExecutionStack.ExecutionStack;
import Model.ExecutionStack.ExecutionStackInterface;
import Model.ProgramState;
import Utils.IdGenerator;

public class ForkStatement implements Statement {
    private Statement statement;

    public ForkStatement(Statement statement) {
        this.statement = statement;
    }

    @Override
    public ProgramState execute(ProgramState currentState) throws DivisionByZeroException, UnknownOperationException {

        // create a new program state which we return
        ExecutionStackInterface<Statement> newExecutionStack = new ExecutionStack<Statement>();
        newExecutionStack.push(statement);

        ProgramState forkedProgramState = new ProgramState(
                            newExecutionStack,
                            currentState.getSymbolTable().clone(),
                            currentState.getOutputList(),
                            currentState.getFileTable(),
                            currentState.getHeap(),
                   IdGenerator.generateId() * 10,
                            currentState.getLatchTable());

        return forkedProgramState;
    }

    @Override
    public String toString(){
        return "fork( " + statement.toString() + " )";
    }
}
