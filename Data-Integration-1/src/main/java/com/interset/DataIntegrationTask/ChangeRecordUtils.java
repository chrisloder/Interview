package com.interset.DataIntegrationTask;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ChangeRecordUtils {



	/**
	 * Remove invalid action entries from the given set of change records
	 * @param records change records to be filtered
	 * @return filtered list
	 */
	public static List<ChangeRecord> removeInvalidActionEntries(List<ChangeRecord> records) {

		String[] acceptableActivities = { "createdDoc", "addedText", "changedText", "deletedDoc", "deletedText",
				"archived", "viewDoc" };
		List<String> zz = Arrays.asList(acceptableActivities);

		List<ChangeRecord> filteredList = records.stream().filter(record -> zz.contains(record.getActivity()))
				.collect(Collectors.toList());
		return filteredList;

	}
	
	
	/**
	 * Remove any change records that have duplicate event id's
	 * @param records change records to be filtered
	 * @return filtered list
	 */
	public static List<ChangeRecord> removeDuplicates(List<ChangeRecord> records){
	
		List<ChangeRecord> uniqueRecords = new ArrayList<ChangeRecord>();
		List<Long> uniqueEvents= new ArrayList<Long>();
		
		for(ChangeRecord record : records){
			Long evventId = record.getEventId();
			
			if(!uniqueEvents.contains(evventId)){
				uniqueEvents.add(evventId);
				uniqueRecords.add(record);
			} 
		}
		
		return uniqueRecords;
		
	}

	
	/**
	 * Gets the count of unique user entries from the list of change records
	 * @param records list of change records
	 * @return count of unique users in the change records
	 */
	public static int getUniqueUserCount(List<ChangeRecord> records) {

		List<String> uniqueUsers = new ArrayList<>();
		for (ChangeRecord record : records) {
			if (!uniqueUsers.contains(record.getUser())) {
				uniqueUsers.add(record.getUser());
			}
		}

		return uniqueUsers.size();
	}

	/**
	 * Gets the count of unique files in the list of change records
	 * @param records list of change records
	 * @return count of unique files in the list of change records
	 */
	public static int getUniqueFileCount(List<ChangeRecord> records) {

		List<String> uniqueFiles = new ArrayList<>();
		for (ChangeRecord record : records) {
			if (!uniqueFiles.contains(record.getCSVFileName())) {
				uniqueFiles.add(record.getCSVFileName());
			}
		}

		return uniqueFiles.size();
	}

	/**
	 * Gets the earliest start date in the list of change records
	 * @param records list of change records
	 * @return earliest start date in the list of change records
	 */
	public static String getStartDate(List<ChangeRecord> records) {

		Date startDate = new Date();

		SimpleDateFormat csvFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

		for (ChangeRecord record : records) {

			Date recordDate;
			try {
				recordDate = csvFormat.parse(record.getCSVTimestamp());
				if (recordDate.before(startDate)) {
					startDate = recordDate;
				}
			} catch (ParseException e) {
				System.out.println("There was a problem parsing the timestamp.");
				e.printStackTrace();
			}

		}

		return csvFormat.format(startDate);
	}
	
	/**
	 * Gets the latest date in the list of change records
	 * @param records list of change records
	 * @return latest date
	 */
	public static String getEndDate(List<ChangeRecord> records) {

		Date endDate = new Date();

		SimpleDateFormat csvFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

		for (ChangeRecord record : records) {

			Date recordDate;
			try {
				recordDate = csvFormat.parse(record.getCSVTimestamp());
				if (recordDate.after(endDate)) {
					endDate = recordDate;
				}
			} catch (ParseException e) {
				System.out.println("There was a problem parsing the timestamp.");
				e.printStackTrace();
			}

		}

		return csvFormat.format(endDate);
	}
	
	/**
	 * Returns a map containing the count of each of the filtered actions
	 * @param records list of change records
	 * @return
	 */
	public static Map<String, Object> getActionMap(List<ChangeRecord> records){
		
		Map<String,Object> actionMap = new HashMap<String, Object>();
		int addCount = 0;
		int removeCount = 0;
		int accessedCount=0;
		for (ChangeRecord record : records) {

			switch (record.getCSVAction()){
			
				case "ADD":
					addCount++;
					break;
				case "REMOVE":
					removeCount++;
					break;
				case "ACCESSED":
					accessedCount++;
					break;
				
			}
		}
		actionMap.put("ADD", addCount+"");
		actionMap.put("REMOVE", removeCount+"");
		actionMap.put("ACCESSED", accessedCount+"");
		
		return actionMap;
		
	}
}
