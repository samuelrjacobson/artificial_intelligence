//This program uses the ant colony optimization algorithm to find a viable cycle between cities given the weights of roads connecting them.
//Requires CycleOptimizer.java (algorithm file) and input file with roads' weights as siblings to this file.

import java.util.Scanner;
import java.io.*;

public class CycleOptimizerDriver
{
    /********************************************************************/

    //Main method
	public static void main(String[] args)
    {
		try {
			//get input data
	    	Scanner keyboard = new Scanner(System.in);
	    	System.out.print("Enter an input file name: ");
	    	String inputFile = keyboard.next();
	        
	        //read number of cities and roads
	        Scanner inputReader = new Scanner(new File(inputFile));
	        int numberCities = inputReader.nextInt();    
	        int numberRoads = inputReader.nextInt();    
	        
	        //create adjacency matrix
	        int size = numberCities;
	        int[][] matrix = new int[size][size];

	        //assign weights to matrix
	        for (int i = 0; i < numberRoads; i++)
	        {
	        	int road1 = inputReader.nextInt();
	        	int road2 = inputReader.nextInt();
	        	int weight = inputReader.nextInt();
	        	
	        	matrix[road1 - 1][road2 - 1] = weight;
	        	matrix[road2 - 1][road1 - 1] = weight;
	        }
	            
	        //create solver
	        CycleOptimizer a = new CycleOptimizer(matrix, size);

	        //set parameters
	        //int numberIterations, double chemicalExponent, double distanceExponent, 
	        //double initialDeposit, double depositAmount, double decayRate,  int seed
	        int numberIterations = 150; double chemicalExponent = 1.0; double distanceExponent = 1.0; double initialDeposit = 0.1;
	        double depositAmount = 1.0; double decayRate = 0.5; int seed = 602;
	        a.setParameters(numberIterations, chemicalExponent, distanceExponent, initialDeposit, depositAmount, decayRate, seed);

	        //find optimal solution
	        a.solve();
    	}
    	catch(IOException e) {
    		e.printStackTrace();
    	}
    }
}