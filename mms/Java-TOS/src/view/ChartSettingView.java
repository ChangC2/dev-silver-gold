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
import javafx.scene.control.ComboBox;
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

public class ChartSettingView {

    Stage screen_stage;

    private static ChartSettingView instance;

    public static ChartSettingView getInstance() {
        if (instance == null) {
            instance = new ChartSettingView();
        }
        return instance;
    }

    //https://www.codeproject.com/Articles/1116455/Simple-Application-For-Creating-Serial-Number-Base
    //https://www.dreamincode.net/forums/topic/176014-serial-generator-and-validator/

    TextField txtFanucIP;
    TextField txtFanucPort;

    TextField txtPLCIP;
    TextField txtPLCPort;

    TextField txtLineWidth;
    TextField txtWLPercent;
    TextField txtDefaultHL;
    TextField txtLLErrorPercent;
    TextField txtTLPercent;
    TextField txtScaleChart;
    TextField txtDefaultWL;



    ComboBox controlComboBox;
    Button btnCancel;
    Button btnSave;


    BorderPane pane;
    VBox vBoxChartSettings;
    public ChartSettingView() {
        initView();
    }

    private void initView() {
        pane = new BorderPane();
        pane.setPrefSize(503.0, 377);
        pane.setPadding(new Insets(30));

        vBoxChartSettings = new VBox(30);
        vBoxChartSettings.setPadding(new Insets(30.0,50.0,30.0,50.0));

        Label labelChartSettings = new Label("Chart Settings");
        labelChartSettings.setFont(Font.font("Tahoma", FontWeight.BOLD, 22));
        labelChartSettings.setTextFill(Color.WHITE);

        // Line Width
        HBox hBoxLineWidth = new HBox(5);
        hBoxLineWidth.setAlignment(Pos.CENTER);
        Label labelLineWidth = new Label("Line Width:");
        //labelSiteCode.getStyleClass().add("label-info-value");
        labelLineWidth.setTextFill(Color.WHITE);
        labelLineWidth.setFont(Font.font(14));
        labelLineWidth.setPrefWidth(300);
        labelLineWidth.setPrefHeight(26);
        //labelServerIP.setMaxWidth(100);

        txtLineWidth = new TextField(String.valueOf(PreferenceManager.getLineWidth()));
        HBox.setHgrow(txtLineWidth, Priority.ALWAYS);
        txtLineWidth.setPrefHeight(30);
        txtLineWidth.getStyleClass().add("setting_inputs");
        hBoxLineWidth.getChildren().addAll(labelLineWidth, txtLineWidth);
        txtLineWidth.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*(\\.\\d*)?")) {
                    txtLineWidth.setText(oldValue);
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

        vBoxChartSettings.getChildren().addAll(labelChartSettings, hBoxLineWidth,hbBtn);

        pane.setCenter(vBoxChartSettings);

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

        // Fanuc IP and Port
        txtFanucIP.setText(PreferenceManager.getFanucIP());
        if (PreferenceManager.getFanucPort() == 0) {
            txtFanucPort.setText("");
        } else {
            txtFanucPort.setText(String.valueOf(PreferenceManager.getFanucPort()));
        }

        // PLC IP and Port
        txtPLCIP.setText(PreferenceManager.getPLCIP());
        if (PreferenceManager.getPLCPort() == 0) {
            txtPLCPort.setText("");
        } else {
            txtPLCPort.setText(String.valueOf(PreferenceManager.getPLCPort()));
        }
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
                // Save Settings
                PreferenceManager.setLineWidth(Float.parseFloat(txtLineWidth.getText().trim()));

                // Save and Return to the main screen.

                Toast.message("Save Successful!");
                close();
            }
        }
    };
}
