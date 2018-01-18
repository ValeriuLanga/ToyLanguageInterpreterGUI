package Gui;

import Model.Exceptions.FileException;
import Model.FileTable.FileDescriptor;
import Model.ProgramState;
import Model.Repository.RepositoryInterface;
import Model.Statements.Statement;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class ProgramStateExecutorController implements Initializable {

    @FXML
    private TableView<Map.Entry<String, Integer>> symbolTableTableView;
    @FXML
    private TableView<Map.Entry<Integer, FileDescriptor>> fileTableTableView;
    @FXML
    private TableView<Map.Entry<Integer, Integer>> heapTableView;
    @FXML
    private ListView<Integer> outputListView;
    @FXML
    private ListView<String> executionStackListView;
    @FXML
    private ListView<Integer> programStatesIdsListView;
    @FXML
    private TextField currentNumberOfProgramStates;
    @FXML
    private Button runOneStepButton;

    // define some basics for the actual running of programs
    private RepositoryInterface repository;
    private ExecutorService executorService;

    Iterable<Map.Entry<Integer, Integer>> heapRepresentation;
    Iterable<Map.Entry<Integer, FileDescriptor>> fileTableRepresentation;
    Iterable<Map.Entry<String, Integer>> symbolTableRepresentation;
    Iterable<Integer> outputListRepresentation;
    Iterable<Statement> executionStackRepresentation;
    List<String> executionStackStringRepresentation;
    List<Integer> programIdsRepresentation;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    // this method is called by the controller of the ProgramStateSelector
    public void initializeProgram(ProgramState selectedProgramState, RepositoryInterface repo) {

        repository = repo;

        // set the program ids
        programIdsRepresentation = repository.getProgramsList().stream().map(e -> e.getProgramId()).collect(Collectors.toList());
        ObservableList<Integer> programIdsObservableList = FXCollections.observableArrayList(programIdsRepresentation);
        programStatesIdsListView.setItems(programIdsObservableList);

        // set the number of program states :)

        // execution stack operation
        executionStackRepresentation = selectedProgramState.getExecutionStack().getUnderlayingList();
        List<Statement> executionList = new ArrayList<>();
        executionStackRepresentation.forEach(e -> executionList.add(e));
        executionStackStringRepresentation = executionList.stream().map(e -> e.toString()).collect(Collectors.toList());

        ObservableList<String> executionStackObservableList = FXCollections.observableArrayList(executionStackStringRepresentation);
        executionStackListView.setItems(executionStackObservableList);

        // symbol table operation
        symbolTableRepresentation = selectedProgramState.getSymbolTable().getAsIterableMap();
        List<Map.Entry<String, Integer>> symbolTableList = new ArrayList<>();

        symbolTableRepresentation.forEach(e -> symbolTableList.add(e));
        ObservableList<Map.Entry<String, Integer>> symbolTableObservableList = FXCollections.observableArrayList(symbolTableList);

        TableColumn<Map.Entry<String, Integer>, String> variableNameColumn = new TableColumn<>("Variable Name");
        variableNameColumn.setCellValueFactory(p -> (ObservableValue<String>) new SimpleStringProperty(p.getValue().getKey()));

        TableColumn<Map.Entry<String, Integer>, Integer> valueColumn = new TableColumn<>("Value");
        valueColumn.setCellValueFactory(p -> (ObservableValue<Integer>) new SimpleIntegerProperty(p.getValue().getValue()).asObject());

        symbolTableTableView.getColumns().clear();
        symbolTableTableView.getColumns().addAll(variableNameColumn, valueColumn);
        symbolTableTableView.setItems(symbolTableObservableList);

        // file table operations
        fileTableRepresentation = selectedProgramState.getFileTable().getAsIterable();

        List<Map.Entry<Integer, FileDescriptor>> fileList = new ArrayList<>();
        fileTableRepresentation.forEach(e -> fileList.add(e));

        ObservableList<Map.Entry<Integer, FileDescriptor>> fileTableObservable = FXCollections.observableArrayList(fileList);

        TableColumn<Map.Entry<Integer, FileDescriptor>, Integer> fileDescriptorColumn = new TableColumn<>("File Descriptor");
        fileDescriptorColumn.setCellValueFactory(p -> (ObservableValue<Integer>) new SimpleIntegerProperty(p.getValue().getKey()).asObject());
        TableColumn<Map.Entry<Integer, FileDescriptor>, String> fileColumn = new TableColumn<>("File Name");
        fileColumn.setCellValueFactory(p -> (ObservableValue<String>) new SimpleStringProperty(p.getValue().getValue().toString()));

        fileTableTableView.getColumns().clear();
        fileTableTableView.getColumns().addAll(fileDescriptorColumn, fileColumn);
        fileTableTableView.setItems(fileTableObservable);


        // heap operations
        heapRepresentation = selectedProgramState.getHeap().getAsIterable();

        List<Map.Entry<Integer, Integer>> heapList = new ArrayList<>();
        heapList.forEach(e -> heapList.add(e));

        ObservableList<Map.Entry<Integer, Integer>> heapObservable = FXCollections.observableArrayList(heapList);

        TableColumn<Map.Entry<Integer, Integer>, Integer> addressColumn = new TableColumn<>("Address");
        addressColumn.setCellValueFactory(p -> (ObservableValue<Integer>) new SimpleIntegerProperty(p.getValue().getKey()).asObject());
        TableColumn<Map.Entry<Integer, Integer>, Integer> addressValueColumn = new TableColumn<>("Value");
        addressValueColumn.setCellValueFactory(p -> (ObservableValue<Integer>) new SimpleIntegerProperty(p.getValue().getValue()).asObject());

        heapTableView.getColumns().clear();
        heapTableView.getColumns().addAll(addressColumn, addressValueColumn);
        heapTableView.setItems(heapObservable);

        // output list operations
        outputListRepresentation = selectedProgramState.getOutputList().getAsIterable();

        List<Integer> outputList = new ArrayList<>();
        outputListRepresentation.forEach(e -> outputList.add(e));

        ObservableList<Integer> outputListObservable = FXCollections.observableArrayList(outputList);
        outputListView.setItems(outputListObservable);

        runOneStepButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // actually run one step :)
                oneStepGUI();

                if (repository.getProgramsList().get(0).isCompleted() && repository.getProgramsList().size() == 1) {
                    runOneStepButton.disableProperty();
                    runOneStepButton.setVisible(false);
                    //spIdNext.setDividerPositions(1);
                    //spId.setDividerPositions(0.13);
                }

                ProgramState currentProgramState = selectedProgramState;
                /*
                try {
                    currentProgramState = repository.getByProgramId(programStatesIdsListView.getSelectionModel().getSelectedItem());
                } catch (Exception ex) {
                    //currentProgramState = repo.setCurrent(0).getCurrent();
                }
                */
                // program ids
                programIdsRepresentation = repo.getProgramsList().stream().map(e -> e.getProgramId()).collect(Collectors.toList());
                if (!programIdsRepresentation.equals(programIdsObservableList)) {
                    programIdsObservableList.setAll(programIdsRepresentation);
                    programStatesIdsListView.setItems(programIdsObservableList);
                }

                // EXECUTION STACK
                executionStackRepresentation = currentProgramState.getExecutionStack().getUnderlayingList();
                executionList.clear();
                executionStackRepresentation.forEach(e -> executionList.add(e));
                executionStackStringRepresentation = executionList.parallelStream().map(e -> e.toString()).collect(Collectors.toList());
                executionStackObservableList.setAll(executionStackStringRepresentation);
                executionStackListView.setItems(executionStackObservableList);

                // SYMBOL TABLE
                symbolTableRepresentation = currentProgramState.getSymbolTable().getAsIterableMap();
                symbolTableList.clear();
                symbolTableRepresentation.forEach(e -> symbolTableList.add(e));
                symbolTableObservableList.setAll(symbolTableList);
                symbolTableTableView.setItems(symbolTableObservableList);

                // FILE TABLE
                fileTableRepresentation = currentProgramState.getFileTable().getAsIterable();

                fileList.clear();
                fileTableRepresentation.forEach(e -> fileList.add(e));
                fileTableObservable.setAll(fileList);
                fileTableTableView.setItems(fileTableObservable);

                // HEAP
                heapRepresentation = currentProgramState.getHeap().getAsIterable();
                heapList.clear();
                heapRepresentation.forEach(e -> heapList.add(e));
                heapObservable.setAll(heapList);
                heapTableView.setItems(heapObservable);

                // OUTPUT
                outputListRepresentation = currentProgramState.getOutputList().getAsIterable();
                outputList.clear();
                outputListRepresentation.forEach(e -> outputList.add(e));
                outputListObservable.setAll(outputList);
                outputListView.setItems(outputListObservable);
                ;
            }
        });

        programStatesIdsListView.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<Integer>() {
                    public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
                        if (newValue == null)
                            newValue = repository.getProgramsList().get(0).getProgramId();

                        ProgramState newProgramState = repository.getByProgramId(newValue);


                        // EXECUTION STACK
                        executionStackRepresentation = newProgramState.getExecutionStack().getAsIterable();
                        executionList.clear();
                        executionStackRepresentation.forEach(e -> executionList.add(e));
                        executionStackStringRepresentation = executionList.stream().map(e -> e.toString()).collect(Collectors.toList());
                        executionStackObservableList.setAll(executionStackStringRepresentation);
                        executionStackListView.setItems(executionStackObservableList);

                        // SYMBOL TABLE
                        symbolTableRepresentation = newProgramState.getSymbolTable().getAsIterableMap();
                        symbolTableList.clear();
                        symbolTableRepresentation.forEach(e -> symbolTableList.add(e));
                        symbolTableObservableList.setAll(symbolTableList);
                        symbolTableTableView.setItems(symbolTableObservableList);

                    }
                    ;
                });
    }
    public void oneStepAllPrg(List<ProgramState> programStates) throws InterruptedException {
        programStates.forEach(program -> {
            try {
                repository.logProgramState(program);
            } catch (IOException e) {
                throw new FileException(e.getMessage());
            }
        });

        // run concurrently one step for each program
        // prepare the list of callables
        List<Callable<ProgramState>> callablesList = programStates.stream()
                .map((ProgramState programState) -> (Callable<ProgramState>)(()->{return programState.executeOnce();}))
                .collect(Collectors.toList());

        // start the execution
        // will return a list of threads
        List<ProgramState> newProgramsList = executorService.invokeAll(callablesList)
                .stream().map(future -> {
                    try{
                        return future.get();
                    }
                    catch(Exception e){

                        Alert alert = new Alert(Alert.AlertType.ERROR, e.toString());
                        runOneStepButton.disableProperty();
                        runOneStepButton.setVisible(false);
                        alert.show();
                    }
                    return null;
                }).filter(program -> program != null)//.filter(Objects::nonNull)
                .collect(Collectors.toList());

        // add the new programs to the list of program states
        programStates.addAll(newProgramsList);

        // log after the execution
        programStates.forEach(program -> {
            try {
                repository.logProgramState(program);
                System.out.println(program);
            } catch (IOException e) {
                throw new FileException(e.getMessage());
            }
        });

        // save the states in the repo
        repository.setProgramsList(programStates);
    }

    public void oneStepGUI()
    {
        executorService = Executors.newFixedThreadPool(2);
        List<ProgramState> prgList = repository.getProgramsList();

        if(prgList.isEmpty())
        {
            return;
        }

        try {
            oneStepAllPrg(prgList);
        }
        catch (InterruptedException interruptedExc){
            Alert alert = new Alert(Alert.AlertType.ERROR, interruptedExc.toString());
            alert.show();
        }

        //repo.setAll(removeCompletedPrg(repo.getAll()));
        executorService.shutdown();
    }
}


