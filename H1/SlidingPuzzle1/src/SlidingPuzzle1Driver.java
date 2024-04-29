// Driver program for sliding puzzle. Computer transforms board from initial to goal state using A* algorithm.
// Requires SlidingPuzzle1.java and an input file in same directory as this file
import java.io.*;
import java.util.*;

//Tester program for sliding board puzzle solver with A* search
public class SlidingPuzzle1Driver {
  //main method for testing
  public static void main(String[] args)
  {   
	  // Print instructions
	  System.out.println("Input file of this format:");
	  System.out.println("Size (single-digit)\n");
	  System.out.println("Initial board\n");
	  System.out.println("Goal board\n");
	  System.out.println("Evaluation function (1=heuristic only, 2=path cost, 3=both\n");
	  System.out.println("Heuristic (1=mismatches, 2=taxi distances)\n");
	  System.out.println("Enter a file name: ");

	  // Get file name
	  Scanner keyboard = new Scanner(System.in);
	  String inputFile = keyboard.next();
	  
	  //read data from file
      try {
      	FileInputStream fiStream = new FileInputStream(inputFile);
      	Scanner fileReader = new Scanner(fiStream);
		
      	while (fileReader.hasNext()) {
      		
      		// Copy initial board to 2D array
      		int size = fileReader.nextInt();
      		int[][] initial = new int[size][size];
      		int[][] goal = new int[size][size];
      		for(int i = 0; i < size; i++) {
      			for(int j = 0; j < size; j++) {
          			initial[i][j] = fileReader.nextInt();
          		}
      		}
      		
      		// Copy goal board to 2D array
      		for(int i = 0; i < size; i++) {
      			for(int j = 0; j < size; j++) {
          			goal[i][j] = fileReader.nextInt();
          		}
      		}
      		
      		// Get evaluation and heuristic choices
      		int evalType = fileReader.nextInt();
      		int heuristicType = fileReader.nextInt();
      		
      		if(evalType > 3 || heuristicType > 2) System.out.println("Invalid eval/heuristic choice");
      		else {
	      		//solve sliding puzzle
	            SlidingPuzzle1 s = new SlidingPuzzle1(initial, goal, size, evalType, heuristicType);
	            s.solve(inputFile);
      		}
            
      	}
      	fileReader.close();
  	}
  	catch (FileNotFoundException e) {
  		e.printStackTrace();
  	}     
  }
}

