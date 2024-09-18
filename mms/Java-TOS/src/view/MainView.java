package view;

import Controller.Api;
import Controller.LoginResultListener;
import GLG.GlgRTChartView;
import Haas.EthernetHaasMonitor;
import Mail.MailSender;
import Main.Resource;
import Model.*;
import Utils.PreferenceManager;
import Utils.Utils;
import Utils.DateTimeUtils;
import Utils.CustomTimer;
import com.jfoenix.controls.JFXDrawer;
import de.re.easymodbus.exceptions.ModbusException;
import de.re.easymodbus.modbusclient.ModbusClient;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.http.util.TextUtils;

import javax.swing.*;
import java.io.IOException;
import java.util.*;

import javax.swing.Timer;

import Utils.HttpClientUtils;

//https://github.com/MangoAutomation/modbus4j
//https://www.youtube.com/watch?v=jcsvt_6m21E

public class MainView {
    private static MainView instance;
    BorderPane mainContentView;
    BorderPane main_pane;
    Button btnChangeVars;
    Button btnNavMenu;
    Button btnKeylock;
    Button btnReloadTool;
    Button btnReset;
    Button btnOpenToolDataFile;
    Button btnExit;                     // It is not used now.

    // Device Connect Status
    ImageView ivCNCCtrlConnStatus;      // Fanuc Connection Status
    ImageView ivTOSCtrlConnStatus;      // PLC Connection Status
    Image imageStatusOn;
    Image imageStatusOff;
    Image imageStatusSemi;

    // Status Values
    Label labelMachine;
    Label labelStatusBar;

    private static final Boolean USE_PLC_ALARM = false;     // We don't use this Alarm
    private static final int PLC_ALARM_IDLE = 0;
    private static final int PLC_ALARM_LOWLIMIT = 1;
    private static final int PLC_ALARM_WEARLIMIT = 2;
    private static final int PLC_ALARM_HIGHLIMIT = 3;

    private static final int SYSTEM_ALARM_IDLE = 0;
    private static final int SYSTEM_ALARM_MONITOR = 1;
    private static final int SYSTEM_ALARM_MONITOR_ADAPTIVE = 2;
    private static final int SYSTEM_ALARM_HIGH_LIMIT = 3;
    private static final int SYSTEM_ALARM_HIGH_LIMIT_ADAPTIVE = 4;
    private static final int SYSTEM_ALARM_WEAR_LIMIT = 5;
    private static final int SYSTEM_ALARM_WEAR_LIMIT_ADAPTIVE = 6;
    private static final int SYSTEM_ALARM_LOW_LIMIT = 7;
    private int currSystemAlarmStatus = SYSTEM_ALARM_IDLE;

    private CustomTimer highLimitTimer = new CustomTimer();
    private CustomTimer wearLimitTimer = new CustomTimer();
    private CustomTimer lowLimitTimer = new CustomTimer();

    Timeline timelineAnimAlarmStatus = new Timeline(new KeyFrame(Duration.seconds(0.5), new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
            if (currSystemAlarmStatus == SYSTEM_ALARM_IDLE) {
                labelStatusBar.setStyle("-fx-border-color: white;-fx-text-fill: white;");
            } else if (currSystemAlarmStatus == SYSTEM_ALARM_MONITOR || currSystemAlarmStatus == SYSTEM_ALARM_MONITOR_ADAPTIVE) {
                labelStatusBar.setStyle("-fx-border-color: green;-fx-text-fill: green;");
            } else if (currSystemAlarmStatus == SYSTEM_ALARM_HIGH_LIMIT || currSystemAlarmStatus == SYSTEM_ALARM_HIGH_LIMIT_ADAPTIVE) {
                labelStatusBar.setStyle("-fx-border-color: red;-fx-text-fill: red;");
            } else if (currSystemAlarmStatus == SYSTEM_ALARM_WEAR_LIMIT || currSystemAlarmStatus == SYSTEM_ALARM_WEAR_LIMIT_ADAPTIVE) {
                labelStatusBar.setStyle("-fx-border-color: orange;-fx-text-fill: orange;");
            } else if (currSystemAlarmStatus == SYSTEM_ALARM_LOW_LIMIT) {
                labelStatusBar.setStyle("-fx-border-color: dodgerblue;-fx-text-fill: dodgerblue;");
            }
        }
    }), new KeyFrame(Duration.seconds(1), evt -> labelStatusBar.setStyle("-fx-border-color: white;-fx-text-fill: white;")));

    Button btnTeachMode;
    Button btnTeachOnetool;
    Button btnMacroInterruptEnable;
    Button btnAdaptiveEnable;
    Button btnLeadInFREnable;

    Timeline timelineAnimTeachMode = new Timeline(new KeyFrame(Duration.seconds(.5), evt -> btnTeachMode.setStyle("-fx-background-color:green;")),
            new KeyFrame(Duration.seconds(1), evt -> btnTeachMode.setStyle("-fx-background-color:white")));

    Timeline timelineAnimTeachOneTool = new Timeline(new KeyFrame(Duration.seconds(.5), evt -> btnTeachOnetool.setStyle("-fx-background-color:green;")),
            new KeyFrame(Duration.seconds(1), evt -> btnTeachOnetool.setStyle("-fx-background-color:white")));

    VBox vBoxTool;
    Label valueTool;

    VBox vBoxSection;
    Label valueSection;

    VBox vBoxChannel;
    Label valueChannel;

    Stopwatch stopWatch;

    VBox vBoxComment;
    Label titleComment;
    Label valueComment;

    Timer timerStopWatcher;

    Label valueOptimize;        // It is not showing in UI, only used for detecting the signal change and for using the event.
    Label valueFeedHoldStatus;  // It is not showing in UI, only used for detecting the signal change and for using the event.
    Label valueStartDelay;
    Label valueHighLimitDelay;
    Label valueWearLimitDelay;
    Label valueAdaptiveMin;
    Label valueAdaptiveMax;
    Label valueSensorScaleSend;
    Label valueMonitorTime;

    // ToolBox Area Filter Info
    VBox vBoxFilter;
    Label valueFilter;

    Label valueHp;
    Label valueHp2;

    Label valueTarget;

    Label valueHighLimit;
    Label valueAdaptiveHighPercentage;

    Label valueWearLimit;
    Label valueAdaptiveWearPercentage;

    Label valueIdle;
    Label valueLeadInTrigger;

    Label valueLearnedLowLimitTimer;
    Label valueLowLimitTimer;

    Label valueFeedrate;

    Label valueInnerTimers;
    Label valueSendSignals;

    VBox vBoxProgNumber;
    Label titleProgNum;
    Label valueProgNum;

    JFXDrawer drawer;
    Button menuDasgboard;
    Button menuViewHST;
    Button menuTimeSavings;
    Button menuViewAlarams;
    Button menuMMS;
    Button menuSettings;
    Button menuOptimizationReporting;
    Button menuSimFanuc;
    Button menuSimPLC;
    Button menuProcessMonitor;
    Button menuCheckUpdate;

    Button btnTitleMonitor;         // Hidden, it is only used in Dev mode to simulate the Monitor(Fanuc.Optimize) signaul

    // Combo and Save Data panel
    VBox vBoxSaveVars;
    ComboBox priorityComboBox;
    Button btnSaveJob;

    Label valueLearnedMonitorTime;
    Label valueElapsedMonitorTime;
    VBox vBoxHp;
    VBox vBoxHp2;
    VBox vBoxTarget;

    VBox vBoxHighLimit;
    VBox vBoxAdaptiveHighLimit;
    VBox vBoxWearLimit;
    VBox vBoxAdaptiveWearLimit;

    VBox vBoxLearnedLowLimitTime;
    VBox vBoxLowLimitTimer;
    VBox vBoxIdle;
    VBox vBoxLeadInTrigger;

    HSTFileSaveManager hstFileSaveManager;
    String[] plotVariableNames = new String[]{"Target", "High", "Wear", "Low", "HP", "LEAD-IN TRIGGER", "IDLE", "Feedrate", "Current Seq Num", "HP2"};

    CSVSTFMReportManager csvReportManager;

    private GlgRTChartView applet = new GlgRTChartView();
    //private JApplet applet = new HelloWorldApplet();

    AnchorPane boxHomeSliders;

    // Filter Slider
    VBox vBoxSliderFilter;
    Slider sliderAdjustFilter;
    Label labelSliderFilterValue;

    // Normal Slider Value
    VBox vBoxSlider;
    Slider sliderAdjust;
    Label labelSliderValue;

    // Adaptive Slider Value
    VBox vBoxSliderAdaptive;
    Slider sliderAdjustAdaptive;
    Label labelSliderAdaptiveValue;
    int adaptiveViewTag = 0; // 0: Adaptive High%, 1: Adaptive Wear%

    ModbusClient modbusClientFanuc;
    String currFanucIP;
    int currFanucPort;

    ModbusClient modbusClientPLC;
    String currPLCIP;
    int currPLCPort;

    ModbusClient modbusClientRpi;
    String currRpiIP;
    int currRpiPort;

    boolean shutdownModbusClients = false;
    Thread threadReadFanuc;
    Thread threadReadPLC;
    Thread threadRpi;

    EthernetHaasMonitor ethernetHaasMonitor;

    UIDataModel uiVars = new UIDataModel();

    Thread threadAlarmCheck;

    private static final int plotCount = 2;

    public static MainView getInstance() {
        if (instance == null) {
            instance = new MainView();
        }
        return instance;
    }

    public MainView() {

        main_pane = new BorderPane();
        VBox vBoxMenuBar = new VBox();
        // Add Title Panel
        vBoxMenuBar.getChildren().add(getTitleMenuBox());
        Separator separator1 = new Separator();
        vBoxMenuBar.getChildren().add(separator1);
        main_pane.setTop(vBoxMenuBar);

        AnchorPane anchorPane = new AnchorPane();
        mainContentView = new BorderPane();
        VBox vBoxToolBar = new VBox();
        // Add Info Panel
        vBoxToolBar.getChildren().add(getToolbarBox());
        Separator separator2 = new Separator();
        vBoxToolBar.getChildren().add(separator2);

        // Add Gauge Panel
        vBoxToolBar.getChildren().add(getGaugeBox());

        mainContentView.setTop(vBoxToolBar);

        // Init HSTSaveManager
        hstFileSaveManager = new HSTFileSaveManager();

        // Add GLG
        applet.init();
        final SwingNode swingNode = new SwingNode();
        SwingUtilities.invokeLater(() -> {
                    swingNode.setContent(applet);
                    applet.start();

                    // Set HST Manager
                    applet.setHSTManager(hstFileSaveManager);
                }
        );

        csvReportManager = new CSVSTFMReportManager();
        //csvReportManager.init();

        AnchorPane newAnchoPane = new AnchorPane();
        newAnchoPane.getChildren().add(swingNode);

        AnchorPane.setRightAnchor(swingNode, 0.0);
        AnchorPane.setLeftAnchor(swingNode, 0.0);
        AnchorPane.setTopAnchor(swingNode, 0.0);
        AnchorPane.setBottomAnchor(swingNode, 0.0);

        newAnchoPane.setMaxWidth(Double.MAX_VALUE);
        newAnchoPane.setMaxHeight(Double.MAX_VALUE);

        mainContentView.setCenter(newAnchoPane);

        // Define Sliders
        boxHomeSliders = new AnchorPane();

        // Filter Slider
        double filterSliderInitValue = 10.0;
        vBoxSliderFilter = new VBox();
        vBoxSliderFilter.setSpacing(2);
        vBoxSliderFilter.setPadding(new Insets(1, 3, 1, 3));
        vBoxSliderFilter.setAlignment(Pos.CENTER);
        vBoxSliderFilter.getStyleClass().add("vbox-borderless");

        Label titleFilterSlider = new Label("Filter\nValue:");
        titleFilterSlider.setStyle("-fx-font-weight: bold");
        titleFilterSlider.setTextFill(Color.WHITE);
        titleFilterSlider.setAlignment(Pos.CENTER);
        Font filterSliderFont = new Font("Arial", 16);
        titleFilterSlider.setFont(filterSliderFont);

        labelSliderFilterValue = new Label(String.format("%.2f", filterSliderInitValue));
        labelSliderFilterValue.getStyleClass().add("label-info-value");
        labelSliderFilterValue.setFont(ViewHelper.getInstance().getFontInformation());
        labelSliderFilterValue.setTextFill(Color.WHITE);

        // Creat the Slider
        sliderAdjustFilter = new Slider(0, 20.0, filterSliderInitValue);

        //Setting its orientation to vertical
        sliderAdjustFilter.setOrientation(Orientation.VERTICAL);
        sliderAdjustFilter.setMin(0);
        sliderAdjustFilter.setMax(20);
        sliderAdjustFilter.setValue(10);
        //sliderFilterAdjust.setShowTickLabels(true);
        sliderAdjustFilter.setShowTickMarks(true);
        sliderAdjustFilter.setMajorTickUnit(0.01);
        sliderAdjustFilter.setMinorTickCount(1);
        sliderAdjustFilter.setBlockIncrement(0.05);

        // * Filter slider use stand-alone slider, so no need to change slider.
        sliderAdjustFilter.getStyleClass().add("slider-orange");
        sliderAdjustFilter.styleProperty().bind(Bindings.createStringBinding(() -> {
            double percentage = (sliderAdjustFilter.getValue() - sliderAdjustFilter.getMin()) / (sliderAdjustFilter.getMax() - sliderAdjustFilter.getMin()) * 100.0;
            return String.format("-slider-orange-track-color: linear-gradient(to top, -slider-orange-filled-track-color 0%%, "
                            + "-slider-orange-filled-track-color %f%%, -fx-base %f%%, -fx-base 100%%);",
                    percentage, percentage);
        }, sliderAdjustFilter.valueProperty(), sliderAdjustFilter.minProperty(), sliderAdjustFilter.maxProperty()));

        sliderAdjustFilter.setPadding(new Insets(0, 20, 40, 20));
        VBox.setVgrow(sliderAdjustFilter, Priority.ALWAYS);
        sliderAdjustFilter.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                //Setting the angle for the rotation
                if (oldValue == newValue)
                    return;

                labelSliderFilterValue.setTextFill(Color.rgb(255, 255, 0));
                labelSliderFilterValue.setText(String.format("%.1f", newValue));
                valueFilter.setText(String.format("%.1f", newValue));

                int feedbackAddr = 17;
                int feedbackVal = (int) (newValue.doubleValue() * 100);

                if (isPLCConnected()) {
                    ModbusClient modbusPLCClient = getNewPLCConn();
                    if (feedbackAddr > 0 && modbusPLCClient != null && modbusPLCClient.isConnected()) {

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    modbusPLCClient.WriteSingleRegister(feedbackAddr, feedbackVal);
                                    modbusPLCClient.Disconnect();
                                } catch (ModbusException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }
                }
            }
        });
        vBoxSliderFilter.getChildren().addAll(titleFilterSlider, labelSliderFilterValue, sliderAdjustFilter);
        vBoxSliderFilter.setVisible(false);
        AnchorPane.setRightAnchor(vBoxSliderFilter, 0.0);
        AnchorPane.setLeftAnchor(vBoxSliderFilter, 0.0);
        AnchorPane.setTopAnchor(vBoxSliderFilter, 0.0);
        AnchorPane.setBottomAnchor(vBoxSliderFilter, 0.0);

        // Add Slider Panel Elements
        double sliderInitValue = 1.0;
        vBoxSlider = new VBox();
        vBoxSlider.setAlignment(Pos.CENTER);
        Font sliderFont = new Font("Arial", 16);
        labelSliderValue = new Label(String.format("%.2f", sliderInitValue));
        labelSliderValue.setStyle("-fx-font-weight: bold");
        labelSliderValue.setTextFill(Color.WHITE);
        labelSliderValue.setFont(sliderFont);

        Label labelSliderTitle = new Label("Value");
        labelSliderTitle.setStyle("-fx-font-weight: bold");
        labelSliderTitle.setTextFill(Color.WHITE);
        labelSliderTitle.setFont(sliderFont);

        sliderAdjust = new Slider(0, 3.00, sliderInitValue);
        sliderAdjust.setOrientation(Orientation.VERTICAL);
        sliderAdjust.setShowTickLabels(false);
        sliderAdjust.setSnapToTicks(true);
        sliderAdjust.setShowTickMarks(true);
        sliderAdjust.setMajorTickUnit(0.01);
        sliderAdjust.setBlockIncrement(0.02);

        sliderAdjust.setPadding(new Insets(30, 20, 25, 20));
        VBox.setVgrow(sliderAdjust, Priority.ALWAYS);

        sliderAdjust.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (oldValue == newValue)
                    return;

                //Setting the angle for the rotation
                if (getValueProgNum() > 0) {
                    labelSliderValue.setText(String.format("%.02f", newValue));
                    String val = String.valueOf(priorityComboBox.getValue());
                    int feedbackAddr = 0;
                    int feedbackVal = (int) (newValue.doubleValue() * 100);

                    switch (val) {
                        case "TARGET":
                            valueTarget.setText(String.format("%.02f", newValue));
                            labelSliderValue.setText(String.format("%.02f", newValue));
                            labelSliderValue.setTextFill(Color.rgb(0, 128, 0));
                            feedbackAddr = 1;

                            break;
                        case "HIGH":
                            valueHighLimit.setText(String.format("%.02f", newValue));
                            labelSliderValue.setText(String.format("%.02f", newValue));

                            labelSliderValue.setTextFill(Color.rgb(255, 0, 0));
//                        sliderAdjust.setStyle("");
//                        sliderAdjust.styleProperty().set("");

                            feedbackAddr = 2;

                            break;
                        case "WEAR":
                            valueWearLimit.setText(String.format("%.02f", newValue));
                            labelSliderValue.setText(String.format("%.02f", newValue));

                            labelSliderValue.setTextFill(Color.rgb(255, 165, 0));
                            feedbackAddr = 3;

                            break;
//
                        case "IDLE":
                            valueIdle.setText(String.format("%.02f", newValue));
                            labelSliderValue.setText(String.format("%.02f", newValue));

                            labelSliderValue.setTextFill(Color.rgb(255, 255, 255));
                            feedbackAddr = 34;

                            break;
                        case "LEAD-IN TRIGGER":
                            valueLeadInTrigger.setText(String.format("%.02f", newValue));
                            labelSliderValue.setText(String.format("%.02f", newValue));

                            labelSliderValue.setTextFill(Color.rgb(139, 69, 19));
                            feedbackAddr = 22;
                            break;
                    }

                    if (isTeachMode()) {
                        // In teach mode, we don't send process values.
                        return;
                    }

                    // * In case of disconnected status to the PLC, it blocks process,
                    // so check global connection first before send new slider value.
                    if (isPLCConnected()) {

                        ModbusClient modbusPLCClient = getNewPLCConn();
                        if (feedbackAddr > 0 && modbusPLCClient != null && modbusPLCClient.isConnected()) {

                            int finalFeedbackAddr = feedbackAddr;
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        modbusPLCClient.WriteSingleRegister(finalFeedbackAddr, feedbackVal);
                                        modbusPLCClient.Disconnect();
                                    } catch (ModbusException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }).start();
                        }
                    }
                }
            }
        });
        vBoxSlider.getChildren().addAll(labelSliderValue, sliderAdjust, labelSliderTitle);
        AnchorPane.setRightAnchor(vBoxSlider, 0.0);
        AnchorPane.setLeftAnchor(vBoxSlider, 0.0);
        AnchorPane.setTopAnchor(vBoxSlider, 0.0);
        AnchorPane.setBottomAnchor(vBoxSlider, 0.0);

        //VBox bBoxSliderAdaptive;
        //Slider sliderAdjustAdaptive;
        //Label labelSliderAdaptiveValue;

        vBoxSliderAdaptive = new VBox();
        vBoxSliderAdaptive.setVisible(false);
        vBoxSliderAdaptive.setAlignment(Pos.CENTER);
        Font sliderAdaptiveFont = new Font("Arial", 16);
        labelSliderAdaptiveValue = new Label(String.valueOf(0));
        labelSliderAdaptiveValue.setStyle("-fx-font-weight: bold");
        labelSliderAdaptiveValue.setTextFill(Color.WHITE);
        labelSliderAdaptiveValue.setFont(sliderAdaptiveFont);

        Label labelSliderAdaptiveTitle = new Label("Value");
        labelSliderAdaptiveTitle.setStyle("-fx-font-weight: bold");
        labelSliderAdaptiveTitle.setTextFill(Color.WHITE);
        labelSliderAdaptiveTitle.setFont(sliderAdaptiveFont);

        sliderAdjustAdaptive = new Slider(0, 250, 1);
        sliderAdjustAdaptive.setOrientation(Orientation.VERTICAL);
        sliderAdjustAdaptive.setShowTickLabels(false);
        sliderAdjustAdaptive.setSnapToTicks(true);
        sliderAdjustAdaptive.setShowTickMarks(true);
        sliderAdjustAdaptive.setMinorTickCount(1);
        sliderAdjustAdaptive.setMajorTickUnit(1);
        sliderAdjustAdaptive.setBlockIncrement(2);

        sliderAdjustAdaptive.setPadding(new Insets(30, 20, 25, 20));
        VBox.setVgrow(sliderAdjustAdaptive, Priority.ALWAYS);

        sliderAdjustAdaptive.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (oldValue == newValue)
                    return;

                if (adaptiveViewTag == 0) {         // Adaptive High %
                    valueAdaptiveHighPercentage.setText(String.format("%d", newValue.intValue()));
                    labelSliderAdaptiveValue.setText(String.format("%d", newValue.intValue()));
                    // labelSliderAdaptiveValue.setTextFill(Color.rgb(255, 0, 0));
                } else if (adaptiveViewTag == 1) {   // Adaptive High %
                    valueAdaptiveWearPercentage.setText(String.format("%d", newValue.intValue()));
                    labelSliderAdaptiveValue.setText(String.format("%d", newValue.intValue()));
                    // labelSliderAdaptiveValue.setTextFill(Color.rgb(255, 165, 0));
                }
            }
        });
        vBoxSliderAdaptive.getChildren().addAll(labelSliderAdaptiveValue, sliderAdjustAdaptive, labelSliderAdaptiveTitle);
        AnchorPane.setRightAnchor(vBoxSliderAdaptive, 0.0);
        AnchorPane.setLeftAnchor(vBoxSliderAdaptive, 0.0);
        AnchorPane.setTopAnchor(vBoxSliderAdaptive, 0.0);
        AnchorPane.setBottomAnchor(vBoxSliderAdaptive, 0.0);

        boxHomeSliders.getChildren().addAll(vBoxSliderFilter, vBoxSlider, vBoxSliderAdaptive);

        mainContentView.setRight(boxHomeSliders);

        // Setting the anchor to the main contents
        AnchorPane.setTopAnchor(mainContentView, 0.0);
        AnchorPane.setLeftAnchor(mainContentView, 0.0);
        AnchorPane.setRightAnchor(mainContentView, 0.0);
        AnchorPane.setBottomAnchor(mainContentView, 0.0);

        anchorPane.getChildren().add(mainContentView);

        // Attach Navigation Menu
        drawer = new JFXDrawer(new Duration(300));
        drawer.setDefaultDrawerSize(200);
        drawer.setMaxWidth(-Double.MAX_VALUE);
        drawer.setMinWidth(-Double.MAX_VALUE);
        drawer.setPrefHeight(400);
        drawer.setResizableOnDrag(false);
        drawer.setSidePane(getNavMenuView());
        drawer.setDirection(JFXDrawer.DrawerDirection.LEFT);
        drawer.setOnDrawerOpening(e -> {
            //Transition animation = hamburger.getAnimation();
            //animation.setRate(1);
            //animation.play();
            ViewHelper.getInstance().setButtonIcon(btnNavMenu, Resource.MENU_ICON_SIZE, LogoManager.getInstance().getMenuCloseIcon());
        });

        drawer.setOnDrawerClosing(e -> {
            //Transition animation = hamburger.getAnimation();
            //animation.setRate(-1);
            //animation.play();
            ViewHelper.getInstance().setButtonIcon(btnNavMenu, Resource.MENU_ICON_SIZE, LogoManager.getInstance().getMenuIcon());
        });

        btnNavMenu.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (drawer.isClosed() || drawer.isClosing()) drawer.open();
                else drawer.close();
            }
        });
        AnchorPane.setTopAnchor(drawer, 0.0);
        AnchorPane.setLeftAnchor(drawer, 0.0);
        AnchorPane.setBottomAnchor(drawer, 0.0);
        anchorPane.getChildren().add(drawer);

        main_pane.setCenter(anchorPane);

        /*byte[] floatBytes = new byte[]{0, 0, 10, 1};
        byte[] intBytes = new byte[]{0, 0, 10, 1};
        int aaa = (int) ByteBuffer.wrap(floatBytes).getFloat();
        int bbb = ByteBuffer.wrap(floatBytes).getInt();*/

        //0, 16256
        /*int[] valueBytes = ModbusClient.ConvertFloatToTwoRegisters(1);
        int[] fanucHoldings = new int[2];
        fanucHoldings[1] = valueBytes[1];
        fanucHoldings[0] = valueBytes[0];
        int testVal = (int) ModbusClient.ConvertRegistersToFloat(fanucHoldings);*/

        /*int[] fanucHoldings = new int[2];
        fanucHoldings[1] = 16192;
        fanucHoldings[0] = 0;
        float testVal = ModbusClient.ConvertRegistersToFloat(fanucHoldings);*/

        // Auto connect thread
        shutdownModbusClients = false;

        // Fanuc Data read thread
        threadReadFanuc = new Thread(new Runnable() {
            @Override
            public void run() {
                while (!shutdownModbusClients) {

                    // try to connect Fanuc
                    if (PreferenceManager.getControlType() == PreferenceManager.CONTROL_TYPE_FANUC_HAAS) {
                        String faasIP = PreferenceManager.getHaasIP();
                        int faasPort = PreferenceManager.getHaasPort();

                        if (ethernetHaasMonitor == null) {
                            ethernetHaasMonitor = new EthernetHaasMonitor(faasIP, faasPort);
                            ethernetHaasMonitor.start();
                        } else {
                            if (!ethernetHaasMonitor.isSameDeviceInfo(faasIP, faasPort)) {
                                ethernetHaasMonitor.setNewDeviceInfo(faasIP, faasPort);
                            }
                        }

                        int hassConnectStatus = ethernetHaasMonitor.isConnected() ? 1 : 0;

                        if (hassConnectStatus != uiVars.machineConnectStatus) {
                            uiVars.machineConnectStatus = hassConnectStatus;
                            updateConnectStatus();
                        }

                        uiVars.Optimize = ethernetHaasMonitor.getMonitor() > 0 ? 1 : 0;
                        uiVars.Tool = ethernetHaasMonitor.getTool();
                        uiVars.Section = ethernetHaasMonitor.getSection();
                        uiVars.Channel = ethernetHaasMonitor.getChannel();
                        uiVars.InCycle = ethernetHaasMonitor.getInCycle();

                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                valueOptimize.setText(String.valueOf(uiVars.Optimize));
                                btnTitleMonitor.setText(uiVars.Optimize > 0 ? "MONITOR \nON" : "MONITOR \nOFF");

                                valueTool.setText(String.valueOf(uiVars.Tool));
                                valueSection.setText(String.valueOf(uiVars.Section));
                                valueChannel.setText(String.valueOf(uiVars.Channel));

                                // Still not using FeedHold in Haas
                                //valueFeedHoldStatus.setText(String.valueOf(uiVars.FeedHoldStatus));
                            }
                        });
                    } else {
                        // Check Reconnect Status
                        String fanucIP = PreferenceManager.getFanucIP();
                        int fanucPort = PreferenceManager.getFanucPort();
                        boolean needToConnectFanuc = false;
                        if (modbusClientFanuc != null && modbusClientFanuc.isConnected()) {
                            // Check changed status; If Setting was changed, then try to reconnect
                            if (!fanucIP.equals(currFanucIP) || fanucPort != currFanucPort) {
                                disconnectFanuc();

                                needToConnectFanuc = true;
                            }
                        } else {
                            needToConnectFanuc = true;
                        }

                        if (needToConnectFanuc) {
                            if (Utils.isValidIP(fanucIP) && Utils.isValidPort(fanucPort)) {
                                connectFanuc();
                            }
                        }

                        if (modbusClientFanuc != null && modbusClientFanuc.isConnected()) {

                            try {
                            /*
                            int[] optimizes = ModbusClient.ConvertFloatToTwoRegisters(0);
                            optimizes = ModbusClient.ConvertFloatToTwoRegisters(1);         //[0, 16256]
                            int[] tools = ModbusClient.ConvertFloatToTwoRegisters(10);      //[0, 16672]
                            int[] sections = ModbusClient.ConvertFloatToTwoRegisters(20);   //[0, 16800]
                            int[] channels = ModbusClient.ConvertFloatToTwoRegisters(3);    //[0, 16448]
                            int[] progNums = ModbusClient.ConvertFloatToTwoRegisters(5);    //[0, 16544]
                            */

                                int[] holdingFanucRegs = modbusClientFanuc.ReadHoldingRegisters(1000, 32);
                                int[] valueRegs = new int[2];

                                // In case of MonitorSignalFrom != 0, use PLC monitor signal and manual TCS values.
                                if (PreferenceManager.getMonitorSignalFrom() == PreferenceManager.MONITOR_SIGNAL_FROM_FANUC) {
                                    // Optimize 1000
                                    valueRegs[0] = holdingFanucRegs[0];
                                    valueRegs[1] = holdingFanucRegs[1];
                                    uiVars.Optimize = (int) ModbusClient.ConvertRegistersToFloat(valueRegs);

                                    // Tool 1002
                                    valueRegs[0] = holdingFanucRegs[2];
                                    valueRegs[1] = holdingFanucRegs[3];
                                    uiVars.Tool = (int) ModbusClient.ConvertRegistersToFloat(valueRegs);

                                    // Section 1004
                                    valueRegs[0] = holdingFanucRegs[4];
                                    valueRegs[1] = holdingFanucRegs[5];
                                    uiVars.Section = (int) ModbusClient.ConvertRegistersToFloat(valueRegs);

                                    // Channel 1006
                                    valueRegs[0] = holdingFanucRegs[6];
                                    valueRegs[1] = holdingFanucRegs[7];
                                    uiVars.Channel = (int) ModbusClient.ConvertRegistersToFloat(valueRegs);
                                }

                                // Program Number
                                // Channel 1008
                                valueRegs[0] = holdingFanucRegs[8];
                                valueRegs[1] = holdingFanucRegs[9];
                                uiVars.ProgramNumber = (int) ModbusClient.ConvertRegistersToFloat(valueRegs);

                                // InCycle Status
                                valueRegs[0] = holdingFanucRegs[10];
                                valueRegs[1] = holdingFanucRegs[11];
                                uiVars.InCycle = (int) ModbusClient.ConvertRegistersToFloat(valueRegs);

                                // Fanuc FeedHold Status
                                valueRegs[0] = holdingFanucRegs[12];
                                valueRegs[1] = holdingFanucRegs[13];
                                uiVars.FeedHoldStatus = (int) ModbusClient.ConvertRegistersToFloat(valueRegs);

                                // Fanuc Machine Connect Status
                                int fanucConnectStatus = 0;
                                valueRegs[0] = holdingFanucRegs[14];
                                valueRegs[1] = holdingFanucRegs[15];
                                fanucConnectStatus = (int) ModbusClient.ConvertRegistersToFloat(valueRegs);

                                if (fanucConnectStatus != uiVars.machineConnectStatus) {
                                    uiVars.machineConnectStatus = fanucConnectStatus;
                                    updateConnectStatus();
                                }

                                valueRegs[0] = holdingFanucRegs[16];
                                valueRegs[1] = holdingFanucRegs[17];
                                uiVars.currentSequenceNumber = (int) ModbusClient.ConvertRegistersToFloat(valueRegs);

                                // Read T Modal Data
                                uiVars.tModal.dataNo = holdingFanucRegs[18];
                                uiVars.tModal.type = holdingFanucRegs[19];
                                uiVars.tModal.gData = holdingFanucRegs[20];

                                // Read S Modal Data
                                uiVars.sModal.dataNo = holdingFanucRegs[21];
                                uiVars.sModal.type = holdingFanucRegs[22];
                                uiVars.sModal.gData = holdingFanucRegs[23];

                                // Read F Modal Data
                                uiVars.fModal.dataNo = holdingFanucRegs[24];
                                uiVars.fModal.type = holdingFanucRegs[25];
                                valueRegs[0] = holdingFanucRegs[26];
                                valueRegs[1] = holdingFanucRegs[27];
                                uiVars.fModal.gData = ModbusClient.ConvertRegistersToFloat(valueRegs);

                                // Read M Modal Data
                                uiVars.mModal.dataNo = holdingFanucRegs[28];
                                uiVars.mModal.type = holdingFanucRegs[29];
                                uiVars.mModal.gData = holdingFanucRegs[30];

                                if (uiVars.ProgramNumber == 0 && uiVars.Tool == 0 && uiVars.Section == 0 && uiVars.Channel == 0) {
                                    // * Exception of Modbus Reading
                                    // Check Modbus module
                                /*Platform.runLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.message("Fanuc values are all 0!");
                                    }
                                });*/
                                    LogManager.getInstance().addNewLog("Fanuc Values are 0!", isTeachMode(), isMonitorOn());
                                } else {
                                    Platform.runLater(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (uiVars.ProgramNumber > 0 || !uiVars.isManualProgramNumber) {
                                                valueProgNum.setText(String.valueOf(uiVars.ProgramNumber));
                                                uiVars.isManualProgramNumber = false;
                                            }

                                            // In case of MonitorSignalFrom != 0, use PLC monitor signal and manual TCS values.
                                            if (PreferenceManager.getMonitorSignalFrom() == PreferenceManager.MONITOR_SIGNAL_FROM_FANUC) {
                                                valueTool.setText(String.valueOf(uiVars.Tool));
                                                valueSection.setText(String.valueOf(uiVars.Section));
                                                valueChannel.setText(String.valueOf(uiVars.Channel));

                                                valueOptimize.setText(String.valueOf(uiVars.Optimize));
                                                btnTitleMonitor.setText(uiVars.Optimize > 0 ? "MONITOR \nON" : "MONITOR \nOFF");
                                            }

                                            valueFeedHoldStatus.setText(String.valueOf(uiVars.FeedHoldStatus));

                                            // * If Tool Data File does not exist, create a new one
                                            // * We use this code
                                        /*if (uiVars.ProgramNumber > 0 && !JobDataManager.getInstance().isFileExists(uiVars.ProgramNumber)) {

                                            JobData newItem = new JobData();
                                            newItem.tool = MainView.this.uiVars.Tool;
                                            newItem.section = MainView.this.uiVars.Section;
                                            newItem.channel = MainView.this.uiVars.Channel;

                                            JobDataManager.getInstance().createNewJobData(MainView.this.uiVars.ProgramNumber, newItem);
                                        }*/
                                        }
                                    });
                                }
                            } catch (ModbusException modbusException) {
                                modbusException.printStackTrace();
                                LogManager.getInstance().addNewLog("Excpt1:" + modbusException.getMessage(), isTeachMode(), isMonitorOn());

                                disconnectFanuc();
                            } catch (IOException ioException) {
                                ioException.printStackTrace();
                                LogManager.getInstance().addNewLog("Excpt1:" + ioException.getMessage(), isTeachMode(), isMonitorOn());

                                disconnectFanuc();
                            }
                        }
                    }

                    try {
                        Thread.sleep(PreferenceManager.getDataCycle());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        threadReadFanuc.setDaemon(true);
        threadReadFanuc.start();

        // PLC Data read thread
        threadReadPLC = new Thread(new Runnable() {
            @Override
            public void run() {

                System.out.println("Start Reading PLC!!!");
                LogManager.getInstance().addNewLog("Start Reading PLC!!!", isTeachMode(), isMonitorOn());

                while (!shutdownModbusClients) {

                    // try to connect PLC

                    // Check Reconnect Status
                    String plcIP = PreferenceManager.getPLCIP();
                    int plcPort = PreferenceManager.getPLCPort();
                    boolean needToConnectPLC = false;
                    if (modbusClientPLC != null && modbusClientPLC.isConnected()) {
                        // Check changed status; If Setting was changed, then try to reconnect
                        if (!plcIP.equals(currPLCIP) || plcPort != currPLCPort) {
                            disconnectPLC();
                            needToConnectPLC = true;
                        }
                    } else {
                        needToConnectPLC = true;
                    }

                    if (needToConnectPLC) {
                        if (Utils.isValidIP(plcIP) && Utils.isValidPort(plcPort)) {
                            connectPLC();
                        }
                    }

                    // * In case of PLC Simulator Enabled, then turn off PLC modbus communication totally
                    if (!PreferenceManager.isPLCSimulatorEnabled() && isPLCConnected()) {
                        try {

                            // *Now we disable the following logic and function block,
                            // *instead of using PLC alarm, we use TOS internal Alarm to send signals.
                            // in teach mode plc -> target high
                            // not teach mode
                            // Read Coils
                            /*
                            boolean[] coilPLCRegs = modbusClientPLC.ReadCoils(5, 3);
                            boolean newMacroInterruptSignal = coilPLCRegs[0];              // 6
                            boolean newToolWearSignal = coilPLCRegs[1];                    // 7

                            // Changed Macro Interrupt Signal
                            if (uiVars.MacroInterruptSignal != newMacroInterruptSignal) {
                                uiVars.MacroInterruptSignal = newMacroInterruptSignal;
                                feedbackMacroInterruptSignalToFanuc(newMacroInterruptSignal);
                            }

                            // Change Tool Wear Signal
                            if (uiVars.ToolWearSignal != newToolWearSignal) {
                                uiVars.ToolWearSignal = newToolWearSignal;

                                if (newToolWearSignal) {
                                    feedbackToolWearSignalToFanuc(newToolWearSignal);
                                }
                            }
                            */

                            // Read Holding Registers
                            int[] holdingPLCRegs = modbusClientPLC.ReadHoldingRegisters(0, 50);
                            if (isValidPLCData(holdingPLCRegs)) {

                                // * Get following values to validate the modbus reading
                                float target = holdingPLCRegs[1] / 100.0f;
                                float highLimit = holdingPLCRegs[2] / 100.0f;
                                float wearLimit = holdingPLCRegs[3] / 100.0f;
                                float idle = holdingPLCRegs[34] / 100.0f;
                                float feedrate = holdingPLCRegs[35] / 100.0f;

                                // From Version 3.68, we use one value of (tool.section * 100)
                                //int plcMonitor = holdingPLCRegs[40];
                                //int plcTool = holdingPLCRegs[41];
                                //int plcSection = holdingPLCRegs[42];
                                //int plcChannel = holdingPLCRegs[43];

                                int plcProgramNumber = holdingPLCRegs[44];

                                // New logic
                                int MTS = holdingPLCRegs[40];

                                /*String procValLog = String.format("Process Values: %f, %f, %f, %f, %f", holdingPLCRegs[0] / 100.0f, highLimit, wearLimit, idle, feedrate);
                                System.out.println(procValLog);
                                LogManager.getInstance().addNewLog(procValLog, isTeachMode(), isMonitorOn());*/

                                if (target == 0 && highLimit == 0 && wearLimit == 0 && idle == 0 && feedrate == 0) {
                                    // * Modbus Exception, check module
                                    /*Platform.runLater(new Runnable() {
                                        @Override
                                        public void run() {
                                            //Toast.message("Process Values are all 0!" + holdingPLCRegs[0] / 100.0f);
                                            //AlertManager.getInstance().showAlert("PLC Data", "Process Values are all 0!");
                                        }
                                    });*/
                                    LogManager.getInstance().addNewLog("Process Values are 0!", isTeachMode(), isMonitorOn());
                                } else {
                                    uiVars.HP = holdingPLCRegs[0] / 100.0f;                 // Realtime

                                    uiVars.Target = target;
                                    uiVars.HighLimit = highLimit;
                                    uiVars.WearLimit = wearLimit;

                                    float sensorScale = holdingPLCRegs[5] / 100.0f;         // Realtime
                                    if (sensorScale > 0) {
                                        // Check new sensor scale
                                        if (uiVars.SensorScale != sensorScale) {
                                            uiVars.SensorScale = sensorScale;
                                            feedbackPVMinMaxValueToFanuc(sensorScale, 0);
                                        }
                                    }

                                    uiVars.LowLimit = holdingPLCRegs[4];                    // Realtime :
                                    uiVars.LowLimitTimer = holdingPLCRegs[8];               // Realtime : We don't apply 100 for float value for the time

                                    uiVars.LeadInTrigger = holdingPLCRegs[22] / 100.0f;
                                    uiVars.Idle = idle;
                                    uiVars.Feedrate = feedrate;                             // Realtime

                                    // Alarm Status
                                    boolean newAlarm = false;
                                    int alarmState = holdingPLCRegs[38];
                                    if (alarmState != uiVars.Alarm) {
                                        uiVars.Alarm = alarmState;
                                        newAlarm = true;
                                    }
                                    boolean finalNewAlarm = newAlarm;

                                    Platform.runLater(new Runnable() {
                                        @Override
                                        public void run() {

                                            // In case of MonitorSignalFrom = 1, use PLC monitor signal and manual TCS values.
                                            if (PreferenceManager.getControlType() < PreferenceManager.CONTROL_TYPE_FANUC_HAAS &&               // Means Fanuc Series
                                                    PreferenceManager.getMonitorSignalFrom() >= PreferenceManager.MONITOR_SIGNAL_FROM_MANUAL) { // Means Manual Or PLC

                                                int plcMonitor = 0;
                                                int plcTool = 0;
                                                int plcSection = 0;
                                                int plcChannel = 0;     // We will not use this
                                                if (MTS > 0) {
                                                    plcMonitor = 1;
                                                    String str = String.format("%.2f", (float) MTS / 100.0f);
                                                    String[] tsValues = str.split("\\.");
                                                    plcTool = parseIntValue(tsValues[0]);
                                                    plcSection = parseIntValue(tsValues[1]);
                                                }

                                                if( plcMonitor != uiVars.Optimize) {
                                                    uiVars.Optimize = plcMonitor;
                                                    valueOptimize.setText(String.valueOf(uiVars.Optimize));
                                                    btnTitleMonitor.setText(uiVars.Optimize > 0 ? "MONITOR \nON" : "MONITOR \nOFF");
                                                }

                                                if (PreferenceManager.getMonitorSignalFrom() == PreferenceManager.MONITOR_SIGNAL_FROM_PLC) {
                                                    uiVars.Tool = plcTool;
                                                    uiVars.Section = plcSection;
                                                    uiVars.Channel = plcChannel;
                                                    valueTool.setText(String.valueOf(uiVars.Tool));
                                                    valueSection.setText(String.valueOf(uiVars.Section));
                                                    valueChannel.setText(String.valueOf(uiVars.Channel));

                                                    if (plcProgramNumber > 0 || !uiVars.isManualProgramNumber) {
                                                        uiVars.ProgramNumber = plcProgramNumber;
                                                        valueProgNum.setText(String.valueOf(plcProgramNumber));
                                                        uiVars.isManualProgramNumber = false;
                                                    }
                                                }
                                            }

                                            // * Regardless of teachmode, should process 4 values
                                            valueHp.setText(String.format("%.2f", uiVars.HP));
                                            valueLowLimitTimer.setText(String.format("%.1f", uiVars.LowLimitTimer));
                                            valueFeedrate.setText(String.valueOf(uiVars.Feedrate) + "%");

                                            // Adjust Scales
                                            if (uiVars.SensorScale != 0) {
                                                if (sliderAdjust.getMax() != uiVars.SensorScale) {
                                                    sliderAdjust.setMax(uiVars.SensorScale);
                                                }
                                                applet.setChartScale(uiVars.SensorScale);
                                            }

                                            /*if (checkNewSensorScale) {
                                                readSensorScale();
                                            }*/

                                            // Update Status if new Alarm
                                            if (finalNewAlarm) {
                                                updateMachineStatus();

                                                // * Send email is only possible TeachMode=OFF
                                                if (!isTeachMode()) {
                                                    sendAlarmEmail(uiVars.Alarm);
                                                }
                                            }

                                            // In case of teachmode = 1, use PLC value
                                            if (isTeachMode()) {

                                                valueTarget.setText(String.format("%.2f", uiVars.Target));

                                                valueHighLimit.setText(String.format("%.2f", uiVars.HighLimit));
                                                valueWearLimit.setText(String.format("%.2f", uiVars.WearLimit));

                                                valueIdle.setText(String.format("%.2f", uiVars.Idle));
                                                valueLeadInTrigger.setText(String.format("%.2f", uiVars.LeadInTrigger));
                                                valueLowLimitTimer.setText(String.format("%.1f", uiVars.LowLimitTimer));

                                                valueLearnedLowLimitTimer.setText(String.format("%.1f", uiVars.LowLimit));

                                                // Update Slider with UI
                                                updateSliderWithUIValues();
                                            }
                                        }
                                    });
                                }
                            } else {
                                LogManager.getInstance().addNewLog("PLC readings are all 0!", isTeachMode(), isMonitorOn());

                                //disconnectPLC();
                                //connectPLC();
                            }
                        } catch (ModbusException modbusException) {
                            modbusException.printStackTrace();

                            disconnectPLC();
                            connectPLC();
                        } catch (IOException ioException) {
                            ioException.printStackTrace();

                            disconnectPLC();
                            connectPLC();
                        } catch (Exception e) {
                            e.printStackTrace();

                            disconnectPLC();
                            connectPLC();
                        }
                    } else {
                        // LogManager.getInstance().addNewLog("No Conn to PLC!!!", isTeachMode(), isMonitorOn());
                    }

                    // Sleep Cycle
                    try {
                        if (modbusClientPLC != null && modbusClientPLC.isConnected()) {
                            Thread.sleep(PreferenceManager.getDataCycle());
                        } else {
                            Thread.sleep(5000);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                System.out.println("Finish Reading PLC!!!");
            }
        });
        threadReadPLC.setDaemon(true);
        threadReadPLC.start();

        // PLC Data read thread
        threadRpi = new Thread(new Runnable() {
            @Override
            public void run() {

                System.out.println("Start Reading Rpi!!!");
                LogManager.getInstance().addNewLog("Start Reading Rpi!!!", isTeachMode(), isMonitorOn());

                while (!shutdownModbusClients) {

                    // try to connect PLC
                    if (PreferenceManager.isHP2Enabled()) {
                        // Check Reconnect Status
                        String rpiIP = PreferenceManager.getHP2IP();
                        int rpiPort = PreferenceManager.getHP2Port();
                        boolean needToConnectRpi = false;
                        if (modbusClientRpi != null && modbusClientRpi.isConnected()) {
                            // Check changed status; If Setting was changed, then try to reconnect
                            if (!rpiIP.equals(currRpiIP) || rpiPort != currRpiPort) {
                                disconnectRpi();
                                needToConnectRpi = true;
                            }
                        } else {
                            needToConnectRpi = true;
                        }

                        if (needToConnectRpi) {
                            if (Utils.isValidIP(rpiIP) && Utils.isValidPort(rpiPort)) {
                                connectRpi();
                            }
                        }

                        // * In case of PLC Simulator Enabled, then turn off PLC modbus communication totally
                        if (isRpiConnected()) {
                            try {
                                // Read Holding Registers
                                int[] holdingRpiRegs = modbusClientRpi.ReadHoldingRegisters(PreferenceManager.getHP2RegAddr(), 2);
                                float hp2 = ModbusClient.ConvertRegistersToFloat(holdingRpiRegs);
                                if (hp2 >= 0 && hp2 <= 20) {
                                    // Check Peak
                                    uiVars.HP2 = hp2;
                                    Platform.runLater(new Runnable() {
                                        @Override
                                        public void run() {
                                            valueHp2.setText(String.format("%.2f", uiVars.HP2));
                                        }
                                    });

                                    // We use this function in the Rpi Fanuc Bridge
                                    /*if (hp2 >= PreferenceManager.getHP2X()) {
                                        csvReportManager.writeData();
                                    }*/
                                }
                            } catch (ModbusException modbusException) {
                                modbusException.printStackTrace();

                                disconnectRpi();
                                connectRpi();
                            } catch (IOException ioException) {
                                ioException.printStackTrace();

                                disconnectRpi();
                                connectRpi();
                            } catch (Exception e) {
                                e.printStackTrace();

                                disconnectRpi();
                                connectRpi();
                            }
                        } else {
                            // LogManager.getInstance().addNewLog("No Conn to PLC!!!", isTeachMode(), isMonitorOn());
                        }


                    }

                    // Sleep Cycle
                    try {
                        if (modbusClientRpi != null && modbusClientRpi.isConnected()) {
                            Thread.sleep(PreferenceManager.getDataCycle());
                        } else {
                            Thread.sleep(5000);
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                System.out.println("Finish Reading PLC!!!");
            }
        });
        threadRpi.setDaemon(true);
        threadRpi.start();

        // Alarm Check Thread
        threadAlarmCheck = new Thread(new Runnable() {
            @Override
            public void run() {

                // Alarm Check Module
                while (!shutdownModbusClients) {
                    // We need to add the following alarm logic to TOS app. Currently the PLC is generating the alarms,
                    // but we need to also allow TOS app to generate alarms because then it will work with old hardware systems.
                    // These are the alarm conditions, logic and reaction, there are also pictures below to clearly show which vars we are using in TOS.
                    // System Alarm is reset WHEN:
                    // 1. User select reset button
                    // 2. When monitor changes from 0 to 1

                    int newSystemAlarmStatus = SYSTEM_ALARM_IDLE;

                    if (isMonitorOn()) {
                        newSystemAlarmStatus = SYSTEM_ALARM_MONITOR;
                        if (isAdaptiveOn()) {
                            newSystemAlarmStatus = SYSTEM_ALARM_MONITOR_ADAPTIVE;
                        }
                    }

                    JobData curJobData = null;
                    if (isMonitorOn() && !isTeachMode()) {
                        int jobID = getValueProgNum();
                        curJobData = JobDataManager.getInstance().getJobData(jobID, uiVars.Tool, uiVars.Section, uiVars.Channel);
                        if (curJobData != null) {

                            double feedrate = getValueFeedrate();
                            double highLimit = getValueHighLimit();
                            double wearLimit = getValueWearLimit();

                            // Wear Limit Alarms
                            /*Make change to high/wear limit logic when adaptive=1
                            Current logic= when adaptive=1 then High/Wear limit alarms are generated when feedrate<high/wear limit.
                            Want to give option for each TSC to choose this logic or traditional logic: High/Wear limit alarms generated if HP>limits.
                            Basically want to able to select which logic to use from the TSC data file, for each tool.
                            So need to add another field in tool data file, just a checkbox.
                            When save tool file, it should default to "traditional" logic.*/
                            if (!isAdaptiveOn() || curJobData.wearLogicFeedrate) {
                                if (uiVars.HP >= wearLimit) {
                                    wearLimitTimer.start();
                                } else {
                                    wearLimitTimer.reset();
                                }
                            } else {
                                if (feedrate < curJobData.adaptiveWearLimit) {
                                    wearLimitTimer.start();
                                } else {
                                    wearLimitTimer.reset();
                                }
                            }

                            // Wear Limit Alarm
                            // System Status = "Wear Limit Alarm"
                            // System Stats Box Color = Flashing Orange
                            // Logic =
                            // IF monitor=1 && teachMode=0 && adaptiveEnable=0 && hp > wearLimit &&
                            // wearLimitTimer > wearLimitDelay && elapsedTime > defaultStartDelay THEN
                            // macroInterrupt=0 && toolLifeWearSignal=1 && //status="Wear Limit Alarm"
                            // wearLimitTimer = ON WHEN hp > wearLimit
                            // wearLimitTimer = RESET WHEN hp < wearLimit
                            if ((!isAdaptiveOn() || curJobData.wearLogicFeedrate) &&
                                    uiVars.HP > wearLimit && wearLimitTimer.getTimeInSeconds() > curJobData.wearLimitDelay &&
                                    getValueElapsedMonitorTime() > curJobData.startDelay) {
                                newSystemAlarmStatus = SYSTEM_ALARM_WEAR_LIMIT;
                                //btnMacroInterruptEnable.setText("MACRO INTERRUPT\nOFF");
                                //feedbackToolWearSignalToFanuc(true);
                            }

                            // Wear Limit Alarm - Adaptive
                            // System Status = "Wear Limit Alarm - Adaptive"
                            // System Stats Box Color = Flashing Orange
                            // Logic =
                            // IF monitor=1 && teachMode=0 && adaptiveEnable=0 && feedrate < adaptiveWearLimit% &&
                            // wearLimitTimer > wearLimitDelay && elapsedTime > defaultStartDelay THEN
                            // macroInterrupt=0 && //toolLifeWearSignal=1 && status="Wear Limit Alarm - Adaptive"
                            // wearLimitTimer = ON WHEN feedrate < wearHighLimit%
                            // wearLimitTimer = RESET WHEN feedrate > wearHighLimit%
                            if ((isAdaptiveOn() && curJobData.wearLogicFeedrate == false) &&
                                    feedrate < curJobData.adaptiveWearLimit && wearLimitTimer.getTimeInSeconds() > curJobData.wearLimitDelay &&
                                    getValueElapsedMonitorTime() > curJobData.startDelay) {
                                newSystemAlarmStatus = SYSTEM_ALARM_WEAR_LIMIT_ADAPTIVE;
                                //btnMacroInterruptEnable.setText("MACRO INTERRUPT\nOFF");
                                //feedbackToolWearSignalToFanuc(true);
                            }

                            // We disable Low Limit Alarm temporarily
                            /*//Low Limit Alarm
                            double idle = getValueIdle();
                            double lowLimitError = PreferenceManager.getLowLimitErrorPercent();
                            double deadbandHigh = idle + idle * lowLimitError / 100.0;
                            double deadbandLow = idle - idle * lowLimitError / 100.0;
                            boolean isHPInsideDeadband = false;
                            if (uiVars.HP > deadbandLow && uiVars.HP < deadbandHigh) {
                                lowLimitTimer.start();
                                isHPInsideDeadband = true;
                            } else {
                                lowLimitTimer.reset();
                            }

                            // System Status = "Low Limit Alarm"
                            // System Stats Box Color = Flashing Blue
                            // Logic =
                            // IF monitor=1 && teachMode=0 && HP inside Deadband && lowLimitTimer > learnedLowLimitTime &&
                            // elapsedTime > defaultStartDelay && macroInterruptEnable=1 THEN
                            // macroInterrupt=1 && //toolLifeWearSignal=1 && status="Low Limit Alarm"
                            // lowLimitTimer = ON WHEN hp > (idle + (idle*(lowLimitError / 100))
                            // lowLimitTimer = ON WHEN hp < (idle - (idle*lowLimitError / 100))
                            // lowLimitTimer = RESET WHEN hp is outside of the above calculations
                            if (isHPInsideDeadband && lowLimitTimer.getTimeInSeconds() > getValueLearnedLowLimitTimer() &&
                                    getValueElapsedMonitorTime() > curJobData.startDelay) {
                                newSystemAlarmStatus = SYSTEM_ALARM_LOW_LIMIT;

                                //btnMacroInterruptEnable.setText("MACRO INTERRUPT\nON");
                                //feedbackToolWearSignalToFanuc(true);
                            }*/

                            // High Limit Alarms
                            if (!isAdaptiveOn()) {
                                if (uiVars.HP >= highLimit) {
                                    highLimitTimer.start();
                                } else {
                                    highLimitTimer.reset();
                                }
                            } else {
                                if (feedrate < curJobData.adaptiveHighLimit) {
                                    highLimitTimer.start();
                                } else {
                                    highLimitTimer.reset();
                                }
                            }

                            // High Limit Alarm
                            // System Status = "High Limit Alarm"
                            // System Stats Box Color = Flashing Red
                            // Logic =
                            // IF monitor=1 && teachMode=0 && adaptiveEnable=0 && hp > highLimit && highLimitTimer > highLimitDelay &&
                            // elapsedTime > defaultStartDelay && macroInterruptEnable=1 THEN
                            // macroInterrupt=1 && //toolLifeWearSignal=1 && status="High Limit Alarm"
                            // highLimitTimer = ON WHEN hp > highLimit
                            // highLimitTimer = RESET WHEN hp < highLimit
                            if (!isAdaptiveOn() && uiVars.HP > highLimit && highLimitTimer.getTimeInSeconds() > curJobData.highLimitDelay &&
                                    getValueElapsedMonitorTime() > curJobData.startDelay) {
                                newSystemAlarmStatus = SYSTEM_ALARM_HIGH_LIMIT;

                                //btnMacroInterruptEnable.setText("MACRO INTERRUPT\nON");
                                //feedbackToolWearSignalToFanuc(true);
                            }


                            // High Limit Alarm - Adaptive
                            // System Status = "High Limit Alarm - Adaptive"
                            // System Stats Box Color = Flashing Red
                            // Logic =
                            // IF monitor=1 && teachMode=0 && adaptiveEnable=1 && feedrate < adaptiverHighLimit% &&
                            // highLimitTimer > highLimitDelay && elapsedTime > defaultStartDelay && macroInterruptEnable=1 THEN
                            // macroInterrupt=1 && toolLifeWearSignal=1 && status="High Limit Alarm - Adaptive"
                            // highLimitTimer = ON WHEN feedrate < adaptiveHighLimit%
                            // highLimitTimer = RESET WHEN feedrate > adaptiveHighLimit%
                            if (isAdaptiveOn() && feedrate < curJobData.adaptiveHighLimit && highLimitTimer.getTimeInSeconds() > curJobData.highLimitDelay &&
                                    getValueElapsedMonitorTime() > curJobData.startDelay) {
                                newSystemAlarmStatus = SYSTEM_ALARM_HIGH_LIMIT_ADAPTIVE;

                                //btnMacroInterruptEnable.setText("MACRO INTERRUPT\nON");
                                //feedbackToolWearSignalToFanuc(true);
                            }
                        }
                    } else {
                        // If not the monitor=1 && teachmode=0, always reset time
                        highLimitTimer.reset();
                        wearLimitTimer.reset();
                        lowLimitTimer.reset();
                    }

                    if (currSystemAlarmStatus != newSystemAlarmStatus) {

                        // * All alarms should stay active until monitor change to 0
                        if (currSystemAlarmStatus == SYSTEM_ALARM_IDLE ||
                                newSystemAlarmStatus == SYSTEM_ALARM_IDLE ||
                                (newSystemAlarmStatus != SYSTEM_ALARM_MONITOR && newSystemAlarmStatus != SYSTEM_ALARM_MONITOR_ADAPTIVE)) {

                            currSystemAlarmStatus = newSystemAlarmStatus;

                            String newAlarmType = "";

                            if (currSystemAlarmStatus == SYSTEM_ALARM_IDLE) {
                                newAlarmType = "IDLE";
                            } else if (currSystemAlarmStatus == SYSTEM_ALARM_MONITOR) {
                                newAlarmType = "Monitoring";
                            } else if (currSystemAlarmStatus == SYSTEM_ALARM_MONITOR_ADAPTIVE) {
                                newAlarmType = "Monitoring - Adaptive";
                            } else if (currSystemAlarmStatus == SYSTEM_ALARM_HIGH_LIMIT) {
                                newAlarmType = "High Limit Alarm";
                            } else if (currSystemAlarmStatus == SYSTEM_ALARM_HIGH_LIMIT_ADAPTIVE) {
                                newAlarmType = "High Limit Alarm - Adaptive";
                            } else if (currSystemAlarmStatus == SYSTEM_ALARM_WEAR_LIMIT) {
                                newAlarmType = "Wear Limit Alarm";
                            } else if (currSystemAlarmStatus == SYSTEM_ALARM_WEAR_LIMIT_ADAPTIVE) {
                                newAlarmType = "Wear Limit Alarm - Adaptive";
                            } else if (currSystemAlarmStatus == SYSTEM_ALARM_LOW_LIMIT) {
                                newAlarmType = "Low Limit Alarm";
                            }

                            String finalNewAlarmType = newAlarmType;
                            JobData finalCurJobData = curJobData;
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {

                                    labelStatusBar.setText(finalNewAlarmType);

                                    if (currSystemAlarmStatus == SYSTEM_ALARM_IDLE) {
                                        timelineAnimAlarmStatus.pause();
                                        labelStatusBar.setStyle("-fx-border-color: white;-fx-text-fill: white;");
                                    } else {
                                        timelineAnimAlarmStatus.play();
                                    }

                                    // Alarm Output logic for macro interrupt:
                                    // if High Limit Alarm = 1 && macroInterruptEnable =1, then Fanuc WearSignal and MacroInterrupt = 1
                                    // if High Limit Alarm = 1 && macroInterruptEnable =0, then Fanuc WearSignal = 1 and MacroInterrupt = 0

                                    if (currSystemAlarmStatus == SYSTEM_ALARM_HIGH_LIMIT || currSystemAlarmStatus == SYSTEM_ALARM_HIGH_LIMIT_ADAPTIVE) {
                                        feedbackToolWearSignalToFanuc(true);
                                        feedbackMacroInterruptSignalToFanuc(isMacroInterruptOn());
                                    } else if (currSystemAlarmStatus == SYSTEM_ALARM_WEAR_LIMIT || currSystemAlarmStatus == SYSTEM_ALARM_WEAR_LIMIT_ADAPTIVE) {
                                        feedbackToolWearSignalToFanuc(true);
                                        feedbackMacroInterruptSignalToFanuc(false);
                                    } else if (currSystemAlarmStatus == SYSTEM_ALARM_LOW_LIMIT) {
                                        // feedbackToolWearSignalToFanuc(true);
                                        // feedbackMacroInterruptSignalToFanuc(true);
                                    }
                                }
                            });

                            if (currSystemAlarmStatus == SYSTEM_ALARM_HIGH_LIMIT ||
                                    currSystemAlarmStatus == SYSTEM_ALARM_HIGH_LIMIT_ADAPTIVE ||
                                    currSystemAlarmStatus == SYSTEM_ALARM_WEAR_LIMIT ||
                                    currSystemAlarmStatus == SYSTEM_ALARM_WEAR_LIMIT_ADAPTIVE ||
                                    currSystemAlarmStatus == SYSTEM_ALARM_LOW_LIMIT) {

                                // * Send email is only possible TeachMode=OFF
                                if (!isTeachMode()) {
                                    sendSystemAlarmEmail();
                                }

                                // Save on Local DB and Report Current Alarm
                                int progNum = getValueProgNum();
                                int tool = getValueTool();
                                int section = getValueSection();
                                int channel = getValueChannel();

                                float elapsedTime = (float) getValueElapsedMonitorTime();

                                Date currTime = new Date();
                                String valueDate1 = DateTimeUtils.formatDate(currTime, "MM/dd/yyyy");
                                String valueDate2 = DateTimeUtils.formatDate(currTime, "yyyy-MM-dd");
                                String valueTime = DateTimeUtils.formatDate(currTime, "HH:mm:ss");

                                SystemAlertManager.getInstance().addNewLog(valueDate1, valueTime, progNum, tool, section, channel, newAlarmType, elapsedTime);

                                String factoryID = PreferenceManager.getFactoryID();
                                String machineID = PreferenceManager.getMachineID();

                                if (!TextUtils.isEmpty(factoryID) && !TextUtils.isEmpty(machineID)) {
                                    HttpPost post = new HttpPost(Api.SERVE_URL + Api.api_systemAlarm);

                                    // add request parameters or form parameters
                                    List<NameValuePair> urlParameters = new ArrayList<>();
                                    urlParameters.add(new BasicNameValuePair("customerId", factoryID));
                                    urlParameters.add(new BasicNameValuePair("machineId", machineID));
                                    urlParameters.add(new BasicNameValuePair("date", valueDate2));
                                    urlParameters.add(new BasicNameValuePair("time", valueTime));
                                    urlParameters.add(new BasicNameValuePair("progNum", String.valueOf(progNum)));
                                    urlParameters.add(new BasicNameValuePair("tool", String.valueOf(tool)));
                                    urlParameters.add(new BasicNameValuePair("section", String.valueOf(section)));
                                    urlParameters.add(new BasicNameValuePair("channel", String.valueOf(channel)));
                                    urlParameters.add(new BasicNameValuePair("alarmType", newAlarmType));
                                    urlParameters.add(new BasicNameValuePair("elapsedTime", String.valueOf(elapsedTime)));

                                    try {
                                        post.setEntity(new UrlEncodedFormEntity(urlParameters));
                                        HttpClient httpClient = HttpClientBuilder.create().build();
                                        HttpResponse result = httpClient.execute(post);
                                        String response = EntityUtils.toString(result.getEntity(), "UTF-8");
                                        System.out.println(response);
                                    } catch (Exception e) {
                                        e.printStackTrace();

                                        Platform.runLater(() -> {
                                            Toast.message("Fail to upload system alarm info!");
                                        });
                                    }
                                }
                            }
                        }
                    }

                    /*Platform.runLater(new Runnable() {
                        @Override
                        public void run() {

                            valueInnerTimers.setText(String.format("High Timer : %.1f\nWear Timer : %.1f\nLow Timer : %.1f",
                                    highLimitTimer.getTimeInSeconds(), wearLimitTimer.getTimeInSeconds(), lowLimitTimer.getTimeInSeconds()));
                            valueSendSignals.setText(String.format("MacroIntrpt : %d\nToolWear : %d",
                                    uiVars.fanucMacroInterruptSignal ? 1 : 0, uiVars.fanucToolWearSignal ? 1 : 0));
                        }
                    });*/

                    // Sleep Cycle
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        threadAlarmCheck.setDaemon(true);
        threadAlarmCheck.start();
    }

    private void updateConnectStatus() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                if (modbusClientFanuc != null && modbusClientFanuc.isConnected()) {

                    if (uiVars.machineConnectStatus == 1) {
                        ivCNCCtrlConnStatus.setImage(imageStatusOn);
                    } else {
                        ivCNCCtrlConnStatus.setImage(imageStatusSemi);
                    }
                    PreferenceManager.setFanucConnectionStatus(true);
                } else {
                    ivCNCCtrlConnStatus.setImage(imageStatusOff);

                    PreferenceManager.setFanucConnectionStatus(false);
                }

                if (PreferenceManager.isPLCSimulatorEnabled() || (modbusClientPLC != null && modbusClientPLC.isConnected())) {
                    ivTOSCtrlConnStatus.setImage(imageStatusOn);
                } else {
                    ivTOSCtrlConnStatus.setImage(imageStatusOff);
                }
            }
        });
    }

    private void disconnectFanuc() {
        try {
            if (modbusClientFanuc != null) {
                modbusClientFanuc.Disconnect();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        updateConnectStatus();
    }

    private void connectFanuc() {
        // try to connect Fanuc
        String fanucIP = PreferenceManager.getFanucIP();
        int fanucPort = PreferenceManager.getFanucPort();

        if (Utils.isValidIP(fanucIP) && Utils.isValidPort(fanucPort)) {
            try {
                modbusClientFanuc = new ModbusClient(fanucIP, fanucPort);
                modbusClientFanuc.Connect();

                currFanucIP = fanucIP;
                currFanucPort = fanucPort;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        updateConnectStatus();
    }

    private ModbusClient getNewFanucConn() {
        // try to connect Fanuc
        String fanucIP = PreferenceManager.getFanucIP();
        int fanucPort = PreferenceManager.getFanucPort();

        ModbusClient newFanucConn = null;
        if (Utils.isValidIP(fanucIP) && Utils.isValidPort(fanucPort)) {
            try {
                newFanucConn = new ModbusClient(fanucIP, fanucPort);
                newFanucConn.Connect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return newFanucConn;
    }

    private void disconnectPLC() {
        try {
            if (modbusClientPLC != null) {
                modbusClientPLC.Disconnect();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        updateConnectStatus();
    }

    private void connectPLC() {
        // try to connect PLC
        String plcIP = PreferenceManager.getPLCIP();
        int plcPort = PreferenceManager.getPLCPort();
        if (Utils.isValidIP(plcIP) && Utils.isValidPort(plcPort)) {
            try {
                modbusClientPLC = new ModbusClient(plcIP, plcPort);
                modbusClientPLC.Connect();

                currPLCIP = plcIP;
                currPLCPort = plcPort;
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

        updateConnectStatus();
    }

    private ModbusClient getNewPLCConn() {
        if (!isPLCConnected()) {
            return null;
        }

        // try to connect PLC
        String plcIP = PreferenceManager.getPLCIP();
        int plcPort = PreferenceManager.getPLCPort();

        ModbusClient newPLCConn = null;
        if (Utils.isValidIP(plcIP) && Utils.isValidPort(plcPort)) {
            try {
                newPLCConn = new ModbusClient(plcIP, plcPort);
                newPLCConn.Connect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (newPLCConn != null && !newPLCConn.isConnected()) {
            try {
                newPLCConn.Disconnect();
                newPLCConn = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return newPLCConn;
    }

    private boolean isFanucConnected() {
        if (modbusClientFanuc != null && modbusClientFanuc.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isPLCConnected() {
        if (modbusClientPLC != null && modbusClientPLC.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    private boolean isRpiConnected() {
        if (modbusClientRpi != null && modbusClientRpi.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    private void disconnectRpi() {
        try {
            if (modbusClientRpi != null) {
                modbusClientRpi.Disconnect();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void connectRpi() {
        // try to connect PLC
        String rpiIP = PreferenceManager.getHP2IP();
        int rpiPort = PreferenceManager.getHP2Port();
        if (Utils.isValidIP(rpiIP) && Utils.isValidPort(rpiPort)) {
            try {
                modbusClientRpi = new ModbusClient(rpiIP, rpiPort);
                modbusClientRpi.Connect();

                currRpiIP = rpiIP;
                currRpiPort = rpiPort;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (modbusClientRpi != null && !modbusClientRpi.isConnected()) {
            try {
                modbusClientRpi.Disconnect();
                modbusClientRpi = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    boolean checkPegs(int array[]) {
        int size = array.length;
        for (int i = 0; i < size; i++) {
            if (array[i] != 0) {
                return false;
            }
        }
        return true;
    }

    public BorderPane getView() {
        return main_pane;
    }

    public void terminate() {
        // Cancel Auto Connect Process
        shutdownModbusClients = true;

        // Cancel HAAS Monitor
        if (ethernetHaasMonitor != null) {
            ethernetHaasMonitor.stop();
        }

        // Stop Timer
        if (timerStopWatcher != null) {
            timerStopWatcher.stop();
        }

        // Disconnect Fanuc
        disconnectFanuc();

        // Disconnect PLC
        disconnectPLC();

        // Close HST FIle Save Manager and Make it As Free
        if (hstFileSaveManager != null) {
            hstFileSaveManager.finishWriting();
        }

        if (csvReportManager != null) {
            csvReportManager.finishWriting();
        }
    }

    private HBox getTitleMenuBox() {
        HBox hTopMenuBox = new HBox();
        hTopMenuBox.setAlignment(Pos.CENTER);
        hTopMenuBox.setSpacing(10);
        hTopMenuBox.setPadding(new Insets(15, 15, 15, 15));

        btnNavMenu = ViewHelper.makeMenuIconButton("", Resource.MENU_ICON_SIZE, 5, LogoManager.getInstance().getMenuIcon(), "Menu");
        btnKeylock = ViewHelper.makeMenuIconButton("", Resource.MENU_ICON_SIZE, 5, LogoManager.getInstance().getKeylockIcon(), "Lock Tools");
        btnKeylock.setText("OFF");
        btnKeylock.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                if (btnKeylock.getText().equals("OFF")) {
                    boxHomeSliders.setVisible(false);

                    vBoxSaveVars.setVisible(false);
                    btnKeylock.setText("ON");
                } else {
                    boxHomeSliders.setVisible(true);

                    vBoxSaveVars.setVisible(true);
                    btnKeylock.setText("OFF");
                }
            }
        });
        btnReloadTool = ViewHelper.makeMenuIconButton("Reload Tool", Resource.MENU_ICON_SIZE, 5, LogoManager.getInstance().getArrowDownIcon(), "");
        btnReloadTool.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (isTeachMode()) {
                    Toast.message("App is now in Teach Mode.");
                } else {
                    reloadToolData();
                }
            }
        });
        btnReset = ViewHelper.makeMenuIconButton("Reset Alarm", Resource.MENU_ICON_SIZE, 5, LogoManager.getInstance().getAlertIcon(), "");
        btnReset.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Send Reset Alarm to PLC!");

                if (USE_PLC_ALARM) {
                    // Send Alarm Reset to PLC
                    ModbusClient modbusClient = getNewPLCConn();
                    if (modbusClient != null && modbusClient.isConnected()) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    modbusClient.WriteSingleRegister(38, 0);

                                    modbusClient.Disconnect();
                                } catch (ModbusException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }
                } else {
                    resetAlarm();
                }
            }
        });

        //btnOpenToolDataFile = ViewHelper.makeMenuButton("Open Tool Data File", 10, "");
        btnOpenToolDataFile = new Button("Open Tool Data File");
        btnOpenToolDataFile.setAlignment(Pos.CENTER);
        btnOpenToolDataFile.getStyleClass().add("button-gradient5");
        //btnOpenToolDataFile.setPrefWidth(120);
        btnOpenToolDataFile.setPrefHeight(30);
        //btnOpenToolDataFile.setMaxWidth(120);
        //btnOpenToolDataFile.setMinWidth(120);
        btnOpenToolDataFile.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ToolDataView.getInstance().show();
            }
        });

        btnExit = ViewHelper.makeMenuIconButton("", Resource.MENU_ICON_SIZE, 5, LogoManager.getInstance().getExitIcon(), "Exit");
        btnExit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Stage stage = (Stage) btnExit.getScene().getWindow();
                stage.close();
            }
        });

        hTopMenuBox.getChildren().addAll(btnNavMenu, btnKeylock, btnReloadTool, btnReset, btnOpenToolDataFile);
        Region left1Region = new Region();
        HBox.setHgrow(left1Region, Priority.ALWAYS);
        Region left2Region = new Region();
        HBox.setHgrow(left2Region, Priority.ALWAYS);
        Region right1Region = new Region();
        HBox.setHgrow(right1Region, Priority.ALWAYS);
        Region right2Region = new Region();
        HBox.setHgrow(right2Region, Priority.ALWAYS);
        Region right3Region = new Region();
        HBox.setHgrow(right3Region, Priority.ALWAYS);

        ImageView topLogo = new ImageView();
        topLogo.setImage(LogoManager.getInstance().getBannerLogo());
        topLogo.setFitWidth(250);
        topLogo.setPreserveRatio(true);
        topLogo.setSmooth(true);
        topLogo.setCache(true);

        // Device Connect Status
        imageStatusOn = LogoManager.getInstance().getStatusOnIcon();
        imageStatusOff = LogoManager.getInstance().getStatusOffIcon();
        imageStatusSemi = LogoManager.getInstance().getStatusSemiIcon();

        VBox vBoxConnStatus = new VBox(5);
        HBox hBoxFanucCtrlStatus = new HBox(5);
        hBoxFanucCtrlStatus.setAlignment(Pos.CENTER_LEFT);
        ivCNCCtrlConnStatus = new ImageView();
        ivCNCCtrlConnStatus.setFitWidth(16);
        ivCNCCtrlConnStatus.setFitHeight(16);
        ivCNCCtrlConnStatus.setImage(imageStatusOff);
        Label labelTOSController = new Label("CNC Control");
        labelTOSController.setFont(Font.font("Tahoma", FontWeight.BOLD, 12));
        labelTOSController.setTextFill(Color.WHITE);
        hBoxFanucCtrlStatus.getChildren().addAll(ivCNCCtrlConnStatus, labelTOSController);

        HBox hBoxPLCCtrlStatus = new HBox(5);
        hBoxPLCCtrlStatus.setAlignment(Pos.CENTER_LEFT);
        ivTOSCtrlConnStatus = new ImageView();
        ivTOSCtrlConnStatus.setFitWidth(16);
        ivTOSCtrlConnStatus.setFitHeight(16);
        ivTOSCtrlConnStatus.setImage(imageStatusOff);
        Label labelPLCController = new Label("TOS Controller");
        labelPLCController.setFont(Font.font("Tahoma", FontWeight.BOLD, 12));
        labelPLCController.setTextFill(Color.WHITE);
        hBoxPLCCtrlStatus.getChildren().addAll(ivTOSCtrlConnStatus, labelPLCController);

        vBoxConnStatus.getChildren().addAll(hBoxFanucCtrlStatus, hBoxPLCCtrlStatus);

        // Machine Name
        labelMachine = new Label(PreferenceManager.getMachineID());
        labelMachine.setPadding(new Insets(7, 10, 7, 10));
        labelMachine.setId("label-devicename");
        labelMachine.setFont(ViewHelper.getInstance().getFontInformation());
        labelMachine.setTextFill(Color.WHITE);
        //labelMachine.setTooltip(ViewHelper.makeNormalTooltip("Machine Name"));
        updateMachineInfo();

        // Status Bar
        timelineAnimAlarmStatus.setCycleCount(Timeline.INDEFINITE);
        timelineAnimAlarmStatus.setAutoReverse(false);

        labelStatusBar = new Label("Idle");
        labelStatusBar.setPadding(new Insets(7, 10, 7, 10));
        labelStatusBar.setId("label-StatusBar");
        labelStatusBar.setMinWidth(100);
        labelStatusBar.setTextAlignment(TextAlignment.CENTER);
        labelStatusBar.setFont(ViewHelper.getInstance().getFontInformation());
        labelStatusBar.setTextFill(Color.WHITE);
        //labelStatusBar.setTooltip(ViewHelper.makeNormalTooltip("Status"));

//        // Current Time
//        labelTimer = new Label(DateTimeUtils.getCurrentTimeString());
//        labelTimer.setPadding(new Insets(10, 10, 10, 10));
//        labelTimer.setId("label-clock");
//        labelTimer.setFont(ViewHelper.getInstance().getFontInformation());
//        labelTimer.setTextFill(Color.WHITE);
//        labelTimer.setTooltip(ViewHelper.makeNormalTooltip("Current Time"));

        hTopMenuBox.getChildren().addAll(left1Region/*, left2Region*/);
        hTopMenuBox.getChildren().add(topLogo);
        hTopMenuBox.getChildren().addAll(right1Region, right2Region/*, right3Region*/);
        hTopMenuBox.getChildren().add(new Group(vBoxConnStatus));
        hTopMenuBox.getChildren().addAll(labelMachine, labelStatusBar);
        //box.getChildren().add(btnExit);

        return hTopMenuBox;
    }

    public HBox getToolbarBox() {
        HBox hbox = new HBox();
        hbox.setAlignment(Pos.CENTER);
        hbox.setSpacing(15);
        hbox.setPadding(new Insets(10, 15, 10, 15));

        // Job ID
        vBoxProgNumber = new VBox();
        vBoxProgNumber.setSpacing(2);
        vBoxProgNumber.setPadding(new Insets(1, 3, 1, 3));
        vBoxProgNumber.setAlignment(Pos.CENTER);
        vBoxProgNumber.getStyleClass().add("vbox-info");
        vBoxProgNumber.setPrefWidth(150);
        vBoxProgNumber.setMaxWidth(250);
        vBoxProgNumber.setMinWidth(100);
        HBox.setHgrow(vBoxProgNumber, Priority.SOMETIMES);
        hbox.getChildren().addAll(vBoxProgNumber);
        vBoxProgNumber.setOnMouseClicked(viewHandler);


        titleProgNum = new Label("PROGRAM NUMBER");
        titleProgNum.getStyleClass().add("label-info-title");
        titleProgNum.setFont(ViewHelper.getInstance().getFontInformation());
        titleProgNum.setTextFill(Color.WHITE);

        valueProgNum = new Label(String.valueOf(MainView.this.uiVars.ProgramNumber));
        valueProgNum.getStyleClass().add("label-info-value");
        valueProgNum.setFont(ViewHelper.getInstance().getFontInformation());
        valueProgNum.setTextFill(Color.WHITE);
        //valueProgNum.setTooltip(ViewHelper.makeNormalTooltip("JOB ID"));

        valueProgNum.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {

                // Refresh Tool Data View Contents
                // * In case of Fanuc Program number was changed, then should reload Tool Data
                // * In case of manual input of prog number, no need to reload, so we use following condition
                if (uiVars.ProgramNumber > 0) {
                    ToolDataView.getInstance().refreshToolData();
                }

                // Reload Tool File Data
                if (!isTeachMode()) {
                    reloadToolData();
                }
            }
        });
        vBoxProgNumber.getChildren().addAll(titleProgNum, valueProgNum);

        // TOOL
        vBoxTool = new VBox();
        vBoxTool.setSpacing(2);
        vBoxTool.setPadding(new Insets(1, 3, 1, 3));
        vBoxTool.setAlignment(Pos.CENTER);
        vBoxTool.getStyleClass().add("vbox-info");
        vBoxTool.setPrefWidth(100);
        vBoxTool.setMaxWidth(100);
        vBoxTool.setMinWidth(80);
        HBox.setHgrow(vBoxTool, Priority.SOMETIMES);
        hbox.getChildren().addAll(vBoxTool);

        vBoxTool.setOnMouseClicked(vBoxHandler);

        Label titleTool = new Label("TOOL");
        titleTool.getStyleClass().add("label-info-title");
        titleTool.setFont(ViewHelper.getInstance().getFontInformation());
        titleTool.setTextFill(Color.WHITE);

        valueTool = new Label("0");
        valueTool.getStyleClass().add("label-info-value");
        valueTool.setFont(ViewHelper.getInstance().getFontInformation());
        valueTool.setTextFill(Color.WHITE);
        //valueTool.setTooltip(ViewHelper.makeNormalTooltip("Tool"));
        valueTool.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (isTeachMode()) {
                    saveToolDataFile();
                } else {
                    reloadToolData();
                }
            }
        });

        vBoxTool.getChildren().addAll(titleTool, valueTool);

        // SECTION
        vBoxSection = new VBox();
        vBoxSection.setSpacing(2);
        vBoxSection.setPadding(new Insets(1, 3, 1, 3));
        vBoxSection.setAlignment(Pos.CENTER);
        vBoxSection.getStyleClass().add("vbox-info");
        vBoxSection.setPrefWidth(100);
        vBoxSection.setMaxWidth(100);
        vBoxSection.setMinWidth(80);
        HBox.setHgrow(vBoxSection, Priority.SOMETIMES);
        hbox.getChildren().addAll(vBoxSection);

        vBoxSection.setOnMouseClicked(vBoxHandler);

        Label titleSection = new Label("SECTION");
        titleSection.getStyleClass().add("label-info-title");
        titleSection.setFont(ViewHelper.getInstance().getFontInformation());
        titleSection.setTextFill(Color.WHITE);

        valueSection = new Label("0");
        valueSection.getStyleClass().add("label-info-value");
        valueSection.setFont(ViewHelper.getInstance().getFontInformation());
        valueSection.setTextFill(Color.WHITE);
        //valueSection.setTooltip(ViewHelper.makeNormalTooltip("Section"));
        valueSection.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (isTeachMode()) {
                    saveToolDataFile();
                } else {
                    reloadToolData();
                }

                if (isMonitorOn()) {

                    // * Report Savings
                    reportSavings();

                    // * Reset Elapsed Learned Time when Section changes and Monitor=1
                    valueElapsedMonitorTime.setText("0.0 sec");
                    if (stopWatch != null) {
                        stopWatch.stop(); // Stops the elapsed timer
                    }

                    stopWatch = new Stopwatch();
                    stopWatch.start();
                }
            }
        });

        vBoxSection.getChildren().addAll(titleSection, valueSection);

        // CHANNEL
        vBoxChannel = new VBox();
        vBoxChannel.setSpacing(2);
        vBoxChannel.setPadding(new Insets(1, 3, 1, 3));
        vBoxChannel.setAlignment(Pos.CENTER);
        vBoxChannel.getStyleClass().add("vbox-info");
        vBoxChannel.setPrefWidth(100);
        vBoxChannel.setMaxWidth(100);
        vBoxChannel.setMinWidth(80);
        HBox.setHgrow(vBoxChannel, Priority.SOMETIMES);
        hbox.getChildren().addAll(vBoxChannel);

        vBoxChannel.setOnMouseClicked(vBoxHandler);

        Label titleChannel = new Label("CHANNEL");
        titleChannel.getStyleClass().add("label-info-title");
        titleChannel.setFont(ViewHelper.getInstance().getFontInformation());
        titleChannel.setTextFill(Color.WHITE);

        valueChannel = new Label("0");
        valueChannel.getStyleClass().add("label-info-value");
        valueChannel.setFont(ViewHelper.getInstance().getFontInformation());
        valueChannel.setTextFill(Color.WHITE);
        //valueChannel.setTooltip(ViewHelper.makeNormalTooltip("Channel"));
        //valueChannel.textProperty().addListener(TSCListener);
        valueChannel.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                feedbackChannelValue();

                if (isTeachMode()) {
                    saveToolDataFile();
                } else {
                    reloadToolData();
                }
            }
        });
        vBoxChannel.getChildren().addAll(titleChannel, valueChannel);

        // COMMENT
        vBoxComment = new VBox();
        vBoxComment.setSpacing(2);
        vBoxComment.setPadding(new Insets(1, 3, 1, 3));
        vBoxComment.setAlignment(Pos.CENTER);
        vBoxComment.getStyleClass().add("vbox-info");
        vBoxComment.setPrefWidth(150);
        vBoxComment.setMaxWidth(200);
        vBoxComment.setMinWidth(80);
        HBox.setHgrow(vBoxComment, Priority.SOMETIMES);
        hbox.getChildren().addAll(vBoxComment);

        titleComment = new Label("COMMENT");
        titleComment.getStyleClass().add("label-info-title");
        titleComment.setFont(ViewHelper.getInstance().getFontInformation());
        titleComment.setTextFill(Color.WHITE);

        valueComment = new Label("");
        valueComment.getStyleClass().add("label-info-value");
        valueComment.setFont(ViewHelper.getInstance().getFontInformation());
        valueComment.setTextFill(Color.WHITE);
        //valueComment.setTooltip(ViewHelper.makeNormalTooltip("Channel"));

        vBoxComment.getChildren().addAll(titleComment, valueComment);

        // Button Macro Interrupt
        btnMacroInterruptEnable = new Button("MACRO INTERRUPT\nOFF");
        btnMacroInterruptEnable.setAlignment(Pos.CENTER);
        btnMacroInterruptEnable.setTextAlignment(TextAlignment.CENTER);
        btnMacroInterruptEnable.getStyleClass().add("button-info");
        btnMacroInterruptEnable.setPrefWidth(150);
        btnMacroInterruptEnable.setMaxWidth(150);
        btnMacroInterruptEnable.setMinWidth(100);
        hbox.getChildren().add(btnMacroInterruptEnable);
        btnMacroInterruptEnable.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {

                if (!valueProgNum.getText().equals("0")) {
                    if (isMacroInterruptOn()) {
                        btnMacroInterruptEnable.setText("MACRO INTERRUPT\nOFF");
                        btnMacroInterruptEnable.setStyle("-fx-background-color:white");
                    } else {
                        btnMacroInterruptEnable.setText("MACRO INTERRUPT\nON");
                        btnMacroInterruptEnable.setStyle("-fx-background-color:green");
                    }
                }
            }
        });
        btnMacroInterruptEnable.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                feedbackSwitchStatus();
                if (t1.equals("MACRO INTERRUPT\nON")) {
                    btnMacroInterruptEnable.setStyle("-fx-background-color:green");
                } else {
                    btnMacroInterruptEnable.setStyle("-fx-background-color:white");
                }
            }
        });

        // Button Teach Mode
        btnTeachMode = new Button("TEACH MODE\nOFF");
        btnTeachMode.setAlignment(Pos.CENTER);
        btnTeachMode.setTextAlignment(TextAlignment.CENTER);
        btnTeachMode.getStyleClass().add("button-info");
        btnTeachMode.setPrefWidth(100);
        btnTeachMode.setMaxWidth(100);
        btnTeachMode.setMinWidth(100);
        hbox.getChildren().add(btnTeachMode);

        btnTeachMode.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {

                toggleTeachMode();

                updateAdaptiveStatus();
            } // Runs when teach mode is turned off and sets the slider and combobox visible
        });

        // Button Teach OneTime
        btnTeachOnetool = new Button("TEACH ONE TOOL\nOFF");
        btnTeachOnetool.setAlignment(Pos.CENTER);
        btnTeachOnetool.setTextAlignment(TextAlignment.CENTER);
        btnTeachOnetool.getStyleClass().add("button-info");
        btnTeachOnetool.setPrefWidth(140);
        btnTeachOnetool.setMaxWidth(150);
        btnTeachOnetool.setMinWidth(120);
        hbox.getChildren().add(btnTeachOnetool);

        btnTeachMode.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String t, String t1) {
                vBoxComment.getChildren().setAll(titleComment, valueComment);
                if (t1.equals("TEACH MODE\nOFF")) {
                    timelineAnimTeachMode.stop();
                    btnTeachMode.setStyle("-fx-background-color:white");

                    // TeachMode=0, show slider and Save Button panel
                    boxHomeSliders.setVisible(true);
                    vBoxSaveVars.setVisible(true);
                } else {
                    // TeachMode=1, hide slider and Save Button panel
                    boxHomeSliders.setVisible(false);
                    vBoxSaveVars.setVisible(false);

                    // When start the teach mode, reset the Max Low Limit Timer
                    uiVars.MaxLowLimitTimer = 0;
                }

                // Send new status to PLC
                feedbackSwitchStatus();

                // Update Machine Status
                updateMachineStatus();
            }
        });

        btnTeachOnetool.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String oldValue, String newValue) { // if new value is set to off turn it off and show vbox savevars with combobox and slider
                feedbackSwitchStatus();

                if (newValue.equals("TEACH ONE TOOL\nOFF")) {
                    timelineAnimTeachOneTool.stop();
                    btnTeachOnetool.setStyle("-fx-background-color:white");

                    boxHomeSliders.setVisible(true);

                    vBoxSaveVars.setVisible(true);
                    // priorityComboBox.setVisible(true);
                } else {
                    boxHomeSliders.setVisible(false);

                    vBoxSaveVars.setVisible(false);
                }

                // Update Machine Status
                updateMachineStatus();
            }
        });

        btnTeachOnetool.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {

                if (isTeachOneToolMode()) {
                    // * Let me explain Teach Mode and Teach One Tool buttons: Teach Mode button is turned on and off by user.
                    // * Teach One Tool button is turned on by user but is turned off when Monitor changes from 1 to 0 (on to off),
                    // * and it also turns off Teach Mode button

                    // * user couldn't make teachOneTool=0 from 1 by click its button
                    // * So disable this button action
                    // btnTeachOnetool.setText("TEACH ONE TOOL\nOFF");
                    // btnTeachMode.setText("TEACH MODE\nOFF");
                } else {
                    // Should turn on
                    // * user click teachOneTool, it automatically turns on teachMode

                    if (getValueProgNum() == 0) {
                        Toast.message("Invalid Program Number!");
                    } else {
                        valueElapsedMonitorTime.setText("0.0 sec");
                        btnTeachOnetool.setText("TEACH ONE TOOL\nON");
                        btnTeachMode.setText("TEACH MODE\nON");
                        vBoxSaveVars.setVisible(false);

                        boxHomeSliders.setVisible(false);

                        if (uiVars.Optimize > 0) { // Makes sure that the machine is not running before teach mode is on
                            JOptionPane.showMessageDialog(null, "Machine is already running! Please turn on Teach One Tool before starting the cycle");
                        } else {
                            TextField textField = new TextField();
                            textField.setPromptText("Enter Tool Name");
                            Button saveButton = new Button("Save Name");
                            vBoxComment.getChildren().setAll(titleComment, textField, saveButton);
                            saveButton.setOnAction(event ->
                            {
                                try { // Saves the comment that the user enters during teach mode
                                    JobDataManager.getInstance().saveJobData(textField.getText());
                                    valueComment.setText(textField.getText()); //
                                    vBoxComment.getChildren().setAll(titleComment, valueComment);
                                } catch (IOException ex) {
                                    ex.printStackTrace();
                                }
                            });

                            /*if (valueTarget.getText().equals("0.0") &&
                                    valueHighLimit.getText().equals("0.0") &&
                                    valueLowLimitTime.getText().equals("0.0") &&
                                    valueIdle.getText().equals("0.0")) {

                                valueElapsedMonitorTime.setText("0.0 sec");
                                btnTeachOnetool.setText("TEACH ONE TOOL\nON");
                                btnTeachMode.setText("TEACH MODE\nON");
                                vBoxSaveVars.setVisible(false);
                                vSliderBox.setVisible(false);
                            } else {
                                int reply = JOptionPane.showConfirmDialog(null, "Are you sure you wish to overwrite existing data?",
                                        "Teach Setting Warning", JOptionPane.YES_NO_OPTION);
                                if (reply == JOptionPane.YES_OPTION) {
                                    valueElapsedMonitorTime.setText("0.0 sec");
                                    btnTeachOnetool.setText("TEACH ONE TOOL\nON");
                                    btnTeachMode.setText("TEACH MODE\nON");
                                    vBoxSaveVars.setVisible(false); // Hides save new values button during teach mode
                                    vSliderBox.setVisible(false); // Hides Value Slider during teach mode

                                    TextField textField = new TextField();
                                    textField.setPromptText("Enter Tool Name");
                                    Button saveButton = new Button("Save Name");
                                    vBoxComment.getChildren().setAll(titleComment, textField, saveButton);
                                    saveButton.setOnAction(event ->
                                    {
                                        try { // Saves the comment that the user enters during teach mode
                                            JobDataManager.getInstance().saveJobData(textField.getText());
                                            valueComment.setText(textField.getText()); //
                                            vBoxComment.getChildren().setAll(titleComment, valueComment);
                                        } catch (IOException ex) {
                                            ex.printStackTrace();
                                        }
                                    });
                                } else { // Cancels teach mode
                                    JOptionPane.showMessageDialog(null, "Teach Mode Cancelled");
                                    btnTeachMode.setText("TEACH MODE\nOFF");
                                }
                            }*/
                        }
                    }
                }
            }
        });

        // Button Feedrate Control Off
        btnAdaptiveEnable = new Button("ADAPTIVE\nOFF");
        btnAdaptiveEnable.setAlignment(Pos.CENTER);
        btnAdaptiveEnable.setTextAlignment(TextAlignment.CENTER);
        btnAdaptiveEnable.getStyleClass().add("button-info");
        btnAdaptiveEnable.setPrefWidth(140);
        btnAdaptiveEnable.setMaxWidth(150);
        btnAdaptiveEnable.setMinWidth(100);
        hbox.getChildren().add(btnAdaptiveEnable);
        btnAdaptiveEnable.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if (valueProgNum.getText().equals("0")) {
                    Toast.message("Invalid Program Number!");
                } else {
                    if (isAdaptiveOn()) {
                        btnAdaptiveEnable.setText("ADAPTIVE\nOFF");
                        btnAdaptiveEnable.setStyle("-fx-background-color:white");
                    } else {
                        btnAdaptiveEnable.setText("ADAPTIVE\nON");
                        btnAdaptiveEnable.setStyle("-fx-background-color:green");
                    }
                }
            }
        });
        btnAdaptiveEnable.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String oldValue, String newValue) {
                feedbackSwitchStatus();

                if (newValue.equals("ADAPTIVE\nON")) {
                    System.out.println("AD on");
                    btnAdaptiveEnable.setStyle("-fx-background-color:green");

                    // High & Wear Limit Titles
                    vBoxHighLimit.setVisible(false);
                    vBoxAdaptiveHighLimit.setVisible(true);

                    vBoxWearLimit.setVisible(false);
                    vBoxAdaptiveWearLimit.setVisible(true);
                    applet.changeHighWearLimitLinesStyle(false);

                    showSlider(2);
                    sliderAdjustAdaptive.setValue(getValueAdaptiveHighLimit());
                    setAdaptiveSliderTrackColor("red");
                } else {
                    System.out.println("AD off");
                    btnAdaptiveEnable.setStyle("-fx-background-color:white");

                    // *High & Wear Limit Title
                    vBoxHighLimit.setVisible(true);
                    vBoxAdaptiveHighLimit.setVisible(false);

                    vBoxWearLimit.setVisible(true);
                    vBoxAdaptiveWearLimit.setVisible(false);
                    applet.changeHighWearLimitLinesStyle(true);

                    // Determine the Slider block
                    String val = String.valueOf(priorityComboBox.getValue());
                    if (val.equals("FILTER")) {
                        showSlider(0);
                    } else {
                        showSlider(1);
                    }
                }

                // Check Adaptive Enable
                updateAdaptiveStatus();
            }
        });

        // Button Feedrate Control Off
        btnLeadInFREnable = new Button("LEAD-IN FR\nOFF");
        btnLeadInFREnable.setTextAlignment(TextAlignment.CENTER);
        btnLeadInFREnable.setAlignment(Pos.CENTER);
        btnLeadInFREnable.getStyleClass().add("button-info");
        btnLeadInFREnable.setPrefWidth(100);
        btnLeadInFREnable.setMaxWidth(100);
        btnLeadInFREnable.setMinWidth(70);
        hbox.getChildren().add(btnLeadInFREnable);
        btnLeadInFREnable.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if (valueProgNum.getText().equals("0")) {
                    Toast.message("Invalid Program Number!");
                } else {
                    if (isLeadInFROn()) {
                        btnLeadInFREnable.setText("LEAD-IN FR\nOFF");
                        btnLeadInFREnable.setStyle("-fx-background-color:white");
                    } else {
                        btnLeadInFREnable.setText("LEAD-IN FR\nON");
                        btnLeadInFREnable.setStyle("-fx-background-color:green");
                    }
                }
            }
        });
        btnLeadInFREnable.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> ov, String oldValue, String newValue) {
                feedbackSwitchStatus();

                if (newValue.equals("LEAD-IN FR\nON")) {
                    btnLeadInFREnable.setStyle("-fx-background-color:green");
                } else {
                    btnLeadInFREnable.setStyle("-fx-background-color:white");
                }
            }
        });

        // Button Filter
        vBoxFilter = new VBox();
        vBoxFilter.setAlignment(Pos.CENTER);
        vBoxFilter.getStyleClass().add("button-info");
        vBoxFilter.setPrefWidth(100);
        vBoxFilter.setMaxWidth(100);
        vBoxFilter.setMinWidth(70);
        vBoxFilter.setMaxHeight(80);

        Label titleFilter = new Label("FILTER");
        titleFilter.getStyleClass().add("label-info-title");
        titleFilter.setFont(ViewHelper.getInstance().getFontInformation());
        titleFilter.setTextFill(Color.WHITE);

        valueFilter = new Label("0.0");
        valueFilter.getStyleClass().add("label-info-value");
        valueFilter.setFont(ViewHelper.getInstance().getFontInformation());
        valueFilter.setTextFill(Color.WHITE);
        //valueFilter.setTooltip(ViewHelper.makeNormalTooltip("Filter Settings"));

        vBoxFilter.getChildren().addAll(titleFilter, valueFilter);
        hbox.getChildren().add(vBoxFilter);
        vBoxFilter.setOnMouseClicked(vBoxHandler);
        return hbox;
    }

    public HBox getGaugeBox() {
        HBox hGaugeHomeBox = new HBox();
        hGaugeHomeBox.setAlignment(Pos.CENTER);
        hGaugeHomeBox.setSpacing(10);
        hGaugeHomeBox.setPadding(new Insets(10, 15, 10, 15));

        // Value HP
        vBoxHp = new VBox();
        vBoxHp.setSpacing(2);
        vBoxHp.setPadding(new Insets(1, 3, 1, 3));
        vBoxHp.setAlignment(Pos.CENTER);
        vBoxHp.getStyleClass().addAll("vbox-gauge", "vbox-gauge-purple");
        vBoxHp.setPrefWidth(100);
        vBoxHp.setMaxWidth(120);
        vBoxHp.setMinWidth(70);
        vBoxHp.setPrefHeight(35);
        vBoxHp.setMaxHeight(35);
        vBoxHp.setOnMouseClicked(vBoxHandler);
        HBox.setHgrow(vBoxHp, Priority.SOMETIMES);
        hGaugeHomeBox.getChildren().addAll(vBoxHp);

        Label titleHp = new Label("HP");
        titleHp.getStyleClass().add("label-info-title");
        titleHp.setFont(ViewHelper.getInstance().getFontInformation());
        titleHp.setTextFill(Color.WHITE);

        valueHp = new Label("0.00");
        valueHp.getStyleClass().add("label-info-value");
        valueHp.setFont(ViewHelper.getInstance().getFontInformation());
        valueHp.setTextFill(Color.WHITE);
        //valueHp.setTooltip(ViewHelper.makeNormalTooltip("Hp"));
        vBoxHp.getChildren().addAll(titleHp, valueHp);

        // Value HP2
        vBoxHp2 = new VBox();
        vBoxHp2.setSpacing(2);
        vBoxHp2.setPadding(new Insets(1, 3, 1, 3));
        vBoxHp2.setAlignment(Pos.CENTER);
        vBoxHp2.getStyleClass().addAll("vbox-gauge", "vbox-gauge-blue");
        vBoxHp2.setPrefWidth(100);
        vBoxHp2.setMaxWidth(120);
        vBoxHp2.setMinWidth(70);
        vBoxHp2.setPrefHeight(35);
        vBoxHp2.setMaxHeight(35);
        vBoxHp2.setOnMouseClicked(vBoxHandler);
        HBox.setHgrow(vBoxHp2, Priority.SOMETIMES);
        hGaugeHomeBox.getChildren().addAll(vBoxHp2);

        Label titleHp2 = new Label("HP2");
        titleHp2.getStyleClass().add("label-info-title");
        titleHp2.setFont(ViewHelper.getInstance().getFontInformation());
        titleHp2.setTextFill(Color.WHITE);

        valueHp2 = new Label("0.00");
        valueHp2.getStyleClass().add("label-info-value");
        valueHp2.setFont(ViewHelper.getInstance().getFontInformation());
        valueHp2.setTextFill(Color.WHITE);
        //valueHp.setTooltip(ViewHelper.makeNormalTooltip("Hp"));
        vBoxHp2.getChildren().addAll(titleHp2, valueHp2);

        // Value Target
        vBoxTarget = new VBox();
        vBoxTarget.setSpacing(2);
        vBoxTarget.setPadding(new Insets(1, 3, 1, 3));
        vBoxTarget.setAlignment(Pos.CENTER);
        vBoxTarget.getStyleClass().addAll("vbox-gauge", "vbox-gauge-green");
        vBoxTarget.setPrefWidth(100);
        vBoxTarget.setMaxWidth(120);
        vBoxTarget.setMinWidth(70);
        vBoxTarget.setPrefHeight(35);
        vBoxTarget.setMaxHeight(35);
        vBoxTarget.setOnMouseClicked(vBoxHandler);
        HBox.setHgrow(vBoxTarget, Priority.SOMETIMES);
        hGaugeHomeBox.getChildren().addAll(vBoxTarget);

        Label titleTarget = new Label("TARGET");
        titleTarget.getStyleClass().add("label-info-title");
        titleTarget.setFont(ViewHelper.getInstance().getFontInformation());
        titleTarget.setTextFill(Color.WHITE);

        valueTarget = new Label("0.00");
        valueTarget.getStyleClass().add("label-info-value");
        valueTarget.setFont(ViewHelper.getInstance().getFontInformation());
        valueTarget.setTextFill(Color.WHITE);
        //valueTarget.setTooltip(ViewHelper.makeNormalTooltip("Target"));
        valueTarget.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (isTeachMode()) {} else {}

                // Feedback Setpoint value to Fanuc
                feedbackSetPointValueToFanuc();
            }
        });

        vBoxTarget.getChildren().addAll(titleTarget, valueTarget);

        // High Limit Block
        AnchorPane blockHighLimit = new AnchorPane();
        blockHighLimit.setPrefWidth(180);
        blockHighLimit.setMaxWidth(180);
        blockHighLimit.setMinWidth(150);
        blockHighLimit.setPrefHeight(35);
        blockHighLimit.setMaxHeight(35);
        // ----- High Limit
        vBoxHighLimit = new VBox();
        vBoxHighLimit.setSpacing(2);
        vBoxHighLimit.setPadding(new Insets(1, 3, 1, 3));
        vBoxHighLimit.setAlignment(Pos.CENTER);
        vBoxHighLimit.getStyleClass().addAll("vbox-gauge", "vbox-gauge-red");
        vBoxHighLimit.setPrefWidth(180);
        vBoxHighLimit.setMaxWidth(180);
        vBoxHighLimit.setMinWidth(150);
        vBoxHighLimit.setPrefHeight(35);
        vBoxHighLimit.setMaxHeight(35);
        vBoxHighLimit.setOnMouseClicked(vBoxHandler);

        Label titleHighLimit = new Label("HIGH LIMIT");
        titleHighLimit.getStyleClass().add("label-info-title");
        titleHighLimit.setFont(ViewHelper.getInstance().getFontInformation());
        titleHighLimit.setTextFill(Color.WHITE);

        valueHighLimit = new Label("0.00");
        valueHighLimit.getStyleClass().add("label-info-value");
        valueHighLimit.setFont(ViewHelper.getInstance().getFontInformation());
        valueHighLimit.setTextFill(Color.WHITE);
        //valueHighLimit.setTooltip(ViewHelper.makeNormalTooltip("High Limit"));
        vBoxHighLimit.getChildren().addAll(titleHighLimit, valueHighLimit);
        AnchorPane.setRightAnchor(vBoxHighLimit, 0.0);
        AnchorPane.setLeftAnchor(vBoxHighLimit, 0.0);
        AnchorPane.setTopAnchor(vBoxHighLimit, 0.0);
        AnchorPane.setBottomAnchor(vBoxHighLimit, 0.0);

        // ----- High Limit Block
        vBoxAdaptiveHighLimit = new VBox();
        vBoxAdaptiveHighLimit.setSpacing(2);
        vBoxAdaptiveHighLimit.setPadding(new Insets(1, 3, 1, 3));
        vBoxAdaptiveHighLimit.setAlignment(Pos.CENTER);
        vBoxAdaptiveHighLimit.getStyleClass().addAll("vbox-gauge", "vbox-gauge-red");
        vBoxAdaptiveHighLimit.setPrefWidth(180);
        vBoxAdaptiveHighLimit.setMaxWidth(180);
        vBoxAdaptiveHighLimit.setMinWidth(150);
        vBoxAdaptiveHighLimit.setPrefHeight(35);
        vBoxAdaptiveHighLimit.setMaxHeight(35);
        vBoxAdaptiveHighLimit.setOnMouseClicked(vBoxHandler);

        hGaugeHomeBox.getChildren().addAll(vBoxHighLimit);

        Label titleAdaptiveHighPer = new Label("Adaptive High %");
        titleAdaptiveHighPer.getStyleClass().add("label-info-title");
        titleAdaptiveHighPer.setFont(ViewHelper.getInstance().getFontInformation());
        titleAdaptiveHighPer.setTextFill(Color.WHITE);

        valueAdaptiveHighPercentage = new Label(String.valueOf(PreferenceManager.getAdaptiveHighLimit()));
        valueAdaptiveHighPercentage.getStyleClass().add("label-info-value");
        valueAdaptiveHighPercentage.setFont(ViewHelper.getInstance().getFontInformation());
        valueAdaptiveHighPercentage.setTextFill(Color.WHITE);
        //valueAdaptiveHighPercentage.setTooltip(ViewHelper.makeNormalTooltip("Adaptive High %"));
        vBoxAdaptiveHighLimit.getChildren().addAll(titleAdaptiveHighPer, valueAdaptiveHighPercentage);
        AnchorPane.setRightAnchor(vBoxAdaptiveHighLimit, 0.0);
        AnchorPane.setLeftAnchor(vBoxAdaptiveHighLimit, 0.0);
        AnchorPane.setTopAnchor(vBoxAdaptiveHighLimit, 0.0);
        AnchorPane.setBottomAnchor(vBoxAdaptiveHighLimit, 0.0);
        vBoxAdaptiveHighLimit.setVisible(false);

        blockHighLimit.getChildren().addAll(vBoxHighLimit, vBoxAdaptiveHighLimit);
        HBox.setHgrow(blockHighLimit, Priority.SOMETIMES);
        hGaugeHomeBox.getChildren().addAll(blockHighLimit);

        // Wear Limit Block
        AnchorPane blockWearLimit = new AnchorPane();
        blockWearLimit.setPrefWidth(180);
        blockWearLimit.setMaxWidth(180);
        blockWearLimit.setMinWidth(150);
        blockWearLimit.setPrefHeight(35);
        blockWearLimit.setMaxHeight(35);

        // ----- Wear Limit
        vBoxWearLimit = new VBox();
        vBoxWearLimit.setSpacing(2);
        vBoxWearLimit.setPadding(new Insets(1, 3, 1, 3));
        vBoxWearLimit.setAlignment(Pos.CENTER);
        vBoxWearLimit.getStyleClass().addAll("vbox-gauge", "vbox-gauge-orange");
        vBoxWearLimit.setPrefWidth(180);
        vBoxWearLimit.setMaxWidth(180);
        vBoxWearLimit.setMinWidth(150);
        vBoxWearLimit.setPrefHeight(35);
        vBoxWearLimit.setMaxHeight(35);
        vBoxWearLimit.setOnMouseClicked(vBoxHandler);

        Label titleWearLimit = new Label("WEAR LIMIT");
        titleWearLimit.getStyleClass().add("label-info-title");
        titleWearLimit.setFont(ViewHelper.getInstance().getFontInformation());
        titleWearLimit.setTextFill(Color.WHITE);

        valueWearLimit = new Label("0.00");
        valueWearLimit.getStyleClass().add("label-info-value");
        valueWearLimit.setFont(ViewHelper.getInstance().getFontInformation());
        valueWearLimit.setTextFill(Color.WHITE);
        //valueWearLimit.setTooltip(ViewHelper.makeNormalTooltip("Wear Limit"));
        vBoxWearLimit.getChildren().addAll(titleWearLimit, valueWearLimit);
        AnchorPane.setRightAnchor(vBoxWearLimit, 0.0);
        AnchorPane.setLeftAnchor(vBoxWearLimit, 0.0);
        AnchorPane.setTopAnchor(vBoxWearLimit, 0.0);
        AnchorPane.setBottomAnchor(vBoxWearLimit, 0.0);

        // ----- Adaptive Wear Per
        vBoxAdaptiveWearLimit = new VBox();
        vBoxAdaptiveWearLimit.setSpacing(2);
        vBoxAdaptiveWearLimit.setPadding(new Insets(1, 3, 1, 3));
        vBoxAdaptiveWearLimit.setAlignment(Pos.CENTER);
        vBoxAdaptiveWearLimit.getStyleClass().addAll("vbox-gauge", "vbox-gauge-orange");
        vBoxAdaptiveWearLimit.setPrefWidth(180);
        vBoxAdaptiveWearLimit.setMaxWidth(180);
        vBoxAdaptiveWearLimit.setMinWidth(150);
        vBoxAdaptiveWearLimit.setPrefHeight(35);
        vBoxAdaptiveWearLimit.setMaxHeight(35);
        vBoxAdaptiveWearLimit.setOnMouseClicked(vBoxHandler);

        Label titleAdaptiveWearPer = new Label("Adaptive Wear %");
        titleAdaptiveWearPer.getStyleClass().add("label-info-title");
        titleAdaptiveWearPer.setFont(ViewHelper.getInstance().getFontInformation());
        titleAdaptiveWearPer.setTextFill(Color.WHITE);

        valueAdaptiveWearPercentage = new Label(String.valueOf(PreferenceManager.getAdaptiveWearLimit()));
        valueAdaptiveWearPercentage.getStyleClass().add("label-info-value");
        valueAdaptiveWearPercentage.setFont(ViewHelper.getInstance().getFontInformation());
        valueAdaptiveWearPercentage.setTextFill(Color.WHITE);
        //valueAdaptiveWearPercentage.setTooltip(ViewHelper.makeNormalTooltip("Adaptive Wear %"));
        vBoxAdaptiveWearLimit.getChildren().addAll(titleAdaptiveWearPer, valueAdaptiveWearPercentage);
        AnchorPane.setRightAnchor(vBoxAdaptiveWearLimit, 0.0);
        AnchorPane.setLeftAnchor(vBoxAdaptiveWearLimit, 0.0);
        AnchorPane.setTopAnchor(vBoxAdaptiveWearLimit, 0.0);
        AnchorPane.setBottomAnchor(vBoxAdaptiveWearLimit, 0.0);
        vBoxAdaptiveWearLimit.setVisible(false);

        blockWearLimit.getChildren().addAll(vBoxWearLimit, vBoxAdaptiveWearLimit);
        HBox.setHgrow(blockWearLimit, Priority.SOMETIMES);
        hGaugeHomeBox.getChildren().addAll(blockWearLimit);

        // Idle
        vBoxIdle = new VBox();
        vBoxIdle.setSpacing(2);
        vBoxIdle.setPadding(new Insets(1, 3, 1, 3));
        vBoxIdle.setAlignment(Pos.CENTER);
        vBoxIdle.getStyleClass().addAll("vbox-gauge", "vbox-gauge-white");

        vBoxIdle.setPrefWidth(100);
        vBoxIdle.setMaxWidth(120);
        vBoxIdle.setMinWidth(70);
        vBoxIdle.setPrefHeight(35);
        vBoxIdle.setMaxHeight(35);
        vBoxIdle.setOnMouseClicked(vBoxHandler);

        HBox.setHgrow(vBoxIdle, Priority.SOMETIMES);
        hGaugeHomeBox.getChildren().addAll(vBoxIdle);

        Label titleIdle = new Label("IDLE");
        titleIdle.getStyleClass().add("label-info-title");
        titleIdle.setFont(ViewHelper.getInstance().getFontInformation());
        titleIdle.setTextFill(Color.WHITE);

        valueIdle = new Label("0.00");
        valueIdle.getStyleClass().add("label-info-value");
        valueIdle.setFont(ViewHelper.getInstance().getFontInformation());
        valueIdle.setTextFill(Color.WHITE);
        //valueIdle.setTooltip(ViewHelper.makeNormalTooltip("Idle Status"));
        vBoxIdle.getChildren().addAll(titleIdle, valueIdle);

        // LEAD In Trigger
        vBoxLeadInTrigger = new VBox();
        vBoxLeadInTrigger.setSpacing(2);
        vBoxLeadInTrigger.setPadding(new Insets(1, 3, 1, 3));
        vBoxLeadInTrigger.setAlignment(Pos.CENTER);
        vBoxLeadInTrigger.getStyleClass().addAll("vbox-gauge", "vbox-gauge-brown");
        vBoxLeadInTrigger.setPrefWidth(180);
        vBoxLeadInTrigger.setMaxWidth(200);
        vBoxLeadInTrigger.setMinWidth(100);
        vBoxLeadInTrigger.setPrefHeight(35);
        vBoxLeadInTrigger.setMaxHeight(35);
        vBoxLeadInTrigger.setOnMouseClicked(vBoxHandler);

        HBox.setHgrow(vBoxLeadInTrigger, Priority.SOMETIMES);
        hGaugeHomeBox.getChildren().addAll(vBoxLeadInTrigger);

        Label titleLeadInTrigger = new Label("LEAD-IN TRIGGER");
        titleLeadInTrigger.getStyleClass().add("label-info-title");
        titleLeadInTrigger.setFont(ViewHelper.getInstance().getFontInformation());
        titleLeadInTrigger.setTextFill(Color.WHITE);

        valueLeadInTrigger = new Label("0.00");
        valueLeadInTrigger.getStyleClass().add("label-info-value");
        valueLeadInTrigger.setFont(ViewHelper.getInstance().getFontInformation());
        valueLeadInTrigger.setTextFill(Color.WHITE);
        //valueLeadInTrigger.setTooltip(ViewHelper.makeNormalTooltip("LEAD-IN TRIGGER status"));

        vBoxLeadInTrigger.getChildren().addAll(titleLeadInTrigger, valueLeadInTrigger);

        // Learned Low Limit Time
        vBoxLearnedLowLimitTime = new VBox();
        vBoxLearnedLowLimitTime.setSpacing(2);
        vBoxLearnedLowLimitTime.setPadding(new Insets(1, 3, 1, 3));
        vBoxLearnedLowLimitTime.setAlignment(Pos.CENTER);
        vBoxLearnedLowLimitTime.getStyleClass().addAll("vbox-gauge", "vbox-gauge-skyblue");
        vBoxLearnedLowLimitTime.setPrefWidth(220);
        vBoxLearnedLowLimitTime.setMaxWidth(300);
        vBoxLearnedLowLimitTime.setMinWidth(100);
        vBoxLearnedLowLimitTime.setPrefHeight(35);
        vBoxLearnedLowLimitTime.setMaxHeight(35);

        HBox.setHgrow(vBoxLearnedLowLimitTime, Priority.SOMETIMES);
        hGaugeHomeBox.getChildren().addAll(vBoxLearnedLowLimitTime);

        Label titleLearnedLowLimitTime = new Label("LEARNED LOW LIMIT TIME");
        titleLearnedLowLimitTime.getStyleClass().add("label-info-title");
        titleLearnedLowLimitTime.setFont(ViewHelper.getInstance().getFontInformation());
        titleLearnedLowLimitTime.setTextFill(Color.WHITE);

        valueLearnedLowLimitTimer = new Label("0.0");
        valueLearnedLowLimitTimer.getStyleClass().add("label-info-value");
        valueLearnedLowLimitTimer.setFont(ViewHelper.getInstance().getFontInformation());
        valueLearnedLowLimitTimer.setTextFill(Color.WHITE);
        //valueLearnedLowLimitTimer.setTooltip(ViewHelper.makeNormalTooltip("Learned Low Limit Timer"));

        vBoxLearnedLowLimitTime.getChildren().addAll(titleLearnedLowLimitTime, valueLearnedLowLimitTimer);

        // Low Limit Timer
        vBoxLowLimitTimer = new VBox();
        vBoxLowLimitTimer.setSpacing(2);
        vBoxLowLimitTimer.setPadding(new Insets(1, 3, 1, 3));
        vBoxLowLimitTimer.setAlignment(Pos.CENTER);
        vBoxLowLimitTimer.getStyleClass().addAll("vbox-gauge", "vbox-gauge-skyblue");
        vBoxLowLimitTimer.setPrefWidth(180);
        vBoxLowLimitTimer.setMaxWidth(300);
        vBoxLowLimitTimer.setMinWidth(70);
        vBoxLowLimitTimer.setPrefHeight(35);
        vBoxLowLimitTimer.setMaxHeight(35);
        vBoxLowLimitTimer.setOnMouseClicked(vBoxHandler);

        HBox.setHgrow(vBoxLowLimitTimer, Priority.SOMETIMES);
        hGaugeHomeBox.getChildren().addAll(vBoxLowLimitTimer);

        Label titleLowLimitTimer = new Label("LOW LIMIT TIMER");
        titleLowLimitTimer.getStyleClass().add("label-info-title");
        titleLowLimitTimer.setFont(ViewHelper.getInstance().getFontInformation());
        titleLowLimitTimer.setTextFill(Color.WHITE);

        valueLowLimitTimer = new Label("0.0");
        valueLowLimitTimer.getStyleClass().add("label-info-value");
        valueLowLimitTimer.setFont(ViewHelper.getInstance().getFontInformation());
        valueLowLimitTimer.setTextFill(Color.WHITE);
        //valueLowLimitTimer.setTooltip(ViewHelper.makeNormalTooltip("Low Limit Timer"));

        vBoxLowLimitTimer.getChildren().addAll(titleLowLimitTimer, valueLowLimitTimer);


        // Leanred Mornitor Time
        VBox vBoxLearnedMonitorTime = new VBox();
        vBoxLearnedMonitorTime.setSpacing(2);
        vBoxLearnedMonitorTime.setPadding(new Insets(1, 3, 1, 3));
        vBoxLearnedMonitorTime.setAlignment(Pos.CENTER);
        vBoxLearnedMonitorTime.getStyleClass().addAll("vbox-gauge", "vbox-gauge-filledblue");
        vBoxLearnedMonitorTime.setPrefWidth(230);
        vBoxLearnedMonitorTime.setMaxWidth(230);
        vBoxLearnedMonitorTime.setMinWidth(120);
        vBoxLearnedMonitorTime.setPrefHeight(35);
        vBoxLearnedMonitorTime.setMaxHeight(35);
        HBox.setHgrow(vBoxLearnedMonitorTime, Priority.SOMETIMES);
        hGaugeHomeBox.getChildren().addAll(vBoxLearnedMonitorTime);

        Label titleMornitorTime = new Label("LEARNED MONITOR TIME");
        titleMornitorTime.getStyleClass().add("label-info-title");
        titleMornitorTime.setFont(ViewHelper.getInstance().getFontInformation());
        titleMornitorTime.setTextFill(Color.WHITE);

        valueLearnedMonitorTime = new Label("0.0 sec");
        valueLearnedMonitorTime.getStyleClass().add("label-info-value-fixed-size");
        valueLearnedMonitorTime.setFont(ViewHelper.getInstance().getFontInformation());
        valueLearnedMonitorTime.setTextFill(Color.WHITE);
        //valueLearnedMonitorTime.setTooltip(ViewHelper.makeNormalTooltip("Leanred Monitor Time"));

        vBoxLearnedMonitorTime.getChildren().addAll(titleMornitorTime, valueLearnedMonitorTime);

        // Leanred Mornitor Time
        VBox vBoxElapsedMTime = new VBox();
        vBoxElapsedMTime.setSpacing(2);
        vBoxElapsedMTime.setPadding(new Insets(1, 3, 1, 3));
        vBoxElapsedMTime.setAlignment(Pos.CENTER);
        vBoxElapsedMTime.getStyleClass().addAll("vbox-gauge", "vbox-gauge-filledblue");
        vBoxElapsedMTime.setPrefWidth(230);
        vBoxElapsedMTime.setMaxWidth(230);
        vBoxElapsedMTime.setMinWidth(140);
        vBoxElapsedMTime.setPrefHeight(35);
        vBoxElapsedMTime.setMaxHeight(35);
        HBox.setHgrow(vBoxElapsedMTime, Priority.SOMETIMES);
        hGaugeHomeBox.getChildren().addAll(vBoxElapsedMTime);

        Label titleElapsedMTime = new Label("ELAPSED MONITOR TIME");
        titleElapsedMTime.getStyleClass().add("label-info-title");
        titleElapsedMTime.setFont(ViewHelper.getInstance().getFontInformation());
        titleElapsedMTime.setTextFill(Color.WHITE);

        valueElapsedMonitorTime = new Label("0.0 sec");
        valueElapsedMonitorTime.getStyleClass().add("label-info-value-fixed-size");
        valueElapsedMonitorTime.setFont(ViewHelper.getInstance().getFontInformation());
        valueElapsedMonitorTime.setTextFill(Color.WHITE);
        //valueElapsedMonitorTime.setTooltip(ViewHelper.makeNormalTooltip("Elapsed Monitor Time"));

        vBoxElapsedMTime.getChildren().addAll(titleElapsedMTime, valueElapsedMonitorTime);

        // FEEDRATE
        VBox vBoxFeedrate = new VBox();
        vBoxFeedrate.setSpacing(2);
        vBoxFeedrate.setPadding(new Insets(1, 3, 1, 3));
        vBoxFeedrate.setAlignment(Pos.CENTER);
        vBoxFeedrate.getStyleClass().addAll("vbox-gauge", "vbox-gauge-grey");
        vBoxFeedrate.setPrefWidth(100);
        vBoxFeedrate.setMaxWidth(120);
        vBoxFeedrate.setMinWidth(70);
        vBoxFeedrate.setPrefHeight(35);
        vBoxFeedrate.setMaxHeight(35);
        HBox.setHgrow(vBoxFeedrate, Priority.SOMETIMES);
        hGaugeHomeBox.getChildren().addAll(vBoxFeedrate);

        Label titleFeedrate = new Label("FEEDRATE");
        titleFeedrate.getStyleClass().add("label-info-title");
        titleFeedrate.setFont(ViewHelper.getInstance().getFontInformation());
        titleFeedrate.setTextFill(Color.WHITE);

        valueFeedrate = new Label("0%");
        valueFeedrate.getStyleClass().add("label-info-value");
        valueFeedrate.setFont(ViewHelper.getInstance().getFontInformation());
        valueFeedrate.setTextFill(Color.WHITE);
        //valueFeedrate.setTooltip(ViewHelper.makeNormalTooltip("Feedrate"));

        vBoxFeedrate.getChildren().addAll(titleFeedrate, valueFeedrate);


        valueInnerTimers = new Label("High Timer : 0\nWear Timer : 0\nLow Timer : 0");
        valueInnerTimers.getStyleClass().add("label-info-title");
        valueInnerTimers.setFont(ViewHelper.getInstance().getFontInformation());
        valueInnerTimers.setTextFill(Color.WHITE);
        valueInnerTimers.setPrefWidth(160);
        //valueInnerTimers.setTooltip(ViewHelper.makeNormalTooltip("Inner Timer"));
        //hGaugeHomeBox.getChildren().addAll(valueInnerTimers);

        valueSendSignals = new Label("MacroIntrpt : 0\nToolWear : 0");
        valueSendSignals.getStyleClass().add("label-info-title");
        valueSendSignals.setFont(ViewHelper.getInstance().getFontInformation());
        valueSendSignals.setTextFill(Color.WHITE);
        valueSendSignals.setPrefWidth(160);
        //valueSendSignals.setTooltip(ViewHelper.makeNormalTooltip("Inner Timer"));
        //hGaugeHomeBox.getChildren().addAll(valueSendSignals);


        valueStartDelay = new Label("0");
        valueHighLimitDelay = new Label("0");
        valueWearLimitDelay = new Label("0");

        valueAdaptiveMin = new Label("0");
        valueAdaptiveMax = new Label("0");

        valueSensorScaleSend = new Label("0");
        valueOptimize = new Label("0");
        valueOptimize.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {

                // Optimize Changed

                // Send switch status to PLC
                feedbackSwitchStatus();

                // IF monitor changes from 1 to 0 AND teachMode=1 AND teachOneTool=1 THEN insert/update record AND teachmode=0
                // IF monitor changes from 1 to 0 AND teachMode=1 AND teachOneTool=0 THEN insert/update record AND teachmode=1
                if (newValue.equals("0")) {

                    // Finish HST Recordings
                    hstFileSaveManager.finishWriting();

                    // * Report Savings
                    // When teachMode=0 AND adaptiveEnable=1 AND monitor change from 1 to 0,
                    // When monitor=1 and section change
                    if (!isTeachMode() && isAdaptiveOn()) {
                        reportSavings();
                    }

                    if (stopWatch != null) {
                        stopWatch.stop(); // Stops the elapsed timer
                    }

                    if (isTeachMode()) {
                        // Save Tool Data File
                        saveToolDataFile();

                        if (isTeachOneToolMode()) {
                            // Checks if teach mode was turned on by teach one tool
                            sliderAdjust.setVisible(true);
                            priorityComboBox.setVisible(true);

                            btnTeachMode.setText("TEACH MODE\nOFF");
                            timelineAnimTeachMode.stop();
                            btnTeachMode.setStyle("-fx-background-color:white");

                            btnTeachOnetool.setText("TEACH ONE TOOL\nOFF");
                            btnTeachOnetool.setStyle("-fx-background-color:white");
                            timelineAnimTeachOneTool.stop(); // Stop the teach one time flashing
                        } else {
                            timelineAnimTeachMode.stop();
                            btnTeachMode.setStyle("-fx-background-color:white");
                        }
                    }
                } else { // If optimize is running or == 1:

                    // Start HST Recording
                    if (!isTeachMode()) {
                        String hstFileName = String.format("TOS(%s-%d-%d-%d-%d).hst", DateTimeUtils.getFileSuffixString(),
                                getValueProgNum(), getValueTool(), getValueSection(), getValueChannel());
                        hstFileSaveManager.init(hstFileName, plotVariableNames);
                    }

                    if (stopWatch != null) {
                        stopWatch.stop();
                    }
                    stopWatch = new Stopwatch();
                    stopWatch.start();

                    if (isTeachMode()) {
                        timelineAnimTeachMode.setCycleCount(Timeline.INDEFINITE);
                        timelineAnimTeachMode.setAutoReverse(false);
                        timelineAnimTeachMode.play();
                    }

                    if (isTeachOneToolMode()) {
                        timelineAnimTeachOneTool.setCycleCount(Timeline.INDEFINITE);
                        timelineAnimTeachOneTool.setAutoReverse(false);
                        timelineAnimTeachOneTool.play();
                    }

                    // *System Alarm is reset WHEN:
                    //1. User select reset button
                    //2. When monitor changes from 0 to 1
                    resetAlarm();
                }

                // Update Machine Status
                updateMachineStatus();

                updateAdaptiveStatus();
            }
        });

        // Monitor
        btnTitleMonitor = new Button("MONITOR \nOFF");
        btnTitleMonitor.setAlignment(Pos.CENTER);
        btnTitleMonitor.setTextAlignment(TextAlignment.CENTER);
        btnTitleMonitor.getStyleClass().add("button-gradient5");
        btnTitleMonitor.setPrefWidth(150);
        btnTitleMonitor.setPrefHeight(50);
        btnTitleMonitor.setMaxWidth(200);
        btnTitleMonitor.setMinWidth(120);
        btnTitleMonitor.setOnAction(event -> {

            // * This button is not used by user click, so we hide.
            // * Monitor status is get from Fanuc

            // Change button Title to show on or off
            if (isMonitorOn()) {
                btnTitleMonitor.setText("MONITOR \nOFF");
                valueOptimize.setText("0");
            } else {
                btnTitleMonitor.setText("MONITOR \nON");
                //uiVars.Optimize = 1;
                valueOptimize.setText("1");
            }
        });
        btnTitleMonitor.setVisible(false);
        //hGaugeHomeBox.getChildren().addAll(btnTitleMonitor);

        valueFeedHoldStatus = new Label("0");
        valueFeedHoldStatus.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (newValue.equals("0") && isMonitorOn()) {
                    // FEED FOLD is now turnoff and elapsed timer was paused due to the FEED HOLD?
                    // Resume the elapsed timer
                    if (stopWatch != null && stopWatch.isPaused()) {
                        stopWatch.resume();
                    }
                } else if(newValue.equals("1") && isMonitorOn()) {
                    // Now enter to the FEED FOLD and elapsed timer was already running
                    // Pause the elapsed timer
                    if (stopWatch != null && stopWatch.isRunning()) {
                        stopWatch.pause();
                    }
                }
            }
        });

        btnChangeVars = new Button("CHANGE VARS");
        btnChangeVars.setOnMouseClicked(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                if (getValueProgNum() > 0) {

                    Random r = new Random();
                    int randInd = r.nextInt(JobDataManager.getInstance().getJobDataList().size());

                    uiVars.Tool = JobDataManager.getInstance().getJobDataList().get(randInd).tool;
                    uiVars.Section = JobDataManager.getInstance().getJobDataList().get(randInd).section;
                    uiVars.Channel = JobDataManager.getInstance().getJobDataList().get(randInd).channel;
                    valueTool.setText(String.valueOf(uiVars.Tool));
                    valueSection.setText(String.valueOf(uiVars.Section));
                    valueChannel.setText(String.valueOf(uiVars.Channel));

                }
                //MainView.getInstance().setToolBarBox(Integer.parseInt(valueProgNum.getText()), randInd);
            }
        });

        btnChangeVars.setAlignment(Pos.CENTER);
        btnChangeVars.getStyleClass().add("button-gradient5");
        btnChangeVars.setPrefWidth(200);
        btnChangeVars.setPrefHeight(60);
        btnChangeVars.setMaxWidth(200);
        btnChangeVars.setMinWidth(120);
        // hGaugeHomeBox.getChildren().addAll(btnChangeVars);

        Button btnFanucZero = new Button("Fanuc 0 VARS");
        btnFanucZero.setOnMouseClicked(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                uiVars.ProgramNumber = 0;
                valueProgNum.setText(String.valueOf(uiVars.ProgramNumber));
                //MainView.getInstance().setToolBarBox(Integer.parseInt(valueProgNum.getText()), randInd);
            }
        });

        Button btnFanucNewProgramNumber = new Button("Fanuc new program Num");
        btnFanucNewProgramNumber.setOnMouseClicked(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                Random r = new Random();
                int randInd = r.nextInt(2000);
                int randTool = r.nextInt(100);
                int randSection = r.nextInt(100);
                int randChannel = r.nextInt(100);

                uiVars.ProgramNumber = randInd;
                uiVars.Tool = randTool;

                uiVars.Section = randSection;
                uiVars.Channel = randChannel;
                valueProgNum.setText(String.valueOf(uiVars.ProgramNumber));
                valueTool.setText(String.valueOf(uiVars.Tool));
                valueSection.setText(String.valueOf(uiVars.Section));
                valueChannel.setText(String.valueOf(uiVars.Channel));
                //MainView.getInstance().setToolBarBox(Integer.parseInt(valueProgNum.getText()), randInd);
            }
        });

        Button btnFanucNewTSC = new Button("Fanuc new TOOL SECTION OR CHANNEL");
        btnFanucNewTSC.setOnMouseClicked(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                Random r = new Random();
                int randInd = r.nextInt(15);
                int randInd1 = r.nextInt(15);
                int randInd2 = r.nextInt(15);

                uiVars.ProgramNumber = Integer.parseInt(valueProgNum.getText());
                uiVars.Tool = randInd;
                uiVars.Section = randInd1;
                uiVars.Channel = randInd2;

                valueProgNum.setText(String.valueOf(uiVars.ProgramNumber));
                valueTool.setText(String.valueOf(uiVars.Tool));
                valueSection.setText(String.valueOf(uiVars.Section));
                valueChannel.setText(String.valueOf(uiVars.Channel));
                //MainView.getInstance().setToolBarBox(Integer.parseInt(valueProgNum.getText()), randInd);
            }
        });

        Button btnSimulateTeachMode = new Button("SimulateChangeValuesDuringTeachMode");
        btnSimulateTeachMode.setOnMouseClicked(new EventHandler<Event>() {
            @Override
            public void handle(Event event) {
                Random r = new Random();
                int randInd = r.nextInt(3);
                int randInd1 = r.nextInt(3);
                int randInd2 = r.nextInt(3);
                int randInd3 = r.nextInt(3);
                int randInd4 = r.nextInt(3);
                int randInd5 = r.nextInt(3);
                int randInd6 = r.nextInt(3);
                int randInd7 = r.nextInt(3);
                int randInd8 = r.nextInt(3);
                int randInd9 = r.nextInt(3);
                int randInd10 = r.nextInt(3);
                int randInd11 = r.nextInt(3);
                int randInd12 = r.nextInt(3);
                int randInd13 = r.nextInt(3);
                int randInd14 = r.nextInt(3);
                int randInd15 = r.nextInt(3);


                uiVars.ProgramNumber = Integer.parseInt(valueProgNum.getText());
                valueProgNum.setText(String.valueOf(uiVars.ProgramNumber));

                uiVars.Target = randInd;
                uiVars.HighLimit = randInd2;
                uiVars.LowLimit = randInd1;
                uiVars.WearLimit = randInd3;
                uiVars.Idle = randInd4;
                uiVars.LowLimitTimer = randInd5;
                uiVars.AdaptiveFeedrateEnable = true;
                uiVars.MacroInterruptEnable = true;
                uiVars.LeadInFeedrate = randInd6;
                uiVars.LeadInFeedrateEnable = true;
                uiVars.StartDelay = randInd7;
                uiVars.HighLimitDelay = randInd8;
                uiVars.WearLimitDelay = randInd9;
                uiVars.AdaptiveFeedrateMin = randInd10;
                uiVars.AdaptiveFeedrateMax = randInd11;
                uiVars.AdaptiveWearLimit = randInd12;
                uiVars.AdaptiveHighLimit = randInd13;
                uiVars.Filter = randInd14;
                uiVars.SensorScaleSend = randInd15;
                //
                //  ADD MONITOR TIME HERE
                // uiVars.Time

                valueTarget.setText(String.format("%.02f", uiVars.Target));
                valueHighLimit.setText(String.format("%.02f", uiVars.HighLimit));
                valueLowLimitTimer.setText(String.format("%.0f", uiVars.LowLimit));
                //valueComment.setText("TEACH MODE RUNNING");
                valueIdle.setText(String.format("%.02f", uiVars.Idle));
                valueWearLimit.setText(String.format("%.02f", uiVars.WearLimit));
                btnAdaptiveEnable.setText(uiVars.AdaptiveFeedrateEnable ? "ADAPTIVE\nON" : "ADAPTIVE\nOFF");
                btnMacroInterruptEnable.setText(uiVars.MacroInterruptEnable ? "MACRO INTERRUPT\nON" : "MACRO INTERRUPT\nOFF");
                valueLeadInTrigger.setText(String.format("%.02f", uiVars.LeadInTrigger));
                btnLeadInFREnable.setText(uiVars.LeadInFeedrateEnable ? "LEAD-IN FR\nON" : "LEAD-IN FR\nOFF");
                valueStartDelay.setText(String.valueOf(uiVars.StartDelay));
                valueHighLimitDelay.setText(String.valueOf(uiVars.HighLimitDelay));
                valueWearLimitDelay.setText(String.valueOf(uiVars.WearLimitDelay));
                valueAdaptiveMin.setText(String.valueOf(uiVars.AdaptiveFeedrateMin));
                valueAdaptiveMax.setText(String.valueOf(uiVars.AdaptiveFeedrateMax));


                valueFilter.setText(String.valueOf(uiVars.Filter));
                valueSensorScaleSend.setText(String.valueOf(uiVars.SensorScaleSend));

                //
                // ADD MONITOR TIME
                // valueMonitorTime.setText(String.valueOf(uiVars.));

                //MainView.getInstance().setToolBarBox(Integer.parseInt(valueProgNum.getText()), randInd);
            }
        });
        //hbox.getChildren().addAll(btnFanucZero, btnFanucNewProgramNumber, btnFanucNewTSC, btnSimulateTeachMode);

        vBoxSaveVars = new VBox();
        vBoxSaveVars.setSpacing(5);
        vBoxSaveVars.setPadding(new Insets(1, 3, 1, 3));
        vBoxSaveVars.setAlignment(Pos.CENTER);
        vBoxSaveVars.setPrefWidth(150);
        vBoxFeedrate.setMaxWidth(150);
        vBoxFeedrate.setMinWidth(120);

        // We hide this combobox for now, because some cases, when click combobox, it is not working correctly.
        priorityComboBox = new ComboBox();
        priorityComboBox.getItems().addAll("TARGET", "HIGH", "WEAR", "IDLE", "LEAD-IN TRIGGER", "FILTER");
        priorityComboBox.setValue("TARGET");
        priorityComboBox.prefWidth(150);
        priorityComboBox.setMaxWidth(150);
        priorityComboBox.minWidth(120);
        priorityComboBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {

                updateSliderWithUIValues();
            }
        });
        priorityComboBox.setVisible(false);
        //priorityComboBox.getStyleClass().add("combo-box-base");
        HBox.setHgrow(priorityComboBox, Priority.ALWAYS);

        btnSaveJob = new Button("Update \nTool Data");
        btnSaveJob.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                updateToolDataFile();
            }
        });
        btnSaveJob.setAlignment(Pos.CENTER);
        btnSaveJob.setTextAlignment(TextAlignment.CENTER);
        btnSaveJob.getStyleClass().add("button-gradient5");
        btnSaveJob.setPrefWidth(150);
        btnSaveJob.setMaxWidth(150);
        btnSaveJob.setMinWidth(120);

        vBoxSaveVars.getChildren().addAll(/*priorityComboBox, */btnSaveJob);

        HBox.setHgrow(vBoxSaveVars, Priority.ALWAYS);

        hGaugeHomeBox.getChildren().add(vBoxSaveVars);

        return hGaugeHomeBox;
    }

    private VBox getNavMenuView() {
        VBox vNavMenuBox = new VBox();
        vNavMenuBox.setPadding(new Insets(20, 20, 20, 20));
        vNavMenuBox.setSpacing(15);
        vNavMenuBox.getStyleClass().add("navigation_view");

        vNavMenuBox.setPrefWidth(200);
        vNavMenuBox.setPrefHeight(400);
        vNavMenuBox.setMaxWidth(Double.NEGATIVE_INFINITY);
        vNavMenuBox.setMaxHeight(Double.MAX_VALUE);

        ImageView topLogo = new ImageView();
        topLogo.setImage(LogoManager.getInstance().getBannerLogo());
        topLogo.setFitWidth(200);
        topLogo.setPreserveRatio(true);
        topLogo.setSmooth(true);
        topLogo.setCache(true);

        // Top Image
        vNavMenuBox.getChildren().add(topLogo);

        // Menu Icons
        menuDasgboard = ViewHelper.makeNavMenuButton(Resource.NAV_MENU_ICON_SIZE, 5, LogoManager.getInstance().getMenuDashImage(), "Dashboard");
        menuViewHST = ViewHelper.makeNavMenuButton(Resource.NAV_MENU_ICON_SIZE, 5, LogoManager.getInstance().getMenuGraphImage(), "View HST File");
        menuTimeSavings = ViewHelper.makeNavMenuButton(Resource.NAV_MENU_ICON_SIZE, 5, LogoManager.getInstance().getMenuTimeSavingImage(), "Time Savings");
        menuViewAlarams = ViewHelper.makeNavMenuButton(Resource.NAV_MENU_ICON_SIZE, 5, LogoManager.getInstance().getAlertIcon(), "View Alarms");
        menuMMS = ViewHelper.makeNavMenuButton(Resource.NAV_MENU_ICON_SIZE, 5, LogoManager.getInstance().getMenuMMSImage(), "MMS");
        menuSettings = ViewHelper.makeNavMenuButton(Resource.NAV_MENU_ICON_SIZE, 5, LogoManager.getInstance().getMenuSettingImage(), "Settings");
        menuOptimizationReporting = ViewHelper.makeNavMenuButton(Resource.NAV_MENU_ICON_SIZE, 5, LogoManager.getInstance().getMenuMaintenanceImage(), "Optimization Reporting");
        menuSimFanuc = ViewHelper.makeNavMenuButton(Resource.NAV_MENU_ICON_SIZE, 5, LogoManager.getInstance().getMenuInstallImage(), "Fanuc Simulator");
        menuSimPLC = ViewHelper.makeNavMenuButton(Resource.NAV_MENU_ICON_SIZE, 5, LogoManager.getInstance().getMenuInstallImage(), "PLC Simulator");
        menuProcessMonitor = ViewHelper.makeNavMenuButton(Resource.NAV_MENU_ICON_SIZE, 5, LogoManager.getInstance().getMenuInstallImage(), "Process Monitor");
        menuCheckUpdate = ViewHelper.makeNavMenuButton(Resource.NAV_MENU_ICON_SIZE, 5, LogoManager.getInstance().getMenuCheckUpdateImage(), "Check Updates");

        menuDasgboard.setOnAction(navMenuHandler);
        menuViewHST.setOnAction(navMenuHandler);
        menuTimeSavings.setOnAction(navMenuHandler);
        menuViewAlarams.setOnAction(navMenuHandler);
        menuMMS.setOnAction(navMenuHandler);
        menuSettings.setOnAction(navMenuHandler);
        menuProcessMonitor.setOnAction(navMenuHandler);
        menuOptimizationReporting.setOnAction(navMenuHandler);
        menuSimFanuc.setOnAction(navMenuHandler);
        menuSimPLC.setOnAction(navMenuHandler);
        menuCheckUpdate.setOnAction(navMenuHandler);

        vNavMenuBox.getChildren().addAll(menuDasgboard, menuViewHST, menuTimeSavings, menuViewAlarams, menuMMS,
                menuSettings, menuOptimizationReporting, menuSimFanuc, menuSimPLC, menuProcessMonitor, menuCheckUpdate);

        return vNavMenuBox;
    }

    public class Stopwatch {
        Timeline timeline;
        Text text;

        int hours = 0, mins = 0, secs = 0, millis = 0;

        void change(Text text) {
            millis++; // increases milliseconds that get changed to min:sec format

            if (millis == 10) {
                secs++;
                millis = 0;
            }
            /*if (secs == 60) {
                mins++;
                secs = 0;
            }
            if (mins == 60) {
                hours++;
                mins = 0;
            }*/

            /*if (hours > 0) {
                // text.setText((((mins / 10) == 0) ? "0" : "") + mins + ":" + (((secs / 10) == 0) ? "0" : "") + secs);
                // text.setText(String.format("%02d:%02d:%02d secs", hours, mins, secs));
            } else {
                //text.setText((((mins / 10) == 0) ? "0" : "") + mins + ":" + (((secs / 10) == 0) ? "0" : "") + secs);
                //text.setText(String.format("%02d:%02d secs", mins, secs));
            }*/
            double passSecs = secs + millis * 0.1;
            text.setText(String.format("%.01f %s", passSecs, passSecs > 0 ? "secs" : "sec"));

            valueElapsedMonitorTime.setText(text.getText()); // Sets elapsed time value to current timer vals
        }

        public void start() {
            text = new Text("0.0 sec"); // Sets Default Text

            timeline = new Timeline(new KeyFrame(Duration.millis(100), new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    change(text);
                }
            }));
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.setAutoReverse(false);

            timeline.play();
        }

        public void stop() { // Stops timer and resets flashing teach buttons
            timeline.pause();
            timeline.stop();
            timeline.getKeyFrames().clear();

            if (isTeachOneToolMode() || isTeachMode()) {
                valueLearnedMonitorTime.setText(text.getText());
                valueElapsedMonitorTime.setText("0.0 sec");
                vBoxSaveVars.setVisible(true);

                boxHomeSliders.setVisible(true);
            }
        }

        public boolean isRunning() {
            return timeline.getStatus() == Animation.Status.RUNNING;
        }

        public boolean isPaused() {
            return timeline.getStatus() == Animation.Status.PAUSED;
        }

        public void pause() {
            if (timeline.getStatus() == Animation.Status.RUNNING) {
                timeline.pause();
            }
        }

        public void resume() {
            if (timeline.getStatus() == Animation.Status.PAUSED) {
                timeline.play();
            }
        }
    }

    public void updateMachineInfo() {
        String machineID = PreferenceManager.getMachineID();
        if (machineID == null || machineID.trim().isEmpty()) {
            labelMachine.setVisible(false);
        } else {
            labelMachine.setText(machineID);
            labelMachine.setVisible(true);
        }
    }

    public void updateMachineStatus() {

        if (USE_PLC_ALARM) {

            /*
            1."Idle"
                    - When the system is not in alarm, not in teach mode and not in monitor mode
            2."Monitor On"
                    - When Monitor=1 and TeachMode=0
            3."Monitor On - Teach Mode"
                    - When Monitor=1 and Teachmode=1
            4."Alarm - 'type of alarm'"
                    - High
                    - Wear
                    - Low
            5."Idle - Teach Mode"
                    - If Monitor=0 and Teachmode=1

            There are two methods for alarm to reset:
            1.User select "Reset Alarm" button on top ribbon of UI
            2.Monitor change from ON to OFF
            */

            if (uiVars.Alarm == PLC_ALARM_IDLE) {
                if (isTeachMode() && isMonitorOn()) {
                    labelStatusBar.setText("Monitor On - Teach Mode");
                } else if (isTeachMode()) {
                    labelStatusBar.setText("Idle - Teach Mode");
                } else if (isMonitorOn()) {
                    labelStatusBar.setText("Monitor On");
                } else {
                    labelStatusBar.setText("Idle");
                }
            } else if (uiVars.Alarm == PLC_ALARM_LOWLIMIT) {
                labelStatusBar.setText("Low Limit Alarm!");
            } else if (uiVars.Alarm == PLC_ALARM_WEARLIMIT) {
                labelStatusBar.setText("Wear Limit Alarm!");
            } else if (uiVars.Alarm == PLC_ALARM_HIGHLIMIT) {
                labelStatusBar.setText("High Limit Alarm!");
            }
        }
    }

    // Reset All Alarms
    // *System Alarm is reset WHEN:
    //1. User select reset button
    //2. When monitor changes from 0 to 1
    private void resetAlarm() {
        // Change Status
        currSystemAlarmStatus = SYSTEM_ALARM_IDLE;

        // Change UI
        timelineAnimAlarmStatus.pause();
        labelStatusBar.setText("IDLE");
        labelStatusBar.setStyle("-fx-border-color: white;-fx-text-fill: white;");

        // Stop Times
        highLimitTimer.reset();
        wearLimitTimer.reset();
        lowLimitTimer.reset();

        feedbackMacroInterruptSignalToFanuc(false);

        // We disable this logic, Signal Wear is not related with alarm reset or monitor.
        //feedbackToolWearSignalToFanuc(false);

        /*
        valueInnerTimers.setText(String.format("High Timer : %.1f\nWear Timer : %.1f\nLow Timer : %.1f",
                highLimitTimer.getTimeInSeconds(), wearLimitTimer.getTimeInSeconds(), lowLimitTimer.getTimeInSeconds()));

        valueSendSignals.setText(String.format("MacroIntrpt : %d\nToolWear : %d",
                uiVars.fanucMacroInterruptSignal ? 1 : 0, uiVars.fanucToolWearSignal ? 1 : 0));
         */
    }

    public void setNewToolNumber(int newJobID) {
        valueProgNum.setText(String.valueOf(newJobID));
        uiVars.isManualProgramNumber = true;

        // Reload Data
        try {
            setToolBarBox(newJobID);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Reload values using job ID
    public void setToolBarBox(int jobID) throws IOException {

        if (jobID == 0) {
            valueComment.setText("");
            valueTarget.setText("0.00");
            valueHighLimit.setText(String.format("%.02f", 0.0));
            valueWearLimit.setText(String.format("%.02f", 0.0));

            valueAdaptiveHighPercentage.setText(String.format("%.02f", (float) PreferenceManager.getAdaptiveHighLimit()));
            valueAdaptiveWearPercentage.setText(String.format("%.02f", (float) PreferenceManager.getAdaptiveWearLimit()));

            valueIdle.setText("0.00");
            valueLeadInTrigger.setText("0.00");

            valueLearnedLowLimitTimer.setText("0.00");
            btnAdaptiveEnable.setText(PreferenceManager.isAdaptiveEnabled() ? "ADAPTIVE\nON" : "ADAPTIVE\nOFF");
            btnMacroInterruptEnable.setText(PreferenceManager.isMacroInterruptEnabled() ? "MACRO INTERRUPT\nON" : "MACRO INTERRUPT\nOFF");
            btnLeadInFREnable.setText(PreferenceManager.isLeadInFREnabled() ? "LEAD-IN FR\nON" : "LEAD-IN FR\nOFF");

            valueStartDelay.setText("0");
            valueHighLimitDelay.setText(String.valueOf(PreferenceManager.getDefaultHighLimitDelay()));
            valueWearLimitDelay.setText(String.valueOf(PreferenceManager.getDefaultWearLimitDelay()));
            valueAdaptiveMin.setText(String.valueOf(PreferenceManager.getAdaptiveMin()));
            valueAdaptiveMax.setText(String.valueOf(PreferenceManager.getAdaptiveMax()));
            valueFilter.setText(String.valueOf(PreferenceManager.getDefaultFilter()));
            valueSensorScaleSend.setText("0");
            // valueMonitorTime.setText("0");
        } else {
            JobData curJobData = JobDataManager.getInstance().getJobData(jobID, uiVars.Tool, uiVars.Section, uiVars.Channel);
            if (curJobData != null) {
                valueComment.setText(String.valueOf(curJobData.comment));
                valueTarget.setText(String.format("%.02f", curJobData.target));
                valueHighLimit.setText(String.format("%.02f", curJobData.highLimit));
                valueWearLimit.setText(String.format("%.02f", curJobData.wearLimit));

                valueAdaptiveHighPercentage.setText(String.format("%.02f", curJobData.adaptiveHighLimit));
                valueAdaptiveWearPercentage.setText(String.format("%.02f", curJobData.adaptiveWearLimit));

                valueIdle.setText(String.format("%.02f", curJobData.idle));
                valueLeadInTrigger.setText(String.format("%.02f", curJobData.leadInTrigger));

                valueLearnedLowLimitTimer.setText(String.valueOf(curJobData.lowLimitTime));
                btnAdaptiveEnable.setText(curJobData.adaptiveEnable ? "ADAPTIVE\nON" : "ADAPTIVE\nOFF");
                btnMacroInterruptEnable.setText(curJobData.macroInterruptEnable ? "MACRO INTERRUPT\nON" : "MACRO INTERRUPT\nOFF");
                btnLeadInFREnable.setText(curJobData.leadInEnable ? "LEAD-IN FR\nON" : "LEAD-IN FR\nOFF");

                valueStartDelay.setText(String.valueOf(curJobData.startDelay));
                valueHighLimitDelay.setText(String.valueOf(curJobData.highLimitDelay));
                valueWearLimitDelay.setText(String.valueOf(curJobData.wearLimitDelay));
                valueAdaptiveMin.setText(String.valueOf(curJobData.adaptiveMin));
                valueAdaptiveMax.setText(String.valueOf(curJobData.adaptiveMax));
                valueFilter.setText(String.valueOf(curJobData.filter));
                valueSensorScaleSend.setText(String.valueOf(curJobData.sensorScaleSend));
                // valueMonitorTime.setText(String.valueOf(curJobData.monitorTime));

                // Show Time
                double totalSecs = curJobData.monitorTime;
                /*int hours = totalSecs / 3600;
                totalSecs -= hours * 3600;
                int mins = totalSecs / 60;
                totalSecs -= mins * 60;
                int secs = totalSecs;
                if (hours > 0) {
                    valueLearnedMonitorTime.setText(String.format("%02d:%02d:%02d secs", hours, mins, secs));
                } else {
                    valueLearnedMonitorTime.setText(String.format("%02d:%02d secs", mins, secs));
                }*/
                valueLearnedMonitorTime.setText(String.format("%.01f %s", totalSecs, totalSecs > 0 ? "secs" : "sec"));

                sliderAdjustFilter.setValue(curJobData.filter);

                String val = String.valueOf(priorityComboBox.getValue());
                switch (val) {
                    case "TARGET":
                        double valueTarget = curJobData.target;
                        labelSliderValue.setText(String.format("%.02f", valueTarget));
                        sliderAdjust.setValue(valueTarget);

                        break;
                    case "HIGH":
                        double valueHigh = curJobData.highLimit;
                        labelSliderValue.setText(String.format("%.02f", valueHigh));
                        sliderAdjust.setValue(valueHigh);

                        break;
                    case "WEAR":
                        double valueWear = curJobData.wearLimit;
                        labelSliderValue.setText(String.format("%.02f", valueWear));
                        sliderAdjust.setValue(valueWear);

                        break;
                    case "IDLE":
                        double valueIdle = curJobData.idle;
                        labelSliderValue.setText(String.format("%.02f", valueIdle));
                        sliderAdjust.setValue(valueIdle);

                        break;
                    case "LEAD-IN TRIGGER":
                        double valueLeadInFR = curJobData.leadInTrigger;
                        labelSliderValue.setText(String.format("%.02f", valueLeadInFR));
                        sliderAdjust.setValue(valueLeadInFR);

                        break;
                    case "FILTER":
                        double valueFilter = curJobData.filter;
                        labelSliderFilterValue.setText(String.format("%.02f", valueFilter));
                        sliderAdjustFilter.setValue(valueFilter);

                        break;
                }

                // Focus Target value
                priorityComboBox.setValue("TARGET");
                sliderAdjust.setValue(curJobData.target);
                valueTarget.setText(String.format("%.02f", curJobData.target));

                Toast.message("Reloaded Tool Data!");
            } else {
                // Tool Data is not exists!
                Toast.message("No Tool Data!");
            }

            // * Event no tool data, send default values to the PLC
            if (!isTeachMode()) {
                feedbackPLCValues(curJobData);
            }

            feedbackAdaptiveMinMaxValueToFanuc(curJobData.adaptiveMax, curJobData.adaptiveMin);
        }
    }

    // Triggers for UI to READ tool file:
    public void reloadToolData() {
        try {
            // * TCS is changed and reload tool data
            // * This works in case of teachmode=0
            if (!isTeachMode()) {
                setToolBarBox(Integer.parseInt(valueProgNum.getText()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateSliderWithUIValues() {
        String val = String.valueOf(priorityComboBox.getValue());

        switch (val) {
            case "TARGET":
                showSlider(1);

                double valueTarget = getValueTarget();
                labelSliderValue.setText(String.format("%.02f", valueTarget));
                labelSliderValue.setTextFill(Color.rgb(0, 128, 0));

                sliderAdjust.setValue(valueTarget);
                VBox.setVgrow(sliderAdjust, Priority.ALWAYS);
                setSliderTrackColor("green");

                break;
            case "HIGH":
                showSlider(1);

                double valueHigh = getValueHighLimit();
                labelSliderValue.setText(String.format("%.02f", valueHigh));
                labelSliderValue.setTextFill(Color.rgb(255, 0, 0));

                sliderAdjust.setValue(valueHigh);
                VBox.setVgrow(sliderAdjust, Priority.ALWAYS);
                setSliderTrackColor("red");

                break;
            case "WEAR":
                showSlider(1);

                double valueWear = getValueWearLimit();
                labelSliderValue.setText(String.format("%.02f", valueWear));
                sliderAdjust.setValue(valueWear);
                labelSliderValue.setTextFill(Color.rgb(255, 165, 0));

                VBox.setVgrow(sliderAdjust, Priority.ALWAYS);
                setSliderTrackColor("orange");

                break;
            case "IDLE":
                showSlider(1);

                double valueIdle = getValueIdle();
                labelSliderValue.setText(String.format("%.02f", valueIdle));
                sliderAdjust.setValue(valueIdle);
                labelSliderValue.setTextFill(Color.rgb(255, 255, 255));

                VBox.setVgrow(sliderAdjust, Priority.ALWAYS);
                setSliderTrackColor("white");

                break;
            case "LEAD-IN TRIGGER":
                showSlider(1);

                double valueLeadInFR = getValueLeadInTrigger();
                labelSliderValue.setText(String.format("%.02f", valueLeadInFR));
                sliderAdjust.setValue(valueLeadInFR);
                labelSliderValue.setTextFill(Color.rgb(255, 255, 0));

                VBox.setVgrow(sliderAdjust, Priority.ALWAYS);
                setSliderTrackColor("yellow");

                break;
            case "FILTER":
                showSlider(0);

                double valueFilter = getValueFilter();
                labelSliderFilterValue.setText(String.format("%.02f", valueFilter));
                sliderAdjustFilter.setValue(valueFilter);
                VBox.setVgrow(sliderAdjustFilter, Priority.ALWAYS);

                // * Filter slider use stand-alone slider, so no need to change slider.
                //setSliderTrackColor("orange");
                break;
        }
    }

    private void showSlider(int showFilterSlider) {
        if (showFilterSlider == 0) {
            vBoxSliderFilter.setVisible(true);
            vBoxSlider.setVisible(false);
            vBoxSliderAdaptive.setVisible(false);
        } else if (showFilterSlider == 1) {
            vBoxSliderFilter.setVisible(false);
            vBoxSlider.setVisible(true);
            vBoxSliderAdaptive.setVisible(false);
        } else if (showFilterSlider == 2) {
            vBoxSliderFilter.setVisible(false);
            vBoxSlider.setVisible(false);
            vBoxSliderAdaptive.setVisible(true);
        }
    }

    public void setSliderTrackColor(String newColor) {
        if (priorityComboBox.getValue() == "FILTER") {
            // Only sets the filter slider values
            // * When load UI, this was already set, so no need to call again here
            /*sliderFilterAdjust.getStyleClass().add("slider-" + newColor);
            sliderFilterAdjust.styleProperty().bind(Bindings.createStringBinding(() -> {
                double percentage = (sliderFilterAdjust.getValue() - sliderFilterAdjust.getMin()) / (sliderFilterAdjust.getMax() - sliderFilterAdjust.getMin()) * 100.0;
                return String.format("-slider-" + newColor + "-track-color: linear-gradient(to top, -slider-" + newColor + "-filled-track-color 0%%, "
                                + "-slider-" + newColor + "-filled-track-color %f%%, -fx-base %f%%, -fx-base 100%%);",
                        percentage, percentage);
            }, sliderFilterAdjust.valueProperty(), sliderFilterAdjust.minProperty(), sliderFilterAdjust.maxProperty()));*/
        } else {
            sliderAdjust.styleProperty().unbind(); // removes previous color bindings

            //// Parses old style and replaces with new style
            String oldStyle = sliderAdjust.getStyleClass().toString();
            oldStyle = oldStyle.substring(oldStyle.indexOf("-") + 1, oldStyle.length());
//                sliderAdjust.getStyleClass().removeAll()
            sliderAdjust.getStyleClass().removeAll("slider-" + oldStyle);
            sliderAdjust.getStyleClass().add("slider-" + newColor);

            // Binds styles to new colors and fills track up to the thumb
            sliderAdjust.styleProperty().bind(Bindings.createStringBinding(() -> {
                double percentage = (sliderAdjust.getValue() - sliderAdjust.getMin()) / (sliderAdjust.getMax() - sliderAdjust.getMin()) * 100.0;
                return String.format("-slider-" + newColor + "-track-color: linear-gradient(to top, -slider-" + newColor + "-filled-track-color 0%%, "
                                + "-slider-" + newColor + "-filled-track-color %f%%, -fx-base %f%%, -fx-base 100%%);",
                        percentage, percentage);
            }, sliderAdjust.valueProperty(), sliderAdjust.minProperty(), sliderAdjust.maxProperty()));
        }
    }

    public void setAdaptiveSliderTrackColor(String newColor) {
        sliderAdjustAdaptive.styleProperty().unbind(); // removes previous color bindings

        //// Parses old style and replaces with new style
        String oldStyle = sliderAdjustAdaptive.getStyleClass().toString();
        oldStyle = oldStyle.substring(oldStyle.indexOf("-") + 1, oldStyle.length());
//                sliderAdjust.getStyleClass().removeAll()
        sliderAdjustAdaptive.getStyleClass().removeAll("slider-" + oldStyle);
        sliderAdjustAdaptive.getStyleClass().add("slider-" + newColor);

        // Binds styles to new colors and fills track up to the thumb
        sliderAdjustAdaptive.styleProperty().bind(Bindings.createStringBinding(() -> {
            double percentage = (sliderAdjustAdaptive.getValue() - sliderAdjustAdaptive.getMin()) / (sliderAdjustAdaptive.getMax() - sliderAdjustAdaptive.getMin()) * 100.0;
            return String.format("-slider-" + newColor + "-track-color: linear-gradient(to top, -slider-" + newColor + "-filled-track-color 0%%, "
                            + "-slider-" + newColor + "-filled-track-color %f%%, -fx-base %f%%, -fx-base 100%%);",
                    percentage, percentage);
        }, sliderAdjustAdaptive.valueProperty(), sliderAdjustAdaptive.minProperty(), sliderAdjustAdaptive.maxProperty()));
    }

    public boolean isValidPLCData(int[] plcRegisters) {
        for (int value : plcRegisters) {
            if (value != 0) {
                return true;
            }
        }
        return false;
    }

    public boolean isTeachMode() {
        return btnTeachMode.getText().equalsIgnoreCase("TEACH MODE\nON");
    }

    public boolean isTeachOneToolMode() {
        return btnTeachOnetool.getText().equalsIgnoreCase("TEACH ONE TOOL\nON");
    }

    public double getValueTarget() {
        return parseDoubleValue(valueTarget.getText());
    }

    public String getValueComment() {
        return valueComment.getText();
    }

    public double getValueHighLimit() {
        return parseDoubleValue(valueHighLimit.getText());
    }

    public double getValueLowLimitTimer() {
        return parseDoubleValue(valueLowLimitTimer.getText());
    }

    public double getValueLearnedLowLimitTimer() {
        return parseDoubleValue(valueLearnedLowLimitTimer.getText());
    }

    public void setSimulatorHPValue(float simHP) {
        uiVars.HP = simHP;
        valueHp.setText(String.format("%.2f", uiVars.HP));
    }

    public double getValueHP() {
        return parseDoubleValue(valueHp.getText());
    }

    public double getValueChartScale() {
        return applet.getChartScale();
    }

    public void setSimulatorScaleValue(float scale) {
        uiVars.SensorScale = scale;
        applet.setChartScale(scale);

        feedbackPVMinMaxValueToFanuc(scale, 0);
    }

    public double getValueWearLimit() {
        return parseDoubleValue(valueWearLimit.getText());
    }

    public double getValueFeedrateScale() {
        double scale = 3;
        // Adjust Scales
        if (uiVars.SensorScale > 0) {
            scale = uiVars.SensorScale;
        }
        String strFeedrate = valueFeedrate.getText().replaceAll("%", "");
        double valueFeedrate = parseDoubleValue(strFeedrate);

        return (valueFeedrate * (scale - 0.05)) / 200.0;
    }

    public int getCurrentSequenceNumber() {
        return uiVars.currentSequenceNumber;
    }

    public float getHP2() {
        return uiVars.HP2;
    }

    public String getModalData() {
        String modalData = String.format("%d,%d,%f,%d,%d,%f,%d,%d,%f,%d,%d,%f",
                uiVars.sModal.dataNo, uiVars.sModal.type, uiVars.sModal.gData,
                uiVars.tModal.dataNo, uiVars.tModal.type, uiVars.tModal.gData,
                uiVars.fModal.dataNo, uiVars.fModal.type, uiVars.fModal.gData,
                uiVars.mModal.dataNo, uiVars.mModal.type, uiVars.mModal.gData);
        return modalData;
    }

    public double getValueFeedrate() {

        String strFeedrate = valueFeedrate.getText().replaceAll("%", "");
        double valueFeedrate = parseDoubleValue(strFeedrate);

        return valueFeedrate;
    }

    public int getValueAdaptiveHighLimit() {
        String strHighPer = valueAdaptiveHighPercentage.getText();
        int valueHighLimitPer = parseIntValue(strHighPer);
        return valueHighLimitPer;
    }

    public double getValueAdaptiveHighPercentage() {
        double scale = 3;
        // Adjust Scales
        if (uiVars.SensorScale > 0) {
            scale = uiVars.SensorScale;
        }
        String strHighPer = valueAdaptiveHighPercentage.getText();
        double valueHighPer = parseDoubleValue(strHighPer);

        return (valueHighPer * (scale - 0.05)) / 200.0;
    }

    public int getValueAdaptiveWearLimit() {
        String strWearPer = valueAdaptiveWearPercentage.getText();
        int valueWearLimitPer = parseIntValue(strWearPer);
        return valueWearLimitPer;
    }

    public double getValueAdaptiveWearPercentage() {
        double scale = 3;
        // Adjust Scales
        if (uiVars.SensorScale > 0) {
            scale = uiVars.SensorScale;
        }
        String strWearPer = valueAdaptiveWearPercentage.getText();
        double valueWearPer = parseDoubleValue(strWearPer);

        return (valueWearPer * (scale - 0.05)) / 200.0;
    }

    public double getValueLeadInTrigger() {
        return parseDoubleValue(valueLeadInTrigger.getText());
    }

    public double getValueIdle() {
        return parseDoubleValue(valueIdle.getText());
    }

    public double getValueFilter() {
        return parseDoubleValue(valueFilter.getText());
    }

    public boolean isAdaptiveOn() {
        return btnAdaptiveEnable.getText().equalsIgnoreCase("ADAPTIVE\nON");
    }

    public boolean isMacroInterruptOn() {
        return btnMacroInterruptEnable.getText().equalsIgnoreCase("MACRO INTERRUPT\nON");
    }

    public boolean isLeadInFROn() {
        return btnLeadInFREnable.getText().equalsIgnoreCase("LEAD-IN FR\nON");
    }

    public boolean isMonitorOn() {
        if (uiVars.Optimize > 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean getInCycleStatus() {
        return uiVars.InCycle > 0;
    }

    public double getValueStartDelay() {
        return parseDoubleValue(valueStartDelay.getText());
    }

    public double getValueHighLimitDelay() {
        return parseDoubleValue(valueHighLimitDelay.getText());
    }

    public double getValueWearLimitDelay() {
        return parseDoubleValue(valueWearLimitDelay.getText());
    }

    public double getValueAdaptiveMin() {
        return parseDoubleValue(valueAdaptiveMin.getText());
    }

    public double getValueAdaptiveMax() {
        return parseDoubleValue(valueAdaptiveMax.getText());
    }

    public double getValueSensorScaleSend() {
        return parseDoubleValue(valueSensorScaleSend.getText());
    }

    public double getValueLearnedMonitorTime() {
        double secs = 0;
        String timeValue = valueLearnedMonitorTime.getText().replace("secs", "").replace("sec", "").trim();
        secs = parseDoubleValue(timeValue);

        return secs;
    }

    public double getValueElapsedMonitorTime() {
        double secs = 0;
        String timeValue = valueElapsedMonitorTime.getText().replace("secs", "").replace("sec", "").trim();
        secs = parseDoubleValue(timeValue);

        return secs;
    }

    public int getValueProgNum() {
        return parseIntValue(valueProgNum.getText());
    }

    public int getValueTool() {
        return parseIntValue(valueTool.getText());
    }

    public int getValueSection() {
        return parseIntValue(valueSection.getText());
    }

    public int getValueChannel() {
        return parseIntValue(valueChannel.getText());
    }

    private double parseDoubleValue(String value) {
        if (value == null && value.trim().isEmpty()) {
            return 0;
        }

        double ret = 0;
        try {
            ret = Double.parseDouble(value.trim());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    private float parseFloatValue(String value) {
        if (value == null && value.trim().isEmpty()) {
            return 0;
        }

        float ret = 0;
        try {
            ret = Float.parseFloat(value.trim());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
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

    private boolean checkPermissionStatus(int permissionLevel) {
        if (PreferenceManager.getUserLevel() >= permissionLevel) {
            return true;
        }

        return false;
    }

    public void askUserToLogin(int permissionLevel, LoginResultListener loginListener) {
        String alertMessage = "";
        if (PreferenceManager.getUserLevel() == -1) {
            alertMessage = "Pleaes login to use function";
        } else if (PreferenceManager.getUserLevel() < permissionLevel) {
            alertMessage = "Permission is not allowed, would you like to login with your account?";
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.WINDOW_MODAL);
        alert.setTitle("Warning!");
        Stage dialogStage = (Stage) alert.getDialogPane().getScene().getWindow();
        dialogStage.getIcons().add(LogoManager.getInstance().getLogo());
        alert.setHeaderText("Permission is not allowed!");
        alert.setContentText(alertMessage);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            LoginView.getInstance().show(loginListener);
        }
    }

    // This function is not used
    public void createJob(int jobID) {
        try {
            if (JobDataManager.getInstance().isFileExists(jobID)) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.initModality(Modality.WINDOW_MODAL);
                alert.setTitle("Confirm Overwrite");
                Stage dialogStage = (Stage) alert.getDialogPane().getScene().getWindow();
                dialogStage.getIcons().add(LogoManager.getInstance().getLogo());
                alert.setHeaderText("Job Data Already Exists.");
                alert.setContentText("Are you sure you want to overwrite?");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    // Overwrite Job Data
                    if (JobDataManager.getInstance().createJobData(jobID, new JobData())) {
                        AlertManager.getInstance().showAlert("Success", "New Job File Data successfully created!");
                        JobDataManager.getInstance().loadJobData(jobID);
                    }
                }
            } else {
                // New Job Data
                if (JobDataManager.getInstance().createJobData(jobID, new JobData())) {
                    JobDataManager.getInstance().loadJobData(jobID);
                    AlertManager.getInstance().showAlert("Success", "New Job File Data successfully created!");
                    setToolBarBox(jobID);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            AlertManager.getInstance().showAlert("Error", e.getMessage());
        }
    }

    private void feedbackSwitchStatus() {

        System.out.println("Send switch to PLC!");

        // Send current switch status to PLC
        ModbusClient modbusPLCClient = getNewPLCConn();

        if (modbusPLCClient != null && modbusPLCClient.isConnected()) {
            final boolean[] switchStatus = new boolean[5];
            switchStatus[0] = isTeachMode();
            switchStatus[1] = isAdaptiveOn();
            switchStatus[2] = isLeadInFROn();
            switchStatus[3] = isMacroInterruptOn();
            switchStatus[4] = uiVars.Optimize > 0;

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        modbusPLCClient.WriteMultipleCoils(0, switchStatus);

                        modbusPLCClient.Disconnect();
                    } catch (ModbusException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    public void feedbackPLCValues(JobData jobData) {
        System.out.println("Send data to PLC!");

        ModbusClient modbusPLCClient = getNewPLCConn();

        // Send current switch status to PLC
        if (modbusPLCClient != null && modbusPLCClient.isConnected()) {
            // Write Coils Status
            final boolean[] switchStatus = new boolean[5];
            switchStatus[0] = isTeachMode();
            switchStatus[1] = isAdaptiveOn();
            switchStatus[2] = isLeadInFROn();
            switchStatus[3] = isMacroInterruptOn();
            switchStatus[4] = uiVars.Optimize > 0;

            // Write Holdings Status
            final int[] holdingPLCRegs = new int[40];

            // * Send all modbus to PLC even if no tool data
            if (jobData == null) {

                holdingPLCRegs[0] = (int) (uiVars.HP * 100);
                holdingPLCRegs[1] = (int) (uiVars.Target * 100);
                holdingPLCRegs[2] = (int) (uiVars.HighLimit * 100);
                holdingPLCRegs[3] = (int) (uiVars.WearLimit * 100);
                holdingPLCRegs[4] = (int) (getValueLearnedLowLimitTimer());

                // * 40006(5) is used to read scale,
                // * For writing, we use 40037(36)
                //holdingPLCRegs[5] = (int) (jobData.sensorScaleSend * 100);
                holdingPLCRegs[36] = (int) (10 * 100);

                holdingPLCRegs[6] = (int) (uiVars.LearnedMonitorTime);
                holdingPLCRegs[7] = (int) (uiVars.ElapsedMonitorTime);

                holdingPLCRegs[8] = (int) (uiVars.LowLimitTimer);
                holdingPLCRegs[9] = (int) (uiVars.TotalWork * 100);
                holdingPLCRegs[10] = (int) (uiVars.Work * 100);
                holdingPLCRegs[11] = (int) (PreferenceManager.getDefaultHighLimitDelay() * 100);
                holdingPLCRegs[12] = (int) (PreferenceManager.getDefaultWearLimitDelay() * 100);
                holdingPLCRegs[13] = (int) (PreferenceManager.getLowLimitErrorPercent() * 100);
                holdingPLCRegs[14] = (int) (PreferenceManager.getPIDGain() * 1000);
                holdingPLCRegs[15] = (int) (PreferenceManager.getPIDReset() * 1000);
                holdingPLCRegs[16] = (int) (PreferenceManager.getPIDRate() * 1000);
                holdingPLCRegs[17] = (int) (PreferenceManager.getDefaultFilter() * 100);
                holdingPLCRegs[18] = (int) (PreferenceManager.getHighLimitPercent() * 100);
                holdingPLCRegs[19] = (int) (PreferenceManager.getWearLimitPercent() * 100);
                holdingPLCRegs[20] = (int) (PreferenceManager.getTargetLoadPercent() * 100);
                holdingPLCRegs[21] = (int) (PreferenceManager.getScaleChartAboveHighLimit() * 100);
                holdingPLCRegs[22] = (int) (getValueLeadInTrigger() * 100);
                holdingPLCRegs[23] = (int) (PreferenceManager.getDefaultStartDelay() * 100);
                holdingPLCRegs[24] = (int) (uiVars.TimeSavingsSecond * 100);
                holdingPLCRegs[25] = (int) (uiVars.TimeSavingsPercent * 100);

                holdingPLCRegs[26] = PreferenceManager.getSensorAddress1();
                holdingPLCRegs[27] = PreferenceManager.getSensorAddress2();
                holdingPLCRegs[28] = PreferenceManager.getSensorAddress3();

                holdingPLCRegs[29] = (int) (PreferenceManager.getAdaptiveMin() * 100);
                holdingPLCRegs[30] = (int) (PreferenceManager.getAdaptiveMax() * 100);
                holdingPLCRegs[31] = (int) (PreferenceManager.getAdaptiveHighLimit() * 100);
                holdingPLCRegs[32] = (int) (PreferenceManager.getAdaptiveWearLimit() * 100);
                holdingPLCRegs[33] = (int) (PreferenceManager.getDefaultLeadInFeedrate() * 100);

                holdingPLCRegs[34] = (int) (uiVars.Idle * 100);
                holdingPLCRegs[35] = (int) (uiVars.Feedrate * 100);
                holdingPLCRegs[36] = (int) (10 * 100);
                holdingPLCRegs[37] = getValueChannel();

                holdingPLCRegs[39] = (int) (PreferenceManager.getDefaultLeadInTrigger() * 100);
            } else {

                holdingPLCRegs[0] = (int) (uiVars.HP * 100);
                holdingPLCRegs[1] = (int) (jobData.target * 100);
                holdingPLCRegs[2] = (int) (jobData.highLimit * 100);
                holdingPLCRegs[3] = (int) (jobData.wearLimit * 100);
                holdingPLCRegs[4] = (int) (getValueLearnedLowLimitTimer());

                // * 40006(5) is used to read scale,
                // * For writing, we use 40037(36)
                //holdingPLCRegs[5] = (int) (jobData.sensorScaleSend * 100);
                holdingPLCRegs[36] = (int) (jobData.sensorScaleSend * 100);

                holdingPLCRegs[6] = (int) (jobData.monitorTime);
                holdingPLCRegs[7] = (int) (uiVars.ElapsedMonitorTime);

                holdingPLCRegs[8] = (int) (jobData.lowLimitTime);
                holdingPLCRegs[9] = (int) (uiVars.TotalWork * 100);
                holdingPLCRegs[10] = (int) (uiVars.Work * 100);
                holdingPLCRegs[11] = (int) (jobData.highLimitDelay * 100);
                holdingPLCRegs[12] = (int) (jobData.wearLimitDelay * 100);
                holdingPLCRegs[13] = (int) (PreferenceManager.getLowLimitErrorPercent() * 100);
                holdingPLCRegs[14] = (int) (PreferenceManager.getPIDGain() * 1000);
                holdingPLCRegs[15] = (int) (PreferenceManager.getPIDReset() * 1000);
                holdingPLCRegs[16] = (int) (PreferenceManager.getPIDRate() * 1000);
                holdingPLCRegs[17] = (int) (jobData.filter * 100);
                holdingPLCRegs[18] = (int) (PreferenceManager.getHighLimitPercent() * 100);
                holdingPLCRegs[19] = (int) (PreferenceManager.getWearLimitPercent() * 100);
                holdingPLCRegs[20] = (int) (PreferenceManager.getTargetLoadPercent() * 100);
                holdingPLCRegs[21] = (int) (PreferenceManager.getScaleChartAboveHighLimit() * 100);
                holdingPLCRegs[22] = (int) (getValueLeadInTrigger() * 100);
                holdingPLCRegs[23] = (int) (PreferenceManager.getDefaultStartDelay() * 100);
                holdingPLCRegs[24] = (int) (uiVars.TimeSavingsSecond * 100);
                holdingPLCRegs[25] = (int) (uiVars.TimeSavingsPercent * 100);

                holdingPLCRegs[26] = PreferenceManager.getSensorAddress1();
                holdingPLCRegs[27] = PreferenceManager.getSensorAddress2();
                holdingPLCRegs[28] = PreferenceManager.getSensorAddress3();

                holdingPLCRegs[29] = (int) (jobData.adaptiveMin * 100);
                holdingPLCRegs[30] = (int) (jobData.adaptiveMax * 100);
                holdingPLCRegs[31] = (int) (jobData.adaptiveHighLimit * 100);
                holdingPLCRegs[32] = (int) (jobData.adaptiveWearLimit * 100);
                holdingPLCRegs[33] = (int) (jobData.leadInFeedrate * 100);

                holdingPLCRegs[34] = (int) (jobData.idle * 100);
                holdingPLCRegs[35] = (int) (uiVars.Feedrate * 100);
                holdingPLCRegs[36] = (int) (jobData.sensorScaleSend * 100);
                holdingPLCRegs[37] = getValueChannel();

                holdingPLCRegs[39] = (int) (PreferenceManager.getDefaultLeadInTrigger() * 100);

                sendSensorScale(jobData.sensorScaleSend);
            }

            new Thread(new Runnable() {
                @Override
                public void run() {
                    String msg = "Success to write PLC!";
                    try {
                        modbusPLCClient.WriteMultipleCoils(0, switchStatus);
                        modbusPLCClient.WriteMultipleRegisters(0, holdingPLCRegs);

                        modbusPLCClient.Disconnect();
                    } catch (ModbusException e) {
                        e.printStackTrace();

                        LogManager.getInstance().addNewLog(msg, isTeachMode(), isMonitorOn());
                        msg = "Failed to write PLC: " + e.getMessage();
                    } catch (IOException e) {
                        e.printStackTrace();
                        msg = "Failed to write PLC: " + e.getMessage();

                        LogManager.getInstance().addNewLog(msg, isTeachMode(), isMonitorOn());
                    }

                    String finalMsg = msg;
                    /*Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            Toast.message(finalMsg);
                        }
                    });*/
                }
            }).start();
        } else {
            //Toast.message("PLC connection Error!");
            LogManager.getInstance().addNewLog("Feedback PLC Failed, socket closed!", isTeachMode(), isMonitorOn());
        }
    }

    // Send sensor scale
    boolean checkNewSensorScale = false;
    public void sendSensorScale(float scale) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String scaleHPIP = PreferenceManager.getScaleHPIP();
                if (!TextUtils.isEmpty(scaleHPIP)) {
                    HttpGet getRequest = new HttpGet(String.format("http://%s/user.spi?fshp=%d", scaleHPIP, (int) (scale * 10)));
                    try {
                        HttpClient httpClient = HttpClientBuilder.create().build();
                        HttpResponse result = httpClient.execute(getRequest);
                        String response = EntityUtils.toString(result.getEntity(), "UTF-8");

                        checkNewSensorScale = true;
                        /*getRequest = new HttpGet(String.format("http://%s/user.htm", scaleHPIP));
                        httpClient = HttpClientBuilder.create().build();
                        result = httpClient.execute(getRequest);
                        response = EntityUtils.toString(result.getEntity(), "UTF-8");

                        String finalResponse = response;
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                Toast.message(finalResponse);
                            }
                        });*/
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void readSensorScale() {
        String scaleHPIP = PreferenceManager.getScaleHPIP();
        if (!TextUtils.isEmpty(scaleHPIP)) {
            try {
                HttpGet getRequest = new HttpGet(String.format("http://%s/user.htm", scaleHPIP));
                HttpClient httpClient = HttpClientBuilder.create().build();
                HttpResponse result = httpClient.execute(getRequest);
                String response = EntityUtils.toString(result.getEntity(), "UTF-8");
                if (!TextUtils.isEmpty(response)) {
                    String[] values = response.split(" ");
                    if (values != null && values.length > 0) {
                        try {
                            uiVars.SensorScale = Float.parseFloat(values[0]);
                            String finalResponse = response;
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.message(finalResponse);
                                    applet.setChartScale(uiVars.SensorScale);
                                }
                            });
                        } catch (Exception e) {e.printStackTrace();}
                    }
                }

                checkNewSensorScale = false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void feedbackChannelValue() {
        System.out.println("Send Channel to PLC!");

        ModbusClient modbusPLCClient = getNewPLCConn();

        // Send current switch status to PLC
        if (modbusPLCClient != null && modbusPLCClient.isConnected()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        modbusPLCClient.WriteSingleRegister(37, getValueChannel());

                        modbusPLCClient.Disconnect();
                    } catch (ModbusException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    // Foward Macro Interrupt Signal from PLC to Fanuc
    // This is not used anywhere now.
    public void feedbackSignalsToFanuc(boolean isToolStatusChanged) {

        if (PreferenceManager.getControlType() >= PreferenceManager.CONTROL_TYPE_FANUC_HAAS) {
            // Not Fanuc, disable write
            //Toast.message("Control Type is not Fanuc and Fanuc Write is disabled!");
            return;
        }

        // Send current MacroInterruptSignal status to Fanuc
        ModbusClient modbusFanucClient = getNewFanucConn();

        if (modbusFanucClient != null && modbusFanucClient.isConnected()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        // Put Coils
                        boolean[] newCoilRegs = new boolean[2];
                        newCoilRegs[0] = uiVars.MacroInterruptSignal;
                        newCoilRegs[1] = uiVars.ToolWearSignal;
                        modbusFanucClient.WriteMultipleCoils(0, newCoilRegs);

                        // Put Holdings
                        if (isToolStatusChanged) {
                            int[] fanucHoldings = new int[2];
                            int[] valueBytes = ModbusClient.ConvertFloatToTwoRegisters(uiVars.ToolWearSignal ? 1 : 0);
                            fanucHoldings[1] = valueBytes[1];
                            fanucHoldings[0] = valueBytes[0];

                            modbusFanucClient.WriteMultipleRegisters(1101, fanucHoldings);
                        }

                        modbusFanucClient.Disconnect();
                    } catch (ModbusException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    // Foward Macro Interrupt Signal from PLC to Fanuc
    public void feedbackMacroInterruptSignalToFanuc(boolean status) {
        if (PreferenceManager.getControlType() >= PreferenceManager.CONTROL_TYPE_FANUC_HAAS) {
            // Not Fanuc, disable write
            //Toast.message("Control Type is not Fanuc and Fanuc Write is disabled!");
            return;
        }

        if (!isFanucConnected())
            return;

        // Send current MacroInterruptSignal status to Fanuc
        ModbusClient modbusFanucClient = getNewFanucConn();

        if (modbusFanucClient != null && modbusFanucClient.isConnected()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        modbusFanucClient.WriteSingleCoil(0, status);

                        uiVars.fanucMacroInterruptSignal = status;

                        modbusFanucClient.Disconnect();
                    } catch (ModbusException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    // Foward Macro Interrupt Signal from PLC to Fanuc
    public void updateAdaptiveStatus() {
        if (isAdaptiveOn() && isMonitorOn() && !isTeachMode()) {
            feedbackAdaptiveEnableToFanuc(true);
        } else {
            feedbackAdaptiveEnableToFanuc(false);
        }
    }

    public void feedbackAdaptiveEnableToFanuc(boolean status) {
        if (PreferenceManager.getControlType() >= PreferenceManager.CONTROL_TYPE_FANUC_HAAS) {
            // Not Fanuc, disable write
            //Toast.message("Control Type is not Fanuc and Fanuc Write is disabled!");
            return;
        }

        if (!isFanucConnected())
            return;

        // Send current MacroInterruptSignal status to Fanuc
        ModbusClient modbusFanucClient = getNewFanucConn();

        if (modbusFanucClient != null && modbusFanucClient.isConnected()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        modbusFanucClient.WriteSingleCoil(1, status);

                        uiVars.fanucAdaptiveEnableSignal = status;

                        modbusFanucClient.Disconnect();
                    } catch (ModbusException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    // Foward Macro Interrupt Signal from PLC to Fanuc
    public void feedbackToolWearSignalToFanuc(boolean status) {

        if (PreferenceManager.getControlType() >= PreferenceManager.CONTROL_TYPE_FANUC_HAAS) {
            // Not Fanuc, disable write
            //Toast.message("Control Type is not Fanuc and Fanuc Write is disabled!");
            return;
        }

        if (!isFanucConnected())
            return;

        // Check same tool
        if (uiVars.toolnumForTheLastWearSignal == uiVars.Tool) {
            return;
        }

        // Send current MacroInterruptSignal status to Fanuc
        ModbusClient modbusFanucClient = getNewFanucConn();

        if (modbusFanucClient != null && modbusFanucClient.isConnected()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        int[] fanucHoldings = new int[2];

                        // * Status should be backward, for example True means 0
                        int[] valueBytes = ModbusClient.ConvertFloatToTwoRegisters(status ? 0 : 1);
                        fanucHoldings[1] = valueBytes[1];
                        fanucHoldings[0] = valueBytes[0];

                        modbusFanucClient.WriteMultipleRegisters(1100, fanucHoldings);

                        // Save Current Status
                        uiVars.fanucToolWearSignal = status;
                        uiVars.toolnumForTheLastWearSignal = uiVars.Tool;

                        modbusFanucClient.Disconnect();
                    } catch (ModbusException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    // Send PID PV(Target) Signal to Fanuc
    ModbusClient modbusFanucSPClient;
    public void feedbackSetPointValueToFanuc() {

        // Monitor Signal From is Manual, It can disable all Fanuc read/write, but specifically TSC
        if (PreferenceManager.getControlType() >= PreferenceManager.CONTROL_TYPE_FANUC_HAAS) {
            // Not Fanuc, disable write
            //Toast.message("Control Type is not Fanuc and Fanuc Write is disabled!");
            return;
        }

        if (!isFanucConnected())
            return;

        // Connect Check
        if (modbusFanucSPClient == null || !modbusFanucSPClient.isConnected()) {
            modbusFanucSPClient = getNewFanucConn();
        }

        // If Connection is normal, put data
        if (modbusFanucSPClient != null && modbusFanucSPClient.isConnected()) {
            /*new Thread(new Runnable() {
                @Override
                public void run() {

                }
            }).start();*/

            boolean needsReconnect = false;
            try {
                int[] fanucHoldings = new int[2];
                float target = parseFloatValue(valueTarget.getText());
                int[] valueBytes = ModbusClient.ConvertFloatToTwoRegisters(target);
                fanucHoldings[1] = valueBytes[1];
                fanucHoldings[0] = valueBytes[0];
                modbusFanucSPClient.WriteMultipleRegisters(1102, fanucHoldings);
            } catch (ModbusException e) {
                e.printStackTrace();
                needsReconnect = true;
            } catch (IOException e) {
                e.printStackTrace();
                needsReconnect = true;
            }

            if (needsReconnect) {
                try {
                    modbusFanucSPClient.Disconnect();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                modbusFanucSPClient = null;
            }
        }
    }

    public void feedbackAutoMonitorSettingsToFanuc() {
        if (PreferenceManager.getControlType() >= PreferenceManager.CONTROL_TYPE_FANUC_HAAS) {
            // Not Fanuc, disable write
            //Toast.message("Control Type is not Fanuc and Fanuc Write is disabled!");
            return;
        }

        if (!isFanucConnected()) {
            Toast.message("Fanuc is not connected, please try later after connect with Fanuc.");
            return;
        }

        // Send current MacroInterruptSignal status to Fanuc
        ModbusClient modbusFanucClient = getNewFanucConn();

        if (modbusFanucClient != null && modbusFanucClient.isConnected()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        int[] fanucHoldings = new int[6];

                        // * Status should be backward, for example True means 0
                        int[] valueBytes = ModbusClient.ConvertFloatToTwoRegisters(PreferenceManager.isAutoMonitorStatus() ? 1 : 0);
                        fanucHoldings[0] = valueBytes[0];
                        fanucHoldings[1] = valueBytes[1];

                        valueBytes = ModbusClient.ConvertFloatToTwoRegisters(PreferenceManager.getSpindleThreshold());
                        fanucHoldings[2] = valueBytes[0];
                        fanucHoldings[3] = valueBytes[1];

                        valueBytes = ModbusClient.ConvertFloatToTwoRegisters(PreferenceManager.getSpindleTime());
                        fanucHoldings[4] = valueBytes[0];
                        fanucHoldings[5] = valueBytes[1];

                        modbusFanucClient.WriteMultipleRegisters(1104, fanucHoldings);

                        modbusFanucClient.Disconnect();
                    } catch (ModbusException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    public void feedbackAdaptiveMinMaxValueToFanuc(float max, float min) {
        if (PreferenceManager.getControlType() >= PreferenceManager.CONTROL_TYPE_FANUC_HAAS) {
            // Not Fanuc, disable write
            //Toast.message("Control Type is not Fanuc and Fanuc Write is disabled!");
            return;
        }

        if (!isFanucConnected()) {
            Toast.message("Fanuc is not connected, please try later after connect with Fanuc.");
            return;
        }

        // Send current MacroInterruptSignal status to Fanuc
        ModbusClient modbusFanucClient = getNewFanucConn();

        if (modbusFanucClient != null && modbusFanucClient.isConnected()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        int[] fanucHoldings = new int[4];

                        // * Status should be backward, for example True means 0
                        int[] valueBytes = ModbusClient.ConvertFloatToTwoRegisters(PreferenceManager.isAutoMonitorStatus() ? 1 : 0);

                        valueBytes = ModbusClient.ConvertFloatToTwoRegisters(max);
                        fanucHoldings[0] = valueBytes[0];
                        fanucHoldings[1] = valueBytes[1];

                        valueBytes = ModbusClient.ConvertFloatToTwoRegisters(min);
                        fanucHoldings[2] = valueBytes[0];
                        fanucHoldings[3] = valueBytes[1];

                        modbusFanucClient.WriteMultipleRegisters(1110, fanucHoldings);

                        modbusFanucClient.Disconnect();
                    } catch (ModbusException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    public void feedbackPVMinMaxValueToFanuc(float max, float min) {
        if (PreferenceManager.getControlType() >= PreferenceManager.CONTROL_TYPE_FANUC_HAAS) {
            // Not Fanuc, disable write
            //Toast.message("Control Type is not Fanuc and Fanuc Write is disabled!");
            return;
        }

        if (!isFanucConnected()) {
            Toast.message("Fanuc is not connected, please try later after connect with Fanuc.");
            return;
        }

        // Send current MacroInterruptSignal status to Fanuc
        ModbusClient modbusFanucClient = getNewFanucConn();

        if (modbusFanucClient != null && modbusFanucClient.isConnected()) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        int[] fanucHoldings = new int[4];

                        // * Status should be backward, for example True means 0
                        int[] valueBytes = ModbusClient.ConvertFloatToTwoRegisters(PreferenceManager.isAutoMonitorStatus() ? 1 : 0);

                        valueBytes = ModbusClient.ConvertFloatToTwoRegisters(max);
                        fanucHoldings[0] = valueBytes[0];
                        fanucHoldings[1] = valueBytes[1];

                        valueBytes = ModbusClient.ConvertFloatToTwoRegisters(min);
                        fanucHoldings[2] = valueBytes[0];
                        fanucHoldings[3] = valueBytes[1];

                        modbusFanucClient.WriteMultipleRegisters(1114, fanucHoldings);

                        modbusFanucClient.Disconnect();
                    } catch (ModbusException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    public void setManualTCSVals(int tool, int section, int channel) {
        uiVars.Tool = tool;
        uiVars.Section = section;
        uiVars.Channel = channel;

        valueTool.setText(String.valueOf(tool));
        valueSection.setText(String.valueOf(section));
        valueChannel.setText(String.valueOf(channel));
    }

    // Foward Macro InterruptSignal from PLC to Fanuc
    public void sendFanucSimulVals(int prog, int tool, int section, int channel, int optimize) {

        // Monitor Signal From is Manual, It can disable all Fanuc read/write, but specifically TSC
        if (PreferenceManager.getControlType() >= PreferenceManager.CONTROL_TYPE_FANUC_HAAS) {
            // Not Fanuc, disable write
            Toast.message("Control Type is not Fanuc and Fanuc Write is disabled!");
            return;
        }

        if (PreferenceManager.getMonitorSignalFrom() >= PreferenceManager.MONITOR_SIGNAL_FROM_PLC) {
            // Signal is from Manual Input or PLC, so disable write!
            Toast.message("Fanuc Simulator is not working in PLC mode.");
            return;
        }

        // Send current MacroInterruptSignal status to Fanuc
        ModbusClient modbusFanucClient = getNewFanucConn();

        if (modbusFanucClient != null && modbusFanucClient.isConnected()) {

            int[] fanucHoldings = new int[10];
            int[] valueBytes = ModbusClient.ConvertFloatToTwoRegisters(optimize > 0 ? 1 : 0);
            fanucHoldings[0] = valueBytes[0];
            fanucHoldings[1] = valueBytes[1];

            valueBytes = ModbusClient.ConvertFloatToTwoRegisters(tool);
            fanucHoldings[2] = valueBytes[0];
            fanucHoldings[3] = valueBytes[1];

            valueBytes = ModbusClient.ConvertFloatToTwoRegisters(section);
            fanucHoldings[4] = valueBytes[0];
            fanucHoldings[5] = valueBytes[1];

            valueBytes = ModbusClient.ConvertFloatToTwoRegisters(channel);
            fanucHoldings[6] = valueBytes[0];
            fanucHoldings[7] = valueBytes[1];

            valueBytes = ModbusClient.ConvertFloatToTwoRegisters(prog);
            fanucHoldings[8] = valueBytes[0];
            fanucHoldings[9] = valueBytes[1];

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        modbusFanucClient.WriteMultipleRegisters(1000, fanucHoldings);

                        modbusFanucClient.Disconnect();
                    } catch (ModbusException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } else {
            //Toast.message("Fanuc is not connected!");
        }
    }

    public void toggleTeachMode() {
        if (isTeachMode()) {
            // Currently Teachmode=On, Turn off it
            // * User can turn off Teach Mode manually
            // * if this happens, record is inserted or updated right when user turns off teach mode manually

            // * Again Changed Logic :
            // Only save tool data according the Optimize Sequence(TeachMode=1 and monitor is changed from 1 to 0, then save tool file)
            //saveToolDataFile();

            btnTeachOnetool.setText("TEACH ONE TOOL\nOFF");
            try {
                timelineAnimTeachMode.stop();
                btnTeachMode.setStyle("-fx-background-color:white");
                btnTeachMode.setText("TEACH MODE\nOFF");
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            vBoxComment.getChildren().setAll(titleComment, valueComment);
            vBoxSaveVars.setVisible(true);

            boxHomeSliders.setVisible(true);
        } else {
            // Currently Teachmode = OFF, user want to turn ON
            if (getValueProgNum() == 0) {
                Toast.message("Invalid Program Number!");
            } else {
                if (uiVars.Optimize > 0 || isMonitorOn()) { // Makes sure that the machine is not running before teach mode is on
                    Toast.message("Machine is already running! Please turn on Teach Mode before starting the cycle");
                } else {
                    valueElapsedMonitorTime.setText("0.0 sec");
                    btnTeachMode.setText("TEACH MODE\nON");

                    vBoxSaveVars.setVisible(false); // Hides save new values button during teach mode
                    boxHomeSliders.setVisible(false); // Hides Value Slider during teach mode

                    // Set Default Settings
                    btnAdaptiveEnable.setText(PreferenceManager.isAdaptiveEnabled() ? "ADAPTIVE\nON" : "ADAPTIVE\nOFF");
                    btnMacroInterruptEnable.setText(PreferenceManager.isMacroInterruptEnabled() ? "MACRO INTERRUPT\nON" : "MACRO INTERRUPT\nOFF");
                    btnLeadInFREnable.setText(PreferenceManager.isLeadInFREnabled() ? "LEAD-IN FR\nON" : "LEAD-IN FR\nOFF");

                    // * No need to call again, when the switch status is changed by above 3 lines,
                    // * following function is automatically called.
                    //feedbackSwitchStatus();

                    TextField textField = new TextField();
                    textField.setPromptText("Enter Tool Name");
                    Button saveButton = new Button("Save Name");
                    vBoxComment.getChildren().setAll(titleComment, textField, saveButton);
                    saveButton.setOnAction(event ->
                    {
                        try { // Saves the comment that the user enters during teach mode
                            JobDataManager.getInstance().saveJobData(textField.getText());
                            valueComment.setText(textField.getText()); //
                            vBoxComment.getChildren().setAll(titleComment, valueComment);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    });
                }
            }
        }

        // Update Current Status
        updateMachineStatus();
    }

    public void saveToolDataFile() {
        // Save current UI values to the file

        // * Check TeachMode=0/1 status to validate the following actions.
        int progNum = getValueProgNum();
        if (progNum == 0) {
            return;
        }

        // Find Original Data
        JobData curJobData = JobDataManager.getInstance().getJobData(progNum, uiVars.Tool, uiVars.Section, uiVars.Channel);
        if (curJobData == null) {
            curJobData = new JobData();
            curJobData.tool = MainView.this.uiVars.Tool;
            curJobData.section = MainView.this.uiVars.Section;
            curJobData.channel = MainView.this.uiVars.Channel;
        }

        curJobData.comment = getValueComment();
        curJobData.target = (float) getValueTarget();
        curJobData.highLimit = (float) getValueHighLimit();
        curJobData.wearLimit = (float) getValueWearLimit();
        curJobData.idle = (float) getValueIdle();
        curJobData.lowLimitTime = (float) getValueLearnedLowLimitTimer();

        if (isTeachMode()) {
            curJobData.adaptiveEnable = PreferenceManager.isAdaptiveEnabled();
            curJobData.macroInterruptEnable = PreferenceManager.isMacroInterruptEnabled();
            curJobData.leadInEnable = PreferenceManager.isLeadInFREnabled();
        } else {
            curJobData.adaptiveEnable = isAdaptiveOn();
            curJobData.macroInterruptEnable = isMacroInterruptOn();
            curJobData.leadInEnable = isLeadInFROn();
        }

        curJobData.leadInFeedrate = PreferenceManager.getDefaultLeadInFeedrate(); //(float) getValueFeedrate();
        curJobData.leadInTrigger = (float) getValueLeadInTrigger();

        curJobData.startDelay = PreferenceManager.getDefaultStartDelay();
        curJobData.highLimitDelay = PreferenceManager.getDefaultHighLimitDelay();
        curJobData.wearLimitDelay = PreferenceManager.getDefaultWearLimitDelay();
        curJobData.adaptiveMin = PreferenceManager.getAdaptiveMin();
        curJobData.adaptiveMax = PreferenceManager.getAdaptiveMax();
        curJobData.adaptiveWearLimit = PreferenceManager.getAdaptiveWearLimit();
        curJobData.adaptiveHighLimit = PreferenceManager.getAdaptiveHighLimit();
        curJobData.filter = (float) getValueFilter();
        curJobData.sensorScaleSend = uiVars.SensorScale;
        curJobData.monitorTime = (float) getValueLearnedMonitorTime();

        if (JobDataManager.getInstance().updateToolDataWithCurrent(progNum, curJobData)) {
            Toast.message("Tool Data Save Success!");
        } else {
            Toast.message("Tool Data Save Failure!");
        }

        /*// Way1
        if (!(valueProgNum.getText().equals("0"))) {
            try {
                JobDataManager.getInstance().loadJobDataOnChange(Integer.parseInt(valueProgNum.getText()),
                        Integer.parseInt(valueTool.getText()),
                        Integer.parseInt(valueSection.getText()),
                        Integer.parseInt(valueChannel.getText()));
            } catch (IOException e) {
                e.printStackTrace();
            }
            JobDataManager.getInstance().saveChangedToolData();
            ToolDataView.getInstance().showJobData();
        }

        // Way 2
        if (!(valueProgNum.getText().equals("0"))) { // Makes sure theres a valid program number then saves the data
            System.out.println("Saving tool data!");

            JobDataManager.getInstance().saveChangedToolData();
            ToolDataView.getInstance().showJobData();
        }*/
    }

    public void updateToolDataFile() {
        // Update current slider values to the file

        // * Check TeachMode=0/1 status to validate the following actions.
        int progNum = getValueProgNum();
        if (progNum == 0) {
            return;
        }

        // Find Original Data
        JobData curJobData = JobDataManager.getInstance().getJobData(progNum, uiVars.Tool, uiVars.Section, uiVars.Channel);
        if (curJobData == null) {
            Toast.message("No Tool Data!");
            return;
        }

        // Switch Status
        curJobData.adaptiveEnable = isAdaptiveOn();
        curJobData.leadInEnable = isLeadInFROn();
        curJobData.macroInterruptEnable = isMacroInterruptOn();

        // Slider Value
        String val = String.valueOf(priorityComboBox.getValue());

        switch (val) {
            case "TARGET":
                curJobData.target = (float) getValueTarget();
                break;
            case "HIGH":
                curJobData.highLimit = (float) getValueHighLimit();
                break;
            case "WEAR":
                curJobData.wearLimit = (float) getValueWearLimit();
                break;
            case "IDLE":
                curJobData.idle = (float) getValueIdle();
                break;
            case "LEAD-IN TRIGGER":
                curJobData.leadInTrigger = (float) getValueLeadInTrigger();
                break;
            case "FILTER":
                curJobData.filter = (float) getValueFilter();
                break;
        }

        // Refresh Tool Data file if it was opened.
        ToolDataView.getInstance().refreshToolData();

        if (JobDataManager.getInstance().updateToolDataWithCurrent(progNum, curJobData)) {
            Toast.message("Tool Data Save Success!");
        } else {
            Toast.message("Tool Data Save Failure!");
        }
    }

    final EventHandler<ActionEvent> navMenuHandler = new EventHandler<ActionEvent>() {

        @Override
        public void handle(final ActionEvent event) {
            // Close Menu first
            if (!drawer.isClosed() && !drawer.isClosing())
                drawer.close();

            if (event.getSource() == menuDasgboard) {
                System.out.println("Dashboard!");
            } else if (event.getSource() == menuViewHST) {
                HSTFileView.getInstance().show();
            } else if (event.getSource() == menuTimeSavings) {
                TimeSavingsDataView.getInstance().show();
            } else if (event.getSource() == menuViewAlarams) {
                SystemAlertDataView.getInstance().show();
            } else if (event.getSource() == menuMMS) {

                MMSView.getInstance().show();

                // When is required user login, then use the following comment block module
                /*if (checkPermissionStatus(0)) {

                    //Toast.message("Coming soon!");
                    MMSView.getInstance().show();
                } else {
                    askUserToLogin(0, new LoginResultListener() {
                        @Override
                        public void onLoginSuccess() {
                            //Toast.message("Coming soon!");
                            MMSView.getInstance().show();
                        }

                        @Override
                        public void onLoginCancelled() {
                        }
                    });
                }*/
            } else if (event.getSource() == menuSettings) {
                System.out.println("Settings!");
                //LoginView.getInstance().show();
                try {
                    SettingsView.getInstance().start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // DeviceSettingView.getInstance().show();
            }else if (event.getSource() == menuProcessMonitor) {
                System.out.println("Process Monitor!");

                ProcessMonitorView.getInstance().show();
            } else if (event.getSource() == menuOptimizationReporting) {
                System.out.println("Optimization Reports");
                /*try {
                    OptimizationResultsView.getInstance().start();
                } catch (IOException e) {
                    e.printStackTrace();
                }*/
            } else if (event.getSource() == menuSimFanuc) {
                FanucSimulatorView.getInstance().show();
            } else if (event.getSource() == menuSimPLC) {
                PLCSimulatorView.getInstance().show();
            } else if (event.getSource() == menuCheckUpdate) {
                checkVersionUpdate(true);
            }
        }
    };

    // Report Savings
    private void reportSavings() {
        // * Report Savings
        // When teachMode=0 AND adaptiveEnable=1 AND monitor change from 1 to 0,
        // When monitor=1 and section change

        int progNum = getValueProgNum();
        int tool = getValueTool();
        int section = getValueSection();
        int channel = getValueChannel();
        float learnedTime = (float) getValueLearnedMonitorTime();
        float elapsedTime = (float) getValueElapsedMonitorTime();
        float timeSavings = learnedTime - elapsedTime;
        float timeSavingPer = 0;
        if (learnedTime > 0) {
            timeSavingPer = timeSavings * 100 / learnedTime;
        }

        Date currTime = new Date();
        String valueDate1 = DateTimeUtils.formatDate(currTime, "MM/dd/yyyy");
        String valueDate2 = DateTimeUtils.formatDate(currTime, "yyyy-MM-dd");
        String valueTime = DateTimeUtils.formatDate(currTime, "HH:mm:ss");

        TimeSavingManager.getInstance().addNewLog(valueDate1, valueTime, progNum, tool, section, channel, learnedTime, elapsedTime, timeSavings, timeSavingPer);

        String factoryID = PreferenceManager.getFactoryID();
        String machineID = PreferenceManager.getMachineID();

        if (!TextUtils.isEmpty(factoryID) && !TextUtils.isEmpty(machineID)) {
            float finalTimeSavingPer = timeSavingPer;
            new Thread(new Runnable() {
                @Override
                public void run() {

                    HttpPost post = new HttpPost(Api.SERVE_URL + Api.api_timeSavings);
                    // add request parameters or form parameters
                    List<NameValuePair> urlParameters = new ArrayList<>();
                    urlParameters.add(new BasicNameValuePair("customerId", factoryID));
                    urlParameters.add(new BasicNameValuePair("machineId", machineID));
                    urlParameters.add(new BasicNameValuePair("date", valueDate2));
                    urlParameters.add(new BasicNameValuePair("time", valueTime));
                    urlParameters.add(new BasicNameValuePair("progNum", String.valueOf(progNum)));
                    urlParameters.add(new BasicNameValuePair("tool", String.valueOf(tool)));
                    urlParameters.add(new BasicNameValuePair("section", String.valueOf(section)));
                    urlParameters.add(new BasicNameValuePair("channel", String.valueOf(channel)));
                    urlParameters.add(new BasicNameValuePair("learnedTime", String.valueOf(learnedTime)));
                    urlParameters.add(new BasicNameValuePair("elapsedTime", String.valueOf(elapsedTime)));
                    urlParameters.add(new BasicNameValuePair("timeSavings", String.valueOf(timeSavings)));
                    urlParameters.add(new BasicNameValuePair("timeSavingsPercentage", String.valueOf(finalTimeSavingPer)));

                    try {
                        post.setEntity(new UrlEncodedFormEntity(urlParameters));
                        HttpClient httpClient = HttpClientBuilder.create().build();
                        HttpResponse result = httpClient.execute(post);
                        String response = EntityUtils.toString(result.getEntity(), "UTF-8");


                    } catch (Exception e) {
                        e.printStackTrace();

                        Platform.runLater(() -> {
                            Toast.message("Fail to upload time savings info!");
                        });
                    }
                }
            }).start();
        }
    }

    final EventHandler<MouseEvent> viewHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            if (event.getSource() == vBoxProgNumber) {
                ToolDataView.getInstance().show();
            }
        }
    };

    final EventHandler<MouseEvent> vBoxHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            Object src = event.getSource();
            if (src == vBoxTarget) {
                showSlider(1);

                String colorTarget = "green";
                setSliderTrackColor(colorTarget);
                labelSliderValue.setTextFill(Color.rgb(0, 128, 0));

                priorityComboBox.setValue("TARGET");

                sliderAdjust.setValue(getValueTarget());
            } else if (src == vBoxHighLimit) {
                showSlider(1);

                String colorHigh = "red";
                labelSliderValue.setTextFill(Color.rgb(255, 0, 0));

                setSliderTrackColor(colorHigh);
                priorityComboBox.setValue("HIGH");

                sliderAdjust.setValue(getValueHighLimit());
            } else if (src == vBoxWearLimit) {
                showSlider(1);

                String colorWear = "orange";
                setSliderTrackColor(colorWear);
                labelSliderValue.setTextFill(Color.rgb(255, 165, 0));

                priorityComboBox.setValue("WEAR");

                sliderAdjust.setValue(getValueWearLimit());
            } else if (src == vBoxIdle) {
                showSlider(1);

                String colorIdle = "white";
                labelSliderValue.setTextFill(Color.rgb(255, 255, 255));

                setSliderTrackColor(colorIdle);

                priorityComboBox.setValue("IDLE");

                sliderAdjust.setValue(getValueIdle());
            } else if (src == vBoxLeadInTrigger) {
                showSlider(1);

                String colorLead = "yellow";
                labelSliderValue.setTextFill(Color.rgb(255, 255, 0));

                setSliderTrackColor(colorLead);

                priorityComboBox.setValue("LEAD-IN TRIGGER");

                sliderAdjust.setValue(getValueLeadInTrigger());
            } else if (src == vBoxFilter) {
                showSlider(0);

                String colorFilter = "orange";

                setSliderTrackColor(colorFilter);

                priorityComboBox.setValue("FILTER");

                sliderAdjustFilter.setValue(getValueFilter());
            } else if (src == vBoxAdaptiveHighLimit) {

                showSlider(2);

                adaptiveViewTag = 0;

                int valueTarget = getValueAdaptiveHighLimit();
                labelSliderAdaptiveValue.setText(String.format("%d", valueTarget));
                labelSliderAdaptiveValue.setTextFill(Color.rgb(255, 0, 0));

                sliderAdjustAdaptive.setValue(valueTarget);
                VBox.setVgrow(sliderAdjust, Priority.ALWAYS);

                setAdaptiveSliderTrackColor("red");
            } else if (src == vBoxAdaptiveWearLimit) {
                showSlider(2);

                adaptiveViewTag = 1;

                int valueTarget = getValueAdaptiveWearLimit();
                labelSliderAdaptiveValue.setText(String.format("%d", valueTarget));
                labelSliderAdaptiveValue.setTextFill(Color.rgb(255, 165, 0));

                sliderAdjustAdaptive.setValue(valueTarget);
                VBox.setVgrow(sliderAdjust, Priority.ALWAYS);

                setAdaptiveSliderTrackColor("orange");
            } else if (src == vBoxTool || src == vBoxSection || src == vBoxChannel) {
                if (PreferenceManager.getControlType() != PreferenceManager.CONTROL_TYPE_FANUC_HAAS &&
                        PreferenceManager.getMonitorSignalFrom() == PreferenceManager.MONITOR_SIGNAL_FROM_MANUAL) {
                    TCSManualInputView.getInstance().show();
                }
            }
        }
    };

    String onlineTOSVersionName = "";
    int onlineFanucVersionCode = 0;

    boolean isTOSNewVersion = false;
    boolean isFanucNewversion = false;

    public void checkVersionUpdate(boolean showMessage) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String version = Resource.LOCALVER;
                int curTOSMajor = Integer.parseInt(version.substring(0, version.indexOf(".")));
                int curTOSMinor = Integer.parseInt(version.substring(version.indexOf(".") + 1));
                int curTOSVerionCode = curTOSMajor * 1000 + curTOSMinor;

                int curFanucVersionCode = PreferenceManager.getFanucVersionCode();

                HttpGet getRequest = new HttpGet(Api.SERVE_URL + Api.api_version);
                try {
                    HttpClient httpClient = HttpClientUtils.getHttpWithTrustAlLStrategy();//HttpClientBuilder.create().build();
                    HttpResponse result = httpClient.execute(getRequest);
                    String response = EntityUtils.toString(result.getEntity(), "UTF-8");

                    int onlineTOSMajor = 0;
                    int onlineTOSMiner = 0;
                    int onlineTOSVerionCode = 0;

                    onlineFanucVersionCode = 0;

                    String[] versionsInfo = response.split("\\.");
                    try {
                        if (versionsInfo != null && versionsInfo.length > 0) {
                            onlineTOSMajor = Integer.parseInt(versionsInfo[0]);
                        }
                        if (versionsInfo != null && versionsInfo.length > 1) {
                            onlineTOSMiner = Integer.parseInt(versionsInfo[1]);
                        }
                        onlineTOSVerionCode = onlineTOSMajor * 1000 + onlineTOSMiner;

                        if (versionsInfo != null && versionsInfo.length > 2) {
                            onlineFanucVersionCode = Integer.parseInt(versionsInfo[2]);
                        }

                    } catch (Exception e) {
                    }

                    isTOSNewVersion = false;
                    if (onlineTOSVerionCode > curTOSVerionCode) {
                        isTOSNewVersion = true;
                        onlineTOSVersionName = String.format("%d.%d", onlineTOSMajor, onlineTOSMiner);
                    }

                    isFanucNewversion = false;
                    // Currently we're ignoring update Fanuc through TOS
                    if (onlineFanucVersionCode > curFanucVersionCode && false) {
                        isFanucNewversion = true;
                    }

                    Platform.runLater(() -> {
                        if (isTOSNewVersion | isFanucNewversion) {
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.initModality(Modality.APPLICATION_MODAL);
                            alert.setTitle("New Version");
                            Stage dialogStage = (Stage) alert.getDialogPane().getScene().getWindow();
                            dialogStage.getIcons().add(LogoManager.getInstance().getLogo());
                            alert.setHeaderText(null);
                            alert.setContentText("New Versions are available!\nWould you like to update?");

                            Optional<ButtonType> clickResult = alert.showAndWait();
                            if (clickResult.get() == ButtonType.OK) {
                                UpdateAppView.getInstance().updateApp(isTOSNewVersion, onlineTOSVersionName, isFanucNewversion, onlineFanucVersionCode);
                            }
                        } else {
                            if (showMessage) {
                                Toast.message("Current versions are up to date!");
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();

                    Platform.runLater(() -> {
                        Toast.message("Fail to get version information!");
                    });
                }
            }
        }).start();
    }

    // Send PLC Alarm Email
    public void sendAlarmEmail(int alertType) {

        ArrayList<String> toMails = new ArrayList<>();
        if (Utils.isValidEmail(PreferenceManager.getAlertEmail1())) {
            toMails.add(PreferenceManager.getAlertEmail1());
        }
        if (Utils.isValidEmail(PreferenceManager.getAlertEmail2())) {
            toMails.add(PreferenceManager.getAlertEmail2());
        }
        if (Utils.isValidEmail(PreferenceManager.getAlertEmail3())) {
            toMails.add(PreferenceManager.getAlertEmail3());
        }

        if (toMails.isEmpty()) {
            System.out.println("Email list is empty!");
            return;
        }

        //Machine Name’ cycle ended at: ‘cycle stop time”
        String machineName = PreferenceManager.getMachineID();
        if (machineName.isEmpty())
            machineName = "Machine";
        String alarmTime = DateTimeUtils.toStringFormat_12(new Date());

        String subject = "TOS V2 Alarm";
        String contents = "";

        if (alertType == PLC_ALARM_LOWLIMIT) {

            if (!PreferenceManager.isAlertOnLlEnabled()) {
                return;
            }
            contents = String.format("%s raised LOW LIMIT alarm at %s.", machineName, alarmTime);
        } else if (alertType == PLC_ALARM_WEARLIMIT) {
            if (!PreferenceManager.isAlertOnWlEnabled()) {
                return;
            }
            contents = String.format("%s raised WEAR LIMIT alarm at %s.", machineName, alarmTime);
        } else if (alertType == PLC_ALARM_HIGHLIMIT) {
            if (!PreferenceManager.isAlertOnHlEnabled()) {
                return;
            }
            contents = String.format("%s raised HIGH LIMIT alarm at %s.", machineName, alarmTime);
        }

        if (TextUtils.isEmpty(contents)) {
            // Empty Email
            return;
        }

        new MailSender("reports@slymms.com",
                "246896321S!",
                toMails,
                subject,
                contents, new MailSender.MailSendCallback() {
            @Override
            public void onMailFailed(String error) {
            }

            @Override
            public void onMailSent() {
                System.out.println("Email successfully sent!");
            }
        }).start();
    }

    // Send System Alarm Email
    public void sendSystemAlarmEmail() {

        ArrayList<String> toMails = new ArrayList<>();
        if (Utils.isValidEmail(PreferenceManager.getAlertEmail1())) {
            toMails.add(PreferenceManager.getAlertEmail1());
        }
        if (Utils.isValidEmail(PreferenceManager.getAlertEmail2())) {
            toMails.add(PreferenceManager.getAlertEmail2());
        }
        if (Utils.isValidEmail(PreferenceManager.getAlertEmail3())) {
            toMails.add(PreferenceManager.getAlertEmail3());
        }

        if (toMails.isEmpty()) {
            System.out.println("Email list is empty!");
            return;
        }

        //Machine Name’ cycle ended at: ‘cycle stop time”
        String machineName = PreferenceManager.getMachineID();
        if (machineName.isEmpty())
            machineName = "Machine";
        String alarmTime = DateTimeUtils.toStringFormat_12(new Date());

        String subject = "TOS V2 Alarm";
        String contents = "";

        if (currSystemAlarmStatus == SYSTEM_ALARM_LOW_LIMIT) {

            if (!PreferenceManager.isAlertOnLlEnabled()) {
                return;
            }
            contents = String.format("%s raised LOW LIMIT alarm at %s.", machineName, alarmTime);
        } else if (currSystemAlarmStatus == SYSTEM_ALARM_WEAR_LIMIT || currSystemAlarmStatus == SYSTEM_ALARM_WEAR_LIMIT_ADAPTIVE) {
            if (!PreferenceManager.isAlertOnWlEnabled()) {
                return;
            }

            if (currSystemAlarmStatus == SYSTEM_ALARM_WEAR_LIMIT) {
                contents = String.format("%s raised WEAR LIMIT alarm at %s.", machineName, alarmTime);
            } else {
                contents = String.format("%s raised WEAR LIMIT ADAPTIVE alarm at %s.", machineName, alarmTime);
            }
        } else if (currSystemAlarmStatus == SYSTEM_ALARM_HIGH_LIMIT || currSystemAlarmStatus == SYSTEM_ALARM_HIGH_LIMIT_ADAPTIVE) {
            if (!PreferenceManager.isAlertOnHlEnabled()) {
                return;
            }

            if (currSystemAlarmStatus == SYSTEM_ALARM_HIGH_LIMIT) {
                contents = String.format("%s raised HIGH LIMIT alarm at %s.", machineName, alarmTime);
            } else {
                contents = String.format("%s raised HIGH LIMIT ADAPTIVE alarm at %s.", machineName, alarmTime);
            }
        } else {
            // In other case, we never send the alert email
            return;
        }

        // Check the Empty Contents
        if (TextUtils.isEmpty(contents)) {
            return;
        }

        new MailSender("reports@slymms.com",
                "246896321S!",
                toMails,
                subject,
                contents, new MailSender.MailSendCallback() {
            @Override
            public void onMailFailed(String error) {
            }

            @Override
            public void onMailSent() {
                System.out.println("Email successfully sent!");
            }
        }).start();
    }
}
