package view;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFormattedTextField;

public class TextField extends JFormattedTextField {

	public TextField(){		
		this.setBackground(Color.WHITE);
		this.setPreferredSize(new Dimension(500,30));
		this.setEditable(true);
		this.setVisible(true);		
		this.setName("TEXT FIELD");
	}

	
}
