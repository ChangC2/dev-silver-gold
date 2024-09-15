package view;

import GLG.GlgHSTChartView;
import Model.*;
import Utils.PreferenceManager;
import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javax.swing.*;
import java.io.File;
import java.util.prefs.Preferences;

public class HSTFileView {
    BorderPane viewPane = new BorderPane();
    Stage screen_stage;

    private static HSTFileView instance;

    public static HSTFileView getInstance() {
        if(instance == null){
            instance = new HSTFileView();
        }
        return instance;
    }
    public HSTFileView() {
        initView();
    }

    TextField txtFilePath;
    Button btnLoadData;

    private final GlgHSTChartView applet = new GlgHSTChartView();

    Button btnClose;

    private void initView() {

        viewPane.setTop(getTopView());
        viewPane.setBottom(getBottomView());

        // Init GraphView
        applet.init();
        final SwingNode swingNode = new SwingNode();
        SwingUtilities.invokeLater(() -> {
                    swingNode.setContent(applet);
                    applet.start();
                }
        );
        viewPane.setCenter(swingNode);

        // Init Scene
        Scene scene = new Scene(viewPane, 1200, 800);
        scene.getStylesheets().add(getClass().getClassLoader().getResource("resource/style/rootStyles.css").toExternalForm());

        screen_stage = new Stage(StageStyle.DECORATED);
        screen_stage.setTitle("HST File Viewer");

        screen_stage.setScene(scene);
        screen_stage.centerOnScreen();
        screen_stage.getIcons().add(LogoManager.getInstance().getLogo());
        //screen_stage.setAlwaysOnTop(true);
        screen_stage.setResizable(true);
        screen_stage.setMinWidth(1000);
        screen_stage.setMinHeight(700);
    }

    public void show() {
        screen_stage.show();
    }
    public void close(){
        screen_stage.close();
    }

    private HBox getTopView() {
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(10);
        hBox.setPadding(new Insets(15, 15, 15, 15));

        Label labelJobID = new Label("HST File Path");
        labelJobID.setPadding(new Insets(10, 10, 10, 10));
        labelJobID.setId("label-jobid");
        labelJobID.setTextFill(Color.WHITE);
        hBox.getChildren().add(labelJobID);

        txtFilePath = new TextField();
        txtFilePath.setEditable(false);
        txtFilePath.setPromptText("");
        txtFilePath.setPrefWidth(200);
        txtFilePath.setPrefHeight(30);
        txtFilePath.setAlignment(Pos.CENTER_LEFT);
        txtFilePath.setMaxWidth(Double.POSITIVE_INFINITY);
        HBox.setHgrow(txtFilePath, Priority.ALWAYS);
        hBox.getChildren().add(txtFilePath);

        // Button Load File Data
        btnLoadData = new Button("Open File");
        btnLoadData.setAlignment(Pos.CENTER);
        btnLoadData.getStyleClass().add("button-gradient5");
        btnLoadData.setPrefWidth(120);
        btnLoadData.setPrefHeight(30);
        btnLoadData.setMaxWidth(120);
        btnLoadData.setMinWidth(120);
        hBox.getChildren().add(btnLoadData);
        btnLoadData.setOnAction(buttonHandler);

        return hBox;
    }

    private HBox getBottomView() {
        HBox hButtonBox = new HBox();
        hButtonBox.setAlignment(Pos.CENTER_RIGHT);
        hButtonBox.setSpacing(10);
        hButtonBox.setPadding(new Insets(10, 10, 10, 10));

        // Button Close And Save
        btnClose = new Button("Close");
        btnClose.setAlignment(Pos.CENTER);
        btnClose.getStyleClass().add("button-gradient5");
        btnClose.setPrefWidth(150);
        btnClose.setMaxWidth(200);
        btnClose.setMinWidth(120);
        hButtonBox.getChildren().add(btnClose);
        btnClose.setOnAction(buttonHandler);

        return hButtonBox;
    }

    final EventHandler<ActionEvent> buttonHandler = new EventHandler<ActionEvent>(){

        @Override
        public void handle(final ActionEvent event) {

            if (event.getSource() == btnLoadData) {
                // Load Job Data
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Open HST file");
                fileChooser.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("HST Files", "*.hst")
                );
                String prevHSTPath = PreferenceManager.getStringValue(PreferenceManager.KEY_HST_PATH);
                if (prevHSTPath != null && !prevHSTPath.isEmpty()) {
                    File prevHSTFolder = new File(prevHSTPath);
                    if (!prevHSTFolder.exists()) {
                        prevHSTFolder.mkdirs();
                    }
                    fileChooser.setInitialDirectory(prevHSTFolder);
                }

                File selectedFile = fileChooser.showOpenDialog(viewPane.getScene().getWindow());
                if (selectedFile != null) {
                    String hstFilePath = selectedFile.getAbsolutePath();

                    txtFilePath.setText(hstFilePath);

                    String folderPath = selectedFile.getParent();
                    HSTFileReadManager fileReadManager = new HSTFileReadManager();
                    try {
                        fileReadManager.init(hstFilePath);
                        applet.showHSTData(fileReadManager);

                        // Save HST Path for the next use
                        PreferenceManager.saveValue(PreferenceManager.KEY_HST_PATH, folderPath);
                    } catch (Exception e) {
                        e.printStackTrace();

                        AlertManager.getInstance().showAlert("HST File", e.getMessage());
                    }
                }
            } else if (event.getSource() == btnClose) {
                close();
            }
        }
    };
}
