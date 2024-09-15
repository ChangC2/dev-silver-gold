package view;

import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class AlertManager {

    public static AlertManager instance;
    public static AlertManager getInstance() {
        if (instance == null) {
            instance = new AlertManager();
        }
        return instance;
    }

    public void showAlertWithHeaderText(String title, String headerText, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(content);

        Stage dialogStage = (Stage) alert.getDialogPane().getScene().getWindow();
        dialogStage.getIcons().add(LogoManager.getInstance().getLogo());

        alert.showAndWait();
    }

    public void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);

        Stage dialogStage = (Stage) alert.getDialogPane().getScene().getWindow();
        dialogStage.getIcons().add(LogoManager.getInstance().getLogo());

        alert.showAndWait();
    }
}
