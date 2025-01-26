package com.ibntaymiyya.gym.Util;


import com.ibntaymiyya.gym.model.DialogConfig;

import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialog;
import io.github.palexdev.materialfx.dialogs.MFXGenericDialogBuilder;
import io.github.palexdev.materialfx.dialogs.MFXStageDialog;
import io.github.palexdev.materialfx.enums.ScrimPriority;
import io.github.palexdev.mfxresources.fonts.MFXFontIcon;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


public class Dialog {




    private MFXGenericDialog dialogContent;
    public static MFXStageDialog dialog;
    Stage stage;






    public Dialog(DialogConfig config) {


        Platform.runLater(() -> {

            this.dialogContent = MFXGenericDialogBuilder.build()
                    .makeScrollable(true)
                    .get();


            // Debugging: Check if the resource is found
            String resourcePath = "/com/ibntaymiyya/gym/css/Dialogs.css";
            URL resourceUrl = getClass().getResource(resourcePath);
            if (resourceUrl == null) {
                System.out.println("Resource Not found: " + resourcePath);
            } else {
                dialogContent.getStylesheets().add(resourceUrl.toExternalForm());
            }


            this.dialog = MFXGenericDialogBuilder.build(dialogContent)
                    .toStageDialogBuilder()
                    .initOwner(config.getStage())
                    .initModality(Modality.APPLICATION_MODAL)
                    .setDraggable(true)
                    .setTitle("Dialogs Preview")
                    .setOwnerNode(config.getGrid())
                    .setCenterInOwnerNode(true)
                    .setScrimPriority(ScrimPriority.NODE)
                    .setScrimOwner(true)
                    .get();


            /*
            * 1 -> Dialog Node (VBox)
            * 2 -> Dialog Check
            * 3 -> Dialog Info
            * 4 -> Dialog warn
            * 5 -> Dialog Error
            * Default -> Dialog General
            */

            // Chose Witch Dialog showing

            switch (config.getWitchDialog()){
                case 1: dialogNode(config.getHeaderText(), config.getvBox());
                break;
                case 2: dialogCheck(config.getHeaderText(), config.getContentText()); dialogContent.getStyleClass().add("mfx-check-dialog");
                break;
                case 3: dialogInfo(config.getHeaderText(), config.getContentText());
                break;
                case 4: dialogWarn(config.getHeaderText(), config.getContentText());
                break;
                case 5: dialogError(config.getHeaderText(), config.getContentText());
                break;
                default: dialogGeneral(config.getHeaderText(), config.getContentText());
                break;
            }


            // Chose 1 OR 2 Buttons in Dialog & set Action for Btn

            if (config.getBtnNum() == 2) {


                MFXButton okButton = new MFXButton(config.getOkButtonName());
                okButton.setOnAction(event -> {
                    config.getEventHandler().handle(event);

                });

                MFXButton cancelButton = new MFXButton(" الغاء ");
                cancelButton.setOnAction(event -> dialog.close());

                dialogContent.addActions(okButton, cancelButton);
                //dialogContent.setMaxSize(400, 200);


            } else if ( config.getBtnNum() == 1) {

                dialogContent.addActions(  Map.entry(new MFXButton(" حسناّ "), event -> dialog.close())  );
            }


            //dialogContent.setMaxSize(400,200);


        });



    }
    public void showDialog() {
        Platform.runLater(() -> dialog.show());
    }









    public void dialogNode(String headerText,VBox vbox ) {
        MFXFontIcon checkIcon = new MFXFontIcon("fas-gear", 20);
        dialogContent.setHeaderIcon(checkIcon);
        dialogContent.setHeaderText(headerText);
        dialogContent.setContent(vbox);
        dialog.showDialog();

    }

    public void dialogGeneral(String headerText, String contentText) {
        MFXFontIcon checkIcon = new MFXFontIcon("fas-circle-check", 18);
        dialogContent.setHeaderIcon(checkIcon);
        dialogContent.setHeaderText(headerText);
        dialogContent.setContentText(contentText);
        convertDialogTo(null);
        dialog.showDialog();

    }
    public void dialogCheck(String headerText, String contentText) {
        MFXFontIcon checkIcon = new MFXFontIcon("fas-circle-check", 18);
        dialogContent.setHeaderIcon(checkIcon);
        dialogContent.setHeaderText(headerText);
        dialogContent.setContentText(contentText);
        dialog.showDialog();

    }

    public void dialogInfo(String headerText,String contentText){
            MFXFontIcon infoIcon = new MFXFontIcon("fas-circle-info", 18);
            dialogContent.setHeaderIcon(infoIcon);
            dialogContent.setHeaderText(headerText);
            dialogContent.setContentText(contentText);
            convertDialogTo("mfx-check-dialog");
            dialog.showDialog();
    }

    public void dialogWarn(String headerText,String contentText){
        MFXFontIcon warnIcon = new MFXFontIcon("fas-circle-exclamation",18);
        dialogContent.setHeaderIcon(warnIcon);
        dialogContent.setHeaderText(headerText);
        dialogContent.setContentText(contentText);
        convertDialogTo("mfx-warn-dialog");
        dialog.showDialog();

    }
    public void dialogError(String headerText,String contentText){
        MFXFontIcon errorIcon = new MFXFontIcon("fas-circle-xmark",18);
        dialogContent.setHeaderIcon(errorIcon);
        dialogContent.setHeaderText(headerText);
        dialogContent.setContentText(contentText);
        convertDialogTo("mfx-error-dialog");
        dialog.showDialog();

    }





    private void convertDialogTo(String styleSheet){
        dialogContent.getStyleClass().removeIf(
                s -> s.equals("mfx-check-dialog") || s.equals("mfx-info-dialog") || s.equals("mfx-warn-dialog") || s.equals("mfx-error-dialog")
        );

        if (styleSheet != null){
            dialogContent.getStyleClass().add(styleSheet);
        }
    }



}
