package com.shawncockburn.PITools.util;

import com.shawncockburn.PITools.controllers.ImageDataImportController;
import com.shawncockburn.PITools.controllers.IntroController;
import com.shawncockburn.PITools.controllers.MainWindowController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ViewLoader {

    public enum VIEW {
        INTRO,
        MAIN_WINDOW,
        IMAGE_DATA_IMPORT
    }
    private Map<Pane, Object> loadedControllers = new HashMap<>();

    public Pane loadPane(VIEW view) throws Exception {
        Object controller = getController(view);
        URL fxmlLocation = getFXML(view);
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlLocation);
        fxmlLoader.setController(controller);
        Pane pane = fxmlLoader.load();
        loadedControllers.put(pane, controller);
        return pane;
    }

    public Object getLoadedController(Pane pane) {
        return loadedControllers.get(pane);
    }

    private Object getController(VIEW view) throws Exception {
        switch (view) {
            case INTRO :
                return new IntroController();
            case MAIN_WINDOW:
                return new MainWindowController();
            case IMAGE_DATA_IMPORT:
                return new ImageDataImportController();
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
            case IMAGE_DATA_IMPORT:
                return new File("src/com/shawncockburn/PITools/resources/fxml/imageDataImportPane.fxml").toURI().toURL();
            default:
                throw new Exception("No matching FXML");
        }
    }
}


