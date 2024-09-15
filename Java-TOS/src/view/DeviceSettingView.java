package view;

import Utils.PreferenceManager;
import Utils.Utils;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
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

public class DeviceSettingView {

    Stage screen_stage;

    private static DeviceSettingView instance;

    public static DeviceSettingView getInstance() {
        if (instance == null) {
            instance = new DeviceSettingView();
        }
        return instance;
    }

    //https://www.codeproject.com/Articles/1116455/Simple-Application-For-Creating-Serial-Number-Base
    //https://www.dreamincode.net/forums/topic/176014-serial-generator-and-validator/

    // Fanuc Settings
    TextField txtFanucIP;
    TextField txtFanucPort;

    // Haas Settings
    TextField txtHaasIP;
    TextField txtHaasPort;
    TextField txtHaasAddrMonitor;
    TextField txtHaasAddrTool;
    TextField txtHaasAddrSection;
    TextField txtHaasAddrChannel;
    TextField txtHaasAddrProgNum;

    // PLC Settings
    TextField txtPLCIP;
    TextField txtPLCPort;

    TextField txtScaleHPIP;

    TextField txtReadCycle;

    TextField txtSensor1Addr;
    TextField txtSensor2Addr;
    TextField txtSensor3Addr;

    Button btnCancel;
    Button btnSave;

    ComboBox controlComboBox;

    String[] controlTypes = new String[] {"Fanuc 18i", "Fanuc 0i", "Fanuc 31i", "Fanuc i", "Okuma P300", "HAAS"};
    String[] monitorSignalOptionsForCommon = new String[] {"Fanuc", "Manual", "PLC"};
    String[] monitorSignalOptionsForHAAS = new String[] {"Machine"};

    ComboBox monitorSignalComboBox;

    BorderPane pane;
    VBox vBoxDeviceSetting;

    public DeviceSettingView() {
        initView();
    }

    private void initView() {
        pane = new BorderPane();
        pane.setPrefSize(503.0, 377);
        pane.setPadding(new Insets(30));

        vBoxDeviceSetting = new VBox(4);
        vBoxDeviceSetting.setPadding(new Insets(20.0, 50.0, 20.0, 50.0));

        Font titleFont = Font.font("Tahoma", FontWeight.BOLD, 15);

        Label labelFanucSettings = new Label("Fanuc Bridge Settings");
        labelFanucSettings.setFont(titleFont);
        labelFanucSettings.setTextFill(Color.WHITE);

        Font labelFont = Font.font(14);
        int heightInputField = 25;

        // Control Type
        HBox hBoxControlType = new HBox(5);
        hBoxControlType.setAlignment(Pos.CENTER);
        Label labelControlType = new Label("Control Type:");
        //labelSiteCode.getStyleClass().add("label-info-value");
        labelControlType.setTextFill(Color.WHITE);
        labelControlType.setFont(labelFont);
        labelControlType.setPrefWidth(173);
        labelControlType.setPrefHeight(26);

        controlComboBox = new ComboBox();
        HBox.setHgrow(controlComboBox, Priority.ALWAYS);
        controlComboBox.getItems().addAll(controlTypes);
        controlComboBox.setValue("Control");
        controlComboBox.setPrefWidth(250);
        controlComboBox.setPrefHeight(heightInputField);
        controlComboBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                String val = String.valueOf(controlComboBox.getValue());
                switch (val) {
                    case "Fanuc 18i":
                        System.out.println("18i");
                        break;
                    case "Fanuc 0i":
                        System.out.println("oi");
                        break;
                    case "Fanuc 31i":
                        System.out.println("31i");
                        break;
                    case "Fanuc i":
                        System.out.println("i");
                        break;
                    case "Okuma P300":
                        System.out.println("p300");
                        break;
                    case "HAAS":
                        System.out.println("p300");
                        break;
                }

                monitorSignalComboBox.getItems().clear();
                monitorSignalComboBox.getItems().addAll(controlComboBox.getSelectionModel().getSelectedIndex() == 5 ?
                        monitorSignalOptionsForHAAS : monitorSignalOptionsForCommon);
                monitorSignalComboBox.getSelectionModel().select(0);
            }
        });
        int controlType = PreferenceManager.getControlType();
        controlComboBox.getSelectionModel().select(controlType);

        hBoxControlType.getChildren().addAll(labelControlType, controlComboBox);

        // Monitor Signal From
        HBox hBoxMonitorSignal = new HBox(5);
        hBoxMonitorSignal.setAlignment(Pos.CENTER);
        Label labelMonitorSignalFrom = new Label("Monitor Signal From:");
        //labelSiteCode.getStyleClass().add("label-info-value");
        labelMonitorSignalFrom.setTextFill(Color.WHITE);
        labelMonitorSignalFrom.setFont(labelFont);
        labelMonitorSignalFrom.setPrefWidth(173);
        labelMonitorSignalFrom.setPrefHeight(26);
        //labelServerIP.setMaxWidth(100);

        monitorSignalComboBox = new ComboBox();
        HBox.setHgrow(monitorSignalComboBox, Priority.ALWAYS);
        monitorSignalComboBox.getItems().addAll(controlType == PreferenceManager.CONTROL_TYPE_FANUC_HAAS  ? monitorSignalOptionsForHAAS : monitorSignalOptionsForCommon);
        monitorSignalComboBox.setValue("Fanuc");
        monitorSignalComboBox.setPrefHeight(heightInputField);
        monitorSignalComboBox.setPrefWidth(250);
        monitorSignalComboBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                String val = String.valueOf(controlComboBox.getValue());
            }
        });

        int signalMode = PreferenceManager.getMonitorSignalFrom();
        monitorSignalComboBox.getSelectionModel().select(signalMode);

        hBoxMonitorSignal.getChildren().addAll(labelMonitorSignalFrom, monitorSignalComboBox);

        // Fanuc Bridge IP
        HBox hBoxFanucIp = new HBox(5);
        hBoxFanucIp.setAlignment(Pos.CENTER);
        Label labelServerIP = new Label("Fanuc Bridge IP:");
        //labelSiteCode.getStyleClass().add("label-info-value");
        labelServerIP.setTextFill(Color.WHITE);
        labelServerIP.setFont(labelFont);
        labelServerIP.setPrefWidth(173);
        labelServerIP.setPrefHeight(26);
        //labelServerIP.setMaxWidth(100);

        txtFanucIP = new TextField();
        HBox.setHgrow(txtFanucIP, Priority.ALWAYS);
        txtFanucIP.setPrefHeight(heightInputField);
        txtFanucIP.getStyleClass().add("setting_inputs");
        hBoxFanucIp.getChildren().addAll(labelServerIP, txtFanucIP);

        // Fanuc Bridge Port
        HBox hBoxFanucPort = new HBox(5);
        hBoxFanucPort.setAlignment(Pos.CENTER);
        Label labelDevPort = new Label("Fanuc Bridge Port:");
        //labelSiteKey.getStyleClass().add("label-info-value");
        labelDevPort.setTextFill(Color.WHITE);
        labelDevPort.setPrefWidth(173);
        labelDevPort.setPrefHeight(26);
        labelDevPort.setFont(labelFont);

        txtFanucPort = new TextField();
        HBox.setHgrow(txtFanucPort, Priority.ALWAYS);
        txtFanucPort.setPrefHeight(heightInputField);
        txtFanucPort.getStyleClass().add("setting_inputs");

        hBoxFanucPort.getChildren().addAll(labelDevPort, txtFanucPort);

        HBox hBoxFanucPortComment = new HBox(5);
        hBoxFanucPortComment.setAlignment(Pos.CENTER);
        Label labelBlank = new Label("");
        //labelSiteKey.getStyleClass().add("label-info-value");
        labelBlank.setTextFill(Color.WHITE);
        labelBlank.setPrefWidth(173);
        labelBlank.setPrefHeight(26);
        labelBlank.setFont(labelFont);

        Label labelDevPortComment = new Label("(Port 502 = UI App, 602 = Service)");
        labelDevPortComment.setTextFill(Color.WHITE);
        HBox.setHgrow(labelDevPortComment, Priority.ALWAYS);
        labelDevPortComment.setPrefHeight(26);
        labelDevPortComment.setFont(labelFont);
        hBoxFanucPortComment.getChildren().addAll(labelBlank, labelDevPortComment);

        // HAAS Settings
        Label labelHAASSettings = new Label("HAAS Settings");
        labelHAASSettings.setFont(titleFont);
        labelHAASSettings.setTextFill(Color.WHITE);

        // HAAS IP
        HBox hBoxHaasIp = new HBox(5);
        hBoxHaasIp.setAlignment(Pos.CENTER);
        Label labelHaasIP = new Label("HAAS IP:");
        //labelSiteCode.getStyleClass().add("label-info-value");
        labelHaasIP.setTextFill(Color.WHITE);
        labelHaasIP.setFont(labelFont);

        labelHaasIP.setPrefWidth(173);
        labelHaasIP.setPrefHeight(26);

        txtHaasIP = new TextField();
        HBox.setHgrow(txtHaasIP, Priority.ALWAYS);
        txtHaasIP.setPrefHeight(heightInputField);
        txtHaasIP.getStyleClass().add("setting_inputs");
        hBoxHaasIp.getChildren().addAll(labelHaasIP, txtHaasIP);

        // HAAS Port
        HBox hBoxHaasPort = new HBox(5);
        hBoxHaasPort.setAlignment(Pos.CENTER);
        Label labelHaasPort = new Label("HAAS Port:");
        //labelSiteKey.getStyleClass().add("label-info-value");
        labelHaasPort.setTextFill(Color.WHITE);
        labelHaasPort.setFont(labelFont);
        labelHaasPort.setPrefWidth(173);
        labelHaasPort.setPrefHeight(26);
        // labelPLCPort.setMaxWidth(100);

        txtHaasPort = new TextField();
        HBox.setHgrow(txtHaasPort, Priority.ALWAYS);
        txtHaasPort.setPrefHeight(heightInputField);
        txtHaasPort.getStyleClass().add("setting_inputs");
        hBoxHaasPort.getChildren().addAll(labelHaasPort, txtHaasPort);

        // HAAS Monitor On/Off
        HBox hBoxHaasMonitor = new HBox(5);
        hBoxHaasMonitor.setAlignment(Pos.CENTER);
        Label labelHaasMonitor = new Label("Monitor On/Off:");
        //labelSiteKey.getStyleClass().add("label-info-value");
        labelHaasMonitor.setTextFill(Color.WHITE);
        labelHaasMonitor.setFont(labelFont);

        labelHaasMonitor.setPrefWidth(173);
        labelHaasMonitor.setPrefHeight(26);
        // labelPLCPort.setMaxWidth(100);

        txtHaasAddrMonitor = new TextField();
        HBox.setHgrow(txtHaasAddrMonitor, Priority.ALWAYS);
        txtHaasAddrMonitor.setPrefHeight(heightInputField);
        txtHaasAddrMonitor.getStyleClass().add("setting_inputs");
        hBoxHaasMonitor.getChildren().addAll(labelHaasMonitor, txtHaasAddrMonitor);

        // HAAS Tool
        HBox hBoxHaasTool = new HBox(5);
        hBoxHaasTool.setAlignment(Pos.CENTER);
        Label labelHaasTool = new Label("Tool:");
        //labelSiteKey.getStyleClass().add("label-info-value");
        labelHaasTool.setTextFill(Color.WHITE);
        labelHaasTool.setFont(labelFont);

        labelHaasTool.setPrefWidth(173);
        labelHaasTool.setPrefHeight(26);
        // labelPLCPort.setMaxWidth(100);

        txtHaasAddrTool = new TextField();
        HBox.setHgrow(txtHaasAddrTool, Priority.ALWAYS);
        txtHaasAddrTool.setPrefHeight(heightInputField);
        txtHaasAddrTool.getStyleClass().add("setting_inputs");
        hBoxHaasTool.getChildren().addAll(labelHaasTool, txtHaasAddrTool);

        // HAAS Section
        HBox hBoxHaasSection = new HBox(5);
        hBoxHaasSection.setAlignment(Pos.CENTER);
        Label labelHaasSection = new Label("Section:");
        //labelSiteKey.getStyleClass().add("label-info-value");
        labelHaasSection.setTextFill(Color.WHITE);
        labelHaasSection.setFont(labelFont);

        labelHaasSection.setPrefWidth(173);
        labelHaasSection.setPrefHeight(26);
        // labelPLCPort.setMaxWidth(100);

        txtHaasAddrSection = new TextField();
        HBox.setHgrow(txtHaasAddrSection, Priority.ALWAYS);
        txtHaasAddrSection.setPrefHeight(heightInputField);
        txtHaasAddrSection.getStyleClass().add("setting_inputs");
        hBoxHaasSection.getChildren().addAll(labelHaasSection, txtHaasAddrSection);

        // HAAS Channel
        HBox hBoxHaasChannel = new HBox(5);
        hBoxHaasChannel.setAlignment(Pos.CENTER);
        Label labelHaasChannel = new Label("Channel:");
        //labelSiteKey.getStyleClass().add("label-info-value");
        labelHaasChannel.setTextFill(Color.WHITE);
        labelHaasChannel.setFont(labelFont);

        labelHaasChannel.setPrefWidth(173);
        labelHaasChannel.setPrefHeight(26);
        // labelPLCPort.setMaxWidth(100);

        txtHaasAddrChannel = new TextField();
        HBox.setHgrow(txtHaasAddrChannel, Priority.ALWAYS);
        txtHaasAddrChannel.setPrefHeight(heightInputField);
        txtHaasAddrChannel.getStyleClass().add("setting_inputs");
        hBoxHaasChannel.getChildren().addAll(labelHaasChannel, txtHaasAddrChannel);

        // HAAS Channel
        HBox hBoxHaasProgN = new HBox(5);
        hBoxHaasProgN.setAlignment(Pos.CENTER);
        Label labelHaasProgN = new Label("Program Number:");
        //labelSiteKey.getStyleClass().add("label-info-value");
        labelHaasProgN.setTextFill(Color.WHITE);
        labelHaasProgN.setFont(labelFont);

        labelHaasProgN.setPrefWidth(173);
        labelHaasProgN.setPrefHeight(26);
        // labelPLCPort.setMaxWidth(100);

        txtHaasAddrProgNum = new TextField();
        HBox.setHgrow(txtHaasAddrProgNum, Priority.ALWAYS);
        txtHaasAddrProgNum.setPrefHeight(heightInputField);
        txtHaasAddrProgNum.getStyleClass().add("setting_inputs");
        hBoxHaasProgN.getChildren().addAll(labelHaasProgN, txtHaasAddrProgNum);

        // PLC Settings
        Label labelPLCSettings = new Label("PLC Settings");
        labelPLCSettings.setFont(titleFont);
        labelPLCSettings.setTextFill(Color.WHITE);

        // PLC IP
        HBox hBoxPLCIp = new HBox(5);
        hBoxPLCIp.setAlignment(Pos.CENTER);
        Label labelPLCIP = new Label("PLC IP:");
        //labelSiteCode.getStyleClass().add("label-info-value");
        labelPLCIP.setTextFill(Color.WHITE);
        labelPLCIP.setFont(labelFont);

        labelPLCIP.setPrefWidth(173);
        labelPLCIP.setPrefHeight(26);

        txtPLCIP = new TextField();
        HBox.setHgrow(txtPLCIP, Priority.ALWAYS);
        txtPLCIP.setPrefHeight(heightInputField);
        txtPLCIP.getStyleClass().add("setting_inputs");
        hBoxPLCIp.getChildren().addAll(labelPLCIP, txtPLCIP);

        // PLC Port
        HBox hBoxPLCPort = new HBox(5);
        hBoxPLCPort.setAlignment(Pos.CENTER);
        Label labelPLCPort = new Label("PLC Port:");
        //labelSiteKey.getStyleClass().add("label-info-value");
        labelPLCPort.setTextFill(Color.WHITE);
        labelPLCPort.setFont(labelFont);

        labelPLCPort.setPrefWidth(173);
        labelPLCPort.setPrefHeight(26);
        // labelPLCPort.setMaxWidth(100);

        txtPLCPort = new TextField();
        HBox.setHgrow(txtPLCPort, Priority.ALWAYS);
        txtPLCPort.setPrefHeight(heightInputField);
        txtPLCPort.getStyleClass().add("setting_inputs");
        hBoxPLCPort.getChildren().addAll(labelPLCPort, txtPLCPort);

        // Sensor Scale Domain
        HBox hBoxSensorScaleDomain = new HBox(5);
        hBoxSensorScaleDomain.setAlignment(Pos.CENTER);
        Label labelScaleHPIP = new Label("Scale HP IP:");
        //labelSiteCode.getStyleClass().add("label-info-value");
        labelScaleHPIP.setTextFill(Color.WHITE);
        labelScaleHPIP.setFont(labelFont);
        labelScaleHPIP.setPrefWidth(173);
        labelScaleHPIP.setPrefHeight(26);

        txtScaleHPIP = new TextField();
        HBox.setHgrow(txtScaleHPIP, Priority.ALWAYS);
        txtScaleHPIP.setPrefHeight(heightInputField);
        txtScaleHPIP.getStyleClass().add("setting_inputs");
        hBoxSensorScaleDomain.getChildren().addAll(labelScaleHPIP, txtScaleHPIP);


        // PLC Settings
        Label labelTimeSettings = new Label("Time Settings");
        labelTimeSettings.setFont(titleFont);
        labelTimeSettings.setTextFill(Color.WHITE);

        HBox hBoxTime = new HBox(5);
        hBoxTime.setAlignment(Pos.CENTER);
        Label labelReadTime = new Label("Read Cycle(ms):");
        //labelSiteCode.getStyleClass().add("label-info-value");
        labelReadTime.setTextFill(Color.WHITE);
        labelReadTime.setFont(labelFont);
        labelReadTime.setPrefWidth(173);
        labelReadTime.setPrefHeight(26);

        txtReadCycle = new TextField();
        HBox.setHgrow(txtReadCycle, Priority.ALWAYS);
        txtReadCycle.setPrefHeight(heightInputField);
        txtReadCycle.getStyleClass().add("setting_inputs");
        hBoxTime.getChildren().addAll(labelReadTime, txtReadCycle);
        txtReadCycle.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    txtReadCycle.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        HBox hBoxSensor1 = new HBox(5);
        hBoxTime.setAlignment(Pos.CENTER);
        Label labelSensor1Addr = new Label("Sensor 1 Address:");
        //labelSiteCode.getStyleClass().add("label-info-value");
        labelSensor1Addr.setTextFill(Color.WHITE);
        labelSensor1Addr.setFont(labelFont);
        labelSensor1Addr.setPrefWidth(173);
        labelSensor1Addr.setPrefHeight(26);

        txtSensor1Addr = new TextField(String.valueOf(PreferenceManager.getSensorAddress1()));
        HBox.setHgrow(txtSensor1Addr, Priority.ALWAYS);
        txtSensor1Addr.setPrefHeight(heightInputField);
        txtSensor1Addr.getStyleClass().add("setting_inputs");
        hBoxSensor1.getChildren().addAll(labelSensor1Addr, txtSensor1Addr);
        txtSensor1Addr.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    txtSensor1Addr.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        HBox hBoxSensor2 = new HBox(5);
        hBoxTime.setAlignment(Pos.CENTER);
        Label labelSensor2Addr = new Label("Sensor 2 Address:");
        //labelSiteCode.getStyleClass().add("label-info-value");
        labelSensor2Addr.setTextFill(Color.WHITE);
        labelSensor2Addr.setFont(labelFont);
        labelSensor2Addr.setPrefWidth(173);
        labelSensor2Addr.setPrefHeight(26);

        txtSensor2Addr = new TextField(String.valueOf(PreferenceManager.getSensorAddress2()));
        HBox.setHgrow(txtSensor2Addr, Priority.ALWAYS);
        txtSensor2Addr.setPrefHeight(heightInputField);
        txtSensor2Addr.getStyleClass().add("setting_inputs");
        hBoxSensor2.getChildren().addAll(labelSensor2Addr, txtSensor2Addr);
        txtSensor2Addr.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    txtSensor2Addr.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        HBox hBoxSensor3 = new HBox(5);
        hBoxTime.setAlignment(Pos.CENTER);
        Label labelSensor3Addr = new Label("Sensor 3 Address:");
        //labelSiteCode.getStyleClass().add("label-info-value");
        labelSensor3Addr.setTextFill(Color.WHITE);
        labelSensor3Addr.setFont(labelFont);
        labelSensor3Addr.setPrefWidth(173);
        labelSensor3Addr.setPrefHeight(26);

        txtSensor3Addr = new TextField(String.valueOf(PreferenceManager.getSensorAddress3()));
        HBox.setHgrow(txtSensor3Addr, Priority.ALWAYS);
        txtSensor3Addr.setPrefHeight(heightInputField);
        txtSensor3Addr.getStyleClass().add("setting_inputs");
        hBoxSensor3.getChildren().addAll(labelSensor3Addr, txtSensor3Addr);
        txtSensor3Addr.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    txtSensor3Addr.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

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

        // Load App Settings
        loadAppSettings();

        // Deply on the UI
        vBoxDeviceSetting.getChildren().addAll(labelFanucSettings, hBoxControlType, hBoxMonitorSignal, hBoxFanucIp, hBoxFanucPort, hBoxFanucPortComment, new Separator(),
                labelHAASSettings, hBoxHaasIp, hBoxHaasPort, hBoxHaasMonitor, hBoxHaasTool, hBoxHaasSection, hBoxHaasChannel, hBoxHaasProgN, new Separator(),
                labelPLCSettings, hBoxPLCIp, hBoxPLCPort, new Separator(),
                hBoxSensorScaleDomain, new Separator(),
                labelTimeSettings, hBoxTime, new Separator(),
                hBoxSensor1, hBoxSensor2, hBoxSensor3,
                hbBtn);
        pane.setCenter(vBoxDeviceSetting);

        // Init Scene
        Scene scene = new Scene(pane, 400, 377);
        scene.getStylesheets().add(getClass().getClassLoader().getResource("resource/style/rootStyles.css").toExternalForm());

        screen_stage = new Stage(StageStyle.DECORATED);
        screen_stage.setTitle("Settings");

        screen_stage.setScene(scene);
        screen_stage.centerOnScreen();
        screen_stage.getIcons().add(LogoManager.getInstance().getLogo());
        //screen_stage.setAlwaysOnTop(true);
        screen_stage.setResizable(false);
        screen_stage.setMinWidth(400);
        screen_stage.setMinHeight(377);
    }

    public void show() {
        screen_stage.show();

        loadAppSettings();
    }

    public void loadAppSettings() {
        int controlType = PreferenceManager.getControlType();
        controlComboBox.getSelectionModel().select(controlType);

        int signalMode = PreferenceManager.getMonitorSignalFrom();
        monitorSignalComboBox.getSelectionModel().select(signalMode);

        // Fanuc IP and Port
        txtFanucIP.setText(PreferenceManager.getFanucIP());
        if (PreferenceManager.getFanucPort() == 0) {
            txtFanucPort.setText("");
        } else {
            txtFanucPort.setText(String.valueOf(PreferenceManager.getFanucPort()));
        }
        //--------------------------------------------------------------------------------------------------------------

        // HAAS IP and Port and Address Information
        txtHaasIP.setText(PreferenceManager.getHaasIP());
        if (PreferenceManager.getHaasPort() == 0) {
            txtHaasPort.setText("");
        } else {
            txtHaasPort.setText(String.valueOf(PreferenceManager.getHaasPort()));
        }

        if (PreferenceManager.getHaasAddrMonitor() == 0) {
            txtHaasAddrMonitor.setText("");
        } else {
            txtHaasAddrMonitor.setText(String.valueOf(PreferenceManager.getHaasAddrMonitor()));
        }

        if (PreferenceManager.getHaasAddrTool() == 0) {
            txtHaasAddrTool.setText("");
        } else {
            txtHaasAddrTool.setText(String.valueOf(PreferenceManager.getHaasAddrTool()));
        }

        if (PreferenceManager.getHaasAddrSection() == 0) {
            txtHaasAddrSection.setText("");
        } else {
            txtHaasAddrSection.setText(String.valueOf(PreferenceManager.getHaasAddrSection()));
        }

        if (PreferenceManager.getHaasAddrChannel() == 0) {
            txtHaasAddrChannel.setText("");
        } else {
            txtHaasAddrChannel.setText(String.valueOf(PreferenceManager.getHaasAddrChannel()));
        }

        if (PreferenceManager.getHaasAddrProgN() == 0) {
            txtHaasAddrProgNum.setText("");
        } else {
            txtHaasAddrProgNum.setText(String.valueOf(PreferenceManager.getHaasAddrProgN()));
        }
        // -------------------------------------------------------------------------------------------------------------

        // PLC IP and Port
        txtPLCIP.setText(PreferenceManager.getPLCIP());
        if (PreferenceManager.getPLCPort() == 0) {
            txtPLCPort.setText("");
        } else {
            txtPLCPort.setText(String.valueOf(PreferenceManager.getPLCPort()));
        }
        //--------------------------------------------------------------------------------------------------------------

        // Scale HP IP
        txtScaleHPIP.setText(PreferenceManager.getScaleHPIP());

        // Time Settings.
        txtReadCycle.setText(String.valueOf(PreferenceManager.getDataCycle()));

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
                int controlType = controlComboBox.getSelectionModel().getSelectedIndex();
                PreferenceManager.setControlType(controlType);

                int signalMode = monitorSignalComboBox.getSelectionModel().getSelectedIndex();
                PreferenceManager.setMonitorSignalFrom(signalMode);

                // Save Fanuc Settings
                String strFanucIP = txtFanucIP.getText();
                String strFanucPort = txtFanucPort.getText();
                int fanucPort = parseIntValue(strFanucPort);
                if (controlType != 5) { // This means Fanuc Settings
                    if (TextUtils.isEmpty(strFanucIP) || TextUtils.isEmpty(strFanucPort)) {
                        Toast.message("Invalid Fanuc Bridge Info!");
                        return;
                    }

                    strFanucIP = strFanucIP.trim();

                    if (!Utils.isValidIP(strFanucIP) || !Utils.isValidPort(fanucPort)) {
                        Toast.message("Invalid Fanuc Bridge Info!");
                        return;
                    }
                }
                PreferenceManager.setFanucIP(strFanucIP);
                PreferenceManager.setFanucPort(fanucPort);

                // Save HAAS Settings
                String strHaasIP = txtHaasIP.getText();
                String strHaasPort = txtHaasPort.getText();
                int faasPort = parseIntValue(strHaasPort);

                int faasAddrMonitor = parseIntValue(txtHaasAddrMonitor.getText());
                int faasAddrTool = parseIntValue(txtHaasAddrTool.getText());
                int faasAddrSection = parseIntValue(txtHaasAddrSection.getText());
                int faasAddrChannel = parseIntValue(txtHaasAddrChannel.getText());
                int faasAddrProgN = parseIntValue(txtHaasAddrProgNum.getText());
                if (controlType == 5) { // This means HAAS Settings
                    if (!Utils.isValidIP(strHaasIP) || !Utils.isValidPort(faasPort)) {
                        Toast.message("Invalid Haas IP/Port Info!");
                        return;
                    }

                    if (faasAddrMonitor == 0 || faasAddrTool == 0 || faasAddrSection == 0 || faasAddrChannel == 0 || faasAddrProgN == 0) {
                        Toast.message("Invalid Haas Macro Address Info!");
                        return;
                    }

                    strHaasIP = strHaasIP.trim();
                }
                PreferenceManager.setHaasIP(strHaasIP);
                PreferenceManager.setHaasPort(faasPort);
                PreferenceManager.setHaasAddrMonitor(faasAddrMonitor);
                PreferenceManager.setHaasAddrTool(faasAddrTool);
                PreferenceManager.setHaasAddrSection(faasAddrSection);
                PreferenceManager.setHaasAddrChannel(faasAddrChannel);
                PreferenceManager.setHaasAddrProgN(faasAddrProgN);

                // Save PLC Settings
                if (txtPLCIP.getText() == null || txtPLCIP.getText().trim().isEmpty() || txtPLCPort.getText() == null || txtPLCPort.getText().trim().isEmpty()) {
                    Toast.message("Invalid PLC Info!");
                    return;
                }
                String strPLCIP = txtPLCIP.getText().trim();
                String strPLCPort = txtPLCPort.getText().trim();
                int plcPort = parseIntValue(strPLCPort);
                if (!Utils.isValidIP(strPLCIP) || !Utils.isValidPort(plcPort)) {
                    Toast.message("Invalid PLC Info!");
                    return;
                }

                // Save and Return to the main screen.
                PreferenceManager.setPLCIP(strPLCIP);
                PreferenceManager.setPLCPort(plcPort);

                String scaleHPIP = txtScaleHPIP.getText().trim();
                if (!TextUtils.isEmpty(scaleHPIP) && !Utils.isValidIP(strPLCIP)) {
                    Toast.message("Invalid Scale HP IP.");
                    return;
                }
                PreferenceManager.setScaleHPIP(scaleHPIP);


                int dataCycle = parseIntValue(txtReadCycle.getText().trim());
                PreferenceManager.setDataCycle(dataCycle);

                int sensor1Addr = parseIntValue(txtSensor1Addr.getText().trim());
                int sensor2Addr = parseIntValue(txtSensor2Addr.getText().trim());
                int sensor3Addr = parseIntValue(txtSensor3Addr.getText().trim());

                PreferenceManager.setSensorAddress1(sensor1Addr);
                PreferenceManager.setSensorAddress2(sensor2Addr);
                PreferenceManager.setSensorAddress3(sensor3Addr);

                Toast.message("Save Successful!");
                close();
            }
        }
    };

    private int parseIntValue(String value) {
        if (value == null && value.trim().isEmpty()) {
            return 0;
        }

        int ret = 0;
        try {
            ret = Integer.parseInt(value.trim());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }
}
