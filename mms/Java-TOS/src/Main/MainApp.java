package Main;

import Controller.Api;
import Database.DBConnection;
import Database.ReportService;
import Haas.EthernetHaasMonitor;
import Model.MMSAgent;
import Model.ShiftTime;
import Model.ShiftTimeManager;
import Utils.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.http.util.TextUtils;
import org.json.JSONException;
import org.json.JSONObject;
import view.*;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainApp extends Application {

    public static Stage mainStage;

    private static MainApp instance;

    /* // Lock Instances -Way1
    RandomAccessFile randomAccessFile;
    FileLock fileLock;*/

    // Way1 - Use Socket
    private static final int INSTANCE_LOCK_PORT = 51507;
    ServerSocket serverSocket;

    public static MainApp getInstance() {
        if (instance == null) {
            instance = new MainApp();
        }
        return instance;
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void stop() throws Exception {
        super.stop();

        if (serverSocket != null) {
            serverSocket.close();
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        String domain = String.format("http://%s/user.spi?fshp=%d", "127.0.0.1", (int) (2.3 * 10));

        try{
            serverSocket =
                    new ServerSocket(INSTANCE_LOCK_PORT, 10, InetAddress.getLocalHost());
        } catch(java.net.BindException b){
            System.out.println("Already Running...");

            // Bind Exception Already have instance.
            AlertManager.getInstance().showAlert("Warning", "TOS App is already running!");
            Platform.exit();
            return;
        } catch(Exception e){
            System.out.println(e.toString());
        }

        /*
        // Registry Operation to write the Fanuc Bridge Settings
        WinRegistry.writeStringValue(WinRegistry.HKEY_LOCAL_MACHINE,
                "SOFTWARE\\xxxFactoryBridgexxx\\FactorySettings",
                "TestVar1",
                "AABaasdfdsasdfasdfasdfasd");

        String value = WinRegistry.readString(WinRegistry.HKEY_LOCAL_MACHINE,  // HKEY
                "SOFTWARE\\xxxFactoryBridgexxx\\FactorySettings",           // Key
                "TestVar1");                                    // ValueName

        String decVal = new Encrypter().decrypt(value);
        System.out.println("Windows Distribution = " + value);
        */

        /*File file = new File("tos-one-instance");
        randomAccessFile = new RandomAccessFile(file, "rw");
        fileLock = randomAccessFile.getChannel().tryLock();

        System.out.print(fileLock == null);
        if (fileLock == null) {
            AlertManager.getInstance().showAlert("Warning", "TOS App is already running!");
            Platform.exit();
        }*/

        Resource.initWindowSize();

        mainStage = primaryStage;

        // Start Splash
        showSplash();

        // Retrieve PLC Simulator Settings to prevent the unexpected crash.
        PreferenceManager.setPLCSimulatorEnabled(false);

        DBConnection.connection();

        // Check Serial Number
        //String serialNumber = DeviceInfo.getMotherboardNumber();
        //System.out.println(serialNumber);

        String keyTest = new KeyGen().makeTheKey("3625a2254789");
        //System.out.println(keyTest);

    }

    private void initMainView() {

        Scene scene = new Scene(MainView.getInstance().getView());

        scene.getStylesheets().add(getClass().getClassLoader().getResource("resource/style/rootStyles.css").toExternalForm());

        mainStage.setScene(scene);
        mainStage.setMaximized(true);
        mainStage.setResizable(true);
        mainStage.setMinWidth(1000);
        mainStage.setMinHeight(700);
        mainStage.initStyle(StageStyle.DECORATED);
        //mainStage.initStyle(StageStyle.UNDECORATED);
        mainStage.setTitle(Resource.TITLE + "(" + "2." + Resource.LOCALVER + ")");
        mainStage.getIcons().add(LogoManager.getInstance().getLogo());
        mainStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent event) {

                // Release Lock
                try {
                    /*fileLock.release();
                    randomAccessFile.close();*/

                    System.out.println("Closing");
                } catch (Exception ex) {
                    System.out.print(ex.getMessage());
                }

                MainView.getInstance().terminate();

                // Stop Report Service
                ReportService.getInstance().stop();

                // Stop MMSAgent
                MMSAgent.getInstance().stop();

                DBConnection.close();
                System.exit(0);
            }
        });
    }

    private void showSplash() {
        SplashView.getInstance().show();
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try {
                    Thread.sleep(500);
                    Platform.runLater(() -> {
                        SplashView.getInstance().close();

                        //PreferenceManager.setLicenseApproved(true);

                        // Check License
                        if (PreferenceManager.isLicenseApproved()) {
                            showMainView();
                        } else {
                            SerialKeyCheckView.getInstance().show();
                        }

                        //MMSView.getInstance().show();
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
        task.setOnSucceeded(e -> {});
        new Thread(task).start();
    }

    public void showMainView() {

        // Init Main Window
        initMainView();

        mainStage.show();

        // Start Report Service
        ReportService.getInstance().start();

        // Start MMSAgent
        MMSAgent.getInstance().start();

        // Check new app version
        MainView.getInstance().checkVersionUpdate(false);

        loadAppSettings();
    }

    public void loadAppSettings() {
        loadMachineSettings();
        loadShiftSettings();
    }

    public void loadMachineSettings() {}

    public void loadShiftSettings() {

        String factoryID = PreferenceManager.getFactoryID();
        if (TextUtils.isEmpty(factoryID)) {
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpPost post = new HttpPost(Api.SERVE_URL + Api.api_getShiftDetails);
                // add request parameters or form parameters
                List<NameValuePair> urlParameters = new ArrayList<>();
                urlParameters.add(new BasicNameValuePair("customer_id", factoryID));
                urlParameters.add(new BasicNameValuePair("machineId", PreferenceManager.getMachineID()));
                urlParameters.add(new BasicNameValuePair("date", DateTimeUtils.toStringFormat_13(new Date())));

                try {
                    post.setEntity(new UrlEncodedFormEntity(urlParameters));
                    HttpClient httpClient = HttpClientBuilder.create().build();
                    HttpResponse result = httpClient.execute(post);
                    String json = EntityUtils.toString(result.getEntity(), "UTF-8");

                    JSONObject response = new JSONObject(json);
                    try {
                        if (response.has("status") && response.getBoolean("status")) {

                            JSONObject jsonShiftData = response.getJSONObject("shift_info");

                            String shift1Start = jsonShiftData.getString("shift1_start");
                            String shift1End = jsonShiftData.getString("shift1_end");

                            String shift2Start = jsonShiftData.getString("shift2_start");
                            String shift2End = jsonShiftData.getString("shift2_end");

                            String shift3Start = jsonShiftData.getString("shift3_start");
                            String shift3End = jsonShiftData.getString("shift3_end");

                            ArrayList<ShiftTime> newShiftArray = new ArrayList<>();
                            if (!TextUtils.isEmpty(shift1Start) && !TextUtils.isEmpty(shift1End)) {
                                newShiftArray.add(new ShiftTime(shift1Start, shift1End));
                            }

                            if (!TextUtils.isEmpty(shift2Start) && !TextUtils.isEmpty(shift2End)) {
                                newShiftArray.add(new ShiftTime(shift2Start, shift2End));
                            }

                            if (!TextUtils.isEmpty(shift3Start) && !TextUtils.isEmpty(shift3End)) {
                                newShiftArray.add(new ShiftTime(shift3Start, shift3End));
                            }

                            ShiftTimeManager.getInstance().setShiftTime(newShiftArray);
                        } else {
                            String message = "";
                            if (response.has("message") && !response.isNull("message")) {
                                message = response.getString("message");
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void closeMainView() {
        mainStage.close();
    }

}
