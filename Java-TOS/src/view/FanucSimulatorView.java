package view;

import Utils.PreferenceManager;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
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

public class FanucSimulatorView {

    Stage screen_stage;

    private static FanucSimulatorView instance;

    public static FanucSimulatorView getInstance() {
        if (instance == null) {
            instance = new FanucSimulatorView();
        }
        return instance;
    }

    //https://www.codeproject.com/Articles/1116455/Simple-Application-For-Creating-Serial-Number-Base
    //https://www.dreamincode.net/forums/topic/176014-serial-generator-and-validator/

    TextField txtProgNum;
    TextField txtTool;
    TextField txtSection;
    TextField txtChannel;
    TextField txtOptimize;

    Button btnMacInterruptEnable;
    Button btnToolWearSignal;

    Button btnCancel;
    Button btnSave;

    BorderPane pane;
    VBox vBoxDeviceSetting;
    public FanucSimulatorView() {
        initView();
    }

    private void initView() {
        pane = new BorderPane();
        pane.setPrefSize(600, 550);
        pane.setPadding(new Insets(30));

        vBoxDeviceSetting = new VBox(20);
        vBoxDeviceSetting.setPadding(new Insets(30.0,50.0,30.0,50.0));

        Label labelFanucSettings = new Label("Fanuc Manual Setting");
        labelFanucSettings.setFont(Font.font("Tahoma", FontWeight.BOLD, 20));
        labelFanucSettings.setTextFill(Color.WHITE);

        Font labelFont = Font.font(14);

        // Fanuc Bridge IP
        HBox hBoxProgN = new HBox(5);
        hBoxProgN.setAlignment(Pos.CENTER);
        Label labelProgNum = new Label("Program Number:");
        //labelSiteCode.getStyleClass().add("label-info-value");
        labelProgNum.setTextFill(Color.WHITE);
        labelProgNum.setFont(labelFont);
        labelProgNum.setPrefWidth(173);
        labelProgNum.setPrefHeight(26);
        //labelServerIP.setMaxWidth(100);

        txtProgNum = new TextField();
        HBox.setHgrow(txtProgNum, Priority.ALWAYS);
        txtProgNum.setPrefHeight(30);
        txtProgNum.getStyleClass().add("setting_inputs");
        txtProgNum.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    txtProgNum.setText(oldValue);
                }
            }
        });
        hBoxProgN.getChildren().addAll(labelProgNum, txtProgNum);

        // Fanuc Bridge Port
        HBox hBoxTool = new HBox(5);
        hBoxTool.setAlignment(Pos.CENTER);
        Label labelTool = new Label("Tool:");
        //labelSiteKey.getStyleClass().add("label-info-value");
        labelTool.setTextFill(Color.WHITE);
        labelTool.setPrefWidth(173);
        labelTool.setPrefHeight(26);
        labelTool.setFont(labelFont);

        txtTool = new TextField();
        HBox.setHgrow(txtTool, Priority.ALWAYS);
        txtTool.setPrefHeight(30);
        txtTool.getStyleClass().add("setting_inputs");
        txtTool.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    txtTool.setText(oldValue);
                }
            }
        });
        hBoxTool.getChildren().addAll(labelTool, txtTool);

        // PLC IP
        HBox hBoxSection = new HBox(5);
        hBoxSection.setAlignment(Pos.CENTER);
        Label labelSection = new Label("Section:");
        //labelSiteCode.getStyleClass().add("label-info-value");
        labelSection.setTextFill(Color.WHITE);
        labelSection.setFont(labelFont);
        labelSection.setPrefWidth(173);
        labelSection.setPrefHeight(26);

        txtSection = new TextField();
        HBox.setHgrow(txtSection, Priority.ALWAYS);
        txtSection.setPrefHeight(30);
        txtSection.getStyleClass().add("setting_inputs");
        txtSection.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    txtSection.setText(oldValue);
                }
            }
        });
        hBoxSection.getChildren().addAll(labelSection, txtSection);

        // PLC Port
        HBox hBoxChannel = new HBox(5);
        hBoxChannel.setAlignment(Pos.CENTER);
        Label labelChannel = new Label("Channel:");
        //labelSiteKey.getStyleClass().add("label-info-value");
        labelChannel.setTextFill(Color.WHITE);
        labelChannel.setFont(labelFont);
        labelChannel.setPrefWidth(173);
        labelChannel.setPrefHeight(26);

        txtChannel = new TextField();
        HBox.setHgrow(txtChannel, Priority.ALWAYS);
        txtChannel.setPrefHeight(30);
        txtChannel.getStyleClass().add("setting_inputs");
        txtChannel.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    txtSection.setText(oldValue);
                }
            }
        });
        hBoxChannel.getChildren().addAll(labelChannel, txtChannel);

        HBox hBoxOptimize = new HBox(5);
        hBoxOptimize.setAlignment(Pos.CENTER);
        Label labelOptimize = new Label("Monitor(0/1):");
        //labelSiteCode.getStyleClass().add("label-info-value");
        labelOptimize.setTextFill(Color.WHITE);
        labelOptimize.setFont(labelFont);
        labelOptimize.setPrefWidth(173);
        labelOptimize.setPrefHeight(26);

        txtOptimize = new TextField();
        HBox.setHgrow(txtOptimize, Priority.ALWAYS);
        txtOptimize.setPrefHeight(30);
        txtOptimize.getStyleClass().add("setting_inputs");
        txtOptimize.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    txtOptimize.setText(oldValue);
                }
            }
        });
        hBoxOptimize.getChildren().addAll(labelOptimize, txtOptimize);
        txtOptimize.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    txtOptimize.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        // Macro Interrupt Enable
        HBox hBoxMacroInterruptEnable = new HBox(5);
        hBoxOptimize.setAlignment(Pos.CENTER);
        Label labelMacInterruptEnable = new Label("Macro Interrupt Enable");
        //labelSiteCode.getStyleClass().add("label-info-value");
        labelMacInterruptEnable.setTextFill(Color.WHITE);
        labelMacInterruptEnable.setFont(labelFont);
        labelMacInterruptEnable.setPrefWidth(300);
        labelMacInterruptEnable.setPrefHeight(26);

        btnMacInterruptEnable = new Button("OFF");
        btnMacInterruptEnable.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                boolean newMacInterruptEnabledStatus = false;
                if (btnMacInterruptEnable.getText().equalsIgnoreCase("OFF")) {
                    btnMacInterruptEnable.setText("ON");
                    newMacInterruptEnabledStatus = true;
                } else {
                    btnMacInterruptEnable.setText("OFF");
                }

                MainView.getInstance().feedbackMacroInterruptSignalToFanuc(newMacInterruptEnabledStatus);
            }
        });
        hBoxMacroInterruptEnable.getChildren().addAll(labelMacInterruptEnable, btnMacInterruptEnable);

        HBox hToolWearSignalBox = new HBox();
        hToolWearSignalBox.setAlignment(Pos.BASELINE_RIGHT);
        btnToolWearSignal = new Button("Send Tool_Wear_Signal High");
        btnToolWearSignal.setPrefHeight(35);
        btnToolWearSignal.setPrefWidth(250);
        btnToolWearSignal.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                MainView.getInstance().feedbackToolWearSignalToFanuc(true);
            }
        });
        hToolWearSignalBox.getChildren().add(btnToolWearSignal);

        // Buttons Action
        btnCancel = new Button("  Close  ");
        btnCancel.getStyleClass().add("button-gradient5");
        btnCancel.setOnAction(buttonHandler);

        btnSave = new Button("   Save   ");
        btnSave.getStyleClass().add("button-gradient5");
        btnSave.setOnAction(buttonHandler);

        HBox hbBtn = new HBox(25);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().addAll(btnCancel, btnSave);

        // SET PREF MANAGER VALs
        // Fanuc IP and Port
        txtProgNum.setText(String.valueOf(MainView.getInstance().getValueProgNum()));
        txtTool.setText(String.valueOf(MainView.getInstance().getValueTool()));
        txtSection.setText(String.valueOf(MainView.getInstance().getValueSection()));
        txtChannel.setText(String.valueOf(MainView.getInstance().getValueChannel()));
        txtOptimize.setText(MainView.getInstance().isMonitorOn() ? "1" : "0");

        vBoxDeviceSetting.getChildren().addAll(labelFanucSettings, hBoxProgN, hBoxTool, hBoxSection, hBoxChannel, hBoxOptimize,
                new Separator(),
                hToolWearSignalBox, hBoxMacroInterruptEnable, hbBtn);

        pane.setCenter(vBoxDeviceSetting);

        // Init Scene
        Scene scene = new Scene(pane, 600, 550);
        scene.getStylesheets().add(getClass().getClassLoader().getResource("resource/style/rootStyles.css").toExternalForm());

        screen_stage = new Stage(StageStyle.DECORATED);
        screen_stage.setTitle("Fanuc Simulator");

        screen_stage.setScene(scene);
        screen_stage.centerOnScreen();
        screen_stage.getIcons().add(LogoManager.getInstance().getLogo());
        //screen_stage.setAlwaysOnTop(true);
        screen_stage.setResizable(false);
        screen_stage.setMinWidth(600);
        screen_stage.setMinHeight(550);
    }

    public void show() {
        screen_stage.show();
        screen_stage.requestFocus();
        screen_stage.toFront();

        txtProgNum.setText(String.valueOf(MainView.getInstance().getValueProgNum()));
        txtTool.setText(String.valueOf(MainView.getInstance().getValueTool()));
        txtSection.setText(String.valueOf(MainView.getInstance().getValueSection()));
        txtChannel.setText(String.valueOf(MainView.getInstance().getValueChannel()));
        txtOptimize.setText(MainView.getInstance().isMonitorOn() ? "1" : "0");
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
                // Save Fanuc Settings
                if (txtProgNum.getText() == null || txtProgNum.getText().trim().isEmpty() ||
                        txtTool.getText() == null || txtTool.getText().trim().isEmpty() ||
                        txtSection.getText() == null || txtSection.getText().trim().isEmpty() ||
                        txtChannel.getText() == null || txtChannel.getText().trim().isEmpty() ||
                        txtOptimize.getText() == null || txtOptimize.getText().trim().isEmpty()) {
                    Toast.message("Please input values");
                    return;
                }

                int progNum = Integer.parseInt(txtProgNum.getText().trim());
                int tool = Integer.parseInt(txtTool.getText().trim());
                int section = Integer.parseInt(txtSection.getText().trim());
                int channel = Integer.parseInt(txtChannel.getText().trim());
                int optimize = Integer.parseInt(txtOptimize.getText().trim());

                MainView.getInstance().sendFanucSimulVals(progNum, tool, section, channel, optimize);
            }
        }
    };
}
