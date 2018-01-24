package Model.Statements;

import Model.Exceptions.DivisionByZeroException;
import Model.Exceptions.UnknownOperationException;
import Model.Expressions.Expression;
import Model.Heap.HeapInterface;
import Model.LatchTable.LatchTableInterface;
import Model.ProgramState;
import Model.SymbolTable.SymbolTableInterface;
import Utils.FreeAddressGenerator;

public class NewLatchStatement implements Statement {
    private String variableName;
    private Expression expression;

    public NewLatchStatement(String variableName, Expression expression) {
        this.variableName = variableName;
        this.expression = expression;
    }

    @Override
    public ProgramState execute(ProgramState currentState) throws DivisionByZeroException, UnknownOperationException {
        SymbolTableInterface<String, Integer> symbolTable = currentState.getSymbolTable();
        HeapInterface<Integer, Integer> heap = currentState.getHeap();
        LatchTableInterface<Integer, Integer> latchTable = currentState.getLatchTable();

        // evaluate the expression
        int countDownValue = expression.eval(symbolTable, heap);

        // generate a new address
        int newLatchTableIndex = FreeAddressGenerator.generateFreeAddress(latchTable);

        // add the entry to the latch table
        latchTable.add(newLatchTableIndex, countDownValue);

        if(symbolTable.contains(variableName)){
            // update the 'address' stored in the symbol table
            symbolTable.update(variableName, newLatchTableIndex);
        }
        else{
            // add a new entry in the heap
            symbolTable.add(variableName, newLatchTableIndex);
        }

        return null;
    }
}
