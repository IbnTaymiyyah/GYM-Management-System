package com.ibntaymiyya.gym.Util;

import com.ibntaymiyya.gym.model.AddMemberGetterAndSetter;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


import com.ibntaymiyya.gym.model.DialogConfig;
import com.ibntaymiyya.gym.model.DietPlans;
import com.ibntaymiyya.gym.model.MainModel;
import com.mysql.cj.xdevapi.JsonArray;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DBExportAndImportInfo {

    static DBAddMember dbAddMember;

    Stage stage;
    GridPane gride;
    public void setGridPane(GridPane gridpane){
        gride = gridpane;
    }




    /* Get Member By id For Export */



    public List<AddMemberGetterAndSetter> getMemberInfoForExport(int memberId) {
        List<AddMemberGetterAndSetter> member = new ArrayList<>();
        String memberInfoCommand = "{CALL GetSubsDaysLeft()}";

        try (Connection connection = DBConnection.getCon();
             PreparedStatement statement = connection.prepareStatement(memberInfoCommand)) {


            ResultSet resultSet = statement.executeQuery();


            while (resultSet.next()) {
                int id = resultSet.getInt("member_id");

                if (id != memberId){
                    continue;
                }

                AddMemberGetterAndSetter memberInfo = new AddMemberGetterAndSetter();
                memberInfo.setId(resultSet.getInt("member_id"));
                memberInfo.setName(resultSet.getString("name"));
                memberInfo.setPhone(resultSet.getString("phone_number"));
                memberInfo.setDateStart(String.valueOf(resultSet.getDate("membership_start_date").toLocalDate()));
                memberInfo.setDateEnd(String.valueOf(resultSet.getDate("membership_end_date").toLocalDate()));
                memberInfo.setAge(resultSet.getInt("age"));
                memberInfo.setWeight(resultSet.getInt("weight"));
                memberInfo.setHeight(resultSet.getInt("hight"));

                int daysLeft = resultSet.getInt("days_left");

                if (daysLeft <= 0){
                    memberInfo.setDaysLeft(0);
                }else {

                    memberInfo.setDaysLeft(resultSet.getInt("days_left"));
                }


                memberInfo.setHoldDays(resultSet.getInt("holdDays"));
                memberInfo.setPay(resultSet.getDouble("sub_pay"));
                memberInfo.setTotalPayed(resultSet.getDouble("totalpayed"));
                memberInfo.setIsOnHold(resultSet.getString("isOnHold"));
                memberInfo.setGender(resultSet.getString("gender"));
                member.add(memberInfo);

            }


        } catch (SQLException e) {
            showDialog(stage,gride,1,5,decoyActionHandler,"خطاء",e.getMessage(),"" ,decoyVBox);
        }

        return member;
    }






    /*  EXPORT All Members */

    public List<AddMemberGetterAndSetter> getMembersInfoToExport(){
        List<AddMemberGetterAndSetter> member = new ArrayList<>();
        String memberInfoCommand = "{CALL GetSubsDaysLeft()}";

        try (Connection connection = DBConnection.getCon();
             PreparedStatement statement = connection.prepareStatement(memberInfoCommand)) {


            ResultSet resultSet = statement.executeQuery();


            while (resultSet.next()) {

                AddMemberGetterAndSetter memberInfo = new AddMemberGetterAndSetter();
                memberInfo.setId(resultSet.getInt("member_id"));
                memberInfo.setName(resultSet.getString("name"));
                memberInfo.setPhone(resultSet.getString("phone_number"));
                memberInfo.setDateStart(String.valueOf(resultSet.getDate("membership_start_date").toLocalDate()));
                memberInfo.setDateEnd(String.valueOf(resultSet.getDate("membership_end_date").toLocalDate()));
                memberInfo.setAge(resultSet.getInt("age"));
                memberInfo.setWeight(resultSet.getInt("weight"));
                memberInfo.setHeight(resultSet.getInt("hight"));


                int daysLeft = resultSet.getInt("days_left");

                if (daysLeft <= 0){
                    memberInfo.setDaysLeft(0);
                }else {

                    memberInfo.setDaysLeft(resultSet.getInt("days_left"));
                }


                memberInfo.setHoldDays(resultSet.getInt("holdDays"));
                memberInfo.setPay(resultSet.getDouble("sub_pay"));
                memberInfo.setTotalPayed(resultSet.getDouble("totalpayed"));
                memberInfo.setIsOnHold(resultSet.getString("isOnHold"));
                memberInfo.setGender(resultSet.getString("gender"));
                member.add(memberInfo);

            }


        } catch (SQLException e) {
            showDialog(stage,gride,1,5,decoyActionHandler,"خطاء",e.getMessage(),"" ,decoyVBox);
        }

        return member;
    }


    /*----------------------------------EXPORTING METHOD--------------------------------------*/

    /*  Export All Members Info To CSV  */

    public void exportMembersInfoToCSV(String filePath , GridPane gride){
        List<AddMemberGetterAndSetter> members = getMembersInfoToExport();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try (CSVWriter writer = new CSVWriter(new OutputStreamWriter(new FileOutputStream(filePath), StandardCharsets.UTF_8))){

            String[] header = {"ID", "الاسم", "رقم الهاتف", "العمر", "الوزن", "الطول", "النوع", "تاريخ البدء", "تاريخ الانتهاء", "الايام المتبقيه", "مبلغ الاشتراك", "كل المدفوع", "الايام المجمده", "هل الاشتراك مجمد"};
            writer.writeNext(header);

            for (AddMemberGetterAndSetter membersInfo : members){
                String[] memberData = {

                        String.valueOf(membersInfo.getId()),
                        membersInfo.getName(),
                        String.valueOf(membersInfo.getPhone()),
                        String.valueOf(membersInfo.getAge()),
                        String.valueOf(membersInfo.getWeight()),
                        String.valueOf(membersInfo.getHeight()),
                        membersInfo.getGender(),
                        membersInfo.getDateStart(),
                        membersInfo.getDateEnd(),
                        String.valueOf(membersInfo.getDaysLeft()),
                        String.valueOf(membersInfo.getPay()),
                        String.valueOf(membersInfo.getTotalPayed()),
                        String.valueOf(membersInfo.getHoldDays()),
                        membersInfo.getIsOnHold()

                };
                writer.writeNext(memberData);
            }

            showDialog(stage, gride, 1, 2, decoyActionHandler, "تم", "تم استخراج بيانات كل المشتركين "  , "", decoyVBox);
        }catch (IOException e){
            showDialog(stage,gride,1,5,decoyActionHandler,"خطاء",e.getMessage(),"" ,decoyVBox);
        }
    }

    /*  Export Member Info To CSV   */

    public void exportMemberToCSV(String filePath, int memberId , GridPane gride) {
        List<AddMemberGetterAndSetter> member = getMemberInfoForExport(memberId);
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String memberName = "";


        try (CSVWriter writer = new CSVWriter(new OutputStreamWriter(new FileOutputStream(filePath), StandardCharsets.UTF_8))) {

            // Write the header row
            String[] header = {"ID", "الاسم", "رقم الهاتف", "العمر", "الوزن", "الطول", "النوع", "تاريخ البدء", "تاريخ الانتهاء", "الايام المتبقيه", "مبلغ الاشتراك", "كل المدفوع", "الايام المجمده", "هل الاشتراك مجمد", "اسم النظام الغذائي ", "وصف النظام الغذائي"};
            writer.writeNext(header);

            // Write the member data
            for (AddMemberGetterAndSetter memberInfo : member) {
                memberName = memberInfo.getName();

                // Write the member's information first (only once)
                String[] memberData = {

                        String.valueOf(memberInfo.getId()),
                        memberInfo.getName(),
                        String.valueOf(memberInfo.getPhone()),
                        String.valueOf(memberInfo.getAge()),
                        String.valueOf(memberInfo.getWeight()),
                        String.valueOf(memberInfo.getHeight()),
                        memberInfo.getGender(),
                        memberInfo.getDateStart(),
                        memberInfo.getDateEnd(),
                        String.valueOf(memberInfo.getDaysLeft()),
                        String.valueOf(memberInfo.getPay()),
                        String.valueOf(memberInfo.getTotalPayed()),
                        String.valueOf(memberInfo.getHoldDays()),
                        memberInfo.getIsOnHold()

                };
                writer.writeNext(memberData);


            }

            showDialog(stage, gride, 1, 2, decoyActionHandler, "تم", "تم استخراج بيانات " + "\n" + memberName , "", decoyVBox);


        } catch (IOException e) {
            showDialog(stage,gride,1,5,decoyActionHandler,"خطاء",e.getMessage(),"" ,decoyVBox);

        }
    }





    /*----------------------------------IMPORT METHOD--------------------------------------*/

    public void importMembersFromCSV(String filePath,GridPane gride) {
        Connection connection = null; // Declare outside try for access in handleImportError

        try {
            connection = DBConnection.getCon(); // Get connection only once
            CSVReader csvReader = new CSVReader(new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8));


            String[] header = csvReader.readNext(); // Read the header

            String insertMembersSql = "INSERT INTO gym.members (member_id, name, phone_number, membership_start_date, membership_end_date, age, weight, hight, sub_pay, gender, holdDays, isOnHold) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            connection.setAutoCommit(false); // Start transaction ONLY ONCE, outside loop
            try (PreparedStatement membersStmt = connection.prepareStatement(insertMembersSql)) {

                String[] row;
                while ((row = csvReader.readNext()) != null) {

                    if (row.length > 0) { // Check for empty rows
                        try {

                            membersStmt.setInt(1, generateUniqueRandomNumber()); // member_id
                            membersStmt.setString(2, row[1]); // name
                            membersStmt.setString(3, row[2]); // phone_number (String)


                            membersStmt.setString(4, parseDateToMySQL(row[7])); // membership_start_date
                            membersStmt.setString(5, parseDateToMySQL(row[8])); // membership_end_date

                            membersStmt.setInt(6, parseIntegerSafely(row[3])); // age
                            membersStmt.setInt(7, parseIntegerSafely(row[4])); // weight
                            membersStmt.setInt(8, parseIntegerSafely(row[5])); // height
                            membersStmt.setDouble(9, parseDoubleSafely(row[10])); // sub_pay
                            membersStmt.setString(10, row[6]); // gender
                            membersStmt.setInt(11, parseIntegerSafely(row[12])); // holdDays
                            membersStmt.setString(12, row[13]);   // isOnHold (String)





                            membersStmt.executeUpdate();

                        } catch (NumberFormatException ex) {
                            handleImportError(connection, "Number format error on row: " + Arrays.toString(row), ex ,gride);

                            return; // OR remove this to skip the bad row

                        } catch (SQLException ex) {
                            handleImportError(connection, "Database error on row: " + Arrays.toString(row), ex , gride);

                            return; // OR re-throw to stop completely

                        }


                    }
                    connection.commit();

                }



            } catch (SQLException e) { // Outer catch for statement/transaction errors
                handleImportError(connection, "Statement or Transaction Error", e, gride);
            }


        } catch (SQLException | IOException e) {
            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException ex) {
                    handleImportError(connection,"rollback failed", ex , gride);
                }
            }

            handleImportError(connection, "Connection or File Error", e ,  gride);
        }
    }


    private void handleImportError(Connection connection, String message, Exception e ,GridPane gride) {
        try {
            if (connection != null) {
                connection.rollback();
            }
        } catch (SQLException ex) {

            showDialog(stage,gride,1,5,decoyActionHandler,"خطاء","Rollback failed: "+ex.getMessage(),"" ,decoyVBox);

            ex.printStackTrace();
        }
        e.printStackTrace();

    }



    private String parseDateToMySQL(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return "2025-01-01 00:00:00";
        }

        try {

            DateTimeFormatter inputDateFormatter = DateTimeFormatter.ofPattern("M/d/yyyy");

            DateTimeFormatter mysqlDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            LocalDate date = LocalDate.parse(dateStr.trim(), inputDateFormatter);
            return mysqlDateFormatter.format(date.atStartOfDay());
        } catch (DateTimeParseException e) {
            System.err.println("Invalid date format: " + dateStr + ". Using default date/time.");
            return "2025-01-01 00:00:00";
        }
    }



    private int parseIntegerSafely(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private double parseDoubleSafely(String value) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }


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




    /*----------------------------------Dialog METHODS--------------------------------------*/



    /*     Dialog Handel     */

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
        Dialog.dialog.close();
    };
    // Decoy VBox
    VBox decoyVBox;






}