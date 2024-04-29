// Computer vs human game
// Requires CompetitiveXOPuzzleDriver.java in same directory as this file
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.Scanner;

//This program plays X vs O game using min-max, depth limit,
//board evaluation, and alpha-beta pruning
public class CompetitiveXOPuzzle
{
    private final char EMPTY = ' ';                //empty slot
    private final char COMPUTER = 'X';             //computer
    private final char PLAYER = '0';               //player
    private final int MIN = 0;                     //min level
    private final int MAX = 1;                     //max level
    private final int LIMIT = 6;                   //depth limit

    //Board class (inner class)
    private class Board
    {
        private char[][] array;                    //board array

        //Constructor of Board class
        private Board(int size)
        {
            array = new char[size][size];          //create array
                                             
            for (int i = 0; i < size; i++)         //fill array with empty slots   
                for (int j = 0; j < size; j++)
                    array[i][j] = EMPTY;
        }
    }

    private Board board;                           //game board
    private int size;                              //size of board
    private int computerXScore = 0;
    private int playerOScore = 0;
    private String outputString = "";
    
    //Constructor
    public CompetitiveXOPuzzle(int size)
    {
        this.board = new Board(size);              //create game board 
        this.size = size;                          //set board size
    }

    //Method plays game
    public void play()
    {
        while (true)                               //computer and player take turns
        {
            board = playerMove(board);             //player makes a move
            
            playerOScore = score(board, PLAYER);			//get current player score  
            
            //display score
            String nextString = "Score:\nPlayer " + playerOScore + ", Computer " + computerXScore + "\n";
            outputString += nextString;
            System.out.print(nextString);

            if (full(board)) {						//end if board is full
            	break;
            }

            board = computerMove(board);           //computer makes a move

            computerXScore = score(board, COMPUTER);		//get current computer score
            
            //display score
            nextString = "Score:\nPlayer " + playerOScore + ", Computer " + computerXScore + "\n";
            outputString += nextString;
            System.out.print(nextString);
            
            if (full(board)) {						//end if board is full
            	break;
            }
        }
        
        // Declare winner
        if(playerOScore > computerXScore) {
        	outputString += "Player wins!\n";
        	System.out.println("Player wins!");
        }
        else if(playerOScore < computerXScore) {
        	outputString += "Computer wins.\n";
        	System.out.println("Computer wins.");
        }
        else {
        	outputString += "Draw\n";
        	System.out.println("Draw");
        }
        
        // Print game history/results to file
        FileWriter fileWriter;
        try {
     	   fileWriter = new FileWriter("xo_game_output.txt");
     	   PrintWriter printWriter = new PrintWriter(fileWriter);
     	   printWriter.write(outputString);
	   			printWriter.close();
		} catch (IOException e) {
				e.printStackTrace();
		}
    }

    //Method lets the player make a move
    private Board playerMove(Board board)
    {
        System.out.print("Make a move: ");         //prompt player
     
        Scanner scanner = new Scanner(System.in);  //read player's move
        int i = scanner.nextInt();
        int j = scanner.nextInt();
        
        String nextString = "Player move: (" + i + ", " + j + ")\n";
        outputString += nextString;
        System.out.print(nextString);

        board.array[i][j] = PLAYER;                //place player symbol

        displayBoard(board);                       //display board

        return board;                              //return updated board
    }

    //Method determines computer's move
    private Board computerMove(Board board)
    {                                              //generate children of board
        LinkedList<Board> children = generate(board, COMPUTER);

        int maxIndex = -1;
        int maxValue = Integer.MIN_VALUE;
                                                   //find the child with
        for (int i = 0; i < children.size(); i++)  //largest minmax value
        {
            int currentValue = minmax(children.get(i), MIN, 1, Integer.MIN_VALUE, Integer.MAX_VALUE);
            if (currentValue > maxValue)
            {
                maxIndex = i;
                maxValue = currentValue;
            }
        }

        Board result = children.get(maxIndex);     //choose the child as next move
        
        //Get coordinates of computer's move
        int xCoord = 0, yCoord = 0;
        boolean found = false;
        for (int i = 0; i < size; i++) {           
            for (int j = 0; j < size; j++) {
            	if(board.array[i][j] != result.array[i][j]) {
            		found = true;
            		xCoord = i;
            		yCoord = j;
            		break;
            	}
            }
        	if (found) break;
    	}
        
        // Display computer's move
        String nextString = "Computer move: (" + xCoord + ", " + yCoord + ")\n";
        outputString += nextString;
        System.out.print(nextString);
                                                  
        displayBoard(result);                      //print next move

        return result;                             //return updated board
    }

    //Method computes minmax value of a board
    private int minmax(Board board, int level, int depth, int alpha, int beta)
    {
        if (full(board) || depth >= LIMIT)		   //if board is terminal or depth limit is reached
            return evaluate(board);                //evaluate board
        else                                       
        {
            if (level == MAX)                      //if board is at max level (as opposed to min)
            {
                 LinkedList<Board> children = generate(board, COMPUTER);
                                                   //generate children of board
                 int maxValue = Integer.MIN_VALUE;

                 for (int i = 0; i < children.size(); i++)
                 {                                 //find minmax values of children
                     int currentValue = minmax(children.get(i), MIN, depth+1, alpha, beta);
                                                   
                     if (currentValue > maxValue)  //find maximum of minmax values
                         maxValue = currentValue;
                                                   
                     if (maxValue >= beta)         //if maximum exceeds beta stop
                         return maxValue;
                                                   
                     if (maxValue > alpha)         //if maximum exceeds alpha update alpha
                         alpha = maxValue;
                 }

                 return maxValue;                  //return maximum value   
            }
            else                                   //if board is at min level (as opposed to max)
            {                     
                 LinkedList<Board> children = generate(board, PLAYER);
                                                   //generate children of board
                 int minValue = Integer.MAX_VALUE;

                 for (int i = 0; i < children.size(); i++)
                 {                                 //find minmax values of children
                     int currentValue = minmax(children.get(i), MAX, depth+1, alpha, beta);
                                     
                     if (currentValue < minValue)  //find minimum of minmax values
                         minValue = currentValue;
                                     
                     if (minValue <= alpha)        //if minimum is less than alpha stop
                         return minValue;
                                     
                     if (minValue < beta)          //if minimum is less than beta update beta
                         beta = minValue;
                 }

                 return minValue;                  //return minimum value 
            }
        }
    }

    //Method generates children of board using a symbol
    private LinkedList<Board> generate(Board board, char symbol)
    {
        LinkedList<Board> children = new LinkedList<Board>();
                                                   //empty list of children
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)         //go thru board
                if (board.array[i][j] == EMPTY)
                {                                  //if slot is empty
                    Board child = copy(board);     //put the symbol and
                    child.array[i][j] = symbol;    //create child board
                    children.addLast(child);
                }

        return children;                           //return list of children
    }

    private int score(Board board, char z) {
    	int numPairs = 0;
    	int numTrips = 0;
    	
    	for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {         //go thru board
            	
            	// If current coordinates is the char
            	if(board.array[i][j] == z) {
            		
            		boolean south, east;       //decide whether current slot has S, E neighbors
    	  		    south = i == size-1 ? false : true;
    	  		    east = j == size-1 ? false : true; 
    	  		    
    	  		    // Increment counters of pairs and triplets
    	  		    if(south && board.array[i+1][j] == z) {
    	  		    	numPairs++;
    	  		    	if(i+1 != size-1 && board.array[i+2][j] == z) numTrips++;
    	  		    }
    	  		    if(east && board.array[i][j+1] == z) {
    	  		    	numPairs++;
    	  		    	if(j+1 != size-1 && board.array[i][j+2] == z) numTrips++;
    	  		    }
            	}
            }
    	}
    	return 2 * numPairs + 3 * numTrips;		// Evaluate score based on number of pairs and triplets
    }
    
    //Method returns current score differential
    private int evaluate(Board board) {
    	int playerScore = score(board, PLAYER);
    	int computerScore = score(board, COMPUTER);
    	return computerScore - playerScore;
    }
    
  //Method checks whether a board is full
    private boolean full(Board board)
    {
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                if (board.array[i][j] == EMPTY)
                   return false;

        return true;
    }

    //Method makes copy of a board
    private Board copy(Board board)
    {
        Board result = new Board(size);      

        for (int i = 0; i < size; i++)       
            for (int j = 0; j < size; j++)
                result.array[i][j] = board.array[i][j];

        return result;                       
    }

    //Method displays board
    private void displayBoard(Board board)
    {
    	for(int j = 0; j < size; j++) {	// formatting
    		outputString += "  - ";
    		System.out.print("  - ");
    	}
    	outputString += "\n";
		System.out.println();
        for (int i = 0; i < size; i++) //print each element of board
        {
            outputString += "| ";
    		System.out.print("| ");
            for (int j = 0; j < size; j++) {
                outputString += board.array[i][j] + " | ";
          		System.out.print(board.array[i][j] + " | ");
            }
            outputString += "\n";
            System.out.println();
            for(int j = 0; j < size; j++) {
            	outputString += "  - ";
        		System.out.print("  - ");
            }
            outputString += "\n";
            System.out.println();
        }   
        outputString += "\n";
        System.out.println();     
    }
}
