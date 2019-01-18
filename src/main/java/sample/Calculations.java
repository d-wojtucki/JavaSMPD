package sample;

import Jama.Matrix;
import database.ObjectClass;
import database.SingleObject;
import org.apache.commons.math3.util.CombinatoricsUtils;

import java.util.*;

public class Calculations {
    public static ObjectClass acerObjectClass;
    public static ObjectClass quercusObjectClass;
    public static Map<Integer, Double> resultsMap = new HashMap<>();
    public static Map<Integer, SFS> SFSResultMap = new HashMap<>();
    double fisherTemp = 0;

//    public static int[] sfsIndexes;
//    public static double sfsValue;

    public static void instantiateObjectClasses(ArrayList<SingleObject> listOfAllObjects) {
        acerObjectClass = new ObjectClass("Acer");
        quercusObjectClass = new ObjectClass("Quercus");
        for (SingleObject object : listOfAllObjects) {
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
        //System.out.println(FisherResult.getFisherResults(featureCount));
    }

    public static void calculateFisher(int featureCount, ObjectClass acer, ObjectClass quercus) {
        Iterator<int[]> iterator = CombinatoricsUtils.combinationsIterator(acer.getAverageMatrix().getRowDimension(),
                featureCount);
        double tempFisher = 0;
        double fisherValue = 0;
        while (iterator.hasNext()) {
            int[] next = iterator.next();
            tempFisher = calculateFisher(next, acer, quercus);
            resultsMap.put(next[0], tempFisher);
            if (tempFisher > fisherValue) {
                FisherResult.indexes = next;
                FisherResult.value = tempFisher;
                fisherValue = tempFisher;
            }
        }
    }

    public static void calculateSFS(int featureCount) {
        Iterator<int[]> iterator = CombinatoricsUtils.combinationsIterator(acerObjectClass.getAverageMatrix().getRowDimension(),
                featureCount);
        calculateFisher(1);
        double temp = 0;
        double fisherValue = 0;
        while (iterator.hasNext()) {
            int[] next = iterator.next();
            for (int i = 0; i < next.length; i++) {
                temp = resultsMap.get(next[i]);
                if (fisherValue < temp) {
                    SFSResult.value = temp;
                    //SFSResult.sfsFeature = next[i];
                    fisherValue = temp;
                }
            }
        }
    }

    private static double calculateFisher(int[] features, ObjectClass acer, ObjectClass quercus) {
        double minus = calculateEuclidesDistance(
                acer.getAverageMatrix().getMatrix(features, 0, 0).getRowPackedCopy(),
                quercus.getAverageMatrix().getMatrix(features, 0, 0).getRowPackedCopy());

        Matrix sA = getSMatrix(acer.getFeatureMatrix().getMatrix(0, acer.getFeatureMatrix().getRowDimension() - 1, features),
                acer.getAverageMatrix().getMatrix(features, 0, 0));
        Matrix sB = getSMatrix(quercus.getFeatureMatrix().getMatrix(0, quercus.getFeatureMatrix().getRowDimension() - 1, features),
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

    public void calculateSFS22(int featuresCount) {
        calculateFisher(1);
        for (int g = 2; g <= featuresCount; g++) {
            calculateSFSV2(g);
        }
    }

    public void calculateSFSV2(int featuresCount) {

        if (featuresCount > 1) {
            int[] next;
            if (featuresCount > 2) {
                next = Arrays.copyOf(SFSResult.sfsFeature, SFSResult.sfsFeature.length);
            } else {
                next = Arrays.copyOf(FisherResult.indexes, FisherResult.indexes.length);
            }
            int[] tab = new int[next.length + 1];

            double tempSFS = SFSResult.value;

            for (int i = 0; i < next.length; i++) {
                tab[i] = next[i];
            }
            for (int j = 0; j < acerObjectClass.getAverageMatrix().getRowDimension(); j++) {
                if (tab[featuresCount - 1] == j) {
                    continue;
                }
                tab[featuresCount - 1] = j;
                int[] copyTab = Arrays.copyOf(tab,tab.length);
                fisherTemp = calculateFisher(tab, acerObjectClass, quercusObjectClass);
                if (fisherTemp > tempSFS) {
                    SFSResultMap.put(j, new SFS(copyTab, fisherTemp));
                    tempSFS = fisherTemp;
                    SFSResult.sfsFeature = copyTab;
                }
            }
            fisherTemp = 0;
        }
    }

    public static class FisherResult {
        public static int[] indexes;
        public static double value;

        public FisherResult() {
            value = 0;
            indexes[0] = 0;
        }

        public static String getFisherResults(int featureCount) {
            String resultPrintout = "Fisher for " + featureCount + " features\n";
            for (int i : indexes) {
                resultPrintout += "Feature number: " + i + ",\n";
            }
            resultPrintout += "Value: " + value;
            return resultPrintout;
        }

        public static int[] getValues() {
            return indexes;
        }
    }

    public static class SFSResult {
        public static double value;
        public static int[] sfsFeature;

        public SFSResult() {
            value = FisherResult.value;
            sfsFeature = FisherResult.indexes;
        }

        public static String getSfsResult(int featureCount) {
            String resultPrintout = "SFS for " + featureCount + " features\n";
            for (int i : sfsFeature) {
                resultPrintout += "Features number: " + i + ",\n";
            }
            resultPrintout += "Value: " + value;
            return resultPrintout;
        }
    }

    public class SFS {
        public int[] tab;
        public double value;

        public SFS(int[] tab, double value) {
            this.tab = tab;
            this.value = value;
        }

        public int[] getTab() {
            return tab;
        }

        public void setTab(int[] tab) {
            this.tab = tab;
        }

        public double getValue() {
            return value;
        }

        public void setValue(double value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "SFS{" +
                    "tab=" + Arrays.toString(tab) +
                    ", value=" + value +
                    '}';
        }
    }
}
