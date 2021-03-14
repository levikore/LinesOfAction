# LinesOfAction
## About the game:
Lines of Action is a game played on a chessboard. Each player controls 12 white or black stones arranged on the board as follows: six black stones in the top row, six in the bottom, six whites in the leftmost column and six in the far right.

Each stone can move in all eight directions around it but its steps are limited to the total number of stones that are in its way.
So if a stone moves diagonaly and there are 2 stones in said diagonal, the stone can only move by 2 squeres.

To win the player must connect all of his stones on the board.

In player vs player mode the system will check the correctness of the steps and stop the program when it detects a win mode.

In player vs computer mode, the system will scan the computer's move options and select from them the moves that will bring the computer closer to victory in the most effective way.
This mode has three levels of dificulty: easy, meadium, hard

## Computer AI in player vs. computer mode:
### "Weighing a move" algorithm
The purpose of the algorithm: to test the best move for the computer
Algorithm Description: The algorithm is based on the MiniMax search tree. Each possible game board mode is displayed as a node in the search tree. The root in the tree is the current state of the board. Every child is created as a result of a possible move.
The program looks for the best move for the computer and assumes that the opponent is also looking for the best move for himself (i.e. the worst move for the computer).
With each dificulty level, the algorithm creates deeper levels whitin the tree.
The program considers every possible state in the game tree and returns the state with the highest weight.

The weight of each state is derided from two characteristics:
1) Quad Heuristic (Euler Value) - This feature divides the game board into 81 squares, each square contains four smaller squares and builds an Euler number according to these situations:

![image](https://user-images.githubusercontent.com/41550958/111065483-bcbda880-84c2-11eb-843a-9323d712e620.png)

  This is the characteristic of aspiration for unity.

2) Centralization Heuristic - This feature gives weight to each level on the game board in the following way:

![image](https://user-images.githubusercontent.com/41550958/111065572-2938a780-84c3-11eb-98b1-e64d849803a7.png)

  for example:
  
  ![image](https://user-images.githubusercontent.com/41550958/111065610-4b322a00-84c3-11eb-8958-581dbc18f8d0.png)

  This is the characteristic of aspiration for proximity to the center. 


The weight of the state is calculated with the following formula:

 (myCentralization – myEulerNum) - (oppCentralization – oppEulerNum)
 
## Demo:

![ezgif com-video-to-gif](https://user-images.githubusercontent.com/41550958/111066073-d14f7000-84c5-11eb-8987-f159d7e8566f.gif)


![image](https://user-images.githubusercontent.com/41550958/111065894-dbbd3a00-84c4-11eb-9e72-b283000447c3.png)

![image](https://user-images.githubusercontent.com/41550958/111066041-a9f8a300-84c5-11eb-87c4-b4882c493ee9.png)

