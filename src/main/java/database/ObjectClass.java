package database;

import Jama.Matrix;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;

public class ObjectClass {

    public String className;
    public ArrayList<SingleObject> objectList = new ArrayList();

    private Matrix featureMatrix;
    private Matrix averageMatrix;

    public ObjectClass(String className) {
        this.className = className;
    }

    public void calculateMatrixes() {
        ArrayList<double[]> featuresList = new ArrayList<double[]>();

        for (SingleObject object : objectList) {
            Double[] features = new Double[object.getFeaturesNumber()];
            features = object.getFeatures().toArray(features);

            double [] featuresInPrimitive = ArrayUtils.toPrimitive(features);
            featuresList.add(featuresInPrimitive);
        }
        this.featureMatrix = createFeatureMatrix(featuresList);
        this.averageMatrix = calculateAverageMatrix(this.featureMatrix);
    }

    private Matrix createFeatureMatrix(ArrayList<double[]> featuresList) {
        double[][] arrayOfFeatures = new double[featuresList.size()][featuresList.get(0).length];
        for (int i=0; i<featuresList.size(); i++) {
            arrayOfFeatures[i] = featuresList.get(i);
        }
        return new Matrix(arrayOfFeatures);
    }

    private Matrix calculateAverageMatrix(Matrix matrix) {
        double[][] doubleArray = new double[matrix.getColumnDimension()][1];
        for (int i = 0; i < matrix.getColumnDimension(); i++) {
            doubleArray[i][0] = calculateAvg(matrix.transpose().getArray()[i]);
        }
        return new Matrix(doubleArray);
    }

    private double calculateAvg(double[] doubleArray) {
        double average = 0;
        if (doubleArray != null) {
            for (double value : doubleArray) average += value;
            if (average != 0) average /= doubleArray.length;
        }
        return average;
    }

    public String getClassName() {
        return this.className;
    }

    public Matrix getFeatureMatrix() {
        return featureMatrix;
    }

    public Matrix getAverageMatrix() {
        return averageMatrix;
    }
}
