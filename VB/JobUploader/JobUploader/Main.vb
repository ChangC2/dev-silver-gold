Imports MySql.Data.MySqlClient
Imports System.IO
Imports System.Net
Imports System.Net.Http
Imports System.Text
Imports Newtonsoft.Json
Imports Newtonsoft.Json.Linq
Imports Back

Public Class Main
    Private Sub Main_Load(sender As Object, e As EventArgs) Handles MyBase.Load
        BackgroundWorker1.WorkerReportsProgress = True
        BackgroundWorker1.WorkerSupportsCancellation = True
        Me.CenterToScreen()
        Me.Text = "Job Data Uploader (" + innerVersion + ")"
        txtHost.Text = GetEncryptedAppSettings(SETTINGS_KEY_DBHOST)
        txtPort.Text = GetEncryptedAppSettings(SETTINGS_KEY_DBPORT)
        txtUsername.Text = GetEncryptedAppSettings(SETTINGS_KEY_DBUSER)
        txtPassword.Text = GetEncryptedAppSettings(SETTINGS_KEY_DBPASS)
        txtDBName.Text = GetEncryptedAppSettings(SETTINGS_KEY_DBNAME)
        txtCustomerID.Text = GetEncryptedAppSettings(SETTINGS_KEY_FACTORYID)
        txtTableName.Text = GetEncryptedAppSettings(SETTINGS_KEY_TBNAME)

        Dim scheduledUpload As String = GetEncryptedAppSettings(SETTINGS_KEY_SCHEDULED_UPLOAD)
        chkScheduledUpload.Checked = scheduledUpload <> ""
        cmbSchedules.Enabled = chkScheduledUpload.Checked
        If chkScheduledUpload.Checked Then
            Dim intervalIndexString As String = GetEncryptedAppSettings(SETTINGS_KEY_DATA_UPDATE_INTERVAL)
            If intervalIndexString = "" Then
                cmbSchedules.SelectedIndex = 0
            Else
                cmbSchedules.SelectedIndex = CInt(intervalIndexString)
            End If
        End If

        Dim runAtStartup As String = GetEncryptedAppSettings(SETTINGS_KEY_RUN_AT_STARTUP)
        chkRunAtStartup.Checked = runAtStartup <> ""

        Dim location = System.Reflection.Assembly.GetExecutingAssembly().Location
        Dim appPath = Path.GetDirectoryName(location)
        Dim logFileName As String = appPath + "/logs/" + DateTime.Now.ToString("yyyy-MM-dd") + ".log"
        If Not File.Exists(logFileName) Then
            Try
                Using sw As StreamWriter = File.CreateText(logFileName)
                    sw.WriteLine("--- " + DateTime.Now.ToString("yyyy-MM-dd") + " ---") ' Write text to the file
                End Using
            Catch ex As Exception
            End Try
        End If

        Dim logReader As StreamReader = My.Computer.FileSystem.OpenTextFileReader(logFileName)
        Dim oneLog As String
        Do
            oneLog = logReader.ReadLine
            If oneLog IsNot Nothing Then
                listStatusLog.Items.Add(oneLog)
            End If
        Loop Until oneLog Is Nothing
        logReader.Close()
    End Sub

    Private Sub Main_FormClosing(sender As Object, e As System.Windows.Forms.FormClosingEventArgs) Handles Me.FormClosing

    End Sub

    Private Sub BackgroundWorker1_DoWork(sender As Object, e As System.ComponentModel.DoWorkEventArgs) Handles BackgroundWorker1.DoWork
        Dim worker As System.ComponentModel.BackgroundWorker = CType(sender, System.ComponentModel.BackgroundWorker)

        btnStart.Text = "Stop uploading Job Data"
        btnClearLogs.Enabled = False
        listStatusLog.Items.Clear()

        If txtHost.Text = "" Then
            MessageBox.Show("Please input host!")
            Return
        End If
        If txtPort.Text = "" Then
            MessageBox.Show("Please input port!")
            Return
        End If
        If txtUsername.Text = "" Then
            MessageBox.Show("Please input username!")
            Return
        End If
        If txtDBName.Text = "" Then
            MessageBox.Show("Please input database name!")
            Return
        End If
        If txtCustomerID.Text = "" Then
            MessageBox.Show("Please input customer ID!")
            Return
        End If
        If txtTableName.Text = "" Then
            MessageBox.Show("Please input table name!")
            Return
        End If

        SaveEncryptedAppSettings(SETTINGS_KEY_FACTORYID, txtCustomerID.Text)
        SaveEncryptedAppSettings(SETTINGS_KEY_DBHOST, txtHost.Text)
        SaveEncryptedAppSettings(SETTINGS_KEY_DBPORT, txtPort.Text)
        SaveEncryptedAppSettings(SETTINGS_KEY_DBUSER, txtUsername.Text)
        SaveEncryptedAppSettings(SETTINGS_KEY_DBNAME, txtDBName.Text)
        SaveEncryptedAppSettings(SETTINGS_KEY_DBPASS, txtPassword.Text)
        SaveEncryptedAppSettings(SETTINGS_KEY_TBNAME, txtTableName.Text)

        Dim customerID As String = GetEncryptedAppSettings(SETTINGS_KEY_FACTORYID)
        Dim dbHost As String = GetEncryptedAppSettings(SETTINGS_KEY_DBHOST)
        Dim dbPort As String = GetEncryptedAppSettings(SETTINGS_KEY_DBPORT)
        Dim dbUser As String = GetEncryptedAppSettings(SETTINGS_KEY_DBUSER)
        Dim dbPass As String = GetEncryptedAppSettings(SETTINGS_KEY_DBPASS)
        Dim dbName As String = GetEncryptedAppSettings(SETTINGS_KEY_DBNAME)
        Dim tbName As String = GetEncryptedAppSettings(SETTINGS_KEY_TBNAME)

        Dim connStr As String = "server=" & dbHost &
                                    ";user=" + dbUser &
                                    ";database=" & dbName &
                                    ";port=" & dbPort &
                                    ";password=" & dbPass & ";"
        Dim conn As New MySqlConnection(connStr)

        Dim location = System.Reflection.Assembly.GetExecutingAssembly().Location
        Dim appPath = Path.GetDirectoryName(location)
        Dim logFileName As String = appPath + "/logs/" + DateTime.Now.ToString("yyyy-MM-dd") + ".log"
        If Not File.Exists(logFileName) Then
            Try
                Using sw As StreamWriter = File.CreateText(logFileName)
                    sw.WriteLine("--- " + DateTime.Now.ToString("yyyy-MM-dd") + " ---") ' Write text to the file
                End Using
            Catch ex As Exception
            End Try
        End If

        Dim logReader As StreamReader = My.Computer.FileSystem.OpenTextFileReader(logFileName)
        Dim oneLog As String
        Do
            oneLog = logReader.ReadLine
            If oneLog IsNot Nothing Then
                listStatusLog.Items.Add(oneLog)
            End If
        Loop Until oneLog Is Nothing
        logReader.Close()

        Dim logFile As System.IO.StreamWriter
        logFile = My.Computer.FileSystem.OpenTextFileWriter(logFileName, True)
        Try
            listStatusLog.Items.Add(DateTime.Now.ToString("MM/dd/yyyy HH:mm:ss") + ": " + "Connecting to MySQL...")
            logFile.WriteLine(DateTime.Now.ToString("MM/dd/yyyy HH:mm:ss") + ": " + "Connecting to MySQL...")
            conn.Open()

            Dim recordCount As Integer = 0
            Dim runStep As Integer = 0
            Dim countQuery As String = String.Format("SELECT COUNT(*) FROM `{0}` WHERE 1 = 1", tbName)
            Dim commandCountReader As New MySqlCommand(countQuery, conn)
            recordCount = Convert.ToInt32(commandCountReader.ExecuteScalar())
            commandCountReader.Dispose()

            Dim commandReader As New MySqlCommand("Select * from " & tbName & " where 1 = 1", conn)
            Dim reader As MySqlDataReader = commandReader.ExecuteReader
            listStatusLog.Items.Add(DateTime.Now.ToString("MM/dd/yyyy HH:mm:ss") + ": " + "Done")
            logFile.WriteLine(DateTime.Now.ToString("MM/dd/yyyy HH:mm:ss") + ": " + "Done")
            While reader.Read
                runStep += 1
                If worker.CancellationPending = True Then
                    e.Cancel = True
                    Exit While
                Else
                    worker.ReportProgress(runStep * 100 / recordCount)
                End If
                oneLog = DateTime.Now.ToString("MM/dd/yyyy HH:mm:ss") + ": " + "Reading Row..." + "  Job ID: " + GetField(reader, "id")
                logFile.WriteLine(oneLog)
                listStatusLog.Items.Add(oneLog)
                Dim jobID As String = GetField(reader, "id")
                Dim order_type As String = GetField(reader, "order_type")
                Dim seq_no As String = GetField(reader, "seq_no")
                Dim status_type_val As String = GetField(reader, "status_type_val")
                Dim pr_order_no As String = GetField(reader, "pr_order_no")
                Dim customer As String = GetField(reader, "company_name")
                Dim partNumber As String = GetField(reader, "sa_order_line_no")
                Dim description As String = GetField(reader, "to_item_no")
                Dim qtyRequired As String = GetField(reader, "order_qty")
                Dim aux1data As String = GetField(reader, "dim1")
                Dim aux2data As String = GetField(reader, "dim2")
                Dim aux3data As String = GetField(reader, "form_dim1")
                Dim pr_center_no As String = GetField(reader, "pr_center_no")
                Dim short_desc As String = GetField(reader, "short_desc")
                Dim bom_item As String = GetField(reader, "bom_item")
                Dim bom_short_cd As String = GetField(reader, "bom_short_cd")
                Dim bom_item_no As String = GetField(reader, "bom_item_no")
                Dim bom_dim1 As String = GetField(reader, "bom_dim1")
                Dim bom_dim2 As String = GetField(reader, "bom_dim2")
                Dim bom_dim3 As String = GetField(reader, "bom_dim3")
                Dim bom_dim4 As String = GetField(reader, "bom_dim4")
                Dim bom_dim5 As String = GetField(reader, "bom_dim5")
                Dim bom_dim6 As String = GetField(reader, "bom_dim6")
                Dim bom_uom As String = GetField(reader, "bom_uom")
                Dim dueDate As String = DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss")
                Try
                    dueDate = reader.GetDateTime(reader.GetOrdinal("due_dt")).ToString("yyyy-MM-dd HH:mm:ss")
                Catch ex As Exception
                    dueDate = DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss")
                End Try
                Dim orderDate As String = DateTime.Now.ToString("yyyy-MM-dd HH:mm:ss")

                Try
                    Dim myWebClient As New WebClient()

                    ServicePointManager.SecurityProtocol = SecurityProtocolType.Tls12

                    Dim apiUrl As String = URL_POSTJOBDATA
                    Dim dictData As New Specialized.NameValueCollection()

                    dictData.Add("jobID", jobID)
                    dictData.Add("order_type", order_type)
                    dictData.Add("seq_no", seq_no)
                    dictData.Add("status_type_val", status_type_val)
                    dictData.Add("pr_order_no", pr_order_no)
                    dictData.Add("customer", customer)
                    dictData.Add("partNumber", partNumber)
                    dictData.Add("description", description)
                    dictData.Add("qtyRequired", qtyRequired)
                    dictData.Add("dueDate", dueDate)
                    dictData.Add("orderDate", orderDate)
                    dictData.Add("aux1data", aux1data)
                    dictData.Add("aux2data", aux2data)
                    dictData.Add("aux3data", aux3data)
                    dictData.Add("pr_center_no", pr_center_no)
                    dictData.Add("short_desc", short_desc)
                    dictData.Add("bom_item", bom_item)
                    dictData.Add("bom_short_cd", bom_short_cd)
                    dictData.Add("bom_item_no", bom_item_no)
                    dictData.Add("bom_dim1", bom_dim1)
                    dictData.Add("bom_dim2", bom_dim2)
                    dictData.Add("bom_dim3", bom_dim3)
                    dictData.Add("bom_dim4", bom_dim4)
                    dictData.Add("bom_dim5", bom_dim5)
                    dictData.Add("bom_dim6", bom_dim6)
                    dictData.Add("bom_uom", bom_uom)
                    dictData.Add("customer_id", customerID)

                    Dim responseArray As Byte() = myWebClient.UploadValues(New Uri(apiUrl), "POST", dictData)

                    Dim resString As String = Encoding.Default.GetString(responseArray)

                    Dim result = JsonConvert.DeserializeObject(resString)
                    Dim json As JObject = JObject.Parse(resString)
                    Dim status As Boolean = json.SelectToken("status")

                    If status = True Then
                        'cntReportSuccess = cntReportSuccess + 1
                        'LocalDBMan.DeleteGanttData(ganttItem.id)
                    End If

                Catch ex As WebException
                    Dim exMsg As String = ex.Message
                    If Not String.IsNullOrEmpty(exMsg) Then
                        'addLog("ErrGData:" & ex.Message)
                    End If
                Catch ex As Exception
                    Dim exMsg As String = ex.Message
                    If Not String.IsNullOrEmpty(exMsg) Then
                        'addLog("ErrGData:" & ex.Message)
                    End If
                End Try
            End While
            commandReader.Dispose()
            reader.Close()
            conn.Close()

        Catch ex As Exception
            logFile.WriteLine(DateTime.Now.ToString("MM/dd/yyyy HH:mm:ss") + ": " + ex.ToString())
            listStatusLog.Items.Add(DateTime.Now.ToString("MM/dd/yyyy HH:mm:ss") + ": " + ex.ToString())
        End Try
        logFile.WriteLine(DateTime.Now.ToString("MM/dd/yyyy HH:mm:ss") + ": " + "Success to upload data!")
        listStatusLog.Items.Add(DateTime.Now.ToString("MM/dd/yyyy HH:mm:ss") + ": " + "Success to upload data!")
        logFile.Close()
        btnStart.Text = "Start Query and Match"
        btnClearLogs.Enabled = True
    End Sub

    Private Sub BackgroundWorker1_ProgressChanged(sender As Object, e As System.ComponentModel.ProgressChangedEventArgs) Handles BackgroundWorker1.ProgressChanged
        ' Update the UI to reflect the progress made
        ProgressBar1.Value = e.ProgressPercentage
    End Sub

    Private Sub BackgroundWorker1_RunWorkerCompleted(sender As Object, e As System.ComponentModel.RunWorkerCompletedEventArgs) Handles BackgroundWorker1.RunWorkerCompleted
        If e.Cancelled Then
            MessageBox.Show("Operation was canceled")
        ElseIf e.Error IsNot Nothing Then
            MessageBox.Show("Error occurred: " & e.Error.Message)
        Else
            MessageBox.Show("Operation completed successfully")
        End If
    End Sub

    Private Sub BtnStart_Click(sender As Object, e As EventArgs) Handles btnStart.Click
        If btnStart.Text = "Start Query and Match" Then
            BackgroundWorker1.RunWorkerAsync()
        Else
            BackgroundWorker1.CancelAsync()
        End If
    End Sub

    Function GetField(reader As MySqlDataReader, ByVal name As String) As String
        Try
            If Not reader.IsDBNull(reader.GetOrdinal(name)) Then
                If reader.GetFieldType(reader.GetOrdinal(name)) = GetType(Double) Then
                    Dim value As Double = reader.GetDouble(reader.GetOrdinal(name))
                    Return value.ToString()
                ElseIf reader.GetFieldType(reader.GetOrdinal(name)) = GetType(Integer) Then
                    Dim value As Double = reader.GetInt64(reader.GetOrdinal(name))
                    Return value.ToString()
                ElseIf reader.GetFieldType(reader.GetOrdinal(name)) = GetType(String) Then
                    Return reader.GetString(reader.GetOrdinal(name))
                Else
                    Return ""
                End If
            Else
                Return ""
            End If
        Catch ex As Exception
            Return ""
        End Try
    End Function

    Private Sub ChkScheduledUpload_CheckedChanged(sender As Object, e As EventArgs) Handles chkScheduledUpload.CheckedChanged
        If chkScheduledUpload.Checked Then
            SaveEncryptedAppSettings(SETTINGS_KEY_SCHEDULED_UPLOAD, "TRUE")
            cmbSchedules.Enabled = True
        Else
            SaveEncryptedAppSettings(SETTINGS_KEY_SCHEDULED_UPLOAD, "")
            cmbSchedules.Enabled = False
        End If
    End Sub

    Private Sub chkRunAtStartup_CheckedChanged(sender As Object, e As EventArgs) Handles chkRunAtStartup.CheckedChanged
        If chkRunAtStartup.Checked Then
            SaveEncryptedAppSettings(SETTINGS_KEY_RUN_AT_STARTUP, "TRUE")
            My.Computer.Registry.LocalMachine.OpenSubKey("SOFTWARE\Microsoft\Windows\CurrentVersion\Run", True).SetValue(Application.ProductName, Application.ExecutablePath)
        Else
            SaveEncryptedAppSettings(SETTINGS_KEY_RUN_AT_STARTUP, "")
            My.Computer.Registry.LocalMachine.OpenSubKey("SOFTWARE\Microsoft\Windows\CurrentVersion\Run", True).DeleteValue(Application.ProductName)
        End If
    End Sub


    Private Sub cmbSchedules_SelectedIndexChanged(sender As Object, e As EventArgs) Handles cmbSchedules.SelectedIndexChanged
        SaveEncryptedAppSettings(SETTINGS_KEY_DATA_UPDATE_INTERVAL, CStr(cmbSchedules.SelectedIndex))
    End Sub

    Private Sub BtnClearLogs_Click(sender As Object, e As EventArgs) Handles btnClearLogs.Click
        listStatusLog.Items.Clear()
        Dim location = System.Reflection.Assembly.GetExecutingAssembly().Location
        Dim appPath = Path.GetDirectoryName(location)
        Dim logFileName As String = appPath + "/logs/" + DateTime.Now.ToString("yyyy-MM-dd") + ".log"
        If File.Exists(logFileName) Then
            System.IO.File.WriteAllText(logFileName, "")
        End If
    End Sub

End Class
