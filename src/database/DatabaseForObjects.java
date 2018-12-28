package database;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DatabaseForObjects {
    private List<SingleObject> singleObjects;
    private Map<String, Integer> classCounters;
    private List<String> classNameList;
    private List<Integer> featuresIDs;
    private List<List<Float>> featuresList;

    private int noClass;
    private int noObjects;
    private int noFeatures;

    public DatabaseForObjects() {
        this.noClass = 0;
        this.noObjects = 0;
        this.noFeatures = 0;
        this.singleObjects = new ArrayList<>();
        this.classCounters = new HashMap<>();
        this.classNameList = new ArrayList<>();
        this.featuresIDs = new Vector<>();
        this.featuresList = new ArrayList<>();
    }

    public boolean addObject(SingleObject object) {
        if (noFeatures == 0) {
            noFeatures = object.getFeaturesNumber();
        } else if (noFeatures != object.getFeaturesNumber()) {
            return false;
        }
        singleObjects.add(object);
        noObjects++;
        //todo możliwie usunąć
        if (!classCounters.containsKey(object.getClassName())) {
            classNameList.add(object.getClassName());
        }
        return true;
    }

    public boolean loadToDatabase(String filename) throws IOException {
        //todo funkcja czyszcząca bazę przy ponownym załadowaniu dla ciągłości programu
        Stream<String> stream = Files.lines(Paths.get(filename));
        String[] data = stream.toArray(String[]::new);
        parseFistFileLineToGetFeaturesID(data);

        noFeatures = featuresIDs.size();
        noObjects = data.length - 1;

        String[] test = data.clone();

        for (int i = 1; i < data.length; i++) {
            data[i] = data[i].substring(0, data[i].indexOf(","));
            classNameList.add(data[i]);
            test[i] = test[i].substring(test[i].indexOf(",") + 1);
        }
        convertStringArrayToFloatList(test);
        for (int j = 0; j < featuresList.size(); j++) {
            System.out.println(featuresList.get(j).size() + " " + featuresList.size());
            singleObjects.add(new SingleObject(classNameList.get(j), featuresList.get(j)));
        }
        System.out.println(singleObjects.get(1).toString());
        System.out.println(classNameList.size());
        return true;
    }

    public void parseFistFileLineToGetFeaturesID(String[] data) {
        String features = data[0].substring(3).replaceAll(" ", "");
        featuresIDs = Arrays.stream(features.split(",")).map(Integer::parseInt).collect(Collectors.toList());
    }

    public void convertStringArrayToFloatList(String[] tab) {
        for (int i = 1; i < tab.length; i++) {
            List<Float> floatList = Arrays.stream(tab[i].split(",")).map(Float::parseFloat).collect(Collectors.toList());
            featuresList.add(floatList);
        }
    }

    public List<SingleObject> getSingleObjects() {
        return singleObjects;
    }

    public Map<String, Integer> getClassCounters() {
        return classCounters;
    }

    public Set<String> getClassNames() {
        return new HashSet<>(classNameList);
    }

    public List<Integer> getFeautersIDs() {
        return featuresIDs;
    }

    public int getNoClass() {
        return noClass;
    }

    public int getNoObjects() {
        return noObjects;
    }

    public int getNoFeatures() {
        return noFeatures;
    }
}
