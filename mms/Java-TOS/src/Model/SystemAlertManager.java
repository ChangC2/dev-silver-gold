package Model;

import Utils.DateTimeUtils;
import Utils.PreferenceManager;
import Utils.Utils;

import java.io.*;
import java.nio.file.FileSystems;
import java.util.ArrayList;
import java.util.Date;

public class SystemAlertManager {

    private static SystemAlertManager instance;

    public static SystemAlertManager getInstance() {
        if (instance == null) {
            instance = new SystemAlertManager();
        }
        return instance;
    }

    // ---------------------------------------------------------------------------------------------------
    // New Functions Set for managing the job data individually
    // ---------------------------------------------------------------------------------------------------

    private int parseInt(String strVal, int defVal) {
        if (strVal == null || strVal.trim().length() == 0) {
            return defVal;
        } else {
            try {
                return Integer.parseInt(strVal.trim());
            } catch (NumberFormatException e) {
                return defVal;
            }
        }
    }

    private float parseFloat(String strVal, float defVal) {
        if (strVal == null || strVal.trim().length() == 0) {
            return defVal;
        } else {
            try {
                return Float.parseFloat(strVal.trim());
            } catch (NumberFormatException e) {
                return defVal;
            }
        }
    }

    public synchronized String getFolderPath() {
        String toolDataFolderPath = PreferenceManager.getToolDataFilepath();

        return Utils.getAppDataFolder(toolDataFolderPath, "TOSSystemAlerts");
    }

    // LogManager.getInstance().addNewLog("Process Values are 0!", isTeachMode(), isMonitorOn());
    // LogManager.getInstance().addNewLog("PLC readings are all 0!", isTeachMode(), isMonitorOn());

    // Create new JOB Data
    public synchronized boolean addNewLog(String date, String time, int progNum, int tool, int section, int channel, String alarmType, float elapsedMonitorTime) {

        /*Date currTime = new Date();
        String valueDate = DateTimeUtils.formatDate(currTime, "MM/dd/yyyy");
        String valueTime = DateTimeUtils.formatDate(currTime, "HH:mm:ss");*/

        try {
            // Create Directory
            String absolutePath = getFolderPath();

            String filePath = absolutePath + File.separator + String.format("%d System Alarm.csv", progNum);
            boolean fileExists = new File(filePath).exists();
            Writer fileWriter = new FileWriter(filePath, true); // append contents
            BufferedWriter bf = new BufferedWriter(fileWriter);

            if (!fileExists) {
                // Write Header
                bf.append("Date, Time, Tool, Section, Channel, Alarm Type, Elapsed Monitor Time");
                bf.newLine();
            }

            String lineData = String.format("%s, %s, %d, %d, %d, %s, %f",
                    date, time, tool, section, channel, alarmType, elapsedMonitorTime);

            bf.append(lineData);
            bf.newLine();

            bf.close();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    public synchronized ArrayList<SystemAlertData> getSavingData(int progNum) {
        ArrayList<SystemAlertData> dataList = new ArrayList<>();

        String absolutePath = getFolderPath();
        String filePath = absolutePath + File.separator + String.format("%d System Alarm.csv", progNum);
        boolean fileExists = new File(filePath).exists();

        if (fileExists) {
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new FileReader(new File(filePath)));
                String line;
                while ((line = reader.readLine()) != null) {
                    // System.out.println(line);

                    // Check Header String
                    if (line.contains("Date") && line.contains("Time")) {
                        continue;
                    }

                    String[] values = line.split("\\s*,\\s*");
                    if (values == null || values.length < 7) {
                        continue;
                    }

                    // Parse Data
                    SystemAlertData newItem = new SystemAlertData();
                    newItem.setDate(values[0]);
                    newItem.setTime(values[1]);

                    newItem.setTool(parseInt(values[2], 0));
                    newItem.setSection(parseInt(values[3], 0));
                    newItem.setChannel(parseInt(values[4], 0));

                    newItem.setAlarmType(values[5]);
                    newItem.setElapsedMonitorTime(parseFloat(values[6], 0));

                    dataList.add(newItem);
                }

                reader.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return dataList;
    }

    public synchronized ArrayList<SystemAlertData> getSavingData(int progNum, Date startDate, Date endDate) {
        ArrayList<SystemAlertData> dataList = new ArrayList<>();

        String absolutePath = getFolderPath();
        String filePath = absolutePath + File.separator + String.format("%d System Alarm.csv", progNum);
        boolean fileExists = new File(filePath).exists();

        if (fileExists) {
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new FileReader(new File(filePath)));
                String line;
                while ((line = reader.readLine()) != null) {
                    // System.out.println(line);

                    // Check Header String
                    if (line.contains("Date") && line.contains("Time")) {
                        continue;
                    }

                    String[] values = line.split("\\s*,\\s*");
                    if (values == null || values.length < 7) {
                        continue;
                    }

                    // Check Date
                    Date alertDate = DateTimeUtils.parseDataFromFormat(values[0], "MM/dd/yyyy");
                    if (alertDate.before(startDate) || alertDate.after(endDate)) {
                        continue;
                    }

                    // Parse Data
                    SystemAlertData newItem = new SystemAlertData();
                    newItem.setDate(values[0]);
                    newItem.setTime(values[1]);

                    newItem.setTool(parseInt(values[2], 0));
                    newItem.setSection(parseInt(values[3], 0));
                    newItem.setChannel(parseInt(values[4], 0));

                    newItem.setAlarmType(values[5]);
                    newItem.setElapsedMonitorTime(parseFloat(values[6], 0));

                    dataList.add(newItem);
                }

                reader.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return dataList;
    }
}
