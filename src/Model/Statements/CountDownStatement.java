package Model.Statements;

import Model.Exceptions.DivisionByZeroException;
import Model.Exceptions.UnknownOperationException;
import Model.LatchTable.LatchTableInterface;
import Model.ProgramState;
import Model.SymbolTable.SymbolTableInterface;

public class CountDownStatement implements Statement{
    private String variableName;

    public CountDownStatement(String variableName) {
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

        // if the counter is not 0, decrement it and update the latch table
        if(counter > 0){
            counter = counter -1;
            latchTable.add(latchTableIndex, counter);
        }
        // if the counter is zero, the countdown is finished and we do nothing

        return null;
    }

    @Override
    public String toString() {
        return "Await(" + variableName + ')';
    }
}
