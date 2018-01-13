package View;

import Model.Exceptions.DivisionByZeroException;
import Model.Exceptions.FileException;
import Model.Exceptions.GenericException;
import Model.Exceptions.UnknownOperationException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

public class TextMenu {
    private HashMap<String, Command> commands;

    public TextMenu()
    {
        commands = new HashMap<>();
    }

    public void addCommand(Command command)
    {
        this.commands.put(command.getOption(), command);
    }

    public void printMenu()
    {
        System.out.println();
        System.out.println("Menu:");
        for(Command c : commands.values())
        {
            System.out.println(c);
        }
    }

    public void show()// throws InterruptedException, DivisionByZeroException, UnknownOperationException, IOException {
    {
        @SuppressWarnings("resource")
        Scanner in = new Scanner(System.in);
        while(true)
        {
            printMenu();
            System.out.println();
            System.out.print("Input option: -> ");
            String option = in.nextLine();
            Command command = this.commands.get(option);
            if(command == null)
            {
                System.out.println("Invalid input!");
            }
            else
            {
                try{
                    command.execute();
                }
                catch(DivisionByZeroException | UnknownOperationException | IOException | FileException | InterruptedException exception){
                    throw(new GenericException(exception.getMessage()));
                }
            }
        }
    }
}
