package sample;

import database.DatabaseForObjects;
import database.SingleObject;
import org.apache.commons.lang3.LocaleUtils;
import org.apache.commons.math3.analysis.function.Sin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static java.util.Collections.shuffle;

public abstract class Classifier {
    static ArrayList<SingleObject> trainingObjects;
    static ArrayList<SingleObject> testObjects;
    public static ArrayList<SingleObject> listOfAllObjects;
    public static List<SingleObject> acerObjects = new ArrayList<>();
    public static List<SingleObject> quercusObjects = new ArrayList<>();
    public static int[] bestFeatures = {};
    int percentage;

    public Classifier() {
        listOfAllObjects = DatabaseForObjects.singleObjects;
    }
    public static void fillBestFeatures(int[] features){
            bestFeatures = features;
    }

    double getDistanceBetweenTwoObjects(SingleObject object1, SingleObject object2) {
        //fillBestFeatures();
        if(!Calculations.SFSResultMap.isEmpty()){
            bestFeatures = Calculations.SFSResultMap.get(0).getTab();
            Arrays.asList(bestFeatures).forEach(System.out::println);
        }
        double sumOfPoweredSubtractions = 0.0D;
        List<Double> object1Features = object1.getFeatures();
        List<Double> object2Features = object2.getFeatures();
        if(bestFeatures != null){
            for (Integer feature: bestFeatures) {
                sumOfPoweredSubtractions += Math.pow(object2Features.get(feature) - object1Features.get(feature), 2.0D);
            }
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

    static String setTrainingAndTestObject(int percentage) {
        splitClasses();
        shuffle(acerObjects);
        shuffle(quercusObjects);
        trainingObjects.clear();
        testObjects.clear();
        int newAcerQuantity = percentage * acerObjects.size() / 100;
        int newQuercusQuantity = percentage * quercusObjects.size() / 100;

        for (int i = 0; i < newAcerQuantity; i++) {
            trainingObjects.add(acerObjects.get(i));
        }
        for (int i = 0; i < newQuercusQuantity; i++) {
            trainingObjects.add(quercusObjects.get(i));
        }
        for (int i = newAcerQuantity; i < acerObjects.size(); i++) {
            testObjects.add(acerObjects.get(i));
        }
        for (int i = newQuercusQuantity; i < quercusObjects.size(); i++) {
            testObjects.add(quercusObjects.get(i));
        }
        shuffle(trainingObjects);
        return ("TrainingObjects amount: " + trainingObjects.size() + ".\nTestObjects amount: " + testObjects.size());
    }
    private static void splitClasses(){
        acerObjects.clear();
        quercusObjects.clear();
        for(SingleObject object: listOfAllObjects){
            if(object.getClassName().contains("Acer")){
                acerObjects.add(object);
            }else{
                quercusObjects.add(object);
            }
        }
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