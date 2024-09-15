package view;

import Controller.Api;
import Controller.LoginResultListener;
import Main.Resource;
import Utils.DeviceInfo;
import Utils.PreferenceManager;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.Glow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
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

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class LoginView {
    BorderPane viewPane = new BorderPane();
    Stage screen_stage;

    double xOffset = 0;
    double yOffset = 0;

    private static LoginView instance;

    public static LoginView getInstance() {
        if(instance == null){
            instance = new LoginView();
        }
        return instance;
    }
    public LoginView() {
        initView();
    }

    Button btnClose;
    TextField txtLoginID;
    Button btnLogin;

    LoginResultListener loginListener;

    private void initView() {

        viewPane.setPadding(new Insets(15, 15, 15, 15));
        viewPane.setTop(getTopView());
        viewPane.setCenter(getCenterView());

        //Instantiating the Shadow class
        InnerShadow dropShadow = new InnerShadow ();
        dropShadow.setBlurType(BlurType.GAUSSIAN);
        dropShadow.setColor(Color.LIGHTYELLOW);
        dropShadow.setHeight(5);
        dropShadow.setWidth(5);
        dropShadow.setRadius(5);
        viewPane.setEffect(dropShadow);

        // Init Scene
        Scene scene = new Scene(viewPane, 400, 300);
        scene.getStylesheets().add(getClass().getClassLoader().getResource("resource/style/rootStyles.css").toExternalForm());
        scene.setOnMousePressed(event -> {
            xOffset = screen_stage.getX() - event.getScreenX();
            yOffset = screen_stage.getY() - event.getScreenY();
        });
        //Lambda mouse event handler
        scene.setOnMouseDragged(event -> {
            screen_stage.setX(event.getScreenX() + xOffset);
            screen_stage.setY(event.getScreenY() + yOffset);
        });


        screen_stage = new Stage(StageStyle.UNDECORATED);
        screen_stage.setTitle("SignIn");

        screen_stage.setScene(scene);
        screen_stage.centerOnScreen();
        screen_stage.setAlwaysOnTop(true);
        screen_stage.initModality(Modality.APPLICATION_MODAL);
        screen_stage.getIcons().add(LogoManager.getInstance().getLogo());
        //screen_stage.setAlwaysOnTop(true);
        screen_stage.setResizable(false);
        screen_stage.setMinWidth(400);
        screen_stage.setMinHeight(300);
    }

    public void show(LoginResultListener listener) {
        screen_stage.show();
        enable();
        txtLoginID.setText("");

        loginListener = listener;
    }
    public void close(){
        screen_stage.close();
    }

    private HBox getTopView() {
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_RIGHT);

        btnClose = ViewHelper.makeMenuIconButton("", Resource.MENU_ICON_SIZE, 10, LogoManager.getInstance().getCloseIcon(), "Close");
        hBox.getChildren().add(btnClose);

        btnClose.setOnAction(buttonHandler);

        return hBox;
    }

    private VBox getCenterView() {
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(25);

        ImageView topLogo = new ImageView();
        topLogo.setImage(LogoManager.getInstance().getBannerLogo());
        topLogo.setFitWidth(250);
        topLogo.setPreserveRatio(true);
        topLogo.setSmooth(true);
        topLogo.setCache(true);
        vBox.getChildren().add(topLogo);

        //Instantiating the Glow class
        //setting level of the glow effect
        Glow glow = new Glow();
        glow.setLevel(0.3);
        topLogo.setEffect(glow);

        txtLoginID = new TextField();
        txtLoginID.setPromptText("Login ID Input");
        txtLoginID.setPrefWidth(500);
        txtLoginID.setPrefHeight(40);
        txtLoginID.getStyleClass().add("textfield-round");
        txtLoginID.setAlignment(Pos.CENTER);
        vBox.getChildren().add(txtLoginID);

        btnLogin = new Button("LOGIN");
        btnLogin.setAlignment(Pos.CENTER);
        btnLogin.getStyleClass().add("button-gradient6");
        btnLogin.setPrefWidth(500);
        btnLogin.setPrefHeight(45);
        btnLogin.setMaxWidth(500);
        btnLogin.setMinWidth(300);
        vBox.getChildren().add(btnLogin);
        btnLogin.setOnAction(buttonHandler);

        return vBox;
    }

    final EventHandler<ActionEvent> buttonHandler = new EventHandler<ActionEvent>(){

        @Override
        public void handle(final ActionEvent event) {

            if (event.getSource() == btnLogin) {
                Task<Void> task = new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        try {
                            login();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                };
                task.setOnSucceeded(e -> {
                    enable();
                });
                new Thread(task).start();
                disable();
            } else if (event.getSource() == btnClose) {
                close();

                if (loginListener != null) {
                    loginListener.onLoginCancelled();
                }
            }
        }
    };

    private void disable() {
        btnClose.setDisable(true);
        btnLogin.setDisable(true);
        txtLoginID.setDisable(true);
    }

    private void enable() {
        Platform.runLater(() -> {
            btnClose.setDisable(false);
            btnLogin.setDisable(false);
            txtLoginID.setDisable(false);
        });
    }

    private void login() {
        String strLoginID = txtLoginID.getText().trim();
        if (strLoginID.isEmpty()) {
            return;
        }

        HttpPost post = new HttpPost(Api.SERVE_URL + Api.api_login);
        // add request parameters or form parameters
        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("userId", strLoginID));
        urlParameters.add(new BasicNameValuePair("deviceID", DeviceInfo.getSerialNumber()));
        //JOptionPane.showMessageDialog(null, urlParameters);

        try {
            post.setEntity(new UrlEncodedFormEntity(urlParameters));
            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpResponse result = httpClient.execute(post);
            String json = EntityUtils.toString(result.getEntity(), "UTF-8");

            JSONObject res_obj = new JSONObject(json);
            if (res_obj.optBoolean("status")) {
                JSONObject data_obj = res_obj.getJSONObject("data");
                String name = data_obj.getString("username_full");
                String avatar = data_obj.getString("user_picture");
                int userLevel = data_obj.optInt("security_level");

                PreferenceManager.setUserID(strLoginID);
                PreferenceManager.setUserName(name);
                PreferenceManager.setUserAvatar(avatar);
                PreferenceManager.setUserLevel(userLevel);
                PreferenceManager.setLogin();

                Platform.runLater(() -> {
                    Toast.message("Login Success!");

                    enable();
                    close();

                    if (loginListener != null) {
                        loginListener.onLoginSuccess();
                    }
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
                Toast.message("Fail to sign in !");
                enable();
            });
        }
    }
}
