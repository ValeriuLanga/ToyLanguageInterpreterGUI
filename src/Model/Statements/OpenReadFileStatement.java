package Model.Statements;

import Model.Exceptions.DivisionByZeroException;
import Model.Exceptions.FileException;
import Model.Exceptions.UnknownOperationException;
import Model.FileTable.FileDescriptor;
import Model.FileTable.FileTableInterface;
import Model.ProgramState;
import Utils.IdGenerator;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class OpenReadFileStatement implements Statement {
    private String fileName;
    private String fileId;

    public OpenReadFileStatement(String fileName, String fielId){
        this.fileName    = fileName;
        this.fileId      = fielId;
    }

    @Override
    public ProgramState execute(ProgramState currentState) throws DivisionByZeroException, UnknownOperationException, FileException{
        FileTableInterface fileTable = currentState.getFileTable();

        if(fileTable.contains(new FileDescriptor(fileName, null))){
            throw new FileException("File already open!");
        }
        try{
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
            int newId = IdGenerator.generateId();

            // create a new FileDescriptor
            FileDescriptor fd = new FileDescriptor(fileName, bufferedReader);
            fileTable.add(newId, fd);

            // add the FileDescriptor to the SymbolTable
            currentState.getSymbolTable().add(fileId, newId);
        }
        catch (IOException exception){
            throw new FileException(exception.getMessage());
        }

        return null;
    }

    @Override
    public String toString(){
        return "open ( fileId: " + fileId + " fileName: " + fileName + ")";
    }
}
