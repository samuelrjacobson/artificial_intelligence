//Driver program for genetic algorithm choosing which products to buy (much like knapsack problem)
//Requires GeneticAlgoInvesting.java in same directory, and an input file containing products with buying and selling prices.

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class GeneticAlgoInvestingDriver
{
	//Main method
	public static void main(String[] args) throws IOException
	{
		//get input file name and open
   		Scanner keyboard = new Scanner(System.in);
   		System.out.print("Input file name: ");
   		String inputFile = keyboard.next();
   		Scanner inFile = new Scanner(new File(inputFile));

        //read number of products
        int numberItems = inFile.nextInt();
        
        //read budget
        int budget = inFile.nextInt();

		int[][] table = new int[numberItems][3];

        //for each item
        for (int i = 0; i < numberItems; i++)
        {
        	//record product label
        	table[i][0] = inFile.nextInt();
        	
            //read buying price
        	table[i][1] = inFile.nextInt();
        	
        	//read target price
        	table[i][2] = inFile.nextInt();
        	
        	//calculate profit
        	table[i][2] -= table[i][1];
        }

   		//create solver
   		GeneticAlgoInvesting k = new GeneticAlgoInvesting(table, numberItems, budget);

   		//set parameters of genetic algorithm
   		//(int populationSize, int stringLength, int numberIterations, double crossoverRate, double mutationRate, int seed)
   		k.setParameters(200, numberItems, 1000, 0.5, 0.1, 4329);

   		//find near optimal solution
   		k.solve();
   		
   		keyboard.close();
  }
}