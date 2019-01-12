package sample;

import Jama.Matrix;
import database.ObjectClass;
import database.SingleObject;
import org.apache.commons.math3.util.CombinatoricsUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Calculations {
    public static ObjectClass acerObjectClass ;
    public static ObjectClass quercusObjectClass;

    public static void instantiateObjectClasses(ArrayList<SingleObject> listOfAllObjects) {
        acerObjectClass = new ObjectClass("Acer");
        quercusObjectClass = new ObjectClass("Quercus");
        for(SingleObject object : listOfAllObjects) {
            if (object.getClassName().startsWith("Acer ")) {
                acerObjectClass.objectList.add(object);
            } else if (object.getClassName().startsWith("Quercus ")) {
                quercusObjectClass.objectList.add(object);
            } else
                System.out.println("Obiekt nierozpoznany");
        }
        System.out.println("Acer count: " + acerObjectClass.objectList.size());
        System.out.println("Quercus count: " + quercusObjectClass.objectList.size());

        acerObjectClass.calculateMatrixes();
        quercusObjectClass.calculateMatrixes();
    }

    public static void calculateFisher(int featureCount) {
        calculateFisher(featureCount, acerObjectClass, quercusObjectClass);
        System.out.println(FisherResult.getFisherResults(featureCount));
    }

    public static void calculateFisher(int featureCount, ObjectClass acer, ObjectClass quercus) {
        Iterator<int[]> iterator = CombinatoricsUtils.combinationsIterator(acer.getAverageMatrix().getRowDimension(),
                featureCount);
        //int[] chosenFeatures = new int[featureCount];
        double fisherValue = 0;
        while (iterator.hasNext()) {
            int[] next = iterator.next();
            double tempFisher = calculateFisher(next, acer, quercus);
            if (tempFisher > fisherValue) {
                FisherResult.indexes = next;
                FisherResult.value = tempFisher;
                fisherValue = tempFisher;
            }
        }
    }

    private static double calculateFisher(int[] features, ObjectClass acer, ObjectClass quercus) {

        double minus = calculateEuclidesDistance(
                acer.getAverageMatrix().getMatrix(features, 0, 0).getRowPackedCopy(),
                quercus.getAverageMatrix().getMatrix(features, 0, 0).getRowPackedCopy());

        Matrix sA = getSMatrix(acer.getFeatureMatrix().getMatrix(0, acer.getFeatureMatrix().getRowDimension() -1, features),
                acer.getAverageMatrix().getMatrix(features, 0, 0));
        Matrix sB = getSMatrix(quercus.getFeatureMatrix().getMatrix(0, quercus.getFeatureMatrix().getRowDimension() -1, features),
                quercus.getAverageMatrix().getMatrix(features, 0, 0));
        double fisher = minus / (sA.det() + sB.det());
        return fisher;
    }

    public static double calculateEuclidesDistance(double[] avgA, double[] avgB) {
        int size = avgA.length;

        double avgSumPoweredSubstraction = 0;

        for (int i = 0; i < size; i++) {
            avgSumPoweredSubstraction += Math.pow(avgA[i] - avgB[i], 2);
        }

        return Math.sqrt(avgSumPoweredSubstraction);
    }

    private static Matrix getSMatrix(Matrix x, Matrix avg) {
        // Matrix minus = getMinusElement(x, avg);
        double[][] doubles = replicateVectorToMatrix(avg.getRowPackedCopy(), x.getRowDimension());
        Matrix avgNew = new Matrix(doubles);
        Matrix minus1 = x.transpose().minus(avgNew);

        Matrix times = minus1.times(minus1.transpose());
        return times;
    }

    private static double[][] replicateVectorToMatrix(double[] vector, int matrixLength) {
        double[][] matrix = new double[vector.length][matrixLength];

        for (int i = 0; i < vector.length; i++) {
            for (int j = 0; j < matrixLength; j++) {
                matrix[i][j] = vector[i];
            }
        }

        return matrix;
    }

    public static class FisherResult {
        public static int[] indexes;
        public static double value;

        public FisherResult() {
            indexes[0] = 0;
            value = 0;
        }

        public static String getFisherResults(int featureCount) {
            String resultPrintout = "Fisher for " + featureCount + " features\n";
            for (int i : indexes) {
                resultPrintout+="Feature number: " + i +",\n";
            }
            resultPrintout+="Value: " + value;
            return resultPrintout;
        }
    }
}
