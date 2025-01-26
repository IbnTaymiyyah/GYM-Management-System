package com.ibntaymiyya.gym.Util;

import com.ibntaymiyya.gym.model.AddMemberGetterAndSetter;
import com.ibntaymiyya.gym.model.AnalysisModel;
import io.github.palexdev.materialfx.controls.MFXTableView;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DBAnalysis {

    private AnalysisModel analysisModel;

    public void setAnalysisModel(AnalysisModel analysisModel){this.analysisModel = analysisModel;}


    /*   Current Month Profit   */

    public Integer getProfitFromCurrentMonth(){
        String command = "SELECT SUM(totalpayed) FROM members WHERE MONTH(membership_start_date) = MONTH(CURDATE()) AND YEAR(membership_start_date) = YEAR(CURDATE())";

        try (Connection connection = DBConnection.getCon();
             PreparedStatement statement = connection.prepareStatement(command);
             ResultSet resultSet = statement.executeQuery();
        ){

            if (resultSet.next()){
                int profit = resultSet.getInt(1);
                return profit;
            } else {
                return 0;
            }

        }catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    /*     Count All Members      */

    public Integer getAllMembersCount(){
        String command = "SELECT COUNT(*) FROM members";

        try (Connection connection = DBConnection.getCon();
        PreparedStatement preparedStatement = connection.prepareStatement(command);
        ResultSet resultSet = preparedStatement.executeQuery();

        ){
            if (resultSet.next()){
                int allMembers = resultSet.getInt(1);
                return allMembers;
            }else {
                return 0;
            }



        } catch (SQLException e) {
            throw new RuntimeException(e);

        }


    }


    /*    Count All Members Days Left 0      */

    public Integer getExpiredSubscriptionsCount(){
        String checkExpiredCommand = "SELECT COUNT(*) FROM (SELECT DATEDIFF(membership_end_date, CURDATE()) AS days_left FROM members) AS temp WHERE days_left <= 0";

        try (Connection connection = DBConnection.getCon();
             PreparedStatement checkExpiredStatement = connection.prepareStatement(checkExpiredCommand);
             ResultSet resultSet = checkExpiredStatement.executeQuery();
        ){

            if (resultSet.next()){
                return resultSet.getInt(1);
            } else {
                return 0;
            }

        }catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    /*   Count Hold Members   */

    public Integer getHoldSubscriptionsCount(){
        String command = "SELECT COUNT(*) FROM members WHERE isOnHold = 'yes'";

        try (Connection connection = DBConnection.getCon();
        PreparedStatement statement = connection.prepareStatement(command);
        ResultSet resultSet = statement.executeQuery()){

            if (resultSet.next()){
                return resultSet.getInt(1);
            }else {
                return 0;
            }

        }catch (SQLException e){
            e.printStackTrace();
            return null;
        }
    }



    /*   Get Hold Subs   */

    public void getHoldSubs(MFXTableView<AddMemberGetterAndSetter> HoldSubsTable){
        String command = "{Call GetHoldSubs()}";

        try (Connection connection = DBConnection.getCon();
        PreparedStatement statement = connection.prepareStatement(command)){

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()){
                int id = resultSet.getInt("member_id");
                String name = resultSet.getString("name");
                int holdDays = resultSet.getInt("holdDays");

                setInfoHoldSubs(HoldSubsTable,id,name,holdDays);
            }

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    /*   Get Total Payed Members   */

    public void getTotalPayedMembers(MFXTableView<AddMemberGetterAndSetter> totalPayedTable) {
        String command = "SELECT member_id, name, totalpayed FROM members ORDER BY totalpayed DESC";
        try (Connection connection = DBConnection.getCon();
             PreparedStatement statement = connection.prepareStatement(command)) {

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("member_id");
                String name = resultSet.getString("name");
                double totalPayed = resultSet.getDouble("totalpayed");
                setInfoTotalPayed(totalPayedTable, id, name, totalPayed);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    /*     Get Profit Data For Chart     */


        public Map<String , Double> getProfitData(){
            Map<String , Double> profitData = new HashMap<>();
            String command = "SELECT DATE_FORMAT(membership_start_date, '%m-%d-%Y') AS FormatedDate , SUM(totalpayed) AS Profit " +
                    "FROM members WHERE YEAR(membership_start_date) = YEAR(CURDATE()) AND MONTH(membership_start_date) = MONTH(CURDATE())" +
                    "GROUP BY FormatedDate";

            try (Connection connection = DBConnection.getCon();
            PreparedStatement statement = connection.prepareStatement(command)){

                try (ResultSet resultSet = statement.executeQuery()){
                 while (resultSet.next()){
                     String date = resultSet.getString("FormatedDate");
                     double profit = resultSet.getDouble("Profit");
                     profitData.put(date , profit);
                 }
                }

            }catch (SQLException e){
                e.printStackTrace();
            }
            return profitData;
        }

        public Map<String , Double> getProfitDataRange(LocalDate startDate, LocalDate endDate){
            Map<String , Double> profitData = new HashMap<>();
            String command = "SELECT DATE_FORMAT(membership_start_date, '%m-%d-%Y') AS FormatedDate , SUM(totalpayed) AS Profit " +
                    "FROM members WHERE membership_start_date BETWEEN  ? AND ?" +
                    "GROUP BY FormatedDate";

            try (Connection connection = DBConnection.getCon();
            PreparedStatement statement = connection.prepareStatement(command)){

                statement.setDate(1, Date.valueOf(startDate));
                statement.setDate(2, Date.valueOf(endDate));

                try (ResultSet resultSet = statement.executeQuery()){
                 while (resultSet.next()){
                     String date = resultSet.getString("FormatedDate");
                     double profit = resultSet.getDouble("Profit");
                     profitData.put(date , profit);
                 }
                }

            }catch (SQLException e){
                e.printStackTrace();

            }
            return profitData;
        }


        /*   Get All Members Data For Chart      */

    public Map<String , Integer> getMembersData(){
        Map<String,Integer> membersData = new HashMap<>();
        String command = "SELECT DATE_FORMAT(membership_start_date ,'%m-%d-%Y') AS FormatedDate , COUNT(member_id) AS MembersData "
                + "FROM members " +
                "WHERE YEAR(membership_start_date) = YEAR(CURDATE()) AND MONTH(membership_start_date) = MONTH(CURDATE()) " +
                "GROUP BY FormatedDate";

        try (Connection connection = DBConnection.getCon();
        PreparedStatement statement = connection.prepareStatement(command)){

            try (ResultSet resultSet = statement.executeQuery()){
                while (resultSet.next()){
                    String date = resultSet.getString("FormatedDate");
                    int members = resultSet.getInt("MembersData");
                    membersData.put(date,members);
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return membersData;
    }

    public Map<String , Integer> getMembersDataRange(LocalDate startDate, LocalDate endDate){
        Map<String,Integer> membersData = new HashMap<>();
        String command = "SELECT DATE_FORMAT(membership_start_date ,'%m-%d-%Y') AS FormatedDate , COUNT(member_id) AS MembersData "
                + "FROM members " +
                "WHERE membership_start_date BETWEEN ? AND ?" +
                "GROUP BY FormatedDate";

        try (Connection connection = DBConnection.getCon();
        PreparedStatement statement = connection.prepareStatement(command)){

            statement.setDate(1,Date.valueOf(startDate));
            statement.setDate(2,Date.valueOf(endDate));

            try (ResultSet resultSet = statement.executeQuery()){
                while (resultSet.next()){
                    String date = resultSet.getString("FormatedDate");
                    int members = resultSet.getInt("MembersData");
                    membersData.put(date,members);
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return membersData;
    }



    /*   Get All Members Data For Chart      */

    public Map<String , Integer> getEndSubsData(){
        Map<String,Integer> membersData = new HashMap<>();
        String command = "SELECT DATE_FORMAT(membership_start_date ,'%m-%d-%Y') AS FormatedDate , COUNT(member_id) AS EndSubsData "
                + "FROM members " +
                "WHERE YEAR(membership_end_date) <= YEAR(CURDATE()) AND MONTH(membership_end_date) <= MONTH(CURDATE()) " +
                "GROUP BY FormatedDate";

        try (Connection connection = DBConnection.getCon();
        PreparedStatement statement = connection.prepareStatement(command)){

            try (ResultSet resultSet = statement.executeQuery()){
                while (resultSet.next()){
                    String date = resultSet.getString("FormatedDate");
                    int members = resultSet.getInt("EndSubsData");
                    membersData.put(date,members);
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return membersData;
    }

    public Map<String , Integer> getEndSubsDataRange(LocalDate startDate, LocalDate endDate){
        Map<String,Integer> membersData = new HashMap<>();
        String command = "SELECT DATE_FORMAT(membership_start_date ,'%m-%d-%Y') AS FormatedDate , COUNT(member_id) AS EndSubsData "
                + "FROM members " +
                "WHERE membership_end_date BETWEEN ? AND ?" +
                "GROUP BY FormatedDate";

        try (Connection connection = DBConnection.getCon();
        PreparedStatement statement = connection.prepareStatement(command)){

            statement.setDate(1,Date.valueOf(startDate));
            statement.setDate(2,Date.valueOf(endDate));

            try (ResultSet resultSet = statement.executeQuery()){
                while (resultSet.next()){
                    String date = resultSet.getString("FormatedDate");
                    int members = resultSet.getInt("EndSubsData");
                    membersData.put(date,members);
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return membersData;
    }


    /*   Get Hold Subs Data For Chart      */

    public Map<String , Integer> getHoldSubsData(){
        Map<String,Integer> membersData = new HashMap<>();
        String command = "SELECT DATE_FORMAT(membership_start_date ,'%m-%d-%Y') AS FormatedDate , COUNT(member_id) AS HoldSubsData "
                + "FROM members " +
                "WHERE YEAR(membership_start_date) = YEAR(CURDATE()) AND MONTH(membership_start_date) = MONTH(CURDATE()) AND isOnHold = 'yes' " +
                "GROUP BY FormatedDate";

        try (Connection connection = DBConnection.getCon();
        PreparedStatement statement = connection.prepareStatement(command)){

            try (ResultSet resultSet = statement.executeQuery()){
                while (resultSet.next()){
                    String date = resultSet.getString("FormatedDate");
                    int members = resultSet.getInt("HoldSubsData");
                    membersData.put(date,members);
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return membersData;
    }

    public Map<String , Integer> getHoldSubsDataRange(LocalDate startDate, LocalDate endDate){
        Map<String,Integer> membersData = new HashMap<>();
        String command = "SELECT DATE_FORMAT(membership_start_date ,'%m-%d-%Y') AS FormatedDate , COUNT(member_id) AS HoldSubsData "
                + "FROM members " +
                "WHERE isOnHold = 'yes' BETWEEN ? AND ?" +
                "GROUP BY FormatedDate";

        try (Connection connection = DBConnection.getCon();
        PreparedStatement statement = connection.prepareStatement(command)){

            statement.setDate(1,Date.valueOf(startDate));
            statement.setDate(2,Date.valueOf(endDate));

            try (ResultSet resultSet = statement.executeQuery()){
                while (resultSet.next()){
                    String date = resultSet.getString("FormatedDate");
                    int members = resultSet.getInt("HoldSubsData");
                    membersData.put(date,members);
                }
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return membersData;
    }




    /*------------------------------------PRIVATE------------------------------------*/


    /*   Set Info Getter And Setter For Hold Subs Table   */
    private void setInfoHoldSubs(MFXTableView<AddMemberGetterAndSetter> HoldSubsTable, int id, String name,  int holdDays) {
        AddMemberGetterAndSetter setInfoNewMemberTabel = new AddMemberGetterAndSetter();

        setInfoNewMemberTabel.setId(id);
        setInfoNewMemberTabel.setName(name);
        setInfoNewMemberTabel.setHoldDays(holdDays);

        HoldSubsTable.getItems().add(setInfoNewMemberTabel);
    }

    /*   Set Info Getter And Setter For Hold Subs Table   */
    private void setInfoTotalPayed(MFXTableView<AddMemberGetterAndSetter> totalPayedTable, int id, String name, Double totalPayed) {
        AddMemberGetterAndSetter setInfoNewMemberTabel = new AddMemberGetterAndSetter();

        setInfoNewMemberTabel.setId(id);
        setInfoNewMemberTabel.setName(name);
        setInfoNewMemberTabel.setTotalPayed(totalPayed);

        totalPayedTable.getItems().add(setInfoNewMemberTabel);
    }





}
