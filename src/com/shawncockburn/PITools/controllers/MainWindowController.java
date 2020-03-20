package com.shawncockburn.PITools.controllers;

import animatefx.animation.FadeIn;
import animatefx.animation.FadeOut;
import com.jfoenix.controls.JFXButton;
import com.shawncockburn.PITools.uiComponents.ButtonComponent;
import com.shawncockburn.PITools.util.AlertWindowHelper;
import com.shawncockburn.PITools.util.ViewLoader;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MainWindowController {

    private Map<String,Button> navButtonMap = new HashMap<>();
    private Button currentSelectedButton;
    private WorkCompletableController currentLoadedController;

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
        addNavButton("Image Data", ViewLoader.VIEW.IMAGE_DATA_IMPORT);
    }

    //this adds a button to the nav bar with a onclick event handler for the targeted ViewLoader.VIEW
    private Button addNavButton(String innerText, ViewLoader.VIEW view){
        Button button = ButtonComponent.getButton(innerText, JFXButton.ButtonType.FLAT);
        EventHandler<ActionEvent> eventHandler = event -> {
            if (currentLoadedController != null){
                if (currentLoadedController.workIsComplete()){
                    handleButtonClicked(button, view);
                } else {
                    Optional<ButtonType> continueWork = AlertWindowHelper
                            .setupDefaultAlert(
                                    Alert.AlertType.CONFIRMATION,
                                    "Work Not Finished",
                                    "The currently tool has unfinished work",
                                    "Are you sure you want to exit without completing/saving your current work?")
                            .showAndWait();
                    if (continueWork.get() == ButtonType.OK){
                        handleButtonClicked(button, view);
                    }
                }
            } else {
                handleButtonClicked(button, view);
            }
        };
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

        if (apView.getChildren().size() != 0){
            new FadeOut(apView).setSpeed(0.7).play();
            apView.getChildren().clear();
        }
        if (pane != null){
            apView.getChildren().add(pane);
            new FadeIn(apView).setSpeed(0.7).play();
        } else {
            return false;
        }
        Object loadedController = viewLoader.getLoadedController(pane);
        if (isExtendedFromWorkCompletableController(loadedController.getClass())){
            setCurrentLoadedController((WorkCompletableController) loadedController);
        } else {
            setCurrentLoadedController(null);
        }
        return true;
    }

    private Boolean isExtendedFromWorkCompletableController(Class cls){
        return cls != WorkCompletableController.class && WorkCompletableController.class.isAssignableFrom(cls);
    }

    private void setCurrentLoadedController(WorkCompletableController controller){
        currentLoadedController = controller;
    }

    private void handleButtonClicked(Button clickedButton, ViewLoader.VIEW view){
        if (loadAPView(view)){
            ButtonComponent.setButtonStyle(currentSelectedButton, ButtonComponent.BUTTON_STYLE.INACTIVE);
            currentSelectedButton = clickedButton;
            FadeOut fadeOut = new FadeOut(currentSelectedButton);
            fadeOut.setSpeed(5);
            fadeOut.setOnFinished(event -> ButtonComponent.setButtonStyle(currentSelectedButton, ButtonComponent.BUTTON_STYLE.ACTIVE));
            fadeOut.play();
            new FadeIn(currentSelectedButton).setDelay(Duration.millis(150)).setSpeed(2).play();
        }
    }

    public Map<String, Button> getNavButtonMap() {
        return navButtonMap;
    }

}
