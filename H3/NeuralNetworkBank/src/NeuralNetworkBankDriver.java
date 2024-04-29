//Driver program for neural network classifying bank customers by risk level
//Requires NeuralNetworkBank.java in same directory, and training, validation, and test files.

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

//Program tests neural network in a specific application
public class NeuralNetworkBankDriver
{
	static int numberInputs = 5;	//5 inputs credit score, income, age, sex, and marital status
	static int numberOutputs = 1;	//risk level--high, medium, low, undetermined
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
    	
    	//get converted file names
    	String convertedTrain = trainFile + "_converted.txt";
    	String convertedValid = validFile + "_converted.txt";
    	String convertedTest = testFile + "_converted.txt";

    	//convert data
    	convertInputFile(trainFile, convertedTrain, false);
    	convertInputFile(validFile, convertedValid, false);
    	convertInputFile(testFile, convertedTest, true);
    	
        //construct neural network
        NeuralNetworkBank network = new NeuralNetworkBank();

        //load training data
        network.loadTrainingData(convertedTrain, numberInputs, numberOutputs);
        
        //input and output files
        Scanner inFile = new Scanner(new File(trainFile));

        //read number of records
        int numberRecords = inFile.nextInt(); 
        
        //get number of hidden nodes
        numberHidden = (int)(0.666667 * numberInputs) + numberOutputs;
        System.out.println(numberHidden);

        //set parameters of network
        network.setParameters(numberHidden, 15000, 0.3, 2375);  //int numberMiddle (hidden nodes), int numberIterations, double rate, int seed
        
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
    
    //Method converts input file
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
          	//convert input values and print to file      
          	//convert credit score and print to file
         	double credit = inFile.nextDouble();
         	double convertedCredit = convertCredit(credit);
           	outFile.print(convertedCredit + " ");
           	
           	//convert income and print to file
         	double income = inFile.nextDouble();
         	double convertedIncome = convertIncome(income);
           	outFile.print(convertedIncome + " ");
           	
           	//convert age and print to file
         	double age = inFile.nextDouble();
         	double convertedAge = convertAge(age);
           	outFile.print(convertedAge + " ");
           	
           	//convert sex and print to file
         	String sex = inFile.next();
         	double convertedSex = convertSex(sex);
           	outFile.print(convertedSex + " ");
           	
           	//convert marital status and print to file
         	String status = inFile.next();
         	double convertedStatus = convertMaritalStatus(status);
           	outFile.print(convertedStatus + " ");
            
           	if(!isTestFile) {
           		//convert and print risk class value to file
           		String output = inFile.next();                     
           		double convertedClass = convertClass(output);
           		outFile.print(convertedClass + " ");
           	}
           	
           	//print newline
          	outFile.println();
        }

        inFile.close();
        outFile.close();
    }
    /*************************************************************************/
    //Method normalizes credit score
    public static double convertCredit(double value)
    {
    	double[] bounds = findNewRange(500, 900);
        return (value - bounds[0]) / (bounds[1] - bounds[0]);
    } 
    /*************************************************************************/
    //Method normalizes income
    public static double convertIncome(double value)
    {
    	double[] bounds = findNewRange(30, 90);
        return (value - bounds[0]) / (bounds[1] - bounds[0]);
    } 
    /*************************************************************************/
    //Method normalizes age
    public static double convertAge(double value)
    {
    	double[] bounds = findNewRange(30, 80);
        return (value - bounds[0]) / (bounds[1] - bounds[0]);
    } 
    /*************************************************************************/
    //Method normalizes sex
    public static double convertSex(String sex)
    {
    	return "male".equals(sex) ? 0.2 : 0.8;
    } 
    /*************************************************************************/
    //Method normalizes marital status
    public static double convertMaritalStatus(String status)
    {
    	if("single".equals(status)) return 0.2;
    	else if("divorced".equals(status)) return 0.5;
    	else return 0.8;
    } 
    /*************************************************************************/
    //Method normalizes risk class
    public static double convertClass(String risk)
    {
    	if("undetermined".equals(risk)) return 0.2;
    	else if("low".equals(risk)) return 0.4;
    	else if("medium".equals(risk)) return 0.6;
    	else return 0.8;
    } 
    /*************************************************************************/
    //Method denormalizes risk class
    public static String deconvertClass(double value)
    {
    	if(value < 0.3) return "undetermined";
    	else if(value < 0.5) return "low";
    	else if(value < 0.7) return "medium";
    	else return "high";
    } 
    /*************************************************************************/
    //Method finds a wider range for data
    public static double[] findNewRange(double min, double max)
    {
    	double range = max - min;
    	double halfRange = range / 2;
    	double avg = (max + min) / 2;
    	double[] limits = new double[2];
    	limits[0] = avg + (halfRange * 1.25);
    	limits[1] = avg - (halfRange * 1.25);
    	return limits;
    }
}
