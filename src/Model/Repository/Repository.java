package Model.Repository;

import Model.ProgramState;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Repository implements RepositoryInterface {
    private List<ProgramState> programList;
    private String LogFilePath;


    public Repository(String FilePath)    {
        programList = new ArrayList<ProgramState>();
        LogFilePath = FilePath;
    }

    public void addProgramState(ProgramState programState)    {
        programList.add(programState);
    }

    /*
    @Override
    public ProgramState getCurrentProgramState() {
         return arrayList.get(0);
    }
    */

    @Override
    public List<ProgramState> getProgramsList() {
        return programList;
    }

    @Override
    public void setProgramsList(List<ProgramState> programsList) {
        programList = programsList;
    }

    @Override
    public void logProgramState(ProgramState programState) throws IOException{
        PrintWriter printWriter = new PrintWriter(new BufferedWriter(new FileWriter(LogFilePath, true)));
        printWriter.print("\n");
        printWriter.print(programState);
        printWriter.close();
    }

    @Override
    public void setFileName( String fileName ){
        this.LogFilePath = fileName;
    }

    @Override
    public ProgramState getByProgramId(int id) {
        for(ProgramState state : programList)
        {
            if (state.getProgramId() == id)
                return state;
        }

        return null;
    }
}
