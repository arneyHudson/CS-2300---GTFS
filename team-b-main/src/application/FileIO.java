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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

/**
 * Utility class for importing, exporting, and validating files
 */
public class FileIO {

    private static DataStructures dataStructure;

    /**
     * Validates, sorts, and reads imported files
     *
     * @param file an imported file
     * @throws IllegalArgumentException if the header or file name is invalid
     * @throws FileNotFoundException if there is a problem reading from the file
     * @author bassoe
     */
    public static void read(File file, Controller controller) throws IllegalArgumentException, FileNotFoundException {
        dataStructure = controller.getDataStructure();
        try(Scanner fileReader = new Scanner(file)) {
            ArrayList<String> fileHeader = parseFileLine(fileReader.nextLine());
            if (isValidFileHeader(file, fileHeader)) {
                createObjects(parseEntireFile(fileReader, file), file, fileHeader);
            } else {
                throw new IllegalArgumentException("Invalid Header");
            }
        }
    }

    /**
     * Writes a GTFS File for Exporting
     *
     * @param file the file object associated with the file to be written for exporting
     * @throws FileNotFoundException if the file object associated with the File to be exported is invalid
     * @author bassoe
     */
    public static boolean write(File file) throws FileNotFoundException {
        try(PrintWriter writer = new PrintWriter(file)) {
            writer.println(writeHeader(file));
            writeToFileType(file, writer);
        }
        return true;
    }

    /**
     * Writes a GTFS File header for exporting
     *
     * @param file file object associated with the file to be written for exporting
     * @return String representing the file header for a specified file type
     * @author bassoe
     */
    private static String writeHeader(File file) {
        String fileName = file.getName();
        String fileHeader;
        switch (fileName) {
            case "stops.txt" -> fileHeader = new Stop().getFileHeader();
            case "trips.txt" -> fileHeader = new Trip().getFileHeader();
            case "stop_times.txt" -> fileHeader = new StopTime().getFileHeader();
            case "routes.txt" -> fileHeader = new Route().getFileHeader();
            default -> throw new IllegalArgumentException("Invalid file type");
        }
        return fileHeader;
    }

    /**
     * Stores all the written lines of a file to be exported in a 2D ArrayList
     *
     * @param file File to export
     * @param writer for writing to a GTFS for exporting
     * @author bassoe
     */
    private static void writeToFileType(File file, PrintWriter writer) {
        String fileName = file.getName();
        switch (fileName) {
            case "stops.txt" -> writeToStopFile(writer, dataStructure.getStopMap());
            case "trips.txt" -> writeToTripFile(writer, dataStructure.getTripMap());
            case "stop_times.txt" -> writeToStopTimeFile(writer, dataStructure.getStopTimeMap());
            case "routes.txt" -> writeToRouteFile(writer, dataStructure.getRouteMap());
            default -> throw new IllegalArgumentException("Invalid file type");
        }
    }

    /**
     * Parses an individual line of a file into readable data
     *
     * @param line String containing data from a single line within the file
     * @return a collection of Strings representing a line in a file
     * @author arneyh
     */
    private static ArrayList<String> parseFileLine(String line) {
        ArrayList<String> lineList = new ArrayList<>();
        String[] lineArray = line.split(",", -1);
        lineList.addAll(Arrays.asList(lineArray));
        return lineList;
    }

    /**
     * Parses a file body and validates the data within each line
     *
     * @param fileReader Scanner object used to read the txt file
     * @return a 2D ArrayList containing all lines of a file
     * @author bassoe
     */
    private static ArrayList<ArrayList<String>> parseEntireFile(Scanner fileReader, File file) throws IllegalArgumentException {
        ArrayList<ArrayList<String>> allFileLines = new ArrayList<>();
        ArrayList<String> tempLineList;
        String line;
        do {
            line = fileReader.nextLine();

            if(isValidFileBodyLine(file, line)) {
                tempLineList = parseFileLine(line);
                allFileLines.add(tempLineList);
            }

        } while(fileReader.hasNextLine());

        return allFileLines;
    }

    /**
     * Validates the data within an individual file body line
     *
     * @param fileBodyLine ranging the 2nd to last line within a file
     * @return true if the required data is present and formatted properly and false otherwise
     * @author arneyh
     */
    public static boolean isValidFileBodyLine(File file, String fileBodyLine) throws IllegalArgumentException {
        String fileName = file.getName();
        return switch (fileName) {
            case "stops.txt" -> isStopValidFileBodyLine(fileBodyLine);
            case "trips.txt" -> isTripValidFileBodyLine(fileBodyLine);
            case "stop_times.txt" -> isStopTimeValidFileBodyLine(fileBodyLine);
            case "routes.txt" -> isRouteValidFileBodyLine(fileBodyLine);
            default -> throw new IllegalArgumentException("Attempted to use Invalid File");
        };
    }

    /**
     * helper method for isValidBodyLine that tests if a stop file is valid
     *
     * @param fileBodyLine a line from the route file
     * @return true if the file is valid, false if it is not
     * @author arneyh edited by bassoe
     */
    public static boolean isStopValidFileBodyLine(String fileBodyLine) {
        String[] checkArray = fileBodyLine.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);

        boolean hasReqFields = !(checkArray[0].equals("") || checkArray[3].equals("") || checkArray[4].equals(""));

        return hasReqFields &&
                (Double.parseDouble(checkArray[3]) > -90 && Double.parseDouble(checkArray[3]) < 90) &&
                (Double.parseDouble(checkArray[4]) > -180 && Double.parseDouble(checkArray[4]) < 180);
    }

    /**
     * helper method for isValidBodyLine that tests if a trip file is valid
     *
     * @param fileBodyLine a line from the route file
     * @return true if the file is valid, false if it is not
     * @author arneyh
     */
    public static boolean isTripValidFileBodyLine(String fileBodyLine) {
        String[] checkArray = fileBodyLine.split(",");
        return !(checkArray[0].equals("") || checkArray[2].equals(""));
    }

    /**
     * helper method for isValidBodyLine that tests if a stopTime file is valid
     *
     * @param fileBodyLine a line from the route file
     * @return true if the file is valid, false if it is not
     * @author arneyh edited by bassoe
     */
    public static boolean isStopTimeValidFileBodyLine(String fileBodyLine) {
        String[] checkArray = fileBodyLine.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);

        boolean areReqPresent = !(checkArray[0].equals("") ||
                checkArray[1].equals("") ||
                checkArray[2].equals("") ||
                checkArray[3].equals("") ||
                checkArray[4].equals(""));

        boolean isValidStopID = dataStructure.getStopMap().containsKey(checkArray[3]);
        boolean isValidStopSequ = Integer.parseInt(checkArray[4]) >= 0;


        return areReqPresent && isValidArrDepTimes(checkArray[1], checkArray[2]) && isValidStopID && isValidStopSequ;

    }

    /**
     * Determines if strings from a file line are valid arrival and departure times
     *
     * @param arrivalTime time of arrival for stop time object
     * @param departTime time of departure for stop time object
     * @return true if both arrival and depart times are in range
     * @author bassoe
     */
    private static boolean isValidArrDepTimes(String arrivalTime, String departTime) {
        int arrHour = StopTime.getHourValue(arrivalTime);
        int depHour = StopTime.getHourValue(departTime);

        return arrHour < 24 && arrHour >= 0 &&
                depHour < 24 && depHour >= 0;

    }

    /**
     * helper method for isValidBodyLine that tests if a route file is valid
     *
     * @param fileBodyLine a line from the route file
     * @return true if the file is valid, false if it is not
     * @author arneyh
     */
    public static boolean isRouteValidFileBodyLine(String fileBodyLine) {
        String[] checkArray = fileBodyLine.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
        return !(checkArray[0].equals("") || checkArray[7].equals(""));
    }

    /**
     * Reads the header of the imported file to determine the type of GTFS data it holds
     *
     * @param fileHeader an ArrayList of fields from the file's header
     * @return true if the header specifies the required fields
     * @throws IllegalArgumentException if the file type is not specified by the program
     * @author bassoe
     */
    private static boolean isValidFileHeader(File file, ArrayList<String> fileHeader) throws IllegalArgumentException {
        String fileName = file.getName();
        boolean isValidHeader;
        switch (fileName) {
            case "stops.txt" -> isValidHeader = areRequiredFieldsPresent(fileHeader, new Stop().getRequiredHeaderFields());
            case "trips.txt" -> isValidHeader = areRequiredFieldsPresent(fileHeader, new Trip().getRequiredHeaderFields());
            case "stop_times.txt" -> isValidHeader = areRequiredFieldsPresent(fileHeader, new StopTime().getRequiredHeaderFields());
            case "routes.txt" -> isValidHeader = areRequiredFieldsPresent(fileHeader, new Route().getRequiredHeaderFields());
            default -> throw new IllegalArgumentException("Invalid file type");
        }
        return isValidHeader;
    }

    /**
     * Creates all stop, route, trip, and stop time objects
     *
     * @param allBodyLines the 2nd to last lines within a file
     * @param file the imported file
     * @param fileHeader 1st line of a file containing its fields
     * @throws IllegalArgumentException if the file name does not match one specified by the program
     * @author created by arneyh, modified by bassoe
     */
    private static void createObjects(ArrayList<ArrayList<String>> allBodyLines, File file, ArrayList<String> fileHeader) throws IllegalArgumentException {
        String fileName = file.getName();
        switch (fileName) {
            case "stops.txt" -> createStop(allBodyLines, fileHeader);
            case "trips.txt" -> createTrip(allBodyLines, fileHeader);
            case "routes.txt" -> createRoute(allBodyLines, fileHeader);
            case "stop_times.txt" -> createStopTime(allBodyLines, fileHeader);

            default -> throw new IllegalArgumentException("Invalid file type");
        }
    }

    /**
     * Extracts single attributes from a file line
     *
     * @param fileHeader header of a file containing it's indexed fields
     * @param requiredHeaderField the fields that will be stored in GTFS objects
     * @param line a single body line from a file
     * @return a String representing a GTFS attribute
     * @author bassoe
     */
    private static String getRequiredField(ArrayList<String> fileHeader,
                                           String requiredHeaderField,
                                           ArrayList<String> line) {
        int index = fileHeader.indexOf(requiredHeaderField);
        return line.get(index);
    }

    /**
     * Creates a Stop object
     *
     * @param allBodyLines the 2nd to last lines within a file
     * @param fileHeader 1st line of a file containing its fields
     * @author created by arneyh, modified by bassoe
     */
    public static void createStop(ArrayList<ArrayList<String>> allBodyLines, ArrayList<String> fileHeader) {
        String stopID;
        String stopLat;
        String stopLon;
        Stop stop;

        for(ArrayList<String> line : allBodyLines) {
            stopID = getRequiredField(fileHeader, "stop_id", line);
            stopLat = getRequiredField(fileHeader, "stop_lat", line);
            stopLon = getRequiredField(fileHeader, "stop_lon", line);
            stop = new Stop(stopID, stopLat, stopLon);
            dataStructure.addStopToMap(stop);
        }
    }

    /**
     * Creates a Trip object
     *
     * @param allBodyLines the 2nd to last lines within a file
     * @param fileHeader 1st line of a file containing its fields
     * @author created by arneyh, modified by bassoe
     */
    private static void createTrip(ArrayList<ArrayList<String>> allBodyLines, ArrayList<String> fileHeader) {
        String routeID;
        String tripID;
        Trip trip;

        for(ArrayList<String> line : allBodyLines) {
            routeID = getRequiredField(fileHeader, "route_id", line);
            tripID = getRequiredField(fileHeader, "trip_id", line);
            routeID = routeID.replaceAll(" ", "");
            tripID = tripID.replaceAll(" ", "");
            trip = new Trip(routeID, tripID);
            dataStructure.addTripToMap(trip);
        }
    }

    /**
     * Creates a StopTimes object
     *
     * @param allBodyLines the 2nd to last lines within a file
     * @param fileHeader 1st line of a file containing its fields
     * @author created by arneyh, modified by bassoe
     */
    private static void createStopTime(ArrayList<ArrayList<String>> allBodyLines, ArrayList<String> fileHeader) {
        String stopID;
        String tripID;
        String stopSequence;
        String arrivalTime;
        String departTime;
        StopTime stopTime;

        for(ArrayList<String> line : allBodyLines) {
            stopID = getRequiredField(fileHeader, "stop_id", line);
            tripID = getRequiredField(fileHeader, "trip_id", line);
            arrivalTime = getRequiredField(fileHeader, "arrival_time", line);
            departTime = getRequiredField(fileHeader, "departure_time", line);
            stopSequence = getRequiredField(fileHeader, "stop_sequence", line);
            stopTime = new StopTime(stopID, tripID, stopSequence, arrivalTime, departTime);
            dataStructure.addStopTimeToMap(stopTime);
            dataStructure.addStopTripPairToMap(stopID, tripID);

        }
    }

    /**
     * Creates a Route object
     *
     * @param allBodyLines the 2nd to last lines within a file
     * @param fileHeader 1st line of a file containing its fields
     * @author created by arneyh, modified by bassoe
     */
    private static void createRoute(ArrayList<ArrayList<String>> allBodyLines, ArrayList<String> fileHeader) {
        String routeID;
        String routeType;
        Route route;

        for(ArrayList<String> line : allBodyLines) {
            routeID = getRequiredField(fileHeader, "route_id", line);
            routeType = getRequiredField(fileHeader, "route_type", line);
            routeID = routeID.replaceAll(" ", "");
            routeType = routeType.replaceAll(" ", "");
            route = new Route(routeID, routeType);
            dataStructure.addRouteToMap(route);

        }
    }

    /**
     * Writes a Stop object to a file
     *
     * @param writer PrintWriter for writing to a file
     * @param stopMap data structure for retrieving objects from
     * @author bassoe
     */
    private static void writeToStopFile(PrintWriter writer, HashMap<String, Stop> stopMap) {
        Set<String> idSet = stopMap.keySet();
        Iterator<String> iterator = idSet.iterator();
        String writeLine;
        Stop temp;

        do {
            temp = stopMap.get(iterator.next());
            writeLine = temp.getBodyLine();
            writer.println(writeLine);
        } while(iterator.hasNext());
    }

    /**
     * Writes a Trip object to a file
     *
     * @param writer PrintWriter for writing to a file
     * @param tripMap data structure for retrieving objects from
     * @author bassoe
     */
    private static void writeToTripFile(PrintWriter writer, HashMap<String, Trip> tripMap) {
        Set<String> idSet = tripMap.keySet();
        Iterator<String> iterator = idSet.iterator();
        String writeLine;
        Trip temp;

        do {
            temp = tripMap.get(iterator.next());
            writeLine = temp.getBodyLine();
            writer.println(writeLine);
        } while(iterator.hasNext());
    }

    /**
     * Writes a StopTime object to a file
     *
     * @param writer PrintWriter for writing to a file
     * @param stopTimeMap data structure for retrieving objects from
     * @author bassoe
     */
    private static void writeToStopTimeFile(PrintWriter writer, HashMap<String, ArrayList<StopTime>> stopTimeMap) {
        int counter = 0;

        Set<String> idSet = stopTimeMap.keySet();

        Iterator<String> iterator = idSet.iterator();

        ArrayList<StopTime> allStopTimesForTripID;

        do {
            allStopTimesForTripID = stopTimeMap.get(iterator.next());
            counter += writeStopTimeTripMappings(writer, allStopTimesForTripID);

        } while(iterator.hasNext());
    }

    /**
     * Writes StopTime and Trip objects to a file
     *
     * @param writer PrintWriter for writing to a file
     * @param allStopTimesForTripID data structure for retrieving objects from
     * @return the total number of lines written
     * @author bassoe
     */
    private static int writeStopTimeTripMappings(PrintWriter writer, ArrayList<StopTime> allStopTimesForTripID) {
        Iterator<StopTime> iterator = allStopTimesForTripID.iterator();
        String stopTimeLine;
        StopTime tempStopTime;
        int counter = 0;
        do {
            counter++;
            tempStopTime = iterator.next();
            stopTimeLine = tempStopTime.getBodyLine();
            writer.println(stopTimeLine);

        } while(iterator.hasNext());
        return counter;
    }

    /**
     * Writes a Route object to a file
     *
     * @param writer PrintWriter for writing to a file
     * @param routeMap data structure for retrieving objects from
     * @author bassoe
     */
    private static void writeToRouteFile(PrintWriter writer, HashMap<String, Route> routeMap) {
        Set<String> idSet = routeMap.keySet();
        Iterator<String> iterator = idSet.iterator();
        String writeLine;
        Route temp;

        do {
            temp = routeMap.get(iterator.next());
            writeLine = temp.getBodyLine();
            writer.println(writeLine);
        } while(iterator.hasNext());
    }

    /**
     * Checks to see if all elements from the required header fields
     * are contained in the file header
     *
     * @param fileHeader an ArrayList of fields from the file's header
     * @param requiredHeaderFields the required fields a header must contain
     *                             specified by the file type
     * @return true if the header contains all the required fields and false if it does not
     * @author bassoe
     */
    private static boolean areRequiredFieldsPresent(ArrayList<String> fileHeader, String[] requiredHeaderFields) {
        ArrayList<String> reqHeaderFieldsArr = new ArrayList<>(Arrays.asList(requiredHeaderFields));
        return fileHeader.containsAll(reqHeaderFieldsArr);
    }
}
