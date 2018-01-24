package Model.Statements;

import Model.Exceptions.DivisionByZeroException;
import Model.Exceptions.UnknownOperationException;
import Model.ExecutionStack.ExecutionStack;
import Model.LatchTable.LatchTableInterface;
import Model.ProgramState;
import Model.SymbolTable.SymbolTableInterface;

public class AwaitStatement implements Statement {
    private String variableName;

    public AwaitStatement(String variableName) {
        this.variableName = variableName;
    }

    @Override
    public ProgramState execute(ProgramState currentState) throws DivisionByZeroException, UnknownOperationException {
        SymbolTableInterface<String, Integer> symbolTable = currentState.getSymbolTable();
        LatchTableInterface<Integer, Integer> latchTable = currentState.getLatchTable();

        // get the latch table index
        int latchTableIndex = symbolTable.get(variableName);

        // get the counter from the latch table
        int counter = latchTable.get(latchTableIndex);

        // if the counter is not 0, i.e. the Decrementor has not finished
        if(counter > 0){
            currentState.getExecutionStack().push(this);
        }
        // otherwise we do nothing ; we have completed the wait

        return null;
    }

    @Override
    public String toString() {
        return "Await(" + variableName + ')';
    }
}
