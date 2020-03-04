package com.shawncockburn.PITools.controllers;

import javafx.beans.property.SimpleBooleanProperty;

public class WorkCompletableController {

    private SimpleBooleanProperty workComplete = new SimpleBooleanProperty();

    public Boolean workIsComplete() {
        return workComplete.get();
    }

    public void isComplete(Boolean isComplete) {
        workComplete.setValue(isComplete);
    }

    public SimpleBooleanProperty getSimpleBooleanProperty(){
        return workComplete;
    }
}
