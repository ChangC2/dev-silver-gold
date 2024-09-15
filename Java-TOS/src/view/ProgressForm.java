package view;

import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ProgressForm {
    private final Stage dialogStage;

    private final ProgressIndicator pin = new ProgressIndicator(-1.0f);

    public ProgressForm() {
        dialogStage = new Stage();
        dialogStage.initStyle(StageStyle.TRANSPARENT);
        dialogStage.setResizable(false);
        //dialogStage.initModality(Modality.APPLICATION_MODAL);

        // PROGRESS BAR
        pin.setProgress(-1F);
        pin.setPrefWidth(150);
        pin.setPrefHeight(150);

        StackPane root = new StackPane(pin);
        root.setStyle("-fx-background-radius: 10; -fx-background-color: rgba(0, 0, 0, 0.7); -fx-padding: 20;");
        //root.setOpacity(0);

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        dialogStage.setScene(scene);
    }

    public void activateProgressBar(final Task<?> task)  {
        //pin.progressProperty().bind(task.progressProperty());
        dialogStage.show();
    }

    public Stage getDialogStage() {
        return dialogStage;
    }
}
