package com.ibntaymiyya.gym.Util;

import com.ibntaymiyya.gym.controllers.Setting;
import com.ibntaymiyya.gym.model.SettingModel;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXFilterComboBox;
import javafx.application.Platform;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DBUsers {
    private SettingModel settingModel;
    private Setting setting ;

    public DBUsers() {

    }
    public void setSettingModel(SettingModel settingModel){
        this.settingModel = settingModel;
    }

    public void setSetting(Setting setting){
        this.setting = setting;
    }




    /*     Add New User To DataBase with own Permissions     */

    public void addNewUser(String userName,String password , List<Integer> permissions) throws SQLException {
        String addNewCommand = "INSERT INTO gym.users (username,password) VALUES (?,?)";
        String addPermissionsCommand ="INSERT INTO gym.userpermissions(user_id, permission_id) VALUES (?,?)";
        String setAdminStatusCommand ="{CALL SetAdminStatus(?)}";


        try(Connection connection = DBConnection.getCon()) {
            connection.setAutoCommit(false);

            try (PreparedStatement userStatement = connection.prepareStatement(addNewCommand,PreparedStatement.RETURN_GENERATED_KEYS);
            PreparedStatement permissionStatement = connection.prepareStatement(addPermissionsCommand);
                 CallableStatement isAdminStatus = connection.prepareCall(setAdminStatusCommand);
            ) {


                userStatement.setString(1, userName);
                userStatement.setString(2, password);

                userStatement.executeUpdate();


                ResultSet generatedKeys = userStatement.getGeneratedKeys();
                int userId = -1;
                if (generatedKeys.next()) {
                    userId = generatedKeys.getInt(1);
                }else{
                    throw new SQLException("Creating user failed, no ID obtained.");
                }


                for (int permissionId : permissions) {
                    if (permissionExist(connection,permissionId)){
                        permissionStatement.setInt(1,userId);
                        permissionStatement.setInt(2,permissionId);
                        permissionStatement.executeUpdate();
                    }else {
                        System.out.println("Permission ID " + permissionId + " does not exist in the permissions table.");
                    }

                }

                isAdminStatus.setInt(1,userId);
                isAdminStatus.execute();


                connection.commit();
            } catch (SQLException e) {

                connection.rollback();
                e.printStackTrace();
            } finally {

                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        }



    public void updateUser(String oldName,String userName, String password, List<Integer> permissions) throws SQLException {
        int userId = returnUserId(oldName, DBConnection.getCon());

        if (userId == -1) {
            throw new SQLException("User not found: " + userName);
        }

        String updateUserCommand = "UPDATE gym.users SET username = ?, password = ? WHERE user_id = ?";
        String addPermissionsCommand = "INSERT INTO gym.userpermissions(user_id, permission_id) VALUES (?, ?)";
        String deletePermissionsCommand = "DELETE FROM gym.userpermissions WHERE user_id = ? AND permission_id = ?";
        String setAdminStatusCommand = "{CALL SetAdminStatus(?)}";

        try (Connection connection = DBConnection.getCon()) {
            connection.setAutoCommit(false);

            try (PreparedStatement updateUserStatement = connection.prepareStatement(updateUserCommand);
                 PreparedStatement addPermissionsStatement = connection.prepareStatement(addPermissionsCommand);
                 PreparedStatement deletePermissionsStatement = connection.prepareStatement(deletePermissionsCommand);
                 CallableStatement isAdminStatus = connection.prepareCall(setAdminStatusCommand)) {

                updateUserStatement.setString(1, userName);
                updateUserStatement.setString(2, password);
                updateUserStatement.setInt(3, userId);
                updateUserStatement.executeUpdate();


                List<Integer> currentPermissions = getUserPermissions(userId, connection);

                for (int permissionId : permissions) {
                    if (permissionExist(connection, permissionId) && !currentPermissions.contains(permissionId)) {
                        addPermissionsStatement.setInt(1, userId);
                        addPermissionsStatement.setInt(2, permissionId);
                        addPermissionsStatement.executeUpdate();
                    }
                }


                for (int permissionId : currentPermissions) {
                    if (!permissions.contains(permissionId)) {
                        deletePermissionsStatement.setInt(1, userId);
                        deletePermissionsStatement.setInt(2, permissionId);
                        deletePermissionsStatement.executeUpdate();
                    }
                }


                isAdminStatus.setInt(1, userId);
                isAdminStatus.execute();

                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                e.printStackTrace();
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /*     Get All Users From DataBase WITHOUT own Permissions     */

    public void getAllUsers(MFXComboBox<String> comboBox) {
        String getAllUsersCommand = "SELECT username FROM gym.users";

        try (Connection connection = DBConnection.getCon();
             PreparedStatement getAllUsers = connection.prepareStatement(getAllUsersCommand)) {

            ResultSet resultSet = getAllUsers.executeQuery();
            List<String> allUsersList = new ArrayList<>();

            while (resultSet.next()) {
                String username = resultSet.getString("username");
                if (username != null) {
                    allUsersList.add(username);
                }
            }

            // Ensure the list is not empty before setting it to the combo box
            if (!allUsersList.isEmpty()) {
                Platform.runLater(() -> comboBox.getItems().setAll(allUsersList));
            } else {
                System.out.println("No users found in the database.");
                Platform.runLater(() -> comboBox.setPromptText("لا يوجد مستخدمين"));
            }

        } catch (SQLException e) {
            System.out.println("MySQL Is OFF, comboBox Users Not Work");
        }
    }

    /*     Remove Selected User      */
    public void removeUser(String userName){
        String removeUserCommand = "DELETE FROM gym.users WHERE username = ?";
        String removeUserPermissionsCommand = "DELETE FROM gym.userpermissions WHERE user_id = ?";

        try (Connection connection = DBConnection.getCon();
        PreparedStatement removeUserStatement = connection.prepareStatement(removeUserCommand);
        PreparedStatement removePermissionsStatement = connection.prepareStatement(removeUserPermissionsCommand)
        ){
            connection.setAutoCommit(false);

            int userId = returnUserId(userName, connection);
            if (userId == -1) {
                settingModel.userIsNotFoundDilaog();

                return;
            }


            removePermissionsStatement.setInt(1, userId);
            removePermissionsStatement.executeUpdate();


            removeUserStatement.setString(1, userName);
            int isRemoved = removeUserStatement.executeUpdate();



            settingModel.userIsRemovedDiloag(isRemoved > 0);
            connection.commit();

        }catch (SQLException e){
            e.printStackTrace();
            System.out.println("Remove User Method NoT Work");

        }
    }

    /*  Check if UserName Is Taken or NOT  */

    public boolean isUserNameTaken(String userName) {
        String checkUserNameCommand = "SELECT COUNT(*) FROM gym.users WHERE username = ?";

        try (Connection connection = DBConnection.getCon();
             PreparedStatement checkUserName = connection.prepareStatement(checkUserNameCommand)) {

            checkUserName.setString(1, userName);
            ResultSet resultSet = checkUserName.executeQuery();

            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }



    private int returnUserId (String username , Connection connection){
        String returnIdCommand = "SELECT user_id FROM gym.users WHERE username = ?";
        try(
        PreparedStatement preparedStatement = connection.prepareStatement(returnIdCommand)
        ){
            preparedStatement.setString(1,username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                return resultSet.getInt("user_id");
            }


        }catch (SQLException e){
            System.out.println("Return User ID  Method NoT Work");
            e.printStackTrace();
        }
        return -1;
    }


    // Get User Permissions
    private List<Integer> getUserPermissions(int userId, Connection connection) throws SQLException {
        List<Integer> permissions = new ArrayList<>();
        String getPermissionsCommand = "SELECT permission_id FROM gym.userpermissions WHERE user_id = ?";

        try (PreparedStatement getPermissionsStatement = connection.prepareStatement(getPermissionsCommand)) {
            getPermissionsStatement.setInt(1, userId);
            ResultSet rs = getPermissionsStatement.executeQuery();

            while (rs.next()) {
                permissions.add(rs.getInt("permission_id"));
            }
        }

        return permissions;
    }


    /*     Check of Permission is Exist in DataBase     */
    private boolean permissionExist(Connection connection,int permissionId) throws SQLException{
        String checkPermissionCommand = "SELECT permission_id FROM gym.permissions WHERE permission_id = ?";

        try(PreparedStatement preparedStatement = connection.prepareStatement(checkPermissionCommand)) {

            preparedStatement.setInt(1,permissionId);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        }
    }








    }



