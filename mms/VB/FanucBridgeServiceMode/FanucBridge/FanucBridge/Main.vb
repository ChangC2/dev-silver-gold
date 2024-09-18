Imports System.IO
Imports System.Linq
Imports System.Net
Imports System.Text
Imports System.Text.RegularExpressions
Imports EasyModbus
Imports Newtonsoft.Json
Imports Newtonsoft.Json.Linq

Public Class Main

    Private Const SETTING_FILE As String = "fanuc_settings.json"

    Dim WithEvents objModbusServer As ModbusServer
    Dim objModbusClient As ModbusClient
    Dim objModbusPLCClient As ModbusClient
    Dim signalOnArray As Integer() = EasyModbus.ModbusClient.ConvertFloatToRegisters(1)
    Dim signalOffArray As Integer() = EasyModbus.ModbusClient.ConvertFloatToRegisters(0)

    Dim ipAddress As String
    Dim portNum As Integer
    Dim readCycleTime As Integer

    Public bolHandleObtained As Boolean = False
    Public intLibHndl As Integer = 0

    Dim deviceSetting As FanucSettings
    Dim macroSettingDic As Dictionary(Of Integer, MacroDataModel)
    Dim pmcSettingDic As Dictionary(Of Integer, PMCDataModel)

    ' Report Data & Report Thread
    Dim fanucData As FanucData = New FanucData()

    Dim timeCurrentMilis As Long = 0
    Dim timePrevStatusMilis As Long = 0

    ' Loca Database to manage gantt data 
    ' https//www.pconlife.com/viewfileinfo/mono-data-sqlite-dll/

    ' Auto Connect Logic
    Dim needAutoConnect As Boolean = True
    Dim reconnectTryTime As Long = 0

    ' https://www.inventcom.net/fanuc-focas-library/misc/cnc_sysinfo

    ' UI Thread Invoke Trigger
    Dim stopInvoking As Boolean = False

    ' Speed Check
    Dim runSpeedCheckThread As Boolean = False
    Dim speedCheckThread As System.Threading.Thread

    ' Form Load
    Private Sub Form1_Load(sender As Object, e As EventArgs) Handles MyBase.Load

        Dim gData As Single = 5500

        gData = 1.0 * 5500 / (10 ^ 3)

        'Dim testVar As String = GetRegSetting(SETTINGS_APPNAME, SETTINGS_SECTION_FACTORY, "TestVar1", "")
        'SaveRegSetting(SETTINGS_APPNAME, SETTINGS_SECTION_FACTORY, "TestVar", "Badaaa123")

        ' Show Version info in the Title
        Me.Text = String.Format("Fanuc Bridge UI Settings V{0}", innerVersion)

        ' Select TMS Tabs as default
        tabTiConn.SelectedIndex = 0

        ' Show App Version
        lblAppVersion.Text = String.Format("Fanuc Bridge UI Settings V{0}", innerVersion)

        ' Test Code
        'Dim mils = GetCurrentTimeMilis()
        'Dim uShortVal As UShort = ConvertShortToUShort(-5)
        'Dim stringFormat As String = String.Format("CURRENT(O{0}) MAIN(O{1})", 5689, 1245)
        'Dim zone As Long = GetCurrentTimeMilis() - GetCurrentTimeMilisLocal()

        ' Allow Transparent
        SetStyle(ControlStyles.SupportsTransparentBackColor, True)

        ' Show App Settings
        ShowAppSettings()

        ' makeTempSettings()
        ' Dim floatValArray() As Integer = ModbusClient.ConvertFloatToRegisters(25.2)
        ' Dim singleValue As Single = ModbusClient.ConvertRegistersToFloat(floatValArray)

        ' Load Fanuc Address Settings
        LoadFanucSettings()

        ' Init Modbus Server
        objModbusServer = New EasyModbus.ModbusServer
        objModbusServer.Port = 502
        objModbusServer.Listen()
        ' objModbusServer.mHoldingRegisters.Item(100) = 100
        ' objModbusServer.mHoldingRegisters.Item(101) = 101

        ' Init Chart
        stripChart = New StripChart()

        ' Show App Local IP for Modbus Clients
        Try
            Dim ipV4 As String = Dns.GetHostEntry(Dns.GetHostName()).AddressList _
            .Where(Function(a As IPAddress) Not a.IsIPv6LinkLocal AndAlso Not a.IsIPv6Multicast AndAlso Not a.IsIPv6SiteLocal) _
            .First() _
            .ToString()

            lblModbusSvrIP.Text = ipV4
        Catch ex As Exception
            lblModbusSvrIP.Text = "Sorry!" & vbCrLf & "Please find IPV4 in ConrolPanel."
        End Try

        ' Show Auto Monitor Status
        Dim autoMonitorStatus As Integer = StringToInt(GetEncryptedAppSettings(SETTINGS_KEY_AUTO_MONITOR))
        Dim autoMonitorEnable As Boolean = IIf(autoMonitorStatus > 0, True, False)
        chkAutoMonitor.Checked = autoMonitorEnable
        txtSpindleTime.Text = GetEncryptedAppSettings(SETTINGS_KEY_SPINDLE_TIME)
        txtThreshold.Text = GetEncryptedAppSettings(SETTINGS_KEY_SPINDLE_THRESHOLD)

        ' Load PLC IP and Port
        tbPLCIP.Text = GetEncryptedAppSettings(SETTINGS_KEY_PLCIP)          ' "192.168.1.23"
        tbPLCPort.Text = GetEncryptedAppSettings(SETTINGS_KEY_PLCPORT)      ' 502

        ' Load Fanuc Adaptive Enable Settings
        Dim adaptiveEnableType As String = GetEncryptedAppSettings(SETTINGS_KEY_ADAPTIVE_ENABLE_TYPE)
        If String.IsNullOrEmpty(adaptiveEnableType) = False Then
            cbBoxTypePMC.SelectedIndex = Int(adaptiveEnableType)
        End If
        tbAdaptiveEnableAddr.Text = GetEncryptedAppSettings(SETTINGS_KEY_ADAPTIVE_ADDRESS)  ' G12

        ' Connect to local Server
        objModbusClient = New EasyModbus.ModbusClient
        objModbusClient.IPAddress = "127.0.0.1"
        objModbusClient.Port = 502
        objModbusClient.Connect()

        Dim registerInt As Integer() = EasyModbus.ModbusClient.ConvertFloatToRegisters(10.5)
        registerInt = EasyModbus.ModbusClient.ConvertFloatToRegisters(20.5)
        registerInt = EasyModbus.ModbusClient.ConvertFloatToRegisters(30.5)

        Dim values As Integer() = objModbusClient.ReadInputRegisters(0, 10)
        Dim strVal As String = EasyModbus.ModbusClient.ConvertRegistersToString(values, 0, values.Length)


        ' Reset Timers
        timePrevStatusMilis = GetCurrentTimeMilis()

        ' Try to auto connect with device
        ConnectFanuc(False)

        ' Triggers Periodical Scanning 
        Timer1.Enabled = True

    End Sub

    Private Sub Form1_FormClosing(ByVal sender As Object, ByVal e As System.Windows.Forms.FormClosingEventArgs) Handles MyBase.FormClosing
        If MessageBox.Show("Are you sure to close this application?", "Close", MessageBoxButtons.YesNo, MessageBoxIcon.Question) =
            DialogResult.Yes Then

            ' Disconnect Modbus
            If objModbusClient IsNot Nothing AndAlso objModbusClient.Connected Then
                objModbusClient.Disconnect()
            End If

            ' Disconnect Modbus
            If objModbusPLCClient IsNot Nothing AndAlso objModbusPLCClient.Connected Then
                objModbusPLCClient.Disconnect()
            End If

            ' Stop Modbus Server
            If objModbusServer IsNot Nothing Then
                Try
                    objModbusServer.StopListening()
                Catch ex As Exception
                End Try

            End If

            ' Close Fanuc Connection
            If bolHandleObtained = True Then
                CloseFOCASHandle(intLibHndl)
            End If

            Timer1.Enabled = False

            ' Disable UI Invokes in Threads
            stopInvoking = True

            ' Sleep 1 second
            Threading.Thread.Sleep(1000)
        Else
            e.Cancel = True
        End If
    End Sub

    Public Sub ShowAlert(ByVal need As Boolean, ByVal msg As String,
                               Optional ByVal title As String = "",
                               Optional ByVal button As MessageBoxButtons = MessageBoxButtons.OK,
                               Optional ByVal icon As MessageBoxIcon = MessageBoxIcon.Information)

        If need = True Then
            MessageBox.Show(msg, title, button, icon)
        End If
    End Sub

    Public Sub ShowAppSettings()
        ' Load Machine IP and Port
    End Sub

    Private Sub ConnectFanuc(Optional ByVal showMessage As Boolean = True)
        reconnectTryTime = GetCurrentTimeMilis()

        ' Check Fanuc Device Information
        bolHandleObtained = False
        ipAddress = GetEncryptedAppSettings(SETTINGS_KEY_FANUCIP)
        portNum = StringToInt(GetEncryptedAppSettings(SETTINGS_KEY_FANUCPORT))

        ' Check Connect Information
        If ipAddress.Trim.Length = 0 OrElse portNum = 0 Then
            ShowAlert(showMessage, "Please input machine information to connect!")
            Return
        End If

        ' Get Current Device Settings
        deviceSetting = New FanucSettings
        macroSettingDic = New Dictionary(Of Integer, MacroDataModel)
        pmcSettingDic = New Dictionary(Of Integer, PMCDataModel)

        ' Scann Macro Data Table
        For i As Integer = 0 To gridViewMacros.Rows.Count - 1
            Dim tableRow As DataGridViewRow = gridViewMacros.Rows(i)
            Dim descObj As Object = tableRow.Cells(1).Value
            Dim modAddrObj As Object = tableRow.Cells(2).Value
            Dim fanucAddrObj As Object = tableRow.Cells(4).Value

            ' Check empty rows
            If isEmptyCellValue(descObj) AndAlso
                isEmptyCellValue(modAddrObj) AndAlso
                isEmptyCellValue(fanucAddrObj) Then

                Continue For
            End If

            ' Save Item data
            Dim descVal As String = getStringVal(descObj)
            Dim modAddrVal As Integer = getIntVal(modAddrObj, -1)
            Dim fanucAddrVal As Integer = getIntVal(fanucAddrObj, -1)

            Dim newMacroSetting As MacroDataModel = New MacroDataModel(descVal, modAddrVal, fanucAddrVal, tableRow)
            deviceSetting.macroSettings.Add(newMacroSetting)

            ' Construct Dictionary
            If modAddrVal <> -1 Then
                ' Has Valid Modbus Address and continue check data duplication about the same address.
                Dim dupItem As MacroDataModel = Nothing
                Try
                    dupItem = macroSettingDic.Item(modAddrVal)
                Catch ex As KeyNotFoundException
                End Try

                If dupItem IsNot Nothing Then
                    ' Macro Setting is duplicate
                    ShowAlert(showMessage, "Setting Error",
                              "Modbus Address Duplication Error!" & vbCrLf & "Please check your Macro setting again.",
                              MessageBoxButtons.OK, MessageBoxIcon.Warning)
                    Return
                Else
                    macroSettingDic.Add(modAddrVal, newMacroSetting)
                End If
            End If

        Next

        ' Scann PMC Data Table
        For i As Integer = 0 To gridViewPMCs.Rows.Count - 1
            Dim tableRow As DataGridViewRow = gridViewPMCs.Rows(i)
            Dim descObj As Object = tableRow.Cells(1).Value
            Dim modAddrObj As Object = tableRow.Cells(2).Value
            Dim fanucAddrObj As Object = tableRow.Cells(4).Value

            ' Check empty rows
            If isEmptyCellValue(descObj) AndAlso
                isEmptyCellValue(modAddrObj) AndAlso
                isEmptyCellValue(fanucAddrObj) Then

                Continue For
            End If

            ' Save Item data
            Dim descVal As String = getStringVal(descObj)
            Dim modAddrVal As Integer = getIntVal(modAddrObj, -1)
            Dim fanucAddrVal As String = getStringVal(fanucAddrObj)

            Dim newPMCSetting As PMCDataModel = New PMCDataModel(descVal, modAddrVal, fanucAddrVal, tableRow)
            deviceSetting.pmcSettings.Add(newPMCSetting)

            ' Construct Dictionary
            If modAddrVal <> -1 Then
                ' Has Valid Modbus Address and continue check data duplication about the same address.
                Dim dupItem As PMCDataModel = Nothing
                Try
                    dupItem = pmcSettingDic.Item(modAddrVal)
                Catch ex As KeyNotFoundException
                End Try

                If dupItem IsNot Nothing Then
                    ' Macro Setting is duplicate
                    ShowAlert(showMessage, "Setting Error",
                              "Modbus Address Duplication Error!" & vbCrLf & "Please check your PMC setting again.",
                              MessageBoxButtons.OK, MessageBoxIcon.Warning)

                    Return
                Else
                    pmcSettingDic.Add(modAddrVal, newPMCSetting)
                End If
            End If
        Next

        ' If valid item is not exist, then show warnning and return
        If deviceSetting.macroSettings.Count = 0 AndAlso deviceSetting.pmcSettings.Count = 0 Then
            ' App Should work without these macro and pmc settings
            ' showWarnning("Warnning!", "Your Macro And PMC Setting doesn't have any valid item." & vbCrLf & "Please check your input again.")
            ' Return
        End If

        ' Save settings for the future use
        SaveFanucSettings(deviceSetting)

        ' Try to Connect
        If GetFocasHandle(ipAddress, portNum, showMessage) < 0 Then
            bolHandleObtained = False
        Else
            ' Save Fanuc Device IP and Port
            SaveEncryptedAppSettings(SETTINGS_KEY_FANUCIP, ipAddress)
            SaveEncryptedAppSettings(SETTINGS_KEY_FANUCPORT, CStr(portNum))

            ' Enable Disconnect Button
            bolHandleObtained = True
            btnDisconnect.Enabled = True
            btnDisconnect.Text = "Disconnect"

            ' Disable Connect Button
            btnConnect.Enabled = False
            btnConnect.Text = "Connected"

            ' Disable User inputs for DataGridView
            gridViewMacros.Columns.Item(1).ReadOnly = True
            gridViewMacros.Columns.Item(2).ReadOnly = True
            gridViewMacros.Columns.Item(4).ReadOnly = True

            ' Start AutoConnect Logic
            needAutoConnect = True

            ' Get the Fanuc machine Info to get the device type
            Dim odbSysInfo As New Focas1.ODBSYS
            If Focas1.cnc_sysinfo(intLibHndl, odbSysInfo) = Focas1.EW_OK Then

                fanucData.addinfo = odbSysInfo.addinfo
                fanucData.max_axis = odbSysInfo.max_axis
                fanucData.cnc_type = New String(odbSysInfo.cnc_type).Trim()
                fanucData.mt_type = New String(odbSysInfo.mt_type).Trim()
                fanucData.series = New String(odbSysInfo.series).Trim()
                fanucData.version = New String(odbSysInfo.version).Trim()
                fanucData.axes = New String(odbSysInfo.axes).Trim()

            End If

        End If

        ' Triggers Periodical Scanning 
        Dim readCycleTime As Integer = 200
        Try
            readCycleTime = Integer.Parse(GetEncryptedAppSettings(SETTINGS_KEY_READ_CYCLE_TIME))
        Catch ex As Exception
        End Try

        Timer1.Interval = readCycleTime

    End Sub


    ' Setting Button
    Private Sub btnSettings_Click(sender As Object, e As EventArgs) Handles btnSettings.Click
        SettingsForm.Show()
        Return
    End Sub

    ' Connect Button
    Private Sub btnConnect_Click(sender As Object, e As EventArgs) Handles btnConnect.Click

        ConnectFanuc()

    End Sub

    ' Disconnect Button
    Private Sub btnDisconnect_Click(sender As Object, e As EventArgs) Handles btnDisconnect.Click
        DisconnectFannuc(False)

        ' In case of User Triggered Disconnect, we don't try auto connect
        needAutoConnect = False
    End Sub

    Private Sub DisconnectFannuc(need As Boolean)

        If bolHandleObtained = True Then
            ShowAlert(need, "Handle closed successfully! - Return code " & CloseFOCASHandle(intLibHndl))
        Else
            ShowAlert(need, "There is no handle open for this application!")
        End If

        bolHandleObtained = False

        ' Enable Connect Button
        btnConnect.Enabled = True
        btnConnect.Text = "Connect"

        ' Disable Disconnect Button
        btnDisconnect.Enabled = False
        btnDisconnect.Text = "Disconnected"

        ' Enable User inputs for DataGridView
        gridViewMacros.Columns.Item(1).ReadOnly = False
        gridViewMacros.Columns.Item(2).ReadOnly = False
        gridViewMacros.Columns.Item(4).ReadOnly = False

    End Sub

    Function GetFocasHandle(ByVal strCNCIPAddressAs As String, ByVal intFOCASPortNumber As Integer, ByVal showError As Boolean, Optional ByVal intFOCASTimeout As Integer = 2)
        Dim retStatus As Integer = Focas1.cnc_allclibhndl3(strCNCIPAddressAs, intFOCASPortNumber, intFOCASTimeout, intLibHndl)

        Dim connMsg As String = ""
        Dim connTitle As String = ""

        Select Case retStatus
            Case -8     ' Allocation of handle number is failed
                'MessageBox.Show("Allocation of handle number has failed!", "Error!", MessageBoxButtons.OK, MessageBoxIcon.Warning)
                connTitle = "Error!"
                connMsg = "Allocation of handle number has failed!"
            Case -15    ' There is no DLL file for each CNC series
                'MessageBox.Show("There is no DLL file for each CNC series!", "Error!", MessageBoxButtons.OK, MessageBoxIcon.Warning)
                connTitle = "Error!"
                connMsg = "There is no DLL file for each CNC series!"
            Case -16    ' Socket communication Error
                'MessageBox.Show("Socket communication Error", "Error!", MessageBoxButtons.OK, MessageBoxIcon.Warning)
                connTitle = "Error!"
                connMsg = "Socket communication Error"
            Case Is > -1
                'MessageBox.Show("Connection is success, The number of handle is " & intHandleNo.ToString, "Success!", MessageBoxButtons.OK, MessageBoxIcon.Information)
                connTitle = "Success!"
                connMsg = "Connection is success, The number of handle is " & intLibHndl.ToString
        End Select

        If showError = True Then
            MessageBox.Show(connMsg, connTitle, MessageBoxButtons.OK, MessageBoxIcon.Information)
        End If

        Return retStatus

    End Function

    Function CloseFOCASHandle(ByVal intHandleID As Integer) As Integer
        Dim intReturnCode As Integer = -9999
        intReturnCode = Focas1.cnc_freelibhndl(intHandleID)
        Return intReturnCode
    End Function

    Public Function MakeInitSettings() As FanucSettings
        Dim newSetting As New FanucSettings
        newSetting.macroSettings.Add(New MacroDataModel("Monitor", 1001, 100))
        newSetting.macroSettings.Add(New MacroDataModel("Tool", 1003, 101))
        newSetting.macroSettings.Add(New MacroDataModel("Section", 1005, 102))
        newSetting.macroSettings.Add(New MacroDataModel("Channel", 1007, 103))
        newSetting.macroSettings.Add(New MacroDataModel("Program Number", 1009, 104))

        newSetting.pmcSettings.Add(New PMCDataModel("Macro Interrupt", 1, "G53.3"))

        SaveFanucSettings(newSetting)

        Return newSetting

    End Function

    Public Sub SaveFanucSettings(ByVal newSetting As FanucSettings)
        If IsNothing(newSetting) Then
            Return
        End If

        Dim result = JsonConvert.SerializeObject(newSetting, Newtonsoft.Json.Formatting.Indented)
        Console.WriteLine(result)

        Try
            Dim strPath As String = Application.StartupPath()
            Dim objStreamWriter As StreamWriter

            'Pass the file path and the file name to the StreamWriter constructor.
            objStreamWriter = New StreamWriter(Path.Combine(strPath, SETTING_FILE), False, Encoding.UTF8)

            'Write a line of text.
            objStreamWriter.Write(result)

            'Close the file.
            objStreamWriter.Close()
        Catch ex As Exception
        End Try
    End Sub

    Public Sub LoadFanucSettings()

        Dim initSetting As New FanucSettings

        Dim objStreamReader As StreamReader
        Dim strLine As String

        ' Get Current Path
        Dim strPath As String = Application.StartupPath()
        'Pass the file path and the file name to the StreamReader constructor.

        Try
            objStreamReader = New StreamReader(Path.Combine(strPath, SETTING_FILE))

            Dim fileContents As String = ""

            'Read the first line of text.
            strLine = objStreamReader.ReadLine

            'Continue to read until you reach the end of the file.
            Do While Not strLine Is Nothing

                fileContents = fileContents & vbCrLf & strLine

                'Write the line to the Console window.
                Console.WriteLine(strLine)

                'Read the next line.
                strLine = objStreamReader.ReadLine
            Loop

            'Close the file.
            objStreamReader.Close()

            ' Parse String to Object, ex
            ' Dim arrayVal As List(Of preferenceModel) = JsonConvert.DeserializeObject(Of List(Of preferenceModel))(RichTextBox2.Text)
            initSetting = JsonConvert.DeserializeObject(Of FanucSettings)(fileContents)

        Catch ex As Exception

        End Try

        If IsNothing(initSetting) OrElse initSetting.IsEmpty() Then
            initSetting = MakeInitSettings()
        End If

        ' Show in the UI
        ShowFanucSettings(initSetting)

    End Sub

    ' Show Settings in the DataGridViews
    Private Sub ShowFanucSettings(fanucSetting As FanucSettings)
        If fanucSetting Is Nothing Then
            Return
        End If

        ' Show Current Macro Settings
        If fanucSetting.macroSettings IsNot Nothing AndAlso fanucSetting.macroSettings.Count > 0 Then
            For i As Integer = 0 To fanucSetting.macroSettings.Count - 1
                Dim macroSetting As MacroDataModel = fanucSetting.macroSettings.Item(i)
                Dim record As DataGridViewRow = gridViewMacros.Rows(gridViewMacros.NewRowIndex).Clone

                With record
                    .Cells(0).Value = i + 1
                    .Cells(1).Value = macroSetting.GetDescription()
                    .Cells(2).Value = macroSetting.GetModbusAddress()
                    .Cells(4).Value = macroSetting.GetFanucAddress()
                End With

                gridViewMacros.Rows.Add(record)
            Next
        End If

        ' Show Current NC Settings
        If fanucSetting.pmcSettings IsNot Nothing AndAlso fanucSetting.pmcSettings.Count > 0 Then
            For i As Integer = 0 To fanucSetting.pmcSettings.Count - 1
                Dim pmcSetting As PMCDataModel = fanucSetting.pmcSettings.Item(i)
                Dim record As DataGridViewRow = gridViewPMCs.Rows(gridViewPMCs.NewRowIndex).Clone

                With record
                    .Cells(0).Value = i + 1
                    .Cells(1).Value = pmcSetting.GetDescription()
                    .Cells(2).Value = pmcSetting.GetModbusAddress()
                    .Cells(4).Value = pmcSetting.GetFanucAddress()
                End With

                gridViewPMCs.Rows.Add(record)
            Next
        End If
    End Sub

    Private Function isValidModbusAddr(ByVal value As Object) As Boolean
        If value Is Nothing Then
            Return False
        End If

        ' Check Modbus Address
        Dim modbusAddr As Integer = -1
        Try
            modbusAddr = Convert.ToInt32(value)
        Catch ex As Exception
        End Try

        If modbusAddr < 0 OrElse modbusAddr >= 65535 Then
            Return False
        End If

        Return True
    End Function

    Private Function isEmptyCellValue(ByVal cellVal As Object) As Boolean
        If IsNothing(cellVal) Then
            Return True
        End If

        Dim stringValue As String = cellVal.ToString.Trim
        If String.IsNullOrEmpty(stringValue) Then
            Return True
        End If

        Return False

    End Function

    Private Function getStringVal(ByVal cellVal As Object) As String
        If IsNothing(cellVal) Then
            Return ""
        End If

        Dim stringValue As String = cellVal.ToString.Trim
        Return stringValue
    End Function

    Private Function getIntVal(ByVal cellVal As Object, Optional ByVal defVal As Integer = -1) As Integer
        If IsNothing(cellVal) Then
            Return defVal
        End If

        Dim intValue As Integer = defVal
        Try
            intValue = Int(cellVal)
        Catch ex As Exception
        End Try

        Return intValue
    End Function

    Private Sub updateRowNo(ByVal dataGridView As DataGridView)
        For i As Integer = 0 To dataGridView.Rows.Count - 1
            dataGridView.Rows(i).Cells(0).Value = i + 1
        Next
    End Sub

    Private Sub gridViewMacros_CellEndEdit(sender As Object, e As DataGridViewCellEventArgs) Handles gridViewMacros.CellEndEdit

        Dim cellObj As Object = gridViewMacros.CurrentRow.Cells(e.ColumnIndex).Value

        If e.ColumnIndex = 1 Then
            ' Check Description
            'If cellObj Is Nothing OrElse cellObj.ToString.Length < 10 Then
            '    MessageBox.Show("Error!")
            '    'gridViewMacros.CurrentRow.Cells(e.ColumnIndex).Value = "Ok?"

            '    If gridViewMacros.Rows.Count - 1 <> e.RowIndex Then
            '        gridViewMacros.Rows.RemoveAt(e.RowIndex)
            '    End If
            'End If
        ElseIf e.ColumnIndex = 2 Then
            ' Check Modbus Address
            If isValidModbusAddr(cellObj) = False Then
                showWarnning("Modbus Address", ERROR_MODBUS_ADDRESS_FORMAT)
                gridViewMacros.CurrentRow.Cells(e.ColumnIndex).Value = Nothing
            End If
        ElseIf e.ColumnIndex = 4 Then
            ' Check Fanuc Address
            If MacroDataModel.isValidFanucAddress(cellObj) = False Then
                showWarnning("Fanuc Address", ERROR_MACRO_ADDRESS_FORMAT)
                gridViewMacros.CurrentRow.Cells(e.ColumnIndex).Value = Nothing
            End If
        End If

        updateRowNo(gridViewMacros)
    End Sub

    Private Sub gridViewMacros_RowsRemoved(sender As Object, e As DataGridViewRowsRemovedEventArgs) Handles gridViewMacros.RowsRemoved
        ' Update Row Numbers
        updateRowNo(gridViewMacros)
    End Sub

    Private Sub gridViewPMCs_CellEndEdit(sender As Object, e As DataGridViewCellEventArgs) Handles gridViewPMCs.CellEndEdit
        Dim cellObj As Object = gridViewPMCs.CurrentRow.Cells(e.ColumnIndex).Value

        If e.ColumnIndex = 1 Then
            ' Check Description
            'If cellObj Is Nothing OrElse cellObj.ToString.Length < 10 Then
            '    MessageBox.Show("Error!")
            '    'gridViewPMCs.CurrentRow.Cells(e.ColumnIndex).Value = "Ok?"

            '    If gridViewPMCs.Rows.Count - 1 <> e.RowIndex Then
            '        gridViewPMCs.Rows.RemoveAt(e.RowIndex)
            '    End If
            'End If
        ElseIf e.ColumnIndex = 2 Then
            ' Check Modbus Address
            If isValidModbusAddr(cellObj) = False Then
                showWarnning("Modbus Address", ERROR_MODBUS_ADDRESS_FORMAT)
                gridViewPMCs.CurrentRow.Cells(e.ColumnIndex).Value = Nothing
            End If
        ElseIf e.ColumnIndex = 4 Then
            ' Check Fanuc Address
            If PMCDataModel.isValidFanucAddress(cellObj) = False Then
                showWarnning("Fanuc Address", ERROR_PMC_ADDRESS_FORMAT)
                gridViewPMCs.CurrentRow.Cells(e.ColumnIndex).Value = Nothing
            End If
        End If

        updateRowNo(gridViewPMCs)
    End Sub

    Private Sub gridViewPMCs_RowsRemoved(sender As Object, e As DataGridViewRowsRemovedEventArgs) Handles gridViewPMCs.RowsRemoved
        ' Update Row Numbers
        updateRowNo(gridViewPMCs)
    End Sub

    Private Sub showWarnning(ByVal title As String, ByVal message As String)
        MessageBox.Show(message, title, MessageBoxButtons.OK, MessageBoxIcon.Warning)
    End Sub

    Private Sub showError(ByVal title As String, ByVal message As String)
        MessageBox.Show(message, title, MessageBoxButtons.OK, MessageBoxIcon.Error)
    End Sub

    Private Sub Timer1_Tick(sender As Object, e As EventArgs) Handles Timer1.Tick

        ' Show Auto Monitor Status
        Dim autoMonitorStatus As Integer = StringToInt(GetEncryptedAppSettings(SETTINGS_KEY_AUTO_MONITOR))
        Dim autoMonitorEnable As Boolean = IIf(autoMonitorStatus > 0, True, False)
        If autoMonitorEnable <> chkAutoMonitor.Checked Then
            chkAutoMonitor.Checked = autoMonitorEnable
        End If

        Dim spindleTimeSetting As String = GetEncryptedAppSettings(SETTINGS_KEY_SPINDLE_TIME)
        If spindleTimeSetting.Equals(txtSpindleTime.Text) = False Then
            txtSpindleTime.Text = spindleTimeSetting
        End If

        Dim spindleThresholdSetting As String = GetEncryptedAppSettings(SETTINGS_KEY_SPINDLE_THRESHOLD)
        If spindleThresholdSetting.Equals(txtThreshold.Text) = False Then
            txtThreshold.Text = spindleThresholdSetting
        End If

        ' Show Full Date Time
        lblDateTime.Text = GetDayTimeFullString()

        If bolHandleObtained = True Then
            ' Get NC Values
            GetNCData()

            ' Get PMC Values
            GetPMCData()

            ' Get Timers
            GetCNCTimers()

            ' Get Tool Info
            GetCurrToolInfo()

            ' Get Spindle Data
            Dim autoMonitorCheck As Boolean = StringToInt(GetEncryptedAppSettings(SETTINGS_KEY_AUTO_MONITOR)) > 0
            If autoMonitorCheck = True Then
                GetSpindleData()

                lblCurrSpindleRPM.Text = String.Format("Curr Speed(rpm):{0:f2}", fanucData.spindleSpeed)

                Dim newAutoMonitorStatus As Boolean = False

                Dim spindleSpeedThreshold As Double = StringToDouble(GetEncryptedAppSettings(SETTINGS_KEY_SPINDLE_THRESHOLD))
                Dim spindleDelayTime As Double = StringToDouble(GetEncryptedAppSettings(SETTINGS_KEY_SPINDLE_TIME))

                If spindleSpeedThreshold > 0 AndAlso fanucData.spindleSpeed >= spindleSpeedThreshold Then
                    If fanucData.autoMonitorStartTime = 0 Then
                        fanucData.autoMonitorStartTime = GetCurrentTimeMilis()
                    ElseIf Math.Abs(GetCurrentTimeMilis() - fanucData.autoMonitorStartTime) > spindleDelayTime Then
                        newAutoMonitorStatus = True
                    End If
                Else
                    fanucData.autoMonitorStartTime = 0
                End If

                If fanucData.currAutoMonitorStatus <> newAutoMonitorStatus Then
                    If newAutoMonitorStatus Then
                        objModbusServer.mHoldingRegisters.Item(1001) = ConvertUShortToShort(signalOnArray(0))
                        objModbusServer.mHoldingRegisters.Item(1002) = ConvertUShortToShort(signalOnArray(1))
                    Else
                        objModbusServer.mHoldingRegisters.Item(1001) = ConvertUShortToShort(signalOffArray(0))
                        objModbusServer.mHoldingRegisters.Item(1002) = ConvertUShortToShort(signalOffArray(1))

                        ' Switched to Turned OFF and reset TCS
                        ResetTCS()
                    End If

                    fanucData.currAutoMonitorStatus = newAutoMonitorStatus
                End If

            End If

            ' Get Program Info
            Dim ret1 As Boolean = GetProgramInfo()

            ' Get State
            Dim ret2 As Boolean = GetStateInfo()

            ' Get Alarm
            Dim ret3 As Boolean = GetAlarmInfo()

            Dim ret = ret1 Or ret2 Or ret3

            If ret = False Then
                DisconnectFannuc(False)
            End If

        Else
            fanucData.run = -1
            fanucData.spindleSpeed = 0
        End If

        ' Check Run status: Send to Incycle Status for TOS
        If fanucData.run = 3 Then
            objModbusServer.mHoldingRegisters.Item(1011) = ConvertUShortToShort(signalOnArray(0))
            objModbusServer.mHoldingRegisters.Item(1012) = ConvertUShortToShort(signalOnArray(1))
        Else
            objModbusServer.mHoldingRegisters.Item(1011) = ConvertUShortToShort(signalOffArray(0))
            objModbusServer.mHoldingRegisters.Item(1012) = ConvertUShortToShort(signalOffArray(1))
        End If

        ' Check FEED FOLD Status
        Dim feedFoldStatus As Boolean = False
        If fanucData.cnc_type.Equals("15") Then
            ' Series 15/15i  
            ' 1: HOLD
            If fanucData.run = 1 OrElse fanucData.run = 0 Then
                feedFoldStatus = True
            End If
        ElseIf fanucData.mt_type.Equals("W") AndAlso (fanucData.cnc_type.Equals("16") OrElse
                fanucData.cnc_type.Equals("18")) Then
            ' Series 16i/18i-W
            ' 3: F-HOLD
            If fanucData.run = 3 OrElse fanucData.run = 4 Then
                feedFoldStatus = True
            End If
        ElseIf fanucData.cnc_type.Equals("16") OrElse
            fanucData.cnc_type.Equals("18") OrElse
            fanucData.cnc_type.Equals("21") OrElse
            fanucData.cnc_type.Equals("0") OrElse
            fanucData.cnc_type.Equals("30") OrElse
            fanucData.cnc_type.Equals("31") OrElse
            fanucData.cnc_type.Equals("32") OrElse
            fanucData.cnc_type.Equals("PH") Then
            ' Series 16/18/21, 16i/18i/21i, 0i, 30i/31i/32i, Power Mate i, PMi-A, odbst
            ' 2: HOLD
            If fanucData.run = 2 OrElse fanucData.run = 1 Then
                feedFoldStatus = True
            End If
        Else
            ' No case
        End If

        lblCncType.Text = "CNC TYPE: " & fanucData.cnc_type & "-" & fanucData.mt_type
        lblRunVal.Text = "RUN VALUE: " & Str(fanucData.run) & "-" & Str(fanucData.motion)
        lblFeedFoldStatus.Text = "FEED FOLD: " & IIf(feedFoldStatus, "ON", "OFF")

        If feedFoldStatus Then
            objModbusServer.mHoldingRegisters.Item(1013) = ConvertUShortToShort(signalOnArray(0))
            objModbusServer.mHoldingRegisters.Item(1014) = ConvertUShortToShort(signalOnArray(1))
        Else
            objModbusServer.mHoldingRegisters.Item(1013) = ConvertUShortToShort(signalOffArray(0))
            objModbusServer.mHoldingRegisters.Item(1014) = ConvertUShortToShort(signalOffArray(1))
        End If
        ' -------------------------------------------------------------------------------------

        ' Fanuc Connection Status
        If bolHandleObtained = True Then
            objModbusServer.mHoldingRegisters.Item(1015) = ConvertUShortToShort(signalOnArray(0))
            objModbusServer.mHoldingRegisters.Item(1016) = ConvertUShortToShort(signalOnArray(1))
        Else
            objModbusServer.mHoldingRegisters.Item(1015) = ConvertUShortToShort(signalOffArray(0))
            objModbusServer.mHoldingRegisters.Item(1016) = ConvertUShortToShort(signalOffArray(1))
        End If

        ' Get Current Sequence Number
        If bolHandleObtained = True AndAlso GetCurrentSeqNum() = True Then
            Dim seqValArray As Integer() = EasyModbus.ModbusClient.ConvertFloatToRegisters(fanucData.currSequNum)
            objModbusServer.mHoldingRegisters.Item(1017) = ConvertUShortToShort(seqValArray(0))
            objModbusServer.mHoldingRegisters.Item(1018) = ConvertUShortToShort(seqValArray(1))
        Else
            objModbusServer.mHoldingRegisters.Item(1017) = ConvertUShortToShort(signalOffArray(0))
            objModbusServer.mHoldingRegisters.Item(1018) = ConvertUShortToShort(signalOffArray(1))
        End If
        lblSeqNum.Text = "SeqNum:" & fanucData.currSequNum

        ' Get T, S, F, M Modal data
        GetModalData()

        ' Try to reconnect, 10s Intervals
        If bolHandleObtained = False AndAlso needAutoConnect Then
            ' Disconnect status, Reconnect modules
            If timeCurrentMilis - reconnectTryTime > 10000 Then
                ConnectFanuc(False)
            End If
        End If

    End Sub

    Private Sub GetNCData()

        ' https://www.inventcom.net/fanuc-focas-library/ncdata/cnc_rdmacror
        ' Dynamic Data Structure Fields
        ' https://docs.microsoft.com/en-us/dotnet/visual-basic/programming-guide/language-features/data-types/structures-And-other-programming-elements

        ' Check Macro Data
        If deviceSetting Is Nothing OrElse deviceSetting.macroSettings Is Nothing OrElse deviceSetting.macroSettings.Count = 0 Then
            Return
        End If

        Dim macroData As New Focas1.IODBMR
        macroData.data.data1 = New Focas1.IODBMR_data
        macroData.data.data2 = New Focas1.IODBMR_data
        macroData.data.data3 = New Focas1.IODBMR_data
        macroData.data.data4 = New Focas1.IODBMR_data
        macroData.data.data5 = New Focas1.IODBMR_data

        Dim length As Integer = 1   ' Always read 1 register at a once

        macroData.data.data1 = New Focas1.IODBMR_data
        For ncIndex As Integer = 0 To deviceSetting.macroSettings.Count - 1
            Dim macroItem As MacroDataModel = deviceSetting.macroSettings.Item(ncIndex)
            Dim macroItemRow As DataGridViewRow = macroItem.getGridViewRow()
            If macroItem.hasValidFanucAddress() = False Then

                macroItemRow.Cells(6).Value = "Fanuc Address Error!"
                Continue For
            End If

            Dim macroVar As Integer = macroItem.GetFanucAddress()

            Dim retVal As Short = Focas1.cnc_rdmacror(intLibHndl, macroVar, macroVar + length - 1, 8 + 8 * length, macroData)

            If retVal = Focas1.EW_OK Then
                Dim returnData As Focas1.IODBMR_data

                returnData = macroData.data.data1
                'returnData(1) = macroData.data.data2
                'returnData(2) = macroData.data.data3
                'returnData(3) = macroData.data.data4
                'returnData(4) = macroData.data.data5

                Dim macroVal As Single = 0
                If returnData.dec_val > 0 Then
                    macroVal = 1.0 * returnData.mcr_val / (10 ^ returnData.dec_val)
                Else
                    macroVal = returnData.mcr_val
                End If

                macroItemRow.Cells(5).Value = CStr(macroVal)

                If isValidModbusAddr(macroItem.GetModbusAddress()) Then
                    Dim modbusAddr As Integer = macroItem.GetModbusAddress()

                    If modbusAddr = 1001 Then
                        ' Check Auto Monitor Status and skip writting the modbus
                        Dim autoMonitorCheck As Boolean = StringToInt(GetEncryptedAppSettings(SETTINGS_KEY_AUTO_MONITOR)) > 0
                        If autoMonitorCheck Then
                            fanucData.currAutoMonitorStatus = IIf(macroVal > 0, True, False)
                            Continue For
                        End If
                    End If

                    Dim realValArray As Integer() = EasyModbus.ModbusClient.ConvertFloatToRegisters(macroVal)
                    objModbusServer.mHoldingRegisters.Item(modbusAddr + 0) = ConvertUShortToShort(realValArray(0))
                    objModbusServer.mHoldingRegisters.Item(modbusAddr + 1) = ConvertUShortToShort(realValArray(1))

                    macroItemRow.Cells(6).Value = "All is Ok!" & " " & CStr(returnData.mcr_val) & " " & CStr(returnData.dec_val)
                Else
                    macroItemRow.Cells(6).Value = "Fanuc Ok, Modbus Error!"
                End If
            Else
                macroItemRow.Cells(6).Value = GetFanucMessage(retVal)
            End If

        Next
    End Sub

    Private Sub GetPMCData()
        ' https://www.inventcom.net/fanuc-focas-library/pmc/pmc_rdpmcrng

        ' Check PMC Data
        If deviceSetting Is Nothing OrElse deviceSetting.pmcSettings Is Nothing OrElse deviceSetting.pmcSettings.Count = 0 Then
            Return
        End If

        Dim address_s As Integer = 13   ' G013.1
        Dim bitIndex As Integer = 1
        Dim length As Integer = 1
        Dim address_e As Integer = address_s + length - 1

        Dim adr_type As Short = 0 ' means G address
        Dim data_type As Short = 0 ' means Bye data

        Dim pmcAddrData As New Focas1.IODBPMC0
        ReDim pmcAddrData.cdata(10)

        For pmcIndex As Integer = 0 To deviceSetting.pmcSettings.Count - 1
            Dim pmcItem As PMCDataModel = deviceSetting.pmcSettings.Item(pmcIndex)
            Dim pmcItemRow As DataGridViewRow = pmcItem.getGridViewRow()

            If pmcItem.hasValidFanucAddress() = False Then
                pmcItemRow.Cells(6).Value = "Fanuc Address Error!"
                Continue For
            End If

            adr_type = pmcItem.getAdrType()
            address_s = pmcItem.getFanucRegisterAddress()
            address_e = address_s + length - 1
            bitIndex = pmcItem.getFanucBitAddress()

            ' Check Address
            If adr_type = -1 OrElse address_s = -1 OrElse bitIndex = -1 Then
                pmcItemRow.Cells(6).Value = "Fanuc Address Error!"
                Continue For
            End If

            ' Read Real Device Value
            Dim retVal As Short = Focas1.pmc_rdpmcrng(intLibHndl, adr_type, data_type, address_s, address_e, 8 + length, pmcAddrData)

            If retVal = Focas1.EW_OK Then
                Dim byteVal As Byte = pmcAddrData.cdata(0)
                Dim bitVal As Boolean = GetBit(byteVal, bitIndex)

                pmcItemRow.Cells(5).Value = CStr(bitVal)

                If isValidModbusAddr(pmcItem.GetModbusAddress()) Then
                    Dim modbusAddr As Integer = pmcItem.GetModbusAddress()

                    objModbusServer.mCoils.Item(modbusAddr) = bitVal

                    pmcItemRow.Cells(6).Value = "All is Ok!" & " " & CStr(intLibHndl)
                Else
                    pmcItemRow.Cells(6).Value = "Fanuc Ok, Modbus Error!" & " " & CStr(intLibHndl)
                End If
            Else
                pmcItemRow.Cells(6).Value = GetFanucMessage(retVal)
            End If
        Next
    End Sub

    Private Sub GetSpindleData()
        ' https://www.inventcom.net/fanuc-focas-library/position/cnc_rdspeed
        Dim spindleInfo As New Focas1.ODBSPEED

        Try
            ' Read Real Device Value
            Dim retVal As Short = Focas1.cnc_rdspeed(intLibHndl, 1, spindleInfo)

            If retVal = Focas1.EW_OK Then
                fanucData.spindleSpeed = 1.0 * spindleInfo.acts.data / (10 ^ spindleInfo.acts.dec)
            Else
                fanucData.spindleSpeed = 0
            End If
        Catch ex As Exception
        End Try
    End Sub

    Private Sub GetCNCTimers()
        ' https://www.inventcom.net/fanuc-focas-library/misc/cnc_rdtimer

        ' Target CNC doesn't support these function and now disable it

        ' Dim timeData As New Focas1.IODBTIME
        ' Dim lblTimes() As Label = {lblTime1, lblTime2, lblTime3, lblTime4, lblTime5}
        ' Dim lblPrefix() As String = {"Power on time:", "Operating time:", "Cutting time:", "Cycle time:", "Free purpose:"}
        ' For type As Integer = 0 To 4

        ' ' Read Real Device Value
        ' Dim retVal As Short = Focas1.cnc_rdtimer(intLibHndl, type, timeData)

        ' If retVal = Focas1.EW_OK Then
        ' Dim mins As Integer = timeData.minute
        ' Dim miseconds As Integer = timeData.msec
        ' Dim totSeconds = mins * 60 + miseconds / 1000
        '
        ' lblTimes(type).Text = lblPrefix(type) & GetTimeStringFromSeconds(totSeconds)
        ' Else
        ' lblTimes(type).Text = lblPrefix(type) & GetFanucMessage(retVal)
        ' End If
        ' Next
    End Sub

    Private Function GetProgramInfo() As Boolean
        ' https://www.inventcom.net/fanuc-focas-library/program/cnc_rdprgnum
        Dim progInfo As New Focas1.ODBPRO
        ReDim progInfo.dummy(2)

        Try
            ' Read Real Device Value
            Dim retVal As Short = Focas1.cnc_rdprgnum(intLibHndl, progInfo)

            If retVal = Focas1.EW_OK Then


                ' Main and Current Program Information
                fanucData.mainProgram = String.Format("O{0}", progInfo.data)
                fanucData.currentProgram = String.Format("O{0}", progInfo.mdata)

                Return True
            Else
                Dim errorString As String = GetFanucMessage(retVal)

                fanucData.mainProgram = ""  'errorString
                fanucData.currentProgram = ""

                Return False
            End If
        Catch ex As Exception
        End Try

        Return False
    End Function

    Private Function GetStateInfo() As Boolean
        ' https://www.inventcom.net/fanuc-focas-library/misc/cnc_statinfo
        Dim stateInfo As New Focas1.ODBST

        ' Read Real Device Value
        Dim retVal As Short = Focas1.cnc_statinfo(intLibHndl, stateInfo)

        If retVal = Focas1.EW_OK Then
            lblStateInfo.Text = GetStatusValueWithTitle(STATE_INFO_FIELD.AUT, stateInfo.aut) & vbCrLf &
                GetStatusValueWithTitle(STATE_INFO_FIELD.RUN, stateInfo.run) & vbCrLf &
                GetStatusValueWithTitle(STATE_INFO_FIELD.MOTION, stateInfo.motion) & vbCrLf &
                GetStatusValueWithTitle(STATE_INFO_FIELD.MSTB, stateInfo.mstb) & vbCrLf &
                GetStatusValueWithTitle(STATE_INFO_FIELD.EMERGENCY, stateInfo.emergency) & vbCrLf &
                GetStatusValueWithTitle(STATE_INFO_FIELD.ALARM, stateInfo.alarm)

            ' Motion and Run Information
            fanucData.motion = stateInfo.motion
            fanucData.run = stateInfo.run

            Return True
        Else
            Dim errorString As String = GetFanucMessage(retVal)
            lblStateInfo.Text = errorString

            fanucData.motion = -1
            fanucData.run = -1

            Return False
        End If

    End Function

    Private Function GetAlarmInfo() As Boolean
        ' https://www.inventcom.net/fanuc-focas-library/misc/cnc_alarm
        Dim alarmInfo As New Focas1.ODBALM
        Dim lblAlarm() As String = {
                    "P/S 100 ALARM",
                    "P/S 000 ALARM",
                    "P/S 101 ALARM",
                    "P/S ALARM (1-255)",
                    "OT ALARM",
                    "OH ALARM",
                    "SERVO ALARM",
                    "SYSTEM ALARM",
                    "APC ALARM",
                    "SPINDLE ALARM",
                    "P/S ALARM (5000-)"}

        ' Read Real Device Value
        Dim retVal As Short = Focas1.cnc_alarm(intLibHndl, alarmInfo)

        If retVal = Focas1.EW_OK Then
            If alarmInfo.data = 0 Then

            Else
                Dim uShortVal As UShort = ConvertShortToUShort(alarmInfo.data)
                Dim strAlarm As String = "Alarm:(" & uShortVal & ")"

                For idx = 0 To 10
                    If (uShortVal And 1) > 0 Then
                        strAlarm = strAlarm & vbCrLf & lblAlarm(idx)
                    End If
                    uShortVal >>= 1
                Next


                fanucData.alarm = strAlarm

            End If

            Return True
        Else
            Dim errorString As String = GetFanucMessage(retVal)

            fanucData.alarm = errorString

            Return False
        End If

    End Function

    Private Function GetCurrentSeqNum() As Boolean
        ' https://www.inventcom.net/fanuc-focas-library/program/cnc_rdseqnum

        Dim odbSeqData As New Focas1.ODBSEQ
        ReDim odbSeqData.dummy(10)
        Dim retVal As Short = Focas1.cnc_rdseqnum(intLibHndl, odbSeqData)

        If retVal = Focas1.EW_OK Then
            fanucData.currSequNum = odbSeqData.data
            Return True
        Else
            fanucData.currSequNum = 0
            Return False
        End If

    End Function

    Private Function GetModalDataString(modal As Focas1.ODBMDL_3, Optional isFData As Boolean = False) As String
        Dim logString As String = ""
        If isFData = True Then

            Dim gData As Single = modal.aux.aux_data
            If modal.aux.flag1 And &H40 > 0 Then
                gData = 1.0 * modal.aux.aux_data / (10 ^ modal.aux.flag2)
            End If

            logString = "DataNo: " & CStr(modal.datano) & vbCrLf &
                    "Type: " & CStr(modal.type) & vbCrLf &
                    "G_Data: " & CStr(gData) & vbCrLf
        Else
            logString = "DataNo: " & CStr(modal.datano) & vbCrLf &
                    "Type: " & CStr(modal.type) & vbCrLf &
                    "G_Data: " & CStr(modal.aux.aux_data) & vbCrLf
        End If
        Return logString

    End Function

    Private Function GetModalData() As Boolean
        Dim modal As New Focas1.ODBMDL_3
        modal.aux = New Focas1.MODAL_AUX_data

        Dim logString As String = ""

        ' Read Modal Tool
        If ReadModalValue(108, 0, modal, logString) Then
            objModbusServer.mHoldingRegisters.Item(1019) = modal.datano
            objModbusServer.mHoldingRegisters.Item(1020) = modal.type
            objModbusServer.mHoldingRegisters.Item(1021) = GetXValue(modal.aux.aux_data)
        Else
            objModbusServer.mHoldingRegisters.Item(1019) = 0
            objModbusServer.mHoldingRegisters.Item(1020) = 0
            objModbusServer.mHoldingRegisters.Item(1021) = 0
        End If
        lblTValues.Text = GetModalDataString(modal)

        ' Read Modal Spindle
        If ReadModalValue(107, 0, modal, logString) Then
            objModbusServer.mHoldingRegisters.Item(1022) = modal.datano
            objModbusServer.mHoldingRegisters.Item(1023) = modal.type
            objModbusServer.mHoldingRegisters.Item(1024) = GetXValue(modal.aux.aux_data)
        Else
            objModbusServer.mHoldingRegisters.Item(1022) = 0
            objModbusServer.mHoldingRegisters.Item(1023) = 0
            objModbusServer.mHoldingRegisters.Item(1024) = 0
        End If
        lblSValues.Text = GetModalDataString(modal)

        ' Read Modal Feedrate
        If ReadModalValue(103, 0, modal, logString) Then
            objModbusServer.mHoldingRegisters.Item(1025) = modal.datano
            objModbusServer.mHoldingRegisters.Item(1026) = modal.type

            Dim gData As Single = modal.aux.aux_data
            If modal.aux.flag1 And &H40 > 0 Then
                gData = 1.0 * modal.aux.aux_data / (10 ^ modal.aux.flag2)
            End If

            Dim seqValArray As Integer() = EasyModbus.ModbusClient.ConvertFloatToRegisters(gData)

            objModbusServer.mHoldingRegisters.Item(1027) = ConvertUShortToShort(seqValArray(0))
            objModbusServer.mHoldingRegisters.Item(1028) = ConvertUShortToShort(seqValArray(1))
        Else
            objModbusServer.mHoldingRegisters.Item(1025) = 0
            objModbusServer.mHoldingRegisters.Item(1026) = 0
            objModbusServer.mHoldingRegisters.Item(1027) = 0
            objModbusServer.mHoldingRegisters.Item(1028) = 0
        End If
        lblFValues.Text = GetModalDataString(modal, True)

        ' Read Modal MC
        If ReadModalValue(106, 0, modal, logString) Then
            objModbusServer.mHoldingRegisters.Item(1029) = modal.datano
            objModbusServer.mHoldingRegisters.Item(1030) = modal.type
            objModbusServer.mHoldingRegisters.Item(1031) = GetXValue(modal.aux.aux_data)
        Else
            objModbusServer.mHoldingRegisters.Item(1029) = 0
            objModbusServer.mHoldingRegisters.Item(1030) = 0
            objModbusServer.mHoldingRegisters.Item(1031) = 0
        End If
        lblMValues.Text = GetModalDataString(modal)

        Return True
    End Function

    Public Function GetXValue(lParam As UInt32) As Short
        Return CShort(lParam And &HFFFF)
    End Function

    Public Function GetGDataValue(modal As Focas1.ODBMDL_3) As Short
        If modal.aux.flag1 And &H40 > 0 Then
            Dim gDataVal As Integer = modal.aux.aux_data / (10 ^ modal.aux.flag2)
            Return GetXValue(gDataVal)
        Else
            Return GetXValue(modal.aux.aux_data)
        End If
    End Function

    ' Get Current Tool Info
    Private Sub GetCurrToolInfo()
        Dim odbTLife As New Focas1.ODBTLIFE4
        ' Tool group number.
        ' The currently used tool group and tool are referred by specifying 0 in "grp_num". However, in case that any tool group number has never be specified since power-on, 0 is stored.
        odbTLife.datano = 0

        ' Tool use-order number.
        ' In case of specifying 0 in "tuse_num", either the currently used tool for already used tool group or the first tool for not-used tool group is referred.
        odbTLife.type = 0

        ' Read Tool Number
        Dim retVal As Short = GetToolNumber(odbTLife)
        If retVal = Focas1.EW_OK Then

            Dim toolInfoString As String = "Current Tool Num: " & CStr(odbTLife.data)

            ' Read Tool Life Status
            retVal = GetToolInfo(odbTLife)
            If retVal = Focas1.EW_OK Then
                Dim toolLife As Integer = odbTLife.data
                If toolLife = 0 Then

                ElseIf toolLife = 1 Then
                    toolInfoString = toolInfoString & " Tool Life: " & "This tool is registered(available)."
                ElseIf toolLife = 2 Then
                    toolInfoString = toolInfoString & " Tool Life: " & "This tool has expired."
                ElseIf toolLife = 3 Then
                    toolInfoString = toolInfoString & " Tool Life: " & "This tool was skipped."
                Else
                    toolInfoString = toolInfoString & " Tool Life: " & "Unknown Value"
                End If

                lblCurrentToolNum.Text = toolInfoString
            Else
                lblCurrentToolNum.Text = toolInfoString & " Tool Life: " & GetFanucMessage(retVal)
            End If
        Else
            lblCurrentToolNum.Text = "Current Tool Num: " & GetFanucMessage(retVal)
        End If

    End Sub

    ' Get Current Tool Number
    Private Function GetToolNumber(ByRef odbTLife As Focas1.ODBTLIFE4) As Short
        ' https://www.inventcom.net/fanuc-focas-library/toollife/cnc_toolnum
        ' Dim odbTLife As New Focas1.ODBTLIFE4

        Try
            ' Read Real Device Value
            Dim retVal As Short = Focas1.cnc_toolnum(intLibHndl, 0, 0, odbTLife)

            If retVal = Focas1.EW_OK Then
                Return retVal
            Else
                Return retVal
            End If
        Catch ex As Exception
        End Try

        Return False
    End Function

    ' Get Current Tool Info
    Private Function GetToolInfo(ByRef odbTLife As Focas1.ODBTLIFE4) As Short
        ' https://www.inventcom.net/fanuc-focas-library/toollife/cnc_t1info

        'Dim odbTLife As New Focas1.ODBTLIFE4

        Try
            ' Read Real Device Value
            Dim retVal As Short = Focas1.cnc_t1info(intLibHndl, 0, 0, odbTLife)

            If retVal = Focas1.EW_OK Then
                'Return odbTLife.data
                '0   (refer to the following)
                '1   This tool Is registered(available).
                '2   This tool has expired.
                '3   This tool was skipped.
                Return retVal
            Else
                Return retVal
            End If
        Catch ex As Exception
        End Try

        Return False
    End Function

    Private Sub ResetTCS()

        If deviceSetting Is Nothing OrElse deviceSetting.macroSettings Is Nothing OrElse deviceSetting.macroSettings.Count = 0 Then
            Return
        End If

        ' Make new Connection
        Dim intNewHandleNo As Integer
        Dim intNewLibHndl As Integer
        intNewHandleNo = Focas1.cnc_allclibhndl3(ipAddress, portNum, 5, intNewLibHndl)
        If intNewHandleNo < 0 Then
            Return
        End If

        ' https://www.inventcom.net/fanuc-focas-library/ncdata/cnc_wrmacror

        For addIdx As Integer = 0 To deviceSetting.macroSettings.Count - 1
            Dim addrModbus As Integer = deviceSetting.macroSettings(addIdx).GetModbusAddress()
            Dim addrMacro As Integer = deviceSetting.macroSettings(addIdx).GetFanucAddress()

            If addrModbus = 1003 OrElse addrModbus = 1005 OrElse addrModbus = 1007 Then
                Dim macroData As New Focas1.IODBMR
                Dim number As Integer = 1
                macroData.datano_s = addrMacro
                macroData.datano_e = addrMacro + number - 1 ' We only write One address
                macroData.data.data1.dec_val = 3
                macroData.data.data1.mcr_val = 0 * (10 ^ 3) ' Write 0

                Dim retVal As Short = Focas1.cnc_wrmacror(intNewLibHndl, 8 + 8 * number, macroData)

                If retVal = Focas1.EW_OK Then
                    ' "Success!"
                Else
                    ' GetFanucMessage(retVal)
                End If
            End If
        Next

        'Close Connection
        Focas1.cnc_freelibhndl(intNewLibHndl)

    End Sub

    Sub processToolLifeStatus(newStatus As Integer)

        ' Make new Connection
        Dim intNewHandleNo As Integer
        Dim intNewLibHndl As Integer
        intNewHandleNo = Focas1.cnc_allclibhndl3(ipAddress, portNum, 5, intNewLibHndl)
        If intNewHandleNo < 0 Then
            Return
        End If

        ' Logic 1, not used now
        'If (TOS_wear_signal == High && Machine_Tool_Wear_Status == Low && wear_switch == 0) Then {
        'set Machine_Tool_Wear_Status = HIGH
        'wear_switch = 1
        '}
        'If (machine_current_tool change to New value && TOS_wear_signal == LOW) {
        'wear_switch = 0
        '}

        ' Logic 2, Current used
        'If TOS_wear_signal == Low Then Set Machine_Tool_Wear_Status = 1 

        'If TOS_wear_signal == High Then Set Machine_Tool_Wear_Status = 2

        Dim TOS_wear_signal As Boolean = False
        Dim Machine_Tool_Wear_Status As Integer = 1
        If newStatus = 0 Then
            TOS_wear_signal = True
            Machine_Tool_Wear_Status = 3
        End If

        Dim iodbtdRead As New Focas1.IODBTD

        Try
            ' Read Real Device Value
            Dim retVal As Short = Focas1.cnc_rd1tlifedata(intNewLibHndl, 0, 0, iodbtdRead)

            If retVal = Focas1.EW_OK Then

                Dim iodbtdWrite As New Focas1.IODBTD

                iodbtdWrite.datano = iodbtdRead.datano
                iodbtdWrite.type = iodbtdRead.tool_num
                iodbtdWrite.tool_num = iodbtdRead.tool_num
                iodbtdWrite.h_code = iodbtdRead.h_code
                iodbtdWrite.d_code = iodbtdRead.d_code

                iodbtdWrite.tool_inf = Machine_Tool_Wear_Status
                retVal = Focas1.cnc_wr1tlifedata(intNewLibHndl, iodbtdWrite)

                lblTWriteStatus.Text = "Write to CNC:" & CStr(retVal)
            Else
                lblTWriteStatus.Text = "Failed to read CNC LifeData:" & CStr(retVal)
            End If
        Catch ex As Exception
        End Try

        'Close Connection
        Focas1.cnc_freelibhndl(intNewLibHndl)
    End Sub

    ' HoldingRegisters Changes Handler
    Protected Sub HoldingRegistersChangedHandler(registerAddr As Integer, numberOfRegisters As Integer) Handles objModbusServer.HoldingRegistersChanged
        ' Read new Modbus Registers
        Dim registers = New Integer() {
            objModbusServer.mHoldingRegisters.Item(registerAddr),
            objModbusServer.mHoldingRegisters.Item(registerAddr + 1)
        }

        If numberOfRegisters = 1 Then
            registers(1) = 0
        End If


        ' Process the Tool Wear Signal
        If registerAddr = 1101 Then
            Dim newCustomValue As Integer = ModbusClient.ConvertRegistersToFloat(registers)

            processToolLifeStatus(newCustomValue)

            Return
        End If

        ' Process PID Controller HP Value
        If registerAddr = 1103 Then

            setPointValue = ModbusClient.ConvertRegistersToFloat(registers)
            lblSP.Invoke(
                Sub()
                    lblSP.Text = setPointValue.ToString("F2")
                    trackBarSP.Value = setPointValue * 10
                End Sub)
            Return
        End If

        ' Process Auto Monitor Status
        If registerAddr = 1105 Then

            ' Check Data length and process exception
            If numberOfRegisters <> 6 Then
                Return
            End If

            Dim autoMonitorStatus As Boolean = IIf(ModbusClient.ConvertRegistersToFloat(registers) > 0, True, False)

            registers = New Integer() {
                objModbusServer.mHoldingRegisters.Item(1107),
                objModbusServer.mHoldingRegisters.Item(1108)}
            Dim speedThreshold As Single = ModbusClient.ConvertRegistersToFloat(registers)

            registers = New Integer() {
                objModbusServer.mHoldingRegisters.Item(1109),
                objModbusServer.mHoldingRegisters.Item(1110)}
            Dim retainTime As Long = ModbusClient.ConvertRegistersToFloat(registers)

            SaveEncryptedAppSettings(SETTINGS_KEY_AUTO_MONITOR, IIf(autoMonitorStatus, "1", "0"))
            SaveEncryptedAppSettings(SETTINGS_KEY_SPINDLE_THRESHOLD, CStr(speedThreshold))
            SaveEncryptedAppSettings(SETTINGS_KEY_SPINDLE_TIME, CStr(retainTime))

            txtSpindleTime.Invoke(
                Sub()
                    ' Show Auto Monitor Status
                    chkAutoMonitor.Checked = autoMonitorStatus
                    txtSpindleTime.Text = CStr(retainTime)
                    txtThreshold.Text = CStr(speedThreshold)
                End Sub)

            Return

        End If

        ' Adaptive Min Max value
        If registerAddr = 1111 Then

            Dim outputMax As Single = ModbusClient.ConvertRegistersToFloat(registers)
            registers = New Integer() {
                objModbusServer.mHoldingRegisters.Item(1113),
                objModbusServer.mHoldingRegisters.Item(1114)}
            Dim outputMin As Single = ModbusClient.ConvertRegistersToFloat(registers)

            tbOMax.Invoke(
                Sub()
                    tbOMax.Text = outputMax.ToString("F2")
                    tbOMin.Text = outputMin.ToString("F2")
                End Sub)
            If pidCtrl IsNot Nothing Then
                pidCtrl.OutMaxVal = outputMax
                pidCtrl.OutMinVal = outputMin
            End If

            Return
        End If

        If registerAddr = 1115 Then

            Dim pvMax As Single = ModbusClient.ConvertRegistersToFloat(registers)

            tbPVMax.Invoke(
                Sub()
                    tbPVMax.Text = pvMax.ToString("F2")
                End Sub)
            If pidCtrl IsNot Nothing Then
                pidCtrl.PVMaxVal = pvMax
            End If

            Return
        End If

        ' Check Item existance
        Dim macroItem As MacroDataModel
        Try
            macroItem = macroSettingDic.Item(registerAddr)
        Catch ex As Exception
            macroItem = Nothing
        End Try

        If macroItem Is Nothing Then
            ' Not Item Exists
            Return
        End If

        ' Calc Modbus Value and show in the cell
        Dim modVal As Single = ModbusClient.ConvertRegistersToFloat(registers)

        Dim macroItemRow As DataGridViewRow = macroItem.getGridViewRow

        macroItemRow.Cells(3).Value = CStr(modVal)

        ' Check Fanuc Address
        If macroItem.hasValidFanucAddress() = False Then

            macroItemRow.Cells(7).Value = "Fanuc Address Error!"
            Return
        End If

        ' Check Fannuc Connection
        If bolHandleObtained = False Then
            ' Fanuc is still not ready
            macroItemRow.Cells(7).Value = "Fanuc not connected!"
            Return
        End If

        ' Make new Connection
        Dim intNewHandleNo As Integer
        Dim intNewLibHndl As Integer
        intNewHandleNo = Focas1.cnc_allclibhndl3(ipAddress, portNum, 5, intNewLibHndl)
        If intNewHandleNo < 0 Then
            macroItemRow.Cells(7).Value = "Connect Error:" & GetFanucMessage(intNewHandleNo)
            Return
        End If

        ' https://www.inventcom.net/fanuc-focas-library/ncdata/cnc_wrmacror

        Dim macroVar As Integer = macroItem.GetFanucAddress()

        Dim macroData As New Focas1.IODBMR
        Dim number As Integer = 1
        macroData.datano_s = macroVar
        macroData.datano_e = macroVar + number - 1 ' We only write One address
        macroData.data.data1.dec_val = 3
        macroData.data.data1.mcr_val = modVal * (10 ^ 3)

        Dim retVal As Short = Focas1.cnc_wrmacror(intNewLibHndl, 8 + 8 * number, macroData)

        If retVal = Focas1.EW_OK Then
            macroItemRow.Cells(7).Value = "Success!"

        Else
            macroItemRow.Cells(7).Value = GetFanucMessage(retVal)
        End If

        'Close Connection
        Focas1.cnc_freelibhndl(intNewLibHndl)

    End Sub

    ' CoilRegisters Changes Handler
    Protected Sub CoilsChangedHandler(coil As Integer, numberOfCoils As Integer) Handles objModbusServer.CoilsChanged
        ' Read new Modbus Register
        Dim newCoilValue As Boolean = objModbusServer.mCoils.Item(coil)

        ' Check Adaptive Enable Signal
        If coil = 2 Then

            If newCoilValue = True Then

                StartPID(False)
            Else
                StopPID()
            End If
        End If

        ' Check Item existance
        Dim pmcItem As PMCDataModel
        Try
            pmcItem = pmcSettingDic.Item(coil)
        Catch ex As Exception
            pmcItem = Nothing
        End Try

        If pmcItem Is Nothing Then
            ' Not Item Exists
            Return
        End If

        Dim pmcItemRow As DataGridViewRow = pmcItem.getGridViewRow
        ' Show new Value in the cell
        pmcItemRow.Cells(3).Value = "New Val:" & CStr(newCoilValue)

        ' Check Fanuc Address
        If pmcItem.hasValidFanucAddress() = False Then

            pmcItemRow.Cells(7).Value = "Fanuc Address Error!"
            Return
        End If

        ' Check Fannuc Connection
        If bolHandleObtained = False Then
            ' Fanuc is still not ready
            pmcItemRow.Cells(7).Value = "Fanuc not connected!"
            Return
        End If

        ' Make new Connection
        Dim intNewHandleNo As Integer
        Dim intNewLibHndl As Integer
        intNewHandleNo = Focas1.cnc_allclibhndl3(ipAddress, portNum, 5, intNewLibHndl)
        If intNewHandleNo < 0 Then
            pmcItemRow.Cells(7).Value = "Connect Error:" & GetFanucMessage(intNewHandleNo)
            Return
        End If

        ' https://www.inventcom.net/fanuc-focas-library/pmc/pmc_wrpmcrng

        Dim address_s As Integer = pmcItem.getFanucRegisterAddress()
        Dim bitIndex As Integer = pmcItem.getFanucBitAddress()
        Dim length As Integer = 1
        Dim address_e As Integer = address_s + length - 1

        Dim adr_type As Short = pmcItem.getAdrType()    '5 means R address
        Dim data_type As Short = pmcItem.getDataType()  '0 means Byte data
        Dim rAddrData As New Focas1.IODBPMC0
        ReDim rAddrData.cdata(length + 10)

        ' Read Original Value
        Dim retVal As Short = Focas1.pmc_rdpmcrng(intNewLibHndl, adr_type, data_type, address_s, address_e, 8 + length, rAddrData)

        If retVal = Focas1.EW_OK Then

            SetBit(rAddrData.cdata(0), bitIndex, newCoilValue)

            ' Write New Setting
            retVal = Focas1.pmc_wrpmcrng(intNewLibHndl, 8 + length, rAddrData)

            If retVal = Focas1.EW_OK Then
                pmcItemRow.Cells(7).Value = "Success!"
            Else
                pmcItemRow.Cells(7).Value = "2.Write Error:" & GetFanucMessage(retVal)
            End If
        Else
            pmcItemRow.Cells(7).Value = "1.Read Error:" & GetFanucMessage(retVal)
        End If

        'Close Connection
        Focas1.cnc_freelibhndl(intNewLibHndl)
    End Sub

    ' Request Rest API call
    Function postData(ByVal dictData As Specialized.NameValueCollection, ByVal apiUrl As String)
        Dim webClient As New MyWebClient(1)
        'Dim resByte As Byte()
        'Dim resString As String
        'Dim reqString() As Byte

        Try
            webClient.Headers("Accept") = "*/*"
            webClient.Headers.Add("Content-Type", "application/x-www-form-urlencoded")

            ' Add CallBack Listener
            AddHandler webClient.UploadValuesCompleted, AddressOf UploadValuesCompletedEventHandler
            webClient.UploadValuesAsync(New Uri(apiUrl), "POST", dictData)

            Return True
        Catch ex As Exception
            Console.WriteLine(ex.Message)
        End Try
        Return False
    End Function

    ' Callback of Web Request
    Public Sub UploadValuesCompletedEventHandler(sender As Object, e As UploadValuesCompletedEventArgs)

        If IsNothing(e.Error) Then
            Dim webclient = CType(sender, MyWebClient)

            Dim responsebody = (New Text.UTF8Encoding).GetString(e.Result)
            Dim resString As String = Encoding.Default.GetString(e.Result)
            Console.WriteLine(resString)

            Dim result = JsonConvert.DeserializeObject(resString)
            Dim json As JObject = JObject.Parse(resString)
            Dim status As Boolean = json.SelectToken("status")

            'Parse response
            If status = True Then
            Else
            End If
        Else
            MessageBox.Show("Network is unvailable." & vbCrLf & "Please check your connection.", "Network", MessageBoxButtons.OK, MessageBoxIcon.Warning)
        End If
    End Sub


    ' HST Relations ----------------------------------------------------------------------------------------
    Public Sub New()

        ' This call is required by the designer.
        InitializeComponent()

        Timer1.Start()

    End Sub

    Private Sub Form1_Resize(ByVal sender As System.Object, ByVal e As System.EventArgs) Handles MyBase.Resize
    End Sub

    Private Sub btnRunSpeedCheck_Click(sender As Object, e As EventArgs) Handles btnRunSpeedCheck.Click

        ' Check Already Running
        If runSpeedCheckThread = True Then
            runSpeedCheckThread = False
            btnRunSpeedCheck.Text = "Start"
            Return
        End If

        ' Check Fanuc Device Information
        Dim ipAddress As String = GetEncryptedAppSettings(SETTINGS_KEY_FANUCIP)
        Dim portNum As Integer = StringToInt(GetEncryptedAppSettings(SETTINGS_KEY_FANUCPORT))

        ' Check Connect Information
        If ipAddress.Trim.Length = 0 Or portNum = 0 Then
            ShowAlert(True, "Please input machine information to connect!")
            Return
        End If

        btnRunSpeedCheck.Text = "Stop"

        speedCheckThread = New System.Threading.Thread(AddressOf StartCommThread)
        speedCheckThread.Start()

    End Sub

    Public Sub StartCommThread()

        ' Check Fanuc Device Information
        Dim ipAddress As String = GetEncryptedAppSettings(SETTINGS_KEY_FANUCIP)
        Dim portNum As Integer = StringToInt(GetEncryptedAppSettings(SETTINGS_KEY_FANUCPORT))

        Dim libHandle As Integer = 0
        Dim retStatus As Integer = Focas1.cnc_allclibhndl3(ipAddress, portNum, 2, libHandle)

        ' Try to Connect
        If retStatus < 0 Then
            lblSpeedCheckInfo.Invoke(
                Sub()
                    runSpeedCheckThread = False
                    lblSpeedCheckInfo.Text = "Connection Error!"
                    btnRunSpeedCheck.Text = "Start"
                End Sub)
            'Return
        End If

        Dim timeStampStart As Long = GetCurrentTimeMilis()
        Dim timeStampCurrent As Long = timeStampStart

        Dim macroVar As Integer = 100

        Dim startTickVal As Integer = 1
        Dim errorCount As Integer = 0

        runSpeedCheckThread = True

        Do While runSpeedCheckThread

            Dim macroData As New Focas1.IODBMR
            Dim number As Integer = 1
            macroData.datano_s = macroVar
            macroData.datano_e = macroVar + number - 1 ' We only write One address
            macroData.data.data1.dec_val = 1
            macroData.data.data1.mcr_val = startTickVal * (10 ^ 1)

            Dim retVal As Short = Focas1.cnc_wrmacror(libHandle, 8 + 8 * number, macroData)

            If retVal <> Focas1.EW_OK Then
                errorCount = errorCount + 1
            End If

            startTickVal = startTickVal + 1

            ' End Condition
            timeStampCurrent = GetCurrentTimeMilis()

            If timeStampCurrent - timeStampStart > 60000 Then
                runSpeedCheckThread = False
            End If

        Loop

        'Close Connection
        Focas1.cnc_freelibhndl(libHandle)

        ' Make Statistic Values

        Dim timeStampPasstime As Long = timeStampCurrent - timeStampStart

        Dim strSpeedCheckInfo As String = "Speed:" & CStr(timeStampPasstime * 1.0 / startTickVal) & "ms" & vbCrLf &
            "Write Cnt:" & CStr(startTickVal) & vbCrLf &
            "Error Cnt:" & CStr(errorCount) & vbCrLf &
            "Time(ms):" & CStr(timeStampPasstime)

        lblSpeedCheckInfo.Invoke(
            Sub()
                lblSpeedCheckInfo.Text = strSpeedCheckInfo
                btnRunSpeedCheck.Text = "Start"
            End Sub)
    End Sub


    Private Sub btnCopyProg_Click(sender As Object, e As EventArgs) Handles btnCopyProg.Click
        ' https://www.inventcom.net/fanuc-focas-library/program/cnc_copyprog
        ' Check Program Number For Source and Destination

        Dim progSrcNum As Integer = -1
        Dim progDestNum As Integer = -1

        Try
            progSrcNum = Int(txtSrcNCProgNum.Text)
        Catch ex As Exception
        End Try

        Try
            progDestNum = Int(txtDestNCProgNum.Text)
        Catch ex As Exception
        End Try

        If progSrcNum = -1 OrElse progDestNum = -1 Then
            showWarnning("Invalid Program Number", "Please check program number.")
            Return
        End If

        Dim logString As String

        If bolHandleObtained = True Then

            Dim retVal As Short = Focas1.cnc_copyprog(intLibHndl, progSrcNum, progDestNum)
            If retVal = Focas1.EW_OK Then
                logString = "Success copy program!"
            Else
                logString = "Error Code: " & retVal
            End If
        Else
            logString = "Not connected to the Fanuc!"
        End If

        lblCopyProgInfo.Text = logString

    End Sub

    Private Sub btnReadSequenceNum_Click(sender As Object, e As EventArgs) Handles btnReadSequenceNum.Click
        ' https://www.inventcom.net/fanuc-focas-library/program/cnc_rdseqnum

        Dim logString As String

        If bolHandleObtained = True Then
            Dim odbSeqData As New Focas1.ODBSEQ
            Dim retVal As Short = Focas1.cnc_rdseqnum(intLibHndl, odbSeqData)

            If retVal = Focas1.EW_OK Then
                logString = "CURRENT N" & CStr(odbSeqData.data)
            Else
                logString = "Error Code: " & retVal
            End If
        Else
            logString = "Not connected to the Fanuc!"
        End If

        lblCurrSequenceNumInfo.Text = logString

    End Sub

    Private Sub btnReadCurrentProgName_Click(sender As Object, e As EventArgs) Handles btnReadCurrentProgName.Click
        'https://www.inventcom.net/fanuc-focas-library/program/cnc_exeprgname
        'Reads full path name of the program which Is being currently executed in CNC.
        'When the CNC Is stopping, the name of the executed program Is acquired.
        'The program name Is stored in "execprg.name" with maximum 32 character string format.

        Dim logString As String

        If bolHandleObtained = True Then
            Dim odbSeqData As New Focas1.ODBEXEPRG
            Dim retVal As Short = Focas1.cnc_exeprgname(intLibHndl, odbSeqData)

            If retVal = Focas1.EW_OK Then
                logString = "Program Name: " & New String(odbSeqData.name) & vbCrLf &
                    "Program Number: " & CStr(odbSeqData.o_num)
            Else
                logString = "Error Code: " & retVal
            End If
        Else
            logString = "Not connected to the Fanuc!"
        End If

        lblCurrProgNameInfo.Text = logString
    End Sub

    Private Sub btnGetFolderPath1_Click(sender As Object, e As EventArgs) Handles btnGetFolderPath1.Click
        ' Open Folder Browser Dialog
        If txtFolderPath1.Text.Length <> 0 Then
            OpenFileDialog1.FileName = txtFolderPath1.Text
        End If

        If OpenFileDialog1.ShowDialog() = DialogResult.OK Then
            txtFolderPath1.Text = OpenFileDialog1.FileName
        End If

    End Sub

    Private Sub btnReadFileProperty_Click(sender As Object, e As EventArgs) Handles btnReadFileProperty.Click
        Dim maxNumOfProg As Integer = 0

        Try
            maxNumOfProg = Int(txtMaxNumOfProg.Text)
        Catch ex As Exception
        End Try

        ' Check the number of program
        If maxNumOfProg = 0 Then
            showWarnning("Invalid Number of Program", "Please check value.")
            Return
        End If

        ' Check Folder Path
        If txtFolderPath1.Text.Trim().Length = 0 Then
            showWarnning("Warnning", "Please check folder path.")
            Return
        End If

        Dim requNum As Integer = -1

        Try
            requNum = Int(txtSubFolderReqNum.Text)
        Catch ex As Exception
        End Try

        If requNum = -1 Then
            showWarnning("Warnning", "Please check req Num Value.")
            Return
        End If

        Dim logString As String
        If bolHandleObtained = True Then
            Dim idbPDFADIR As New Focas1.IDBPDFADIR
            idbPDFADIR.path = txtFolderPath1.Text.Trim
            idbPDFADIR.req_num = requNum
            idbPDFADIR.size_kind = 1 ' 0:Page 1:Byte 2:KByte 3:MByte
            idbPDFADIR.type = 1 ' 0:Size, comment, process time stamp are not acquired. 1:Size, comment, Process time stamp are acquired.

            Dim odbPDFADIR As New Focas1.ODBPDFADIR

            Dim retVal As Short = Focas1.cnc_rdpdf_alldir(intLibHndl, maxNumOfProg, idbPDFADIR, odbPDFADIR)

            If retVal = Focas1.EW_OK Then

                Dim dateString As String = odbPDFADIR.year.ToString("D4") & "-" &
                    odbPDFADIR.mon.ToString("D2") & "-" &
                    odbPDFADIR.day.ToString("D2") & " " &
                    odbPDFADIR.hour.ToString("D2") & ":" &
                    odbPDFADIR.min.ToString("D2") & ":" &
                    odbPDFADIR.sec.ToString("D2")

                logString = "Name: " & New String(odbPDFADIR.d_f) & vbCrLf &
                    "Process time stamp: " & New String(odbPDFADIR.o_time) & vbCrLf &
                    "Last Edited DateTime: " & dateString & vbCrLf
            Else
                logString = "Error Code: " & retVal
            End If
        Else
            logString = "Not connected to the Fanuc!"
        End If

        lblFilePropertiesInfo.Text = logString

    End Sub

    Private Sub btnReadCurrProgInfo_Click(sender As Object, e As EventArgs) Handles btnReadCurrProgInfo.Click
        'https://www.inventcom.net/fanuc-focas-library/program/cnc_pdf_rdmain

        Dim logString As String
        If bolHandleObtained = True Then

            Dim mpPath As Char()
            ReDim mpPath(242)

            Dim retVal As Short = Focas1.cnc_pdf_rdmain(intLibHndl, mpPath)

            If retVal = Focas1.EW_OK Then
                logString = New String(mpPath)
            Else
                logString = "Error Code: " & retVal
            End If
        Else
            logString = "Not connected to the Fanuc!"
        End If

        txtCurrProgramFileInfo.Text = logString

    End Sub

    Private Sub btnReadModalData_Click(sender As Object, e As EventArgs) Handles btnReadModalData.Click
        Dim modalType As Integer = -1
        Dim modalBlock As Integer = -1

        Try
            modalType = Int(txtModalType.Text)
        Catch ex As Exception
        End Try

        Try
            modalBlock = Int(txtModalBlock.Text)
        Catch ex As Exception
        End Try

        ReadModalValue(modalType, modalBlock)

    End Sub

    Private Sub ReadModalValue(ByVal type As Integer, ByVal block As Integer, Optional fCode As Boolean = False)

        If type = -1 OrElse block = -1 Then
            showWarnning("Invalid Type or Block", "Please input correct type or block.")
            Return
        End If

        Dim logString As String
        If bolHandleObtained = True Then

            Dim modal As New Focas1.ODBMDL_3
            modal.aux = New Focas1.MODAL_AUX_data

            Dim retVal As Short = Focas1.cnc_modal(intLibHndl, type, block, modal)

            If retVal = Focas1.EW_OK Then
                If fCode = True Then

                    Dim gData As Single = modal.aux.aux_data
                    If modal.aux.flag1 And &H40 > 0 Then
                        gData = 1.0 * modal.aux.aux_data / (10 ^ modal.aux.flag2)
                    End If

                    logString = "DataNo: " & CStr(modal.datano) & vbCrLf &
                    "Type: " & CStr(modal.type) & vbCrLf &
                    "G_Data: " & CStr(gData) & vbCrLf
                Else
                    logString = "DataNo: " & CStr(modal.datano) & vbCrLf &
                    "Type: " & CStr(modal.type) & vbCrLf &
                    "G_Data: " & CStr(modal.aux.aux_data) & vbCrLf
                End If
            Else
                logString = "Error Code: " & retVal
            End If
        Else
            logString = "Not connected to the Fanuc!"
        End If

        lblReadModalInfo.Text = logString
    End Sub

    Private Function ReadModalValue(ByVal type As Integer, ByVal block As Integer, ByRef modal As Focas1.ODBMDL_3, ByRef logString As String) As Boolean

        If type = -1 OrElse block = -1 Then
            logString = "Invalid Type or Block"
            Return False
        End If

        Dim bRet As Boolean

        If bolHandleObtained = True Then

            If IsNothing(modal.aux) Then
                modal.aux = New Focas1.MODAL_AUX_data
            End If

            Dim retVal As Short = Focas1.cnc_modal(intLibHndl, type, block, modal)

            If retVal = Focas1.EW_OK Then
                logString = "DataNo: " & CStr(modal.datano) & vbCrLf &
                    "Type: " & CStr(modal.type) & vbCrLf &
                    "G_Data: " & CStr(modal.aux.aux_data) & vbCrLf

                bRet = True
            Else
                logString = "Error Code: " & retVal
                bRet = False
            End If
        Else
            logString = "Not connected to the Fanuc!"
            bRet = False
        End If

        Return bRet
    End Function

    Private Sub btnReadModalTool_Click(sender As Object, e As EventArgs) Handles btnReadModalTool.Click
        ReadModalValue(108, 0)
    End Sub

    Private Sub btnReadModalSpindle_Click(sender As Object, e As EventArgs) Handles btnReadModalSpindle.Click
        ReadModalValue(107, 0)
    End Sub

    Private Sub btnReadModalFeedrate_Click(sender As Object, e As EventArgs) Handles btnReadModalFeedrate.Click
        ReadModalValue(103, 0, True)
    End Sub

    Private Sub btnReadModalMCode_Click(sender As Object, e As EventArgs) Handles btnReadModalMCode.Click
        ReadModalValue(106, 0)
    End Sub

    ' Get the last edit time of the current program number
    Private Sub btnGetLastEditTimeForCurrentProgramNumber_Click(sender As Object, e As EventArgs) Handles btnGetLastEditTimeForCurrentProgramNumber.Click

        Dim logString As String
        Dim retVal As Short

        If bolHandleObtained = True Then

            ' 1. Get the Main Program Path
            Dim mpPath As Char()
            ReDim mpPath(242)

            ' Reads the file information that is select currently as the main program.
            retVal = Focas1.cnc_pdf_rdmain(intLibHndl, mpPath)
            If retVal = Focas1.EW_OK Then
                logString = New String(mpPath)

                Dim mainProgName As String = ""
                Dim sep() As Char = {"/", "\", "//"}
                mainProgName = logString.Split(sep).Last()
                Dim folderPath As String = Microsoft.VisualBasic.Left(logString, logString.Length - mainProgName.Length)

                ' 2. Loop Sub Files
                Dim bSuccessGetInfo As Boolean = False
                Dim idbPDFADIR As New Focas1.IDBPDFADIR
                Dim odbPDFADIR As New Focas1.ODBPDFADIR

                idbPDFADIR.path = folderPath
                idbPDFADIR.size_kind = 1 ' 0:Page 1:Byte 2:KByte 3:MByte
                idbPDFADIR.type = 1 ' 0:Size, comment, process time stamp are not acquired. 1:Size, comment, Process time stamp are acquired.
                idbPDFADIR.req_num = 0

                While Focas1.cnc_rdpdf_alldir(intLibHndl, 1, idbPDFADIR, odbPDFADIR) = Focas1.EW_OK

                    Dim subfileName As String = New String(odbPDFADIR.d_f)

                    If String.Compare(mainProgName, subfileName, False) = 0 Then

                        bSuccessGetInfo = True

                        Dim dateString As String = odbPDFADIR.year.ToString("D4") & "-" &
                        odbPDFADIR.mon.ToString("D2") & "-" &
                        odbPDFADIR.day.ToString("D2") & " " &
                        odbPDFADIR.hour.ToString("D2") & ":" &
                        odbPDFADIR.min.ToString("D2") & ":" &
                        odbPDFADIR.sec.ToString("D2")

                        logString = "Name: " & New String(odbPDFADIR.d_f) & vbCrLf &
                        "Process time stamp: " & New String(odbPDFADIR.o_time) & vbCrLf &
                        "Last Edited DateTime: " & dateString & vbCrLf

                        Exit While

                    End If

                    ' Next Search
                    idbPDFADIR.req_num = idbPDFADIR.req_num + 1
                End While

                If bSuccessGetInfo = False Then
                    logString = logString & " >> Failed to read file info!"
                End If

            Else
                ' Failed to read main program info
                logString = "Program Path >> Error Code: " & retVal
            End If
        Else
            ' Not connected to the Fanuc!
            logString = "Not connected to the Fanuc!"
        End If

        lblLastEditTimeInfo.Text = logString

    End Sub

    ' PID Control
    Public Function GetSetPoint() As Double
        Return setPointValue
    End Function

    Public Function GetProcessValue() As Double
        Return processValue
    End Function

    Public Sub SetOutputValue(value As Double)
        outputValue = value

        Dim intValue As Integer = Int(outputValue)
        Dim bitString As String = Convert.ToString(intValue, 2).PadLeft(8, "0"c) '8 bits

        lblOutput.Invoke(
            Sub()
                progBarOut.Value = intValue
                lblOutput.Text = CStr(outputValue) & "(" & bitString & ")"
            End Sub)

        ' Change Process Value
        'processValue = processValue + (outputValue * 0.2) - (processValue * 0.1) 'value
        'lblPV.Invoke(
        'Sub()
        '   progBarPV.Value = Int(processValue * 10)
        '   lblPV.Text = CStr(processValue)
        'End Sub)

        'Dim larrBits As BitArray = New BitArray(System.BitConverter.GetBytes(100))
        'Dim bits As Boolean() = New Boolean(larrBits.Count - 1) {}
        'larrBits.CopyTo(bits, 0)

        Dim byteVal As Integer = CInt(value)
        byteVal = Math.Max(Math.Min(255, byteVal), 0)

        Dim larrBits As BitArray = New BitArray(System.BitConverter.GetBytes(byteVal))
        Dim bits As Boolean() = New Boolean(larrBits.Count - 1) {}
        larrBits.CopyTo(bits, 0)

        'Bit 0 = G12.0
        'Bit 1 = G12.1

        ' Make new Connection
        ipAddress = GetEncryptedAppSettings(SETTINGS_KEY_FANUCIP)
        portNum = StringToInt(GetEncryptedAppSettings(SETTINGS_KEY_FANUCPORT))

        Dim pidOutLog As String = ""
        Dim intNewHandleNo As Integer
        Dim intNewLibHndl As Integer
        intNewHandleNo = Focas1.cnc_allclibhndl3(ipAddress, portNum, 5, intNewLibHndl)
        If intNewHandleNo >= 0 Then

            ' https://www.inventcom.net/fanuc-focas-library/pmc/pmc_wrpmcrng

            Dim address_s As Integer = adaptiveEnableAddr
            Dim length As Integer = 1
            Dim address_e As Integer = address_s + length - 1

            Dim adr_type As Short = adaptiveEnableType    '0 means G address, 1 means F address
            Dim data_type As Short = 0  '0 means Byte data

            Dim rAddrData As New Focas1.IODBPMC0
            ReDim rAddrData.cdata(length + 10)

            ' Read Original Value
            Dim retVal As Short = Focas1.pmc_rdpmcrng(intNewLibHndl, adr_type, data_type, address_s, address_e, 8 + length, rAddrData)

            If retVal = Focas1.EW_OK Then
                ' put value
                For bitIdx As Integer = 0 To 7
                    SetBit(rAddrData.cdata(0), bitIdx, Not bits(bitIdx))
                Next

                ' Write New Setting
                retVal = Focas1.pmc_wrpmcrng(intNewLibHndl, 8 + length, rAddrData)

                If retVal = Focas1.EW_OK Then
                    pidOutLog = "Success!"
                Else
                    pidOutLog = "2.Write Error:" & GetFanucMessage(retVal)
                End If
            Else
                pidOutLog = "1.Read Error:" & GetFanucMessage(retVal)
            End If

            'Close Connection
            Focas1.cnc_freelibhndl(intNewLibHndl)
        Else
            pidOutLog = "Connect Error!"
        End If

        lblStatusPIDOutToCNC.Invoke(
            Sub()
                lblStatusPIDOutToCNC.Text = pidOutLog
            End Sub)

    End Sub

    Dim pidCtrl As PID
    Dim pidIsRunning As Boolean = False
    Dim processValue As Double = 0
    Dim setPointValue As Double
    Dim outputValue As Double
    Dim adaptiveEnableType As Integer = 0
    Dim adaptiveEnableAddr As Integer = 0

    Private Function GetDoubleFromText(ByVal text As String, Optional ByVal defVal As Double = 0.0) As Double
        Dim retVal As Double = defVal
        Double.TryParse(text, retVal)
        Return retVal
    End Function


    Private Sub StopPID()
        ' Stop PID
        btnStart.Text = "Start"
        If pidCtrl IsNot Nothing Then
            pidCtrl.Disable()
        End If

        tmrReadPLC.Enabled = False

        pidIsRunning = False

        cbBoxTypePMC.Enabled = True
        tbAdaptiveEnableAddr.Enabled = True
    End Sub

    Private Sub StartPID(ByVal showMessage As Boolean)

        ' Check IP and Port 
        Dim IPAddress As String = tbPLCIP.Text
        Dim portNum As Integer = 0
        Try
            portNum = Int(tbPLCPort.Text)
        Catch ex As Exception
        End Try

        ' Check Connect Information
        If IPAddress.Trim.Length = 0 Or portNum = 0 Then
            ShowAlert(showMessage, "Please check PLC ip and port.", "Warning", MessageBoxButtons.OK, MessageBoxIcon.Warning)
            Return
        End If
        SaveEncryptedAppSettings(SETTINGS_KEY_PLCIP, IPAddress)
        SaveEncryptedAppSettings(SETTINGS_KEY_PLCPORT, CStr(portNum))

        ' Check Adaptive Enable Address
        adaptiveEnableType = cbBoxTypePMC.SelectedIndex
        adaptiveEnableAddr = -1

        Try
            adaptiveEnableAddr = Int(tbAdaptiveEnableAddr.Text)
        Catch ex As Exception
        End Try

        If adaptiveEnableAddr = -1 Then
            ShowAlert(showMessage, "Please check adaptive enable address.", "Warning", MessageBoxButtons.OK, MessageBoxIcon.Warning)
            Return
        End If

        SaveEncryptedAppSettings(SETTINGS_KEY_ADAPTIVE_ENABLE_TYPE, CStr(adaptiveEnableType))
        SaveEncryptedAppSettings(SETTINGS_KEY_ADAPTIVE_ADDRESS, CStr(adaptiveEnableAddr))

        ' Disconnect PLC Modbus for new connection
        If objModbusPLCClient IsNot Nothing AndAlso objModbusPLCClient.Connected Then
            objModbusPLCClient.Disconnect()
        End If

        If ReadPLCValue() = False Then
            ShowAlert(showMessage, "Please check PLC connection", "Warning", MessageBoxButtons.OK, MessageBoxIcon.Warning)
            Return
        End If

        tmrReadPLC.Enabled = True

        ' ------------------------------------------------------------------------------
        ' Start PID
        btnStart.Text = "Stop"

        setPointValue = trackBarSP.Value / 10.0

        Dim kP As Double = GetDoubleFromText(tbKp.Text)
        Dim ki As Double = GetDoubleFromText(tbKi.Text)
        Dim kd As Double = GetDoubleFromText(tbKd.Text)

        Dim pVMin As Double = GetDoubleFromText(tbPVMin.Text)
        Dim pVMax As Double = GetDoubleFromText(tbPVMax.Text)

        Dim outMin As Double = GetDoubleFromText(tbOMin.Text)
        Dim outMax As Double = GetDoubleFromText(tbOMax.Text)

        Dim readPV As PIDLibrary.GetDouble = AddressOf GetProcessValue
        Dim readSP As PIDLibrary.GetDouble = AddressOf GetSetPoint
        Dim writeOV As PIDLibrary.SetDouble = AddressOf SetOutputValue

        Dim hz As Double = 1000 / trackBarInterval.Value

        If pidCtrl Is Nothing Then
            ' Create New
            pidCtrl = New PID(kP, ki, kd, pVMax, pVMin, outMax, outMin, readPV, readSP, writeOV, hz)
        Else
            ' Update Param
            pidCtrl.PGain = kP
            pidCtrl.IGain = ki
            pidCtrl.DGain = kd

            pidCtrl.PVMinVal = pVMin
            pidCtrl.PVMaxVal = pVMax

            pidCtrl.OutMinVal = pVMin
            pidCtrl.OutMaxVal = pVMax

            pidCtrl.HzVal = hz
        End If

        pidCtrl.Enable()

        pidIsRunning = True

        cbBoxTypePMC.Enabled = False
        tbAdaptiveEnableAddr.Enabled = False
    End Sub

    Private Sub btnStart_Click(sender As Object, e As EventArgs) Handles btnStart.Click

        If pidIsRunning = True Then
            StopPID()
        Else
            StartPID(True)
        End If
    End Sub

    Private Sub trackBarSP_Scroll(sender As Object, e As EventArgs) Handles trackBarSP.Scroll
        setPointValue = trackBarSP.Value / 10.0
        lblSP.Text = CStr(setPointValue)
    End Sub

    Private Sub trackBarInterval_Scroll(sender As Object, e As EventArgs) Handles trackBarInterval.Scroll
        lblInterval.Text = CStr(trackBarInterval.Value)
    End Sub

    Dim stripChart As StripChart ' builds And contains the strip chart bmp

    Private Sub tmrChart_Tick(sender As Object, e As EventArgs) Handles tmrChart.Tick
        ' update the stripchart
        Dim scale As Double = GetDoubleFromText(tbPVMax.Text)
        stripChart.addSample(setPointValue, processValue, outputValue)
        ' update will rebuild the chart bitmap.
        ' put the New chart bitmap in the pictureBox
        pbPIDChart.Image = stripChart.bmp
    End Sub

    Private Sub tmrReadPLC_Tick(sender As Object, e As EventArgs) Handles tmrReadPLC.Tick
        ReadPLCValue()
    End Sub

    Private Function ReadPLCValue() As Boolean

        Try
            ' Check Connection
            If objModbusPLCClient Is Nothing OrElse objModbusPLCClient.Connected = False Then

                ' Connect to local Server
                Dim plcIP As String = tbPLCIP.Text.Trim
                Dim plcPort As Integer = Int(tbPLCPort.Text.Trim)

                objModbusPLCClient = New EasyModbus.ModbusClient
                objModbusPLCClient.IPAddress = plcIP
                objModbusPLCClient.Port = plcPort
                objModbusPLCClient.Connect()

                If objModbusPLCClient.Connected = False Then
                    Return False
                End If

            End If

            Dim registers As Integer() = objModbusPLCClient.ReadHoldingRegisters(0, 1)
            processValue = registers(0) / 100.0
            progBarPV.Value = Int(processValue * 10)
            lblPV.Text = CStr(processValue)
        Catch ex As Exception
            Return False
        End Try

        Return True

    End Function

    Private Sub txtSpindleTime_TextChanged(sender As Object, e As EventArgs) Handles txtSpindleTime.TextChanged
        Dim digitsOnly As Regex = New Regex("[^\d]")
        txtSpindleTime.Text = digitsOnly.Replace(txtSpindleTime.Text, "")
        SaveEncryptedAppSettings(SETTINGS_KEY_SPINDLE_TIME, txtSpindleTime.Text)
    End Sub

    Private Sub txtThreshold_TextChanged(sender As Object, e As EventArgs) Handles txtThreshold.TextChanged
        SaveEncryptedAppSettings(SETTINGS_KEY_SPINDLE_THRESHOLD, txtThreshold.Text)
    End Sub

    Private Sub chkAutoMonitor_CheckedChanged(sender As Object, e As EventArgs) Handles chkAutoMonitor.CheckedChanged
        SaveEncryptedAppSettings(SETTINGS_KEY_AUTO_MONITOR, IIf(chkAutoMonitor.Checked, "1", "0"))
    End Sub

    Private Sub txtPIDTestVal_TextChanged(sender As Object, e As EventArgs) Handles txtPIDTestVal.TextChanged
        Dim testPIDVal As Integer = -1

        Try
            testPIDVal = Int(txtPIDTestVal.Text)
        Catch ex As Exception
        End Try

        If testPIDVal > 0 Then
            testPIDVal = Math.Min(255, testPIDVal)
            Dim bitString As String = Convert.ToString(testPIDVal, 2).PadLeft(8, "0"c) '8 bits
            lblPIDTestValBinary.Text = "B(" & bitString & ")"
        End If
    End Sub

    Private Sub btnWriteTestPIDVal_Click(sender As Object, e As EventArgs) Handles btnWriteTestPIDVal.Click
        ' Check Adaptive Enable Address
        Dim adaptiveEnableType As Integer = cbBoxTypePMC.SelectedIndex
        Dim adaptiveEnableAddr As Integer = -1

        Try
            adaptiveEnableAddr = Int(tbAdaptiveEnableAddr.Text)
        Catch ex As Exception
        End Try

        If adaptiveEnableType = -1 Then
            MessageBox.Show("Please select address type.", "Warning", MessageBoxButtons.OK, MessageBoxIcon.Warning)
            Return
        End If

        If adaptiveEnableAddr = -1 Then
            MessageBox.Show("Please check adaptive enable address.", "Warning", MessageBoxButtons.OK, MessageBoxIcon.Warning)
            Return
        End If

        Dim testPIDVal As Integer = -1

        Try
            testPIDVal = Int(txtPIDTestVal.Text)
        Catch ex As Exception
        End Try

        If testPIDVal = -1 Then
            MessageBox.Show("Invalid Test Value.", "Warning", MessageBoxButtons.OK, MessageBoxIcon.Warning)
            Return
        End If

        testPIDVal = Math.Min(255, testPIDVal)

        Dim larrBits As BitArray = New BitArray(System.BitConverter.GetBytes(testPIDVal))
        Dim bits As Boolean() = New Boolean(larrBits.Count - 1) {}
        larrBits.CopyTo(bits, 0)

        'Bit 0 = G12.0
        'Bit 1 = G12.1

        ' Make new Connection
        ipAddress = GetEncryptedAppSettings(SETTINGS_KEY_FANUCIP)
        portNum = StringToInt(GetEncryptedAppSettings(SETTINGS_KEY_FANUCPORT))

        Dim intNewHandleNo As Integer
        Dim intNewLibHndl As Integer
        intNewHandleNo = Focas1.cnc_allclibhndl3(ipAddress, portNum, 5, intNewLibHndl)

        Dim pidOutLog As String
        If intNewHandleNo >= 0 Then

            ' https://www.inventcom.net/fanuc-focas-library/pmc/pmc_wrpmcrng

            Dim address_s As Integer = adaptiveEnableAddr
            Dim length As Integer = 1
            Dim address_e As Integer = address_s + length - 1

            Dim adr_type As Short = adaptiveEnableType    '0 means G address, 1 means F address
            Dim data_type As Short = 0  '0 means Byte data

            Dim rAddrData As New Focas1.IODBPMC0
            ReDim rAddrData.cdata(length + 10)

            ' Read Original Value
            Dim retVal As Short = Focas1.pmc_rdpmcrng(intNewLibHndl, adr_type, data_type, address_s, address_e, 8 + length, rAddrData)

            If retVal = Focas1.EW_OK Then
                ' put value
                For bitIdx As Integer = 0 To 7
                    SetBit(rAddrData.cdata(0), bitIdx, Not bits(bitIdx))
                Next

                ' Write New Setting
                retVal = Focas1.pmc_wrpmcrng(intNewLibHndl, 8 + length, rAddrData)

                If retVal = Focas1.EW_OK Then
                    pidOutLog = "Success!"
                Else
                    pidOutLog = "2.Write Error:" & GetFanucMessage(retVal)
                End If
            Else
                pidOutLog = "1.Read Error:" & GetFanucMessage(retVal)
            End If

            'Close Connection
            Focas1.cnc_freelibhndl(intNewLibHndl)
        Else
            pidOutLog = "Connect Error!"
        End If

        lblStatusPIDOutToCNC.Text = pidOutLog
    End Sub

    Private Sub btnAddr3OnOff_Click(sender As Object, e As EventArgs) Handles btnAddr3OnOff.Click
        switchAddressValueOnOff(3)
    End Sub

    Private Sub btnAddr4OnOff_Click(sender As Object, e As EventArgs) Handles btnAddr4OnOff.Click
        switchAddressValueOnOff(4)
    End Sub

    Private Sub switchAddressValueOnOff(address As Integer)
        ' Check Item existance
        Dim pmcItem As PMCDataModel
        Try
            pmcItem = pmcSettingDic.Item(address)
        Catch ex As Exception
            pmcItem = Nothing
        End Try

        If pmcItem Is Nothing Then
            ' Not Item Exists
            ShowAlert(True, "No setting of Address " & CStr(address) & ".", "Warning")
            Return
        End If

        Dim pmcItemRow As DataGridViewRow = pmcItem.getGridViewRow
        ' Show new Value in the cell
        'pmcItemRow.Cells(3).Value = "New Val:" & CStr(newCoilValue)

        ' Check Fanuc Address
        If pmcItem.hasValidFanucAddress() = False Then

            pmcItemRow.Cells(7).Value = "Fanuc Address Error!"
            Return
        End If

        ' Check Fannuc Connection
        If bolHandleObtained = False Then
            ' Fanuc is still not ready
            pmcItemRow.Cells(7).Value = "Fanuc not connected!"
            Return
        End If

        ' Make new Connection
        Dim intNewHandleNo As Integer
        Dim intNewLibHndl As Integer
        intNewHandleNo = Focas1.cnc_allclibhndl3(ipAddress, portNum, 5, intNewLibHndl)
        If intNewHandleNo < 0 Then
            pmcItemRow.Cells(7).Value = "Connect Error:" & GetFanucMessage(intNewHandleNo)
            Return
        End If

        ' https://www.inventcom.net/fanuc-focas-library/pmc/pmc_wrpmcrng

        Dim address_s As Integer = pmcItem.getFanucRegisterAddress()
        Dim bitIndex As Integer = pmcItem.getFanucBitAddress()
        Dim length As Integer = 1
        Dim address_e As Integer = address_s + length - 1

        Dim adr_type As Short = pmcItem.getAdrType()    '5 means R address
        Dim data_type As Short = pmcItem.getDataType()  '0 means Byte data
        Dim rAddrData As New Focas1.IODBPMC0
        ReDim rAddrData.cdata(length + 10)

        ' Read Original Value
        Dim retVal As Short = Focas1.pmc_rdpmcrng(intNewLibHndl, adr_type, data_type, address_s, address_e, 8 + length, rAddrData)

        If retVal = Focas1.EW_OK Then

            Dim currState As Boolean = GetBit(rAddrData.cdata(0), bitIndex)

            SetBit(rAddrData.cdata(0), bitIndex, Not currState)

            ' Write New Setting
            retVal = Focas1.pmc_wrpmcrng(intNewLibHndl, 8 + length, rAddrData)

            If retVal = Focas1.EW_OK Then
                pmcItemRow.Cells(7).Value = "Success!"
            Else
                pmcItemRow.Cells(7).Value = "2.Write Error:" & GetFanucMessage(retVal)
            End If
        Else
            pmcItemRow.Cells(7).Value = "1.Read Error:" & GetFanucMessage(retVal)
        End If

        'Close Connection
        Focas1.cnc_freelibhndl(intNewLibHndl)
    End Sub

End Class
