import Pieces.Piece;

import java.util.Enumeration;

import static java.lang.System.out;


public class AI implements Runnable{

	private final int turn;
	GamePanel gamePanel;
	int fromKey;
	int toRow;
	int toCol;
	
	

	
	public AI(GamePanel gamePanel) {
		// TODO Auto-generated constructor stub
		this.turn= gamePanel.turn;
		this.gamePanel=gamePanel;
		
	}
	
	public void run() {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		gamePanel.index=turn;
		
		if(MiniMax(gamePanel.depth)) //if computer wins
			gamePanel.turn=2; //end the game if there is a winner 
		else gamePanel.ChangeTurn();
		
		gamePanel.repaint();
	}
	
	
	private double regularQuadValue(int key, Player player)//O(1)
	{
		int leftup, rightup, leftdown,rightdown;
		leftup=rightup=leftdown=rightdown=0;
		if(player.pieces.containsKey(key))
			leftup=1;
		if(player.pieces.containsKey(key+1))
			rightup=1;
		if(player.pieces.containsKey(key+GamePanel.N))
			leftdown=1;
		if(player.pieces.containsKey(key+GamePanel.N+1))
			rightdown=1;
		
		int sum=leftup+rightup+leftdown+rightdown;
		if(sum==0 || sum==4)
			return 0;
		if(sum==3)
			return -0.25;
		if(sum==2)
			if(leftup==1 && rightdown==1 || leftdown==1 && rightup==1)
				return -0.5;
			else return 0;
		
		return 0;//blank
	}
	
	private double EulerNumber(Player player)
	{//O(1) on a 8X8 board
	 //for arbitrary N O(N^2)
		double eulerSum=0;
		int key;
		for(key=0;key<=54;key++)
			if(key%8 != 7)
				eulerSum+=regularQuadValue(key, player);
		
		for(key=0;key<=6;key++)//upper row
			if(player.pieces.containsKey(key) && !player.pieces.containsKey(key+1) ||
					!player.pieces.containsKey(key) && player.pieces.containsKey(key+1))
				eulerSum+=0.25;
		
		for(key=56;key<=62;key++)//lower row
			if(player.pieces.containsKey(key) && !player.pieces.containsKey(key+1) ||
					!player.pieces.containsKey(key) && player.pieces.containsKey(key+1))
				eulerSum+=0.25;
		
		for(key=0;key<=48;key+=8)//left col
			if(player.pieces.containsKey(key) && !player.pieces.containsKey(key+8) ||
					!player.pieces.containsKey(key) && player.pieces.containsKey(key+8))
				eulerSum+=0.25;
		
		for(key=7;key<=55;key+=8)//right col
			if(player.pieces.containsKey(key) && !player.pieces.containsKey(key+8) ||
					!player.pieces.containsKey(key) && player.pieces.containsKey(key+8))
				eulerSum+=0.25;
		
		if(player.pieces.containsKey(0)) //left upper corner of the board
			eulerSum+=0.25;
		
		if(player.pieces.containsKey(7)) //right upper corner of the board
			eulerSum+=0.25;
		
		if(player.pieces.containsKey(56)) //left lower corner of the board
			eulerSum+=0.25;
		
		if(player.pieces.containsKey(63)) //right lower corner of the board
			eulerSum+=0.25;
		
		return eulerSum;
	}
	
	private int centralization(Player player)
	{//O(n), n - num of keys of particular color
		int value, sum=0, key;

		 Enumeration  keys = player.pieces.keys(); 
		 while(keys.hasMoreElements()) 
		 {
			 key=Integer.parseInt(keys.nextElement().toString());  
			 value=0; //first layer=0
		     if(key>=9 && key<=14 || key>=49 && key<=54 || 
					(key%8==1 && key!=1 && key!=57) || (key%8==6 && key!=6 && key!=62)) //second layer=1
		    	 value=1;
	    	 else
				if(key>=18 && key<=21 || key>=42 && key<=45 || 
						(key%8==2 && key>=18 && key<=42) || (key%8==5 && key>=21 && key<=45)) //third layer=2
					value=2;
					else
						if(key==27 || key==28 || key==35 || key==36) //fourth layer=3
						    value=3;
				
			 sum+=value;
		}
			return sum;			
	}
	
	private double evaluation(GamePanel tempPanel)
	{//O(N^2)	
		int index=turn;
		int oppindex=(index+1)%2;
		
		double myFinalValue = centralization(tempPanel.players[index]) - EulerNumber(tempPanel.players[index]);
		double oppFinalValue= centralization(tempPanel.players[oppindex]) - EulerNumber(tempPanel.players[oppindex]);
		return myFinalValue - oppFinalValue;	
	}
	//--------------------------------------------------------------------------------
	private boolean MiniMax(int depth)
	{//O((n*N^2)^depth * N^2), n - num of keys 
		int bestKey = 0, bestRow=0, bestCol=0, index=turn, oppindex=(index+1)%2, key;
		double max;
		long mask=gamePanel.createPlayerMask(gamePanel.players[oppindex]); // human
	    max=-1000000; //lowest value (-infinity)
		Enumeration  keys = gamePanel.players[index].pieces.keys(); 
	    Piece piece;
	    while(keys.hasMoreElements()) 
	    { 
	    	         key=Integer.parseInt(keys.nextElement().toString());    	            	        
	    	        	 
	            	 piece = (Piece) gamePanel.players[index].pieces.get(key); 
					 for( int row=0;row<GamePanel.N;row++)
						 for( int col=0;col<GamePanel.N;col++)
						 {	 
							 GamePanel child=new GamePanel(gamePanel);
							
							 Player.pointFrom = new Piece( piece.position, piece.color); //From
							 if(child.players[index].CheckRules(row,col, mask)) {
								 int tokey = row*GamePanel.N + col; 
							 	 if ( child.players[oppindex].pieces.containsKey(tokey))
							 		 child.players[oppindex].pieces.remove(tokey); //eat opponent
							 		
						 		double minval=minValue(child, depth-1);
						 		if(minval>max)
						 		{
						 			max=minval;
						 			bestRow = row; bestCol = col;
						 			bestKey = key;//Integer.parseInt(keys.nextElement().toString());
						 		}
						 	 }
						 			
						 }//for(col=0;col<GamePanel.N;col++)
	    
			}//while
		this.fromKey=bestKey;
		this.toRow=bestRow;
		this.toCol=bestCol;
		
		out.println("from (" + fromKey / 8 + "," + fromKey % 8  + ") to (" + toRow +"," + toCol + ")");
		
		Main.infoPanel.textArea.append(gamePanel.players[index].color+": "+"(" + fromKey / 8 + "," + fromKey % 8 + ")  -->  (" + toRow + "," + toCol + ")\n");
		Player.pointFrom = new Piece(this.fromKey, gamePanel.players[index].color);
		gamePanel.players[index].Move(toRow, toCol);
	   

		
		 int newKey = this.toRow*GamePanel.N + this.toCol;	
		 
		 if (gamePanel.players[oppindex].pieces.containsKey(newKey) )
		 {
			 gamePanel.players[oppindex].pieces.remove(newKey); //eat opponent
			 Main.infoPanel.textArea.append("        ate opponent in: ("+newKey / 8+","+newKey % 8+")\n");
			 
			 out.println("****ate opponent in: ("+newKey / 8+","+newKey % 8+")");
		 }
		 
		 if(gamePanel.checkEnd(newKey)) //check end game
		 {
			 out.println(gamePanel.players[index].color+" WINS");
			 
			 Main.infoPanel.textArea.append(gamePanel.players[index].color+" WINS");
			 return true;
		 }	 
		 else return false;
	}
	
	//--------------------------------------------------------------------------------
	private double maxValue(GamePanel node, int depth)
	{//O((n*N^2)^depth * N^2), n - num of keys 
		int index=turn, oppindex=(index+1)%2, boardSquere=GamePanel.N*GamePanel.N;
		double max, MAX=1000000;
		boolean checkedEnd=false;
		
		if(depth==0)
		{
			double eval=evaluation(node);
			out.println(eval);
			return eval;
		}
		
	    long mask=node.createPlayerMask(node.players[oppindex]); //(index+1)%2  opposite player
	    max=-MAX; //lowest value (-infinity)
	    
	    Enumeration  keys = node.players[index].pieces.keys(); 
        
        while(keys.hasMoreElements()) { 
        	     int key= Integer.parseInt(keys.nextElement().toString());
        	     
        	     if(!checkedEnd) //if terminal state wasn't checked yet
        	     {
        	    	 if(node.checkEnd(key)) //first key
        	    		 return MAX;
        	    	 checkedEnd=true; 
        	     }
        	  
        	    	 
        	     Piece piece = (Piece) node.players[index].pieces.get(key); 
				 for(int row=0;row<GamePanel.N;row++)
					 for(int col=0;col<GamePanel.N;col++)
					 {
						GamePanel child=new GamePanel(node);
						Player.pointFrom = new Piece( piece.position, piece.color);;//From
					 	if(child.players[index].CheckRules(row,col, mask))
					 	{
					 		int tokey = row*GamePanel.N + col;
							child.players[oppindex].pieces.remove(tokey); //eat opponent
					 		 
					 		double minval=minValue(child, depth-1);
					 		if(minval>max)
					 			max=minval;
					 	}
					 			
					 }//for(col=0;col<GamePanel.N;col++)
        	     
			
		}//while
		
		return max;
	}
	//-------------------------------------------------------------------------------------
	private double minValue(GamePanel node, int depth)
	{//O((n*N^2)^depth * N^2), n - num of keys 
		int index=turn, oppindex=(index+1)%2, boardSquere=GamePanel.N*GamePanel.N;
		double min, MIN=-1000000;
		boolean checkedEnd=false;
		
		if(depth==0)
		{
			double eval=evaluation(node);
			out.println(eval);
			return eval;
		}
		
	    long mask=node.createPlayerMask(node.players[index]);
	    min=-MIN; //greatest value (+infinity)
	    
	    
	    Enumeration  keys = node.players[oppindex].pieces.keys(); 
        while(keys.hasMoreElements()) { 
        	    int key=Integer.parseInt(keys.nextElement().toString());
        	    
        	    if(!checkedEnd) //if terminal state wasn't checked yet
        	    {
        	    	node.index=(node.index+1)%2;
       	    	 	if(node.checkEnd(key)) //first key
       	    	 		return MIN;
       	    	 node.index=(node.index+1)%2;//back to my index
       	    	 	checkedEnd=true; 
       	        }
        	      	
            	Piece piece = (Piece) node.players[oppindex].pieces.get(key ); 
				 
				 for(int row=0;row<GamePanel.N;row++)
					 for(int col=0;col<GamePanel.N;col++)
					 {
						GamePanel child=new GamePanel(node);
					    Player.pointFrom =  new Piece( piece.position, piece.color);//From
					 	if(child.players[oppindex].CheckRules(row,col, mask))
					 	{
					 		int tokey = row*GamePanel.N + col; 
					 		if ( child.players[index].pieces.containsKey(tokey))
					 			 child.players[index].pieces.remove(tokey); //eat opponent
					 		
					 		double maxval=maxValue(child, depth-1);
					 		if(maxval<min)
					 			min=maxval;
					 	}
					 }//for(col=0;col<GamePanel.N;col++)
        	    
		}//while
		
		return min;
	}

}
