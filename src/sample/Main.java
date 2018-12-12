package sample;

import database.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        //GridPane root = new GridPane();
//        root.setAlignment(Pos.CENTER);
//        root.setVgap(10);
//        root.setHgap(10);
//        Label greeting = new Label("Welcome to JavaFX");
//        greeting.setTextFill(Color.RED);
//        greeting.setFont(Font.font(("Times New Roman"),FontWeight.BOLD, 70));
//        root.getChildren().add(greeting);
        primaryStage.setTitle("SMPD");
        primaryStage.setScene(new Scene(root, 800, 500));
        primaryStage.show();
    }


    public static void main(String[] args) { launch(args);
        DatabaseForObjects base = new DatabaseForObjects();
            base.loadToDatabase("E:\\git-repos\\demo (1)\\SMPDjavaFX\\src\\database\\Maple_Oak.txt");





    }



}
