package sort;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class TestSort {
	private static ArrayList<String> inputData = new ArrayList<String>();

	public static void main(String[] args){
		//FileInputStream input;
		//BufferedReader reader;


		//String path = "data\\";  //Change file name here
		//File inFile = new File(path);

		File directory = new File("data1");
		File[] files = directory.listFiles();		
		FileInputStream input;
		BufferedReader reader;
		//ArrayList<String> inputData = new ArrayList<String>();						

		for (int numFiles = 0; numFiles < files.length; numFiles++){			//Iterate through the list of files in the directory
			
			if (files[numFiles].toString().endsWith(".csv")) continue;
			if (files[numFiles].isDirectory()) continue;						//Ignore directories	
			Print.print(files[numFiles].toString());
			
			try {
				input = new FileInputStream(files[numFiles]);					//Attempt to read next file in database
			} catch (FileNotFoundException e) {
				System.err.println("Error reading file");						//Exit program if error reading database
				input = null;
				System.exit(0);
			}
			reader = new BufferedReader(new InputStreamReader(input));			//Read the file

			//		try {
			//			input = new FileInputStream(inFile);
			//		} catch (FileNotFoundException e) {			
			//			System.err.println("Error reading file a2_in.txt: data\\a2_in.txt does not exist");
			//			input = null;
			//			System.exit(0);
			//		}
			//		reader = new BufferedReader(new InputStreamReader(input));

			try{
				String info;
				while ((info = reader.readLine()) != null) {
					inputData.add(info);
				}
			}catch (IOException e){
				e.printStackTrace();
			}

			for (int num = 0; num < inputData.size(); num++){
				String temp=inputData.get(num).split("\\|{2}")[0];
				String subject;
				if(temp.charAt(0)=='\"'){
					subject=temp.substring(1, temp.length()-1);
				}
				else{
					subject=temp;
				}
				if (num < inputData.size() - 1){
					String tempNext = inputData.get(num+1).split("\\|{2}")[0];
					String subjectNext;
					if(tempNext.charAt(0)=='\"'){
						subjectNext=tempNext.substring(1, tempNext.length()-1);
					}
					else{
						subjectNext=tempNext;
					}
					if (subject.equals(subjectNext)) continue;
				}
			}
			testSubject();
			testWord();
			
			inputData = new ArrayList<String>();
		}
	}
	public static void testSubject(){
		for ( int i = 0; i < inputData.size()-1; i++){
			if (inputData.get(i).split("\\|{2}")[0].compareTo(inputData.get(i+1).split("\\|{2}")[0]) > 0){
				System.out.println(i);
				break;
			}
		}
		System.out.println(-100);

	}

	public static void testWord(){
		for ( int i = 0; i < inputData.size()-1; i++){
			if (inputData.get(i).split("\\|{2}")[1].compareTo(inputData.get(i+1).split("\\|{2}")[1]) > 0){
				System.out.println(i);
				
			} 
		}
		System.out.println(-100);
	}
}
