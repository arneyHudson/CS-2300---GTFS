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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * @version 1.0
 */
public class DataStructures implements Subject {
    private HashMap<String, Stop> stopMap;
    private HashMap<String, Trip> tripMap;
    private HashMap<String, Route> routeMap;
    private HashMap<String, ArrayList<StopTime>> stopTimeMap;
    private HashMap<String, LinkedList<String>> stopTripMap;

    /**
     * creates data structures for storing and sorting GTFS objects
     */
    public DataStructures() {
        this.stopMap = new HashMap<>();
        this.tripMap = new HashMap<>();
        this.routeMap = new HashMap<>();
        this.stopTimeMap = new HashMap<>();
        this.stopTripMap = new HashMap<>();
    }

    public void addStopToMap(Stop stop) {
        stopMap.put(stop.getStopID(), stop);
    }

    public void addTripToMap(Trip trip) {
        tripMap.put(trip.getTripID(), trip);
    }

    public void addRouteToMap(Route route) {
        routeMap.put(route.getRouteID(), route);
    }

    /**
     * Adds a StopTime object to Map
     *
     * @param stopTime oject to be added
     * @author bassoe
     */
    public void addStopTimeToMap(StopTime stopTime) {
        if(stopTimeMap.containsKey(stopTime.getTripID())) {
            stopTimeMap.get(stopTime.getTripID()).add(stopTime);
        } else {
            stopTimeMap.put(stopTime.getTripID(), new ArrayList<>(List.of(stopTime)));
        }
        addRouteToStop(stopTime);
    }

    /**
     * Adds a route to a stop when a stopTime is added
     *
     * @author sopaj1
     * @param stopTime the stop time being looked at
     * @author sopaj
     */
    public void addRouteToStop(StopTime stopTime) {
        Trip trip = getTripValue(stopTime.getTripID());
        Route route = getRouteValue(trip.getRouteID());
        Stop stop = getStopValue(stopTime.getStopID());
        stop.addRoute(route);
    }

    /**
     * Adds a stop, trip map pair to a data structure
     *
     * @param stopID ID associated with a stop which is the Key for a map
     * @param tripID ID associated with a trip which is the value for a map
     * @author bassoe
     */
    public void addStopTripPairToMap(String stopID, String tripID) {
        if(stopTripMap.containsKey(stopID)) {
            stopTripMap.get(stopID).add(tripID);
        } else {
            stopTripMap.put(stopID, new LinkedList<>(List.of(tripID)));
        }
    }

    public Stop getStopValue(String stopID) {
        return stopMap.get(stopID);
    }

    public Trip getTripValue(String tripID) {
        return tripMap.get(tripID);
    }

    public Route getRouteValue(String routeID) {
        return routeMap.get(routeID);
    }

    public ArrayList<StopTime> getStopTimeValue(String tripID) {
        return stopTimeMap.get(tripID);
    }

    /**
     * Retrieves a StopTime object from a map
     *
     * @param tripID the trip ID associated with a StopTime object (Key)
     * @return the value if the mapping pair or the StopTime object
     * @author bassoe
     */
    public StopTime getStopTime(String tripID) {
        ArrayList<StopTime> stopTimeList = getStopTimeValue(tripID);
        boolean isFound = false;
        StopTime temp = null;

        for(int i = 0; i < stopTimeList.size() && !isFound; ++i)  {
            temp = stopTimeList.get(i);
            if(temp.getTripID().equals(tripID)) {
                isFound = true;
            }
        }
        return temp;
    }

    public LinkedList<String> getStopTripValue(String stopID) {
        return stopTripMap.get(stopID);
    }

    public HashMap<String, Stop> getStopMap() {
        return stopMap;
    }

    public HashMap<String, Trip> getTripMap() {
        return tripMap;
    }

    public HashMap<String, Route> getRouteMap() {
        return routeMap;
    }

    public HashMap<String, ArrayList<StopTime>> getStopTimeMap() {
        return stopTimeMap;
    }

    public HashMap<String, LinkedList<String>> getStopTripMap() {
        return stopTripMap;
    }

    public int getNumStops() {
        return stopMap.size();
    }

    public int getNumTrips() {
        return tripMap.size();
    }

    /**
     * Gets the number of StopTimes for all trip IDs
     *
     * @return the total number of StopTimes in the data structure
     * @author bassoe
     */
    public int getNumStopTimes() {
        Set<String> tripIDSet = stopTimeMap.keySet();
        Iterator<String> iterator = tripIDSet.iterator();
        int counter = 0;
        ArrayList<StopTime> stopTimeTemp;

        do {
           stopTimeTemp = stopTimeMap.get(iterator.next());
           counter += stopTimeTemp.size();

        } while(iterator.hasNext());


        return counter;
    }

    public int getNumRoutes() {
        return routeMap.size();
    }


    @Override
    public String toString() {
        return "stops sz: " + stopMap.size() +
                "\ntrips sz: " + tripMap.size() +
                "\nroute sz: " + routeMap.size() +
                "\nstoptime sz: " + getNumStopTimes() +
                "\nstop-trip sz: " + stopTripMap.size();
    }



}
