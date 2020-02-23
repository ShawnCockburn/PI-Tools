package com.shawncockburn.PITools.uiComponents;


import com.jfoenix.controls.JFXButton;
import javafx.scene.control.Button;

public class ButtonComponent {

    private static final String BUTTON_STYLE_ACTIVE = "custom-jfx-button-active";
    private static final String BUTTON_STYLE_INACTIVE = "custom-jfx-button-inactive";

    public static enum BUTTON_STYLE{
        ACTIVE,
        INACTIVE,
        DISABLED
    }

    public static void setButtonStyle(Button button,BUTTON_STYLE style){
        switch (style){
            case ACTIVE:
                updateStyle(button, BUTTON_STYLE_ACTIVE, false);
                break;
            case INACTIVE:
                updateStyle(button, BUTTON_STYLE_INACTIVE, false);
                break;
            case DISABLED:
                updateStyle(button, BUTTON_STYLE_INACTIVE, true);
                break;
            default:
                updateStyle(button, BUTTON_STYLE_ACTIVE, false);
        }
    }

    public static Button getButton(String innerText, JFXButton.ButtonType buttonType){
        Button button = new JFXButton(innerText);
        button.setPrefWidth(145);
        button.setPrefHeight(40);
        button.getStyleClass().add(BUTTON_STYLE_INACTIVE);
        ((JFXButton) button).setButtonType(buttonType);
        return button;
    }

    private static void updateStyle(Button button, String style, Boolean isDisabled){
        button.getStyleClass().removeAll(BUTTON_STYLE_ACTIVE, BUTTON_STYLE_INACTIVE);
        button.getStyleClass().add(style);
        button.setDisable(isDisabled);
    }

}
