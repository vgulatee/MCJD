package main;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Entry {
	private final String word;
	private final String category;
	private final String subject;
	private final ArrayList<String> contextList;
	private ArrayList<Entry> synonym;

	public Entry(String term, String subject, String category, ArrayList<String> context){

		this.word = term;
		this.contextList = context;		
		this.subject = subject.intern();
		this.category = category.intern();
	}

	public void addSynonym(Entry synonym){
		this.synonym.add(synonym);
	}

	public String getTerm(){
		return this.word;
	}
	
	public String getContext(){
		String context = "";
		ArrayList<String> usageType = new ArrayList<String>();
		usageType.add("\"CORRECT");
		usageType.add("TRADEMARK");
		usageType.add("STANDARDIZED\"");
		ArrayList<String> contextType = new ArrayList<String>();
		contextType.add("\"DEF");
		contextType.add("\"OBS");
		contextType.add("\"CONT");
		contextType.add("\"PHR");
		
		for (int i = 1; i < contextList.size() - 1; i++){

			if (usageType.contains(contextList.get(i)) && usageType.contains(contextList.get(i+1))){
				context += "USAGE:\t" + contextList.get(i).replace("\"", "") + ", ";
			} else if (contextList.get(i).toUpperCase().equals(contextList.get(i))){
				context += contextList.get(i).replace("\"", "") + '\n';
			} else {
				boolean synonym = true;
				for (String cont : contextType){
					if (contextList.get(i).startsWith(cont)) synonym = false;
				}
				if (synonym) context += "SYNONYMS:\t" + contextList.get(i).replace("\"", "") + '\n';
				else {
					context += contextList.get(i).replaceAll("\"", "").replace("DEF:", "DEF:\t");					
					context = context.replace("OBS:","OBS\t").replace("CONT:", "CONT:\t").replace("PHR:","PHR:\t") + '\n';
				}
			}			
		}
		context += contextList.get(contextList.size()-1).replace("\"", " ");
		
		/*for (String detail : contextList) {
			context += detail.replace("\"", " ") + '\n';
			if (context.charAt(0) == ' ') context = context.substring(1);
		}*/
		//return contextList.get(0) + '\n' + contextList.get(1) + '\n' + contextList.get(2);
		return context;
	}
	
	@Override
	public String toString(){		
		return "FIELD:\t" + this.category + "\nSUBJECT:\t" + this.subject + "\nTERM:\t" + this.word + "\n" + this.getContext();
	}
	
	
	public static void main(String[] args){
		FileInputStream input;
		BufferedReader reader;
		ArrayList<String[]> inputData = new ArrayList<String[]>();
		
		String path = "data\\Administration.csv";
		File inFile = new File(path);		
		try {
			input = new FileInputStream(inFile);
		} catch (FileNotFoundException e) {			
			System.err.println("Error reading file");
			input = null;
			System.exit(0);
		}
		reader = new BufferedReader(new InputStreamReader(input));

		try{
			String info;
			while ((info = reader.readLine()) != null) {
				inputData.add(info.split("||"));
			}
		}catch (IOException e){
			e.printStackTrace();
		}
	
		String[] information = inputData.get(5);
		String subject = information[0];
		System.out.println(information[0]);
		String word = information[1];
		
		ArrayList<String> context = new ArrayList<String>();
		
		for (int x = 0; x < information.length; x++){
			System.out.print(x + ":  " +information[x]);			
		}		
	}

}