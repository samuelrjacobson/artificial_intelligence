import java.io.*;
import java.nio.file.Files;
import java.util.*;

//Program tests nearest neighbor classifier in a specific application
public class Classify1or0Driver
{
    /*************************************************************************/

    //number of nearest neighbors
    private static final int NEIGHBORS = 5;

    //Main method
    public static void main(String[] args) throws IOException
    {
    	//get input data (training and validation)
    	Scanner keyboard = new Scanner(System.in);
    	System.out.print("Enter a training file name: ");
    	String originalTrainingFile = keyboard.next();
    	System.out.print("Enter a test file name: ");
    	String testFile = keyboard.next();
    	System.out.print("Enter an output file name for classified data: ");
    	String classifiedFile = keyboard.next();
        
        //read number of records, attributes, classes
        Scanner inputReader = new Scanner(new File(originalTrainingFile));
        int numberRecords = inputReader.nextInt();    
        int numberAttributes = inputReader.nextInt();    
        int numberClasses = inputReader.nextInt();
        
        //convert files
        String trainingFile = "trainingFileConverted.txt";
        convertInputFile(originalTrainingFile, trainingFile);
        int validationError = 0;
        String classifiedData = "";
        
        for(int i = 0; i < numberRecords; i++) {	// for each record, run program with it left out
        	//construct nearest neighbor classifier
            Classify1or0 classifier = new Classify1or0();
            
            Scanner fileReader = new Scanner(new File(trainingFile));
            fileReader.nextLine();
            
            // create new temporary file and add header info
            String tempFile = "tempFile"+i+".txt";
            String validationFile = "validationFile"+i+".txt";
            PrintWriter outFile = new PrintWriter(new FileWriter(tempFile));
            outFile.println(numberRecords-1 + " " + numberAttributes + " " + numberClasses);
            PrintWriter validationFileReader = new PrintWriter(new FileWriter(validationFile));

            for(int j = 0; j < numberRecords; j++) {	// for each record, read and possibly write
            	
            	for(int k = 0; k < Math.sqrt(numberAttributes)+2; k++) {	// for each row
            		String row = fileReader.nextLine();

                	if(i != j) {
    		            outFile.println(row);
                	}
                	else {
                		validationFileReader.println(row);
                	}
            	}
            }
            outFile.close();
            validationFileReader.close();
            
            //load training data
            classifier.loadTrainingData(tempFile);

            //set nearest neighbors
            classifier.setParameters(NEIGHBORS);

            //validate classfier
            if (classifier.validate(validationFile)) validationError += 1;

            //delete temporary files
            try {
            	File file = new File(tempFile);
                Files.delete(file.toPath());
                file = new File(validationFile);
                Files.delete(file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        // classify data
        //construct nearest neighbor classifier
        Classify1or0 classifier = new Classify1or0();
        //load training data
        classifier.loadTrainingData(trainingFile);

        //set nearest neighbors
        classifier.setParameters(NEIGHBORS);

        //classify test data
        classifiedData += classifier.classifyData(testFile);
        
        //calculate error rate
        double errorRate = 100.0*validationError/numberRecords;
        System.out.println("validation error: " + errorRate + "%");
        classifiedData += "Validation error: " + errorRate + "%\n";
        classifiedData += "Number of nearest neighbors: " + NEIGHBORS;
        
        PrintWriter outFile = new PrintWriter(new FileWriter(classifiedFile));
        outFile.print(classifiedData);
        outFile.close();
        
    }

    /*************************************************************************/

    //Method converts file to text format
    private static void convertInputFile(String inputFile, String outputFile) throws IOException
    {
        //input and output files
        Scanner inFile = new Scanner(new File(inputFile));
        PrintWriter outFile = new PrintWriter(new FileWriter(outputFile));

        // transfer header information
        int numberRecords = inFile.nextInt();    
        outFile.print(numberRecords + " ");
        int numberAttributes = inFile.nextInt();    
        outFile.print(numberAttributes + " ");
        int numberClasses = inFile.nextInt();
        outFile.println(numberClasses + " \n");
        
        //for each record
        for (int i = 0; i < numberRecords; i++)    
        {      
        	inFile.nextLine();		inFile.nextLine();
        	String className = inFile.nextLine();				//convert class name
            int num = convertClassToNumber(className);
            outFile.println(num);
            
            for (int j = 0; j < Math.sqrt(numberAttributes); j++) {
            	for (int k = 0; k < Math.sqrt(numberAttributes); k++) {
            		outFile.print(inFile.nextInt() + " ");
            	}
            	outFile.println();
            }
            outFile.println();
        }

        inFile.close();
        outFile.close();
    }

    /****************************************************************************/
    
    //Method converts class name to number
    private static int convertClassToNumber(String className)
    {
        if (className.equals("zero"))
            return 0;
        else if (className.equals("one"))
            return 1; 
        else {
        	System.out.println("not zero nor one");
            return -1;
        }
    }
    
    /****************************************************************************/

    //Method converts class number to name
    public static String convertNumberToClass(int n)
    {
        if (n == 0)
            return "zero";
        else if (n == 1)
            return "one"; 
        return "-1";
    }
}

