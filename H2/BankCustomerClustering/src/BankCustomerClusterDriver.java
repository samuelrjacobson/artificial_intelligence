import java.io.*;
import java.util.Scanner;

//Program tests k-means clustering in a specific application
public class BankCustomerClusterDriver
{
  //Main method
  public static void main(String[] args) throws IOException
  {
      //create clustering object
	  BankCustomerClustering k = new BankCustomerClustering();

	  //get input data (training and validation)
	  Scanner keyboard = new Scanner(System.in);
	  System.out.print("Enter an input file name: ");
	  String inputFile = keyboard.next();
	  System.out.print("Enter an output file name for grouped data: ");
	  String clusteredFile = keyboard.next();
      
	  String normalizedFile = "normalizedInputFile.txt";
	  convertInputFile(inputFile, normalizedFile);
	  
	  //load records
      k.load(normalizedFile);
      
      File normFile = new File(normalizedFile);
	  normFile.delete();
	  
      int numClusters = 0;
      double previousDistance = -1;
      boolean firstTime = true;
      
      for(int i = 1; i < 100; i++) {
    	  //set parameters
          k.setParameters(i, 10000, 58947);

          //perform clustering
          k.cluster();
          
          if(i == 1) previousDistance = k.getTotalDistance();
          else {
        	  double latestDistance = k.getTotalDistance();
        	  
        	  //stop if current SSE is 80% or more as large as previous SSE from k=n-1
        	  if (latestDistance / previousDistance > 0.8 && firstTime) {
        		  firstTime = false;
                  numClusters = i - 1;
        		  break;
        	  }
        	  previousDistance = latestDistance;
          }

          k.display(i + "_" + clusteredFile);
      }
      
      System.out.println("Number of clusters: " + (numClusters));

      //delete extra files
      for(int i = 1; i < numClusters; i++) {
    	  File file = new File(i + "_" + clusteredFile);
    	  file.delete();
      }
      File correctFile = new File(numClusters + "_" + clusteredFile);
      File finalOutputFile = new File(clusteredFile);

      correctFile.renameTo(finalOutputFile);
     
  }
  
  /****************************************************************************/
  
  //Method converts input file
  private static void convertInputFile(String inputFile, String outputFile) throws IOException
  {
      //input and output files
      Scanner inFile = new Scanner(new File(inputFile));
      PrintWriter outFile = new PrintWriter(new FileWriter(outputFile));

      //read number of records
      int numberRecords = inFile.nextInt();    

      //write number of records
      outFile.println(numberRecords);
      
      //read number of attributes
      int numberAttributes = inFile.nextInt();    

      //write number of attributes
      outFile.println(numberAttributes);

      //for each record
      for (int i = 0; i < numberRecords; i++)    
      {                         
          double age = inFile.nextDouble();                      //convert age
          double ageNumber = convertAgeOrIncome(age);
          outFile.print(ageNumber + " ");
          
          double income = inFile.nextDouble();                      //convert income
          double incomeNumber = convertAgeOrIncome(income);
          outFile.print(incomeNumber + " ");

          double creditScore = inFile.nextDouble();                  //convert credit score
          double csNumber = convertCreditScore(creditScore);
          outFile.println(csNumber + " ");
      }

      inFile.close();
      outFile.close();
  }

  /*************************************************************************/

  //Method normalizes age and income
  private static double convertAgeOrIncome(double value)
  {
      return (value - 20) / 80;
  } 

  /****************************************************************************/
  
  //Method normalizes credit score
  private static double convertCreditScore(double value)
  {
      return (value - 500) / 400;
  } 

  /****************************************************************************/
  
  //Method normalizes age and income
  public static double deconvertAgeOrIncome(double value)
  {
      return value * 80 + 20;
  } 

  /****************************************************************************/
  
  //Method normalizes credit score
  public static double deconvertCreditScore(double value)
  {
      return value * 400 + 500;
  } 

  /****************************************************************************/
}
