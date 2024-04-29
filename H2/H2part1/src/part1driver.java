import java.util.Scanner;
import java.io.*;
import java.nio.file.Files;

public class part1driver {

	private static final int NEIGHBORS = 5;

	public static void main(String[] args) {
		try {
			//get input data (training and validation)
	    	Scanner keyboard = new Scanner(System.in);
	    	System.out.print("Enter a training file name: ");
	    	String originalTrainingFile = keyboard.next();
	    	System.out.print("Enter a test file name: ");
	    	String originalTestFile = keyboard.next();
	    	System.out.print("Enter an output file name for classified data: ");
	    	String classifiedFile = keyboard.next();
	        keyboard.close();
	        
	        //read number of records, attributes, classes
	        Scanner inputReader = new Scanner(new File(originalTrainingFile));
	        int numberRecords = inputReader.nextInt();    
	        int numberAttributes = inputReader.nextInt();    
	        int numberClasses = inputReader.nextInt();
	        
	        //convert files
	        String trainingFile = "trainingFileConverted.txt";
	        convertInputFile(originalTrainingFile, trainingFile);
	        String testFile = "testFileConverted.txt";
	        convertTestFile(originalTestFile, testFile);
	        double validationError = 0;
	        String classifiedData = "";
	        
	        for(int i = 0; i < numberRecords; i++) {	// for each record, run program with it left out
	        	//construct nearest neighbor classifier
	            part1 classifier = new part1();
	            
	            Scanner fileReader = new Scanner(new File(trainingFile));
	            fileReader.nextLine();	            fileReader.nextLine();
	            
	            //transfer header info
	            String tempFile = "tempFile"+i+".txt";
	            String validationFile = "validationFile"+i+".txt";
	            PrintWriter outFile = new PrintWriter(new FileWriter(tempFile));
	            outFile.println(numberRecords-1 + " " + numberAttributes + " " + numberClasses);
	            PrintWriter validationFileReader = new PrintWriter(new FileWriter(validationFile));

	            for(int j = 0; j < numberRecords; j++) {	// for each record, read and possibly write
	            	String row = fileReader.nextLine();

	                if(i != j) {	// if it's not the record we're leaving out, add it to temp training file
	    		        outFile.println(row);
	                }
	                else {		// else add it to validation file
	                	validationFileReader.println(row);
	                }
	            }
	            
	            outFile.close();
	            validationFileReader.close();
	            
	            //load training data
	            classifier.loadTrainingData(tempFile);

	            //set nearest neighbors
	            classifier.setParameters(NEIGHBORS);

	            //validate classfier
	            validationError += classifier.validate(validationFile);
	            
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
	        part1 classifier = new part1();
	        //load training data
	        classifier.loadTrainingData(trainingFile);

	        //set nearest neighbors
	        classifier.setParameters(NEIGHBORS);

	        //classify test data
	        classifiedData += classifier.classifyData(testFile);
	        
	        //validationError /= numberRecords;
	        double errorRate = 100.0*validationError/numberRecords;
	        System.out.println("validation error: " + errorRate + "%");
	        classifiedData += "Validation error: " + errorRate + "%\n";
	        classifiedData += "Number of nearest neighbors: 5";
	        
	        PrintWriter outFile = new PrintWriter(new FileWriter(classifiedFile));
	        outFile.print(classifiedData);
	        outFile.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	/*************************************************************************/

    //Method converts file to text format
    private static void convertInputFile(String inputFile, String outputFile) throws IOException
    {
        //input and output files
        Scanner inFile = new Scanner(new File(inputFile));
        PrintWriter outFile = new PrintWriter(new FileWriter(outputFile));

        //transfer header info
        int numberRecords = inFile.nextInt();    
        outFile.print(numberRecords + " ");
        int numberAttributes = inFile.nextInt();    
        outFile.print(numberAttributes + " ");
        int numberClasses = inFile.nextInt();
        outFile.println(numberClasses + " \n");
        
        //for each record
        for (int i = 0; i < numberRecords; i++)    
        {      
        	//convert each attribute
        	int credit = inFile.nextInt();
			double creditScore = convertCredit(credit);
			outFile.print(creditScore + " ");

			int income = inFile.nextInt();
			double newIncome = convertIncome(income);
			outFile.print(newIncome + " ");

			int age = inFile.nextInt();
			double newAge = convertAge(age);
			outFile.print(newAge + " ");

			String sex = inFile.next();
			double newSex = convertSex(sex);
			outFile.print(newSex + " ");

			String maritalStatus = inFile.next();
			double newMaritalStatus = convertMaritalStatus(maritalStatus);
			outFile.print(newMaritalStatus + " ");

			String className = inFile.next();                  //convert class name
			double classNumber = convertClassToNumber(className);
			outFile.println(classNumber);
        }
        inFile.close();
        outFile.close();
    }

	/*************************************************************************/

	//Method converts test file to numerical format
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
			//convert each attribute
			int credit = inFile.nextInt();
			double creditScore = convertCredit(credit);
			outFile.print(creditScore + " ");

			int income = inFile.nextInt();
			double newIncome = convertIncome(income);
			outFile.print(newIncome + " ");

			int age = inFile.nextInt();
			double newAge = convertAge(age);
			outFile.print(newAge + " ");

			String sex = inFile.next();
			double newSex = convertSex(sex);
			outFile.print(newSex + " ");

			String maritalStatus = inFile.next();
			double newMaritalStatus = convertMaritalStatus(maritalStatus);
			outFile.println(newMaritalStatus);
			
			outFile.flush();
		}

		inFile.close();
		outFile.close();
	}

		
	//Methods to convert attributes to numbers
	private static double convertMaritalStatus(String maritalStatus) {
		if(maritalStatus.equals("married"))
			return 0;
		else if(maritalStatus.equals("single"))
			return 0.5;
		else
			return 1;
	}

	private static double convertSex(String sex) {
		if(sex.equals("female"))
			return 0;
		else 
			return 1;
	}

	private static double convertAge(int age) {
		return (age - 30)/50.0;
	}

	private static double convertIncome(int income) {
		return (income - 30)/60.0;
	}

	private static double convertCredit(int credit) {
		return (credit - 500)/400.0;
	}

	//Methods to convert back from numbers
	public static String deconvertMaritalStatus(double n) {
		if (n == 0) return "married";
		else if (n == 0.5) return "single";
		else return "divorced";
	}

	public static String deconvertSex(double n) {
		if (n == 0) return "female";
		else return "male";
	}

	public static int deconvertAge(double age) {
		return (int)(age * 50 + 30);
	}

	public static int deconvertIncome(double income) {
		return (int)(income + 60 + 30);
	}

	public static int deconvertCredit(double credit) {
		return (int)(credit * 400 + 500);
	}


	/****************************************************************************/

	//Method converts class name to number
	private static double convertClassToNumber(String risk)
	{
		if(risk.equals("low"))
			return 1;
		else if(risk.equals("medium"))
			return 2;
		else if(risk.equals("high"))
			return 3;
		else if(risk.equals("undetermined"))
			return 4;
		return -1;
	}
	
	/****************************************************************************/

	//Method converts number to class name
	public static String convertNumberToClass(int number)
	{
		if (number == 1)
			return "low";
		else if (number == 2)
			return "medium"; 
		else if (number == 3)
			return "high";
		else if (number == 4)
			return "undetermined";
		return "-1";
	}

}
