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
    private TableView<Map.Entry<Integer, Integer>> latchTableTableView;

    @FXML
    private Button runOneStepButton;

    // define some basics for the actual running of programs
    private RepositoryInterface repository;
    private ExecutorService executorService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    // this method is called by the controller of the ProgramStateSelector
    public void initializeProgram(ProgramState selectedProgramState, RepositoryInterface repo) {

        repository = repo;

        // set the program ids
        setProgramIdsListView(repository);

        // set the number of program states :)

        // execution stack operation
        setExecutionStackListView(selectedProgramState);

        // symbol table operation
        setSymbolTableTableView(selectedProgramState);

        // file table operations
        setFileTableTableView(selectedProgramState);

        // heap operations
        setHeapTableView(selectedProgramState);

        // output list operations
        setOutputListView(selectedProgramState);


        runOneStepButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // actually run one step :)
                oneStepGUI();

                if (repository.getProgramsList().size() == 1 && repository.getProgramsList().get(0).isCompleted()) {
                    runOneStepButton.disableProperty();
                    runOneStepButton.setVisible(false);
                    Alert alert = new Alert(Alert.AlertType.ERROR, "No more programs to run!");
                    alert.show();
                }

                ProgramState currentProgramState;

                try {
                    currentProgramState = repository.getByProgramId(programStatesIdsListView.getSelectionModel().getSelectedItem());
                    if(currentProgramState == null){
                        //refresh the list since we have an empty program state
                        setProgramIdsListView(repository);
                        currentProgramState = repository.getProgramsList().get(0);
                    }
                } catch (Exception ex) {
                    currentProgramState = repository.getProgramsList().get(0);
                }

                // program ids
                setProgramIdsListView(repository);

                // EXECUTION STACK
                setExecutionStackListView(currentProgramState);

                // SYMBOL TABLE
                setSymbolTableTableView(currentProgramState);

                // FILE TABLE
                setFileTableTableView(currentProgramState);

                // HEAP
                setHeapTableView(currentProgramState);

                // OUTPUT
                setOutputListView(currentProgramState);

                // LATCH
                setLatchTableTableView(currentProgramState);
            }
        });

        programStatesIdsListView.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<Integer>() {
                    public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
                        if (newValue == null)
                            newValue = repository.getProgramsList().get(0).getProgramId();

                        ProgramState newProgramState = repository.getByProgramId(newValue);


                        // EXECUTION STACK
                       setExecutionStackListView(newProgramState);

                        // SYMBOL TABLE
                        setSymbolTableTableView(newProgramState);
                    }
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

        repository.setProgramsList(removeCompletedPrograms(repository.getProgramsList()));

        List<ProgramState> prgList = repository.getProgramsList();
        // check if we still have something to run

        try {
            oneStepAllPrg(prgList);
        }
        catch (InterruptedException interruptedExc){
            //Alert alert = new Alert(Alert.AlertType.ERROR, interruptedExc.toString());
            Alert alert = new Alert(Alert.AlertType.ERROR, "The program had ended!");
            alert.show();
        }


        executorService.shutdown();
    }

    public List<ProgramState> removeCompletedPrograms(List<ProgramState> programStateList){
        return programStateList.stream().filter(x -> !x.isCompleted()).collect(Collectors.toList());
    }

    private void setProgramIdsListView(RepositoryInterface repository){
        List<Integer> programIdsRepresentation = repository.getProgramsList().stream().map(e -> e.getProgramId()).collect(Collectors.toList());
        ObservableList<Integer> programIdsObservableList = FXCollections.observableArrayList(programIdsRepresentation);
        programStatesIdsListView.setItems(programIdsObservableList);
    }

    private void setExecutionStackListView(ProgramState programState){
        Iterable<Statement> executionStackRepresentation = programState.getExecutionStack().getUnderlayingList();
        List<Statement> executionList = new ArrayList<>();
        List<String> executionStackStringRepresentation;

        executionStackRepresentation.forEach(e -> executionList.add(e));
        executionStackStringRepresentation = executionList.stream().map(e -> e.toString()).collect(Collectors.toList());

        ObservableList<String> executionStackObservableList = FXCollections.observableArrayList(executionStackStringRepresentation);
        executionStackListView.setItems(executionStackObservableList);
    }

    private void setSymbolTableTableView(ProgramState programState){

        Iterable<Map.Entry<String, Integer>> symbolTableRepresentation = programState.getSymbolTable().getAsIterableMap();
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
    }

    private void setFileTableTableView(ProgramState programState){
        Iterable<Map.Entry<Integer, FileDescriptor>> fileTableRepresentation = programState.getFileTable().getAsIterable();

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
    }

    private void setHeapTableView(ProgramState programState){
        Iterable<Map.Entry<Integer, Integer>> heapRepresentation = programState.getHeap().getAsIterable();

        List<Map.Entry<Integer, Integer>> heapList = new ArrayList<>();
        heapRepresentation.forEach(e -> heapList.add(e));

        ObservableList<Map.Entry<Integer, Integer>> heapObservable = FXCollections.observableArrayList(heapList);

        TableColumn<Map.Entry<Integer, Integer>, Integer> addressColumn = new TableColumn<>("Address");
        addressColumn.setCellValueFactory(p -> (ObservableValue<Integer>) new SimpleIntegerProperty(p.getValue().getKey()).asObject());
        TableColumn<Map.Entry<Integer, Integer>, Integer> addressValueColumn = new TableColumn<>("Value");
        addressValueColumn.setCellValueFactory(p -> (ObservableValue<Integer>) new SimpleIntegerProperty(p.getValue().getValue()).asObject());

        heapTableView.getColumns().clear();
        heapTableView.getColumns().addAll(addressColumn, addressValueColumn);
        heapTableView.setItems(heapObservable);
    }

    private void setLatchTableTableView(ProgramState programState){
        Iterable<Map.Entry<Integer, Integer>> latchTableRepresentation = programState.getLatchTable().getAsIterable();

        List<Map.Entry<Integer, Integer>> latchTableList = new ArrayList<>();
        latchTableRepresentation.forEach(e -> latchTableList.add(e));

        ObservableList<Map.Entry<Integer, Integer>> latchTableObservable = FXCollections.observableArrayList(latchTableList);

        TableColumn<Map.Entry<Integer, Integer>, Integer> addressColumn = new TableColumn<>("LatchId");
        addressColumn.setCellValueFactory(p -> (ObservableValue<Integer>) new SimpleIntegerProperty(p.getValue().getKey()).asObject());
        TableColumn<Map.Entry<Integer, Integer>, Integer> addressValueColumn = new TableColumn<>("Counter");
        addressValueColumn.setCellValueFactory(p -> (ObservableValue<Integer>) new SimpleIntegerProperty(p.getValue().getValue()).asObject());

        latchTableTableView.getColumns().clear();
        latchTableTableView.getColumns().addAll(addressColumn, addressValueColumn);
        latchTableTableView.setItems(latchTableObservable);
    }

    private void setOutputListView(ProgramState programState){
        Iterable<Integer> outputListRepresentation = programState.getOutputList().getAsIterable();

        List<Integer> outputList = new ArrayList<>();
        outputListRepresentation.forEach(e -> outputList.add(e));

        ObservableList<Integer> outputListObservable = FXCollections.observableArrayList(outputList);
        outputListView.setItems(outputListObservable);
    }
}


