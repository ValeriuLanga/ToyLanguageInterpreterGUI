package Model.Statements;

import Model.Exceptions.DivisionByZeroException;
import Model.Exceptions.FileException;
import Model.Exceptions.UnknownOperationException;
import Model.FileTable.FileDescriptor;
import Model.ProgramState;

import java.io.BufferedReader;
import java.io.IOException;

public class CloseReadFileStatement implements Statement {
    private String fileId;

    public CloseReadFileStatement(String fileId) {
        this.fileId = fileId;
    }

    @Override
    public ProgramState execute(ProgramState currentState) throws DivisionByZeroException, UnknownOperationException {
        try{
            int fdId = currentState.getSymbolTable().get(fileId);
            FileDescriptor fileDescriptor = currentState.getFileTable().get(fdId);

            BufferedReader bufferedReader = fileDescriptor.getBufferedReader();
            bufferedReader.close();

            currentState.getFileTable().remove(fdId);
        }
        catch (IOException exception){
            throw new FileException(exception.getMessage());
        }

        return null;
    }

    @Override
    public String toString(){
        return "close ( fileId: " + fileId + ")";
    }
}
