package sample;

import database.SingleObject;

import java.util.ArrayList;

class NNClassifier extends Classifier {

    @SuppressWarnings("unchecked")
    NNClassifier() {
        trainingObjects = new ArrayList();
        testObjects = new ArrayList();

        //TODO checking if training the classifier works

    }

    @SuppressWarnings("unchecked")
    double classifyTestObjects() {
        ArrayList<SingleObject> acerObjects = new ArrayList();
        ArrayList<SingleObject> quercusObjects = new ArrayList();

        for (SingleObject object : testObjects) {
            classify(object, acerObjects, quercusObjects);
        }
        return getVerifiedPercentageResult(acerObjects, quercusObjects);
    }

    private void classify(SingleObject object, ArrayList<SingleObject> acerObjects, ArrayList<SingleObject> quercusObjects) {
        SingleObject closestObject = trainingObjects.get(0);
        double distanceBetween = this.getDistanceBetweenTwoObjects(object, closestObject);
        for (SingleObject objectFromTrainingList : trainingObjects) {
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