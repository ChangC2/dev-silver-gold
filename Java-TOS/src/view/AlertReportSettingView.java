package view;

import Utils.PreferenceManager;
import Utils.Utils;
import com.google.gson.Gson;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class AlertReportSettingView {

    Stage screen_stage;

    private static AlertReportSettingView instance;

    public static AlertReportSettingView getInstance() {
        if (instance == null) {
            instance = new AlertReportSettingView();
        }
        return instance;
    }

    //https://www.codeproject.com/Articles/1116455/Simple-Application-For-Creating-Serial-Number-Base
    //https://www.dreamincode.net/forums/topic/176014-serial-generator-and-validator/

    TextField txtEmail1;
    TextField txtEmail2;
    TextField txtEmail3;

    Stage screen_sms_help_stage;

    // CheckBox Fields
    CheckBox checkboxHL;
    CheckBox checkboxWL;
    CheckBox checkboxLL;

    ComboBox controlComboBox;
    Button btnCancel;
    Button btnSave;
    Button btnSmsHelp;

    Label labelMetroPCS;
    Label labelTMobile;
    Label labelVerizon;
    Label labelAtnt;
    Label labelSprint;
    Label labelCricket;
    Label labelNextel;
    Label labelCingularGSM;
    Label labelUsCellular;
    Label labelCingularTDMA;

    BorderPane pane;
    VBox vBoxAlertReportSettings;

    public AlertReportSettingView() {
        initView();
    }

    private void initView() {
        pane = new BorderPane();
        pane.setPrefSize(503.0, 377);
        pane.setPadding(new Insets(30));

        vBoxAlertReportSettings = new VBox(25);
        vBoxAlertReportSettings.setPadding(new Insets(30.0, 50.0, 30.0, 50.0));

        // Page Title
        Label labelAlertReportSettings = new Label("Alert & Report Settings");
        labelAlertReportSettings.setFont(Font.font("Tahoma", FontWeight.BOLD, 26));
        labelAlertReportSettings.setTextFill(Color.WHITE);

        // Sub title
        Label labelAlertSettings = new Label("Alert Settings");
        labelAlertSettings.setFont(Font.font("Tahoma", FontWeight.BOLD, 20));
        labelAlertSettings.setUnderline(true);
        labelAlertSettings.setTextFill(Color.WHITE);

        // Checkboxes
        VBox vBoxCheckBoxes = new VBox();
        checkboxHL = new CheckBox("Alert On High Limit");
        checkboxHL.setTextFill(Color.WHITE);
        checkboxHL.setFont(Font.font(14));
        checkboxHL.setPadding(new Insets(0.0, 0.0, 10.0, 0.0));
        checkboxHL.setSelected(PreferenceManager.isAlertOnHlEnabled() ? true : false);

        checkboxWL = new CheckBox("Alert On Wear Limit");
        checkboxWL.setTextFill(Color.WHITE);
        checkboxWL.setFont(Font.font(14));
        checkboxWL.setPadding(new Insets(0.0, 0.0, 10.0, 0.0));
        checkboxWL.setSelected(PreferenceManager.isAlertOnWlEnabled() ? true : false);

        checkboxLL = new CheckBox("Alert On Low Limit");
        checkboxLL.setTextFill(Color.WHITE);
        checkboxLL.setFont(Font.font(14));
        checkboxLL.setPadding(new Insets(0.0, 0.0, 10.0, 0.0));
        checkboxLL.setSelected(PreferenceManager.isAlertOnLlEnabled() ? true : false);

        vBoxCheckBoxes.getChildren().addAll(checkboxHL, checkboxWL, checkboxLL);


        // High Limit Percent
        HBox hBoxEmailSmsList = new HBox(5);
        //hBoxEmailSmsList.setAlignment(Pos.CENTER);

        Label labelEmailSmsList = new Label("Email / SMS List:");
        labelEmailSmsList.setTextFill(Color.WHITE);
        labelEmailSmsList.setFont(Font.font(17));
        labelEmailSmsList.setPrefWidth(173);
        labelEmailSmsList.setPrefHeight(26);

        // SMS help button
        btnSmsHelp = new Button("SMS Help");
        btnSmsHelp.setOnAction(buttonHandler);
        hBoxEmailSmsList.getChildren().addAll(labelEmailSmsList, btnSmsHelp);

        // Add email/sms text field
        HBox hBoxEmail1 = new HBox(5);
        Label labelEmail1 = new Label("Alert Email1");
        labelEmail1.setTextFill(Color.WHITE);
        labelEmail1.setFont(Font.font(14));
        labelEmail1.setPrefWidth(150);
        labelEmail1.setPrefHeight(26);
        txtEmail1 = new TextField();
        txtEmail1.setPrefHeight(30);
        txtEmail1.setPrefWidth(300);
        txtEmail1.getStyleClass().add("setting_inputs");
        hBoxEmail1.getChildren().addAll(labelEmail1, txtEmail1);

        HBox hBoxEmail2 = new HBox(5);
        Label labelEmail2 = new Label("Alert Email2");
        labelEmail2.setTextFill(Color.WHITE);
        labelEmail2.setFont(Font.font(14));
        labelEmail2.setPrefWidth(150);
        labelEmail2.setPrefHeight(26);
        txtEmail2 = new TextField();
        txtEmail2.setPrefHeight(30);
        txtEmail2.setPrefWidth(300);
        txtEmail2.getStyleClass().add("setting_inputs");
        hBoxEmail2.getChildren().addAll(labelEmail2, txtEmail2);

        HBox hBoxEmail3 = new HBox(5);
        Label labelEmail3 = new Label("Alert Email3");
        labelEmail3.setTextFill(Color.WHITE);
        labelEmail3.setFont(Font.font(14));
        labelEmail3.setPrefWidth(150);
        labelEmail3.setPrefHeight(26);
        txtEmail3 = new TextField();
        txtEmail3.setPrefHeight(30);
        txtEmail3.setPrefWidth(300);
        txtEmail3.getStyleClass().add("setting_inputs");
        hBoxEmail3.getChildren().addAll(labelEmail3, txtEmail3);

        /*Gson gson = new Gson();
        String jsonText = PreferenceManager.getEmailList();
        String[] text = gson.fromJson(jsonText, String[].class);
        System.out.println(Arrays.toString(text));*/

        txtEmail1.setText(PreferenceManager.getAlertEmail1());
        txtEmail2.setText(PreferenceManager.getAlertEmail2());
        txtEmail3.setText(PreferenceManager.getAlertEmail3());

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

        vBoxAlertReportSettings.getChildren().addAll(labelAlertReportSettings, labelAlertSettings, vBoxCheckBoxes,
                hBoxEmailSmsList, hBoxEmail1, hBoxEmail2, hBoxEmail3,
                hbBtn);

        pane.setCenter(vBoxAlertReportSettings);

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

    public void showSmsHelp() {
        VBox vBoxSmsHelp = new VBox();
        VBox vBoxTile = new VBox();
        vBoxTile.setAlignment(Pos.TOP_CENTER);
        VBox vBoxCarriers = new VBox();
        vBoxCarriers.setPadding(new Insets(30, 0, 0, 100));


        Label labelSmsHelp = new Label("SMS HELP");
        labelSmsHelp.setFont(Font.font("Tahoma", FontWeight.BOLD, 26));
        labelSmsHelp.setTextFill(Color.WHITE);
        Label labelSmsHelpSubtitle = new Label("If using mobile number, use this carrier format.\nClick on your carrier to save format to clipboard");
        labelSmsHelpSubtitle.setFont(Font.font("Tahoma", FontWeight.BOLD, 18));
        labelSmsHelpSubtitle.setTextFill(Color.WHITE);
        vBoxTile.getChildren().addAll(labelSmsHelp, labelSmsHelpSubtitle);

        HBox hBoxMetroPCS = new HBox();

        labelMetroPCS = new Label("Metro PCS:  ##########@mymetropcs.com");
        labelMetroPCS.setTextFill(Color.WHITE);
        labelMetroPCS.setFont(Font.font(14));
        labelMetroPCS.setPadding(new Insets(10.0, 0.0, 0.0, 0.0));
        labelMetroPCS.getStyleClass().add("hbox-sms");
        labelMetroPCS.setOnMouseClicked(hoverCopy);

        HBox hBoxTMobile = new HBox();
        labelTMobile = new Label("T-Mobile:  ##########@tmomail.net");
        labelTMobile.setTextFill(Color.WHITE);
        labelTMobile.setFont(Font.font(14));
        labelTMobile.setPadding(new Insets(10.0, 0.0, 0.0, 0.0));

        labelTMobile.getStyleClass().add("hbox-sms");
        labelTMobile.setOnMouseClicked(hoverCopy);

        TextField txtTMobile = new TextField("##########@tmomail.net");
        hBoxTMobile.getChildren().addAll(labelTMobile);

        HBox hBoxVerizon = new HBox();
        labelVerizon = new Label("Verizon:  ##########@vtext.com");
        labelVerizon.setTextFill(Color.WHITE);
        labelVerizon.setFont(Font.font(14));
        labelVerizon.setPadding(new Insets(10.0, 0.0, 0.0, 0.0));
        labelVerizon.getStyleClass().add("hbox-sms");
        labelVerizon.setOnMouseClicked(hoverCopy);

        hBoxVerizon.getChildren().addAll(labelVerizon);

        HBox hBoxAtnt = new HBox();
        labelAtnt = new Label("AT&T:  ##########@txt.att.net");
        labelAtnt.setTextFill(Color.WHITE);
        labelAtnt.setFont(Font.font(14));
        labelAtnt.setPadding(new Insets(10.0, 0.0, 0.0, 0.0));
        labelAtnt.getStyleClass().add("hbox-sms");
        labelAtnt.setOnMouseClicked(hoverCopy);

        hBoxAtnt.getChildren().addAll(labelAtnt);

        HBox hBoxSprint = new HBox();
        labelSprint = new Label("Sprint:  ##########@messaging.sprintpcs.com");
        labelSprint.setTextFill(Color.WHITE);
        labelSprint.setFont(Font.font(14));
        labelSprint.setPadding(new Insets(10.0, 0.0, 0.0, 0.0));
        labelSprint.getStyleClass().add("hbox-sms");
        labelSprint.setOnMouseClicked(hoverCopy);

        hBoxSprint.getChildren().addAll(labelSprint);

        HBox hBoxNextel = new HBox();
        labelNextel = new Label("Nextel:  ##########@messaging.nextel.com");
        labelNextel.setTextFill(Color.WHITE);
        labelNextel.setFont(Font.font(14));
        labelNextel.setPadding(new Insets(10.0, 0.0, 0.0, 0.0));
        labelNextel.getStyleClass().add("hbox-sms");
        labelNextel.setOnMouseClicked(hoverCopy);

        hBoxNextel.getChildren().addAll(labelNextel);

        HBox hBoxCricket = new HBox();
        labelCricket = new Label("Cricket:  ##########@sms.mycricket.com");
        labelCricket.setTextFill(Color.WHITE);
        labelCricket.setFont(Font.font(14));
        labelCricket.setPadding(new Insets(10.0, 0.0, 0.0, 0.0));
        labelCricket.getStyleClass().add("hbox-sms");
        labelCricket.setOnMouseClicked(hoverCopy);

        hBoxCricket.getChildren().addAll(labelCricket);

        HBox hBoxUsCellular = new HBox();
        labelUsCellular = new Label("US Cellular:  ##########@email.uscc.net");
        labelUsCellular.setTextFill(Color.WHITE);
        labelUsCellular.setFont(Font.font(14));
        labelUsCellular.setPadding(new Insets(10.0, 0.0, 0.0, 0.0));
        labelUsCellular.getStyleClass().add("hbox-sms");
        labelUsCellular.setOnMouseClicked(hoverCopy);

        hBoxUsCellular.getChildren().addAll(labelUsCellular);

        HBox hBoxCingularGSM = new HBox();
        labelCingularGSM = new Label("Cingular (GSM):  ##########@cingularme.com");
        labelCingularGSM.setTextFill(Color.WHITE);
        labelCingularGSM.setFont(Font.font(14));
        labelCingularGSM.setPadding(new Insets(10.0, 0.0, 0.0, 0.0));
        labelCingularGSM.getStyleClass().add("hbox-sms");
        labelCingularGSM.setOnMouseClicked(hoverCopy);

        hBoxCingularGSM.getChildren().addAll(labelCingularGSM);

        HBox hBoxCingularTDMA = new HBox();
        labelCingularTDMA = new Label("Cingular (TDMA):  ##########@mmode.com");
        labelCingularTDMA.setTextFill(Color.WHITE);
        labelCingularTDMA.setFont(Font.font(14));
        labelCingularTDMA.setPadding(new Insets(10.0, 0.0, 0.0, 0.0));
        labelCingularTDMA.getStyleClass().add("hbox-sms");
        labelCingularTDMA.setOnMouseClicked(hoverCopy);

        hBoxCingularTDMA.getChildren().addAll(labelCingularTDMA);

        vBoxCarriers.getChildren().addAll(hBoxMetroPCS, hBoxTMobile, hBoxVerizon, hBoxAtnt, hBoxSprint, hBoxNextel, hBoxCricket, hBoxUsCellular, hBoxCingularGSM, hBoxCingularTDMA);
        vBoxSmsHelp.getChildren().addAll(vBoxTile, vBoxCarriers);

        Scene sceneSmsHelp = new Scene(vBoxSmsHelp, 400, 377);
        sceneSmsHelp.getStylesheets().add(getClass().getClassLoader().getResource("resource/style/rootStyles.css").toExternalForm());

        screen_sms_help_stage = new Stage(StageStyle.DECORATED);
        screen_sms_help_stage.setTitle("SMS Carrier Info");

        screen_sms_help_stage.setScene(sceneSmsHelp);

        screen_sms_help_stage.centerOnScreen();
        screen_sms_help_stage.getIcons().add(LogoManager.getInstance().getLogo());
        //screen_sms_help_stage.setAlwaysOnTop(true);
        screen_sms_help_stage.setResizable(true);
        screen_sms_help_stage.setMinWidth(600);
        screen_sms_help_stage.setMinHeight(800);

        screen_sms_help_stage.show();
    }

    public void show() {
        screen_stage.show();
    }

    public void close() {
        screen_stage.close();
    }

    final EventHandler<MouseEvent> hoverCopy = new EventHandler<MouseEvent>() {
        String clipboardString;

        @Override
        public void handle(final MouseEvent event) {

            if (event.getSource() == labelAtnt) {
                clipboardString = labelAtnt.getText().substring(labelAtnt.getText().lastIndexOf("#") + 1);
            } else if (event.getSource() == labelVerizon) {
                clipboardString = labelVerizon.getText().substring(labelVerizon.getText().lastIndexOf("#") + 1);
            } else if (event.getSource() == labelSprint) {
                clipboardString = labelSprint.getText().substring(labelSprint.getText().lastIndexOf("#") + 1);
            } else if (event.getSource() == labelMetroPCS) {
                clipboardString = labelMetroPCS.getText().substring(labelMetroPCS.getText().lastIndexOf("#") + 1);
            } else if (event.getSource() == labelCricket) {
                clipboardString = labelCricket.getText().substring(labelCricket.getText().lastIndexOf("#") + 1);
            } else if (event.getSource() == labelTMobile) {
                clipboardString = labelTMobile.getText().substring(labelTMobile.getText().lastIndexOf("#") + 1);
            } else if (event.getSource() == labelUsCellular) {
                clipboardString = labelUsCellular.getText().substring(labelUsCellular.getText().lastIndexOf("#") + 1);
            } else if (event.getSource() == labelNextel) {
                clipboardString = labelNextel.getText().substring(labelNextel.getText().lastIndexOf("#") + 1);
            } else if (event.getSource() == labelCingularTDMA) {
                clipboardString = labelCingularTDMA.getText().substring(labelCingularTDMA.getText().lastIndexOf("#") + 1);
            } else if (event.getSource() == labelCingularGSM) {
                clipboardString = labelCingularGSM.getText().substring(labelCingularGSM.getText().lastIndexOf("#") + 1);
            }

            final ClipboardContent content = new ClipboardContent();
            content.putString(clipboardString);
            Clipboard.getSystemClipboard().setContent(content);
            screen_sms_help_stage.close();
            txtEmail1.setText(clipboardString);
            txtEmail1.positionCaret(0);
            Toast.message("Carrier Info Saved to Clipboard!");

        }
    };

    EventHandler<ActionEvent> buttonHandler = new EventHandler<ActionEvent>() {

        @Override
        public void handle(final ActionEvent event) {

            if (event.getSource() == btnCancel) {
                close();
            } else if (event.getSource() == btnSmsHelp) {
                showSmsHelp();
            } else if (event.getSource() == btnSave) {
                // Save Fanuc Settings
                PreferenceManager.setAlertOnHl(checkboxHL.isSelected() ? true : false);
                PreferenceManager.setAlertOnWl(checkboxWL.isSelected() ? true : false);
                PreferenceManager.setAlertOnLl(checkboxLL.isSelected() ? true : false);

                String email1 = txtEmail1.getText().trim();
                String email2 = txtEmail2.getText().trim();
                String email3 = txtEmail3.getText().trim();

                // Check Email1
                if (!email1.isEmpty() && !Utils.isValidEmail(email1)) {
                    Toast.message("Invalid Alert Email1");
                    return;
                }
                PreferenceManager.setAlertEmail1(email1);

                // Check Email2
                if (!email2.isEmpty() && !Utils.isValidEmail(email2)) {
                    Toast.message("Invalid Alert Email2");
                    return;
                }
                PreferenceManager.setAlertEmail2(email2);

                // Check Email3
                if (!email3.isEmpty() && !Utils.isValidEmail(email3)) {
                    Toast.message("Invalid Alert Email3");
                    return;
                }
                PreferenceManager.setAlertEmail3(email3);

                /*//Set the values
                Gson gson = new Gson();
                if (!(emailListArray == null)) {
                    List<String> textList = new ArrayList<String>(emailListArray);
                    String jsonText = gson.toJson(textList);
                    PreferenceManager.setEmailList(jsonText);
                }*/

                Toast.message("Success to Save Device Info!");
                close();
            }
        }
    };
}
