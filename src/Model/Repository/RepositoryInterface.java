package Model.Repository;

import Model.ProgramState;

import java.io.IOException;
import java.util.List;

public interface RepositoryInterface {
    public void addProgramState(ProgramState programState);

    //public ProgramState getCurrentProgramState();

    public void logProgramState(ProgramState programState)throws IOException;
    public void setFileName(String fileName);
    public ProgramState getByProgramId(int id);
    public List<ProgramState> getProgramsList();
    public void setProgramsList(List<ProgramState> programsList);
}
