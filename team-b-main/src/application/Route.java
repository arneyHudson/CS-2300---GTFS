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


import java.util.ArrayList;

/**
 * Represents a GTFS Route
 *
 * @author bassoe
 * @version 1.0
 */
public class Route implements Observer {

    private String routeID;
    private String routeColor;

    String[] requiredHeaderFields = {"route_id", "route_type"};

    public Stop m_Stop;
    private ArrayList<Stop> stops;
    private ArrayList<Trip> trips;

    public Route(String routeID, String routeColor, ArrayList<Stop> stops, ArrayList<Trip> trips,
                 Stop m_Stop) {
        this.routeID = routeID;
        this.routeColor= routeColor;
        this.stops = stops;
        this.trips = trips;
        this.m_Stop = m_Stop;
    }

    public Route(String routeID, String routeColor) {
        this.routeID = routeID;
        this.routeColor = routeColor;
    }

    public Route() {}

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
     * Gets a file header for a GTFS Route object for writing to a file
     *
     * @return ArrayList of Strings representing the fields of a file header
     */
    @Override
    public String getFileHeader() {
        return "route_id,agency_id,route_short_name,route_long_name,route_desc,route_type,route_url,route_color,route_text_color";
    }

    /**
     * Gets a body line for a specified GTFS file to be exported
     *
     * @return a single body line
     */
    @Override
    public String getBodyLine() {
        return routeID + ",,,,,,," + routeColor + ",";
    }

    /**
     * Gets hashcode for a routeID when adding and retrieving from HashMap
     *
     * @return integer representing a hashcode for a routeID
     * @author bassoe
     */
    @Override
    public int hashCode() {
        return routeID.hashCode();
    }

    /**
     * Determines if two Route objects are equal
     *
     * @param o the Route object to compare this Route object's attributes to
     * @return true if equal, false if not
     * @author bassoe
     */
    @Override
    public boolean equals(Object o) {
        boolean isEqual = false;
        if (o instanceof Route) {
            Route route = (Route) o;
            isEqual = this.routeID.equals(((Route) o).routeID) &&
                    this.routeColor.equals(((Route) o).routeColor);
        }
        return isEqual;
    }

    public String getRouteID() {
        return routeID;
    }

    @Override
    public String toString() {
        return "RouteID: " +routeID;
    }
}
