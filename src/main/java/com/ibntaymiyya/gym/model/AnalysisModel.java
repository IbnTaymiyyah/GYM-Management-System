package com.ibntaymiyya.gym.model;

import com.ibntaymiyya.gym.Main;
import com.ibntaymiyya.gym.Util.DBAnalysis;
import com.ibntaymiyya.gym.Util.DBMembers;
import com.ibntaymiyya.gym.controllers.Analysis;
import com.ibntaymiyya.gym.controllers.MemberPageController;
import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.TileBuilder;
import eu.hansolo.tilesfx.chart.ChartData;
import eu.hansolo.tilesfx.chart.SmoothedChart;
import eu.hansolo.tilesfx.chart.SunburstChart;
import eu.hansolo.tilesfx.chart.TilesFXSeries;
import eu.hansolo.tilesfx.tools.TreeNode;

import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableRow;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import io.github.palexdev.materialfx.filter.IntegerFilter;
import io.github.palexdev.materialfx.filter.StringFilter;
import io.github.palexdev.mfxresources.fonts.IconsProviders;
import io.github.palexdev.mfxresources.fonts.MFXFontIcon;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.TextAlignment;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static eu.hansolo.toolboxfx.Constants.RND;

public class AnalysisModel {

    private DBAnalysis dbAnalysis;
    private Main main = new Main();
    private DBMembers dbMembers = new DBMembers();
    private Analysis analysis = new Analysis();
    private AddMemberGetterAndSetter addMemberGetterAndSetter = new AddMemberGetterAndSetter();


    public AnalysisModel(){
        this.dbAnalysis = new DBAnalysis();
        this.dbAnalysis.setAnalysisModel(this);
    }



    // Hold Subs Table to Show and take Action for Held Subs

    public void holdSubsTable(MFXTableView<AddMemberGetterAndSetter> HoldSubsTable){

        String id = "ID" , name = "الاسم" ,holdDays = "الايام المتبقيه";

        MFXTableColumn<AddMemberGetterAndSetter> idColumn = new MFXTableColumn<>(id,false, Comparator.comparing(AddMemberGetterAndSetter::getId));
        MFXTableColumn<AddMemberGetterAndSetter> nameColumn = new MFXTableColumn<>(name,false,Comparator.comparing(AddMemberGetterAndSetter::getName));
        MFXTableColumn<AddMemberGetterAndSetter> daysLeftColumn = new MFXTableColumn<>(holdDays,false,Comparator.comparing(AddMemberGetterAndSetter::getHoldDays));
        MFXTableColumn<AddMemberGetterAndSetter> buttonColumn = new MFXTableColumn<>("",false,null);


        idColumn.setRowCellFactory(addMemberGetterAndSetter -> new MFXTableRowCell<>(AddMemberGetterAndSetter::getId));
        nameColumn.setRowCellFactory(addMemberGetterAndSetter -> new MFXTableRowCell<>(AddMemberGetterAndSetter::getName));
        daysLeftColumn.setRowCellFactory(addMemberGetterAndSetter -> new MFXTableRowCell<>(AddMemberGetterAndSetter::getHoldDays));

        buttonColumn.setRowCellFactory(addMemberGetterAndSetter -> new MFXTableRowCell<>(null){
            final MFXButton button = new MFXButton("الغاء التجميد");
            final MFXFontIcon icon = new MFXFontIcon();
            String resourcePath ="/com/ibntaymiyya/gym/css/Buttons.css";
            URL resourceUrl = getClass().getResource(resourcePath);






            @Override
            public void update(AddMemberGetterAndSetter item){
                super.update(item);
                setGraphic(null);

                if (resourceUrl == null) {
                    System.out.println("Resource Not found: " + resourcePath);
                } else {
                    button.getStylesheets().add(resourceUrl.toExternalForm());
                }

                button.getStyleClass().add("button-ResumeSubsAnalysis");

                icon.setSize(15);
                icon.setColor(Color.rgb(0, 0, 0, 0.38));
                icon.setTextAlignment(TextAlignment.CENTER);
                icon.setDescription("fas-circle-play");


                if (item != null){
                    button.setGraphic(icon);
                    button.setOnAction(actionEvent ->  {
                        dbMembers.resumeSubscription(item.getId());
                        main.changeScene("fxml/Analysis.fxml");
                    });
                    setGraphic(button);
                }else {
                    setGraphic(null);

                }



            }

        });


        HoldSubsTable.setTableRowFactory(row ->{
            MFXTableRow<AddMemberGetterAndSetter> tableRow = new MFXTableRow<>(HoldSubsTable , null);

            tableRow.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {

                if (event.getClickCount() == 2 && tableRow.getData() != null){
                    AddMemberGetterAndSetter rowData = tableRow.getData();
                    event.consume();

                    int memberId = rowData.getId();
                    setMemberInfo(memberId);
                }
            });
            return tableRow;
        });


        HoldSubsTable.widthProperty().addListener((observableValue, oldValue, newValue) ->{
            double tableWidth = newValue.doubleValue();
            double columnWidth = tableWidth / 4;

            idColumn.setPrefWidth(columnWidth);
            nameColumn.setPrefWidth(columnWidth);
            buttonColumn.setPrefWidth(columnWidth);
            daysLeftColumn.setPrefWidth(columnWidth);


        } );

        HoldSubsTable.getTableColumns().addAll(idColumn,nameColumn,daysLeftColumn,buttonColumn);

        HoldSubsTable.getFilters().addAll(
                new IntegerFilter<>(id,AddMemberGetterAndSetter::getId),
                new StringFilter<>(name,AddMemberGetterAndSetter::getName)

        );

        HoldSubsTable.features().enableBounceEffect();
        HoldSubsTable.features().enableSmoothScrolling(0.7);


        dbAnalysis.getHoldSubs(HoldSubsTable);

    }






    // Total Payed Members Table

    public void totalPayedTable(MFXTableView<AddMemberGetterAndSetter> totalPayedTable){

        String id = "ID" , name = "الاسم" ,totalPayed = "اجمالي المدفوع";

        MFXTableColumn<AddMemberGetterAndSetter> idColumn = new MFXTableColumn<>(id,false, Comparator.comparing(AddMemberGetterAndSetter::getId));
        MFXTableColumn<AddMemberGetterAndSetter> nameColumn = new MFXTableColumn<>(name,false,Comparator.comparing(AddMemberGetterAndSetter::getName));
        MFXTableColumn<AddMemberGetterAndSetter> totalPayedColumn = new MFXTableColumn<>(totalPayed,false,Comparator.comparing(AddMemberGetterAndSetter::getTotalPayed));


        idColumn.setRowCellFactory(addMemberGetterAndSetter -> new MFXTableRowCell<>(AddMemberGetterAndSetter::getId));
        nameColumn.setRowCellFactory(addMemberGetterAndSetter -> new MFXTableRowCell<>(AddMemberGetterAndSetter::getName));
        totalPayedColumn.setRowCellFactory(addMemberGetterAndSetter -> new MFXTableRowCell<>(AddMemberGetterAndSetter::getTotalPayed));



        totalPayedTable.setTableRowFactory(row ->{
            MFXTableRow<AddMemberGetterAndSetter> tableRow = new MFXTableRow<>(totalPayedTable , null);

            tableRow.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {

                if (event.getClickCount() == 2 && tableRow.getData() != null){
                    AddMemberGetterAndSetter rowData = tableRow.getData();
                    event.consume();

                    int memberId = rowData.getId();
                    setMemberInfo(memberId);
                }
            });
            return tableRow;
        });


        totalPayedTable.widthProperty().addListener((observableValue, oldValue, newValue) ->{
            double tableWidth = newValue.doubleValue();
            double columnWidth = tableWidth / 3;

            idColumn.setPrefWidth(columnWidth);
            nameColumn.setPrefWidth(columnWidth);
            totalPayedColumn.setPrefWidth(columnWidth);



        } );

        totalPayedTable.getTableColumns().addAll(idColumn,nameColumn,totalPayedColumn);

        totalPayedTable.getFilters().addAll(
                new IntegerFilter<>(id,AddMemberGetterAndSetter::getId),
                new StringFilter<>(name,AddMemberGetterAndSetter::getName)

        );

        totalPayedTable.features().enableBounceEffect();
        totalPayedTable.features().enableSmoothScrolling(0.7);


        dbAnalysis.getTotalPayedMembers(totalPayedTable);


    }


    /*------------------------------------CHARTS------------------------------------*/



    public Tile lineChart;
    public Tile roundedChart;
    private LocalDate currentDate = LocalDate.now();
    private int  currentYear = currentDate.getYear();
    private int currentMonth = currentDate.getMonthValue();


    /*     Profit Chart , Current Month & Range     */


    public void profitChart( AnchorPane chartsPane ) {
        currentMonthProfitChart();
        chartsPane.getChildren().clear();
        chartsPane.getChildren().add(lineChart);


    }

    private void currentMonthProfitChart() {

        Map<String, Double> profitData = dbAnalysis.getProfitData();
        XYChart.Series<String, Number> seriesProfit = new XYChart.Series<>();


        for (int day = 1; day <= currentDate.lengthOfMonth(); day++) {
             String formattedDate = String.format("%02d-%02d-%04d", currentMonth, day, currentYear);
            double profits = profitData.getOrDefault(formattedDate, 0.00);
            seriesProfit.getData().add(new XYChart.Data<>(formattedDate, profits));

        }

        lineChart = TileBuilder.create().skinType(Tile.SkinType.SMOOTHED_CHART)
                .dateColor(Color.RED)
                .prefSize(744, 359)
                .title("الايرادات هذا الشهر")
                .chartType(Tile.ChartType.AREA)
                .animated(true)
                .smoothing(true)
                .tooltipTimeout(1000)
                .tilesFxSeries(new TilesFXSeries<>(seriesProfit,
                        Tile.GREEN,
                        new LinearGradient(0, 0, 0, 1,
                                true, CycleMethod.NO_CYCLE,
                                new Stop(0, Tile.GREEN),
                                new Stop(1, Color.TRANSPARENT))))

                .build();






    }


    public void profitChartBetween(LocalDate startDateRange, LocalDate endDateRange) {


        Map<String, Double> profitData = dbAnalysis.getProfitDataRange(startDateRange, endDateRange);
        XYChart.Series<String, Number> seriesProfitRange = new XYChart.Series<>();


        LocalDate date = startDateRange;
        while (!date.isAfter(endDateRange)) {
            String formattedDate = String.format("%02d-%02d-%04d", date.getMonthValue(), date.getDayOfMonth(), date.getYear());
            double profits = profitData.getOrDefault(formattedDate, 0.00);
            seriesProfitRange.getData().add(new XYChart.Data<>(formattedDate, profits));
            date = date.plusDays(1);
        }

        lineChart = TileBuilder.create()
                .skinType(Tile.SkinType.SMOOTHED_CHART)
                .prefSize(744, 359)
                .title("الايرادات من " + startDateRange.toString() + " الي " + endDateRange.toString())
                .chartType(Tile.ChartType.AREA)
                .animated(true)
                .smoothing(true)
                .tooltipTimeout(1000)
                .tilesFxSeries(new TilesFXSeries<>(seriesProfitRange,
                        Tile.GREEN,
                        new LinearGradient(0, 0, 0, 1,
                                true, CycleMethod.NO_CYCLE,
                                new Stop(0, Tile.GREEN),
                                new Stop(1, Color.TRANSPARENT))))
                .build();
    }



    /*      All Members Chart , Current Month & Range    */

    public void allMembersChart(AnchorPane chartsPane){
        currentMonthMembers();
        chartsPane.getChildren().clear();
        chartsPane.getChildren().add(lineChart);
    }

    private void currentMonthMembers(){
        Map<String,Integer> membersData = dbAnalysis.getMembersData();
        XYChart.Series<String, Number> seriesMembers = new XYChart.Series<>();

        for (int day = 1 ; day <= currentDate.lengthOfMonth() ; day++){
            String formattedDate = String.format("%02d-%02d-%04d" ,currentMonth,day,currentYear);
            int members = membersData.getOrDefault(formattedDate,0);

            seriesMembers.getData().add(new XYChart.Data<>(formattedDate,members));
        }

        lineChart = TileBuilder.create()
                .skinType(Tile.SkinType.SMOOTHED_CHART)
                .prefSize(744, 359)
                .chartType(Tile.ChartType.AREA)
                .title("المشتركين")
                .alertMessage("NOOOO")
                .animated(true)
                .smoothing(true)
                .tooltipTimeout(1000)
                .tilesFxSeries(new TilesFXSeries<>(seriesMembers,
                        Tile.BLUE,
                                     new LinearGradient(0,0,0,1,
                                             true , CycleMethod.NO_CYCLE,
                                             new Stop(0,Tile.BLUE),
                                             new Stop(1,Color.TRANSPARENT))))

                        .build();

    }

    public void membersRangeChart(LocalDate startDateRange, LocalDate endDateRange){
        Map<String , Integer> membersData = dbAnalysis.getMembersDataRange(startDateRange,endDateRange);
        XYChart.Series<String, Number> seriesMembersRange = new XYChart.Series<>();

        LocalDate date = startDateRange;
        while (!date.isAfter(endDateRange)){
            String formattedDate = String.format("%02d-%02d-%04d", date.getMonthValue(), date.getDayOfMonth(), date.getYear());
            int members = membersData.getOrDefault(formattedDate,0);
            seriesMembersRange.getData().add(new XYChart.Data<>(formattedDate,members));
            date = date.plusDays(1);

        }

        lineChart = TileBuilder.create()
                .skinType(Tile.SkinType.SMOOTHED_CHART)
                .prefSize(744, 359)
                .title( " المشتركين "+ startDateRange.toString() + " الي " + endDateRange.toString())
                .chartType(Tile.ChartType.AREA)
                .animated(true)
                .smoothing(true)
                .tooltipTimeout(1000)
                .tilesFxSeries(new TilesFXSeries<>(seriesMembersRange,
                        Tile.BLUE,
                        new LinearGradient(0,0,0,1,
                                true , CycleMethod.NO_CYCLE,
                                new Stop(0,Tile.BLUE),
                                new Stop(1,Color.TRANSPARENT))))

                .build();


    }



    /*      End Subs Chart , Current Month & Range    */

    public void endSubsChart(AnchorPane chartsPane){
        currentMonthEndSubs();
        chartsPane.getChildren().clear();
        chartsPane.getChildren().add(lineChart);
    }

    private void currentMonthEndSubs(){
        Map<String,Integer> mendSubsData = dbAnalysis.getEndSubsData();
        XYChart.Series<String, Number> seriesEndSubs = new XYChart.Series<>();

        for (int day = 1 ; day <= currentDate.lengthOfMonth() ; day++){
            String formattedDate = String.format("%02d-%02d-%04d" ,currentMonth,day,currentYear);
            int members = mendSubsData.getOrDefault(formattedDate,0);

            seriesEndSubs.getData().add(new XYChart.Data<>(formattedDate,members));
        }

        lineChart = TileBuilder.create()
                .skinType(Tile.SkinType.SMOOTHED_CHART)
                .prefSize(744, 359)
                .chartType(Tile.ChartType.AREA)
                .title("الاشتراكات المنتهيه")
                .animated(true)
                .smoothing(true)
                .tooltipTimeout(1000)
                .tilesFxSeries(new TilesFXSeries<>(seriesEndSubs,
                        Tile.RED,
                                     new LinearGradient(0,0,0,1,
                                             true , CycleMethod.NO_CYCLE,
                                             new Stop(0,Tile.RED),
                                             new Stop(1,Color.TRANSPARENT))))

                        .build();

    }

    public void endSubsRangeChart(LocalDate startDateRange, LocalDate endDateRange){
        Map<String , Integer> mendSubsData = dbAnalysis.getEndSubsDataRange(startDateRange,endDateRange);
        XYChart.Series<String, Number> seriesEndSubsRange = new XYChart.Series<>();

        LocalDate date = startDateRange;
        while (!date.isAfter(endDateRange)){
            String formattedDate = String.format("%02d-%02d-%04d", date.getMonthValue(), date.getDayOfMonth(), date.getYear());
            int members = mendSubsData.getOrDefault(formattedDate,0);
            seriesEndSubsRange.getData().add(new XYChart.Data<>(formattedDate,members));
            date = date.plusDays(1);

        }

        lineChart = TileBuilder.create()
                .skinType(Tile.SkinType.SMOOTHED_CHART)
                .prefSize(744, 359)
                .title( " الاشتراكات المنتهيه من "+ startDateRange.toString() + " الي " + endDateRange.toString())
                .chartType(Tile.ChartType.AREA)
                .animated(true)
                .smoothing(true)
                .tooltipTimeout(1000)
                .tilesFxSeries(new TilesFXSeries<>(seriesEndSubsRange,
                        Tile.RED,
                        new LinearGradient(0,0,0,1,
                                true , CycleMethod.NO_CYCLE,
                                new Stop(0,Tile.RED),
                                new Stop(1,Color.TRANSPARENT))))

                .build();


    }


    /*   Hold Subs Chart , Current Month & Range    */

    public void holdsubsChart(AnchorPane chartsPane){
        currentMonthHoldSubs();
        chartsPane.getChildren().clear();
        chartsPane.getChildren().add(roundedChart);
    }

    private void currentMonthHoldSubs(){
        int allMembersCount = dbAnalysis.getAllMembersCount();
        int holdMembersCount = dbAnalysis.getHoldSubscriptionsCount();

        double holdSubsPercentage = (double) holdMembersCount / allMembersCount  * 100;

        Map<String , Integer> holdSubsData = dbAnalysis.getHoldSubsData();
        Map<String , Integer> endSubsData = dbAnalysis.getEndSubsData();
        Map<String , Integer> allMembersData = dbAnalysis.getMembersData();


        // Tree Data
        TreeNode tree   = new TreeNode(new ChartData("ROOT"));

        TreeNode holdSubsNode = new TreeNode(new ChartData("المجمدة" , holdSubsData.size(),Tile.ORANGE ),tree);
        for (Map.Entry<String,Integer> entry : holdSubsData.entrySet()){
            new TreeNode<>(new ChartData(entry.getKey(),entry.getValue(),Tile.ORANGE),holdSubsNode);
        }
        TreeNode endSubsNode = new TreeNode(new ChartData("المنتهية" , endSubsData.size(),Tile.RED),tree);
        for(Map.Entry<String , Integer> entry : endSubsData.entrySet()){
            new TreeNode<>(new ChartData(entry.getKey(),entry.getValue(),Tile.RED),endSubsNode);
        }
        TreeNode allMembersNode = new TreeNode(new ChartData("الكل" , allMembersData.size(),Tile.BLUE),tree);
        for(Map.Entry<String , Integer> entry : allMembersData.entrySet()){
            new TreeNode<>(new ChartData(entry.getKey(),entry.getValue(),Tile.BLUE),allMembersNode);
        }



        roundedChart = TileBuilder.create().skinType(Tile.SkinType.SUNBURST)
                .prefSize(744, 359)
                .title(" الاشتراكات المجمدة ")
                .textVisible(true)
                .text(  "  نسبة الاشتراكات المجمدة  "+ String.format("%.2f%%" ,holdSubsPercentage))
                .sunburstTree(tree)
                .sunburstBackgroundColor(Tile.BACKGROUND)
                .sunburstTextColor(Tile.BACKGROUND)
                .sunburstUseColorFromParent(true)
                .sunburstTextOrientation(SunburstChart.TextOrientation.TANGENT)
                .sunburstAutoTextColor(true)
                .sunburstUseChartDataTextColor(true)
                .sunburstInteractive(true)
                .build();
    }








    /*------------------------------------PRIVATE------------------------------------*/

    /*  get member Info and Send it to display in Member Page (MemberPageController)  */
    private void setMemberInfo(int memberId){
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



}
