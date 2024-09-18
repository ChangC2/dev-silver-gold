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

import javax.swing.*;

public class AdaptiveSettingView {

    Stage screen_stage;

    private static AdaptiveSettingView instance;

    public static AdaptiveSettingView getInstance() {
        if (instance == null) {
            instance = new AdaptiveSettingView();
        }
        return instance;
    }

    //https://www.codeproject.com/Articles/1116455/Simple-Application-For-Creating-Serial-Number-Base
    //https://www.dreamincode.net/forums/topic/176014-serial-generator-and-validator/

    TextField txtPIDGain;
    TextField txtPIDReset;
    TextField txtPIDRate;


    Button btnCancel;
    Button btnSave;

    BorderPane pane;
    VBox vBoxAdaptiveSettings;

    public AdaptiveSettingView() {
        initView();
    }

    private void initView() {
        pane = new BorderPane();
        pane.setPrefSize(503.0, 377);
        pane.setPadding(new Insets(30));

        vBoxAdaptiveSettings = new VBox(30);
        vBoxAdaptiveSettings.setPadding(new Insets(30.0, 50.0, 30.0, 50.0));

        Label labelAdaptiveSettings = new Label("Adaptive Settings");
        labelAdaptiveSettings.setFont(Font.font("Tahoma", FontWeight.BOLD, 22));
        labelAdaptiveSettings.setTextFill(Color.WHITE);

        Font labelFont = Font.font(14);

        // PID Gain
        HBox hBoxPIDGain = new HBox(5);
        hBoxPIDGain.setAlignment(Pos.CENTER);
        Label labelPIDGain = new Label("PID Gain:");
        //labelSiteKey.getStyleClass().add("label-info-value");
        labelPIDGain.setTextFill(Color.WHITE);
        labelPIDGain.setPrefWidth(300);
        labelPIDGain.setPrefHeight(26);
        labelPIDGain.setFont(labelFont);


        txtPIDGain = new TextField(String.valueOf(PreferenceManager.getPIDGain()));
        HBox.setHgrow(txtPIDGain, Priority.ALWAYS);
        txtPIDGain.setPrefHeight(30);
        txtPIDGain.getStyleClass().add("setting_inputs");
        txtPIDGain.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*(\\.\\d*)?")) {
                    txtPIDGain.setText(oldValue);
                }
            }
        });
        hBoxPIDGain.getChildren().addAll(labelPIDGain, txtPIDGain);

        // PID Reset
        HBox hBoxPIDReset = new HBox(5);
        hBoxPIDReset.setAlignment(Pos.CENTER);
        Label labelPIDReset = new Label("PID Reset:");
        //labelSiteKey.getStyleClass().add("label-info-value");
        labelPIDReset.setTextFill(Color.WHITE);
        labelPIDReset.setPrefWidth(300);
        labelPIDReset.setPrefHeight(26);
        labelPIDReset.setFont(labelFont);


        txtPIDReset = new TextField(String.valueOf(PreferenceManager.getPIDReset()));
        HBox.setHgrow(txtPIDReset, Priority.ALWAYS);
        txtPIDReset.setPrefHeight(30);
        txtPIDReset.getStyleClass().add("setting_inputs");
        txtPIDReset.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*(\\.\\d*)?")) {
                    txtPIDReset.setText(oldValue);
                }
            }
        });
        hBoxPIDReset.getChildren().addAll(labelPIDReset, txtPIDReset);

        // PID Rate
        HBox hBoxPIDRate = new HBox(5);
        hBoxPIDRate.setAlignment(Pos.CENTER);
        Label labelPIDRate = new Label("PID Rate:");
        //labelSiteKey.getStyleClass().add("label-info-value");
        labelPIDRate.setTextFill(Color.WHITE);
        labelPIDRate.setPrefWidth(300);
        labelPIDRate.setPrefHeight(26);
        labelPIDRate.setFont(labelFont);

        txtPIDRate = new TextField(String.valueOf(PreferenceManager.getPIDRate()));
        HBox.setHgrow(txtPIDRate, Priority.ALWAYS);
        txtPIDRate.setPrefHeight(30);
        txtPIDRate.getStyleClass().add("setting_inputs");
        txtPIDRate.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*(\\.\\d*)?")) {
                    txtPIDRate.setText(oldValue);
                }
            }
        });
        hBoxPIDRate.getChildren().addAll(labelPIDRate, txtPIDRate);

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

        vBoxAdaptiveSettings.getChildren().addAll(labelAdaptiveSettings, hBoxPIDGain, hBoxPIDReset, hBoxPIDRate, hbBtn);

        pane.setCenter(vBoxAdaptiveSettings);

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

    EventHandler<ActionEvent> buttonHandler = new EventHandler<ActionEvent>() {

        @Override
        public void handle(final ActionEvent event) {

            if (event.getSource() == btnCancel) {
                JOptionPane.showMessageDialog(null, "Are you sure you wish to cancel? Changes will not be saved!");
            } else if (event.getSource() == btnSave) {
                float pidGain = Float.parseFloat(txtPIDGain.getText().trim());
                float pidReset = Float.parseFloat(txtPIDReset.getText().trim());
                float pidRate = Float.parseFloat(txtPIDRate.getText().trim());

                float maxPIDParam = Float.MAX_VALUE / 1000;

                if (pidGain > maxPIDParam || pidReset > maxPIDParam || pidRate > maxPIDParam) {
                    AlertManager.getInstance().showAlert("Invalid Param", "PID param value should be 0 ~ " + maxPIDParam + ".");
                    return;
                }

                PreferenceManager.setPIDGain(pidGain);
                PreferenceManager.setPIDReset(pidReset);
                PreferenceManager.setPIDRate(pidRate);

                Toast.message("Save Successful!");

                close();
            }
        }
    };
}
