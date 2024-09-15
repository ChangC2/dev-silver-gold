package Utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class KeyGen {
    public final String makeTheKey(String password) {
        String RawHash = "";
        String TheKey = "";
        String AlphaNum = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789" +
                "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789" +
                "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789" +
                "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789" +
                "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789" +
                "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789" +
                "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789" +
                "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
            byte[] hashInBytes = md.digest(password.getBytes(StandardCharsets.UTF_8));
            int ndx = 0;
            int charsProcessed = 0;
            for (ndx = 0; ndx < hashInBytes.length; ndx++) {
                RawHash += String.format("%02x", hashInBytes[ndx]);
            }
            for (ndx = 0; ndx < RawHash.length(); ndx += 2) {
                int index = Integer.parseInt(RawHash.substring(ndx, ndx + 2), 16);
                TheKey += AlphaNum.substring(index, index + 1);
                charsProcessed += 1;
                if (charsProcessed % 4 == 0 && ndx < RawHash.length() - 2) {
                    TheKey += "-";
                }
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return TheKey;
    }
}
