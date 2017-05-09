package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagLayout;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

import javax.swing.JPanel;

public class UserInterface extends JPanel {

	final static int windowHeight;
	final static int windowWidth;

	private static Font font = new Font("TimesRoman", Font.BOLD, 25);
	private static Font font2 = new Font("TimesRoman", Font.BOLD, 20);
	private static Font title = new Font("TimesRoman", Font.BOLD, 50);
	
	static {	

		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gd = ge.getDefaultScreenDevice();
		windowWidth = (int) gd.getDefaultConfiguration().getBounds().getWidth();
		windowHeight = (int) gd.getDefaultConfiguration().getBounds().getHeight();
	}

	public UserInterface(){

	}
	
	public UserInterface(BorderLayout b){
		super(b);
	}
	
	public UserInterface(GridBagLayout b){
		super(b);
	}
	
	public UserInterface(LayoutManager m){
		super(m);
	}
	
	public void paintComponent(Graphics g){
		drawTitle(g);
		drawPrompt(g);
		drawRefine1(g);
		drawRefine2(g);
	}
	

	public void drawTitle(Graphics g){
		Graphics2D g2d = (Graphics2D) g;
		String prompt = "Consolidated Jargon Dictionary";
		g2d.setFont(title);
		g2d.setColor(Color.WHITE);
		Rectangle2D textArea = title.getStringBounds(prompt, g2d.getFontRenderContext());
		Rectangle menuPosition = Layout.getComponentPosition(2);
		
		g2d.drawString(prompt, windowWidth/2 - (int)textArea.getWidth()/2, windowHeight/9);
	}
	
	public void drawPrompt(Graphics g){
		Graphics2D g2d = (Graphics2D) g;
		String prompt = "Search";
		g2d.setFont(font);
		g2d.setColor(Color.WHITE);
		Rectangle2D textArea = font.getStringBounds(prompt, g2d.getFontRenderContext());
		Rectangle menuPosition = Layout.getComponentPosition(0);
		
		//g2d.drawString(prompt, windowWidth/2 - (int)textArea.getWidth()/2, (int) menuPosition.getY() - 30);
		g2d.drawString(prompt, (int) menuPosition.getX(), (int) menuPosition.getY() - (int) textArea.getHeight()/2);
	}
	
	public void drawRefine1(Graphics g){
		Graphics2D g2d = (Graphics2D) g;
		String prompt = "Select a Category:";
		g2d.setFont(font2);
		g2d.setColor(Color.WHITE);
		Rectangle2D textArea = font2.getStringBounds(prompt, g2d.getFontRenderContext());
		
		Rectangle menuPosition = Layout.getComponentPosition(2);
		
		g2d.drawString(prompt, (int) menuPosition.getX(), (int) menuPosition.getY() - (int) textArea.getHeight()/2);
	}
	public void drawRefine2(Graphics g){
		Graphics2D g2d = (Graphics2D) g;
		String prompt = "Select a Subject:";
		g2d.setFont(font2);
		g2d.setColor(Color.WHITE);
		Rectangle2D textArea = font2.getStringBounds(prompt, g2d.getFontRenderContext());
		
		Rectangle menuPosition = Layout.getComponentPosition(3);
		
		g2d.drawString(prompt, (int) menuPosition.getX(), (int) menuPosition.getY() - (int) textArea.getHeight()/2);
	}
	
}
