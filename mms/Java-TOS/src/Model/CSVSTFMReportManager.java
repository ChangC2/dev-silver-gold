package Model;

import Utils.DateTimeUtils;
import Utils.PreferenceManager;
import Utils.Utils;
import view.MainView;

import java.io.*;
import java.util.Calendar;

public class CSVSTFMReportManager  {

    boolean isRecording = false;

    // CSV Data
    Writer fileWriter;
    BufferedWriter bf;
    int currReqNo = 0;

    public synchronized String getFolderPath() {
        String toolDataFolderPath = PreferenceManager.getToolDataFilepath();

        return Utils.getAppDataFolder(toolDataFolderPath, "tooldatafiles");
    }

    public void init() {

        // Directory
        String hstFolderPath = getFolderPath();

        String csvHeader = "HP2,S1,S2,S3,T1,T2,T3,F1,F2,F3,M1,M2,M3,SeqNo";

        String fileName = String.format("ML-AI(%s).csv", DateTimeUtils.dateToString(Calendar.getInstance().getTime(), "yyyy-MM-dd HHmmss"));
        File file = new File(hstFolderPath, fileName);
        try {
            /*if (!file.exists()) {
                file.createNewFile();
            }*/

            isRecording = true;

            // Init CSV file
            String csvFilePath = file.getAbsolutePath();
            fileWriter = new FileWriter(file, false); // Overwrite complete file
            bf = new BufferedWriter(fileWriter);
            bf.append(csvHeader);
            bf.newLine();
            currReqNo = 0;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void finishWriting() {
        try {
            // Close csv record file
            if (bf != null) {
                bf.close();
            }

            if (fileWriter != null) {
                fileWriter.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        isRecording = false;
    }

    public void writeData() throws Exception {
        if (!isRecording) {
            // If not initialized, then ignore data
            return;
        }

        ++currReqNo;
        String csvLineData = String.format("%f,%s,%d", MainView.getInstance().getHP2(), MainView.getInstance().getModalData(), MainView.getInstance().getCurrentSequenceNumber());

        // Save CSV file -----------------------------------------------------------------------------------------------
        if (bf != null) {
            bf.append(csvLineData);
            bf.newLine();
        }
    }
}
