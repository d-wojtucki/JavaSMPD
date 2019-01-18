package sample;

import java.util.ArrayList;
import java.util.List;

public class Clasifiers {
    public static List<String> clasifiersList = new ArrayList<>();

    public static void fillClassifiersList(){
        clasifiersList.add("NN");
        clasifiersList.add("k-NN");
        clasifiersList.add("NM");
    }

    public static List<String> getClasifiersList() {
        return clasifiersList;
    }
}
