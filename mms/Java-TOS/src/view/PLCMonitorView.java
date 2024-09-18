package view;

import Utils.PreferenceManager;
import Utils.Utils;
import de.re.easymodbus.exceptions.ModbusException;
import de.re.easymodbus.modbusclient.ModbusClient;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class PLCMonitorView {

    Stage screen_stage;

    private static PLCMonitorView instance;

    public static PLCMonitorView getInstance() {
        if (instance == null) {
            instance = new PLCMonitorView();
        }
        return instance;
    }

    // PLC Settings
    TextField txtPLCIP;
    TextField txtPLCPort;
    TextField txtCoilAddress, txtCoilPLC, txtCoilValue, txtHRegAddress, txtHRegPLC, txtHRegValue;

    Button btnConnect, btnCoilRead, btnCoilWrite, btnHRegRead, btnHRegWrite;
    VBox vBoxDeviceSetting;

    ModbusClient modbusClientPLC;


    public PLCMonitorView() {
        initView();
    }

    private void initView() {
        Font labelFont = Font.font(14);
        Font titleFont = Font.font("Tahoma", FontWeight.BOLD, 15);
        int heightInputField = 25;

        vBoxDeviceSetting = new VBox(15);
        vBoxDeviceSetting.setPadding(new Insets(20.0, 50.0, 20.0, 50.0));
        vBoxDeviceSetting.setAlignment(Pos.CENTER);


        BorderPane pane = new BorderPane();
        pane.setPrefSize(800, 377);
        pane.setPadding(new Insets(30));

        HBox hBoxIpPort = new HBox(20);

        // PLC IP
        HBox hBoxPLCIp = new HBox(5);
        hBoxPLCIp.setAlignment(Pos.CENTER);
        Label labelPLCIP = new Label("IP Address:");
        //labelSiteCode.getStyleClass().add("label-info-value");
        labelPLCIP.setTextFill(Color.WHITE);
        labelPLCIP.setFont(labelFont);

//        labelPLCIP.setPrefWidth(100);
        labelPLCIP.setPrefHeight(35);

        txtPLCIP = new TextField();
        txtPLCIP.setPrefWidth(200);
        HBox.setHgrow(txtPLCIP, Priority.ALWAYS);
        txtPLCIP.setPrefHeight(heightInputField);
        txtPLCIP.getStyleClass().add("setting_inputs");
        hBoxPLCIp.getChildren().addAll(labelPLCIP, txtPLCIP);

        // PLC Port
        HBox hBoxPLCPort = new HBox(5);
        hBoxPLCPort.setAlignment(Pos.CENTER);
        Label labelPLCPort = new Label("Port:");
        //labelSiteKey.getStyleClass().add("label-info-value");
        labelPLCPort.setTextFill(Color.WHITE);
        labelPLCPort.setFont(labelFont);

//        labelPLCPort.setPrefWidth(200);
        labelPLCPort.setPrefHeight(35);
        // labelPLCPort.setMaxWidth(100);

        txtPLCPort = new TextField();
        txtPLCPort.setPrefWidth(200);
        HBox.setHgrow(txtPLCPort, Priority.ALWAYS);
        txtPLCPort.setPrefHeight(heightInputField);
        txtPLCPort.getStyleClass().add("setting_inputs");
        hBoxPLCPort.getChildren().addAll(labelPLCPort, txtPLCPort);

        btnConnect = new Button("   Connect   ");
        btnConnect.getStyleClass().add("button-gradient5");
        btnConnect.setOnAction(buttonHandler);

        hBoxIpPort.getChildren().addAll(hBoxPLCIp, hBoxPLCPort, btnConnect);

        //Coil
        HBox hBoxCoil = new HBox(20);
        Label labelCoil = new Label("Coil");
        labelCoil.setFont(titleFont);
        labelCoil.setTextFill(Color.WHITE);

        HBox hBoxCoilAddress = new HBox(5);
        hBoxCoilAddress.setAlignment(Pos.CENTER);
        Label labelCoilAddress = new Label("Address:");
        //labelSiteCode.getStyleClass().add("label-info-value");
        labelCoilAddress.setTextFill(Color.WHITE);
        labelCoilAddress.setFont(labelFont);

//        labelPLCIP.setPrefWidth(100);
        labelCoilAddress.setPrefHeight(35);

        txtCoilAddress = new TextField();
        txtCoilAddress.setPrefWidth(100);
        HBox.setHgrow(txtCoilAddress, Priority.ALWAYS);
        txtCoilAddress.setPrefHeight(heightInputField);
        txtCoilAddress.getStyleClass().add("setting_inputs");
        txtCoilAddress.setText("0");
        hBoxCoilAddress.getChildren().addAll(labelCoilAddress, txtCoilAddress);

        // PLC Coil
        HBox hBoxCoilPLC = new HBox(5);
        hBoxCoilPLC.setAlignment(Pos.CENTER);
        Label labelCoilPLC = new Label("Modbus:");
        labelCoilPLC.setTextFill(Color.WHITE);
        labelCoilPLC.setFont(labelFont);

//        labelPLCIP.setPrefWidth(100);
        labelCoilPLC.setPrefHeight(35);

        txtCoilPLC = new TextField();
        txtCoilPLC.setPrefWidth(100);
        HBox.setHgrow(txtCoilPLC, Priority.ALWAYS);
        txtCoilPLC.setPrefHeight(heightInputField);
        txtCoilPLC.getStyleClass().add("setting_inputs");
        hBoxCoilPLC.getChildren().addAll(labelCoilPLC, txtCoilPLC);


        // Value Coil
        HBox hBoxCoilValue = new HBox(5);
        hBoxCoilValue.setAlignment(Pos.CENTER);
        Label labelCoilValue = new Label("Value:");
        labelCoilValue.setTextFill(Color.WHITE);
        labelCoilValue.setFont(labelFont);

//        labelValueIP.setPrefWidth(100);
        labelCoilValue.setPrefHeight(35);

        txtCoilValue = new TextField();
        txtCoilValue.setPrefWidth(200);
        HBox.setHgrow(txtCoilValue, Priority.ALWAYS);
        txtCoilValue.setPrefHeight(heightInputField);
        txtCoilValue.getStyleClass().add("setting_inputs");
        hBoxCoilValue.getChildren().addAll(labelCoilValue, txtCoilValue);


        btnCoilRead = new Button("   Read   ");
        btnCoilRead.getStyleClass().add("button-gradient5");
        btnCoilRead.setOnAction(buttonHandler);

        btnCoilWrite = new Button("   Write   ");
        btnCoilWrite.getStyleClass().add("button-gradient5");
        btnCoilWrite.setOnAction(buttonHandler);

        // Deply on the UI
        hBoxCoil.getChildren().addAll(hBoxCoilAddress, hBoxCoilPLC, hBoxCoilValue, btnCoilRead, btnCoilWrite);


        // HReg
        HBox hBoxHReg = new HBox(20);
        Label labelHReg = new Label("HReg");
        labelHReg.setFont(titleFont);
        labelHReg.setTextFill(Color.WHITE);

        HBox hBoxHRegAddress = new HBox(5);
        hBoxHRegAddress.setAlignment(Pos.CENTER);
        Label labelHRegAddress = new Label("Address:");
        //labelSiteCode.getStyleClass().add("label-info-value");
        labelHRegAddress.setTextFill(Color.WHITE);
        labelHRegAddress.setFont(labelFont);

//        labelPLCIP.setPrefWidth(100);
        labelHRegAddress.setPrefHeight(35);

        txtHRegAddress = new TextField();
        txtHRegAddress.setText("0");
        txtHRegAddress.setPrefWidth(100);
        HBox.setHgrow(txtHRegAddress, Priority.ALWAYS);
        txtHRegAddress.setPrefHeight(heightInputField);
        txtHRegAddress.getStyleClass().add("setting_inputs");
        hBoxHRegAddress.getChildren().addAll(labelHRegAddress, txtHRegAddress);

        // PLC HReg
        HBox hBoxHRegPLC = new HBox(5);
        hBoxHRegPLC.setAlignment(Pos.CENTER);
        Label labelHRegPLC = new Label("Modbus:");
        labelHRegPLC.setTextFill(Color.WHITE);
        labelHRegPLC.setFont(labelFont);

//        labelPLCIP.setPrefWidth(100);
        labelHRegPLC.setPrefHeight(35);

        txtHRegPLC = new TextField();
        txtHRegPLC.setPrefWidth(100);
        HBox.setHgrow(txtHRegPLC, Priority.ALWAYS);
        txtHRegPLC.setPrefHeight(heightInputField);
        txtHRegPLC.getStyleClass().add("setting_inputs");
        hBoxHRegPLC.getChildren().addAll(labelHRegPLC, txtHRegPLC);


        // Value HReg
        HBox hBoxHRegValue = new HBox(5);
        hBoxHRegValue.setAlignment(Pos.CENTER);
        Label labelHRegValue = new Label("Value:");
        labelHRegValue.setTextFill(Color.WHITE);
        labelHRegValue.setFont(labelFont);

//        labelValueIP.setPrefWidth(100);
        labelHRegValue.setPrefHeight(35);

        txtHRegValue = new TextField();
        txtHRegValue.setPrefWidth(200);
        HBox.setHgrow(txtHRegValue, Priority.ALWAYS);
        txtHRegValue.setPrefHeight(heightInputField);
        txtHRegValue.getStyleClass().add("setting_inputs");
        hBoxHRegValue.getChildren().addAll(labelHRegValue, txtHRegValue);


        btnHRegRead = new Button("   Read   ");
        btnHRegRead.getStyleClass().add("button-gradient5");
        btnHRegRead.setOnAction(buttonHandler);

        btnHRegWrite = new Button("   Write   ");
        btnHRegWrite.getStyleClass().add("button-gradient5");
        btnHRegWrite.setOnAction(buttonHandler);

        // Deply on the UI
        hBoxHReg.getChildren().addAll(hBoxHRegAddress, hBoxHRegPLC, hBoxHRegValue, btnHRegRead, btnHRegWrite);

        loadAppSettings();

        vBoxDeviceSetting.getChildren().addAll(hBoxIpPort,  new Separator(), labelCoil, hBoxCoil,  new Separator(), labelHReg, hBoxHReg);
        pane.setCenter(vBoxDeviceSetting);

        // Init Scene
        Scene scene = new Scene(pane, 800, 377);
        scene.getStylesheets().add(getClass().getClassLoader().getResource("resource/style/rootStyles.css").toExternalForm());

        screen_stage = new Stage(StageStyle.DECORATED);
        screen_stage.setTitle("Process Monitor");

        screen_stage.setScene(scene);
        screen_stage.centerOnScreen();
        screen_stage.getIcons().add(LogoManager.getInstance().getLogo());
        //screen_stage.setAlwaysOnTop(true);
        screen_stage.setResizable(false);
        screen_stage.setMinWidth(800);
        screen_stage.setMinHeight(377);
        screen_stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                disconnectPLC();
            }
        });
    }

    public void show() {
        screen_stage.show();
        loadAppSettings();
    }

    public void loadAppSettings() {
        // PLC IP and Port
        txtPLCIP.setText(PreferenceManager.getPLCIP1());
        if (PreferenceManager.getPLCPort1() == 0) {
            txtPLCPort.setText("");
        } else {
            txtPLCPort.setText(String.valueOf(PreferenceManager.getPLCPort1()));
        }
    }

    public void close() {
        screen_stage.close();
    }

    EventHandler<ActionEvent> buttonHandler = new EventHandler<ActionEvent>() {
        @Override
        public void handle(final ActionEvent event) {
            if (event.getSource() == btnConnect) {
                if (modbusClientPLC != null && modbusClientPLC.isConnected()) {
                    disconnectPLC();
                    btnConnect.setText("Connect");
                }else{
                    connectPLC();
                    if (modbusClientPLC != null && modbusClientPLC.isConnected()){
                        btnConnect.setText("Disconnect");
                    }
                }
            }else if (event.getSource() == btnCoilRead){
                readCoil();
            }else if (event.getSource() == btnHRegRead){
                readHReg();
            }else if (event.getSource() == btnCoilWrite){
                writeCoil();
            }else if (event.getSource() == btnHRegWrite){
                writeHReg();
            }
        }
    };

    private void connectPLC() {
        String strPLCIP = txtPLCIP.getText().trim();
        String strPLCPort = txtPLCPort.getText().trim();
        int plcPort = parseIntValue(strPLCPort);
        if (!Utils.isValidIP(strPLCIP) || !Utils.isValidPort(plcPort)) {
            Toast.message("Invalid PLC Info!");
            return;
        }

        if (Utils.isValidIP(strPLCIP) && Utils.isValidPort(plcPort)) {
            try {
                modbusClientPLC = new ModbusClient(strPLCIP, plcPort);
                modbusClientPLC.Connect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (modbusClientPLC != null && !modbusClientPLC.isConnected()) {
            try {
                modbusClientPLC.Disconnect();
                modbusClientPLC = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        savePLCInfo();
    }

    private void savePLCInfo(){
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
        PreferenceManager.setPLCIP1(strPLCIP);
        PreferenceManager.setPLCPort1(plcPort);
    }

    private void disconnectPLC() {
        try {
            if (modbusClientPLC != null) {
                modbusClientPLC.Disconnect();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readCoil(){
        boolean[] coilPLCRegs = new boolean[0];
        try {
            String strAddress = txtCoilAddress.getText().trim();
            int address = parseIntValue(strAddress);
            coilPLCRegs = modbusClientPLC.ReadCoils(address, 1);
        } catch (ModbusException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        txtCoilPLC.setText(coilPLCRegs[0] ? "True" : "False");
    }

    private void writeCoil(){
        try {
            String strAddress = txtCoilAddress.getText().trim();
            int address = parseIntValue(strAddress);
            String strValue = txtCoilValue.getText().trim();
            int value = parseIntValue(strValue);
            modbusClientPLC.WriteSingleCoil(address, value == 0 ? false : true);
            readCoil();
        } catch (ModbusException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readHReg(){
        try {
            String strAddress = txtHRegAddress.getText().trim();
            int address = parseIntValue(strAddress);
            int[] holdingPLCRegs = modbusClientPLC.ReadHoldingRegisters(address, 1);
            txtHRegPLC.setText(String.valueOf(holdingPLCRegs[0]));
        } catch (ModbusException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeHReg(){
        try {
            String strAddress = txtHRegAddress.getText().trim();
            int address = parseIntValue(strAddress);
            String strValue = txtHRegValue.getText().trim();
            int value = parseIntValue(strValue);
            modbusClientPLC.WriteSingleRegister(address, value);
            readHReg();
        } catch (ModbusException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
