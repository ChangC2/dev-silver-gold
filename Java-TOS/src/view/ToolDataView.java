package view;

import Model.BooleanCell;
import Model.JobData;
import Model.JobDataManager;
import Model.TableModelJobData;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.util.converter.FloatStringConverter;
import javafx.util.converter.IntegerStringConverter;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;

public class ToolDataView {
    BorderPane viewPane = new BorderPane();
    Stage screen_stage;

    private static ToolDataView instance;

    public static ToolDataView getInstance() {
        if (instance == null) {
            instance = new ToolDataView();
        }
        return instance;
    }

    public ToolDataView() {
        initView();
    }

    Label labelProgramNumber;
    TextField txtProgramNumber;
    Button btnLoadData;

    Button btnNewJob;

    Button btnClose;
    Button btnSavePDF;
    Label lblPDFLocation;

    TableView<TableModelJobData> tableView;
    private final ObservableList<TableModelJobData> jobDataObservableList = FXCollections.observableArrayList();

    ArrayList<JobData> jobDataList;

    private void initView() {

        viewPane.setTop(getTopView());
        viewPane.setBottom(getBottomView());

        // Init TableView
        tableView = new TableView<TableModelJobData>();
        tableView.setId("job-table");
        tableView.setEditable(true);
        initTableHeader();
        viewPane.setCenter(tableView);

        // Init Scene
        Scene scene = new Scene(viewPane, 1200, 800);
        scene.getStylesheets().add(getClass().getClassLoader().getResource("resource/style/rootStyles.css").toExternalForm());

        screen_stage = new Stage(StageStyle.DECORATED);
        screen_stage.setTitle("PROGRAM INFORMATION");

        screen_stage.setScene(scene);
        screen_stage.centerOnScreen();
        screen_stage.getIcons().add(LogoManager.getInstance().getLogo());
        //screen_stage.setAlwaysOnTop(true);
        screen_stage.setResizable(true);
        screen_stage.setMinWidth(1000);
        screen_stage.setMinHeight(700);

        refreshToolData();
    }

    public void show() {
        screen_stage.show();
        screen_stage.requestFocus();
        screen_stage.toFront();

        // refres tool data
        refreshToolData();
    }

    public void close() {
        screen_stage.close();
    }

    public void refreshToolData() {
        // Program Number Logic
        if (MainView.getInstance().uiVars.ProgramNumber > 0) {
            // In case of Fanuc_ProgNum > 0, use it and disable user inputs

            btnLoadData.setVisible(false);
            txtProgramNumber.setEditable(false);
            labelProgramNumber.setText("PROGRAM NUMBER FROM MACHINE: ");
            txtProgramNumber.setText(String.valueOf(MainView.getInstance().uiVars.ProgramNumber));
        } else {
            // if the prog number entered isn't zero set the program
            btnLoadData.setVisible(true);
            txtProgramNumber.setEditable(true);
            labelProgramNumber.setText("ENTER PROGRAM NUMBER   : ");
            txtProgramNumber.setText(String.valueOf(MainView.getInstance().valueProgNum.getText()));
        }

        showJobData();
    }

    // Load job data
    public void showJobData() {

        int progNum = 0;
        try {
            progNum = Integer.parseInt(txtProgramNumber.getText());
        } catch (Exception e) {
            e.printStackTrace();
        }

        jobDataObservableList.clear();
        if (jobDataList != null && !jobDataList.isEmpty()) {
            jobDataList.clear();
        }
        jobDataList = JobDataManager.getInstance().getJobDataList(progNum);
        for (JobData item : jobDataList) {
            jobDataObservableList.add(new TableModelJobData(item));
        }
    }

    private HBox getTopView() {
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(10);
        hBox.setPadding(new Insets(15, 15, 15, 15));

        labelProgramNumber = new Label("PROGRAM NUMBER");
        labelProgramNumber.setPadding(new Insets(10, 10, 10, 10));
        labelProgramNumber.setId("label-jobid");
        labelProgramNumber.setTextFill(Color.WHITE);
        hBox.getChildren().add(labelProgramNumber);

        txtProgramNumber = new TextField();
        txtProgramNumber.setPromptText("Input Program Number");
        txtProgramNumber.setPrefWidth(200);
        txtProgramNumber.setAlignment(Pos.CENTER);
        hBox.getChildren().add(txtProgramNumber);
        // force the field to be numeric only
        txtProgramNumber.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    txtProgramNumber.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });

        // Button Load File Data
        btnLoadData = new Button("LOAD");
        btnLoadData.setAlignment(Pos.CENTER);
        btnLoadData.getStyleClass().add("button-gradient5");
        btnLoadData.setPrefWidth(120);
        btnLoadData.setPrefHeight(40);
        btnLoadData.setMaxWidth(120);
        btnLoadData.setMinWidth(120);
        hBox.getChildren().add(btnLoadData);
        btnLoadData.setOnAction(buttonHandler);

        Label labelTitle = new Label("TOOL DATA");
        labelTitle.setPadding(new Insets(10, 10, 10, 10));
        labelTitle.setId("label-title-tooldata");
        labelTitle.setFont(ViewHelper.getInstance().getFontInformation());
        labelTitle.setTextFill(Color.WHITE);
        labelTitle.setAlignment(Pos.CENTER);
        labelTitle.setMaxWidth(Double.POSITIVE_INFINITY);
        HBox.setHgrow(labelTitle, Priority.ALWAYS);
        hBox.getChildren().add(labelTitle);

        // Button Insert new Job
        btnNewJob = new Button("INSERT NEW PROGRAM FROM FILE EXPLORER");
        btnNewJob.setAlignment(Pos.CENTER);
        btnNewJob.getStyleClass().add("button-gradient5");
        btnNewJob.setPrefWidth(280);
        btnNewJob.setPrefHeight(40);
        btnNewJob.setMaxWidth(280);
        btnNewJob.setMinWidth(150);
        hBox.getChildren().add(btnNewJob);
        btnNewJob.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Upload Program File");

                new FileChooser.ExtensionFilter("SLY", "*.sly*");
                File selectedFile = fileChooser.showOpenDialog(null);


                String pickUpPathField = selectedFile.getName().split("\\.")[0];

                String FROM = selectedFile.getPath();

                File newF = new File("C:\\Users\\Shaffe City\\IdeaProjects\\TOS328\\tooldatafiles\\" + pickUpPathField + ".sly");

                String TO = newF.getPath();

                System.out.println(newF);
                System.out.println(Paths.get(selectedFile.getPath()));
                CopyOption[] options = new CopyOption[]{
                        StandardCopyOption.REPLACE_EXISTING,
                        StandardCopyOption.COPY_ATTRIBUTES
                };
                try {
                    Files.copy(Paths.get(FROM), Paths.get(TO), options);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                MainView.getInstance().valueProgNum.setText(pickUpPathField);
                screen_stage.close();
            }
        });

        return hBox;
    }

    private VBox getBottomView() {
        VBox vBox = new VBox();
        vBox.setSpacing(2);
        vBox.setPadding(new Insets(1, 15, 1, 15));

        HBox hButtonBox = new HBox();
        hButtonBox.setAlignment(Pos.CENTER);
        hButtonBox.setSpacing(10);

        // Button Close And Save
        btnClose = new Button("Close");
        btnClose.setAlignment(Pos.CENTER);
        btnClose.getStyleClass().add("button-gradient5");
        btnClose.setPrefWidth(150);
        btnClose.setMaxWidth(200);
        btnClose.setMinWidth(120);
        hButtonBox.getChildren().add(btnClose);
        btnClose.setOnAction(buttonHandler);

        // Button Add new Tool/Section
        btnSavePDF = new Button("Save PDF");
        btnSavePDF.setAlignment(Pos.CENTER);
        btnSavePDF.getStyleClass().add("button-gradient5");
        btnSavePDF.setPrefWidth(150);
        btnSavePDF.setMaxWidth(200);
        btnSavePDF.setMinWidth(120);
        hButtonBox.getChildren().add(btnSavePDF);

        vBox.getChildren().add(hButtonBox);

        lblPDFLocation = new Label(" ");
        lblPDFLocation.setPadding(new Insets(10, 10, 10, 10));
        lblPDFLocation.setId("label-pdf-location");
        lblPDFLocation.setFont(ViewHelper.getInstance().getFontInformation());
        lblPDFLocation.setTextFill(Color.WHITE);
        lblPDFLocation.setAlignment(Pos.CENTER);
        vBox.getChildren().add(lblPDFLocation);

        return vBox;
    }

    private void initTableHeader() {
        // Tool
        TableColumn colTool = new TableColumn("Tool");
        colTool.setMinWidth(100);
        colTool.setCellValueFactory(new PropertyValueFactory<TableModelJobData, String>("propTool"));
        colTool.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        colTool.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<TableModelJobData, Integer>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<TableModelJobData, Integer> t) {
                Toast.message("Couldn't change this column!");

                ((TableModelJobData) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                ).setPropTool(t.getOldValue());
            }
        });

        // Section
        TableColumn colSection = new TableColumn("Section");
        colSection.setMinWidth(100);
        colSection.setCellValueFactory(new PropertyValueFactory<TableModelJobData, String>("propSection"));
        colSection.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        colSection.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<TableModelJobData, Integer>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<TableModelJobData, Integer> t) {
                Toast.message("Couldn't change this column!");

                ((TableModelJobData) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                ).setPropSection(t.getOldValue());
            }
        });

        // Channel
        TableColumn colChannel = new TableColumn("Channel");
        colChannel.setMinWidth(100);
        colChannel.setCellValueFactory(new PropertyValueFactory<TableModelJobData, String>("propChannel"));
        colChannel.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        colChannel.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<TableModelJobData, Integer>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<TableModelJobData, Integer> t) {
                Toast.message("Couldn't change this column!");

                ((TableModelJobData) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                ).setPropChannel(t.getOldValue());
            }
        });

        // Comment
        TableColumn colComment = new TableColumn("Comment");
        colComment.setMinWidth(100);
        colComment.setCellValueFactory(new PropertyValueFactory<TableModelJobData, String>("propComment"));
        colComment.setCellFactory(TextFieldTableCell.forTableColumn());
        colComment.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<TableModelJobData, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<TableModelJobData, String> t) {
                if (!checkValidUpdate()) {
                     return;
                }
                ((TableModelJobData) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                ).setPropComment(t.getNewValue());
                saveUpdates();
            }
        });

        // Taget
        TableColumn colTarget = new TableColumn("Target");
        colTarget.setMinWidth(100);
        colTarget.setCellValueFactory(new PropertyValueFactory<TableModelJobData, String>("propTarget"));
        colTarget.setCellFactory(TextFieldTableCell.forTableColumn(new FloatStringConverter()));
        colTarget.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<TableModelJobData, Float>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<TableModelJobData, Float> t) {
                if (!checkValidUpdate()) {
                    return;
                }
                ((TableModelJobData) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                ).setPropTarget(t.getNewValue());
                saveUpdates();
            }
        });

        // High Limit
        TableColumn colHighLimit = new TableColumn("High Limit");
        colHighLimit.setMinWidth(100);
        colHighLimit.setCellValueFactory(new PropertyValueFactory<TableModelJobData, String>("propHighLimit"));
        colHighLimit.setCellFactory(TextFieldTableCell.forTableColumn(new FloatStringConverter()));
        colHighLimit.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<TableModelJobData, Float>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<TableModelJobData, Float> t) {
                if (!checkValidUpdate()) {
                    return;
                }
                ((TableModelJobData) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                ).setPropHighLimit(t.getNewValue());
                saveUpdates();
            }
        });

        // Wear Limit
        TableColumn colWearLimit = new TableColumn("Wear Limit");
        colWearLimit.setMinWidth(100);
        colWearLimit.setCellValueFactory(new PropertyValueFactory<TableModelJobData, String>("propWearLimit"));
        colWearLimit.setCellFactory(TextFieldTableCell.forTableColumn(new FloatStringConverter()));
        colWearLimit.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<TableModelJobData, Float>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<TableModelJobData, Float> t) {
                if (!checkValidUpdate()) {
                    return;
                }
                ((TableModelJobData) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                ).setPropWearLimit(t.getNewValue());
                saveUpdates();
            }
        });

        // Idle
        TableColumn colIdle = new TableColumn("Idle");
        colIdle.setMinWidth(100);
        colIdle.setCellValueFactory(new PropertyValueFactory<TableModelJobData, String>("propIdle"));
        colIdle.setCellFactory(TextFieldTableCell.forTableColumn(new FloatStringConverter()));
        colIdle.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<TableModelJobData, Float>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<TableModelJobData, Float> t) {
                if (!checkValidUpdate()) {
                    return;
                }
                ((TableModelJobData) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                ).setPropIdle(t.getNewValue());
                saveUpdates();
            }
        });

        // Low Limit Time
        TableColumn colLowLimitTime = new TableColumn("Low Limit Time");
        colLowLimitTime.setMinWidth(100);
        colLowLimitTime.setCellValueFactory(new PropertyValueFactory<TableModelJobData, String>("propLowLimitTime"));
        colLowLimitTime.setCellFactory(TextFieldTableCell.forTableColumn(new FloatStringConverter()));
        colLowLimitTime.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<TableModelJobData, Float>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<TableModelJobData, Float> t) {
                if (!checkValidUpdate()) {
                    return;
                }
                ((TableModelJobData) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                ).setPropLowLimitTime(t.getNewValue());
                saveUpdates();
            }
        });

        // Adaptive Enable
        TableColumn colAdaptiveEnable = new TableColumn("Adaptive Enable");
        colAdaptiveEnable.setMinWidth(100);
        colAdaptiveEnable.setCellValueFactory(new PropertyValueFactory<TableModelJobData, Boolean>("propAdaptiveEnable"));
        colAdaptiveEnable.setCellFactory(getBooleanFactory());
        colAdaptiveEnable.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<TableModelJobData, Boolean>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<TableModelJobData, Boolean> t) {
                if (!checkValidUpdate()) {
                    return;
                }
                System.out.println("hi change");
                ((TableModelJobData) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                ).setPropAdaptiveEnable(t.getNewValue());
                saveUpdates();
            }
        });

        // Macro Interrupt Enable
        TableColumn colMacroInterruptEnable = new TableColumn("Macro Interrupt Enable");
        colMacroInterruptEnable.setMinWidth(200);
        colMacroInterruptEnable.setCellValueFactory(new PropertyValueFactory<TableModelJobData, Boolean>("propMacroInterruptEnable"));
        colMacroInterruptEnable.setCellFactory(getBooleanFactory());
        colMacroInterruptEnable.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<TableModelJobData, Boolean>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<TableModelJobData, Boolean> t) {
                if (!checkValidUpdate()) {
                    return;
                }
                ((TableModelJobData) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                ).setPropMacroInterruptEnable(t.getNewValue());
                saveUpdates();
            }
        });

        // Lead-in Feedrate
        TableColumn colLeadInFeedrate = new TableColumn("Lead-in Feedrate");
        colLeadInFeedrate.setMinWidth(150);
        colLeadInFeedrate.setCellValueFactory(new PropertyValueFactory<TableModelJobData, String>("propLeadinFeedrate"));
        colLeadInFeedrate.setCellFactory(TextFieldTableCell.forTableColumn(new FloatStringConverter()));
        colLeadInFeedrate.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<TableModelJobData, Float>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<TableModelJobData, Float> t) {
                if (!checkValidUpdate()) {
                    return;
                }
                ((TableModelJobData) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                ).setPropLeadinFeedrate(t.getNewValue());
                saveUpdates();
            }
        });

        // Lead-in Trigger
        TableColumn colLeadInTrigger = new TableColumn("Lead-in Trigger");
        colLeadInTrigger.setMinWidth(150);
        colLeadInTrigger.setCellValueFactory(new PropertyValueFactory<TableModelJobData, String>("propLeadinTrigger"));
        colLeadInTrigger.setCellFactory(TextFieldTableCell.forTableColumn(new FloatStringConverter()));
        colLeadInTrigger.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<TableModelJobData, Float>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<TableModelJobData, Float> t) {
                if (!checkValidUpdate()) {
                    return;
                }
                ((TableModelJobData) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                ).setPropLeadinTrigger(t.getNewValue());
                saveUpdates();
            }
        });

        // Lead-in Enable
        TableColumn colLeadInEnable = new TableColumn("Lead-in Enable");
        colLeadInEnable.setMinWidth(100);
        colLeadInEnable.setCellValueFactory(new PropertyValueFactory<TableModelJobData, Boolean>("propLeadinEnable"));
        colLeadInEnable.setCellFactory(getBooleanFactory());
        colLeadInEnable.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<TableModelJobData, Boolean>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<TableModelJobData, Boolean> t) {
                if (!checkValidUpdate()) {
                    return;
                }
                ((TableModelJobData) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                ).setPropLeadinEnable(t.getNewValue());
                saveUpdates();
            }
        });

        // Start Delay
        TableColumn colStartDelay = new TableColumn("Start Delay");
        colStartDelay.setMinWidth(100);
        colStartDelay.setCellValueFactory(new PropertyValueFactory<TableModelJobData, String>("propStartDelay"));
        colStartDelay.setCellFactory(TextFieldTableCell.forTableColumn(new FloatStringConverter()));
        colStartDelay.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<TableModelJobData, Float>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<TableModelJobData, Float> t) {
                if (!checkValidUpdate()) {
                    return;
                }
                ((TableModelJobData) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                ).setPropStartDelay(t.getNewValue());
                saveUpdates();
            }
        });

        // High Limit Delay
        TableColumn colHighLimitDelay = new TableColumn("High Limit Delay");
        colHighLimitDelay.setMinWidth(100);
        colHighLimitDelay.setCellValueFactory(new PropertyValueFactory<TableModelJobData, String>("propHighLimitDelay"));
        colHighLimitDelay.setCellFactory(TextFieldTableCell.forTableColumn(new FloatStringConverter()));
        colHighLimitDelay.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<TableModelJobData, Float>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<TableModelJobData, Float> t) {
                if (!checkValidUpdate()) {
                    return;
                }
                ((TableModelJobData) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                ).setPropHighLimitDelay(t.getNewValue());
                saveUpdates();
            }
        });

        // Wear Limit Delay
        TableColumn colWearLimitDelay = new TableColumn("Wear Limit Delay");
        colWearLimitDelay.setMinWidth(100);
        colWearLimitDelay.setCellValueFactory(new PropertyValueFactory<TableModelJobData, String>("propWearLimitDelay"));
        colWearLimitDelay.setCellFactory(TextFieldTableCell.forTableColumn(new FloatStringConverter()));
        colWearLimitDelay.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<TableModelJobData, Float>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<TableModelJobData, Float> t) {
                if (!checkValidUpdate()) {
                    return;
                }
                ((TableModelJobData) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                ).setPropWearLimitDelay(t.getNewValue());
                saveUpdates();
            }
        });

        // Adaptive Min
        TableColumn colAdaptiveMin = new TableColumn("Adaptive Min");
        colAdaptiveMin.setMinWidth(200);
        colAdaptiveMin.setCellValueFactory(new PropertyValueFactory<TableModelJobData, String>("propAdaptiveMin"));
        colAdaptiveMin.setCellFactory(TextFieldTableCell.forTableColumn(new FloatStringConverter()));
        colAdaptiveMin.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<TableModelJobData, Float>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<TableModelJobData, Float> t) {
                if (!checkValidUpdate()) {
                    return;
                }
                ((TableModelJobData) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                ).setPropAdaptiveMin(t.getNewValue());
                saveUpdates();
            }
        });

        // Adaptive Max
        TableColumn colAdaptiveMax = new TableColumn("Adaptive Max");
        colAdaptiveMax.setMinWidth(200);
        colAdaptiveMax.setCellValueFactory(new PropertyValueFactory<TableModelJobData, String>("propAdaptiveMax"));
        colAdaptiveMax.setCellFactory(TextFieldTableCell.forTableColumn(new FloatStringConverter()));
        colAdaptiveMax.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<TableModelJobData, Float>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<TableModelJobData, Float> t) {
                if (!checkValidUpdate()) {
                    return;
                }
                ((TableModelJobData) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                ).setPropAdaptiveMax(t.getNewValue());
                saveUpdates();
            }
        });

        // Adaptive Wear Limit
        TableColumn colAdaptiveWearLimit = new TableColumn("Adaptive Wear Limit");
        colAdaptiveWearLimit.setMinWidth(200);
        colAdaptiveWearLimit.setCellValueFactory(new PropertyValueFactory<TableModelJobData, String>("propAdaptiveWearLimit"));
        colAdaptiveWearLimit.setCellFactory(TextFieldTableCell.forTableColumn(new FloatStringConverter()));
        colAdaptiveWearLimit.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<TableModelJobData, Float>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<TableModelJobData, Float> t) {
                if (!checkValidUpdate()) {
                    return;
                }
                ((TableModelJobData) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                ).setPropAdaptiveWearLimit(t.getNewValue());
                saveUpdates();
            }
        });

        // Adaptive High Limit
        TableColumn colAdaptiveHighLimit = new TableColumn("Adaptive High Limit");
        colAdaptiveHighLimit.setMinWidth(200);
        colAdaptiveHighLimit.setCellValueFactory(new PropertyValueFactory<TableModelJobData, String>("propAdaptiveHighLimit"));
        colAdaptiveHighLimit.setCellFactory(TextFieldTableCell.forTableColumn(new FloatStringConverter()));
        colAdaptiveHighLimit.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<TableModelJobData, Float>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<TableModelJobData, Float> t) {
                if (!checkValidUpdate()) {
                    return;
                }
                ((TableModelJobData) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                ).setPropAdaptiveHighLimit(t.getNewValue());
                saveUpdates();
            }
        });

        // Filter
        TableColumn colFilter = new TableColumn("Filter");
        colFilter.setMinWidth(100);
        colFilter.setCellValueFactory(new PropertyValueFactory<TableModelJobData, String>("propFilter"));
        colFilter.setCellFactory(TextFieldTableCell.forTableColumn(new FloatStringConverter()));
        colFilter.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<TableModelJobData, Float>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<TableModelJobData, Float> t) {
                if (!checkValidUpdate()) {
                    return;
                }
                ((TableModelJobData) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                ).setPropFilter(t.getNewValue());
                saveUpdates();
            }
        });

        // Sensor Scale
        TableColumn colSensorScaleSend = new TableColumn("Sensor Scale Send");
        colSensorScaleSend.setMinWidth(100);
        colSensorScaleSend.setCellValueFactory(new PropertyValueFactory<TableModelJobData, String>("propSensorScaleSend"));
        colSensorScaleSend.setCellFactory(TextFieldTableCell.forTableColumn(new FloatStringConverter()));
        colSensorScaleSend.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<TableModelJobData, Float>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<TableModelJobData, Float> t) {
                if (!checkValidUpdate()) {
                    return;
                }
                ((TableModelJobData) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                ).setPropSensorScaleSend(t.getNewValue());
                saveUpdates();
            }
        });

        // Monitor TIme
        TableColumn colMonitorTime = new TableColumn("Monitor Time");
        colMonitorTime.setMinWidth(100);
        colMonitorTime.setCellValueFactory(new PropertyValueFactory<TableModelJobData, String>("propMonitorTime"));
        colMonitorTime.setCellFactory(TextFieldTableCell.forTableColumn(new FloatStringConverter()));
        colMonitorTime.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<TableModelJobData, Float>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<TableModelJobData, Float> t) {
                if (!checkValidUpdate()) {
                    return;
                }
                ((TableModelJobData) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                ).setPropMonitorTime(t.getNewValue());
                saveUpdates();
            }
        });

        // Wear Logic Feedrate
        TableColumn colWearLogicFeedrate = new TableColumn("Wear Logic - Feedrate");
        colWearLogicFeedrate.setMinWidth(150);
        colWearLogicFeedrate.setCellValueFactory(new PropertyValueFactory<TableModelJobData, Boolean>("propWearLogicFeedrate"));
        colWearLogicFeedrate.setCellFactory(getBooleanFactory());
        colWearLogicFeedrate.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<TableModelJobData, Boolean>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<TableModelJobData, Boolean> t) {
                if (!checkValidUpdate()) {
                    return;
                }
                System.out.println("wl-f change");
                ((TableModelJobData) t.getTableView().getItems().get(
                        t.getTablePosition().getRow())
                ).setPropWearLogicFeedrate(t.getNewValue());
                saveUpdates();
            }
        });


        tableView.getColumns().addAll(colTool, colSection, colChannel, colComment, colTarget, colHighLimit, colWearLimit, colIdle, colLowLimitTime,
                colAdaptiveEnable, colMacroInterruptEnable, colLeadInFeedrate, colLeadInTrigger, colLeadInEnable, colStartDelay, colHighLimitDelay, colWearLimitDelay,
                colAdaptiveMin, colAdaptiveMax, colAdaptiveWearLimit, colAdaptiveHighLimit, colFilter, colSensorScaleSend, colMonitorTime, colWearLogicFeedrate);
        tableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        tableView.setItems(jobDataObservableList);

        /*jobDataObservableList.addListener(new ListChangeListener<TableModelJobData>() {
            @Override
            public void onChanged(Change<? extends TableModelJobData> c) {
                while (c.next()) {
                    if (c.wasUpdated()) {
                        saveUpdates();
                    }
                }
            }
        });*/
    }

    private Callback<TableColumn<TableModelJobData, Boolean>, TableCell<TableModelJobData, Boolean>> getBooleanFactory() {
        Callback<TableColumn<TableModelJobData, Boolean>, TableCell<TableModelJobData, Boolean>> booleanCellFactory =
                new Callback<TableColumn<TableModelJobData, Boolean>, TableCell<TableModelJobData, Boolean>>() {
                    @Override
                    public TableCell<TableModelJobData, Boolean> call(TableColumn<TableModelJobData, Boolean> p) {
                        return new BooleanCell();
                    }
                };
        return booleanCellFactory;
    }

    final EventHandler<ActionEvent> buttonHandler = new EventHandler<ActionEvent>() {

        @Override
        public void handle(final ActionEvent event) {

            if (event.getSource() == btnLoadData) { // LOADS THE DATA FROM FILE
                // Load Job Data
                int jobID = 0;
                try {
                    jobID = Integer.parseInt(txtProgramNumber.getText().trim());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (jobID > 0 && MainView.getInstance().uiVars.ProgramNumber == 0) {
                    MainView.getInstance().setNewToolNumber(jobID);

                    showJobData();
                } else {
                    Toast.message("Invalid Program Number!");
                }
            } else if (event.getSource() == btnClose) {
                close();
            }
        }
    };

    public void saveUpdates() {
        try {
            // Load Job Data
            int jobID = 0;
            try {
                jobID = Integer.parseInt(txtProgramNumber.getText().trim());
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (jobID > 0) {
                if (JobDataManager.getInstance().saveJobData(jobID, jobDataList)) {
                    Toast.message("Changes have been saved!");

                    if (!MainView.getInstance().isTeachMode()) {
                        MainView.getInstance().setToolBarBox(jobID);
                    }
                } else {
                    Toast.message("Failed to save changes!");
                }
            } else {
                Toast.message("Program Number is Invalid!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean checkValidUpdate() {
        if (MainView.getInstance().isTeachMode()) {
            Toast.message("TeachMode On and couldn't update!");
            return false;
        } else {
            return true;
        }
    }
}
