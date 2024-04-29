//Driver program for genetic algorithm choosing which products to buy (much like knapsack problem)
//Requires GeneticAlgoInvestingDriver.java in same directory, and an input file containing products with buying and selling prices.

import java.io.*;
import java.util.*;

/*****************************************************************************/

//This programs solves investing problem using genetic algorithm 
public class GeneticAlgoInvesting
{
    private int[][] table;                //buyingPrices and profits of items
    private int budget;            //maximum budget

    private int populationSize;           //population size
    private int stringLength;             //string length
    private int numberIterations;         //number of iterations
    private double crossoverRate;         //crossover rate
    private double mutationRate;          //mutation rate
    private Random random;                //random number generator

    private int[][] population;           //population of strings
    private double[] fitnessprofits;       //fitness profits of strings              

    /*************************************************************************/

    //Constructor
    public GeneticAlgoInvesting(int[][] table, int numberItems, int budget) 
    {
        //set data
        this.table = table;
        this.budget = budget;
    }

    /*************************************************************************/

    //Method sets parameters of genetic algorithm
    public void setParameters(int populationSize, int stringLength,
    int numberIterations, double crossoverRate, double mutationRate, int seed)
    {
        //set genetic algorithm parameters
        this.populationSize = populationSize;
        this.stringLength = stringLength;
        this.numberIterations = numberIterations;
        this.crossoverRate = crossoverRate;
        this.mutationRate = mutationRate;
        this.random = new Random(seed);

        //create population and fitness arrays
        this.population = new int[populationSize][stringLength];
        this.fitnessprofits = new double[populationSize];
    }

    /*************************************************************************/

    //Method executes genetic algorithm
    public void solve()
    {
         //initialize population
         initialize();

         //create generations
         for (int i = 0; i < numberIterations; i++)
         {
             //crossover strings
             crossover(); 

             //mutate strings
             mutate();

             //reproduce strings
             reproduce();
         }

         //choose best string
         solution();
    }

    /*************************************************************************/

    //Method initializes population
    private void initialize()
    {
        //initialize strings with random 0/1
        for (int i = 0; i < populationSize; i++)
            for (int j = 0; j < stringLength; j++)
                population[i][j] = random.nextInt(2);

        //initial fitness profits are zero
        for (int i = 0; i < populationSize; i++)
            fitnessprofits[i] = 0;
    }

    /*************************************************************************/

    //Method performs crossover operation
    private void crossover()
    {
        //go thru each string
        for (int i = 0; i < populationSize; i++)
        {
             //if crossover is performed
             if (random.nextDouble() < crossoverRate)
             {
                  //choose another string
                  int j = random.nextInt(populationSize);

                  //choose crossover point
                  int cut = random.nextInt(stringLength);

                  //crossover bits of the two strings
                  for (int k = cut; k < stringLength; k++)
                  {
                       int temp = population[i][k];
                       population[i][k] = population[j][k];
                       population[j][k] = temp;
                  }
             }
        }
    }

    /*************************************************************************/

    //Method performs mutation operation
    private void mutate()
    {
        //go thru each bit of each string
        for (int i = 0; i < populationSize; i++)
            for (int j = 0; j < stringLength; j++)
            {
                //if mutation is performed
                if (random.nextDouble() < mutationRate)
                {
                    //flip bit from 0 to 1 or 1 to 0
                    if (population[i][j] == 0)
                        population[i][j] = 1;
                    else
                        population[i][j] = 0;
                }
            }    
    }

    /*************************************************************************/

    //Method performs reproduction operation
    private void reproduce()
    {
        //find fitness profits of all strings
        computeFitnessprofits();

        //create array for next generation
        int[][] nextGeneration = new int[populationSize][stringLength];

        //repeat population number of times
        for (int i = 0; i < populationSize; i++)
        {
            //select a string from current generation based on fitness
            int j = select();

            //copy selected string to next generation
            for (int k = 0; k < stringLength; k++)
                nextGeneration[i][k] = population[j][k];
        }

        //next generation becomes current generation
        for (int i = 0; i < populationSize; i++)
            for (int j = 0; j < stringLength; j++)
                population[i][j] = nextGeneration[i][j];
    }

    /*************************************************************************/

    //Method computes fitness profits of all strings
    private void computeFitnessprofits()
    {
        //compute fitness profits of strings
        for (int i = 0; i < populationSize; i++)
            fitnessprofits[i] = fitness(population[i]);

        //accumulate fitness profits
        for (int i = 1; i < populationSize; i++)
            fitnessprofits[i] = fitnessprofits[i] + fitnessprofits[i-1];

         //normalize accumulated fitness profits
         for (int i = 0; i < populationSize; i++)
            fitnessprofits[i] = fitnessprofits[i]/fitnessprofits[populationSize-1];            
    }

    /*************************************************************************/

    //Method selects a string based on fitness profits
    private int select()
    {
        //create a random number between 0 and 1
        double profit = random.nextDouble();

        //determine on which normalized accumulated fitness profit it falls
        int i;
        for (i = 0; i < populationSize; i++)
            if (profit <= fitnessprofits[i])
                break;

        //return the string index where the random number fell
        return i;
    }

    /*************************************************************************/

    //Method finds the best solution
    private void solution()
    {
        //compute fitness profits of strings
        for (int i = 0; i < populationSize; i++)
            fitnessprofits[i] = fitness(population[i]);

        //find the string with best fitness profit
        int best = 0;
        for (int i = 0; i < populationSize; i++)
            if (fitnessprofits[i] > fitnessprofits[best])
                best = i;

        //display the best string information
        try {
        	display(population[best]);
        }
        catch(IOException e) {
        	e.printStackTrace();
        }
    }

    /*************************************************************************/

    //Method computes fitness profit of a string
    private double fitness(int[] string)
    {
        //initialize buyingPrice and profit
        int buyingPrice = 0;
        int profit = 0;

        //go thru string
        for (int i = 0; i < stringLength; i++)
            //add up profits and buyingPrices of selected items
            if (string[i] == 1)
            {
                buyingPrice += table[i][1];
                profit += table[i][2];  
            }

        //if total buyingPrice does not exceed budget, fitness is total profit
        if (buyingPrice <= budget)
            return profit;
        //if total buyingPrice exceeds budget, fitness is zero
        else
            return 0;   
    }

    /*************************************************************************/

    //Method displays buyingPrice and profit information about a string
    private void display(int[] string) throws IOException
    {
    	//open output file for writing
    	Scanner keyboard = new Scanner(System.in);
    	System.out.print("Enter an output file name: ");
    	String outputFile = keyboard.next();
        PrintWriter fileWriter = new PrintWriter(new FileWriter(outputFile));

        int totalBuyingPrice = 0;
        int totalProfit = 0;
        
        //go thru string
        for (int i = 0; i < stringLength; i++) {
            //if item is selected print its buyingPrice and profit
            if (string[i] == 1) {
            	//print product number
            	fileWriter.println(table[i][0]);
            	
            	//sum up price and profit
            	totalBuyingPrice += table[i][1];
            	totalProfit += table[i][2];
            }
        }
        //print total cost and profit
    	fileWriter.println("Total buying price: " + totalBuyingPrice);
    	fileWriter.println("Total profit: " + totalProfit);
        
        keyboard.close();
        fileWriter.close();
    }

    /*************************************************************************/
}
