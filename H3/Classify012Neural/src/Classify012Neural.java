//Program for neural network classifying binary images as 0, 1, or 2
//Requires Classify012NeuralDriver.java in same directory, and training, validation, and test files.

import java.io.*;
import java.util.*;

//Neural network
public class Classify012Neural
{
    /*************************************************************************/

    //Record class
    private class Record 
    {
        private double[] input;     //inputs of record  
        private double[] output;    //outputs of record        

        //Constructor of Record
        private Record(double[] input, double[] output)
        {
            this.input = input;     //set inputs
            this.output = output;   //set outputs
        }
    }
    
    /*************************************************************************/

    private ArrayList<Record> records;   //list of training records
    private int numberRecords;           //number of training records 
         
    private int numberInputs;            //number of inputs 
    private int numberOutputs;           //number of outputs

    private int numberMiddle;            //number of hidden nodes
    private int numberIterations;        //number of iterations
    private double rate;                 //learning rate

    private double[] input;              //inputs
    private double[] middle;             //outputs at hidden nodes
    private double[] output;             //outputs at output nodes

    private double[] errorMiddle;        //errors at hidden nodes
    private double[] errorOut;           //errors at output nodes
    
    private double[] thetaMiddle;        //thetas at hidden nodes
    private double[] thetaOut;           //theats at output nodes

    private double[][] matrixMiddle;     //weights between input/hidden nodes 
    private double[][] matrixOut;        //weights between hidden/output nodes

    /*************************************************************************/

    //Constructor of neural network
    public Classify012Neural()
    {
        //parameters are zero
        numberRecords = 0;       
        numberInputs = 0;
        numberOutputs = 0;
        numberMiddle = 0;        
        numberIterations = 0;
        rate = 0;

        //arrays are empty
        records = null;          
        input = null;            
        middle = null;
        output = null;
        errorMiddle = null;      
        errorOut = null;
        thetaMiddle = null;      
        thetaOut = null;
        matrixMiddle = null;     
        matrixOut = null;
    }

    /*************************************************************************/

    //Method loads training records from training file
    public void loadTrainingData(String trainingFile, int numIn, int numOut) throws IOException
    {
         Scanner inFile = new Scanner(new File(trainingFile));

         //read number of records
         numberRecords = inFile.nextInt();  
         
         //set numbers of nodes
         numberInputs = numIn;
         numberOutputs = numOut;
        
         //empty list of records
         records = new ArrayList<Record>();        

         //for each training record
         for (int i = 0; i < numberRecords; i++)    
         {
        	 //read outputs
             double[] output = new double[numberOutputs];                                          
             for (int j = 0; j < numberOutputs; j++) 
                 output[j] = inFile.nextDouble();
 
             //read inputs
             double[] input = new double[numberInputs];
             for (int j = 0; j < numberInputs; j++) 
                 input[j] = inFile.nextDouble();

             //create training record
             Record record = new Record(input, output);

             //add record to list
             records.add(record);
         }

         inFile.close();
    }

    /*************************************************************************/

    //Method sets parameters of neural network
    public void setParameters(int numberMiddle, int numberIterations, double rate, int seed)
    {
        //set hidden nodes, iterations, rate
        this.numberMiddle = numberMiddle;
        this.numberIterations = numberIterations;
        this.rate = rate;

        //initialize random number generation
        Random rand = new Random(seed);

        //create input/output arrays
        input = new double[numberInputs];
        middle = new double[numberMiddle];
        output = new double[numberOutputs];

        //create error arrays
        errorMiddle = new double[numberMiddle];
        errorOut = new double[numberOutputs];

        //initialize thetas at hidden nodes
        thetaMiddle = new double[numberMiddle];
        for (int i = 0; i < numberMiddle; i++)
            thetaMiddle[i] = 2*rand.nextDouble() - 1;	//range -1 to 1
         
        //initialize thetas at output nodes
        thetaOut = new double[numberOutputs];
        for (int i = 0; i < numberOutputs; i++)
            thetaOut[i] = 2*rand.nextDouble() - 1;

        //initialize weights between input/hidden nodes
        matrixMiddle = new double[numberInputs][numberMiddle];
        for (int i = 0; i < numberInputs; i++)
            for (int j = 0; j < numberMiddle; j++)
                matrixMiddle[i][j] = 2*rand.nextDouble() - 1;

        //initialize weights between hidden/output nodes
        matrixOut = new double[numberMiddle][numberOutputs];
        for (int i = 0; i < numberMiddle; i++)
            for (int j = 0; j < numberOutputs; j++)
                matrixOut[i][j] = 2*rand.nextDouble() - 1;
    }

    /*************************************************************************/

    //Method trains neural network
    public void train()
    {
        //repeat iteration number of times
        for (int i = 0; i < numberIterations; i++)
            //for each training record
            for (int j = 0; j < numberRecords; j++)
            {
                //calculate input/output
                forwardCalculation(records.get(j).input);

                //compute errors, update weights/thetas
                backwardCalculation(records.get(j).output);
            }
    }

    /*************************************************************************/

    //Method performs forward pass - computes input/output
    private void forwardCalculation(double[] trainingInput)
    {
        //feed inputs of record
        for (int i = 0; i < numberInputs; i++)
            input[i] = trainingInput[i];

        //for each hidden node
        for (int i = 0; i < numberMiddle; i++)
        {
            double sum = 0;

            //for each input, compute input at hidden node
            for (int j = 0; j < numberInputs; j++)
                sum += input[j]*matrixMiddle[j][i]; 
            
            //add theta
            sum += thetaMiddle[i];

            //compute output at hidden node
            middle[i] = 1/(1 + Math.exp(-sum));
        }        

        //for each output node
        for (int i = 0; i < numberOutputs; i++)
        {
            double sum = 0;

            //for each hidden node, compute input at output node
            for (int j = 0; j < numberMiddle; j++)
                sum += middle[j]*matrixOut[j][i]; 
            
            //add theta
            sum += thetaOut[i];

            //compute output at output node
            output[i] = 1/(1 + Math.exp(-sum));
        }        
    }

    /*************************************************************************/

    //Method performs backward pass - computes errors, updates weights/thetas
    private void backwardCalculation(double[] trainingOutput)
    {
        //compute error at each output node
        for (int i = 0; i < numberOutputs; i++)
            errorOut[i] = output[i]*(1-output[i])*(trainingOutput[i]-output[i]);

        //compute error at each hidden node
        for (int i = 0; i < numberMiddle; i++)
        {
            double sum = 0;

            for (int j = 0; j < numberOutputs; j++)
                sum += matrixOut[i][j]*errorOut[j];                
         
            errorMiddle[i] = middle[i]*(1-middle[i])*sum;
        }

        //update weights between hidden/output nodes
        for (int i = 0; i < numberMiddle; i++)
            for (int j = 0; j < numberOutputs; j++)
                matrixOut[i][j] += rate*middle[i]*errorOut[j];

        //update weights between input/hidden nodes
        for (int i = 0; i < numberInputs; i++)
            for (int j = 0; j < numberMiddle; j++)
                matrixMiddle[i][j] += rate*input[i]*errorMiddle[j];

        //update thetas at output nodes
        for (int i = 0; i < numberOutputs; i++)
            thetaOut[i] += rate*errorOut[i];

        //update thetas at hidden nodes
        for (int i = 0; i < numberMiddle; i++)
            thetaMiddle[i] += rate*errorMiddle[i];
    }

    /*************************************************************************/

    //Method computes output of an input
    private double[] test(double[] input)
    {
        //forward pass input
        forwardCalculation(input);
        
        //return output produced
        return output;
    }

    /*************************************************************************/

    //Method reads inputs from input file, computes outputs, and writes outputs 
    //to output file
    public void testData(String inputFile, String outputFile) throws IOException
    {
         Scanner inFile = new Scanner(new File(inputFile));
         PrintWriter outFile = new PrintWriter(new FileWriter(outputFile));

         //read number of records
         int numberRecords = inFile.nextInt();

         //for each record
         for (int i = 0; i < numberRecords; i++)
         {
             double[] input = new double[numberInputs];

             //read input from input file
             for (int j = 0; j < numberInputs; j++)
                  input[j] = inFile.nextDouble();

             //find output using neural network
             double[] output = test(input);

             //write output to output file
             for (int j = 0; j < numberOutputs; j++) {
            	 //convert output data
            	 outFile.print(Classify012NeuralDriver.deconvertClass(output[j]) + " ");                 
             }
             outFile.println();
         }

         inFile.close();
         outFile.close();
    }

    /*************************************************************************/

    //Method validates the network using the data from a file
    public void validate(String validationFile) throws IOException
    {
         Scanner inFile = new Scanner(new File(validationFile));

         //read number of records
         int numberRecords = inFile.nextInt();

         //initialize error
         double sumError = 0;

         //for each record
         for (int i = 0; i < numberRecords; i++)
         {
        	 //read actual outputs
             double[] actualOutput = new double[numberOutputs];
             for (int j = 0; j < numberOutputs; j++)
                  actualOutput[j] = inFile.nextDouble();
             
             //read inputs
             double[] input = new double[numberInputs];
             for (int j = 0; j < numberInputs; j++)
                  input[j] = inFile.nextDouble();

             //find predicted outputs
             double[] predictedOutput = test(input);

             //find error between actual and predicted outputs
             //both values still normalized
             sumError += computeError(actualOutput, predictedOutput);

             //display actual outputs
             for (int j = 0; j < numberOutputs; j++)
                 System.out.print("Actual output: " + actualOutput[j] + " ");
             System.out.println();

             //display predicted outputs
             for (int j = 0; j < numberOutputs; j++)
                 System.out.print("Predicted output: " + predictedOutput[j] + " ");
             System.out.println();
             System.out.println();
         }
  
         //display average error
         System.out.println("Average error: " + sumError/numberRecords);

         inFile.close();
    }

    /*************************************************************************/

    //Method finds root mean square error between actual and predicted output
    private double computeError(double[] actualOutput, double[] predictedOutput)
    {
        double error = 0;

        //sum of squares of errors
        for (int i = 0; i < actualOutput.length; i++)
            error += Math.pow(actualOutput[i] - predictedOutput[i], 2);

        //root mean square error
        return Math.sqrt(error/actualOutput.length);
    }    

    /*************************************************************************/
}