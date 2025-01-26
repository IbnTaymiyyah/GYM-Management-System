package com.ibntaymiyya.gym.model;

import com.ibntaymiyya.gym.Main;
import com.ibntaymiyya.gym.Util.DBAddMember;
import com.ibntaymiyya.gym.Util.DBMembers;
import com.ibntaymiyya.gym.Util.Dialog;
import com.ibntaymiyya.gym.Util.SaveFileManager;
import com.ibntaymiyya.gym.controllers.MemberPageController;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.mfxresources.fonts.IconsProviders;
import io.github.palexdev.mfxresources.fonts.MFXFontIcon;
import io.github.palexdev.mfxresources.fonts.fontawesome.FontAwesomeSolid;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class MemberPageModel implements Initializable {


    private MemberPageController memberPageController;
    private DBMembers dbMembers;
    private DBAddMember dbAddMember = new DBAddMember();
    private Main main = new Main();

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MMMM d,yyyy");
    private static int qucikMonthBtn1 ;
    private static int qucikMonthBtn2 ;
    private static int qucikMonthBtn3;


    private int qucikPayBtn1 ;
    private int qucikPayBtn2 ;
    private int qucikPayBtn3 ;

    private Stage stage;


    public MemberPageModel() {
        this.dbMembers = new DBMembers();
        this.dbMembers.setMemberPageModel(this);
    }


    public void setMemberPageController(MemberPageController controller) {
        this.memberPageController = controller;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        quickBtnValue();
    }


    GridPane gride;
    public void setGridPane(GridPane gridpane){
        gride = gridpane;
    }


    private AddMemberGetterAndSetter reSub = new AddMemberGetterAndSetter();
    private AddMemberGetterAndSetter changeInfo = new AddMemberGetterAndSetter();





    /*--------------------------------------------------------------*/

    /*     Check Days Left  (Change Bar Color)   */

    public void checkDaysLeft(AnchorPane statusBar , String daysLeft , Label statusText2_label ,Label statusText1_label ,Label statusDaysLeft_label , String isOnHold, String holdDays){

        int left = Integer.parseInt(daysLeft);
        statusBar.getStyleClass().removeAll("Active","NotActive","GoExpire");

        if (isOnHold.equals("yes")){

            statusBar.getStyleClass().add("GoExpire");
            statusText1_label.setText(" الاشتراك مَجمد لديه ");
            statusText2_label.setText(" يوم ");
            statusDaysLeft_label.setText(holdDays);

        }else if (left <= 0){

            statusBar.getStyleClass().add("NotActive");
            statusText1_label.setText(" مدة الاشتراك انتهت ");
            statusText2_label.setText(" يوم متبقي ");
            statusDaysLeft_label.setText("0");


        } else if (left <= 7){
            statusBar.getStyleClass().add("GoExpire");
            statusText1_label.setText(" علي وشك الانتهاء ");
            statusText2_label.setText(" يوم متبقي ");
            statusDaysLeft_label.setText(String.valueOf(left));
        }else {
            statusBar.getStyleClass().add("Active");
            statusText1_label.setText(" ساري ");
            statusText2_label.setText(" يوم متبقي ");
            statusDaysLeft_label.setText(String.valueOf(left));
        }

    }


    /*--------------------------------------------------------------*/

    /*      Set 'لايوجد' for null Info    */
    public void nullInfo(String memberAge , String memberWeight,String memberHeight ,
                         Label mamberAge_label, Label memberWeight_label, Label member_Height_label){
        int age = Integer.parseInt(memberAge), weight = Integer.parseInt(memberWeight),
                height = Integer.parseInt(memberHeight);


        if (age == 0){
            mamberAge_label.setText("لايوجد");
        } else {
            mamberAge_label.setText(memberAge + " سنه ");
        }


        if (weight == 0) {
            memberWeight_label.setText("لايوجد");
        } else{
            memberWeight_label.setText(memberWeight + " ك.ج ");
        }

        if (height == 0) {
            member_Height_label.setText("لايوجد");
        } else {
                member_Height_label.setText(memberHeight + " سم ");
            }




    }


    /*--------------------------------------------------------------*/


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

    // DecoyAction
    EventHandler<ActionEvent> decoyActionHandler = event -> {
        System.out.println("This is A Decoy!!!!");
    };
    // Decoy VBox
    VBox decoyVBox;

    public void changeMemberSubDialog(GridPane grid , VBox VBOX_ChangeUserInfoDialog, MFXDatePicker DateStart_MemberDialog ,
                                      MFXTextField Pay_MemberDialog,MFXDatePicker DateEnd_MemberDialog,Label labelWrong_MemberDialog,int id) throws IOException {




        VBOX_ChangeUserInfoDialog.setVisible(true);

        EventHandler<ActionEvent> actionHandler = event -> {

            List<MFXTextField> textFields = Arrays.asList(DateEnd_MemberDialog, DateStart_MemberDialog, Pay_MemberDialog);
            validateTextFields(textFields);

            boolean isValid = textFields.stream().allMatch(textField -> textField != null && !textField.getText().trim().isEmpty());


            if (isValid) {

                // Directly validate the date pickers
                if (!validateDatePicker(DateStart_MemberDialog, DateEnd_MemberDialog)) {
                    labelWrong_MemberDialog.setText("لايمكن اختيار تاريخ قديم");
                    labelWrong_MemberDialog.setVisible(true);
                    DateStart_MemberDialog.setStyle("-fx-border-color: red;");
                    DateEnd_MemberDialog.setStyle("-fx-border-color: red;");
                } else {
                    Dialog.dialog.close();
                    reSub.setId(id);
                    reSub.setPay(Double.parseDouble(Pay_MemberDialog.getText()));
                    dbMembers.reSub(reSub);

                    setMemberInfo(id);
                    memberPageController.handelMemberInfo();
                    labelWrong_MemberDialog.setVisible(false);
                    DateStart_MemberDialog.setStyle("-fx-border-color: lightgray;");
                    DateEnd_MemberDialog.setStyle("-fx-border-color: lightgray;");
                    VBOX_ChangeUserInfoDialog.setVisible(false);
                }
            }
        };

        showDialog(stage, grid, 2, 1, actionHandler, "تعديل", "", "حفظ التعديل", VBOX_ChangeUserInfoDialog);
    }



    /*     Change Member Info      */
    public void changeMemberInfoDialog(GridPane grid, VBox VBOX_ChangeMemberInfoDialog , MFXTextField memberName_MemberDiloag, MFXTextField memberPhone_MemberDiloag, int id,
                                       MFXTextField memberWeight_MemberDiloag, MFXTextField memberHeight_MemberDiloag, MFXTextField memberAge_MemberDiloag, MFXComboBox<String> memberGender_MemberDiloag, Label labelWrongChangeInfo_MemberDialog ){

        VBOX_ChangeMemberInfoDialog.setVisible(true);



        EventHandler<ActionEvent> actionHandler = event -> {

            List<MFXTextField> textFields = Arrays.asList(memberName_MemberDiloag,memberGender_MemberDiloag);
            validateTextFields(textFields);
            boolean isValid = textFields.stream().allMatch(textField -> textField != null && !textField.getText().trim().isEmpty());


            if (isValid){
                Dialog.dialog.close();

                changeInfo.setId(id);
                changeInfo.setName(memberName_MemberDiloag.getText());
                changeInfo.setGender(memberGender_MemberDiloag.getValue());
                changeInfo.setPhone(memberPhone_MemberDiloag.getText());
                changeInfo.setAge(Integer.parseInt(memberAge_MemberDiloag.getText()));
                changeInfo.setHeight(Integer.parseInt(memberHeight_MemberDiloag.getText()));
                changeInfo.setWeight(Integer.parseInt(memberWeight_MemberDiloag.getText()));

                dbMembers.changeMemberInfo(changeInfo);

                VBOX_ChangeMemberInfoDialog.setVisible(false);
                labelWrongChangeInfo_MemberDialog.setVisible(false);

                setMemberInfo(id);
                memberPageController.handelMemberInfo();


            }else labelWrongChangeInfo_MemberDialog.setVisible(true);labelWrongChangeInfo_MemberDialog.setText("املئ الخانات الفارغه ");


        };



        showDialog(stage, grid, 2, 1, actionHandler, "تعديل", "", "حفظ التعديل", VBOX_ChangeMemberInfoDialog);

    }



    /*     DietPlan Dialog     */

    public void addNewDietPlanDialog(GridPane grid,MFXTextField TitelField_DietPlanDialog , TextArea TextArea_DietPlanDialog, VBox VBOX_DietPlanDialog ,int id )  {

        DietPlans dietPlans = new DietPlans();

        VBOX_DietPlanDialog.setVisible(true);

        TitelField_DietPlanDialog.clear();
        TextArea_DietPlanDialog.clear();


        EventHandler<ActionEvent> actionHandler = event -> {

            List<MFXTextField> textFields = Arrays.asList(TitelField_DietPlanDialog);
            validateTextFields(textFields);
            boolean isValid = textFields.stream().allMatch(textField -> textField != null && !textField.getText().trim().isEmpty());


            if (isValid && !TextArea_DietPlanDialog.getText().isEmpty()){
                Dialog.dialog.close();
                dietPlans.setMemberId(id);
                dietPlans.setDescription(TextArea_DietPlanDialog.getText());
                dietPlans.setName(TitelField_DietPlanDialog.getText());
                dbMembers.addNewDietPlan(dietPlans);


                TitelField_DietPlanDialog.clear();
                TextArea_DietPlanDialog.clear();
                VBOX_DietPlanDialog.setVisible(false);

                setMemberInfo(id);
                memberPageController.handelMemberInfo();
            }


        };



        showDialog(stage, grid, 2, 1, actionHandler, "اضافة نظام غذائي", "", "حفظ ",  VBOX_DietPlanDialog );


    }




    /*   Check Dialog   */
    public void updateMemberPageDialog(String headText ,String content , int dialogNum){
        showDialog(stage, gride, 1, dialogNum, decoyActionHandler, headText, content, "", decoyVBox);
    }




    /*--------------------------------------------------------------*/
        /*   DietPlans ListView    */


    public void setupDietPlansList(ListView<DietPlans> dietListView_MemberPage, int id , GridPane gride ,MFXTextField TitelField_DietPlanDialog , TextArea TextArea_DietPlanDialog, VBox VBOX_DietPlanDialog) {
        // Fetch diet plans from the database
        List<DietPlans> dietPlans = dbMembers.getDietPlans(id);

        String resourcePath = "../css/ListView.css";
        URL resorceUrl = getClass().getResource(resourcePath);
        if (resorceUrl == null){
            System.out.println("Resource Not Found" + resourcePath);
        }else {
            dietListView_MemberPage.getStylesheets().add(resorceUrl.toExternalForm());
        }

        // Set the cell factory for the MFXListView
        dietListView_MemberPage.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(DietPlans dietPlan, boolean empty) {
                super.updateItem(dietPlan, empty);
                if (empty || dietPlan == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    Label name = new Label(dietPlan.getName());
                    name.setWrapText(true);
                    name.getStyleClass().add("diet-name");
                    name.setTextOverrun(OverrunStyle.ELLIPSIS);

                    Label description = new Label(dietPlan.getDescription());
                    description.getStyleClass().add("diet-description");
                    description.setWrapText(true);



                    HBox buttonsBox = new HBox(5);
                    Button copyBtn = new Button(" نسخ ");
                    copyBtn.setGraphic(new MFXFontIcon("fas-copy", 16, Color.WHITE));
                    copyBtn.getStyleClass().add("action-button");
                    copyBtn.setOnAction(event -> copyToClipboard(dietPlan.getName(), dietPlan.getDescription()));


                    Button editBtn = new Button(" تعديل ");
                    editBtn.setGraphic(new MFXFontIcon("fas-pen", 16, Color.WHITE));
                    editBtn.getStyleClass().add("action-button");
                    editBtn.setOnAction(event -> editDietPlan(id,dietPlan , gride ,TitelField_DietPlanDialog , TextArea_DietPlanDialog, VBOX_DietPlanDialog));

                    Button deleteBtn = new Button(" حذف ");
                    deleteBtn.setGraphic(new MFXFontIcon("fas-trash-can", 16, Color.WHITE));
                    deleteBtn.getStyleClass().add("button-Remove");
                    deleteBtn.setOnAction(event -> deleteDietPlan(dietPlan,id,gride));

                    buttonsBox.getChildren().addAll(copyBtn, editBtn, deleteBtn);
                    buttonsBox.setAlignment(Pos.CENTER_LEFT);


                    // Use a Pane for flexible layout and alignment
                    Pane spacer = new Pane();  // For flexible spacing
                    HBox.setHgrow(spacer, Priority.ALWAYS);


                    HBox contentGroup = new HBox(15, name, description, spacer, buttonsBox);
                    contentGroup.setAlignment(Pos.CENTER_LEFT);
                    contentGroup.getStyleClass().add("diet-item");
                    contentGroup.prefWidthProperty().bind(dietListView_MemberPage.widthProperty().subtract(40));


                    setGraphic(contentGroup);
                }
            }
        });

        dietListView_MemberPage.setFixedCellSize(0);
        dietListView_MemberPage.setSelectionModel(null);


        dietListView_MemberPage.getItems().clear();
        dietListView_MemberPage.getItems().addAll(dietPlans);
    }




    private void deleteDietPlan(DietPlans dietPlan , int id , GridPane grid) {

        EventHandler<ActionEvent> actionHandler = event -> {
            Dialog.dialog.close();
            dbMembers.deleteDietPlans(dietPlan.getDietId());
            setMemberInfo(id);
            memberPageController.handelMemberInfo();
        };

        showDialog(stage, grid, 2, 4, actionHandler, "تنبيه", "هل تريد حذف هذا النظام الغذائي "+ "\n" + dietPlan.getName(), "حذف ",  decoyVBox );


    }

    private void editDietPlan(int id,DietPlans dietPlan , GridPane grid ,MFXTextField TitelField_DietPlanDialog , TextArea TextArea_DietPlanDialog, VBox VBOX_DietPlanDialog) {

        VBOX_DietPlanDialog.setVisible(true);

        TitelField_DietPlanDialog.setText(dietPlan.getName());
        TextArea_DietPlanDialog.setText(dietPlan.getDescription());

        EventHandler<ActionEvent> actionHandler = event -> {

            List<MFXTextField> textFields = Arrays.asList(TitelField_DietPlanDialog);
            validateTextFields(textFields);
            boolean isValid = textFields.stream().allMatch(textField -> textField != null && !textField.getText().trim().isEmpty());


            if (isValid && !TextArea_DietPlanDialog.getText().isEmpty()){
                Dialog.dialog.close();

                dbMembers.editDietPlans(dietPlan.getDietId() , TitelField_DietPlanDialog.getText(), TextArea_DietPlanDialog.getText());

                TitelField_DietPlanDialog.clear();
                TextArea_DietPlanDialog.clear();
                VBOX_DietPlanDialog.setVisible(false);

                setMemberInfo(id);
                memberPageController.handelMemberInfo();
            }


        };



        showDialog(stage, grid, 2, 1, actionHandler, "تعديل نظام غذائي", "", "حفظ ",  VBOX_DietPlanDialog );
    }

    private void copyToClipboard(String name , String desc) {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putString(name +" \n "+ desc);
        clipboard.setContent(content);
    }



    /*--------------------------------------------------------------*/
        /*   DatePicker   */
    private boolean validateDatePicker(MFXDatePicker DateStart_MemberDialog, MFXDatePicker DateEnd_MemberDialog) {

        // Get the current time
        LocalTime localTime = LocalTime.now();
        String simpleFormatter = localTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
        LocalDate currentDate = LocalDate.now();

        // Validate Start Date
        if (DateStart_MemberDialog.getText() != null && !DateStart_MemberDialog.getText().isEmpty()) {
            String fullInputStart = DateStart_MemberDialog.getText() + " " + simpleFormatter;
            String formattedStartDate = dbAddMember.convertDateFormat(fullInputStart);

            if (formattedStartDate != null) {
                String formattedDateOnlyStart = formattedStartDate.substring(0, 10);
                LocalDate inputStartDate = LocalDate.parse(formattedDateOnlyStart, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                if (inputStartDate.isBefore(currentDate)) {
                    return false;
                }else {
                    reSub.setDateStart(formattedStartDate);
                }
            }
        }

        // Validate End Date
        if (DateEnd_MemberDialog.getText() != null && !DateEnd_MemberDialog.getText().isEmpty()) {
            String fullInputEnd = DateEnd_MemberDialog.getText() + " " + simpleFormatter;
            String formattedEndDate = dbAddMember.convertDateFormat(fullInputEnd);

            if (formattedEndDate != null) {
                String formattedDateOnlyEnd = formattedEndDate.substring(0, 10);
                LocalDate inputEndDate = LocalDate.parse(formattedDateOnlyEnd, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                if (inputEndDate.isBefore(currentDate)) {
                    return false;
                }else {
                    reSub.setDateEnd(formattedEndDate);
                }
            }
        }

        // If both dates are valid
        return true;
    }


    /*--------------------------------------------------------------*/

    public void quickBtnValue(){
        qucikMonthBtn1 = Integer.parseInt(Objects.requireNonNull(SaveFileManager.getText("btnMonth1")));
        qucikMonthBtn2 = Integer.parseInt(Objects.requireNonNull(SaveFileManager.getText("btnMonth2")));
        qucikMonthBtn3 = Integer.parseInt(Objects.requireNonNull(SaveFileManager.getText("btnMonth3")));

        qucikPayBtn1 = Integer.parseInt(Objects.requireNonNull(SaveFileManager.getText("btnPay1")));
        qucikPayBtn2 = Integer.parseInt(Objects.requireNonNull(SaveFileManager.getText("btnPay2")));
        qucikPayBtn3 = Integer.parseInt(Objects.requireNonNull(SaveFileManager.getText("btnPay3")));
    }

    /*      QuickBtn Months      */


    public QuickButtonsInfo  quickButtons1(LocalDate date){
        return updateQuickBtnInfoMonths(qucikMonthBtn1 ,date);
    }
    public QuickButtonsInfo  quickButtons2(LocalDate date){
        return updateQuickBtnInfoMonths(qucikMonthBtn2 ,date);
    }
    public QuickButtonsInfo  quickButtons3(LocalDate date){
        return updateQuickBtnInfoMonths(qucikMonthBtn3 ,date);
    }

    private QuickButtonsInfo updateQuickBtnInfoMonths(int quickMonth ,LocalDate date){
        QuickButtonsInfo info = new QuickButtonsInfo();
        info.setQuickMonth(quickMonth);
        info.setFormattedDateStart(date.format(dateTimeFormatter));
        info.setFormattedDateEnd(date.plusMonths(info.getQuickMonth()).format(dateTimeFormatter));
        return info;
    }

    /*      QuickBtn Pay      */

    public QuickButtonsInfo quickButtonsPay1(){
        return updateQuickBtnInfoPay(qucikPayBtn1);
    }

    public QuickButtonsInfo quickButtonsPay2(){return updateQuickBtnInfoPay(qucikPayBtn2);}

    public QuickButtonsInfo quickButtonsPay3(){
        return updateQuickBtnInfoPay(qucikPayBtn3);
    }

    private QuickButtonsInfo updateQuickBtnInfoPay(int payAmount){
        QuickButtonsInfo info = new QuickButtonsInfo();
        info.setQuickPay(payAmount);
        return info;
    }

    /*--------------------------------------------------------------*/


    /*   Filters Typed in TextField Numbers Only &  Letters Only   */
    public void addNumberOnlyFilter(MFXTextField textField) {
        textField.addEventFilter(KeyEvent.KEY_TYPED, event -> {
            String input = event.getCharacter();
            if (!input.matches("\\d")) {
                event.consume();
            }
        });
    }

    public void addTextOnlyFilter(MFXTextField textField){
        textField.addEventFilter(KeyEvent.KEY_TYPED,event -> {
            String input = event.getCharacter();
            if (!input.matches("[a-zA-Z\\u0600-\\u06FF ]")){
                event.consume();
            }
        });
    }

    private void validateTextFields(List<MFXTextField> textFields){
        for (MFXTextField textField : textFields){
            if (textField == null || textField.getText().trim().isEmpty() ){
                textField.setStyle("-fx-border-color: red;");
            }else {
                textField.setStyle("-fx-border-color: lightgray;");
            }
        }
    }


    /*--------------------------------------------------------------*/

    /*       Remove User      */

    public void getMemberIdToRemove( int id){
        AddMemberGetterAndSetter memberName = dbMembers.getMemberByIdToMembersPage(id);
        String name = memberName != null ? memberName.getName() : null;


        EventHandler<ActionEvent> actionHandler = event -> {
            Dialog.dialog.close();
            dbMembers.removeMember(id);
            main.changeScene("fxml/AllMembers.fxml");

        };

        if (stage != null && !stage.isShowing()) {
            stage.show();
        }

        if (id == -1 || name == null) {
            showDialog(stage, gride, 1, 5, decoyActionHandler, "خطاء", "اختر مستخدم للحذف", "", decoyVBox);
        } else {
            showDialog(stage, gride, 2, 4, actionHandler, "تحذير", "   هل تريد المتابعة و حذف المتدرب : \n  " + name, "متابعة وحذف", decoyVBox);
        }
    }







    /*--------------------------------------------------------------*/
    /*     Update Member Info     */

    private void setMemberInfo(int memberId){
        // get data Form DB to AddMemberGetterAndSetter
        AddMemberGetterAndSetter member = dbMembers.getMemberByIdToMembersPage(memberId);


        //  Set Data
        MemberPageController.name = member.getName();
        MemberPageController.memberId = String.valueOf(memberId);
        MemberPageController.memberAge = String.valueOf(member.getAge());
        MemberPageController.memberGender = member.getGender();
        MemberPageController.memberPhone = String.valueOf(member.getPhone());
        MemberPageController.daysLeft = String.valueOf(member.getDaysLeft());
        MemberPageController.memberHeight = String.valueOf(member.getHeight());
        MemberPageController.memberWeight = String.valueOf(member.getWeight());
        MemberPageController.totalPayed = String.valueOf(member.getTotalPayed());
        MemberPageController.holdDays = String.valueOf(member.getHoldDays());
        MemberPageController.pay = String.valueOf(member.getPay());
        MemberPageController.isOnHold = member.getIsOnHold();
        MemberPageController.dateStart = member.getDateStart();
        MemberPageController.dateEnd = member.getDateEnd();




    }




    /*--------------------------------------------------------------*/

    /*  Observable List for ComboBox Btw Genders Male or Female  */
    public ObservableList<String> createGenderList() {
        ObservableList<String> genderList = FXCollections.observableArrayList();

        genderList.add("ذكر");
        genderList.add("انثي");

        return genderList;
    }



    /*--------------------------------------------------------------*/
    /*   Hold & Resume Button */

    public void holdSubscription(int memberId , int daysToHold){
        dbMembers.holdSubscription(memberId,daysToHold);
        setMemberInfo(memberId);
        memberPageController.handelMemberInfo();
    }

    public void resumeSubscription(int memberId){
        dbMembers.resumeSubscription(memberId);
        setMemberInfo(memberId);
        memberPageController.handelMemberInfo();
    }






}
