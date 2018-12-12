package database;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Vector;
import java.util.stream.Stream;

public class DatabaseForObjects {
    private Vector<SingleObject> singleObjects;
    private Map<String, Integer> classCounters;
    private Vector<String> classNameVector;
    private Vector<Integer> featuresIDs;
    private File file;

    private int noClass;
    private int noObjects;
    private int noFeatures;

    public DatabaseForObjects() {
        this.noClass = 0;
        this.noObjects = 0;
        this.noFeatures = 0;
        this.singleObjects = new Vector<>();
        this.classCounters = new HashMap<>();
        this.classNameVector = new Vector<>();
        this.featuresIDs = new Vector<>();
    }

    public boolean addObject(SingleObject object) {
        if (noFeatures == 0) {
            noFeatures = object.getFeaturesNumber();
        }else if(noFeatures != object.getFeaturesNumber()){
            return false;
        }
        singleObjects.add(object);
        noObjects++;
        //todo możliwie usunąć
        if(!classCounters.containsKey(object.getClassName())){
            classNameVector.add(object.getClassName());
        }
        return true;
    }
    public boolean loadToDatabase(String filename){
        //todo funkcja czyszcząca bazę przy ponownym załadowaniu dla ciągłości programu
        String data = filename;
        try(Stream<String> stream = Files.lines(Paths.get(filename))){
            stream.forEach(System.out::println);
            System.out.println();
        }catch(IOException e){
            e.printStackTrace();
            System.out.println("Cannot open the file");
        }

//        System.out.println(data);
//        String split [] = data.split(",");
//        System.out.println(split[0]);
        return true;
    }

    public Vector<SingleObject> getSingleObjects() {
        return singleObjects;
    }

    public Map<String, Integer> getClassCounters() {
        return classCounters;
    }

    public Vector<String> getClassNameVector() {
        return classNameVector;
    }

    public Vector<Integer> getFeautersIDs() {
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
