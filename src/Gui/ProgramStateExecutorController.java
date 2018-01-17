package Gui;

import Model.FileTable.FileDescriptor;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

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
    //private Iterable<Map.Entry<>>
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
