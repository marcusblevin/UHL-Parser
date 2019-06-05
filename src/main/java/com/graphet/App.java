/**
 * This program parses UHL Trend System export files time series data to match up multiple lines with the same timestamp
 * 
 * Author:		Marcus Levin
 * Date:		June 5, 2019
 * Dependencies:
 * 		
 */
package com.graphet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;


public class App {
	public static void main( String[] args ) {
		String curDir 								= System.getProperty("user.dir");
		File folder 								= new File(curDir);
		List<File> files							= listFilesInFolder(folder);
		BufferedReader br							= null;
		String line									= "";
		List<String> l								= null;
		List<String> columns 						= new ArrayList<String>();
		
		String date = "";

		System.out.println("Running...");
		
		if (files.size() == 0) {
			System.out.println("Didn't find any files");
		}
		
		// step through files and process
		for (File file : files) {
			System.out.println("Processing: "+file.getName());
			
			try {
				br = new BufferedReader(new FileReader(file.getPath()));
				
				/* 
				 * First line of file is the column headers in order
				 */
				if ((line = br.readLine()) != null) {
					columns = new LinkedList<String>(Arrays.asList(line.split(",")));
				}
				
				HashMap<String, String[]> map = new HashMap<String, String[]>();
				
				
				while ((line = br.readLine()) != null) {
					l = new LinkedList<String>(Arrays.asList(line.split(",")));
					
					date = l.get(0);
					
					if (!map.containsKey(date)) {
						map.put(date, new String[columns.size()-1]);
					}
					
					// step through columns and insert values into map
					for (int i=1; i<l.size(); i++) {
						if (!l.get(i).isEmpty()) {
							map.get(date)[i-1] = l.get(i);
						}
					}
					
					
				}
				
				
				// write file to network location or sub-folder
				String fileLocation = file.getParent()+"\\processed\\";
				checkFolderExists(fileLocation); // check if the directory exists already
				fileLocation = checkFileExists(fileLocation+file.getName());
				
				if (fileLocation.length() > 0) {
					new CSVWriter(fileLocation, columns, map);
				}
				
				/*
				for (Map.Entry<String, String[]> entry : map.entrySet()) {
					String k 		= entry.getKey(); 		
					String[] val 	= entry.getValue();		
					
					System.out.print(k+": ");
					for (int i=0; i<val.length; i++) {
						System.out.print(val[i]+", ");
					}
					
					System.out.println(".");
				}
				*/
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		System.out.println("done");
	}
	
	
	
	/**
	 * List all Excel (xls, xlsx) files in a folder
	 * @param File folder
	 * @return List<File>
	 */
	public static List<File> listFilesInFolder(final File folder) {
		List<File> files = new ArrayList<File>();
		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory() && !fileEntry.getAbsolutePath().contains("processed")) {
				files = listFilesInFolder(fileEntry); // recursively list files in sub-folders
			} else if (fileEntry.getName().endsWith(".csv")) {
				files.add(fileEntry);
			}
		}
		return files;
	}
	
	/**
	 * check if directory exists already, create if not
	 * @param folderName
	 */
	public static void checkFolderExists(String folderName) {
		File dir = new File(folderName);
		if (!dir.exists()) {
			dir.mkdir();
		}
	}
	
	/**
	 * Check if file exists, if not increment file count and check using recursion 
	 * @param fileLocation
	 * @return String
	 */
	public static String checkFileExists(String fileLocation) {
		File fc = new File(fileLocation);
		if (!fc.exists()) {
			return fileLocation;
		} else {
			System.out.println("File "+fileLocation+" already exists");
			//return checkFileExists(fileLocation, 1);
			return "";
		}
	}
	
	/**
	 * Check if file exists, if not increment file count and check using recursion
	 * @param fileLocation
	 * @param i
	 * @return String
	 */
	public static String checkFileExists(String fileLocation, Integer i) {
		// check to see if the file exists already
		String fname 	= fileLocation.replace(".", " ("+i+").");
		File fc 		= new File(fname);
		if (fc.exists()) {
			System.out.println("File "+fname+" already exists");
			i++;
			return checkFileExists(fileLocation, i);
		} else {
			return fname;
		}
	}
}