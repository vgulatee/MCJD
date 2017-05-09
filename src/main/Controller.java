package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;

import graphing.Trie;
import search.Query;
import search.SearchTables;
import view.TextDisplayBox;

public class Controller implements MouseListener, ActionListener{

	private view.UserInterface UI;
	private JFormattedTextField inputTextBox;
	private JComboBox refineSubject;
	private JComboBox refineCategory;
	private JButton nextEntry;	
	private String category = "";
	private String subject = "";
	private static ArrayList<String> largeCategory = new ArrayList<String>();

	public static void main (String[] args){

		//Distinguish between categories to use hashtables and binary search vs a string trie
		largeCategory.addAll(Arrays.asList("Administration","Economy","ElectronicsInformatics","HumanitiesSocialSciences","Industries","Math_Physics_NaturalSc", "NamesAndTitles"));								


		//sort.SortController sorter = new sort.SortController();								//Calls the sorting method to ensure data is in proper order		
		Controller control = new Controller();


		long start = System.currentTimeMillis();
		Query.setHash();														//		
		long end = System.currentTimeMillis();
		System.out.println("Hash time: "+ (end-start)/1000.0);

		//Sets window height and width based on device graphics settings
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gd = ge.getDefaultScreenDevice();		
		int width = (int) gd.getDefaultConfiguration().getBounds().getWidth();
		int height = (int) gd.getDefaultConfiguration().getBounds().getHeight();


		control.UI = new view.UserInterface();		
		view.Layout layout = new view.Layout();

		/*
		 * Creates the text field where the user can enter a search query
		 */
		control.inputTextBox = new view.TextField();
		control.inputTextBox.setMargin(new Insets(0,10,0,10));		

		/*
		 * Creates a JButton using an image located in the resources folder
		 * This JButton is to be used to trigger a search
		 */
		ImageIcon searchIcon = new ImageIcon("resources\\search.png");
		Image img = searchIcon.getImage() ;  
		Image newimg = img.getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH ) ;  
		searchIcon = new ImageIcon(newimg);

		JButton submit = new JButton(searchIcon);		
		submit.setName("SUBMIT");
		submit.setPreferredSize(new Dimension(30, 30));
		submit.addMouseListener(control);

		/*
		 * Creates a JButton using an image located in the resources folder
		 * This JButton is to be used to iterate between the entry to display on-screen 
		 */
		control.nextEntry = new JButton();		
		control.nextEntry.setName("NEXT");
		control.nextEntry.setText("NEXT");		
		control.nextEntry.setPreferredSize(new Dimension(80, 30));
		control.nextEntry.addMouseListener(control);


		/*
		 * Extract the list of categories from the files comprising the dataset
		 */
		String path = "data";
		File directory = new File(path);
		String[] files = (String[]) directory.list();
		String[] categoryList = new String[files.length+1];

		categoryList[0] = "NONE";
		for (int i = 0; i < files.length; i++){
			categoryList[i+1] = files[i].substring(0, files[i].length()-4);			
		}				


		/*
		 * Creates a dropdown menu using the trimmed filenames as categories
		 */
		String[] subList= {"Please enter a category"};		
		control.refineCategory = new view.RefineMenu(categoryList);
		control.refineCategory.addActionListener(control);
		control.refineSubject = new view.RefineMenu(subList);
		control.refineSubject.setName("MENU2");


		/*
		 * Sets the layout manager and adds each of the previously defined elements to the UI
		 */
		Color background = new Color(22,130,202);
		control.UI.setLayout(layout);				
		control.UI.add(control.inputTextBox, "TEXT");
		control.UI.add(submit, "SUBMIT");		
		control.UI.add(control.refineCategory, "OPTION");
		control.UI.add(control.refineSubject, "MENU2");
		control.UI.add(control.nextEntry, "NEXT");
		control.UI.setVisible(true);
		control.UI.revalidate();

		//Creates new JFrame and sets state to visible
		JFrame window = new JFrame();		
		window.setSize(width, height);
		window.add(control.UI);	
		window.setBackground(background);
		window.setLocationRelativeTo(null);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);
		//window.setAlwaysOnTop(true);		
	}


	private static String getCategory(File filename){
		return filename.toString().substring(5, filename.toString().length() - 4).intern();
	}

	private void search(String searchTerm){

		File directory = new File("data");
		File[] files = directory.listFiles();
		ArrayList<Entry> results = new ArrayList<Entry>();


		long begin = System.currentTimeMillis();
		for (int numFiles = 0; numFiles < files.length; numFiles++){			//Iterate through the list of files in the directory
			long start = System.currentTimeMillis();


			String currentCategory = getCategory(files[numFiles]);
			if (largeCategory.contains(currentCategory)){
				System.out.println("start: " + currentCategory);
				results.addAll(search.Query.searchQuery(searchTerm.trim()));
				System.out.println("end: " + currentCategory);
				if (results != null && !results.isEmpty()){
					if (results.size() >= 5) break;
				}
			} else {
				System.out.println("start: " + currentCategory);
				results.addAll(Trie.searchCategory(currentCategory, searchTerm.trim()));				
				if (results.size() >= 5) break;
				System.out.println("end: " + currentCategory);
			}						
			long end = System.currentTimeMillis();
			System.out.println("Search all time: "+ (end-start)/1000.0);

		}
		long finish = System.currentTimeMillis();
		System.out.println("Time to search all files: "+ (finish-begin)/1000.0);
		if (results != null && !results.isEmpty()){
			for (Entry term : results) {
				//System.out.println(term);
				UI.add(new TextDisplayBox(term), "DISPLAY BOX");	
				UI.invalidate();
				UI.revalidate();
			}
		}
	}

	private void search(String category, String searchTerm){		
		ArrayList<Entry> results = new ArrayList<Entry>();
		long start = System.currentTimeMillis();

		if (!largeCategory.contains(category)){			
			results = Trie.searchCategory(category, searchTerm.trim());
		} else {
			results = search.Query.searchQuery(category, searchTerm.trim());						
		}

		long end = System.currentTimeMillis();
		System.out.println("Search Category time: "+ (end-start)/1000.0);

		if (results != null && !results.isEmpty()){
			for (Entry term : results) UI.add(new TextDisplayBox(term), "DISPLAY BOX");							
		}
		UI.invalidate();
		UI.revalidate();
	}

	private void search(String category, String subject, String searchTerm){
		ArrayList<Entry> results = new ArrayList<Entry>();
		long start = System.currentTimeMillis();

		if (!largeCategory.contains(category)){
			results = Trie.searchSubject(category, subject, searchTerm.trim());			
		} else {
			results = search.Query.searchQuery(category, subject, searchTerm.trim());	
		}
		
		long end = System.currentTimeMillis();
		System.out.println("Search category and subject time: "+ (end-start)/1000.0);
		
		if (results != null && !results.isEmpty()){
			for (Entry term : results) UI.add(new TextDisplayBox(term), "DISPLAY BOX");				
			UI.invalidate();
			UI.revalidate();
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {			

		//Ignore clicks to the dropdown menus - these objects are maintained by the actionListener
		if (e.getSource().equals(refineCategory) || e.getSource().equals(refineSubject)) return;

		//Display the next Entry
		if (e.getSource().equals(nextEntry)){								
			view.Layout.displayNext();			
			UI.invalidate();
			UI.revalidate();			
			return;
		}

		view.Layout.removeAll();									//Removes all previous results and refreshes state
		switch (category){		
		case "NONE":
			String text = this.inputTextBox.getText();
			if (text.equals("") || text.trim().equals("")) break;
			search(text.trim());
			break;
		case "":
			String text1 = this.inputTextBox.getText();
			if (text1.equals("") || text1.trim().equals("")) break;
			search(text1.trim());
			break;
		default:			
			if (this.subject.equals("")) search(category, this.inputTextBox.getText().trim());
			else search(category, subject, this.inputTextBox.getText().trim());		
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void actionPerformed(ActionEvent e) {

		JComboBox cb = (JComboBox)e.getSource();
		if (e.getSource() == this.refineCategory){
			category = (String)cb.getSelectedItem();
			if (category.equals("NONE")) {
				this.refineSubject.removeAllItems();
				this.refineSubject.addItem("Please select a category");	
				refineSubject.updateUI();
				refineSubject.revalidate();
				return;
			}
			System.out.println(category);
			Iterable<String> subjects = SearchTables.getSubjectID(category);
			if (subjects == null) return;
			this.refineSubject.removeAllItems();
			for (String subject : subjects){
				this.refineSubject.addItem(subject.intern());
			}
			//refineSubject.repaint();
			refineSubject.updateUI();
			refineSubject.revalidate();
		} else if (e.getSource() == this.refineSubject){
			subject = (String)cb.getSelectedItem();
		}
	}


}
