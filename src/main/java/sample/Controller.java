package sample;

import database.DatabaseForObjects;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import static sample.Calculations.SFSResultMap;

public class Controller {

    @FXML
    public TextArea textArea;
    @FXML
    public TextArea textArea2;
    private DatabaseForObjects base = new DatabaseForObjects();
    private int featureCount = 0;
    private String clasifierMethod;
    @FXML
    private Node border;
    @FXML
    private RadioButton fisher;
    @FXML
    private RadioButton sfs;
    @FXML
    private ComboBox<Integer> comboBox;
    @FXML
    private ComboBox<String> clasifiersComboBox;

    public void openFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open file");
        File returnValue = fileChooser.showOpenDialog(border.getScene().getWindow());
        String value = returnValue.toString();
        try {
            base.loadToDatabase(value);
        } catch (IOException e) {
            e.printStackTrace();
        }
        fillComboBox();
        Clasifiers.fillClassifiersList();
        fillClasifiersComboBox();
    }

    public void selectSfs() {
        sfs.setSelected(true);
        fisher.setSelected(false);
    }

    public void selectFisher() {
        fisher.setSelected(true);
        sfs.setSelected(false);
    }

    public void compute() {
        if (fisher.isSelected()) {
            computeFisher();
        } else if (sfs.isSelected()) {
            computeSfs();
        }
    }
    //todo wypełnij napisanymi metodami
    public void clasify(){
        if(clasifierMethod == "NN"){

        }else if(clasifierMethod == "k-NN"){

        }else if(clasifierMethod == "NM"){

        }
    }

    public void computeSfs() {
        Calculations cal = new Calculations();
        cal.calculateSFS22(featureCount);
        printSFSResults(featureCount);
    }

    public void computeFisher() {
        setFeatureCount();
        System.out.println("Calculating for " + featureCount + " features, please wait...");
        Calculations.calculateFisher(featureCount);
        printFisherResults(featureCount);
    }

    public void fillComboBox() {
        comboBox.getItems().addAll(base.getFeautersIDs());
    }

    public void fillClasifiersComboBox() {
        clasifiersComboBox.getItems().addAll(Clasifiers.getClasifiersList());
    }

    public void setClasifierMethod() {
        clasifierMethod = clasifiersComboBox.getSelectionModel().getSelectedItem();
    }

    public void setFeatureCount() {
        featureCount = comboBox.getSelectionModel().getSelectedItem();
    }

    public void printFisherResults(int featureCount) {
        textArea.setText(Calculations.FisherResult.getFisherResults(featureCount));
    }

    public void printSFSResults(int featureCount) {
        //textArea.setText(Calculations.SFSResult.getSfsResult(featureCount));
        for (Calculations.SFS a : SFSResultMap) {
            System.out.println(a.toString());
        }
        textArea.setText(Calculations.SFSResult.getSfsResult(featureCount));
        SFSResultMap.clear();
        Calculations.resetAllFields();
    }

    public void closeApplication() {
        Platform.exit();
        System.exit(0);
    }

    public void printText() {

        textArea.setText("featuresIDs " + base.getFeautersIDs() + "\n" +
                "featuresNo " + base.getNoFeatures() + "\n" +
                "objectsNo " + base.getNoObjects() + "\n" +
                "namesOfClass " + base.getClassNames() + "\n" +
                "numberOfClass " + base.getClassNames().size() + "\n" +
                "first object created " + base.getSingleObjects().get(1).toString() + "\n" +
                "second object created " + base.getSingleObjects().get(70).toString());
    }
}
