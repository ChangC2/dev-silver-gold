Imports System.Net
Imports System.Text
Imports Newtonsoft.Json
Imports Newtonsoft.Json.Linq

Public Class SettingsForm

    ' Form Load Event
    Private Sub SettingsForm_Load(sender As Object, e As EventArgs) Handles MyBase.Load

        ' Load Machine IP and Port
        txtIpAddressInput.Text = GetEncryptedAppSettings(SETTINGS_KEY_FANUCIP)   ' "192.168.1.23"
        txtPortInput.Text = GetEncryptedAppSettings(SETTINGS_KEY_FANUCPORT)              ' 8193
        txtReadCycleTime.Text = GetEncryptedAppSettings(SETTINGS_KEY_READ_CYCLE_TIME)
        If String.IsNullOrEmpty(txtReadCycleTime.Text) Then
            txtReadCycleTime.Text = "200"
        End If

    End Sub

    ' Save Button Actions
    Private Sub btnSave_Click(sender As Object, e As EventArgs) Handles btnSave.Click

        ' Check IP and Port 
        Dim IPAddress As String = txtIpAddressInput.Text
        Dim portNum As Integer = 0
        Dim readCycleTime As Integer = 200

        Try
            portNum = Int(txtPortInput.Text)
        Catch ex As Exception
        End Try

        Try
            readCycleTime = Int(txtReadCycleTime.Text)
        Catch ex As Exception
        End Try

        ' Check Connect Information
        If IPAddress.Trim.Length = 0 Or portNum = 0 Then
            MessageBox.Show("Please check machine ip and port", "Warning", MessageBoxButtons.OK, MessageBoxIcon.Warning)
            Return
        End If

        If readCycleTime <= 0 Then
            readCycleTime = 200
        End If

        SaveEncryptedAppSettings(SETTINGS_KEY_FANUCIP, IPAddress)
        SaveEncryptedAppSettings(SETTINGS_KEY_FANUCPORT, CStr(portNum))
        SaveEncryptedAppSettings(SETTINGS_KEY_READ_CYCLE_TIME, CStr(readCycleTime))
        ' ------------------------------------------------------------------------------

        Main.ShowAppSettings()

        Me.Hide()

    End Sub

    Public Function ValidateEmail(ByVal strCheck As String) As Boolean
        Try
            Dim vEmailAddress As New System.Net.Mail.MailAddress(strCheck)
        Catch ex As Exception
            Return False
        End Try
        Return True
    End Function

    ' Request Rest API call
    Function postData(ByVal dictData As Specialized.NameValueCollection) As Boolean
        Dim webClient As New MyWebClient(1)
        ' Dim resByte As Byte()
        ' Dim resString As String
        ' Dim reqString() As Byte

        Try
            webClient.Headers("Accept") = "*/*"
            webClient.Headers.Add("Content-Type", "application/x-www-form-urlencoded")

            ' Add CallBack Listener
            AddHandler webClient.UploadValuesCompleted, AddressOf UploadValuesCompletedEventHandler
            webClient.UploadValuesAsync(New Uri(URL_LOGINCUSTOMERID), "POST", dictData)

            Return True
        Catch ex As Exception
            Console.WriteLine(ex.Message)
        End Try
        Return False
    End Function

    ' Callback of Web Request
    Public Sub UploadValuesCompletedEventHandler(sender As Object, e As UploadValuesCompletedEventArgs)

        Dim responsebody = (New Text.UTF8Encoding).GetString(e.Result)
        Dim resString As String = Encoding.Default.GetString(e.Result)
        Console.WriteLine(resString)

        Dim result = JsonConvert.DeserializeObject(resString)
        Dim json As JObject = JObject.Parse(resString)
        Dim status As Boolean = json.SelectToken("status").Value(Of Boolean)

        'Parse response
        Dim webclient = CType(sender, MyWebClient)
        If webclient.getTag = 0 Then

            If status = True Then
                ShowMessage("Factory Info", "Success to get factory information.")
            Else
                ShowMessage("Factory Info", "Couldn't get factory information.")
            End If
        Else
            If responsebody.Contains("""status"":true") = True Then
                ShowMessage("Factory Info", "Success to add!")
            Else
                ShowMessage("Factory Info", "Error: Please try again!")
            End If
        End If
    End Sub

    Private Sub ShowMessage(ByVal title As String, ByVal message As String)
        MessageBox.Show(message, title)
    End Sub

    Private Sub btnClose_Click(sender As Object, e As EventArgs) Handles btnClose.Click
        Hide()
    End Sub

End Class