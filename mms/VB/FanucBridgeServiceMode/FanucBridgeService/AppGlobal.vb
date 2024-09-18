Imports System.Collections.Specialized
Imports System.Net.Mail
Imports System.Net.NetworkInformation
Imports Microsoft.Win32

Public Module AppGlobal

    ' Inner Version
    Public Const innerVersion As String = "3.7.29"

    ' Server Url
    Public Const BASE_URL = "https://api.slymms.com/api/"
    Public Const URL_LOGINCUSTOMERID = BASE_URL & "loginWithCustomerId"
    Public Const URL_LOGINUSERID = BASE_URL & "loginWithUserId"
    Public Const URL_JOBINFO = BASE_URL & "getJobData"
    Public Const URL_MACHINESTATUS = BASE_URL & "setMachineStatus"
    Public Const URL_ASSIGNMACHINE = BASE_URL & "assignMachineToUser"

    ' Default Errors
    Public Const ERROR_MODBUS_ADDRESS_FORMAT = "Error!" & vbCrLf & "Please input correct Modbus Address(0-65535)."
    Public Const ERROR_MACRO_ADDRESS_FORMAT = "Error!" & vbCrLf & "Please input macro Address(0-65535)."
    Public Const ERROR_PMC_ADDRESS_FORMAT = "Error!" & vbCrLf & "Please input macro Address(ex: G100.3)."

    ' App Settings Key
    Public Const SETTINGS_APPNAME = "xxxFactoryBridgexxx"

    Public Const SETTINGS_KEY_FANUCIP = "FanucIPAddress"
    Public Const SETTINGS_KEY_FANUCPORT = "FanucPort"
    Public Const SETTINGS_KEY_READ_CYCLE_TIME = "FanucReadCycleTime"

    Public Const SETTINGS_SECTION_FACTORY = "FactorySettings"
    Public Const SETTINGS_SECTION_SEEDS = "slytrackr!"

    Public Const SETTINGS_KEY_FACTORYID = "FactoryID"
    Public Const SETTINGS_KEY_FACTORYNAME = "FactoryName"
    Public Const SETTINGS_KEY_FACTORYLOGO = "FactoryLogo"

    Public Const SETTINGS_KEY_MACHINENAME = "MachineName"

    Public Const SETTINGS_KEY_USERID = "UserID"
    Public Const SETTINGS_KEY_USERNAME = "UserName"
    Public Const SETTINGS_KEY_USERAVATAR = "UserAvatar"
    Public Const SETTINGS_KEY_USERSECURITY = "UserSecurity"

    Public Const SETTINGS_KEY_JOBID = "JobID"
    Public Const SETTINGS_KEY_JOBINFO = "JobInfo"

    Public Const SETTINGS_KEY_LASTSAVETIME = "LastSaveTime"
    Public Const SETTINGS_KEY_TIMEUNCAT = "TimeUnCat"
    Public Const SETTINGS_KEY_TIMEINCYCLE = "TimeInCycle"
    Public Const SETTINGS_KEY_TIMEIDLE1 = "TimeIdle1"
    Public Const SETTINGS_KEY_TIMEIDLE2 = "TimeIdle2"
    Public Const SETTINGS_KEY_TIMEIDLE3 = "TimeIdle3"
    Public Const SETTINGS_KEY_TIMEIDLE4 = "TimeIdle4"
    Public Const SETTINGS_KEY_TIMEIDLE5 = "TimeIdle5"
    Public Const SETTINGS_KEY_TIMEIDLE6 = "TimeIdle6"
    Public Const SETTINGS_KEY_TIMEIDLE7 = "TimeIdle7"
    Public Const SETTINGS_KEY_TIMEIDLE8 = "TimeIdle8"

    Public Const SETTINGS_KEY_USE_CYCLE_START_INTERLOCK = "UseCycleStartInterlock"

    Public Const SETTINGS_KEY_GOODPART = "GoodPart"
    Public Const SETTINGS_KEY_BADPART = "BadPart"

    Public Const SETTINGS_KEY_STOPTIMELIMIT = "StopTimeLimit"
    Public Const SETTINGS_KEY_PLANNEDPRODUCTIONTIME = "PlannedProductionTime"
    Public Const SETTINGS_KEY_MINELAPSEDCYCLETIME = "MinElapsedCycleTime"

    Public Const SETTINGS_KEY_TARGETCYCLETIME = "TargetCycleTime"
    Public Const SETTINGS_KEY_PARTSPERCYCLE = "PartsPerCycle"
    Public Const SETTINGS_KEY_AUTOMATICCOUNT = "AutomaticCount"

    Public Const SETTINGS_KEY_DOWNTIMEREASON1 = "DowntimeReason1"
    Public Const SETTINGS_KEY_DOWNTIMEREASON2 = "DowntimeReason2"
    Public Const SETTINGS_KEY_DOWNTIMEREASON3 = "DowntimeReason3"
    Public Const SETTINGS_KEY_DOWNTIMEREASON4 = "DowntimeReason4"
    Public Const SETTINGS_KEY_DOWNTIMEREASON5 = "DowntimeReason5"
    Public Const SETTINGS_KEY_DOWNTIMEREASON6 = "DowntimeReason6"
    Public Const SETTINGS_KEY_DOWNTIMEREASON7 = "DowntimeReason7"
    Public Const SETTINGS_KEY_DOWNTIMEREASON8 = "DowntimeReason8"

    Public Const SETTINGS_KEY_SEND_ALERT = "SendAlert"
    Public Const SETTINGS_KEY_ALERT_EMAIL1 = "AlertEmail1"
    Public Const SETTINGS_KEY_ALERT_EMAIL2 = "AlertEmail2"
    Public Const SETTINGS_KEY_ALERT_EMAIL3 = "AlertEmail3"

    Public Const SETTINGS_KEY_USE_REPORT = "UseReport"
    Public Const SETTINGS_KEY_EXCEL_FOLDER = "ExcelFolder"

    Public Const SETTINGS_KEY_TICOLLECTOR_COMM = "TiCollectorComm"
    Public Const SETTINGS_KEY_TICOLLECTOR_NODES = "TiCollectorNodes"

    ' HST File Saving
    Public Const SETTINGS_KEY_HST_SERVERIP = "HstServerIP"
    Public Const SETTINGS_KEY_HST_FOLDER = "HstDataFolder"
    Public Const SETTINGS_KEY_HST_VARS = "HstDataVariables"
    Public Const SETTINGS_KEY_HST_ADDR_ONOFF = "HstAddressOnOff"
    Public Const SETTINGS_KEY_HST_ADDR_FILE_BATCH_NAME = "HstFileBatchName"
    Public Const SETTINGS_KEY_HST_ADDR_DATA = "HstAddressData"

    Public Const SETTINGS_KEY_AUTO_MONITOR = "AutoMonitor"
    Public Const SETTINGS_KEY_SPINDLE_TIME = "SpindleTime"
    Public Const SETTINGS_KEY_SPINDLE_THRESHOLD = "SpindleThreshold"

    ' Series 16/18/21, 16i/18i/21i, 0i, 30i/31i/32i, Power Mate i, PMi-A, odbst
    Enum STATE_INFO_FIELD
        TM
        AUT
        RUN
        MOTION
        MSTB
        EMERGENCY
        ALARM
    End Enum

    Dim stateTMModes() As String = {
                    "T mode",
                    "M mode"}

    Dim stateAUTModes() As String = {
                    "MDI",
                    "MEMory",
                    "****",
                    "EDIT",
                    "HaNDle",
                    "JOG",
                    "Teach in JOG",
                    "Teach in HaNDle",
                    "INC·feed",
                    "REFerence",
                    "ReMoTe"}

    Dim stateRUNModes() As String = {
                    "****(reset)",
                    "STOP",
                    "HOLD",
                    "STaRT",
                    "MSTR" ' During Retraction and re-positionaing of tool retraction and recovery, and operation of JOG MDI
    }

    Dim stateMOTIONModes() As String = {
                    "***",
                    "MoTioN",
                    "DWeLl"}

    Dim stateMSTBModes() As String = {
                    "***(Others)",
                    "FIN"}

    Dim stateEMERGENCYModes() As String = {
                    "(Not emergency)",
                    "EMerGency",
                    "ReSET",
                    "WAIT"}

    Dim stateALARMModes() As String = {
                    "***(Others)",
                    "ALarM",
                    "BATtery low",
                    "FAN",
                    "PS Warning",
                    "FSsB warning",
                    "INSulate warning",
                    "ENCoder warning",
                    "PMC alarm"}

    Public Function GetStatusValueWithTitle(ByVal mode As STATE_INFO_FIELD, value As Short) As String

        Dim modeTitle As String
        Dim optionValues As String()

        Select Case mode
            Case STATE_INFO_FIELD.TM
                modeTitle = "T/ M mode selection "
                optionValues = stateTMModes
            Case STATE_INFO_FIELD.AUT
                modeTitle = "AUTOMATIC mode selection"
                optionValues = stateAUTModes
            Case STATE_INFO_FIELD.RUN
                modeTitle = "Status of automatic operation"
                optionValues = stateRUNModes
            Case STATE_INFO_FIELD.MOTION
                modeTitle = "Status of axis movement, dwell"
                optionValues = stateMOTIONModes
            Case STATE_INFO_FIELD.MSTB
                modeTitle = "Status of M,S,T,B function"
                optionValues = stateMSTBModes
            Case STATE_INFO_FIELD.EMERGENCY
                modeTitle = "Status of emergency"
                optionValues = stateEMERGENCYModes
            Case STATE_INFO_FIELD.ALARM
                modeTitle = "Status of alarm"
                optionValues = stateALARMModes
            Case Else
                Return "Unknown"
        End Select

        If optionValues.Length > value Then
            Return modeTitle & " => " & optionValues(value)
        Else
            Return "Unknown"
        End If

    End Function

    Public Function GetStatusValue(ByVal mode As STATE_INFO_FIELD, value As Short) As String

        Dim optionValues As String()

        Select Case mode
            Case STATE_INFO_FIELD.TM
                optionValues = stateTMModes
            Case STATE_INFO_FIELD.AUT
                optionValues = stateAUTModes
            Case STATE_INFO_FIELD.RUN
                optionValues = stateRUNModes
            Case STATE_INFO_FIELD.MOTION
                optionValues = stateMOTIONModes
            Case STATE_INFO_FIELD.MSTB
                optionValues = stateMSTBModes
            Case STATE_INFO_FIELD.EMERGENCY
                optionValues = stateEMERGENCYModes
            Case STATE_INFO_FIELD.ALARM
                optionValues = stateALARMModes
            Case Else
                Return "Unknown"
        End Select

        If optionValues.Length > value Then
            Return optionValues(value)
        Else
            Return "Unknown"
        End If

    End Function


    <System.Runtime.InteropServices.StructLayout(Runtime.InteropServices.LayoutKind.Explicit)>
    Structure EvilUnion
        <System.Runtime.InteropServices.FieldOffset(0)>
        Public UShortValue As UShort
        <System.Runtime.InteropServices.FieldOffset(0)>
        Public ShortValue As Short
    End Structure

    Public Function GetFanucMessage(ByVal returnCode As Integer) As String

        Dim message As String = "Unknown Error!"

        Select Case returnCode
            Case Focas1.EW_PROTOCOL
                message = "Protocal Error"
            Case Focas1.EW_SOCKET
                message = "Windows socket error"
            Case Focas1.EW_NODLL
                message = "Dll not exist error"
            Case Focas1.EW_BUS
                message = "Bus error"
            Case Focas1.EW_SYSTEM2
                message = "System error"
            Case Focas1.EW_HSSB
                message = "Hssb communication error"
            Case Focas1.EW_HANDLE
                message = "Windows Library handle error"
            Case Focas1.EW_VERSION
                message = "CNC/PMC version mismatch"
            Case Focas1.EW_UNEXP
                message = "Abnormal error"
            Case Focas1.EW_SYSTEM
                message = "System error"
            Case Focas1.EW_PARITY
                message = "Shared RAM parity error"
            Case Focas1.EW_MMCSYS
                message = "Emm386 or mmcsys install error"
            Case Focas1.EW_RESET
                message = "Reset or stop occured error"
            Case Focas1.EW_BUSY
                message = "Busy error"
            Case Focas1.EW_OK
                message = "Ok"
            Case Focas1.EW_FUNC
                message = "Command prepare error or pmc no exist"
            Case Focas1.EW_LENGTH
                message = "Data block length error"
            Case Focas1.EW_NUMBER
                message = "Data number error"
            Case Focas1.EW_RANGE
                message = "Address range error"
            Case Focas1.EW_ATTRIB
                message = "Data attribute error"
            Case Focas1.EW_TYPE
                message = "Data type error"
            Case Focas1.EW_DATA
                message = "Data error"
            Case Focas1.EW_NOOPT
                message = "No option error"
            Case Focas1.EW_PROT
                message = "Write protect error"
            Case Focas1.EW_OVRFLOW
                message = "Memory overflow error"
            Case Focas1.EW_PARAM
                message = "CNC parameter incorrect error"
            Case Focas1.EW_BUFFER
                message = "Buffer error"
            Case Focas1.EW_PATH
                message = "Path error"
            Case Focas1.EW_MODE
                message = "CNC mode error"
            Case Focas1.EW_REJECT
                message = "Execution rejected error"
            Case Focas1.EW_DTSRVR
                message = "Data server error"
            Case Focas1.EW_ALARM
                message = "Alarm has been occurred"
            Case Focas1.EW_STOP
                message = "CNC is not running"
            Case Focas1.EW_PASSWD
                message = "Protection data error"
            Case Else
                message = "Unknow Error!"
        End Select

        Return message

    End Function


    Public Function GetBit(data As Byte, index As Integer) As Boolean
        If index < 0 OrElse index > 7 Then
            Throw New ArgumentOutOfRangeException("index")
        End If

        Dim vector As New BitVector32(data)
        Dim mask = CInt(2 ^ index)

        Return vector(mask)
    End Function

    Public Sub SetBit(ByRef data As Byte, index As Integer, value As Boolean)
        If index < 0 OrElse index > 7 Then
            Throw New ArgumentOutOfRangeException("index")
        End If

        Dim vector As New BitVector32(data)
        Dim mask = CInt(2 ^ index)

        vector(mask) = value
        data = CByte(vector.Data)
    End Sub

    Public Function GetXValue(lParam As UInteger) As UShort
        Return CUShort((lParam) And &HFFFF)
    End Function

    Public Function GetYValue(lParam As UInteger) As UShort
        Return CUShort(((lParam) >> 16) And &HFFFF)
    End Function

    Public Function ConvertUShortToShort(val As UShort) As Short
        Dim evil As New EvilUnion With {
            .UShortValue = val
        }
        Return evil.ShortValue
    End Function

    Public Function ConvertShortToUShort(val As Short) As UShort
        Dim evil As New EvilUnion With {
            .ShortValue = val
        }
        Return evil.UShortValue
    End Function

    ' Get Mac Address
    Function getMacAddress() As String
        Dim nics() As NetworkInterface = NetworkInterface.GetAllNetworkInterfaces()
        Return nics(1).GetPhysicalAddress.ToString
    End Function

    '' Save & Restore App Settings
    'Sub SaveAppSettings(key As String, value As String)

    '    Try
    '        SaveSetting(SETTINGS_APPNAME, SETTINGS_SECTION_FACTORY, key, value)
    '    Catch ex As Exception
    '    End Try

    'End Sub

    'Function GetAppSettings(key As String, Optional defVal As String = "") As String
    '    Return GetSetting(SETTINGS_APPNAME, SETTINGS_SECTION_FACTORY, key, defVal)
    'End Function

    Sub RemoveAppSettings(key As String)
        DeleteSetting(SETTINGS_APPNAME, SETTINGS_SECTION_FACTORY, key)
    End Sub

    Sub SaveEncryptedAppSettings(key As String, value As String)
        Dim wrapper As New Simple3Des(SETTINGS_SECTION_SEEDS)

        Dim cipherText As String = wrapper.EncryptData(value)

        ' SaveSetting(SETTINGS_APPNAME, SETTINGS_SECTION_FACTORY, key, cipherText)

        SaveRegSetting(SETTINGS_APPNAME, SETTINGS_SECTION_FACTORY, key, cipherText)
    End Sub

    Sub SaveRegSetting(appName As String, section As String, key As String, value As String)
        Dim regKey As RegistryKey = Registry.LocalMachine.OpenSubKey("Software\" & appName & "\" & section, True)
        If regKey Is Nothing Then
            regKey = Registry.LocalMachine.CreateSubKey("Software\" & appName & "\" & section)
        End If

        regKey.SetValue(key, value)

        regKey.Close()
    End Sub

    Function GetRegSetting(appName As String, section As String, key As String, defVal As String) As String
        Dim regKey As RegistryKey = Registry.LocalMachine.OpenSubKey("Software\" & appName & "\" & section, True)
        If regKey Is Nothing Then
            Return defVal
        Else
            Dim value As String = regKey.GetValue(key, defVal)
            regKey.Close()

            Return value
        End If
    End Function

    Function GetEncryptedAppSettings(key As String) As String
        ' Dim cipherText As String = GetSetting(SETTINGS_APPNAME, SETTINGS_SECTION_FACTORY, key, "")

        Dim cipherText As String = GetRegSetting(SETTINGS_APPNAME, SETTINGS_SECTION_FACTORY, key, "")

        If String.IsNullOrEmpty(cipherText) Then
            Return ""
        Else
            Dim wrapper As New Simple3Des(SETTINGS_SECTION_SEEDS)
            Try
                Dim plainText As String = wrapper.DecryptData(cipherText)
                Return plainText
            Catch ex As System.Security.Cryptography.CryptographicException
                Return ""
            End Try
        End If
    End Function

    Function GetTimeStringFromSeconds(seconds As Long) As String
        Dim iSpan As TimeSpan = TimeSpan.FromSeconds(seconds)

        Dim value As String = iSpan.Hours.ToString.PadLeft(2, "0"c) & ":" &
                                iSpan.Minutes.ToString.PadLeft(2, "0"c) & ":" &
                                iSpan.Seconds.ToString.PadLeft(2, "0"c)
        Return value
    End Function

    Public Function GetCurrentTimeMilis() As Long

        Dim milliseconds = CLng(Date.UtcNow.Subtract(New DateTime(1970, 1, 1)).TotalMilliseconds)
        Return milliseconds
    End Function

    Public Function GetCurrentTimeMilisLocal() As Long
        'Dim regDate = Format(Date.Now(), "ddMMMyyyy HH:mm:ss")
        'Dim reg1Date = Format(New DateTime(1970, 1, 1), "ddMMMyyyy HH:mm:ss")

        Return CLng((Date.Now - New DateTime(1970, 1, 1)).TotalMilliseconds)
    End Function


    Public Function GetDayTimeFullString() As String
        ' Day Time
        Dim dayWeek = Format(Date.Now(), "ddd")
        Dim dayTime = Format(Date.Now(), "MMM dd,yy HH:mm")
        Return dayWeek & " " & dayTime
    End Function

    Public Function GetDayTimeString() As String
        ' Day Time
        Dim dayTime = Format(Date.Now(), "MM/dd/yyyy")
        Return dayTime
    End Function

    Public Function GetTimeString() As String
        ' Day Time
        Dim dayTime = Format(Date.Now(), "HH:mm:ss")
        Return dayTime
    End Function

    Public Function GetCurrentDateTimeString() As String
        ' Day Time
        Dim dayTime = Format(Date.Now(), "yyyy-MM-dd HH:mm:ss")
        Return dayTime
    End Function

    ' Miliseconds to "yyyy-MM-dd HH:mm:ss.SSS"
    Public Function GetCreateAtString(timeTick As Long) As String
        Dim dateJan1st1970 As New DateTime(1970, 1, 1, 0, 0, 0)

        ' Day Time
        Dim dayTime = Format(dateJan1st1970.AddMilliseconds(timeTick), "yyyy-MM-dd HH:mm:ss")
        Return dayTime
    End Function

    ' Miliseconds to "yyyy-MM-dd HH:mm:ss.SSS"
    Public Function GetTimeStamp(timeTick As Long) As String
        Dim dateJan1st1970 As New DateTime(1970, 1, 1, 0, 0, 0)

        ' Day Time
        Dim dayTime = Format(dateJan1st1970.AddMilliseconds(timeTick), "yyyy-MM-dd HH:mm:ss")
        Return dayTime
    End Function

    Public Function GetTimeSecondsFromMidnight() As Long
        Dim timeNow As DateTime = DateTime.Now
        Dim timeMidnight As DateTime = DateTime.Today
        Dim ts As TimeSpan = timeMidnight.Subtract(timeNow)
        Dim secondsFromMidnight As Long = Math.Abs(ts.TotalSeconds)

        Return secondsFromMidnight
    End Function

    Public Function StringToLong(ByVal strVal As String, Optional ByVal defVal As Long = 0) As Long
        Dim longVal As Long = defVal
        Try
            longVal = Convert.ToInt64(strVal)
        Catch ex As Exception
        End Try

        Return longVal
    End Function

    Public Function StringToInt(ByVal strVal As String, Optional ByVal defVal As Integer = 0, Optional ByVal base As Integer = 10) As Long
        Dim intVal As Integer = defVal
        Try
            intVal = Convert.ToInt32(strVal, base)
        Catch ex As Exception
        End Try

        Return intVal
    End Function

    Public Function StringToDouble(ByVal strVal As String, Optional ByVal defVal As Double = 0) As Double
        Dim intVal As Double = defVal
        Try
            intVal = Convert.ToDouble(strVal)
        Catch ex As Exception
        End Try

        Return intVal
    End Function

    Public Function sendEmail(ByVal toEmails() As String, ByVal subject As String, ByVal body As String, ByRef errorString As String) As Boolean

        ' https://www.aspforums.net/Threads/471485/How-to-send-email-from-Yahoo-Gmail-Hotmail-using-C-and-VBNet-in-ASPNet/

        Return True

        Try
            Dim SmtpServer As New SmtpClient()
            SmtpServer.Host = "smtp.gmail.com"
            'SmtpServer.Credentials = New Net.NetworkCredential("chuntianhongsoft@gmail.com", "")
            SmtpServer.Credentials = New Net.NetworkCredential("reports@slymms.com", "246896321S!")

            SmtpServer.Port = 25
            SmtpServer.EnableSsl = True
            'SmtpServer.UseDefaultCredentials = True

            Dim mail As New MailMessage()
            mail = New MailMessage()
            mail.From = New MailAddress("reports@slymms.com")

            For i As Integer = 0 To toEmails.Length - 1
                If Not String.IsNullOrEmpty(toEmails(i)) Then
                    mail.To.Add(toEmails(i))
                End If
            Next

            mail.Subject = subject
            mail.Body = body
            SmtpServer.SendAsync(mail, 0)

            Return True
        Catch ex As Exception
            errorString = ex.ToString
            MsgBox(errorString)
            Return False
        End Try
    End Function

    Public Function linebreak(ByRef myString As String) As String
        If Len(myString) <> 0 Then
            If Right$(myString, 2) = vbCrLf Or Right$(myString, 2) = vbNewLine Then
                myString = Left$(myString, Len(myString) - 2)
            End If
        End If

        Return myString
    End Function

End Module