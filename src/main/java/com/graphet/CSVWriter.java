package com.graphet;

import java.io.File;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CSVWriter {
	private String						fileName		= null;
	private List<String>				columns 		= null; // column headers
	private HashMap<String, String[]> 	rows 			= null;
	
	public CSVWriter(String f, List<String> c, HashMap<String, String[]> r) {
		fileName 	= f;
		columns 	= c;
		rows 		= r;
		
		try {
			PrintWriter pw 		= new PrintWriter(new File(fileName));
			StringBuilder sb	= new StringBuilder();
			
			for (int i=0; i<columns.size(); i++) {
				sb.append(columns.get(i));
				if (i < columns.size()-1)  	{  sb.append(',');  }
				else  						{  sb.append('\n');  }
			}
			
			for (Map.Entry<String, String[]> entry : rows.entrySet()) {
				String k 		= entry.getKey(); 		
				String[] val 	= entry.getValue();		
				
				sb.append(k);
				sb.append(',');
				for (int i=0; i<val.length; i++) {
					String v = val[i] == null ? "" : val[i];
					sb.append(v);
					if (i < val.length-1)  {  sb.append(',');  }
				}
				sb.append('\n');
			}
			
		
			pw.write(sb.toString());
			pw.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}