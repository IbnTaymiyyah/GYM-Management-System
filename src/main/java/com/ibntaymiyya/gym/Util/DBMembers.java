package com.ibntaymiyya.gym.Util;

import com.ibntaymiyya.gym.controllers.MemberPageController;
import com.ibntaymiyya.gym.model.*;
import io.github.palexdev.materialfx.controls.MFXTableView;
import javafx.beans.property.SimpleDoubleProperty;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class DBMembers {

    private MainModel mainModel;
    private SettingModel settingModel;
    private AllMembersModel allMembersModel;
    private MemberPageModel memberPageModel;
    private MemberPageController memberPageController;
    private List<Integer> skipIds = new ArrayList<>();


    public DBMembers() {

    }

    public void setMainModel(MainModel mainModel) {
        this.mainModel = mainModel;
    }
    public void setSettingModel(SettingModel settingModel){this.settingModel = settingModel ;}

    public void setAllMembersModel(AllMembersModel allMembersModel) {
        this.allMembersModel = allMembersModel;
    }

    public void setMemberPageModel(MemberPageModel memberPageModel) {
        this.memberPageModel = memberPageModel;
    }

    public void setMemberPageController(MemberPageController memberPageController) {
        this.memberPageController = memberPageController;
    }


    /*     get Members That Subs almost Expire    */

    public void getExpiringMembers(MFXTableView<AddMemberGetterAndSetter> MainTable_MainP) {
        String getMembersCommand = "{CALL GetMembersExpiringDays(?)}";

        try (Connection connection = DBConnection.getCon();
             PreparedStatement preparedStatement = connection.prepareStatement(getMembersCommand);
        ) {

            preparedStatement.setInt(1, 7);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int skipId = resultSet.getInt("member_id");
                setSkipIds(skipId);

                int id = resultSet.getInt("member_id");
                String name = resultSet.getString("name");
                String phone = resultSet.getString("phone_number");
                java.sql.Date dateStart = resultSet.getDate("membership_start_date");
                java.sql.Date dateEnd = resultSet.getDate("membership_end_date");
                int age = resultSet.getInt("age");
                int weight = resultSet.getInt("weight");
                int height = resultSet.getInt("hight");
                int daysLeft = resultSet.getInt("days_left");
                double subPay = resultSet.getDouble("sub_pay");
                String gender = resultSet.getString("gender");

                setInfoExpiringMember(MainTable_MainP, id, name, phone, dateStart, dateEnd, age, weight, height, daysLeft, subPay, gender);

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /*   Get All Members  Without expiring Members  */

    public void getAllMembers(MFXTableView<AddMemberGetterAndSetter> MainTable_MainP) {
        String getMembersCommand = "SELECT * FROM gym.members  WHERE membership_start_date BETWEEN  CURDATE() - INTERVAL 10 DAY AND CURDATE() + INTERVAL 10 DAY";


        try (Connection connection = DBConnection.getCon();
             PreparedStatement preparedStatement = connection.prepareStatement(getMembersCommand);
        ) {


            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("member_id");

                if (getSkipIds().contains(id)) {
                    continue;
                }

                String name = resultSet.getString("name");
                String phone = resultSet.getString("phone_number");
                java.sql.Date dateStart = resultSet.getDate("membership_start_date");
                java.sql.Date dateEnd = resultSet.getDate("membership_end_date");
                int age = resultSet.getInt("age");
                int weight = resultSet.getInt("weight");
                int height = resultSet.getInt("hight");
                double subPay = resultSet.getDouble("sub_pay");
                String gender = resultSet.getString("gender");

                setInfoNewMember(MainTable_MainP, id, name, phone, dateStart, dateEnd, age, weight, height, subPay, gender);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }








    /*     get All Members     */

    public void getMembers(MFXTableView<AddMemberGetterAndSetter> MainTable_MainP) {
        String getMembersCommand = "SELECT * FROM gym.members";

        try (Connection connection = DBConnection.getCon();
             PreparedStatement preparedStatement = connection.prepareStatement(getMembersCommand);
        ) {


            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {


                int id = resultSet.getInt("member_id");
                String name = resultSet.getString("name");
                String phone = resultSet.getString("phone_number");
                java.sql.Date dateStart = resultSet.getDate("membership_start_date");
                java.sql.Date dateEnd = resultSet.getDate("membership_end_date");
                int age = resultSet.getInt("age");
                int weight = resultSet.getInt("weight");
                int height = resultSet.getInt("hight");
                int daysLeft = resultSet.getInt("days_left");
                double subPay = resultSet.getDouble("sub_pay");
                String gender = resultSet.getString("gender");

                setAllMember(MainTable_MainP, id, name, phone, dateStart, dateEnd, age, weight, height, daysLeft, subPay, gender);

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /*     Get Member Info By Id     */
    public AddMemberGetterAndSetter getMemberByIdToMembersPage(int memberId) {
        String getMembersCommand = "{CALL GetSubsDaysLeft()}";


        try (Connection connection = DBConnection.getCon();
             PreparedStatement preparedStatement = connection.prepareStatement(getMembersCommand)) {


            ResultSet resultSet = preparedStatement.executeQuery();

            // Iterate through the result set
            while (resultSet.next()) {
                int id = resultSet.getInt("member_id");

                // Only process the record if the member_id matches the one we're looking for
                if (id == memberId) {
                    String name = resultSet.getString("name");
                    String phone = resultSet.getString("phone_number");
                    java.sql.Date dateStart = resultSet.getDate("membership_start_date");
                    java.sql.Date dateEnd = resultSet.getDate("membership_end_date");
                    int age = resultSet.getInt("age");
                    int weight = resultSet.getInt("weight");
                    int height = resultSet.getInt("hight");
                    int daysLeft = resultSet.getInt("days_left");
                    int holdDays = resultSet.getInt("holdDays");
                    double subPay = resultSet.getDouble("sub_pay");
                    double totalPayed = resultSet.getDouble("totalpayed");
                    String isOnHold = resultSet.getString("isOnHold");
                    String gender = resultSet.getString("gender");


                    AddMemberGetterAndSetter setInfoMember = new AddMemberGetterAndSetter();

                    setInfoMember.setId(id);
                    setInfoMember.setName(name);
                    setInfoMember.setPhone(phone);


                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    setInfoMember.setDateStart(dateFormat.format(dateStart));
                    setInfoMember.setDateEnd(dateFormat.format(dateEnd));

                    setInfoMember.setAge(age);
                    setInfoMember.setWeight(weight);
                    setInfoMember.setHeight(height);
                    setInfoMember.setDaysLeft(daysLeft);
                    setInfoMember.setPay(subPay);
                    setInfoMember.setTotalPayed(totalPayed);
                    setInfoMember.setHoldDays(holdDays);
                    setInfoMember.setIsOnHold(isOnHold);
                    setInfoMember.setGender(gender);

                    return setInfoMember;



                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    /*     Remove Member     */

    public void removeMember(int id) {
        try (Connection connection = DBConnection.getCon()) {  // Try-with-resources
            connection.setAutoCommit(false); // Start transaction

            try (PreparedStatement dietPlanStmt = connection.prepareStatement("DELETE FROM gym.dietplans WHERE member_id = ?");
                 PreparedStatement memberStmt = connection.prepareStatement("DELETE FROM gym.members WHERE member_id = ?")) {


                dietPlanStmt.setInt(1, id);
                dietPlanStmt.executeUpdate();


                memberStmt.setInt(1, id);
                memberStmt.executeUpdate();

                connection.commit();



            } catch (SQLException e) {
                connection.rollback();
                e.printStackTrace();


            }
        } catch (SQLException e) {
            e.printStackTrace();


        }
    }


    /*   Member reSub   */
    public void reSub(AddMemberGetterAndSetter updateSubMember ){
        String reSubCommand = "UPDATE gym.members SET isOnHold = 'no' , sub_pay = ? , membership_start_date = ? , membership_end_date = ? , totalpayed = COALESCE(totalpayed,0.00) + ?  WHERE member_id = ?";

        try (Connection connection = DBConnection.getCon();
        PreparedStatement preparedStatement = connection.prepareStatement(reSubCommand);
        ){
            preparedStatement.setDouble(1,updateSubMember.getPay());
            preparedStatement.setString(2, updateSubMember.getDateStart());
            preparedStatement.setString(3, updateSubMember.getDateEnd());
            preparedStatement.setDouble(4,updateSubMember.getPay());
            preparedStatement.setInt(5,updateSubMember.getId());

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0){
            memberPageModel.updateMemberPageDialog("تم","تم اعادة الاشتراك",2);
            }else {
                memberPageModel.updateMemberPageDialog("خطاء","حدث خطاء لم يتم التجديد",5);
            }

        }catch (SQLException e){
            memberPageModel.updateMemberPageDialog("خطاء",e.getMessage(),5);
        }

    }




    /*     Member Change Info     */
    public void changeMemberInfo(AddMemberGetterAndSetter updateMemberInfo){
        String updateInfo = "UPDATE gym.members SET name = ? , phone_number = ? , gender = ? ,age = ? , hight = ? , weight = ? WHERE member_id = ?";

        try (Connection connection = DBConnection.getCon();
        PreparedStatement preparedStatement = connection.prepareStatement(updateInfo);
        ){
            preparedStatement.setString(1,updateMemberInfo.getName());
            preparedStatement.setString(2,updateMemberInfo.getPhone());
            preparedStatement.setString(3,updateMemberInfo.getGender());
            preparedStatement.setInt(4,updateMemberInfo.getAge());
            preparedStatement.setInt(5,updateMemberInfo.getHeight());
            preparedStatement.setInt(6,updateMemberInfo.getWeight());
            preparedStatement.setInt(7,updateMemberInfo.getId());

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0){
                memberPageModel.updateMemberPageDialog("تم","تم تعديل البيانات ",2);
            }else {
                memberPageModel.updateMemberPageDialog("خطاء","حدث خطاء لم يتم التغير",5);
            }



        }catch (SQLException e){
            e.printStackTrace();
        }

    }


    /*     Add Diet Plan     */

    public void addNewDietPlan(DietPlans dietPlans){
        String addNewDietPlanCommand = "INSERT INTO gym.dietplans( diet_name, description, member_id) VALUES (?,?,?)";

        try(Connection connection = DBConnection.getCon();
        PreparedStatement preparedStatement = connection.prepareStatement(addNewDietPlanCommand);
        ){
            preparedStatement.setString(1,dietPlans.getName());
            preparedStatement.setString(2,dietPlans.getDescription());
            preparedStatement.setInt(3,dietPlans.getMemberId());


            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0){
                memberPageModel.updateMemberPageDialog("تم","تم اضافة النظام الغذائي  ",2);
            }else {
                memberPageModel.updateMemberPageDialog("خطاء","حدث خطاء لم يتم الحفظ",5);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

    }

    /*     Get DietPlan     */

    public List<DietPlans> getDietPlans(int memberId) {
        List<DietPlans> dietPlans = new ArrayList<>();
        String query = "SELECT diet_plan_id, diet_name, description FROM gym.dietplans WHERE member_id = ?";

        try (Connection connection = DBConnection.getCon();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, memberId);

            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                int id = resultSet.getInt("diet_plan_id");
                String name = resultSet.getString("diet_name");
                String description = resultSet.getString("description");


                DietPlans dietPlan = new DietPlans();
                dietPlan.setDietId(id);
                dietPlan.setName(name);
                dietPlan.setDescription(description);


                dietPlans.add(dietPlan);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return dietPlans;
    }


    /*     Edit DietPlan     */

    public void editDietPlans(int dietId , String name , String desc) {

        String query = "UPDATE dietplans SET diet_name = ? , description = ?  WHERE diet_plan_id = ?";

        try (Connection connection = DBConnection.getCon();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, name);
            preparedStatement.setString(2, desc);
            preparedStatement.setInt(3, dietId);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0){
                memberPageModel.updateMemberPageDialog("تم","تم تعديل النظام الغذائي  ",2);
            }else {
                memberPageModel.updateMemberPageDialog("خطاء","حدث خطاء لم يتم التعديل",5);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /*     Edit DietPlan     */

    public void deleteDietPlans(int dietId ) {

        String query = "DELETE FROM dietplans WHERE diet_plan_id = ?";

        try (Connection connection = DBConnection.getCon();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, dietId);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0){
                memberPageModel.updateMemberPageDialog("تم","تم حذف النظام الغذائي  ",2);
            }else {
                memberPageModel.updateMemberPageDialog("خطاء","حدث خطاء لم يتم الحذف",5);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    /*  Hold Subs  */
    public void holdSubscription(int memberId , int daysToHold){
        String holdSubs = "UPDATE members SET isOnHold = 'yes', holdDays = ?  WHERE member_id = ?";
        try (Connection connection = DBConnection.getCon();
        PreparedStatement statement = connection.prepareStatement(holdSubs);
        ){

            statement.setInt(1, daysToHold);
            statement.setInt(2, memberId);

            statement.executeUpdate();

        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    /* Resume Subs */

    public void resumeSubscription(int memberId){
        String resumeSubs = "UPDATE members SET membership_start_date = CURDATE(),membership_end_date = DATE_ADD(CURDATE(), INTERVAL holdDays DAY), isOnHold = 'no', holdDays = 0 WHERE member_id = ?";
        try (Connection connection = DBConnection.getCon();
             PreparedStatement statement = connection.prepareStatement(resumeSubs);
        ){


            statement.setInt(1,memberId);

            statement.executeUpdate();

        }catch (SQLException e){
            e.printStackTrace();
        }
    }




    /* DELETE Tables Data  */

    public boolean deleteAllDataFromAllTables(String databaseName){
            System.out.println("Iam here now");
        try (Connection connection = DBConnection.getCon();) {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet tables = metaData.getTables(databaseName, null, "%", new String[]{"TABLE"});

            connection.setAutoCommit(false);
            try (Statement statement = connection.createStatement()) {


               // statement.executeUpdate("DELETE FROM userpermissions");


                while (tables.next()) {
                    String tableName = tables.getString("TABLE_NAME");

                    if ("permissions".equalsIgnoreCase(tableName) || "users".equalsIgnoreCase(tableName) || "userpermissions".equalsIgnoreCase(tableName)) {
                        continue;
                    }

                    String deleteSql = "DELETE FROM " + tableName;
                    statement.executeUpdate(deleteSql);
                }
                connection.commit();

                return true;

            } catch (SQLException e) {
                connection.rollback();
                e.printStackTrace();
                return false;

            }

        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }


    }





/*---------------------------------------PRIVATE-----------------------------------------------*/

    // Setter to store skip IDs
    private void setSkipIds(int id) {
        skipIds.add(id);
    }

    // Getter to return the list of skip IDs
    private List<Integer> getSkipIds() {
        return skipIds;
    }


    /*Set Info Getter And Setter For New Members Table   */
    private void setInfoNewMember(MFXTableView<AddMemberGetterAndSetter> MainTable_MainP, int id, String name, String phone, java.sql.Date dateStart, java.sql.Date dateEnd, int age, int weight, int height, double subPay, String gender) {
        AddMemberGetterAndSetter setInfoNewMemberTabel = new AddMemberGetterAndSetter();

        setInfoNewMemberTabel.setId(id);
        setInfoNewMemberTabel.setName(name);
        setInfoNewMemberTabel.setPhone(phone);

        // Format the dates to a readable format
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        setInfoNewMemberTabel.setDateStart(dateFormat.format(dateStart));
        setInfoNewMemberTabel.setDateEnd(dateFormat.format(dateEnd));

        setInfoNewMemberTabel.setAge(age);
        setInfoNewMemberTabel.setWeight(weight);
        setInfoNewMemberTabel.setHeight(height);
        setInfoNewMemberTabel.setPay(subPay);
        setInfoNewMemberTabel.setGender(gender);

        MainTable_MainP.getItems().add(setInfoNewMemberTabel);
    }


    /*   Set Info Getter And Setter For Expiring Members Table   */
    private void setInfoExpiringMember(MFXTableView<AddMemberGetterAndSetter> MainTable_MainP, int id, String name, String phone, java.sql.Date dateStart, java.sql.Date dateEnd, int age, int weight, int height, int daysLeft, double subPay, String gender) {
        AddMemberGetterAndSetter setInfoNewMemberTabel = new AddMemberGetterAndSetter();

        setInfoNewMemberTabel.setId(id);
        setInfoNewMemberTabel.setName(name);
        setInfoNewMemberTabel.setPhone(phone);

        // Format the dates to a readable format
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        setInfoNewMemberTabel.setDateStart(dateFormat.format(dateStart));
        setInfoNewMemberTabel.setDateEnd(dateFormat.format(dateEnd));

        setInfoNewMemberTabel.setAge(age);
        setInfoNewMemberTabel.setWeight(weight);
        setInfoNewMemberTabel.setHeight(height);
        setInfoNewMemberTabel.setDaysLeft(daysLeft);
        setInfoNewMemberTabel.setPay(subPay);
        setInfoNewMemberTabel.setGender(gender);

        MainTable_MainP.getItems().add(setInfoNewMemberTabel);
    }

    /*   Set Info Getter And Setter For  Member Page   */
    private AddMemberGetterAndSetter setInfoExpiringMemberPage(int id, String name, String phone, java.sql.Date dateStart, java.sql.Date dateEnd, int age, int weight, int height, int daysLeft, double subPay, String gender) {
        AddMemberGetterAndSetter setInfoNewMemberTabel = new AddMemberGetterAndSetter();

        setInfoNewMemberTabel.setId(id);
        setInfoNewMemberTabel.setName(name);
        setInfoNewMemberTabel.setPhone(phone);

        // Format the dates to a readable format
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        setInfoNewMemberTabel.setDateStart(dateFormat.format(dateStart));
        setInfoNewMemberTabel.setDateEnd(dateFormat.format(dateEnd));

        setInfoNewMemberTabel.setAge(age);
        setInfoNewMemberTabel.setWeight(weight);
        setInfoNewMemberTabel.setHeight(height);
        setInfoNewMemberTabel.setDaysLeft(daysLeft);
        setInfoNewMemberTabel.setPay(subPay);
        setInfoNewMemberTabel.setGender(gender);
        //memberPageModel.getMemberInfo();
        return setInfoNewMemberTabel;

    }


    /*   Set Info Getter And Setter For Expiring Members Table   */
    private void setAllMember(MFXTableView<AddMemberGetterAndSetter> MainTable_MainP, int id, String name, String phone, java.sql.Date dateStart, java.sql.Date dateEnd, int age, int weight, int height, int daysLeft, double subPay, String gender) {
        AddMemberGetterAndSetter setInfoNewMemberTabel = new AddMemberGetterAndSetter();

        setInfoNewMemberTabel.setId(id);
        setInfoNewMemberTabel.setName(name);
        setInfoNewMemberTabel.setPhone(phone);

        // Format the dates to a readable format
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        setInfoNewMemberTabel.setDateStart(dateFormat.format(dateStart));
        setInfoNewMemberTabel.setDateEnd(dateFormat.format(dateEnd));

        setInfoNewMemberTabel.setAge(age);
        setInfoNewMemberTabel.setWeight(weight);
        setInfoNewMemberTabel.setHeight(height);
        setInfoNewMemberTabel.setDaysLeft(daysLeft);
        setInfoNewMemberTabel.setPay(subPay);
        setInfoNewMemberTabel.setGender(gender);

        MainTable_MainP.getItems().add(setInfoNewMemberTabel);
    }







}
