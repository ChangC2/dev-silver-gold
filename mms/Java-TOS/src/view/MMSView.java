package view;

import Controller.Api;
import Controller.LoginResultListener;
import Database.*;
import Main.Resource;
import Model.*;
import Utils.PreferenceManager;
import Utils.Utils;
import Utils.DateTimeUtils;
import Utils.ChartColorUtils;

import eu.hansolo.medusa.Gauge;
import eu.hansolo.medusa.GaugeBuilder;
import eu.hansolo.medusa.LcdFont;
import eu.hansolo.medusa.Section;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import javafx.util.Duration;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.http.util.TextUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;
import java.util.concurrent.CountDownLatch;

import static Model.MMSAgent.*;
import static javafx.animation.Animation.INDEFINITE;

public class MMSView {

    AnchorPane viewHome = new AnchorPane();
    BorderPane panelMMSView;
    AnchorPane panelIdleSetUp;

    Stage screen_stage;

    private static MMSView instance;

    public static MMSView getInstance() {
        if (instance == null) {
            instance = new MMSView();
        }
        return instance;
    }

    public MMSView() {
        initView();
    }

    Label tvMachineName;
    Label tvMachineCateStatus;

    Color colorGreen = Color.rgb(0x46, 0xc3, 0x92);
    Color colorOrange = Color.rgb(0xff, 0xa3, 0x00);
    Color colorRed = Color.rgb(0xff, 0x00, 0x00);

    ImageView ivFactoryLogo;

    // Device Connect Status
    ImageView ivFanucConnStatus;          // Fanuc Connection Status
    Label txtFanucConnStatus;
    boolean isFanucConnected = false;

    ImageView ivServerConnStatus;       // Server Connection Status
    Label txtServerConnStatus;
    boolean isServerConnected = false;

    Image imageStatusOn;
    Image imageStatusOff;

    Button btnSetttings;

    Circle circleAvatar;
    Label tvUserName;

    Button btnLogin;
    Button btnLogout;

    // Job Info
    Button btnEditJobInfo;
    Label tvJobId;
    Label labelJobState;
    Label tvJobDetails;

    // Goods / Bads
    Label tvGood;
    Button btnGoodUp;
    Button btnGoodDown;

    Label tvBad;
    Button btnBadUp;
    Button btnBadDown;

    // Time Status
    Label tvSyncTime;
    Label tvIdleTime;
    Label tvUnCatTime;
    FadeTransition anminBlinkSync;
    FadeTransition anminBlinkIdle;
    FadeTransition anminBlinkUnCat;

    Label labelCurrentTime;
    Timeline timeline;

    Button btnClose;

    // Gauge Node and gauges
    Node regimeGaugePanel;
    Gauge gaugeAvailablity;
    Gauge gaugeOEE;
    Gauge gaugePerformance;
    Gauge gaugeQuality;

    Node regime24UtilsPanel;

    PieChart pieChart;
    ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();

    private int prevDeviceStatus = DEVICE_STATUS_UNCATEGORIZED;

    String[] colorCharts = ChartColorUtils.chartColorStrings;

    // ---------- Downtime Reason Panel ------------------------
    ImageView ivDowntimeBack;
    VBox backCsLockStatus;

    Label tvTitleStatusInterlocked;
    Label tvElapsedIdleTIme;
    Button[] btnDownTimeStatus = new Button[8];

    Button btnCloseIdleStatus;
    // ---------------------------------------------------------

    boolean isSignalTimerRunning;
    Thread threadCheckSignal;

    MMSAgent mmsAgent;

    private void initView() {

        mmsAgent = MMSAgent.getInstance();

        // Get System Enviroments Variables
        Map<String, String> envs = System.getenv();
        String userHome = System.getProperty("user.home");

        // Main MMS View
        panelMMSView = new BorderPane();
        panelMMSView.setTop(getTopView());
        panelMMSView.setBottom(getBottomView());
        panelMMSView.setCenter(getCenterView());

        AnchorPane.setRightAnchor(panelMMSView, 0.0);
        AnchorPane.setLeftAnchor(panelMMSView, 0.0);
        AnchorPane.setTopAnchor(panelMMSView, 0.0);
        AnchorPane.setBottomAnchor(panelMMSView, 0.0);

        // Downtime Reason Panel
        panelIdleSetUp = getDowntimeReasonPanel();
        AnchorPane.setRightAnchor(panelIdleSetUp, 0.0);
        AnchorPane.setLeftAnchor(panelIdleSetUp, 0.0);
        AnchorPane.setTopAnchor(panelIdleSetUp, 0.0);
        AnchorPane.setBottomAnchor(panelIdleSetUp, 0.0);
        panelIdleSetUp.setVisible(false);

        viewHome.getChildren().addAll(panelMMSView, panelIdleSetUp);

        // Init Scene
        Scene scene = new Scene(viewHome, 1200, 800);
        scene.getStylesheets().add(getClass().getClassLoader().getResource("resource/style/rootStyles.css").toExternalForm());

        screen_stage = new Stage(StageStyle.DECORATED);
        screen_stage.setTitle("MMS");

        screen_stage.setScene(scene);
        screen_stage.centerOnScreen();
        screen_stage.getIcons().add(LogoManager.getInstance().getLogo());
        //screen_stage.setAlwaysOnTop(true);
        screen_stage.setResizable(false);
        screen_stage.setMinWidth(1200);
        screen_stage.setMinHeight(800);

        screen_stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                close();
            }
        });

        /*screen_stage.widthProperty().addListener((obs, oldVal, newVal) -> {
            // Do whatever you want
            centerImage();
        });
        screen_stage.heightProperty().addListener((obs, oldVal, newVal) -> {
            // Do whatever you want
            centerImage();
        });*/
    }

    public void show() {
        boolean wasAlreadyShowing = screen_stage.isShowing();

        screen_stage.show();
        screen_stage.requestFocus();
        screen_stage.toFront();

        showSettings();

        // Set Online Status
        StatusReporter.getInstance().reportStatus("Online");

        // If Already running, no need to init, Only need to update the custome info, downtime reason settings in case of changing in TOS Settings not in MMS Settings
        if (wasAlreadyShowing) {
            return;
        }

        if (!isSignalTimerRunning) {

            threadCheckSignal = new Thread(progressUpdateRunnable);
            threadCheckSignal.start();

            isSignalTimerRunning = true;
        }
    }

    public void close() {

        //* Set Offline Status, We call this function in MMSAgent instance
        //StatusReporter.getInstance().reportStatus("Offline");

        // Stop Timer
        timeline.pause();
        timeline.stop();
        timeline.getKeyFrames().clear();

        stopChinChin();

        // Stop Timer
        if (threadCheckSignal != null) {
            threadCheckSignal.interrupt();
            threadCheckSignal = null;
        }
        isSignalTimerRunning = false;

    }

    private VBox getTopView() {
        VBox vTopBox = new VBox();
        vTopBox.setAlignment(Pos.CENTER);
        vTopBox.setPadding(new Insets(10, 0, 0, 0));

        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(10);
        hBox.setPadding(new Insets(10, 10, 10, 10));

        // Machine Info Box: Name, Status------------------------------
        VBox vMachineInfoBox = new VBox();
        vMachineInfoBox.setPrefWidth(200);
        vMachineInfoBox.setAlignment(Pos.CENTER);
        vMachineInfoBox.setSpacing(5);

        tvMachineName = new Label(PreferenceManager.getMachineID());
        tvMachineName.setPadding(new Insets(3, 3, 3, 3));
        tvMachineName.getStyleClass().add("label-info-value-large");
        tvMachineName.setTextFill(Color.WHITE);
        vMachineInfoBox.getChildren().add(tvMachineName);

        tvMachineCateStatus = new Label(PreferenceManager.isJobSetupStatus() ? "Setup - UnCategorized" : "UnCategorized");
        tvMachineCateStatus.setAlignment(Pos.CENTER);
        tvMachineCateStatus.getStyleClass().addAll("label-info-title-large", "label-red");
        vMachineInfoBox.getChildren().add(tvMachineCateStatus);
        tvMachineCateStatus.setTextFill(Color.RED);
        //--------------------------------------------------------------


        // Left Margin -------------------------------------------------
        Region leftRegion = new Region();
        HBox.setHgrow(leftRegion, Priority.ALWAYS);
        // -------------------------------------------------------------

        // Factory Logo ------------------------------------------------
        ivFactoryLogo = new ImageView();
        ivFactoryLogo.setImage(LogoManager.getInstance().getBannerLogo());
        ivFactoryLogo.setFitWidth(250);
        ivFactoryLogo.setFitHeight(50);
        ivFactoryLogo.setPreserveRatio(true);
        ivFactoryLogo.setSmooth(true);
        ivFactoryLogo.setCache(true);

        try {
            String imageLogoUrl = PreferenceManager.getFactoryLogo();
            imageLogoUrl = Utils.getEscapedUriString(imageLogoUrl);

            //utf8EncodedString = URLEncoder.encode(imageLogoUrl, "UTF-8").replaceAll("\\+", "%20");
            /*URLConnection connection = new URL(utf8EncodedString).openConnection();
            connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
            Image logoImage = new Image(connection.getInputStream());
            ivFactoryLogo.setImage(logoImage);*/

            Image logoImage = new Image(imageLogoUrl, true);
            logoImage.errorProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    System.out.println(newValue);  //Location 4
                }
            });
            logoImage.progressProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                    if (newValue.floatValue() >= 1.0) {
                        ivFactoryLogo.setImage(logoImage);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        // -------------------------------------------------------------

        // Right Margin ------------------------------------------------
        Region rightRegion = new Region();
        HBox.setHgrow(rightRegion, Priority.ALWAYS);
        // -------------------------------------------------------------

        // Device Connect Status
        imageStatusOn = LogoManager.getInstance().getStatusOnIcon();
        imageStatusOff = LogoManager.getInstance().getStatusOffIcon();
        VBox vBoxConnStatus = new VBox(5);
        vBoxConnStatus.setAlignment(Pos.CENTER);

        HBox hBoxFanucCtrlStatus = new HBox(5);
        hBoxFanucCtrlStatus.setAlignment(Pos.CENTER_LEFT);
        ivFanucConnStatus = new ImageView();
        ivFanucConnStatus.setFitWidth(16);
        ivFanucConnStatus.setFitHeight(16);
        ivFanucConnStatus.setImage(imageStatusOff);
        txtFanucConnStatus = new Label("PLC Disconnected");
        txtFanucConnStatus.setFont(Font.font("Tahoma", FontWeight.BOLD, 12));
        txtFanucConnStatus.setTextFill(Color.WHITE);
        hBoxFanucCtrlStatus.getChildren().addAll(ivFanucConnStatus, txtFanucConnStatus);

        HBox hBoxPLCCtrlStatus = new HBox(5);
        hBoxPLCCtrlStatus.setAlignment(Pos.CENTER_LEFT);
        ivServerConnStatus = new ImageView();
        ivServerConnStatus.setFitWidth(16);
        ivServerConnStatus.setFitHeight(16);
        ivServerConnStatus.setImage(imageStatusOff);
        txtServerConnStatus = new Label("Server Disconnected");
        txtServerConnStatus.setFont(Font.font("Tahoma", FontWeight.BOLD, 12));
        txtServerConnStatus.setTextFill(Color.WHITE);
        hBoxPLCCtrlStatus.getChildren().addAll(ivServerConnStatus, txtServerConnStatus);

        vBoxConnStatus.getChildren().addAll(hBoxFanucCtrlStatus, hBoxPLCCtrlStatus);

        // Setting Button
        btnSetttings = ViewHelper.makeMenuIconButton("", Resource.MENU_ICON_SIZE, 10, LogoManager.getInstance().getMenuSettingImage(), "");
        btnSetttings.setOnAction(buttonHandler);

        hBox.getChildren().addAll(vMachineInfoBox, leftRegion, ivFactoryLogo, rightRegion, vBoxConnStatus, btnSetttings);

        vTopBox.getChildren().addAll(hBox, new Separator());

        return vTopBox;
    }

    private VBox getBottomView() {
        VBox vBottomBox = new VBox();
        vBottomBox.setSpacing(5);
        vBottomBox.setPadding(new Insets(10, 0, 10, 0));

        HBox hTimeBox = new HBox();
        hTimeBox.setAlignment(Pos.CENTER);
        hTimeBox.setPadding(new Insets(0, 10, 10, 10));
        hTimeBox.setSpacing(10);

        Label titleInCycle = new Label("In Cycle :");
        titleInCycle.getStyleClass().addAll("label-incycle");
        titleInCycle.setTextFill(Color.GREEN);
        hTimeBox.getChildren().add(titleInCycle);

        tvSyncTime = new Label("00:00:00");
        tvSyncTime.setPadding(new Insets(0, 15, 0, 0));
        tvSyncTime.getStyleClass().addAll("label-incycle");
        tvSyncTime.setTextFill(Color.GREEN);
        hTimeBox.getChildren().add(tvSyncTime);

        Label titleIdle = new Label("Idle :");
        titleIdle.getStyleClass().addAll("label-idle");
        titleIdle.setTextFill(Color.ORANGE);
        titleIdle.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                //MMSDownTImeReasonsView.getInstance().show();
                panelIdleSetUp.setVisible(true);
            }
        });
        hTimeBox.getChildren().add(titleIdle);

        tvIdleTime = new Label("00:00:00");
        tvIdleTime.setPadding(new Insets(0, 15, 0, 0));
        tvIdleTime.getStyleClass().addAll("label-idle");
        tvIdleTime.setTextFill(Color.ORANGE);
        tvIdleTime.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                panelIdleSetUp.setVisible(true);
            }
        });
        hTimeBox.getChildren().add(tvIdleTime);

        Label titleUncate = new Label("Uncategorized :");
        titleUncate.getStyleClass().addAll("label-uncate");
        titleUncate.setTextFill(Color.RED);
        hTimeBox.getChildren().add(titleUncate);

        tvUnCatTime = new Label("00:00:00");
        tvUnCatTime.setPadding(new Insets(0, 15, 0, 0));
        tvUnCatTime.getStyleClass().addAll("label-uncate");
        tvUnCatTime.setTextFill(Color.RED);
        hTimeBox.getChildren().add(tvUnCatTime);

        anminBlinkSync = new FadeTransition(Duration.millis(500), tvSyncTime);
        anminBlinkSync.setFromValue(1.0);
        anminBlinkSync.setToValue(0.3);
        anminBlinkSync.setCycleCount(INDEFINITE);
        anminBlinkSync.setAutoReverse(true);

        anminBlinkIdle = new FadeTransition(Duration.millis(500), tvIdleTime);
        anminBlinkIdle.setFromValue(1.0);
        anminBlinkIdle.setToValue(0.3);
        anminBlinkIdle.setCycleCount(INDEFINITE);
        anminBlinkIdle.setAutoReverse(true);

        anminBlinkUnCat = new FadeTransition(Duration.millis(500), tvUnCatTime);
        anminBlinkUnCat.setFromValue(1.0);
        anminBlinkUnCat.setToValue(0.3);
        anminBlinkUnCat.setCycleCount(INDEFINITE);
        anminBlinkUnCat.setAutoReverse(true);
        anminBlinkUnCat.play();

        HBox hButtonBox = new HBox();
        hButtonBox.setAlignment(Pos.CENTER);
        hButtonBox.setSpacing(10);

        labelCurrentTime = new Label(DateTimeUtils.getDateTime());
        labelCurrentTime.setPadding(new Insets(0, 15, 0, 0));
        labelCurrentTime.getStyleClass().addAll("label-currenttime");
        hButtonBox.getChildren().add(labelCurrentTime);

        timeline = new Timeline(new KeyFrame(Duration.millis(1000), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                labelCurrentTime.setText(DateTimeUtils.getDateTime());
            }
        }));
        timeline.setCycleCount(INDEFINITE);
        timeline.setAutoReverse(false);
        timeline.play();

        // Button Close And Save
        btnClose = new Button("Close");
        btnClose.setAlignment(Pos.CENTER);
        btnClose.getStyleClass().add("button-gradient5");
        btnClose.setPrefWidth(150);
        btnClose.setMaxWidth(200);
        btnClose.setMinWidth(120);
        BorderPane.setMargin(btnClose, new Insets(10, 10, 10, 10));
        hButtonBox.getChildren().add(btnClose);
        btnClose.setOnAction(buttonHandler);

        // Right Margin ------------------------------------------------
        Region rightRegion = new Region();
        HBox.setHgrow(rightRegion, Priority.ALWAYS);
        // -------------------------------------------------------------

        hTimeBox.getChildren().addAll(rightRegion, hButtonBox);

        vBottomBox.getChildren().addAll(new Separator(), hTimeBox);

        return vBottomBox;
    }

    private HBox getCenterView() {
        HBox hCenterBox = new HBox();
        hCenterBox.setAlignment(Pos.CENTER);
        hCenterBox.setSpacing(10);
        hCenterBox.setPadding(new Insets(10, 10, 10, 10));

        // Left Panels
        VBox vLeftBox = new VBox();
        //vLeftBox.setAlignment(Pos.CENTER);
        vLeftBox.setPrefHeight(1000);
        vLeftBox.setPrefWidth(1000);
        //vLeftBox.getStyleClass().addAll("vbox-gauge-background");
        vLeftBox.setSpacing(10);
        vLeftBox.setPadding(new Insets(0, 0, 0, 0));
        HBox.setHgrow(vLeftBox, Priority.ALWAYS);

        HBox userInfoPanel = getUserInfoPanel();
        AnchorPane jobInfoPanel = getJobInfoPanel();
        VBox.setVgrow(jobInfoPanel, Priority.ALWAYS);
        HBox goodsBadsInfoPanel = getGoodsBadsPartPanel();

        vLeftBox.getChildren().addAll(userInfoPanel, jobInfoPanel, goodsBadsInfoPanel);

        // Right Panels
        VBox vRightBox = new VBox();
        vRightBox.setAlignment(Pos.CENTER);
        vRightBox.setSpacing(5);
        vRightBox.setPrefHeight(1000);
        vRightBox.setPrefWidth(1000);
        vRightBox.getStyleClass().addAll("vbox-gauge-background");
        vRightBox.setPadding(new Insets(0, 0, 0, 0));
        HBox.setHgrow(vRightBox, Priority.ALWAYS);

        Pagination gaugeInfoPanel = getGaugeInfoPanel();
        VBox.setVgrow(gaugeInfoPanel, Priority.ALWAYS);

        vRightBox.getChildren().add(getGaugeInfoPanel());

        hCenterBox.getChildren().addAll(vLeftBox, vRightBox);

        return hCenterBox;
    }

    private HBox getUserInfoPanel() {
        HBox hBox = new HBox();
        hBox.setSpacing(10);
        hBox.setPadding(new Insets(10, 10, 10, 10));
        hBox.getStyleClass().addAll("vbox-gauge-background");

        HBox hAvatarNameBox = new HBox();
        hAvatarNameBox.setPadding(new Insets(5, 5, 5, 5));
        hAvatarNameBox.setAlignment(Pos.CENTER);
        hAvatarNameBox.setSpacing(15);
        HBox.setHgrow(hAvatarNameBox, Priority.ALWAYS);

        // https://www.demo2s.com/java/javafx-imageview-setclip-node-value.html
        circleAvatar = new Circle(22.5);
        circleAvatar.setEffect(new DropShadow(+25d, 0d, +2d, Color.WHITE));
        circleAvatar.setFill(new ImagePattern(LogoManager.getInstance().getAvatarLogo()));

        /*circleAvatar.setFill(new ImagePattern(LogoManager.getInstance().getAvatarLogo()));
        try {
            String imageLogoUrl = PreferenceManager.getUserAvatar();
            imageLogoUrl = Utils.getEscapedUriString(imageLogoUrl);

            //utf8EncodedString = URLEncoder.encode(imageLogoUrl, "UTF-8").replaceAll("\\+", "%20");
            *//*URLConnection connection = new URL(utf8EncodedString).openConnection();
            connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
            Image logoImage = new Image(connection.getInputStream());
            ivFactoryLogo.setImage(logoImage);*//*

            Image logoImage = new Image(imageLogoUrl, true);
            logoImage.errorProperty().addListener(new ChangeListener<Boolean>() {
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    System.out.println(newValue);  //Location 4
                }
            });
            logoImage.progressProperty().addListener(new ChangeListener<Number>() {
                @Override
                public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                    if (newValue.floatValue() >= 1.0) {
                        circleAvatar.setFill(new ImagePattern(logoImage));
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        tvUserName = new Label(PreferenceManager.getUserName());
        tvUserName.setPadding(new Insets(3, 3, 3, 3));
        tvUserName.getStyleClass().add("label-info-value-large");
        tvUserName.setTextFill(Color.WHITE);
        tvUserName.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                mmsAgent.resetShiftData(false);
            }
        });

        hAvatarNameBox.getChildren().addAll(circleAvatar, tvUserName);

        btnLogin = ViewHelper.makeMenuIconButton("", Resource.MENU_ICON_SIZE, 10, LogoManager.getInstance().getLoginIcon(), "Login");
        btnLogout = ViewHelper.makeMenuIconButton("", Resource.MENU_ICON_SIZE, 10, LogoManager.getInstance().getExitIcon(), "Logout");

        btnLogin.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                LoginView.getInstance().show(new LoginResultListener() {
                    @Override
                    public void onLoginSuccess() {
                        showUserInfo();
                    }

                    @Override
                    public void onLoginCancelled() {}
                });
            }
        });

        btnLogout.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.initModality(Modality.WINDOW_MODAL);
                alert.setTitle("Logout");
                Stage dialogStage = (Stage) alert.getDialogPane().getScene().getWindow();
                dialogStage.getIcons().add(LogoManager.getInstance().getLogo());
                alert.setHeaderText("Please confirm.");
                alert.setContentText("Would you like to logout?");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK) {
                    logout();
                }
            }
        });

        hBox.getChildren().addAll(hAvatarNameBox, btnLogin, btnLogout);

        return hBox;
    }

    private AnchorPane getJobInfoPanel() {
        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setPadding(new Insets(10, 10, 10, 10));
        anchorPane.getStyleClass().addAll("vbox-gauge-background");

        VBox vJobInfoBox = new VBox();
        //vJobInfoBox.setAlignment(Pos.CENTER);
        vJobInfoBox.setSpacing(5);
        vJobInfoBox.setPadding(new Insets(0, 0, 0, 0));
        vJobInfoBox.setFillWidth(true);
        AnchorPane.setRightAnchor(vJobInfoBox, 0.0);
        AnchorPane.setLeftAnchor(vJobInfoBox, 0.0);
        AnchorPane.setTopAnchor(vJobInfoBox, 0.0);
        AnchorPane.setBottomAnchor(vJobInfoBox, 0.0);

        // Job ID Panel -------------------------------------------------
        HBox hJobIDBox = new HBox();
        hJobIDBox.setAlignment(Pos.CENTER);
        hJobIDBox.setPadding(new Insets(0, 0, 0, 10));
        hJobIDBox.setSpacing(10);

        Label titleJobID = new Label("Job ID :");
        titleJobID.getStyleClass().addAll("label-jobinfo");

        tvJobId = new Label("");
        tvJobId.setPadding(new Insets(0, 15, 0, 0));
        tvJobId.getStyleClass().addAll("label-jobinfo");
        tvJobId.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                mmsAgent.resetShiftData(false);
            }
        });

        labelJobState = new Label("(Setup)");
        labelJobState.setPadding(new Insets(0, 15, 0, 0));
        labelJobState.getStyleClass().addAll("label-jobinfo", "job-status-setup");


        Region space = new Region();
        HBox.setHgrow(space, Priority.ALWAYS);

        btnEditJobInfo = ViewHelper.makeMenuIconButton("", Resource.MENU_ICON_SIZE, 10, LogoManager.getInstance().getImgJobEdit(), "Job Information");
        btnEditJobInfo.setOnAction(buttonHandler);

        hJobIDBox.getChildren().addAll(titleJobID, tvJobId, labelJobState, space, btnEditJobInfo);
        // -------------------------------------------------------------

        // Job Info Panel -------------------------------------------------
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.pannableProperty().set(true);
        scrollPane.fitToWidthProperty().set(true);
        scrollPane.getStyleClass().add("vbox-gauge-background-trans");
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        tvJobDetails = new Label("Customer : AMEURO METALS B.V.\n" +
                "Part Number : WO0022571-1/1\n" +
                "Program Number :\n" +
                "Description : \n" +
                "Parts Per Cycle :\n" +
                "Target Cycle Time :\n" +
                "Qty Required :\n" +
                "Qty Good Completed :\n" +
                "Aux1Data :\n" +
                "Aux2Data :\n" +
                "Aux3Data :\n" +
                "Aux4Data :\n" +
                "Aux5Data :\n" +
                "Aux6Data :\n" +
                "Aux7Data :\n" +
                "Aux8Data :\n" +
                "Aux9Data :\n" +
                "Aux10Data :\n" +
                "Aux11Data :\n" +
                "Aux12Data :");

        tvJobDetails.setPadding(new Insets(0, 0, 0, 10));
        tvJobDetails.getStyleClass().addAll("label-jobinfo-details");

        scrollPane.setContent(tvJobDetails);
        // -------------------------------------------------------------

        vJobInfoBox.getChildren().addAll(hJobIDBox, scrollPane);

        anchorPane.getChildren().addAll(vJobInfoBox);

        return anchorPane;
    }

    private HBox getGoodsBadsPartPanel() {
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(10);

        // Left Goods Panels
        VBox vGoodsBox = new VBox();
        vGoodsBox.setAlignment(Pos.CENTER);
        vGoodsBox.setSpacing(5);
        vGoodsBox.getStyleClass().addAll("vbox-gauge-background");
        vGoodsBox.setPadding(new Insets(0, 0, 0, 0));
        vGoodsBox.setFillWidth(true);
        HBox.setHgrow(vGoodsBox, Priority.ALWAYS);

        Label titleGoodParts = new Label("Good Parts");
        titleGoodParts.setTextFill(Color.GREEN);
        titleGoodParts.getStyleClass().addAll("label-incycle");

        HBox hBoxGoodButtons = new HBox();
        hBoxGoodButtons.setAlignment(Pos.CENTER);
        hBoxGoodButtons.setSpacing(10);
        hBoxGoodButtons.setPadding(new Insets(5, 5, 5, 5));

        btnGoodUp = ViewHelper.makeMenuIconButton("", Resource.MENU_ICON_SIZE, 10, LogoManager.getInstance().getImgGoodUp(), "");
        btnGoodUp.setOnAction(buttonHandler);
        btnGoodDown = ViewHelper.makeMenuIconButton("", Resource.MENU_ICON_SIZE, 10, LogoManager.getInstance().getImgGoodDown(), "");
        ;
        btnGoodDown.setOnAction(buttonHandler);
        tvGood = new Label(String.valueOf(PreferenceManager.getShiftGoodParts()));
        HBox.setHgrow(tvGood, Priority.ALWAYS);
        tvGood.setPrefWidth(250);
        tvGood.setMinWidth(150);
        tvGood.setTextAlignment(TextAlignment.CENTER);
        tvGood.setTextFill(Color.GREEN);
        tvGood.getStyleClass().addAll("label-incycle", "label-center");

        hBoxGoodButtons.getChildren().addAll(btnGoodDown, tvGood, btnGoodUp);
        vGoodsBox.getChildren().addAll(titleGoodParts, hBoxGoodButtons);


        // Right Bad Panels
        VBox vBadsBox = new VBox();
        vBadsBox.setAlignment(Pos.CENTER);
        vBadsBox.setSpacing(5);
        vBadsBox.getStyleClass().addAll("vbox-gauge-background");
        vBadsBox.setPadding(new Insets(0, 0, 0, 0));
        vBadsBox.setFillWidth(true);
        HBox.setHgrow(vBadsBox, Priority.ALWAYS);

        Label titleBadParts = new Label("Bad Parts");
        titleBadParts.setTextFill(Color.RED);
        titleBadParts.getStyleClass().addAll("label-uncate");

        HBox hBoxBadButtons = new HBox();
        hBoxBadButtons.setAlignment(Pos.CENTER);
        hBoxBadButtons.setSpacing(10);
        hBoxBadButtons.setPadding(new Insets(5, 5, 5, 5));

        btnBadUp = ViewHelper.makeMenuIconButton("", Resource.MENU_ICON_SIZE, 10, LogoManager.getInstance().getImgBadUp(), "");
        btnBadUp.setOnAction(buttonHandler);
        btnBadDown = ViewHelper.makeMenuIconButton("", Resource.MENU_ICON_SIZE, 10, LogoManager.getInstance().getImgBadDown(), "");

        btnBadDown.setOnAction(buttonHandler);
        tvBad = new Label(String.valueOf(PreferenceManager.getShiftBadParts()));
        HBox.setHgrow(tvBad, Priority.ALWAYS);
        tvBad.setPrefWidth(250);
        tvBad.setMinWidth(150);
        tvBad.setTextFill(Color.RED);
        tvBad.getStyleClass().addAll("label-uncate", "label-center");
        tvBad.setTextAlignment(TextAlignment.CENTER);

        hBoxBadButtons.getChildren().addAll(btnBadDown, tvBad, btnBadUp);
        vBadsBox.getChildren().addAll(titleBadParts, hBoxBadButtons);

        hBox.getChildren().addAll(vGoodsBox, vBadsBox);

        return hBox;
    }

    private Pagination getGaugeInfoPanel() {
        Pagination pagination = new Pagination(2, 0);
        pagination.getStyleClass().add(Pagination.STYLE_CLASS_BULLET);
        pagination.setPageFactory(new Callback<Integer, Node>() {
            @Override
            public Node call(Integer pageIndex) {
                return createPage(pageIndex);
            }
        });

        return pagination;
    }

    private Node getRegimeGaugePanel() {
        VBox vRegimeBox = new VBox();
        vRegimeBox.setAlignment(Pos.CENTER);
        vRegimeBox.setSpacing(20);
        vRegimeBox.setPadding(new Insets(10, 10, 10, 10));

        Label titleOEE = new Label("OEE");
        titleOEE.setPadding(new Insets(3, 3, 3, 3));
        titleOEE.getStyleClass().add("label-info-value-large");
        titleOEE.setTextFill(Color.WHITE);

        // Left Panels
        HBox hTopBox = new HBox();
        hTopBox.setAlignment(Pos.CENTER);
        hTopBox.setSpacing(15);
        hTopBox.setPadding(new Insets(10, 10, 10, 10));
        hTopBox.setPrefHeight(1000);
        hTopBox.setPrefWidth(1000);
        VBox.setVgrow(hTopBox, Priority.ALWAYS);

        gaugeAvailablity = GaugeBuilder.create()
                .prefSize(300, 300)
                .skinType(Gauge.SkinType.MODERN)
                .title("Availability")
                .unit("%")
                .returnToZero(false)
                .animated(true)
                .animationDuration(800)
                .smoothing(true)
                .decimals(0)
                .tickLabelDecimals(0)
                .tickLabelColor(Color.WHITE)
                .needleBehavior(Gauge.NeedleBehavior.STANDARD)
                .prefHeight(200)
                .barColor(Color.web("#303030"))
                .lcdFont(LcdFont.LCD)
                .sections(new Section(0, 30, Color.web("#ea6d41", 1)),
                        new Section(30, 60, Color.web("#f0a616", 1)),
                        new Section(60, 100, Color.web("#4bb567", 1)))
                .sectionsVisible(true)
                .value(0)
                .build();

        gaugeOEE = GaugeBuilder.create()
                .prefSize(300, 300)
                .skinType(Gauge.SkinType.MODERN)
                .title("OEE")
                .unit("%")
                .returnToZero(false)
                .animated(true)
                .animationDuration(800)
                .smoothing(true)
                .decimals(0)
                .tickLabelDecimals(0)
                .tickLabelColor(Color.WHITE)
                .needleBehavior(Gauge.NeedleBehavior.STANDARD)
                .prefHeight(200)
                .barColor(Color.web("#303030"))
                .lcdFont(LcdFont.LCD)
                .sections(new Section(0, 30, Color.web("#ea6d41", 1)),
                        new Section(30, 60, Color.web("#f0a616", 1)),
                        new Section(60, 100, Color.web("#4bb567", 1)))
                .sectionsVisible(true)
                .value(0)
                .build();

        hTopBox.getChildren().addAll(gaugeAvailablity, gaugeOEE);

        // Bottom Panels
        HBox hBottomBox = new HBox();
        hBottomBox.setAlignment(Pos.CENTER);
        hBottomBox.setSpacing(15);
        hBottomBox.setPadding(new Insets(10, 10, 10, 10));
        hBottomBox.setPrefHeight(1000);
        hBottomBox.setPrefWidth(1000);
        VBox.setVgrow(hBottomBox, Priority.ALWAYS);

        gaugePerformance = GaugeBuilder.create()
                .prefSize(300, 300)
                .skinType(Gauge.SkinType.MODERN)
                .title("Performance")
                .unit("%")
                .returnToZero(false)
                .animated(true)
                .animationDuration(800)
                .smoothing(true)
                .decimals(0)
                .tickLabelDecimals(0)
                .tickLabelColor(Color.WHITE)
                .needleBehavior(Gauge.NeedleBehavior.STANDARD)
                .prefHeight(200)
                .barColor(Color.web("#303030"))
                .lcdFont(LcdFont.LCD)
                .sections(new Section(0, 30, Color.web("#ea6d41", 1)),
                        new Section(30, 60, Color.web("#f0a616", 1)),
                        new Section(60, 100, Color.web("#4bb567", 1)))
                .sectionsVisible(true)
                .value(0)
                .build();

        gaugeQuality = GaugeBuilder.create()
                .prefSize(300, 300)
                .skinType(Gauge.SkinType.MODERN)
                .title("Quality")
                .unit("%")
                .returnToZero(false)
                .animated(true)
                .animationDuration(800)
                .smoothing(true)
                .decimals(0)
                .tickLabelDecimals(0)
                .tickLabelColor(Color.WHITE)
                .needleBehavior(Gauge.NeedleBehavior.STANDARD)
                .prefHeight(200)
                .barColor(Color.web("#303030"))
                .lcdFont(LcdFont.LCD)
                .sections(new Section(0, 30, Color.web("#ea6d41", 1)),
                        new Section(30, 60, Color.web("#f0a616", 1)),
                        new Section(60, 100, Color.web("#4bb567", 1)))
                .sectionsVisible(true)
                .value(0)
                .build();

        hBottomBox.getChildren().addAll(gaugePerformance, gaugeQuality);

        vRegimeBox.getChildren().addAll(titleOEE, hTopBox, hBottomBox);

        return vRegimeBox;
    }

    private Node get24hrUtilsPanel() {
        VBox vRegimeBox = new VBox();
        vRegimeBox.setAlignment(Pos.CENTER);
        vRegimeBox.setSpacing(10);
        vRegimeBox.setFillWidth(true);
        vRegimeBox.setPadding(new Insets(10, 10, 10, 10));

        Label titleOEE = new Label("24hr Utilization");
        titleOEE.setPadding(new Insets(3, 3, 3, 3));
        titleOEE.getStyleClass().add("label-info-value-large");
        titleOEE.setTextFill(Color.WHITE);

        // Left Panels
        HBox hTopBox = new HBox();
        hTopBox.setAlignment(Pos.CENTER);
        hTopBox.setSpacing(5);
        hTopBox.setFillHeight(true);
        hTopBox.setPrefHeight(1000);
        hTopBox.setPrefWidth(1000);
        hTopBox.setPadding(new Insets(0, 0, 0, 0));
        VBox.setVgrow(hTopBox, Priority.ALWAYS);

        pieChartData.addAll(new PieChart.Data("Cycle", 0),
                new PieChart.Data("Uncategorized", 0),
                new PieChart.Data("Clear Chips", 0),
                new PieChart.Data("Wait materials", 0),
                new PieChart.Data("Break", 0),
                new PieChart.Data("No Operator", 0),
                new PieChart.Data("P.M.", 0),
                new PieChart.Data("OKOK", 0),
                new PieChart.Data("Other", 0),
                new PieChart.Data("Offline", 0));

        //Creating a Pie chart
        pieChart = new PieChart(pieChartData);
        //Setting the title of the Pie chart
        pieChart.setTitle("");
        pieChart.setLegendSide(Side.BOTTOM);
        //setting the direction to arrange the data
        pieChart.setClockwise(true);
        //Setting the length of the label line
        pieChart.setLabelLineLength(15);
        //Setting the labels of the pie chart visible
        pieChart.setLabelsVisible(true);
        //Setting the start angle of the pie chart
        pieChart.setStartAngle(90);

        HBox.setHgrow(pieChart, Priority.ALWAYS);

        hTopBox.getChildren().addAll(pieChart);

        vRegimeBox.getChildren().addAll(titleOEE, hTopBox);

        return vRegimeBox;
    }

    public Node createPage(int pageIndex) {

        /*VBox box = new VBox(5);
        int page = pageIndex * 10;
        for (int i = page; i < page + 10; i++) {
            VBox element = new VBox();
            Hyperlink link = new Hyperlink("Item " + (i+1));
            link.setTextFill(Color.WHITE);
            link.setVisited(true);
            Label text = new Label("Search results\nfor "+ link.getText());
            text.setTextFill(Color.WHITE);
            element.getChildren().addAll(link, text);
            box.getChildren().add(element);
        }
        return box;*/

        if (pageIndex == 0) {
            if (regimeGaugePanel == null) {
                regimeGaugePanel = getRegimeGaugePanel();
            }
            return regimeGaugePanel;
        } else {
            if (regime24UtilsPanel == null) {
                regime24UtilsPanel = get24hrUtilsPanel();
            }
            return regime24UtilsPanel;
        }
    }

    private AnchorPane getDowntimeReasonPanel() {
        AnchorPane viewDowntimeReasonPane = new AnchorPane();

        ivDowntimeBack = new ImageView();
        Image image = LogoManager.getInstance().getSplash();
        ivDowntimeBack.setImage(image);
        ivDowntimeBack.fitWidthProperty().bind(viewDowntimeReasonPane.widthProperty());
        ivDowntimeBack.fitHeightProperty().bind(viewDowntimeReasonPane.heightProperty());
        ivDowntimeBack.setSmooth(true);
        ivDowntimeBack.setCache(true);
        ivDowntimeBack.setPreserveRatio(true);
        double oldImageWidth = image.getWidth(), oldImageHeight = image.getHeight();            //saving the original image size and ratio
        double imageRatio = oldImageWidth / oldImageHeight;

        ChangeListener<Number> listener = (obs, ov, nv) -> {
            double paneWidth = viewDowntimeReasonPane.getWidth();
            double paneHeight = viewDowntimeReasonPane.getHeight();

            double paneRatio = paneWidth / paneHeight;                                          //calculating the new pane's ratio
            //after width or height changed
            double newImageWidth = oldImageWidth, newImageHeight = oldImageHeight;

            if (paneRatio > imageRatio) {
                newImageHeight = oldImageWidth / paneRatio;
            } else if (paneRatio < imageRatio) {
                newImageWidth = oldImageHeight * paneRatio;
            }

            ivDowntimeBack.setViewport(new Rectangle2D(                                     // The rectangle used to crop
                    (oldImageWidth - newImageWidth) / 2, (oldImageHeight - newImageHeight) / 2, //MinX and MinY to crop from the center
                    newImageWidth, newImageHeight)                                              // new width and height
            );

            //ivDowntimeBack.setFitWidth(paneWidth);
        };

        viewDowntimeReasonPane.widthProperty().addListener(listener);
        viewDowntimeReasonPane.heightProperty().addListener(listener);

        //centerImage();

        AnchorPane.setTopAnchor(ivDowntimeBack, 0.);
        AnchorPane.setRightAnchor(ivDowntimeBack, 0.);
        AnchorPane.setBottomAnchor(ivDowntimeBack, 0.);
        AnchorPane.setLeftAnchor(ivDowntimeBack, 0.);

        VBox imgBlueBack = new VBox();
        imgBlueBack.setBackground(new Background(new BackgroundFill(Color.web("#1583fe", 0.4), CornerRadii.EMPTY, Insets.EMPTY)));
        AnchorPane.setTopAnchor(imgBlueBack, 0.);
        AnchorPane.setRightAnchor(imgBlueBack, 0.);
        AnchorPane.setBottomAnchor(imgBlueBack, 0.);
        AnchorPane.setLeftAnchor(imgBlueBack, 0.);

        backCsLockStatus = new VBox();
        backCsLockStatus.setBackground(new Background(new BackgroundFill(Color.web("#ffa500", 0), CornerRadii.EMPTY, Insets.EMPTY)));
        AnchorPane.setTopAnchor(backCsLockStatus, 0.);
        AnchorPane.setRightAnchor(backCsLockStatus, 0.);
        AnchorPane.setBottomAnchor(backCsLockStatus, 0.);
        AnchorPane.setLeftAnchor(backCsLockStatus, 0.);

        // Main Button Status
        VBox vDowntimeReasonBox = new VBox();
        //vJobInfoBox.setAlignment(Pos.CENTER);
        vDowntimeReasonBox.setSpacing(10);
        vDowntimeReasonBox.setPadding(new Insets(15, 15, 15, 15));
        vDowntimeReasonBox.setFillWidth(true);
        AnchorPane.setTopAnchor(vDowntimeReasonBox, 0.);
        AnchorPane.setRightAnchor(vDowntimeReasonBox, 0.);
        AnchorPane.setBottomAnchor(vDowntimeReasonBox, 0.);
        AnchorPane.setLeftAnchor(vDowntimeReasonBox, 0.);

        tvTitleStatusInterlocked = new Label("Cycle Start Interlocked");
        tvTitleStatusInterlocked.setPrefWidth(Double.POSITIVE_INFINITY);
        tvTitleStatusInterlocked.setPadding(new Insets(3, 3, 3, 3));
        tvTitleStatusInterlocked.getStyleClass().addAll("label-info-value-xlarge", "label-black", "label-center");
        tvTitleStatusInterlocked.setTextFill(Color.BLACK);

        tvElapsedIdleTIme = new Label("Elapsed Idle Time : 00:00:00");
        tvElapsedIdleTIme.setPrefWidth(Double.POSITIVE_INFINITY);
        tvElapsedIdleTIme.setPadding(new Insets(3, 3, 3, 3));
        tvElapsedIdleTIme.getStyleClass().addAll("label-info-value-large", "label-black", "label-center");
        tvElapsedIdleTIme.setTextFill(Color.BLACK);

        HBox hBox1 = new HBox(10);
        hBox1.setFillHeight(true);
        VBox.setVgrow(hBox1, Priority.ALWAYS);
        btnDownTimeStatus[0] = getDowntimeButton("Downtime Reason1\n00:00:00");
        btnDownTimeStatus[1] = getDowntimeButton("Downtime Reason2\n00:00:00");
        hBox1.getChildren().addAll(btnDownTimeStatus[0], btnDownTimeStatus[1]);

        HBox hBox2 = new HBox(10);
        hBox2.setFillHeight(true);
        VBox.setVgrow(hBox2, Priority.ALWAYS);
        btnDownTimeStatus[2] = getDowntimeButton("Downtime Reason3\n00:00:00");
        btnDownTimeStatus[3] = getDowntimeButton("Downtime Reason4\n00:00:00");
        hBox2.getChildren().addAll(btnDownTimeStatus[2], btnDownTimeStatus[3]);

        HBox hBox3 = new HBox(10);
        hBox3.setFillHeight(true);
        VBox.setVgrow(hBox3, Priority.ALWAYS);
        btnDownTimeStatus[4] = getDowntimeButton("Downtime Reason5\n00:00:00");
        btnDownTimeStatus[5] = getDowntimeButton("Downtime Reason6\n00:00:00");
        hBox3.getChildren().addAll(btnDownTimeStatus[4], btnDownTimeStatus[5]);

        HBox hBox4 = new HBox(10);
        hBox4.setFillHeight(true);
        VBox.setVgrow(hBox4, Priority.ALWAYS);
        btnDownTimeStatus[6] = getDowntimeButton("Downtime Reason7\n00:00:00");
        btnDownTimeStatus[7] = getDowntimeButton("Downtime Reason8\n00:00:00");
        hBox4.getChildren().addAll(btnDownTimeStatus[6], btnDownTimeStatus[7]);

        // Add Event
        for (Button button : btnDownTimeStatus) {
            button.setOnAction(buttonDowntimeReasonHandler);
        }

        btnCloseIdleStatus = getDowntimeButton("Close");
        btnCloseIdleStatus.setPrefWidth(1200);
        btnCloseIdleStatus.setOnAction(buttonHandler);

        vDowntimeReasonBox.getChildren().addAll(tvTitleStatusInterlocked, tvElapsedIdleTIme, hBox1, hBox2, hBox3, hBox4, btnCloseIdleStatus);

        viewDowntimeReasonPane.getChildren().addAll(ivDowntimeBack, imgBlueBack, backCsLockStatus, vDowntimeReasonBox);

        return viewDowntimeReasonPane;
    }

    public void centerImage() {
        /*Image img = ivDowntimeBack.getImage();
        if (img != null) {
            double w = 0;
            double h = 0;

            double ratioX = ivDowntimeBack.getFitWidth() / img.getWidth();
            double ratioY = ivDowntimeBack.getFitHeight() / img.getHeight();

            double reducCoeff = 0;
            if(ratioX >= ratioY) {
                reducCoeff = ratioY;
            } else {
                reducCoeff = ratioX;
            }

            w = img.getWidth() * reducCoeff;
            h = img.getHeight() * reducCoeff;

            ivDowntimeBack.setTranslateX((ivDowntimeBack.getFitWidth() - w) / 2);
            ivDowntimeBack.setTranslateY((ivDowntimeBack.getFitHeight() - h) / 2);
        }*/
    }

    private Button getDowntimeButton(String title) {
        Button downTimeButton = new Button(title);
        //downTimeButton.setPrefHeight(45);
        downTimeButton.setPrefWidth(700);
        downTimeButton.setMaxWidth(Double.POSITIVE_INFINITY);
        downTimeButton.getStyleClass().addAll("button-downtime");
        downTimeButton.setTextAlignment(TextAlignment.CENTER);
        HBox.setHgrow(downTimeButton, Priority.ALWAYS);

        return downTimeButton;
    }

    final EventHandler<ActionEvent> buttonHandler = new EventHandler<ActionEvent>() {

        @Override
        public void handle(final ActionEvent event) {

            if (event.getSource() == btnSetttings) {
                MMSSettingView.getInstance().show();
            } else if (event.getSource() == btnClose) {
                // Close Stage
                screen_stage.close();
            } else if (event.getSource() == btnEditJobInfo) {
                // Job Actions
                new JobInfoActionView().show(new JobInfoActionView.onJobActionListener() {
                    @Override
                    public void onInputJobID() {
                        // Input Factory ID and get info
                        TextInputDialog dialog = new TextInputDialog(PreferenceManager.getJobID());

                        dialog.setTitle("Job ID Input");
                        dialog.setHeaderText(null);
                        dialog.setContentText("Job ID:");

                        Optional<String> result = dialog.showAndWait();
                        result.ifPresent(name -> {

                            if (name != null && !name.isEmpty()) {
                                getJobInfo(name);
                            }
                        });
                    }

                    @Override
                    public void onLogoutOfJob() {
                        PreferenceManager.setJobReworkStatus(false);
                        PreferenceManager.setJobSetupStatus(false);

                        PreferenceManager.setJobID("");
                        PreferenceManager.setJobDetails("");

                        showJobInformation();
                    }

                    @Override
                    public void onUpdateSetUpStatus(boolean setup) {
                        // *Should set first before change job info
                        PreferenceManager.setJobReworkStatus(false);
                        PreferenceManager.setJobSetupStatus(setup);

                        // Update Setup Status
                        mmsAgent.shiftData.setStatusRework(0);
                        mmsAgent.shiftData.setStatusSetup(setup ? 1 : 0);
                        DBTableShiftData.updateShiftData(mmsAgent.shiftData);

                        showJobInformation();
                    }
                });
            } else if (event.getSource() == btnGoodDown) {
                int good_val = Integer.parseInt(tvGood.getText().toString());
                if (good_val > 0) {
                    good_val--;
                    tvGood.setText(String.valueOf(good_val));
                    PreferenceManager.setShiftGoodParts(good_val);
                    mmsAgent.shiftData.setGoodParts(good_val);

                    // Daily Live Data
                    if (PreferenceManager.getGoodParts() > 0) {
                        PreferenceManager.setGoodParts(PreferenceManager.getGoodParts() - 1);
                    }
                }

            } else if (event.getSource() == btnGoodUp) {
                int good_val = Integer.parseInt(tvGood.getText().toString());
                good_val++;
                tvGood.setText(String.valueOf(good_val));
                PreferenceManager.setShiftGoodParts(good_val);
                mmsAgent.shiftData.setGoodParts(good_val);

                // Daily Live Data
                PreferenceManager.setGoodParts(PreferenceManager.getGoodParts() + 1);
            } else if (event.getSource() == btnBadDown) {
                int bad_val = Integer.parseInt(tvBad.getText().toString());
                if (bad_val > 0) {
                    bad_val--;
                    tvBad.setText(String.valueOf(bad_val));
                    PreferenceManager.setShiftBadParts(bad_val);
                    mmsAgent.shiftData.setBadParts(bad_val);

                    // Daily Live Data
                    if (PreferenceManager.getBadParts() > 0) {
                        PreferenceManager.setBadParts(PreferenceManager.getBadParts() - 1);
                    }

                    // Increase Good Part
                    /*int good_val = Integer.parseInt(tvGood.getText().toString());
                    good_val++;
                    tvGood.setText(String.valueOf(good_val));
                    PreferenceManager.setShiftGoodParts(good_val);
                    shiftData.setGoodParts(good_val);


                    PreferenceManager.setGoodParts(PreferenceManager.getGoodParts() + 1);*/
                }
            } else if (event.getSource() == btnBadUp) {
                // Increase Bad Part
                int bad_val = Integer.parseInt(tvBad.getText().toString());
                bad_val++;
                tvBad.setText(String.valueOf(bad_val));
                PreferenceManager.setShiftBadParts(bad_val);
                mmsAgent.shiftData.setBadParts(bad_val);

                // Daily Live Data
                PreferenceManager.setBadParts(PreferenceManager.getBadParts() + 1);

                // Decrease Good Part
                /*int good_val = Integer.parseInt(tvGood.getText().toString());
                if (good_val > 0) {
                    good_val--;
                    tvGood.setText(String.valueOf(good_val));
                    PreferenceManager.setShiftGoodParts(good_val);
                    shiftData.setGoodParts(good_val);

                    // Daily Live Data
                    if (PreferenceManager.getGoodParts() > 0) {
                        PreferenceManager.setGoodParts(PreferenceManager.getGoodParts() - 1);
                    }
                }*/
            } else if (event.getSource() == btnCloseIdleStatus) {
                panelIdleSetUp.setVisible(false);
            }
        }
    };

    final EventHandler<ActionEvent> buttonDowntimeReasonHandler = new EventHandler<ActionEvent>() {

        @Override
        public void handle(final ActionEvent event) {

            try {
                int buttonStatusIdx = DEVICE_STATUS_IDLE1;
                Button selectedButton = (Button) event.getSource();

                if (event.getSource() == btnDownTimeStatus[0]) {
                    buttonStatusIdx = DEVICE_STATUS_IDLE1;
                } else if (event.getSource() == btnDownTimeStatus[1]) {
                    buttonStatusIdx = DEVICE_STATUS_IDLE2;
                } else if (event.getSource() == btnDownTimeStatus[2]) {
                    buttonStatusIdx = DEVICE_STATUS_IDLE3;
                } else if (event.getSource() == btnDownTimeStatus[3]) {
                    buttonStatusIdx = DEVICE_STATUS_IDLE4;
                } else if (event.getSource() == btnDownTimeStatus[4]) {
                    buttonStatusIdx = DEVICE_STATUS_IDLE5;
                } else if (event.getSource() == btnDownTimeStatus[5]) {
                    buttonStatusIdx = DEVICE_STATUS_IDLE6;
                } else if (event.getSource() == btnDownTimeStatus[6]) {
                    buttonStatusIdx = DEVICE_STATUS_IDLE7;
                } else if (event.getSource() == btnDownTimeStatus[7]) {
                    buttonStatusIdx = DEVICE_STATUS_IDLE8;
                }

                synchronized (mmsAgent.timeLock) {
                    // Reset Selected Status
                    resetDownTimeReasonStatus();

                    // Set New Selected Button
                    selectedButton.getStyleClass().add("button-downtime-selected");
                    mmsAgent.opDownTimeStatus = buttonStatusIdx;

                    // Change prev uncate to Downtime Reason Status
                    if (mmsAgent.currentDeviceStatus != DEVICE_STATUS_UNCATEGORIZED) {
                        if (mmsAgent.timeLastUncatStartTime > 0) {
                            // TODO
                            // change the gannt data status from uncate to downtime reason
                        }
                    }

                    // Change new Status
                    if (mmsAgent.currentDeviceStatus != DEVICE_STATUS_INCYCLE) {
                        mmsAgent.currentDeviceStatus = mmsAgent.opDownTimeStatus;
                    }

                    // Global Time Logic
                    mmsAgent.elapsedMiliseconds[mmsAgent.currentDeviceStatus] += mmsAgent.elapsedStopTime;
                    mmsAgent.elapsedMiliseconds[DEVICE_STATUS_UNCATEGORIZED] -= mmsAgent.elapsedStopTime;
                    if (mmsAgent.elapsedMiliseconds[DEVICE_STATUS_UNCATEGORIZED] < 0) {
                        // Important Module, when the app pass the midnight In UnCate State and reset the elapsedSeconds,
                        // following module is essential
                        mmsAgent.elapsedMiliseconds[mmsAgent.currentDeviceStatus] += mmsAgent.elapsedMiliseconds[DEVICE_STATUS_UNCATEGORIZED];
                        mmsAgent.elapsedMiliseconds[DEVICE_STATUS_UNCATEGORIZED] = 0;
                    }

                    // Shift Time Logic
                    long elapsedShiftUnCatMils = mmsAgent.shiftData.getElapsedTimeInMils(DEVICE_STATUS_UNCATEGORIZED);
                    mmsAgent.shiftData.setElapsedTimeInMils(mmsAgent.currentDeviceStatus, elapsedShiftUnCatMils + mmsAgent.elapsedStopTime);
                    elapsedShiftUnCatMils -= mmsAgent.elapsedStopTime;
                    if (elapsedShiftUnCatMils < 0) {
                        elapsedShiftUnCatMils = 0;
                    }
                    mmsAgent.shiftData.setElapsedTimeInMils(DEVICE_STATUS_UNCATEGORIZED, elapsedShiftUnCatMils);

                    // When in Cycle Start Interlock and user selects downtime button, close the downtime button screen
                    if (mmsAgent.statusCsLock == 1) {
                        hideIdleSetupPanel();

                        stopChinChin();
                    }

                    // Reset Stop Time
                    resetStopTime();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private void getJobInfo(String jobID) {
        ProgressForm pForm = new ProgressForm();

        // In real life this task would do something useful and return
        // some meaningful result:
        Task<Void> task = new Task<Void>() {
            @Override
            public Void call() throws InterruptedException {
                updateProgress(20, 100);

                HttpPost post = new HttpPost(Api.SERVE_URL + Api.api_jobInfo);
                // add request parameters or form parameters
                List<NameValuePair> urlParameters = new ArrayList<>();
                urlParameters.add(new BasicNameValuePair("customerId", PreferenceManager.getFactoryID()));
                urlParameters.add(new BasicNameValuePair("jobId", jobID));

                try {
                    post.setEntity(new UrlEncodedFormEntity(urlParameters));
                    HttpClient httpClient = HttpClientBuilder.create().build();
                    HttpResponse result = httpClient.execute(post);
                    String json = EntityUtils.toString(result.getEntity(), "UTF-8");

                    JSONObject res_obj = new JSONObject(json);
                    if (res_obj.optBoolean("status")) {
                        JSONArray dataArray = res_obj.getJSONArray("data");
                        if (dataArray.length() > 0) {
                            JSONObject data_obj = dataArray.getJSONObject(0);
                            String jobID = data_obj.getString("jobID");
                            String jobDetails = data_obj.toString();

                            PreferenceManager.setJobID(jobID);
                            PreferenceManager.setJobDetails(jobDetails);

                            // Parts Per Cycle
                            int partsPerCycle = data_obj.optInt("partsPerCycle");
                            if (partsPerCycle > 0) {
                                PreferenceManager.setPartsPerCycle(partsPerCycle);
                            }

                            // Planned Production Time(targetCycleTime)
                            String targetCycleTimeStr = data_obj.optString("targetCycleTime");
                            if (targetCycleTimeStr.contains(":")) {
                                // Process as time format string
                                String units[] = targetCycleTimeStr.split(":");
                                int h = 0;
                                int m = 0, s = 0;
                                if (units.length == 2) {
                                    // mm:ss
                                    m = Integer.parseInt(units[0]);
                                    s = Integer.parseInt(units[1]);
                                } else if (units.length == 3) {
                                    // hh:mm:ss
                                    h = Integer.parseInt(units[0]);
                                    m = Integer.parseInt(units[1]);
                                    s = Integer.parseInt(units[2]);
                                }

                                int targetCycleTimeInSec = h * 3600 + m * 60 + s;
                                PreferenceManager.setTargetCycleTimeSeconds(targetCycleTimeInSec); // seconds to miliseconds
                            } else {
                                int targetCycleTimeInSec = data_obj.optInt("targetCycleTime");
                                PreferenceManager.setTargetCycleTimeSeconds(targetCycleTimeInSec); // seconds to miliseconds
                            }

                            Platform.runLater(() -> {
                                Toast.message("Success Log In of Job!");
                                showJobInformation();

                                askReworkStatus();
                            });
                        }
                    } else {
                        String errMsg = res_obj.optString("message");
                        if (errMsg == null || errMsg.isEmpty()) {
                            errMsg = "Failed Log In of Job!";
                        }
                        String finalErrMsg = errMsg;
                        Platform.runLater(() -> {
                            Toast.message(finalErrMsg);
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                    Platform.runLater(() -> {
                        Toast.message("Fail to get factory information!");
                    });
                }

                updateProgress(100, 100);
                return null;
            }
        };

        // binds progress of progress bars to progress of task:
        pForm.activateProgressBar(task);

        // in real life this method would get the result of the task
        // and update the UI based on its value:
        task.setOnSucceeded(event -> {
            pForm.getDialogStage().close();
        });

        pForm.getDialogStage().show();

        Thread thread = new Thread(task);
        thread.start();
    }

    private void showJobInformation() {
        String jobID = PreferenceManager.getJobID();
        String jobDetails = PreferenceManager.getJobDetails();

        tvJobId.setText(jobID);
        JobInfo jobInfo = new JobInfo(jobDetails);

        StringBuilder strJobDetailsBuilder = new StringBuilder();
        strJobDetailsBuilder.append("Customer : " + jobInfo.customer + "\n")
                .append("Part Number : " + jobInfo.partNumber + "\n")
                .append("Program Number : " + jobInfo.programNumber + "\n")
                .append("Description : " + jobInfo.description + "\n")
                .append("Parts Per Cycle : " + jobInfo.partsPerCycle + "\n");

        try {
            long targetCycleTimeValue = jobInfo.targetCycleTime;
            strJobDetailsBuilder.append("Target Cycle Time : " + DateTimeUtils.getElapsedTimeMinutesSecondsStringFromSecs(targetCycleTimeValue) + "\n");
        } catch (Exception e) {
            strJobDetailsBuilder.append("Target Cycle Time : " + jobInfo.targetCycleTime + "\n");
            e.printStackTrace();
        }

        strJobDetailsBuilder.append("Qty Required : " + jobInfo.qtyRequired + "\n")
                .append("Qty Good Completed : " + jobInfo.qtyCompleted + "\n")
                .append("Aux1Data : " + jobInfo.aux1data + "\n")
                .append("Aux2Data : " + jobInfo.aux2data + "\n")
                .append("Aux3Data : " + jobInfo.aux3data + "\n");

        tvJobDetails.setText(strJobDetailsBuilder.toString());

        updateJobReworkStatus();
    }

    private void updateJobReworkStatus() {
        if (PreferenceManager.isJobReworkStatus()) {
            labelJobState.setText("(Rework)");
            labelJobState.getStyleClass().removeAll("job-status-setup");
            labelJobState.getStyleClass().add("job-status-rework");
            labelJobState.setVisible(true);
        } else if (PreferenceManager.isJobSetupStatus()) {
            labelJobState.setText("(Setup)");
            labelJobState.getStyleClass().removeAll("job-status-rework");
            labelJobState.getStyleClass().add("job-status-setup");
            labelJobState.setVisible(true);
        } else {
            labelJobState.setText("");
        }
    }

    Runnable progressUpdateRunnable = new Runnable() {
        @Override
        public void run() {

            /*if (progressUpdateHandler != null) {
                reportGanttData();
                progressUpdateHandler.postDelayed(this, 10000);
            }*/

            while (isSignalTimerRunning) {

                CountDownLatch countDownLatch = new CountDownLatch(1);

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {

                        setFanucConnStatus();
                        setServerConnStatus();

                        // Refresh InCycle Time
                        long cycleSeconds = mmsAgent.elapsedMiliseconds[DEVICE_STATUS_INCYCLE];
                        tvSyncTime.setText(DateTimeUtils.getElapsedTimeMinutesSecondsStringFromMilis(cycleSeconds));

                        // Refresh UnCate Time
                        long unCateSeconds = mmsAgent.elapsedMiliseconds[DEVICE_STATUS_UNCATEGORIZED];
                        tvUnCatTime.setText(DateTimeUtils.getElapsedTimeMinutesSecondsStringFromMilis(unCateSeconds));

                        // Calc total Idle time and show it.
                        int idleSeconds = 0;
                        for (int i = 0; i < 8; i++) {
                            idleSeconds += mmsAgent.elapsedMiliseconds[DEVICE_STATUS_IDLE1 + i];
                        }
                        tvIdleTime.setText(DateTimeUtils.getElapsedTimeMinutesSecondsStringFromMilis(idleSeconds));

                        // Down Time Reason Status
                        // Originally only used following for the current downtime reason, but it has occur when the reset, not changed the value
                        if (mmsAgent.currentDeviceStatus >= DEVICE_STATUS_IDLE1) {
                            // Refresh Idle Time(Downtime Reason Time)
                            // Refresh only currently-selected DownTime Reason Status
                            int selectedDowntimeIndex = mmsAgent.currentDeviceStatus - DEVICE_STATUS_IDLE1;
                            if (TextUtils.isEmpty(mmsAgent.titleDownTimeReason[selectedDowntimeIndex])) {
                                // Disable Button
                                if (btnDownTimeStatus[selectedDowntimeIndex].isDisable() == false) {
                                    btnDownTimeStatus[selectedDowntimeIndex].setDisable(true);
                                }
                                mmsAgent.elapsedMiliseconds[mmsAgent.currentDeviceStatus] = 0;

                                String idleStatusText = String.format("Disabled - Reason Not Defined\n%s",
                                        DateTimeUtils.getElapsedTimeMinutesSecondsStringFromMilis(mmsAgent.elapsedMiliseconds[mmsAgent.currentDeviceStatus]));
                                btnDownTimeStatus[selectedDowntimeIndex].setText(idleStatusText);
                            } else {
                                // Enable Button
                                if (btnDownTimeStatus[selectedDowntimeIndex].isDisable()) {
                                    btnDownTimeStatus[selectedDowntimeIndex].setDisable(false);
                                }
                                String idleStatusText = String.format("%s\n%s",
                                        mmsAgent.titleDownTimeReason[selectedDowntimeIndex],
                                        DateTimeUtils.getElapsedTimeMinutesSecondsStringFromMilis(mmsAgent.elapsedMiliseconds[mmsAgent.currentDeviceStatus]));
                                btnDownTimeStatus[selectedDowntimeIndex].setText(idleStatusText);
                            }
                        }

                        if (mmsAgent.currentDeviceStatus != prevDeviceStatus) {
                            // Change current device status value and Switch Animation
                            anminBlinkSync.jumpTo(Duration.ZERO);
                            anminBlinkSync.stop();

                            anminBlinkIdle.jumpTo(Duration.ZERO);
                            anminBlinkIdle.stop();

                            anminBlinkUnCat.jumpTo(Duration.ZERO);
                            anminBlinkUnCat.stop();

                            if (mmsAgent.currentDeviceStatus == DEVICE_STATUS_INCYCLE) {
                                anminBlinkSync.play();

                                tvMachineCateStatus.setText(PreferenceManager.isJobSetupStatus() ? "Setup - In Cycle" : "In Cycle");
                                tvMachineCateStatus.setTextFill(colorGreen);

                            } else if (mmsAgent.currentDeviceStatus == DEVICE_STATUS_UNCATEGORIZED) {
                                anminBlinkUnCat.play();

                                tvMachineCateStatus.setText(PreferenceManager.isJobSetupStatus() ? "Setup - UnCategorized" : "UnCategorized");
                                tvMachineCateStatus.setTextFill(Color.RED);
                            } else {
                                anminBlinkIdle.play();

                                String idleStatus = mmsAgent.titleDownTimeReason[mmsAgent.currentDeviceStatus - DEVICE_STATUS_IDLE1];

                                tvMachineCateStatus.setText(String.format("%sIdle[%s]", PreferenceManager.isJobSetupStatus() ? "Setup - " : "", idleStatus));
                                tvMachineCateStatus.setTextFill(colorOrange);
                            }

                            prevDeviceStatus = mmsAgent.currentDeviceStatus;

                        }

                        tvElapsedIdleTIme.setText("Elapsed Idle Time : " + DateTimeUtils.getElapsedTimeMinutesSecondsStringFromMilis(mmsAgent.elapsedIdleTime));

                        // Show CSLock Status
                        if (mmsAgent.statusCsLock == 1) {
                            if (panelIdleSetUp.isVisible() == false) {
                                showIdleSetupPanel();
                                backCsLockStatus.setVisible(true);
                                tvTitleStatusInterlocked.setVisible(true);

                                playChinChin();
                            }
                        }

                        // Update Regimes
                        if (mmsAgent.timeCurrentMilis - mmsAgent.timeLastRegimeUpdateTime >= INTERVAL_REGIME_UPDATE) {
                            updateRegime();

                            mmsAgent.timeLastRegimeUpdateTime = mmsAgent.timeCurrentMilis;
                        }

                        countDownLatch.countDown();
                    }
                });

                try {
                    Thread.sleep(INTERVAL_SIGNAL_CHECK);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // Lock Processing While the UI thread is finished
                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private void setFanucConnStatus() {
        boolean newFanucConnStatus = PreferenceManager.getFanucConnectionStatus();
        if (newFanucConnStatus == isFanucConnected)
            return;

        if (newFanucConnStatus) {
            txtFanucConnStatus.setText("Connected to Fanuc");
            ivFanucConnStatus.setImage(imageStatusOn);
        } else {
            txtFanucConnStatus.setText("Fanuc Disconnected");
            ivFanucConnStatus.setImage(imageStatusOff);

            // In case of the Fanuc disconnected status, reset to UnCat Status
            onSerialIoError();
        }

        isFanucConnected = newFanucConnStatus;
    }

    private void setServerConnStatus() {
        boolean newServerConnStatus = PreferenceManager.getServerConnectionStatus();
        if (newServerConnStatus == isServerConnected)
            return;

        if (newServerConnStatus) {
            txtServerConnStatus.setText("Connected to Server");
            ivServerConnStatus.setImage(imageStatusOn);
        } else {
            txtServerConnStatus.setText("Server Disconnected");
            ivServerConnStatus.setImage(imageStatusOff);
        }

        isServerConnected = newServerConnStatus;
    }

    private String getStatusTitleFromCode(int code) {
        if (code == DEVICE_STATUS_UNCATEGORIZED) {
            return "UNCAT";
        } else if (code == DEVICE_STATUS_INCYCLE) {
            return "INCYCLE";
        } else {
            return mmsAgent.titleDownTimeReason[code - DEVICE_STATUS_IDLE1];
        }
    }

    private void showSettings() {
        tvMachineName.setText(PreferenceManager.getMachineID());

        // Show user Info
        showUserInfo();

        // Show Job Information
        showJobInformation();

        // Show Downtime Titles and Times
        refreshDowntimeReasons();
    }

    private void assignMachineToUser() {
        // Check Data
        if (TextUtils.isEmpty(PreferenceManager.getFactoryID()) ||
                TextUtils.isEmpty(PreferenceManager.getMachineID()) ||
                TextUtils.isEmpty(PreferenceManager.getUserName())) {
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpPost post = new HttpPost(Api.SERVE_URL + Api.api_assignMachineToUser);
                // add request parameters or form parameters
                List<NameValuePair> urlParameters = new ArrayList<>();
                urlParameters.add(new BasicNameValuePair("userId", PreferenceManager.getUserID()));
                urlParameters.add(new BasicNameValuePair("userName", PreferenceManager.getUserName()));
                urlParameters.add(new BasicNameValuePair("accountID", PreferenceManager.getFactoryID()));
                urlParameters.add(new BasicNameValuePair("machineName", PreferenceManager.getMachineID()));
                urlParameters.add(new BasicNameValuePair("versionInfo", Resource.getVersionInfo()));

                try {
                    post.setEntity(new UrlEncodedFormEntity(urlParameters));
                    HttpClient httpClient = HttpClientBuilder.create().build();
                    HttpResponse result = httpClient.execute(post);
                    String json = EntityUtils.toString(result.getEntity(), "UTF-8");

                    JSONObject response = new JSONObject(json);
                    try {
                        if (response.has("status") && response.getBoolean("status")) {
                        } else {
                            String message = "";
                            if (response.has("message") && !response.isNull("message")) {
                                message = response.getString("message");
                            }
                        }

                        PreferenceManager.setServerConnectionStatus(true);
                    } catch (JSONException e) {
                        e.printStackTrace();

                        PreferenceManager.setServerConnectionStatus(false);
                    }
                } catch (Exception e) {
                    e.printStackTrace();

                    PreferenceManager.setServerConnectionStatus(false);
                }
            }
        }).start();
    }

    private void processNewDeviceStatus(boolean isNewStatusInCycle) {
        if (isNewStatusInCycle) {
            resetStopTime();

            // Hide Stop Time Reason Screen
            hideIdleSetupPanel();
            resetDownTimeReasonStatus();
        }
    }

    public void onSerialIoError() {

        // Reset Downtime reason status
        mmsAgent.opDownTimeStatus = DEVICE_STATUS_UNCATEGORIZED;
        resetDownTimeReasonStatus();
        mmsAgent.currentDeviceStatus = DEVICE_STATUS_UNCATEGORIZED;

        // Changed to Uncategorized time, need to send mail again after stop time
        mmsAgent.sentStopTimeAlert = false;
    }

    public void refreshDowntimeReasons() {
        // Update Downtime reasons
        mmsAgent.loadDowntimeTitles();

        for (int i = 0; i < 8; i++) {
            if (TextUtils.isEmpty(mmsAgent.titleDownTimeReason[i])) {
                btnDownTimeStatus[i].setDisable(true);

                mmsAgent.elapsedMiliseconds[DEVICE_STATUS_IDLE1 + i] = 0;
                String idleStatusText = String.format("Disabled - Reason Not Defined\n%s",
                        DateTimeUtils.getElapsedTimeMinutesSecondsStringFromMilis(mmsAgent.elapsedMiliseconds[DEVICE_STATUS_IDLE1 + i]));
                btnDownTimeStatus[i].setText(idleStatusText);
            } else {
                btnDownTimeStatus[i].setDisable(false);

                String idleStatusText = String.format("%s\n%s",
                        mmsAgent.titleDownTimeReason[i],
                        DateTimeUtils.getElapsedTimeMinutesSecondsStringFromMilis(mmsAgent.elapsedMiliseconds[DEVICE_STATUS_IDLE1 + i]));
                btnDownTimeStatus[i].setText(idleStatusText);
            }
        }
    }

    // Hide Idle Setup Panel
    private void showIdleSetupPanel() {
        panelIdleSetUp.setVisible(true);

        boolean isDowntimeReasonSet = false;
        for (int i = 0; i < 8; i++) {
            if (!TextUtils.isEmpty(mmsAgent.titleDownTimeReason[i])) {
                isDowntimeReasonSet = true;
                break;
            }
        }

        if (isDowntimeReasonSet && mmsAgent.statusCsLock == 1) {
            btnCloseIdleStatus.setVisible(false); // In the original logic
        } else {
            btnCloseIdleStatus.setVisible(true);
        }
    }

    // Show Idel Setup Panel
    private void hideIdleSetupPanel() {
        if (panelIdleSetUp.isVisible()) {
            panelIdleSetUp.setVisible(false);
        }
    }

    private void resetStopTime() {
        //LogManager.getInstance().addNewLog("<= csLock Status");

        mmsAgent.resetStopTime();

        backCsLockStatus.setVisible(false);

        tvTitleStatusInterlocked.setVisible(false);

        btnCloseIdleStatus.setVisible(true);

        // Stop Audio
        stopChinChin();
    }

    private void resetDownTimeReasonStatus() {
        for (int i = 0; i < 8; i++) {
            btnDownTimeStatus[i].getStyleClass().remove("button-downtime-selected");
            //btnDownTimeStatus[i].getStyleClass().add("button-downtime");
        }
    }

    private void updateRegime() {
        // Already do in the Agent
        RegimeUtils.calcRegimes(mmsAgent.regimeData, false);

        // Update Gauges
        gaugeAvailablity.setValue(mmsAgent.regimeData.Availablity);
        gaugePerformance.setValue(mmsAgent.regimeData.performance);
        gaugeQuality.setValue(mmsAgent.regimeData.quality);
        gaugeOEE.setValue(mmsAgent.regimeData.oee);

        // Update PieChart
        long totalTime = 0;
        totalTime += mmsAgent.regimeData.inCycleTime;
        totalTime += mmsAgent.regimeData.unCatTime;
        totalTime += mmsAgent.regimeData.idle1Time;
        totalTime += mmsAgent.regimeData.idle2Time;
        totalTime += mmsAgent.regimeData.idle3Time;
        totalTime += mmsAgent.regimeData.idle4Time;
        totalTime += mmsAgent.regimeData.idle5Time;
        totalTime += mmsAgent.regimeData.idle6Time;
        totalTime += mmsAgent.regimeData.idle7Time;
        totalTime += mmsAgent.regimeData.idle8Time;
        totalTime += mmsAgent.regimeData.offlineT;

        if (totalTime == 0)
            totalTime = 100;

        // Calculate total time
        ArrayList<PieChart.Data> entries = new ArrayList<>();
        entries.add(new PieChart.Data("Cycle", (float) mmsAgent.regimeData.inCycleTime * 100f / totalTime));
        entries.add(new PieChart.Data("Uncategorized", (float) mmsAgent.regimeData.unCatTime * 100f / totalTime));

        if (!TextUtils.isEmpty(PreferenceManager.getDownTimeReason1())) {
            entries.add(new PieChart.Data(PreferenceManager.getDownTimeReason1(), (float) mmsAgent.regimeData.idle1Time * 100f / totalTime));
        }
        if (!TextUtils.isEmpty(PreferenceManager.getDownTimeReason2())) {
            entries.add(new PieChart.Data(PreferenceManager.getDownTimeReason2(), (float) mmsAgent.regimeData.idle2Time * 100f / totalTime));
        }
        if (!TextUtils.isEmpty(PreferenceManager.getDownTimeReason3())) {
            entries.add(new PieChart.Data(PreferenceManager.getDownTimeReason3(), (float) mmsAgent.regimeData.idle3Time * 100f / totalTime));
        }
        if (!TextUtils.isEmpty(PreferenceManager.getDownTimeReason4())) {
            entries.add(new PieChart.Data(PreferenceManager.getDownTimeReason4(), (float) mmsAgent.regimeData.idle4Time * 100f / totalTime));
        }
        if (!TextUtils.isEmpty(PreferenceManager.getDownTimeReason5())) {
            entries.add(new PieChart.Data(PreferenceManager.getDownTimeReason5(), (float) mmsAgent.regimeData.idle5Time * 100f / totalTime));
        }
        if (!TextUtils.isEmpty(PreferenceManager.getDownTimeReason6())) {
            entries.add(new PieChart.Data(PreferenceManager.getDownTimeReason6(), (float) mmsAgent.regimeData.idle6Time * 100f / totalTime));
        }
        if (!TextUtils.isEmpty(PreferenceManager.getDownTimeReason7())) {
            entries.add(new PieChart.Data(PreferenceManager.getDownTimeReason7(), (float) mmsAgent.regimeData.idle7Time * 100f / totalTime));
        }
        if (!TextUtils.isEmpty(PreferenceManager.getDownTimeReason8())) {
            entries.add(new PieChart.Data(PreferenceManager.getDownTimeReason8(), (float) mmsAgent.regimeData.idle8Time * 100f / totalTime));
        }

        entries.add(new PieChart.Data("Offline", (float) mmsAgent.regimeData.offlineT * 100f / totalTime));

        pieChartData.setAll(entries);
    }

    MediaPlayer chinchinPlayer;

    public void stopChinChin() {
        try {
            if (chinchinPlayer != null) {
                chinchinPlayer.stop();
                chinchinPlayer.dispose();
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        chinchinPlayer = null;
    }

    public void playChinChin() {
        // Play Sound
        try {
            stopChinChin();

            String path = getClass().getResource("/resource/chinchin.mp3").toString();

            //Instantiating Media class
            Media media = new Media(path);
            chinchinPlayer = new MediaPlayer(media);

            chinchinPlayer.setVolume(0.3);
            //chinchinPlayer.setAutoPlay(true);
            chinchinPlayer.setOnEndOfMedia(new Runnable() {
                public void run() {
                    chinchinPlayer.seek(Duration.ZERO);
                }
            });
            chinchinPlayer.play();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void logout() {
        // User Information
        PreferenceManager.setUserID("0");
        PreferenceManager.setUserName("Unattended");
        PreferenceManager.setUserAvatar("");
        PreferenceManager.setUserLevel(0);
        PreferenceManager.signOut();

        showUserInfo();
    }

    private void showUserInfo() {

        // Check User Name Changes
        String currUserName = tvUserName.getText().toString().trim();
        if (!currUserName.equals(PreferenceManager.getUserName())) {
            tvUserName.setText(PreferenceManager.getUserName());
        }

        // Update User Avatar
        try {
            String imageLogoUrl = PreferenceManager.getUserAvatar();
            if (TextUtils.isEmpty(imageLogoUrl)) {
                circleAvatar.setFill(new ImagePattern(LogoManager.getInstance().getAvatarLogo()));
            } else {
                imageLogoUrl = Utils.getEscapedUriString(imageLogoUrl);

                //utf8EncodedString = URLEncoder.encode(imageLogoUrl, "UTF-8").replaceAll("\\+", "%20");
                /*URLConnection connection = new URL(utf8EncodedString).openConnection();
                connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
                Image logoImage = new Image(connection.getInputStream());
                ivFactoryLogo.setImage(logoImage);*/

                Image logoImage = new Image(imageLogoUrl, true);
                logoImage.errorProperty().addListener(new ChangeListener<Boolean>() {
                    @Override
                    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                        System.out.println(newValue);  //Location 4
                    }
                });
                logoImage.progressProperty().addListener(new ChangeListener<Number>() {
                    @Override
                    public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                        if (newValue.floatValue() >= 1.0) {
                            circleAvatar.setFill(new ImagePattern(logoImage));
                        }
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (PreferenceManager.isLoginned()) {
            btnLogout.setVisible(true);
            btnLogin.setVisible(false);
        } else {
            btnLogin.setVisible(true);
            btnLogout.setVisible(false);
        }

        assignMachineToUser();
    }

    private void askReworkStatus() {
        PreferenceManager.setJobSetupStatus(false);

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Is this job a rework?", ButtonType.YES, ButtonType.NO);
        alert.initModality(Modality.WINDOW_MODAL);
        alert.setTitle("Rework?");
        Stage dialogStage = (Stage) alert.getDialogPane().getScene().getWindow();
        dialogStage.getIcons().add(LogoManager.getInstance().getLogo());
        //alert.setHeaderText("Please confirm.");
        //alert.setContentText("Is this job a rework?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.YES) {
            PreferenceManager.setJobReworkStatus(true);
            mmsAgent.shiftData.setStatusRework(1);
            DBTableShiftData.updateShiftData(mmsAgent.shiftData);

            updateJobReworkStatus();
        } else if (result.get() == ButtonType.NO) {
            PreferenceManager.setJobReworkStatus(false);
            mmsAgent.shiftData.setStatusRework(0);
            DBTableShiftData.updateShiftData(mmsAgent.shiftData);

            updateJobReworkStatus();
        }
    }
}
