package sample;

import database.DatabaseForObjects;
import database.SingleObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.Collections.shuffle;

public abstract class Classifier {
    static ArrayList<SingleObject> trainingObjects;
    static ArrayList<SingleObject> testObjects;
    public static ArrayList<SingleObject> listOfAllObjects;
    int percentage;

    public Classifier() {
        listOfAllObjects = DatabaseForObjects.singleObjects;
    }

    double getDistanceBetweenTwoObjects(SingleObject object1, SingleObject object2) {
        double sumOfPoweredSubtractions = 0.0D;
        List<Double> object1Features = object1.getFeatures();
        List<Double> object2Features = object2.getFeatures();

        for (int i = 0; i < object1.getFeaturesNumber(); ++i) {
            sumOfPoweredSubtractions += Math.pow(object2Features.get(i) - object1Features.get(i), 2.0D);
        }

        return Math.sqrt(sumOfPoweredSubtractions);
    }

    double getVerifiedPercentageResult(ArrayList<SingleObject> acerObjects, ArrayList<SingleObject> quercusObjects) {
        int counter = 0;
        int testObjectsAmount = acerObjects.size() + quercusObjects.size();
        for (SingleObject object : acerObjects) {
            if (object.getClassName().startsWith("Acer")) {
                ++counter;
            }
        }

        for (SingleObject object : quercusObjects) {
            if (object.getClassName().startsWith("Quercus")) {
                ++counter;
            }
        }

        double xxx = (double) counter / (double) testObjectsAmount;
        double result = xxx * 100.0D;
        return result;
    }

    static String train(int percentage) {
        double objectQuantity = (double) listOfAllObjects.size();
        double countOfElementsToTrainingObjects = objectQuantity * ((double) percentage / 100.0D);
        shuffle(listOfAllObjects);
        for (SingleObject object : listOfAllObjects) {
            if ((double) trainingObjects.size() < countOfElementsToTrainingObjects) {
                trainingObjects.add(object);
            } else {
                testObjects.add(object);
            }
        }

        return ("TrainingObjects amount: " + trainingObjects.size() + ".\nTestObjects amount: " + testObjects.size());

    }

    static void bootstrapTrain(int quantity) {
        trainingObjects.clear();
        testObjects.clear();
        shuffle(listOfAllObjects);
        for(SingleObject object : listOfAllObjects) {
            if(trainingObjects.size() < quantity) trainingObjects.add(object);
            testObjects.add(object);
        }
    }

    static void crossvalidateTrain(int selectedSublist, int numberOfSublists) {
        trainingObjects.clear();
        testObjects.clear();
        int numberOfObjectsInOneSublist = Math.floorDiv(listOfAllObjects.size(),numberOfSublists);
        int currentObjectIterator=0;
        for(SingleObject object : listOfAllObjects) {
            if(currentObjectIterator >= selectedSublist*numberOfObjectsInOneSublist &&
            currentObjectIterator < (selectedSublist*numberOfObjectsInOneSublist)+numberOfObjectsInOneSublist)
                testObjects.add(object);
            else trainingObjects.add(object);
            currentObjectIterator++;
        }
    }
}