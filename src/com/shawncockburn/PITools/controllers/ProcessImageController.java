package com.shawncockburn.PITools.controllers;

import com.jfoenix.controls.JFXProgressBar;
import com.shawncockburn.PITools.uiComponents.ButtonComponent;
import javafx.fxml.FXML;
import com.jfoenix.controls.JFXButton;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;

public class ProcessImageController {


    @FXML
    public VBox vbox;

    public Label selectedImages = new Label("Selected Images");
    public Label selectedDirectory = new Label("Selected Output Directory");
    public ProgressBar pb = new JFXProgressBar();


    @FXML
    private void initialize(){
        selectedImages.setPrefHeight(50);
        selectedDirectory.setPrefHeight(50);
        pb.setPrefWidth(500);
        pb.setPadding(new Insets(20));
        Button selectImages = ButtonComponent.getButton("Select Images", JFXButton.ButtonType.FLAT);
        Button selectOutputDirectory = ButtonComponent.getButton("Select Output Directory", JFXButton.ButtonType.FLAT);
        Button convert = ButtonComponent.getButton("Convert", JFXButton.ButtonType.FLAT);
        ButtonComponent.setButtonStyle(selectImages, ButtonComponent.BUTTON_STYLE.ACTIVE);
        ButtonComponent.setButtonStyle(selectOutputDirectory, ButtonComponent.BUTTON_STYLE.DISABLED);
        ButtonComponent.setButtonStyle(convert, ButtonComponent.BUTTON_STYLE.DISABLED);
        vbox.getChildren().addAll(
                selectImages,
                selectedImages,
                selectOutputDirectory,
                selectedDirectory,
                convert,
                pb
                );
    }
}
