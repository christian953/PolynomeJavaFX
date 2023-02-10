package com.example.polynomgui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class PolynomialApplication extends Application {
    @Override
    public void start(final Stage stage) throws IOException {
        final FXMLLoader fxmlLoader = new FXMLLoader(PolynomialApplication.class.getResource("polynomial-view.fxml"));
        final Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Polynome");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(final String[] args) {
        launch();
    }
}