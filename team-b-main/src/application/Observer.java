/*
 * Course: SE2030 - 081
 * Fall 2022
 * Lab 5
 * Names: Arthur Andersen, Hudson Arney, Eden Basso, Josh Sopa
 * Created: 10/8/2022
 */

package application;


/**
 * Interface implemented by the observers following an observer/subject model
 *
 * @author arneyh
 * @version 1.0
 */
public interface Observer {

	/**
	 * Gets all the required header fields for each object
	 *
	 * @return an Array of required header fields
	 */
	public String[] getRequiredHeaderFields();

	/**
	 * Gets a file header for a GTFS object for writing to a file
	 *
	 * @return ArrayList of Strings representing the fields of a file header
	 */
	public String getFileHeader();

	/**
	 * Gets a body line for a specified GTFS file to be exported
	 *
	 * @return a single body line
	 */
	public String getBodyLine();

}
