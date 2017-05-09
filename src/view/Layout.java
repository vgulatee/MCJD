package view;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.LayoutManager2;
import java.awt.Rectangle;
import java.util.LinkedList;

public class Layout implements LayoutManager2 {

	protected static LinkedList<Component> fixedComponents = new LinkedList<Component>();
	protected static LinkedList<Component> displays = new LinkedList<Component>();
	protected static LinkedList<Dimension> size = new LinkedList<Dimension>();
	public static int numComponents = 0;
	public static int numResults = 0;
	public static int currentEntry = 0;

	final static int windowHeight;
	final static int windowWidth;

	static {	

		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gd = ge.getDefaultScreenDevice();
		windowWidth = (int) gd.getDefaultConfiguration().getBounds().getWidth();
		windowHeight = (int) gd.getDefaultConfiguration().getBounds().getHeight();
	}

	public static Dimension getComponentSize(int index){
		return size.get(index);		
	}

	public static Rectangle getComponentPosition(int index){
		return fixedComponents.get(index).getBounds();
	}


	@Override
	public void addLayoutComponent(String name, Component comp) {
		if (name.equals("TEXT") || name.equals("SUBMIT") || name.equals("OPTION") || name.equals("MENU2") || name.equals("NEXT")) {
			fixedComponents.add(comp);
			size.add(comp.getPreferredSize());
			numComponents++;
		}	
		if (name.equals("DISPLAY BOX")) {
			displays.add(comp);
			numResults++;
		}
	}

	@Override
	public void layoutContainer(Container parent) {		

		int totalWidth = 0;
		for (int i = 0; i < fixedComponents.size()-2; i++){
			Dimension dim = size.get(i);	
			totalWidth += dim.getWidth();
		}


		for (int i = 0; i < fixedComponents.size(); i++){
			Component current = fixedComponents.get(i);
			Dimension dim = size.get(i);

			if (current.getName() == null) continue;

			if (current.getName().equals("TEXT FIELD")) 
				current.setBounds(windowWidth/2 - totalWidth/2 + dim.width/2, windowHeight/3, dim.width,dim.height);
			else if (current.getName().equals("SUBMIT"))
				current.setBounds(windowWidth/2  + totalWidth/2 + dim.width/2, windowHeight/3, dim.width,dim.height);
			else if (current.getName().equals("OPTION"))
				current.setBounds(windowWidth/2 - totalWidth/2, windowHeight/4, dim.width,dim.height);	
			else if (current.getName().equals("MENU2"))
				current.setBounds(windowWidth/2 - totalWidth/2, windowHeight/2-2*dim.height, dim.width,dim.height);	
			else if (current.getName().equals("NEXT")) {				
				current.setBounds(windowWidth/2 + totalWidth/2 + dim.width/2, windowHeight/2 - dim.height, dim.width,dim.height);
			}
		}

		if (displays != null && !displays.isEmpty() && numResults > 0){			
			Component current = displays.get(currentEntry);			
			Dimension dim = displays.get(currentEntry).getPreferredSize();
			current.setBounds(windowWidth/2 - dim.width/2, windowHeight/2, dim.width, dim.height);			
		}

	}

	public static void removeAll(){
		displays = new LinkedList<Component>();
		currentEntry = 0;
		numResults = 0;
	}
	
	public static void displayNext(){
				
		if (numResults == 0) return;
		displays.get(currentEntry).transferFocus();		
				
		currentEntry++;		
		if (currentEntry >= numResults) currentEntry = 0;			
				
		Component current = displays.get(currentEntry);		
		Dimension dim = displays.get(currentEntry).getPreferredSize();
		current.setBounds(windowWidth/2 - dim.width/2, windowHeight/2, dim.width, dim.height);
		current.setVisible(true);
	}
	
	@Override
	public Dimension minimumLayoutSize(Container parent) {
		return preferredLayoutSize(parent);
	}

	@Override
	public Dimension preferredLayoutSize(Container parent) {
		return new Dimension(windowWidth,windowHeight);
	}

	@Override
	public void removeLayoutComponent(Component comp) {
		if (fixedComponents.contains(comp)) fixedComponents.remove(comp);
		else displays.remove(comp);

	}

	@Override
	public void addLayoutComponent(Component arg0, Object arg1) {
		if (arg1 == null) throw new IllegalArgumentException();			
		else if (arg1 instanceof String) addLayoutComponent((String) arg1, arg0);
		else throw new IllegalArgumentException("Invalid constraints specified: " + arg1);

	}

	@Override
	public float getLayoutAlignmentX(Container arg0) {
		return 0.5f;
	}

	@Override
	public float getLayoutAlignmentY(Container arg0) {
		return 0.5f;
	}

	@Override
	public void invalidateLayout(Container arg0) {}

	@Override
	public Dimension maximumLayoutSize(Container parent) {
		return preferredLayoutSize(parent);
	}



}
