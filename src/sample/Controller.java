package sample;

import javafx.fxml.FXML;

import java.awt.*;

public class Controller {
    @FXML
    private TextField name;

    public void onButtonClicked(){
        System.out.println("Hello "+ name);
    }

}
