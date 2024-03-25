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

import java.util.HashMap;

/**
 * Represents a GTFS Stop
 *
 * @author bassoe
 * @version 1.0
 */
public class Stop implements Observer {

    private int numOfTrips;
    private HashMap<String, Route> routes;
    private HashMap<Trip, StopTime> trips;
    public Route mRoute;
    private String stopID;
    private String stopLat;
    private String stopLon;
    private final String[] requiredHeaderFields = {"stop_id", "stop_lon", "stop_lon"};


    public Stop(int numOfTrips, HashMap<String, Route> routes,
                String stopID, HashMap<Trip, StopTime> trips, Route m_Route) {
        this.numOfTrips = numOfTrips;
        this.routes = routes;
        this.stopID = stopID;
        this.trips = trips;
        this.mRoute = m_Route;
    }

    /**
     * Constructor that aligns with txt file
     *
     * @param stopID stopID
     */
    public Stop(String stopID, String stopLat, String stopLon) {
        this.routes = new HashMap<>();
        this.stopID = stopID;
        this.stopLat = stopLat;
        this.stopLon = stopLon;
    }

    public Stop() {
    }

    /**
     * Gets hashcode for a stopID when adding and retrieving from HashMap
     *
     * @return integer representing a hashcode for a stopID
     * @author bassoe
     */
    @Override
    public int hashCode() {
        return stopID.hashCode();
    }

    /**
     * Determines if two Stop objects are equal
     *
     * @param o the Stop object to compare this Stop object's attributes to
     * @return true if equal, false if not
     * @author bassoe
     */
    @Override
    public boolean equals(Object o) {
        boolean isEqual = false;
        if (o instanceof Stop) {
            Stop stop = (Stop) o;
            isEqual = this.stopID.equals(((Stop) o).stopID) &&
                    this.stopLat.equals(((Stop) o).stopLat) &&
                    this.stopLon.equals(((Stop) o).stopLon);
        }
        return isEqual;
    }

    /**
     * Specifies which Strings should be present in the file header
     * @return Strings representing fields in the header
     */
    @Override
    public String[] getRequiredHeaderFields() {
        return requiredHeaderFields;
    }

    /**
     * Gets a file header for a GTFS Stop object for writing to a file
     *
     * @return ArrayList of Strings representing the fields of a file header
     */
    @Override
    public String getFileHeader() {
        return "stop_id,stop_name,stop_desc,stop_lat,stop_lon";
    }

    /**
     * Gets a body line for a specified GTFS file to be exported
     *
     * @return a single body line
     */
    @Override
    public String getBodyLine() {
        return this.stopID + ",,," + this.stopLat + "," + this.stopLon;
    }

    public void addRoute(Route route) {
        routes.put(route.getRouteID(), route);
    }

    public HashMap<String, Route> getRoutes() {
        return routes;
    }

    public String getStopID() {
        return stopID;
    }

    public void setRoute(HashMap<String, Route> route) {
        this.routes = route;
    }

    @Override
    public String toString() {
        return "stopID: " + stopID;
    }
}
