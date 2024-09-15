package view;

import Utils.PreferenceManager;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static javafx.scene.text.TextAlignment.CENTER;

public class OptimizationResultsView {

SplitPane sPane = new SplitPane();
Stage screen_stage;

    private static OptimizationResultsView instance;

    public static OptimizationResultsView getInstance() throws IOException {
        if (instance == null) {
            instance = new OptimizationResultsView();
        }
        return instance;
    }

    public OptimizationResultsView() throws IOException { initView(); }
public AnchorPane menuPane;
    public AnchorPane settingsPane;

    public HBox menuGeneralSettings;
    public HBox menuAlertReportSettings;
    public HBox menuTeachModeSettings;
    public HBox menuInterfaceSettings;
    public HBox menuChartSettings;
    public HBox menuAdaptiveSettings;

    // MENU OPTIONS
    public HBox hBoxMenuInterfaceSettings;
    public HBox hBoxMenuGeneralSettings;
    public HBox hBoxMenuAdaptiveSettings;
    public HBox hBoxMenuTeachModeSettings;
    public HBox hBoxMenuAlertReportSettings;
    public HBox hBoxMenuChartSettings;


    public VBox vBoxEditProfileSettings;
    public VBox vBoxGeneralSettings;
    public VBox vBoxInterfaceSettings;
    public VBox vBoxTeachModeSettings;
    public VBox vBoxAlertReportSettings;
    public VBox vBoxAdaptiveSettings;
    public VBox vBoxChartSettings;
    public Button menuEditProfile;
    public Button btnEditProfile;
    public Label labelFullName;

    public Circle circlePicture;
    public BufferedImage im;

    double xOffset = 0;
    double yOffset = 0;




    public void initView() throws IOException {
        sPane.getItems().addAll(getMenuPane(),getSettingsPane());

        Scene scene = new Scene(sPane, 800, 600);
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
        screen_stage.setTitle("Optimization Results");

        screen_stage.setScene(scene);
        screen_stage.centerOnScreen();
        screen_stage.getIcons().add(LogoManager.getInstance().getLogo());
        //screen_stage.setAlwaysOnTop(true);
        screen_stage.setResizable(true);
        screen_stage.setMinWidth(1000);
        screen_stage.setMinHeight(700);


    }
    public AnchorPane getSettingsPane(){
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

        VBox vBoxMenuTop = new VBox();
        vBoxMenuTop.setAlignment(Pos.CENTER);
        vBoxMenuTop.setPadding(new Insets(40, 0, 0, 70));
        circlePicture = new Circle(63);
        getProfilePicture();
        circlePicture.setOnMouseClicked(clickHandler);
        labelFullName = new Label(PreferenceManager.getName());
        labelFullName.setFont(Font.font("Tahoma", FontWeight.BOLD, 17));
        labelFullName.setTextFill(Color.WHITE);
        btnEditProfile = new Button("Edit Profile");
        btnEditProfile.setOnMouseClicked(clickHandler);

        vBoxMenuTop.getChildren().addAll(circlePicture, labelFullName, btnEditProfile);


        VBox vBoxMenuBottom = new VBox(15);
        vBoxMenuBottom.setPadding(new Insets(75, 0, 0, 50));
        vBoxMenuBottom.setAlignment(Pos.CENTER);


        hBoxMenuInterfaceSettings = new HBox();
        hBoxMenuInterfaceSettings.prefHeight(18);
        hBoxMenuInterfaceSettings.prefWidth(200);
        hBoxMenuInterfaceSettings.getStyleClass().add("hbox-menu-options");

        hBoxMenuInterfaceSettings.setOnMouseClicked(clickHandler);
        ImageView imageMenuInterfaceSettings = new ImageView("resource/images/ic_menu_setting.png");
        imageMenuInterfaceSettings.setFitHeight(30);
        imageMenuInterfaceSettings.setFitWidth(38);

        Label labelMenuInterfaceSettings = new Label("Interface Settings");
        labelMenuInterfaceSettings.setFont(Font.font("Tahoma", FontWeight.BOLD, 17));
        labelMenuInterfaceSettings.setTextAlignment(CENTER);
        labelMenuInterfaceSettings.setTextFill(Color.WHITE);
        labelMenuInterfaceSettings.prefHeight(35);
        labelMenuInterfaceSettings.prefWidth(162);
        labelMenuInterfaceSettings.setPadding(new Insets(0, 0, 0, 15));
        hBoxMenuInterfaceSettings.getChildren().addAll(imageMenuInterfaceSettings, labelMenuInterfaceSettings);

        hBoxMenuGeneralSettings = new HBox();
        hBoxMenuGeneralSettings.prefHeight(18);
        hBoxMenuGeneralSettings.prefWidth(200);
        hBoxMenuGeneralSettings.getStyleClass().add("selected-border");
        hBoxMenuGeneralSettings.setOnMouseClicked(clickHandler);
        hBoxMenuGeneralSettings.getStyleClass().add("hbox-menu-options");

        ImageView imageMenuGeneralSettings = new ImageView("resource/images/ic_menu_install.png");
        imageMenuGeneralSettings.setFitHeight(30);
        imageMenuGeneralSettings.setFitWidth(38);

        Label labelMenuGeneralSettings = new Label("General Settings");
        labelMenuGeneralSettings.setFont(Font.font("Tahoma", FontWeight.BOLD, 17));
        labelMenuGeneralSettings.setTextAlignment(CENTER);
        labelMenuGeneralSettings.setTextFill(Color.WHITE);
        labelMenuGeneralSettings.prefHeight(35);
        labelMenuGeneralSettings.prefWidth(162);
        labelMenuGeneralSettings.setPadding(new Insets(0, 0, 0, 15));
        hBoxMenuGeneralSettings.getChildren().addAll(imageMenuGeneralSettings, labelMenuGeneralSettings);

        hBoxMenuAdaptiveSettings = new HBox();
        hBoxMenuAdaptiveSettings.prefHeight(18);
        hBoxMenuAdaptiveSettings.prefWidth(200);
        hBoxMenuAdaptiveSettings.getStyleClass().add("hbox-menu-options");
        hBoxMenuAdaptiveSettings.setOnMouseClicked(clickHandler);
        ImageView imageMenuAdaptiveSettings = new ImageView("resource/images/ic_menu_setting.png");
        imageMenuAdaptiveSettings.setFitHeight(30);
        imageMenuAdaptiveSettings.setFitWidth(38);

        Label labelMenuAdaptiveSettings = new Label("Adaptive Settings");
        labelMenuAdaptiveSettings.setFont(Font.font("Tahoma", FontWeight.BOLD, 17));
        labelMenuAdaptiveSettings.setTextAlignment(CENTER);
        labelMenuAdaptiveSettings.setTextFill(Color.WHITE);
        labelMenuAdaptiveSettings.prefHeight(35);
        labelMenuAdaptiveSettings.prefWidth(162);
        labelMenuAdaptiveSettings.setPadding(new Insets(0, 0, 0, 15));
        hBoxMenuAdaptiveSettings.getChildren().addAll(imageMenuAdaptiveSettings, labelMenuAdaptiveSettings);

        hBoxMenuTeachModeSettings = new HBox();
        hBoxMenuTeachModeSettings.prefHeight(18);
        hBoxMenuTeachModeSettings.prefWidth(200);
        hBoxMenuTeachModeSettings.getStyleClass().add("hbox-menu-options");
        hBoxMenuTeachModeSettings.setOnMouseClicked(clickHandler);

        ImageView imageMenuTeachModeSettings = new ImageView("resource/images/ic_user_manual.png");
        imageMenuTeachModeSettings.setFitHeight(30);
        imageMenuTeachModeSettings.setFitWidth(38);

        Label labelMenuTeachModeSettings = new Label("Teach Mode Settings");
        labelMenuTeachModeSettings.setFont(Font.font("Tahoma", FontWeight.BOLD, 17));
        labelMenuTeachModeSettings.setTextAlignment(CENTER);
        labelMenuTeachModeSettings.setTextFill(Color.WHITE);
        labelMenuTeachModeSettings.prefHeight(35);
        labelMenuTeachModeSettings.prefWidth(162);
        labelMenuTeachModeSettings.setPadding(new Insets(0, 0, 0, 15));
        hBoxMenuTeachModeSettings.getChildren().addAll(imageMenuTeachModeSettings, labelMenuTeachModeSettings);

        hBoxMenuAlertReportSettings = new HBox();
        hBoxMenuAlertReportSettings.prefHeight(18);
        hBoxMenuAlertReportSettings.prefWidth(200);
        hBoxMenuAlertReportSettings.getStyleClass().add("hbox-menu-options");
        hBoxMenuAlertReportSettings.setOnMouseClicked(clickHandler);
        ImageView imageMenuAlertReportSettings = new ImageView("resource/images/ic_alert.png");
        imageMenuAlertReportSettings.setFitHeight(30);
        imageMenuAlertReportSettings.setFitWidth(38);

        Label labelMenuAlertReportSettings = new Label("Alert / Report Settings");
        labelMenuAlertReportSettings.setFont(Font.font("Tahoma", FontWeight.BOLD, 17));
        labelMenuAlertReportSettings.setTextAlignment(CENTER);
        labelMenuAlertReportSettings.setTextFill(Color.WHITE);
        labelMenuAlertReportSettings.prefHeight(35);
        labelMenuAlertReportSettings.prefWidth(162);
        labelMenuAlertReportSettings.setPadding(new Insets(0, 0, 0, 15));
        hBoxMenuAlertReportSettings.getChildren().addAll(imageMenuAlertReportSettings, labelMenuAlertReportSettings);

        hBoxMenuChartSettings = new HBox();
        hBoxMenuChartSettings.prefHeight(18);
        hBoxMenuChartSettings.prefWidth(200);
        hBoxMenuChartSettings.setOnMouseClicked(clickHandler);
        hBoxMenuChartSettings.getStyleClass().add("hbox-menu-options");
        ImageView imageMenuChartSettings = new ImageView("resource/images/ic_menu_graph.png");
        imageMenuChartSettings.setFitHeight(30);
        imageMenuChartSettings.setFitWidth(38);

        Label labelMenuChartSettings = new Label("Chart Settings");
        labelMenuChartSettings.setFont(Font.font("Tahoma", FontWeight.BOLD, 17));
        labelMenuChartSettings.setTextAlignment(CENTER);
        labelMenuChartSettings.setTextFill(Color.WHITE);
        labelMenuChartSettings.prefHeight(35);
        labelMenuChartSettings.prefWidth(162);
        labelMenuChartSettings.setPadding(new Insets(0, 0, 0, 15));
        hBoxMenuChartSettings.getChildren().addAll(imageMenuChartSettings, labelMenuChartSettings);


        vBoxMenuBottom.getChildren().addAll(hBoxMenuGeneralSettings, hBoxMenuInterfaceSettings, hBoxMenuTeachModeSettings, hBoxMenuAlertReportSettings, hBoxMenuAdaptiveSettings, hBoxMenuChartSettings);
        vBoxMenu.getChildren().addAll(vBoxMenuTop, vBoxMenuBottom);
        menuPane.getChildren().add(vBoxMenu);

        return menuPane;
    }

    public void getProfilePicture() throws IOException {
        System.out.println(PreferenceManager.getProfilePicture());


        if(PreferenceManager.getProfilePicture()== ""){
            System.out.println("no pic");
        }
        else{
            im = ImageIO.read(new File(PreferenceManager.getProfilePicture()));
            Image image = SwingFXUtils.toFXImage(im, null );
            circlePicture.setFill(new ImagePattern(image));
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

            }
            else{
                hBoxMenuChartSettings.setStyle("");
                hBoxMenuAdaptiveSettings.setStyle("");
                hBoxMenuAlertReportSettings.setStyle("");
                hBoxMenuTeachModeSettings.setStyle("");
                hBoxMenuInterfaceSettings.setStyle("");
                hBoxMenuGeneralSettings.setStyle("");


                if (event.getSource().equals(hBoxMenuGeneralSettings)) {

                    settingsPane.getChildren().clear();
                    GeneralSettingView gv = new GeneralSettingView();
                    settingsPane.getChildren().add(gv.vBoxGeneralSettings);
                    hBoxMenuGeneralSettings.setStyle("-fx-border-color: #fff;\n" +
                            "-fx-border-width: 0 3 0 0;");


                } else if (event.getSource().equals(hBoxMenuTeachModeSettings)) {
                    settingsPane.getChildren().clear();
                    TeachModeSettingView tv = new TeachModeSettingView();
                    settingsPane.getChildren().add(tv.vBoxTeachModeSetting);
                    hBoxMenuTeachModeSettings.setStyle("-fx-border-color: #fff;\n" +
                            "-fx-border-width: 0 3 0 0;");

                } else if (event.getSource().equals(btnEditProfile)) {
                    settingsPane.getChildren().clear();
                    EditProfileSettingView ev = new EditProfileSettingView();
                    settingsPane.getChildren().add(ev.vBoxEditProfileSettings);

                } else if (event.getSource().equals(hBoxMenuInterfaceSettings)) {
                    settingsPane.getChildren().clear();
                    DeviceSettingView dv = new DeviceSettingView();
                    settingsPane.getChildren().add(dv.vBoxDeviceSetting);
                    hBoxMenuInterfaceSettings.setStyle("-fx-border-color: #fff;\n" +
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
                }
            }
        }
    };

    public void start() {
        screen_stage.show();

    }

}


