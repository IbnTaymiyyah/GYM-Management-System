package com.ibntaymiyya.gym.controllers;

import com.ibntaymiyya.gym.Main;
import com.ibntaymiyya.gym.Util.*;
import com.ibntaymiyya.gym.Util.Dialog;
import com.ibntaymiyya.gym.model.*;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.controls.models.spinner.IntegerSpinnerModel;
import io.github.palexdev.mfxresources.fonts.IconsProviders;
import io.github.palexdev.mfxresources.fonts.MFXFontIcon;
import io.github.palexdev.mfxresources.fonts.fontawesome.FontAwesomeSolid;
import javafx.application.HostServices;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;

import java.awt.*;
import java.io.File;
import java.net.URI;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.List;

public class Setting implements Initializable {

    @FXML
    private Button SaveChangeBtn;

    @FXML
    private Button addNewUserBtn_setting;

    @FXML
    private MFXButton back_btn_memberPage;

    @FXML
    private Button changeActivtionCodeBtn_setting;

    @FXML
    private Button changeCurrentInfoUserBtn_setting;

    @FXML
    private MFXFilterComboBox<String> chosseUserToDelete_setting;

    @FXML
    private Label currentAccountName;

    @FXML
    private Button deleteBtn_DB;

    @FXML
    private Button deleteChossedUser_setting;

    @FXML
    private Button exportAnalysisBtn;

    @FXML
    private Button exportMemberInfoBtn_excel;

    @FXML
    private Button getBackupBtn_DB;

    @FXML
    private Button getBackupMemberBtn_excel;

    @FXML
    private MFXTextField hwidField_setting;

    @FXML
    private MFXTextField nameOfGYM;

    @FXML
    private MFXTextField nameOfNewUser;

    @FXML
    private MFXSpinner<Integer> numberBtn1_Pay;

    @FXML
    private MFXSpinner<Integer> numberBtn2_Pay;

    @FXML
    private MFXSpinner<Integer> numberBtn3_Pay;

    @FXML
    private MFXSpinner<Integer> numberOfBtn1_month;

    @FXML
    private MFXSpinner<Integer> numberOfBtn2_month;

    @FXML
    private MFXSpinner<Integer> numberOfBtn3_month;

    @FXML
    private MFXPasswordField passwordOfCurrentUser;

    @FXML
    private Button removeCurrentAnalysisBtn;

    @FXML
    private Button saveBackupBtn_DB;

    @FXML
    private Button signoutBtn_setting;

    @FXML
    private Button socialMediaDevBtn_setting;
    @FXML
    public  AnchorPane ancPane_SettingPage_tab1;

    @FXML
    private MFXCheckListView<String> permissionsCheckList_settingP;

    @FXML
    private GridPane gride;

    @FXML
    private MFXTextField changeSNField_setting;

    @FXML
    private Label labelWrongCurrentPass_Setting;

    @FXML
    private Label labelWrongNewPass_Setting;

    @FXML
    private HBox layerSN_Setting;
    @FXML
    private VBox little_VBOOCD;
    @FXML
    private MFXPasswordField passwordOfNewUser;
    @FXML
    private Button saveNewPassBtn_setting;
    @FXML
    private Button saveNewSNBtn_setting;
    @FXML
    private VBox vbox_SettingPage_tab1;
    @FXML
    private Label labelWrongNewUserName_Setting;
    @FXML
    private Label labelWrongNameOfGYM_Setting;

    @FXML
    private MFXFontIcon GitHub_LinkIcon;

    @FXML
    private Button Github_Button;

    @FXML
    private Button Telegram_Button;

    @FXML
    private MFXFontIcon Telegram_LinkIcon;



    /*------------------------------------------------------------*/

    /*     Change User Info Dialog Fxml     */

    @FXML
    private MFXTextField NameOfUser_ChangeDialog;

    @FXML
    private MFXPasswordField PasswordOfUser_ChangeDialog;

    @FXML
    private VBox VBOX_ChangeUserInfoDialog;

    @FXML
    private Label labelWrongChangeUserName_SettingDialog;

    @FXML
    private Label labelWrongChangeUserPass_SettingDialog;

    @FXML
    private MFXCheckListView<String> permissionsCheckList_ChangeDialog;


    /*------------------------------------------------------------*/

    /*     Change User Info Dialog Fxml     */

    @FXML
    private Label LabelError_AdminPermissionDialog;

    @FXML
    private Label Label_AdminPermisssionDialog;

    @FXML
    private MFXPasswordField PasswordFieldPass_AdminPermissionDialog;

    @FXML
    private MFXTextField TextFieldUserName_AdminPermissionDialog;

    @FXML
    private VBox VBOX_AdminPermissionDialog;

    /*------------------------------------------------------------*/

    @FXML
    private HBox AddNewUserWidget_SettingPage;

    @FXML
    private HBox MonthsWidget_SettingPage;

    @FXML
    private HBox PriceWidget_SettingPage;

    @FXML
    private HBox RemoveUserWidget_SettingPage;

    @FXML
    private Button deleteAllData;



    /*------------------------------------------------------------*/


    private boolean adminPermission = false;






    ObservableList<String> permissions = AccountInfoTab.permissions;
    Main main = new Main();
    MainModel mainModel;
    MemberPageModel memberPageModel;
    MainController mainController = new MainController();
    SaveFileManager saveFileManager = new SaveFileManager();



    private DBCheckRunning dbCheckRunning;
    private HWID hwid;
    private SettingModel settingModel ;
    private DBUsers dbUsers;
    private DBLoginUser dbLoginUser = new DBLoginUser();
    private DialogConfig dialogConfig;
    private LoginModel loginModel = new LoginModel();
    private DBExportAndImportInfo dbExportAndImportInfo;




    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dbUsers = new DBUsers();
        dbUsers.setSetting(this);

        settingModel = new SettingModel(this);
        dbCheckRunning = new DBCheckRunning();
        loginModel = new LoginModel();
        dbLoginUser = new DBLoginUser();
        main = new Main();
        mainController = new MainController();
        mainModel = new MainModel();
        memberPageModel = new MemberPageModel();
        dbExportAndImportInfo = new DBExportAndImportInfo();
        hwid = new HWID();



        saveFileManager = new SaveFileManager();
        SaveChangeBtn.setOnAction(event -> sendInfoNameAndQuickBtns());
        permissionsCheckList_settingP.setItems(permissions);
        permissionsCheckList_ChangeDialog.setItems(permissions);
        updateComboBoxUsers();
        setTextFieldAppName();
        passwordFieldsValid();
        textFieldsValid();
        textOnlyFields();
        spinnerModel();
        handler();
        setHWID();

        getCurrentUserPermissions();
    }



    private void handler(){
        setIcons();
        currentAccountName.setText(SaveFileManager.getText("currentuser"));
        passwordOfCurrentUser.setText(SaveFileManager.getText("currentuserpassword"));

        // update User Dialog
        NameOfUser_ChangeDialog.setText(SaveFileManager.getText("currentuser"));
        PasswordOfUser_ChangeDialog.setText(SaveFileManager.getText("currentuserpassword"));
        boolean perm1 = Boolean.parseBoolean(SaveFileManager.getText("perm1"));
        boolean perm2 = Boolean.parseBoolean(SaveFileManager.getText("perm2"));
        boolean perm3 = Boolean.parseBoolean(SaveFileManager.getText("perm3"));
        boolean perm4 = Boolean.parseBoolean(SaveFileManager.getText("perm4"));
        boolean perm5 = Boolean.parseBoolean(SaveFileManager.getText("perm5"));

            if (perm1) {
                permissionsCheckList_ChangeDialog.getSelectionModel().selectItem("تغيير الاسعار و الشهور");
            }
            if (perm2) {
                permissionsCheckList_ChangeDialog.getSelectionModel().selectItem("تعديل صلاحيات الحساب");
            }
            if (perm3) {
                permissionsCheckList_ChangeDialog.getSelectionModel().selectItem("اضافة حساب");
            }
            if (perm4) {
                permissionsCheckList_ChangeDialog.getSelectionModel().selectItem("حذف قاعدة بيانات البرنامج");
            }
            if (perm5) {
                permissionsCheckList_ChangeDialog.getSelectionModel().selectItem("حذف الحسابات");
            }



    }





    /*     Spinner Model For Pay & Month QuickButtons     */

    public void spinnerModel(){


        //  Month Btn spinner

        int valueMonthBtn1 =  Integer.parseInt(Objects.requireNonNull(SaveFileManager.getText("btnMonth1")));
        int valueMonthBtn2 =  Integer.parseInt(Objects.requireNonNull(SaveFileManager.getText("btnMonth2")));
        int valueMonthBtn3 =  Integer.parseInt(Objects.requireNonNull(SaveFileManager.getText("btnMonth3")));

        IntegerSpinnerModel spinnerModelMonth1 = SpinnerModel.integerSpinnerModel(1,24,1,valueMonthBtn1);
        IntegerSpinnerModel spinnerModelMonth2 = SpinnerModel.integerSpinnerModel(1,24,1,valueMonthBtn2);
        IntegerSpinnerModel spinnerModelMonth3 = SpinnerModel.integerSpinnerModel(1,24,1,valueMonthBtn3);
        numberOfBtn1_month.setSpinnerModel(spinnerModelMonth1);
        numberOfBtn2_month.setSpinnerModel(spinnerModelMonth2);
        numberOfBtn3_month.setSpinnerModel(spinnerModelMonth3);

        //  Pay Btn spinner


        int valuePayBtn1= Integer.parseInt(Objects.requireNonNull(SaveFileManager.getText("btnPay1")));
        int valuePayBtn2= Integer.parseInt(Objects.requireNonNull(SaveFileManager.getText("btnPay2")));
        int valuePayBtn3= Integer.parseInt(Objects.requireNonNull(SaveFileManager.getText("btnPay3")));

        IntegerSpinnerModel spinnerModelPay1= SpinnerModel.integerSpinnerModel(10,3000,10,valuePayBtn1);
        IntegerSpinnerModel spinnerModelPay2= SpinnerModel.integerSpinnerModel(10,3000,10,valuePayBtn2);
        IntegerSpinnerModel spinnerModelPay3= SpinnerModel.integerSpinnerModel(10,3000,10,valuePayBtn3);

        numberBtn1_Pay.setSpinnerModel(spinnerModelPay1);
        numberBtn2_Pay.setSpinnerModel(spinnerModelPay2);
        numberBtn3_Pay.setSpinnerModel(spinnerModelPay3);



    }

    /*-----------------------------------------------------------*/


    /*       Change Size of Hbox  Change Serial Number     */

    public void changeSNBtn(){
        settingModel.showLayerHbox(59,464,layerSN_Setting);
    }

    public void saveChangeSNBtn(){
       // Add Check for Serial Number is correct

        settingModel.showLayerHbox(0,0,layerSN_Setting);
        settingModel.checkSerialNumberDialog(gride);



    }



    /*       Change Current User Info    */

    public void changeUserPasswordBtn(){
        settingModel.changeUserInfoDialog(gride,VBOX_ChangeUserInfoDialog,VBOX_AdminPermissionDialog,TextFieldUserName_AdminPermissionDialog,
                PasswordFieldPass_AdminPermissionDialog,LabelError_AdminPermissionDialog , adminPermission ,
                NameOfUser_ChangeDialog,PasswordOfUser_ChangeDialog,labelWrongChangeUserName_SettingDialog,labelWrongChangeUserPass_SettingDialog,permissionsCheckList_ChangeDialog);

    }



   /*-----------------------------------------------------------*/



    /*   Quick Button Change Value : Send to MainModel Class  */

    @FXML
    private void sendInfoNameAndQuickBtns() {



        if (!Objects.equals(changeName(),"")){



            settingModel.checkNameAndQuickBtnsDilaog(gride);

            // set Save Values of The App
            SaveFileManager.setMix("btnMonth1",numberOfBtn1_month.getValue());
            SaveFileManager.setMix("btnMonth2",numberOfBtn2_month.getValue());
            SaveFileManager.setMix("btnMonth3",numberOfBtn3_month.getValue());
            SaveFileManager.setMix("btnPay1",numberBtn1_Pay.getValue());
            SaveFileManager.setMix("btnPay2",numberBtn2_Pay.getValue());
            SaveFileManager.setMix("btnPay3",numberBtn3_Pay.getValue());
            mainModel.quickBtnValues();


            SaveFileManager.setText("appName",changeName());




        }else {



            settingModel.errorNameAndQuickBtnsDilaog(gride);


        }



    }

    private String changeName() {
        String getNameOfTheGym = nameOfGYM.getText();
        return getNameOfTheGym;
    }


    public void setTextFieldAppName(){
        String appName = SaveFileManager.getText("appName");
        nameOfGYM.setText(appName);
    }

    /*----------------------------------------------------------------*/

    /*     Add New User with Permissions      */

    public void addNewUser()  {
        String userName = nameOfNewUser.getText(), userNamePassword = passwordOfNewUser.getText();
        List<Integer> selectedPermissionIndex = settingModel.permissionsIndex(permissionsCheckList_settingP);


        if (labelWrongNewPass_Setting.isVisible() || labelWrongNewUserName_Setting.isVisible() || Objects.equals(nameOfNewUser.getText(),"") || Objects.equals(passwordOfNewUser.getText(),"") ){
            settingModel.errorAddNewUserDialog(gride);


        }else
        if (dbCheckRunning.checkMySqlService()) {
            if (settingModel.isUserNameTaken(userName)) {

                labelWrongNewUserName_Setting.setVisible(true);
                labelWrongNewUserName_Setting.setText("هذا الاسمو موجود بالفعل ، اختر اسم آخر ");
            } else {
                try {
                    dbUsers.addNewUser(userName, userNamePassword, selectedPermissionIndex);
                    updateComboBoxUsers();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                settingModel.checkAddNewUserDialog(gride);

                permissionsCheckList_settingP.getSelectionModel().clearSelection();
                nameOfNewUser.clear();
                passwordOfNewUser.clear();

            }
        }

    }




    /*----------------------------------------------------------------*/

    /*     Field Filters     */

    private void passwordFieldsValid(){
        settingModel.passwordFieldValidation(passwordOfCurrentUser,labelWrongCurrentPass_Setting);
        settingModel.passwordFieldValidation(passwordOfNewUser,labelWrongNewPass_Setting);

        settingModel.passwordFieldValidation(PasswordOfUser_ChangeDialog,labelWrongChangeUserPass_SettingDialog);

        settingModel.passwordFieldValidation(PasswordFieldPass_AdminPermissionDialog,LabelError_AdminPermissionDialog);

    }

    private void textFieldsValid(){
        settingModel.textFieldValidation(nameOfGYM,labelWrongNameOfGYM_Setting,3);
        settingModel.textFieldValidation(nameOfNewUser,labelWrongNewUserName_Setting,3);

        settingModel.textFieldValidation(NameOfUser_ChangeDialog,labelWrongChangeUserName_SettingDialog,3);

        settingModel.textFieldValidation(TextFieldUserName_AdminPermissionDialog,LabelError_AdminPermissionDialog,3);
    }
    private void textOnlyFields(){
        settingModel.textOnlyFilter(passwordOfNewUser);
        settingModel.textOnlyFilter(nameOfNewUser);
        settingModel.textOnlyFilter(nameOfGYM);
        settingModel.textOnlyFilter(passwordOfCurrentUser);
        settingModel.textOnlyFilter(changeSNField_setting);
        settingModel.textOnlyFilter(PasswordOfUser_ChangeDialog);
        settingModel.textOnlyFilter(NameOfUser_ChangeDialog);
        settingModel.textOnlyFilter(TextFieldUserName_AdminPermissionDialog);
        settingModel.textOnlyFilter(PasswordFieldPass_AdminPermissionDialog);

    }


    /*----------------------------------------------------------------*/

    /*     Export All Members To Excel CSV Button      */
    @FXML
    private void exportMembersToCSV(){
        if (dbCheckRunning.checkMySqlService()) {

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("حفظ بيانات المشتركين");
            fileChooser.setInitialFileName(" حفظ بيانات مشتركين " + nameOfGYM.getText() + " بتاريخ " + LocalDate.now());
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
            File selectedFile = fileChooser.showSaveDialog(ancPane_SettingPage_tab1.getScene().getWindow());

            if (selectedFile != null) {
                String filePath = selectedFile.getAbsolutePath();
                dbExportAndImportInfo.exportMembersInfoToCSV(filePath, gride);
            }
        }
    }

    /* Import All Members Info From CSV File */


    @FXML
    private void importMembersFromCSV(){
        if (dbCheckRunning.checkMySqlService()) {

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("اختار ملف CSV لأضافتة للبرنامج ");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));

            // Use showOpenDialog() for importing/opening a file:
            File selectedFile = fileChooser.showOpenDialog(ancPane_SettingPage_tab1.getScene().getWindow()); // Corrected!


            if (selectedFile != null) {
                String filePath = selectedFile.getAbsolutePath();
                dbExportAndImportInfo.importMembersFromCSV(filePath, gride);

            }
        }
    }



    /*----------------------------------------------------------------*/

    /*     DELETE All Data From All Tables     */

    @FXML
    private void deleteAllData(){
        if (dbCheckRunning.checkMySqlService()) {

            settingModel.deleteAllData(gride);
        }

    }




    /*----------------------------------------------------------------*/

    /*     Remove User      */
    @FXML
    private void removeUser(){

        if (dbCheckRunning.checkMySqlService()) {

            try {
                settingModel.getUserNameToRemove(String.valueOf(chosseUserToDelete_setting.getSelectedItem()), chosseUserToDelete_setting,gride);
                updateComboBoxUsers();
            }catch (Exception e){
                System.out.println("BOLOA");

            }
       }
    }


    /*----------------------------------------------------------------*/

    /*     Updates     */

    // Update ComboBox Get Users
    private void updateComboBoxUsers(){
        dbUsers.getAllUsers(chosseUserToDelete_setting);
    }




    /*----------------------------------------------------------------*/





    /*     Set HWID into TextField     */

    private void setHWID(){
        hwidField_setting.setText(hwid.codeHWID());
    }

    /*----------------------------------------------------------------*/

    /* Set Disable Permissions For Current User */
    public void getCurrentUserPermissions() {
            // Update User Data First


            // Get user Permissions
            boolean perm1 = SaveFileManager.getText("perm1").equals("true");
            boolean perm2 = SaveFileManager.getText("perm2").equals("true");
            boolean perm3 = SaveFileManager.getText("perm3").equals("true");
            boolean perm4 = SaveFileManager.getText("perm4").equals("true");
            boolean perm5 = SaveFileManager.getText("perm5").equals("true");

            if (!perm1) {

                PriceWidget_SettingPage.setDisable(true);
                MonthsWidget_SettingPage.setDisable(true);
            }
            adminPermission = perm2;
            if (!perm3) {
                AddNewUserWidget_SettingPage.setDisable(true);
            }
            if (!perm4) {
                deleteAllData.setDisable(true);
            }
            if (!perm5) {
                RemoveUserWidget_SettingPage.setDisable(true);
            }





    }


    // Take Action after user Change Permissions
    public void setPermmissionsTemp(){

        List<String> selectedPermissions = permissionsCheckList_ChangeDialog.getSelectionModel().getSelectedValues();

        for (String returnSelectedPermissionsIndex : selectedPermissions){
            if (returnSelectedPermissionsIndex.contains("تغيير الاسعار و الشهور")){

                PriceWidget_SettingPage.setDisable(false);
                MonthsWidget_SettingPage.setDisable(false);

            } else if (returnSelectedPermissionsIndex.contains("تعديل صلاحيات الحساب")) {
                adminPermission = true;

            }else if (returnSelectedPermissionsIndex.contains("اضافة حساب")) {
                AddNewUserWidget_SettingPage.setDisable(false);

            }else if (returnSelectedPermissionsIndex.contains("حذف قاعدة بيانات البرنامج")) {
                deleteAllData.setDisable(false);

            }else if (returnSelectedPermissionsIndex.contains("حذف الحسابات")) {
                RemoveUserWidget_SettingPage.setDisable(false);

            }
        }

    }


    /*----------------------------------------------------------------*/


    // set Icons Telegram & GitHub Buttons
    private void setIcons() {
        // Telegram Link Icon
        Telegram_LinkIcon.setSize(14);
        Telegram_LinkIcon.setColor(Color.DARKGRAY);
        Telegram_LinkIcon.setTextAlignment(TextAlignment.CENTER);
        Telegram_LinkIcon.setDescription("fas-arrow-up-right-from-square");

        // GitHub Link Icon
        GitHub_LinkIcon.setSize(14);
        GitHub_LinkIcon.setColor(Color.DARKGRAY);
        GitHub_LinkIcon.setTextAlignment(TextAlignment.CENTER);
        GitHub_LinkIcon.setDescription("fas-arrow-up-right-from-square");


        // Telegram Button Icon
        MFXFontIcon telegramIcon = createIcon("fab-telegram", 15, Color.WHITE);
        Telegram_Button.setGraphic(telegramIcon);

        // GitHub Button Icon
        MFXFontIcon githubIcon = createIcon("fab-github", 15, Color.WHITE);
        Github_Button.setGraphic(githubIcon);
    }

    // Helper method to create MFXFontIcon
    private MFXFontIcon createIcon(String description, double size, Color color) {
        MFXFontIcon icon = new MFXFontIcon();
        icon.setIconsProvider(IconsProviders.FONTAWESOME_BRANDS);
        icon.setSize(size);
        icon.setColor(color);
        icon.setTextAlignment(TextAlignment.CENTER);
        icon.setDescription(description);
        return icon;
    }

    @FXML
    private void telegramLink(){
        openLink("https://t.me/ad_kh1");
    }
    @FXML
    private void gitHubLink(){
        openLink("https://github.com/IbnTaymiyyah");
    }

    private void openLink(String url) {
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)){
            try {
                Desktop.getDesktop().browse(new URI(url));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }





    /*----------------------------------------------------------------*/

    @FXML
    private void signOut()  {
        settingModel.signOutToLogin(gride);
    }


    /*----------------------------------------------------------------*/


    /*  Back Buttons  */
    public void mainPage(){

        main.changeScene("fxml/MainPage.fxml");

    }


}
