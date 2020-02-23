package com.shawncockburn.PITools.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class IntroController {

    @FXML
    private Label introText;

    @FXML
    private void initialize(){
        introText.setText("Welcome to PI Tools.\nTools listed on the left nav bar.\n\n\nMade by Shawn Cockburn.");
    }
}
