package Model.Statements;

import Model.Exceptions.DivisionByZeroException;
import Model.Exceptions.UnknownOperationException;
import Model.ProgramState;

public interface Statement {
    public ProgramState execute(ProgramState currentState) throws DivisionByZeroException, UnknownOperationException;
}
