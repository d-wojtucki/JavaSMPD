package sample;

import database.SingleObject;
import org.apache.commons.math3.analysis.function.Sin;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        for (SingleObject objectFromTrainingList : trainingObjects) {
            for (Map.Entry<SingleObject, Double> entry : mapOfClosestObject.entrySet()) {

                if (getDistanceBetweenTwoObjects(object, entry.getKey()) > getDistanceBetweenTwoObjects(object,objectFromTrainingList)) {
                    //closestDistance = entry.getValue();
                    //
                    //
                    //closestObject = entry.getKey();

                    mapOfClosestObject.remove(entry.getKey());
                    mapOfClosestObject.put(objectFromTrainingList,getDistanceBetweenTwoObjects(object,objectFromTrainingList));

                }
            }
            sortMap(mapOfClosestObject);
        }

        int acerCount=0;
        int quercusCount=0;

        for(Map.Entry<SingleObject, Double> entry : mapOfClosestObject.entrySet()) {
            if(entry.getKey().getClassName().startsWith("Acer")) acerCount++;
            else quercusCount++;
        }

        if (acerCount>quercusCount) {
            acerObjects.add(object);
        } else
            quercusObjects.add(object);
    }

    private Map<SingleObject, Double> sortMap(Map<SingleObject, Double> map) {
        final Map<SingleObject, Double> sortedByCount = map.entrySet()
                .stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        return sortedByCount;
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