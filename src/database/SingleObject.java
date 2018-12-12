package database;

import java.util.Vector;

public class SingleObject {
    private int classId;
    private String className;
    private Vector<Float> features;

    public SingleObject(int classId, String className, Vector<Float> features) {
        this.classId = classId;
        this.className = className;
        this.features = new Vector<>();
    }

    public int getClassId() {
        return classId;
    }

    public String getClassName() {
        return className;
    }

    public int getFeaturesNumber() {
        return features.size();
    }
    public Vector<Float> getFeatures(){
        return features;
    }
}


