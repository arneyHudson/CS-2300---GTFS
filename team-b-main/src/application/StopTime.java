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

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * Represents a GTFS Stop Time
 */
public class StopTime implements Observer {

    private String stopID;
    private String tripID;
    private String stopSequence;
    private String arrivalTime;
    private String departTime;
    private boolean isPM;

    private final String[] requiredHeaderFields = {"stop_id", "trip_id", "stop_sequence", "arrival_time", "departure_time"};

    public StopTime() {
    }

    public StopTime(String stopID, String tripID, String stopSequence, String stopTime, String departTime) {
        this.stopID = stopID;
        this.tripID = tripID;
        this.stopSequence = stopSequence;
        this.arrivalTime = stopTime;
        this.departTime = departTime;
    }

    /**
     * Determines all possible trips associated to a stop and parses their arrival times to be compared to det. the next trip
     *
     * @param dataStructure where the data structures in which stops and trips are stored
     * @param stopTime StopTime object associated with a stopID spec. by the user
     * @param currentTime the current time to compare to all possible arrival times
     * @return a string associated with the next arriving trip at a spec. stop
     * @throws NoSuchElementException if a time cannot be matched to a trip
     * @author bassoe
     */
    public String getNextTrip(DataStructures dataStructure, StopTime stopTime, LocalTime currentTime) throws NoSuchElementException {
        LocalTime nextArrivalTime = getNextArrivalTime(dataStructure, stopTime.stopID, currentTime);
        String nextTripID = getNextTripID(dataStructure, nextArrivalTime, stopTime.stopID);
        return nextTripID;
    }

    /**
     * Gets the next Arrival time (in military format) for a stopID based off the current time
     *
     * @param stopID String associated with a stop object
     * @param currentTime current time specified by the user
     * @return closest arrival time in LocalTime format
     * @throws NoSuchElementException if a stop-trip pair doesn't exist for the stopID
     * @author bassoe
     */
    public LocalTime getNextArrivalTime(DataStructures dataStructure, String stopID, LocalTime currentTime) throws NoSuchElementException {

        if(!dataStructure.getStopTripMap().containsKey(stopID)) {
            throw new NoSuchElementException("No such stop for stop_id: " + stopID);
        }

        // gets all trips associated with spec. stopID
        LocalTime target;
        //String stopID = stopTime.stopID;
        ArrayList<StopTime> allStopTimes = new ArrayList<>();
        ArrayList<String> allStringArrTime = new ArrayList<>();

        ArrayList<String> allTrips = new ArrayList<>(dataStructure.getStopTripValue(stopID));
        allTrips.forEach(tripID -> allStopTimes.add(dataStructure.getStopTime(tripID)));
        allStopTimes.forEach(time -> allStringArrTime.add(time.getArrivalTime()));

        // creates arrayList for LocalTime stoptime obj
        ArrayList<LocalTime> localArrivalTimes = getLocalTime(allStringArrTime, currentTime);

        // sort arrival times
        localArrivalTimes = (ArrayList<LocalTime>) localArrivalTimes.stream().sorted().collect(Collectors.toList());

        target = localArrivalTimes.get(0);

        return target;
    }

    /**
     * Determines if a stop time is before or after the user specified time
     *
     * @param arrivalTime arrivalTime for a stop
     * @param currentTime time to compare with specified by the user
     * @return true if the time is after the current time, false if before
     * @author bassoe
     */
    private boolean isBeforeStopTime(LocalTime arrivalTime, LocalTime currentTime) {
        return currentTime.isBefore(arrivalTime);
    }

    /**
     * Gets the Integer hour value from an arrival time
     *
     * @param arrivalString String value of an arrival time
     * @return integer representing hour of arrival time
     * @author bassoe
     */
    public static int getHourValue(String arrivalString) {
        String hourStr = arrivalString.substring(0, arrivalString.indexOf(":"));
        return Integer.parseInt(hourStr);
    }

    /**
     * Converts String rep of military times into LocalTime objects in standard time format
     * and validates that they are after the spec. time
     *
     * @param arrivalTimes arrivalTimes of all trips for a stop in military time
     * @return a List of LocalTime objects in standard time
     * @author bassoe
     */
    private ArrayList<LocalTime> getLocalTime(ArrayList<String> arrivalTimes, LocalTime currentTime) {
        ArrayList<LocalTime> standardTimes = new ArrayList<>();
        ArrayList<LocalTime> tempTimes = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_TIME;
        LocalTime tempTime;
        int offSet = 0;

        for(String time : arrivalTimes) {

            int hourStr = getHourValue(time);
            if (hourStr > 23) {
                offSet = hourStr - 23;
                time = "0" + offSet + time.substring(time.indexOf(":"));
            }

            tempTime = LocalTime.parse(time, formatter);
            tempTimes.add(tempTime);

            if(isBeforeStopTime(tempTime, currentTime)) {
                standardTimes.add(tempTime);
            }
        }

        if(standardTimes.size() == 0) {
            standardTimes = tempTimes;
        }

        return standardTimes;
    }

    /**
     * Converts a LocalTime object from Military Time to Standard Time
     *
     * @param localTime LocalTime object formated in military time
     * @return LocalTime object formatted in standard time
     * @author bassoe
     */
    public LocalTime toStandardTime(LocalTime localTime) {
        if(localTime.getHour() > 12) {
            localTime = localTime.minusHours(12);
            isPM = true;

        } else if(localTime.getHour() == 0) {
            localTime = localTime.plusHours(12);
            isPM = false;
        }
        return localTime;
    }

    /**
     * Gets the next tripID associated with the next arrival time for a certian stop
     *
     * @param dataStructure where the data structures in which stops and trips are stored
     * @param nextArrivalTime the next arrival time for a stop based off the current time
     * @param stopID the stopID associated with a stop the user would like to view a trip for
     * @return tripID associated with the next trip arriving at a spec. stop
     * @throws NoSuchElementException if a time cannot be matched to any trips
     * @author bassoe
     */
    private String getNextTripID(DataStructures dataStructure, LocalTime nextArrivalTime, String stopID) throws NoSuchElementException {
        ArrayList<String> allTripID = new ArrayList<>(dataStructure.getStopTripValue(stopID));
        String nextTripID = null;
        String targetTime = null;
        boolean isNotFound = true;
        String strArrTime = nextArrivalTime.format(DateTimeFormatter.ISO_LOCAL_TIME);
        int counter = 0;

        for(int i = 0; i < allTripID.size() && isNotFound; ++i) {
            nextTripID = allTripID.get(i);
            targetTime = dataStructure.getStopTime(nextTripID).getArrivalTime();
            isNotFound = !strArrTime.equals(targetTime);
            counter++;
        }

        if(nextTripID != null && strArrTime.equals(targetTime)) {
            return nextTripID;
        } else {
            throw new NoSuchElementException("The Arrival time being searched for is not available within the data.");
        }
    }

    /**
     * Gets hashcode for a tripID when adding and retrieving from HashMap
     *
     * @return integer representing a hashcode for a tripID
     * @author bassoe
     */
    @Override
    public int hashCode() {
        String hashingValue = tripID;
        return hashingValue.hashCode();
    }

    @Override
    public String[] getRequiredHeaderFields() {
        return requiredHeaderFields;
    }

    /**
     * Gets a file header for a GTFS StopTime object for writing to a file
     *
     * @return ArrayList of Strings representing the fields of a file header
     */
    @Override
    public String getFileHeader() {
        return "trip_id,arrival_time,departure_time,stop_id,stop_sequence,stop_headsign,pickup_type,drop_off_type";
    }

    /**
     * Gets a body line for a specified GTFS file to be exported
     *
     * @return a single body line
     */
    @Override
    public String getBodyLine() {
        return this.tripID + "," + this.arrivalTime + "," + this.departTime  + "," + this.stopID + "," + this.stopSequence + ",,,";
    }

    public String getTripID() {
        return tripID;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public String getStopID() {
        return stopID;
    }

    public void setStopID(String stopID) {
        this.stopID = stopID;
    }

    /**
     * Determines if two StopTime objects are equal
     *
     * @param o the StopTime object to compare this stopTime object's attributes to
     * @return true if equal, false if not
     * @author bassoe
     */
    @Override
    public boolean equals(Object o) {
        boolean isEqual = false;
        if (o instanceof StopTime) {
            StopTime stopTime = (StopTime) o;
            isEqual = this.stopID.equals(((StopTime) o).stopID) &&
                    this.tripID.equals(((StopTime) o).tripID) &&
                    this.stopSequence.equals(((StopTime) o).stopSequence) &&
                    this.arrivalTime.equals(((StopTime) o).arrivalTime) &&
                    this.departTime.equals(((StopTime) o).departTime);
        }
        return isEqual;
    }

    public boolean isPM() {
        return isPM;
    }

    @Override
    public String toString() {
        return "stopID: " + stopID;
    }
}
