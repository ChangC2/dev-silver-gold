package view;

import Model.LogManager;
import Utils.PreferenceManager;
import Utils.Utils;
import de.re.easymodbus.exceptions.ModbusException;
import de.re.easymodbus.modbusclient.ModbusClient;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.util.*;

public class ProcessMonitorView {

    Stage screen_stage;

    private static ProcessMonitorView instance;

    public static ProcessMonitorView getInstance() {
        if (instance == null) {
            instance = new ProcessMonitorView();
        }
        return instance;
    }

    BorderPane pane;
    private Button activeButton = null;
    private String inactiveButtonStyle = "-fx-background-color: #333; -fx-text-fill: white; -fx-border-color: white; " +
            "-fx-border-width: 2px; -fx-border-radius: 5px; -fx-background-radius: 5px;";
    private Map<Button, Polygon> buttonArrowMap = new HashMap<>();
    ArrayList<Button> btns = new ArrayList<>();

    int timerInterval = 1000;
    int showCount = 100;
    String modbusIp = "192.168.0.53";
    int modbusPort = 502;
    int[] hregAddresses = {
            204,
            200,
            201,
            206,
            202,
            205
    };

    double[] rates = {
            1,
            1,
            0.01,
            0.01,
            1,
            0.1
    };

    String[] titles = {
            "Vibration Lvl:\t  %d mm²/s",
            "Chip Lvl:\t\t  %d mm",
            "Flow Rate:\t  %.2f L/min",
            "Coolant Quality: %d %%",
            "Coolant Lvl:\t  %d mm",
            "Coolant Temp:\t  %d C"
    };

    String[] btnTitles = {
            "Vibration Lvl",
            "Chip Lvl",
            "Flow Rate",
            "Coolant Quality",
            "Coolant Lvl",
            "Coolant Temp"
    };
    String[] chartTitles = {
            "Vibration Lvl",
            "Chip Lvl",
            "Flow Rate",
            "Coolant Quality",
            "Coolant Lvl",
            "Coolant Temp"
    };

    ModbusClient modbusClientPLC;
    int hregIndex = 0;
    ArrayList<float[]> values = new ArrayList<>();
    ArrayList<String> times = new ArrayList<>();
    Timer timer = new Timer();

    public ProcessMonitorView() {
        initView();
    }

    private void initView() {
        connectPLC();
        pane = new BorderPane();
        pane.setPrefSize(1000, 600);
        pane.setPadding(new Insets(30, 50, 30, 30));
        setupLeftPanel();
        setupRightPanel();

        // Scene and stage setup
        Scene scene = new Scene(pane);
        scene.getStylesheets().add(getClass().getClassLoader().getResource("resource/style/rootStyles.css").toExternalForm());

        screen_stage = new Stage(StageStyle.DECORATED);
        screen_stage.setTitle("Process Monitor");
        screen_stage.setScene(scene);
        screen_stage.centerOnScreen();
        screen_stage.setResizable(false);
        screen_stage.setOnCloseRequest((WindowEvent event) -> {
            close();
            // Optional: Add additional cleanup code here if needed
        });


        screen_stage.setOnShown((WindowEvent event) -> {
            TimerTask timerTask = new TimerTask()
            {
                public void run()
                {
                    System.out.println("Start Reading Process Monitor PLC!!!");
                    connectPLC();
                    float[] oneTimeValues = new float[hregAddresses.length];
                    for (int i=0;i<hregAddresses.length;i++){
                        try {
                            int[] holdingPLCRegs = modbusClientPLC.ReadHoldingRegisters(hregAddresses[i], 1);
                            oneTimeValues[i] = (float)(holdingPLCRegs[0] * rates[i]);
                            if (i==2){
                                btnTitles[i] = String.format(titles[i], (float)(holdingPLCRegs[0] * rates[i]));
                            }else{
                                btnTitles[i] = String.format(titles[i], (int)(holdingPLCRegs[0] * rates[i]));
                            }
                            System.out.println(btnTitles[i]);
                        } catch (ModbusException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    values.add(oneTimeValues);
                    if (values.size() > showCount){
                        values.remove(0);
                    }
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            for (int i=0;i<btns.size();i++) {
                                btns.get(i).setText(btnTitles[i]);
                            }
                            setupRightPanel();
                        }
                    });
                }
            };
            timer = new Timer();
            timer.scheduleAtFixedRate(timerTask, 0, timerInterval);
        });

    }

    private  void setupLeftPanel() {
        VBox leftPanel = new VBox(10);
        leftPanel.setPadding(new Insets(20));
        leftPanel.setPrefWidth(300);
        leftPanel.setAlignment(Pos.CENTER);

        GridPane grid = new GridPane();
        grid.setHgap(5);
        grid.setVgap(10);
        grid.setAlignment(Pos.CENTER);

        // Array of button texts and corresponding values
        for (int i = 0; i < btnTitles.length; i++) {
            Button btn = createStyledButton(btnTitles[i], inactiveButtonStyle);
            btns.add(btn);
            // Polygon for the arrow, initially not visible
            Polygon arrow = new Polygon(0.0, 0.0, 10.0, 5.0, 0.0, 10.0);
            arrow.setStyle("-fx-fill: #EEE;");
            arrow.setVisible(false);
            buttonArrowMap.put(btn, arrow);

            grid.add(btn, 0, i);
            grid.add(arrow, 1, i);
        }

        // Add grid to left panel
        leftPanel.getChildren().add(grid);

        pane.setLeft(leftPanel);
        handleButtonAction(btns.get(0));
    }

    private void setupRightPanel() {
        // Axes setup
        NumberAxis xAxis = new NumberAxis();
        xAxis.setLabel("Time");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Value");

        // Creating the line chart
        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle(chartTitles[hregIndex]);

        // Populate the series with data
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("Hennig");

        // Sample data
        try {
            for (int i=0;i<values.size();i++){
                series.getData().add(new XYChart.Data<>(i, Float.valueOf(values.get(i)[hregIndex]) ));
                System.out.println(String.format("Process Monitor Values: %.2f", values.get(i)[hregIndex]));
            }
        }catch (Exception e){
            System.out.println("Process Monitor :" + e.getLocalizedMessage());
        }
        // Adding series to chart
        lineChart.getData().add(series);

        pane.setCenter(lineChart);
    }

    private Button createStyledButton(String text, String style) {
        Button btn = new Button(text);
        btn.setFont(Font.font("Arial", FontWeight.NORMAL, 18));
        btn.setMinWidth(280);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setMinWidth(50);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setStyle(style);
        btn.setOnAction(event -> handleButtonAction(btn));
        return btn;
    }

    private void handleButtonAction(Button button) {
        // Set button style
        if (activeButton != null) {
            buttonArrowMap.get(activeButton).setVisible(false);
            activeButton.setStyle(inactiveButtonStyle);
        }
        button.setStyle("-fx-background-color: #555; -fx-text-fill: #EEE; -fx-border-color: #EEE;" +
                "-fx-border-width: 2px; -fx-border-radius: 5px; -fx-background-radius: 5px;");
        buttonArrowMap.get(button).setVisible(true);
        activeButton = button;

        // Process button event
        String buttonText = button.getText();
        System.out.println("Button pressed: " + buttonText);
        if (buttonText.contains("Vibration Lvl:")) {
            hregIndex = 0;
            System.out.println("Handling Vibration Level");
        } else if (buttonText.contains("Chip Lvl:")) {
            hregIndex = 1;
            System.out.println("Handling Chip Level");
        } else if (buttonText.contains("Flow Rate:")) {
            hregIndex = 2;
            System.out.println("Handling Flow Rate");
        } else if (buttonText.contains("Coolant Quality:")) {
            hregIndex = 3;
            System.out.println("Handling Coolant Quality");
        } else if (buttonText.contains("Coolant Lvl:")) {
            hregIndex = 4;
            System.out.println("Handling Coolant Level");
        } else if (buttonText.contains("Coolant Temp:")) {
            hregIndex = 5;
            System.out.println("Handling Coolant Temperature");
        }
        setupRightPanel();
    }

    public void show() {
        screen_stage.show();
        screen_stage.requestFocus();
        screen_stage.toFront();
    }

    public void close() {
        timer.cancel();
        disconnectPLC();
        screen_stage.close();
    }

    private void connectPLC() {
        try {
            if (modbusClientPLC == null || !modbusClientPLC.isConnected()) {
                modbusClientPLC = new ModbusClient(modbusIp, modbusPort);
                modbusClientPLC.Connect();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void disconnectPLC() {
        try {
            if (modbusClientPLC != null) {
                modbusClientPLC.Disconnect();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
