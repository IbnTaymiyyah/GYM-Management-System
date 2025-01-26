package com.ibntaymiyya.gym.model;

import com.ibntaymiyya.gym.Main;
import com.ibntaymiyya.gym.Util.DBMembers;
import com.ibntaymiyya.gym.controllers.AllMembers;
import com.ibntaymiyya.gym.controllers.MemberPageController;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableRow;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.MFXTextField;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.filter.BooleanFilter;
import io.github.palexdev.materialfx.filter.IntegerFilter;
import io.github.palexdev.materialfx.filter.StringFilter;
import io.github.palexdev.mfxresources.fonts.IconsProviders;
import io.github.palexdev.mfxresources.fonts.MFXFontIcon;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.net.URL;
import java.util.Comparator;
import java.util.ResourceBundle;

public class AllMembersModel implements Initializable {

    private ObservableList<AddMemberGetterAndSetter> allMembersList = FXCollections.observableArrayList();
    private ObservableList<AddMemberGetterAndSetter> filteredList = FXCollections.observableArrayList();
    DBMembers dbMembers ;
    private AllMembers allMembers ;
    private MemberPageModel memberPageModel = new MemberPageModel();
    private MemberPageController memberPageController ;
    Main main = new Main();



    public AllMembersModel() {
        this.dbMembers = new DBMembers();
        this.dbMembers.setAllMembersModel(this);

        this.memberPageController = new MemberPageController();
        this.memberPageController.setAllMembersModel(this);

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }



    public void allMembers(MFXTableView<AddMemberGetterAndSetter> mainTable, MFXTextField searchField) {
        String id = "ID", name = "الاسم", phone = "رقم الهاتف", dateStart = "تاريخ الاشتراك", dateEnd = "تاريخ الانتهاء", gender = "النوع";

        MFXTableColumn<AddMemberGetterAndSetter> idColumn = new MFXTableColumn<>(id, false, Comparator.comparing(AddMemberGetterAndSetter::getId));
        MFXTableColumn<AddMemberGetterAndSetter> nameColumn = new MFXTableColumn<>(name, false, Comparator.comparing(AddMemberGetterAndSetter::getName));
        MFXTableColumn<AddMemberGetterAndSetter> phoneColumn = new MFXTableColumn<>(phone, false, Comparator.comparing(AddMemberGetterAndSetter::getPhone));
        MFXTableColumn<AddMemberGetterAndSetter> dateStartColumn = new MFXTableColumn<>(dateStart, false, Comparator.comparing(AddMemberGetterAndSetter::getDateStart));
        MFXTableColumn<AddMemberGetterAndSetter> dateEndColumn = new MFXTableColumn<>(dateEnd, false, Comparator.comparing(AddMemberGetterAndSetter::getDateEnd));
        MFXTableColumn<AddMemberGetterAndSetter> genderColumn = new MFXTableColumn<>(gender, false, Comparator.comparing(AddMemberGetterAndSetter::getGender));

        idColumn.setRowCellFactory(addMemberGetterAndSetter -> new MFXTableRowCell<>(AddMemberGetterAndSetter::getId));
        nameColumn.setRowCellFactory(addMemberGetterAndSetter -> new MFXTableRowCell<>(AddMemberGetterAndSetter::getName));
        phoneColumn.setRowCellFactory(addMemberGetterAndSetter -> new MFXTableRowCell<>(AddMemberGetterAndSetter::getPhone));
        dateStartColumn.setRowCellFactory(addMemberGetterAndSetter -> new MFXTableRowCell<>(AddMemberGetterAndSetter::getDateStart));
        dateEndColumn.setRowCellFactory(addMemberGetterAndSetter -> new MFXTableRowCell<>(AddMemberGetterAndSetter::getDateEnd));

        genderColumn.setRowCellFactory(addMemberGetterAndSetter -> new MFXTableRowCell<>(null) {
            private final Button genderButton = new Button();

            {

                genderButton.setAlignment(Pos.CENTER);

                genderButton.setPadding(new Insets(1 , 13 , 1 , 13));

                genderButton.setDisable(false);
            }

            @Override
            public void update(AddMemberGetterAndSetter item) {
                getChildren().clear();
                getChildren().add(genderButton);

                String gender = item.getGender();
                genderButton.setText(gender);
                genderButton.getStyleClass().removeAll("Male", "Female");

                if (item != null) {

                    if (gender.equals("ذكر")) {
                        genderButton.getStyleClass().add("Male");

                        setGraphic(genderButton);
                    } else {
                        genderButton.getStyleClass().add("Female");

                        setGraphic(genderButton);
                    }
                }else {setGraphic(null);}
            }
        });



        mainTable.setTableRowFactory(row -> {
            MFXTableRow<AddMemberGetterAndSetter> tableRow = new MFXTableRow<>(mainTable, null);

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

        mainTable.widthProperty().addListener((observableValue, oldValue, newValue) -> {
            double tableWidth = newValue.doubleValue();
            double columnWidth = tableWidth / 6;

            idColumn.setPrefWidth(columnWidth);
            nameColumn.setPrefWidth(columnWidth);
            phoneColumn.setPrefWidth(columnWidth);
            dateEndColumn.setPrefWidth(columnWidth);
            dateStartColumn.setPrefWidth(columnWidth);
            genderColumn.setPrefWidth(columnWidth);
        });

        mainTable.getTableColumns().addAll(idColumn, nameColumn, phoneColumn, genderColumn, dateStartColumn, dateEndColumn);

        mainTable.getFilters().addAll(
                new IntegerFilter<>(id, AddMemberGetterAndSetter::getId),
                new StringFilter<>(name, AddMemberGetterAndSetter::getName),
                new StringFilter<>(phone, AddMemberGetterAndSetter::getPhone)
        );

        mainTable.features().enableBounceEffect();
        mainTable.features().enableSmoothScrolling(0.7);

        dbMembers.getMembers(mainTable);

        allMembersList.addAll(mainTable.getItems());

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            setFilteredList(newValue, mainTable);
        });
    }




    /*     get member Info and Send it to display in Member Page (MemberPageController)      */
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

        // Display the Scene
        main.changeScene("fxml/MemberPage.fxml");


    }





    /*     Table view Filter For Get any Text From text Field       */
    private void setFilteredList(String searchText , MFXTableView<AddMemberGetterAndSetter> mainTable){
        filteredList.clear();

        if (searchText == null || searchText.isEmpty()){
            mainTable.getItems().setAll(allMembersList);
        }else {
            for (AddMemberGetterAndSetter member : allMembersList){
                if (member.getName().toLowerCase().contains(searchText.toLowerCase())
                        || String.valueOf(member.getId()).contains(searchText)
                        || String.valueOf(member.getPhone()).contains(searchText)

                ){
                    filteredList.add(member);
                }
            }

            mainTable.getItems().setAll(filteredList);
        }
    }








}
