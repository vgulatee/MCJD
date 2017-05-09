package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JTextArea;

import main.Entry;

public class TextDisplayBox extends JTextArea {

	public TextDisplayBox(Entry term){	
		Font f = new Font("Times New Roman", Font.BOLD, 16);
		this.setFont(f);
		
		this.setBackground(new Color(255,248,198));		
		this.setEditable(false);		
		this.setName("DISPLAY BOX");
		this.setLineWrap(true);
		
		this.setText(term.toString());
		
		int maxLen = 50;
		int lines = 0;
		for (String def : term.getContext().split("\n")) {
			maxLen = Math.max(maxLen, def.length());
			lines++;
		}
		
		this.setPreferredSize(new Dimension(800,300));
	}
	
}
