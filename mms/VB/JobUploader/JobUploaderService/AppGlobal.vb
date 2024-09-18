Imports System.Collections.Specialized
Imports System.Net.Mail
Imports System.Net.NetworkInformation
Imports Microsoft.Win32

Public Module AppGlobal

    ' Inner Version
    Public Const innerVersion As String = "2.24"

    ' Server Url
    Public Const BASE_URL = "https://api.slymms.com/api/"
    Public Const URL_LOGINCUSTOMERID = BASE_URL & "loginWithCustomerId"
    Public Const URL_LOGINUSERID = BASE_URL & "loginWithUserId"
    Public Const URL_JOBINFO = BASE_URL & "getJobData"
    Public Const URL_POSTJOBDATA = BASE_URL & "postJobData"

    ' App Settings Key
    Public Const SETTINGS_APPNAME = "xxxJobUploaderxxx"
    Public Const SETTINGS_SECTION_JOBUPLOADER = "JobUploaderSettings"
    Public Const SETTINGS_SECTION_SEEDS = "slytrackr!"

    Public Const SETTINGS_KEY_FACTORYID = "FactoryID"
    Public Const SETTINGS_KEY_DBHOST = "DBHOST"
    Public Const SETTINGS_KEY_DBPORT = "DBPORT"
    Public Const SETTINGS_KEY_DBUSER = "DBUSER"
    Public Const SETTINGS_KEY_DBPASS = "DBPASS"
    Public Const SETTINGS_KEY_DBNAME = "DBNAME"
    Public Const SETTINGS_KEY_TBNAME = "TBNAME"

    Public Const SETTINGS_KEY_DATA_UPDATE_INTERVAL = "DataUpdateInterval"
    Public Const SETTINGS_KEY_SCHEDULED_UPLOAD = "ScheduledUpload"
    Public Const SETTINGS_KEY_RUN_AT_STARTUP = "RunAtStartup"


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

    Public dataUpdateIntervals() As Integer = {
                    15 * 60 * 1000,
                    30 * 60 * 1000,
                    60 * 60 * 1000,
                    2 * 60 * 60 * 1000,
                    3 * 60 * 60 * 1000,
                    5 * 60 * 60 * 1000,
                    8 * 60 * 60 * 1000,
                    24 * 60 * 60 * 1000}


    <System.Runtime.InteropServices.StructLayout(Runtime.InteropServices.LayoutKind.Explicit)>
    Structure EvilUnion
        <System.Runtime.InteropServices.FieldOffset(0)>
        Public UShortValue As UShort
        <System.Runtime.InteropServices.FieldOffset(0)>
        Public ShortValue As Short
    End Structure



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
        DeleteSetting(SETTINGS_APPNAME, SETTINGS_SECTION_JOBUPLOADER, key)
    End Sub

    Sub SaveEncryptedAppSettings(key As String, value As String)
        Dim wrapper As New Simple3Des(SETTINGS_SECTION_SEEDS)

        Dim cipherText As String = wrapper.EncryptData(value)

        ' SaveSetting(SETTINGS_APPNAME, SETTINGS_SECTION_FACTORY, key, cipherText)

        SaveRegSetting(SETTINGS_APPNAME, SETTINGS_SECTION_JOBUPLOADER, key, cipherText)
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

        Dim cipherText As String = GetRegSetting(SETTINGS_APPNAME, SETTINGS_SECTION_JOBUPLOADER, key, "")

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