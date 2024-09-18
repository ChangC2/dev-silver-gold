package view;

import Utils.PreferenceManager;
import Utils.Utils;
import Utils.DateTimeUtils;

import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.http.util.TextUtils;

public class MMSSettingView {

    Stage screen_stage;

    private static MMSSettingView instance;

    public static MMSSettingView getInstance() {
        if (instance == null) {
            instance = new MMSSettingView();
        }
        return instance;
    }

    Slider sliderStopTime;
    Label valueStopTimeLimit;

    Slider sliderPlannedProductioTime;
    Label valuePlannedProductionTime;

    CheckBox ckAutomaticCount;
    Slider sliderMinElapsedCycleTime;
    Label valueMinElapsedCycleTime;
    TextField txtPartsPerCycle;
    CheckBox ckSendAlert;

    TextField txtDownTimeTitle1;
    TextField txtDownTimeTitle2;
    TextField txtDownTImeTitle3;
    TextField txtDownTImeTitle4;
    TextField txtDownTImeTitle5;
    TextField txtDownTImeTitle6;
    TextField txtDownTImeTitle7;
    TextField txtDownTImeTitle8;

    Button btnCancel;
    Button btnSave;

    BorderPane pane;

    public MMSSettingView() {
        initView();
    }

    private void initView() {
        pane = new BorderPane();
        pane.setPrefSize(900, 570);
        pane.setPadding(new Insets(30));

        HBox hBoxPanel = new HBox(20);

        // Left Panel
        VBox vBoxLeftPanel = new VBox(10);
        vBoxLeftPanel.setFillWidth(true);
        HBox.setHgrow(vBoxLeftPanel, Priority.ALWAYS);
        vBoxLeftPanel.setMaxWidth(Double.POSITIVE_INFINITY);
        vBoxLeftPanel.setPrefWidth(500);
        vBoxLeftPanel.setPadding(new Insets(0.0, 20.0, 0.0, 0.0));
        Label labelTimeSettings = new Label("Time Settings");
        labelTimeSettings.setFont(Font.font("Tahoma", FontWeight.BOLD, 16));
        labelTimeSettings.setTextFill(Color.WHITE);
        labelTimeSettings.setPadding(new Insets(0.0, 0.0, 15.0, 0.0));

        // Stop Time Limit
        HBox hBoxStopTimeLimit = new HBox();
        hBoxStopTimeLimit.setAlignment(Pos.CENTER);
        Label labelStopTimeLimit = new Label("Stop Time Limit");
        labelStopTimeLimit.setFont(Font.font("Tahoma", FontWeight.NORMAL, 14));
        labelStopTimeLimit.setTextFill(Color.WHITE);

        valueStopTimeLimit = new Label("00:00:00");
        HBox.setHgrow(valueStopTimeLimit, Priority.ALWAYS);
        valueStopTimeLimit.setAlignment(Pos.CENTER_RIGHT);
        valueStopTimeLimit.setMaxWidth(Double.POSITIVE_INFINITY);
        valueStopTimeLimit.setFont(Font.font("Tahoma", FontWeight.BOLD, 16));
        valueStopTimeLimit.setTextFill(Color.WHITE);
        hBoxStopTimeLimit.getChildren().addAll(labelStopTimeLimit, valueStopTimeLimit);

        long stopTimeLimitInSecs = PreferenceManager.getStopTimeLimit() / 1000;
        valueStopTimeLimit.setText(DateTimeUtils.getElapsedTimeMinutesSecondsStringFromSecs(stopTimeLimitInSecs));

        sliderStopTime = new Slider(1, 480, stopTimeLimitInSecs);                       // Slider Unit is second
        //Setting its orientation to vertical
        sliderStopTime.setOrientation(Orientation.HORIZONTAL);
        sliderStopTime.setMin(1);
        sliderStopTime.setMax(480);
        sliderStopTime.setValue(stopTimeLimitInSecs);
        //sliderFilterAdjust.setShowTickLabels(true);
        sliderStopTime.setShowTickMarks(false);
        sliderStopTime.setMajorTickUnit(5);
        sliderStopTime.setMinorTickCount(1);
        sliderStopTime.setBlockIncrement(10);

        // * Filter slider use stand-alone slider, so no need to change slider.
        sliderStopTime.getStyleClass().add("slider-orange");
        sliderStopTime.styleProperty().bind(Bindings.createStringBinding(() -> {
            double percentage = (sliderStopTime.getValue() - sliderStopTime.getMin()) / (sliderStopTime.getMax() - sliderStopTime.getMin()) * 100.0;
            return String.format("-slider-orange-track-color: linear-gradient(to right, -slider-orange-filled-track-color 0%%, "
                            + "-slider-orange-filled-track-color %f%%, -fx-base %f%%, -fx-base 100%%);",
                    percentage, percentage);
        }, sliderStopTime.valueProperty(), sliderStopTime.minProperty(), sliderStopTime.maxProperty()));

        sliderStopTime.setPadding(new Insets(0, 0, 0, 00));
        sliderStopTime.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                //Setting the angle for the rotation
                if (oldValue == newValue)
                    return;
                valueStopTimeLimit.setText(DateTimeUtils.getElapsedTimeMinutesSecondsStringFromSecs(newValue.longValue()));
            }
        });

        // Planned Production Time
        HBox hBoxPlannedProductionTime = new HBox();
        hBoxPlannedProductionTime.setPadding(new Insets(20,0,0,0));
        hBoxStopTimeLimit.setAlignment(Pos.CENTER);
        Label labelPlannedProduction = new Label("Planned Production Time");
        labelPlannedProduction.setFont(Font.font("Tahoma", FontWeight.NORMAL, 14));
        labelPlannedProduction.setTextFill(Color.WHITE);

        valuePlannedProductionTime = new Label("00:00:00");
        HBox.setHgrow(valuePlannedProductionTime, Priority.ALWAYS);
        valuePlannedProductionTime.setAlignment(Pos.CENTER_RIGHT);
        valuePlannedProductionTime.setMaxWidth(Double.POSITIVE_INFINITY);
        valuePlannedProductionTime.setFont(Font.font("Tahoma", FontWeight.BOLD, 16));
        valuePlannedProductionTime.setTextFill(Color.WHITE);
        hBoxPlannedProductionTime.getChildren().addAll(labelPlannedProduction, valuePlannedProductionTime);

        long plannedProductionTimeMins = PreferenceManager.getPlannedProductionTime() / 60000;
        valuePlannedProductionTime.setText(DateTimeUtils.getElapsedTimeMinutesSecondsStringFromSecs(plannedProductionTimeMins));

        sliderPlannedProductioTime = new Slider(60, 600, plannedProductionTimeMins);   // Slider Unit is minutes
        //Setting its orientation to vertical
        sliderPlannedProductioTime.setOrientation(Orientation.HORIZONTAL);
        sliderPlannedProductioTime.setMin(60);      // 60 min
        sliderPlannedProductioTime.setMax(600);    // 24 hours
        sliderPlannedProductioTime.setValue(plannedProductionTimeMins);
        //sliderFilterAdjust.setShowTickLabels(true);
        sliderPlannedProductioTime.setShowTickMarks(false);
        sliderPlannedProductioTime.setMajorTickUnit(5);
        sliderPlannedProductioTime.setMinorTickCount(1);
        sliderPlannedProductioTime.setBlockIncrement(10);

        // * Filter slider use stand-alone slider, so no need to change slider.
        sliderPlannedProductioTime.getStyleClass().add("slider-orange");
        sliderPlannedProductioTime.styleProperty().bind(Bindings.createStringBinding(() -> {
            double percentage = (sliderPlannedProductioTime.getValue() - sliderPlannedProductioTime.getMin()) / (sliderPlannedProductioTime.getMax() - sliderPlannedProductioTime.getMin()) * 100.0;
            return String.format("-slider-orange-track-color: linear-gradient(to right, -slider-orange-filled-track-color 0%%, "
                            + "-slider-orange-filled-track-color %f%%, -fx-base %f%%, -fx-base 100%%);",
                    percentage, percentage);
        }, sliderPlannedProductioTime.valueProperty(), sliderPlannedProductioTime.minProperty(), sliderPlannedProductioTime.maxProperty()));

        sliderPlannedProductioTime.setPadding(new Insets(0, 0, 0, 00));
        sliderPlannedProductioTime.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                //Setting the angle for the rotation
                if (oldValue == newValue)
                    return;
                valuePlannedProductionTime.setText(DateTimeUtils.getElapsedTimeMinutesSecondsStringFromMins(newValue.longValue()));
            }
        });

        // Automatic Parts Counter Settings
        Label labelAutoPartsCounter = new Label("Automatic Parts Counter");
        labelAutoPartsCounter.setFont(Font.font("Tahoma", FontWeight.BOLD, 16));
        labelAutoPartsCounter.setTextFill(Color.WHITE);
        labelAutoPartsCounter.setPadding(new Insets(30.0, 0.0, 0.0, 0.0));

        ckAutomaticCount = new CheckBox("Automatic Count");
        ckAutomaticCount.setTextFill(Color.WHITE);
        ckAutomaticCount.setFont(Font.font(14));
        ckAutomaticCount.setPadding(new Insets(0.0, 0.0, 0, 0.0));
        ckAutomaticCount.setSelected(PreferenceManager.isAutomaticPartsCounter());
        
        HBox hBoxMinElapsedCycleTime = new HBox();
        hBoxMinElapsedCycleTime.setPadding(new Insets(10,0,0,0));
        hBoxStopTimeLimit.setAlignment(Pos.CENTER);
        Label labelMinElapsedCycleTime = new Label("Min Elapsed Cycle Time");
        labelMinElapsedCycleTime.setFont(Font.font("Tahoma", FontWeight.NORMAL, 14));
        labelMinElapsedCycleTime.setTextFill(Color.WHITE);

        valueMinElapsedCycleTime = new Label("00:00:00");
        HBox.setHgrow(valueMinElapsedCycleTime, Priority.ALWAYS);
        valueMinElapsedCycleTime.setAlignment(Pos.CENTER_RIGHT);
        valueMinElapsedCycleTime.setMaxWidth(Double.POSITIVE_INFINITY);
        valueMinElapsedCycleTime.setFont(Font.font("Tahoma", FontWeight.BOLD, 16));
        valueMinElapsedCycleTime.setTextFill(Color.WHITE);
        hBoxMinElapsedCycleTime.getChildren().addAll(labelMinElapsedCycleTime, valueMinElapsedCycleTime);

        long minElapsedCycleTime = PreferenceManager.getMinElapsedStopTime() / 1000;
        valueMinElapsedCycleTime.setText(DateTimeUtils.getElapsedTimeMinutesSecondsStringFromSecs(minElapsedCycleTime));

        sliderMinElapsedCycleTime = new Slider(5, 600, minElapsedCycleTime);   // Unit is seconds.
        //Setting its orientation to vertical
        sliderMinElapsedCycleTime.setOrientation(Orientation.HORIZONTAL);
        sliderMinElapsedCycleTime.setMin(5);
        sliderMinElapsedCycleTime.setMax(600);
        sliderMinElapsedCycleTime.setValue(minElapsedCycleTime);
        //sliderFilterAdjust.setShowTickLabels(true);
        sliderMinElapsedCycleTime.setShowTickMarks(false);
        sliderMinElapsedCycleTime.setMajorTickUnit(5);
        sliderMinElapsedCycleTime.setMinorTickCount(1);
        sliderMinElapsedCycleTime.setBlockIncrement(10);

        // * Filter slider use stand-alone slider, so no need to change slider.
        sliderMinElapsedCycleTime.getStyleClass().add("slider-orange");
        sliderMinElapsedCycleTime.styleProperty().bind(Bindings.createStringBinding(() -> {
            double percentage = (sliderMinElapsedCycleTime.getValue() - sliderMinElapsedCycleTime.getMin()) / (sliderMinElapsedCycleTime.getMax() - sliderMinElapsedCycleTime.getMin()) * 100.0;
            return String.format("-slider-orange-track-color: linear-gradient(to right, -slider-orange-filled-track-color 0%%, "
                            + "-slider-orange-filled-track-color %f%%, -fx-base %f%%, -fx-base 100%%);",
                    percentage, percentage);
        }, sliderMinElapsedCycleTime.valueProperty(), sliderMinElapsedCycleTime.minProperty(), sliderMinElapsedCycleTime.maxProperty()));

        sliderMinElapsedCycleTime.setPadding(new Insets(0, 0, 0, 00));
        sliderMinElapsedCycleTime.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                //Setting the angle for the rotation
                if (oldValue == newValue)
                    return;
                valueMinElapsedCycleTime.setText(DateTimeUtils.getElapsedTimeMinutesSecondsStringFromSecs(newValue.longValue()));
            }
        });


        HBox hBoxPartsPerCycle = new HBox(15);
        Label labelPPC = new Label("Parts per cycle");
        labelPPC.setTextFill(Color.WHITE);
        labelPPC.setFont(Font.font(14));
        labelPPC.setPrefWidth(150);
        labelPPC.setPrefHeight(26);
        txtPartsPerCycle = new TextField(String.valueOf(PreferenceManager.getPartsPerCycle()));
        txtPartsPerCycle.setPrefHeight(30);
        txtPartsPerCycle.setPrefWidth(200);
        txtPartsPerCycle.getStyleClass().add("setting_inputs");
        hBoxPartsPerCycle.getChildren().addAll(labelPPC, txtPartsPerCycle);
        txtPartsPerCycle.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    txtPartsPerCycle.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        // Automatic Parts Counter Settings
        Label labelCycleStopAlert = new Label("Cycle Stop Alert");
        labelCycleStopAlert.setFont(Font.font("Tahoma", FontWeight.BOLD, 16));
        labelCycleStopAlert.setTextFill(Color.WHITE);
        labelCycleStopAlert.setPadding(new Insets(30.0, 0.0, 0.0, 0.0));

        ckSendAlert = new CheckBox("Send Alert");
        ckSendAlert.setTextFill(Color.WHITE);
        ckSendAlert.setFont(Font.font(14));
        ckSendAlert.setPadding(new Insets(0.0, 0.0, 0, 0.0));
        ckSendAlert.setSelected(PreferenceManager.isCycleStopAlert());

        vBoxLeftPanel.getChildren().addAll(labelTimeSettings,
                hBoxStopTimeLimit, sliderStopTime,
                hBoxPlannedProductionTime, sliderPlannedProductioTime,
                labelAutoPartsCounter, ckAutomaticCount, hBoxMinElapsedCycleTime, sliderMinElapsedCycleTime, hBoxPartsPerCycle, labelCycleStopAlert, ckSendAlert);

        // Right Panel
        VBox vBoxRightPanel = new VBox(25);
        HBox.setHgrow(vBoxRightPanel, Priority.ALWAYS);
        vBoxRightPanel.setMaxWidth(Double.POSITIVE_INFINITY);
        vBoxRightPanel.setPadding(new Insets(0, 0, 0, 10));

        Label labelAlertReportSettings = new Label("Downtime Settings");
        labelAlertReportSettings.setFont(Font.font("Tahoma", FontWeight.BOLD, 16));
        labelAlertReportSettings.setTextFill(Color.WHITE);
        labelAlertReportSettings.setPadding(new Insets(0.0, 0.0, 15.0, 0.0));

        // Downtime Reason Settings
        HBox hBoxReason1 = new HBox(5);
        Label labelEmail1 = new Label("Downtime Reason1");
        labelEmail1.setTextFill(Color.WHITE);
        labelEmail1.setFont(Font.font(14));
        labelEmail1.setPrefWidth(150);
        labelEmail1.setPrefHeight(26);
        txtDownTimeTitle1 = new TextField(PreferenceManager.getDownTimeReason1());
        txtDownTimeTitle1.setPrefHeight(30);
        txtDownTimeTitle1.setPrefWidth(250);
        txtDownTimeTitle1.getStyleClass().add("setting_inputs");
        hBoxReason1.getChildren().addAll(labelEmail1, txtDownTimeTitle1);

        HBox hBoxReason2 = new HBox(5);
        Label labelEmail2 = new Label("Downtime Reason2");
        labelEmail2.setTextFill(Color.WHITE);
        labelEmail2.setFont(Font.font(14));
        labelEmail2.setPrefWidth(150);
        labelEmail2.setPrefHeight(26);
        txtDownTimeTitle2 = new TextField(PreferenceManager.getDownTimeReason2());
        txtDownTimeTitle2.setPrefHeight(30);
        txtDownTimeTitle2.setPrefWidth(250);
        txtDownTimeTitle2.getStyleClass().add("setting_inputs");
        hBoxReason2.getChildren().addAll(labelEmail2, txtDownTimeTitle2);

        HBox hBoxReason3 = new HBox(5);
        Label labelEmail3 = new Label("Downtime Reason3");
        labelEmail3.setTextFill(Color.WHITE);
        labelEmail3.setFont(Font.font(14));
        labelEmail3.setPrefWidth(150);
        labelEmail3.setPrefHeight(26);
        txtDownTImeTitle3 = new TextField(PreferenceManager.getDownTimeReason3());
        txtDownTImeTitle3.setPrefHeight(30);
        txtDownTImeTitle3.setPrefWidth(250);
        txtDownTImeTitle3.getStyleClass().add("setting_inputs");
        hBoxReason3.getChildren().addAll(labelEmail3, txtDownTImeTitle3);

        HBox hBoxReason4 = new HBox(5);
        Label labelEmail4 = new Label("Downtime Reason4");
        labelEmail4.setTextFill(Color.WHITE);
        labelEmail4.setFont(Font.font(14));
        labelEmail4.setPrefWidth(150);
        labelEmail4.setPrefHeight(26);
        txtDownTImeTitle4 = new TextField(PreferenceManager.getDownTimeReason4());
        txtDownTImeTitle4.setPrefHeight(30);
        txtDownTImeTitle4.setPrefWidth(250);
        txtDownTImeTitle4.getStyleClass().add("setting_inputs");
        hBoxReason4.getChildren().addAll(labelEmail4, txtDownTImeTitle4);

        HBox hBoxReason5 = new HBox(5);
        Label labelEmail5 = new Label("Downtime Reason5");
        labelEmail5.setTextFill(Color.WHITE);
        labelEmail5.setFont(Font.font(14));
        labelEmail5.setPrefWidth(150);
        labelEmail5.setPrefHeight(26);
        txtDownTImeTitle5 = new TextField(PreferenceManager.getDownTimeReason5());
        txtDownTImeTitle5.setPrefHeight(30);
        txtDownTImeTitle5.setPrefWidth(250);
        txtDownTImeTitle5.getStyleClass().add("setting_inputs");
        hBoxReason5.getChildren().addAll(labelEmail5, txtDownTImeTitle5);

        HBox hBoxReason6 = new HBox(5);
        Label labelEmail6 = new Label("Downtime Reason6");
        labelEmail6.setTextFill(Color.WHITE);
        labelEmail6.setFont(Font.font(14));
        labelEmail6.setPrefWidth(150);
        labelEmail6.setPrefHeight(26);
        txtDownTImeTitle6 = new TextField(PreferenceManager.getDownTimeReason6());
        txtDownTImeTitle6.setPrefHeight(30);
        txtDownTImeTitle6.setPrefWidth(250);
        txtDownTImeTitle6.getStyleClass().add("setting_inputs");
        hBoxReason6.getChildren().addAll(labelEmail6, txtDownTImeTitle6);

        HBox hBoxReason7 = new HBox(5);
        Label labelEmail7 = new Label("Downtime Reason7");
        labelEmail7.setTextFill(Color.WHITE);
        labelEmail7.setFont(Font.font(14));
        labelEmail7.setPrefWidth(150);
        labelEmail7.setPrefHeight(26);
        txtDownTImeTitle7 = new TextField(PreferenceManager.getDownTimeReason7());
        txtDownTImeTitle7.setPrefHeight(30);
        txtDownTImeTitle7.setPrefWidth(250);
        txtDownTImeTitle7.getStyleClass().add("setting_inputs");
        hBoxReason7.getChildren().addAll(labelEmail7, txtDownTImeTitle7);

        HBox hBoxReason8 = new HBox(5);
        Label labelEmail8 = new Label("Downtime Reason8");
        labelEmail8.setTextFill(Color.WHITE);
        labelEmail8.setFont(Font.font(14));
        labelEmail8.setPrefWidth(150);
        labelEmail8.setPrefHeight(26);
        txtDownTImeTitle8 = new TextField(PreferenceManager.getDownTimeReason8());
        txtDownTImeTitle8.setPrefHeight(30);
        txtDownTImeTitle8.setPrefWidth(250);
        txtDownTImeTitle8.getStyleClass().add("setting_inputs");
        hBoxReason8.getChildren().addAll(labelEmail8, txtDownTImeTitle8);

        // Buttons Action
        btnCancel = new Button("   Cancel   ");
        btnCancel.getStyleClass().add("button-gradient5");
        btnCancel.setOnAction(buttonHandler);

        btnSave = new Button("   Save   ");
        btnSave.getStyleClass().add("button-gradient5");
        btnSave.setOnAction(buttonHandler);

        HBox hbBtn = new HBox(25);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().addAll(btnCancel, btnSave);

        vBoxRightPanel.getChildren().addAll(labelAlertReportSettings, hBoxReason1, hBoxReason2, hBoxReason3, hBoxReason4, hBoxReason5, hBoxReason6, hBoxReason7, hBoxReason8,
                hbBtn);

        hBoxPanel.getChildren().addAll(vBoxLeftPanel, vBoxRightPanel);

        pane.setCenter(hBoxPanel);

        // Init Scene
        Scene scene = new Scene(pane, 900, 570);
        scene.getStylesheets().add(getClass().getClassLoader().getResource("resource/style/rootStyles.css").toExternalForm());

        screen_stage = new Stage(StageStyle.DECORATED);
        screen_stage.setTitle("MMS Settings");

        screen_stage.setScene(scene);
        screen_stage.centerOnScreen();
        screen_stage.getIcons().add(LogoManager.getInstance().getLogo());
        //screen_stage.setAlwaysOnTop(true);
        screen_stage.setResizable(false);
        screen_stage.setMinWidth(900);
        screen_stage.setMinHeight(570);
    }

    public void show() {
        screen_stage.show();
    }

    public void close() {
        screen_stage.close();
    }

    EventHandler<ActionEvent> buttonHandler = new EventHandler<ActionEvent>() {

        @Override
        public void handle(final ActionEvent event) {

            if (event.getSource() == btnCancel) {
                close();
            } else if (event.getSource() == btnSave) {
                // Save MMS Settings
                String downTimeReason1 = txtDownTimeTitle1.getText().trim();
                String downTimeReason2 = txtDownTimeTitle2.getText().trim();
                String downTimeReason3 = txtDownTImeTitle3.getText().trim();
                String downTimeReason4 = txtDownTImeTitle4.getText().trim();
                String downTimeReason5 = txtDownTImeTitle5.getText().trim();
                String downTimeReason6 = txtDownTImeTitle6.getText().trim();
                String downTimeReason7 = txtDownTImeTitle7.getText().trim();
                String downTimeReason8 = txtDownTImeTitle8.getText().trim();

                // Check all is empty
                if (TextUtils.isEmpty(downTimeReason1) &&
                        TextUtils.isEmpty(downTimeReason2) &&
                        TextUtils.isEmpty(downTimeReason3) &&
                        TextUtils.isEmpty(downTimeReason4) &&
                        TextUtils.isEmpty(downTimeReason5) &&
                        TextUtils.isEmpty(downTimeReason6) &&
                        TextUtils.isEmpty(downTimeReason7) &&
                        TextUtils.isEmpty(downTimeReason8)) {

                    Toast.message("Please input downtime reasons!");
                    return;
                }

                // Downtime Reasons
                PreferenceManager.setDowntimeReason1(downTimeReason1);
                PreferenceManager.setDowntimeReason2(downTimeReason2);
                PreferenceManager.setDowntimeReason3(downTimeReason3);
                PreferenceManager.setDowntimeReason4(downTimeReason4);
                PreferenceManager.setDowntimeReason5(downTimeReason5);
                PreferenceManager.setDowntimeReason6(downTimeReason6);
                PreferenceManager.setDowntimeReason7(downTimeReason7);
                PreferenceManager.setDowntimeReason8(downTimeReason8);

                // Refresh Downtime Reasons
                MMSView.getInstance().refreshDowntimeReasons();

                // Stop Time Limit
                PreferenceManager.setStopTimeLimit((long) sliderStopTime.getValue() * 1000);

                // Planned Production TIme
                PreferenceManager.setPlannedProductionTime((long) sliderPlannedProductioTime.getValue() * 60000);   // UI Slider is minutes, but Save as milis

                // Automatic Parts Counter
                PreferenceManager.setMinElapsedStopTime((long) sliderMinElapsedCycleTime.getValue() * 1000);
                PreferenceManager.setAutomaticPartsCounter(ckAutomaticCount.isSelected());

                int partsPerCycle = 0;
                try {
                    partsPerCycle = Integer.parseInt(txtPartsPerCycle.getText());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (partsPerCycle <= 0) {
                    Toast.message("Please input Parts per cycle!");
                    return;
                }
                PreferenceManager.setPartsPerCycle(partsPerCycle);

                PreferenceManager.setCycleStopAlert(ckSendAlert.isSelected());

                Toast.message("Success to Save Settings!");
                close();
            }
        }
    };
}
