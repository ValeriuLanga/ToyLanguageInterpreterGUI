package main;

import Model.ExecutionStack.ExecutionStack;
import Model.ExecutionStack.ExecutionStackInterface;
import Model.Expressions.*;
import Model.FileTable.FileDescriptor;
import Model.FileTable.FileTable;
import Model.Heap.Heap;
import Model.OutputList.OutputList;
import Model.OutputList.OutputListInterface;
import Model.ProgramState;
import Model.Repository.Repository;
import Model.Repository.RepositoryInterface;
import Model.Statements.*;
import Model.SymbolTable.SymbolTable;
import Model.SymbolTable.SymbolTableInterface;
import Utils.IdGenerator;
import View.ExitCommand;
import View.RunExample;
import View.TextMenu;


public class Interpreter {

    public static void main_old(String[] args){
        TextMenu menu = new TextMenu();

        menu.addCommand(new ExitCommand("0", "Exit!"));

        //
        // 1st statement below
        //

        Statement statement1 = new CompoundStatement(
                new AssignStatement("a", new ConstantExpression(10)),
                new PrintStatement(new VariableExpression("a")));

        ExecutionStackInterface<Statement> executionStack1  = new ExecutionStack<>();
        executionStack1.push(statement1);

        SymbolTableInterface<String, Integer> symbolTable1  = new SymbolTable<>();
        OutputListInterface<Integer> outputList1            = new OutputList<>();
        FileTable<Integer, FileDescriptor> fileTable1       = new FileTable<>();
        Heap<Integer, Integer>  heap1                       = new Heap<>();
        ProgramState programState1                          = new ProgramState(executionStack1, symbolTable1, outputList1, fileTable1, heap1, IdGenerator.generateId());

        RepositoryInterface repository1                     = new Repository("LogFile1.txt");
        repository1.addProgramState(programState1);
        Controller.Controller controller1 = new Controller.Controller(repository1);

        menu.addCommand(new RunExample("1", programState1.toString(), controller1));

        //
        // 2nd statement below
        //

        Statement statement2 = new CompoundStatement(
                        new CompoundStatement(
                                new CompoundStatement(
                                        new CompoundStatement(
                                                new OpenReadFileStatement("example.txt", "f"),
                                                new ReadFileStatement("f", "c")),
                                        new PrintStatement(new VariableExpression("c"))),
                                new IfStatement(new VariableExpression("c"),
                                        new CompoundStatement(
                                                new ReadFileStatement("f", "c"),
                                                new PrintStatement(new VariableExpression("c"))),
                                        new PrintStatement(new ConstantExpression(0)))),
                        new CloseReadFileStatement("f"));

        ExecutionStackInterface<Statement> executionStack2  = new ExecutionStack<>();
        executionStack2.push(statement2);

        SymbolTableInterface<String, Integer> symbolTable2  = new SymbolTable<>();
        OutputListInterface<Integer> outputList2            = new OutputList<>();
        FileTable<Integer, FileDescriptor> fileTable2       = new FileTable<>();
        Heap<Integer, Integer>  heap2                       = new Heap<>();
        ProgramState programState2                          = new ProgramState(executionStack2, symbolTable2, outputList2, fileTable2, heap2, IdGenerator.generateId());

        RepositoryInterface repository2                     = new Repository("LogFile2.txt");
        repository2.addProgramState(programState2);
        Controller.Controller controller2 = new Controller.Controller(repository2);

        menu.addCommand(new RunExample("2", statement2.toString(), controller2));

        //
        // 3rd statement below
        //

        /*
        Statement statement3 = new CompoundStatement(
                new CompoundStatement(
                        new CompoundStatement(
                                new CompoundStatement(
                                        new AssignStatement("v", new ConstantExpression(10)),
                                        new NewAddressStatement("v", new ConstantExpression(20))),
                                new NewAddressStatement("a", new ConstantExpression(20))),
                        new PrintStatement(new ArithmeticExpression('+',
                                new ConstantExpression(100),
                                new ReadAddressExpression("v")))),
                new PrintStatement(new ArithmeticExpression('+',
                        new ConstantExpression(100),
                        new ReadAddressExpression("a"))));
        */

        Statement statement3 = new CompoundStatement(
                new CompoundStatement(
                        new CompoundStatement(
                                new CompoundStatement(
                                        new CompoundStatement(
                                                new AssignStatement("v", new ConstantExpression(10)),
                                                new NewAddressStatement("v", new ConstantExpression(20))),
                                        new NewAddressStatement("a", new ConstantExpression(20))),

                                new AssignStatement("a",new ConstantExpression(10))),

                                new PrintStatement(new ArithmeticExpression('+',
                                        new ConstantExpression(100),
                                        new ReadAddressExpression("v")))),
                        new PrintStatement(new ArithmeticExpression('+',
                                new ConstantExpression(100),
                                new ReadAddressExpression("a"))));

        ExecutionStackInterface<Statement> executionStack3  = new ExecutionStack<>();
        executionStack3.push(statement3);

        SymbolTableInterface<String, Integer> symbolTable3  = new SymbolTable<>();
        OutputListInterface<Integer> outputList3            = new OutputList<>();
        FileTable<Integer, FileDescriptor> fileTable3       = new FileTable<>();
        Heap<Integer, Integer>  heap3                       = new Heap<>();
        ProgramState programState3                          = new ProgramState(executionStack3, symbolTable3, outputList3, fileTable3, heap3, IdGenerator.generateId());

        RepositoryInterface repository3                     = new Repository("LogFile3.txt");
        repository3.addProgramState(programState3);

        Controller.Controller controller3                              = new Controller.Controller(repository3);

        menu.addCommand(new RunExample("3", statement3.toString(), controller3));

        //
        // 4th Statement below
        //
        Statement statement4 = new PrintStatement(new BooleanExpression(">", new ConstantExpression(2), new ConstantExpression(1)));

        ExecutionStackInterface<Statement> executionStack4  = new ExecutionStack<>();
        executionStack4.push(statement4);

        SymbolTableInterface<String, Integer> symbolTable4  = new SymbolTable<>();
        OutputListInterface<Integer> outputList4            = new OutputList<>();
        FileTable<Integer, FileDescriptor> fileTable4       = new FileTable<>();
        Heap<Integer, Integer>  heap4                       = new Heap<>();
        ProgramState programState4                          = new ProgramState(executionStack4, symbolTable4, outputList4, fileTable4, heap4, IdGenerator.generateId());

        RepositoryInterface repository4                     = new Repository("LogFile4.txt");
        repository4.addProgramState(programState4);

        Controller.Controller controller4                              = new Controller.Controller(repository4);

        menu.addCommand(new RunExample("4", statement4.toString(), controller4));

        //
        // 5th Statement below
        //
        Statement statement5 = new CompoundStatement(
                new CompoundStatement(
                        new AssignStatement("v", new ConstantExpression(6)),
                        new WhileStatement(new BooleanExpression("!=", new VariableExpression("v"), new ConstantExpression(4)),
                                new CompoundStatement(
                                        new PrintStatement(new VariableExpression("v")),
                                        new AssignStatement("v", new ArithmeticExpression('-',
                                                new VariableExpression("v"),
                                                new ConstantExpression(1)))))),
                new PrintStatement(new VariableExpression("v")));

        ExecutionStackInterface<Statement> executionStack5  = new ExecutionStack<>();
        executionStack5.push(statement5);

        SymbolTableInterface<String, Integer> symbolTable5  = new SymbolTable<>();
        OutputListInterface<Integer> outputList5            = new OutputList<>();
        FileTable<Integer, FileDescriptor> fileTable5       = new FileTable<>();
        Heap<Integer, Integer>  heap5                       = new Heap<>();
        ProgramState programState5                          = new ProgramState(executionStack5, symbolTable5, outputList5, fileTable5, heap5, IdGenerator.generateId());

        RepositoryInterface repository5                     = new Repository("LogFile5.txt");
        repository5.addProgramState(programState5);

        Controller.Controller controller5                              = new Controller.Controller(repository5);

        menu.addCommand(new RunExample("5", statement5.toString(), controller5));


        //
        //  6th statement below
        //

        Statement statement6 = new CompoundStatement(
                new CompoundStatement(
                        new AssignStatement("v", new ConstantExpression(10)),
                        new NewAddressStatement("a",new ConstantExpression(22))),
                new ForkStatement(
                        new CompoundStatement(
                                new CompoundStatement(
                                        new WriteAddressStatement("a", new ConstantExpression(30)),
                                        new AssignStatement("v", new ConstantExpression(32))),
                                new CompoundStatement(
                                        new PrintStatement(new VariableExpression("v")),
                                        new PrintStatement(new ReadAddressExpression("a"))))));

        //Statement statement6 = new ForkStatement(statement5);

        ExecutionStack<Statement> executionStack6 = new ExecutionStack<>();
        executionStack6.push(statement6);

        ProgramState programState6 = new ProgramState(executionStack6, new SymbolTable<String, Integer>(),
                new OutputList<Integer>(), new FileTable<Integer, FileDescriptor>(), new Heap<Integer, Integer>(), IdGenerator.generateId());

        RepositoryInterface repository6 = new Repository("LogFile6.txt");
        repository6.addProgramState(programState6);

        Controller.Controller controller6 = new Controller.Controller(repository6);

        menu.addCommand(new RunExample("6", statement6.toString(), controller6));

        // statement 7, ForStatement
        Statement statement7 = new CompoundStatement(
          new AssignStatement("v", new ConstantExpression(20)),
          new CompoundStatement(
                  new ForStatement(
                          new AssignStatement("v", new ConstantExpression(20)),
                          new BooleanExpression("<", new VariableExpression("v"), new ConstantExpression(3)),
                          new AssignStatement("v", new ArithmeticExpression(
                                  '+',
                                  new ConstantExpression(1),
                                  new VariableExpression("v"))),
                          new ForkStatement(new CompoundStatement(new PrintStatement(new VariableExpression("v")),
                                  new AssignStatement("v", new ArithmeticExpression(
                                          '+',
                                          new ConstantExpression(1),
                                          new VariableExpression("v")))
                                  ))),
                  new PrintStatement(new VariableExpression("v")))
          );

        ExecutionStack<Statement> executionStack7 = new ExecutionStack<>();
        executionStack7.push(statement7);

        ProgramState programState7 = new ProgramState(executionStack7, new SymbolTable<String, Integer>(),
                new OutputList<Integer>(), new FileTable<Integer, FileDescriptor>(), new Heap<Integer, Integer>(), IdGenerator.generateId());

        RepositoryInterface repository7 = new Repository("LogFile7.txt");
        repository7.addProgramState(programState7);

        Controller.Controller controller7 = new Controller.Controller(repository7);

        menu.addCommand(new RunExample("7", statement7.toString(), controller7));


        menu.show();
    }
}
