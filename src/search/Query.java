package search;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import main.Entry;

public class Query {
	
	static String[] categoryList = {"Math_Physics_NaturalSc", "Industries", "HumanitiesSocialSciences",
			"NamesAndTitles", "Administration", "ElectronicsInformatics", "Economy" };

	public static void setHash() {

		FileInputStream input;
		BufferedReader reader;
		File directory = new File("data");
		File[] files = directory.listFiles();
		ArrayList<Position> subjectList;
		ArrayList<String> context;
		ArrayList<Entry> words;

		boolean categoryFound = false;


		for (int numFiles = 0; numFiles < files.length; numFiles++) {

			File currentFile = files[numFiles];
			if (currentFile.isDirectory()) continue; // Ignore directories

			String currentCategory = getCategory(files[numFiles]).intern();			
			if (Arrays.asList(categoryList).contains(currentCategory)) categoryFound = true;			

			try {
				input = new FileInputStream(files[numFiles]);
			} catch (FileNotFoundException e) {
				System.err.println("Error reading file" + files[numFiles].toString());
				input = null;
				System.exit(0);
			}
			reader = new BufferedReader(new InputStreamReader(input));

			try {
				String info;
				String subject = " ";
				Position pos = null;
				subjectList = new ArrayList<Position>();

				if (categoryFound == true) {
					context = new ArrayList<String>();
					words = new ArrayList<Entry>();
					Entry term;
					int counter = 0;
					int counterH = 0;
					int counterQ = 0;
					int start = 0;

					while ((info = reader.readLine()) != null) {
						String[] line = info.split("\\|{2}");

						if (line[0].equals(subject)) {
							counter++;
							char ch = java.lang.Character.toLowerCase(line[1].charAt(0));

							if (ch >= 'a' && ch < 'j') {
								counterH = counter;
							} else if (ch >= 'j' && ch < 'r') {
								counterQ = counter;
							}

							int len = line.length;
							if (len > 2) {
								for (int index = 3; index < len - 1; index++) {	//Is there a reason you had length - 1 instead of length?
									if (line[index].trim() != "") {
										context.add(line[index]);
									}									
								}
								term = new Entry(line[1], line[0], currentCategory,context);
								words.add(term);

							} else if (len == 1) {
								context.add("no def");
								term = new Entry("no term", line[0], currentCategory, context);
								words.add(term);
							} else {
								context.add("no def");
								term = new Entry(line[1], line[0], currentCategory, context);
								words.add(term);
							}

						} else if (!line[0].equals("||")) {
							pos = new Position(subject, start, counter, counterH, counterQ);
							if (SearchTables.getWord(pos) == null) {
								SearchTables.addWord(pos, words);
							}
							context = new ArrayList<String>();
							words = new ArrayList<Entry>();
							counterH = 0;
							counterQ = 0;
							subjectList.add(pos);
							subject = line[0].intern();
							counter++;
							start = counter;

							int len = line.length;
							if (len > 2) {
								for (int k = 3; k < len - 1; k++) {
									if (line[k].trim() != "") {
										context.add(line[k]);
									}									
								}
								term = new Entry(line[1], line[0], currentCategory, context);
								words.add(term);
							} else if (len == 1) {
								context.add("no def");
								term = new Entry("no term", line[0], currentCategory, context);
								words.add(term);
							} else {
								context.add("no def");
								term = new Entry(line[1], line[0], currentCategory, context);
								words.add(term);
							}
						}
					}

					pos = new Position(subject, start, counter, counterH, counterQ);
					if (SearchTables.getWord(pos) == null) {
						SearchTables.addWord(pos, words);
					}
					subjectList.add(pos);

					if (SearchTables.getSubject(currentCategory) == null) {
						SearchTables.addSubject(currentCategory, subjectList);

					}
				} else {
					int counter = 0;
					int counterH = 0;
					int counterQ = 0;
					int start = 0;
					while ((info = reader.readLine()) != null) {
						String[] line = info.split("\\|{2}");
						if (line[0].equals(subject)) {
							counter++;
							char ch = java.lang.Character.toLowerCase(line[1].charAt(0));
							if (ch >= 'a' && ch < 'j') {
								counterH = counter;
							} else if (ch >= 'j' && ch < 'r') {
								counterQ = counter;
							}

						} else if (!line[0].equals("\\|{2}")) {
							pos = new Position(subject, start, counter, counterH, counterQ);
							counterH = 0;
							counterQ = 0;
							subjectList.add(pos);
							subject = line[0].intern();
							counter++;
							start = counter;

						}
					}
					subjectList.add(pos);

					if (SearchTables.getSubject(currentCategory) == null) {
						SearchTables.addSubject(currentCategory, subjectList);
					}
					input.close();
					input = null;
					reader.close();
					reader = null;
				}
			} catch (IOException e) {
				System.err.println("Error while reading file" + files[numFiles].toString());
				input = null;
				reader = null;
			}
		}
	}

	public static ArrayList<Entry> searchQuery(String category, String subject, String entry) {

		ArrayList<Entry> result = new ArrayList<Entry>();
		ArrayList<Entry> relatedTerms = new ArrayList<Entry>();
		int start = 0, end = 0;

		ArrayList<Position> subjectTable = SearchTables.getSubject(category);
		Position pos = subBinarySearch(subjectTable, subject);

		ArrayList<Entry> wordTable = SearchTables.getWord(pos);
		char ch = java.lang.Character.toLowerCase(entry.charAt(0));

		if (ch >= 'a' && ch < 'j') {
			start = pos.getS();
			end = pos.getH();
		} else if (ch >= 'j' && ch < 'r') {
			start = pos.getH() + 1;
			end = pos.getQ();
		}

		else if (ch >= 'r' && ch <= 'z') {
			start = pos.getQ() + 1;
			end = pos.getE();
		}

		Entry term = wordbinarySearch(wordTable, entry, start, end);
		relatedTerms.addAll(ContainsClose(wordTable, entry, start, end));

		if (term != null) {
			result.add(term);
			//Related.addAll(ContainsClose(WordTable, entry, start, end));
		}
		if (result.isEmpty()) {
			return relatedTerms;
		} else {
			return result;
		}
	}

	public static ArrayList<Entry> searchQuery(String category, String entry) {

		ArrayList<Entry> result = new ArrayList<Entry>();
		ArrayList<Entry> relatedTerms = new ArrayList<Entry>();
		ArrayList<Position> subjectTable = SearchTables.getSubject(category);

		int start = 0, end = 0;
		for (Position pos : subjectTable) {
			if (pos.equals(null) || pos.ID().trim().equals("")) {
				continue;
			}

			ArrayList<Entry> wordTable = SearchTables.getWord(pos);
			char ch = java.lang.Character.toLowerCase(entry.charAt(0));

			if (ch >= 'a' && ch < 'j') {
				start = pos.getS();
				end = pos.getH();

			} else if (ch >= 'j' && ch < 'r') {
				start = pos.getH() + 1;
				end = pos.getQ();

			} else if (ch >= 'r' && ch <= 'z') {
				start = pos.getQ() + 1;
				end = pos.getE();
			}

			Entry term = wordbinarySearch(wordTable, entry, start, end);
			if (term == null) {
				continue;
			} else {
				result.add(term);
			}

			//Related.addAll(ContainsClose(WordTable, entry, start, end));
		}

		if (result.isEmpty()) {
			return relatedTerms;
		} else {
			return result;
		}
	}

	private static Collection<Entry> ContainsClose(ArrayList<Entry> wordTable, String entry, int start, int end) {

		ArrayList<Entry> relatedTerms = new ArrayList<Entry>();

		int i = 4;

		if (entry.length() < 5) {
			i = entry.length();
		}

		int count = 0;

		while (i > 0 && count < 10) {
			boolean found = false;

			String value = entry.substring(0, i);
			Entry term = binarySearch(wordTable, value, start, end, i);
			if (term == null) {
				i--;

			} else {

				if (!relatedTerms.contains(term)) {
					relatedTerms.add(term);
					++start;
					count++;
				} else {
					++start;
				}
			}
		}
		return relatedTerms;
	}

	public static ArrayList<Entry> searchQuery(String entry) {

		ArrayList<Entry> result = new ArrayList<Entry>();
		ArrayList<Entry> relatedTerms = new ArrayList<Entry>();

		int start = 0, end = 0;

		for (String currentCategory : categoryList) {
			Iterable<Position> subTable = SearchTables.getSubject(currentCategory);
			for (Position pos : subTable) {
				if (pos.ID().trim().equals("")) {
					continue;
				}

				ArrayList<Entry> WordTable = SearchTables.getWord(pos);
				char ch = java.lang.Character.toLowerCase(entry.charAt(0));

				if (ch >= 'a' && ch < 'j') {
					start = pos.getS();
					end = pos.getH();

				} else if (ch >= 'j' && ch < 'r') {
					start = pos.getH() + 1;
					end = pos.getQ();

				} else if (ch >= 'r' && ch <= 'z') {
					start = pos.getQ() + 1;
					end = pos.getE();
				}

				Entry term = wordbinarySearch(WordTable, entry, start, end);
				if (term == null) {
					continue;
				} else {
					result.add(term);
					//Related.addAll(ContainsClose(WordTable, entry, start, end));
				}				
			}
		}
		
		if (result.isEmpty()) {
			return relatedTerms;
		} else {
			return result;	
		}		
	}

	public static Position subBinarySearch(ArrayList<Position> subjectTable, String value) {
		return subBinarySearch(subjectTable, value, 0, subjectTable.size() - 1);
	}

	public static Entry wordbinarySearch(ArrayList<Entry> wordTable, String value, int start, int end) {
		if (start > end) {
			return null;
		}

		int mid = (end + start) / 2;
		if (mid < 0) {
			return null;
		}
		
		String term = wordTable.get(mid).getTerm().toLowerCase().intern();
		value = value.toLowerCase().intern();
		
		if (term.equals(value)) {
			return wordTable.get(mid);
		} else if (term.compareTo(value) > 0) {
			return wordbinarySearch(wordTable, value, start, mid - 1);
		} else if (term.compareTo(value) < 0) {
			return wordbinarySearch(wordTable, value, mid + 1, end);
		}
		return null;
	}

	public static Entry binarySearch(ArrayList<Entry> wordTable, String value, int start, int end, int index) {
		if (start > end) {
			return null;
		}

		int mid = (end + start) / 2;
		if (mid < 0) {
			return null;
		}
		
		String term = wordTable.get(mid).getTerm().toLowerCase().substring(0, index).intern();
		value = value.toLowerCase().intern();
		
		if (term.equals(value)) {
			return wordTable.get(mid);
		} else if (term.compareTo(value) > 0) {
			return binarySearch(wordTable, value, start, mid - 1, index);
		} else if (term.compareTo(value) < 0) {
			return binarySearch(wordTable, value, mid + 1, end, index);
		}
		return null;
	}

	public static Position subBinarySearch(ArrayList<Position> subjectTable, String value, int start, int end) {
		
		if (start > end) {
			return null;
		}

		int mid = (end + start) / 2;
		
		String ID = subjectTable.get(mid).ID();
		value = value.intern();
		
		if (ID.equals(value)) {
			return subjectTable.get(mid);
		} else if (ID.compareTo(value) > 0) {
			return subBinarySearch(subjectTable, value, start, mid - 1);
		} else {
			return subBinarySearch(subjectTable, value, mid + 1, end);
		}
	}

	private static String getCategory(File filename){
		return filename.toString().substring(5, filename.toString().length() - 4).intern();
	}
	
	public static void main(String[] args) {

		System.out.println("TEST1");
		double startTime = System.currentTimeMillis();
		Query.setHash();
		double endTime = System.currentTimeMillis();
		System.out.println((endTime - startTime));
		
		long startTime1 = System.currentTimeMillis();
		// Querynew.searchQuery("NamesAndTitles", "Titles of Monographs",		"Conserve");
		long endTime1 = System.currentTimeMillis();
		System.out.println((endTime1 - startTime1) / 1000);
		
		long startTime2 = System.currentTimeMillis();
		ArrayList<Entry> LOL = (ArrayList<Entry>)Query.searchQuery("Math_Physics_NaturalSc", "Chemistry");
		for (Entry E : LOL) {
			System.out.println(E.toString());
			//System.out.println(E.getTerm()+", "+E.getSubject());
		}
		long endTime2 = System.currentTimeMillis();
		System.out.println((endTime1 - startTime1) / 1000);
		
		long startTime3 = System.currentTimeMillis();
		ArrayList<Entry> OLO = (ArrayList<Entry>) Query.searchQuery("Adult"); 
		for (Entry E : OLO) {
			System.out.println(E.toString());
			//System.out.println(E.getTerm()+", "+E.getSubject());
		}
		long endTime3 =System.currentTimeMillis();
		System.out.println((endTime1 -  startTime1) / 1000);


	}
}
