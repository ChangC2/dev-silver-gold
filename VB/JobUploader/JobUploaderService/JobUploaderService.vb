Imports MySql.Data.MySqlClient
Imports System.IO
Imports System.Net
Imports System.Net.Http
Imports System.Text
Imports Newtonsoft.Json
Imports Newtonsoft.Json.Linq

Public Class JobUploaderService

    Dim jobDataUploadThread As System.Threading.Thread
    Dim DATA_UPDATE_INTERVAL As Integer = 1000 * 5           ' 5 seconds
    Dim runThread As Boolean = False

    Protected Overrides Sub OnStart(ByVal args() As String)
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

        Dim logFile As System.IO.StreamWriter
        logFile = My.Computer.FileSystem.OpenTextFileWriter(logFileName, True)
        Dim scheduledUpload As String = GetEncryptedAppSettings(SETTINGS_KEY_SCHEDULED_UPLOAD)
        logFile.WriteLine(DateTime.Now.ToString("MM/dd/yyyy HH:mm:ss") + ": Service Started")
        logFile.Close()

        runThread = True

        Dim intervalIndexString As String = GetEncryptedAppSettings(SETTINGS_KEY_DATA_UPDATE_INTERVAL)
        If intervalIndexString = "" Then
            DATA_UPDATE_INTERVAL = dataUpdateIntervals(0)
        Else
            DATA_UPDATE_INTERVAL = dataUpdateIntervals(CInt(intervalIndexString))
        End If
        'DATA_UPDATE_INTERVAL = 60 * 1000
        jobDataUploadThread = New System.Threading.Thread(AddressOf JobDataUploadHandler)
        jobDataUploadThread.Start()
    End Sub

    Protected Overrides Sub OnStop()
        ' Add code here to perform any tear-down necessary to stop your service.
        runThread = False
    End Sub

    ' ------ Job Data Upload -------------------------------
    Private Sub JobDataUploadHandler()
        Do While runThread
            Dim scheduledUpload As String = GetEncryptedAppSettings(SETTINGS_KEY_SCHEDULED_UPLOAD)
            If scheduledUpload <> "" Then
                ReportJobData()
            End If
            System.Threading.Thread.Sleep(DATA_UPDATE_INTERVAL)
        Loop
    End Sub

    Private Sub ReportJobData()
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

        Dim logFile As System.IO.StreamWriter
        logFile = My.Computer.FileSystem.OpenTextFileWriter(logFileName, True)
        logFile.WriteLine(DateTime.Now.ToString("MM/dd/yyyy HH:mm:ss") + ": " + "Connecting to MySQL from Service...")

        Dim customerID As String = GetEncryptedAppSettings(SETTINGS_KEY_FACTORYID)
        Dim dbHost As String = GetEncryptedAppSettings(SETTINGS_KEY_DBHOST)
        Dim dbPort As String = GetEncryptedAppSettings(SETTINGS_KEY_DBPORT)
        Dim dbUser As String = GetEncryptedAppSettings(SETTINGS_KEY_DBUSER)
        Dim dbPass As String = GetEncryptedAppSettings(SETTINGS_KEY_DBPASS)
        Dim dbName As String = GetEncryptedAppSettings(SETTINGS_KEY_DBNAME)
        Dim tbName As String = GetEncryptedAppSettings(SETTINGS_KEY_TBNAME)

        If customerID = "" Or dbHost = "" Or dbPort = "" Or dbUser = "" Or dbName = "" Or tbName = "" Then
            Return
        End If

        Dim connStr As String = "server=" & dbHost &
                                    ";user=" + dbUser &
                                    ";database=" & dbName &
                                    ";port=" & dbPort &
                                    ";password=" & dbPass & ";"
        Dim conn As New MySqlConnection(connStr)
        Try
            conn.Open()
            Dim commandReader As New MySqlCommand("Select * from " & tbName & " where 1 = 1", conn)
            Dim reader As MySqlDataReader = commandReader.ExecuteReader
            logFile.WriteLine(DateTime.Now.ToString("MM/dd/yyyy HH:mm:ss") + ": " + "Done")
            While reader.Read
                logFile.WriteLine(DateTime.Now.ToString("MM/dd/yyyy HH:mm:ss") + ": " + "Reading Row..." + "  Job ID: " + GetField(reader, "id"))
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
        End Try
        logFile.WriteLine(DateTime.Now.ToString("MM/dd/yyyy HH:mm:ss") + ": " + "Success to upload data!")
        logFile.Close()
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

End Class
