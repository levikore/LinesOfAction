import Pieces.Color;
import Pieces.Piece;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Enumeration;
import java.util.Hashtable;

import static java.lang.System.out;


class GamePanel extends JPanel{
 
	public static int  N=8;
	private Image imageBoard;	
    public int turn;
    public  Player [] players = new Player[2];
    int index;
    public boolean isAI;
    public int depth;// depth of game tree in AI
   
	
    public GamePanel(){
        super();
        try {    
        	imageBoard=new ImageIcon("pictures/boardd.jpg").getImage();
      
        } catch (Exception ex) {
              // handle exception...
        }
        players[0] =new Player(Color.WHITE);
        players[1] =new Player(Color.BLACK);
        
        isAI=true;
        this.turn = 1; 
        depth=1;
        this.setBounds(0, 0, N*Piece.SIZE,N*Piece.SIZE);
        showGUI();

        this.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
            	GamePanel gamePanel = (GamePanel)e.getSource();
            	int row = (e.getY()-63) / Piece.SIZE;
            	int col = (e.getX()-65) / Piece.SIZE;
            	
            	gamePanel.DoStep(col, row);
            	
            	if(isAI && turn == 0)
            	{
            		AI myAI= new AI(gamePanel);
            		Thread think = new Thread(myAI);
            		think.start();         		
            	}
            }
        });
    }
    
    public GamePanel(GamePanel gamePanel)
    {
    	super();
		this.setBounds(0, 0, N*Piece.SIZE,N*Piece.SIZE);
        
        players = new Player[2];
        
        players[0] =new Player(Color.WHITE);
        players[1] =new Player(Color.BLACK);
        
        for(int i=0;i<=1;i++)
        {
        	players[i].pieces=new Hashtable();
        	
        	Enumeration  keys = gamePanel.players[i].pieces.keys(); 
            while(keys.hasMoreElements()) 
            { 
            	int key= Integer.parseInt(keys.nextElement().toString());
            	players[i].pieces.put(key, new Piece(key, gamePanel.players[i].color));
            }
            players[i].pointFrom=null;
            players[i].color=gamePanel.players[i].color;
            players[i].myMask=gamePanel.players[i].myMask;
            
            turn=gamePanel.turn;
        }
    }
    
    
    public void showGUI(){
    	players[0].setPieces();
    	players[1].setPieces();
    }
   
   
	@Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(imageBoard, 0, 0, null); // see javadoc for more info on the parameters
        players[0].Draw(g);
        players[1].Draw(g);
    }
    
  //---------------------------------------------------------------------------
    public  void   ChangeTurn(){
        //Change the turn.
  	  turn = turn == 0 ? 1 : 0; //0-white, 1-black
  	  Main.infoPanel.labelTurn.setText("TURN: "+players[turn].color);
       
       
    } //ChangeTurn.
    
    //---------------------------------------------------------------------------
    public void DoStep(int col, int row)  {//O(N^2)
        boolean   bDoStep = false;
        int key = row*GamePanel.N + col;
        
//        if(turn==2)
//        	return false;
        
        if ( Player.pointFrom == null )
        {
        	 //  From
            
        	 if ( players[0].pieces.containsKey(key)){
        		 Player.pointFrom =  (Piece) players[0].pieces.get(key );
        		 index = 0;
        	 }
        	 else {
        		 Player.pointFrom =  (Piece) players[1].pieces.get(key );
        		 index = 1;
        	 }
        	
             bDoStep = true;
        	
        	 
        }//if ( Player.pointFrom == null )
        
        else{  // Dest
        	if(index==turn)
        	{
        		 int oppindex=(index+1)%2;//opposite player
        		 long mask=createPlayerMask(players[oppindex]); //(index+1)%2  opposite player
        		 int oldRow = Player.pointFrom.GetRow(),  oldCol = Player.pointFrom.GetCol();
        		 bDoStep =players[index].CheckRules(row,col, mask);
              
        		 if(bDoStep)
        		 {
        			 Main.infoPanel.textArea.append(players[index].color+": "+"(" + oldRow + "," + oldCol + ")  -->  (" + row + "," + col + ")\n");
        			 
        			 if ( players[oppindex].pieces.containsKey(key))
        			 {
        				 players[oppindex].pieces.remove(key); //eat opponent 
        				 Main.infoPanel.textArea.append("         ate opponent in: ("+key / 8+","+key % 8+")\n");
        			 }
        			 
        			 if(!checkEnd(key))
        				 ChangeTurn();
        			 else 
        			 {
        				 turn=2; //end the game if there is a winner 
        				 Main.infoPanel.textArea.append(players[index].color+" WINS");
        				 out.println(players[index].color+" WINS");
        			 }//else
        		 }//if(bDoStep)
        		 else Main.infoPanel.textArea.append(players[index].color+": Invalid Move\n");
        		 
        		 repaint();
        	}//if(index==turn)
        	 
        	else {
        		Player.pointFrom=null;
			}
        }// else{  // Dest 

	} // DoStep( int col
    //----------------------------------------------------------------------------
   public long createPlayerMask(Player player) //O(N^2)
   {
   	    long mask=0, prod=1;
   	    int boardSquere=N*N;
   	   
   		for(int key=boardSquere-1; key>=0; key--)
   		{
   			int coeff=(player.pieces.containsKey(key)) ? 1 : 0;
   	   	    mask+=coeff*prod;
   	   	    prod*=2;
   		}
   		return mask;
   }
   //-----------------------------------------------------------------------------
   public boolean checkEnd(int key)
   {//O(N^2)
	   long displayMask = 1L << (N*N-1-key);
	   players[index].myMask = createPlayerMask(players[index]);
	   int numOfPieces = countOnesInPlayerMask(players[index].myMask), numOfPiecesInCluster = countPiecesInCluster(displayMask, key);
	   
	   //out.println("numOfPieces: "+numOfPieces+"numOfPiecesInCluster:"+numOfPiecesInCluster);
	   
	   return numOfPieces==numOfPiecesInCluster;
	      
   }
   //-----------------------------------------------------------------------------
   private int countPiecesInCluster(long displayMask, int key)//O(n) n - num of pieces in cluster
   {
	   int right1, right7, right8, right9, left1, left7, left8, left9;
	   if((players[index].myMask & displayMask) != 0)
	   {
		   players[index].myMask ^= displayMask;
		   
		   left8 = countPiecesInCluster(displayMask << 8, key-8);
		   right8 = countPiecesInCluster(displayMask >>> 8, key+8);
		   
		   if(key%8 != 0)
		   {
			   left1 = countPiecesInCluster(displayMask << 1, key-1);
			   left9 = countPiecesInCluster(displayMask << 9, key-9);
			   right7 = countPiecesInCluster(displayMask >>> 7, key+7);
		   }
		   else
			   left1=left9=right7=0;
		   
		   if(key%8 != 7)
		   {
			   right1 = countPiecesInCluster(displayMask >>> 1, key+1);
			   right9 = countPiecesInCluster(displayMask >>> 9, key+9);
			   left7 = countPiecesInCluster(displayMask << 7, key-7);
		   }
		   else
			   right1=right9=left7=0;
		   
		   return right1 + right7 + right8 + right9 + left1 + left7 + left8 + left9 + 1;
		  
	   }
	   return 0;	  
   }
   //-----------------------------------------------------------------------------
   private int countOnesInPlayerMask(long mask)//O(N^2)
	{
	    int boardSquere=N*N, count=0; 
		long displayMask = 1L << (boardSquere-1); 
	
	    for ( int i = 0; i < boardSquere; i++ )
	    { 
		     if((mask & displayMask) != 0)
		     {
		    	 //out.print("1");
		    	 count++;
		     }
		     //else out.print("0");
		    	 
		    mask <<= 1;  // shift value left by 1  
		} // for 
	    //out.println("");
	    return count;
	}
   //----------------------------------------------------------------------------

    
  }