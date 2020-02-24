package com.shawncockburn.PITools;

import com.shawncockburn.PITools.data.Data;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("resources/fxml/mainWindow.fxml"));
        primaryStage.setTitle("PI Tools");
        primaryStage.setScene(new Scene(root, 890, 590));
        primaryStage.setResizable(false);
        primaryStage.setFullScreen(false);
        primaryStage.show();

    }


    public static void main(String[] args) {
        Boolean dataExists = true;
        try {
            Data.checkDataExists();
        } catch (Exception e) {
            e.printStackTrace();
            dataExists = false;
        }
        if (dataExists) {
            launch(args);
        }
    }
}
