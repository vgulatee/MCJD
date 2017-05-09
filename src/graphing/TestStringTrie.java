package graphing;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestStringTrie {	

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {			
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {		
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {			
		String[] input = new String[]{"antibodies", "antibody", "antigen", "antipathy", "antioxidant", "antioxidant", "antipathy", "are", "boo", "boo", "by", "chow", "sea", "seashells", "seashells", "sells", "sells", "she", "she", "shells", "shore", "surely", "tea", "the", "the", "theme"};				
		Trie tree = new Trie(input);

		String prefix = "anti";
		ArrayList<String> words = tree.search("", prefix);
		int length = prefix.length();
		for (String word : words){
			if (word.length() < prefix.length()) fail("The word cannot be shorted than the specified prefix");
			if (!word.startsWith(prefix)) fail("String does not start with the specified prefix");
			if (word.length() < length) {
				fail("Each subsequent word found must be equal or greater in length than the previous word");
			} else {
				length = word.length();
			}
		}
	}

	@Test
	public void testFileBased(){
		String prefix = "Alkylation (Petroleum)";
		String filename = "data\\Petroleum.txt";
				
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
			ArrayList<String> data = new ArrayList<String>();
			String info;
			while ((info = reader.readLine()) != null){
				data.add(info);
			}

			String[] dataset = new String[data.size()];
			data.toArray(dataset);
			
			Trie tree = new Trie(dataset);
						
			ArrayList<String> words = tree.search("", prefix);
			int length = prefix.length();
			for (String word : words){
				if (word.length() < prefix.length()) fail("The word cannot be shorted than the specified prefix");
				if (!word.startsWith(prefix)) fail("String does not start with the specified prefix");
				if (word.length() < length) {
					fail("Each subsequent word found must be equal or greater in length than the previous word");
				} else {
					length = word.length();
				}
			}
			
			input.close();
			input = null;
			reader.close();
			reader = null;
			System.gc();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
