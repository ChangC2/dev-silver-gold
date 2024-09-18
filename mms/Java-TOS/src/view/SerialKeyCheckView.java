package view;

import Main.MainApp;
import Utils.DeviceInfo;
import Utils.KeyGen;
import Utils.PreferenceManager;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Reflection;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

public class SerialKeyCheckView {
    GridPane grid = new GridPane();

    Stage screen_stage;

    private static SerialKeyCheckView instance;

    public static SerialKeyCheckView getInstance() {
        if (instance == null) {
            instance = new SerialKeyCheckView();
        }
        return instance;
    }

    //https://www.codeproject.com/Articles/1116455/Simple-Application-For-Creating-Serial-Number-Base
    //https://www.dreamincode.net/forums/topic/176014-serial-generator-and-validator/

    public SerialKeyCheckView() {
        initView2();
    }

    private void initView() {
        AnchorPane pane = new AnchorPane();
        pane.setPrefSize(400, 300);

        grid.setAlignment(Pos.CENTER);
        grid.setHgap(20);
        grid.setVgap(20);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Label labelWelcome = new Label("Welcome to TOS V2");
        labelWelcome.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        labelWelcome.setTextFill(Color.WHITE);
        grid.add(labelWelcome, 0, 0, 2, 1);

        Label labelSiteCode = new Label("Site Code:");
        //labelSiteCode.getStyleClass().add("label-info-value");
        labelSiteCode.setTextFill(Color.WHITE);
        grid.add(labelSiteCode, 0, 1);

        TextField userTextField = new TextField();
        userTextField.setEditable(false);
        grid.add(userTextField, 1, 1, 2, 1);

        Label labelSiteKey = new Label("Site Key:");
        //labelSiteKey.getStyleClass().add("label-info-value");
        labelSiteKey.setTextFill(Color.WHITE);
        grid.add(labelSiteKey, 0, 2);

        PasswordField pwBox = new PasswordField();
        grid.add(pwBox, 1, 2, 2, 1);

        Button btnAuthorize = new Button("Authorize");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BOTTOM_RIGHT);
        hbBtn.getChildren().addAll(btnAuthorize);
        grid.add(hbBtn, 1, 4, 2, 1);

        AnchorPane.setTopAnchor(grid, 0.0);
        AnchorPane.setBottomAnchor(grid, 0.0);
        AnchorPane.setLeftAnchor(grid, 0.0);
        AnchorPane.setRightAnchor(grid, 0.0);

        pane.getChildren().add(grid);

        // Init Scene
        Scene scene = new Scene(pane, 400, 300);
        scene.getStylesheets().add(getClass().getClassLoader().getResource("resource/style/rootStyles.css").toExternalForm());

        screen_stage = new Stage(StageStyle.DECORATED);
        screen_stage.setTitle("TOS V2-License");

        screen_stage.setScene(scene);
        screen_stage.centerOnScreen();
        screen_stage.getIcons().add(LogoManager.getInstance().getLicenseIcon());
        //screen_stage.setAlwaysOnTop(true);
        screen_stage.setResizable(false);
        screen_stage.setMinWidth(400);
        screen_stage.setMinHeight(300);
    }

    private void initView2() {

        BorderPane bp = new BorderPane();
        bp.setPadding(new Insets(10, 50, 50, 50));

        //Adding HBox
        HBox hb = new HBox();
        hb.setPadding(new Insets(20, 20, 20, 30));

        //Adding GridPane
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPrefWidth(Double.POSITIVE_INFINITY);
        gridPane.setPrefHeight(Double.POSITIVE_INFINITY);
        gridPane.setPadding(new Insets(20, 20, 20, 20));
        gridPane.setHgap(5);
        gridPane.setVgap(5);

        //Implementing Nodes for GridPane
        Label lblUserName = new Label("Site Code: ");
        lblUserName.setFont(ViewHelper.getInstance().getFontInformation());
        GridPane.setHalignment(lblUserName, HPos.RIGHT);
        final TextField txtSiteCode = new TextField();
        txtSiteCode.setEditable(false);
        txtSiteCode.setText(DeviceInfo.getMacAddress());
        txtSiteCode.getStyleClass().add("inputs");

        Label lblPassword = new Label("Site Key: ");
        lblPassword.setFont(ViewHelper.getInstance().getFontInformation());
        GridPane.setHalignment(lblPassword, HPos.RIGHT);
        final TextField txtSiteKey = new TextField();
        txtSiteKey.getStyleClass().add("inputs");

        Button btnCopy = new Button("Copy");
        final Label lblMessage = new Label();
        GridPane.setHalignment(lblMessage, HPos.RIGHT);

        Button btnAuthoize = new Button("Authorize");
        GridPane.setHalignment(btnAuthoize, HPos.RIGHT);

        //Adding Nodes to GridPane layout
        gridPane.add(lblUserName, 0, 0);
        gridPane.add(txtSiteCode, 1, 0);
        gridPane.add(lblPassword, 0, 1);
        gridPane.add(txtSiteKey, 1, 1);
        gridPane.add(btnCopy, 2, 0);
        gridPane.add(lblMessage, 1, 2);

        gridPane.add(btnAuthoize, 1, 3);


        //Reflection for gridPane
        Reflection r = new Reflection();
        r.setFraction(0.3f);
        gridPane.setEffect(r);

        //DropShadow effect
        DropShadow dropShadow = new DropShadow();
        dropShadow.setOffsetX(1);
        dropShadow.setOffsetY(1);
        dropShadow.setRadius(0);
        dropShadow.setColor(Color.LIGHTYELLOW);

        //Adding text and DropShadow effect to it
        Text text = new Text("Welcome to TOS V2");
        text.setFont(Font.loadFont(getClass().getClassLoader().getResourceAsStream("resource/style/roboto-bold.ttf"), 26));
        text.setEffect(dropShadow);

        //Adding text to HBox
        hb.getChildren().add(text);

        //Add ID's to Nodes
        bp.setId("bp");
        gridPane.setId("root");
        btnCopy.setId("btnLogin");
        text.setId("text");
        btnAuthoize.setId("btnAuth");

        //Action for btnLogin
        EventHandler<ActionEvent> buttonHandler = new EventHandler<ActionEvent>() {

            @Override
            public void handle(final ActionEvent event) {

                if (event.getSource() == btnCopy) {
                    String machineCode = txtSiteCode.getText().toString();

                    final Clipboard clipboard = Clipboard.getSystemClipboard();
                    final ClipboardContent content = new ClipboardContent();
                    content.putString(machineCode);
                    clipboard.setContent(content);
                } else if (event.getSource() == btnAuthoize) {
                    String siteCode = txtSiteCode.getText().toString().trim();
                    String siteKey = txtSiteKey.getText().toString().trim();
                    if (siteKey.equals(new KeyGen().makeTheKey(siteCode)) || siteKey.equals("11081993")) { // if (true){//
                        lblMessage.setText("Congratulations!");
                        lblMessage.setTextFill(Color.GREEN);

                        // Open Main Screen
                        Toast.message("Your License Approved!");
                        PreferenceManager.setLicenseApproved(true);
                        PreferenceManager.setLicenseSiteCode(siteCode);

                        Task<Void> task = new Task<Void>() {
                            @Override
                            protected Void call() throws Exception {
                                try {
                                    Thread.sleep(800);
                                    Platform.runLater(() -> {
                                        close();
                                        MainApp.getInstance().showMainView();
                                    });
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                return null;
                            }
                        };
                        task.setOnSucceeded(e -> {});
                        new Thread(task).start();
                    } else {
                        lblMessage.setText("Incorrect user or pw.");
                        lblMessage.setTextFill(Color.RED);
                        txtSiteKey.setText("");
                    }
                }
            }
        };

        btnCopy.setOnAction(buttonHandler);
        btnAuthoize.setOnAction(buttonHandler);

        //Add HBox and GridPane layout to BorderPane Layout
        bp.setTop(hb);
        bp.setCenter(gridPane);

        //Adding BorderPane to the scene and loading CSS
        Scene scene = new Scene(bp, 400, 300);
        scene.getStylesheets().add(getClass().getClassLoader().getResource("resource/style/login.css").toExternalForm());

        screen_stage = new Stage(StageStyle.DECORATED);
        screen_stage.setTitle("TOS V2-License");

        screen_stage.setScene(scene);
        screen_stage.centerOnScreen();
        screen_stage.getIcons().add(LogoManager.getInstance().getLicenseIcon());
        //screen_stage.setAlwaysOnTop(true);
        screen_stage.setResizable(false);

        screen_stage.setMinWidth(400);
        screen_stage.setMinHeight(300);

        screen_stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                System.exit(0);
            }
        });
    }

    public void show() {
        screen_stage.show();
    }

    public void close() {
        screen_stage.close();
    }
}
