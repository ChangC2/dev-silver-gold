/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

public class Api {
    public static final String SERVE_URL = "http://api.slymms.com";
    public static final String api_login = "/api/loginWithUserId";
    public static final String api_customerInfo = "/api/loginWithCustomerId";
    public static final String api_timeSavings = "/api/postTimeSavings";
    public static final String api_systemAlarm = "/api/postAlarms";
    public static final String api_jobInfo = "/api/getJobData";
    public static final String api_postGanttData = "/api/postGanttData";
    public static final String api_postShiftData = "/api/postShiftData";
    public static final String api_postHSTData = "/api/postHSTData";

    public static final String api_postJobDataCompletedParts = "/api/postJobDataCompletedParts";

    public static final String api_setMachineStatus = "/api/setMachineStatus";
    public static final String api_assignMachineToUser = "/api/assignMachineToUser";
    public static final String api_getCurrentPartsCount = "/api/getCurrentPartsCount";

    public static final String api_getAppSetting = "/api/getAppSetting";
    public static final String api_getShiftDetails = "/api/get_shift_detail";

    public static final String api_version = "/Terminals/appTOS.ver";
    public static final String api_build_tos = "/Terminals/TOSV2Setup.exe";
    public static final String api_build_fanuc = "/Terminals/FanucServiceSetup.exe";
    public static final String api_build_fanuc1 = "/Terminals/FanucServiceSetup.msi";
    public static final String api_build_fanuc2 = "/Terminals/setup.exe";
    public static final String api_build_fanuc3 = "/Terminals/setup.bat";
}
