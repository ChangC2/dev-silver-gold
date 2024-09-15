package view;

import Utils.PreferenceManager;
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

public class EditProfileSettingView {

    Stage screen_stage;

    private static EditProfileSettingView instance;

    public static EditProfileSettingView getInstance() {
        if (instance == null) {
            instance = new EditProfileSettingView();
        }
        return instance;
    }

    //https://www.codeproject.com/Articles/1116455/Simple-Application-For-Creating-Serial-Number-Base
    //https://www.dreamincode.net/forums/topic/176014-serial-generator-and-validator/

    TextField txtFanucIP;
    TextField txtFanucPort;

    TextField txtPLCIP;
    TextField txtPLCPort;

    TextField txtHLPercent;
    TextField txtName;
    TextField txtPhone;
    TextField txtUsername;
    TextField txtCompanyID;
    TextField txtScaleChart;
    TextField txtEmail;



    ComboBox controlComboBox;
    Button btnCancel;
    Button btnSave;


    BorderPane pane;
    VBox vBoxEditProfileSettings;
    
    public EditProfileSettingView() {
        initView();
    }

    private void initView() {
        pane = new BorderPane();
        pane.setPrefSize(503.0, 377);
        pane.setPadding(new Insets(30));

        vBoxEditProfileSettings = new VBox(30);
        vBoxEditProfileSettings.setPadding(new Insets(30.0,50.0,30.0,50.0));

        Label labelEditProfileSettings = new Label("Edit Profile Settings");
        labelEditProfileSettings.setFont(Font.font("Tahoma", FontWeight.BOLD, 26));
        labelEditProfileSettings.setTextFill(Color.WHITE);

        // High Limit Percent
        Label labelProfileDetails = new Label("Profile Details");
        labelProfileDetails.setFont(Font.font("Tahoma", FontWeight.BOLD, 22));
        labelProfileDetails.setTextFill(Color.WHITE);
        //labelSiteCode.getStyleClass().add("label-info-value");
       
        //labelServerIP.setMaxWidth(100);

        // Wear Limit Percent
        HBox hBoxName = new HBox(5);
        hBoxName.setAlignment(Pos.CENTER);
        Label labelName = new Label("Name:");
        //labelSiteKey.getStyleClass().add("label-info-value");
        labelName.setTextFill(Color.WHITE);
        labelName.setPrefWidth(300);
        labelName.setPrefHeight(26);
        labelName.setFont(Font.font(17));


        txtName = new TextField(PreferenceManager.getName());
        HBox.setHgrow(txtName, Priority.ALWAYS);
        txtName.setPrefHeight(30);
        txtName.getStyleClass().add("setting_inputs");
        hBoxName.getChildren().addAll(labelName, txtName);

        // Low Limit Error Percent
        HBox hBoxUsername = new HBox(5);
        hBoxUsername.setAlignment(Pos.CENTER);
        Label labelUsername = new Label("Username:");
        //labelSiteKey.getStyleClass().add("label-info-value");
        labelUsername.setTextFill(Color.WHITE);
        labelUsername.setPrefWidth(300);
        labelUsername.setPrefHeight(26);
        labelUsername.setFont(Font.font(17));

        txtUsername = new TextField(PreferenceManager.getUserName());
        HBox.setHgrow(txtUsername, Priority.ALWAYS);
        txtUsername.setPrefHeight(30);
        txtUsername.getStyleClass().add("setting_inputs");
        hBoxUsername.getChildren().addAll(labelUsername, txtUsername);

        // Target Limit Percent
        HBox hBoxCompanyID = new HBox(5);
        hBoxCompanyID.setAlignment(Pos.CENTER);
        Label labelCompanyID = new Label("Company ID:");
        //labelSiteKey.getStyleClass().add("label-info-value");
        labelCompanyID.setTextFill(Color.WHITE);
        labelCompanyID.setPrefWidth(300);
        labelCompanyID.setPrefHeight(26);
        labelCompanyID.setFont(Font.font(17));


        txtCompanyID = new TextField(PreferenceManager.getCompanyID());
        HBox.setHgrow(txtCompanyID, Priority.ALWAYS);
        txtCompanyID.setPrefHeight(30);
        txtCompanyID.getStyleClass().add("setting_inputs");
        hBoxCompanyID.getChildren().addAll(labelCompanyID, txtCompanyID);


        // High Limit Percent
        Label labelContactInformation = new Label("Contact Information");
        labelContactInformation.setFont(Font.font("Tahoma", FontWeight.BOLD, 22));
        labelContactInformation.setTextFill(Color.WHITE);
        
        // Default High Limit
        HBox hBoxPhone = new HBox(5);
        hBoxPhone.setAlignment(Pos.CENTER);
        Label labelPhone = new Label("Phone:");
        //labelSiteKey.getStyleClass().add("label-info-value");
        labelPhone.setTextFill(Color.WHITE);
        labelPhone.setPrefWidth(300);
        labelPhone.setPrefHeight(26);
        labelPhone.setFont(Font.font(17));

        txtPhone = new TextField(PreferenceManager.getPhone());
        txtPhone.setText(PreferenceManager.getPhone());
        HBox.setHgrow(txtPhone, Priority.ALWAYS);
        txtPhone.setPrefHeight(30);
        txtPhone.getStyleClass().add("setting_inputs");
        hBoxPhone.getChildren().addAll(labelPhone, txtPhone);


        // Default Wear Limit
        HBox hBoxEmail = new HBox(5);
        hBoxEmail.setAlignment(Pos.CENTER);
        Label labelEmail = new Label("Email:");
        //labelSiteKey.getStyleClass().add("label-info-value");
        labelEmail.setTextFill(Color.WHITE);
        labelEmail.setPrefWidth(300);
        labelEmail.setPrefHeight(26);
        labelEmail.setFont(Font.font(17));

        txtEmail = new TextField(PreferenceManager.getEmail());
        txtEmail.setText(PreferenceManager.getEmail());
        HBox.setHgrow(txtEmail, Priority.ALWAYS);
        txtEmail.setPrefHeight(30);
        txtEmail.getStyleClass().add("setting_inputs");
        hBoxEmail.getChildren().addAll(labelEmail, txtEmail);


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

        vBoxEditProfileSettings.getChildren().addAll(labelEditProfileSettings, labelProfileDetails, hBoxName, hBoxUsername, hBoxCompanyID, new Separator(),labelContactInformation, hBoxPhone, hBoxEmail,hbBtn);

        pane.setCenter(vBoxEditProfileSettings);

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
                PreferenceManager.setPhone(txtPhone.getText().trim());
                PreferenceManager.setEmail(txtEmail.getText().trim());
                PreferenceManager.setCompanyID(txtCompanyID.getText().trim());
                PreferenceManager.setUserName(txtUsername.getText().trim());
                PreferenceManager.setName(txtName.getText().trim());

                // Save and Return to the main screen.

                Toast.message("Save Successful!");
                close();
            }
        }
    };
}
