package view;

import Utils.PreferenceManager;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class JobInfoActionView {

    // Job Actions Listener
    public interface onJobActionListener {
        void onInputJobID();
        void onLogoutOfJob();
        void onUpdateSetUpStatus(boolean setup);
    }

    Stage screen_stage;

    Button btnInputJobID;
    Button btnJobLogout;
    SwitchButton btnSwitch;
    Button btnUpdateSetup;
    Button btnCancel;

    BorderPane pane;
    VBox vBoxDeviceSetting;
    public JobInfoActionView() {
        initView();
    }

    onJobActionListener jobActionListener;

    private void initView() {
        pane = new BorderPane();
        pane.setPrefSize(450, 320);
        pane.setPadding(new Insets(15));

        vBoxDeviceSetting = new VBox(20);
        vBoxDeviceSetting.setFillWidth(true);
        vBoxDeviceSetting.setPadding(new Insets(10.0,10.0,10.0,10.0));

        Label labelTitle = new Label("Please select Job ID input mode.");
        labelTitle.setFont(Font.font("Tahoma", FontWeight.BOLD, 16));
        labelTitle.setAlignment(Pos.CENTER);
        labelTitle.getStyleClass().add("");
        labelTitle.setMaxWidth(Double.POSITIVE_INFINITY);
        labelTitle.setTextFill(Color.WHITE);

        // Buttons Action
        btnInputJobID = new Button("Log In of Job");
        btnInputJobID.getStyleClass().addAll("button-gradient5");
        btnInputJobID.setMaxWidth(Double.POSITIVE_INFINITY);
        btnInputJobID.setMinHeight(45);
        btnInputJobID.setOnAction(buttonHandler);

        btnJobLogout = new Button("Log Out of Job");
        btnJobLogout.getStyleClass().addAll("button-gradient5");
        btnJobLogout.setMaxWidth(Double.POSITIVE_INFINITY);
        btnJobLogout.setMinHeight(45);
        btnJobLogout.setOnAction(buttonHandler);

        // Simulator
        HBox hBoxSetup = new HBox(5);
        hBoxSetup.setAlignment(Pos.CENTER_RIGHT);
        Label labelUseSimulator = new Label("Setup Status");
        //labelSiteCode.getStyleClass().add("label-info-value");
        labelUseSimulator.setTextFill(Color.WHITE);
        labelUseSimulator.setFont(Font.font(14));
        labelUseSimulator.setPrefWidth(173);
        labelUseSimulator.setPrefHeight(26);
        //labelServerIP.setMaxWidth(100);

        btnSwitch = new SwitchButton();
        btnSwitch.setChecked(PreferenceManager.isJobSetupStatus());
        btnSwitch.setSwitchListener(new SwitchButton.SwitchChangeListener() {
            @Override
            public void stateChanged(Boolean state) {

            }
        });
        btnUpdateSetup = new Button(" Update ");
        btnUpdateSetup.getStyleClass().addAll("button-gradient5");
        btnUpdateSetup.setPrefWidth(120);
        btnUpdateSetup.setMinHeight(40);
        btnUpdateSetup.setOnAction(buttonHandler);
        hBoxSetup.getChildren().addAll(labelUseSimulator, btnSwitch, btnUpdateSetup);


        btnCancel = new Button("  Close  ");
        btnCancel.getStyleClass().addAll("button-gradient5");
        btnCancel.setMaxWidth(Double.POSITIVE_INFINITY);
        btnCancel.setMinHeight(45);
        btnCancel.setOnAction(buttonHandler);

        vBoxDeviceSetting.getChildren().addAll(labelTitle, btnInputJobID, btnJobLogout, hBoxSetup, new Separator(), btnCancel);

        pane.setCenter(vBoxDeviceSetting);

        // Init Scene
        Scene scene = new Scene(pane, 450, 320);
        scene.getStylesheets().add(getClass().getClassLoader().getResource("resource/style/rootStyles.css").toExternalForm());

        screen_stage = new Stage(StageStyle.DECORATED);
        screen_stage.setTitle("Job Log In / Out");

        screen_stage.setScene(scene);
        screen_stage.centerOnScreen();
        screen_stage.getIcons().add(LogoManager.getInstance().getLogo());
        screen_stage.setAlwaysOnTop(false);
        screen_stage.setResizable(false);
        screen_stage.initModality(Modality.APPLICATION_MODAL);
        screen_stage.setMinWidth(450);
        screen_stage.setMinHeight(320);
    }

    public void show(onJobActionListener listener) {
        screen_stage.show();
        screen_stage.requestFocus();
        screen_stage.toFront();

        jobActionListener = listener;
    }

    public void close() {
        screen_stage.close();
    }

    EventHandler<ActionEvent> buttonHandler = new EventHandler<ActionEvent>(){

        @Override
        public void handle(final ActionEvent event) {

            if (event.getSource() == btnCancel) {
                close();
            } else if (event.getSource() == btnInputJobID) {
                close();
                if (jobActionListener != null) {
                    jobActionListener.onInputJobID();
                }
            } else if (event.getSource() == btnJobLogout) {
                close();
                if (jobActionListener != null) {
                    jobActionListener.onLogoutOfJob();
                }
            } else if (event.getSource() == btnUpdateSetup) {
                if (jobActionListener != null) {
                    jobActionListener.onUpdateSetUpStatus(btnSwitch.isChecked());
                }
            }
        }
    };
}
