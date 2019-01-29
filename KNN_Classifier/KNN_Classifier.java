
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class KNN_Classifier {

    static Float[][] trainingSet = new Float[90][4];
    static Integer[] trainingSetClasses = new Integer[90];


    public static void main (String args[])
    {

        String trainingSet_fileName = "C:\\Work\\Misc\\CCE-DM\\IRIS_TrainingSet.txt";
        String testSet_fileName = "C:\\Work\\Misc\\CCE-DM\\IRIS_TestSet.txt";

        String trainingSetFileLine = null;

        int kValue = 3;

        try {

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(trainingSet_fileName), "UTF-8"));

            int trainingSetIndexNumber=0;

            // Construct dataStructure to hold training data set
//            Float[][] trainingSet = new Float[90][4];
//            Integer[] trainingSetClasses = new Integer[90];

            while((trainingSetFileLine = bufferedReader.readLine()) != null) {

                System.out.println("The line read from training file is : "+trainingSetFileLine);

                String[] trainingSet_lineArray = trainingSetFileLine.split(",");

                trainingSet[trainingSetIndexNumber][0]=Float.parseFloat(trainingSet_lineArray[0]);
                trainingSet[trainingSetIndexNumber][1]=Float.parseFloat(trainingSet_lineArray[1]);
                trainingSet[trainingSetIndexNumber][2]=Float.parseFloat(trainingSet_lineArray[2]);
                trainingSet[trainingSetIndexNumber][3]=Float.parseFloat(trainingSet_lineArray[3]);

                trainingSetClasses[trainingSetIndexNumber]=Integer.parseInt(trainingSet_lineArray[4]);

//                System.out.println("Training set at "+trainingSetIndexNumber+":"+trainingSet[trainingSetIndexNumber][0]
//                        +"|"+trainingSet[trainingSetIndexNumber][1]+"|"+trainingSet[trainingSetIndexNumber][2]
//                        +"|"+trainingSet[trainingSetIndexNumber][3]+"|"+trainingSetClasses[trainingSetIndexNumber]);

                trainingSetIndexNumber++;

            }


            //Construct the dataStructure to hold test data set
            bufferedReader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(testSet_fileName), "UTF-8"));

            String testSetFileLine = null;

            int testSetIndexNumber=0;
            Float[][] testSet = new Float[60][4];
            Integer[] testSetClasses = new Integer[60];

            while((testSetFileLine = bufferedReader.readLine()) != null) {

                System.out.println("The line read from test file is : "+testSetFileLine);

                String[] testSet_lineArray = testSetFileLine.split(",");

                testSet[testSetIndexNumber][0]=Float.parseFloat(testSet_lineArray[0]);
                testSet[testSetIndexNumber][1]=Float.parseFloat(testSet_lineArray[1]);
                testSet[testSetIndexNumber][2]=Float.parseFloat(testSet_lineArray[2]);
                testSet[testSetIndexNumber][3]=Float.parseFloat(testSet_lineArray[3]);

                testSetClasses[testSetIndexNumber]=Integer.parseInt(testSet_lineArray[4]);

//                System.out.println("Test set at "+testSetIndexNumber+":"+testSet[testSetIndexNumber][0]
//                        +"|"+testSet[testSetIndexNumber][1]+"|"+testSet[testSetIndexNumber][2]
//                        +"|"+testSet[testSetIndexNumber][3]+"|"+testSetClasses[testSetIndexNumber]);

                testSetIndexNumber++;

            }

            bufferedReader.close();

            int correct=0;
            Integer index=null;

            Integer closestClass = null;

            for(int i=0;i<60;i++)
            {
                float minDistance = 10000;

                HashMap<Double,Integer> distanceMap = new HashMap<>() ;

                for(int j=0;j<90;j++)
                {


                    double distance=0;

                    for(int k=0;k<4;k++)
                    {
                        distance= (float) (distance+Math.pow((trainingSet[j][k]-testSet[i][k]),2));
                    }

                    distance = Math.pow(distance,0.5);

                    distanceMap.put(distance,j);

                }


                closestClass = calculateClosestClass(distanceMap,kValue);


                if(closestClass==testSetClasses[i])
                {
                    correct++;
                }

            }


            System.out.println("\n\nK value : "+kValue);
            System.out.println("Number of correct classifications = "+(correct));
            System.out.println("Classification accuracy = "+(correct*1.0/60)*100);





        }
        catch(FileNotFoundException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
        catch(IOException exIO) {
            System.out.println(exIO.getMessage());
        }

    }

    private static Integer calculateClosestClass(final HashMap<Double,Integer> distanceMapToSort, int kValue)
    {


        System.out.println("\n\n distanceMapToSort to be sorted\n\n");

//        printMap(distanceMapToSort);

        System.out.println("\n\n treeMap sorted\n\n");



        TreeMap<Double,Integer> distanceMapSorted = new TreeMap<Double,Integer>(distanceMapToSort);
//        printMap(distanceMapSorted);


        System.out.println("For the kvalue "+kValue+" the truncated map will be :");


        int count=0;

        int count1=0;
        int count2=0;
        int count3=0;




        for (Map.Entry<Double,Integer> entry: distanceMapSorted.entrySet()) {

            System.out.println("Key="+entry.getKey()+"|Value="+entry.getValue());

            if (count >= kValue) break;


            System.out.println("trainingSetClasses[entry.getValue()]="+trainingSetClasses[entry.getValue()]);

            if(trainingSetClasses[entry.getValue()].equals(1))
                count1++;
            else if(trainingSetClasses[entry.getValue()].equals(2))
                count2++;
            else if(trainingSetClasses[entry.getValue()].equals(3))
                count3++;

            count++;
        }

        int returnedClass=0;

        if(count1>count2) {

            if(count1>count3)
                returnedClass= 1;
            else
                returnedClass= 3;
        }
        else {

            if(count2>count3)
                returnedClass= 2;
            else
                returnedClass= 3;

        }


        System.out.println("returnedClass="+returnedClass);

        return returnedClass;

    }



    public static <K, V> void printMap(Map<K, V> map) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            System.out.println("Key : " + entry.getKey()
                    + " Value : " + entry.getValue());
        }
    }


}
