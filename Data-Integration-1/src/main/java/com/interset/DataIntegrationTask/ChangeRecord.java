package com.interset.DataIntegrationTask;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author cloder
 *
 */
public class ChangeRecord {


	private Long eventId;
	private String user;
	private String ipAddr;
	private String file;
	private String activity;
	private String timestamp;
	private String timeOffset;

	public Long getEventId() {
		return eventId;
	}

	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getIpAddr() {
		return ipAddr;
	}

	public void setIpAddr(String ipAddr) {
		this.ipAddr = ipAddr;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public String getActivity() {
		return activity;
	}

	public void setActivity(String activity) {
		this.activity = activity;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timeStamp) {
		this.timestamp = timeStamp;
	}

	public String getTimeOffset() {
		return timeOffset;
	}

	public void setTimeOffset(String timeOffset) {
		this.timeOffset = timeOffset;
	}

	/**
	 * This will return the time stamp in the ISO 8601 format
	 * 
	 * @return the csv formatted timestamp
	 */
	public String getCSVTimestamp() {
		String csvTimestamp = "";
		SimpleDateFormat jsonFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ssa");
		SimpleDateFormat csvFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
		try {
			Date jsonDate = jsonFormat.parse(getTimestamp());
			csvTimestamp = csvFormat.format(jsonDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return csvTimestamp;
	}

	/**
	 * this will return the action that has been mapped for the CSV
	 * 
	 * @return the mapped action
	 */
	public String getCSVAction() {
		String csvAction = "";
		String jsonAction = getActivity();

		switch (jsonAction) {
		case "createdDoc":
		case "addedText":
		case "changedText":
			csvAction = "ADD";
			break;
		case "deletedDoc":
		case "deletedText":
		case "archived":
			csvAction = "REMOVE";
			break;
		case "viewDoc":
			csvAction = "ACCESSED";
			break;
		default:
			System.out.println("Action of type '" + jsonAction + "' has been set to REMOVE so it can be filtered out.");
		}

		return csvAction;
	}

	/**
	 * This will return the user portion for the csv
	 * 
	 * @return the user
	 */
	public String getCSVUser() {
		String csvUser = "";
		String jsonUser = getUser();
		if(jsonUser.contains("@")){
			csvUser = jsonUser.substring(0, jsonUser.indexOf("@"));
		} else {
			csvUser = jsonUser;
		}

		return csvUser;
	}

	/**
	 * This will return the folder path to the file accessed
	 * 
	 * @return the path
	 */
	public String getCSVPathToFile() {
		String csvPath = "";
		String jsonFilePath = getFile();

		csvPath = jsonFilePath.substring(0, jsonFilePath.lastIndexOf("/") + 1);
		return csvPath;
	}

	/**
	 * This will return the CSV formatted file name form the filepath
	 * 
	 * @return the file name
	 */
	public String getCSVFileName() {
		String csvFileName = "";
		String jsonFilePath = getFile();
		csvFileName = jsonFilePath.substring(jsonFilePath.lastIndexOf("/") + 1);

		return csvFileName;
	}

	/**
	 * Get the CSV formatted IP Address
	 * 
	 * @return the ip address
	 */
	public String getCSVIPAddress() {
		// at the moment no conversion needs to be made for csv IP address, just
		// creating method for consistency
		return getIpAddr();
	}
	
	
	/** This will return a what the line would look like when in the csv file
	 * @return the line as it would look in the CSV file
	 */
	public String getCSVLineOutput(){
		return getCSVTimestamp() + "," + getCSVAction() + "," + getCSVUser() + "," + getCSVPathToFile() + "," + getCSVFileName() + "," + getCSVIPAddress() + System.getProperty("line.separator");
	}

}
