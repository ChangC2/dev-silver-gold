package Model;

import Utils.PreferenceManager;
import Utils.Utils;

import java.io.*;
import java.nio.file.FileSystems;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogManager {

    private static LogManager instance;
    private static final Boolean IsDebuggingMode = false;

    public static LogManager getInstance() {
        if (instance == null) {
            instance = new LogManager();
        }
        return instance;
    }

    // ---------------------------------------------------------------------------------------------------
    // New Functions Set for managing the job data individually
    // ---------------------------------------------------------------------------------------------------

    public synchronized String getFolderPath() {
        String toolDataFolderPath = PreferenceManager.getToolDataFilepath();

        return Utils.getAppDataFolder(toolDataFolderPath, "TOSLogs");
    }

    public static String formatDate(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    // LogManager.getInstance().addNewLog("Process Values are 0!", isTeachMode(), isMonitorOn());
    // LogManager.getInstance().addNewLog("PLC readings are all 0!", isTeachMode(), isMonitorOn());

    // Create new JOB Data
    public synchronized boolean addNewLog(String logMessage, boolean isTeachMode, boolean isMonitorOn) {

        if (!IsDebuggingMode)
            return true;

        Date currTime = new Date();
        String fileDate = formatDate(currTime, "yyyy-MM-dd");
        String logDate = formatDate(currTime, "yyyy-MM-dd HH:mm:ss.SSS");

        try {
            // Create Directory
            String absolutePath = getFolderPath();

            String filePath = absolutePath + File.separator + String.format("%s.log", fileDate);
            Writer fileWriter = new FileWriter(filePath, true); //overwrites file
            BufferedWriter bf = new BufferedWriter(fileWriter);

            String lineData = String.format("%s\t%s\t\t%s\t%s", logDate, logMessage, isTeachMode ? "Teach ON" : "Teach OFF", isMonitorOn ? "Monitor ON" : "Monitor Off");

            bf.append(lineData);
            bf.newLine();

            bf.close();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    // Create new JOB Data
    public synchronized boolean addNewLog(String logMessage) {

        if (!IsDebuggingMode)
            return true;

        Date currTime = new Date();
        String fileDate = formatDate(currTime, "yyyy-MM-dd");
        String logDate = formatDate(currTime, "yyyy-MM-dd HH:mm:ss.SSS");

        try {
            // Create Directory
            String absolutePath = getFolderPath();

            String filePath = absolutePath + File.separator + String.format("%s.log", fileDate);
            Writer fileWriter = new FileWriter(filePath, true); //overwrites file
            BufferedWriter bf = new BufferedWriter(fileWriter);

            String lineData = String.format("%s\t%s", logDate, logMessage);

            bf.append(lineData);
            bf.newLine();

            bf.close();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }
}
