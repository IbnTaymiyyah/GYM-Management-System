package com.ibntaymiyya.gym.controllers;

import com.ibntaymiyya.gym.Main;
import com.ibntaymiyya.gym.Util.DBExportAndImportInfo;
import com.ibntaymiyya.gym.Util.DBMembers;
import com.ibntaymiyya.gym.Util.SaveFileManager;
import com.ibntaymiyya.gym.model.*;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.mfxresources.fonts.IconsProviders;
import io.github.palexdev.mfxresources.fonts.MFXFontIcon;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class MemberPageController implements Initializable{

    @FXML
    private AnchorPane MainPane_MemberPage;

    @FXML
    private MFXButton back_btn_memberPage;

    @FXML
    private Label LabelCurrentSubPayed_MemberPage;

    @FXML
    private Label LabelToatalPayed_MemberPage;

    @FXML
    private Button changeInfo_btn;

    @FXML
    private Label dateEnd_label;

    @FXML
    private Label dateStart_Label;

    @FXML
    private Button exportMemberInfo_btn;
    @FXML
    private Button removeMember_btn;

    @FXML
    private Button holdSub_Btn;

    @FXML
    private Label mamberAge_label;

    @FXML
    private Label memberId_label;

    @FXML
    public Label memberName_label;

    @FXML
    private Label memberPhone_label;

    @FXML
    private Label memberWeight_label;

    @FXML
    private Label member_Height_label;

    @FXML
    private Button reSub_btn;

    @FXML
    private Label scince_label;

    @FXML
    private Label statusDaysLeft_label;

    @FXML
    private Label statusText1_label;

    @FXML
    private Label statusText2_label;
    @FXML
    private Label dateTo;
    @FXML
    private AnchorPane statusBar;
    @FXML
    private Label memberGender_label;
    @FXML
    private Button daietBtn;
    @FXML
    private GridPane grid;

    @FXML
    private ListView<DietPlans> dietListView_MemberPage;

    /*--------------------------------------------------------*/

    /*      Dialog reSub     */
    @FXML
    private MFXDatePicker DateEnd_MemberDialog;

    @FXML
    private MFXDatePicker DateStart_MemberDialog;

    @FXML
    private MFXTextField Pay_MemberDialog;

    @FXML
    private VBox VBOX_ChangeMemberSubDialog;

    @FXML
    private Label labelWrong_MemberDialog;

    @FXML
    private MFXButton quickBtn1_MemberDialog;

    @FXML
    private MFXButton quickBtn2_MemberDialog;

    @FXML
    private MFXButton quickBtn3_MemberDialog;

    @FXML
    private MFXButton quickBtnPay1_MemberDialog;

    @FXML
    private MFXButton quickBtnPay2_MemberDialog;

    @FXML
    private MFXButton quickBtnPay3_MemberDialog;



/*--------------------------------------------------------*/

    /*     Dialog Change Info      */

    @FXML
    private VBox VBOX_ChangeMemberInfoDialog;

    @FXML
    private Label labelWrongChangeInfo_MemberDialog;

    @FXML
    private MFXTextField memberAge_MemberDiloag;

    @FXML
    private MFXComboBox<String> memberGender_MemberDiloag;

    @FXML
    private MFXTextField memberHeight_MemberDiloag;

    @FXML
    private MFXTextField memberName_MemberDiloag;

    @FXML
    private MFXTextField memberPhone_MemberDiloag;

    @FXML
    private MFXTextField memberWeight_MemberDiloag;


    /*--------------------------------------------------------*/
    /*     DietPlan Dialog     */


    @FXML
    private TextArea TextArea_DietPlanDialog;

    @FXML
    private MFXTextField TitelField_DietPlanDialog;

    @FXML
    private VBox VBOX_DietPlanDialog;




    /*--------------------------------------------------------*/





    Main main =new Main();

    private DBExportAndImportInfo dbExportAndImportInfo;
    private DBMembers dbMembers ;
    private MemberPageModel memberPageModel;
    private AllMembersModel allMembersModel;


    public static int hold;
    private AddMemberGetterAndSetter member;


    public MemberPageController(){
        this.memberPageModel = new MemberPageModel();
    this.memberPageModel.setMemberPageController(this);

    this.dbMembers = new DBMembers();
    this.dbMembers.setMemberPageController(this);

    this.dbExportAndImportInfo = new DBExportAndImportInfo();
    }

    public void setMemberPageModel(MemberPageModel model) {
        this.memberPageModel = model;
    }
    public void setAllMembersModel(AllMembersModel model) {
        this.allMembersModel = model;
    }




    public static String name;
    public static String memberId;
    public static String memberPhone;
    public static String dateStart;
    public static String dateEnd;
    public static String memberAge;
    public static String memberWeight;
    public static String memberHeight;
    public static String daysLeft;
    public static String memberGender;
    public static String totalPayed;
    public static String pay;
    public static String holdDays;
    public static String isOnHold;








    @Override
    public void initialize(URL location, ResourceBundle resources) {



        fieldFilter();
        selecteGender();
        handelMemberInfo();
        setBtnNameDialog();
        initializeButtonsIcon();
        memberPageModel.quickBtnValue();
        memberPageModel.setGridPane(grid);
        dbExportAndImportInfo.setGridPane(grid);


    }


    public void handelMemberInfo(){
        memberPageModel.checkDaysLeft(statusBar,daysLeft,statusText2_label,statusText1_label,statusDaysLeft_label , isOnHold , holdDays);
        memberPageModel.nullInfo(memberAge , memberWeight, memberHeight,mamberAge_label,memberWeight_label,member_Height_label);

        holdSub_Btn.setText("تجميد الاشتراك");

        if (Integer.parseInt(statusDaysLeft_label.getText()) <= 0){
            holdSub_Btn.setDisable(true);

        }

        if (memberId_label != null) memberId_label.setText( "#" + memberId);
        if (memberName_label != null) memberName_label.setText(name);
        if (memberPhone_label != null) memberPhone_label.setText(memberPhone);
        if (dateStart_Label != null) dateStart_Label.setText(dateStart);
        if (dateEnd_label != null) dateEnd_label.setText(dateEnd);
        if (memberGender_label != null) memberGender_label.setText(memberGender);
        if (LabelToatalPayed_MemberPage != null) LabelToatalPayed_MemberPage.setText(totalPayed);
        if (LabelCurrentSubPayed_MemberPage != null) LabelCurrentSubPayed_MemberPage.setText(pay);


        // Dialog
        memberName_MemberDiloag.setText(name);

        if (memberGender.equals("ذكر")) {
            memberGender_MemberDiloag.getSelectionModel().selectFirst();
        } else {
            memberGender_MemberDiloag.getSelectionModel().selectLast();
        }


        memberAge_MemberDiloag.setText(memberAge);
        memberPhone_MemberDiloag.setText(memberPhone);
        memberHeight_MemberDiloag.setText(memberHeight);
        memberWeight_MemberDiloag.setText(memberWeight);

        memberPageModel.setupDietPlansList(dietListView_MemberPage, Integer.parseInt(memberId) , grid ,  TitelField_DietPlanDialog , TextArea_DietPlanDialog , VBOX_DietPlanDialog );

        if(isOnHold.equals("yes")){
            dateEnd_label.setText("-/-/-");
            dateStart_Label.setText("-/-/-");
            holdSub_Btn.setText("استكمال الاشتراك");
        }


        initializeButtonsIcon();


    }


    // Hold Button
    @FXML
    private void memberIsOnHold(){
        if (isOnHold.equals("yes")) {
            memberPageModel.resumeSubscription(Integer.parseInt(memberId));
        } else {
            if (memberId == null || memberId.isEmpty()) {
                System.out.println("Invalid memberId: " + memberId);
                return;
            }

            int daysToHold = Integer.parseInt(daysLeft);
            memberPageModel.holdSubscription(Integer.parseInt(memberId), daysToHold);
        }
    }

    @FXML
    private void exportToCSV(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("حفظ بيانات المتدرب ");
        fileChooser.setInitialFileName(name + " بتاريخ " + LocalDate.now());
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File selectedFile = fileChooser.showSaveDialog(MainPane_MemberPage.getScene().getWindow());

        if (selectedFile != null){
            String filePath = selectedFile.getAbsolutePath();
            dbExportAndImportInfo.exportMemberToCSV(filePath,Integer.parseInt(memberId) , grid);

        }
    }


    // ComboBox For Select Gender
    private void selecteGender() {
        ObservableList<String> genders = memberPageModel.createGenderList();
        memberGender_MemberDiloag.setItems(genders);
    }


    /* Btn Remove Member */
    @FXML
    private void removeMember(){
        memberPageModel.getMemberIdToRemove(Integer.parseInt(memberId));
    }


    /*  set Icons into Btns  */

    private void initializeButtonsIcon() {

        setBtnIcon(changeInfo_btn, "fas-user-pen", Color.WHITE);
        setBtnIcon(exportMemberInfo_btn, "fas-file-export", Color.WHITE);
        setBtnIcon(removeMember_btn, "fas-trash-can", Color.rgb(255, 4, 0, 0.53));
        setBtnIcon(reSub_btn, "fas-arrow-rotate-left", Color.WHITE);
        setBtnIcon(daietBtn ,"fas-square-plus",Color.WHITE);

        if (isOnHold.equals("yes")){
            setBtnIcon(holdSub_Btn,"fas-circle-play",Color.rgb(0, 0, 0, 0.38));
        }else{
            setBtnIcon(holdSub_Btn,"fas-circle-pause",Color.rgb(0, 0, 0, 0.38));
        }
    }

    private void setBtnIcon(Button button, String iconDescription, Color color) {
        MFXFontIcon icon = new MFXFontIcon();
        icon.setSize(15);
        icon.setColor(color);
        icon.setTextAlignment(TextAlignment.CENTER);
        icon.setDescription(iconDescription);
        button.setGraphic(icon);
    }

    /*   Fields Filter    */
    private void fieldFilter(){
        memberPageModel.addTextOnlyFilter(memberName_MemberDiloag);
        memberPageModel.addNumberOnlyFilter(Pay_MemberDialog);
        memberPageModel.addNumberOnlyFilter(memberPhone_MemberDiloag);
        memberPageModel.addNumberOnlyFilter(memberAge_MemberDiloag);
        memberPageModel.addNumberOnlyFilter(memberWeight_MemberDiloag);
        memberPageModel.addNumberOnlyFilter(memberHeight_MemberDiloag);

    }


    /*     Change Member Info  Dialog   */

    public void changeMemberInfo(){
        memberPageModel.changeMemberInfoDialog(grid,VBOX_ChangeMemberInfoDialog,memberName_MemberDiloag,memberPhone_MemberDiloag,Integer.parseInt(memberId),memberWeight_MemberDiloag,memberHeight_MemberDiloag,memberAge_MemberDiloag,memberGender_MemberDiloag,labelWrongChangeInfo_MemberDialog);
    }

    /* DietPlans Dialog */

    public void addNewDietPlan()  {
        memberPageModel.addNewDietPlanDialog(grid,TitelField_DietPlanDialog,TextArea_DietPlanDialog,VBOX_DietPlanDialog, Integer.parseInt(memberId));
    }


    /*     Change Member Sub Dialog      */

    private void setBtnNameDialog(){
        quickBtn1_MemberDialog.setText(SaveFileManager.getText("btnMonth1")+ " ( شهر ) ");
        quickBtn2_MemberDialog.setText(SaveFileManager.getText("btnMonth2")+ " ( شهر ) ");
        quickBtn3_MemberDialog.setText(SaveFileManager.getText("btnMonth3")+ " ( شهر ) ");

        quickBtnPay1_MemberDialog.setText(SaveFileManager.getText("btnPay1"));
        quickBtnPay2_MemberDialog.setText(SaveFileManager.getText("btnPay2"));
        quickBtnPay3_MemberDialog.setText(SaveFileManager.getText("btnPay3"));
    }

    public void changeMemberSub() throws IOException {
        memberPageModel.changeMemberSubDialog(grid,VBOX_ChangeMemberSubDialog,DateStart_MemberDialog ,Pay_MemberDialog,DateEnd_MemberDialog,labelWrong_MemberDialog, Integer.parseInt(memberId));
    }


    // Quick Button For Set Start & End Months Sub.... Here : Connect methods with buttons on fxml file


    public void quickBtn1(){updateQuickBtn(memberPageModel.quickButtons1(LocalDate.now()));}
    public void quickBtn2(){updateQuickBtn(memberPageModel.quickButtons2(LocalDate.now()));}
    public void quickBtn3(){updateQuickBtn(memberPageModel.quickButtons3(LocalDate.now()));}

    private void updateQuickBtn(QuickButtonsInfo info){
        DateTimeFormatter formatePattern = DateTimeFormatter.ofPattern("MMMM d,yyyy");
        DateStart_MemberDialog.setValue(LocalDate.parse(info.getFormattedDateStart(), formatePattern));
        DateEnd_MemberDialog.setValue(LocalDate.parse(info.getFormattedDateEnd(),formatePattern));
    }


    // Quick Button For Pay Buttons   Here : Create Method of Quick Buttons ( How Much Sub ) with use QuickButtonsInfo as Getter & Setter

    public void quickBtn4(){updatePaymentQuickBtn(memberPageModel.quickButtonsPay1());}
    public void quickBtn5(){updatePaymentQuickBtn(memberPageModel.quickButtonsPay2());}
    public void quickBtn6(){updatePaymentQuickBtn(memberPageModel.quickButtonsPay3());}
    private void updatePaymentQuickBtn(QuickButtonsInfo info){
        String name = String.valueOf(info.getQuickPay());
        Pay_MemberDialog.setText(name);
    }











    /*    Back Btn    */


    public void mainPage(){
        main.changeScene("fxml/AllMembers.fxml");

    }


}
