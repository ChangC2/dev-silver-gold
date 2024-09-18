Imports System.IO
Imports System.Threading
Imports System.Windows.Forms
Imports EasyModbus
Imports Newtonsoft.Json

Public Class FanucBridgeService

    Private Const SETTING_FILE As String = "fanuc_settings.json"

    Dim WithEvents objModbusServer As ModbusServer
    Dim objModbusClient As ModbusClient
    Dim signalOnArray As Integer() = EasyModbus.ModbusClient.ConvertFloatToRegisters(1)
    Dim signalOffArray As Integer() = EasyModbus.ModbusClient.ConvertFloatToRegisters(0)

    Dim deviceSetting As FanucSettings
    Dim macroSettingDic As Dictionary(Of Integer, MacroDataModel)
    Dim pmcSettingDic As Dictionary(Of Integer, PMCDataModel)

    Private settingsLock As New Object

    Dim timeCurrentMilis As Long = 0
    Dim reconnectTryTime As Long = 0

    Dim ipAddress As String = ""
    Dim portNum As Integer = 0
    Dim readCycleTime As Integer = 200

    Public bolHandleObtained As Boolean = False
    Public intLibHndl As Integer = 0

    ' Save the last time worked for the upload
    Dim lastTimeUploaded As Long = 0

    Dim fanucData As FanucData = New FanucData()

    ' Data Scan Timer
    ' Dim tmr As Timers.Timer
    Dim dataThread As System.Threading.Thread
    Dim runDataThread As Boolean = False

    Protected Overrides Sub OnStart(ByVal args() As String)
        ' Add code here to start your service. This method should set things
        ' in motion so your service can do its work.

        Try
            ' Init Modbus Server
            objModbusServer = New EasyModbus.ModbusServer
            objModbusServer.Port = 602
            objModbusServer.Listen()
            ' objModbusServer.mHoldingRegisters.Item(100) = 100
            ' objModbusServer.mHoldingRegisters.Item(101) = 101

            addLog("Modbus works in Port 602")

            ' Set the last time as Now - 5 secs
            If lastTimeUploaded = 0 Then
                lastTimeUploaded = GetCurrentTimeMilis() - 1000 * 60 * 10
            End If

            '' Time Settings
            'tmr = New Timers.Timer()
            'tmr.Interval = 500 * 1 ' Interval is 1 seconds
            'AddHandler tmr.Elapsed, AddressOf TimeTickHandler
            'tmr.Enabled = True

            runDataThread = True
            dataThread = New System.Threading.Thread(AddressOf TimeTickHandler)
            dataThread.Start()

            addLog("Scheduler Started!")
        Catch ex As Exception
            addLog(ex.Message)
        End Try

        ' Start Log
        addLog("Service is Started!")
    End Sub

    Protected Overrides Sub OnStop()
        ' Close Gantt Report Threads
        runDataThread = False
        If dataThread IsNot Nothing Then
            dataThread.Abort()
        End If

        ' Add code here to perform any tear-down necessary to stop your service.
        Try
            objModbusServer.StopListening()
        Catch ex As Exception
            addLog(ex.Message)
        End Try

        ' Stop Log
        addLog("Service is Stoped!")
    End Sub

    'Private Sub TimeTickHandler(obj As Object, e As EventArgs)
    Private Sub TimeTickHandler()

        Do While runDataThread
            timeCurrentMilis = GetCurrentTimeMilis()

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
                deviceSetting = JsonConvert.DeserializeObject(Of FanucSettings)(fileContents)

                ' Check Device Settings, if invalid then return.
                If deviceSetting Is Nothing OrElse deviceSetting.IsEmpty() Then
                    Return
                End If

                SyncLock settingsLock

                    macroSettingDic = New Dictionary(Of Integer, MacroDataModel)
                    pmcSettingDic = New Dictionary(Of Integer, PMCDataModel)

                    ' Construct NC dictionary info
                    If deviceSetting.macroSettings IsNot Nothing And deviceSetting.macroSettings.Count > 0 Then
                        For i As Integer = 0 To deviceSetting.macroSettings.Count - 1
                            Dim newMacroSetting As MacroDataModel = deviceSetting.macroSettings.Item(i)
                            Dim modAddrVal As Integer = newMacroSetting.GetModbusAddress()

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
                                    ShowAlert(True, "Setting Error",
                                          "Modbus Addr Dup For Macro!",
                                          MessageBoxButtons.OK, MessageBoxIcon.Warning)
                                Else
                                    macroSettingDic.Add(modAddrVal, newMacroSetting)
                                End If
                            End If

                        Next
                    End If

                    ' Construct PMC dictionary info
                    If deviceSetting.pmcSettings IsNot Nothing And deviceSetting.pmcSettings.Count > 0 Then
                        For i As Integer = 0 To deviceSetting.pmcSettings.Count - 1
                            Dim newPMCSetting As PMCDataModel = deviceSetting.pmcSettings.Item(i)
                            Dim modAddrVal As Integer = newPMCSetting.GetModbusAddress()

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
                                    ShowAlert(True, "Setting Error",
                                          "Modbus Addr Dup For PMC!",
                                          MessageBoxButtons.OK, MessageBoxIcon.Warning)
                                Else
                                    pmcSettingDic.Add(modAddrVal, newPMCSetting)
                                End If
                            End If
                        Next
                    End If

                End SyncLock

                ' Try to reconnect, 10s Intervals
                If bolHandleObtained = False Then
                    ' Disconnect status, Reconnect modules
                    If timeCurrentMilis - reconnectTryTime > 10000 Then
                        ConnectFanuc(False)
                    End If
                End If

                If bolHandleObtained = True Then
                    ' Get NC Values
                    GetNCData()

                    ' Get PMC Values
                    GetPMCData()

                    ' Get Timers
                    GetCNCTimers()

                    ' Get Spindle Data
                    Dim autoMonitorCheck As Boolean = StringToInt(GetEncryptedAppSettings(SETTINGS_KEY_AUTO_MONITOR)) > 0
                    If autoMonitorCheck = True Then
                        GetSpindleData()

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
                        DisconnectFannuc()
                    End If
                Else
                    fanucData.run = -1
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

                ' Check Run status: Send to Incycle Status for TOS
                If feedFoldStatus Then
                    objModbusServer.mHoldingRegisters.Item(1013) = ConvertUShortToShort(signalOnArray(0))
                    objModbusServer.mHoldingRegisters.Item(1014) = ConvertUShortToShort(signalOnArray(1))
                Else
                    objModbusServer.mHoldingRegisters.Item(1013) = ConvertUShortToShort(signalOffArray(0))
                    objModbusServer.mHoldingRegisters.Item(1014) = ConvertUShortToShort(signalOffArray(1))
                End If

                ' Fanuc Connection Status
                If bolHandleObtained Then
                    objModbusServer.mHoldingRegisters.Item(1015) = ConvertUShortToShort(signalOnArray(0))
                    objModbusServer.mHoldingRegisters.Item(1016) = ConvertUShortToShort(signalOnArray(1))
                Else
                    objModbusServer.mHoldingRegisters.Item(1015) = ConvertUShortToShort(signalOffArray(0))
                    objModbusServer.mHoldingRegisters.Item(1016) = ConvertUShortToShort(signalOffArray(1))
                End If

                ' Get Current Sequence Number
                'If GetCurrentSeqNum() = True Then
                '    Dim seqValArray As Integer() = EasyModbus.ModbusClient.ConvertFloatToRegisters(fanucData.currSequNum)
                '    objModbusServer.mHoldingRegisters.Item(1017) = ConvertUShortToShort(seqValArray(0))
                '    objModbusServer.mHoldingRegisters.Item(1018) = ConvertUShortToShort(seqValArray(1))
                'Else
                '    objModbusServer.mHoldingRegisters.Item(1017) = ConvertUShortToShort(signalOffArray(0))
                '    objModbusServer.mHoldingRegisters.Item(1018) = ConvertUShortToShort(signalOffArray(1))
                'End If

            Catch ex As Exception
                Dim exMsg As String = ex.Message
                If Not String.IsNullOrEmpty(exMsg) Then
                    addLog("ErrTmr:" & ex.Message)
                End If
            End Try

            readCycleTime = StringToInt(GetEncryptedAppSettings(SETTINGS_KEY_READ_CYCLE_TIME), 200)
            If readCycleTime <= 0 Then
                readCycleTime = 200
            End If

            System.Threading.Thread.Sleep(readCycleTime)
        Loop

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

            If macroItem.hasValidFanucAddress() = False Then

                addLog("ErrNCFanucAddr!")
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

                If isValidModbusAddr(macroItem.GetModbusAddress()) Then
                    Dim modbusAddr As Integer = macroItem.GetModbusAddress()

                    If modbusAddr = 1001 Then
                        ' Check Auto Monitor Status and skip writting the modbus
                        Dim autoMonitorCheck As Boolean = StringToInt(GetEncryptedAppSettings(SETTINGS_KEY_AUTO_MONITOR)) > 0
                        If autoMonitorCheck Then
                            fanucData.currAutoMonitorStatus = IIf(macroVal > 0, True, False)
                            addLog("SkipMonitorW-Auto")
                            Continue For
                        End If
                    End If

                    Dim realValArray As Integer() = EasyModbus.ModbusClient.ConvertFloatToRegisters(macroVal)
                    objModbusServer.mHoldingRegisters.Item(modbusAddr + 0) = ConvertUShortToShort(realValArray(0))
                    objModbusServer.mHoldingRegisters.Item(modbusAddr + 1) = ConvertUShortToShort(realValArray(1))

                    addLog("SucReadNC")
                Else
                    addLog("ErrNCModbus!")
                End If
            Else
                addLog("ErrNCRead:" & GetFanucMessage(retVal))
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

            If pmcItem.hasValidFanucAddress() = False Then
                addLog("ErrPMCFanucAddr!")
                Continue For
            End If

            adr_type = pmcItem.getAdrType()
            address_s = pmcItem.getFanucRegisterAddress()
            address_e = address_s + length - 1
            bitIndex = pmcItem.getFanucBitAddress()

            ' Check Address
            If adr_type = -1 OrElse address_s = -1 OrElse bitIndex = -1 Then
                addLog("ErrPMCFanucAddr!")
                Continue For
            End If

            ' Read Real Device Value
            Dim retVal As Short = Focas1.pmc_rdpmcrng(intLibHndl, adr_type, data_type, address_s, address_e, 8 + length, pmcAddrData)

            If retVal = Focas1.EW_OK Then
                Dim byteVal As Byte = pmcAddrData.cdata(0)
                Dim bitVal As Boolean = GetBit(byteVal, bitIndex)

                If isValidModbusAddr(pmcItem.GetModbusAddress()) Then
                    Dim modbusAddr As Integer = pmcItem.GetModbusAddress()

                    objModbusServer.mCoils.Item(modbusAddr) = bitVal

                    addLog("SucReadPMC")
                Else
                    addLog("ErrPMCModbus!")
                End If
            Else
                addLog("ErrPMCRead:" & GetFanucMessage(retVal))
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
                'lblMainProgInfo.Text = String.Format("Main Program : O{0}", progInfo.mdata)
                'lblCurrProgInfo.Text = String.Format("Current Program : O{0}", progInfo.data)

                '' Main and Current Program Information
                'FanucData.mainProgram = String.Format("O{0}", progInfo.data)
                'FanucData.currentProgram = String.Format("O{0}", progInfo.mdata)

                Return True
            Else
                Dim errorString As String = GetFanucMessage(retVal)
                'lblMainProgInfo.Text = ""   'errorString
                'lblCurrProgInfo.Text = ""

                'FanucData.mainProgram = ""  'errorString
                'FanucData.currentProgram = ""

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
            'lblStateInfo.Text = GetStatusValueWithTitle(STATE_INFO_FIELD.AUT, stateInfo.aut) & vbCrLf &
            'GetStatusValueWithTitle(STATE_INFO_FIELD.RUN, stateInfo.run) & vbCrLf &
            'GetStatusValueWithTitle(STATE_INFO_FIELD.MOTION, stateInfo.motion) & vbCrLf &
            'GetStatusValueWithTitle(STATE_INFO_FIELD.MSTB, stateInfo.mstb) & vbCrLf &
            'GetStatusValueWithTitle(STATE_INFO_FIELD.EMERGENCY, stateInfo.emergency) & vbCrLf &
            'GetStatusValueWithTitle(STATE_INFO_FIELD.ALARM, stateInfo.alarm)

            ' Motion and Run Information
            fanucData.motion = stateInfo.motion
            fanucData.run = stateInfo.run

            Return True
        Else
            Dim errorString As String = GetFanucMessage(retVal)
            'lblStateInfo.Text = errorString

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
                'lblAlarmInfo.Text = "No Alarm"
            Else
                Dim uShortVal As UShort = ConvertShortToUShort(alarmInfo.data)
                Dim strAlarm As String = "Alarm:(" & uShortVal & ")"

                For idx = 0 To 10
                    If (uShortVal And 1) > 0 Then
                        strAlarm = strAlarm & vbCrLf & lblAlarm(idx)
                    End If
                    uShortVal >>= 1
                Next

                'lblAlarmInfo.Text = strAlarm

                'FanucData.alarm = strAlarm

            End If

            Return True
        Else
            Dim errorString As String = GetFanucMessage(retVal)
            'lblAlarmInfo.Text = errorString

            'FanucData.alarm = errorString

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

            Else

            End If
        Catch ex As Exception
        End Try

        'Close Connection
        Focas1.cnc_freelibhndl(intNewLibHndl)
    End Sub

    ' HoldingRegisters Changes Handler
    Protected Sub HoldingRegistersChangedHandler(registerAddr As Integer, numberOfRegisters As Integer) Handles objModbusServer.HoldingRegistersChanged
        If macroSettingDic Is Nothing Then
            Return
        End If

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

            Return

        End If

        ' Check Item existance
        Dim macroItem As MacroDataModel
        Try
            SyncLock settingsLock
                macroItem = macroSettingDic.Item(registerAddr)
            End SyncLock
        Catch ex As Exception
            macroItem = Nothing
        End Try

        If macroItem Is Nothing Then
            ' Not Item Exists
            Return
        End If

        ' Calc Modbus Value and show in the cell
        Dim modVal As Single = ModbusClient.ConvertRegistersToFloat(registers)

        ' Check Fanuc Address
        If macroItem.hasValidFanucAddress() = False Then

            addLog("MHW: Fanuc Addr Err!")
            Return
        End If

        ' Check Fannuc Connection
        If bolHandleObtained = False Then
            ' Fanuc is still not ready
            addLog("MHW: Fanuc not connected!")
            Return
        End If

        If String.IsNullOrEmpty(ipAddress) OrElse portNum <= 0 Then
            Return
        End If

        ' Make new Connection
        Dim intNewHandleNo As Integer
        Dim intNewLibHndl As Integer
        intNewHandleNo = Focas1.cnc_allclibhndl3(ipAddress, portNum, 5, intNewLibHndl)
        If intNewHandleNo < 0 Then
            addLog("MHW: Conn Err:" & GetFanucMessage(intNewHandleNo))
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
            addLog("MHW: Success!")

        Else
            addLog("MHW: " & GetFanucMessage(retVal))
        End If

        'Close Connection
        Focas1.cnc_freelibhndl(intNewLibHndl)

    End Sub

    ' CoilRegisters Changes Handler
    Protected Sub CoilsChangedHandler(coil As Integer, numberOfCoils As Integer) Handles objModbusServer.CoilsChanged
        If pmcSettingDic Is Nothing Then
            Return
        End If

        ' Read new Modbus Register
        Dim newCoilValue As Boolean = objModbusServer.mCoils.Item(coil)

        ' Check Item existance
        Dim pmcItem As PMCDataModel
        Try
            SyncLock settingsLock
                pmcItem = pmcSettingDic.Item(coil)
            End SyncLock

        Catch ex As Exception
            pmcItem = Nothing
        End Try

        If pmcItem Is Nothing Then
            ' Not Item Exists
            Return
        End If

        ' Check Fanuc Address
        If pmcItem.hasValidFanucAddress() = False Then

            addLog("MCW: Fanuc Addr Err!")
            Return
        End If

        ' Check Fannuc Connection
        If bolHandleObtained = False Then
            ' Fanuc is still not ready
            addLog("MCW: Fanuc not connected!")
            Return
        End If

        If String.IsNullOrEmpty(ipAddress) OrElse portNum <= 0 Then
            Return
        End If

        ' Make new Connection
        Dim intNewHandleNo As Integer
        Dim intNewLibHndl As Integer
        intNewHandleNo = Focas1.cnc_allclibhndl3(ipAddress, portNum, 5, intNewLibHndl)
        If intNewHandleNo < 0 Then
            addLog("MCW: Connect Err:" & GetFanucMessage(intNewHandleNo))
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
                addLog("MCW: Success!")
            Else
                addLog("MCW: 2.Write Err:" & GetFanucMessage(retVal))
            End If
        Else
            addLog("MCW: 1.Read Err:" & GetFanucMessage(retVal))
        End If

        'Close Connection
        Focas1.cnc_freelibhndl(intNewLibHndl)
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

    Private Sub ConnectFanuc(Optional ByVal showMessage As Boolean = True)
        reconnectTryTime = GetCurrentTimeMilis()

        ' Check Fanuc Device Information
        bolHandleObtained = False
        ipAddress = GetEncryptedAppSettings(SETTINGS_KEY_FANUCIP)
        portNum = StringToInt(GetEncryptedAppSettings(SETTINGS_KEY_FANUCPORT))

        ' Check Connect Information
        If ipAddress.Trim.Length = 0 Or portNum = 0 Then
            ShowAlert(showMessage, "No machine info!")
            Return
        End If

        addLog("Connect: " & ipAddress & ":" & portNum)

        ' Try to Connect
        If GetFocasHandle(ipAddress, portNum, showMessage) < 0 Then
            bolHandleObtained = False
        Else
            bolHandleObtained = True

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

        addLog(connMsg)

        If showError Then
            MessageBox.Show(connMsg, connTitle, MessageBoxButtons.OK, MessageBoxIcon.Information)
        End If

        Return retStatus

    End Function

    Function CloseFOCASHandle(ByVal intHandleID As Integer) As Integer
        Dim intReturnCode As Integer = -9999
        intReturnCode = Focas1.cnc_freelibhndl(intHandleID)
        Return intReturnCode
    End Function

    Private Sub DisconnectFannuc()

        If bolHandleObtained = True Then
            addLog("Handle closed successfully! - Return code " & CloseFOCASHandle(intLibHndl))
        Else
            addLog("There is no handle open for this application!")
        End If

        bolHandleObtained = False
    End Sub

    Public Sub ShowAlert(ByVal need As Boolean, ByVal msg As String,
                               Optional ByVal title As String = "",
                               Optional ByVal button As MessageBoxButtons = MessageBoxButtons.OK,
                               Optional ByVal icon As MessageBoxIcon = MessageBoxIcon.Information)

        If need Then
            'MessageBox.Show(msg, title, button, icon)
            addLog(msg)

        End If
    End Sub

    Private Sub addLog(text As String)

        ' Write Log to the file
        FileIO.WriteToFile(text)

    End Sub

End Class
