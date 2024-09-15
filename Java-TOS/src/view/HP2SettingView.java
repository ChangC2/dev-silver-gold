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
import org.apache.http.util.TextUtils;

public class HP2SettingView {

    Stage screen_stage;

    private static HP2SettingView instance;

    public static HP2SettingView getInstance() {
        if (instance == null) {
            instance = new HP2SettingView();
        }
        return instance;
    }

    //https://www.codeproject.com/Articles/1116455/Simple-Application-For-Creating-Serial-Number-Base
    //https://www.dreamincode.net/forums/topic/176014-serial-generator-and-validator/

    SwitchButton btnSwitch;

    TextField txtHP2IP;
    TextField txtHP2Port;
    TextField txtHP2RegAddr;
    TextField txtHP2X;

    Button btnSave;

    BorderPane pane;
    VBox vBoxHP2Settings;

    public HP2SettingView() {
        initView();
    }

    private void initView() {
        pane = new BorderPane();
        pane.setPrefSize(503.0, 377);
        pane.setPadding(new Insets(30));

        vBoxHP2Settings = new VBox(30);
        vBoxHP2Settings.setPadding(new Insets(30.0, 50.0, 30.0, 50.0));

        Label labelAdaptiveSettings = new Label("HP2 Settings");
        labelAdaptiveSettings.setFont(Font.font("Tahoma", FontWeight.BOLD, 22));
        labelAdaptiveSettings.setTextFill(Color.WHITE);

        Font labelFont = Font.font(14);

        // Simulator
        HBox hBoxSimulator = new HBox(5);
        hBoxSimulator.setAlignment(Pos.CENTER);
        Label labelUseHp2 = new Label("Enable HP2");
        //labelSiteCode.getStyleClass().add("label-info-value");
        labelUseHp2.setTextFill(Color.WHITE);
        labelUseHp2.setFont(labelFont);
        labelUseHp2.setPrefWidth(300);
        labelUseHp2.setPrefHeight(26);
        //labelServerIP.setMaxWidth(100);
        btnSwitch = new SwitchButton();
        hBoxSimulator.getChildren().addAll(labelUseHp2, btnSwitch);

        // IP
        HBox hBoxIP = new HBox(5);
        hBoxIP.setAlignment(Pos.CENTER);
        Label labelIP = new Label("Rpi IP:");
        //labelSiteKey.getStyleClass().add("label-info-value");
        labelIP.setTextFill(Color.WHITE);
        labelIP.setPrefWidth(300);
        labelIP.setPrefHeight(26);
        labelIP.setFont(labelFont);

        txtHP2IP = new TextField(PreferenceManager.getHP2IP());
        HBox.setHgrow(txtHP2IP, Priority.ALWAYS);
        txtHP2IP.setPrefHeight(30);
        txtHP2IP.getStyleClass().add("setting_inputs");
        hBoxIP.getChildren().addAll(labelIP, txtHP2IP);

        // PORT
        HBox hBoxPort = new HBox(5);
        hBoxPort.setAlignment(Pos.CENTER);
        Label labelPort = new Label("Rpi Port:");
        //labelSiteKey.getStyleClass().add("label-info-value");
        labelPort.setTextFill(Color.WHITE);
        labelPort.setPrefWidth(300);
        labelPort.setPrefHeight(26);
        labelPort.setFont(labelFont);

        txtHP2Port = new TextField(String.valueOf(PreferenceManager.getHP2Port()));
        HBox.setHgrow(txtHP2Port, Priority.ALWAYS);
        txtHP2Port.setPrefHeight(30);
        txtHP2Port.getStyleClass().add("setting_inputs");
        txtHP2Port.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    txtHP2Port.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        hBoxPort.getChildren().addAll(labelPort, txtHP2Port);

        // Address
        HBox hBoxModbusAddr = new HBox(5);
        hBoxModbusAddr.setAlignment(Pos.CENTER);
        Label labelModbusAddr = new Label("Modbus Address:");
        //labelSiteKey.getStyleClass().add("label-info-value");
        labelModbusAddr.setTextFill(Color.WHITE);
        labelModbusAddr.setPrefWidth(300);
        labelModbusAddr.setPrefHeight(26);
        labelModbusAddr.setFont(labelFont);

        txtHP2RegAddr = new TextField(String.valueOf(PreferenceManager.getHP2RegAddr()));
        HBox.setHgrow(txtHP2RegAddr, Priority.ALWAYS);
        txtHP2RegAddr.setPrefHeight(30);
        txtHP2RegAddr.getStyleClass().add("setting_inputs");
        txtHP2RegAddr.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    txtHP2RegAddr.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
        hBoxModbusAddr.getChildren().addAll(labelModbusAddr, txtHP2RegAddr);

        // X value for record csv file
        HBox hBoxH2X = new HBox(5);
        hBoxH2X.setAlignment(Pos.CENTER);
        Label labelH2X = new Label("X (When HP > X, Records csv.):");
        //labelSiteKey.getStyleClass().add("label-info-value");
        labelH2X.setTextFill(Color.WHITE);
        labelH2X.setPrefWidth(300);
        labelH2X.setPrefHeight(26);
        labelH2X.setFont(labelFont);

        txtHP2X = new TextField(String.valueOf(PreferenceManager.getHP2X()));
        HBox.setHgrow(txtHP2X, Priority.ALWAYS);
        txtHP2X.setPrefHeight(30);
        txtHP2X.getStyleClass().add("setting_inputs");
        txtHP2X.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*(\\.\\d*)?")) {
                    txtHP2X.setText(oldValue);
                }
            }
        });
        hBoxH2X.getChildren().addAll(labelH2X, txtHP2X);


        btnSave = new Button("   Save   ");
        btnSave.getStyleClass().add("button-gradient5");
        btnSave.setOnAction(buttonHandler);

        HBox hbBtn = new HBox(25);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().addAll(btnSave);

        loadAppSettings();

        vBoxHP2Settings.getChildren().addAll(labelAdaptiveSettings, hBoxSimulator, hBoxIP, hBoxPort, hBoxModbusAddr, /*hBoxH2X, */hbBtn);

        pane.setCenter(vBoxHP2Settings);

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

    public void close() {
        screen_stage.close();
    }

    private void loadAppSettings() {
        btnSwitch.setChecked(PreferenceManager.isHP2Enabled());
        txtHP2IP.setText(PreferenceManager.getHP2IP());
        txtHP2Port.setText(String.valueOf(PreferenceManager.getHP2Port()));
        txtHP2RegAddr.setText(String.valueOf(PreferenceManager.getHP2RegAddr()));
        txtHP2X.setText(String.valueOf(PreferenceManager.getHP2X()));
    }

    EventHandler<ActionEvent> buttonHandler = new EventHandler<ActionEvent>() {

        @Override
        public void handle(final ActionEvent event) {

            if (event.getSource() == btnSave) {
                PreferenceManager.setHP2Enabled(btnSwitch.isChecked());

                if (btnSwitch.isChecked()) {
                    // Save Settings
                    String strIP = txtHP2IP.getText();
                    String strPort = txtHP2Port.getText();
                    int hp2Port = parseIntValue(strPort);
                    if (TextUtils.isEmpty(strIP) || TextUtils.isEmpty(strPort)) {
                        Toast.message("Invalid Fanuc Bridge Info!");
                        return;
                    }

                    strIP = strIP.trim();

                    if (!Utils.isValidIP(strIP) || !Utils.isValidPort(hp2Port)) {
                        Toast.message("Invalid IP or port Info!");
                        return;
                    }

                    int hp2ModbusAddr = parseIntValue(txtHP2RegAddr.getText());
                    float hp2X = Float.parseFloat(txtHP2X.getText().trim());

                    PreferenceManager.setHP2IP(strIP);
                    PreferenceManager.setHP2Port(hp2Port);
                    PreferenceManager.setHP2RegAddr(hp2ModbusAddr);
                    PreferenceManager.setHP2X(hp2X);
                }

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
