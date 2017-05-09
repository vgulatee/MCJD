package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;

public class RefineMenu extends JComboBox /*implements ActionListener*/ {

	public RefineMenu(String[] elements){
		super(elements);
		this.setBackground(Color.WHITE);
		this.setPreferredSize(new Dimension(200,30));
		this.setName("OPTION");
		this.setEditable(false);
		this.setOpaque(true);
		//addActionListener(this);
		this.setVisible(true);		
	}

	/*public void actionPerformed(ActionEvent e) {
        JComboBox cb = (JComboBox)e.getSource();
        String category = (String)cb.getSelectedItem();
        updateLabel(category);
    }
	
	protected void updateLabel(String name) {
        System.out.println(name);        
    }*/
}
