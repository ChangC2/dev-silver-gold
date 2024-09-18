package view;

import Controller.Api;
import Main.MainApp;
import Utils.PreferenceManager;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.Glow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.http.util.TextUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

public class UpdateAppView {
    BorderPane viewPane = new BorderPane();
    Stage screen_stage;

    double xOffset = 0;
    double yOffset = 0;

    private static UpdateAppView instance;
    private boolean bRunning = false;

    public static UpdateAppView getInstance() {
        if(instance == null){
            instance = new UpdateAppView();
        }
        return instance;
    }
    public UpdateAppView() {
        initView();
    }

    Label labelUpdateInfo;
    ProgressIndicator progressIndicator;
    Button btnCancel;

    private void initView() {

        viewPane.setPadding(new Insets(15, 15, 15, 15));
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
        Scene scene = new Scene(viewPane, 300, 200);
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
        screen_stage.setTitle("Downloading Updates...");

        screen_stage.setScene(scene);
        screen_stage.centerOnScreen();
        screen_stage.setAlwaysOnTop(true);
        screen_stage.initModality(Modality.WINDOW_MODAL);
        screen_stage.getIcons().add(LogoManager.getInstance().getLogo());
        //screen_stage.setAlwaysOnTop(true);
        screen_stage.setResizable(false);
        screen_stage.setMinWidth(300);
        screen_stage.setMinHeight(200);
    }

    public void updateApp(boolean bUpdateTOS, String versionName, boolean bUpdateFanuc, int fanucVersionCode) {
        screen_stage.show();
        screen_stage.requestFocus();
        screen_stage.toFront();

        if (bRunning)
            return;

        bRunning = true;
        update(bUpdateTOS, versionName, bUpdateFanuc, fanucVersionCode);
    }
    public void close(){
        screen_stage.close();
    }

    private VBox getCenterView() {
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(20);

        Font labelFont = Font.font(16);
        labelUpdateInfo = new Label("Downloading Updates...\n(1/2)");
        labelUpdateInfo.setTextFill(Color.WHITE);
        labelUpdateInfo.setTextAlignment(TextAlignment.CENTER);
        HBox.setHgrow(labelUpdateInfo, Priority.ALWAYS);
        labelUpdateInfo.setFont(labelFont);
        Glow glow = new Glow();
        glow.setLevel(0.3);
        labelUpdateInfo.setEffect(glow);

        progressIndicator = new ProgressIndicator();

        btnCancel = new Button("Cancel");
        btnCancel.setTextAlignment(TextAlignment.CENTER);
        btnCancel.getStyleClass().add("button-gradient6");
        btnCancel.setPrefWidth(150);
        btnCancel.setOnAction(buttonHandler);
        btnCancel.setVisible(false);

        vBox.getChildren().addAll(labelUpdateInfo, progressIndicator/*, btnCancel*/);

        return vBox;
    }

    final EventHandler<ActionEvent> buttonHandler = new EventHandler<ActionEvent>(){

        @Override
        public void handle(final ActionEvent event) {

            if (event.getSource() == btnCancel) {
            }
        }
    };

    private void update(boolean bUpdateTOS, String tosVersionName, boolean bUpdateFanuc, int fanucVersionCode) {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                if (bUpdateFanuc) {
                    try {
                        InputStream in = new URL(Api.SERVE_URL + Api.api_build_fanuc).openStream();
                        Path newUpdatesPath = Paths.get(System.getProperty("user.home"), "Downloads", String.format("FanucServiceSetup(V%d).exe", fanucVersionCode));
                        Files.copy(in, newUpdatesPath, StandardCopyOption.REPLACE_EXISTING);

                        Platform.runLater(() -> {
                            screen_stage.hide();
                        });

                        ProcessBuilder builder = new ProcessBuilder(newUpdatesPath.toString());
                        Process process = builder.start();
                        int exitValue = process.waitFor();
                        if (exitValue == 0) {
                            PreferenceManager.setFanucVersionCode(fanucVersionCode);
                        }
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();

                        Platform.runLater(() -> {
                            bRunning = false;
                            close();

                            String error = "Fanuc update failed!";
                            if (e != null && !TextUtils.isEmpty(e.getMessage())) {
                                error = error + " " + e.getMessage();
                            }
                            Toast.message(error);
                        });

                        return null;
                    }
                }

                if (bUpdateTOS) {
                    Platform.runLater(() -> {
                        labelUpdateInfo.setText("Downloading Updates...\n(2/2)");
                        screen_stage.show();
                    });

                    // Continue to update TOS
                    try {
                        InputStream in = new URL(Api.SERVE_URL + Api.api_build_tos).openStream();
                        Path newUpdatesPath = Paths.get(System.getProperty("user.home"), "Downloads", String.format("TOSV2SetupV%s.exe", tosVersionName));
                        Files.copy(in, newUpdatesPath, StandardCopyOption.REPLACE_EXISTING);

                        Platform.runLater(() -> {
                            bRunning = false;
                            close();
                            Toast.message("Downloaded new version!");

                            // Run new version setup
                            try {
                                Process process = new ProcessBuilder(newUpdatesPath.toString()).start();
                                MainApp.getInstance().closeMainView();
                            } catch (IOException e) {
                                e.printStackTrace();
                                Toast.message(e.getMessage());
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();

                        Platform.runLater(() -> {
                            bRunning = false;
                            close();
                            Toast.message(e.getMessage());
                        });
                    }
                } else {
                    Platform.runLater(() -> {
                        bRunning = false;
                        close();
                    });
                }

                return null;
            }
        };
        new Thread(task).start();
    }

    private void updateWithBatchFile(boolean bUpdateTOS, String tosVersionName, boolean bUpdateFanuc, int fanucVersionCode) {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                if (bUpdateFanuc) {
                    try {
                        Path folderPath = Paths.get(System.getProperty("user.home"), "Downloads", "FanucBridgeUpdates");
                        File fanucUpdateFolder = folderPath.toFile();
                        //Creating the directory
                        boolean bCreateFolder = true;
                        if (fanucUpdateFolder.exists()) {
                            for (File file: Objects.requireNonNull(fanucUpdateFolder.listFiles())) {
                                if (!file.isDirectory()) {
                                    file.delete();
                                }
                            }
                        } else {
                            bCreateFolder = fanucUpdateFolder.mkdir();
                        }

                        if (bCreateFolder) {
                            InputStream in1 = new URL(Api.SERVE_URL + Api.api_build_fanuc1).openStream();
                            Path newUpdatesPath1 = Paths.get(System.getProperty("user.home"), "Downloads", "FanucBridgeUpdates", "FanucServiceSetup.msi");
                            Files.copy(in1, newUpdatesPath1, StandardCopyOption.REPLACE_EXISTING);

                            InputStream in2 = new URL(Api.SERVE_URL + Api.api_build_fanuc2).openStream();
                            Path newUpdatesPath2 = Paths.get(System.getProperty("user.home"), "Downloads", "FanucBridgeUpdates", "setup.exe");
                            Files.copy(in2, newUpdatesPath2, StandardCopyOption.REPLACE_EXISTING);

                            InputStream in3 = new URL(Api.SERVE_URL + Api.api_build_fanuc3).openStream();
                            Path newUpdatesPath3 = Paths.get(System.getProperty("user.home"), "Downloads", "FanucBridgeUpdates", "setup.bat");
                            Files.copy(in3, newUpdatesPath3, StandardCopyOption.REPLACE_EXISTING);

                            String setupFilePath = newUpdatesPath3.toString().replace("\\", "\\\\");

                            //String command = String.format("cmd.exe /c start \"\" /runas %s", setupFilePath);

                            Platform.runLater(() -> {
                                screen_stage.hide();
                            });

                            // Run new version setup
                            //Process process = new ProcessBuilder(command).start();
                            String command = "cmd.exe";
                            String arg1 = "/c";
                            String arg2 = "start";
                            String arg3 = "";
                            //String arg4 = "/runas";
                            String arg5 = "cmd.exe";
                            String arg6 = "/c";
                            String arg7 = String.format("\"%s\"", newUpdatesPath3.toString());

                            ProcessBuilder builder = new ProcessBuilder(command, arg1, arg2, arg3, /*arg4, */arg5, arg6, arg7);
                            Process process = builder.start();
                            int exitValue = process.waitFor();
                            if (exitValue == 0) {
                                PreferenceManager.setFanucVersionCode(fanucVersionCode);
                            }
                        } else {
                            throw new IOException("Couldn't create downlonad folder.");
                        }
                    } catch (IOException | InterruptedException e) {
                        e.printStackTrace();

                        Platform.runLater(() -> {
                            bRunning = false;
                            close();

                            String error = "Fanuc update failed!";
                            if (e != null && !TextUtils.isEmpty(e.getMessage())) {
                                error = error + " " + e.getMessage();
                            }
                            Toast.message(error);
                        });

                        return null;
                    }
                }

                if (bUpdateTOS) {
                    Platform.runLater(() -> {
                        labelUpdateInfo.setText("Downloading Updates...\n(2/2)");
                        screen_stage.show();
                    });

                    // Continue to update TOS
                    try {
                        InputStream in = new URL(Api.SERVE_URL + Api.api_build_tos).openStream();
                        Path newUpdatesPath = Paths.get(System.getProperty("user.home"), "Downloads", String.format("TOSV2SetupV%s.exe", tosVersionName));
                        Files.copy(in, newUpdatesPath, StandardCopyOption.REPLACE_EXISTING);

                        Platform.runLater(() -> {
                            bRunning = false;
                            close();
                            Toast.message("Downloaded new version!");

                            // Run new version setup
                            try {
                                Process process = new ProcessBuilder(newUpdatesPath.toString()).start();
                                MainApp.getInstance().closeMainView();
                            } catch (IOException e) {
                                e.printStackTrace();
                                Toast.message(e.getMessage());
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();

                        Platform.runLater(() -> {
                            bRunning = false;
                            close();
                            Toast.message(e.getMessage());
                        });
                    }
                } else {
                    Platform.runLater(() -> {
                        bRunning = false;
                        close();
                    });
                }

                return null;
            }
        };
        new Thread(task).start();
    }
}
