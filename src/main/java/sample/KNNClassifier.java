package sample;

import database.SingleObject;
import java.util.ArrayList;

public class KNNClassifier extends Classifier {
    public KNNClassifier(int percentageDesired) {
        trainingObjects = new ArrayList();
        testObjects = new ArrayList();
        this.percentage = percentageDesired;
        train(percentageDesired);
    }


}