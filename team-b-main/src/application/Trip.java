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


/**
 * Represents a GTFS Trip
 *
 * @author bassoe
 * @version 1.0
 */
public class Trip implements Observer {

    private String tripID;
    private String routeID;

    public Route mRoute;
    public Stop mStop;

    String[] requiredHeaderFields = {"route_id", "trip_id"};


    public Trip(String tripID, Route m_Route, Stop m_Stop) {
        this.tripID = tripID;
        this.mRoute = m_Route;
        this.mStop = m_Stop;

    }

    public Trip(String routeID, String tripID){
        this.tripID = tripID;
        this.routeID = routeID;

    }

    public Trip() {

    }

    /**
     * Gets hashcode for a tripID when adding and retrieving from HashMap
     *
     * @return integer representing a hashcode for a tripID
     * @author bassoe
     */
    @Override
    public int hashCode() {
        return tripID.hashCode();
    }

    /**
     * Determines if two trip objects are equal
     *
     * @param o the trip object to compare this trip object's attributes to
     * @return true if equal, false if not
     * @author bassoe
     */
    @Override
    public boolean equals(Object o) {
        boolean isEqual = false;
        if (o instanceof Trip) {
            Trip trip = (Trip) o;
            isEqual = this.tripID.equals(((Trip) o).tripID) &&
                    this.routeID.equals(((Trip) o).routeID);
        }
        return isEqual;
    }

    /**
     * Specifies which Strings should be present in the file header
     *
     * @return Strings representing fields in the header
     */
    @Override
    public String[] getRequiredHeaderFields() {
        return requiredHeaderFields;
    }

    /**
     * Gets a file header for a GTFS Trip object for writing to a file
     *
     * @return ArrayList of Strings representing the fields of a file header
     */
    @Override
    public String getFileHeader() {
        return "route_id,service_id,trip_id,trip_headsign,direction_id,block_id,shape_id";
    }

    /**
     * Gets a body line for a specified GTFS file to be exported
     *
     * @return a single body line
     */
    @Override
    public String getBodyLine() {
        return this.routeID + ",," + this.tripID + ",,,,";
    }

    public String getTripID() {
        return tripID;
    }

    public String getRouteID() {
        return routeID;
    }

    @Override
    public String toString() {
        return "tripID: " + tripID;
    }

}
