package com.shawncockburn.PITools.util;

import com.shawncockburn.PITools.data.ImageData;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.concurrent.Service;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Modality;
import javafx.stage.StageStyle;

import java.io.PrintWriter;
import java.io.StringWriter;

public class AlertWindowHelper {

    public static Alert setupDefaultAlert(Alert.AlertType alertType, String title, String header, String content){
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.initModality(Modality.APPLICATION_MODAL);
        return alert;
    }

    public static Alert setupExceptionAlert(String title, String header, String content, Exception e){
        Alert alert = setupDefaultAlert(Alert.AlertType.ERROR, title, header, content);
        if (e != null){
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            String exceptionText = sw.toString();

            Label label = new Label("The exception stacktrace was:");

            TextArea textArea = new TextArea(exceptionText);
            textArea.setEditable(false);
            textArea.setWrapText(true);

            textArea.setMaxWidth(Double.MAX_VALUE);
            textArea.setMaxHeight(Double.MAX_VALUE);
            GridPane.setVgrow(textArea, Priority.ALWAYS);
            GridPane.setHgrow(textArea, Priority.ALWAYS);

            GridPane expContent = new GridPane();
            expContent.setMaxWidth(Double.MAX_VALUE);
            expContent.add(label, 0, 0);
            expContent.add(textArea, 0, 1);

            alert.getDialogPane().setExpandableContent(expContent);
        }

        return alert;
    }

    public static Alert setupTextAreaAlert(String title, String header, String content, String contentText){
        Alert alert = setupDefaultAlert(Alert.AlertType.ERROR, title, header, content);

            Label label = new Label("See info:");

            TextArea textArea = new TextArea(contentText);
            textArea.setEditable(false);
            textArea.setWrapText(true);

            textArea.setMaxWidth(Double.MAX_VALUE);
            textArea.setMaxHeight(Double.MAX_VALUE);
            GridPane.setVgrow(textArea, Priority.ALWAYS);
            GridPane.setHgrow(textArea, Priority.ALWAYS);

            GridPane Content = new GridPane();
            Content.setMaxWidth(Double.MAX_VALUE);
            Content.add(label, 0, 0);
            Content.add(textArea, 0, 1);

            alert.getDialogPane().setExpandableContent(Content);


        return alert;
    }

    public static Dialog<ImageData> setupImageDataInputDialog(){

        Dialog<ImageData> dialog = new Dialog<>();
        dialog.setTitle("New Product Dialog");
        dialog.setHeaderText("Fill out the fields below");

        ButtonType buttonType = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(buttonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField productCode = new TextField();
        productCode.setPromptText("Product Code");
        TextField productWebCode = new TextField();
        productWebCode.setPromptText("Product Web Code");
        TextField productName = new TextField();
        productName.setPromptText("Product Name");


        grid.add(new Label("Product Code:"), 0, 0);
        grid.add(productCode, 1, 0);
        grid.add(new Label("Product Web Code:"), 0, 1);
        grid.add(productWebCode, 1, 1);
        grid.add(new Label("Product Name:"), 0, 2);
        grid.add(productName, 1, 2);

        Node okayButton = dialog.getDialogPane().lookupButton(buttonType);
        okayButton.setDisable(true);

        ChangeListener<String> textChangeListener = (observable, oldValue, newValue) -> {
            if (productCode.getText().trim().isEmpty() || productName.getText().trim().isEmpty() || productWebCode.getText().trim().isEmpty()) {
                okayButton.setDisable(true);
            } else {
                okayButton.setDisable(false);
            }
        };

        productCode.textProperty().addListener(textChangeListener);
        productName.textProperty().addListener(textChangeListener);
        productWebCode.textProperty().addListener(textChangeListener);

        dialog.getDialogPane().setContent(grid);

        Platform.runLater(productCode::requestFocus);



        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == buttonType) {
                return new ImageData(productCode.getText(), productWebCode.getText(), productName.getText());
            }
            return null;
        });
        return dialog;
    }

    public static Dialog<ImageData> setupImageDataEditDialog(ImageData imageData){

        Dialog<ImageData> dialog = new Dialog<>();
        dialog.setTitle("New Product Dialog");
        dialog.setHeaderText("Fill out the fields below");

        ButtonType buttonType = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(buttonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField productCode = new TextField();
        productCode.setText(imageData.getProductCode());
        TextField productWebCode = new TextField();
        productWebCode.setText(imageData.getProductWebCode());
        TextField productName = new TextField();
        productName.setText(imageData.getProductName());


        grid.add(new Label("Product Code:"), 0, 0);
        grid.add(productCode, 1, 0);
        grid.add(new Label("Product Web Code:"), 0, 1);
        grid.add(productWebCode, 1, 1);
        grid.add(new Label("Product Name:"), 0, 2);
        grid.add(productName, 1, 2);

        Node okayButton = dialog.getDialogPane().lookupButton(buttonType);
        okayButton.setDisable(false);

        ChangeListener<String> textChangeListener = (observable, oldValue, newValue) -> {
            if (productCode.getText().trim().isEmpty() || productName.getText().trim().isEmpty() || productWebCode.getText().trim().isEmpty()) {
                okayButton.setDisable(true);
            } else {
                okayButton.setDisable(false);
            }
        };

        productCode.textProperty().addListener(textChangeListener);
        productName.textProperty().addListener(textChangeListener);
        productWebCode.textProperty().addListener(textChangeListener);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == buttonType) {
                return new ImageData(imageData.getId() ,productCode.getText(), productWebCode.getText(), productName.getText());
            }
            return null;
        });
        return dialog;
    }

    public static Dialog setupProgressIndicationDialog(String title, String header, Service service){

        Dialog<ImageData> dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        dialog.setWidth(400);


        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 10, 10, 10));

        Label label = new Label();
        label.setPrefWidth(400);
        label.textProperty().bind(service.messageProperty());
        ProgressBar progressBar = new ProgressBar();
        progressBar.setPrefWidth(350);
        progressBar.progressProperty().bind(service.progressProperty());
        Button button = new Button("Cancel");
        button.setAlignment(Pos.CENTER);

        grid.add(label, 0, 0);
        grid.add(progressBar, 0, 1);
        grid.add(button, 0, 2);

        dialog.getDialogPane().setContent(grid);

        button.setOnAction(event -> service.cancel());

        dialog.setOnCloseRequest(event -> service.cancel());

        dialog.initStyle(StageStyle.UNIFIED);
        return dialog;
    }


}
