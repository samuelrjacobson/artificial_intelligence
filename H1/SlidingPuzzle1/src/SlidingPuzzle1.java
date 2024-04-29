// Sliding puzzle. Computer transforms board from initial to goal state using A* algorithm.
// Requires SlidingPuzzle1Driver.java and an input file in same directory as this file
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;

//This program solves sliding puzzle using A* algorithm
public class SlidingPuzzle1 {

	//Board class (inner class)
  private class Board
  {
      private int[][] array;                 //board array
      private int gvalue;                     //path cost
      private int hvalue;                     //heuristic value
      private int fvalue;                     //gvalue plus hvalue
      private Board parent;                   //parent board

      //Constructor of board class
      private Board(int[][] initial, int size)
      {
          this.array = new int[size][size];  //create board array

          for (int i = 0; i < size; i++)      //copy given array
              for (int j = 0; j < size; j++)
                  this.array[i][j] = initial[i][j];

          this.gvalue = 0;                    //path cost, heuristic value,
          this.hvalue = 0;                    //fvalue are all 0
          this.fvalue = 0;

          this.parent = null;                 //no parent
      }
  }

  private Board initial;                         //initial board
  private Board goal;                            //goal board
  private int size;                              //board size
  private int evalType;
  private int heuristicType;
  private String outputString = "";

  //Constructor of SlidingPuzzle1 class
  public SlidingPuzzle1(int[][] initial, int[][] goal, int size, int evalType, int heuristicType)
  {
      this.size = size;                          //set size of board
      this.initial = new Board(initial, size);   //create initial board
      this.goal = new Board(goal, size);         //create goal board
      this.evalType = evalType;
      this.heuristicType = heuristicType;
  }

  //Method solves sliding puzzle
  public void solve(String fileName)
  {
       LinkedList<Board> openList = new LinkedList<Board>();  //open list
       LinkedList<Board> closedList = new LinkedList<Board>();//closed list

       openList.addFirst(initial);   //add initial board to open list     

       long startTime = System.currentTimeMillis();	// start timer
	    
       while (!openList.isEmpty())   //while open list has more boards
       {
           int best = selectBest(openList);       //select best board

           Board board = openList.remove(best);   //remove board
                                                  
           closedList.addLast(board);             //add board to closed list

           if (goal(board))                       //if board is goal
           {
        	   long endTime = System.currentTimeMillis();	// stop timer
               long timeElapsed = (endTime - startTime);
               outputString += "Time elapsed: " + timeElapsed + " milliseconds\n";
               outputString += "Number of boards searched: " + closedList.size() + "\n";
               
               displayPath(board);                //display path to goal
               
               // Print results
               System.out.println(outputString);
               
	           // open file for writing
	           FileWriter fileWriter;
	           try {
	        	   fileWriter = new FileWriter(fileName + "_" + evalType + "_" + heuristicType + "_output.txt");
	        	   PrintWriter printWriter = new PrintWriter(fileWriter);
	        	   printWriter.write(outputString);
		   			printWriter.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
	   		   
               return;                            //stop search
           }
           else                                   //if board is not goal
           {
               LinkedList<Board> children = generate(board);//create children

               for (int i = 0; i < children.size(); i++)
               {                                     //for each child
                   Board child = children.get(i);    
                                                     
                   if (!exists(child, closedList))   //if child is not in closed list
                   {
                        if (!exists(child, openList))//if child is not in open list
                            openList.addLast(child); //add to open list
                        else                          
                        {                            //if child is already in open list
                            int index = find(child, openList);
                            if (child.fvalue < openList.get(index).fvalue)
                            {                            //if fvalue of new copy
                                openList.remove(index);  //is less than old copy
                                openList.addLast(child); //replace old copy
                            }                            //with new copy
                        }                               
                   }     
               }                                  
           }                                       
       }

       System.out.println("no solution");            //no solution if there are
  }                                                  //no boards in open list

  //Method creates children of a board
  private LinkedList<Board> generate(Board board)
  {
      int i = 0, j = 0;
      boolean found = false;

      for (i = 0; i < size; i++)              //find location of empty slot
      {                                       //of board
          for (j = 0; j < size; j++)
              if (board.array[i][j] == 0)
              {   
                  found = true;
                  break;
              }
          
          if (found)
             break;
      }

      boolean north, south, east, west;       //decide whether empty slot
      north = i == 0 ? false : true;          //has N, S, E, W neighbors
      south = i == size-1 ? false : true;
      east = j == size-1 ? false : true; 
      west = j == 0 ? false : true;

      LinkedList<Board> children = new LinkedList<Board>();//list of children

      if (north) children.addLast(createChild(board, i, j, 'N')); //add N, S, E, W
      if (south) children.addLast(createChild(board, i, j, 'S')); //children if
      if (east) children.addLast(createChild(board, i, j, 'E'));  //they exist
      if (west) children.addLast(createChild(board, i, j, 'W'));  
                                                                  
      return children;                        //return children      
  }

  //Method creates a child of a board by swapping empty slot in a 
  //given direction
  private Board createChild(Board board, int i, int j, char direction)
  {
      Board child = copy(board);                   //create copy of board

      if (direction == 'N')                        //swap empty slot to north
      {
          child.array[i][j] = child.array[i-1][j];
          child.array[i-1][j] = 0;
      }
      else if (direction == 'S')                   //swap empty slot to south
      {
          child.array[i][j] = child.array[i+1][j];
          child.array[i+1][j] = 0;
      }
      else if (direction == 'E')                   //swap empty slot to east
      {
          child.array[i][j] = child.array[i][j+1];
          child.array[i][j+1] = 0;
      }
      else                                         //swap empty slot to west
      {
          child.array[i][j] = child.array[i][j-1];
          child.array[i][j-1] = 0;
      }

      child.gvalue = board.gvalue + 1;             //parent path cost plus one

      if(heuristicType == 1) child.hvalue = heuristic_M(child);           //heuristic value of child
      else if (heuristicType == 2) child.hvalue = heuristic_D(child);

      if(evalType == 1) child.fvalue = child.hvalue;
      else if(evalType == 2) child.fvalue = child.gvalue;
      else if(evalType == 3) child.fvalue = child.gvalue + child.hvalue;  //gvalue plus hvalue

      child.parent = board;                        //assign parent to child

      return child;                                //return child
  }

  //Method computes heuristic value of board based on misplaced values
  private int heuristic_M(Board board)
  {
      int value = 0;                               //initial heuristic value

      for (int i = 0; i < size; i++)               //go thru board and
          for (int j = 0; j < size; j++)           //count misplaced values
              if (board.array[i][j] != goal.array[i][j])
                 value += 1;                       

      return value;                                //return heuristic value
  }

  //Method computes heuristic value of board
  //Heuristic value is the sum of taxi distances of misplaced values
  private int heuristic_D(Board board)
  {
      //initial heuristic value
      int value = 0;

      //go thru board
      for (int i = 0; i < size; i++)
          for (int j = 0; j < size; j++)
              //if value mismatches in goal board    
              if (board.array[i][j] != goal.array[i][j]) 
              {                                
                  //locate value in goal board
                  int x = 0, y = 0;         
                  boolean found = false;
                  for (x = 0; x < size; x++)
                  {                       
                      for (y = 0; y < size; y++)
                          if (goal.array[x][y] == board.array[i][j])
                          {                 
                              found = true; 
                              break;
                          } 
                      if (found)
                         break;                        
                  }
          
                  //find city distance between two locations
                  value += (int)Math.abs(x-i) + (int)Math.abs(y-j);
              }
                   
      //return heuristic value               
      return value;
  }

  //Method locates the board with minimum fvalue in a list of boards
  private int selectBest(LinkedList<Board> list)
  {
      int minValue = list.get(0).fvalue;           //initialize minimum
      int minIndex = 0;                            //value and location

      for (int i = 0; i < list.size(); i++)
      {
          int value = list.get(i).fvalue;
          if (value < minValue)                    //updates minimums if
          {                                        //board with smaller
              minValue = value;                    //fvalue is found
              minIndex  = i;
          } 
      }

      return minIndex;                             //return minimum location
  }   

  //Method creates copy of a board
  private Board copy(Board board)
  {
       return new Board(board.array, size);
  }

  //Method decides whether a board is goal
  private boolean goal(Board board)
  {
      return identical(board, goal);           //compare board with goal
  }                                             

  //Method decides whether a board exists in a list
  private boolean exists(Board board, LinkedList<Board> list)
  {
      for (int i = 0; i < list.size(); i++)    //compare board with each
          if (identical(board, list.get(i)))   //element of list
             return true;

      return false;
  }

  //Method finds location of a board in a list
  private int find(Board board, LinkedList<Board> list)
  {
      for (int i = 0; i < list.size(); i++)    //compare board with each
          if (identical(board, list.get(i)))   //element of list
             return i;

      return -1;
  }
  
  //Method decides whether two boards are identical
  private boolean identical(Board p, Board q)
  {
      for (int i = 0; i < size; i++)
          for (int j = 0; j < size; j++)
              if (p.array[i][j] != q.array[i][j])
                  return false;      //if there is a mismatch then false

      return true;                   //otherwise true
  }

  //Method displays path from initial to current board
  private void displayPath(Board board)
  {
      LinkedList<Board> list = new LinkedList<Board>();

      Board pointer = board;         //start at current board

      while (pointer != null)        //go back towards initial board
      {
          list.addFirst(pointer);    //add boards to beginning of list

          pointer = pointer.parent;  //keep going back
      }
      outputString += "Number of moves: " + list.size() + "\n";
                                     //print boards in list
      for (int i = 0; i < list.size(); i++)  
          displayBoard(list.get(i));
  }

  //Method displays board
  private void displayBoard(Board board)
  {
	  for(int j = 0; j < size; j++)	// formatting
    	  outputString += "  - ";
      outputString += "\n";
      for (int i = 0; i < size; i++) //print each element of board
      {
          outputString += "| ";
          for (int j = 0; j < size; j++)
              outputString += board.array[i][j] + " | ";
          outputString += "\n";
          for(int j = 0; j < size; j++)
        	  outputString += "  - ";
          outputString += "\n";
      }   
      outputString += "\n";
  }
}
