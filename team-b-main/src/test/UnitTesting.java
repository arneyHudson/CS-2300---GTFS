package test;

import application.DataStructures;
import application.FileIO;
import application.Route;
import application.Stop;
import application.Trip;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import application.Controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Class that contains tests for certain features
 */
public class UnitTesting {

    private DataStructures dataStructures = new DataStructures();
    private Controller controller;
    private File stopFile = new File("gtfsFiles/GTFS_MCTS/stops.txt");
    private File tripFile = new File("gtfsFiles/GTFS_MCTS/trips.txt");
    private File routeFile = new File("gtfsFiles/GTFS_MCTS/routes.txt");
    private File stopTimeFile = new File("gtfsFiles/GTFS_MCTS/stop_times.txt");

    // Chicago files
    private File stopFileChi = new File("gtfsFiles/GTFS_Chicago/routes.txt");
    private File tripFileChi = new File("gtfsFiles/GTFS_Chicago/trips.txt");
    private File routeFileChi = new File("gtfsFiles/GTFS_Chicago/routes.txt");
    private File stopTimeFileChi = new File("gtfsFiles/GTFS_Chicago/stop_times.txt");
    private ArrayList<File> mkeFiles;
    private ArrayList<File> chiFiles;


    @BeforeEach
    void setUp() {
        controller = new Controller();
        mkeFiles = new ArrayList<>();
        chiFiles = new ArrayList<>();

        mkeFiles.add(stopFile);
        mkeFiles.add(tripFile);
        mkeFiles.add(routeFile);
        mkeFiles.add(stopTimeFile);

        chiFiles.addAll(Arrays.asList(stopFileChi, tripFileChi, routeFileChi, stopTimeFileChi));

    }

    // Validating Files Tests

    /**
     * @author arneyh
     * Validating Stop Files Test - Uses a random line from a stop file and tests if it is valid
     */
    @Test
    @DisplayName("Validate Stop Files Test")
    void validateStopFiles() {
        String fileLine = "6712,STATE & 5101 #6712,,43.0444475,-87.9779369";
        try {
            assertTrue(FileIO.isStopValidFileBodyLine(fileLine));
        } catch (Exception e) {
            System.out.println("error");
        }
    }


    /**
     * @author arneyh
     * Validating Trip Files Test - Uses a random line from a trip file and tests if it is valid
     */
    @Test
    @DisplayName("Validate Trip Files Test")
    void validateTripFiles() {
        String fileLine = "64,17-SEP_SUN,21736571_2549,60TH-VLIET,0,64102,17-SEP_64_0_23";
        try {
            assertTrue(FileIO.isTripValidFileBodyLine(fileLine));
        } catch (Exception e) {
            System.out.println("error");
        }
    }

    /**
     * @author arneyh
     * Validating StopTime Files Test - Uses a random line from a stopTime file and tests if it is valid
     */
    @Test
    @DisplayName("Validate Stop Time Files Test")
    void validateStopTimeFiles() {
        String fileLine = "21736564_2535,09:01:00,09:01:00,6279,19,,0,0";
        try {
            assertTrue(FileIO.isStopTimeValidFileBodyLine(fileLine));
        } catch (Exception e) {
            System.out.println("error");
        }
    }

    /**
     * @author arneyh
     * Validating Route Files Test - Uses a random line from a route file and tests if it is valid
     */
    @Test
    @DisplayName("Validate Route Files Test")
    void validateRouteFiles() {
        String fileLine = "40,MCTS,40,College-Ryan Flyer,,3,,008345,";
        try {
            assertTrue(FileIO.isRouteValidFileBodyLine(fileLine));
        } catch (Exception e) {
            System.out.println("error");
        }
    }

    // Importing Files Tests
    @Test
    @DisplayName("Import Stop Files Test")
    void importStopFiles() {
        int numStops = 5392;
        Stop randomStop = new Stop();
        try {
            controller.importFiles(mkeFiles);
            Assertions.assertEquals(numStops, controller.getDataStructure().getStopMap().size());
        } catch (Exception e) {
            System.out.println("error");
        }
    }

    @Test
    @DisplayName("Import Trip Files Test")
    void importTripFiles() {
        int numTrips = 9300;
        try {
            controller.importFiles(mkeFiles);
            Assertions.assertEquals(numTrips, controller.getDataStructure().getTripMap().size());
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Test
    @DisplayName("Import Stop Time Files Test")
    void importStopTimeFiles() {
        int numStopTimes = 659723;
        try {
            controller.importFiles(mkeFiles);
            Assertions.assertEquals(numStopTimes, controller.getDataStructure().getStopTimeMap().size());
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    @Test
    @DisplayName("Import Route Files Test")
    void importRouteFiles() {
        int numRoutes = 62;
        try {
            controller.importFiles(mkeFiles);
            Assertions.assertEquals(numRoutes, dataStructures.getNumRoutes());
        } catch (FileNotFoundException invalidFileExc) {
            System.out.println("Route File for testing import not found");
        }
    }

    // Exporting Files Test
    @Test
    @DisplayName("Export Stop Files Test")
    void exportStopFiles() {
//        try {
//            // Importing a single stops.txt file for data
//            FileIO.read(stopFile, controller);
//            // exporting the data from the imported stops.txt file
//            controller.exportFiles(new File("gtfsFiles/gtfsExportedFiles"));
//            // attempting to import the file which was exported
//            controller.importFiles(new ArrayListnew File("gtfsFiles/gtfsExportedFiles/stops.txt"));
//        } catch(FileNotFoundException invalidFileExc) {
//            System.out.println("Stop File for testing export not found");
//        }

    }

    @Test
    @DisplayName("Routes in a stop")
    void routesInStop() {

        try {
            controller.importFiles(mkeFiles);
            Assertions.assertEquals(new Route("28", "10th Street").getRouteID(),
                    controller.getDataStructure().getStopValue("4132").getRoutes().get("28").getRouteID());
        } catch (Exception e){
            System.out.println(e);
        }
    }

    @Test
    @DisplayName("Export Trips Files Test")
    void exportTripFiles() {

    }

    @Test
    @DisplayName("Export Stop Time File Test")
    void exportStopTimeFiles() {

    }

    @Test
    @DisplayName("Export Route Files Test")
    void exportRouteFiles() {

    }

    // Feature 4
    @Test
    @DisplayName("Number of Trips each Stop is found on")
    void numTrips() {

    }

    /**
     * Validates that the number of Route objects matches the number of routeIDs within the route objects
     *
     * @author bassoe
     */
    @Test
    @DisplayName("Number of routes vs. valid routeIDs")
    void numValidRoutes() {
        Route tempRoute;

        try {
            controller.importFiles(mkeFiles);
            int numRouteObj = dataStructures.getNumRoutes();
            int numRouteID = 0;
            Set<String> allRoutes = dataStructures.getRouteMap().keySet();
            for (String routeID : allRoutes) {
                tempRoute = dataStructures.getRouteValue(routeID);
                if (tempRoute.getRouteID() != null && !tempRoute.getRouteID().equals("")) {
                    numRouteID++;
                }
            }
            Assertions.assertEquals(numRouteID, numRouteObj);
        } catch (FileNotFoundException invalidFileExc) {
            System.out.println("Route File for testing import not found");
        }

        dataStructures = new DataStructures();

        try {
            controller.importFiles(chiFiles);
            int numRouteObj = dataStructures.getNumRoutes();
            int numRouteID = 0;
            Set<String> allRoutes = dataStructures.getRouteMap().keySet();
            for (String routeID : allRoutes) {
                tempRoute = dataStructures.getRouteValue(routeID);
                if (tempRoute.getRouteID() != null && !tempRoute.getRouteID().equals("")) {
                    numRouteID++;
                }
            }
            Assertions.assertEquals(numRouteID, numRouteObj);
        } catch (FileNotFoundException invalidFileExc) {
            System.out.println("Route File for testing import not found");
        }
    }

    /**
     * Validates that the number of Stop objects matches the number of routeIDs within the route objects
     *
     * @author bassoe
     */
    @Test
    @DisplayName("Number of stops vs. valid stopIDs")
    void numValidStops() {
        Stop tempStop;

        try {
            controller.importFiles(mkeFiles);
            int numStopObj = dataStructures.getNumStops();
            int numStopID = 0;
            Set<String> allStops = dataStructures.getStopMap().keySet();
            for (String stopID : allStops) {
                tempStop = dataStructures.getStopValue(stopID);
                if (tempStop.getStopID() != null && !tempStop.getStopID().equals("")) {
                    numStopID++;
                }
            }
            Assertions.assertEquals(numStopID, numStopObj);
        } catch (FileNotFoundException invalidFileExc) {
            System.out.println("Stop File for testing import not found");
        }

        dataStructures = new DataStructures();

        try {
            controller.importFiles(mkeFiles);
            int numStopObj = dataStructures.getNumStops();
            int numStopID = 0;
            Set<String> allStops = dataStructures.getStopMap().keySet();
            for (String stopID : allStops) {
                tempStop = dataStructures.getStopValue(stopID);
                if (tempStop.getStopID() != null && !tempStop.getStopID().equals("")) {
                    numStopID++;
                }
            }
            Assertions.assertEquals(numStopID, numStopObj);
        } catch (FileNotFoundException invalidFileExc) {
            System.out.println("Stop File for testing import not found");
        }
    }

    /**
     * Validates that the number of Trip objects matches the number of tripIDs within the Trip objects
     *
     * @author bassoe
     */
    @Test
    @DisplayName("Number of trips vs. valid tripIDs")
    void numValidTrips() {
        Trip tempTrip;

        try {
            controller.importFiles(mkeFiles);
            int numTripObj = dataStructures.getNumTrips();
            int numTripID = 0;
            Set<String> allTrips = dataStructures.getTripMap().keySet();
            for (String tripID : allTrips) {
                tempTrip = dataStructures.getTripValue(tripID);
                if (tempTrip.getTripID() != null && !tempTrip.getTripID().equals("")) {
                    numTripID++;
                }
            }
            Assertions.assertEquals(numTripID, numTripObj);
        } catch (FileNotFoundException invalidFileExc) {
            System.out.println("Trip File for testing import not found.");
        }

        dataStructures = new DataStructures();

        try {
            controller.importFiles(chiFiles);
            int numTripObj = dataStructures.getNumTrips();
            int numTripID = 0;
            Set<String> allTrips = dataStructures.getTripMap().keySet();
            for (String tripID : allTrips) {
                tempTrip = dataStructures.getTripValue(tripID);
                if (tempTrip.getTripID() != null && !tempTrip.getTripID().equals("")) {
                    numTripID++;
                }
            }
            Assertions.assertEquals(numTripID, numTripObj);
        } catch (FileNotFoundException invalidFileExc) {
            System.out.println("Trip File for testing import not found.");
        }
    }

    /**
     * Validates that the number of Trip objects matches the number of routeIDs within the Trip objects
     *
     * @author bassoe
     */
    @Test
    @DisplayName("All Trip objects contain routeIDs")
    void tripContainRouteID() {
        Trip tempTrip;

        try {
            controller.importFiles(mkeFiles);
            Set<String> allTripID = dataStructures.getTripMap().keySet();
            int numRouteID = 0;
            int numTrips = dataStructures.getNumTrips();

            for(String tripID : allTripID) {
                tempTrip = dataStructures.getTripValue(tripID);
                if(tempTrip.getTripID() != null && !tempTrip.getRouteID().equals("")) {
                    numRouteID++;
                }
                Assertions.assertEquals(numRouteID, numTrips);
            }

        } catch(FileNotFoundException noFileExc) {
            System.out.println("No File found for testing routeID in Trips");
        }

        dataStructures = new DataStructures();

        try {
            controller.importFiles(chiFiles);
            Set<String> allTripID = dataStructures.getTripMap().keySet();
            int numRouteID = 0;
            int numTrips = dataStructures.getNumTrips();

            for(String tripID : allTripID) {
                tempTrip = dataStructures.getTripValue(tripID);
                if(tempTrip.getTripID() != null && !tempTrip.getRouteID().equals("")) {
                    numRouteID++;
                }
                Assertions.assertEquals(numRouteID, numTrips);
            }

        } catch(FileNotFoundException noFileExc) {
            System.out.println("No File found for testing routeID in Trips");
        }

    }
}
