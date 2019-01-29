import java.io.*;
import java.util.*;

public class KMeans_ImplementationAndEvaluation {


    public static void main (String args[])
    {

         ArrayList<DatasetObject_KMeans> mainDataSetList = new ArrayList<DatasetObject_KMeans>();

        String mainDataSet_fileName = "C:\\Work\\Misc\\CCE-DM\\IRIS_Dataset_Normalised.txt";

        String mainDataSetFileLine = null;



        try {

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(mainDataSet_fileName), "UTF-8"));

            int mainDataSetIndexNumber=0;

            while((mainDataSetFileLine = bufferedReader.readLine()) != null) {

                System.out.println("The line read from main dataset file is : "+mainDataSetFileLine);

                String[] mainDataSet_lineArray = mainDataSetFileLine.split(",");



                Float trainFeature1 =Float.parseFloat(mainDataSet_lineArray[0]);
                Float trainFeature2=Float.parseFloat(mainDataSet_lineArray[1]);
                Float trainFeature3=Float.parseFloat(mainDataSet_lineArray[2]);
                Float trainFeature4=Float.parseFloat(mainDataSet_lineArray[3]);


                int mainDataSetClusterLable =Integer.parseInt(mainDataSet_lineArray[4]);

                mainDataSetList.add(new DatasetObject_KMeans(trainFeature1,trainFeature2,trainFeature3,trainFeature4,mainDataSetClusterLable));

//                System.out.println("Training set at "+trainingSetIndexNumber+":"+trainingSet[trainingSetIndexNumber][0]
//                        +"|"+trainingSet[trainingSetIndexNumber][1]+"|"+trainingSet[trainingSetIndexNumber][2]
//                        +"|"+trainingSet[trainingSetIndexNumber][3]+"|"+trainingSetClasses[trainingSetIndexNumber]);

                mainDataSetIndexNumber++;

            }


            bufferedReader.close();


            System.out.println("############################################################################################################################");

            System.out.println("Size of the main data set before clustering = "+mainDataSetList.size());


            ///////////////////////////////////////////////////////////////
            // KMeans Implementation Begins


            // Let K = 3

            // Centroid #1
            DatasetObject_KMeans centroid_1 = null;

            // Centroid #2
            DatasetObject_KMeans centroid_2 = null;

            // Centroid #3
            DatasetObject_KMeans centroid_3 = null;


            // Arraylist for Cluster #1
            ArrayList<DatasetObject_KMeans> clusterList_1 = new ArrayList<DatasetObject_KMeans>();

            // Arraylist for Cluster #2
            ArrayList<DatasetObject_KMeans> clusterList_2 = new ArrayList<DatasetObject_KMeans>();

            // Arraylist for Cluster #3
            ArrayList<DatasetObject_KMeans> clusterList_3 = new ArrayList<DatasetObject_KMeans>();

            // Trail 1 -> First 3 patterns will be the initial centroids:

            centroid_1=mainDataSetList.get(0);
            centroid_2=mainDataSetList.get(1);
            centroid_3=mainDataSetList.get(2);


            // Trail 2 -> 3 Random patterns:

//            Random r = new Random();
//            int Low = 0;
//            int High = 149;
//            int randomIndex1 = r.nextInt(High-Low) + Low;
//            int randomIndex2 = r.nextInt(High-Low) + Low;
//            int randomIndex3 = r.nextInt(High-Low) + Low;
//
//            centroid_1=mainDataSetList.get(randomIndex1);
//            centroid_2=mainDataSetList.get(randomIndex2);
//            centroid_3=mainDataSetList.get(randomIndex3);


            // Trail 3 -> One pattern from each class:

//            int indexForClass1 = 0;
//            int indexForClass2 = 50;
//            int indexForClass3 = 100;
//
//            centroid_1=mainDataSetList.get(indexForClass1);
//            centroid_2=mainDataSetList.get(indexForClass2);
//            centroid_3=mainDataSetList.get(indexForClass3);
//
//
            System.out.println("Initial centroid_1="+centroid_1.toString());
            System.out.println("Initial centroid_2="+centroid_2.toString());
            System.out.println("Initial centroid_3="+centroid_3.toString());


            DatasetObject_KMeans initialCentroidForCluster_1 = centroid_1;
            DatasetObject_KMeans initialCentroidForCluster_2 = centroid_2;
            DatasetObject_KMeans initialCentroidForCluster_3 = centroid_3;

            clusterList_1.add(centroid_1);
            clusterList_2.add(centroid_2);
            clusterList_3.add(centroid_3);

            ArrayList<DatasetObject_KMeans> clusterList_1_reference = returnCopyOfList(clusterList_1);
            ArrayList<DatasetObject_KMeans> clusterList_2_reference = returnCopyOfList(clusterList_2);
            ArrayList<DatasetObject_KMeans> clusterList_3_reference = returnCopyOfList(clusterList_3);

            while(true) {


                for (DatasetObject_KMeans datasetObject : mainDataSetList) {

                    switch (determineClosestCentroid(datasetObject, centroid_1, centroid_2, centroid_3)) {


                        case 1:
                            System.out.println("Point " + datasetObject.toString() + " will be alloted to cluster 1");
                            clusterList_1.add(datasetObject);
                            break;

                        case 2:
                            System.out.println("Point " + datasetObject.toString() + " will be alloted to cluster 2");
                            clusterList_2.add(datasetObject);
                            break;

                        case 3:
                            System.out.println("Point " + datasetObject.toString() + " will be alloted to cluster 3");
                            clusterList_3.add(datasetObject);
                            break;

                        case 0:
                            break;

                    }
                }

//                System.out.println("clusterList_1.size()="+clusterList_1.size());
//                System.out.println("clusterList_1_reference.size()="+clusterList_1_reference.size());
//
//                System.out.println("clusterList_2.size()="+clusterList_2.size());
//                System.out.println("clusterList_2_reference.size()="+clusterList_2_reference.size());
//
//                System.out.println("clusterList_3.size()="+clusterList_3.size());
//                System.out.println("clusterList_3_reference.size()="+clusterList_3_reference.size());



                if (areEqual(clusterList_1, clusterList_1_reference) && areEqual(clusterList_2, clusterList_2_reference) && areEqual(clusterList_3, clusterList_3_reference)) {
                    System.out.println("The reference clusters match with final clusters. Exiting the loop");
                    break;
                }
                else
                {

                    if(clusterList_1.size()!=0)
                        centroid_1=calculateCentroid(clusterList_1,1);
                    if(clusterList_2.size()!=0)
                        centroid_2=calculateCentroid(clusterList_2,2);
                    if(clusterList_3.size()!=0)
                        centroid_3=calculateCentroid(clusterList_3,3);


                    clusterList_1_reference.clear();
                    clusterList_2_reference.clear();
                    clusterList_3_reference.clear();

                    clusterList_1_reference = returnCopyOfList(clusterList_1);
                    clusterList_2_reference = returnCopyOfList(clusterList_2);
                    clusterList_3_reference = returnCopyOfList(clusterList_3);

                    clusterList_1.clear();
                    clusterList_2.clear();
                    clusterList_3.clear();

                }


            }

            System.out.println("\n\n");
            System.out.println("initialCentroidForCluster_1="+initialCentroidForCluster_1.toString());
            System.out.println("initialCentroidForCluster_2="+initialCentroidForCluster_2.toString());
            System.out.println("initialCentroidForCluster_3="+initialCentroidForCluster_3.toString());

            System.out.println("#################################################################");

            printArrayList("clusterList_1",clusterList_1);

            System.out.println("#################################################################");

            System.out.println("#################################################################");

            printArrayList("clusterList_2",clusterList_2);

            System.out.println("#################################################################");

            System.out.println("#################################################################");

            printArrayList("clusterList_3",clusterList_3);

            System.out.println("#################################################################");

            ArrayList<DatasetObject_KMeans> trainList = new ArrayList<DatasetObject_KMeans>();

            ArrayList<DatasetObject_KMeans> condensedList = new ArrayList<DatasetObject_KMeans>();


            //////////////////////////////////////////////////////////////////////////////////
            // Evaluations

            System.out.println("\n\nEvaluations of the clusters:");

            System.out.println("------------------------------------------------------------------");

            System.out.println("1. SSE");


            // 1. SSE

            double SSE = calculateSSE(clusterList_1,clusterList_2,clusterList_3);

            System.out.println("\nSSE = "+SSE);

            System.out.println("------------------------------------------------------------------");

            // 2. Entropy

            System.out.println("2. Entropy\n");

            // For cluster 1

            //Total number of patterns in clusters
            double m1 = clusterList_1.size();
            double m2 = clusterList_2.size();
            double m3 = clusterList_3.size();

            System.out.println("m1="+m1+"|m2="+m2+"|m3="+m3+"\n");


            // Number of patterns in cluster 1 belonging to class 1, 2 and 3

            double m11 = 0;
            double m12 = 0;
            double m13 = 0;

            for(DatasetObject_KMeans obj : clusterList_1)
            {
                if(obj.clusterLable==1)
                    m11++;
                if(obj.clusterLable==2)
                    m12++;
                if(obj.clusterLable==3)
                    m13++;
            }

            System.out.println("m11="+m11+"|m12="+m12+"|m13="+m13);


            // Number of patterns in cluster 2 belonging to class 1, 2 and 3

            double m21 = 0;
            double m22 = 0;
            double m23 = 0;

            for(DatasetObject_KMeans obj2 : clusterList_2)
            {
                if(obj2.clusterLable==1)
                    m21++;
                if(obj2.clusterLable==2)
                    m22++;
                if(obj2.clusterLable==3)
                    m23++;
            }

            System.out.println("m21="+m21+"|m22="+m22+"|m23="+m23);

            // Number of patterns in cluster 3 belonging to class 1, 2 and 3

            double m31 = 0;
            double m32 = 0;
            double m33 = 0;

            for(DatasetObject_KMeans obj3 : clusterList_3)
            {
                if(obj3.clusterLable==1)
                    m31++;
                if(obj3.clusterLable==2)
                    m32++;
                if(obj3.clusterLable==3)
                    m33++;
            }

            System.out.println("m31="+m31+"|m32="+m32+"|m33="+m33);

            // Calculating the probabilities

            System.out.println("\nCalculating the probabilities");


            double p11=m11/m1;
            double p12=m12/m1;
            double p13=m13/m1;

            System.out.println("p11="+p11+"|p12="+p12+"|p13="+p13);


            double p21=m21/m2;
            double p22=m22/m2;
            double p23=m23/m2;

            System.out.println("p21="+p21+"|p22="+p22+"|p23="+p23);


            double p31=m31/m3;
            double p32=m32/m3;
            double p33=m33/m3;

            System.out.println("p31="+p31+"|p32="+p32+"|p33="+p33);


            System.out.println("\nCalculating the entropies of clusters");

            double e1=0;

            if(p11!=0)
                e1=e1+(p11*(Math.log(p11)/Math.log(2)));
            if(p12!=0)
                e1=e1+(p12*(Math.log(p12)/Math.log(2)));
            if(p13!=0)
                e1=e1+(p13*(Math.log(p13)/Math.log(2)));

            e1=-1*e1;


            double e2=0;

            if(p21!=0)
                e2=e2+(p21*(Math.log(p21)/Math.log(2)));
            if(p22!=0)
                e2=e2+(p22*(Math.log(p22)/Math.log(2)));
            if(p23!=0)
                e2=e2+(p23*(Math.log(p23)/Math.log(2)));

            e2=-1*e2;


            double e3=0;

            if(p31!=0)
                e3=e3+(p31*(Math.log(p31)/Math.log(2)));
            if(p32!=0)
                e3=e3+(p32*(Math.log(p32)/Math.log(2)));
            if(p33!=0)
                e3=e3+(p33*(Math.log(p33)/Math.log(2)));

            e3=-1*e3;


//            double e1 = -1 * (p11*(Math.log(p11)/Math.log(2))) + (p12*(Math.log(p12)/Math.log(2))) + (p13*(Math.log(p13)/Math.log(2)));
//            double e2 = -1 * (p21*(Math.log(p21)/Math.log(2))) + (p22*(Math.log(p22)/Math.log(2))) + (p23*(Math.log(p23)/Math.log(2)));
//            double e3 = -1 * (p31*(Math.log(p31)/Math.log(2))) + (p32*(Math.log(p32)/Math.log(2))) + (p33*(Math.log(p33)/Math.log(2)));

            System.out.println("e1="+e1+"|e2="+e2+"|e3="+e3);

            System.out.println("\nTotal Entropy of clusters");

            double e = e1+e2+e3;

            System.out.println("e="+e);



            System.out.println("\n------------------------------------------------------------------");

            // 3. Purity

            System.out.println("3. Purity\n");

            double purity_1=(p11>p12)?(p11>p13?p11:p13):(p12>p13?p12:p13);
            double purity_2=(p21>p22)?(p21>p23?p21:p23):(p22>p23?p22:p23);
            double purity_3=(p31>p32)?(p31>p33?p31:p33):(p32>p33?p32:p33);

            System.out.println("purity of cluster 1="+purity_1+" or "+purity_1*100+"%");
            System.out.println("purity of cluster 2="+purity_2+" or "+purity_2*100+"%");
            System.out.println("purity of cluster 3="+purity_3+" or "+purity_3*100+"%");

            double purity = ((double)(clusterList_1.size())/(double)mainDataSetList.size())*purity_1 + ((double)(clusterList_2.size())/(double)mainDataSetList.size())*purity_2 +
                    ((double)(clusterList_3.size())/(double)mainDataSetList.size())*purity_3;

            System.out.println("Overall purity of clustering, Purity ="+purity);


            System.out.println("\n------------------------------------------------------------------");

            // 4. Precision

            System.out.println("4. Precision\n");

            System.out.println("Here precisionXY means precision(X,Y) where X = cluster and Y = class");

            double precision11=p11;
            double precision12=p12;
            double precision13=p13;

            System.out.println("precision11="+precision11+"|precision12="+precision12+"|precision13="+precision13);

            double precision21=p21;
            double precision22=p22;
            double precision23=p23;

            System.out.println("precision21="+precision21+"|precision22="+precision22+"|precision23="+precision23);

            double precision31=p31;
            double precision32=p32;
            double precision33=p33;

            System.out.println("precision31="+precision31+"|precision32="+precision32+"|precision33="+precision33);


            System.out.println("\n------------------------------------------------------------------");

            // 5. Recall

            System.out.println("5. Recall\n");

            System.out.println("Here recallXY means recall(X,Y) where X = cluster and Y = class");

            double numberOfPatternsOfClass_1=50;
            double numberOfPatternsOfClass_2=50;
            double numberOfPatternsOfClass_3=50;

            double recall11=m11/numberOfPatternsOfClass_1;
            double recall12=m12/numberOfPatternsOfClass_1;
            double recall13=m13/numberOfPatternsOfClass_1;

            System.out.println("recall11="+recall11+"|recall12="+recall12+"|recall13="+recall13);

            double recall21=m21/numberOfPatternsOfClass_2;
            double recall22=m22/numberOfPatternsOfClass_2;
            double recall23=m23/numberOfPatternsOfClass_2;

            System.out.println("recall21="+recall21+"|recall22="+recall22+"|recall23="+recall23);


            double recall31=m31/numberOfPatternsOfClass_3;
            double recall32=m32/numberOfPatternsOfClass_3;
            double recall33=m33/numberOfPatternsOfClass_3;

            System.out.println("recall31="+recall31+"|recall32="+recall32+"|recall33="+recall33);


            System.out.println("\n------------------------------------------------------------------");

            // 6. F-measure

            System.out.println("6. F-measure\n");

            System.out.println("Here FXY means F-measure(X,Y) where X = cluster and Y = class");


            double F11 = (2*precision11*recall11)/(precision11+recall11);
            double F12 = (2*precision12*recall12)/(precision12+recall12);
            double F13 = (2*precision13*recall13)/(precision13+recall13);

            System.out.println("F11="+F11+"|F12="+F12+"|F13="+F13);

            double F21 = (2*precision21*recall21)/(precision21+recall21);
            double F22 = (2*precision22*recall22)/(precision22+recall22);
            double F23 = (2*precision23*recall23)/(precision23+recall23);

            System.out.println("F21="+F21+"|F22="+F22+"|F23="+F23);

            double F31 = (2*precision31*recall31)/(precision31+recall31);
            double F32 = (2*precision32*recall32)/(precision32+recall32);
            double F33 = (2*precision33*recall33)/(precision33+recall33);

            System.out.println("F31="+F31+"|F32="+F32+"|F33="+F33);

            System.out.println("Observation : F measure is observed to be NaN (undefined) for some clusters and classes because of precision and recall for that pair cluster and class being 0");



        }
        catch(FileNotFoundException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
        catch(IOException exIO) {
            System.out.println(exIO.getMessage());
        }

    }


    private static double calculateSSE(ArrayList<DatasetObject_KMeans> clusterList_1, ArrayList<DatasetObject_KMeans> clusterList_2, ArrayList<DatasetObject_KMeans> clusterList_3) {

        DatasetObject_KMeans centroid_cluster1 = calculateCentroid(clusterList_1,1);
        DatasetObject_KMeans centroid_cluster2 = calculateCentroid(clusterList_2,2);
        DatasetObject_KMeans centroid_cluster3 = calculateCentroid(clusterList_3,3);

        double totalError = 0;

        for(DatasetObject_KMeans clusterPoint_1 : clusterList_1) {

            totalError = totalError + Math.pow(distanceBetween(centroid_cluster1,clusterPoint_1),2);

        }

        for(DatasetObject_KMeans clusterPoint_2 : clusterList_2) {

            totalError = totalError + Math.pow(distanceBetween(centroid_cluster2,clusterPoint_2),2);

        }

        for(DatasetObject_KMeans clusterPoint_3 : clusterList_3) {

            totalError = totalError + Math.pow(distanceBetween(centroid_cluster3,clusterPoint_3),2);

        }

//        System.out.println("totalError="+totalError);

        return totalError;


    }

    private static double distanceBetween(DatasetObject_KMeans centroid_cluster1, DatasetObject_KMeans clusterPoint) {

        double distanceBetweenPatterns = 0;

        distanceBetweenPatterns = distanceBetweenPatterns + Math.pow(centroid_cluster1.feature1 - clusterPoint.feature1, 2);
        distanceBetweenPatterns = distanceBetweenPatterns + Math.pow(centroid_cluster1.feature2 - clusterPoint.feature2, 2);
        distanceBetweenPatterns = distanceBetweenPatterns + Math.pow(centroid_cluster1.feature3 - clusterPoint.feature3, 2);
        distanceBetweenPatterns = distanceBetweenPatterns + Math.pow(centroid_cluster1.feature4 - clusterPoint.feature4, 2);

        distanceBetweenPatterns = Math.pow(distanceBetweenPatterns, 0.5);

        return distanceBetweenPatterns;

    }

    private static DatasetObject_KMeans calculateCentroid(ArrayList<DatasetObject_KMeans> clusterList_ref,int clusterLable) {


//        System.out.println("Lets calculate centroid for cluster"+clusterLable);
//        System.out.println("Size of cluster "+clusterLable+": "+clusterList_ref.size());
//        printArrayList("clusterList_ref",clusterList_ref);


        Float feature1;
        Float feature2;
        Float feature3;
        Float feature4;

        float total_1=0;
        float total_2=0;
        float total_3=0;
        float total_4=0;

        for(DatasetObject_KMeans data : clusterList_ref)
        {
            total_1=total_1+data.feature1;
            total_2=total_2+data.feature2;
            total_3=total_3+data.feature3;
            total_4=total_4+data.feature4;
        }

        feature1=total_1/clusterList_ref.size();
        feature2=total_2/clusterList_ref.size();
        feature3=total_3/clusterList_ref.size();
        feature4=total_4/clusterList_ref.size();

        DatasetObject_KMeans newCentroid =  new DatasetObject_KMeans(feature1,feature2,feature3,feature4,clusterLable);

        System.out.println("Centroid for cluster "+clusterLable+": "+newCentroid);


        return newCentroid;



    }

    private static ArrayList<DatasetObject_KMeans> returnCopyOfList(ArrayList<DatasetObject_KMeans> referenceList) {

        ArrayList<DatasetObject_KMeans> copyListToBeReturned = new ArrayList<DatasetObject_KMeans>();

        for(DatasetObject_KMeans objectFromReferenceList : referenceList)
        {
            copyListToBeReturned.add(objectFromReferenceList);
        }

        return copyListToBeReturned;

    }

    private static void printArrayList(String nameOfList, ArrayList<DatasetObject_KMeans> arrayList) {

        for(DatasetObject_KMeans alOject : arrayList)
        {

            System.out.println(nameOfList+"="+alOject.feature1+"|"+alOject.feature2+"|"+alOject.feature3+"|"+alOject.feature4+"|"+alOject.clusterLable);

        }

    }


    static Integer determineClosestCentroid(DatasetObject_KMeans datasetObjectToBeClustered, DatasetObject_KMeans centroid_1_ref, DatasetObject_KMeans centroid_2_ref, DatasetObject_KMeans centroid_3_ref)
    {


        Integer designatedCluster=null;


        double distanceFromCentroid_1 = 0;

//        System.out.println("datasetObjectToBeClustered.feature1="+datasetObjectToBeClustered.feature1);
//        System.out.println("datasetObjectToBeClustered.feature2="+datasetObjectToBeClustered.feature2);
//        System.out.println("datasetObjectToBeClustered.feature3="+datasetObjectToBeClustered.feature3);
//        System.out.println("datasetObjectToBeClustered.feature4="+datasetObjectToBeClustered.feature4);
//
//
//        System.out.println("centroid_1_ref.feature1="+centroid_1_ref.feature1);
//        System.out.println("centroid_1_ref.feature2="+centroid_1_ref.feature2);
//        System.out.println("centroid_1_ref.feature3="+centroid_1_ref.feature3);
//        System.out.println("centroid_1_ref.feature1="+centroid_1_ref.feature4);



        distanceFromCentroid_1 = distanceFromCentroid_1 + Math.pow(datasetObjectToBeClustered.feature1 - centroid_1_ref.feature1, 2);
        distanceFromCentroid_1 = distanceFromCentroid_1 + Math.pow(datasetObjectToBeClustered.feature2 - centroid_1_ref.feature2, 2);
        distanceFromCentroid_1 = distanceFromCentroid_1 + Math.pow(datasetObjectToBeClustered.feature3 - centroid_1_ref.feature3, 2);
        distanceFromCentroid_1 = distanceFromCentroid_1 + Math.pow(datasetObjectToBeClustered.feature4 - centroid_1_ref.feature4, 2);

        distanceFromCentroid_1 = Math.pow(distanceFromCentroid_1, 0.5);

//        System.out.println("distanceFromCentroid_1="+distanceFromCentroid_1);

        double distanceFromCentroid_2 = 0;

        distanceFromCentroid_2 = distanceFromCentroid_2 + Math.pow(datasetObjectToBeClustered.feature1 - centroid_2_ref.feature1, 2);
        distanceFromCentroid_2 = distanceFromCentroid_2 + Math.pow(datasetObjectToBeClustered.feature2 - centroid_2_ref.feature2, 2);
        distanceFromCentroid_2 = distanceFromCentroid_2 + Math.pow(datasetObjectToBeClustered.feature3 - centroid_2_ref.feature3, 2);
        distanceFromCentroid_2 = distanceFromCentroid_2 + Math.pow(datasetObjectToBeClustered.feature4 - centroid_2_ref.feature4, 2);

        distanceFromCentroid_2 = Math.pow(distanceFromCentroid_2, 0.5);

//        System.out.println("distanceFromCentroid_2="+distanceFromCentroid_2);

        double distanceFromCentroid_3 = 0;

        distanceFromCentroid_3 = distanceFromCentroid_3 + Math.pow(datasetObjectToBeClustered.feature1 - centroid_3_ref.feature1, 2);
        distanceFromCentroid_3 = distanceFromCentroid_3 + Math.pow(datasetObjectToBeClustered.feature2 - centroid_3_ref.feature2, 2);
        distanceFromCentroid_3 = distanceFromCentroid_3 + Math.pow(datasetObjectToBeClustered.feature3 - centroid_3_ref.feature3, 2);
        distanceFromCentroid_3 = distanceFromCentroid_3 + Math.pow(datasetObjectToBeClustered.feature4 - centroid_3_ref.feature4, 2);

        distanceFromCentroid_3 = Math.pow(distanceFromCentroid_3, 0.5);

//        System.out.println("distanceFromCentroid_3="+distanceFromCentroid_3);


        if(distanceFromCentroid_1<distanceFromCentroid_2) {

            if(distanceFromCentroid_1<distanceFromCentroid_3)
                designatedCluster= 1;
            else
                designatedCluster= 3;
        }
        else {

            if(distanceFromCentroid_2<distanceFromCentroid_3)
                designatedCluster= 2;
            else
                designatedCluster= 3;
        }


        if(distanceFromCentroid_1==0||distanceFromCentroid_2==0||distanceFromCentroid_3==0)
        {
            System.out.println("Distance was zero. Point itself is a centroid");
            designatedCluster=0;
        }

        System.out.println("designatedCluster for "+datasetObjectToBeClustered.toString()+" is "+designatedCluster);

        return designatedCluster;

    }




    static boolean areEqual(ArrayList<DatasetObject_KMeans> firstList, ArrayList<DatasetObject_KMeans> secondList)
    {

//        System.out.println("areEqual begins");

        int firstListSize=firstList.size();
        int secondListSize=secondList.size();

        if(firstListSize!=secondListSize)
            return false;
        else
        {

            int correct=0;
            for(int index=0;index<firstListSize;index++)
            {
                DatasetObject_KMeans firstListObject = firstList.get(index);

//                System.out.println("firstListObject="+firstListObject.toString());

                for(int sIndex=0;sIndex<secondListSize;sIndex++)
                {
                    DatasetObject_KMeans secondListObject = secondList.get(sIndex);

//                    System.out.println("secondListObject="+secondListObject.toString());

                    if(firstListObject.feature1==secondListObject.feature1 && firstListObject.feature2==secondListObject.feature2 &&
                            firstListObject.feature3==secondListObject.feature3 && firstListObject.feature4==secondListObject.feature4 )
                    {
//                        System.out.println("Correct ++");
                        correct++;
                        break;
                    }
                }

            }

//            System.out.println("Correct no.="+correct+" and firstListSize="+firstListSize);


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

class DatasetObject_KMeans {

    float feature1;
    float feature2;
    float feature3;
    float feature4;

    Integer clusterLable;


    public DatasetObject_KMeans(float feature1, float feature2, float feature3, float feature4, Integer clusterLable) {
        this.feature1 = feature1;
        this.feature2 = feature2;
        this.feature3 = feature3;
        this.feature4 = feature4;
        this.clusterLable = clusterLable;
    }

    @Override
    public String toString() {
        return "DatasetObject_KMeans= feature1:"+feature1+"|feature2:"+feature2+"|feature3:"+feature3+"|feature4:"+feature4+"|clusterLable:"+clusterLable;
    }
}
