package com.shawncockburn.PITools.controllers;

import com.jfoenix.controls.JFXButton;
import com.shawncockburn.PITools.data.ImageData;
import com.shawncockburn.PITools.uiComponents.ButtonComponent;
import com.shawncockburn.PITools.util.AlertWindowHelper;
import com.shawncockburn.PITools.util.SQLiteUtil;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;

import java.sql.SQLException;
import java.util.*;

public class ImageDataImportController extends WorkCompletableController {

    private List<ImageData> insertImageDataList = new ArrayList<>();
    private List<ImageData> updateImageDataList = new ArrayList<>();
    private List<ImageData> deleteImageDataList = new ArrayList<>();

    private SimpleBooleanProperty dataChanges = new SimpleBooleanProperty();
    private ObservableList<ImageData> tableData = FXCollections.observableArrayList();

    private enum BUTTON_FUNCTION{
        ADD,
        EDIT,
        DELETE,
        COMMIT
    }

    @FXML
    private TableView tableView;

    @FXML
    private HBox buttonBar;

    @FXML
    private void initialize(){
        getSimpleBooleanProperty().bind(dataChanges.not());
        setupController(false);
    }

    private void setupController(Boolean isReload){
        if (isReload){
            dataChanges.setValue(false);
            setTableDataDatabaseData();
        } else {
            dataChanges.setValue(false);
            addDefaultButtons();
            setupTableView();
        }
        tableData.addListener((ListChangeListener<ImageData>) c -> {
            if (deleteImageDataList.size() != 0 || insertImageDataList.size() != 0 || updateImageDataList.size() != 0) {
                dataChanges.setValue(true);
            } else {
                dataChanges.setValue(false);
            }
        });
    }

    private void addDefaultButtons(){
        Button addButton = ButtonComponent.getButton("Add Image Data", JFXButton.ButtonType.FLAT);
        Button editButton = ButtonComponent.getButton("Edit Selected", JFXButton.ButtonType.FLAT);
        Button deleteButton = ButtonComponent.getButton("Delete Selected", JFXButton.ButtonType.FLAT);
        Button commitButton = ButtonComponent.getButton("Commit New Data", JFXButton.ButtonType.FLAT);

        ButtonComponent.setButtonStyle(addButton, ButtonComponent.BUTTON_STYLE.ACTIVE);
        ButtonComponent.setButtonStyle(editButton, ButtonComponent.BUTTON_STYLE.DISABLED);
        ButtonComponent.setButtonStyle(deleteButton, ButtonComponent.BUTTON_STYLE.DISABLED);
        ButtonComponent.setButtonStyle(commitButton, ButtonComponent.BUTTON_STYLE.DISABLED);

        addButtonEventhandler(addButton, BUTTON_FUNCTION.ADD);
        addButtonEventhandler(editButton, BUTTON_FUNCTION.EDIT);
        addTableSelectionButtonEventListener(editButton);
        addButtonEventhandler(deleteButton, BUTTON_FUNCTION.DELETE);
        addTableSelectionButtonEventListener(deleteButton);
        addButtonEventhandler(commitButton, BUTTON_FUNCTION.COMMIT);
        commitButtonIsEnabled(commitButton);

        buttonBar.getChildren().addAll(addButton, editButton, deleteButton, commitButton);
    }

    private Button addTableSelectionButtonEventListener(Button button){
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                button.setDisable(false);
            } else {button.setDisable(true);}
        });
        return button;
    }

    private Button commitButtonIsEnabled(Button button){
        button.disableProperty().bind(dataChanges.not());
        return button;
    }

    private Button addButtonEventhandler(Button button, BUTTON_FUNCTION buttonFunction){
        EventHandler<ActionEvent> eventHandler;
        switch (buttonFunction){
            case ADD: eventHandler = event -> handleAddButtonClicked();
            break;
            case EDIT: eventHandler = event -> handleEditButtonClicked();
            break;
            case COMMIT: eventHandler = event -> handleCommitButtonClicked();
            break;
            case DELETE: eventHandler = event -> handleDeleteButtonClicked();
            break;
            default: eventHandler = null;
        }
        button.setOnAction(eventHandler);
        return button;
    }

    private void handleDeleteButtonClicked() {
        ImageData imageData = (ImageData) tableView.getSelectionModel().getSelectedItem();
        if (imageData != null) {
            setImageDataChanges(imageData, SQLiteUtil.SQL_UPDATE_TYPE.DELETE);
            tableView.getItems().remove(imageData);
        }
    }

    private void handleCommitButtonClicked() {
        Map<ImageData, SQLiteUtil.SQL_UPDATE_TYPE> updates = new HashMap<>();
        for (ImageData imageData: deleteImageDataList) {
            updates.put(imageData, SQLiteUtil.SQL_UPDATE_TYPE.DELETE);
        }
        for (ImageData imageData: insertImageDataList) {
            updates.put(imageData, SQLiteUtil.SQL_UPDATE_TYPE.INSERT);
        }
        for (ImageData imageData: updateImageDataList) {
            updates.put(imageData, SQLiteUtil.SQL_UPDATE_TYPE.UPDATE);
        }

        List<ImageData> errors = SQLiteUtil.updateSQLDatabase(updates);
        if (errors.size() > 0){
            String contentText = "These Products were not changed in the database:";
            for (ImageData imageData: errors) {
                contentText = contentText + "\n" + imageData.getProductCode();
            }
            AlertWindowHelper.setupTextAreaAlert("Error Updating SQL Database", "These Products could not be updated" , null, contentText).showAndWait();
        }
        resetAndReloadAll();
    }

    private void handleEditButtonClicked() {
        ImageData selectedImageData = (ImageData) tableView.getSelectionModel().getSelectedItem();
        Optional<ImageData> imageData = AlertWindowHelper.setupImageDataEditDialog((ImageData) tableView.getSelectionModel().getSelectedItem()).showAndWait();
        if (imageData.isPresent() && selectedImageData != null) {
            setImageDataChanges(imageData.get(), SQLiteUtil.SQL_UPDATE_TYPE.UPDATE);
            tableView.getItems().set(tableView.getItems().indexOf(selectedImageData), imageData.get());
        }
    }

    private void handleAddButtonClicked() {
        Optional<ImageData> imageData = AlertWindowHelper.setupImageDataInputDialog().showAndWait();
        if (imageData.isPresent()) {
            ImageData errorImageData = setImageDataChanges(imageData.get(), SQLiteUtil.SQL_UPDATE_TYPE.INSERT);
            if (errorImageData == null) {
                tableView.getItems().add(imageData.get());
            }
        }
    }

    private void setupTableView(){
        //id column
        TableColumn<Integer, ImageData> idColumn = new TableColumn<>("SQL ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        //productCode column
        TableColumn<String, ImageData> productCodeColumn = new TableColumn<>("Product Code");
        productCodeColumn.setCellValueFactory(new PropertyValueFactory<>("productCode"));
        //productWebCode column
        TableColumn<String, ImageData> productWebCodeColumn = new TableColumn<>("Product Web Code");
        productWebCodeColumn.setCellValueFactory(new PropertyValueFactory<>("productWebCode"));
        //productName column
        TableColumn<String, ImageData> productNameColumn = new TableColumn<>("Product Name");
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("productName"));

        tableView.getColumns().addAll(idColumn, productCodeColumn, productWebCodeColumn, productNameColumn);
        tableView.setPlaceholder(new Label("There is no data to display..."));
        tableView.setItems(tableData);
        setTableDataDatabaseData();
    }

    private void setTableDataDatabaseData(){
        SQLiteUtil sqLiteUtil = new SQLiteUtil();
        ObservableList<ImageData> imageDataList = null;
        try {
            imageDataList = FXCollections.observableList(sqLiteUtil.getAllData());
        } catch (SQLException e) {
            AlertWindowHelper.setupExceptionAlert("Exception", "There was an error setting up the table", "An exception occurred when fetching table data, see more below.", e).show();
        }
        if (imageDataList != null){
            if (imageDataList.size() != 0){
                tableData.addAll(imageDataList);
            }
        }
    }

    private ImageData setImageDataChanges(ImageData imageData, SQLiteUtil.SQL_UPDATE_TYPE sqlUpdateType){
        switch (sqlUpdateType){
            case INSERT:
                if (!SqlImageDataDuplicateCheck(imageData, sqlUpdateType)) {
                    insertImageDataList.add(imageData);
                    System.out.println("Insert: "+ imageData.getProductCode());
                } else {
                    AlertWindowHelper.setupDefaultAlert(Alert.AlertType.WARNING, "Could not add product",  imageData.getProductCode() +" Already exists in database", "Try updating this product instead").show();
                    return imageData;
                }
                break;
            case UPDATE:
                if (!SqlImageDataDuplicateCheck(imageData, sqlUpdateType)) {
                    updateImageDataList.add(imageData);
                    System.out.println("update: "+ imageData.getProductCode());
                }
                break;
            case DELETE:
                if (!SqlImageDataDuplicateCheck(imageData, sqlUpdateType)) {
                    deleteImageDataList.add(imageData);
                    System.out.println("delete: "+ imageData.getProductCode());
                }
                break;

        }
        return null;
    }

    private Boolean SqlImageDataDuplicateCheck(ImageData imageData, SQLiteUtil.SQL_UPDATE_TYPE sqlUpdateType){
        switch (sqlUpdateType){
            case INSERT:
                SQLiteUtil sqLiteUtil = new SQLiteUtil();
                try {
                    List<ImageData> results = sqLiteUtil.getData(imageData);
                    if (results.size() > 0){
                        return true;
                    }
                } catch (SQLException e) {
                    AlertWindowHelper.setupExceptionAlert("Exception", "An Exception occurred", "Could not verify SQL Database for duplicates", e).show();
                }
                break;
            case UPDATE:
                if (imageData.getId() == null || imageData.getId() == 0){
                    return true;
                }
                break;
            case DELETE:
                if (imageData.getId() == null || imageData.getId() == 0){
                    return true;
                }
                break;

        }
        return false;
    }

    private void resetAndReloadAll(){
        insertImageDataList.clear();
        updateImageDataList.clear();
        deleteImageDataList.clear();
        tableData.clear();
        setupController(true);
    }
}
