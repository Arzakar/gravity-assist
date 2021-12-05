package com.klimashin;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class ApplicationMain extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            AnchorPane root = (AnchorPane) FXMLLoader.load(ApplicationMain.class.getResource("ApplicationMain.fxml"));
            Scene scene = new Scene(root, 600, 600);

            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (NullPointerException ex) {
            ex.getStackTrace();
        }
    }
}
