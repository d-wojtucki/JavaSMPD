package sample;

import database.SingleObject;
import java.util.ArrayList;

public class NNClassifier extends Classifier {
    public NNClassifier(int percentageDesired) {
        trainingObjects = new ArrayList();
        testObjects = new ArrayList();
        this.percentage = percentageDesired;
        //TODO checking if training the classifier works
        //train(percentageDesired);
    }

    @SuppressWarnings("unchecked")
    public double classifyTestObjects() {
        ArrayList<SingleObject> acerObjects = new ArrayList();
        ArrayList<SingleObject> quercusObjects = new ArrayList();

        for(SingleObject object : testObjects) {
            classify(object, acerObjects, quercusObjects);
        }
        return verifyPercentage(acerObjects, quercusObjects);
    }

    private void classify(SingleObject object, ArrayList<SingleObject> acerObjects, ArrayList<SingleObject> quercusObjects) {
        SingleObject closestObject = (SingleObject)trainingObjects.get(0);
        double distanceBetween = this.getDistanceBetweenTwoObjects(object, closestObject);
        for(SingleObject objectFromTrainingList : trainingObjects) {
            if (this.getDistanceBetweenTwoObjects(object, objectFromTrainingList) < distanceBetween) {
                closestObject = objectFromTrainingList;
                distanceBetween = this.getDistanceBetweenTwoObjects(object, objectFromTrainingList);
            }
        }

        if (closestObject.getClassName().startsWith("Acer")) {
            acerObjects.add(object);
        }

        if (closestObject.getClassName().startsWith("Quercus")) {
            quercusObjects.add(object);
        }
    }
}