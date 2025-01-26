package com.ibntaymiyya.gym.controllers;

import com.ibntaymiyya.gym.Main;
import com.ibntaymiyya.gym.Util.*;
import com.ibntaymiyya.gym.model.AddMemberGetterAndSetter;
import com.ibntaymiyya.gym.model.MainModel;
import com.ibntaymiyya.gym.model.QuickButtonsInfo;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.theming.JavaFXThemes;
import io.github.palexdev.materialfx.theming.MaterialFXStylesheets;
import io.github.palexdev.materialfx.theming.UserAgentBuilder;
import io.github.palexdev.mfxresources.fonts.IconsProviders;
import io.github.palexdev.mfxresources.fonts.*;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.converter.NumberStringConverter;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;

public class MainController implements Initializable {

    @FXML
    private AnchorPane ParentPane_mainpage;

    @FXML
    private Button btnAddNew_main;

    @FXML
    private Button btnClearFields_main;

    @FXML
    private MFXTextField memberAge_main;

    @FXML
    private MFXComboBox<String> memberGender_main;

    @FXML
    private MFXTextField memberHeight_main;

    @FXML
    private MFXTextField memberName_main;

    @FXML
    private MFXTextField memberPhone_main;

    @FXML
    private MFXDatePicker memberSubEnd_main;

    @FXML
    private MFXTextField memberSubPay_main;

    @FXML
    private MFXDatePicker memberSubStart_main;

    @FXML
    private MFXTextField memberWeight_main;

    @FXML
    private MFXButton quickBtn1_main;

    @FXML
    private MFXButton quickBtn2_main;

    @FXML
    private MFXButton quickBtn3_main;

    @FXML
    private MFXTableView<AddMemberGetterAndSetter> MainTable_MainP;

    @FXML
    private MFXTableView<AddMemberGetterAndSetter> SecondTableGoExpire_MainP;


    @FXML
    private MFXButton quickBtnPay1_main;

    @FXML
    private MFXButton quickBtnPay2_main;

    @FXML
    private MFXButton quickBtnPay3_main;

    @FXML
    private Label titleTop;

    @FXML
    private HBox top_hbox;

    @FXML
    private Label timeAM_or_PM_mainP;

    @FXML
    private Label timeNumber_mainP;

    @FXML
    private GridPane grid;



    LocalTime localTime = LocalTime.now();
    LocalDate date = LocalDate.now();

    MainModel mainModel = new MainModel();
    Main main = new Main();

    DBAddMember dbAddMember = new DBAddMember();
    DBMembers dbMembers = new DBMembers();

    MemberPageController memberPageController = new MemberPageController();

    QuickButtonsInfo info = new QuickButtonsInfo();
    SaveFileManager saveFileManager = new SaveFileManager();
     Setting setting;
     Stage stage ;
     private DBCheckRunning dbCheckRunning;


    private AddMemberGetterAndSetter addMemberGetterAndSetter;





    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        addMemberGetterAndSetter = new AddMemberGetterAndSetter();
        dbAddMember = new DBAddMember();
        dbCheckRunning = new DBCheckRunning();
        saveFileManager = new SaveFileManager();
        setting = new Setting();

        // Set Tables In Main Page
        mainModel.expiringMembers(SecondTableGoExpire_MainP);
        mainModel.setNewMembersTable(MainTable_MainP);





        selecteGender();
        numbersOnlyTextFields();
        textOnlyTextFields();
        currentTime();
        bindFieldsToModel();
        getNewNameOfApp();
        activeSaveData();
        mainModel.quickBtnValues();





    }



    public void activeSaveData(){
        titleTop.setText(getNewNameOfApp());

        quickBtn1_main.setText(SaveFileManager.getText("btnMonth1")+ " ( شهر ) ");
        quickBtn2_main.setText(SaveFileManager.getText("btnMonth2")+ " ( شهر ) ");
        quickBtn3_main.setText(SaveFileManager.getText("btnMonth3")+ " ( شهر ) ");

        quickBtnPay1_main.setText(SaveFileManager.getText("btnPay1"));
        quickBtnPay2_main.setText(SaveFileManager.getText("btnPay2"));
        quickBtnPay3_main.setText(SaveFileManager.getText("btnPay3"));

    }

    public String getNewNameOfApp(){
       String titleText = SaveFileManager.getText("appName");

        if (titleText == null) {

         System.out.println(" Title in Save File Manager is NUll ");}

        return titleText;
    }




    /*    Get Time Now from method in MainModel  */
    public void currentTime(){
        mainModel.timeNow(() -> {
            timeNumber_mainP.setText(new SimpleDateFormat("hh:mm").format(new Date()));
            timeAM_or_PM_mainP.setText(new SimpleDateFormat("a").format(new Date()));

        });
    }


    /*    Get Info From TextField and added to Getter & Setter Class  */

    // get Validate Date To send
    AtomicBoolean isDateValid = new AtomicBoolean(true);
    private void bindFieldsToModel() {

        addMemberGetterAndSetter.setId(MainModel.generateUniqueRandomNumber());







        String simpleFormatter = localTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        LocalDate currentDate = LocalDate.now();



        memberSubStart_main.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                String fullInput =  newValue +" "+ simpleFormatter;
                String formatData = dbAddMember.convertDateFormat(fullInput);

                if (formatData != null){

                    String formattedDateOnly = formatData.substring(0,10);
                    LocalDate inputDate = LocalDate.parse(formattedDateOnly, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                    if (inputDate.isBefore(currentDate)){

                         isDateValid.set(false);
                    }else {

                        isDateValid.set(true);
                        addMemberGetterAndSetter.setDateStart(formatData);
                    }

                }

            }
        });

        memberSubEnd_main.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {


                String fullInput = newValue +" "+ simpleFormatter;
                String formatDate = dbAddMember.convertDateFormat(fullInput);

                if (formatDate != null){
                    String formattedDateOnly = formatDate.substring(0,10);
                    LocalDate inputDate = LocalDate.parse(formattedDateOnly,DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                    if (inputDate.isBefore(currentDate)){

                        isDateValid.set(false);
                    }else {
                        isDateValid.set(true);
                        addMemberGetterAndSetter.setDateEnd(formatDate);
                    }

                }
            }
        });


        // Bind the fields to the model
        memberName_main.textProperty().bindBidirectional(addMemberGetterAndSetter.nameProperty());
        memberGender_main.textProperty().bindBidirectional(addMemberGetterAndSetter.genderProperty());
        memberPhone_main.textProperty().bindBidirectional(addMemberGetterAndSetter.phoneProperty());
        memberAge_main.textProperty().bindBidirectional(addMemberGetterAndSetter.ageProperty(), new NumberStringConverter());
        memberWeight_main.textProperty().bindBidirectional(addMemberGetterAndSetter.weightProperty(), new NumberStringConverter());
        memberHeight_main.textProperty().bindBidirectional(addMemberGetterAndSetter.heightProperty(), new NumberStringConverter());
        memberSubPay_main.textProperty().bindBidirectional(addMemberGetterAndSetter.payProperty(), new NumberStringConverter());

        // Clear Fields
        memberPhone_main.setText("");
        memberAge_main.setText("");
        memberWeight_main.setText("");
        memberHeight_main.setText("");
        memberSubPay_main.setText("");
        memberGender_main.clearSelection();


    }







    /*   TextFileds Filters   */

    public void numbersOnlyTextFields(){
        mainModel.addNumberOnlyFilter(memberAge_main);
        mainModel.addNumberOnlyFilter(memberWeight_main);
        mainModel.addNumberOnlyFilter(memberHeight_main);
        mainModel.addNumberOnlyFilter(memberPhone_main);
        mainModel.addNumberOnlyFilter(memberSubPay_main);
    }

    public void textOnlyTextFields(){
        mainModel.addTextOnlyFilter(memberName_main);
    }




    // ComboBox For Select Gender
    private void selecteGender() {
        ObservableList<String> genders = mainModel.createGenderList();
        memberGender_main.setItems(genders);
    }


    // Quick Button For Set Start & End Months Sub.... Here : Connect methods with buttons on fxml file

    private void updateQuickBtn(QuickButtonsInfo info , MFXButton quickBtnLabel){

        memberSubStart_main.setValue(LocalDate.parse(info.getFormattedDateStart(),DateTimeFormatter.ofPattern("MMMM d,yyyy")));
        memberSubEnd_main.setValue(LocalDate.parse(info.getFormattedDateEnd(), DateTimeFormatter.ofPattern("MMMM d,yyyy")));
    }

    public void quickBtn1(){
        updateQuickBtn(mainModel.quickButtons1(LocalDate.now()), quickBtn1_main);


    }

    public void quickBtn2(){
        LocalDate currentDate = LocalDate.now();
        QuickButtonsInfo info = mainModel.quickButtons2(currentDate);


        memberSubStart_main.setValue(LocalDate.parse(info.getFormattedDateStart(), DateTimeFormatter.ofPattern("MMMM d,yyyy")));
        memberSubEnd_main.setValue(LocalDate.parse(info.getFormattedDateEnd(), DateTimeFormatter.ofPattern("MMMM d,yyyy")));


    }



    public void quickBtn3(){
        LocalDate currentDate = LocalDate.now();
        QuickButtonsInfo info = mainModel.quickButtons3(currentDate);


        memberSubStart_main.setValue(LocalDate.parse(info.getFormattedDateStart(), DateTimeFormatter.ofPattern("MMMM d,yyyy")));
        memberSubEnd_main.setValue(LocalDate.parse(info.getFormattedDateEnd(), DateTimeFormatter.ofPattern("MMMM d,yyyy")));


    }

    // Quick Button For Pay Buttons   Here : Create Method of Quick Buttons ( How Much Sub ) with use QuickButtonsInfo as Getter & Setter

    private void updatePaymentQuickBtn(QuickButtonsInfo info){
        String name =String.valueOf(info.getQuickPay()) ;
        memberSubPay_main.setText(name);
    }
    public void quickBtn4(){
        updatePaymentQuickBtn(mainModel.quickButtonsPay1());
    }

    public void quickBtn5(){
        updatePaymentQuickBtn(mainModel.quickButtonsPay2());
    }

    public void quickBtn6(){
        updatePaymentQuickBtn(mainModel.quickButtonsPay3());
    }




    /*  Add New Member to Method in DBAddMember Class  */
    public void addNewMember() {


        List<MFXTextField> textFields = Arrays.asList(memberName_main,memberGender_main,memberSubPay_main,memberSubStart_main,memberSubEnd_main);
        validateTextFields(textFields);

        // Check if any text field is invalid
        boolean isValid = textFields.stream().allMatch(textField -> textField != null && !textField.getText().trim().isEmpty());

        if (isValid) {

            if (!isDateValid.get()){


                memberSubStart_main.setStyle("-fx-border-color: red;");
                memberSubEnd_main.setStyle("-fx-border-color: red;");
            }else {

                DBAddMember.addMemberToDatabase(addMemberGetterAndSetter);
                memberSubStart_main.setStyle("-fx-border-color: lightgray;");
                memberSubEnd_main.setStyle("-fx-border-color: lightgray;");
                clearFields();
            }
        }

    }
    private void validateTextFields(List<MFXTextField> textFields) {
        for (MFXTextField textField : textFields) {
            if (textField == null || textField.getText().trim().isEmpty()) {
                textField.setStyle("-fx-border-color: red;");
            }else textField.setStyle("-fx-border-color: lightgray;");
        }
    }

    /*--------------------------------------------------------------*/


    @FXML
    private void signOut()  {
        mainModel.signOutToLogin(grid);
    }


    /*--------------------------------------------------------------*/


    /*      GO Buttons     */

    public void allMembersPage(){
        if (dbCheckRunning.checkMySqlService()) {
            main.changeScene("fxml/AllMembers.fxml");
        }


    }
    public void settingPage(){

        main.changeScene("fxml/Setting.fxml");


    }

    public void analysisPage(){

        if (dbCheckRunning.checkMySqlService()) {

            main.changeScene("fxml/Analysis.fxml");
        }


    }



    // Clear All fields
    public void clearFields() {
        main.changeScene("fxml/MainPage.fxml");
    }


}