package com.ibntaymiyya.gym.controllers;

import com.ibntaymiyya.gym.Main;
import com.ibntaymiyya.gym.model.AddMemberGetterAndSetter;
import com.ibntaymiyya.gym.model.AllMembersModel;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class AllMembers implements Initializable {
    @FXML
    private MFXTableView<AddMemberGetterAndSetter> allMembers_table;

    @FXML
    private MFXTextField textField_Seaech;

    Main main = new Main();
    private final AllMembersModel allMembersModel ;

    public AllMembers() {
        this.allMembersModel = new AllMembersModel();  // Now AllMembers knows about AllMembersModel
    }



    public void backToMainPage(ActionEvent event) {

        main.changeScene("fxml/MainPage.fxml");
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        allMembersModel.allMembers(allMembers_table,textField_Seaech);
    }
}
