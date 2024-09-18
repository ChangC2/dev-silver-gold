package Haas;

import org.apache.http.util.TextUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class EthernetHaas {

    public String serverIP;
    public int port;
    Socket socket;
    InputStream inputStream;
    OutputStream outputStream;

    public EthernetHaas(String _serverIP, int _port) {
        serverIP = _serverIP;
        port = _port;
    }

    public void setServerIP(String serverIP) { this.serverIP = serverIP; }
    public String getServerIP() { return serverIP; }

    // Check New connection info is same connection or not
    public boolean isSameConnectionInfo(String newIP, int newPort) {
        if (serverIP.equals(newIP) && port == newPort) {
            return true;
        }

        return false;
    }

    public void setPort(int port) { this.port = port; }
    public int getPort() { return port; }

    public void connect() {

        close();
        try {
            socket = new Socket(serverIP, port);
            outputStream = socket.getOutputStream();
            inputStream = socket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isConnected() {
        if (socket == null || socket.isClosed() || !socket.isConnected()) {
            return false;
        }

        return true;
    }

    public void close() {
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String sendCommand(String cmd) {

        String result = null;

        if (!isConnected()) {
            connect();
        }

        if (isConnected()) {
            String newCommand = cmd + "\r\n";
            byte[] data = newCommand.getBytes(StandardCharsets.US_ASCII);

            try {
                outputStream.write(data);
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                data = new byte[2048];
                int length = inputStream.read(data, 0, data.length);
                String responseString = new String(data, 0, length, StandardCharsets.US_ASCII);

                if (!TextUtils.isEmpty(responseString)) {
                    responseString = responseString.trim();
                    responseString = trimEnd(responseString, ">");
                    responseString = responseString.trim();
                }

                result = responseString;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    public static String trimStart(String text, String trimBy) {
        int beginIndex = 0;
        int endIndex = text.length();

        while (text.substring(beginIndex, endIndex).startsWith(trimBy)) {
            beginIndex += trimBy.length();
        }

        return text.substring(beginIndex, endIndex);
    }

    public static String trimEnd(String text, String trimBy) {
        int beginIndex = 0;
        int endIndex = text.length();

        while (text.substring(beginIndex, endIndex).endsWith(trimBy)) {
            endIndex -= trimBy.length();
        }

        return text.substring(beginIndex, endIndex);
    }

    public static String trim(String text, String trimBy) {
        int beginIndex = 0;
        int endIndex = text.length();

        while (text.substring(beginIndex, endIndex).startsWith(trimBy)) {
            beginIndex += trimBy.length();
        }

        while (text.substring(beginIndex, endIndex).endsWith(trimBy)) {
            endIndex -= trimBy.length();
        }

        return text.substring(beginIndex, endIndex);
    }

}
