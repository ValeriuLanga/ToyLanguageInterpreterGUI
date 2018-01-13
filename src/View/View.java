package View;/*

package View;

import java.util.Scanner;
import Model.*;
import Controller.*;
import Model.Expressions.ArithmeticExpression;
import Model.Expressions.ConstantExpression;
import Model.Expressions.VariableExpression;
import Model.Repository.Repository;
import Model.Repository.RepositoryInterface;
import Model.Statements.*;

public class View {
    private static int executeAll;
    private Scanner keyboardScanner = new Scanner(System.in);

    private int selectExistingProgram(){
        int input;

        System.out.println("Choose a program from the below:");
        System.out.println("1. a=10; Print(a)");
        System.out.println("2. a=2+3*5; b=a+1; Print(b)");
        System.out.println("3. a=0;\n" + "(If a Then v=2 Else v=3);\n" + " Print(v)");

        input = keyboardScanner.nextInt();
        return input;
    }

    private void executeAllAsk(){
        int input;

        System.out.println("Execute all programs? 1/0");
        input = keyboardScanner.nextInt();

        if(input != 1 && input != 0) {
            System.out.println("Invalid input!");
            return ;
        }

        if(input == 1)
            executeAll =  1;
        else
            executeAll = 0;
    }

    public static void main(String args[]) {
        Statement statement1 = new CompoundStatement(
                new AssignStatement("a", new ConstantExpression(10)),
                new PrintStatement(new VariableExpression("a")));


        Statement statement2 = new CompoundStatement(new AssignStatement(
                                                            "a",
                                                            new ArithmeticExpression('+',
                                                                                    new ConstantExpression(2),
                                                                                    new ArithmeticExpression('*',
                                                                                            new ConstantExpression(3),
                                                                                            new ConstantExpression(5)))),
                                                            new CompoundStatement(new AssignStatement("b",
                                                                                        new ArithmeticExpression('+',
                                                                                                new VariableExpression("a"),
                                                                                                    new ConstantExpression(1))),
                                                                    new PrintStatement(new VariableExpression("b"))));

        Statement statement3 = new CompoundStatement(new AssignStatement("a", new ConstantExpression(0)),
                new CompoundStatement(new IfStatement(new VariableExpression("a"),new AssignStatement("v",new ConstantExpression(2)), new
                        AssignStatement("v", new ConstantExpression(3))), new PrintStatement(new VariableExpression("v"))));

        ExecutionStackInterface<Statement> executionStack = new ExecutionStack<>();
        SymbolTableInterface<String, Integer> symbolTable = new SymbolTable<>();
        OutputListInterface<Integer> outputList = new OutputList<>();
        ProgramState programState = new ProgramState(executionStack, symbolTable, outputList);

        RepositoryInterface repository = new Repository("LogFile.txt");
        repository.addProgramState(programState);

        Controller controller = new Controller(repository);

        View view = new View();
        int prg = view.selectExistingProgram();

        switch (prg) {
            case 1: {
                executionStack.push(statement1);
                break;
            }
            case 2: {
                executionStack.push(statement2);
                break;
            }
            case 3: {
                executionStack.push(statement3);
                break;
            }
            default: {
                System.out.println("No program corresponding to this number");
            }
        }

        view.executeAllAsk();

        switch (executeAll) {
            case 1: {
                try {
                    controller.executeAll();
                }
                catch (Exception exc){
                    System.out.println(exc.getMessage());
                }
                return;
            }
            case 0: {
                try {
                    controller.executeOnce();
                }
                catch (Exception exc){
                    System.out.println(exc.getMessage());
                }
                return;
            }
            default: {
                System.out.println("No option corresponding to this number");
            }
        }

    }
}
*/