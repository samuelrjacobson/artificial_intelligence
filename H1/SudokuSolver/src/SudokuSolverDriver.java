// Driver program for Sudoku solver using constraint satisfaction algorithm
// Requires SudokuSolver.java and an input file in same directory as this file
import java.util.*;
import java.io.*;

public class SudokuSolverDriver {
  //main method to receive input file and call SudokuSolver
  public static void main(String[] args) {   
	  // print instructions
	  System.out.println("Input file of this format:");
	  System.out.println("Size (one number)\n");
	  System.out.println("Initial board\n");
	  System.out.println("Enter a file name: ");

	  // read input file name
	  Scanner keyboard = new Scanner(System.in);
	  String inputFile = keyboard.next();
	  
	  // read puzzle data from file
      try {
      	FileInputStream fiStream = new FileInputStream(inputFile);
      	Scanner fileReader = new Scanner(fiStream);
		
      	while (fileReader.hasNext()) {
            		
    		// copy initial board to 2D array
    		int size = fileReader.nextInt();
    		String[][] initial = new String[size][size];
    		for(int i = 0; i < size; i++) {
    			for(int j = 0; j < size; j++) {
        			initial[i][j] = fileReader.next();
        		}
    		}
  
      		// solve Sudoku puzzle
            SudokuSolver s = new SudokuSolver(initial, size);
            s.solve(inputFile);
            
      	}
      	fileReader.close();
  	}
  	catch (FileNotFoundException e) {
  		e.printStackTrace();
  	}     
  }
}

