package Model.Statements;

import Model.Exceptions.DivisionByZeroException;
import Model.Exceptions.HeapException;
import Model.Exceptions.UnknownOperationException;
import Model.Expressions.Expression;
import Model.Heap.HeapInterface;
import Model.ProgramState;
import Model.SymbolTable.SymbolTableInterface;

public class WriteAddressStatement implements Statement {

    private String variableName;
    private Expression expression;

    public WriteAddressStatement(String variableName, Expression expression) {
        this.variableName = variableName;
        this.expression = expression;
    }

    @Override
    public ProgramState execute(ProgramState currentState) throws DivisionByZeroException, UnknownOperationException {
        SymbolTableInterface<String, Integer> symbolTable = currentState.getSymbolTable();
        HeapInterface<Integer, Integer> heap =  currentState.getHeap();

        int value = expression.eval(symbolTable, heap);

        // get the address at which we want to update the value
        int address = symbolTable.get(variableName);
        if( !heap.contains(address) ){
            throw new HeapException("Invalid address!");
        }

        // update the address to the new value
        heap.add(address, value);

        return null;
    }

    @Override
    public String toString(){
        return " WriteHeap( address of: " + variableName + " value: " +  expression.toString() + ";";
    }
}
