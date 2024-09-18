package view;

import Utils.PreferenceManager;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
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

public class AutoMonitorSettingView {

    Stage screen_stage;

    private static AutoMonitorSettingView instance;

    public static AutoMonitorSettingView getInstance() {
        if (instance == null) {
            instance = new AutoMonitorSettingView();
        }
        return instance;
    }

    //https://www.codeproject.com/Articles/1116455/Simple-Application-For-Creating-Serial-Number-Base
    //https://www.dreamincode.net/forums/topic/176014-serial-generator-and-validator/
    CheckBox checkboxAM;
    TextField txtSpeedTh;
    TextField txtRetainTime;

    Button btnCancel;
    Button btnSave;

    BorderPane pane;
    VBox vBoxAutoMonitorSettings;
    public AutoMonitorSettingView() {
        initView();
    }

    private void initView() {
        pane = new BorderPane();
        pane.setPrefSize(503.0, 377);
        pane.setPadding(new Insets(30));

        vBoxAutoMonitorSettings = new VBox(30);
        vBoxAutoMonitorSettings.setPadding(new Insets(30.0,30.0,30.0,50.0));

        Font labelFont = Font.font(14);

        // System Info Title
        Label labelSystemInfo = new Label("Fanuc Auto Monitor Settings");
        labelSystemInfo.setFont(Font.font("Tahoma", FontWeight.BOLD, 16));
        labelSystemInfo.setTextFill(Color.WHITE);

        // Auto Monitor
        checkboxAM = new CheckBox("Auto Monitor");
        checkboxAM.setTextFill(Color.WHITE);
        checkboxAM.setFont(Font.font(14));
        checkboxAM.setPadding(new Insets(0.0, 0.0, 10.0, 0.0));
        checkboxAM.setSelected(PreferenceManager.isAutoMonitorStatus());

        // Speed Threshold
        HBox hBoxThreshold = new HBox(5);
        hBoxThreshold.setAlignment(Pos.CENTER);
        Label labelEmail = new Label("Speed Threshold:");
        //labelSiteKey.getStyleClass().add("label-info-value");
        labelEmail.setTextFill(Color.WHITE);
        labelEmail.setPrefWidth(200);
        labelEmail.setPrefHeight(26);
        labelEmail.setFont(labelFont);

        txtSpeedTh = new TextField(String.valueOf(PreferenceManager.getSpindleThreshold()));
        HBox.setHgrow(txtSpeedTh, Priority.ALWAYS);
        txtSpeedTh.setPrefHeight(30);
        txtSpeedTh.getStyleClass().add("setting_inputs");
        txtSpeedTh.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*(\\.\\d*)?")) {
                    txtSpeedTh.setText(oldValue);
                }
            }
        });
        hBoxThreshold.getChildren().addAll(labelEmail, txtSpeedTh);

        // Site Key
        HBox hBoxTime = new HBox(5);
        hBoxTime.setAlignment(Pos.CENTER);
        Label labelSiteKey = new Label("Time(ms):");
        //labelSiteKey.getStyleClass().add("label-info-value");
        labelSiteKey.setTextFill(Color.WHITE);
        labelSiteKey.setPrefWidth(200);
        labelSiteKey.setPrefHeight(26);
        labelSiteKey.setFont(labelFont);

        txtRetainTime = new TextField(String.valueOf(PreferenceManager.getSpindleTime()));
        HBox.setHgrow(txtRetainTime, Priority.ALWAYS);
        txtRetainTime.setPrefHeight(30);
        txtRetainTime.getStyleClass().add("setting_inputs");
        txtRetainTime.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    txtRetainTime.setText(oldValue);
                }
            }
        });
        hBoxTime.getChildren().addAll(labelSiteKey, txtRetainTime);

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

        vBoxAutoMonitorSettings.getChildren().addAll(
                labelSystemInfo, checkboxAM, hBoxThreshold, hBoxTime, hbBtn);

        pane.setCenter(vBoxAutoMonitorSettings);

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
    }

    public void close() {
        screen_stage.close();
    }

    EventHandler<ActionEvent> buttonHandler = new EventHandler<ActionEvent>(){

        @Override
        public void handle(final ActionEvent event) {

            if (event.getSource() == btnCancel) {
                close();
            } else if (event.getSource() == btnSave) {
                // Save Auto Monitor Settings
                PreferenceManager.setAutoMonitorStatus(checkboxAM.isSelected());
                PreferenceManager.setSpindleThreshold(Float.parseFloat(txtSpeedTh.getText().trim())); // Default Filter
                PreferenceManager.setSpindleTime(Integer.parseInt(txtRetainTime.getText().trim()));

                MainView.getInstance().feedbackAutoMonitorSettingsToFanuc();

                // Save and Return to the main screen.
                Toast.message("Save Successful!");
                close();
            }
        }
    };
}
