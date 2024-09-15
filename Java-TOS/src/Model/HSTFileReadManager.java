package Model;

import GLG.DataPoint;
import Utils.DateTimeUtils;
import com.google.common.io.LittleEndianDataInputStream;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class HSTFileReadManager extends HSTFileBaseManager {

    String filePath;
    String[] varNames;
    HSTHeader hstHeader;

    FileInputStream fis;
    LittleEndianDataInputStream stream;

    public int getNumsOfVar() {
        if (hstHeader == null) {
            return 0;
        }

        return hstHeader.numsOfVar;
    }

    public void init(String filePath) throws Exception {
        this.filePath = filePath;
        File file = new File(filePath);

        try {
            fis = new FileInputStream(file);
            stream = new LittleEndianDataInputStream(new BufferedInputStream(fis));

            // Read Header
            hstHeader = new HSTHeader();
            hstHeader.version = stream.readShort();
            hstHeader.dataOffset = stream.readShort();
            hstHeader.itemLength = stream.readShort();
            hstHeader.nameFieldLength = stream.readShort();
            stream.read(hstHeader.otherHeads);
            hstHeader.numsOfVar = stream.readShort();

            // Read Variable Name
            byte[] bytesVarNames = new byte[hstHeader.nameFieldLength];
            stream.read(bytesVarNames);
            String strVarname = new String(bytesVarNames, Charset.forName("UTF-8")).trim();
            strVarname = strVarname.replaceAll("\t", "");
            varNames = strVarname.split("\\s+");

            // Move cursor to read real data.
            stream.skipBytes(hstHeader.dataOffset - hstHeader.nameFieldLength - 70);  // 70 : size of HSTHeader
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw e;
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    public ArrayList<DataPoint> getPlotData() {
        if (stream == null || hstHeader == null)
            return null;

        try {
            // Check Available Data
            if (stream.available() >= 8 + hstHeader.numsOfVar * 8) {
                ArrayList<DataPoint> dataPoints = new ArrayList<>();

                byte[] dataBytes = new byte[8];

                // Read record time(Windows File Time) and convert it to unix time
                stream.read(dataBytes);
                long recordFileTime = toLong(dataBytes);
                long unixTimeStamp = DateTimeUtils.filetimeToMillis(recordFileTime);

                for (int i = 0; i < hstHeader.numsOfVar; i++) {
                    DataPoint dataPoint = new DataPoint();
                    dataPoint.time_stamp = unixTimeStamp / 1000.;
                    dataPoint.value_valid = true;

                    // Read item plot data
                    stream.read(dataBytes);
                    dataPoint.value = toDouble(dataBytes);

                    dataPoints.add(dataPoint);
                }

                return dataPoints;
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    public void finishReading() {
        try {
            if (stream != null) {
                stream.close();
            }

            if (fis != null) {
                fis.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
