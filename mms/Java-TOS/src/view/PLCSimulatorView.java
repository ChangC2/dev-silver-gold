package view;

import Utils.PreferenceManager;
import com.sun.org.apache.xml.internal.utils.PrefixResolver;
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
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sun.applet.Main;

public class PLCSimulatorView {

    Stage screen_stage;

    private static PLCSimulatorView instance;

    public static PLCSimulatorView getInstance() {
        if (instance == null) {
            instance = new PLCSimulatorView();
        }
        return instance;
    }

    //https://www.codeproject.com/Articles/1116455/Simple-Application-For-Creating-Serial-Number-Base
    //https://www.dreamincode.net/forums/topic/176014-serial-generator-and-validator/

    SwitchButton btnSwitch;

    BorderPane pane;
    VBox vBoxDeviceSetting;
    Label labelSliderValueHP;
    Slider sliderHP;

    Label labelSliderValueScale;
    Slider sliderScale;

    Button btnCancel;

    public PLCSimulatorView() {
        initView();
    }

    private void initView() {
        pane = new BorderPane();
        pane.setPrefSize(600, 550);
        pane.setPadding(new Insets(15, 30, 15, 30));

        vBoxDeviceSetting = new VBox(20);
        vBoxDeviceSetting.setPadding(new Insets(30.0,50.0,30.0,50.0));

        Label labelPLCSettings = new Label("TOS Manual Settings");
        labelPLCSettings.setFont(Font.font("Tahoma", FontWeight.BOLD, 20));
        labelPLCSettings.setTextFill(Color.WHITE);

        Font labelFont = Font.font(14);

        // Simulator
        HBox hBoxSimulator = new HBox(5);
        hBoxSimulator.setAlignment(Pos.CENTER);
        Label labelUseSimulator = new Label("Enable Simulator");
        //labelSiteCode.getStyleClass().add("label-info-value");
        labelUseSimulator.setTextFill(Color.WHITE);
        labelUseSimulator.setFont(labelFont);
        labelUseSimulator.setPrefWidth(173);
        labelUseSimulator.setPrefHeight(26);
        //labelServerIP.setMaxWidth(100);

        btnSwitch = new SwitchButton();
        btnSwitch.setSwitchListener(new SwitchButton.SwitchChangeListener() {
            @Override
            public void stateChanged(Boolean state) {
                PreferenceManager.setPLCSimulatorEnabled(state);

                sliderHP.setDisable(!state);
                sliderScale.setDisable(!state);
            }
        });
        hBoxSimulator.getChildren().addAll(labelUseSimulator, btnSwitch);


        // Fanuc Bridge Port
        HBox hBoxSliders = new HBox(5);
        hBoxSliders.setAlignment(Pos.CENTER);
        hBoxSliders.setPrefHeight(450);

        // Add HP Slider Panel Elements
        double sliderInitValueHP = MainView.getInstance().getValueHP();
        VBox vBoxSliderHP = new VBox();
        vBoxSliderHP.setAlignment(Pos.CENTER);
        Font sliderFont = new Font("Arial", 16);
        labelSliderValueHP = new Label(String.format("%.2f", sliderInitValueHP));
        labelSliderValueHP.setStyle("-fx-font-weight: bold");
        labelSliderValueHP.setTextFill(Color.WHITE);
        labelSliderValueHP.setFont(sliderFont);

        Label labelSliderTitleHP = new Label("HP");
        labelSliderTitleHP.setStyle("-fx-font-weight: bold");
        labelSliderTitleHP.setTextFill(Color.WHITE);
        labelSliderTitleHP.setFont(sliderFont);

        sliderHP = new Slider(0, 5.00, sliderInitValueHP);
        sliderHP.setOrientation(Orientation.VERTICAL);
        sliderHP.setShowTickLabels(false);
        sliderHP.setSnapToTicks(true);
        sliderHP.setShowTickMarks(true);
        sliderHP.setMajorTickUnit(0.1);
        sliderHP.setBlockIncrement(0.1);
        sliderHP.getStyleClass().add("slider-purple");
        sliderHP.styleProperty().bind(Bindings.createStringBinding(() -> {
            double percentage = (sliderHP.getValue() - sliderHP.getMin()) / (sliderHP.getMax() - sliderHP.getMin()) * 100.0;
            return String.format("-slider-purple-track-color: linear-gradient(to top, -slider-purple-filled-track-color 0%%, "
                            + "-slider-purple-filled-track-color %f%%, -fx-base %f%%, -fx-base 100%%);",
                    percentage, percentage);
        }, sliderHP.valueProperty(), sliderHP.minProperty(), sliderHP.maxProperty()));

        sliderHP.setPadding(new Insets(5, 20, 5, 20));
        VBox.setVgrow(sliderHP, Priority.ALWAYS);

        sliderHP.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (newValue == oldValue)
                    return;

                labelSliderValueHP.setText(String.format("%.02f", newValue.floatValue()));

                if (btnSwitch.isChecked()) {
                    MainView.getInstance().setSimulatorHPValue(newValue.floatValue());
                }
            }
        });
        vBoxSliderHP.getChildren().addAll(labelSliderValueHP, sliderHP, labelSliderTitleHP);


        // Add Scale Slider Panel Elements
        double sliderInitValueScale = MainView.getInstance().getValueChartScale();
        VBox vBoxSliderScale = new VBox();
        vBoxSliderScale.setAlignment(Pos.CENTER);

        labelSliderValueScale = new Label(String.format("%.2f", sliderInitValueScale));
        labelSliderValueScale.setStyle("-fx-font-weight: bold");
        labelSliderValueScale.setTextFill(Color.WHITE);
        labelSliderValueScale.setFont(sliderFont);

        Label labelSliderTitleScale = new Label("Scale");
        labelSliderTitleScale.setStyle("-fx-font-weight: bold");
        labelSliderTitleScale.setTextFill(Color.WHITE);
        labelSliderTitleScale.setFont(sliderFont);

        sliderScale = new Slider(1, 5.00, sliderInitValueScale);
        sliderScale.setOrientation(Orientation.VERTICAL);
        sliderScale.setShowTickLabels(false);
        sliderScale.setSnapToTicks(true);
        sliderScale.setShowTickMarks(true);
        sliderScale.setMajorTickUnit(0.1);
        sliderScale.setBlockIncrement(0.1);
        sliderScale.getStyleClass().add("slider-orange");
        sliderScale.styleProperty().bind(Bindings.createStringBinding(() -> {
            double percentage = (sliderScale.getValue() - sliderScale.getMin()) / (sliderScale.getMax() - sliderScale.getMin()) * 100.0;
            return String.format("-slider-orange-track-color: linear-gradient(to top, -slider-orange-filled-track-color 0%%, "
                            + "-slider-orange-filled-track-color %f%%, -fx-base %f%%, -fx-base 100%%);",
                    percentage, percentage);
        }, sliderScale.valueProperty(), sliderScale.minProperty(), sliderScale.maxProperty()));

        sliderScale.setPadding(new Insets(5, 20, 5, 20));
        VBox.setVgrow(sliderScale, Priority.ALWAYS);

        sliderScale.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (newValue == oldValue)
                    return;

                labelSliderValueScale.setText(String.format("%.02f", newValue.floatValue()));

                if (btnSwitch.isChecked()) {
                    MainView.getInstance().setSimulatorScaleValue(newValue.floatValue());
                }
            }
        });
        vBoxSliderScale.getChildren().addAll(labelSliderValueScale, sliderScale, labelSliderTitleScale);

        hBoxSliders.getChildren().addAll(vBoxSliderHP, vBoxSliderScale);


        // Buttons Action
        btnCancel = new Button("  Close  ");
        btnCancel.getStyleClass().add("button-gradient5");
        btnCancel.setOnAction(buttonHandler);

        HBox hbBtn = new HBox(25);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().addAll(btnCancel);

        vBoxDeviceSetting.getChildren().addAll(labelPLCSettings, hBoxSimulator, hBoxSliders, new Separator(), hbBtn);

        pane.setCenter(vBoxDeviceSetting);

        // Init Scene
        Scene scene = new Scene(pane, 600, 550);
        scene.getStylesheets().add(getClass().getClassLoader().getResource("resource/style/rootStyles.css").toExternalForm());

        screen_stage = new Stage(StageStyle.DECORATED);
        screen_stage.setTitle("TOS Simulator");

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

        double initHP = MainView.getInstance().getValueHP();
        labelSliderValueHP.setText(String.format("%.02f", initHP));
        sliderHP.setValue(initHP);

        double initScale = MainView.getInstance().getValueChartScale();
        labelSliderValueScale.setText(String.format("%.02f", initScale));
        if (initScale > sliderScale.getMax()) {
            sliderScale.setMax((int) (initScale + 1));
        }
        sliderScale.setValue(initScale);

        boolean isPLCSimulatorMode = PreferenceManager.isPLCSimulatorEnabled();
        btnSwitch.setChecked(isPLCSimulatorMode);
        sliderHP.setDisable(!isPLCSimulatorMode);
        sliderScale.setDisable(!isPLCSimulatorMode);
    }

    public void close() {
        screen_stage.close();

        //PreferenceManager.setPLCSimulatorEnabled(false);
    }

    EventHandler<ActionEvent> buttonHandler = new EventHandler<ActionEvent>(){

        @Override
        public void handle(final ActionEvent event) {

            if (event.getSource() == btnCancel) {
                close();
            }
        }
    };
}
