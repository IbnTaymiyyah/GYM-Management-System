package com.ibntaymiyya.gym.Util;

import com.ibntaymiyya.gym.model.SettingModel;
import com.ibntaymiyya.gym.model.UserDataGetterAndSetter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class DBLoginUser {

    private SettingModel settingModel;

    public DBLoginUser() {

    }
    public void setSettingModel(SettingModel settingModel){
        this.settingModel = settingModel;
    }




    /*     Check if User Is Exist     */

    public boolean isUserExist(String username, String password) {
        String checkUserCommand = "SELECT COUNT(*) FROM gym.users WHERE username = ? AND password = ?";

        try (Connection connection = DBConnection.getCon();
             PreparedStatement checkUser = connection.prepareStatement(checkUserCommand)) {

            checkUser.setString(1, username);
            checkUser.setString(2,password);
            ResultSet resultSet = checkUser.executeQuery();

            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }


        /*     Check if User is Admin or Not     */

    public boolean isUserAdmin(String userName){
        String checkIsAdminCommand = "SELECT isAdmin FROM gym.users WHERE username = ?";

        try(Connection connection = DBConnection.getCon();
            PreparedStatement preparedStatement = connection.prepareStatement(checkIsAdminCommand)
        ){
            preparedStatement.setString(1,userName);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()){
                String isAdmin = resultSet.getString("isAdmin");

                return "yes".equalsIgnoreCase(isAdmin) || "true".equalsIgnoreCase(isAdmin);
            }


        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }


    /*    Get User Data With Permissions     */



    public UserDataGetterAndSetter getUserDetails(String username) {
        UserDataGetterAndSetter userData = new UserDataGetterAndSetter();
        List<Integer> allPermissions = Arrays.asList(1, 2, 3, 4, 5);

        String userQuery = "SELECT username, password, user_id FROM users WHERE username = ?";
        String permissionsQuery = "SELECT permission_id FROM userpermissions WHERE user_id = ?";

        try (Connection connection = DBConnection.getCon();
             PreparedStatement userStatement = connection.prepareStatement(userQuery)) {

            userStatement.setString(1, username);
            ResultSet userResult = userStatement.executeQuery();

            if (userResult.next()) {
                String password = userResult.getString("password");
                int userId = userResult.getInt("user_id");
                userData.setPassword(password);

                try (PreparedStatement permissionsStatement = connection.prepareStatement(permissionsQuery)) {
                    permissionsStatement.setInt(1, userId);
                    ResultSet permissionsResult = permissionsStatement.executeQuery();

                    Map<Integer,Boolean> permissionsMap = new HashMap<>();
                    for (Integer perm : allPermissions){
                        permissionsMap.put(perm,false);
                    }
                    while (permissionsResult.next()) {
                        int permissionId = permissionsResult.getInt("permission_id");
                        permissionsMap.put(permissionId,true);
                    }

                    userData.setPermissionsMap(permissionsMap);

                    return userData;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }














}
