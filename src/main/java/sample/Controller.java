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

public class Controller {

    @FXML
    public TextArea textArea;
    private DatabaseForObjects base = new DatabaseForObjects();
    private int featureCount = 0;
    @FXML
    private Node border;
    @FXML
    private RadioButton fisher;
    @FXML
    private RadioButton sfs;
    @FXML
    private ComboBox<Integer> comboBox;

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

    public void computeSfs() {
        Calculations.calculateSFS(featureCount);
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

    public void setFeatureCount() {
        featureCount = comboBox.getSelectionModel().getSelectedItem();
    }

    public void printFisherResults(int featureCount) {
        textArea.setText(Calculations.FisherResult.getFisherResults(featureCount));
    }

    public void printSFSResults(int featureCount) {
        textArea.setText(Calculations.SFSResult.getSfsResult(featureCount));
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
