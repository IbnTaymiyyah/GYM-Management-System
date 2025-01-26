package com.ibntaymiyya.gym.Util;

import com.ibntaymiyya.gym.controllers.Login;
import com.ibntaymiyya.gym.model.DialogConfig;
import com.ibntaymiyya.gym.model.SettingModel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

public class DBCheckRunning {


    Login login ;
    SettingModel settingModel;


        public void loginPage(Login login){
            this.login = login;
        }
        public void settingModel(SettingModel settingModel){
            this.settingModel = settingModel;
        }




    public Boolean checkMySqlService(){



            try (Connection connection = DBConnection.getCon()) {
                return true;

            } catch (SQLException e) {
                return false;
            }

    }


}
