package com.ibntaymiyya.gym.model;

import com.ibntaymiyya.gym.Main;
import com.ibntaymiyya.gym.Util.*;
import com.ibntaymiyya.gym.controllers.Setting;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.materialfx.validation.Constraint;
import io.github.palexdev.materialfx.validation.Severity;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class SettingModel {


    private MFXComboBox<String> userComboBox;
    private Dialog dialog;
    private Setting setting;
    public DBUsers dbUsers;
    public DBMembers dbMembers;
    public DBLoginUser dbLoginUser;
    private DBCheckRunning dbCheckRunning;
    private static final PseudoClass INVALID_PSEUDO_CLASS = PseudoClass.getPseudoClass("invalid");

    Stage stage;
    GridPane gride;

    public SettingModel(Setting setting) {
        this.setting = setting;
        this.dbLoginUser = new DBLoginUser();
        this.dbLoginUser.setSettingModel(this);

        this.dbCheckRunning = new DBCheckRunning();
        this.dbCheckRunning.settingModel(this);

        this.dbUsers = new DBUsers();
        this.dbUsers.setSettingModel(this);

        this.dbMembers = new DBMembers();
        this.dbMembers.setSettingModel(this);
    }

    public void setGridPane(GridPane gridpane){
        gride = gridpane;
    }



    /*------------------------------------------------------------------------*/

    /*      Change Size of Hbox when User click on Change Serial Number         */

    private void changeSizeLayer(int prefHeight , int prefWidth, HBox hBox){
        if (prefWidth == 0 && prefHeight == 0){

            hBox.setPrefHeight(prefHeight);
            hBox.setPrefWidth(prefWidth);
            hBox.setVisible(false);
            hBox.setDisable(true);

        }else {
            hBox.setPrefHeight(prefHeight);
            hBox.setPrefWidth(prefWidth);
            hBox.setVisible(true);
            hBox.setDisable(false);

        }


    }

    public void showLayerHbox(int prefHeight , int prefWidth, HBox hBox){
        changeSizeLayer(prefHeight,prefWidth,hBox);

    }




    /*------------------------------------------------------------------------*/


    // Check if Permission is Selected Form CheckList and return Value(Index) to Same Permissions in Database
    public List<Integer> permissionsIndex(MFXCheckListView<String> checkList){
        List<String> selectedPermissions = checkList.getSelectionModel().getSelectedValues();
        List<Integer> selectedPermissionIndex = new ArrayList<>();
        for (String returnSelectedPermissionsIndex : selectedPermissions){
            if (returnSelectedPermissionsIndex.contains("تغيير الاسعار و الشهور")){
                selectedPermissionIndex.add(1);

            } else if (returnSelectedPermissionsIndex.contains("تعديل صلاحيات الحساب")) {
                selectedPermissionIndex.add(2);
            }else if (returnSelectedPermissionsIndex.contains("اضافة حساب")) {
                selectedPermissionIndex.add(3);
            }else if (returnSelectedPermissionsIndex.contains("حذف قاعدة بيانات البرنامج")) {
                selectedPermissionIndex.add(4);
            }else if (returnSelectedPermissionsIndex.contains("حذف الحسابات")) {
                selectedPermissionIndex.add(5);
            }
        }

        return selectedPermissionIndex;
    }




    /*----------------------------------------------------------------*/


    /*         Text & Password Fields Validation ,and Filter          */

    public void passwordFieldValidation(MFXPasswordField passwordField , Label validationLabel){
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
            }
        });

        passwordField.delegateFocusedProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue && !newValue){
                List<Constraint> constraints = passwordField.validate();
                if (!constraints.isEmpty()){
                passwordField.pseudoClassStateChanged(INVALID_PSEUDO_CLASS,true);
                validationLabel.setText(constraints.get(0).getMessage());
                validationLabel.setVisible(true);
                }
            }
        });
    }

    public void textFieldValidation(MFXTextField textField , Label validationLabel,int charNum){
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
            }
        });

        textField.delegateFocusedProperty().addListener((observable, oldValue, newValue) -> {
                if (oldValue && !newValue){
                List<Constraint> constraints = textField.validate();
                if (!constraints.isEmpty()){
                textField.pseudoClassStateChanged(INVALID_PSEUDO_CLASS,true);
                validationLabel.setText(constraints.get(0).getMessage());
                validationLabel.setVisible(true);
                }
            }
        });
    }



    // Filter For Text Fields & Password Fields
    public void textOnlyFilter(MFXTextField textField){
        textField.addEventFilter(KeyEvent.KEY_TYPED, event -> {
            String input = event.getCharacter();
            if (!input.matches("[a-zA-Z\\u0600-\\u06FF ]")){
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

    /*       Remove User      */
    public void getUserNameToRemove(String userNameToRemove, MFXFilterComboBox<String> comboBox , GridPane gride){
        EventHandler<ActionEvent> actionHandler = event -> {
            Dialog.dialog.close();

            dbUsers.removeUser(userNameToRemove);

            comboBox.clearSelection();

            dbUsers.getAllUsers(comboBox);

        };

        if (userNameToRemove.equals("") || Objects.equals(userNameToRemove ,"null")){
            showDialog(stage,gride,1,5,decoyActionHandler,"خطاء","اختر مستخدم للحذف","",decoyVBox);
        } else showDialog(stage, gride, 2, 4, actionHandler, "تحذير", "   هل تريد المتابعة و حذف المستخدم \n  " + userNameToRemove, "متابعة وحذف", decoyVBox);

    }


    /*       Check if User is Remove Success        */
    public void userIsRemovedDiloag(boolean check){

        if (check) {
            showDialog(stage, gride, 1, 2, decoyActionHandler, "تم", "تم حذف المستخدم ", "", decoyVBox);
        }else showDialog(stage,gride,1,5,decoyActionHandler,"خطاء","حدث خطاء أثناء حذف المستخدم","",decoyVBox);

    }

    public void userIsNotFoundDilaog(){
        showDialog(stage,gride,1,5,decoyActionHandler,"خطاء","هذا المستخدم غير موجود","",decoyVBox);
    }




    /*----------------------------------------------------------------*/

    /*
     * 1 -> Dialog Node (VBox)
     * 2 -> Dialog Check
     * 3 -> Dialog Info
     * 4 -> Dialog warn
     * 5 -> Dialog Error
     * Default -> Dialog General
     */


    /*      Show Dialogs       */

    private void showDialog(Stage stage, GridPane gride, int BtnNum, int witchDialog, EventHandler eventHandler , String headerText , String contentText , String okButtonName, VBox vBox){
        Dialog dialog = new Dialog(new DialogConfig(stage, gride, BtnNum,witchDialog,eventHandler,headerText,contentText,okButtonName,vBox));
        dialog.showDialog();
    }

    /*      Dialogs Events      */

    // DecoyAction
    EventHandler<ActionEvent> decoyActionHandler = event -> {
        System.out.println("This is A Decoy!!!!");
    };
    // Decoy VBox
    VBox decoyVBox;

    /*----------------------------------------------------------------*/


    // Change Current User Info



    public void changeUserInfoDialog(GridPane gride, VBox VBOX_ChangeUserInfoDialog, VBox VBOX_AdminPermissionDialog, MFXTextField textFieldUserName_AdminPermissionDialog
            , MFXPasswordField passwordFieldPass_AdminPermissionDialog, Label labelError_AdminPermissionDialog, Boolean adminPermission
            , MFXTextField NameOfUser_ChangeDialog ,MFXPasswordField PasswordOfUser_ChangeDialog , Label labelWrongChangeUserName_SettingDialog ,Label labelWrongChangeUserPass_SettingDialog ,MFXCheckListView<String> permissionsCheckList_ChangeDialog   ) {
        AtomicBoolean isAdmin = new AtomicBoolean(false);

        VBOX_ChangeUserInfoDialog.setVisible(true);
        VBOX_AdminPermissionDialog.setVisible(true);

        EventHandler<ActionEvent> updateUserInfo = event -> {

            if (dbCheckRunning.checkMySqlService()) {
                String oldName = SaveFileManager.getText("currentuser");
                String userName = NameOfUser_ChangeDialog.getText();
                String password = PasswordOfUser_ChangeDialog.getText();


                List<Integer> selectedPermissionIndex = permissionsIndex(permissionsCheckList_ChangeDialog);


                if (userName.isEmpty() || password.isEmpty() || userName.length() <= 4 || password.length() <= 4) {
                    labelWrongChangeUserName_SettingDialog.setText("خطأ في البيانات المدخلة");
                    labelWrongChangeUserName_SettingDialog.setVisible(true);
                    return;
                }

                if (isUserNameTaken(userName) && !userName.equals(oldName)) {
                    System.out.println("User name is already taken.");
                    labelWrongChangeUserName_SettingDialog.setText("هذا الاسم موجود بالفعل، اختر اسم آخر");
                    labelWrongChangeUserName_SettingDialog.setVisible(true);
                    return;
                }

                    System.out.println("Is User Name Taken: " + isUserNameTaken(userName));



                    System.out.println("Database Connection: " + dbCheckRunning.checkMySqlService());
                    if (dbCheckRunning.checkMySqlService()) {

                        try {

                            dbUsers.updateUser(oldName,userName, password, selectedPermissionIndex);
                            getUserData(userName);
                            labelWrongChangeUserName_SettingDialog.setVisible(false);
                            Dialog.dialog.close();

                        } catch (SQLException e) {

                            Dialog.dialog.close();
                            System.err.println("Error updating user information: " + e.getMessage());
                            e.printStackTrace();
                        }
                    }


            }
        };


        EventHandler<ActionEvent> checkAgainHandler = event -> {
            String userName = textFieldUserName_AdminPermissionDialog.getText();
            String password = passwordFieldPass_AdminPermissionDialog.getText();

            if (dbCheckRunning.checkMySqlService()) {

                if (dbLoginUser.isUserExist(userName, password)) {
                    isAdmin.set(dbLoginUser.isUserAdmin(userName));

                    if (isAdmin.get() || adminPermission) {
                        Dialog.dialog.close();
                        showDialog(stage, gride, 2, 1, updateUserInfo, "تعديل", "", "حفظ التعديل", VBOX_ChangeUserInfoDialog);
                        textFieldUserName_AdminPermissionDialog.clear();
                        passwordFieldPass_AdminPermissionDialog.clear();
                        VBOX_AdminPermissionDialog.setVisible(false);
                    } else {
                        labelError_AdminPermissionDialog.setVisible(true);
                        labelError_AdminPermissionDialog.setText("الحساب الذي ادخلته لا يمتلك صلاحية الوصول");
                    }
                } else {
                    labelError_AdminPermissionDialog.setVisible(true);
                    labelError_AdminPermissionDialog.setText("هذا الحساب غير موجود");
                }
            }
        };

        EventHandler<ActionEvent> actionHandler = event -> {
            Dialog.dialog.close();
            VBOX_ChangeUserInfoDialog.setVisible(false);
            VBOX_AdminPermissionDialog.setVisible(false);
        };

        if (!adminPermission) {
            showDialog(stage, gride, 2, 1, checkAgainHandler, "لا تمتلك صلاحية الوصول", "", "متابعة", VBOX_AdminPermissionDialog);
        } else {
           showDialog(stage, gride, 2, 1, updateUserInfo, "تعديل", "", "حفظ التعديل", VBOX_ChangeUserInfoDialog);
            textFieldUserName_AdminPermissionDialog.clear();
            passwordFieldPass_AdminPermissionDialog.clear();
        }
    }




    // Serial Number Dialog
    public void checkSerialNumberDialog(GridPane gride){
        showDialog(stage,gride,1,2,decoyActionHandler,"تم","ايوااا كدا ","",decoyVBox);
    }

    // ( Check & Error ) Name And QuickButtons in Setting Page
    public void checkNameAndQuickBtnsDilaog(GridPane gride){
        showDialog(stage,gride,1,2,decoyActionHandler,"تم","تم حفظ التغيرات بنجاح","",decoyVBox);
    }
    public void errorNameAndQuickBtnsDilaog(GridPane gride){
        showDialog(stage,gride,1,5,decoyActionHandler,"خطاء","تحقق من اسم الصاله الرياضيه","",decoyVBox);
    }

    // ( Check & Error ) Add New User With Permissions
    public void checkAddNewUserDialog(GridPane gride){
        showDialog(stage,gride,1,2,decoyActionHandler,"تم","تم اضافة المستخدم ","",decoyVBox);
    }
    public void errorAddNewUserDialog(GridPane gride){
        showDialog(stage,gride,1,5,decoyActionHandler,"خطاء","تحقق من نص اسم المستخدم او كلمه السر","",decoyVBox);
    }


    // Get user Data and set into SaveFile

    private void getUserData(String userName) {
        UserDataGetterAndSetter userData = dbLoginUser.getUserDetails(userName);

        SaveFileManager.setText("currentuser", userName);
        SaveFileManager.setText("currentuserpassword", userData.getPassword());

        Map<Integer, Boolean> permissionsMap = userData.getPermissionsMap();

        if (permissionsMap != null) {
            for (Map.Entry<Integer, Boolean> entry : permissionsMap.entrySet()) {
                SaveFileManager.setText("perm" + entry.getKey(), entry.getValue().toString());
            }
        } else {
            for (int i = 1; i <= 5; i++) {
                SaveFileManager.setText("perm" + i, "false");
            }
        }

        setting.getCurrentUserPermissions();
        setting.setPermmissionsTemp();
    }




    /*----------------------------------------------------------------*/

    /*    DELETE ALL Data and Refresh     */
    public void deleteAllData(GridPane gride){
        EventHandler<ActionEvent> actionHandler = event -> {
            Dialog.dialog.close();
           if( dbMembers.deleteAllDataFromAllTables("gym")){
               showDialog(stage,gride,1,2,decoyActionHandler,"تم","تم حذف قاعدة البيانات ","",decoyVBox);

           }else {
               showDialog(stage,gride,1,5,decoyActionHandler,"خطاء","لم يتم حذف قاعدة البيانات","",decoyVBox);

           }
        };


        showDialog(stage,gride,2,4,actionHandler, "تحذير","هل تريد حذف قاعدة بيانات البرنامج "+"\nسيتم حذف كل شئ","متابعة و حذف ",decoyVBox);

    }




    /*--------------------------------------------------------------*/

    /*   Sign out , Close Main Page Stage and Open Login Stage   */

    public void signOutToLogin(GridPane grid)  {

        EventHandler<ActionEvent> actionHandler = event -> {
            Dialog.dialog.close();
            Main.getInstance().openPrimaryStage();
            Main.mainStage.close();


        };


        showDialog(stage,grid,2,4,actionHandler,"تنبية", "هل تريد تسجيل الخروج من الحساب الحالي ؟","تسجيل خروج",decoyVBox);
    }



    /*--------------------------------------------------------------*/


    /*     UserName is Already Taken     */
    public boolean isUserNameTaken(String userName){
        return dbUsers.isUserNameTaken(userName);
    }


    /*----------------------------------------------------------------*/










}
