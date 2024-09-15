package Model;

import Utils.PreferenceManager;
import Utils.Utils;

import java.io.*;
import java.nio.file.FileSystems;
import java.util.ArrayList;

public class JobDataManager {

    private static JobDataManager instance;

    public static JobDataManager getInstance() {
        if (instance == null) {
            instance = new JobDataManager();
        }
        return instance;
    }

    int jobID;

    ArrayList<JobData> jobDataList = new ArrayList<>();

    public ArrayList<JobData> getJobDataList() {
        return jobDataList;
    }

    // Load Job Data from the File
    public boolean loadJobData(int newJobID) throws IOException {
        // Reset Data
        jobID = newJobID;
        jobDataList.clear();

        String absolutePath = FileSystems.getDefault()
                .getPath("tooldatafiles")
                .normalize()
                .toAbsolutePath()
                .toString();
        new File(absolutePath).mkdirs();
        String filePath = String.format("%s/%d.sly", absolutePath, newJobID);

        BufferedReader reader = new BufferedReader(new FileReader(new File(filePath)));
        String line;
        while ((line = reader.readLine()) != null) {
            // System.out.println(line);

            String[] values = line.split("\\|\\|\\|");
            if (values == null || values.length <= 22) {
                continue;
            }
            //System.out.println(values[0].getClass());
            //System.out.println(values[1].getClass());
            //System.out.println(values[2].getClass());

            // System.out.println(values[1] + "values[1]");
            // Parse Data
            JobData newItem = new JobData();
            newItem.tool = parseInt(values[0], 0);
            newItem.section = parseInt(values[1], 0);
            newItem.channel = parseInt(values[2], 0);
            newItem.comment = values[3].trim();
            newItem.target = parseFloat(values[4], 0);
            newItem.highLimit = parseFloat(values[5], 0);
            newItem.wearLimit = parseFloat(values[6], 0);
            newItem.idle = parseFloat(values[7], 0);
            newItem.lowLimitTime = parseFloat(values[8], 0);
            newItem.adaptiveEnable = parseInt(values[9], 0) > 0;
            newItem.macroInterruptEnable = parseInt(values[10], 0) > 0;
            newItem.leadInFeedrate = parseFloat(values[11], 0);
            newItem.leadInEnable = parseInt(values[12], 0) > 0;
            newItem.startDelay = parseFloat(values[13], 0);
            newItem.highLimitDelay = parseFloat(values[14], 0);
            newItem.wearLimitDelay = parseFloat(values[15], 0);
            newItem.adaptiveMin = parseFloat(values[16], 0);
            newItem.adaptiveMax = parseFloat(values[17], 0);
            newItem.adaptiveWearLimit = parseFloat(values[18], 0);
            newItem.adaptiveHighLimit = parseFloat(values[19], 0);
            newItem.filter = parseFloat(values[20], 0);
            newItem.sensorScaleSend = parseFloat(values[21], 0);
            newItem.monitorTime = parseFloat(values[22], 0);

            // Newly Added Item in V3.0
            if (values.length >= 24) {
                newItem.leadInTrigger = parseFloat(values[23], 0);
            }

            jobDataList.add(newItem);
        }

        reader.close();

        if (jobDataList.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    // Save Current Memory Job Data to the File
    public boolean saveJobData() throws IOException {
        if (jobDataList.isEmpty()) {
            throw new IOException("Job Data is Empty!");
        }

        // Directory
        String absolutePath = FileSystems.getDefault()
                .getPath("tooldatafiles")
                .normalize()
                .toAbsolutePath()
                .toString();
        new File(absolutePath).mkdirs();

        String filePath = absolutePath + File.separator + String.format("%d.sly", jobID);
        Writer fileWriter = new FileWriter(filePath, false); //overwrites file
        BufferedWriter bf = new BufferedWriter(fileWriter);

        for (JobData item : jobDataList) {

            String lineData = String.format("%d ||| %d ||| %d ||| %s ||| %f ||| %f ||| %f ||| %f ||| %f ||| %d ||| %d ||| %f ||| %d ||| %f ||| %f ||| %f ||| %f ||| %f ||| %f ||| %f ||| %f ||| %f ||| %f ||| %f",
                    item.tool, item.section, item.channel, item.comment, item.target, item.highLimit, item.wearLimit, item.idle, item.lowLimitTime, item.adaptiveEnable ? 1 : 0, item.macroInterruptEnable ? 1 : 0, item.leadInFeedrate, item.leadInEnable ? 1 : 0, item.startDelay, item.highLimitDelay, item.wearLimitDelay,
                    item.adaptiveMin, item.adaptiveMax, item.adaptiveWearLimit, item.adaptiveHighLimit, item.filter, item.sensorScaleSend, item.monitorTime, item.leadInTrigger);
            bf.append(lineData);
            bf.newLine();
        }

        bf.close();
        fileWriter.close();

        return true;
    }

    // Save Current Memory Job Data to the File
    public boolean saveJobData(String comment) throws IOException {
        if (jobDataList.isEmpty()) {
            throw new IOException("Job Data is Empty!");
        }

        // Directory
        String absolutePath = FileSystems.getDefault()
                .getPath("tooldatafiles")
                .normalize()
                .toAbsolutePath()
                .toString();
        new File(absolutePath).mkdirs();

        String filePath = absolutePath + File.separator + String.format("%d.sly", jobID);
        Writer fileWriter = new FileWriter(filePath, false); //overwrites file
        BufferedWriter bf = new BufferedWriter(fileWriter);

        for (JobData item : jobDataList) {

            String lineData = String.format("%d ||| %d ||| %d ||| %s ||| %f ||| %f ||| %f ||| %f ||| %f ||| %d ||| %d ||| %f ||| %d ||| %f ||| %f ||| %f ||| %f ||| %f ||| %f ||| %f ||| %f ||| %f ||| %f ||| %f ||| %d",
                    item.tool, item.section, item.channel, item.comment, item.target, item.highLimit, item.wearLimit, item.idle, item.lowLimitTime, item.adaptiveEnable ? 1 : 0, item.macroInterruptEnable ? 1 : 0, item.leadInFeedrate, item.leadInEnable ? 1 : 0, item.startDelay, item.highLimitDelay, item.wearLimitDelay,
                    item.adaptiveMin, item.adaptiveMax, item.adaptiveWearLimit, item.adaptiveHighLimit, item.filter, item.sensorScaleSend, item.monitorTime, item.leadInTrigger, item.wearLogicFeedrate ? 1 : 0);
            bf.append(lineData);
            bf.newLine();
        }

        bf.close();
        fileWriter.close();

        return true;
    }

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

    // Check Job Data File is exists or not
    public boolean isFileExists(int jobID) {

        // Create Directory
        String absolutePath = FileSystems.getDefault()
                .getPath("tooldatafiles")
                .normalize()
                .toAbsolutePath()
                .toString();
        new File(absolutePath).mkdirs();

        String filePath = absolutePath + File.separator + String.format("%d.sly", jobID);
        File idea = new File(filePath);
        return idea.exists();
    }

    // Create new JOB Data
    public boolean createJobData(int newJobID, JobData newJobData) throws IOException {
        if (newJobID == 0) {
            return false;
        }
        try {
            // Create Directory
            String absolutePath = FileSystems.getDefault()
                    .getPath("tooldatafiles")
                    .normalize()
                    .toAbsolutePath()
                    .toString();
            new File(absolutePath).mkdirs();

            String filePath = absolutePath + File.separator + String.format("%d.sly", newJobID);
            Writer fileWriter = new FileWriter(filePath, true); //overwrites file
            BufferedWriter bf = new BufferedWriter(fileWriter);

            String lineData = String.format("%d ||| %d ||| %d ||| %s ||| %f ||| %f ||| %f ||| %f ||| %f ||| %d ||| %d ||| %f ||| %d ||| %f ||| %f ||| %f ||| %f ||| %f ||| %f ||| %f ||| %f ||| %f ||| %f ||| %f ||| %d",
                    newJobData.tool, newJobData.section, newJobData.channel, newJobData.comment, newJobData.target, newJobData.highLimit, newJobData.wearLimit, newJobData.idle, newJobData.lowLimitTime, newJobData.adaptiveEnable ? 1 : 0, newJobData.macroInterruptEnable ? 1 : 0, newJobData.leadInFeedrate, newJobData.leadInEnable ? 1 : 0, newJobData.startDelay, newJobData.highLimitDelay, newJobData.wearLimitDelay,
                    newJobData.adaptiveMin, newJobData.adaptiveMax, newJobData.adaptiveWearLimit, newJobData.adaptiveHighLimit, newJobData.filter, newJobData.sensorScaleSend, newJobData.monitorTime, newJobData.leadInTrigger, newJobData.wearLogicFeedrate ? 1 : 0);
            bf.append(lineData);
            bf.newLine();

            bf.close();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }

        // Add new Job data
        this.jobID = newJobID;
        this.jobDataList.clear();
        this.jobDataList.add(newJobData);

        return true;
    }

    // ---------------------------------------------------------------------------------------------------
    // New Functions Set for managing the job data individually
    // ---------------------------------------------------------------------------------------------------

    public synchronized String getFolderPath() {
        String toolDataFolderPath = PreferenceManager.getToolDataFilepath();

        return Utils.getAppDataFolder(toolDataFolderPath, "tooldatafiles");
    }

    // Load Job Data List From the File with the same program number
    public synchronized ArrayList<JobData> getJobDataList(int newJobID) {

        ArrayList<JobData> dataList = new ArrayList<JobData>();

        String absolutePath = getFolderPath();
        String filePath = String.format("%s/%d.sly", absolutePath, newJobID);

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(new File(filePath)));
            String line;
            while ((line = reader.readLine()) != null) {
                // System.out.println(line);

                String[] values = line.split("\\|\\|\\|");
                if (values == null || values.length <= 22) {
                    continue;
                }
                //System.out.println(values[0].getClass());
                //System.out.println(values[1].getClass());
                //System.out.println(values[2].getClass());

                // System.out.println(values[1] + "values[1]");
                // Parse Data
                JobData newItem = new JobData();
                newItem.tool = parseInt(values[0], 0);
                newItem.section = parseInt(values[1], 0);
                newItem.channel = parseInt(values[2], 0);
                newItem.comment = values[3].trim();
                newItem.target = parseFloat(values[4], 0);
                newItem.highLimit = parseFloat(values[5], 0);
                newItem.wearLimit = parseFloat(values[6], 0);
                newItem.idle = parseFloat(values[7], 0);
                newItem.lowLimitTime = parseFloat(values[8], 0);
                newItem.adaptiveEnable = parseInt(values[9], 0) > 0;
                newItem.macroInterruptEnable = parseInt(values[10], 0) > 0;
                newItem.leadInFeedrate = parseFloat(values[11], 0);
                newItem.leadInEnable = parseInt(values[12], 0) > 0;
                newItem.startDelay = parseFloat(values[13], 0);
                newItem.highLimitDelay = parseFloat(values[14], 0);
                newItem.wearLimitDelay = parseFloat(values[15], 0);
                newItem.adaptiveMin = parseFloat(values[16], 0);
                newItem.adaptiveMax = parseFloat(values[17], 0);
                newItem.adaptiveWearLimit = parseFloat(values[18], 0);
                newItem.adaptiveHighLimit = parseFloat(values[19], 0);
                newItem.filter = parseFloat(values[20], 0);
                newItem.sensorScaleSend = parseFloat(values[21], 0);
                newItem.monitorTime = parseFloat(values[22], 0);

                // Newly Added Item in V3.0
                if (values.length >= 24) {
                    newItem.leadInTrigger = parseFloat(values[23], 0);
                }

                // Newly Added Item in V3.65
                if (values.length >= 25) {
                    newItem.wearLogicFeedrate = parseInt(values[24], 0) > 0;
                }

                dataList.add(newItem);
            }

            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return dataList;
    }

    // Load Job Data from the File with the certain prog and tool, section and channel
    public synchronized JobData getJobData(int newJobID, int toolID, int sectionID, int channelID) {

        String absolutePath = getFolderPath();
        String filePath = String.format("%s/%d.sly", absolutePath, newJobID);

        JobData findData = null;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File(filePath)));
            String line;

            while ((line = reader.readLine()) != null) {
                // System.out.println(line);

                String[] values = line.split("\\|\\|\\|");
                if (values == null || values.length <= 22) {
                    continue;
                }

                try {
                    if (Integer.parseInt(values[0].trim()) == toolID &&
                            Integer.parseInt(values[1].trim()) == sectionID &&
                            Integer.parseInt(values[2].trim()) == channelID) {
                        // Parse Data
                        JobData newItem = new JobData();
                        newItem.tool = parseInt(values[0], 0);
                        newItem.section = parseInt(values[1], 0);
                        newItem.channel = parseInt(values[2], 0);
                        newItem.comment = values[3].trim();
                        newItem.target = parseFloat(values[4], 0);
                        newItem.highLimit = parseFloat(values[5], 0);
                        newItem.wearLimit = parseFloat(values[6], 0);
                        newItem.idle = parseFloat(values[7], 0);
                        newItem.lowLimitTime = parseFloat(values[8], 0);
                        newItem.adaptiveEnable = parseInt(values[9], 0) > 0;
                        newItem.macroInterruptEnable = parseInt(values[10], 0) > 0;
                        newItem.leadInFeedrate = parseFloat(values[11], 0);
                        newItem.leadInEnable = parseInt(values[12], 0) > 0;
                        newItem.startDelay = parseFloat(values[13], 0);
                        newItem.highLimitDelay = parseFloat(values[14], 0);
                        newItem.wearLimitDelay = parseFloat(values[15], 0);
                        newItem.adaptiveMin = parseFloat(values[16], 0);
                        newItem.adaptiveMax = parseFloat(values[17], 0);
                        newItem.adaptiveWearLimit = parseFloat(values[18], 0);
                        newItem.adaptiveHighLimit = parseFloat(values[19], 0);
                        newItem.filter = parseFloat(values[20], 0);
                        newItem.sensorScaleSend = parseFloat(values[21], 0);
                        newItem.monitorTime = parseFloat(values[22], 0);

                        if (values.length >= 24) {
                            newItem.leadInTrigger = parseFloat(values[23], 0);
                        }

                        // Newly Added Item in V3.65
                        if (values.length >= 25) {
                            newItem.wearLogicFeedrate = parseInt(values[24], 0) > 0;
                        }

                        findData = newItem;
                        break;
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }

            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return findData;
    }

    // Create new JOB Data
    public synchronized boolean createNewJobData(int newJobID, JobData item) {
        if (newJobID == 0) {
            return false;
        }
        try {
            // Create Directory
            String absolutePath = getFolderPath();

            String filePath = absolutePath + File.separator + String.format("%d.sly", newJobID);
            Writer fileWriter = new FileWriter(filePath, true); //overwrites file
            BufferedWriter bf = new BufferedWriter(fileWriter);

            String lineData = String.format("%d ||| %d ||| %d ||| %s ||| %f ||| %f ||| %f ||| %f ||| %f ||| %d ||| %d ||| %f ||| %d ||| %f ||| %f ||| %f ||| %f ||| %f ||| %f ||| %f ||| %f ||| %f ||| %f ||| %f ||| %d",
                    item.tool, item.section, item.channel, item.comment, item.target, item.highLimit, item.wearLimit, item.idle, item.lowLimitTime, item.adaptiveEnable ? 1 : 0, item.macroInterruptEnable ? 1 : 0, item.leadInFeedrate, item.leadInEnable ? 1 : 0, item.startDelay, item.highLimitDelay, item.wearLimitDelay,
                    item.adaptiveMin, item.adaptiveMax, item.adaptiveWearLimit, item.adaptiveHighLimit, item.filter, item.sensorScaleSend, item.monitorTime, item.leadInTrigger, item.wearLogicFeedrate ? 1 : 0);

            bf.append(lineData);
            bf.newLine();

            bf.close();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    // Save Current Memory Job Data to the File
    public synchronized boolean saveJobData(int progNum, ArrayList<JobData> dataList) {
        if (dataList.isEmpty()) {
            return false;
        }

        // Directory
        String absolutePath = getFolderPath();

        String filePath = absolutePath + File.separator + String.format("%d.sly", progNum);
        try {
            //overwrites file
            Writer fileWriter = new FileWriter(filePath, false); // Overwrite complete file
            BufferedWriter bf = new BufferedWriter(fileWriter);

            for (JobData item : dataList) {

                String lineData = String.format("%d ||| %d ||| %d ||| %s ||| %f ||| %f ||| %f ||| %f ||| %f ||| %d ||| %d ||| %f ||| %d ||| %f ||| %f ||| %f ||| %f ||| %f ||| %f ||| %f ||| %f ||| %f ||| %f ||| %f ||| %d",
                        item.tool, item.section, item.channel, item.comment, item.target, item.highLimit, item.wearLimit, item.idle, item.lowLimitTime, item.adaptiveEnable ? 1 : 0, item.macroInterruptEnable ? 1 : 0, item.leadInFeedrate, item.leadInEnable ? 1 : 0, item.startDelay, item.highLimitDelay, item.wearLimitDelay,
                        item.adaptiveMin, item.adaptiveMax, item.adaptiveWearLimit, item.adaptiveHighLimit, item.filter, item.sensorScaleSend, item.monitorTime, item.leadInTrigger, item.wearLogicFeedrate ? 1 : 0);
                bf.append(lineData);
                bf.newLine();
            }

            bf.close();
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }

    public synchronized boolean updateToolDataWithCurrent(int jobID, JobData newJobData) {

        ArrayList<JobData> dataList = new ArrayList<JobData>();

        String absolutePath = getFolderPath();
        String filePath = String.format("%s/%d.sly", absolutePath, jobID);

        // Find original Item
        boolean isNewItem = true;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File(filePath)));
            String line;
            while ((line = reader.readLine()) != null) {
                // System.out.println(line);

                String[] values = line.split("\\|\\|\\|");
                if (values == null || values.length <= 22) {
                    continue;
                }

                // Parse Data
                JobData currItem = new JobData();
                currItem.tool = parseInt(values[0], 0);
                currItem.section = parseInt(values[1], 0);
                currItem.channel = parseInt(values[2], 0);
                currItem.comment = values[3].trim();
                currItem.target = parseFloat(values[4], 0);
                currItem.highLimit = parseFloat(values[5], 0);
                currItem.wearLimit = parseFloat(values[6], 0);
                currItem.idle = parseFloat(values[7], 0);
                currItem.lowLimitTime = parseFloat(values[8], 0);
                currItem.adaptiveEnable = parseInt(values[9], 0) > 0;
                currItem.macroInterruptEnable = parseInt(values[10], 0) > 0;
                currItem.leadInFeedrate = parseFloat(values[11], 0);
                currItem.leadInEnable = parseInt(values[12], 0) > 0;
                currItem.startDelay = parseFloat(values[13], 0);
                currItem.highLimitDelay = parseFloat(values[14], 0);
                currItem.wearLimitDelay = parseFloat(values[15], 0);
                currItem.adaptiveMin = parseFloat(values[16], 0);
                currItem.adaptiveMax = parseFloat(values[17], 0);
                currItem.adaptiveWearLimit = parseFloat(values[18], 0);
                currItem.adaptiveHighLimit = parseFloat(values[19], 0);
                currItem.filter = parseFloat(values[20], 0);
                currItem.sensorScaleSend = parseFloat(values[21], 0);
                currItem.monitorTime = parseFloat(values[22], 0);

                // Newly Added Item in V3.0
                if (values.length >= 24) {
                    currItem.leadInTrigger = parseFloat(values[23], 0);
                }

                // Newly Added Item in V3.65
                if (values.length >= 25) {
                    currItem.wearLogicFeedrate = parseInt(values[24], 0) > 0;
                }

                if (currItem.tool == newJobData.tool && currItem.section == newJobData.section && currItem.channel == newJobData.channel) {
                    isNewItem = false;
                    dataList.add(newJobData);
                } else {
                    dataList.add(currItem);
                }
            }
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // If no exists, then add new record
        if (isNewItem) {
            dataList.add(newJobData);
        }

        return saveJobData(jobID, dataList);
    }
}
