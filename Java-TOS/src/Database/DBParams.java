/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

import java.io.File;

public class DBParams {

    public static String LOCAL_DATABASE_PATH = "jdbc:sqlite:database/mms.db";
    public static String LOCAL_DB_USER = "root";
    public static String LOCAL_DB_PASS = "";

    public static final String EX_CONNECTION_URL = "jdbc:mysql://72.167.224.113:3306/CloudBoost_MMS";
    public static final String EX_DB_USER = "mms";
    public static final String EX_DB_PASS = "D3]uTTtsZ7m~}}e=";

    public static String BANKROLL_TABLE;
    public static String BET_TABLE;
    public static String BETTYPE_TABLE;
    public static String USER_TABLE;

    static {
        String os = System.getProperty("os.name");
        if (os.startsWith("Windows")) {
            //windowns 
            //String dir_path = "C:/Betting/database/";
            //String dir_path = System.getProperty("user.dir") + File.separator + "LocalDB" + File.separator;
            String dir_path = System.getProperty("user.home") + File.separator + "MMSLocalDB" + File.separator;

            File db_dir = new File(dir_path);
            if (!db_dir.exists()) {
                db_dir.mkdirs();
            }
            LOCAL_DATABASE_PATH = "jdbc:sqlite:" + dir_path + "MMS.db";
        } else {
            String dir_path = System.getProperty("user.home") + File.separator + "MMS/database/";
            File db_dir = new File(dir_path);
            if (!db_dir.exists()) {
                db_dir.mkdirs();
            }
            LOCAL_DATABASE_PATH = "jdbc:sqlite:" + dir_path + "MMS.db";
        }

        BANKROLL_TABLE = "CREATE TABLE IF NOT EXISTS \"bankroll\" (\n"
                + "	\"id\"	INTEGER NOT NULL UNIQUE,\n"
                + "	\"name\"	TEXT NOT NULL,\n"
                + "	\"balance\"	NUMERIC,\n"
                + "	\"u_balance\"	NUMERIC,\n"
                + "	\"unit\"	NUMERIC,\n"
                + "	\"m_profilt\"	NUMERIC,\n"
                + "	\"w_profilt\"	NUMERIC,\n"
                + "	\"d_profilt\"	NUMERIC,\n"
                + "	\"type\"	INTEGER DEFAULT 0,\n"
                + "	PRIMARY KEY(\"id\" AUTOINCREMENT)\n"
                + ");";

        BET_TABLE = "CREATE TABLE IF NOT EXISTS \"bet\" (\n"
                + "	\"id\"	INTEGER NOT NULL UNIQUE,\n"
                + "	\"bid\"	INTEGER NOT NULL,\n"
                + "	\"bet_id\"	TEXT,\n"
                + "	\"year\"	INTEGER,\n"
                + "	\"month\"	INTEGER,\n"
                + "	\"week\"	INTEGER,\n"
                + "	\"date\"	TEXT,\n"
                + "	\"time\"	TEXT,\n"
                + "	\"user\"	TEXT,\n"
                + "	\"sport\"	TEXT,\n"
                + "	\"country\"	TEXT,\n"
                + "	\"league\"	TEXT,\n"
                + "	\"home\"	TEXT,\n"
                + "	\"away\"	TEXT,\n"
                + "	\"status\"	TEXT,\n"
                + "	\"score\"	TEXT,\n"
                + "	\"type\"	TEXT,\n"
                + "	\"units\"	NUMERIC,\n"
                + "	\"amount\"	NUMERIC,\n"
                + "	\"odd\"	NUMERIC,\n"
                + "	\"balance\"	NUMERIC,\n"
                + "	\"win\"	INTEGER DEFAULT 0,\n"
                + "	PRIMARY KEY(\"id\" AUTOINCREMENT)\n"
                + ");";

        BETTYPE_TABLE = "CREATE TABLE IF NOT EXISTS \"bettype\" (\n"
                + "	\"id\"	INTEGER NOT NULL UNIQUE,\n"
                + "	\"name\"	TEXT NOT NULL,\n"
                + "	PRIMARY KEY(\"id\" AUTOINCREMENT)\n"
                + ");";

        USER_TABLE = "CREATE TABLE IF NOT EXISTS \"user\" (\n"
                + "	\"id\"	INTEGER NOT NULL UNIQUE,\n"
                + "	\"name\"	TEXT NOT NULL,\n"
                + "	PRIMARY KEY(\"id\" AUTOINCREMENT)\n"
                + ");";
    }
}
