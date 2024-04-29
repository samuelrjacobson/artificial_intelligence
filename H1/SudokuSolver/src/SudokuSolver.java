// Sudoku solver using constraint satisfaction algorithm
// Requires SudokuSolverDriver.java and an input file in same directory as this file
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class SudokuSolver {

	private String outputString = "";	// will print to console and write to file
	private String[][] board;	// puzzle board
	private int n;	// board will be n*n size, with n*n regions of n x n size each.
	
	//Constructor of Sudoku class
    public SudokuSolver(String[][] board, int size)
    {
    	this.board = board;                   //set initial board
    	this.n = (int)Math.sqrt(size);
    }

    // called by driver program
    public void solve(String fileName) {
    	display();							// display initial board
    	
        if (fill(0)) {                      //fill the board starting at the beginning
           display();                         //if success display board
           
			// open file for writing
			FileWriter fileWriter;
			try {
				fileWriter = new FileWriter(fileName + "_output.txt");	//write outputString to file
				PrintWriter printWriter = new PrintWriter(fileWriter);
				printWriter.write(outputString);
				printWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
        else
           System.out.println("No solution"); //otherwise failure    
    }

    //Method fills a board using recursion/backtracking. 
    //It fills the board starting at a given location
    private boolean fill(int location)
    {
        int x = location / (n*n);             //find x,y coordinates of
        int y = location % (n*n);              //current location
        int value;						// try values from 1 to n^2
        
        if (location > Math.pow(n, 4) - 1)     //if location exceeds board
            return true;                 	//whole board is filled

        else if (!("w".equals(board[x][y]) || "e".equals(board[x][y]) || "o".equals(board[x][y])))    //if location already has value
            return fill(location+1);     	//fill the rest of board
       
        else if ("e".equals(board[x][y])){         	//if e, try even values at location till one works
            for (value = 2; value <= n*n; value += 2)
            {
            	board[x][y] = "" + value;     //try numbers 1 to n^2 at the location            	

                if (check(x, y) && fill(location+1))
                    return true;         //if number causes no conflicts and the rest
            }                            //of board can be filled then done

            board[x][y] = "e";             //if none of numbers 1 to n^2 work then
            return false;                //empty the location and backtrack
        }
        else if ("o".equals(board[x][y])){         	//if o, try odd values at location till one works
            for (value = 1; value <= n*n; value += 2)
            {
            	board[x][y] = "" + value;     //try numbers 1 to n^2 at the location            	

                if (check(x, y) && fill(location+1))
                    return true;         //if number causes no conflicts and the rest
            }                            //of board can be filled then done

            board[x][y] = "o";             //if none of numbers 1 to n^2 work then
            return false;                //empty the location and backtrack
        }
        else {								//if w, try values at location till one works
            for (value = 1; value <= n*n; value++)
            {
            	board[x][y] = "" + value;     //try numbers 1 to n^2 at the location

                if (check(x, y) && fill(location+1))
                    return true;         //if number causes no conflicts and the rest
            }                            //of board can be filled then done

            board[x][y] = "w";             //if none of numbers 1 to n^2 work then
            return false;                //empty the location and backtrack
        }
    }
    
    //Method checks whether a value at a given location causes any conflicts
    private boolean check(int x, int y) {
        int a, b, i, j;
        
        for (j = 0; j < n*n; j++)         //check value causes conflict in row
            if (j != y && board[x][j].equals(board[x][y]))	// if numbers aren't distinct
                return false;   
                
        for (i = 0; i < n*n; i++)         //check value causes conflict in column
            if (i != x && board[i][y].equals(board[x][y]))
                return false;
            
        a = (x/n)*n; b = (y/n)*n;       //check value causes conflict in
        for (i = 0; i < n; i++)         //n x n region
             for (j = 0; j < n; j++)
                 if ((a + i != x) && (b + j != y) && board[a+i][b+j].equals(board[x][y]))
                     return false;
                     
        return true;
    }

    //Method displays a board
    private void display() {
        for (int i = 0; i < n*n; i++)
        {
            for (int j = 0; j < n*n; j++) {
            	if(board[i][j].length() == 1) { 	// if value is single digit/letter, add space
            		outputString += " ";
            		System.out.printf(" ");
            	}
               	outputString += board[i][j] + " |";		// display value at location
                System.out.print(board[i][j] + " |");	// separate columns with |
            }
            outputString += "\n";
            System.out.println();

            for (int j = 0; j < n*n; j++) {
            	outputString += " -  ";			// separate rows with -
                System.out.print(" -  ");	
        	}
        	outputString += "\n";
            System.out.println();
        }
    	outputString += "\n";
    	outputString += "\n";

        System.out.println();
        System.out.println();
    }
}
