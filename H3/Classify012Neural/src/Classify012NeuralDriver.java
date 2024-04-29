//Driver program for neural network classifying binary images as 0, 1, or 2
//Requires Classify012Neural.java in same directory, and training, validation, and test files.

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

//Program tests neural network
public class Classify012NeuralDriver
{
	//numbers of each type of node
	static int numberInputs = 256;		//each record is 16x16 binary image,
	static int numberOutputs = 1;		//classified as zero, one, or two.
	static int numberHidden;
	
    //Main method
    public static void main(String[] args) throws IOException
    {
    	//get input file names
    	Scanner keyboard = new Scanner(System.in);
    	System.out.print("Training file name: ");
    	String trainFile = keyboard.next();
    	System.out.print("Validation file name: ");
    	String validFile = keyboard.next();
    	System.out.print("Test file name: ");
    	String testFile = keyboard.next();
    	
    	//compute converted file names
    	String convertedTrain = trainFile + "_converted.txt";
    	String convertedValid = validFile + "_converted.txt";
    	String convertedTest = testFile + "_converted.txt";

    	//convert data
    	convertInputFile(trainFile, convertedTrain, false);
    	convertInputFile(validFile, convertedValid, false);
    	convertInputFile(testFile, convertedTest, true);
    	
        //construct neural network
    	Classify012Neural network = new Classify012Neural();

        //load training data
        network.loadTrainingData(convertedTrain, numberInputs, numberOutputs);
        
        //get number of hidden nodes
        numberHidden = (int)(0.666667 * numberInputs) + numberOutputs;
        System.out.println(numberHidden);
        
        //set parameters of network
        network.setParameters(numberHidden, 1000, 0.5, 2375);  //int numberMiddle (hidden nodes), int numberIterations, double rate, int seed
        
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
    
    //Method converts input file to normalized values
    private static void convertInputFile(String inputFile, String outputFile, boolean isTestFile) throws IOException
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
          	//convert class names and print to file, unless this is the test file
           	if(!isTestFile) {
           		String output = inFile.next();                     
           		double convertedClass = convertClass(output);
           		outFile.print(convertedClass + " ");
           	}
           	
           	//copy input values to file
           	for(int j = 0; j < numberInputs; j++) {
           		double input = inFile.nextDouble();                     
           		double convertedVal = convertIndex(input);
           		outFile.print(convertedVal + " ");
           	}
           	
           	//print newline
          	outFile.println();
        }

        inFile.close();
        outFile.close();
    }
    /*************************************************************************/
    //Method normalizes binary bit
    public static double convertIndex(double value)
    {
    	if(value == 0) return 0.2;
    	else return 0.8;
    } 
    /*************************************************************************/
    //Method normalizes number class
    public static double convertClass(String num)
    {
    	if("zero".equals(num)) return 0.2;
    	else if("one".equals(num)) return 0.5;
    	else return 0.8;
    } 
    /*************************************************************************/
    //Method denormalizes number class
    public static String deconvertClass(double value)
    {
    	if(value < 0.35) return "zero";
    	else if(value < 0.65) return "one";
    	else return "two";
    } 
    /*************************************************************************/
}
