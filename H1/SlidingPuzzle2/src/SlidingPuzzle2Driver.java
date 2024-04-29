// Driver program for sliding puzzle. Computer transforms board from initial to goal state using A* algorithm.
// Requires SlidingPuzzle2.java and an input file in same directory as this file
import java.io.*;
import java.util.*;

//Driver program for sliding board puzzle solver with A* search
public class SlidingPuzzle2Driver {
  //main method for testing
  public static void main(String[] args)
  {   
	  // Print instructions
	  System.out.println("Input file of this format:");
	  System.out.println("Size (single-digit)\n");
	  System.out.println("Initial board\n");
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
      		char[][] initial = new char[size][size];
      		char[][] goal = new char[size][size];
      		for(int i = 0; i < size; i++) {
      			for(int j = 0; j < size; j++) {
          			initial[i][j] = fileReader.next().charAt(0);
          		}
      		}
      		
      		// Copy initial board to 1D array for sorting
      		char[] charArray = new char[size * size];
      		int k = -1;
      		for(int i = 0; i < size; i++) {
      			for(int j = 0; j < size; j++) {
      				k++;
      				charArray[k] = initial[i][j];
      			}
      		}
      		
      		// Sort by ASCII, so numbers in order, then Gs, then Rs
      		Arrays.sort(charArray);
      		
      		// Put Rs before Gs
      		for(int i = 0; i < charArray.length; i++) {
      			for(int j = i + 1; j < charArray.length; j++) {
      				if(charArray[i] == 'G' && charArray[j] == 'R') {
      					
      					// swap R and G
      					char temp = charArray[i];
      					charArray[i] = charArray[j];
      					charArray[j] = temp;
      				}
      			}
      		}
      		
      		// Copy 1D array to goal 2D array
      		k = 0;
      		for(int i = 0; i < size; i++) {
      			for(int j = 0; j < size; j++) {
      				goal[i][j] = charArray[k++];
      			}
      		}
      		
      		//solve sliding puzzle
            SlidingPuzzle2 s = new SlidingPuzzle2(initial, goal, size);
            s.solve(inputFile);
            
      	}
      	fileReader.close();
  	}
  	catch (FileNotFoundException e) {
  		e.printStackTrace();
  	}     
  }
}

