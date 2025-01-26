package com.ibntaymiyya.gym.model;



import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DialogConfig {

    private final Stage stage;
    private final GridPane grid;
    private final int btnNum;
    private final int witchDialog;

    private final String headerText;
    private final String contentText;
    private final String okButtonName;

    private final VBox vBox;


    private EventHandler<ActionEvent> eventHandler;

    public DialogConfig(Stage stage, GridPane grid, int btnNum ,int witchDialog, EventHandler eventHandler, String headerText ,String contentText ,String okButtonName, VBox vBox ) {
        this.stage = stage;
        this.grid = grid;
        this.btnNum = btnNum;
        this.witchDialog = witchDialog;
        this.eventHandler = eventHandler;
        this.headerText = headerText;
        this.contentText = contentText;
        this.okButtonName = okButtonName;
        this.vBox = vBox;
    }

    public Stage getStage() {
        return stage;
    }

    public GridPane getGrid() {
        return grid;
    }

    public int getBtnNum() {
        return btnNum;
    }

    public int getWitchDialog(){return witchDialog;}

    public EventHandler getEventHandler(){return eventHandler;}

    public String getHeaderText(){return headerText;}

    public String getContentText(){return contentText;}
    public String getOkButtonName(){return okButtonName;}

    public VBox getvBox(){return vBox;}
}
