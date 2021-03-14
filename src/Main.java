import java.awt.BorderLayout;

import javax.swing.*;

import Pieces.Piece;

import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;

import java.util.Enumeration;


public class Main {

	static GamePanel panel ;
	static InfoPanel infoPanel ;
	
	public static void main(String[] args) {
		
		JFrame frame = new JFrame("Lines of Action");
			
		panel = new GamePanel();
		frame.getContentPane().add(panel);    
		infoPanel = new InfoPanel();
		frame.getContentPane().add(infoPanel,BorderLayout.EAST); 
		   
		frame.setSize( GamePanel.N*Piece.SIZE+350, GamePanel.N*Piece.SIZE+180); 
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.show();  
		
		
		
		
		MenuBar menu= new MenuBar();
		  
	    Menu game = new Menu("Game");
	    
		Menu newGame = new Menu("New Game"); 
		game.add(newGame);
		
		MenuItem pvp=new MenuItem("Player vs. Player");
		Menu pvc = new Menu("Player vs. Computer");
		newGame.add(pvp);
		newGame.add(pvc);
		
		MenuItem easy =new MenuItem("Easy");
		MenuItem medium =new MenuItem("Medium");
		MenuItem hard =new MenuItem("Hard");
		pvc.add(easy);
		pvc.add(medium);
		pvc.add(hard);
		
		MenuItem exit = new MenuItem("Exit");
		game.add(exit);
		game.addSeparator();
		menu.add(game);
		         
		     
		frame.setMenuBar(menu);

		//add a listener to this item
		pvp.addActionListener(evt -> { //listen for event

			  int key1, key2;
			  Enumeration  keys1 = panel.players[0].pieces.keys();
			  Enumeration  keys2 = panel.players[1].pieces.keys();
			  while(keys1.hasMoreElements())
			  {
				  key1=Integer.parseInt(keys1.nextElement().toString());
				  panel.players[0].pieces.remove(key1);
			  }

			  while(keys2.hasMoreElements())
			  {
				  key2=Integer.parseInt(keys2.nextElement().toString());
				  panel.players[1].pieces.remove(key2);
			  }

			  panel.showGUI();
			  panel.repaint();
			  panel.turn=1;
			  infoPanel.labelTurn.setText("Turn: BLACK");
			  infoPanel.textArea.setText("Start game...\n----------------------------\n");

			  panel.isAI=false;
			  infoPanel.labelMode.setText("Mode: Player vs. Player");
		});
		
		easy.addActionListener(new java.awt.event.ActionListener() //add a listener to this item
		 {
		  public void actionPerformed(java.awt.event.ActionEvent evt) { //listen for event
			  
			    int key1, key2;
			    Enumeration  keys1 = panel.players[0].pieces.keys(); 
			    Enumeration  keys2 = panel.players[1].pieces.keys(); 
			    while(keys1.hasMoreElements()) 
			    { 
			    	key1=Integer.parseInt(keys1.nextElement().toString());
			    	panel.players[0].pieces.remove(key1);
			    }
			    
			    while(keys2.hasMoreElements()) 
			    { 
			    	key2=Integer.parseInt(keys2.nextElement().toString());
			    	panel.players[1].pieces.remove(key2);
			    }
			  
			    panel.showGUI();
			    panel.repaint();
			    panel.turn=1;
			    infoPanel.labelTurn.setText("Turn: BLACK");
			    infoPanel.textArea.setText("Start game...\n----------------------------\n");
		   
			    panel.isAI=true;
			    panel.depth=1;
			    infoPanel.labelMode.setText("Mode: Player vs. Computer: easy");
			    
		  }
		 } );
		
		medium.addActionListener(new java.awt.event.ActionListener() //add a listener to this item
		 {
		  public void actionPerformed(java.awt.event.ActionEvent evt) { //listen for event
			  
			    int key1, key2;
			    Enumeration  keys1 = panel.players[0].pieces.keys(); 
			    Enumeration  keys2 = panel.players[1].pieces.keys(); 
			    while(keys1.hasMoreElements()) 
			    { 
			    	key1=Integer.parseInt(keys1.nextElement().toString());
			    	panel.players[0].pieces.remove(key1);
			    }
			    
			    while(keys2.hasMoreElements()) 
			    { 
			    	key2=Integer.parseInt(keys2.nextElement().toString());
			    	panel.players[1].pieces.remove(key2);
			    }
			  
			    panel.showGUI();
			    panel.repaint();
			    panel.turn=1;
			    infoPanel.labelTurn.setText("Turn: BLACK");
			    infoPanel.textArea.setText("Start game...\n----------------------------\n");
		   
			    panel.isAI=true;
			    panel.depth=2;
			    infoPanel.labelMode.setText("Mode: Player vs. Computer: medium");
		  }
		 } );

		//add a listener to this item
		hard.addActionListener(evt -> { //listen for event

			  int key1, key2;
			  Enumeration  keys1 = panel.players[0].pieces.keys();
			  Enumeration  keys2 = panel.players[1].pieces.keys();
			  while(keys1.hasMoreElements())
			  {
				  key1=Integer.parseInt(keys1.nextElement().toString());
				  panel.players[0].pieces.remove(key1);
			  }

			  while(keys2.hasMoreElements())
			  {
				  key2=Integer.parseInt(keys2.nextElement().toString());
				  panel.players[1].pieces.remove(key2);
			  }

			  panel.showGUI();
			  panel.repaint();
			  panel.turn=1;
			  infoPanel.labelTurn.setText("Turn: BLACK");
			  infoPanel.textArea.setText("Start game...\n----------------------------\n");

			  panel.isAI=true;
			  panel.depth=3;
			  infoPanel.labelMode.setText("Mode: Player vs. Computer: hard");
		});

		//add a listener to this item
		exit.addActionListener(evt -> { //listen for event
		System.exit(0);
		});
	   
	}

}
