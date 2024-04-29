//Driver program for neural network estimating output values for unknown function given inputs
//Requires NeuralNetwork1.java in same directory, and training, validation, and test files.

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

//Program tests neural network in a specific application
public class NeuralNetwork1Driver
{
	static double maxInput;		//largest value in input files
	static double minInput;		//smallest value in input files
	static double newMax;		//maximum boundary to range (larger than maxInput)
	static double newMin;		//minimum boundary to range (smaller than minInput)
	
	static int numberInputs;	//numbers of each type of node
	static int numberOutputs;
	static int numberHidden;
	
    //Main method
    public static void main(String[] args) throws IOException
    {
    	//get input names
    	Scanner keyboard = new Scanner(System.in);
    	System.out.print("Training file name: ");
    	String trainFile = keyboard.next();
    	System.out.print("Validation file name: ");
    	String validFile = keyboard.next();
    	System.out.print("Test file name: ");
    	String testFile = keyboard.next();
    	
    	//get range of data
    	findRange(trainFile, validFile, testFile);
    	
    	//get converted file names
    	String convertedTrain = trainFile + "_converted.txt";
    	String convertedValid = validFile + "_converted.txt";
    	String convertedTest = testFile + "_converted.txt";

    	//convert data
    	convertTrainingFile(trainFile, convertedTrain);
    	convertValidationFile(validFile, convertedValid);
    	convertTestFile(testFile, convertedTest);
    	
        //construct neural network
        NeuralNetwork1 network = new NeuralNetwork1();

        //load training data
        network.loadTrainingData(convertedTrain);
        
        //get number of hidden nodes
        numberHidden = (int)(0.666667 * numberInputs) + numberOutputs;

        //set parameters of network
        network.setParameters(numberHidden, 10000, 0.5, 2375);  //int numberMiddle (hidden nodes), int numberIterations, double rate, int seed

        //train network
        network.train();

        //get output file name
        System.out.print("Output file name: ");
    	String outFile = keyboard.next();
    	
        //test network
        network.testData(convertedTest, outFile);

        //validate network
        network.validate(convertedValid);
        
        //delete temporary files
        File file = new File(convertedTrain); 
        file.delete();
        file = new File(convertedValid); 
        file.delete();
        file = new File(convertedTest); 
        file.delete();
        
        keyboard.close();
    }
    
    //Method calculates a range of data based on max and min values across all 3 input files
    private static void findRange(String trainingFile, String validationFile, String testFile) throws IOException
    {
    	//store max and min values of input
        maxInput = Double.MIN_VALUE;
        minInput = Double.MAX_VALUE;
        
        //training file
        //get file
        Scanner inFile = new Scanner(new File(trainingFile));

        //read number of records
        int numberRecords = inFile.nextInt();    
        
        //read number of inputs
        int numberInputs = inFile.nextInt();    
        
        //read number of outputs
        int numberOutputs = inFile.nextInt();    
        
        //for each record
        for (int i = 0; i < numberRecords; i++)    
        {                         
        	//for each value, check if it's the max or min of range
        	for (int j = 0; j < numberInputs + numberOutputs; j++)    
            { 
        		double input = inFile.nextDouble();       
        		
        		//update max or min value based on new data
        		if(input > maxInput) maxInput = input;
        		if(input < minInput) minInput = input;
            }
        }
        
        //validation file
        //get file
        inFile = new Scanner(new File(validationFile));

        //read number of records
        numberRecords = inFile.nextInt();       
        
        //for each record
        for (int i = 0; i < numberRecords; i++)    
        {                         
        	//for each value, check if it's the max or min of range
        	for (int j = 0; j < numberInputs + numberOutputs; j++)    
            { 
        		double input = inFile.nextDouble(); 
        		
        		//update max or min value based on new data
        		if(input > maxInput) maxInput = input;
        		if(input < minInput) minInput = input;
            }
        }
        
        //test file
        //get file
        inFile = new Scanner(new File(testFile));

        //read number of records
        numberRecords = inFile.nextInt();      
        
        //for each record
        for (int i = 0; i < numberRecords; i++)    
        {                         
        	//for each input
        	for (int j = 0; j < numberInputs; j++)    
            { 
        		double input = inFile.nextDouble();
        		
        		//update max or min value based on new data
        		if(input > maxInput) maxInput = input;
        		if(input < minInput) minInput = input;
            }
        	// no output
        }

        inFile.close();
        
        //add padding to range
        double range = maxInput - minInput;
        double halfRange = range / 2;
        double avg = (maxInput + minInput) / 2;
        newMax = avg - (halfRange * 1.25);
        newMin = avg + (halfRange * 1.25);
    }
        
    //Method converts training file
    private static void convertTrainingFile(String inputFile, String outputFile) throws IOException
    {
    	//input and output files
        Scanner inFile = new Scanner(new File(inputFile));
        PrintWriter outFile = new PrintWriter(new FileWriter(outputFile));

        //read number of records
        int numberRecords = inFile.nextInt();    

        //write number of records
        outFile.println(numberRecords);
        
        //read number of inputs
        numberInputs = inFile.nextInt();    

        //write number of inputs
        outFile.println(numberInputs);
        
        //read number of outputs
        numberOutputs = inFile.nextInt();    

        //write number of outputs
        outFile.println(numberOutputs);
            
        //for each record
        for (int i = 0; i < numberRecords; i++)    
        {                         
          	//for each input
          	for (int j = 0; j < numberInputs; j++)    //convert input values and print to file
            { 
            		
         		double input = inFile.nextDouble();                     
           		double convertedNumber = convert(input);
           		outFile.print(convertedNumber + " ");
            }
          	for (int j = 0; j < numberOutputs; j++)    //convert and print output values to file
            {
          		double output = inFile.nextDouble();                     
           		double convertedNumber = convert(output);
           		outFile.print(convertedNumber + " ");
            }
          	outFile.println();
        }

        inFile.close();
        outFile.close();
    }
    
    //Method converts validation file
    private static void convertValidationFile(String inputFile, String outputFile) throws IOException
    {
    	//input and output files
        Scanner inFile = new Scanner(new File(inputFile));
        PrintWriter outFile = new PrintWriter(new FileWriter(outputFile));

        //read number of records
        int numberRecords = inFile.nextInt();    

        //write number of records
        outFile.println(numberRecords);
        
        //for each record
        for (int i = 0; i < numberRecords; i++)    
        {                         
          	//for each input
          	for (int j = 0; j < numberInputs; j++)    //convert input values and print to file
            { 
            		
         		double input = inFile.nextDouble();                     
           		double convertedNumber = convert(input);
           		outFile.print(convertedNumber + " ");
            }
          	for (int j = 0; j < numberOutputs; j++)    //convert output and print to file
            {
          		double output = inFile.nextDouble();                     
           		double convertedNumber = convert(output);
           		outFile.print(convertedNumber + " ");
            }
          	outFile.println();
        }

        inFile.close();
        outFile.close();
    }
    
    //Method converts test file
    private static void convertTestFile(String inputFile, String outputFile) throws IOException
    {
    	//input and output files
        Scanner inFile = new Scanner(new File(inputFile));
        PrintWriter outFile = new PrintWriter(new FileWriter(outputFile));

        //read number of records
        int numberRecords = inFile.nextInt();    

        //write number of records
        outFile.println(numberRecords);
            
        //for each record
        for (int i = 0; i < numberRecords; i++)    
        {                         
          	//for each input
          	for (int j = 0; j < numberInputs; j++)    //convert input values and print to file
            { 
            		
         		double input = inFile.nextDouble();                     
           		double convertedNumber = convert(input);
           		outFile.print(convertedNumber + " ");
            }
          	outFile.println();
          	//no output
        }

        inFile.close();
        outFile.close();
    }

    /*************************************************************************/

    //Method normalizes age and income
    private static double convert(double value)
    {
        return (value - newMin) / (newMax - newMin);
    } 
    /*************************************************************************/

    //Method normalizes age and income
    public static double deconvert(double value)
    {
        return value * (newMax - newMin) + newMin;
    } 

}
