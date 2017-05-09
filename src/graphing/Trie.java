package graphing;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import main.Entry;

/**
 * The search trie is a data structure composed of nodes and links
 * Each node contains a character value and links to valid subsequent characters
 * Nodes are marked as either terminal or non-terminal depending on whether the resulting string
 * 	is a valid character
 * 
 * @author Aravi Premachandran
 * @author Karnvir Bining
 * @author Nikhil Patel
 * @author Vishesh Gulatee 
 *
 */
public class Trie {

	public Node root = new Node();
	private static String folder = "data";
	private static String extension = ".txt";


	public static void main(String[] args){		
		long startTime = System.currentTimeMillis();
		String filename = "data\\NamesAndTitles.txt";
		Trie.callTrie();
		readFromFileTest(filename);
		hardCodeTest();
		long endTime = System.currentTimeMillis();
		System.out.println((endTime - startTime)/1000.0);		
	}


	public Trie(String[] data) {				
		constructTrie(root, data, 0, data.length, 0);
	}


	public static ArrayList<Entry> searchSubject(String category, String subject, String searchTerm){
		File file = new File(folder + "\\" + category + extension);			
		FileInputStream input;		
		ArrayList<Entry> entries = new ArrayList<Entry>();

		try {
			input = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			System.err.println("Error reading file");
			input = null;
			System.exit(0);
		}
		BufferedReader reader = new BufferedReader(new InputStreamReader(input));

		String info;
		ArrayList<String> data = new ArrayList<String>();

		try {
			
			while ((info = reader.readLine()) != null) {				
				if (info.split("\\|{2}")[0].equals(subject)) data.add(info);
			}
			String[] dataset = new String[data.size()];
			data.toArray(dataset);
			data = null;

			Trie tree = new Trie(dataset);			
			
			ArrayList<String> words = tree.search(subject, searchTerm);
			if (words == null) return null;
			for (String word : words){
				String[] context = word.split("\\|{2}");
				ArrayList<String> detail = new ArrayList<String>();
				for (int i = 1; i < context.length; i++)
					if (!context[i].trim().equals("")) {							
						detail.add(context[i]);
					}
				entries.add(new main.Entry(context[0], subject, category, detail));
			}

			Node.resetNodes();
			words = null;
			tree = null;				
			data = null;
			dataset = null;	
			input.close();
			input = null;
			reader.close();
			reader = null;
			System.gc();

		} catch (IOException e){
			e.printStackTrace();			
			input = null;
			reader = null;
			System.gc();
		}
		return entries;
	}

	public static ArrayList<Entry> searchCategory(String category, String searchTerm){

		File file = new File(folder + "\\" + category + extension);			
		FileInputStream input;		
		ArrayList<String> results = new ArrayList<String>();
		ArrayList<Entry> entries = new ArrayList<Entry>();
		
		try {
			input = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			System.err.println("Error reading file");
			input = null;
			System.exit(0);
		}
		BufferedReader reader = new BufferedReader(new InputStreamReader(input));

		String info;
		ArrayList<String> data = new ArrayList<String>();

		try {			
			ArrayList<String> subjectList = new ArrayList<String>();			
			while ((info = reader.readLine()) != null) {				
				data.add(info);
				String[] line = info.split("\\|{2}");				//Characters || are used as delimiters because commas used in entries					
				if (!line[0].equals("||") && !line[0].trim().equals("") && !subjectList.contains(line[0])) {						 
					subjectList.add(line[0]);										//Stores the name of the subject
				}
			}
			
			String[] dataset = new String[data.size()];
			data.toArray(dataset);
			data = null;

			long start = System.currentTimeMillis();
			Trie tree = new Trie(dataset);
			long end = System.currentTimeMillis();
			System.out.println("Trie building time: " + (end-start)/1000.0);
			
			int matches = 0;
			for (String currentSubject : subjectList){
				if (matches >= 5) break;
				ArrayList<String> words = tree.search(currentSubject, searchTerm);
				if (words == null) continue;
				else matches++;
				results.addAll(words);
				for (String word : words){	
					String[] context = word.split("\\|{2}");
					ArrayList<String> detail = new ArrayList<String>();
					for (int i = 1; i < context.length; i++)
						if (!context[i].trim().equals("")) {							
							detail.add(context[i]);
						}
					entries.add(new main.Entry(context[1], currentSubject, category, detail));
				}
				words = null;
			}
												
			Node.resetNodes();	
			subjectList = null;
			tree = null;				
			data = null;
			dataset = null;	
			input.close();
			input = null;
			reader.close();
			reader = null;
			System.gc();

		} catch (IOException e){
			e.printStackTrace();			
			input = null;
			reader = null;
			System.gc();
		}
		return entries;
	}

	public ArrayList<String> searchAll(String searchTerm){
		return null;
	}


	/**
	 * Finds and returns a list of all strings starting with the specified prefix
	 * @param prefix the prefix string that each word must start with
	 * @return the list of words starting with the specified prefix
	 */
	public ArrayList<String> search(String subject, String prefix){		

		Node start = getPrefix(prefix);
		if (start == null) return null;			
		return breadthFirstSearch(subject, prefix, start);
	}


	/**
	 * @param str the prefix string to search for
	 * @return the Node associated with the end of the string
	 */
	private Node getPrefix(String str){
		return getPrefix(this.root, str);
	}

	/**
	 * @param start the Node to start the search at
	 * @param str the prefix string to search for 
	 * @return the Node associated with the end of the prefix string
	 */
	private Node getPrefix(Node start, String str){

		Node end = start;
		for (char ch : str.toCharArray()){			
			end = end.getNode(ch);			
			if (end == null) return null;
		}		
		return end;
	}

	/**
	 * Performs a breadth-first search to find all words starting with a given prefix
	 * @param prefix the prefix string to be prepended to the search path
	 * @param last the Node at which to start the search
	 */
	private ArrayList<String> breadthFirstSearch(String subject, String prefix, Node last){

		ArrayList<String> words = new ArrayList<String>();
		ArrayList<String> suffixes = breadthFirstSearch(last, subject, prefix);

		for (String suffix : suffixes){
			words.add(suffix);
		}
		return words;
	}

	/**
	 * Performs a breadth-first search starting at the specified Node and
	 * returning all valid strings that contain the sequence of Nodes ending with the specified Node
	 * @param last the node from which to start the breadth-first search
	 */
	@SuppressWarnings("unchecked")
	private ArrayList<String> breadthFirstSearch (Node last, String subject, String searchTerm){

		ArrayList<LinkedList<Node>> strings = new ArrayList<LinkedList<Node>>();
		LinkedList<Node> chosen = new LinkedList<Node>();		
		LinkedList<Node> validString = new LinkedList<Node>();

		int[] edgeUsed = new int[Node.count()];
		int numMatches = 0;

		chosen.addFirst(last);															//Add source Node to the queue				

		while(!chosen.isEmpty()){
			Node next = chosen.removeFirst();											//Get the next Node to be visited

			if (next.isTerminal() && next.getPre().equals(subject)){	
				if (numMatches >= 5) break;
				numMatches++;
				for (int v = next.getID(); v != last.getID(); v = edgeUsed[v]){			//Construct the path using the trace of visited Nodes					
					validString.push(Node.getNode(v));
				}						
				strings.add((LinkedList<Node>)validString.clone());						//Store the path (as a valid string)				
				validString = new LinkedList<Node>();									//Clear the entries in the path so that the next (if any) paths can be found
			}			
			for (Node child : next.getChild()){											//Add the child Nodes to the queue
				chosen.addLast(child);													//Add the specified child Node to the list of Nodes still to be explored
				edgeUsed[child.getID()] = next.getID();									//Marks (and stores) the parent as the Node visited before the child Node
			}
		}		

		/*//Prints out the list of valid substrings that continue from the 'last' Node 
		for (LinkedList<Node> seqs : strings){
			for (Node character : seqs){
				System.out.print(character.getValue());
			}
			System.out.println();
		}
		System.out.println();*/

		ArrayList<String> words = new ArrayList<String>();
		String suffix = "";
		for (LinkedList<Node> seqs : strings){
			for (Node character : seqs){				
				suffix += character.getValue();
				if (character.isTerminal()){					
					suffix += character.getEnd();			
					suffix = character.getPre() + "||" + searchTerm + suffix;
				}
			}
			words.add(suffix);
			suffix = "";
		}
		return words;
	}

	
	private void reduceNodes(Node parent){
		if (parent.getChild().size() == 1){
			Node child = parent.getChild().get(0);
			
		}
	}
	
	
	/**
	 * @param current the most recent parent node in the construction of the tree
	 * @param data the list of strings to be input into the trie
	 * @param lo lower index for the subarray currently being used in construction of the tree
	 * @param hi upper index for the subarray currently being used in construction of the tree
	 * @param position the character position currently being used as the partitioning character
	 */
	private void constructTrie(Node current, String[] data, int lo, int hi, int position){

		if (hi < lo) return;
		sort.QuickThreeString.sortComparable(data, lo, hi-1, position);

		int start = lo;		
		//System.out.println("lo: " + lo + " hi: " + hi);
		for (int i = lo; i < hi; i++){					
			if ((char) getChar(data[i],position) == (char) -1) continue;											//Ignore if the character is an EOS
			if ((char) getChar(data[i],position) == (char) -2) continue;											//Ignore if the character is an EOS

			Node character = new Node((char) getChar(data[i],position));											//Creates a new Node using the character at the position			
			if (getChar(data[i],position+1) < 0 || hi - lo == 1) {				//Test if hi-lo == 1 messes up
				character.setTerminal(true);																		//If the next character is an EOS mark this character as an EOS
				character.setEnd(data[i].substring(data[i].indexOf('|') + position + 3, data[i].length()));
				character.setPre(data[i].substring(0, data[i].indexOf('|')));
			}
			if (i + 1 < hi && (getChar(data[i],position) == getChar(data[i+1],position))){							//If the next string has same character at the same position								
				if (!current.containsChild(character)){
					current.addChildNode(character);																//Add this Character to the list of character values branching from this Node
				}
			} else {																												
				if (!current.containsChild(character)) {					
					current.addChildNode(character);																//Add this Character to the list of character values branching from this Node	
				}
				constructTrie(current.getNode(character), data, start, i+1, position+1);							//Recursively construct trie using this as the next root node
				start = i+1;																						//Continue building the trie from the next partition
			}
		}				
	}

	private static int getChar(String str, int pos){
		int sep = str.indexOf('|') + 2;
		if (sep == -1) return -1;
		if (pos + sep >= str.length()) return -2;
		else {							
			int count = 0;			
			for (int i = sep; i < pos + sep; i++){
				if (str.charAt(i) == '|') count++;
			}
			if (count < 2){
				return str.charAt(pos + sep);
			} else {
				return -1;
			}
		}
	}



	/*
	 * 
	 * The following methods are to be used solely for testing or as redundancies
	 * 
	 */


	public static void callTrie(){
		long startTime = System.currentTimeMillis();

		File directory = new File("data");
		File[] files = directory.listFiles();		
		FileInputStream input;
		BufferedReader reader;		

		int matches = 0;

		for (int numFiles = 0; numFiles < files.length; numFiles++){			//Iterate through the list of files in the directory

			File currentFile = files[numFiles];
			if (currentFile.toString().equals("data\\Administration.txt")) continue;
			if (currentFile.toString().equals("data\\Economy.txt")) continue;
			if (currentFile.toString().equals("data\\ElectronicsInformatics.txt")) continue;
			if (currentFile.toString().equals("data\\HumanitiesSocialSciences.txt")) continue;
			if (currentFile.toString().equals("data\\industries.txt")) continue;
			if (currentFile.toString().equals("data\\NamesAndTitles.txt")) continue;
			if (currentFile.isDirectory()) continue;							//Ignore directories
			if (currentFile.toString().endsWith(".csv")) continue;
			if (currentFile.toString().contains("sorted")) continue;			

			try {
				input = new FileInputStream(files[numFiles]);					//Attempt to read next file in database
			} catch (FileNotFoundException e) {
				System.err.println("Error reading file");						//Exit program if error reading database
				input = null;
				System.exit(0);
			}
			reader = new BufferedReader(new InputStreamReader(input));			//Read the file


			try {
				String info;
				String subject = " ";	
				ArrayList<String> data = new ArrayList<String>();
				ArrayList<String> subjectList = new ArrayList<String>();			
				while ((info = reader.readLine()) != null) {				
					data.add(info);
					String[] line = info.split("\\|{2}");				//Characters || are used as delimiters because commas used in entries					
					if (!line[0].equals("||") && !line[0].equals(subject) && !line[0].equals("")) {						 
						subjectList.add(subject);										//Stores the name of the subject
						subject = line[0];
					}
				}
				input.close();
				input = null;
				reader.close();
				reader = null;
				System.gc();
				subjectList.add(subject);

				String currentCategory = files[numFiles].toString();
				currentCategory = currentCategory.substring(5, currentCategory.length()-4);


				String[] dataset = new String[data.size()];
				data.toArray(dataset);
				Trie tree = new Trie(dataset);											

				String search = "scope";

				for (String currentSubject : subjectList){
					if (currentSubject.equals(" ")) continue;
					String prefix = search;	

					if (matches >= 5) break;
					ArrayList<String> words = tree.search(currentSubject, prefix);
					if (words == null) continue;
					else matches++;
					for (String word : words){
						System.out.println(currentSubject + "||" + prefix + word);
					}
				}
				Node.resetNodes();
				subjectList = null;
				tree = null;				
				data = null;
				dataset = null;				
				System.gc();

				if (matches >= 5) break;

			} catch (IOException e){
				e.printStackTrace();
			}


		}
		long endTime = System.currentTimeMillis();		
		System.out.println((endTime - startTime)/1000.0);
	}

	/**
	 * Constructs a trie and performs a breadth-first search
	 * Input is taken as a sequence of lines read from a file
	 */
	public static void readFromFileTest(String filename){
		File file = new File(filename);			
		FileInputStream input;
		BufferedReader reader;


		try {
			input = new FileInputStream(file);					//Attempt to read next file in database
		} catch (FileNotFoundException e) {
			System.err.println("Error reading file");						//Exit program if error reading database
			input = null;
			System.exit(0);
		}
		reader = new BufferedReader(new InputStreamReader(input));			//Read the file

		try {
			String info;
			String subject = " ";	
			ArrayList<String> data = new ArrayList<String>();
			ArrayList<String> subjectList = new ArrayList<String>();			
			while ((info = reader.readLine()) != null) {				
				data.add(info);
				String[] line = info.split("\\|{2}");				//Characters || are used as delimiters because commas used in entries					
				if (!line[0].equals("||") && !line[0].equals(subject) && !line[0].equals("")) {						 
					subjectList.add(subject);										//Stores the name of the subject
					subject = line[0];
				}
			}

			String[] dataset = new String[data.size()];
			data.toArray(dataset);

			Trie tree = new Trie(dataset);

			String prefix = "chemical";
			for (String subjects : subjectList){				
				ArrayList<String> words = tree.search(subject, prefix);			
				if (words == null) continue;
				for (String word : words){
					System.out.println(word);
				}
			}					
			tree = null;
			input.close();
			input = null;
			reader.close();
			reader = null;
			System.gc();
		} catch (IOException e) {
			input = null;
			reader = null;
			e.printStackTrace();
		}

	}


	/**
	 * Constructs a trie and performs a breadth-first search
	 * Inputs are hard-coded strings
	 */
	public static void hardCodeTest(){
		String[] a = new String[]{"antibodies", "antibody", "antigen", "antipathy", "antioxidant", "antioxidant", "antipathy", "are", "boo", "boo", "by", "chow", "sea", "seashells", "seashells", "sells", "sells", "she", "she", "shells", "shore", "surely", "tea", "the", "the", "theme"};		
		String prefix = "anti";

		Trie tree = new Trie(a);
		ArrayList<String> words = tree.search("", prefix);

		for (String word : words){
			System.out.println(word);
		}

	}

}
