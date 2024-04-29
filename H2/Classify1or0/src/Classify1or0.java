import java.io.*;
import java.util.*;

//Nearest neighbor classifier
public class Classify1or0
{
    /*************************************************************************/

    //Record class (inner class)
    private class Record 
    {
        private double[] attributes;         //attributes of record      
        private int className;               //class of record

        //Constructor of Record
        private Record(double[] attributeArray, int className2)
        {
            this.attributes = attributeArray;    //set attributes 
            this.className = className2;      //set class
        }
    }

    /*************************************************************************/

    private int numberRecords;               //number of training records   
    private int numberAttributes;            //number of attributes   
    private int numberClasses;               //number of classes
    private int numberNeighbors;             //number of nearest neighbors
    private ArrayList<Record> records;       //list of training records

    /*************************************************************************/

    //Constructor of NearestNeighbor
    public Classify1or0()
    {         
        //initial data is empty           
        numberRecords = 0;      
        numberAttributes = 0;
        numberClasses = 0;
        numberNeighbors = 0; 
        records = null;                        
    }

    /*************************************************************************/

    //Method loads data from training file
    public void loadTrainingData(String trainingFile) throws IOException
    {
         Scanner inFile = new Scanner(new File(trainingFile));

         //read number of records, attributes, classes
         numberRecords = inFile.nextInt();
         numberAttributes = inFile.nextInt();
         numberClasses = inFile.nextInt();

         //create empty list of records
         records = new ArrayList<Record>();        

         //for each record
         for (int i = 0; i < numberRecords; i++)    
         {
        	 //read class name
             int className = inFile.nextInt();
             
             //create attribute array
             double[] attributeArray = new double[numberAttributes]; 
                                     
             //read attribute values
             for (int j = 0; j < numberAttributes; j++)   
                  attributeArray[j] = inFile.nextDouble();  

             //create record
             Record record = new Record(attributeArray, className);

             //add record to list of records
             records.add(record);
         }

         inFile.close();
    }

    /*************************************************************************/

    //Method sets number of nearest neighbors
    public void setParameters(int numberNeighbors)
    {
        this.numberNeighbors = numberNeighbors;
    }

    /*************************************************************************/

    //Method reads records from test file, determines their classes, 
    //and writes classes to classified file
    public String classifyData(String testFile) throws IOException
    {
         Scanner inFile = new Scanner(new File(testFile));

         String outputData = "";
         
         //read number of records
         int numberRecords = inFile.nextInt();

         //for each record
         for (int i = 0; i < numberRecords; i++)
         {
             //create attribute array
             double[] attributeArray = new double[numberAttributes];

             //read attribute values
             for (int j = 0; j < numberAttributes; j++)
                  attributeArray[j] = inFile.nextDouble();

             //find class of attributes
             int className = classify(attributeArray);

             //write class name
             outputData += Classify1or0Driver.convertNumberToClass(className) + "\n";
             
             //write attribute values
             int count = 0;
             for (int j = 0; j < Math.sqrt(numberAttributes); j++) {
            	 for (int k = 0; k < Math.sqrt(numberAttributes); k++) {;
            		 outputData += ((int)attributeArray[count++] + " ");
            	 }
            	 outputData += "\n";
             }
         }

         inFile.close();
         //outFile.close();
         return outputData;
    }    

    /*************************************************************************/

    //Method determines the class of a set of attributes
    private int classify(double[] attributes)
    {
        double[] distance = new double[numberRecords];
        int[] id = new int[numberRecords];

        //find distances between attributes and all records
        for (int i = 0; i < numberRecords; i++)
        {
            distance[i] = distance(attributes, records.get(i).attributes);
            id[i] = i;
        }

        //find nearest neighbors
        nearestNeighbor(distance, id);

        //find majority class of nearest neighbors
        int className = majority(id);

        //return class
        return className;
    }

    /*************************************************************************/

    //Method finds the nearest neighbors
    private void nearestNeighbor(double[] distance, int[] id)
    {
        //sort distances and choose nearest neighbors
        for (int i = 0; i < numberNeighbors; i++)
            for (int j = i; j < numberRecords; j++)
                if (distance[i] > distance[j])
                {
                    double tempDistance = distance[i];
                    distance[i] = distance[j];
                    distance[j] = tempDistance;

                    int tempId = id[i];
                    id[i] = id[j];
                    id[j] = tempId;
                }
    }

    /*************************************************************************/

    //Method finds the majority class of nearest neighbors
    private int majority(int[] id)
    {
        double[] frequency = new double[numberClasses];	// frequency of each class among neighbors
        				// each element stores number of neighbors of that class

        //class frequencies are zero initially
        for (int i = 0; i < numberClasses; i++)
            frequency[i] = 0;

        //each neighbor contributes 1 to its class
        for (int i = 0; i < numberNeighbors; i++)
            frequency[(records.get(id[i]).className)] += 1;

        //find majority class
        int maxIndex = 0;                         
        for (int i = 0; i < numberClasses; i++)   
            if (frequency[i] > frequency[maxIndex])
               maxIndex = i;

        return maxIndex;
    }

    /*************************************************************************/

    //Method finds Binary distance between two points
    private double distance(double[] u, double[] v)
    {
        double distance = 0;         

        for (int i = 0; i < u.length; i++)
            if(u[i] != v[i]) distance += 1;
 
        return distance / u.length;               
    }

    /*************************************************************************/

    //Method validates classifier using validation file and displays error rate
    public boolean validate(String validationFile) throws IOException
    {
         Scanner inFile = new Scanner(new File(validationFile));

         //initially zero errors
         int numberErrors = 0;
         
         double[] attributeArray = new double[numberAttributes];

         //read actual class
         int actualClass = inFile.nextInt();
             
         //read attributes
         for (int j = 0; j < numberAttributes; j++)
             attributeArray[j] = inFile.nextDouble();
         
         //find class predicted by classifier
         int predictedClass = classify(attributeArray);

         //error if predicted and actual classes do not match
         if (predictedClass != actualClass)
            numberErrors += 1;
         
         inFile.close();

         //get error rate
         return (numberErrors > 0);

    }

    /************************************************************************/
    
}

