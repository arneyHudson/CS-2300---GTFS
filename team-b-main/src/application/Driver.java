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

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Driver class responsible for triggering the GUI to be displayed to the user
 */
public class Driver extends Application {
    /**
     * Method for testing purposes
     * @param args args
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader mainLoader = new FXMLLoader();
        Parent root = mainLoader.load(getClass().getResource("GTFSBoundary.fxml").openStream());
        stage.setTitle("Main");
        stage.setScene(new Scene(root));
        stage.show();
        Controller mainController = mainLoader.getController();
        mainController.setMainPane((Pane)root);

        FXMLLoader stopPageLoader = new FXMLLoader();
        Stage stopStage = new Stage();
        Parent stopRoot = stopPageLoader.load(getClass().getResource("StopPage.fxml").openStream());

        StopPageController stopPageController = stopPageLoader.getController();
        stopStage.setTitle("Stop Page");
        stopStage.setScene(new Scene(stopRoot));
        stopStage.hide();

        mainController.setStopStage(stopStage);

        stopPageController.setMainStage(stage);
        stopPageController.setMainController(mainController);
    }


}
