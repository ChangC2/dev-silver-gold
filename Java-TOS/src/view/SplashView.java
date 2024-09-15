package view;

import Utils.Utils;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class SplashView {
    BorderPane splash_pane = new BorderPane();
    Stage splash_stage;

    public ProgressBar status_bar = new ProgressBar();

    private static SplashView instance;

    public static SplashView getInstance(){
        if(instance == null){
            instance = new SplashView();
        }
        return instance;
    }
    public SplashView(){
        initView();
    }

    private void initView(){
        try {
            Utils.disableSslVerification();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ImageView splash_image_view = new ImageView();
        splash_image_view.setImage(LogoManager.getInstance().getSplash());
        splash_image_view.setFitWidth(800);
        splash_image_view.setFitHeight(450);


        status_bar.setPrefWidth(800);
        splash_pane.setCenter(splash_image_view);
        //splash_pane.setBottom(status_bar);

        Scene scene = new Scene(splash_pane, 800, 450);
        scene.getStylesheets().add(getClass().getClassLoader().getResource("resource/style/rootStyles.css").toExternalForm());

        splash_stage = new Stage(StageStyle.TRANSPARENT);
        splash_stage.setScene(scene);
        splash_stage.centerOnScreen();
        splash_stage.getIcons().add(LogoManager.getInstance().getLogo());
        //splash_stage.setAlwaysOnTop(true);
        splash_stage.setResizable(false);
    }

    public void show() {
        splash_stage.show();
    }
    public void close(){
        splash_stage.close();
    }
}
