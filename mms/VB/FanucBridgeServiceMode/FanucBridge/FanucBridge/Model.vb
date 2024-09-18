
Imports System.Net
Imports Newtonsoft.Json

Public Module AppModel
    Public Class JSONServerResult
        Public status As Boolean
        Public msg As String
    End Class

    Public Class MyWebClient
        Inherits WebClient

        Dim tag As Integer
        Public Sub New(tag As Integer)
            Me.tag = tag
        End Sub

        Public Function getTag() As Integer
            Return Me.tag
        End Function

    End Class

    Public Class MacroDataModel
        Public description As String
        Public modbusAddress As Integer
        Private modbusValue As Single
        Public fanucAddress As Integer
        Private fanucValue As Single

        Private rowObj As DataGridViewRow

        Public Sub New()
            description = ""
        End Sub

        Public Sub New(ByVal des As String, ByVal modAddr As Integer, ByVal fanucAddr As Integer, Optional tableRow As DataGridViewRow = Nothing)
            description = des
            modbusAddress = modAddr
            fanucAddress = fanucAddr

            rowObj = tableRow

            'If rowObj IsNot Nothing Then
            'rowObj.Cells(5).Value = 100
            'End If
        End Sub

        Public Sub SetDescription(ByVal desc As String)
            description = desc
        End Sub

        Public Function GetDescription() As String
            Return description
        End Function

        Public Sub SetModbusAddress(ByVal addr As Integer)
            modbusAddress = addr
        End Sub

        Public Function GetModbusAddress() As Integer
            Return modbusAddress
        End Function

        Public Sub SetModbusValue(ByVal val As Single)
            modbusValue = val
        End Sub

        Public Function GetModbusValue() As Single
            Return modbusValue
        End Function

        Public Sub SetFanucAddress(ByVal addr As Integer)
            fanucAddress = addr
        End Sub

        Public Function GetFanucAddress() As Integer
            Return fanucAddress
        End Function

        Public Sub SetFanucValue(ByVal val As Single)
            fanucValue = val
        End Sub

        Public Function GetFanucValue() As Single
            Return fanucValue
        End Function

        Public Function hasValidFanucAddress() As Boolean
            Return isValidFanucAddress(fanucAddress)
        End Function

        Public Shared Function isValidFanucAddress(objVal As Object) As Boolean
            If IsNothing(objVal) Then
                Return False
            End If

            ' Check Modbus Address
            Dim fanucAddr As Integer = -1
            Try
                fanucAddr = Convert.ToInt32(objVal)
            Catch ex As Exception
            End Try

            ' Fanuc Addr is Short, Short max value is 32767
            If fanucAddr < 0 OrElse fanucAddr > 32767 Then
                Return False
            End If

            Return True
        End Function

        Public Function getGridViewRow() As DataGridViewRow
            Return rowObj
        End Function
    End Class


    Public Class PMCDataModel
        Public description As String
        Public modbusAddress As Integer
        Private modbusValue As Boolean
        Public fanucAddress As String
        Private fanucValue As Boolean

        Private rowObj As DataGridViewRow

        Public Sub New()
            description = ""
        End Sub

        Public Sub New(ByVal des As String, ByVal modAddr As Integer, ByVal fanucAddr As String, Optional tableRow As DataGridViewRow = Nothing)
            description = des
            modbusAddress = modAddr
            fanucAddress = fanucAddr

            rowObj = tableRow
        End Sub

        Public Sub SetDescription(ByVal desc As String)
            description = desc
        End Sub

        Public Function GetDescription() As String
            Return description
        End Function

        Public Sub SetModbusAddress(ByVal addr As Integer)
            modbusAddress = addr
        End Sub

        Public Function GetModbusAddress() As Integer
            Return modbusAddress
        End Function

        Public Sub SetModbusValue(ByVal val As Boolean)
            modbusValue = val
        End Sub

        Public Function GetModbusValue() As Boolean
            Return modbusValue
        End Function

        Public Sub SetFanucAddress(ByVal addr As String)
            fanucAddress = addr
        End Sub

        Public Function GetFanucAddress() As String
            Return fanucAddress
        End Function

        Public Sub SetFanucValue(ByVal val As Boolean)
            fanucValue = val
        End Sub

        Public Function GetFanucValue() As Boolean
            Return fanucValue
        End Function

        Public Function hasValidFanucAddress() As Boolean
            Return isValidFanucAddress(fanucAddress)
        End Function

        Public Shared Function isValidFanucAddress(objVal As Object) As Boolean

            If IsNothing(objVal) Then
                Return False
            End If

            Dim addr As String = objVal.ToString

            If addr.Length < 2 Then
                Return False
            End If

            If addr.StartsWith("G") OrElse
                addr.StartsWith("F") OrElse
                addr.StartsWith("Y") OrElse
                addr.StartsWith("X") OrElse
                addr.StartsWith("A") OrElse
                addr.StartsWith("R") OrElse
                addr.StartsWith("T") OrElse
                addr.StartsWith("K") OrElse
                addr.StartsWith("C") OrElse
                addr.StartsWith("D") Then

                Try
                    Dim addressBody As String = addr.Substring(1)
                    Dim dotPosition As Integer = addressBody.IndexOf(".")
                    Dim registerBody As Short = Convert.ToInt16(addressBody.Substring(0, dotPosition))
                    Dim bitBody As Short = Convert.ToInt16(addressBody.Substring(dotPosition + 1))
                Catch ex As Exception
                    Return False
                End Try

                Return True
            Else
                Return False
            End If
        End Function

        Public Function getFanucRegisterAddress() As Short
            Try
                Dim addressBody As String = fanucAddress.Substring(1)
                Dim dotPosition As Integer = addressBody.IndexOf(".")
                Dim registerBody As Short = Convert.ToInt16(addressBody.Substring(0, dotPosition))
                Dim bitBody As Short = Convert.ToInt16(addressBody.Substring(dotPosition + 1))

                Return registerBody
            Catch ex As Exception
                Return -1
            End Try
        End Function

        Public Function getFanucBitAddress() As Short
            Try
                Dim addressBody As String = fanucAddress.Substring(1)
                Dim dotPosition As Integer = addressBody.IndexOf(".")
                Dim registerBody As Short = Convert.ToInt16(addressBody.Substring(0, dotPosition))
                Dim bitBody As Short = Convert.ToInt16(addressBody.Substring(dotPosition + 1))

                Return bitBody
            Catch ex As Exception
                Return -1
            End Try
        End Function

        Public Function getAdrType() As Short
            If fanucAddress.StartsWith("G") Then
                Return 0
            ElseIf fanucAddress.StartsWith("F") Then
                Return 1
            ElseIf fanucAddress.StartsWith("Y") Then
                Return 2
            ElseIf fanucAddress.StartsWith("X") Then
                Return 3
            ElseIf fanucAddress.StartsWith("A") Then
                Return 4
            ElseIf fanucAddress.StartsWith("R") Then
                Return 5
            ElseIf fanucAddress.StartsWith("T") Then
                Return 6
            ElseIf fanucAddress.StartsWith("K") Then
                Return 7
            ElseIf fanucAddress.StartsWith("C") Then
                Return 8
            ElseIf fanucAddress.StartsWith("D") Then
                Return 9
            Else
                Return -1
            End If
        End Function

        Public Function getDataType() As Short
            Return 0
        End Function

        Public Function getGridViewRow() As DataGridViewRow
            Return rowObj
        End Function
    End Class

    Public Class HaasDataModel
        Public description As String
        Public modbusAddress As Integer
        Private modbusValue As Single
        Public macroAddress As Integer
        Private fanucValue As Single

        Private rowObj As DataGridViewRow

        Public Sub New()
            description = ""
        End Sub

        Public Sub New(ByVal des As String, ByVal modAddr As Integer, ByVal macroAddr As Integer, Optional tableRow As DataGridViewRow = Nothing)
            description = des
            modbusAddress = modAddr
            macroAddress = macroAddr

            rowObj = tableRow

            'If rowObj IsNot Nothing Then
            'rowObj.Cells(5).Value = 100
            'End If
        End Sub

        Public Sub SetDescription(ByVal desc As String)
            description = desc
        End Sub

        Public Function GetDescription() As String
            Return description
        End Function

        Public Sub SetModbusAddress(ByVal addr As Integer)
            modbusAddress = addr
        End Sub

        Public Function GetModbusAddress() As Integer
            Return modbusAddress
        End Function

        Public Sub SetModbusValue(ByVal val As Single)
            modbusValue = val
        End Sub

        Public Function GetModbusValue() As Single
            Return modbusValue
        End Function

        Public Sub SetMacroAddress(ByVal addr As Integer)
            macroAddress = addr
        End Sub

        Public Function GetMacroAddress() As Integer
            Return macroAddress
        End Function

        Public Sub SetFanucValue(ByVal val As Single)
            fanucValue = val
        End Sub

        Public Function GetFanucValue() As Single
            Return fanucValue
        End Function

        Public Function hasValidMacroAddress() As Boolean
            Return isValidMacroAddress(macroAddress)
        End Function

        Public Shared Function isValidMacroAddress(objVal As Object) As Boolean
            If IsNothing(objVal) Then
                Return False
            End If

            ' Check Modbus Address
            Dim fanucAddr As Integer = -1
            Try
                fanucAddr = Convert.ToInt32(objVal)
            Catch ex As Exception
            End Try

            ' Fanuc Addr is Short, Short max value is 32767
            If fanucAddr < 0 OrElse fanucAddr > 32767 Then
                Return False
            End If

            Return True
        End Function

        Public Function getGridViewRow() As DataGridViewRow
            Return rowObj
        End Function
    End Class

    Public Class FanucSettings
        Public macroSettings As List(Of MacroDataModel)
        Public pmcSettings As List(Of PMCDataModel)

        Public Sub New()
            macroSettings = New List(Of MacroDataModel)
            pmcSettings = New List(Of PMCDataModel)
        End Sub

        Public Function IsEmpty() As Boolean
            ' Check Array
            If macroSettings Is Nothing OrElse pmcSettings Is Nothing Then
                Return True
            End If

            ' Check Data
            If macroSettings.Count = 0 AndAlso pmcSettings.Count = 0 Then
                Return True
            End If

            Return False
        End Function

    End Class

    Public Class FanucData
        Public mainProgram As String
        Public currentProgram As String

        Public motion As Integer
        Public run As Integer           ' 3 means Incycle, other values, uncategorized

        Public alarm As String

        Public currSequNum As Integer

        ' Fanuc Machine Info(cnc_sysinfo)
        'For example, the following information are gotten by execution of this function on Series 16i-M (B0F1-0001) system with 
        '3 servo axes And without loader control.

        'sysinfo.addinfo  = 2
        'sysinfo.max_axis = 8
        'sysinfo.cnc_type = "16"
        'sysinfo.mt_type  = " M"
        'sysinfo.series   = "B0F1"
        'sysinfo.version  = "0001"
        'sysinfo.axes     = " 3"

        Public addinfo As Short     ' Additional information
        Public max_axis As Short    ' Max. controlled axes
        Public cnc_type As String   ' Kind of CNC (ASCII)
        Public mt_type As String    ' Kind of CNC (ASCII)
        Public series As String     ' Series number (ASCII)
        Public version As String    ' Version number (ASCII)
        Public axes As String       ' Current controlled axes(ASCII)

        Public currAutoMonitorStatus As Boolean
        Public spindleSpeed As Double
        Public autoMonitorStartTime As Long

        Public Sub New()
            mainProgram = ""
            currentProgram = ""
            motion = -1
            run = -1
            alarm = ""
            currSequNum = 0

            addinfo = 0
            max_axis = 0
            cnc_type = ""
            mt_type = ""
            series = ""
            version = ""
            axes = ""

            currAutoMonitorStatus = False
            spindleSpeed = 0
            autoMonitorStartTime = 0

        End Sub
    End Class

    Enum STATE_DEVICE
        UnCat
        InCycle
        Idle1
        Idle2
        Idle3
        Idle4
        Idle5
        Idle6
        Idle7
        Idle8
    End Enum

    Class DeviceStatus

        Private Index As Integer
        Private Name As String
        Private ColorString As String
        Private ColorRGB As Color

        Public Shared ReadOnly Uncat As DeviceStatus = New DeviceStatus(0, "Idle-Uncategorized", "#ff0000")
        Public Shared ReadOnly InCycle As DeviceStatus = New DeviceStatus(1, "InCycle", "#46c392")

        Public Shared ReadOnly Idle1 As DeviceStatus = New DeviceStatus(2, "Idle1", "#ffec00")
        Public Shared ReadOnly Idle2 As DeviceStatus = New DeviceStatus(3, "Idle2", "#549afc")
        Public Shared ReadOnly Idle3 As DeviceStatus = New DeviceStatus(4, "Idle3", "#c000db")
        Public Shared ReadOnly Idle4 As DeviceStatus = New DeviceStatus(5, "Idle4", "#9898db")
        Public Shared ReadOnly Idle5 As DeviceStatus = New DeviceStatus(6, "Idle5", "#B0E0E6")
        Public Shared ReadOnly Idle6 As DeviceStatus = New DeviceStatus(7, "Idle6", "#6aa786")
        Public Shared ReadOnly Idle7 As DeviceStatus = New DeviceStatus(8, "Idle7", "#c0a0c0")
        Public Shared ReadOnly Idle8 As DeviceStatus = New DeviceStatus(9, "Idle8", "#808080")

        Public Shared ReadOnly Offline As DeviceStatus = New DeviceStatus(10, "Offline", "#000000")

        Private Sub New(index As Integer, name As String, color As String)
            Me.Index = index
            Me.Name = name
            Me.ColorString = color
            Me.ColorRGB = ColorTranslator.FromHtml(ColorString)
        End Sub

        Public Overrides Function ToString() As String
            Return Me.ColorString
        End Function

        Public Overrides Function Equals(obj As Object) As Boolean
            If obj Is Nothing OrElse Not Me.GetType() Is obj.GetType() Then
                Return False
            End If

            Dim other As DeviceStatus = CType(obj, DeviceStatus)
            Return Me.Index = other.Index
        End Function

        Public Function getIndex() As Integer
            Return Index
        End Function

        Public Function getName() As String
            Return Name
        End Function

        Public Sub SetName(newName As String)
            If Not String.IsNullOrEmpty(newName) Then
                Name = newName
            End If
        End Sub

        Public Function getColor() As String
            Return ColorString
        End Function

        Public Function getRGBColor() As Color
            Return ColorRGB
        End Function

    End Class

    Public Class GanttData
        Public createdAt As String
        Public customerId As String
        Public machineId As String
        Public operatorName As String
        Public status As String
        Public color As String
        Public startTime As Long
        Public endTime As Long
        Public timeStamp As String
        Public timeStampMs As Long
        Public jobId As String
        Public comment As String
        Public mainProg As String
        Public currProg As String
        Public other As String = ""

        ' Not used for report, only used data management
        Public id As Long
        Public uploaded As Boolean

        Public Sub New()
            createdAt = ""
            machineId = ""
            operatorName = ""
            status = ""
            color = ""
            startTime = 0
            endTime = 0
            timeStamp = ""
            timeStampMs = 0
            jobId = ""
            comment = ""
            mainProg = ""
            currProg = ""

            id = GetCurrentTimeMilis()
            uploaded = False

        End Sub

        Public Overrides Function Equals(obj As Object) As Boolean
            If obj Is Nothing OrElse Not Me.GetType() Is obj.GetType() Then
                Return False
            End If

            Dim other As GanttData = CType(obj, GanttData)
            Return Me.id = other.id
        End Function

    End Class

    Public Class JobInfo
        Public Id As String = ""
        Public jobID As String = ""
        Public customer As String = ""
        Public partNumber As String = ""
        Public programNumber As String = ""
        Public description As String = ""
        Public partsPerCycle As String = ""
        Public targetCycleTime As String = ""
        Public qtyRequired As String = ""
        Public qtyCompleted As String = ""
        Public orderDate As String = ""
        Public dueDate As String = ""
        Public qtyGoodCompleted As String = ""
        Public qtyBadCompleted As String = ""

        Public Overloads Function ToString() As String
            Dim result = JsonConvert.SerializeObject(Me)
            Return result
        End Function

        Public Shared Function GetJobInfo(ByVal jsonData As String) As JobInfo
            Dim newInfo As JobInfo = JsonConvert.DeserializeObject(Of JobInfo)(jsonData)
            Return newInfo
        End Function

        Public Function getJobDetails() As String

            Dim cycleTime As Integer = 0
            Try
                cycleTime = CInt(targetCycleTime)
            Catch ex As Exception
            End Try

            Dim details As String = String.Format("Job ID : {0}", jobID) & vbCrLf &
                String.Format("Customer : {0}", customer) & vbCrLf &
                String.Format("Part Number : {0}", partNumber) & vbCrLf &
                String.Format("Program Number : {0}", programNumber) & vbCrLf &
                String.Format("Description : {0}", description) & vbCrLf &
                String.Format("Parts Per Cycle : {0}", partsPerCycle) & vbCrLf &
                String.Format("Target Cycle Time : {0}", GetTimeStringFromSeconds(cycleTime)) & vbCrLf &
                String.Format("Qty Required : {0}", qtyRequired) & vbCrLf &
                String.Format("Qty Good Completed : {0}", qtyGoodCompleted)

            Return details

        End Function

    End Class

    Public Class RegimeData
        Public utilization As Double
        Public offlineT As Long

        Public Availablity As Double
        Public performance As Double
        Public quality As Double
        Public oee As Double

        Public inCycleTime As Long
        Public unCatTime As Long

        Public idle1Time As Long
        Public idle2Time As Long
        Public idle3Time As Long
        Public idle4Time As Long
        Public idle5Time As Long
        Public idle6Time As Long
        Public idle7Time As Long
        Public idle8Time As Long

        Public Sub New()
            utilization = 0
            offlineT = 0

            Availablity = 0
            performance = 0
            quality = 0
            oee = 0

            inCycleTime = 0
            unCatTime = 0

            idle1Time = 0
            idle2Time = 0
            idle3Time = 0
            idle4Time = 0
            idle5Time = 0
            idle6Time = 0
            idle7Time = 0
            idle8Time = 0

        End Sub

    End Class

    Public Class DataPoint
        Public Property Argument() As String
        Public Property Value() As Double
        Public Property Color() As Color

        Public Shared Function GetDataPoints() As List(Of DataPoint)
            Return New List(Of DataPoint) From {
                    New DataPoint With {.Argument = "Russia", .Value = 17.0752, .Color = Color.Red},
                    New DataPoint With {.Argument = "Canada", .Value = 9.98467, .Color = Color.Green},
                    New DataPoint With {.Argument = "USA", .Value = 9.63142, .Color = Color.Blue},
                    New DataPoint With {.Argument = "China", .Value = 9.59696, .Color = Color.Black},
                    New DataPoint With {.Argument = "Brazil", .Value = 8.511965, .Color = Color.Red},
                    New DataPoint With {.Argument = "Australia", .Value = 7.68685},
                    New DataPoint With {.Argument = "India", .Value = 3.28759},
                    New DataPoint With {.Argument = "Others", .Value = 81.2}
                }
        End Function
    End Class

    ' Ti Collector Data Structures
    Enum TICOLLECTOR_NODE_TYPE
        NODE_MACHINE = 0            ' Machine
        NODE_TEMP_SENSOR = 1        ' Temperature Sensor
        NODE_VIB_SENSOR = 2         ' Vibration Sensor
        NODE_CURR_SENSOR = 3        ' Current Sensor
    End Enum

    Public Function getTiCollectorSensorType(ByVal type As Integer) As String
        If type = 0 Then
            Return "Machine"
        ElseIf type = 1 Then
            Return "Temperature Sensor"
        ElseIf type = 2 Then
            Return "Vibration Sensor"
        ElseIf type = 3 Then
            Return "Current Sensor"
        Else
            Return "Unknown"
        End If
    End Function

    Public Function getTiCollectorSensorInterface(ByVal type As Integer) As String
        If type = 0 Then
            Return "RF Light Sensor"
        ElseIf type = 1 Then
            Return "Temperature Sensor"
        ElseIf type = 2 Then
            Return "Vibration Sensor"
        ElseIf type = 3 Then
            Return "Current Sensor"
        Else
            Return "Unknown"
        End If
    End Function

    Public Class TiCollectorNode
        Public devType As Integer
        Public devId As Integer
        Public devName As String
        Public totalIncycle As Long
        Public totalUncat As Long
        Public lastCountingDate As String

        Private signalStrength As String
        Private batteryLife As String = ""
        Private data1 As String = "0"
        Private data2 As String = "0"
        Private data3 As String = "0"

        ' ListViewItem in the UI
        Private listViewItem As ListViewItem

        ' Save Last Report Time
        Private lastReportTimeMils As Long

        Private reportTimeInterval As Long


        Public Sub setStrength(ByVal value As String)
            Me.signalStrength = value
        End Sub

        Public Function getStrength() As String
            Return Me.signalStrength
        End Function

        Public Sub setBatLife(ByVal value As String)
            Me.batteryLife = value
        End Sub

        Public Function getBatLife() As String
            Return Me.batteryLife
        End Function

        Public Sub setData1(ByVal value As String)
            Me.data1 = value
        End Sub

        Public Function getData1() As String
            Return Me.data1
        End Function

        Public Sub setData2(ByVal value As String)
            Me.data2 = value
        End Sub

        Public Function getData2() As String
            Return Me.data2
        End Function

        Public Sub setData3(ByVal value As String)
            Me.data3 = value
        End Sub

        Public Function getData3() As String
            Return Me.data3
        End Function

        Public Sub setListViewItem(ByVal item As ListViewItem)
            Me.listViewItem = item
        End Sub

        Public Function getListViewItem()
            Return Me.listViewItem
        End Function

        Public Sub setLastReportTime(val As Long)
            Me.lastReportTimeMils = val
        End Sub

        Public Function getLastReportTime() As Long
            Return Me.lastReportTimeMils
        End Function

        Public Sub setReportTimeInterval(val As Long)
            Me.reportTimeInterval = val
        End Sub

        Public Function getReportTimeInterval() As Long
            Return Me.reportTimeInterval
        End Function

    End Class

End Module

