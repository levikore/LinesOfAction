package Pieces;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;


public class Piece {
	public static int SIZE = 72;
	public int  position;
	public Color  color;
	public Image imageBlack,imageWhite;
	
	
	public Piece(int pos, Color  color){
		 this.position = pos;
		 this.color = color;
		 try {    
				imageBlack=new ImageIcon("pictures/redd.png").getImage();  
				imageWhite=new ImageIcon("pictures/whitee.png").getImage(); 
		    } catch (Exception ex) {
		              // handle exception...
		    }
	}


	public  void Draw(Graphics g){
		 if ( this.color == Color.BLACK)
			 g.drawImage(imageBlack, GetCol()*SIZE +63, GetRow()*SIZE+65, null);
		 else
			 g.drawImage(imageWhite, GetCol()*SIZE +63, GetRow()*SIZE+65, null);
	 }
	
		
	public int GetCol(){		

		return this.position % 8;
	}
	
	public int GetRow(){
		return this.position  / 8;
	}


	public void SetNewPos(int i) {

		this.position = i;
	}
	 
}
