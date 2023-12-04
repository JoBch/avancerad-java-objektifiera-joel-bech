package com.example.avanceradjavaobjektifierajoelbech;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import com.google.gson.*;

public class Main extends Application {

    public TableView<String[]> myTable;
    public File file;
    public ObservableList<String[]> tableData = FXCollections.observableArrayList();

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("table-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 600);
        stage.setTitle("USE MY TABLEVIEW");
        stage.setScene(scene);
        stage.show();
    }

    public void chooseFile() {

        //Initiating a FileChooser making it possible to choose which file you want to display in the table
        FileChooser fileC = new FileChooser();
        fileC.setInitialDirectory(new File("src"));
        fileC.setTitle("Choose file to display");

        //Filter to filter out the typ of files I want the program to work with
        fileC.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("csv", "*.csv"),
                new FileChooser.ExtensionFilter("json", "*.json"),
                new FileChooser.ExtensionFilter("xml", "*.xml"),
                new FileChooser.ExtensionFilter("ALL FILES", "*.*")
        );
        //If we get a valid file it will print the path to it in the console
        file = fileC.showOpenDialog(null);
        if (file != null) {
            System.out.println(file.getPath());
        } else {
            System.out.println("Error");
        }
    }

    public void chosenFileCSV() { //Only works for CSV
        try {
            //Taking my value from the chooseFile method
            File f = new File(String.valueOf(file));
            Scanner sc = new Scanner(f);

            //Reading the first line in my file to get column headers
            if (sc.hasNext()) {
                String headerLine = sc.nextLine();
                String[] columnHeaders = headerLine.split(",");

                //Loop through each column header to create columns in my table
                for (int i = 0; i < columnHeaders.length; i++) {
                    int columnNumber = i;

                    //Putting the name in my headers
                    TableColumn<String[], String> column = new TableColumn<>(columnHeaders[i]);

                    //CellValue dictates what is in my columns, using the column list to do so
                    column.setCellValueFactory(param -> {
                        //Assigns the value of column to array of rows
                        String[] row = param.getValue();
                        //Checking if row and columns is not out of bounds and adding columns
                        if (row != null && row.length > columnNumber) {
                            return new SimpleStringProperty(row[columnNumber]);
                        } else {
                            return null;
                        }
                    });
                    //Adding my columns to my table using column variable from for loop
                    myTable.getColumns().add(column);
                }
            }

            //Reading the remaining lines to fill the rows
            int maxColumns = 0;
            while (sc.hasNext()) {
                String line = sc.nextLine();
                String[] rowData = line.split(",");
                tableData.add(rowData);
                if (rowData.length > maxColumns) {
                    maxColumns = rowData.length;
                }
            }

            sc.close();

        } catch (IOException e) {
            System.out.println(e);
        }
        //Setting all the values to my table
        myTable.setItems(tableData);
    }

    public void chosenFileJson() {
/*
        This method is made with the help of chatGPT.
        I made the base of the code by myself based on the code from chosenFileCSV, as I didn't understand the magic in Gson
        I had to use chatGPT to explain it for me and use its input to make the last 20% of the code.
*/
        try {
            File f = new File(String.valueOf(file));
            Scanner sc = new Scanner(f);

            if (sc.hasNext()) {
                //Scanner read the document and puts it in a string
                //\\Z is a delimiter which indicates the end of the file, so the scanner reads the whole file
                String fileContent = sc.useDelimiter("\\Z").next();

                //Using Gson to parse the Json file into a Jsonarray
                JsonArray jsonArray = JsonParser.parseString(fileContent).getAsJsonArray();

                //Gets the column headers from the first JSON object
                JsonObject firstObject = jsonArray.get(0).getAsJsonObject();
                //Creates a Tablecolumn for each
                firstObject.keySet().forEach(name -> {
                    String columnName = name;
                    TableColumn<String[], String> column = new TableColumn<>(columnName);
                    //CellValue dictates what is in my columns, using the column list to do so
                    column.setCellValueFactory(param -> {
                        String[] row = param.getValue();
                        if (row != null && row.length > getIndex(firstObject, name)) {
                            return new SimpleStringProperty(row[getIndex(firstObject, name)]);
                        } else {
                            return null;
                        }
                    });
                    //Adding my columns to my table using column variable from for loop
                    myTable.getColumns().add(column);
                });

                //Reading the remaining lines to fill the rows/columns in my table
                //Skipping the first JsonArray as it is my headers
                for (int i = 1; i < jsonArray.size(); i++) {
                    JsonObject rowData = jsonArray.get(i).getAsJsonObject();
                    String[] rowValues = new String[firstObject.size()];
                    rowData.entrySet().forEach(member -> {
                        int index = getIndex(firstObject, member.getKey());
                        if (index >= 0 && index < rowValues.length) {
                            rowValues[index] = member.getValue().getAsString();
                        }
                    });
                    //Adding my rowValues to the right index depending on columnHeader values
                    tableData.add(rowValues);
                }
            }

            sc.close();
            //Setting all the values to my table
            myTable.setItems(tableData);

        } catch (IOException e) {
            System.out.println(e);
        }
    }

    //A method to help my JsonObject find what columnHeader to put the data in
    private int getIndex(JsonObject jsonObject, String key) {
        int index = 0;
        for (String name : jsonObject.keySet()) {
            if (name.equals(key)) {
                return index;
            }
            index++;
        }
        return -1;
    }
}

