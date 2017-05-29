package com.interset.DataIntegrationTask;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TaskRunner {

	public static Map<String, Object> statsMap = new HashMap();
	
	public static void main(String args[]) {

		// Check arguments
		if (args.length != 2) {
			System.out.println(
					"We currently only expect 2 arguments! A path to a JSON file to read, and a path for a CSV file to write.");
			System.exit(1);
		}

		// Read arguments
		Path jsonFile = null;

		try {
			jsonFile = Paths.get(args[0]);
		} catch (InvalidPathException e) {
			System.err.println("Couldn't convert JSON file argument [" + args[0] + "] into a path!");
			throw e;
		}

		Path csvFile = null;

		try {
			csvFile = Paths.get(args[1]);
		} catch (InvalidPathException e) {
			System.err.println("Couldn't convert CSV file argument [" + args[1] + "] into a path!");
			throw e;
		}

		// Validate arguments
		if (!Files.exists(jsonFile)) {
			System.err.println("JSON file [" + jsonFile.toString() + "] doesn't exist!");
			System.exit(1);
		}

		if (!Files.isWritable(csvFile.getParent())) {
			System.err.println("Can't write to the directory [" + csvFile.getParent().toString()
					+ "] to create the CSV file! Does directory exist?");
			System.exit(1);
		}

		// Create the CSV file
		System.out.println(
				"Reading file [" + jsonFile.toString() + "], and writing to file [" + csvFile.toString() + "].");

		//parse and create the CSV file
		parseJsonFileAndCreateCsvFile(jsonFile, csvFile);
		
		//call method to ouptu the stats to the console
		outputRunStats();
		
		System.out.println("File processing complete.");

	}
	
	/**
	 * This method will output the stats in JSON on the console
	 */
	public static void outputRunStats(){
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			String jsonString = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(statsMap);
			System.out.println(jsonString);
		} catch (JsonProcessingException e) {
			System.out.println("There was a problem creating the object map or processing the JSON output.");
			e.printStackTrace();
		}
		
		
	}

	/**
	 * This will parse the json file and output the filtered csv
	 * @param jsonFile the json file to be parsed
	 * @param csvFile the csv file output
	 */
	public static void parseJsonFileAndCreateCsvFile(Path jsonFile, Path csvFile) {

	
		List<ChangeRecord> changeRecords = getListOfChangeRecords(jsonFile);
		int linesRead = changeRecords.size();
		statsMap.put("linesRead",linesRead); 
		statsMap.put("uniqueUsers", ChangeRecordUtils.getUniqueUserCount(changeRecords));
		
		//filter out duplicates
		changeRecords = ChangeRecordUtils.removeDuplicates(changeRecords);
		int duplicateCount = linesRead - changeRecords.size();
		
		//filter out invalid actions
		changeRecords = ChangeRecordUtils.removeInvalidActionEntries(changeRecords);
		
		int actionRemovalCount = changeRecords.size() - duplicateCount;
	
	
		Map<String,Object> droppedEventsMap = new HashMap();
		droppedEventsMap.put("No action mapping", actionRemovalCount);
		droppedEventsMap.put("Duplicates", duplicateCount);
		
		statsMap.put("droppedEvents", droppedEventsMap);
	
		statsMap.put("uniqueFiles",ChangeRecordUtils.getUniqueFileCount(changeRecords));
		statsMap.put("startDate", ChangeRecordUtils.getStartDate(changeRecords));
		statsMap.put("endDate", ChangeRecordUtils.getEndDate(changeRecords));
		Map<String,Object> actionMap = ChangeRecordUtils.getActionMap(changeRecords);
		statsMap.put("actions", actionMap);	
		
		
		Object[] headers = {"TIMESTAMP","ACTION","USER","FOLDER","FILENAME"};
		
		FileWriter fileWriter = null;
		CSVPrinter csvPrinter = null;
		CSVFormat csvFormat = CSVFormat.DEFAULT.withRecordSeparator(System.lineSeparator());
		
		try {
			fileWriter = new FileWriter(csvFile.toFile());
			csvPrinter = new CSVPrinter(fileWriter, csvFormat);

			csvPrinter.printRecord(headers);

			for(ChangeRecord changeRecord : changeRecords){
				List record = new ArrayList();
				record.add(changeRecord.getCSVTimestamp());
				record.add(changeRecord.getCSVAction());
				record.add(changeRecord.getCSVUser());
				record.add(changeRecord.getCSVPathToFile());
				record.add(changeRecord.getCSVFileName());
				csvPrinter.printRecord(record);
			}
		
		} catch (IOException e) {
			System.out.println("There was a problem accessing the CSV file");
			e.printStackTrace();
		} finally {
			try {
				fileWriter.flush();
				fileWriter.close();
				csvPrinter.close();
			} catch (IOException e) {
				System.out.println("There was a problem closing out the writers");
				e.printStackTrace();
			}
			
		}
				

	}

	/**
	 * This method will parse the change records file and create a list of changeRecords objects
	 * @param jsonFile the json file to parse
	 * @return list of change records
	 */
	private static List<ChangeRecord> getListOfChangeRecords(Path jsonFile) {
		
		 List<ChangeRecord> changeRecords =  new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(jsonFile.toFile()))) {
		    String line;
		    while ((line = br.readLine()) != null) {
				 ObjectMapper objectMapper = new ObjectMapper();
				 ChangeRecord changeRecord = objectMapper.readValue(line, ChangeRecord.class);
				 changeRecords.add(changeRecord);
		    }
		} catch (IOException e) {
			System.out.println("ERROR: There was a problem with acccessing the file " + jsonFile.getFileName() + ".  Is the path correct?  Do you have access to it?");
			e.printStackTrace();
		}
		
		return changeRecords;
	}

}
