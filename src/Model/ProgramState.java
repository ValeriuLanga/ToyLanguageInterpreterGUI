package Model;

import Model.Exceptions.DivisionByZeroException;
import Model.Exceptions.ExecutionStackException;
import Model.Exceptions.UnknownOperationException;
import Model.ExecutionStack.ExecutionStackInterface;
import Model.FileTable.FileDescriptor;
import Model.FileTable.FileTableInterface;
import Model.Heap.HeapInterface;
import Model.OutputList.OutputListInterface;
import Model.Statements.Statement;
import Model.SymbolTable.SymbolTableInterface;

public class ProgramState {
    private ExecutionStackInterface<Statement> executionStack;
    private SymbolTableInterface<String, Integer> symbolTable;
    private OutputListInterface<Integer> outputList;
    private FileTableInterface<Integer, FileDescriptor> fileTable;
    private HeapInterface<Integer, Integer> heap;
    private int programId;

    public ProgramState(ExecutionStackInterface<Statement> stack,
                        SymbolTableInterface<String, Integer> symbolTable,
                        OutputListInterface<Integer> outputList,
                        FileTableInterface<Integer, FileDescriptor> fileTable,
                        HeapInterface<Integer, Integer> heap,
                        int programId){

        this.executionStack = stack;
        this.symbolTable = symbolTable;
        this.outputList = outputList;
        this.fileTable = fileTable;
        this.heap = heap;
        this.programId = programId;
    }

    public int getProgramId() {
        return programId;
    }

    public ExecutionStackInterface<Statement> getExecutionStack() {
       return executionStack;
    }

    public OutputListInterface<Integer> getOutputList(){
        return outputList;
    }

    public SymbolTableInterface<String, Integer> getSymbolTable(){
        return symbolTable;
    }

    public FileTableInterface<Integer, FileDescriptor> getFileTable() { return fileTable; }

    public HeapInterface<Integer, Integer> getHeap() {
        return heap;
    }

    public boolean isCompleted(){
        if(executionStack.isEmpty())
            return true;
        return false;
    }

    public ProgramState executeOnce() throws DivisionByZeroException, UnknownOperationException, ExecutionStackException {
        if(executionStack.isEmpty()){
            throw new ExecutionStackException("[ExecutionStack] is empty!");
        }

        Statement statement = executionStack.pop();

        return statement.execute(this);
    }

    @Override
    public String toString(){
        return  "Id: " + programId + "\n" +
                "ExecutionStack: " + executionStack.toString() + "\nSymbolTable: " + symbolTable.toString()
                + "\nOutput: " + outputList.toString()
                +"\nFileTable: " + fileTable.toString()
                +"\nHeap: " + heap.toString()
                + '\n';
    }
}
