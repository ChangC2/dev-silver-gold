package view;

import Controller.Api;
import Main.MainApp;
import Utils.DeviceInfo;
import Utils.PreferenceManager;
import javafx.application.Platform;
import javafx.concurrent.Task;
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
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GeneralSettingView {

    Stage screen_stage;

    private static GeneralSettingView instance;

    public static GeneralSettingView getInstance() {
        if (instance == null) {
            instance = new GeneralSettingView();
        }
        return instance;
    }

    //https://www.codeproject.com/Articles/1116455/Simple-Application-For-Creating-Serial-Number-Base
    //https://www.dreamincode.net/forums/topic/176014-serial-generator-and-validator/
    TextField txtFactoryID;
    Button btnSyncFactoryInfo;
    TextField txtMachineID;
    TextField txtToolDataFilepath;
    Button btnFileFolder;
    TextField txtSiteCode;
    TextField txtAppVersion;
    TextField txtEmail;
    TextField txtSiteKey;

    Button btnCancel;
    Button btnSave;

    BorderPane pane;
    VBox vBoxGeneralSettings;
    public GeneralSettingView() {
        initView();
    }

    private void initView() {
        pane = new BorderPane();
        pane.setPrefSize(503.0, 377);
        pane.setPadding(new Insets(30));

        vBoxGeneralSettings = new VBox(30);
        vBoxGeneralSettings.setPadding(new Insets(30.0,30.0,30.0,50.0));

        Label labelGeneralSettings = new Label("General Settings");
        labelGeneralSettings.setFont(Font.font("Tahoma", FontWeight.BOLD, 20));
        labelGeneralSettings.setTextFill(Color.WHITE);

        Font labelFont = Font.font(14);

        // Factory ID
        HBox hBoxFactoryID = new HBox(5);
        hBoxFactoryID.setAlignment(Pos.CENTER);
        Label labelFactoryID = new Label("Factory ID:");
        //labelSiteKey.getStyleClass().add("label-info-value");
        labelFactoryID.setTextFill(Color.WHITE);
        labelFactoryID.setPrefWidth(200);
        labelFactoryID.setPrefHeight(26);
        labelFactoryID.setFont(labelFont);

        txtFactoryID = new TextField();
        txtFactoryID.setText(PreferenceManager.getFactoryID());
        HBox.setHgrow(txtFactoryID, Priority.ALWAYS);
        txtFactoryID.setPrefHeight(30);
        txtFactoryID.setEditable(false);
        txtFactoryID.getStyleClass().add("setting_inputs");

        btnSyncFactoryInfo = ViewHelper.makeMenuIconButton("", 20, 3, LogoManager.getInstance().getMenuCheckUpdateImage(), "Choose Folder");
        btnSyncFactoryInfo.setOnAction(buttonHandler);
        hBoxFactoryID.getChildren().addAll(labelFactoryID, txtFactoryID, btnSyncFactoryInfo);

        // Machine ID
        HBox hBoxMachineID = new HBox(5);
        hBoxMachineID.setAlignment(Pos.CENTER);
        Label labelMachineID = new Label("Machine ID:");
        //labelSiteCode.getStyleClass().add("label-info-value");
        labelMachineID.setTextFill(Color.WHITE);
        labelMachineID.setFont(labelFont);
        labelMachineID.setPrefWidth(200);
        labelMachineID.setPrefHeight(26);
        //labelServerIP.setMaxWidth(100);

        txtMachineID = new TextField();
        txtMachineID.setText(PreferenceManager.getMachineID());
        HBox.setHgrow(txtMachineID, Priority.ALWAYS);
        txtMachineID.setPrefHeight(30);
        txtMachineID.getStyleClass().add("setting_inputs");
        hBoxMachineID.getChildren().addAll(labelMachineID, txtMachineID);

        // Tool Data Path
        HBox hBoxToolDataFilepath = new HBox(5);
        hBoxToolDataFilepath.setAlignment(Pos.CENTER);
        Label labelToolDataFilepath = new Label("Tool Date Filepath:");
        //labelSiteKey.getStyleClass().add("label-info-value");
        labelToolDataFilepath.setTextFill(Color.WHITE);
        labelToolDataFilepath.setPrefWidth(200);
        labelToolDataFilepath.setPrefHeight(26);
        labelToolDataFilepath.setFont(labelFont);

        txtToolDataFilepath = new TextField();
        txtToolDataFilepath.setText(PreferenceManager.getToolDataFilepath());
        HBox.setHgrow(txtToolDataFilepath, Priority.ALWAYS);
        txtToolDataFilepath.setPrefHeight(30);
        txtToolDataFilepath.setEditable(false);
        txtToolDataFilepath.getStyleClass().add("setting_inputs");

        btnFileFolder = ViewHelper.makeMenuIconButton("", 20, 3, LogoManager.getInstance().getFolderIcon(), "Choose Folder");
        btnFileFolder.setOnAction(buttonHandler);
        hBoxToolDataFilepath.getChildren().addAll(labelToolDataFilepath, txtToolDataFilepath, btnFileFolder);

        // System Info Title
        Label labelSystemInfo = new Label("System Info");
        labelSystemInfo.setFont(Font.font("Tahoma", FontWeight.BOLD, 18));
        labelSystemInfo.setTextFill(Color.WHITE);

        // Low Limit Error Percent
        HBox hBoxAppVersion = new HBox(5);
        hBoxAppVersion.setAlignment(Pos.CENTER);
        Label labelAppVersion = new Label("App Version:");
        //labelSiteKey.getStyleClass().add("label-info-value");
        labelAppVersion.setTextFill(Color.WHITE);
        labelAppVersion.setPrefWidth(200);
        labelAppVersion.setPrefHeight(26);
        labelAppVersion.setFont(labelFont);


        txtAppVersion = new TextField(PreferenceManager.getAppVersion());
        txtAppVersion.setText(PreferenceManager.getAppVersion());
        HBox.setHgrow(txtAppVersion, Priority.ALWAYS);
        txtAppVersion.setPrefHeight(30);
        txtAppVersion.getStyleClass().add("setting_inputs");
        hBoxAppVersion.getChildren().addAll(labelAppVersion, txtAppVersion);


        // Email
        HBox hBoxEmail = new HBox(5);
        hBoxEmail.setAlignment(Pos.CENTER);
        Label labelEmail = new Label("Email:");
        //labelSiteKey.getStyleClass().add("label-info-value");
        labelEmail.setTextFill(Color.WHITE);
        labelEmail.setPrefWidth(200);
        labelEmail.setPrefHeight(26);
        labelEmail.setFont(labelFont);


        txtEmail = new TextField(PreferenceManager.getEmail());
        txtEmail.setText(PreferenceManager.getEmail());
        HBox.setHgrow(txtEmail, Priority.ALWAYS);
        txtEmail.setPrefHeight(30);
        txtEmail.getStyleClass().add("setting_inputs");
        hBoxEmail.getChildren().addAll(labelEmail, txtEmail);


        // Site Code
        HBox hBoxSiteCode = new HBox(5);
        hBoxSiteCode.setAlignment(Pos.CENTER);
        Label labelSiteCode = new Label("Site Code:");
        //labelSiteKey.getStyleClass().add("label-info-value");
        labelSiteCode.setTextFill(Color.WHITE);
        labelSiteCode.setPrefWidth(200);
        labelSiteCode.setPrefHeight(26);
        labelSiteCode.setFont(labelFont);


        txtSiteCode = new TextField(PreferenceManager.getLicenseSiteCode());
        txtSiteCode.setText(PreferenceManager.getLicenseSiteCode());
        HBox.setHgrow(txtSiteCode, Priority.ALWAYS);
        txtSiteCode.setPrefHeight(30);
        txtSiteCode.getStyleClass().add("setting_inputs");
        hBoxSiteCode.getChildren().addAll(labelSiteCode, txtSiteCode);


        // Site Key
        HBox hBoxSiteKey = new HBox(5);
        hBoxSiteKey.setAlignment(Pos.CENTER);
        Label labelSiteKey = new Label("Site Key:");
        //labelSiteKey.getStyleClass().add("label-info-value");
        labelSiteKey.setTextFill(Color.WHITE);
        labelSiteKey.setPrefWidth(200);
        labelSiteKey.setPrefHeight(26);
        labelSiteKey.setFont(labelFont);


        txtSiteKey = new TextField(PreferenceManager.getSiteKey());
        txtSiteKey.setText(PreferenceManager.getSiteKey());
        HBox.setHgrow(txtSiteKey, Priority.ALWAYS);
        txtSiteKey.setPrefHeight(30);
        txtSiteKey.getStyleClass().add("setting_inputs");
        hBoxSiteKey.getChildren().addAll(labelSiteKey, txtSiteKey);


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

        vBoxGeneralSettings.getChildren().addAll(labelGeneralSettings, hBoxFactoryID, hBoxMachineID, hBoxToolDataFilepath,
                new Separator(),
                labelSystemInfo, hBoxAppVersion, hBoxEmail, hBoxSiteCode, hBoxSiteKey, hbBtn);

        pane.setCenter(vBoxGeneralSettings);

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
                PreferenceManager.setMachineID(txtMachineID.getText().trim());
                MainView.getInstance().updateMachineInfo();
                MainApp.getInstance().loadMachineSettings();

                PreferenceManager.setToolDataFilepath(txtToolDataFilepath.getText().trim());
                PreferenceManager.setAppVersion(txtAppVersion.getText().trim());
                PreferenceManager.setEmail(txtEmail.getText().trim());
                PreferenceManager.setLicenseSiteCode(txtSiteCode.getText().trim());
                PreferenceManager.setSiteKey(txtSiteKey.getText().trim());

                // Save and Return to the main screen.
                Toast.message("Save Successful!");
                close();
            } else if (event.getSource() == btnFileFolder) {
                DirectoryChooser directoryChooser = new DirectoryChooser();
                String toolDataPath = PreferenceManager.getToolDataFilepath();
                File toolDataFolder = new File(toolDataPath);
                if (toolDataFolder.exists()) {
                    directoryChooser.setInitialDirectory(toolDataFolder);
                }

                File selectedDirectory = directoryChooser.showDialog(screen_stage);
                if (selectedDirectory != null) {

                    // Check Directory is available or not
                    if (selectedDirectory.canRead() && selectedDirectory.canWrite()/*new File(selectedDirectory, "tooldatafiles").mkdirs()*/) {
                        txtToolDataFilepath.setText(selectedDirectory.getAbsolutePath());
                    } else {
                        Toast.message("User permission error, please choose another folder!");
                    }
                }
            } else if (event.getSource() == btnSyncFactoryInfo) {
                // Input Factory ID and get info
                TextInputDialog dialog = new TextInputDialog(PreferenceManager.getFactoryID());

                dialog.setTitle("Factory ID Input");
                dialog.setHeaderText(null);
                dialog.setContentText("Factory ID:");

                Optional<String> result = dialog.showAndWait();
                result.ifPresent(name -> {
                    getFactoryInfo(name);
                });
            }
        }
    };

    private void getFactoryInfo(String factoryID) {
        ProgressForm pForm = new ProgressForm();

        // In real life this task would do something useful and return
        // some meaningful result:
        Task<Void> task = new Task<Void>() {
            @Override
            public Void call() throws InterruptedException {
                updateProgress(20, 100);

                HttpPost post = new HttpPost(Api.SERVE_URL + Api.api_customerInfo);
                // add request parameters or form parameters
                List<NameValuePair> urlParameters = new ArrayList<>();
                urlParameters.add(new BasicNameValuePair("customerId", factoryID));
                urlParameters.add(new BasicNameValuePair("deviceId", DeviceInfo.getSerialNumber()));

                try {
                    post.setEntity(new UrlEncodedFormEntity(urlParameters));
                    HttpClient httpClient = HttpClientBuilder.create().build();
                    HttpResponse result = httpClient.execute(post);
                    String json = EntityUtils.toString(result.getEntity(), "UTF-8");

                    JSONObject res_obj = new JSONObject(json);
                    if (res_obj.optBoolean("status")) {
                        JSONObject data_obj = res_obj.getJSONObject("data");
                        String name = data_obj.getString("name");
                        String avatar = data_obj.getString("logo");

                        PreferenceManager.setFactoryID(factoryID);
                        MainApp.getInstance().loadShiftSettings();    // Load Shift Settings

                        PreferenceManager.setFactoryName(name);
                        PreferenceManager.setFactoryLogo(avatar);

                        Platform.runLater(() -> {
                            Toast.message("Login Success!");
                            txtFactoryID.setText(factoryID);
                        });
                    } else {
                        String errMsg = res_obj.optString("message");
                        if (errMsg == null || errMsg.isEmpty()) {
                            errMsg = "Fail to sign in !";
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
                return null ;
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
}
