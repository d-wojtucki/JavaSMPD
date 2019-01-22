package sample;

import database.SingleObject;

import java.util.ArrayList;
import java.util.List;

public class NMClassifier extends Classifier {

    private SingleObject averageAcer;
    private SingleObject averageQuercus;

    @SuppressWarnings("unchecked")
    public NMClassifier() {
        trainingObjects = new ArrayList();
        testObjects = new ArrayList();
    }

    @SuppressWarnings("unchecked")
    public double classifyTestObjects() {
        ArrayList<SingleObject> acerObjects = new ArrayList();
        ArrayList<SingleObject> quercusObjects = new ArrayList();

        getAverageOfObjectFeatures();
        for (SingleObject object : testObjects) {
            classify(object, acerObjects, quercusObjects);
        }
        System.out.println(acerObjects.size());
        System.out.println(quercusObjects.size());
        return getVerifiedPercentageResult(acerObjects, quercusObjects);
    }

    private void classify(SingleObject object, ArrayList<SingleObject> acerObjects, ArrayList<SingleObject> quercusObjects) {
        double distanceBetweenAcerAverage = getDistanceBetweenTwoObjects(object, averageAcer);
        double distanceBetweenQuercusAverage = getDistanceBetweenTwoObjects(object, averageQuercus);

        if(distanceBetweenAcerAverage < distanceBetweenQuercusAverage) acerObjects.add(object);
        else quercusObjects.add(object);
    }

    @SuppressWarnings({"unchecked","Duplicates"})
    private void getAverageOfObjectFeatures () {
        ArrayList<SingleObject> acerTrainingObjects = new ArrayList();
        ArrayList<SingleObject> quercusTrainingObjects = new ArrayList();
        List<Double> acerAverageFeatures = new ArrayList();
        List<Double> quercusAverageFeatures = new ArrayList();

        for(SingleObject object : trainingObjects) {
            if(object.getClassName().startsWith("Acer")) {
                acerTrainingObjects.add(object);
            }
            else if(object.getClassName().startsWith("Quercus")) {
                quercusTrainingObjects.add(object);
            }
        }

        if(!acerTrainingObjects.isEmpty()) {
            for(int i=0;i<64;i++) {
                double featureSumValue = 0.0D;
                for(SingleObject object : acerTrainingObjects) {
                    featureSumValue = featureSumValue + object.getFeatures().get(i);
                }
                acerAverageFeatures.add(featureSumValue/64);
            }
        }
        if(!quercusTrainingObjects.isEmpty()) {
            for(int i=0;i<64;i++) {
                double featureSumValue = 0.0D;
                for(SingleObject object : quercusTrainingObjects) {
                    featureSumValue = featureSumValue + object.getFeatures().get(i);
                }
                quercusAverageFeatures.add(featureSumValue/64);
            }
        }
        averageAcer = new SingleObject("Acer", acerAverageFeatures);
        averageQuercus = new SingleObject("Quercus", quercusAverageFeatures);
    }
}
