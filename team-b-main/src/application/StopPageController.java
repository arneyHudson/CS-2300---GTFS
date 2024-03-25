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
package application;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalTime;
import java.util.*;

public class StopPageController extends Controller implements Initializable {
    @FXML
    public Pane mainPane;

    @FXML
    public Button importFileButton;

    @FXML
    public Button exportFileButton;

    @FXML
    public Button editFilesButton;

    @FXML
    public Label stopIDLabel;

    @FXML
    public Label numberOfTrips;

    @FXML
    public TextField searchBar;

    @FXML
    public ToggleGroup searchGroup;

    @FXML
    public RadioButton searchByStopRadio;

    @FXML
    public RadioButton searchByRouteRadio;

    @FXML
    public RadioButton searchByTripRadio;

    @FXML
    public TextArea textArea;
    public Label nextTripLabel;

    private DataStructures dataStructure;
    private Stage mainStage;
    private Controller mainController;
    private int counter = 0;
    private Stage anotherStopPageStage;

    public static String stopID;

    /**
     * @author AJ Andersen
     */
    public StopPageController(){
        // instantiate DS when driver creates Controller object so only 1 DS is created
        this.dataStructure = Controller.dataStructure;
    }

    /**
     * Iterates through the LinkedList of trips and returns the number of trips for the specified stop
     * @param tripList The list that contains all trips for a specified stop
     * @return returns number of trips as an int
     * @throws NoSuchElementException if there are no trips populating the linkedlist for the specified GTFS object
     * @author AJ Andersen
     */
    private int getNumTrips(LinkedList<String> tripList) throws NoSuchElementException {
        if(tripList.isEmpty()) {
            throw new NoSuchElementException("There are no trips associated with the specified GTFS data");
        }
        Iterator<String> iterator = tripList.listIterator();
        int counter = 0;
        do {
            iterator.next();
            counter++;
        } while(iterator.hasNext());
        return counter;
    }

    /**
     * Creates a FileChooser object which is displayed to the user for importing file
     * @return FileChooser object
     */
    private FileChooser getFileChooser(String title) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(title);
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("stops", "*.txt"),
                new FileChooser.ExtensionFilter("trips", "*.txt"),
                new FileChooser.ExtensionFilter("stop_times", "*.txt"),
                new FileChooser.ExtensionFilter("routes", "*.txt"));
        return fileChooser;
    }


    /**
     * Handler for displaying a FileChooser for importing multiple files
     *
     * @param event The option for importing files is chosen
     */
    @FXML
    public void importFileButtonPress(ActionEvent event) {
        showPopUp("Select all Milwaukee files to import.");
        FileChooser fileChooser = getFileChooser("Select GTFS File(s) to Import");
        List<File> files = fileChooser.showOpenMultipleDialog(mainPane.getScene().getWindow());
        if(files != null){
            try {
                importFiles(files);
            } catch (IllegalArgumentException | FileNotFoundException invalidException) {
                showAlert(invalidException.getMessage());
            }
        }
    }


    /**
     * Handler for exporting GTFS files
     *
     * @param actionEvent user selects option to export GTFS Files
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
     */
    public void importFiles(List<File> files) throws IllegalArgumentException, FileNotFoundException {
        File stopTime = files.get(1);
        for (int i = 0; i < files.size(); ++i) {
            if (files.get(i).getName().equals("stop_times.txt")) {
                stopTime = files.get(i);
            } else {
                FileIO.read(files.get(i), this);
            }
        }
        FileIO.read(stopTime, this);

    }

    /**
     * Exports files form the GTFS program to a user's file system
     *
     * @param file File object associated with a file name to be exported
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
     */
    private void showAlert(String exceptionText) {
        Alert alert = new Alert(Alert.AlertType.ERROR, exceptionText);
        alert.setTitle("File Error");
        alert.showAndWait()
                .filter(response -> response == ButtonType.OK)
                .ifPresent(response -> alert.close());
    }

    public DataStructures getDataStructure() {
        return dataStructure;
    }

    /**
     * When enter is pressed in the search bar, the user will either be sent to the stop page, trip page* or route page*
     * *not yet implemented
     * @param actionEvent When enter is pressed in the searchbar
     * @author AJ Andersen
     */
    @FXML
    public void searchBarAction(ActionEvent actionEvent) throws IOException {
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

    /**
     * Sets up the stage for the StopPage
     * @param stopID The StopID being searched
     * @author AJ Andersen
     */
    @FXML
    public void searchByStop(String stopID) throws IOException {
        showStop();
    }

    @FXML
    public void searchByRoute(String routeID) {
        System.out.println(routeID + "Route");
    }

    @FXML
    public void searchByTrip(String tripID) {
        System.out.println(tripID + "Trip");
    }

    public void setMainController(Controller mainController) {
        this.mainController = mainController;
    }

    public void setMainStage(Stage stage) {
        this.mainStage = stage;
    }

    /**
     * Used to show update the page to show the desired stopID and the number of stops for that ID
     * @param mouseEvent The movement of the mouse that updates the page
     * @author AJ Andersen
     */
    public void Update(MouseEvent mouseEvent) {
        StopTime stopTime = new StopTime();
        stopTime.setStopID(stopID);
        counter ++;
        if (stopIDLabel != null && counter == 1){
            stopIDLabel.setText("Stop #" + stopID);
            numberOfTrips.setText("Number of trips: "+getNumTrips(dataStructure.getStopTripValue(stopID)));
            textArea.setText(dataStructure.getStopValue(stopID).getRoutes().toString());

            if(stopTime.isPM()) {
                nextTripLabel.setText("Next Trip: " + stopTime.getNextTrip(dataStructure, stopTime, LocalTime.now())
                        + " at " + stopTime.getNextArrivalTime(dataStructure, stopID, LocalTime.now()) + "pm");
            } else {
                nextTripLabel.setText("Next Trip: " + stopTime.getNextTrip(dataStructure, stopTime, LocalTime.now())
                        + " at " + stopTime.getNextArrivalTime(dataStructure, stopID, LocalTime.now()) + "am");
            }
        }
    }

    /**
     * Shows the searched stop in a new window
     * @throws IOException Throws IO exception if the reseource StopPage.fxml
     * @author AJ Andersen
     */
    @Override
    public void showStop() throws IOException {
        counter = 0;
        FXMLLoader stopPageLoader = new FXMLLoader();
        anotherStopPageStage = new Stage();
        Parent stopRoot = stopPageLoader.load(getClass().getResource("StopPage.fxml").openStream());

        StopPageController stopPageController = stopPageLoader.getController();
        stopPageController.setDataStructure(this.dataStructure);
        stopID = searchBar.getText();
        anotherStopPageStage.setTitle("Stop Page");
        anotherStopPageStage.setScene(new Scene(stopRoot));
        anotherStopPageStage.show();
    }

    /**
     * Sets the data structure to the same data structure that the main page uses
     * @param dataStructure the data structure that holds all routes, trips, stops, and stoptimes
     * @author AJ Andesrsen
     */
    public void setDataStructure(DataStructures dataStructure){
        this.dataStructure = dataStructure;
    }

    /**
     * Shows a popup with a selected message
     * @param message the specified message to be shown to the user
     * @author AJ Andersen
     */
    private void showPopUp(String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message);
        alert.setTitle("Hold up");
        alert.setHeaderText("Before you start...");
        alert.showAndWait();
    }
}
