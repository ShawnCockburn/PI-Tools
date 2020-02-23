package com.shawncockburn.PITools.util;

import com.shawncockburn.PITools.controllers.IntroController;
import com.shawncockburn.PITools.controllers.MainWindowController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

import java.io.File;
import java.net.URL;

public class ViewLoader {

    public enum VIEW {
        INTRO,
        MAIN_WINDOW
    }

    public Pane loadPane(VIEW view) throws Exception {
        Object controller = getController(view);
        URL fxmlLocation = getFXML(view);
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlLocation);
        fxmlLoader.setController(controller);
        return fxmlLoader.load();
    }

    private Object getController(VIEW view) throws Exception {
        switch (view) {
            case INTRO :
                return new IntroController();
            case MAIN_WINDOW:
                return new MainWindowController();
            default:
                throw new Exception("No matching controller");
        }
    }
    private URL getFXML(VIEW view) throws Exception {
        switch (view) {
            case INTRO :
                return new File("src/com/shawncockburn/PITools/resources/fxml/introPane.fxml").toURI().toURL();
            case MAIN_WINDOW:
                return new File("src/com/shawncockburn/PITools/resources/fxml/mainWindow.fxml").toURI().toURL();
            default:
                throw new Exception("No matching FXML");
        }
    }
}


