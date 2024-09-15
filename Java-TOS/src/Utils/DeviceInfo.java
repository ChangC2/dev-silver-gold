package Utils;

import java.io.*;
import java.net.*;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Scanner;

public class DeviceInfo {

    private static String sn = null;

    public static String getSerialNumber() {

        if (sn != null) {
            return sn;
        }

        String OS = System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH);
        if ((OS.indexOf("mac") >= 0) || (OS.indexOf("darwin") >= 0)) {
            return getSerialNumber4Mac();
        } else if (OS.indexOf("win") >= 0) {
            return getSerialNumber4Win();
        } else if (OS.indexOf("nux") >= 0) {
            return getSerialNumber4Linux();
        } else {
            return "Other";
        }
    }

    // For Windows
    private static final String getSerialNumber4Win() {

        if (sn != null) {
            return sn;
        }

        OutputStream os = null;
        InputStream is = null;

        Runtime runtime = Runtime.getRuntime();
        Process process = null;
        try {
            process = runtime.exec(new String[]{"wmic", "bios", "get", "serialnumber"});
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        os = process.getOutputStream();
        is = process.getInputStream();

        try {
            os.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Scanner sc = new Scanner(is);
        try {
            while (sc.hasNext()) {
                String next = sc.next();
                if ("SerialNumber".equals(next)) {
                    sn = sc.next().trim();
                    break;
                }
            }
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        if (sn == null) {
            throw new RuntimeException("Cannot find computer SN");
        }

        return sn;
    }

    // For Mac
    private static final String getSerialNumber4Mac() {

        if (sn != null) {
            return sn;
        }

        OutputStream os = null;
        InputStream is = null;

        Runtime runtime = Runtime.getRuntime();
        Process process = null;
        try {
            process = runtime.exec(new String[]{"/usr/sbin/system_profiler", "SPHardwareDataType"});
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        os = process.getOutputStream();
        is = process.getInputStream();

        try {
            os.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String line = null;
        String marker = "Serial Number";
        try {
            while ((line = br.readLine()) != null) {
                if (line.contains(marker)) {
                    sn = line.split(":")[1].trim();
                    break;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        if (sn == null) {
            throw new RuntimeException("Cannot find computer SN");
        }

        return sn;
    }

    // For Linux
    private static final String getSerialNumber4Linux() {

        if (sn == null) {
            readDmidecode();
        }
        if (sn == null) {
            readLshal();
        }
        if (sn == null) {
            throw new RuntimeException("Cannot find computer SN");
        }

        return sn;
    }

    private static BufferedReader read(String command) {

        OutputStream os = null;
        InputStream is = null;

        Runtime runtime = Runtime.getRuntime();
        Process process = null;
        try {
            process = runtime.exec(command.split(" "));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        os = process.getOutputStream();
        is = process.getInputStream();

        try {
            os.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return new BufferedReader(new InputStreamReader(is));
    }

    private static void readDmidecode() {

        String line = null;
        String marker = "Serial Number:";
        BufferedReader br = null;

        try {
            br = read("dmidecode -t system");
            while ((line = br.readLine()) != null) {
                if (line.indexOf(marker) != -1) {
                    sn = line.split(marker)[1].trim();
                    break;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private static void readLshal() {

        String line = null;
        String marker = "system.hardware.serial =";
        BufferedReader br = null;

        try {
            br = read("lshal");
            while ((line = br.readLine()) != null) {
                if (line.indexOf(marker) != -1) {
                    sn = line.split(marker)[1].replaceAll("\\(string\\)|(\\')", "").trim();
                    break;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public static String getMotherboardNumber() {

        String OS = System.getProperty("os.name", "generic").toLowerCase(Locale.ENGLISH);
        if (OS.indexOf("nux") >= 0) {
            return getLinuxMotherBoardSerialNumber();
        } else {
            return getWindowsMotherBoardSerialNumber();
        }
    }

    private static String getLinuxMotherBoardSerialNumber() {

        // command to be executed on the terminal
        String command = "sudo dmidecode -s baseboard-serial-number";

        // variable to store the Serial Number
        String serialNumber = null;

        // try block
        try {

            // declaring the process to run the command
            Process SerialNumberProcess
                    = Runtime.getRuntime().exec(command);

            // getting the input stream using
            // InputStreamReader using Serial Number Process
            InputStreamReader ISR = new InputStreamReader(
                    SerialNumberProcess.getInputStream());

            // declaring the Buffered Reader
            BufferedReader br = new BufferedReader(ISR);

            // reading the serial number using
            // Buffered Reader
            serialNumber = br.readLine().trim();

            // waiting for the system to return
            // the serial number
            SerialNumberProcess.waitFor();

            // closing the Buffered Reader
            br.close();
        }

        // catch block
        catch (Exception e) {

            // printing the exception
            e.printStackTrace();

            // giving the serial number the value null
            serialNumber = null;
        }

        // returning the serial number
        return serialNumber;
    }

    private static String getWindowsMotherBoardSerialNumber() {

        // command to be executed on the terminal
        String command = "wmic baseboard get serialnumber";

        // variable to store the Serial Number
        String serialNumber = null;

        // try block
        try {
            // declaring the process to run the command
            Process SerialNumberProcess
                    = Runtime.getRuntime().exec(command);

            // getting the input stream using
            // InputStreamReader using Serial Number Process
            InputStreamReader ISR = new InputStreamReader(
                    SerialNumberProcess.getInputStream());

            // declaring the Buffered Reader
            BufferedReader br = new BufferedReader(ISR);


            // reading the serial number using
            // Buffered Reader
            String line;
            while ((line = br.readLine()) != null) {
                serialNumber += line.trim();
            }
            serialNumber = serialNumber.replace("SerialNumber", "");

            // waiting for the system to return
            // the serial number
            SerialNumberProcess.waitFor();

            // closing the Buffered Reader
            br.close();
        }
        // catch block
        catch (Exception e) {

            // printing the exception
            e.printStackTrace();

            // giving the serial number the value null
            serialNumber = "PGPAZ015J960BC";//null;
        }

        // returning the serial number
        return serialNumber;
    }

    public static String getSerialNumber(String drive) {
        String result = "";
        try {
            File file = File.createTempFile("realhowto", ".vbs");
            file.deleteOnExit();
            FileWriter fw = new java.io.FileWriter(file);

            String vbs = "Set objFSO = CreateObject(\"Scripting.FileSystemObject\")\n"
                    + "Set colDrives = objFSO.Drives\n"
                    + "Set objDrive = colDrives.item(\"" + drive + "\")\n"
                    + "Wscript.Echo objDrive.SerialNumber";  // see note
            fw.write(vbs);
            fw.close();
            Process p = Runtime.getRuntime().exec("cscript //NoLogo " + file.getPath());
            BufferedReader input =
                    new BufferedReader
                            (new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = input.readLine()) != null) {
                result += line;
            }
            input.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.trim();
    }

    public static String getMacAddress() {
        String macAddress = "";
        String addressType = "mac";
        try {

            InetAddress lanIp = null;

            String ipAddress = null;
            Enumeration<NetworkInterface> net = null;
            net = NetworkInterface.getNetworkInterfaces();

            if (net != null) {
                while (net.hasMoreElements()) {
                    NetworkInterface element = net.nextElement();
                    Enumeration<InetAddress> addresses = element.getInetAddresses();

                    while ((addresses != null && addresses.hasMoreElements()) &&
                            (element.getHardwareAddress() != null && element.getHardwareAddress().length > 0) &&
                            !isVMMac(element.getHardwareAddress())) {
                        InetAddress ip = addresses.nextElement();
                        if (ip instanceof Inet4Address) {

                            if (ip.isSiteLocalAddress()) {
                                ipAddress = ip.getHostAddress();
                                lanIp = InetAddress.getByName(ipAddress);
                            }
                        }
                    }
                }
            }


            if (lanIp == null) {
                macAddress = "001A3FF14CC6";
                return macAddress;
            }

            if (addressType.equals("ip")) {
                // IP Address
                macAddress = lanIp.toString().replaceAll("^/+", "");

            } else if (addressType.equals("mac")) {
                // Mac Address
                macAddress = getMacAddress(lanIp);

            }
        } catch (UnknownHostException | SocketException e) {
            e.printStackTrace();
            macAddress = "001A3FF14CC6";
        }

        return macAddress;
    }

    private static boolean isVMMac(byte[] mac) {
        if(null == mac) return false;
        byte invalidMacs[][] = {
                {0x00, 0x05, 0x69},             //VMWare
                {0x00, 0x1C, 0x14},             //VMWare
                {0x00, 0x0C, 0x29},             //VMWare
                {0x00, 0x50, 0x56},             //VMWare
                {0x08, 0x00, 0x27},             //Virtualbox
                {0x0A, 0x00, 0x27},             //Virtualbox
                {0x00, 0x03, (byte)0xFF},       //Virtual-PC
                {0x00, 0x15, 0x5D}              //Hyper-V
        };

        for (byte[] invalid: invalidMacs){
            if (invalid[0] == mac[0] && invalid[1] == mac[1] && invalid[2] == mac[2]) return true;
        }

        return false;
    }

    private static String getMacAddress(InetAddress ip) {
        String address = null;
        try {

            NetworkInterface network = NetworkInterface.getByInetAddress(ip);
            byte[] mac = network.getHardwareAddress();

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < mac.length; i++) {
                sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "" : ""));
            }
            address = sb.toString();

        } catch (SocketException ex) {

            ex.printStackTrace();

        }

        return address;
    }
}
