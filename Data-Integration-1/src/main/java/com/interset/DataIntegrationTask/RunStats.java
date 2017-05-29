package com.interset.DataIntegrationTask;

public class RunStats {
	
	/**
	 * Sample output:
	 * 
	 * {
    "linesRead":10000,
    "droppedEventsCounts": 150
    "droppedEvents": {
        "No action mapping": 134,
        "Duplicates": 16
    },
    "uniqueUsers": 23,
    "uniqueFiles": 256,
    "startDate": "2015-11-04T13:15:37.000-05:00",
    "endDate": "2016-01-15T06:54:23.000-05:00",
    "actions": {
        "ADD": 5550,
        "REMOVE": 2025,
        "ACCESSED": 2275
    }
}
	 */
	
	private int linesRead;
	private int droppedEventsCounts;
	private int noActionMapping;
	private int duplicates;
	private int uniqueUsers;
	private int uniqueFiles;
	private String startDate;
	private String endDate;
	private int actionADD;
	private int actionREMOVE;
	private int actionACCESSED;
	
	
	public RunStats(){
		
	}


	public int getLinesRead() {
		return linesRead;
	}


	public void setLinesRead(int linesRead) {
		this.linesRead = linesRead;
	}


	public int getDroppedEventsCounts() {
		return droppedEventsCounts;
	}


	public void setDroppedEventsCounts(int droppedEventsCounts) {
		this.droppedEventsCounts = droppedEventsCounts;
	}


	public int getNoActionMapping() {
		return noActionMapping;
	}


	public void setNoActionMapping(int noActionMapping) {
		this.noActionMapping = noActionMapping;
	}


	public int getDuplicates() {
		return duplicates;
	}


	public void setDuplicates(int duplicates) {
		this.duplicates = duplicates;
	}


	public int getUniqueUsers() {
		return uniqueUsers;
	}


	public void setUniqueUsers(int uniqueUsers) {
		this.uniqueUsers = uniqueUsers;
	}


	public int getUniqueFiles() {
		return uniqueFiles;
	}


	public void setUniqueFiles(int uniqueFiles) {
		this.uniqueFiles = uniqueFiles;
	}


	public String getStartDate() {
		return startDate;
	}


	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}


	public String getEndDate() {
		return endDate;
	}


	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}


	public int getActionADD() {
		return actionADD;
	}


	public void setActionADD(int actionADD) {
		this.actionADD = actionADD;
	}


	public int getActionREMOVE() {
		return actionREMOVE;
	}


	public void setActionREMOVE(int actionREMOVE) {
		this.actionREMOVE = actionREMOVE;
	}


	public int getActionACCESSED() {
		return actionACCESSED;
	}


	public void setActionACCESSED(int actionACCESSED) {
		this.actionACCESSED = actionACCESSED;
	}
	
	
	

}
