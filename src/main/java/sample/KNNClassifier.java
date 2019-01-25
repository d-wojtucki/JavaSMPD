package sample;

import database.SingleObject;
import org.apache.commons.math3.analysis.function.Sin;

import java.util.*;

class KNNClassifier extends Classifier {

    KNNClassifier() {
        //trainingObjects = new ArrayList();
        //testObjects = new ArrayList();
    }

    @SuppressWarnings("unchecked")
    double classifyTestObjects(int k) {
        ArrayList<SingleObject> acerObjects = new ArrayList();
        ArrayList<SingleObject> quercusObjects = new ArrayList();

        for (SingleObject object : testObjects) {
            classify(object, acerObjects, quercusObjects, k);
        }
        return getVerifiedPercentageResult(acerObjects, quercusObjects);
    }

    private void classify(SingleObject object, ArrayList<SingleObject> acerObjects, ArrayList<SingleObject> quercusObjects, int k) {
        Map<SingleObject, Double> mapOfClosestObject = getMapOfClosetObject(object, k);
        double closestDistance = getDistanceBetweenTwoObjects(object, trainingObjects.get(0));
        SingleObject closestObject = trainingObjects.get(0);

        for (SingleObject objectFromTrainingList : trainingObjects) {
            for (Map.Entry<SingleObject, Double> entry : mapOfClosestObject.entrySet()) {

                if (this.getDistanceBetweenTwoObjects(entry.getKey(), objectFromTrainingList) < closestDistance) {
                    closestDistance = entry.getValue();
                    closestObject = entry.getKey();
                }
            }
        }

        if (closestObject.getClassName().startsWith("Acer")) {
            acerObjects.add(object);
        }

        if (closestObject.getClassName().startsWith("Quercus")) {
            quercusObjects.add(object);
        }
    }

    private Map<SingleObject, Double> getMapOfClosetObject(SingleObject object, int k) {
        Map<SingleObject, Double> mapOfClosetObject = new HashMap<>();
        for (int i = 0; i < k; i++) {
            SingleObject closestObject = trainingObjects.get(k);
            double distanceBetween = this.getDistanceBetweenTwoObjects(object, closestObject);
            mapOfClosetObject.put(closestObject, distanceBetween);
        }
        return mapOfClosetObject;
    }
}