package search;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;
import java.util.TreeSet;

import main.Entry;

public class SearchTables {
	
	private static Hashtable<String, ArrayList<Position>> categories = new Hashtable<String, ArrayList<Position>>();
	private static Hashtable<Position, ArrayList<Entry>> words= new Hashtable<Position, ArrayList<Entry>>();
	
	
	public static void addSubject(String category, ArrayList<Position> table){
		SearchTables.categories.put(category, table);		
	}
	
	public static ArrayList<Position> getSubject(String category){
		return SearchTables.categories.get(category);
	}
	
	public static ArrayList<Entry> getWord(Position pos){
		return SearchTables.words.get(pos);
	}
	
	public static void addWord(Position pos, ArrayList<Entry> table ){
		SearchTables.words.put(pos, table);
	}
	
	public static void ToString(){
		Set<String> keys = SearchTables.categories.keySet();
		int index=0;
		 for (String key: keys){
			 
	            ArrayList<Position> lol=categories.get(key);
	            System.out.println(key);
	            for(Position p: lol){
	            	System.out.println(p.toString());
	            	System.out.println(index);
	            	index++;
	            }
	            System.out.println(" ");
	            }
	}
	public static Set<String> getKey(){
		 Set<String> keys = SearchTables.categories.keySet();
		 return new TreeSet<String>(keys);
	}
	
	public static Iterable<String> getSubjectID(String category) {
		ArrayList<String>ID=new ArrayList<String>();
		for(Position P: categories.get(category)){
			ID.add(P.ID().intern());
		}
		return ID;
	}
}
