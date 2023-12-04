package com.example.avanceradjavaobjektifierajoelbech;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.SortEvent;
import javafx.scene.control.TableView;

public class Controller extends Main {

    @FXML
    private TableView<String[]> myTable;

    @FXML
    private Label welcomeText;

    //Button for selecting my file in a file chooser and convert file to display on table
    @FXML
    void onClickSave(ActionEvent event) {
        myTable.getColumns().clear();
        tableData.clear();
        chooseFile();
        if (file.getName().endsWith(".csv")) {
            chosenFileCSV();
        } else {
            chosenFileJson();
        }
    }

    //Button to clear my table and tableData list
    @FXML
    void onClickClearTable(ActionEvent event) {
        myTable.getColumns().clear();
        tableData.clear();
    }

    //Bult in SortEvent from table
    @FXML
    void onClickSortTable(SortEvent<TableView<String[]>> event) {
    }
}


