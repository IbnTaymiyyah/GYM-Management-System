package com.ibntaymiyya.gym.model;

import com.ibntaymiyya.gym.Util.DBLoginUser;
import com.ibntaymiyya.gym.Util.DBUsers;
import com.ibntaymiyya.gym.Util.Dialog;
import com.ibntaymiyya.gym.Util.SaveFileManager;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.validation.Constraint;
import io.github.palexdev.materialfx.validation.Severity;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LoginModel {

    private static final PseudoClass INVALID_PSEUDO_CLASS = PseudoClass.getPseudoClass("invalid");
    private final DBUsers dbUsers = new DBUsers();
    private final DBLoginUser dbLoginUser = new DBLoginUser();












    /*----------------------------------------------------------------*/


    /*         Text & Password Fields Validation ,and Filter          */

    public Boolean passwordFieldValidation(MFXPasswordField passwordField , Label validationLabel){
        Constraint passwordLength = Constraint.Builder.build()
                .setSeverity(Severity.INFO)
                .setMessage("عدد الحروف قليل ، اكتب 5 حروف على الاقل")
                .setCondition(passwordField.textProperty().length().greaterThanOrEqualTo(5))
                .get();

        passwordField.getValidator()
                .constraint(passwordLength);

        passwordField.getValidator().validProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue){
                validationLabel.setVisible(false);
                passwordField.pseudoClassStateChanged(INVALID_PSEUDO_CLASS,false);
                passwordField.setStyle("-fx-border-color: lightgray;");
            }
        });

        passwordField.delegateFocusedProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue && !newValue){
                List<Constraint> constraints = passwordField.validate();
                if (!constraints.isEmpty()){
                    passwordField.setStyle("-fx-border-color: red;");
                    passwordField.pseudoClassStateChanged(INVALID_PSEUDO_CLASS,true);
                    validationLabel.setText(constraints.get(0).getMessage());
                    validationLabel.setVisible(true);
                }
            }
        });

        return passwordField.getText().length() >= 5;
    }

    public Boolean textFieldValidation(MFXTextField textField , Label validationLabel, int charNum){
        Constraint textLength = Constraint.Builder.build()
                .setSeverity(Severity.INFO)
                .setMessage("عدد الحروف قليل ، اكتب "+ charNum +" حروف على الاقل")
                .setCondition(textField.textProperty().length().greaterThanOrEqualTo(charNum))
                .get();

        textField.getValidator()
                .constraint(textLength);

        textField.getValidator().validProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue){
                validationLabel.setVisible(false);
                textField.pseudoClassStateChanged(INVALID_PSEUDO_CLASS,false);
                textField.setStyle("-fx-border-color: lightgray;");
            }
        });

        textField.delegateFocusedProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue && !newValue){
                List<Constraint> constraints = textField.validate();
                if (!constraints.isEmpty()){
                    textField.setStyle("-fx-border-color: red;");
                    textField.pseudoClassStateChanged(INVALID_PSEUDO_CLASS,true);
                    validationLabel.setText(constraints.get(0).getMessage());
                    validationLabel.setVisible(true);
                }
            }
        });
        return textField.getText().length() >= charNum;
    }



    // Filter For Text Fields & Password Fields
    public void textOnlyFilter(MFXTextField textField){
        textField.addEventFilter(KeyEvent.KEY_TYPED, event -> {
            String input = event.getCharacter();
            if (!input.matches("[a-zA-Z\\u0600-\\u06FF]")){
                event.consume();
            }
        });
    }

    public void textOnlyFilter(MFXPasswordField passwordField){
        passwordField.addEventFilter(KeyEvent.KEY_TYPED, event -> {
            String input = event.getCharacter();
            if (!input.matches("[a-zA-Z\\d]")){
                event.consume();
            }
        });
    }

    /*----------------------------------------------------------------*/

  /*  Get Current User Login Data and Save it in Text_Values.properties    */

    public void getUserData(String userName) {
        UserDataGetterAndSetter userData = dbLoginUser.getUserDetails(userName);


                SaveFileManager.setText("currentuser", userName);
                SaveFileManager.setText("currentuserpassword", userData.getPassword());


        Map<Integer, Boolean> permissionsMap = userData.getPermissionsMap();
        if (permissionsMap != null) {
            for (Map.Entry<Integer, Boolean> entry : permissionsMap.entrySet()) {
                SaveFileManager.setText("perm" + entry.getKey(), entry.getValue().toString());
            }
    } else {
        System.out.println("User data not found for username: " + userName);
    }

    }






    /*----------------------------------------------------------------*/

    /*     UserName is Already Taken     */
    public boolean isUserNameTaken(String userName){
        return dbUsers.isUserNameTaken(userName);
    }




}
