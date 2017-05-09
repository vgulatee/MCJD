package sort;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.ArrayList;

public class JargonSort {

	public static void sort() {

		File directory = new File("data");
		File[] files = directory.listFiles();		
		FileInputStream input;
		BufferedReader reader;		

		for (int numFiles = 0; numFiles < files.length; numFiles++){			//Iterate through the list of files in the directory
			
			if (files[numFiles].isDirectory()) continue;						//Ignore directories	
			//if (files[numFiles].toString().endsWith(".csv")) continue;
			
			Print.print(files[numFiles].toString());
			
			try {
				input = new FileInputStream(files[numFiles]);					//Attempt to read next file in database
			} catch (FileNotFoundException e) {
				System.err.println("Error reading file");						//Exit program if error reading database
				input = null;
				System.exit(0);
			}
			reader = new BufferedReader(new InputStreamReader(input));			//Read the file

			int size = 0;
			try {
				while (reader.readLine() != null) size++;
				input.close();
				input = null;
				reader.close();
				reader = null;
				System.gc();
			} catch (IOException e1) {
				e1.printStackTrace();
			}			
			
			String[] data = new String[size];
			//Phrase[] data = new Phrase[size];
					

			try {								
				String info;

				input = new FileInputStream(files[numFiles]);						//Attempt to read next file in database
				reader = new BufferedReader(new InputStreamReader(input));			//Read the file
				
				while ((info = reader.readLine()) != null) {
					data[size-1] = info;
					//data[size-1] = new Phrase(info.toCharArray());
					size--;
					if (size < 0) break;
				}
				
				input.close();
				input = null;
				reader.close();
				reader = null;
				System.gc();
					
				QuickThreeString.sortComparable(data, 0, data.length-1, 0);
				//QuickThreePhrase.sortPhraseComparable(data, 0, data.length-1, 0);
				
				String destPath = files[numFiles].toString();
				destPath = destPath.substring(0, destPath.length()-4);
				destPath += ".txt";			
				File dest = new File(destPath);
				Writer wr = new BufferedWriter(new FileWriter(dest));
				
				for (int i = 0; i < data.length; i++){					
					//wr.write(data[i].getCharacters());
					wr.write(data[i]);
					wr.write("\r\n");
				}
												
				data = null;
				wr.close();
				wr = null;
				System.gc();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e){
				e.printStackTrace();
			}
			
		}
	}
	
}