package view;

import Chart.DateAxis;
import Model.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.chart.Axis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class TimeSavingsDataView {
    BorderPane viewPane = new BorderPane();
    Stage screen_stage;

    private static TimeSavingsDataView instance;

    public static TimeSavingsDataView getInstance() {
        if (instance == null) {
            instance = new TimeSavingsDataView();
        }
        return instance;
    }

    public TimeSavingsDataView() {
        initView();
    }

    Label labelProgramNumber;
    TextField txtProgramNumber;
    Button btnLoadData;

    Button btnViewMode;
    ImageView tableImage;
    ImageView graphImage;
    boolean isTableViewMode = true;

    Button btnClose;

    TableView<TimeSavingData> tableView;
    private final ObservableList<TimeSavingData> timeObservableList = FXCollections.observableArrayList();
    ArrayList<TimeSavingData> timeDataList;

    // Chart Control
    LineChart<Number, Number> lineChart;

    private void initView() {

        viewPane.setTop(getTopView());
        viewPane.setBottom(getBottomView());

        // Center Content Panel
        AnchorPane anchorPane = new AnchorPane();
        viewPane.setCenter(anchorPane);

        // -- Init TableView
        tableView = new TableView<TimeSavingData>();
        tableView.setId("job-table");
        tableView.setEditable(true);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        initTableHeader();
        AnchorPane.setRightAnchor(tableView, 0.0);
        AnchorPane.setLeftAnchor(tableView, 0.0);
        AnchorPane.setTopAnchor(tableView, 0.0);
        AnchorPane.setBottomAnchor(tableView, 0.0);
        anchorPane.getChildren().add(tableView);

        // -- Graph View
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();

        xAxis.setLabel("");
        yAxis.setLabel("Time(sec)");
        lineChart = new LineChart<Number, Number>(xAxis, yAxis);
        lineChart.setTitle("Time Savings");
        lineChart.setVisible(false);
        AnchorPane.setRightAnchor(lineChart, 0.0);
        AnchorPane.setLeftAnchor(lineChart, 0.0);
        AnchorPane.setTopAnchor(lineChart, 0.0);
        AnchorPane.setBottomAnchor(lineChart, 0.0);
        anchorPane.getChildren().add(lineChart);

        // Init Scene
        Scene scene = new Scene(viewPane, 1200, 800);
        scene.getStylesheets().add(getClass().getClassLoader().getResource("resource/style/rootStyles.css").toExternalForm());

        screen_stage = new Stage(StageStyle.DECORATED);
        screen_stage.setTitle("Time Savings Information");

        screen_stage.setScene(scene);
        screen_stage.centerOnScreen();
        screen_stage.getIcons().add(LogoManager.getInstance().getLogo());
        //screen_stage.setAlwaysOnTop(true);
        screen_stage.setResizable(true);
        screen_stage.setMinWidth(1000);
        screen_stage.setMinHeight(700);

        showData();
    }

    public void show() {
        screen_stage.show();
        screen_stage.requestFocus();
        screen_stage.toFront();

        // refres tool data
        showData();
    }

    public void close() {
        screen_stage.close();
    }

    // Load job data
    public void showData() {

        int progNum = 0;
        try {
            progNum = Integer.parseInt(txtProgramNumber.getText());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Reload Data
        if (timeDataList != null && !timeDataList.isEmpty()) {
            timeDataList.clear();
        }
        timeDataList = TimeSavingManager.getInstance().getSavingData(progNum);

        // Init TableView Data
        timeObservableList.clear();
        for (TimeSavingData item : timeDataList) {
            timeObservableList.add(item);
        }

        // Init LineChart Data
        ObservableList<XYChart.Data<Number, Number>> series1Data = FXCollections.observableArrayList();
        ObservableList<XYChart.Data<Number, Number>> series2Data = FXCollections.observableArrayList();
        ObservableList<XYChart.Data<Number, Number>> series3Data = FXCollections.observableArrayList();

        for (int i = 0; i < timeDataList.size(); i++) {
            TimeSavingData timeSavingData = timeDataList.get(i);

            XYChart.Data date1 = new XYChart.Data(i + 1, timeSavingData.getElapsedMonitorTime());
            series1Data.add(date1);

            XYChart.Data date2 = new XYChart.Data(i + 1, timeSavingData.getLearnedMonitorTime());
            series2Data.add(date2);

            XYChart.Data date3 = new XYChart.Data(i + 1, timeSavingData.getTimeSavings());
            series3Data.add(date3);
        }

        ObservableList<XYChart.Series<Number, Number>> series = FXCollections.observableArrayList();
        series.add(new XYChart.Series<>("Elapsed Monitor Time", series1Data));
        series.add(new XYChart.Series<>("Learned Monitor Time", series2Data));
        series.add(new XYChart.Series<>("Time Savings", series3Data));
        lineChart.setData(series);

        // Set up tool tip
        ViewHelper.updateTooltipBehavior(100, 5000, 200, true);
        for (int i = 0; i < lineChart.getData().size(); i++) {
            XYChart.Series<Number, Number> s = lineChart.getData().get(i);
            String tooltipInfo = "";
            if (i == 0) {
                tooltipInfo = "Elapsed Monitor Time : ";
            } else if (i == 1) {
                tooltipInfo = "Learned Monitor Time : ";
            } else if (i == 2) {
                tooltipInfo = "Time Savings(s) : ";
            }

            for (int j = 0; j < s.getData().size(); j++) {
                XYChart.Data<Number, Number> d = s.getData().get(j);
                TimeSavingData timeSavingData = timeDataList.get(j);
                String dateTime = timeSavingData.getDate() + " " + timeSavingData.getTime();

                Tooltip tooltip = new Tooltip(dateTime + "\n" + tooltipInfo + d.getYValue());
                tooltip.setStyle("-fx-font-size: 14");
                Tooltip.install(d.getNode(), tooltip);

                //Adding class on hover
                d.getNode().setOnMouseEntered(event -> d.getNode().getStyleClass().add("onHover"));

                //Removing class on exit
                d.getNode().setOnMouseExited(event -> d.getNode().getStyleClass().remove("onHover"));
            }
        }
    }

    private HBox getTopView() {
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(10);
        hBox.setPadding(new Insets(15, 15, 15, 15));

        labelProgramNumber = new Label("PROGRAM NUMBER");
        labelProgramNumber.setPadding(new Insets(10, 10, 10, 10));
        labelProgramNumber.setId("label-jobid");
        labelProgramNumber.setTextFill(Color.WHITE);
        hBox.getChildren().add(labelProgramNumber);

        txtProgramNumber = new TextField();
        txtProgramNumber.setPromptText("Input Program Number");
        txtProgramNumber.setPrefWidth(200);
        txtProgramNumber.setAlignment(Pos.CENTER);
        hBox.getChildren().add(txtProgramNumber);
        // force the field to be numeric only
        txtProgramNumber.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    txtProgramNumber.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        // Button Load File Data
        btnLoadData = new Button("LOAD");
        btnLoadData.setAlignment(Pos.CENTER);
        btnLoadData.getStyleClass().add("button-gradient5");
        btnLoadData.setPrefWidth(120);
        btnLoadData.setPrefHeight(40);
        btnLoadData.setMaxWidth(120);
        btnLoadData.setMinWidth(120);
        hBox.getChildren().add(btnLoadData);
        btnLoadData.setOnAction(buttonHandler);

        Label labelTitle = new Label("");
        labelTitle.setPadding(new Insets(10, 10, 10, 10));
        labelTitle.setId("label-title-tooldata");
        labelTitle.setFont(ViewHelper.getInstance().getFontInformation());
        labelTitle.setTextFill(Color.WHITE);
        labelTitle.setAlignment(Pos.CENTER);
        labelTitle.setMaxWidth(Double.POSITIVE_INFINITY);
        HBox.setHgrow(labelTitle, Priority.ALWAYS);
        hBox.getChildren().add(labelTitle);

        // Button Insert new Job
        tableImage = new ImageView(LogoManager.getInstance().getMenuTableView());
        tableImage.setFitWidth(24);
        tableImage.setFitHeight(24);

        graphImage = new ImageView(LogoManager.getInstance().getMenuGraphImage());
        graphImage.setFitWidth(24);
        graphImage.setFitHeight(24);

        btnViewMode = new Button();
        btnViewMode.setGraphic(graphImage);
        btnViewMode.setText("");
        btnViewMode.setContentDisplay(ContentDisplay.CENTER);
        btnViewMode.setPadding(new Insets(5, 5, 5, 5));
        btnViewMode.setMaxWidth(Double.MAX_VALUE);
        btnViewMode.getStyleClass().add("icon-button");
        btnViewMode.setCursor(Cursor.HAND);

        hBox.getChildren().add(btnViewMode);
        btnViewMode.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                isTableViewMode = !isTableViewMode;

                // Change Icon
                btnViewMode.setGraphic(isTableViewMode ? graphImage : tableImage);

                if (isTableViewMode) {
                    lineChart.setVisible(false);
                    tableView.setVisible(true);
                } else {
                    tableView.setVisible(false);
                    lineChart.setVisible(true);
                }
            }
        });

        return hBox;
    }

    private VBox getBottomView() {
        VBox vBox = new VBox();
        vBox.setSpacing(10);
        vBox.setPadding(new Insets(10, 15, 10, 15));

        HBox hButtonBox = new HBox();
        hButtonBox.setAlignment(Pos.CENTER);
        hButtonBox.setSpacing(10);

        // Button Close And Save
        btnClose = new Button("Close");
        btnClose.setAlignment(Pos.CENTER);
        btnClose.getStyleClass().add("button-gradient5");
        btnClose.setPrefWidth(150);
        btnClose.setMaxWidth(200);
        btnClose.setMinWidth(120);
        BorderPane.setMargin(btnClose, new Insets(10, 10, 10, 10));
        hButtonBox.getChildren().add(btnClose);
        btnClose.setOnAction(buttonHandler);

        vBox.getChildren().add(hButtonBox);

        return vBox;
    }

    private void initTableHeader() {
        // Date
        PercentageTableColumn colDate = new PercentageTableColumn("Date");
        //colDate.setMinWidth(100);
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));

        // Time
        PercentageTableColumn colTime = new PercentageTableColumn("Time");
        //colTime.setMinWidth(100);
        colTime.setCellValueFactory(new PropertyValueFactory<>("time"));

        // Tool
        PercentageTableColumn colTool = new PercentageTableColumn("Tool");
        //colTool.setMinWidth(100);
        colTool.setCellValueFactory(new PropertyValueFactory<>("tool"));

        // Section
        PercentageTableColumn colSection = new PercentageTableColumn("Section");
        //colSection.setMinWidth(100);
        colSection.setCellValueFactory(new PropertyValueFactory<>("section"));

        // Channel
        PercentageTableColumn colChannel = new PercentageTableColumn("Channel");
        //colChannel.setMinWidth(100);
        colChannel.setCellValueFactory(new PropertyValueFactory<>("channel"));

        // Learned Monitor Time
        PercentageTableColumn colLearnedMonitorTime = new PercentageTableColumn("Learned Monitor Time");
        //colLearnedMonitorTime.setMinWidth(150);
        colLearnedMonitorTime.setPercentageWidth(15);
        colLearnedMonitorTime.setCellValueFactory(new PropertyValueFactory<>("learnedMonitorTime"));

        // Wear Limit
        PercentageTableColumn colElapsedMonitorTime = new PercentageTableColumn("Elapsed Monitor Time");
        //colElapsedMonitorTime.setMinWidth(150);
        colElapsedMonitorTime.setPercentageWidth(15);
        colElapsedMonitorTime.setCellValueFactory(new PropertyValueFactory<TableModelJobData, String>("elapsedMonitorTime"));

        // Saving Time
        PercentageTableColumn colTimeSavings = new PercentageTableColumn("Time Savings(s)");
        //colTimeSavings.setMinWidth(100);
        colTimeSavings.setCellValueFactory(new PropertyValueFactory<>("timeSavings"));

        // Low Limit Time
        PercentageTableColumn colTimeSavingsPer = new PercentageTableColumn("Time Savings(%)");
        //colTimeSavingsPer.setMinWidth(100);
        colTimeSavingsPer.setCellValueFactory(new PropertyValueFactory<>("timeSavingsPer"));

        /*tableView.setColumnResizePolicy( TableView.CONSTRAINED_RESIZE_POLICY );
        colDate.setMaxWidth( 1f * Integer.MAX_VALUE * 10 );                 // 10% width
        colTime.setMaxWidth( 1f * Integer.MAX_VALUE * 10 );                 // 10% width
        colTool.setMaxWidth( 1f * Integer.MAX_VALUE * 10 );                 // 10% width
        colSection.setMaxWidth( 1f * Integer.MAX_VALUE * 10 );              // 10% width
        colChannel.setMaxWidth( 1f * Integer.MAX_VALUE * 10 );              // 10% width
        colLearnedMonitorTime.setMaxWidth( 1f * Integer.MAX_VALUE * 10 );   // 10% width
        colElapsedMonitorTime.setMaxWidth( 1f * Integer.MAX_VALUE * 10 );   // 10% width
        colTimeSavings.setMaxWidth( 1f * Integer.MAX_VALUE * 15 );          // 15% width
        colTimeSavingsPer.setMaxWidth( 1f * Integer.MAX_VALUE * 15 );       // 15% width*/

        tableView.getColumns().addAll(colDate, colTime, colTool, colSection, colChannel, colLearnedMonitorTime, colElapsedMonitorTime, colTimeSavings, colTimeSavingsPer);
        tableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        tableView.setItems(timeObservableList);
    }

    final EventHandler<ActionEvent> buttonHandler = new EventHandler<ActionEvent>() {

        @Override
        public void handle(final ActionEvent event) {

            if (event.getSource() == btnLoadData) { // LOADS THE DATA FROM FILE
                showData();
            } else if (event.getSource() == btnClose) {
                close();
            }
        }
    };
}
