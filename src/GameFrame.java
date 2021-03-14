import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.WeakHashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Pieces.Piece;


public class GameFrame extends JFrame {
	
	 private JPanel infoPanel;
	 private GamePanel gamePanel;
	 public JLabel infoLabel;
	 
	 @SuppressWarnings("deprecation")
	public GameFrame (String title)
	 {
		    super(title);
		     
		    this.setLayout(new GridLayout(0, 1));           //split the grid with panels
			setLocationRelativeTo(null);
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);   
			setResizable(false);
			
		    gamePanel=new GamePanel();
		    this.infoLabel = new JLabel("ldzkhd", JLabel.RIGHT);
            infoLabel.setSize(GamePanel.N*Piece.SIZE, GamePanel.N*Piece.SIZE);
           // infoLabel.setB
		    //gamePanel.add(infoLabel);
		    getContentPane().add(gamePanel);     
		    setSize( GamePanel.N*Piece.SIZE+150+infoLabel.getWidth(), GamePanel.N*Piece.SIZE+180);
	        
	        
	        this.infoPanel = new JPanel(new BorderLayout());
	        this.infoPanel.add(this.infoLabel);
	        this.add(this.infoPanel);
	        
	       
			show();    
	 }
}
	 
	 
	

	



