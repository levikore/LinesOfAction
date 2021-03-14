import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

import Pieces.Piece;


public class InfoPanel extends JPanel{
	public  JLabel labelTurn;
	public JLabel labelMode;
	public JTextArea  textArea;
	
	public InfoPanel(){
	        super(new GridBagLayout());
		 
		    labelTurn = new JLabel();
		    labelTurn = new JLabel("Turn: BLACK");
		    
		    labelMode = new JLabel();
		    labelMode = new JLabel("Mode: Player vs. Computer: easy");
	 
	        textArea = new JTextArea(5, 20);
	        textArea.setEditable(false);
	        JScrollPane scrollPane = new JScrollPane(textArea);
	 
	        //Add Components to this panel.
	        GridBagConstraints c = new GridBagConstraints();
	        c.gridwidth = GridBagConstraints.REMAINDER;
	 
	        c.fill = GridBagConstraints.HORIZONTAL;
	        add(labelMode, c);
	        add(labelTurn, c);
	        
	        c.fill = GridBagConstraints.BOTH;
	        c.weightx = 1.0;
	        c.weighty = 1.0;
	        add(scrollPane, c);
	        textArea.setText("Start game...\n----------------------------\n");
	}
	

}
