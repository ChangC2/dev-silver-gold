package Haas;

import Utils.PreferenceManager;
import org.apache.http.util.TextUtils;

import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EthernetHaasMonitor {
    private static final int HEARTBEAT = 500;
    public String serverIP;
    public int port;

    EthernetHaas ethernetHaas;
    Timer dataTimer;

    public String mAvail = "";
    public String mMode = "";
    public String mZeroRet = "";
    public String mExecution = "";
    public String mEstop = "";
    public String mProgram = "";
    public String mPartCount = "";

    public String mXact = "";
    public String mYact = "";
    public String mZact = "";
    public String mSpindleSpeed = "";

    private int monitor;
    private int tool;
    private int section;
    private int channel;
    private int inCycle;

    public EthernetHaasMonitor(String _serverIP, int _port) {
        serverIP = _serverIP;
        port = _port;

        ethernetHaas = new EthernetHaas(serverIP, port);
    }

    public boolean isSameDeviceInfo(String newServerIP, int newPort) {
        if (serverIP.equals(newServerIP) && port == newPort) {
            return true;
        } else {
            return false;
        }
    }

    public void setNewDeviceInfo(String newServerIP, int newPort) {
        stop();
        ethernetHaas.setServerIP(newServerIP);
        ethernetHaas.setPort(newPort);
        start();
    }

    public boolean isConnected() {
        if (ethernetHaas != null) {
            return ethernetHaas.isConnected();
        } else {
            return false;
        }
    }

    public void start() {
        if (ethernetHaas != null) {
            ethernetHaas.connect();
        }

        dataTimer = new Timer();
        dataTimer.scheduleAtFixedRate(dataReadTask, 0, HEARTBEAT);
    }

    TimerTask dataReadTask = new TimerTask() {
        @Override
        public void run() {
            //process100();
            //process104();
            ProcessQ500();
            //ProcessQ600();
            ProcessQ600X();
        }
    };

    public void stop() {
        if (ethernetHaas != null) {
            ethernetHaas.close();
        }

        if (dataTimer != null) {
            dataTimer.cancel();
        }
    }

    /*
    Q100 – Machine Serial Number
    Q101 – Control Software Version
    Q102 – Machine Model Number
    Q104 – Mode (LIST PROG, MDI, MEM, JOG, etc.)
    Q200 – Tool Changes (total)
    Q201 – Tool Number in use
    Q300 – Power-on Time (total)
    Q301 – Motion Time (total)
    Q303 – Last Cycle Time
    Q304 – Previous Cycle Time
    Q400 – not currently used
    Q401 – not currently used
    Q402 – M30 Parts Counter #1 (resettable at control)
    Q403 – M30 Parts Counter #2 (resettable at control)
    Q500 – Three-in-one (PROGRAM, Oxxxxx, STATUS, PARTS, xxxxx)
    Q600 Macro or system variable
     */

    //"SERIAL NUMBER, 1234567";
    private void process100() {
        String response = ethernetHaas.sendCommand("?Q100");
        if (!TextUtils.isEmpty(response)) {
            processAvailability(response);
        }
    }

    private void processAvailability(String response) {
        Pattern pattern = Pattern.compile("^>SERIAL NUMBER, (.*)$");
        Matcher matcher = pattern.matcher(response);

        if (matcher.matches()) {
            mAvail = "AVAILABLE";
        } else {
            mAvail = "UNAVAILABLE";
        }
    }

    //Q104 – Mode (LIST PROG, MDI, MEM, JOG, etc.)
    private void process104() {
        String response = ethernetHaas.sendCommand("?Q104");
        if (!TextUtils.isEmpty(response)) {
            processControllerMode(response);
            processZeroReturn(response);
        }
    }

    private void processControllerMode(String response) {
        Pattern pattern = Pattern.compile("^>MODE, (.*)$");
        Matcher match = pattern.matcher(response);
        if (match.matches() && match.groupCount() >= 1) {
            String val = match.group(1);
            switch (val) {
                case "(MDI)": mMode = "MANUAL_DATA_INPUT"; break;
                case "(JOG)": mMode = "MANUAL"; break;
                case "(ZERO RET)": mMode = "MANUAL"; break;
                default: mMode = "AUTOMATIC"; break;
            }
        }
    }

    private void processZeroReturn(String response) {
        Pattern pattern = Pattern.compile("^>MODE, (.*)$");
        Matcher match = pattern.matcher(response);
        if (match.matches() && match.groupCount() >= 1) {
            String val = match.group(1);
            switch (val) {
                case "(ZERO RET)": mZeroRet = "NO ZERO X"; break;
                default: mZeroRet = "Level.Info"; break;
            }
        }
    }

    // Q500 – Three-in-one (PROGRAM, Oxxxxx, STATUS, PARTS, xxxxx)
    private void ProcessQ500() {
        String response = ethernetHaas.sendCommand("?Q500");
        if (!TextUtils.isEmpty(response)) {
            //log.Info("Q500 : " + response);

            ProcessExecution(response);
            ProcessEmergencyStop(response);
            ProcessProgramName(response);
            ProcessPartCount(response);
        }
    }

    private void ProcessExecution(String response) {

        Pattern pattern = Pattern.compile("^>PROGRAM, .*, (.*), PARTS, [0-9]*$");
        Matcher match = pattern.matcher(response);

        if (match.matches() && match.groupCount() >= 1) {

            String val = match.group(1);
            if (val.equals("IDLE"))  {
                mExecution = "READY";
            } else if (val.equals("FEED HOLD")) {
                mExecution = "INTERRUPTED";
            } else if (val.equals("ALARM ON")) {
                mExecution = "STOPPED";
            }
        }

        pattern = Pattern.compile("^>STATUS (.*)$");
        match = pattern.matcher(response);

        if (match.matches() && match.groupCount() >= 1) {
            String val = match.group(1);
            if (val.equals("BUSY")) {
                mExecution = "ACTIVE";
                inCycle = 1;
            } else {
                inCycle = 0;
            }
        } else {
            inCycle = 0;
        }
    }

    private void ProcessEmergencyStop(String response) {
        Pattern pattern = Pattern.compile("^>PROGRAM, .*, (.*), PARTS, [0-9]*$");
        Matcher match = pattern.matcher(response);
        if (match.matches() && match.groupCount() >= 1) {
            String val = match.group(1);
            if (val.equals("ALARM ON")) {
                mEstop = "TRIGGERED";
            } else {
                mEstop = "ARMED";
            }
        } else {
            mEstop = "ARMED";
        }
    }

    private void ProcessProgramName(String response) {
        Pattern pattern = Pattern.compile("^>PROGRAM, (.*), .*, PARTS, [0-9]*$");
        Matcher match = pattern.matcher(response);
        if (match.matches() && match.groupCount() >= 1) {
            String val = match.group(1);
            if (val.equals("MDI")) mProgram = "";
            else mProgram = val;
        }
    }

    private void ProcessPartCount(String response) {
        Pattern pattern = Pattern.compile("^>PROGRAM, .*, .*, PARTS, ([0-9]*)$");
        Matcher match = pattern.matcher(response);
        if (match.matches() && match.groupCount() >= 1) {
            mPartCount = match.group(1);
        }
    }

    // Q600 Macro or system variable
    private void ProcessQ600() {
        ProcessAxisActualPositions();
        ProcessSpindle();
    }

    private void ProcessAxisActualPositions() {
        ProcessAxisActualPosition_X();
        ProcessAxisActualPosition_Y();
        ProcessAxisActualPosition_Z();
    }

    private String GetVariable(int variable) {
        String response = ethernetHaas.sendCommand("?Q600 " + variable);
        if (!TextUtils.isEmpty(response)) {
            //log.Info("Q600 : " + variable.ToString() + " : " + response);
            System.out.println("Q600 : " + String.valueOf(variable) + " : " + response);

            Pattern pattern = Pattern.compile("^>MACRO, (.*)$");
            Matcher match = pattern.matcher(response);
            if (match.matches() && match.groupCount() >= 1) {
                return match.group(1);
            }
        }

        return null;
    }

    private void ProcessAxisActualPosition_X() {
        String s = GetVariable(5041);
        if (s != null) {
            mXact = s;
        }
    }

    private void ProcessAxisActualPosition_Y() {
        String s = GetVariable(5042);
        if (s != null) {
            mYact = s;
        }
    }

    private void ProcessAxisActualPosition_Z() {
        String s = GetVariable(5043);
        if (s != null) {
            mZact = s;
        }
    }

    private void ProcessSpindle() {
        ProcessSpindle_Speed();
    }

    private void ProcessSpindle_Speed() {
        String s = GetVariable(3027);
        if (!TextUtils.isEmpty(s)) {
            mSpindleSpeed = s;
        }
    }

    private void ProcessQ600X() {
        /*for (int i = 1; i <= 10; i++) {
            String s = GetVariable(i);
            System.out.println(s);
        }*/
        int addrMonitor = PreferenceManager.getHaasAddrMonitor();
        int addrTool = PreferenceManager.getHaasAddrTool();
        int addrSection = PreferenceManager.getHaasAddrSection();
        int addrChannel = PreferenceManager.getHaasAddrChannel();

        monitor = getIntValue(GetVariable(addrMonitor));
        tool = getIntValue(GetVariable(addrTool));
        section = getIntValue(GetVariable(addrSection));
        channel = getIntValue(GetVariable(addrChannel));
    }

    private int getIntValue(String strVal) {
        try {
            return (int) Float.parseFloat(strVal);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int getMonitor() { return monitor; }
    public int getTool() { return tool; }
    public int getSection() { return section; }
    public int getChannel() { return channel; }
    public int getInCycle() { return inCycle;}
}
