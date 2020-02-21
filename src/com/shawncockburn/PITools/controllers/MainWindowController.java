package com.shawncockburn.PITools.controllers;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.HashMap;
import java.util.Map;

public class MainWindowController {

    private Map<String,Button> navButtonMap = new HashMap<>();

    @FXML
    public Pane btnPane;

    @FXML
    public Pane btnIconPane;

    @FXML
    public AnchorPane anchorPane;

    @FXML
    private AnchorPane apView;

    @FXML
    private VBox btnVbox;

    @FXML
    private void initialize(){

    }

    private Button addNavButton(String innerText, EventHandler<ActionEvent> eventHandler){
        Button button = new JFXButton(innerText);
        button.setPrefWidth(145);
        button.setPrefHeight(40);
        button.getStyleClass().add("custom-jfx-button-menu");
        ((JFXButton) button).setButtonType(JFXButton.ButtonType.FLAT);
        button.setOnAction(eventHandler);
        getNavButtonMap().put(innerText, button);
        btnVbox.getChildren().add(button);
        return button;
    }

    public Map<String, Button> getNavButtonMap() {
        return navButtonMap;
    }

}
