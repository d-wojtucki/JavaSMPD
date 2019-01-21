package sample;

import database.DatabaseForObjects;
import database.SingleObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Classifier {
    public static ArrayList<SingleObject> trainingObjects;
    public static ArrayList<SingleObject> testObjects;
    private static ArrayList<SingleObject> listOfAllObjects;
    public int percentage;

    public Classifier() {
        listOfAllObjects = DatabaseForObjects.singleObjects;
    }

    protected double getDistanceBetweenTwoObjects(SingleObject object1, SingleObject object2) {
        double sumOfPoweredSubtractions = 0.0D;
        List<Double> object1Features = object1.getFeatures();
        List<Double> object2Features = object2.getFeatures();

        for(int i = 0; i < object1.getFeaturesNumber(); ++i) {
            sumOfPoweredSubtractions += Math.pow((Double)object2Features.get(i) - (Double)object1Features.get(i), 2.0D);
        }

        return Math.sqrt(sumOfPoweredSubtractions);
    }

    protected double verifyPercentage(ArrayList<SingleObject> acerObjects, ArrayList<SingleObject> quercusObjects) {
        int counter = 0;
        int testObjectsAmount = acerObjects.size() + quercusObjects.size();
        for(SingleObject object : acerObjects) {
            if (object.getClassName().startsWith("Acer")) {
                ++counter;
            }
        }

       for(SingleObject object : quercusObjects) {
            if (object.getClassName().startsWith("Quercus")) {
                ++counter;
            }
        }

        double xxx = (double)counter / (double)testObjectsAmount;
        double result = xxx * 100.0D;
        return result;
    }

    public static String train(int percentage) {
        double objectQuantity = (double)listOfAllObjects.size();
        double countOfElementsToTrainingObjects = objectQuantity * ((double)percentage / 100.0D);
        Collections.shuffle(listOfAllObjects);
        for(SingleObject object : listOfAllObjects) {
            if ((double)trainingObjects.size() < countOfElementsToTrainingObjects) {
                trainingObjects.add(object);
            } else {
                testObjects.add(object);
            }
        }

        return ("TrainingObjects amount: " + trainingObjects.size() + ".\nTestObjects amount: " + testObjects.size());

    }
}