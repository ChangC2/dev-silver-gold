package Database;

import Controller.Api;
import Main.Resource;
import Utils.DateTimeUtils;
import Utils.PreferenceManager;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.http.util.TextUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class ReportService {

    private static ReportService instance;

    public static ReportService getInstance() {
        if (instance == null) {
            instance = new ReportService();
        }
        return instance;
    }

    Boolean isRunning = false;
    Thread reportThread;
    Thread shiftDataThread;

    RegimeData regimeData;

    long lastReportTime = 0;
    int cntReportSuccess = 0;

    private static final long STATUS_UPDATE_INTERVAL = 1000 * 5;    // 5 seconds
    private static final long GAUGE_UPDATE_INTERVAL = 1000 * 60;    // 60 seconds
    private static final long SHIFTDATA_UPDATE_INTERVAL = 1000 * 60;// 60 seconds

    public void start() {
        if (isRunning == false) {

            regimeData = new RegimeData();

            // Run Gantt & Regime data thread
            reportThread = new Thread(progressUpdateRunnable);
            reportThread.start();

            // Run Shift data thread
            shiftDataThread = new Thread(shiftDataUploadRunnable);
            shiftDataThread.start();
        }

        isRunning = true;
    }

    public void stop() {
        if (reportThread != null) {
            reportThread.interrupt();
            reportThread = null;
        }

        if (shiftDataThread != null) {
            shiftDataThread.interrupt();
            shiftDataThread = null;
        }

        isRunning = false;
    }

    Runnable progressUpdateRunnable = new Runnable() {
        @Override
        public void run() {

            /*if (progressUpdateHandler != null) {
                reportGanttData();
                progressUpdateHandler.postDelayed(this, 10000);
            }*/

            while (reportThread != null) {
                reportGanttDataApi();

                try {
                    Thread.sleep(STATUS_UPDATE_INTERVAL);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    Runnable shiftDataUploadRunnable = new Runnable() {
        @Override
        public void run() {
            while (shiftDataThread != null) {
                uploadShiftData();
                try {
                    Thread.sleep(SHIFTDATA_UPDATE_INTERVAL);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private void reportGanttData() {

        //Log.e("Alarm", "" + sequence++);

        String accountID = PreferenceManager.getFactoryID();
        String machineID = PreferenceManager.getMachineID();

        if (TextUtils.isEmpty(accountID) || TextUtils.isEmpty(machineID))
            return;

        // Get Data
        List<GanttDataModel> ganttDataModels = DBTableGanttData.getAllGanttData();

        // If gantt is empty, then check Report time to reduce the db connection
        long currentTIme = System.currentTimeMillis();
        if (ganttDataModels.isEmpty() && (currentTIme - lastReportTime) < GAUGE_UPDATE_INTERVAL) {
            //Log.e("report", "No Gantt");
            return;
        }
        lastReportTime = currentTIme;

        Connection con = null;
        try {
            //Class.forName("com.mysql.jdbc.Driver");

            DriverManager.setLoginTimeout(10);

            String dbConnURL = DBParams.EX_CONNECTION_URL;
            String dbUser = DBParams.EX_DB_USER;
            String dbPass = DBParams.EX_DB_PASS;

            /*if (appSettings.isUsingLocalServer()) {
                dbConnURL = String.format("jdbc:mysql://%s:3309/slymms", appSettings.getLocalServerIP());
                dbUser = "root";
                dbPass = "root";
            }*/

            con = DriverManager.getConnection(dbConnURL, dbUser, dbPass);

            if (con == null) {
                System.out.println("report: DB conn Null!");

                // Send Server disconnect status
                sendServerConnectionStatus(false);
                return;
            }

            sendServerConnectionStatus(true);
            System.out.println("report: DB conn OK!!!");

            if (ganttDataModels.size() > 0) {

                for (int i = 0; i < ganttDataModels.size(); i++) {
                    GanttDataModel ganttData = ganttDataModels.get(i);

                    //System.out.println("report", String.format("%s, %s, %s, %s, %s, %d, %d", ganttData.getColor(), ganttData.getMachineId(), ganttData.getOperator(), ganttData.getStatus(),
                    //        ganttData.getTimeStamp(), ganttData.getStart(), ganttData.getEnd()));
                    if (ganttData.getStart() == 0 || ganttData.getEnd() == 0) {
                        // Invalid Records
                        DBTableGanttData.deleteGantData(ganttData);
                        continue;
                    }

                    String statementString = "";
                    PreparedStatement preparedStatement = null;
                    int records = 0;
                    boolean updateRecords = false;

                    if ("UPDATE_STATUS".equalsIgnoreCase(ganttData.getCreatedAt())) {
                        // Need to change status
                        statementString = String.format("update %s_ganttdata set status=?, color=?, comment=? where start>=? and end<=? and machine_id=?", accountID);
                        preparedStatement = con.prepareStatement(statementString);

                        preparedStatement.setString(1, ganttData.getStatus());
                        preparedStatement.setString(2, ganttData.getColor());
                        preparedStatement.setString(3, "update");
                        preparedStatement.setLong(4, ganttData.getStart() / 1000);
                        preparedStatement.setLong(5, ganttData.getEnd() / 1000);
                        preparedStatement.setString(6, ganttData.getMachineId());

                        updateRecords = true;
                    } else {
                        // Add new Record
                        statementString = String.format("insert into %s_ganttdata (created_at, machine_id, Operator, status, color, start, end, time_stamp, time_stamp_ms, job_id, interface)"
                                + " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", accountID);
                        preparedStatement = con.prepareStatement(statementString);

                        preparedStatement.setString(1, ganttData.getCreatedAt());
                        preparedStatement.setString(2, ganttData.getMachineId());
                        preparedStatement.setString(3, ganttData.getOperator());
                        preparedStatement.setString(4, ganttData.getStatus());
                        preparedStatement.setString(5, ganttData.getColor());
                        preparedStatement.setLong(6, ganttData.getStart() / 1000);
                        preparedStatement.setLong(7, ganttData.getEnd() / 1000);
                        preparedStatement.setString(8, ganttData.getTimeStamp());
                        preparedStatement.setInt(9, ganttData.getTimeStampMs());
                        preparedStatement.setString(10, ganttData.getJobId());
                        preparedStatement.setString(11, String.format("MMS Interface Module(%d%%)", ganttData.getBattLev()));
                    }

                    try {
                        records = preparedStatement.executeUpdate();
                    } catch (SQLException e) {
                        System.out.println("report: " + e.getMessage());

                        // Incase of duplicate recordes, we think it is ok for now.
                        if ("23000".equals(e.getSQLState())) {
                            records = 1;
                        }
                    }

                    if (records > 0) {
                        DBTableGanttData.deleteGantData(ganttData);
                        System.out.println("report: Upload Record OK!!!");
                    } else if (Math.abs(System.currentTimeMillis() - ganttData.getStart()) > 86400000) { // 1 Day Later data, then throw it
                        System.out.println("report: Old Data Throwing!!!");
                        DBTableGanttData.deleteGantData(ganttData);
                    } else {
                        System.out.println("report: Upload Record Error");
                    }
                }
            } else {
                System.out.println("report: No Gantt");
            }

            ///*
            //------- Calculate Regimes ---------
            // Check Reset Status
            String currentTimeString = DateTimeUtils.getDayTimeStringInUniformat();
            String lastRecordTime = PreferenceManager.getLastTime();

            /*if (myApp.isAppRunningStatus() == false &&
                    !currentTimeString.equals(lastRecordTime)) {
                // Report previous date
                appSettings.setTimeUnCat(0);
                appSettings.setTimeInCycle(0);
                appSettings.setTimeIdle1(0);
                appSettings.setTimeIdle2(0);
                appSettings.setTimeIdle3(0);
                appSettings.setTimeIdle4(0);
                appSettings.setTimeIdle5(0);
                appSettings.setTimeIdle6(0);
                appSettings.setTimeIdle7(0);
                appSettings.setTimeIdle8(0);

                appSettings.setBadParts(0);
                appSettings.setGoodParts(0);

                appSettings.setLastTime(currentTimeString);
            }*/

            // Calculate Regime Data
            RegimeUtils.calcRegimes(regimeData, true);

            String statementString = String.format("update %s_hst set Utilization=?, inCycle=?, uncat=?, offline=?, r1t=?, r2t=?, " +
                    "r3t=?, r4t=?, r5t=?, r6t=?, r7t=?, r8t=?, oee=?, availability=?, quality=?, performance=?, goodParts=?, badParts=?, square_inch=? where `date`=? and `machine_id`=?", accountID);
            PreparedStatement preparedStatement = con.prepareStatement(statementString);

            preparedStatement.setInt(1, (int) regimeData.utilization);
            preparedStatement.setInt(2, (int) regimeData.inCycleTime);
            preparedStatement.setInt(3, (int) (regimeData.unCatTime));
            preparedStatement.setInt(4, (int) (regimeData.offlineT));
            preparedStatement.setInt(5, (int) (regimeData.idle1Time));
            preparedStatement.setInt(6, (int) (regimeData.idle2Time));
            preparedStatement.setInt(7, (int) (regimeData.idle3Time));
            preparedStatement.setInt(8, (int) (regimeData.idle4Time));
            preparedStatement.setInt(9, (int) (regimeData.idle5Time));
            preparedStatement.setInt(10, (int) (regimeData.idle6Time));
            preparedStatement.setInt(11, (int) (regimeData.idle7Time));
            preparedStatement.setInt(12, (int) (regimeData.idle8Time));
            preparedStatement.setInt(13, (int) regimeData.oee);
            preparedStatement.setInt(14, (int) regimeData.Availablity);
            preparedStatement.setInt(15, (int) regimeData.quality);
            preparedStatement.setInt(16, (int) regimeData.performance);
            preparedStatement.setInt(17, PreferenceManager.getGoodParts());
            preparedStatement.setInt(18, PreferenceManager.getBadParts());
            preparedStatement.setFloat(19, regimeData.squareInch);
            preparedStatement.setString(20, currentTimeString);
            preparedStatement.setString(21, machineID);

            int records = 0;
            try {
                records = preparedStatement.executeUpdate();
            } catch (SQLException e) {
                System.out.println("report: " + e.getMessage());
            }

            if (records == 0) {
                statementString = String.format("insert into `%s_hst` (`Utilization`, `inCycle`, `uncat`, `offline`, `r1t`, `r2t`, " +
                        "`r3t`, `r4t`, `r5t`, `r6t`, `r7t`, `r8t`, `oee`, `availability`, `quality`, `performance`, `date`, `machine_id`, `goodParts`, `badParts`, `square_inch`) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", accountID);
                preparedStatement = con.prepareStatement(statementString);

                preparedStatement.setInt(1, (int) regimeData.utilization);
                preparedStatement.setInt(2, (int) regimeData.inCycleTime);
                preparedStatement.setInt(3, (int) (regimeData.unCatTime));
                preparedStatement.setInt(4, (int) (regimeData.offlineT));
                preparedStatement.setInt(5, (int) (regimeData.idle1Time));
                preparedStatement.setInt(6, (int) (regimeData.idle2Time));
                preparedStatement.setInt(7, (int) (regimeData.idle3Time));
                preparedStatement.setInt(8, (int) (regimeData.idle4Time));
                preparedStatement.setInt(9, (int) (regimeData.idle5Time));
                preparedStatement.setInt(10, (int) (regimeData.idle6Time));
                preparedStatement.setInt(11, (int) (regimeData.idle7Time));
                preparedStatement.setInt(12, (int) (regimeData.idle8Time));
                preparedStatement.setInt(13, (int) regimeData.oee);
                preparedStatement.setInt(14, (int) regimeData.Availablity);
                preparedStatement.setInt(15, (int) regimeData.quality);
                preparedStatement.setInt(16, (int) regimeData.performance);
                preparedStatement.setString(17, currentTimeString);
                preparedStatement.setString(18, machineID);
                preparedStatement.setInt(19, PreferenceManager.getGoodParts());
                preparedStatement.setInt(20, PreferenceManager.getBadParts());
                preparedStatement.setFloat(21, regimeData.squareInch);

                try {
                    records = preparedStatement.executeUpdate();
                } catch (SQLException e) {
                    System.out.println("report: " + e.getMessage());
                }

                if (records > 0) {
                    System.out.println("report: Added new record");
                }
            } else {
                System.out.println("report: Updated regime");
            }

            String qtyCompleted = PreferenceManager.getQtyCompleted();
            String jobId = PreferenceManager.getJobID();
            if (!TextUtils.isEmpty(qtyCompleted) && !TextUtils.isEmpty(jobId)) {
                String[] partsValue = qtyCompleted.split(",");
                if (partsValue.length ==2) {
                    int qtyGoodCompleted = Integer.parseInt(partsValue[0]);
                    int qtyBadCompleted = Integer.parseInt(partsValue[1]);

                    statementString = String.format("update %s_jobdata set qtyGoodCompleted=?, qtyBadCompleted=? where jobID=?", accountID);
                    preparedStatement = con.prepareStatement(statementString);

                    preparedStatement.setInt(1, qtyGoodCompleted);
                    preparedStatement.setInt(2, qtyBadCompleted);
                    preparedStatement.setString(3, jobId);

                    try {
                        records = preparedStatement.executeUpdate();
                    } catch (SQLException e) {
                        System.out.println("report: " + e.getMessage());
                    }

                    if (records > 0) {
                        // Update Success
                        System.out.println("report: Added QTY Completed");
                    }

                    PreferenceManager.setQtyCompleted("");
                }
            }
            //*/

            // Close the connection
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("report: " + e.getMessage());

            sendServerConnectionStatus(false);
        }
    }

    private void reportGanttDataApi() {

        String accountID = PreferenceManager.getFactoryID();
        String machineID = PreferenceManager.getMachineID();

        if (TextUtils.isEmpty(accountID) || TextUtils.isEmpty(machineID))
            return;

        // Get Data
        List<GanttDataModel> ganttDataModels = DBTableGanttData.getAllGanttData();

        // If gantt is empty, then check Report time to reduce the db connection
        long currentTIme = System.currentTimeMillis();
        if (ganttDataModels.isEmpty() && (currentTIme - lastReportTime) < GAUGE_UPDATE_INTERVAL) {
            //Log.e("report", "No Gantt");
            return;
        }
        lastReportTime = currentTIme;

        if (ganttDataModels.size() > 0) {

            cntReportSuccess = 0;

            for (int i = 0; i < ganttDataModels.size(); i++) {
                GanttDataModel ganttData = ganttDataModels.get(i);

                //Log.e("report", String.format("%s, %s, %s, %s, %s, %d, %d", ganttData.getColor(), ganttData.getMachineId(), ganttData.getOperator(), ganttData.getStatus(),
                //        ganttData.getTimeStamp(), ganttData.getStart(), ganttData.getEnd()));
                if (ganttData.getStart() == 0 || ganttData.getEnd() == 0) {
                    // Invalid Records
                    DBTableGanttData.deleteGantData(ganttData);
                    continue;
                }

                // 1 Day Later data, then throw it
                if (Math.abs(System.currentTimeMillis() - ganttData.getStart()) > 86400000) {
                    DBTableGanttData.deleteGantData(ganttData);
                    continue;
                }

                HttpPost post = new HttpPost(Api.SERVE_URL + Api.api_postGanttData);
                // add request parameters or form parameters
                List<NameValuePair> urlParameters = new ArrayList<>();

                if ("UPDATE_STATUS".equalsIgnoreCase(ganttData.getCreatedAt())) {
                    // Need to change status
                    //statementString = String.format("update %s_ganttdata set status=?, color=?, comment=? where start>=? and end<=? and machine_id=?", accountID);

                    urlParameters.add(new BasicNameValuePair("action", "1")); // Means that this is update action, not insert new record.
                    urlParameters.add(new BasicNameValuePair("customer_id", accountID));
                    urlParameters.add(new BasicNameValuePair("machine_id", ganttData.getMachineId()));

                    urlParameters.add(new BasicNameValuePair("status", ganttData.getStatus()));
                    urlParameters.add(new BasicNameValuePair("color", ganttData.getColor()));
                    urlParameters.add(new BasicNameValuePair("comment", "update"));

                    urlParameters.add(new BasicNameValuePair("start", String.valueOf(ganttData.getStart() / 1000)));
                    urlParameters.add(new BasicNameValuePair("end", String.valueOf(ganttData.getEnd() / 1000)));
                } else {
                    // Add new Record
                    //statementString = String.format("insert into %s_ganttdata (created_at, machine_id, Operator, status, color, start, end, time_stamp, time_stamp_ms, job_id, interface)"
                    //        + " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", accountID);

                    urlParameters.add(new BasicNameValuePair("action", "0")); // Means that this is for insertting new record.
                    urlParameters.add(new BasicNameValuePair("customer_id", accountID));
                    urlParameters.add(new BasicNameValuePair("machine_id", ganttData.getMachineId()));

                    urlParameters.add(new BasicNameValuePair("created_at", ganttData.getCreatedAt()));
                    urlParameters.add(new BasicNameValuePair("Operator", ganttData.getOperator()));
                    urlParameters.add(new BasicNameValuePair("status", ganttData.getStatus()));
                    urlParameters.add(new BasicNameValuePair("color", ganttData.getColor()));

                    urlParameters.add(new BasicNameValuePair("start", String.valueOf(ganttData.getStart() / 1000)));
                    urlParameters.add(new BasicNameValuePair("end", String.valueOf(ganttData.getEnd() / 1000)));

                    urlParameters.add(new BasicNameValuePair("time_stamp", ganttData.getTimeStamp()));
                    urlParameters.add(new BasicNameValuePair("time_stamp_ms", String.valueOf(ganttData.getTimeStampMs())));
                    urlParameters.add(new BasicNameValuePair("job_id", ganttData.getJobId()));
                    urlParameters.add(new BasicNameValuePair("interface", Resource.getVersionInfo()));
                }

                try {
                    post.setEntity(new UrlEncodedFormEntity(urlParameters));
                    HttpClient httpClient = HttpClientBuilder.create().build();

                    HttpResponse result = httpClient.execute(post);
                    String json = EntityUtils.toString(result.getEntity(), "UTF-8");

                    JSONObject response = new JSONObject(json);
                    try {
                        if (response.has("status") && response.getBoolean("status")) {

                            cntReportSuccess++;
                            DBTableGanttData.deleteGantData(ganttData);
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

            if (cntReportSuccess > 0) {
                sendServerConnectionStatus(true);
            } else {
                sendServerConnectionStatus(false);
            }
        } else {
            System.out.println("report: No Gantt");
        }

        ///*
        //------- Calculate Regimes ---------
        // Check Reset Status
        String currentTimeString = DateTimeUtils.getDayTimeStringInUniformat();

            /*String lastRecordTime = appSettings.getLastTime();
            if (myApp.isAppRunningStatus() == false &&
                    !currentTimeString.equals(lastRecordTime)) {
                // Report previous date
                appSettings.setTimeUnCat(0);
                appSettings.setTimeInCycle(0);
                appSettings.setTimeIdle1(0);
                appSettings.setTimeIdle2(0);
                appSettings.setTimeIdle3(0);
                appSettings.setTimeIdle4(0);
                appSettings.setTimeIdle5(0);
                appSettings.setTimeIdle6(0);
                appSettings.setTimeIdle7(0);
                appSettings.setTimeIdle8(0);

                appSettings.setBadParts(0);
                appSettings.setGoodParts(0);

                appSettings.setLastTime(currentTimeString);

            }*/

        // Calculate Regime Data and upload to HST ---------------------------------------------
        RegimeUtils.calcRegimes(regimeData, true);

        HttpPost post = new HttpPost(Api.SERVE_URL + Api.api_postHSTData);
        // add request parameters or form parameters
        List<NameValuePair> urlParameters = new ArrayList<>();

        urlParameters.add(new BasicNameValuePair("customer_id", accountID));
        urlParameters.add(new BasicNameValuePair("date", currentTimeString));
        urlParameters.add(new BasicNameValuePair("machine_id", machineID));

        urlParameters.add(new BasicNameValuePair("Utilization", String.valueOf((int) regimeData.utilization)));
        urlParameters.add(new BasicNameValuePair("inCycle", String.valueOf(regimeData.inCycleTime)));
        urlParameters.add(new BasicNameValuePair("uncat", String.valueOf(regimeData.unCatTime)));
        urlParameters.add(new BasicNameValuePair("offline", String.valueOf(regimeData.offlineT)));

        urlParameters.add(new BasicNameValuePair("r1t", String.valueOf(regimeData.idle1Time)));
        urlParameters.add(new BasicNameValuePair("r2t", String.valueOf(regimeData.idle2Time)));
        urlParameters.add(new BasicNameValuePair("r3t", String.valueOf(regimeData.idle3Time)));
        urlParameters.add(new BasicNameValuePair("r4t", String.valueOf(regimeData.idle4Time)));
        urlParameters.add(new BasicNameValuePair("r5t", String.valueOf(regimeData.idle5Time)));
        urlParameters.add(new BasicNameValuePair("r6t", String.valueOf(regimeData.idle6Time)));
        urlParameters.add(new BasicNameValuePair("r7t", String.valueOf(regimeData.idle7Time)));
        urlParameters.add(new BasicNameValuePair("r8t", String.valueOf(regimeData.idle8Time)));

        urlParameters.add(new BasicNameValuePair("oee", String.valueOf((int) regimeData.oee)));
        urlParameters.add(new BasicNameValuePair("availability", String.valueOf((int) regimeData.Availablity)));
        urlParameters.add(new BasicNameValuePair("quality", String.valueOf((int) regimeData.quality)));
        urlParameters.add(new BasicNameValuePair("performance", String.valueOf((int) regimeData.performance)));
        urlParameters.add(new BasicNameValuePair("goodParts", String.valueOf(PreferenceManager.getGoodParts())));
        urlParameters.add(new BasicNameValuePair("badParts", String.valueOf(PreferenceManager.getBadParts())));
        urlParameters.add(new BasicNameValuePair("square_inch", String.valueOf(regimeData.squareInch)));

        try {
            post.setEntity(new UrlEncodedFormEntity(urlParameters));
            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpResponse result = httpClient.execute(post);
            String json = EntityUtils.toString(result.getEntity(), "UTF-8");
            System.out.println(json);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Upload JobData Completed Count ------------------------------------------------------
        String qtyCompleted = PreferenceManager.getQtyCompleted();
        String jobId = PreferenceManager.getJobID();
        if (!TextUtils.isEmpty(qtyCompleted) && !TextUtils.isEmpty(jobId)) {
            String[] partsValue = qtyCompleted.split(",");
            if (partsValue.length == 2) {
                int qtyGoodCompleted = Integer.parseInt(partsValue[0]);
                int qtyBadCompleted = Integer.parseInt(partsValue[1]);

                post = new HttpPost(Api.SERVE_URL + Api.api_postJobDataCompletedParts);
                // add request parameters or form parameters
                urlParameters = new ArrayList<>();

                urlParameters.add(new BasicNameValuePair("customer_id", accountID));
                urlParameters.add(new BasicNameValuePair("qtyGoodCompleted", String.valueOf(qtyGoodCompleted)));
                urlParameters.add(new BasicNameValuePair("qtyBadCompleted", String.valueOf(qtyBadCompleted)));
                urlParameters.add(new BasicNameValuePair("jobID", jobId));

                try {
                    post.setEntity(new UrlEncodedFormEntity(urlParameters));
                    HttpClient httpClient = HttpClientBuilder.create().build();
                    HttpResponse result = httpClient.execute(post);
                    String json = EntityUtils.toString(result.getEntity(), "UTF-8");

                    JSONObject response = new JSONObject(json);
                    try {
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void uploadShiftData() {
        // Get Data
        List<ShiftDataModel> shiftDataList = DBTableShiftData.getUpdatedShiftDataList();
        if (shiftDataList.size() > 5) {
            shiftDataList = shiftDataList.subList(0, 5);
        }

        if (shiftDataList.size() > 0) {
            for (int i = 0; i < shiftDataList.size(); i++) {

                ShiftDataModel shiftData = shiftDataList.get(i);

                HttpPost post = new HttpPost(Api.SERVE_URL + Api.api_postShiftData);
                // add request parameters or form parameters
                List<NameValuePair> urlParameters = new ArrayList<>();

                urlParameters.add(new BasicNameValuePair("customer_id", PreferenceManager.getFactoryID()));

                urlParameters.add(new BasicNameValuePair("shift_id", String.valueOf(shiftData.getShiftId())));

                urlParameters.add(new BasicNameValuePair("jobId", shiftData.getJobID()));
                urlParameters.add(new BasicNameValuePair("sequenceNo", shiftData.getJobSequenceNo()));

                urlParameters.add(new BasicNameValuePair("machine", shiftData.getMachine()));
                urlParameters.add(new BasicNameValuePair("operator", shiftData.getOperator()));
                urlParameters.add(new BasicNameValuePair("userid", shiftData.getUserID()));

                urlParameters.add(new BasicNameValuePair("date", DateTimeUtils.toStringFormat_12(new Date(shiftData.getStartTime()))));

                urlParameters.add(new BasicNameValuePair("startTime", String.valueOf(shiftData.getStartTime())));
                urlParameters.add(new BasicNameValuePair("stopTime", String.valueOf(shiftData.getStopTime())));

                urlParameters.add(new BasicNameValuePair("oee", String.valueOf(shiftData.getOee())));
                urlParameters.add(new BasicNameValuePair("availability", String.valueOf(shiftData.getAvailablity())));
                urlParameters.add(new BasicNameValuePair("quality", String.valueOf(shiftData.getQuality())));
                urlParameters.add(new BasicNameValuePair("performance", String.valueOf(shiftData.getPerformance())));

                urlParameters.add(new BasicNameValuePair("goodParts", String.valueOf(shiftData.getGoodParts())));
                urlParameters.add(new BasicNameValuePair("badParts", String.valueOf(shiftData.getBadParts())));

                urlParameters.add(new BasicNameValuePair("uncat", String.valueOf(shiftData.getElapsedTimeInMils(0))));
                urlParameters.add(new BasicNameValuePair("inCycle", String.valueOf(shiftData.getElapsedTimeInMils(1))));
                urlParameters.add(new BasicNameValuePair("offline", String.valueOf(shiftData.getOffLineT())));
                urlParameters.add(new BasicNameValuePair("r1t", String.valueOf(shiftData.getElapsedTimeInMils(2))));
                urlParameters.add(new BasicNameValuePair("r2t", String.valueOf(shiftData.getElapsedTimeInMils(3))));
                urlParameters.add(new BasicNameValuePair("r3t", String.valueOf(shiftData.getElapsedTimeInMils(4))));
                urlParameters.add(new BasicNameValuePair("r4t", String.valueOf(shiftData.getElapsedTimeInMils(5))));
                urlParameters.add(new BasicNameValuePair("r5t", String.valueOf(shiftData.getElapsedTimeInMils(6))));
                urlParameters.add(new BasicNameValuePair("r6t", String.valueOf(shiftData.getElapsedTimeInMils(7))));
                urlParameters.add(new BasicNameValuePair("r7t", String.valueOf(shiftData.getElapsedTimeInMils(8))));
                urlParameters.add(new BasicNameValuePair("r8t", String.valueOf(shiftData.getElapsedTimeInMils(9))));

                urlParameters.add(new BasicNameValuePair("aux1data", String.valueOf(shiftData.getAuxData1())));
                urlParameters.add(new BasicNameValuePair("aux2data", String.valueOf(shiftData.getAuxData2())));
                urlParameters.add(new BasicNameValuePair("aux3data", String.valueOf(shiftData.getAuxData3())));

                urlParameters.add(new BasicNameValuePair("rework", String.valueOf(shiftData.getStatusRework())));
                urlParameters.add(new BasicNameValuePair("setup", String.valueOf(shiftData.getStatusSetup())));

                urlParameters.add(new BasicNameValuePair("shiftTime", shiftData.getShiftSetting()));
                urlParameters.add(new BasicNameValuePair("targetCycleTime", String.valueOf(shiftData.getTargetCycleTimeSeconds() * 1000))); // Convert to Miliseconds.
                urlParameters.add(new BasicNameValuePair("plannedProductionTime", String.valueOf(shiftData.getPlannedProductionTime())));   // Already used Miliseconds unit.

                try {
                    post.setEntity(new UrlEncodedFormEntity(urlParameters));
                    HttpClient httpClient = HttpClientBuilder.create().build();
                    HttpResponse result = httpClient.execute(post);
                    String json = EntityUtils.toString(result.getEntity(), "UTF-8");

                    JSONObject response = new JSONObject(json);
                    try {
                        if (response.has("status") && response.getBoolean("status")) {

                            if (shiftData.isCompleted()) {
                                // This data was completed, remove record in the db.
                                System.out.println("shiftData: Completed shift, remove record!");
                                DBTableShiftData.deleteShiftData(shiftData);
                            } else {
                                // This is new Shift Data in server and got new shiftID and update in local db.
                                if (shiftData.getShiftId() == 0) {
                                    System.out.println("shiftData: New shift data, refresh shift ID.");

                                    // User Information is exist
                                    int shiftId = response.optInt("shift_id");
                                    if (shiftId > 0) {
                                        shiftData.setShiftId(shiftId);
                                    }
                                }

                                // Update Shift Data Status
                                DBTableShiftData.updateShiftStatusAsProcessed(shiftData);
                            }
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
        }
    }

    // send server disconnect status
    private void sendServerConnectionStatus(boolean status) {
        // New version is available
        PreferenceManager.setServerConnectionStatus(status);
    }
}
