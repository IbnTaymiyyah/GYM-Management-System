package com.ibntaymiyya.gym.Util;

import com.ibntaymiyya.gym.model.AddMemberGetterAndSetter;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.SimpleFormatter;


public class DBAddMember {





    public static void addMemberToDatabase(AddMemberGetterAndSetter addMemberGetterAndSetter){

        String sqlCommand = "INSERT INTO gym.members (member_id, name, phone_number, membership_start_date, membership_end_date, age, weight, hight, sub_pay, gender, totalpayed) VALUES (?,?,?,?,?,?,?,?,?,?,?)";

        try (Connection connection = DBConnection.getCon();
             PreparedStatement preparedStatement = connection.prepareStatement(sqlCommand)){


            preparedStatement.setInt(1, addMemberGetterAndSetter.getId());
            preparedStatement.setString(2, addMemberGetterAndSetter.getName());
            preparedStatement.setString(3, addMemberGetterAndSetter.getPhone());
            preparedStatement.setString(4, addMemberGetterAndSetter.getDateStart());
            preparedStatement.setString(5, addMemberGetterAndSetter.getDateEnd());
            preparedStatement.setInt(6, addMemberGetterAndSetter.getAge());
            preparedStatement.setInt(7, addMemberGetterAndSetter.getWeight());
            preparedStatement.setInt(8, addMemberGetterAndSetter.getHeight());
            preparedStatement.setDouble(9, addMemberGetterAndSetter.getPay());
            preparedStatement.setString(10, addMemberGetterAndSetter.getGender());
            preparedStatement.setDouble(11, addMemberGetterAndSetter.getPay());

            preparedStatement.executeUpdate();
            connection.setAutoCommit(true);

        }catch (SQLException e){
            e.printStackTrace();
        }
    }





    /*   Check if random Id For Member is Taken or Not   */
    public static boolean isNumberTaken(int number) {
        if (number == 0) {
            return false;
        }

        String query = "SELECT COUNT(*) FROM members WHERE member_id = ?";

        try (Connection connection = DBConnection.getCon();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, number);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt(1) > 0;
            }

        } catch (SQLException e) {
            System.out.println("In method isNumberTaken, query failed: " + e.getMessage());
        }

        return false;
    }


    // Method For Convert Date Format to Match the Database Date Format
    public String convertDateFormat(String dateString){

        try {
            SimpleDateFormat inputDate = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss");
            Date date = inputDate.parse(dateString);
            SimpleDateFormat outputDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return outputDate.format(date);

        } catch (Exception e) {
            System.out.println("Failed Change Date Format");
            e.printStackTrace();
            return null;
        }

    }

}
