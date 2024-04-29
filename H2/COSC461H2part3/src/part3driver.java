import java.util.Scanner;
import java.io.*;
import java.nio.file.Files;

public class part3driver {

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
	        
	        //read number of attribute values
	        int[] attributesArray = new int[numberAttributes];
	        for (int i = 0; i < numberAttributes; i++)
	        	attributesArray[i] = inputReader.nextInt();
	        
	        String trainingFile = "trainingFileConverted.txt";
	        convertInputFile(originalTrainingFile, trainingFile);
	        String testFile = "testFileConverted.txt";
	        convertTestFile(originalTestFile, testFile);
	        double validationError = 0;
	        String classifiedData = "";
	        
	        for(int i = 0; i < numberRecords; i++) {	// for each record, run program with it left out
	        	//construct nearest neighbor classifier
	            part3 classifier = new part3();
	            
	            Scanner fileReader = new Scanner(new File(trainingFile));
	            fileReader.nextLine();	fileReader.nextLine();	fileReader.nextLine();
	            
	            // create temporary training and validation files
	            String tempFile = "tempFile"+i+".txt";
	            String validationFile = "validationFile"+i+".txt";
	            PrintWriter outFile = new PrintWriter(new FileWriter(tempFile));
	            PrintWriter validationFileReader = new PrintWriter(new FileWriter(validationFile));

	            // write header info to training files
	            outFile.println(numberRecords-1 + " " + numberAttributes + " " + numberClasses);
	            for (int j = 0; j < numberAttributes; j++)
		        	outFile.print(attributesArray[j] + " ");
	            outFile.println("\n");
	            
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

	            //compute probabilities
	            classifier.computeProbability();

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
	        part3 classifier = new part3();
	        //load training data
	        classifier.loadTrainingData(trainingFile);
	        
	        //compute probabilities
            classifier.computeProbability();

	        //classify test data
	        classifiedData += classifier.classifyData(testFile);
	        
	        double errorRate = 100.0*validationError/numberRecords;
	        System.out.println("validation error: " + errorRate + "%");
	        classifiedData += "Validation error: " + errorRate + "%\n";
	        
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

        int numberRecords = inFile.nextInt();    
        outFile.print(numberRecords + " ");
        int numberAttributes = inFile.nextInt();    
        outFile.print(numberAttributes + " ");
        int numberClasses = inFile.nextInt();
        outFile.println(numberClasses);
        
        //read number of attribute values
        for (int i = 0; i < numberAttributes; i++)
            outFile.print(inFile.nextInt() + " ");
        outFile.println("\n");
        
        //for each record
        for (int i = 0; i < numberRecords; i++)    
        {      
        	//convert attributes
			int numLangs = convertNumLangs(inFile.nextInt());
			outFile.print(numLangs + " ");

			int javaKnowledge = convertJavaKnowledge(inFile.next());
			outFile.print(javaKnowledge + " ");

			int yearsExperience = convertYearsExperience(inFile.nextInt());
			outFile.print(yearsExperience + " ");
			
			int major = convertMajor(inFile.next());
			outFile.print(major + " ");
			
			int academicPerformance = convertAcademicPerformance(inFile.next());
			outFile.print(academicPerformance + " ");

			String className = inFile.next();                  //convert class name
			int classNumber = convertClassToNumber(className);
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
			//convert attributes
			int numLangs = convertNumLangs(inFile.nextInt());
			outFile.print(numLangs + " ");

			int javaKnowledge = convertJavaKnowledge(inFile.next());
			outFile.print(javaKnowledge + " ");

			int yearsExperience = convertYearsExperience(inFile.nextInt());
			outFile.print(yearsExperience + " ");
			
			int major = convertMajor(inFile.next());
			outFile.print(major + " ");
			
			int academicPerformance = convertAcademicPerformance(inFile.next());
			outFile.print(academicPerformance + " ");
			
			outFile.flush();
		}

		inFile.close();
		outFile.close();
	}			

	//Methods to convert to numbers
	private static int convertNumLangs(int n) {
		return n + 1;
	}

	private static int convertJavaKnowledge(String s) {
		if ("java".equals(s)) return 1;
		else return 2;
	}
	
	private static int convertYearsExperience(int n) {
		return n + 1;
	}
	
	private static int convertMajor(String s) {
		if ("cs".equals(s)) return 1;
		else return 2;
	}
	
	private static int convertAcademicPerformance(String s) {
		if ("A".equals(s))
			return 1;
	    else if ("B".equals(s))
	        return 2;
	    else if ("C".equals(s))
	        return 3; 
		else 
			return 4;
	}
	
	//Methods to convert back from numbers
	public static int deconvertNumLangs(int n) {
		return n - 1;
	}

	public static String deconvertJavaKnowledge(int n) {
		if (n == 1) return "java";
		else return "no";
	}
	
	public static int deconvertYearsExperience(int n) {
		return n - 1;
	}
	
	public static String deconvertMajor(int n) {
		if (n == 1) return "cs";
		else return "other";
	}
	
	public static String deconvertAcademicPerformance(int n) {
		if (n == 1)
			return "A";
	    else if (n == 2)
	        return "B";
	    else if (n == 3)
	        return "C"; 
		else 
			return "D";
	}

	/****************************************************************************/

	//Method converts class name to number
	private static int convertClassToNumber(String s)
	{
		if("interview".equals(s))
			return 1;
		else 
			return 2;
	}
	
	/****************************************************************************/

	//Method converts number to class name
	public static String convertNumberToClass(int number)
	{
		if (number == 1)
			return "interview";
		else 
			return "no"; 
	}

}
