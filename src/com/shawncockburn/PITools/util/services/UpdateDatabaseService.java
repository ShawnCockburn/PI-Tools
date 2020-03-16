package com.shawncockburn.PITools.util.services;

import com.shawncockburn.PITools.data.ImageData;
import com.shawncockburn.PITools.util.SQLiteUtil;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UpdateDatabaseService extends Service<List<ImageData>> {

    private Map<ImageData, SQLiteUtil.SQL_UPDATE_TYPE> updates;


    public UpdateDatabaseService(Map<ImageData, SQLiteUtil.SQL_UPDATE_TYPE> updates) {
        this.updates = updates;
    }


    @Override
    protected Task<List<ImageData>> createTask() {
        return new Task<List<ImageData>>() {
            @Override
            protected List<ImageData> call() {

                List<ImageData> imageDataList = new ArrayList<>(updates.keySet());
                List<ImageData> errors = new ArrayList<>();
                int progress = 0;
                for (ImageData imageData : imageDataList) {
                    if (isCancelled()) {
                        cancelled();
                        break;
                    }
                    try {
                        SQLiteUtil.updateSingleItemSQLDatabase(imageData, updates.get(imageData));
                    } catch (Exception e) {
                        errors.add(imageData);
                    }

                    updateProgress(progress, imageDataList.size());
                    updateMessage((updates.get(imageData) + ": ").toLowerCase() + imageData.getProductName());
                    progress++;
                }
                succeeded();
                return errors;
            }
        };
    }
}