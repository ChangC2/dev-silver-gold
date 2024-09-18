package view;

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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class TCSManualInputView {

    Stage screen_stage;

    private static TCSManualInputView instance;

    public static TCSManualInputView getInstance() {
        if (instance == null) {
            instance = new TCSManualInputView();
        }
        return instance;
    }

    //https://www.codeproject.com/Articles/1116455/Simple-Application-For-Creating-Serial-Number-Base
    //https://www.dreamincode.net/forums/topic/176014-serial-generator-and-validator/


    TextField txtTool;
    TextField txtSection;
    TextField txtChannel;

    Button btnCancel;
    Button btnSet;

    BorderPane pane;
    VBox vBoxDeviceSetting;
    public TCSManualInputView() {
        initView();
    }

    private void initView() {
        pane = new BorderPane();
        pane.setPrefSize(600, 300);
        pane.setPadding(new Insets(20));

        vBoxDeviceSetting = new VBox(20);
        vBoxDeviceSetting.setPadding(new Insets(30.0,50.0,30.0,50.0));

        Label labelFanucSettings = new Label("Manual Inputs");
        labelFanucSettings.setFont(Font.font("Tahoma", FontWeight.BOLD, 20));
        labelFanucSettings.setTextFill(Color.WHITE);

        Font labelFont = Font.font(14);

        // Tool
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

        // Section
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

        // Channel
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

        // Buttons Action
        btnCancel = new Button("  Close  ");
        btnCancel.getStyleClass().add("button-gradient5");
        btnCancel.setOnAction(buttonHandler);

        btnSet = new Button("   Set   ");
        btnSet.getStyleClass().add("button-gradient5");
        btnSet.setOnAction(buttonHandler);

        HBox hbBtn = new HBox(25);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().addAll(btnCancel, btnSet);

        // SET PREF MANAGER VALs
        // Fanuc IP and Port
        txtTool.setText(String.valueOf(MainView.getInstance().getValueTool()));
        txtSection.setText(String.valueOf(MainView.getInstance().getValueSection()));
        txtChannel.setText(String.valueOf(MainView.getInstance().getValueChannel()));

        vBoxDeviceSetting.getChildren().addAll(labelFanucSettings, hBoxTool, hBoxSection, hBoxChannel,
                new Separator(), hbBtn);

        pane.setCenter(vBoxDeviceSetting);

        // Init Scene
        Scene scene = new Scene(pane, 600, 300);
        scene.getStylesheets().add(getClass().getClassLoader().getResource("resource/style/rootStyles.css").toExternalForm());

        screen_stage = new Stage(StageStyle.DECORATED);
        screen_stage.setTitle("Input TCS Values");

        screen_stage.setScene(scene);
        screen_stage.centerOnScreen();
        screen_stage.getIcons().add(LogoManager.getInstance().getLogo());
        //screen_stage.setAlwaysOnTop(true);
        screen_stage.setResizable(false);
        screen_stage.setMinWidth(600);
        screen_stage.setMinHeight(300);
    }

    public void show() {
        screen_stage.show();
        screen_stage.requestFocus();
        screen_stage.toFront();

        txtTool.setText(String.valueOf(MainView.getInstance().getValueTool()));
        txtSection.setText(String.valueOf(MainView.getInstance().getValueSection()));
        txtChannel.setText(String.valueOf(MainView.getInstance().getValueChannel()));
    }

    public void close() {
        screen_stage.close();
    }

    EventHandler<ActionEvent> buttonHandler = new EventHandler<ActionEvent>(){

        @Override
        public void handle(final ActionEvent event) {

            if (event.getSource() == btnCancel) {
                close();
            } else if (event.getSource() == btnSet) {
                // Save Fanuc Settings
                if (txtTool.getText() == null || txtTool.getText().trim().isEmpty() ||
                        txtSection.getText() == null || txtSection.getText().trim().isEmpty() ||
                        txtChannel.getText() == null || txtChannel.getText().trim().isEmpty()) {
                    Toast.message("Please input values");
                    return;
                }

                int tool = Integer.parseInt(txtTool.getText().trim());
                int section = Integer.parseInt(txtSection.getText().trim());
                int channel = Integer.parseInt(txtChannel.getText().trim());

                MainView.getInstance().setManualTCSVals(tool, section, channel);
            }
        }
    };
}
