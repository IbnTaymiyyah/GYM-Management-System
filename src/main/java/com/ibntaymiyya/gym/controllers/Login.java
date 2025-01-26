package com.ibntaymiyya.gym.controllers;

import com.ibntaymiyya.gym.Main;
import com.ibntaymiyya.gym.Util.DBCheckRunning;
import com.ibntaymiyya.gym.Util.DBLoginUser;
import com.ibntaymiyya.gym.Util.DBUsers;
import com.ibntaymiyya.gym.Util.SaveFileManager;
import com.ibntaymiyya.gym.model.LoginModel;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.mfxresources.fonts.MFXFontIcon;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class Login implements Initializable {

    @FXML
    private StackPane FieldsStack;

    @FXML
    private Button LoadLoginFields_btn;

    @FXML
    private Button LoadRegisterFields_btn;

    @FXML
    private Label labelError_Login;

    @FXML
    private Label label_ERROR_Password;

    @FXML
    private Label label_ERROR_userName;

    @FXML
    private Button loginButton;

    @FXML
    private AnchorPane login_Fields_Arch;

    @FXML
    private MFXTextField nameOfNewUser;

    @FXML
    private Label nameOfTheApp_Login;

    @FXML
    private MFXTextField nameOf_LoginRES;

    @FXML
    private MFXPasswordField passwordOfNewUser;

    @FXML
    private MFXPasswordField passwordOf_LoginRES;

    @FXML
    private Button registerButton;

    @FXML
    private AnchorPane register_Fields_Arch;

    @FXML
    private GridPane grid;

    @FXML
    private Pane connectionBALL;

    @FXML
    private Label connectionTEXT;

    @FXML
    private Label labelGood_Login;



    private Button lastSelectedButton = LoadLoginFields_btn;
    private String selectedBtn = "tabsLoginPage-Active";

    private DBCheckRunning dbCheckRunning;
    private DBLoginUser dbLoginUser;
    private LoginModel loginModel ;
    private DBUsers dbUsers ;
    private Main main;




    @Override
        public void initialize(URL url, ResourceBundle resourceBundle) {

        dbCheckRunning = new DBCheckRunning();
        dbCheckRunning.loginPage(this);
        dbLoginUser = new DBLoginUser();
        loginModel = new LoginModel();
        dbUsers = new DBUsers();
        main = new Main();



        Platform.runLater(() -> {
            dbCheckRunning.checkMySqlService();
        });

        selectedButton(LoadLoginFields_btn,selectedBtn);
        setConnectionStatus();
        handler();


        }



    private void handler() {
        nameOfTheApp_Login.setText(SaveFileManager.getText("appName"));

        passwordOfNewUser.textProperty().addListener((observable, oldValue, newValue) -> {
            loginModel.passwordFieldValidation(passwordOfNewUser, label_ERROR_Password);
        });

        nameOfNewUser.textProperty().addListener((observable, oldValue, newValue) -> {
            loginModel.textFieldValidation(nameOfNewUser, label_ERROR_userName, 5);
        });

        passwordOf_LoginRES.textProperty().addListener((observable, oldValue, newValue) -> {
            loginModel.passwordFieldValidation(passwordOf_LoginRES, labelError_Login);
        });

        nameOf_LoginRES.textProperty().addListener((observable, oldValue, newValue) -> {
            loginModel.textFieldValidation(nameOf_LoginRES, labelError_Login, 5);
        });

        loginModel.textOnlyFilter(nameOfNewUser);
        loginModel.textOnlyFilter(nameOf_LoginRES);
        loginModel.textOnlyFilter(passwordOf_LoginRES);
        loginModel.textOnlyFilter(passwordOfNewUser);


    }



    public void setConnectionStatus(){

        if (!dbCheckRunning.checkMySqlService()){
            connectionBALL.setStyle("-fx-background-color: red; -fx-background-radius: 100px");
            connectionTEXT.setStyle("-fx-text-fill: red");
            connectionTEXT.setText("غير متصل");
        }else {
            connectionBALL.setStyle("-fx-background-color : #00AE67FF ; -fx-background-radius: 100px");
            connectionTEXT.setStyle("-fx-text-fill: green");
            connectionTEXT.setText("متصل");
        }

        }

    public void selectedButton(Button selectedButton ,String activeClass ){
        if (lastSelectedButton != null) {
            lastSelectedButton.getStyleClass().removeIf(style -> style.contains("Active"));
        }
        selectedButton.getStyleClass().add(activeClass);
        lastSelectedButton = selectedButton;
        selectedBtn = activeClass;


    }



    @FXML
    private void loadLoginFields(){

        selectedButton(LoadLoginFields_btn,"tabsLoginPage-Active");
        dbCheckRunning.checkMySqlService();
        login_Fields_Arch.setVisible(true);
        register_Fields_Arch.setVisible(false);
        clearRegisterFields();
        setConnectionStatus();
        clearLoginFields();

    }

    @FXML
    private void loadRegisterFields(){

        selectedButton(LoadRegisterFields_btn,"tabsLoginPage-Active");
        dbCheckRunning.checkMySqlService();
        login_Fields_Arch.setVisible(false);
        register_Fields_Arch.setVisible(true);
        labelGood_Login.setVisible(false);
        clearRegisterFields();
        setConnectionStatus();
        clearLoginFields();

    }


    @FXML
    private void login() {
        labelGood_Login.setVisible(false);
        String userName = nameOf_LoginRES.getText();
        String password = passwordOf_LoginRES.getText();

        boolean isPasswordValid = loginModel.passwordFieldValidation(passwordOf_LoginRES, labelError_Login);
        boolean isUserNameValid = loginModel.textFieldValidation(nameOf_LoginRES, labelError_Login, 5);

        if (isPasswordValid && isUserNameValid) {
            if (dbCheckRunning.checkMySqlService()) {
                if (dbLoginUser.isUserExist(userName, password)) {
                    loginModel.getUserData(userName);
                    main.mainStage();
                    labelError_Login.setVisible(false);
                } else {
                    labelError_Login.setText("المستخدم غير موجود");
                    labelError_Login.setVisible(true);
                }
            }
        } else {
            labelError_Login.setText("خطأ في البيانات المدخلة");
            labelError_Login.setVisible(true);

        }
    }

    /*     Add New User with Permissions      */

    @FXML
    public void addNewUser()  {
        String userName = nameOfNewUser.getText(), userNamePassword = passwordOfNewUser.getText();
        boolean isPasswordValid = loginModel.passwordFieldValidation(passwordOfNewUser, label_ERROR_Password);
        boolean isUserNameValid = loginModel.textFieldValidation(nameOfNewUser, label_ERROR_userName, 5);
        List<Integer> selectedPermissionIndex = new ArrayList<>();
        selectedPermissionIndex.add(3);

        if (isPasswordValid && isUserNameValid) {
            if (dbCheckRunning.checkMySqlService()) {
                if (loginModel.isUserNameTaken(userName)) {
                    label_ERROR_userName.setVisible(true);
                    label_ERROR_userName.setText("هذا الاسم موجود بالفعل، اختر اسم آخر");
                } else {


                    try {
                        dbUsers.addNewUser(userName, userNamePassword, selectedPermissionIndex);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    labelGood_Login.setVisible(true);
                    loadLoginFields();

                }
            }
        }else {
            label_ERROR_userName.setVisible(true);
            label_ERROR_userName.setText("خطاء في البيانات المدخلة");
            labelGood_Login.setVisible(false);
        }
    }


    private void clearLoginFields(){
        nameOf_LoginRES.clear();
        passwordOf_LoginRES.clear();
        labelError_Login.setVisible(false);
        nameOf_LoginRES.setStyle("-fx-border-color: lightgray;");
        passwordOf_LoginRES.setStyle("-fx-border-color: lightgray;");
    }
    private void clearRegisterFields(){
        nameOfNewUser.clear();
        passwordOfNewUser.clear();
        label_ERROR_userName.setVisible(false);
        label_ERROR_Password.setVisible(false);
        nameOfNewUser.setStyle("-fx-border-color: lightgray;");
        passwordOfNewUser.setStyle("-fx-border-color: lightgray;");
    }







}









