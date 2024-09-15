
Imports System.Net
Imports System.Windows.Forms
Imports System.Drawing

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

End Module

