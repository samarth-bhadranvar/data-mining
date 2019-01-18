
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class CNN_PrototypeSetCreator {




    static int kValue = 1;

    public static void main (String args[])
    {

         ArrayList<DatasetObject> trainingSetList = new ArrayList<DatasetObject>();
         Integer[] trainingSetClasses = new Integer[90];

         ArrayList<DatasetObject> testSetList = new ArrayList<DatasetObject>();



        String trainingSet_fileName = "C:\\Work\\Misc\\CCE-DM\\IRIS_TrainingSet.txt";
        String testSet_fileName = "C:\\Work\\Misc\\CCE-DM\\IRIS_TestSet.txt";

        String trainingSetFileLine = null;



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



                Float trainFeature1 =Float.parseFloat(trainingSet_lineArray[0]);
                Float trainFeature2=Float.parseFloat(trainingSet_lineArray[1]);
                Float trainFeature3=Float.parseFloat(trainingSet_lineArray[2]);
                Float trainFeature4=Float.parseFloat(trainingSet_lineArray[3]);


                int trainClassLabel =Integer.parseInt(trainingSet_lineArray[4]);

                trainingSetList.add(new DatasetObject(trainingSetIndexNumber,trainFeature1,trainFeature2,trainFeature3,trainFeature4,trainClassLabel));

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

                Float testFeature1 =Float.parseFloat(testSet_lineArray[0]);
                Float testFeature2=Float.parseFloat(testSet_lineArray[1]);
                Float testFeature3=Float.parseFloat(testSet_lineArray[2]);
                Float testFeature4=Float.parseFloat(testSet_lineArray[3]);

                int testClassLabel =Integer.parseInt(testSet_lineArray[4]);

                testSetList.add(new DatasetObject(trainingSetIndexNumber,testFeature1,testFeature2,testFeature3,testFeature4,testClassLabel));

//                System.out.println("Test set at "+testSetIndexNumber+":"+testSet[testSetIndexNumber][0]
//                        +"|"+testSet[testSetIndexNumber][1]+"|"+testSet[testSetIndexNumber][2]
//                        +"|"+testSet[testSetIndexNumber][3]+"|"+testSetClasses[testSetIndexNumber]);

                testSetIndexNumber++;

            }

            bufferedReader.close();


            System.out.println("############################################################################################################################");

            ///////////////////////////////////////////////////////////////
            // CNN Implementation Begins

            ArrayList<DatasetObject> trainList = new ArrayList<DatasetObject>();

//    static Float[][] condensedSet = new Float[90][4];
            ArrayList<DatasetObject> condensedList = new ArrayList<DatasetObject>();



//    static Float[][] reducedSet = new Float[90][4];

            ArrayList<DatasetObject> reducedSet = new ArrayList<DatasetObject>();

            trainList = trainingSetList;

//            printArrayList("trainList",trainList);

            System.out.println("trainList size="+trainList.size());

            // Add first element of train set to condensed set
            condensedList.add(trainList.get(0));

            printArrayList("condensedList", condensedList);

            ArrayList<DatasetObject> condensedListForComparision;


            do {

                condensedListForComparision = returnCopyOfList(condensedList);

                printArrayList("condensedListForComparision", condensedListForComparision);

                ArrayList<DatasetObject> reducedList = new ArrayList<DatasetObject>();

                reducedList = calculateReducedList(trainList, condensedList);

                printArrayList("reducedList", reducedList);

                System.out.println("reducedListSize="+reducedList.size());


                ArrayList<DatasetObject> reducedListBuffer = new ArrayList<DatasetObject>();
                reducedListBuffer=returnCopyOfList(reducedList);
                System.out.println("reducedListBuffer size initially="+reducedListBuffer.size());

//                int reduceListBufferCounter=0;


//                Iterator reducedListIterator=reducedList.iterator();

                while (reducedList.size() > 0) {

                    System.out.println("reducedList.size() now is "+reducedList.size());

                    if(reducedListBuffer.size()==0)
                    {
                        System.out.println("reducedListBuffer is now empty");

                        System.out.println("condensedListForComparision.size="+condensedListForComparision.size());
                        System.out.println("condensedList.size="+condensedList.size());

                        printArrayList("condensedListForComparision when buffer size 0",condensedListForComparision);
                        printArrayList("condensedList when buffer size 0",condensedList);

                        if(areEqual(condensedListForComparision,condensedList)) {
                            System.out.println("Will break now");
                            break;
                        }
                        else {
                            condensedListForComparision.clear();
                            condensedListForComparision = returnCopyOfList(condensedList);
                        }

                        reducedListBuffer=returnCopyOfList(reducedList);
//                        if(areEqual(reducedList,reducedListBuffer))
//                        {
//                            System.out.println("Reached breaking point");
//                            break;
//                        }
                    }

                    DatasetObject topObjectFromReducedList = reducedListBuffer.get(0);

//                    DatasetObject topObjectFromReducedList = (DatasetObject) reducedListIterator.next();

                    System.out.println("topObjectFromReducedList="+topObjectFromReducedList.feature1+"|"+topObjectFromReducedList.feature2+"|"+topObjectFromReducedList.feature3+"|"
                            +topObjectFromReducedList.feature4);



                    int closestClassInCondensedList = findClassOfNearestNeighbour(topObjectFromReducedList, condensedList);

                    System.out.println("closestClassInCondensedList="+closestClassInCondensedList+" and topObjectFromReducedList.classLabel="+topObjectFromReducedList.classLabel);

                    if (closestClassInCondensedList == topObjectFromReducedList.classLabel) {
                        reducedListBuffer.remove(topObjectFromReducedList);
//                        reducedList.remove(topObjectFromReducedList);
                        System.out.println("reducedList size after no deletion from this list="+reducedList.size());
                        System.out.println("reducedListBuffer size after deletion from this list="+reducedListBuffer.size());

                        // Same class found, not selected for condensed set
//                        continue;
                    } else {
                        condensedList.add(topObjectFromReducedList);
                        System.out.println("condensedList size="+condensedList.size());
                        printArrayList("condesedList after add:",condensedList);
                        reducedList.remove(topObjectFromReducedList);
                        System.out.println("reducedList size after remove="+reducedList.size());
                        System.out.println("reducedListBuffer size after no deletion of this list="+reducedListBuffer.size());
                        printArrayList("reducedList after remove:",reducedList);
                        reducedListBuffer.remove(topObjectFromReducedList);

                    }

//                    if(areEqual(reducedList,reducedListBuffer))
//                    {
//                        break;
//                    }


//                    reduceListBufferCounter++;

                }

            }while(!areEqual(condensedListForComparision,condensedList));


            printArrayList("Final condensedList",condensedList);


        }
        catch(FileNotFoundException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
        catch(IOException exIO) {
            System.out.println(exIO.getMessage());
        }

    }

    private static ArrayList<DatasetObject> returnCopyOfList(ArrayList<DatasetObject> referenceList) {

        ArrayList<DatasetObject> copyListToBeReturned = new ArrayList<DatasetObject>();

        for(DatasetObject objectFromReferenceList : referenceList)
        {
            copyListToBeReturned.add(objectFromReferenceList);
        }

        return copyListToBeReturned;

    }

    private static void printArrayList(String nameOfList, ArrayList<DatasetObject> arrayList) {

        for(DatasetObject alOject : arrayList)
        {

            System.out.println(nameOfList+" at "+alOject.index+"="+alOject.feature1+"|"+alOject.feature2+"|"+alOject.feature3+"|"+alOject.feature4+"|"+alOject.classLabel);

        }

    }

    private static Integer calculateClosestClassInGivenList(final HashMap<Double,Integer> distanceMapToSort, ArrayList<DatasetObject> referenceList)
    {


        System.out.println("\n\n distanceMapToSort to be sorted\n\n");

        printMap(distanceMapToSort);

        System.out.println("\n\n treeMap sorted\n\n");



        TreeMap<Double,Integer> distanceMapSorted = new TreeMap<Double,Integer>(distanceMapToSort);
        printMap(distanceMapSorted);


        System.out.println("For the kvalue "+kValue+" the truncated map will be :");


        int count=0;

        int count1=0;
        int count2=0;
        int count3=0;




        for (Map.Entry<Double,Integer> entry: distanceMapSorted.entrySet()) {

            System.out.println("Key="+entry.getKey()+"|Value="+entry.getValue());

            if (count >= kValue) break;


            System.out.println("referenceList.get(entry.getValue())="+referenceList.get(entry.getValue()).classLabel);
//            if(trainingSetClasses[entry.getValue()].equals(1))
//                count1++;
//            else if(trainingSetClasses[entry.getValue()].equals(2))
//                count2++;
//            else if(trainingSetClasses[entry.getValue()].equals(3))
//                count3++;

            if((referenceList.get(entry.getValue()).classLabel+"").equals(1+"")) {
                count1++;
            } else if((referenceList.get(entry.getValue()).classLabel+"").equals(2+""))
                count2++;
            else if((referenceList.get(entry.getValue()).classLabel+"").equals(3+""))
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


    private static ArrayList<DatasetObject> calculateReducedList(ArrayList<DatasetObject> trainListForCalculation, ArrayList<DatasetObject> condensedListForCalculation)
    {

        if(condensedListForCalculation==null)
        {
            return trainListForCalculation;
        }

        ArrayList reducedList = new ArrayList();

        int trainSize=trainListForCalculation.size();
        int consensedSize=condensedListForCalculation.size();


        for(int trainCounter=0;trainCounter<trainSize;trainCounter++)
        {

            for(int condensedCounter=0;condensedCounter<consensedSize;condensedCounter++)
            {

                if(trainListForCalculation.get(trainCounter).feature1==condensedListForCalculation.get(condensedCounter).feature1 && trainListForCalculation.get(trainCounter).feature2==condensedListForCalculation.get(condensedCounter).feature2 &&
                        trainListForCalculation.get(trainCounter).feature3==condensedListForCalculation.get(condensedCounter).feature3 && trainListForCalculation.get(trainCounter).feature4==condensedListForCalculation.get(condensedCounter).feature4)
                {
                    continue;
                }
                else
                {
                    reducedList.add(new DatasetObject(trainCounter,trainListForCalculation.get(trainCounter).feature1,trainListForCalculation.get(trainCounter).feature2,trainListForCalculation.get(trainCounter).feature3
                            ,trainListForCalculation.get(trainCounter).feature4,trainListForCalculation.get(trainCounter).classLabel));

                }

            }

        }

        return reducedList;


    }


    private static int findClassOfNearestNeighbour(DatasetObject objectFromReducedSet, ArrayList<DatasetObject> condensedListUnderConsideration) {

//        int correct = 0;
//        Integer indexInCondensedList = null;

        System.out.println("condensedListUnderConsideration under findClassOfNearestNeighbour=");

        printArrayList("condensedListUnderConsideration",condensedListUnderConsideration);

        Integer closestClass = null;

            float minDistance = 10000;

            HashMap<Double, Integer> distanceMap = new HashMap<>();

            for (int indexInCondensedList = 0; indexInCondensedList < condensedListUnderConsideration.size(); indexInCondensedList++) {


                double distance = 0;

                distance = distance + Math.pow(objectFromReducedSet.feature1 - condensedListUnderConsideration.get(indexInCondensedList).feature1, 2);
                distance = distance + Math.pow(objectFromReducedSet.feature2 - condensedListUnderConsideration.get(indexInCondensedList).feature2, 2);
                distance = distance + Math.pow(objectFromReducedSet.feature3 - condensedListUnderConsideration.get(indexInCondensedList).feature3, 2);
                distance = distance + Math.pow(objectFromReducedSet.feature4 - condensedListUnderConsideration.get(indexInCondensedList).feature4, 2);

                distance = Math.pow(distance, 0.5);

                System.out.println("distance="+distance+" indexInCondensedList="+indexInCondensedList);

                distanceMap.put(distance, indexInCondensedList);

            }


            System.out.println("Distance map = ");
            printMap(distanceMap);


            closestClass = calculateClosestClassInGivenList(distanceMap,condensedListUnderConsideration);

            return closestClass;


    }

    static boolean areEqual(ArrayList<DatasetObject> firstList, ArrayList<DatasetObject> secondList)
    {
        int firstListSize=firstList.size();
        int secondListSize=secondList.size();

        if(firstListSize!=secondListSize)
            return false;
        else
        {

            int correct=0;
            for(int index=0;index<firstListSize;index++)
            {
                DatasetObject firstListObject = firstList.get(index);

                for(int sIndex=0;sIndex<secondListSize;sIndex++)
                {
                    DatasetObject secondListObject = secondList.get(sIndex);

                    if(firstListObject.feature1==secondListObject.feature1 && firstListObject.feature2==secondListObject.feature2 &&
                            firstListObject.feature3==secondListObject.feature3 && firstListObject.feature4==secondListObject.feature4 )
                    {
                        correct++;
                    }
                }

            }

            if(correct==firstListSize)
                return true;
            else
                return false;
        }

    }

    public static <K, V> void printMap(Map<K, V> map) {
        for (Map.Entry<K, V> entry : map.entrySet()) {
            System.out.println("Key : " + entry.getKey()
                    + " Value : " + entry.getValue());
        }
    }


}

class DatasetObject {

    int index;

    float feature1;
    float feature2;
    float feature3;
    float feature4;

    int classLabel;


    public DatasetObject(int index, float feature1, float feature2, float feature3, float feature4, int classLabel) {
        this.index = index;
        this.feature1 = feature1;
        this.feature2 = feature2;
        this.feature3 = feature3;
        this.feature4 = feature4;
        this.classLabel = classLabel;
    }
}
