package com.ibntaymiyya.gym.controllers;

import com.ibntaymiyya.gym.Main;

import com.ibntaymiyya.gym.Util.DBAnalysis;
import com.ibntaymiyya.gym.model.AddMemberGetterAndSetter;
import com.ibntaymiyya.gym.model.AnalysisModel;
import io.github.palexdev.materialfx.controls.*;
import io.github.palexdev.mfxresources.fonts.IconsProviders;
import io.github.palexdev.mfxresources.fonts.MFXFontIcon;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.TextAlignment;

import java.awt.*;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class Analysis implements Initializable {

    @FXML
    private MFXButton back_btn_analysisP;
    @FXML
    private Button btn_export_analysis;

    @FXML
    private Label num_all_members;

    @FXML
    private Label num_end_sub;

    @FXML
    private Label num_hold_sub;

    @FXML
    private Label num_profit_current_month;

    @FXML
    private Button applayProfitRange;

    @FXML
    private Button cancelProfitRange;

    @FXML
    private MFXFontIcon endSubs_Icon;

    @FXML
    private MFXFontIcon holdSubs_Icon;

    @FXML
    private MFXFontIcon members_Icon;

    @FXML
    private MFXFontIcon profit_Icon;

    @FXML
    private MFXDatePicker endProfitRange_Date;

    @FXML
    private MFXDatePicker startProfitRange_Date;

    @FXML
    private AnchorPane archPane_Charts;

    @FXML
    private Button allMembersButton_Chart;

    @FXML
    private Button holdSubsButton_Chart;

    @FXML
    private Button endSubsButton_Chart;

    @FXML
    private Button profitButton_Chart;
    @FXML
    private AnchorPane ancherPane_DateRange;



    @FXML
    private MFXTableView<AddMemberGetterAndSetter> ExpenciveSubsTable_Analysis;

    @FXML
    private MFXTableView<AddMemberGetterAndSetter> HoldSubsTable_Analysis;


    Main main = new Main();
    private DBAnalysis dbAnalysis;
    private  AnalysisModel analysisModel;


    private Button lastSelectedButton = profitButton_Chart;
    private String selectedChart = "profit-Active";


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        dbAnalysis = new DBAnalysis();
        analysisModel = new AnalysisModel();
        selectedChartButtons(profitButton_Chart,selectedChart);


        handler();
    }

    private void handler(){
        num_profit_current_month.setText(String.valueOf(dbAnalysis.getProfitFromCurrentMonth()));
        num_all_members.setText(String.valueOf(dbAnalysis.getAllMembersCount()));
        num_end_sub.setText(String.valueOf(dbAnalysis.getExpiredSubscriptionsCount()));
        num_hold_sub.setText(String.valueOf(dbAnalysis.getHoldSubscriptionsCount()));

        analysisModel.holdSubsTable(HoldSubsTable_Analysis);
        analysisModel.totalPayedTable(ExpenciveSubsTable_Analysis);

        analysisModel.profitChart(archPane_Charts  );


        

        setIcons();


    }


    private void setIcons(){
        profit_Icon.setSize(37);
        profit_Icon.setColor(Color.rgb(0, 255, 0, 1));
        profit_Icon.setDescription("fas-dollar-sign");
        profit_Icon.setLayoutY(70);
        profit_Icon.setLayoutX(52);

        members_Icon.setSize(35);
        members_Icon.setColor(Color.rgb(0, 60, 255, 1));
        members_Icon.setDescription("fas-people-group");
        members_Icon.setLayoutY(70);
        members_Icon.setLayoutX(62);

        endSubs_Icon.setSize(35);
        endSubs_Icon.setColor(Color.RED);
        endSubs_Icon.setDescription("fas-stop");
        endSubs_Icon.setLayoutY(70);
        endSubs_Icon.setLayoutX(32);

        holdSubs_Icon.setSize(35);
        holdSubs_Icon.setColor(Color.ORANGE);
        holdSubs_Icon.setDescription("fas-pause");
        holdSubs_Icon.setLayoutY(70);
        holdSubs_Icon.setLayoutX(35);

    }

    public void selectedChartButtons(Button selectedButton ,String activeClass ){
        if (lastSelectedButton != null) {
            lastSelectedButton.getStyleClass().removeIf(style -> style.contains("Active"));
        }
        selectedButton.getStyleClass().add(activeClass);
        lastSelectedButton = selectedButton;
        selectedChart = activeClass;


    }


    @FXML
    private void setDateRange() {

        if (startProfitRange_Date.getValue() == null || endProfitRange_Date.getValue() == null) {

            startProfitRange_Date.setStyle("-fx-border-color: red;");
            endProfitRange_Date.setStyle("-fx-border-color: red;");
        } else {

            startProfitRange_Date.setStyle("-fx-border-color: lightgray;");
            endProfitRange_Date.setStyle("-fx-border-color: lightgray;");

            if (selectedChart.contains("profit-Active")){
                analysisModel.profitChartBetween(startProfitRange_Date.getValue(), endProfitRange_Date.getValue());


            }else if (selectedChart.contains("allMembers-Active")){
                analysisModel.membersRangeChart(startProfitRange_Date.getValue(), endProfitRange_Date.getValue());

            }else if (selectedChart.contains("holdSubs-Active")){
                ancherPane_DateRange.setDisable(true);

            }else if (selectedChart.contains("expireSub-Active")){
                analysisModel.endSubsRangeChart(startProfitRange_Date.getValue(), endProfitRange_Date.getValue());

            }

            archPane_Charts.getChildren().clear();
            archPane_Charts.getChildren().add(analysisModel.lineChart);
        }

    }

    @FXML
    private void setProfitCharts(){
        analysisModel.profitChart(archPane_Charts);
        archPane_Charts.getChildren().clear();
        archPane_Charts.getChildren().add(analysisModel.lineChart);
        selectedChartButtons(profitButton_Chart,"profit-Active");
        ancherPane_DateRange.setDisable(false);
        cleanDateRangeFields();
    }
    @FXML
    private void setMembersCharts(){
        analysisModel.allMembersChart(archPane_Charts);
        archPane_Charts.getChildren().clear();
        archPane_Charts.getChildren().add(analysisModel.lineChart);
        selectedChartButtons(allMembersButton_Chart,"allMembers-Active");
        ancherPane_DateRange.setDisable(false);
        cleanDateRangeFields();
    }
    @FXML
    private void setHoldSubsCharts(){
        analysisModel.holdsubsChart(archPane_Charts);
        archPane_Charts.getChildren().clear();
        archPane_Charts.getChildren().add(analysisModel.roundedChart);
        selectedChartButtons(holdSubsButton_Chart,"holdSubs-Active");
        lastSelectedButton = holdSubsButton_Chart;
        selectedChart = "holdSubs-Active";
        ancherPane_DateRange.setDisable(true);
        cleanDateRangeFields();
    }

    @FXML
    private void setEndSubsCharts(){
        analysisModel.endSubsChart(archPane_Charts);
        archPane_Charts.getChildren().clear();
        archPane_Charts.getChildren().add(analysisModel.lineChart);
        selectedChartButtons(endSubsButton_Chart,"expireSub-Active");
        ancherPane_DateRange.setDisable(false);
        cleanDateRangeFields();
    }

    @FXML
    private void cancelProfitRange(){
        cleanDateRangeFields();
        
        if (selectedChart.contains("profit-Active")){
            analysisModel.profitChart(archPane_Charts );

        }else if (selectedChart.contains("allMembers-Active")){
            analysisModel.allMembersChart(archPane_Charts);

        }else if (selectedChart.contains("expireSub-Active")){
            analysisModel.endSubsChart(archPane_Charts);
        }
    }
    private void cleanDateRangeFields(){
        startProfitRange_Date.setStyle("-fx-border-color: lightgray;");
        endProfitRange_Date.setStyle("-fx-border-color: lightgray;");
        startProfitRange_Date.clear();
        endProfitRange_Date.clear();
    }




    public void mainPage(){

        main.changeScene("fxml/MainPage.fxml");

    }


}
