package View;

import Model.Exceptions.DivisionByZeroException;
import Model.Exceptions.UnknownOperationException;

import java.io.IOException;

public abstract class Command {
    private String option;
    private String description;

    public Command(String option, String description)
    {
        this.option = option;
        this.description = description;
    }

    public abstract void execute() throws InterruptedException, UnknownOperationException, DivisionByZeroException, IOException;

    public String getOption()
    {
        return option;
    }

    public String getDescription()
    {
        return description;
    }

    @Override
    public String toString()
    {
        return String.format("%2s : %s", option, description);
    }
}
