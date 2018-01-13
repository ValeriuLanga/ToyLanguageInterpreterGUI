package Model.Statements;

import Model.Exceptions.DivisionByZeroException;
import Model.Exceptions.FileException;
import Model.Exceptions.UnknownOperationException;
import Model.FileTable.FileDescriptor;
import Model.ProgramState;

import java.io.BufferedReader;
import java.io.IOException;

public class ReadFileStatement implements Statement {
    private String fileId;
    private String variableName;

    public ReadFileStatement(String fileId, String variableName){
        this.fileId = fileId;
        this.variableName = variableName;
    }

    @Override
    public ProgramState execute(ProgramState currentState) throws DivisionByZeroException, UnknownOperationException, FileException {
        try {
            // get the actual file descriptor
            int fdId = currentState.getSymbolTable().get(fileId);
            FileDescriptor fd = currentState.getFileTable().get(fdId);
            BufferedReader bufferedReader = fd.getBufferedReader();

            // read a line
            String string = bufferedReader.readLine();

            // convert to the variable
            int value;

            if (string == null) {
                value = 0;
            } else {
                value = Integer.parseInt(string);
            }

            // add the value to the symbol table
            currentState.getSymbolTable().add(variableName, value);
        }
        catch (IOException e){
            throw new FileException(e.getMessage());
        }

        return null;
    }

    @Override
    public String toString(){
        return "read (" + fileId + "," + variableName + ");";
    }
}
