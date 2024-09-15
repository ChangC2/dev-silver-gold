package view;

import Utils.DateTimeUtils;
import Utils.PreferenceManager;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MMSDownTImeReasonsView {
    AnchorPane viewPane = new AnchorPane();
    Stage screen_stage;

    private static MMSDownTImeReasonsView instance;

    public static MMSDownTImeReasonsView getInstance() {
        if (instance == null) {
            instance = new MMSDownTImeReasonsView();
        }
        return instance;
    }

    public MMSDownTImeReasonsView() {
        initView();
    }

    VBox ivCSLockStatus;

    Label titleCycleStartInterlock;
    Label valueElapsed;
    Button[] btnDownTimeStatus = new Button[8];
    String[] titleDownTimeReason = new String[8];

    Button btnDowntimePanelClose;

    private void initView() {

        ImageView ivBack = new ImageView();
        ivBack.setImage(LogoManager.getInstance().getSplash());
        ivBack.setSmooth(true);
        ivBack.setCache(true);
        ivBack.setPreserveRatio(true);

        AnchorPane.setTopAnchor(ivBack, 0.);
        AnchorPane.setRightAnchor(ivBack, 0.);
        AnchorPane.setBottomAnchor(ivBack, 0.);
        AnchorPane.setLeftAnchor(ivBack, 0.);

        VBox imgBlueBack = new VBox();
        imgBlueBack.setBackground(new Background(new BackgroundFill(Color.web("#1583fe", 0.4), CornerRadii.EMPTY, Insets.EMPTY)));
        AnchorPane.setTopAnchor(imgBlueBack, 0.);
        AnchorPane.setRightAnchor(imgBlueBack, 0.);
        AnchorPane.setBottomAnchor(imgBlueBack, 0.);
        AnchorPane.setLeftAnchor(imgBlueBack, 0.);

        ivCSLockStatus = new VBox();
        ivCSLockStatus.setBackground(new Background(new BackgroundFill(Color.web("#ffa500", 0), CornerRadii.EMPTY, Insets.EMPTY)));
        AnchorPane.setTopAnchor(ivCSLockStatus, 0.);
        AnchorPane.setRightAnchor(ivCSLockStatus, 0.);
        AnchorPane.setBottomAnchor(ivCSLockStatus, 0.);
        AnchorPane.setLeftAnchor(ivCSLockStatus, 0.);

        viewPane.getChildren().addAll(ivBack, imgBlueBack, ivCSLockStatus, getDowntimeButtonPane());

        // Init Scene
        Scene scene = new Scene(viewPane, 1000, 550);
        scene.getStylesheets().add(getClass().getClassLoader().getResource("resource/style/rootStyles.css").toExternalForm());

        screen_stage = new Stage(StageStyle.DECORATED);
        screen_stage.setTitle("DownTime Reasons");

        screen_stage.setScene(scene);
        screen_stage.centerOnScreen();
        screen_stage.getIcons().add(LogoManager.getInstance().getLogo());
        screen_stage.setAlwaysOnTop(true);
        screen_stage.setResizable(false);
        screen_stage.initModality(Modality.APPLICATION_MODAL);
        screen_stage.setMinWidth(1000);
        screen_stage.setMinHeight(550);

        ivBack.fitWidthProperty().bind(screen_stage.widthProperty());
        ivBack.fitHeightProperty().bind(screen_stage.heightProperty());
    }

    private VBox getDowntimeButtonPane() {
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

        titleCycleStartInterlock = new Label("Cycle Start Interlocked");
        titleCycleStartInterlock.setPrefWidth(1000);
        titleCycleStartInterlock.setPadding(new Insets(3, 3, 3, 3));
        titleCycleStartInterlock.getStyleClass().addAll("label-info-value-xlarge", "label-black", "label-center");
        titleCycleStartInterlock.setTextFill(Color.BLACK);

        valueElapsed = new Label("Elapsed Idle Time : 00:00:00");
        valueElapsed.setPrefWidth(1000);
        valueElapsed.setPadding(new Insets(3, 3, 3, 3));
        valueElapsed.getStyleClass().addAll("label-info-value-large", "label-black", "label-center");
        valueElapsed.setTextFill(Color.BLACK);

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

        btnDowntimePanelClose = getDowntimeButton("Close");
        btnDowntimePanelClose.setPrefWidth(1000);

        vDowntimeReasonBox.getChildren().addAll(titleCycleStartInterlock, valueElapsed, hBox1, hBox2, hBox3, hBox4, btnDowntimePanelClose);

        return vDowntimeReasonBox;
    }

    public void show() {
        screen_stage.show();
        screen_stage.requestFocus();
        screen_stage.toFront();

        // Update Downtime reasons
        titleDownTimeReason[0] = PreferenceManager.getDownTimeReason1();
        titleDownTimeReason[1] = PreferenceManager.getDownTimeReason2();
        titleDownTimeReason[2] = PreferenceManager.getDownTimeReason3();
        titleDownTimeReason[3] = PreferenceManager.getDownTimeReason4();
        titleDownTimeReason[4] = PreferenceManager.getDownTimeReason5();
        titleDownTimeReason[5] = PreferenceManager.getDownTimeReason6();
        titleDownTimeReason[6] = PreferenceManager.getDownTimeReason7();
        titleDownTimeReason[7] = PreferenceManager.getDownTimeReason8();

        refreshDowntimeReasons();
    }

    public void close() {
        screen_stage.close();
    }

    private Button getDowntimeButton(String title) {
        Button downTimeButton = new Button(title);
        //downTimeButton.setPrefHeight(45);
        downTimeButton.setPrefWidth(500);
        downTimeButton.getStyleClass().addAll("button-downtime");
        downTimeButton.setTextAlignment(TextAlignment.CENTER);
        HBox.setHgrow(downTimeButton, Priority.ALWAYS);

        return downTimeButton;
    }

    private void refreshDowntimeReasons() {

        /*for (int i = 0; i < 8; i++) {
            //btnDownTimeStatus[i].setText(titleDownTimeReason[i]);

            if (TextUtils.isEmpty(titleDownTimeReason[i])) {
                btnDownTimeStatus[i].setDisable(true);

                elapsedMiliseconds[DEVICE_STATUS_IDLE1 + i] = 0;
                String idleStatusText = String.format("Disabled - Reason Not Defined\n%s",
                        DateTimeUtils.getElapsedTimeMinutesSecondsStringFromMilis(elapsedMiliseconds[DEVICE_STATUS_IDLE1 + i]));
                btnDownTimeStatus[i].setText(idleStatusText);
            } else {
                btnDownTimeStatus[i].setDisable(false);

                String idleStatusText = String.format("%s\n%s",
                        titleDownTimeReason[i],
                        DateTimeUtils.getElapsedTimeMinutesSecondsStringFromMilis(elapsedMiliseconds[DEVICE_STATUS_IDLE1 + i]));
                btnDownTimeStatus[i].setText(idleStatusText);
            }
        }*/
    }

    final EventHandler<ActionEvent> buttonHandler = new EventHandler<ActionEvent>() {

        @Override
        public void handle(final ActionEvent event) {

            if (event.getSource() == btnDowntimePanelClose) {
                close();
            }
        }
    };
}
