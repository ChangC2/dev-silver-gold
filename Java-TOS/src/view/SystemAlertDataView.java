package view;

import Model.*;
import Utils.DateTimeUtils;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.http.client.utils.DateUtils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

public class SystemAlertDataView {
    BorderPane viewPane = new BorderPane();
    Stage screen_stage;

    private static SystemAlertDataView instance;

    public static SystemAlertDataView getInstance() {
        if (instance == null) {
            instance = new SystemAlertDataView();
        }
        return instance;
    }

    public SystemAlertDataView() {
        initView();
    }

    Label labelProgramNumber;
    TextField txtProgramNumber;

    Label labelDateFrom;
    DatePicker dpStart;

    Label labelDateTo;
    DatePicker dpEnd;

    Button btnLoadData;

    Button btnClose;

    TableView<SystemAlertData> tableView;
    private final ObservableList<SystemAlertData> timeObservableList = FXCollections.observableArrayList();
    ArrayList<SystemAlertData> alertDataList;

    private void initView() {

        viewPane.setTop(getTopView());
        viewPane.setBottom(getBottomView());

        // Center Content Panel
        AnchorPane anchorPane = new AnchorPane();
        viewPane.setCenter(anchorPane);

        // -- Init TableView
        tableView = new TableView<SystemAlertData>();
        tableView.setId("job-table");
        tableView.setEditable(true);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        initTableHeader();
        AnchorPane.setRightAnchor(tableView, 0.0);
        AnchorPane.setLeftAnchor(tableView, 0.0);
        AnchorPane.setTopAnchor(tableView, 0.0);
        AnchorPane.setBottomAnchor(tableView, 0.0);
        anchorPane.getChildren().add(tableView);

        // Init Scene
        Scene scene = new Scene(viewPane, 1200, 800);
        scene.getStylesheets().add(getClass().getClassLoader().getResource("resource/style/rootStyles.css").toExternalForm());

        screen_stage = new Stage(StageStyle.DECORATED);
        screen_stage.setTitle("System Alarms");

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
        if (alertDataList != null && !alertDataList.isEmpty()) {
            alertDataList.clear();
        }

        Date startDate = new Date();
        LocalDate localStartDate = dpStart.getValue();
        if (localStartDate != null) {
            Instant instant = Instant.from(localStartDate.atStartOfDay(ZoneId.systemDefault()));
            startDate = Date.from(instant);
        } else {
            startDate = DateTimeUtils.parseDataFromFormat("1970-01-01", "yyyy-MM-dd");
        }

        Date endDate = new Date();
        LocalDate localEndDate = dpStart.getValue();
        if (localEndDate != null) {
            Instant instant = Instant.from(localEndDate.atStartOfDay(ZoneId.systemDefault()));
            endDate = Date.from(instant);
        }

        alertDataList = SystemAlertManager.getInstance().getSavingData(progNum, startDate, endDate);

        // Init TableView Data
        timeObservableList.clear();
        for (SystemAlertData item : alertDataList) {
            timeObservableList.add(item);
        }
    }

    private HBox getTopView() {
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(10);
        hBox.setPadding(new Insets(15, 15, 15, 15));

        labelProgramNumber = new Label("PROGRAM NUMBER");
        labelProgramNumber.setPadding(new Insets(10, 3, 10, 10));
        labelProgramNumber.setId("label-jobid");
        labelProgramNumber.setTextFill(Color.WHITE);
        hBox.getChildren().add(labelProgramNumber);

        txtProgramNumber = new TextField();
        txtProgramNumber.setPromptText("Program Number");
        txtProgramNumber.setPrefWidth(120);
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

        labelDateFrom = new Label("From:");
        labelDateFrom.setPadding(new Insets(10, 3, 10, 10));
        labelDateFrom.setId("label-jobid");
        labelDateFrom.setTextFill(Color.WHITE);
        hBox.getChildren().add(labelDateFrom);

        dpStart = new DatePicker();
        // Hide week numbers
        dpStart.setShowWeekNumbers(false);
        dpStart.prefWidth(100);
        hBox.getChildren().add(dpStart);

        labelDateTo = new Label("To:");
        labelDateTo.setPadding(new Insets(10, 3, 10, 10));
        labelDateTo.setId("label-jobid");
        labelDateTo.setTextFill(Color.WHITE);
        hBox.getChildren().add(labelDateTo);

        dpEnd = new DatePicker();
        // Hide week numbers
        dpEnd.setShowWeekNumbers(false);
        dpEnd.prefWidth(100);
        hBox.getChildren().add(dpEnd);

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
        PercentageTableColumn colAlarmType = new PercentageTableColumn("Alarm Type");
        //colLearnedMonitorTime.setMinWidth(150);
        colAlarmType.setPercentageWidth(30);
        colAlarmType.setCellValueFactory(new PropertyValueFactory<>("alarmType"));

        // Wear Limit
        PercentageTableColumn colElapsedMonitorTime = new PercentageTableColumn("Elapsed Monitor Time");
        //colElapsedMonitorTime.setMinWidth(150);
        colElapsedMonitorTime.setPercentageWidth(15);
        colElapsedMonitorTime.setCellValueFactory(new PropertyValueFactory<TableModelJobData, String>("elapsedMonitorTime"));


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

        tableView.getColumns().addAll(colDate, colTime, colTool, colSection, colChannel, colAlarmType, colElapsedMonitorTime);
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
