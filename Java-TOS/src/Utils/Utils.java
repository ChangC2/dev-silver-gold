package Utils;

import javax.net.ssl.*;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.cert.X509Certificate;
import java.util.regex.Pattern;

public class Utils {

    public static boolean isValidIP(String ip) {
        if (ip == null || ip.trim().isEmpty()) {
            return false;
        }
        String ipString = ip.trim();
        Pattern PATTERN = Pattern.compile(
                "^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");
        return PATTERN.matcher(ipString).matches();
    }

    public static boolean isValidPort(int port) {
        if (port > 0 && port < 65536) {
            return true;
        } else {
            return false;
        }
    }

    public static String getSystemDownloadPath(String fileName) {
        Path downloadPath = Paths.get(System.getProperty("user.home"), "Downloads", fileName);
        String downloadFilePath = downloadPath.toString();
        System.out.println(downloadFilePath);
        return downloadFilePath;
    }

    public static boolean isValidEmail(String email) {
        Pattern emailPattern = Pattern.compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+");
        return emailPattern.matcher(email).matches();
    }

    public static String getEscapedUriString(String urlString) {
        String newUrlString = urlString;
        try {
            URL url = new URL(urlString);
            URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
            newUrlString = uri.toASCIIString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return newUrlString;
    }

    public static String getAppDataFolder(String initFolderPathUserSet, String folderName) {

        File toolDataFolder = new File(initFolderPathUserSet);
        // 1. Check init folder which user set before, if available, then use it.
        if (toolDataFolder.exists() || toolDataFolder.mkdirs()) {
            return initFolderPathUserSet;
        }

        // 2. Check Application Directory
        String applicationFolderPath = FileSystems.getDefault()
                .getPath(folderName)
                .normalize()
                .toAbsolutePath()
                .toString();
        File applicationFolder = new File(applicationFolderPath);
        if(applicationFolder.exists() || applicationFolder.mkdirs()) {
            PreferenceManager.setToolDataFilepath(applicationFolderPath);
            return applicationFolderPath;
        }

        // 3. Check System User Directory
        String systemUserDirectory = Paths.get(System.getProperty("user.home"), folderName).normalize().toAbsolutePath().toString();
        new File(systemUserDirectory).mkdirs();

        PreferenceManager.setToolDataFilepath(systemUserDirectory);
        return systemUserDirectory;
    }

    public static void disableSslVerification() throws Exception {
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    }
                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    }
                }
        };

        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        // Create all-trusting host name verifier
        HostnameVerifier allHostsValid = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };

        // Install the all-trusting host verifier
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
    }
}
