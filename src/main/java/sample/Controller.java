package sample;

import database.DatabaseForObjects;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;

import static sample.Calculations.SFSResultMap;

public class Controller {
    private DatabaseForObjects base = new DatabaseForObjects();
    private NNClassifier nnClassifier;
    private NMClassifier nmClassifier;
    private KNNClassifier knnClassifier;
    private int featureCount = 0;
    private String clasifierMethod;
    private int percentageValue = 80;
    private int kfeatures = 0;
    private int bootstrapValue = 3;

    @FXML
    public TextArea textArea;
    @FXML
    public TextArea textArea2;
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
    @FXML
    private ComboBox<Integer> knumbersComboBox;
    @FXML
    private TextField percentage;
    @FXML
    private TextField bootstrapInput;

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
        percentage.setText(String.valueOf(percentageValue));
        bootstrapInput.setText(String.valueOf(bootstrapValue));
        fillClasifiersComboBox();
        fillKComboBox();
        nnClassifier = new NNClassifier();
        knnClassifier = new KNNClassifier();
        nmClassifier = new NMClassifier();
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
    public void clasify() {
        if (clasifierMethod == "NN") {
            classifyNN();
        } else if (clasifierMethod == "k-NN") {
            classifyKNN(kfeatures);
        } else if (clasifierMethod == "NM") {
            classifyNM();
        }
    }

    public void setKfeatures() {
        kfeatures = knumbersComboBox.getSelectionModel().getSelectedItem();
    }

    public void classifyKNN(int k) {
        double result = knnClassifier.classifyTestObjects(k);
        System.out.println(result);
        printClassifierResults(result);
    }

    public void classifyNN() {
        double result = nnClassifier.classifyTestObjects();
        System.out.println(result);
        printClassifierResults(result);
    }

    public void classifyNM() {
        double result = nmClassifier.classifyTestObjects();
        System.out.println(result);
        printClassifierResults(result);
    }

    public void train() {
        setClassifierPercentage();
        Classifier.trainingObjects.clear();
        Classifier.testObjects.clear();
        printTrainingResults(Classifier.setTrainingAndTestObject(percentageValue));
    }

    public void computeSfs() {
        Calculations cal = new Calculations();
        //todo uwaga, nie resetuje wyników!!!
        //Calculations.resetAllFields();
        cal.calculateSFS22(featureCount);
        printSFSResults(featureCount);
    }

    public void computeFisher() {
        setFeatureCount();
        System.out.println("Calculating for " + featureCount + " features, please wait...");
        Calculations.calculateFisher(featureCount);
        printFisherResults(featureCount);
    }

    public void doBootstrap() {
        setBootstrapInput();
        if (clasifierMethod == "NN") {
            bootstrap("NN");
        } else if (clasifierMethod == "k-NN") {
            bootstrap("KNN");
        } else if (clasifierMethod == "NM") {
            bootstrap("NM");
        }
    }

    @SuppressWarnings({"unchecked","Duplicates"})
    private void bootstrap(String type) {
        double result = 0.0D;
        ArrayList<Double> resultsList = new ArrayList();
        for(int i=0; i<bootstrapValue; i++) {
            //Collections.shuffle(Classifier.listOfAllObjects);
            Classifier.bootstrapTrain(100);
            double var = getSpecificClassifierExecuted(type);
            result+=var;
            resultsList.add(var);
        }
        result = result/bootstrapValue;
        printBootstrapResults(result, resultsList);
    }

    public void doCrossvalidation() {
        setBootstrapInput();
        if (clasifierMethod == "NN") {
            crossvalidate("NN");
        } else if (clasifierMethod == "k-NN") {
            crossvalidate("KNN");
        } else if (clasifierMethod == "NM") {
            crossvalidate("NM");
        }
    }

    @SuppressWarnings({"unchecked","Duplicates"})
    private void crossvalidate(String type) {
        Collections.shuffle(Classifier.listOfAllObjects);
        double result = 0.0D;
        ArrayList<Double> resultsList = new ArrayList();
        for(int i=0; i<bootstrapValue; i++) {
            Classifier.crossvalidateTrain(i, bootstrapValue);
            double var = getSpecificClassifierExecuted(type);
            result+=var;
            resultsList.add(var);
        }
        result = result/bootstrapValue;
        printCrossvalidationResults(result, resultsList);
    }

    public double getSpecificClassifierExecuted(String type) {
        double result = 0.0D;
        if(type.equals("NN"))
            result = nnClassifier.classifyTestObjects();
        if(type.equals("NM"))
            result = nmClassifier.classifyTestObjects();
        if(type.equals("KNN"))
            result = knnClassifier.classifyTestObjects(kfeatures);
        return result;
    }

    public void fillComboBox() {
        comboBox.getItems().addAll(base.getFeautersIDs());
        comboBox.getSelectionModel().selectFirst();
    }

    public void fillClasifiersComboBox() {
        clasifiersComboBox.getItems().add("NN");
        clasifiersComboBox.getItems().add("k-NN");
        clasifiersComboBox.getItems().add("NM");
        clasifiersComboBox.getItems().add("k-NN");
        clasifiersComboBox.getSelectionModel().selectFirst();
    }

    public void fillKComboBox() {
        for (int i = 1; i <= 10; i++)
            knumbersComboBox.getItems().add(i);

        knumbersComboBox.getSelectionModel().selectFirst();
    }

    public void setClasifierMethod() {
        clasifierMethod = clasifiersComboBox.getSelectionModel().getSelectedItem();
    }

    public void setClassifierPercentage() {
        percentageValue = Integer.parseInt(percentage.getCharacters().toString());
    }

    public void setBootstrapInput() { bootstrapValue = Integer.parseInt(bootstrapInput.getCharacters().toString()); }

    public void setFeatureCount() {
        featureCount = comboBox.getSelectionModel().getSelectedItem();
    }

    public void printFisherResults(int featureCount) {
        textArea.setText(Calculations.FisherResult.getFisherResults(featureCount));
    }

    public void printClassifierResults(double result) {
        DecimalFormat df = new DecimalFormat("#.##");
        textArea2.setText("Classifier selected: " + clasifierMethod +
                "\nPercentage of samples in \ntraining set: " + percentageValue + "%" +
                "\nResult: " + df.format(result) + "% of samples \nclassified correctly.");
    }

    public void printBootstrapResults(double result, ArrayList<Double> resultsList) {
        DecimalFormat df = new DecimalFormat("#.##");
        textArea2.setText("Classifier selected: " + clasifierMethod +
                "\nBootstrap method, k=" + bootstrapValue +
                "\nPercentage of samples in \ntraining set: " + percentageValue + "%" +
                "\nResult: " + df.format(result) + "% of samples \nclassified correctly."+
                "\n\nResults table: " + getResultsTablePrintout(resultsList));
    }

    public void printCrossvalidationResults(double result, ArrayList<Double> resultsList) {
        DecimalFormat df = new DecimalFormat("#.##");
        textArea2.setText("Classifier selected: " + clasifierMethod +
                "\nCrossvalidation method, k=" + bootstrapValue +
                "\nPercentage of samples in \ntraining set: " + percentageValue + "%" +
                "\nResult: " + df.format(result) + "% of samples \nclassified correctly."+
                "\n\nResults table: " + getResultsTablePrintout(resultsList));
    }

    public String getResultsTablePrintout(ArrayList<Double> resultsList) {
        String answerPrintout="[ ";
        DecimalFormat df = new DecimalFormat("#.##");
        for(Double result : resultsList) answerPrintout+=df.format(result) + " ";
        answerPrintout+="]";
        return answerPrintout;
    }

    public void printTrainingResults(String trainingResults) {
        textArea2.setText(trainingResults);
    }

    public void printSFSResults(int featureCount) {
        for (Calculations.SFS a : SFSResultMap) {
            System.out.println(a.toString());
        }
        textArea.setText(Calculations.SFSResult.getSfsResult(featureCount));
        SFSResultMap.clear();
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
