/*
This file is part of GTFS Application.

GTFS Application is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later
version.

GTFS Application is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty
of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.

You should have received a copy of the GNU General Public License along with GTFS Application. If not, see <https://www.gnu.org
/licenses/>.
 */
/*
 * Course: SE2030 - 081
 * Fall 2022
 * Lab 5
 * Names: Arthur Andersen, Hudson Arney, Eden Basso, Josh Sopa
 * Created: 10/8/2022
 */

package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;

/**
 * Controller class for GUI of a GTFS program
 *
 * @author bassoe
 * @version 1.0
 */
public class Controller implements Initializable {

    @FXML
    public Pane mainPane;

    @FXML
    public Button importFileButton;

    @FXML
    public Button exportFileButton;

    @FXML
    public Button editFilesButton;

    @FXML
    public RadioButton searchByStopRadio;

    @FXML
    public RadioButton searchByRouteRadio;

    @FXML
    public RadioButton searchByTripRadio;

    @FXML
    public TextField searchBar;

    public static DataStructures dataStructure;
    private Stage stopPageStage;
    private boolean filesImported = false;
    private ArrayList<File> allFiles;

    public Controller() {
        dataStructure = new DataStructures();
        allFiles = new ArrayList<>();
    }

    public void setMainPane(Pane mainPane){
        this.mainPane = mainPane;
    }

    /**
     * Creates a FileChooser object which is displayed to the user for importing files
     *
     * @return FileChooser object
     * @author bassoe
     */
    private FileChooser getFileChooser(String title) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("stops", "*.txt"),
                new FileChooser.ExtensionFilter("trips", "*.txt"),
                new FileChooser.ExtensionFilter("routes", "*.txt"),
                new FileChooser.ExtensionFilter("stop_times", "*.txt"));
        return fileChooser;
    }

    /**
     * Handler for displaying a FileChooser for importing multiple files
     *
     * @param event The option for importing files is chosen
     * @author bassoe
     */
    @FXML
    public void importFileButtonPress(ActionEvent event) {
        showPopUp("Select all GTFS files to import.");
        FileChooser fileChooser = getFileChooser("Select GTFS File(s) to Import");
        List<File> files = fileChooser.showOpenMultipleDialog(mainPane.getScene().getWindow());

        if(files != null){
            allFiles.addAll(files);
            try {
                importFiles(allFiles);
                filesImported = true;
            } catch (IllegalArgumentException invalidException) {
                showAlert(invalidException.getMessage());
            } catch (FileNotFoundException noFile) {
                showAlert(noFile.getMessage());
                importFileButtonPress(new ActionEvent());
            }
        }
        files = new ArrayList<>();
    }

    /**
     * Handler for exporting GTFS files
     *
     * @param actionEvent user selects option to export GTFS Files
     * @author bassoe
     */
    @FXML
    public void exportFileButtonPress(ActionEvent actionEvent) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select location to export GTFS files");
        File file = directoryChooser.showDialog(mainPane.getScene().getWindow());

        try {
            exportFiles(file);

        } catch (FileNotFoundException | IllegalArgumentException invalidException) {
            showAlert(invalidException.getMessage());
        }
        System.out.println("file is written");
    }

    /**
     * Takes specified files if valid and distributes data to designated data structures
     *
     * @param files a list of imported files
     * @throws IllegalArgumentException if the header of a file is invalid
     * @throws FileNotFoundException if any of the files attempting to be read cannot be found referencing the associated File object
     * or a specific GTFS file is missing
     * @author bassoe
     */
    public void importFiles(ArrayList<File> files) throws IllegalArgumentException, FileNotFoundException {
        ArrayList<String> missing;
        if(areFilesMissing(files) && areDataStructuresEmpty()) {
            if(areFilesMissing(files)) {
                missing = getMissingFileName(files);
            } else {
                missing = getEmptyDataStructures();
            }

            throw new FileNotFoundException("Please import the missing GTFS file before continuing: " + missing);
        }
        if(files.stream().anyMatch(file -> file.getName().equals("stop_times.txt"))) {
            ListIterator<File> iterator = files.listIterator();
            File temp;
            do {
                temp = iterator.next();

            } while(iterator.hasNext() && !temp.getName().equals("stop_times.txt"));
            files.remove(iterator.previousIndex());
            files.add(temp);
        }

        for (File file : files) {
            FileIO.read(file, this);
        }
    }

    /**
     * Determines if all needed files have not been imported
     *
     * @param files List of imported files
     * @return true if files are missing, false if all needed files are contained in list
     */
    private boolean areFilesMissing(ArrayList<File> files) {
        ArrayList<String> neededFiles = new ArrayList<>(Arrays.asList("stops.txt", "trips.txt", "routes.txt", "stop_times.txt"));
        ArrayList<String> importedFiles = new ArrayList<>();
        files.forEach(file -> importedFiles.add(file.getName()));
        return !importedFiles.containsAll(neededFiles);
    }

    /**
     * Gets the names of all files that have not been imported and are required
     *
     * @param files files that have been imported
     * @return a list of files that still need to be imported
     * @author bassoe
     */
    private ArrayList<String> getMissingFileName(ArrayList<File> files) {
        ArrayList<String> neededFiles = new ArrayList<>(Arrays.asList("stops.txt", "trips.txt", "routes.txt", "stop_times.txt"));
        ArrayList<String> importedFiles = new ArrayList<>();
        files.forEach(file -> importedFiles.add(file.getName()));
        neededFiles.removeAll(importedFiles);
        return neededFiles;
    }

    /**
     * Determines of any data structures are empty upon import
     *
     * @return true of any data structures are empty, false if all are full
     * @author bassoe
     */
    private boolean areDataStructuresEmpty() {
        return dataStructure.getRouteMap().isEmpty() ||
                dataStructure.getTripMap().isEmpty() ||
                dataStructure.getStopTimeMap().isEmpty() ||
                dataStructure.getStopMap().isEmpty();
    }

    /**
     * Gets the name of files needed to fill empty data structures
     *
     * @return a list of need files to import
     * @author bassoe
     */
    private ArrayList<String> getEmptyDataStructures() {
        ArrayList<String> emptyDataStructures = new ArrayList<>();
        if(dataStructure.getStopMap().isEmpty()) {
            emptyDataStructures.add("stops.txt");
        } if(dataStructure.getTripMap().isEmpty()) {
            emptyDataStructures.add("trips.txt");
        } if(dataStructure.getRouteMap().isEmpty()) {
            emptyDataStructures.add("routes.txt");
        } if(dataStructure.getStopTimeMap().isEmpty()) {
            emptyDataStructures.add("stop_times.txt");
        }
        return emptyDataStructures;
    }

    /**
     * Exports files form the GTFS program to a user's file system
     *
     * @param file File object associated with a file name to be exported
     * @author bassoe
     */
    public void exportFiles(File file) throws FileNotFoundException, IllegalArgumentException {
        boolean areFilesToExport = false;
        if(!dataStructure.getStopMap().isEmpty()) {
            areFilesToExport = FileIO.write(new File(file.getAbsolutePath() + "\\stops.txt"));
        } if(!dataStructure.getTripMap().isEmpty()) {
            areFilesToExport = FileIO.write(new File(file.getAbsolutePath() + "\\trips.txt"));
        } if(!dataStructure.getStopTimeMap().isEmpty()) {
            areFilesToExport = FileIO.write(new File(file.getAbsolutePath() + "\\stop_times.txt"));
        } if(!dataStructure.getRouteMap().isEmpty()) {
            areFilesToExport = FileIO.write(new File(file.getAbsolutePath() + "\\routes.txt"));
        } if(!areFilesToExport) {
            throw new IllegalArgumentException("There is no data in the system to export");
        }

    }

    /**
     * Displays an alert if an invalid file is imported
     *
     * @param exceptionText message indicating why a problem occurred
     * @author bassoe
     */
    private void showAlert(String exceptionText) {
        Alert alert = new Alert(Alert.AlertType.ERROR, exceptionText);
        alert.setTitle("File Error");
        alert.showAndWait()
                .filter(response -> response == ButtonType.OK)
                .ifPresent(response -> alert.close());
    }

    /**
     * Shows a non error popup to the user
     * @param message specified message to show to the user
     * @author AJ Andersen
     */
    private void showPopUp(String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message);
        alert.setTitle("Hold up");
        alert.setHeaderText("Before you start...");
        alert.showAndWait();
    }

    public DataStructures getDataStructure() {
        return dataStructure;
    }

    /**
     * Searches by stop ID
     * @param stopID The ID of the stop to be searched
     * @throws IOException from showing the StopPage
     * @author AJ Andersen
     */
    @FXML
    public void searchByStop(String stopID) throws IOException {
        StopPageController.stopID = stopID;
        showStop();

        StopTime testStopTime = new StopTime();
        testStopTime.setStopID(stopID);
        try {
            testStopTime.getNextArrivalTime(
                    dataStructure,
                    testStopTime.getStopID(),
                    LocalTime.now(ZoneId.systemDefault()));
//            System.out.println(testStopTime.getNextTrip(dataStructure,
//                    testStopTime,
//                    LocalTime.now(ZoneId.systemDefault())));
        } catch(NoSuchElementException noStopID) {
            showAlert(noStopID.getMessage());
        }
    }

    public void searchByRoute(String routeID) {
        System.out.println(routeID + "Route");
    }

    public void searchByTrip(String tripID) {
        System.out.println(tripID + "Trip");
    }

    public void editFilesButtonPress(ActionEvent actionEvent) {
    }

    public void searchByStopItemPress(ActionEvent actionEvent) {
    }

    public void searchByRouteItemPress(ActionEvent actionEvent) {
    }

    public void searchByTripItemPress(ActionEvent actionEvent) {
    }

    /**
     * When searchbar is pressed it checks for which button is selected and searches by that ID
     * @param actionEvent when the user presses enter
     * @throws IOException For when it shows a stop
     * @author AJ Andersen
     */
    @FXML
    public void searchBarAction(ActionEvent actionEvent) throws IOException {
        if (filesImported) {
            if (searchBar.getText().equals("")) {
                showAlert("Please enter a valid search");
            } else {
                if (searchByStopRadio.isSelected()) {
                    searchByStop(searchBar.getText());
                } else if (searchByTripRadio.isSelected()) {
                    searchByTrip(searchBar.getText());
                } else if (searchByRouteRadio.isSelected()) {
                    searchByRoute(searchBar.getText());
                } else {
                    showAlert("Please select a valid option");
                }
            }
        }
    }

    /**
     * Shows stop page GUI
     */
    public void showStop() throws IOException {
        stopPageStage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }

    /**
     * Sets the stage for the StopPage
     * @param stopStage the stage for the StopPage
     * @author AJ Andersen
     */
    public void setStopStage(Stage stopStage) {
        this.stopPageStage = stopStage;
    }
}



