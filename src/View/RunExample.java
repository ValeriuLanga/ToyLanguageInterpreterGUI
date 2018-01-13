package View;

import Controller.Controller;
import Model.Exceptions.DivisionByZeroException;
import Model.Exceptions.UnknownOperationException;

import java.io.IOException;

public class RunExample extends Command {
    private Controller controller;

    public RunExample(String option, String description, Controller controller)
    {
        super(option, description);
        this.controller = controller;
    }

    @Override
    public void execute() throws InterruptedException, UnknownOperationException, DivisionByZeroException, IOException {
        controller.executeAll();

        /*
        try{
            controller.executeAll();
        }
        catch(DivisionByZeroException | UnknownOperationException | IOException | FileException exception) {
            System.out.println(exception.getMessage());

            // close any open files, so no handles are left open after the
            // program terminates in case of an exception

           // System.exit(0);
        }
        */
    }

    @Override
    public String toString(){
        return "Run All : " + super.toString();
    }
}
