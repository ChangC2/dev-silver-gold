package Model;

import Utils.DateTimeUtils;
import Utils.PreferenceManager;
import Utils.Utils;
import com.google.common.io.LittleEndianDataOutputStream;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class HSTFileSaveManager extends HSTFileBaseManager {

    String[] varNames;
    HSTHeader hstHeader;

    boolean isRecording = false;

    // HST Binary Data
    FileOutputStream fos;
    LittleEndianDataOutputStream stream;

    public synchronized String getFolderPath() {
        String toolDataFolderPath = PreferenceManager.getToolDataFilepath();

        return Utils.getAppDataFolder(toolDataFolderPath, "tooldatafiles");
    }

    public void init(String fileName, String[] vars) {
        this.varNames = vars;

        // Directory
        String hstFolderPath = getFolderPath();

        String variableNames = "";

        byte[] varSeparatorBytes = {0x09, 0x20, 0x09};
        String varSeparator = new String(varSeparatorBytes);

        for (int i = 0; i < varNames.length; i++) {
            varNames[i] = varNames[i].trim();
            variableNames += varNames[i] + varSeparator;
        }

        byte[] variableNamesBytes = variableNames.getBytes(StandardCharsets.UTF_8);

        hstHeader = new HSTHeader();
        hstHeader.dataOffset = (short) (0x46 + variableNamesBytes.length);
        hstHeader.numsOfVar = (short) varNames.length;
        hstHeader.nameFieldLength = (short) variableNamesBytes.length;

        File file = new File(hstFolderPath, fileName);
        try {
            fos = new FileOutputStream(file);
            stream = new LittleEndianDataOutputStream(new BufferedOutputStream(fos));

            // Write Headers
            stream.writeShort(hstHeader.version);
            stream.writeShort(hstHeader.dataOffset);
            stream.writeShort(hstHeader.itemLength);
            stream.writeShort(hstHeader.nameFieldLength);
            stream.write(hstHeader.otherHeads);
            stream.writeShort(hstHeader.numsOfVar);

            // Write Variable names
            stream.write(variableNamesBytes);
            stream.flush();

            isRecording = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void finishWriting() {
        try {
            // Close hst log file
            if (stream != null) {
                stream.close();
            }

            if (fos != null) {
                fos.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        isRecording = false;
    }

    public void writeData(ArrayList<Double> values) throws Exception {
        if (!isRecording) {
            // If not initialized, then ignore data
            return;
        }
        if (stream == null || hstHeader == null) {
            throw new Exception("HST File not initialized!");
        }
        if (hstHeader.numsOfVar != values.size()) {
            //throw new Exception("HST Data size mismatch!");
        }
        long currUnixTimeMils = System.currentTimeMillis();
        long recordFileTime = DateTimeUtils.millisToFiletime(currUnixTimeMils);

        // Put record File Time Stamp
        stream.write(toByteArray(recordFileTime));

        // Put Data
        for (int i = 0; i < values.size(); i++) {
            stream.write(toByteArray(values.get(i)));

        }
        stream.flush();
    }
}
