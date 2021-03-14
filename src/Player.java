import java.awt.Graphics;
import java.awt.Point;
import java.util.Enumeration;
import java.util.Hashtable;

import static java.lang.System.out;


import Pieces.Color;
import Pieces.Piece;

public class Player {
 
	  Hashtable pieces = new Hashtable();
	  public static Piece pointFrom;
	  Color color;
	  long myMask;
	
//-----------------------------------------------------------------------------------	
//-----------------------------------------------------------------------------------	
	  public Player(Color color){
		  this.color = color;
		  this.pointFrom = null;	
		  this.myMask=0L;
		  }
	  
	  void  setPieces(){
		    if ( color == Color.WHITE ) {
			    for (int row = 1; row < GamePanel.N-1; row++) {
			       pieces.put(row*GamePanel.N + 0, new Piece(row*GamePanel.N+0, color));
	   			   pieces.put(row*GamePanel.N + GamePanel.N-1, new Piece(row*GamePanel.N+GamePanel.N-1, color));
				}    	  
		    }
		    else {
			    for (int col = 1; col < GamePanel.N-1; col++) {
				       pieces.put(0*GamePanel.N + col, new Piece(0*GamePanel.N+col, color));
		   			   pieces.put((GamePanel.N-1)*GamePanel.N + col, new Piece((GamePanel.N-1)*GamePanel.N+col, color));
					}    
			}
	  }
//------------------------------------------------------------------------------------
//------------------------------------------------------------------------------------
	public void Draw(Graphics g) {
		    Enumeration  keys = pieces.keys(); 
	        Piece piece;
	        while(keys.hasMoreElements()) { 
	        	piece = (Piece) pieces.get( Integer.parseInt(keys.nextElement().toString())); 
	        	piece.Draw(g);
	        } 
		
	}
//------------------------------------------------------------------------------------
//------------------------------------------------------------------------------------
	public boolean CheckRules(int row, int col, long oppMask) //בדיקה האם המהלך חוקי
	{//O(N)
	
		boolean valMove=false;
		
		if(pieces.containsKey(row*GamePanel.N+col))  //same color piece on end destination
		{
			 pointFrom = null;
			 return false;
		}
			
		if(pointFrom.GetRow()==row) //movement along a row
			valMove=moveInRow(col, row, oppMask);
		else
			if(pointFrom.GetCol()==col) //movement along a column
				valMove=moveInCol(col,  row,  oppMask);
			else 
				if(row-pointFrom.GetRow() == col-pointFrom.GetCol()) //movement along a diagonal parallel to main diagonal
					valMove=moveInMainDiags(col, row, oppMask);
				else
					if(row-pointFrom.GetRow() == pointFrom.GetCol()-col)//movement along a diagonal parallel to secondary diagonal
						valMove=moveInSecondaryDiags(col, row, oppMask);
			
			
		if(valMove)
		 Move(row,col);
		
	    pointFrom = null;
	    return valMove;
	}
	//------------------------------------------------------------------
	/*ROW*/
	//------------------------------------------------------------------
	private boolean moveInRow(int col, int row, long oppMask)
	{//O(N)
		oppMask >>>= (GamePanel.N-1-row)*GamePanel.N;
		int countPiece=countPiecesInRow(row, (byte) oppMask); 
		
		//out.println(countPiece);	
		
		if(countPiece != Math.abs(col-pointFrom.GetCol())) //num of pieces != num of moves
	    {
	    	 pointFrom = null;
	    	 return false;
	    }
	   
	    if(pointFrom.GetCol() < col) //move right
	    	return moveRightInRow(col, (byte) oppMask);
	    else //move left
	    	return moveLeftInRow(col, (byte) oppMask);
	}
	//-------------------------------------------------------------------
	/*count num of pieces in row:*/
	//-------------------------------------------------------------------
	private int countPiecesInRow(int row, byte rowMask)
	{//(O(N)
		 int countPiece=0;
		//count num of my pieces:
		for(int key=row*GamePanel.N; key<(row+1)*GamePanel.N; key++) 
			if(pieces.containsKey(key)) 
				countPiece++;
	    
	    return CountOppPieces(countPiece, rowMask);//continuation: count of opponents pieces
	}
	//--------------------------------------------------------------------
	//--------------------------------------------------------------------
	private boolean moveRightInRow(int col, byte tempMask)
	{//O(N)
	    
		    byte displayMask = (byte) (1 << (GamePanel.N-2-pointFrom.GetCol())); //N-2=N-1-1
	    	
	    	for ( int i = pointFrom.GetCol()+1; i < col; i++ )
		    { 
			     if((tempMask & displayMask) != 0)// an opponents piece was found on the way 
			    	 return false;
			    	 
			      tempMask <<= 1; // shift value left by 1  
			} // for 
	    	
	    return true;
	}
	//----------------------------------------------------------------------
	//----------------------------------------------------------------------
	private boolean moveLeftInRow(int col, byte tempMask)
	{//O(N)
		byte displayMask = (byte) (1 << (GamePanel.N-pointFrom.GetCol())); //N=N-1+1
    	
    	for ( int i = pointFrom.GetCol()-1; i > col; i-- )
	    { 
		     if((tempMask & displayMask) != 0)// an opponents piece was found on the way 
		    	 return false;
		      tempMask >>= 1; // shift value right by 1  
		} // for 
    	
    	return true;
	}
	//-----------------------------------------------------------------------
	/*COL*/
	//------------------------------------------------------------------
	private boolean moveInCol(int col, int row, long oppMask)
	{//O(N)
		byte colMask=0, prod=1;
   	   
   	    oppMask /= (long) Math.pow(2, GamePanel.N-1-col);
   	   
   		for(int i=GamePanel.N-1; i>=0; i--) //creating col mask
   		{
   			int coeff = (int)(oppMask % 2);
   	   	    colMask+=coeff*prod;
   	   	    prod*=2;
   	   	    oppMask /= (long) Math.pow(2, GamePanel.N);
   		}
		int countPiece=countPiecesInCol(col, colMask); 
		
		//out.println(countPiece);	
		
		if(countPiece != Math.abs(row-pointFrom.GetRow())) //num of pieces != num of moves
	    {
	    	 pointFrom = null;
	    	 return false;
	    }
	   
	    if(pointFrom.GetRow() < row) //move down
	    	return moveDownInCol(row, colMask);
	    else //move up
	    	return moveUpInCol(row, colMask);
	}
	//-------------------------------------------------------------------
	/*count num of pieces in col:*/
	//-------------------------------------------------------------------
	private int countPiecesInCol(int col, byte colMask)
	{//O(N)
		 int countPiece=0, lastkey=col+(GamePanel.N-1)*GamePanel.N;
		//count num of my pieces:
		for(int key=col; key<=lastkey; key+=GamePanel.N) 
			if(pieces.containsKey(key)) 
				countPiece++;
		
	    return CountOppPieces(countPiece, colMask);//continuation: count of opponents pieces
	}
	//--------------------------------------------------------------------
	//--------------------------------------------------------------------
	private boolean moveDownInCol(int row, byte colMask)
	{//O(N)
	    
		    byte displayMask = (byte) (1 << (GamePanel.N-2-pointFrom.GetRow())); //N-2=N-1-1
	    	
	    	for ( int i = pointFrom.GetRow()+1; i < row; i++ )
		    { 
			     if((colMask & displayMask) != 0)// an opponents piece was found on the way 
			    	 return false;
			    	 
			      colMask <<= 1; // shift value left by 1  
			} // for 
	    	
	    return true;
	}
	//----------------------------------------------------------------------
	//----------------------------------------------------------------------
	private boolean moveUpInCol(int row, byte colMask)
	{//O(N)
		byte displayMask = (byte) (1 << (GamePanel.N-pointFrom.GetRow())); //N=N-1+1
    	
    	for ( int i = pointFrom.GetRow()-1; i > row; i-- )
	    { 
		     if((colMask & displayMask) != 0)// an opponents piece was found on the way 
		    	 return false;
		     colMask >>= 1; // shift value right by 1  
		} // for 
    	
    	return true;
	}
	//----------------------------------------------------------------------
	//----------------------------------------------------------------------
	private int CountOppPieces(int countPiece, byte tempMask)
	{//O(N)
		byte displayMask = (byte) (1 << (GamePanel.N-1)); 
	    for ( int i = 0; i < GamePanel.N; i++ )
	    { 
		     if((tempMask & displayMask) != 0) 
		    	 countPiece++;
		      tempMask <<= 1;  // shift value left by 1  
		} // for 
	    return countPiece;
	}
	//-----------------------------------------------------------------------
	/*Main Diags*/
	//-----------------------------------------------------------------------
	private boolean moveInMainDiags(int col, int row, long oppMask)
	{//O(N)
		byte diagMask=0, prod=1;
		int diff=row-col, diagSize;
		
		if(diff>=0) //lower main diag half (lower left corner of the board)
		{
			diagSize=GamePanel.N - diff;	
			oppMask /= (long) Math.pow(2, diff);
		}
			
		
		else //upper main diag half (upper right corner of the board)
		{
			diagSize=GamePanel.N + diff;	
			oppMask /= (long) Math.pow(2, (-diff)*GamePanel.N);
		}
		
		/*test*/
		//out.println("diagSize: "+diagSize);
		
   	   int oppPieceCount=0;
   		for(int i=1; i<=diagSize; i++) //creating diagMask
   		{
   			int coeff = (int)(oppMask % 2);
   	   	    diagMask+=coeff*prod;
   	   	    prod*=2;
   	   	    oppMask /= (long) Math.pow(2, GamePanel.N+1);
   	   	    oppPieceCount += coeff; //counting opponents pieces (coeff = 1 or 0) 
   		}
   		
   		/*test*/
   		//out.println("my: "+countMyPiecesInMainDiags(diff, diagMask)+", "+"opp: "+oppPieceCount);
   		
		int countPiece = countMyPiecesInMainDiags(diff, diagMask) + oppPieceCount; 
		
		//out.println("total: "+countPiece);
		
		if(countPiece != Math.abs(row-pointFrom.GetRow())) //num of pieces != num of moves
	    {
	    	 pointFrom = null;
	    	 return false;
	    }
	   
	    if(pointFrom.GetRow() < row) //move down-right
	    	return moveDownRightInDiag(row,col, diagMask);
	    else //move up-left
	    	return moveUpLeftInDiag(row,col, diagMask);
	}
	//-------------------------------------------------------------------
	/*count num of pieces in main diagonals:*/
	//-------------------------------------------------------------------
	private int countMyPiecesInMainDiags(int diff, byte diagMask)
	{//O(N)
		 int countPiece=0, diagSize, key;
		 
		 if(diff>=0) //lower main diag half (lower left corner of the board)
		 {
			 key=diff * GamePanel.N; //diff = upper row of diag (col=0)
			 diagSize=GamePanel.N - diff;
		 }
		 
		 else //upper main diag half (upper right corner of the board)
		 {
			 key=Math.abs(diff);  //diff = left col of diag (row=0)
			 diagSize=GamePanel.N + diff;	
		 }
			
	
		//count num of my pieces:
		for(int i=1; i<=diagSize; i++) 
		{
			if(pieces.containsKey(key)) 
				countPiece++;
			key += GamePanel.N+1;
		}
			
	    return countPiece;
	}
	//--------------------------------------------------------------------
	//--------------------------------------------------------------------
	private boolean moveDownRightInDiag(int row, int col, byte diagMask)
	{//O(N)
		    if(row>=col) //lower main diag half (lower left corner of the board)
		    	return moveDownInCol(row, diagMask);
		    else //upper main diag half (upper right corner of the board)
		    	return moveRightInRow(col, diagMask);	    	
	}
	//----------------------------------------------------------------------
	//----------------------------------------------------------------------
	private boolean moveUpLeftInDiag(int row, int col, byte diagMask)
	{//O(N)
		if(row>=col) //lower main diag half (lower left corner of the board)
	    	return moveUpInCol(row, diagMask);
	    else //upper main diag half (upper right corner of the board)
	    	return moveLeftInRow(col, diagMask);	 
	}
	//----------------------------------------------------------------------
	/*Secondary Diags*/
	//-----------------------------------------------------------------------
	private boolean moveInSecondaryDiags(int col, int row, long oppMask)
	{//O(N)
		byte diagMask=0, prod=1;
		int sum=row+col, diagSize;
		
		if(sum < GamePanel.N) //upper secondary diag half (upper left corner of the board)
		{
			diagSize=sum+1;	
			oppMask /= (long) Math.pow(2, GamePanel.N*(GamePanel.N-sum)-1);
		}
			
		
		else //lower secondary diag half (lower right corner of the board)
		{
			diagSize=2*GamePanel.N - 1 - sum;	
			oppMask /= (long) Math.pow(2, 2*(GamePanel.N-1) - sum);
		}
		
		/*test*/
		//out.println("diagSize: "+diagSize);
		
   	    int oppPieceCount=0;
   		for(int i=1; i<=diagSize; i++) //creating diagMask
   		{
   			int coeff = (int)(oppMask % 2);
   	   	    diagMask+=coeff*prod;
   	   	    prod*=2;
   	   	    oppMask /= (long) Math.pow(2, GamePanel.N-1);
   	   	    oppPieceCount += coeff; //counting opponents pieces (coeff = 1 or 0) 
   		}
   		
   		/*test*/
   		//out.println("my: "+countMyPiecesInSecondaryDiags(sum, diagMask)+", "+"opp: "+oppPieceCount);
   		
		int countPiece = countMyPiecesInSecondaryDiags(sum, diagMask) + oppPieceCount; 
		
		//out.println("total: "+countPiece);
		
		if(countPiece != Math.abs(row-pointFrom.GetRow())) //num of pieces != num of moves
	    {
	    	 pointFrom = null;
	    	 return false;
	    }
	   
	    if(pointFrom.GetRow() < row) //move down-left
	    	return moveDownLeftInDiag(row,col, diagMask);
	    else //move up-right
	    	return moveUpRightInDiag(row,col, diagMask);
	}
	//-------------------------------------------------------------------
	/*count num of pieces in secondary diagonals:*/
	//-------------------------------------------------------------------
	private int countMyPiecesInSecondaryDiags(int sum, byte diagMask)
	{//O(N)
		 int countPiece=0, diagSize, key;
		 
		 if(sum < GamePanel.N) //upper secondary diag half (upper left corner of the board)
		 {
			 key=sum; //sum = right col of diag (row=0)
			 diagSize=sum+1;	
		 }
		 
		 else //lower secondary diag half (lower right corner of the board)
		 {
			 key=GamePanel.N*(sum-6)-1;  //sum = upper row of diag + N-1 (col=N-1).  **GamePanel.N - 2 = 6
			 diagSize=2*GamePanel.N - 1 - sum;	
		 }
			
	
		//count num of my pieces:
		for(int i=1; i<=diagSize; i++) 
		{
			if(pieces.containsKey(key)) 
				countPiece++;
			key += GamePanel.N-1;
		}
			
	    return countPiece;
	}
	//--------------------------------------------------------------------
	//-------------------------------------------------------------------
	private boolean moveDownLeftInDiag(int row, int col, byte diagMask)
	{//O(N)
			int sum=row+col;
		    if(sum<GamePanel.N) //upper secondary diag half (upper left corner of the board)
		    	diagMask <<= GamePanel.N - sum - 1;
		    //else: lower secondary diag half (lower right corner of the board)
		    return moveDownInCol(row, diagMask);	    	
	}
	//----------------------------------------------------------------------
	//----------------------------------------------------------------------
	private boolean moveUpRightInDiag(int row, int col, byte diagMask)
	{//O(N)
		int sum=row+col;
	    if(sum<GamePanel.N) //upper secondary diag half (upper left corner of the board)
	    	diagMask <<= GamePanel.N - sum - 1;
	    //else: lower secondary diag half (lower right corner of the board)
	    return moveUpInCol(row, diagMask);	    
	}
	
	//-----------------------------------------------------------------------
	//-----------------------------------------------------------------------
	public void Move(int row, int col) // הזזה
	{//O(1)
		int oldKey = pointFrom.GetRow()*GamePanel.N + pointFrom.GetCol();
		pieces.remove(oldKey);
	    int newKey = row*GamePanel.N + col;
		pointFrom.SetNewPos(newKey);
		pieces.put(newKey, pointFrom);	
    		
	}
	//-----------------------------------------------------------------------
	
}




