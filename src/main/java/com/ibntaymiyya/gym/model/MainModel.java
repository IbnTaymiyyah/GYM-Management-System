package com.ibntaymiyya.gym.model;

import com.ibntaymiyya.gym.Main;
import com.ibntaymiyya.gym.Util.DBAddMember;
import com.ibntaymiyya.gym.Util.DBMembers;
import com.ibntaymiyya.gym.Util.Dialog;
import com.ibntaymiyya.gym.Util.SaveFileManager;
import com.ibntaymiyya.gym.controllers.MemberPageController;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableRow;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.filter.IntegerFilter;
import io.github.palexdev.materialfx.filter.StringFilter;
import io.github.palexdev.mfxresources.fonts.IconsProviders;
import io.github.palexdev.mfxresources.fonts.MFXFontIcon;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.chrono.ChronoLocalDate;
import java.time.chrono.Chronology;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class MainModel implements Initializable{

    static DBAddMember dbAddMember;
    DBMembers dbMembers;
    Main main = new Main();
    private Dialog dialog ;
    Stage stage ;




    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("MMMM d,yyyy");

   private static int[] valueChangebtnMonth;
   private static int[] valueChangebtnPay;

   public static int qucikMonthBtn1 ;
   private static int qucikMonthBtn2 ;
   private static int qucikMonthBtn3;


   private static int qucikPayBtn1 ;
   private static int qucikPayBtn2 ;
   private static int qucikPayBtn3 ;




    public MainModel() {
        valueChangebtnMonth = new int[3];
        valueChangebtnPay = new int[3];

        this.dbMembers = new DBMembers();
        this.dbMembers.setMainModel(this);
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        quickBtnValues();


    }



    // Get Current Time and Set in Main Page
    public void timeNow(Runnable runnable){

        Thread thread = new Thread( ()-> {

            SimpleDateFormat mainPage_HM = new SimpleDateFormat("hh:mm");
            SimpleDateFormat mainPage_AmPm = new SimpleDateFormat("a");

            while (true){

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                final String timeHM = mainPage_HM.format(new Date());
                final String timeAmPm = mainPage_AmPm.format(new Date());

                Platform.runLater(() -> {
                    runnable.run();
                });

            }

        });

        thread.start();

    }




     /*  Observable List for ComboBox Btw Genders Male or Female  */
   public ObservableList<String> createGenderList() {
       ObservableList<String> genderList = FXCollections.observableArrayList();

       genderList.add("ذكر");
       genderList.add("انثي");

       return genderList;
   }




   public void quickBtnValues(){
       qucikMonthBtn1 = Integer.parseInt(Objects.requireNonNull(SaveFileManager.getText("btnMonth1")));
       qucikMonthBtn2 = Integer.parseInt(Objects.requireNonNull(SaveFileManager.getText("btnMonth2")));
       qucikMonthBtn3 = Integer.parseInt(Objects.requireNonNull(SaveFileManager.getText("btnMonth3")));



       qucikPayBtn1 = Integer.parseInt(Objects.requireNonNull(SaveFileManager.getText("btnPay1")));
       qucikPayBtn2 = Integer.parseInt(Objects.requireNonNull(SaveFileManager.getText("btnPay2")));
       qucikPayBtn3 = Integer.parseInt(Objects.requireNonNull(SaveFileManager.getText("btnPay3")));
   }






    /*   Quick Button For Set Start & End Months Sub.... Here : Create Method of Quick Buttons ( Months ) with use QuickButtonsInfo as Getter & Setter   */

    public QuickButtonsInfo quickButtons1(LocalDate date){

        return updateQuickBtnInfoMonths(qucikMonthBtn1, date);



    }

    public QuickButtonsInfo quickButtons2(LocalDate date){
        return updateQuickBtnInfoMonths(qucikMonthBtn2, date);

    }

    public QuickButtonsInfo quickButtons3(LocalDate date){

        return updateQuickBtnInfoMonths(qucikMonthBtn3, date);
    }

    private QuickButtonsInfo updateQuickBtnInfoMonths(int quickMonth, LocalDate date){
        QuickButtonsInfo info = new QuickButtonsInfo();
        info.setQuickMonth(quickMonth);
        info.setFormattedDateStart(date.format(dateTimeFormatter));

        Period period = Period.ofMonths(quickMonth);
        LocalDate endDate = date.plus(period);

        info.setFormattedDateEnd(endDate.format(dateTimeFormatter));
        return info;
    }

     /*  Quick Button For Pay Buttons   Here : Create Method of Quick Buttons ( How Much Sub ) with use QuickButtonsInfo as Getter & Setter  */

    public QuickButtonsInfo quickButtonsPay1(){

        return updateQuickBtnInfoPay(qucikPayBtn1);
    }

    public QuickButtonsInfo quickButtonsPay2(){
        return updateQuickBtnInfoPay(qucikPayBtn2);
    }

    public QuickButtonsInfo quickButtonsPay3(){
        return updateQuickBtnInfoPay(qucikPayBtn3);
    }


    private QuickButtonsInfo updateQuickBtnInfoPay(int payAmount){
       QuickButtonsInfo info = new QuickButtonsInfo();
       info.setQuickPay(payAmount);
       return info;
    }




    /*--------------------------------------------------------------*/

    /*       Main Page Tables       */



    // Second Table For Expiring Members

    public void expiringMembers(MFXTableView<AddMemberGetterAndSetter> MainTable_MainP){
        AddMemberGetterAndSetter getterAndSetter = new AddMemberGetterAndSetter();

        String id = "ID" , name = "الاسم" , dateStart ="تاريخ الاشتراك",dateEnd = "تاريخ الانتهاء",daysLeft = "الايام المتبقيه";
        MFXTableColumn<AddMemberGetterAndSetter> idColumn = new MFXTableColumn<>(id,false, Comparator.comparing(AddMemberGetterAndSetter::getId));
        MFXTableColumn<AddMemberGetterAndSetter> nameColumn = new MFXTableColumn<>(name,false,Comparator.comparing(AddMemberGetterAndSetter::getName));
        MFXTableColumn<AddMemberGetterAndSetter> dateEndColumn = new MFXTableColumn<>(dateEnd,false,Comparator.comparing(AddMemberGetterAndSetter::getDateEnd));
        MFXTableColumn<AddMemberGetterAndSetter> daysLeftColumn = new MFXTableColumn<>(daysLeft,false,Comparator.comparing(AddMemberGetterAndSetter::getIcon));


        idColumn.setRowCellFactory(addMemberGetterAndSetter -> new MFXTableRowCell<>(AddMemberGetterAndSetter::getId));
        nameColumn.setRowCellFactory(addMemberGetterAndSetter -> new MFXTableRowCell<>(AddMemberGetterAndSetter::getName));
        dateEndColumn.setRowCellFactory(addMemberGetterAndSetter -> new MFXTableRowCell<>(AddMemberGetterAndSetter::getDateEnd));

        daysLeftColumn.setRowCellFactory(addMemberGetterAndSetter -> new MFXTableRowCell<>(AddMemberGetterAndSetter::getIcon){
            final MFXFontIcon icon = new MFXFontIcon("fas-circle",16);

            @Override
            public void update(AddMemberGetterAndSetter item){
                super.update(item);
                setGraphic(null);
                setText(null);



                if (item != null){
                    int daysLeft = item.getDaysLeft();

                    if (daysLeft <= 0){
                        icon.setIconsProvider(IconsProviders.FONTAWESOME_SOLID);
                        icon.setColor(Color.RED);
                        icon.setDescription("fas-circle");
                        setGraphic(icon);


                    }else {
                        setText(String.valueOf(daysLeft));
                    }
                }else {
                    setGraphic(null);
                    setText(null);
                }
            }

        });

        MainTable_MainP.setTableRowFactory(row ->{
            MFXTableRow<AddMemberGetterAndSetter> tableRow = new MFXTableRow<>(MainTable_MainP , null);

            tableRow.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {

                if (event.getClickCount() == 2 && tableRow.getData() != null){
                    AddMemberGetterAndSetter rowData = tableRow.getData();
                    event.consume();

                    int memberId = rowData.getId();
                    setMemberInfo(memberId);
                }
            });
            return tableRow;
        });


        MainTable_MainP.widthProperty().addListener((observableValue, oldValue, newValue) ->{
            double tableWidth = newValue.doubleValue();
            double columnWidth = tableWidth / 5;

            idColumn.setPrefWidth(columnWidth);
            nameColumn.setPrefWidth(columnWidth);
            dateEndColumn.setPrefWidth(columnWidth);
            daysLeftColumn.setPrefWidth(columnWidth);


        } );

            MainTable_MainP.getTableColumns().addAll(idColumn, nameColumn, dateEndColumn, daysLeftColumn);


        MainTable_MainP.getFilters().addAll(
                new IntegerFilter<>(id,AddMemberGetterAndSetter::getId),
                new StringFilter<>(name,AddMemberGetterAndSetter::getName)

        );

        MainTable_MainP.features().enableBounceEffect();
        MainTable_MainP.features().enableSmoothScrolling(0.7);


        dbMembers.getExpiringMembers(MainTable_MainP);

    }





   // Main Table For All New Members

    public void setNewMembersTable(MFXTableView<AddMemberGetterAndSetter> MainTable_MainP) {
        AddMemberGetterAndSetter getterAndSetter = new AddMemberGetterAndSetter();

        String id = "ID", name = "الاسم", dateStart = "تاريخ الاشتراك", dateEnd = "تاريخ الانتهاء";

        // Define columns
        MFXTableColumn<AddMemberGetterAndSetter> idColumn = new MFXTableColumn<>(id, false, Comparator.comparing(AddMemberGetterAndSetter::getId));
        MFXTableColumn<AddMemberGetterAndSetter> nameColumn = new MFXTableColumn<>(name, false, Comparator.comparing(AddMemberGetterAndSetter::getName));
        MFXTableColumn<AddMemberGetterAndSetter> dateStartColumn = new MFXTableColumn<>(dateStart, false, Comparator.comparing(AddMemberGetterAndSetter::getDateStart));
        MFXTableColumn<AddMemberGetterAndSetter> dateEndColumn = new MFXTableColumn<>(dateEnd, false, Comparator.comparing(AddMemberGetterAndSetter::getDateEnd));


        // Set cell factories
        idColumn.setRowCellFactory(addMemberGetterAndSetter -> new MFXTableRowCell<>(AddMemberGetterAndSetter::getId));
        nameColumn.setRowCellFactory(addMemberGetterAndSetter -> new MFXTableRowCell<>(AddMemberGetterAndSetter::getName));
        dateStartColumn.setRowCellFactory(addMemberGetterAndSetter -> new MFXTableRowCell<>(AddMemberGetterAndSetter::getDateStart));
        dateEndColumn.setRowCellFactory(addMemberGetterAndSetter -> new MFXTableRowCell<>(AddMemberGetterAndSetter::getDateEnd));



        // Row factory to handle double-click and tooltips
        MainTable_MainP.setTableRowFactory(row -> {
            MFXTableRow<AddMemberGetterAndSetter> tableRow = new MFXTableRow<>(MainTable_MainP, null);

            // Use addEventFilter to prioritize double-click handling
            tableRow.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
                if (event.getClickCount() == 2 && tableRow.getData() != null) {
                    AddMemberGetterAndSetter rowData = tableRow.getData();
                    event.consume();

                    int memberId = rowData.getId();
                    setMemberInfo(memberId);
                }
            });


            return tableRow;
        });


        MainTable_MainP.widthProperty().addListener((observableValue, oldValue, newValue) -> {
            double tableWidth = newValue.doubleValue();
            double columnWidth = tableWidth / 4;

            idColumn.setPrefWidth(columnWidth);
            nameColumn.setPrefWidth(columnWidth);
            dateStartColumn.setPrefWidth(columnWidth);
            dateEndColumn.setPrefWidth(columnWidth);

        });


        MainTable_MainP.getTableColumns().addAll(idColumn, nameColumn, dateStartColumn, dateEndColumn);

        MainTable_MainP.getFilters().addAll(
                new IntegerFilter<>(id, AddMemberGetterAndSetter::getId),
                new StringFilter<>(name, AddMemberGetterAndSetter::getName)
        );

        MainTable_MainP.features().enableBounceEffect();
        MainTable_MainP.features().enableSmoothScrolling(0.7);

        dbMembers.getAllMembers(MainTable_MainP);
    }



    /*  get member Info and Send it to display in Member Page (MemberPageController)  */
    private void setMemberInfo(int memberId){
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

        // Display the Scene
        main.changeScene("fxml/MemberPage.fxml");

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




    // Random Numbers ID MAX 30001 & Check if Number is Already Taken in Database

    public static int randomID(){
        Random random = new Random();
        int idNumberLimit = 30001;
        return random.nextInt(idNumberLimit);
    }

    public static int generateUniqueRandomNumber(){
       int randomNumber ;

       do {
           randomNumber = randomID();

       }while (dbAddMember.isNumberTaken(randomNumber));

           return randomNumber;
    }



}
