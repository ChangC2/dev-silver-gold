package view;

import Main.Resource;
import Utils.PreferenceManager;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import sun.security.jgss.wrapper.GSSCredElement;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import static javafx.scene.text.TextAlignment.CENTER;

public class SettingsView {

    SplitPane sPane = new SplitPane();
    Stage screen_stage;

    private static SettingsView instance;

    public static SettingsView getInstance() throws IOException {
        if (instance == null) {
            instance = new SettingsView();
        }
        return instance;
    }

    public SettingsView() throws IOException {
        initView();
    }

    public AnchorPane menuPane;
    public AnchorPane settingsPane;

    // MENU OPTIONS
    public HBox hBoxMenuGeneralSettings;
    public HBox hBoxMenuInterfaceSettings;
    public HBox hBoxMenuAutoMonitorSettings;
    public HBox hBoxMenuTeachModeSettings;
    public HBox hBoxMenuAlertReportSettings;
    public HBox hBoxMenuAdaptiveSettings;
    public HBox hBoxMenuChartSettings;
    public HBox hBoxMenuHP2Settings;

    public Button btnEditProfile;
    public Label labelFullName;

    public Circle circlePicture;
    public BufferedImage im;

    double xOffset = 0;
    double yOffset = 0;

    public void initView() throws IOException {
        sPane.getItems().addAll(getMenuPane(), getSettingsPane());

        Scene scene = new Scene(sPane, 800, 800);
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

        screen_stage = new Stage(StageStyle.DECORATED);
        screen_stage.setTitle("Settings");

        screen_stage.setScene(scene);
        screen_stage.centerOnScreen();
        screen_stage.getIcons().add(LogoManager.getInstance().getLogo());
        //screen_stage.setAlwaysOnTop(true);
        screen_stage.setResizable(true);
        screen_stage.setMinWidth(1000);
        screen_stage.setMinHeight(800);
    }

    public AnchorPane getSettingsPane() {
        settingsPane = new AnchorPane();
        settingsPane.getChildren().clear();
        GeneralSettingView gv = new GeneralSettingView();
        settingsPane.getChildren().add(gv.vBoxGeneralSettings);

        return settingsPane;
    }

    public AnchorPane getMenuPane() throws IOException {
        menuPane = new AnchorPane();
        VBox vBoxMenu = new VBox();
        vBoxMenu.setAlignment(Pos.CENTER);
        AnchorPane.setTopAnchor(vBoxMenu, 0.);
        AnchorPane.setRightAnchor(vBoxMenu, 0.);
        AnchorPane.setLeftAnchor(vBoxMenu, 0.);
        AnchorPane.setBottomAnchor(vBoxMenu, 0.);

        Font menuItemTitleFont = Font.font("Tahoma", FontWeight.BOLD, 15);
        Insets menuItemPadding = new Insets(5, 0, 5, 0);

        VBox vBoxMenuTop = new VBox();
        vBoxMenuTop.setAlignment(Pos.CENTER);
        vBoxMenuTop.setPadding(new Insets(40, 0, 0, 0));
        circlePicture = new Circle(63);
        getProfilePicture();
        circlePicture.setOnMouseClicked(clickHandler);
        labelFullName = new Label(PreferenceManager.getName());
        labelFullName.setFont(menuItemTitleFont);
        labelFullName.setTextFill(Color.WHITE);
        btnEditProfile = new Button("Edit Profile");
        btnEditProfile.getStyleClass().add("button-gradient5");
        btnEditProfile.setOnMouseClicked(clickHandler);

        vBoxMenuTop.getChildren().addAll(circlePicture, labelFullName, btnEditProfile);

        VBox vBoxMenuBottom = new VBox(15);
        vBoxMenuBottom.setPadding(new Insets(75, 0, 0, 50));
        vBoxMenuBottom.setAlignment(Pos.CENTER);

        // General Settings.
        hBoxMenuGeneralSettings = new HBox();
        hBoxMenuGeneralSettings.setPadding(menuItemPadding);
        hBoxMenuGeneralSettings.prefHeight(18);
        hBoxMenuGeneralSettings.prefWidth(200);
        hBoxMenuGeneralSettings.getStyleClass().add("selected-border");
        hBoxMenuGeneralSettings.setOnMouseClicked(clickHandler);
        hBoxMenuGeneralSettings.getStyleClass().add("hbox-menu-options");

        ImageView imageMenuGeneralSettings = new ImageView("resource/images/ic_menu_install.png");
        imageMenuGeneralSettings.setFitHeight(Resource.NAV_MENU_ICON_SIZE);
        imageMenuGeneralSettings.setFitWidth(Resource.NAV_MENU_ICON_SIZE);

        Label labelMenuGeneralSettings = new Label("General Settings");
        HBox.setHgrow(labelMenuGeneralSettings, Priority.ALWAYS);
        labelMenuGeneralSettings.setFont(menuItemTitleFont);
        labelMenuGeneralSettings.setTextAlignment(CENTER);
        labelMenuGeneralSettings.setTextFill(Color.WHITE);
        labelMenuGeneralSettings.prefHeight(35);
        labelMenuGeneralSettings.prefWidth(170);
        labelMenuGeneralSettings.setPadding(new Insets(0, 0, 0, 15));
        hBoxMenuGeneralSettings.getChildren().addAll(imageMenuGeneralSettings, labelMenuGeneralSettings);

        // Interface Settings
        hBoxMenuInterfaceSettings = new HBox();
        hBoxMenuInterfaceSettings.setPadding(menuItemPadding);
        hBoxMenuInterfaceSettings.prefHeight(18);
        hBoxMenuInterfaceSettings.prefWidth(200);
        hBoxMenuInterfaceSettings.getStyleClass().add("hbox-menu-options");
        hBoxMenuInterfaceSettings.setOnMouseClicked(clickHandler);

        ImageView imageMenuInterfaceSettings = new ImageView("resource/images/ic_menu_setting.png");
        imageMenuInterfaceSettings.setFitHeight(Resource.NAV_MENU_ICON_SIZE);
        imageMenuInterfaceSettings.setFitWidth(Resource.NAV_MENU_ICON_SIZE);

        Label labelMenuInterfaceSettings = new Label("Interface Settings");
        HBox.setHgrow(labelMenuInterfaceSettings, Priority.ALWAYS);
        labelMenuInterfaceSettings.setFont(menuItemTitleFont);
        labelMenuInterfaceSettings.setTextAlignment(CENTER);
        labelMenuInterfaceSettings.setTextFill(Color.WHITE);
        labelMenuInterfaceSettings.prefHeight(35);
        labelMenuInterfaceSettings.prefWidth(170);
        labelMenuInterfaceSettings.setPadding(new Insets(0, 0, 0, 15));
        hBoxMenuInterfaceSettings.getChildren().addAll(imageMenuInterfaceSettings, labelMenuInterfaceSettings);

        // Auto Monitor Setting

        hBoxMenuAutoMonitorSettings = new HBox();
        hBoxMenuAutoMonitorSettings.setPadding(menuItemPadding);
        hBoxMenuAutoMonitorSettings.prefHeight(18);
        hBoxMenuAutoMonitorSettings.prefWidth(200);
        hBoxMenuAutoMonitorSettings.getStyleClass().add("hbox-menu-options");
        hBoxMenuAutoMonitorSettings.setOnMouseClicked(clickHandler);

        ImageView imageMenuAutomonitorSettings = new ImageView("resource/images/ic_menu_automonitor.png");
        imageMenuAutomonitorSettings.setFitHeight(Resource.NAV_MENU_ICON_SIZE);
        imageMenuAutomonitorSettings.setFitWidth(Resource.NAV_MENU_ICON_SIZE);

        Label labelMenuAutoMonitorSettings = new Label("Auto Monitor Settings");
        HBox.setHgrow(labelMenuAutoMonitorSettings, Priority.ALWAYS);
        labelMenuAutoMonitorSettings.setFont(menuItemTitleFont);
        labelMenuAutoMonitorSettings.setTextAlignment(CENTER);
        labelMenuAutoMonitorSettings.setTextFill(Color.WHITE);
        labelMenuAutoMonitorSettings.prefHeight(35);
        labelMenuAutoMonitorSettings.prefWidth(170);
        labelMenuAutoMonitorSettings.setPadding(new Insets(0, 0, 0, 15));
        hBoxMenuAutoMonitorSettings.getChildren().addAll(imageMenuAutomonitorSettings, labelMenuAutoMonitorSettings);

        // Adaptive Settings
        hBoxMenuAdaptiveSettings = new HBox();
        hBoxMenuAdaptiveSettings.setPadding(menuItemPadding);
        hBoxMenuAdaptiveSettings.prefHeight(18);
        hBoxMenuAdaptiveSettings.prefWidth(200);
        hBoxMenuAdaptiveSettings.getStyleClass().add("hbox-menu-options");
        hBoxMenuAdaptiveSettings.setOnMouseClicked(clickHandler);
        ImageView imageMenuAdaptiveSettings = new ImageView("resource/images/ic_menu_setting.png");
        imageMenuAdaptiveSettings.setFitHeight(Resource.NAV_MENU_ICON_SIZE);
        imageMenuAdaptiveSettings.setFitWidth(Resource.NAV_MENU_ICON_SIZE);

        Label labelMenuAdaptiveSettings = new Label("Adaptive Settings");
        HBox.setHgrow(labelMenuAdaptiveSettings, Priority.ALWAYS);
        labelMenuAdaptiveSettings.setFont(menuItemTitleFont);
        labelMenuAdaptiveSettings.setTextAlignment(CENTER);
        labelMenuAdaptiveSettings.setTextFill(Color.WHITE);
        labelMenuAdaptiveSettings.prefHeight(35);
        labelMenuAdaptiveSettings.prefWidth(170);
        labelMenuAdaptiveSettings.setPadding(new Insets(0, 0, 0, 15));
        hBoxMenuAdaptiveSettings.getChildren().addAll(imageMenuAdaptiveSettings, labelMenuAdaptiveSettings);

        // Teach Mode Settings
        hBoxMenuTeachModeSettings = new HBox();
        hBoxMenuTeachModeSettings.setPadding(menuItemPadding);
        hBoxMenuTeachModeSettings.prefHeight(18);
        hBoxMenuTeachModeSettings.prefWidth(200);
        hBoxMenuTeachModeSettings.getStyleClass().add("hbox-menu-options");
        hBoxMenuTeachModeSettings.setOnMouseClicked(clickHandler);

        ImageView imageMenuTeachModeSettings = new ImageView("resource/images/ic_user_manual.png");
        imageMenuTeachModeSettings.setFitHeight(Resource.NAV_MENU_ICON_SIZE);
        imageMenuTeachModeSettings.setFitWidth(Resource.NAV_MENU_ICON_SIZE);

        Label labelMenuTeachModeSettings = new Label("Teach Mode Settings");
        HBox.setHgrow(labelMenuTeachModeSettings, Priority.ALWAYS);
        labelMenuTeachModeSettings.setFont(menuItemTitleFont);
        labelMenuTeachModeSettings.setTextAlignment(CENTER);
        labelMenuTeachModeSettings.setTextFill(Color.WHITE);
        labelMenuTeachModeSettings.prefHeight(35);
        labelMenuTeachModeSettings.prefWidth(170);
        labelMenuTeachModeSettings.setPadding(new Insets(0, 0, 0, 15));
        hBoxMenuTeachModeSettings.getChildren().addAll(imageMenuTeachModeSettings, labelMenuTeachModeSettings);

        // Alert Report Settings
        hBoxMenuAlertReportSettings = new HBox();
        hBoxMenuAlertReportSettings.setPadding(menuItemPadding);
        hBoxMenuAlertReportSettings.prefHeight(18);
        hBoxMenuAlertReportSettings.prefWidth(200);
        hBoxMenuAlertReportSettings.getStyleClass().add("hbox-menu-options");
        hBoxMenuAlertReportSettings.setOnMouseClicked(clickHandler);
        ImageView imageMenuAlertReportSettings = new ImageView("resource/images/ic_alert.png");
        imageMenuAlertReportSettings.setFitHeight(Resource.NAV_MENU_ICON_SIZE);
        imageMenuAlertReportSettings.setFitWidth(Resource.NAV_MENU_ICON_SIZE);

        Label labelMenuAlertReportSettings = new Label("Alert / Report Settings");
        HBox.setHgrow(labelMenuAlertReportSettings, Priority.ALWAYS);
        labelMenuAlertReportSettings.setFont(menuItemTitleFont);
        labelMenuAlertReportSettings.setTextAlignment(CENTER);
        labelMenuAlertReportSettings.setTextFill(Color.WHITE);
        labelMenuAlertReportSettings.prefHeight(35);
        labelMenuAlertReportSettings.prefWidth(170);
        labelMenuAlertReportSettings.setPadding(new Insets(0, 0, 0, 15));
        hBoxMenuAlertReportSettings.getChildren().addAll(imageMenuAlertReportSettings, labelMenuAlertReportSettings);

        // Chart Settings
        hBoxMenuChartSettings = new HBox();
        hBoxMenuChartSettings.setPadding(menuItemPadding);
        hBoxMenuChartSettings.prefHeight(18);
        hBoxMenuChartSettings.prefWidth(200);
        hBoxMenuChartSettings.setOnMouseClicked(clickHandler);
        hBoxMenuChartSettings.getStyleClass().add("hbox-menu-options");
        ImageView imageMenuChartSettings = new ImageView("resource/images/ic_menu_graph.png");
        imageMenuChartSettings.setFitHeight(Resource.NAV_MENU_ICON_SIZE);
        imageMenuChartSettings.setFitWidth(Resource.NAV_MENU_ICON_SIZE);

        Label labelMenuChartSettings = new Label("Chart Settings");
        HBox.setHgrow(labelMenuChartSettings, Priority.ALWAYS);
        labelMenuChartSettings.setFont(menuItemTitleFont);
        labelMenuChartSettings.setTextAlignment(CENTER);
        labelMenuChartSettings.setTextFill(Color.WHITE);
        labelMenuChartSettings.prefHeight(35);
        labelMenuChartSettings.prefWidth(170);
        labelMenuChartSettings.setPadding(new Insets(0, 0, 0, 15));
        hBoxMenuChartSettings.getChildren().addAll(imageMenuChartSettings, labelMenuChartSettings);

        // HP2 Settings
        hBoxMenuHP2Settings = new HBox();
        hBoxMenuHP2Settings.setPadding(menuItemPadding);
        hBoxMenuHP2Settings.prefHeight(18);
        hBoxMenuHP2Settings.prefWidth(200);
        hBoxMenuHP2Settings.setOnMouseClicked(clickHandler);
        hBoxMenuHP2Settings.getStyleClass().add("hbox-menu-options");
        ImageView imageMenuHp2Settings = new ImageView("resource/images/ic_menu_setting.png");
        imageMenuHp2Settings.setFitHeight(Resource.NAV_MENU_ICON_SIZE);
        imageMenuHp2Settings.setFitWidth(Resource.NAV_MENU_ICON_SIZE);

        Label labelMenuHP2Settings = new Label("HP2 Settings");
        HBox.setHgrow(labelMenuHP2Settings, Priority.ALWAYS);
        labelMenuHP2Settings.setFont(menuItemTitleFont);
        labelMenuHP2Settings.setTextAlignment(CENTER);
        labelMenuHP2Settings.setTextFill(Color.WHITE);
        labelMenuHP2Settings.prefHeight(35);
        labelMenuHP2Settings.prefWidth(170);
        labelMenuHP2Settings.setPadding(new Insets(0, 0, 0, 15));
        hBoxMenuHP2Settings.getChildren().addAll(imageMenuHp2Settings, labelMenuHP2Settings);


        vBoxMenuBottom.getChildren().addAll(hBoxMenuGeneralSettings, hBoxMenuInterfaceSettings, hBoxMenuAutoMonitorSettings, hBoxMenuTeachModeSettings,
                hBoxMenuAlertReportSettings, hBoxMenuAdaptiveSettings, hBoxMenuChartSettings, hBoxMenuHP2Settings);
        vBoxMenu.getChildren().addAll(vBoxMenuTop, vBoxMenuBottom);
        menuPane.getChildren().add(vBoxMenu);

        return menuPane;
    }

    public void getProfilePicture() throws IOException {
        System.out.println(PreferenceManager.getProfilePicture());

        if (PreferenceManager.getProfilePicture() == "") {
            System.out.println("no pic");
        } else {
            try {
                im = ImageIO.read(new File(PreferenceManager.getProfilePicture()));
                Image image = SwingFXUtils.toFXImage(im, null);
                circlePicture.setFill(new ImagePattern(image));
            } catch (IOException e) {
                e.printStackTrace();
            }

            circlePicture.setEffect(new DropShadow(+25d, 0d, +2d, Color.DARKSEAGREEN));
        }
    }

    final EventHandler<MouseEvent> clickHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            if (event.getSource() == circlePicture) {

                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Upload Profile Picture");
                File selectedFile = fileChooser.showOpenDialog(null);
                try {
                    im = ImageIO.read(selectedFile);
                    PreferenceManager.setProfilePicture(String.valueOf(selectedFile));

                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println(selectedFile);
                Image image = SwingFXUtils.toFXImage(im, null);

                circlePicture.setFill(new ImagePattern(image));
                circlePicture.setEffect(new DropShadow(+25d, 0d, +2d, Color.DARKSEAGREEN));
            } else {
                hBoxMenuGeneralSettings.setStyle("");
                hBoxMenuInterfaceSettings.setStyle("");
                hBoxMenuAutoMonitorSettings.setStyle("");
                hBoxMenuTeachModeSettings.setStyle("");
                hBoxMenuAlertReportSettings.setStyle("");
                hBoxMenuAdaptiveSettings.setStyle("");
                hBoxMenuChartSettings.setStyle("");

                if (event.getSource().equals(hBoxMenuGeneralSettings)) {
                    settingsPane.getChildren().clear();
                    GeneralSettingView gv = new GeneralSettingView();
                    settingsPane.getChildren().add(gv.vBoxGeneralSettings);
                    hBoxMenuGeneralSettings.setStyle("-fx-border-color: #fff;\n" +
                            "-fx-border-width: 0 3 0 0;");
                } else if (event.getSource().equals(hBoxMenuInterfaceSettings)) {
                    settingsPane.getChildren().clear();
                    DeviceSettingView dv = new DeviceSettingView();
                    settingsPane.getChildren().add(dv.vBoxDeviceSetting);
                    hBoxMenuInterfaceSettings.setStyle("-fx-border-color: #fff;\n" +
                            "-fx-border-width: 0 3 0 0;");
                } else if (event.getSource().equals(hBoxMenuAutoMonitorSettings)) {
                    settingsPane.getChildren().clear();

                    AutoMonitorSettingView dv = new AutoMonitorSettingView();
                    settingsPane.getChildren().add(dv.vBoxAutoMonitorSettings);
                    hBoxMenuAutoMonitorSettings.setStyle("-fx-border-color: #fff;\n" +
                            "-fx-border-width: 0 3 0 0;");
                } else if (event.getSource().equals(hBoxMenuTeachModeSettings)) {
                    settingsPane.getChildren().clear();
                    TeachModeSettingView tv = new TeachModeSettingView();
                    settingsPane.getChildren().add(tv.vBoxTeachModeSetting);
                    hBoxMenuTeachModeSettings.setStyle("-fx-border-color: #fff;\n" +
                            "-fx-border-width: 0 3 0 0;");
                } else if (event.getSource().equals(hBoxMenuAlertReportSettings)) {
                    settingsPane.getChildren().clear();
                    AlertReportSettingView av = new AlertReportSettingView();
                    settingsPane.getChildren().add(av.vBoxAlertReportSettings);
                    hBoxMenuAlertReportSettings.setStyle("-fx-border-color: #fff;\n" +
                            "-fx-border-width: 0 3 0 0;");

                } else if (event.getSource().equals(hBoxMenuAdaptiveSettings)) {
                    settingsPane.getChildren().clear();
                    AdaptiveSettingView as = new AdaptiveSettingView();
                    settingsPane.getChildren().add(as.vBoxAdaptiveSettings);
                    hBoxMenuAdaptiveSettings.setStyle("-fx-border-color: #fff;\n" +
                            "-fx-border-width: 0 3 0 0;");
                } else if (event.getSource().equals(hBoxMenuChartSettings)) {
                    settingsPane.getChildren().clear();
                    ChartSettingView cv = new ChartSettingView();
                    settingsPane.getChildren().add(cv.vBoxChartSettings);
                    hBoxMenuChartSettings.setStyle("-fx-border-color: #fff;\n" +
                            "-fx-border-width: 0 3 0 0;");
                } else if (event.getSource().equals(btnEditProfile)) {
                    settingsPane.getChildren().clear();
                    EditProfileSettingView ev = new EditProfileSettingView();
                    settingsPane.getChildren().add(ev.vBoxEditProfileSettings);
                } else if (event.getSource().equals(hBoxMenuHP2Settings)) {
                    settingsPane.getChildren().clear();
                    HP2SettingView ev = new HP2SettingView();
                    settingsPane.getChildren().add(ev.vBoxHP2Settings);
                }
            }
        }
    };

    public void start() {
        screen_stage.show();
        screen_stage.requestFocus();
        screen_stage.toFront();
    }
}


