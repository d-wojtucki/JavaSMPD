package sample;

import database.DatabaseForObjects;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.stage.FileChooser;

import java.awt.*;
import java.io.File;

public class Controller {
    @FXML
    private TextField name;
    @FXML
    private Node border;
    @FXML
    private Button closebutton;

    public void onButtonClicked(){
        System.out.println("Hello "+ name);
    }
    public void openFile(){
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Open file");
                File returnValue = fileChooser.showOpenDialog(border.getScene().getWindow());
                String value = returnValue.toString();
                DatabaseForObjects base = new DatabaseForObjects();
                base.loadToDatabase(value);
            }
     public void closeApplication(){
         Platform.exit();
         System.exit(0);

     }
}
