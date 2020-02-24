package com.shawncockburn.PITools.controllers;

import com.jfoenix.controls.JFXButton;
import com.shawncockburn.PITools.uiComponents.ButtonComponent;
import com.shawncockburn.PITools.util.ViewLoader;
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
    private Button currentSelectedButton;

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
        currentSelectedButton = addNavButton("Home", ViewLoader.VIEW.INTRO);
        currentSelectedButton.fire();
        //todo: add this function
        addNavButton("Web Convert", ViewLoader.VIEW.INTRO);
        //todo: add this function
        addNavButton("Find Images", ViewLoader.VIEW.INTRO);
        //todo: add this function
        addNavButton("Add Image Data", ViewLoader.VIEW.INTRO);
    }

    //this adds a button to the nav bar with a onclick event handler for the targeted ViewLoader.VIEW
    private Button addNavButton(String innerText, ViewLoader.VIEW view){
        Button button = ButtonComponent.getButton(innerText, JFXButton.ButtonType.FLAT);
        EventHandler<ActionEvent> eventHandler = event -> handleButtonClicked(button, view);
        button.setOnAction(eventHandler);
        getNavButtonMap().put(innerText, button);
        btnVbox.getChildren().add(button);
        return button;
    }

    //this loads the requested ViewLoader.VIEW into the apView
    private Boolean loadAPView(ViewLoader.VIEW view){
        ViewLoader viewLoader = new ViewLoader();
        Pane pane = null;

        try {pane = viewLoader.loadPane(view);}
        catch (Exception e) {e.printStackTrace();}

        if (pane != null){
            apView.getChildren().add(pane);
        } else {
            return false;
        }
        return true;
    }

    private void handleButtonClicked(Button clickedButton, ViewLoader.VIEW view){
        if (loadAPView(view)){
            ButtonComponent.setButtonStyle(currentSelectedButton, ButtonComponent.BUTTON_STYLE.INACTIVE);
            currentSelectedButton = clickedButton;
            ButtonComponent.setButtonStyle(currentSelectedButton, ButtonComponent.BUTTON_STYLE.ACTIVE);
        }
    }

    public Map<String, Button> getNavButtonMap() {
        return navButtonMap;
    }

}
