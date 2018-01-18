package Gui;

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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ProgramStateSelectorController implements Initializable{

    @FXML
    private ListView<String> listViewProgramState;

    @FXML
    private Button buttonSelect;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        List<ProgramState> programStates = new ArrayList<>();

        // initialize statements
        programStates = InitializeProgramStates();

        // get the text representation
        List<String> programStatesTextRepresentation = programStates.stream().
                map(p -> p.getExecutionStack().peek().toString()).collect(Collectors.toList());

        // create an observable program states list from the string representation
        ObservableList<String> observableProgramStatesList = FXCollections.observableList(programStatesTextRepresentation);

        // update the list view
        listViewProgramState.setItems(observableProgramStatesList);

        // make a 'final' copy of the list for the button action
        final List<ProgramState> programStatesFinal = programStates;

        buttonSelect.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                // get the index of the program which we want to run
                int index = listViewProgramState.getSelectionModel().getSelectedIndex();

                // validate the selection
                if(-1 == index){
                    Alert alert = new Alert(Alert.AlertType.ERROR, "No program State Selected!");
                    alert.show();
                    return;
                }

                // get the program state
                ProgramState selectedProgram = programStatesFinal.get(index);

                try
                {
                    // close the selector
                    Stage oldStage = (Stage) buttonSelect.getScene().getWindow();
                    oldStage.close();


                    // start the execution window
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ProgramStateExecutor.fxml"));
                    Pane newPane = fxmlLoader.load();

                    Scene scene = new Scene(newPane);

                    Stage newStage = new Stage();
                    newStage.setScene(scene);
                    newStage.setTitle("Execution Window");

                    // create the log file name
                    String logFileName = new String();
                    logFileName = "LogFile" +  index + ".txt";


                    // send the selected program state to the controller
                    ProgramStateExecutorController  programStateExecutorController = fxmlLoader.getController();

                    // create the repo
                    RepositoryInterface newRepository =  new Repository(logFileName);
                    newRepository.addProgramState(selectedProgram);

                    // initialize the program for this controller
                    programStateExecutorController.initializeProgram(selectedProgram, newRepository);

                    // show the new stage
                    newStage.show();

                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

    }

    private List<ProgramState> InitializeProgramStates() {
        List<ProgramState> programStates = new ArrayList<>();

        Statement statement1 = new CompoundStatement(
                new AssignStatement("a", new ConstantExpression(10)),
                new PrintStatement(new VariableExpression("a")));

        ExecutionStackInterface<Statement> executionStack1  = new ExecutionStack<>();
        executionStack1.push(statement1);

        SymbolTableInterface<String, Integer> symbolTable1  = new SymbolTable<>();
        OutputListInterface<Integer> outputList1            = new OutputList<>();
        FileTable<Integer, FileDescriptor> fileTable1       = new FileTable<>();
        Heap<Integer, Integer> heap1                       = new Heap<>();
        ProgramState programState1                          = new ProgramState(executionStack1, symbolTable1, outputList1, fileTable1, heap1, IdGenerator.generateId());

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

        programStates.add(programState1);
        programStates.add(programState2);
        programStates.add(programState3);
        programStates.add(programState4);
        programStates.add(programState5);
        programStates.add(programState6);

        return  programStates;
    }
}
