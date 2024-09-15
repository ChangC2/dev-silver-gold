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

public class TeachModeSettingView {

    Stage screen_stage;

    private static TeachModeSettingView instance;

    public static TeachModeSettingView getInstance() {
        if (instance == null) {
            instance = new TeachModeSettingView();
        }
        return instance;
    }

    //https://www.codeproject.com/Articles/1116455/Simple-Application-For-Creating-Serial-Number-Base
    //https://www.dreamincode.net/forums/topic/176014-serial-generator-and-validator/

    TextField txtHLPercent;
    TextField txtWLPercent;
    TextField txtLLErrorPercent;
    TextField txtTLPercent;

    TextField txtDefaultHL;
    TextField txtDefaultWL;
    TextField txtDefaultStartDelay;

    TextField txtDefaultFilter;
    TextField txtScaleChart;

    TextField txtDefaultLeadInTrigger;
    TextField txtDefaultLeadInFeedrate;

    Button btnAdaptiveEnable;
    Button btnMacroInterruptEnable;
    Button btnLeadInFREnable;

    TextField txtAdaptiveMin;
    TextField txtAdaptiveMax;
    TextField txtAdaptiveHighLimit;
    TextField txtAdaptiveWearLimit;

    Button btnCancel;
    Button btnSave;


    BorderPane pane;
    VBox vBoxTeachModeSetting;

    public TeachModeSettingView() {
        initView();
    }

    private void initView() {
        pane = new BorderPane();
        pane.setPrefSize(503.0, 400);
        pane.setPadding(new Insets(30));

        vBoxTeachModeSetting = new VBox(5);
        vBoxTeachModeSetting.setPadding(new Insets(30.0, 50.0, 30.0, 50.0));

        Label labelTeachModeSettings = new Label("Teach Mode Settings");
        labelTeachModeSettings.setFont(Font.font("Tahoma", FontWeight.BOLD, 22));
        labelTeachModeSettings.setTextFill(Color.WHITE);

        Font labelFont = Font.font(14);

        // High Limit Percent
        HBox hBoxHLPercent = new HBox(5);
        hBoxHLPercent.setAlignment(Pos.CENTER);
        Label labelHLPercent = new Label("High Limit % Over Target:");
        //labelSiteCode.getStyleClass().add("label-info-value");
        labelHLPercent.setTextFill(Color.WHITE);
        labelHLPercent.setFont(labelFont);
        labelHLPercent.setPrefWidth(300);
        labelHLPercent.setPrefHeight(26);
        //labelServerIP.setMaxWidth(100);

        txtHLPercent = new TextField(String.valueOf(PreferenceManager.getHighLimitPercent()));
        HBox.setHgrow(txtHLPercent, Priority.ALWAYS);
        txtHLPercent.setPrefHeight(30);
        txtHLPercent.getStyleClass().add("setting_inputs");
        txtHLPercent.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*(\\.\\d*)?")) {
                    txtHLPercent.setText(oldValue);
                }
            }
        });
        hBoxHLPercent.getChildren().addAll(labelHLPercent, txtHLPercent);

        // Wear Limit Percent
        HBox hBoxWLPercent = new HBox(5);
        hBoxWLPercent.setAlignment(Pos.CENTER);
        Label labelWLPercent = new Label("Wear Limit % Over Target:");
        //labelSiteKey.getStyleClass().add("label-info-value");
        labelWLPercent.setTextFill(Color.WHITE);
        labelWLPercent.setPrefWidth(300);
        labelWLPercent.setPrefHeight(26);
        labelWLPercent.setFont(labelFont);

        txtWLPercent = new TextField(String.valueOf(PreferenceManager.getWearLimitPercent()));
        HBox.setHgrow(txtWLPercent, Priority.ALWAYS);
        txtWLPercent.setPrefHeight(30);
        txtWLPercent.getStyleClass().add("setting_inputs");
        txtWLPercent.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*(\\.\\d*)?")) {
                    txtWLPercent.setText(oldValue);
                }
            }
        });
        hBoxWLPercent.getChildren().addAll(labelWLPercent, txtWLPercent);

        // Low Limit Error Percent
        HBox hBoxLLErrorPercent = new HBox(5);
        hBoxLLErrorPercent.setAlignment(Pos.CENTER);
        Label labelLLErrorPercent = new Label("Low Limit Error %:");
        //labelSiteKey.getStyleClass().add("label-info-value");
        labelLLErrorPercent.setTextFill(Color.WHITE);
        labelLLErrorPercent.setPrefWidth(300);
        labelLLErrorPercent.setPrefHeight(26);
        labelLLErrorPercent.setFont(labelFont);

        txtLLErrorPercent = new TextField(String.valueOf(PreferenceManager.getLowLimitErrorPercent()));
        HBox.setHgrow(txtLLErrorPercent, Priority.ALWAYS);
        txtLLErrorPercent.setPrefHeight(30);
        txtLLErrorPercent.getStyleClass().add("setting_inputs");
        txtLLErrorPercent.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*(\\.\\d*)?")) {
                    txtLLErrorPercent.setText(oldValue);
                }
            }
        });
        hBoxLLErrorPercent.getChildren().addAll(labelLLErrorPercent, txtLLErrorPercent);

        // Target Limit Percent
        HBox hBoxTLPercent = new HBox(5);
        hBoxTLPercent.setAlignment(Pos.CENTER);
        Label labelTLPercent = new Label("Target Load % Below Peak:");
        //labelSiteKey.getStyleClass().add("label-info-value");
        labelTLPercent.setTextFill(Color.WHITE);
        labelTLPercent.setPrefWidth(300);
        labelTLPercent.setPrefHeight(26);
        labelTLPercent.setFont(labelFont);

        txtTLPercent = new TextField(String.valueOf(PreferenceManager.getTargetLoadPercent()));
        HBox.setHgrow(txtTLPercent, Priority.ALWAYS);
        txtTLPercent.setPrefHeight(30);
        txtTLPercent.getStyleClass().add("setting_inputs");
        txtTLPercent.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*(\\.\\d*)?")) {
                    txtTLPercent.setText(oldValue);
                }
            }
        });
        hBoxTLPercent.getChildren().addAll(labelTLPercent, txtTLPercent);

        // Default High Limit
        HBox hBoxDefaultHL = new HBox(5);
        hBoxDefaultHL.setAlignment(Pos.CENTER);
        Label labelDefaultHL = new Label("Default High Limit Delay:");
        //labelSiteKey.getStyleClass().add("label-info-value");
        labelDefaultHL.setTextFill(Color.WHITE);
        labelDefaultHL.setPrefWidth(300);
        labelDefaultHL.setPrefHeight(26);
        labelDefaultHL.setFont(labelFont);

        txtDefaultHL = new TextField(String.valueOf(PreferenceManager.getDefaultHighLimitDelay()));
        HBox.setHgrow(txtDefaultHL, Priority.ALWAYS);
        txtDefaultHL.setPrefHeight(30);
        txtDefaultHL.getStyleClass().add("setting_inputs");
        txtDefaultHL.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*(\\.\\d*)?")) {
                    txtDefaultHL.setText(oldValue);
                }
            }
        });
        hBoxDefaultHL.getChildren().addAll(labelDefaultHL, txtDefaultHL);

        // Default Wear Limit
        HBox hBoxDefaultWL = new HBox(5);
        hBoxDefaultWL.setAlignment(Pos.CENTER);
        Label labelDefaultWL = new Label("Default Wear Limit Delay:");
        //labelSiteKey.getStyleClass().add("label-info-value");
        labelDefaultWL.setTextFill(Color.WHITE);
        labelDefaultWL.setPrefWidth(300);
        labelDefaultWL.setPrefHeight(26);
        labelDefaultWL.setFont(labelFont);

        txtDefaultWL = new TextField(String.valueOf(PreferenceManager.getDefaultWearLimitDelay()));
        HBox.setHgrow(txtDefaultWL, Priority.ALWAYS);
        txtDefaultWL.setPrefHeight(30);
        txtDefaultWL.getStyleClass().add("setting_inputs");
        txtDefaultWL.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*(\\.\\d*)?")) {
                    txtDefaultWL.setText(oldValue);
                }
            }
        });
        hBoxDefaultWL.getChildren().addAll(labelDefaultWL, txtDefaultWL);

        // Default Start Selay
        HBox hBoxDefaultStartDelay = new HBox(5);
        hBoxDefaultWL.setAlignment(Pos.CENTER);
        Label labelDefaultStartDelay = new Label("Default Start Delay:");
        labelDefaultStartDelay.setTextFill(Color.WHITE);
        labelDefaultStartDelay.setPrefWidth(300);
        labelDefaultStartDelay.setPrefHeight(26);
        labelDefaultStartDelay.setFont(labelFont);

        txtDefaultStartDelay = new TextField(String.valueOf(PreferenceManager.getDefaultStartDelay()));
        HBox.setHgrow(txtDefaultWL, Priority.ALWAYS);
        txtDefaultStartDelay.setPrefHeight(30);
        txtDefaultStartDelay.getStyleClass().add("setting_inputs");
        txtDefaultStartDelay.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*(\\.\\d*)?")) {
                    txtDefaultStartDelay.setText(oldValue);
                }
            }
        });
        hBoxDefaultStartDelay.getChildren().addAll(labelDefaultStartDelay, txtDefaultStartDelay);

        // Default Filter
        HBox hBoxDefaultFilter = new HBox(5);
        hBoxDefaultFilter.setAlignment(Pos.CENTER);
        Label labelDefaultFilter = new Label("Default Filter:");
        //labelSiteKey.getStyleClass().add("label-info-value");
        labelDefaultFilter.setTextFill(Color.WHITE);
        labelDefaultFilter.setPrefWidth(300);
        labelDefaultFilter.setPrefHeight(26);
        labelDefaultFilter.setFont(labelFont);

        txtDefaultFilter = new TextField(String.valueOf(PreferenceManager.getDefaultFilter()));
        HBox.setHgrow(txtDefaultFilter, Priority.ALWAYS);
        txtDefaultFilter.setPrefHeight(30);
        txtDefaultFilter.getStyleClass().add("setting_inputs");
        txtDefaultFilter.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*(\\.\\d*)?")) {
                    txtDefaultFilter.setText(oldValue);
                }
            }
        });
        hBoxDefaultFilter.getChildren().addAll(labelDefaultFilter, txtDefaultFilter);

        // Scale Chart
        HBox hBoxScaleChart = new HBox(5);
        hBoxScaleChart.setAlignment(Pos.CENTER);
        Label labelScaleChart = new Label("Scale Chart Above High Limit:");
        //labelSiteKey.getStyleClass().add("label-info-value");
        labelScaleChart.setTextFill(Color.WHITE);
        labelScaleChart.setPrefWidth(300);
        labelScaleChart.setPrefHeight(26);
        labelScaleChart.setFont(labelFont);

        txtScaleChart = new TextField(String.valueOf(PreferenceManager.getScaleChartAboveHighLimit()));
        HBox.setHgrow(txtScaleChart, Priority.ALWAYS);
        txtScaleChart.setPrefHeight(30);
        txtScaleChart.getStyleClass().add("setting_inputs");
        txtScaleChart.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*(\\.\\d*)?")) {
                    txtScaleChart.setText(oldValue);
                }
            }
        });
        hBoxScaleChart.getChildren().addAll(labelScaleChart, txtScaleChart);

        // Default Lead-In Trigger
        HBox hBoxLeadInTrigger = new HBox(5);
        hBoxLeadInTrigger.setAlignment(Pos.CENTER);
        Label labelDefaultLeadInTrigger = new Label("Lead-In Target Over Idle:");
        //labelSiteKey.getStyleClass().add("label-info-value");
        labelDefaultLeadInTrigger.setTextFill(Color.WHITE);
        labelDefaultLeadInTrigger.setPrefWidth(300);
        labelDefaultLeadInTrigger.setPrefHeight(26);
        labelDefaultLeadInTrigger.setFont(labelFont);

        txtDefaultLeadInTrigger = new TextField(String.valueOf(PreferenceManager.getDefaultLeadInTrigger()));
        HBox.setHgrow(txtDefaultLeadInTrigger, Priority.ALWAYS);
        txtDefaultLeadInTrigger.setPrefHeight(30);
        txtDefaultLeadInTrigger.getStyleClass().add("setting_inputs");
        txtDefaultLeadInTrigger.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*(\\.\\d*)?")) {
                    txtDefaultLeadInTrigger.setText(oldValue);
                }
            }
        });
        hBoxLeadInTrigger.getChildren().addAll(labelDefaultLeadInTrigger, txtDefaultLeadInTrigger);

        // Default Lead-In Feedrate
        HBox hBoxLeadInFeedrate = new HBox(5);
        hBoxLeadInTrigger.setAlignment(Pos.CENTER);
        Label labelDefaultLeadInFeedrate = new Label("Default Lead-In Feedrate:");
        //labelSiteKey.getStyleClass().add("label-info-value");
        labelDefaultLeadInFeedrate.setTextFill(Color.WHITE);
        labelDefaultLeadInFeedrate.setPrefWidth(300);
        labelDefaultLeadInFeedrate.setPrefHeight(26);
        labelDefaultLeadInFeedrate.setFont(labelFont);

        txtDefaultLeadInFeedrate = new TextField(String.valueOf(PreferenceManager.getDefaultLeadInFeedrate()));
        HBox.setHgrow(txtDefaultLeadInFeedrate, Priority.ALWAYS);
        txtDefaultLeadInFeedrate.setPrefHeight(30);
        txtDefaultLeadInFeedrate.getStyleClass().add("setting_inputs");
        txtDefaultLeadInFeedrate.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*(\\.\\d*)?")) {
                    txtDefaultLeadInFeedrate.setText(oldValue);
                }
            }
        });
        hBoxLeadInFeedrate.getChildren().addAll(labelDefaultLeadInFeedrate, txtDefaultLeadInFeedrate);


        // Lead-In FR Enable
        HBox hBoxLeadInFREnable = new HBox(5);
        Label labelLeadInFREnable = new Label("Lead-In FR Enable");
        //labelSiteCode.getStyleClass().add("label-info-value");
        labelLeadInFREnable.setTextFill(Color.WHITE);
        labelLeadInFREnable.setFont(labelFont);
        labelLeadInFREnable.setPrefWidth(300);
        labelLeadInFREnable.setPrefHeight(26);
        //labelServerIP.setMaxWidth(100);

        btnLeadInFREnable = new Button(PreferenceManager.isLeadInFREnabled() ? "ON" : "OFF");
        btnLeadInFREnable.setOnAction(buttonHandler);

        hBoxLeadInFREnable.getChildren().addAll(labelLeadInFREnable, btnLeadInFREnable);

        // Macro Interrupt Enable
        HBox hBoxMacroInterruptEnable = new HBox(5);
        Label labelMacroInterruptEnable = new Label("Macro Interrupt Enable");
        //labelSiteCode.getStyleClass().add("label-info-value");
        labelMacroInterruptEnable.setTextFill(Color.WHITE);
        labelMacroInterruptEnable.setFont(labelFont);
        labelMacroInterruptEnable.setPrefWidth(300);
        labelMacroInterruptEnable.setPrefHeight(26);
        //labelServerIP.setMaxWidth(100);

        btnMacroInterruptEnable = new Button(PreferenceManager.isMacroInterruptEnabled() ? "ON" : "OFF");

        btnMacroInterruptEnable.setOnAction(buttonHandler);

        hBoxMacroInterruptEnable.getChildren().addAll(labelMacroInterruptEnable, btnMacroInterruptEnable);


        // Adaptive Enable
        HBox hBoxAdaptiveEnable = new HBox(5);
        Label labelAdaptiveEnable = new Label("Adaptive Enable");
        //labelSiteCode.getStyleClass().add("label-info-value");
        labelAdaptiveEnable.setTextFill(Color.WHITE);
        labelAdaptiveEnable.setFont(labelFont);
        labelAdaptiveEnable.setPrefWidth(300);
        labelAdaptiveEnable.setPrefHeight(26);
        //labelServerIP.setMaxWidth(100);

        btnAdaptiveEnable = new Button(PreferenceManager.isAdaptiveEnabled() ? "ON" : "OFF");
        btnAdaptiveEnable.setOnAction(buttonHandler);

        hBoxAdaptiveEnable.getChildren().addAll(labelAdaptiveEnable, btnAdaptiveEnable);

        // Adaptive Min
        HBox hBoxAdaptiveMin = new HBox(5);
        hBoxAdaptiveMin.setAlignment(Pos.CENTER);
        Label labelAdaptiveMin = new Label("Adaptive Min:");
        //labelSiteKey.getStyleClass().add("label-info-value");
        labelAdaptiveMin.setTextFill(Color.WHITE);
        labelAdaptiveMin.setPrefWidth(300);
        labelAdaptiveMin.setPrefHeight(26);
        labelAdaptiveMin.setFont(labelFont);

        txtAdaptiveMin = new TextField(String.valueOf(PreferenceManager.getAdaptiveMin()));
        HBox.setHgrow(txtAdaptiveMin, Priority.ALWAYS);
        txtAdaptiveMin.setPrefHeight(30);
        txtAdaptiveMin.getStyleClass().add("setting_inputs");
        txtAdaptiveMin.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    txtAdaptiveMin.setText(oldValue);
                }
            }
        });
        hBoxAdaptiveMin.getChildren().addAll(labelAdaptiveMin, txtAdaptiveMin);

        // Adaptive Max
        HBox hBoxAdaptiveMax = new HBox(5);
        hBoxAdaptiveMax.setAlignment(Pos.CENTER);
        Label labelAdaptiveMax = new Label("Adaptive Max:");
        //labelSiteKey.getStyleClass().add("label-info-value");
        labelAdaptiveMax.setTextFill(Color.WHITE);
        labelAdaptiveMax.setPrefWidth(300);
        labelAdaptiveMax.setPrefHeight(26);
        labelAdaptiveMax.setFont(labelFont);

        txtAdaptiveMax = new TextField(String.valueOf(PreferenceManager.getAdaptiveMax()));
        HBox.setHgrow(txtAdaptiveMax, Priority.ALWAYS);
        txtAdaptiveMax.setPrefHeight(30);
        txtAdaptiveMax.getStyleClass().add("setting_inputs");
        txtAdaptiveMax.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    txtAdaptiveMax.setText(oldValue);
                }
            }
        });
        hBoxAdaptiveMax.getChildren().addAll(labelAdaptiveMax, txtAdaptiveMax);


        // Adaptive High Limit
        HBox hBoxAdaptiveHighLimit = new HBox(5);
        Label labelAdaptiveHighLimit = new Label("Adaptive High Limit");
        //labelSiteCode.getStyleClass().add("label-info-value");
        labelAdaptiveHighLimit.setTextFill(Color.WHITE);
        labelAdaptiveHighLimit.setFont(labelFont);
        labelAdaptiveHighLimit.setPrefWidth(300);
        labelAdaptiveHighLimit.setPrefHeight(26);
        //labelServerIP.setMaxWidth(100);

        txtAdaptiveHighLimit = new TextField(String.valueOf(PreferenceManager.getAdaptiveHighLimit()));
        HBox.setHgrow(txtAdaptiveHighLimit, Priority.ALWAYS);
        txtAdaptiveHighLimit.setPrefHeight(30);
        txtAdaptiveHighLimit.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    txtAdaptiveHighLimit.setText(oldValue);
                }
            }
        });
        txtAdaptiveHighLimit.getStyleClass().add("setting_inputs");

        hBoxAdaptiveHighLimit.getChildren().addAll(labelAdaptiveHighLimit, txtAdaptiveHighLimit);


        // Adaptive Wear Limit
        HBox hBoxAdaptiveWearLimit = new HBox(5);
        hBoxAdaptiveWearLimit.setAlignment(Pos.CENTER);
        Label labelAdaptiveWearLimit = new Label("Adaptive Wear Limit:");
        //labelSiteKey.getStyleClass().add("label-info-value");
        labelAdaptiveWearLimit.setTextFill(Color.WHITE);
        labelAdaptiveWearLimit.setPrefWidth(300);
        labelAdaptiveWearLimit.setPrefHeight(26);
        labelAdaptiveWearLimit.setFont(labelFont);

        txtAdaptiveWearLimit = new TextField(String.valueOf(PreferenceManager.getAdaptiveWearLimit()));
        HBox.setHgrow(txtAdaptiveWearLimit, Priority.ALWAYS);
        txtAdaptiveWearLimit.setPrefHeight(30);
        txtAdaptiveWearLimit.getStyleClass().add("setting_inputs");
        txtAdaptiveWearLimit.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*(\\.\\d*)?")) {
                    txtAdaptiveWearLimit.setText(oldValue);
                }
            }
        });
        hBoxAdaptiveWearLimit.getChildren().addAll(labelAdaptiveWearLimit, txtAdaptiveWearLimit);

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

        vBoxTeachModeSetting.getChildren().addAll(labelTeachModeSettings, hBoxHLPercent, hBoxWLPercent, hBoxLLErrorPercent, hBoxTLPercent,
                hBoxDefaultHL, hBoxDefaultWL, hBoxDefaultStartDelay, hBoxDefaultFilter, hBoxScaleChart, hBoxLeadInTrigger, hBoxLeadInFeedrate,
                new Separator(), hBoxMacroInterruptEnable, hBoxLeadInFREnable, hBoxAdaptiveEnable,
                hBoxAdaptiveMin, hBoxAdaptiveMax, hBoxAdaptiveHighLimit, hBoxAdaptiveWearLimit, hbBtn);

        pane.setCenter(vBoxTeachModeSetting);

        // Init Scene
        Scene scene = new Scene(pane, 400, 400);
        scene.getStylesheets().add(getClass().getClassLoader().getResource("resource/style/rootStyles.css").toExternalForm());

        screen_stage = new Stage(StageStyle.DECORATED);
        screen_stage.setTitle("Settings");

        screen_stage.setScene(scene);
        screen_stage.centerOnScreen();
        screen_stage.getIcons().add(LogoManager.getInstance().getLogo());
        //screen_stage.setAlwaysOnTop(true);
        screen_stage.setResizable(false);
        screen_stage.setMinWidth(400);
        screen_stage.setMinHeight(400);
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
            } else if (event.getSource() == btnMacroInterruptEnable) {
                btnMacroInterruptEnable.setText(btnMacroInterruptEnable.getText().equals("ON") ? "OFF" : "ON");
            } else if (event.getSource() == btnAdaptiveEnable) {
                btnAdaptiveEnable.setText(btnAdaptiveEnable.getText().equals("ON") ? "OFF" : "ON");
            } else if (event.getSource() == btnLeadInFREnable) {
                btnLeadInFREnable.setText(btnLeadInFREnable.getText().equals("ON") ? "OFF" : "ON");
            } else if (event.getSource() == btnSave) {
                // Save Settings
                try {
                    PreferenceManager.setHighLimitPercent(Float.parseFloat(txtHLPercent.getText().trim()));
                    PreferenceManager.setWearLimitPercent(Float.parseFloat(txtWLPercent.getText().trim()));
                    PreferenceManager.setLowLimitErrorPercent(Float.parseFloat(txtLLErrorPercent.getText().trim()));
                    PreferenceManager.setTargetLoadPercent(Float.parseFloat(txtTLPercent.getText().trim())); // Default Filter

                    PreferenceManager.setDefaultHighLimitDelay(Float.parseFloat(txtDefaultHL.getText().trim())); // Default high limit delay
                    PreferenceManager.setDefaultWearLimitDelay(Float.parseFloat(txtDefaultWL.getText().trim())); // Default wear limit delay
                    PreferenceManager.setDefaultStartDelay(Float.parseFloat(txtDefaultStartDelay.getText().trim())); // Default Start delay

                    PreferenceManager.setDefaultFilter(Float.parseFloat(txtDefaultFilter.getText().trim())); // Default Filter
                    PreferenceManager.setScaleChartAboveHighLimit(Float.parseFloat(txtScaleChart.getText().trim())); // Default Filter
                    PreferenceManager.setDefaultLeadInTrigger(Float.parseFloat(txtDefaultLeadInTrigger.getText().trim())); // Default Leadin Trigger
                    PreferenceManager.setDefaultLeadInFeedrate(Float.parseFloat(txtDefaultLeadInFeedrate.getText().trim())); // Default Leadin Feedrate

                    PreferenceManager.setMacroInterruptEnabled(btnMacroInterruptEnable.getText().equals("ON") ? true : false); // Default Filter
                    PreferenceManager.setLeadInFREnabled(btnLeadInFREnable.getText().equals("ON") ? true : false); // Default Filter
                    PreferenceManager.setAdaptiveEnabled(btnAdaptiveEnable.getText().equals("ON") ? true : false); // Default Filter

                    PreferenceManager.setAdaptiveMin(Integer.parseInt(txtAdaptiveMin.getText().trim()));
                    PreferenceManager.setAdaptiveMax(Integer.parseInt(txtAdaptiveMax.getText().trim()));
                    PreferenceManager.setAdaptiveHighLimit(Integer.parseInt(txtAdaptiveHighLimit.getText().trim()));
                    PreferenceManager.setAdaptiveWearLimit(Integer.parseInt(txtAdaptiveWearLimit.getText().trim()));
                    // Save and Return to the main screen.

                    Toast.message("Save Successful!");
                    close();
                } catch (Exception e) {
                    AlertManager.getInstance().showAlert("Invalid Data", e.getMessage());
                }
            }
        }
    };
}
