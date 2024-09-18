
Imports System.Linq
Imports System.Threading
Imports FanucBridge.PIDLibrary

Namespace PIDLibrary
    Public Delegate Function GetDouble() As Double
    Public Delegate Sub SetDouble(ByVal value As Double)
End Namespace

Public Class PID
    Private kp As Double
    Private ki As Double
    Private kd As Double
    Private lastUpdate As DateTime
    Private lastPV As Double
    Private errSum As Double
    Private readPV As GetDouble
    Private readSP As GetDouble
    Private writeOV As SetDouble
    Private pvMax As Double
    Private pvMin As Double
    Private outMax As Double
    Private outMin As Double
    Private computeHz As Double = 20.0F
    Private runThread As Threading.Thread

    Public Property PGain As Double
        Get
            Return kp
        End Get
        Set(ByVal value As Double)
            kp = value
        End Set
    End Property

    Public Property IGain As Double
        Get
            Return ki
        End Get
        Set(ByVal value As Double)
            ki = value
        End Set
    End Property

    Public Property DGain As Double
        Get
            Return kd
        End Get
        Set(ByVal value As Double)
            kd = value
        End Set
    End Property

    Public Property PVMinVal As Double
        Get
            Return pvMin
        End Get
        Set(ByVal value As Double)
            pvMin = value
        End Set
    End Property

    Public Property PVMaxVal As Double
        Get
            Return pvMax
        End Get
        Set(ByVal value As Double)
            pvMax = value
        End Set
    End Property

    Public Property OutMinVal As Double
        Get
            Return outMin
        End Get
        Set(ByVal value As Double)
            outMin = value
        End Set
    End Property

    Public Property HzVal As Double
        Get
            Return computeHz
        End Get
        Set(ByVal value As Double)
            computeHz = value
        End Set
    End Property

    Public Property OutMaxVal As Double
        Get
            Return outMax
        End Get
        Set(ByVal value As Double)
            outMax = value
        End Set
    End Property

    Public ReadOnly Property PIDOK As Boolean
        Get
            Return runThread IsNot Nothing
        End Get
    End Property

    Public Sub New(ByVal pG As Double, ByVal iG As Double, ByVal dG As Double, ByVal pMax As Double, ByVal pMin As Double, ByVal oMax As Double, ByVal oMin As Double, ByVal pvFunc As GetDouble, ByVal spFunc As GetDouble, ByVal outFunc As SetDouble, ByVal hz As Double)
        kp = pG
        ki = iG
        kd = dG
        pvMax = pMax
        pvMin = pMin
        outMax = oMax
        outMin = oMin
        readPV = pvFunc
        readSP = spFunc
        writeOV = outFunc

        computeHz = hz

    End Sub

    Protected Overrides Sub Finalize()
        Disable()
        readPV = Nothing
        readSP = Nothing
        writeOV = Nothing
    End Sub

    Public Sub Enable()
        If runThread IsNot Nothing Then Return
        Reset()
        runThread = New Thread(New ThreadStart(AddressOf Run))
        runThread.IsBackground = True
        runThread.Name = "PID Processor"
        runThread.Start()
    End Sub

    Public Sub Disable()
        If runThread Is Nothing Then Return
        runThread.Abort()
        runThread = Nothing
    End Sub

    Public Sub Reset()
        errSum = 0.0F
        lastUpdate = Date.Now
    End Sub

    Private Function ScaleValue(ByVal value As Double, ByVal valuemin As Double, ByVal valuemax As Double, ByVal scalemin As Double, ByVal scalemax As Double) As Double
        Dim vPerc As Double = (value - valuemin) / (valuemax - valuemin)
        Dim bigSpan As Double = vPerc * (scalemax - scalemin)
        Dim retVal As Double = scalemin + bigSpan
        Return retVal
    End Function

    Private Function Clamp(ByVal value As Double, ByVal min As Double, ByVal max As Double) As Double
        If value > max Then Return max
        If value < min Then Return min
        Return value
    End Function

    Private Sub Compute()
        If readPV Is Nothing OrElse readSP Is Nothing OrElse writeOV Is Nothing Then Return
        Dim pv As Double = readPV()
        Dim sp As Double = readSP()
        pv = Clamp(pv, pvMin, pvMax)
        pv = ScaleValue(pv, pvMin, pvMax, -1.0F, 1.0F)
        sp = Clamp(sp, pvMin, pvMax)
        sp = ScaleValue(sp, pvMin, pvMax, -1.0F, 1.0F)
        Dim err As Double = sp - pv
        Dim pTerm As Double = err * kp
        Dim iTerm As Double = 0.0F
        Dim dTerm As Double = 0.0F
        Dim partialSum As Double = 0.0F
        Dim nowTime As Date = Date.Now

        If Not IsNothing(lastUpdate) Then
            Dim dT As Double = (nowTime - lastUpdate).TotalSeconds

            If pv >= pvMin AndAlso pv <= pvMax Then
                partialSum = errSum + dT * err
                iTerm = ki * partialSum
            End If

            If dT <> 0.0F Then dTerm = kd * (pv - lastPV) / dT
        End If

        lastUpdate = nowTime
        errSum = partialSum
        lastPV = pv
        Dim outReal As Double = pTerm + iTerm + dTerm

        outReal = Clamp(outReal, -1.0F, 1.0F)
        outReal = ScaleValue(outReal, -1.0F, 1.0F, outMin, outMax)
        writeOV(outReal)

    End Sub

    Private Sub Run()
        While True

            Try
                Dim sleepTime As Integer = CInt((1000 / computeHz))
                Thread.Sleep(sleepTime)
                Compute()
            Catch e As Exception
            End Try
        End While
    End Sub

End Class


Public Class StripChart
    Private pBmp As Bitmap
    Private qSP As Queue(Of Double)
    Private qPV As Queue(Of Double)
    Private qMV As Queue(Of Double)
    Private x As Integer = 0, y As Integer = 0
    Private brSP As SolidBrush
    Private brPV As SolidBrush
    Private brMV As SolidBrush
    Private pSP As Pen
    Private pPV As Pen
    Private pMV As Pen

    Public ReadOnly Property bmp As Bitmap
        Get
            Return pBmp
        End Get
    End Property

    Public Sub New()
        pBmp = New Bitmap(1007, 1007, System.Drawing.Imaging.PixelFormat.Format24bppRgb)
        qSP = New Queue(Of Double)(1000)
        qPV = New Queue(Of Double)(1000)
        qMV = New Queue(Of Double)(1000)
        brSP = New SolidBrush(Color.FromArgb(255, Color.Green))
        brPV = New SolidBrush(Color.FromArgb(128, Color.Blue))
        brMV = New SolidBrush(Color.FromArgb(128, Color.Red))
        pSP = New Pen(brSP, 5)
        pPV = New Pen(brPV, 5)
        pMV = New Pen(brMV, 5)
    End Sub

    Public Sub addSample(ByVal sp As Double, ByVal pv As Double, ByVal mv As Double)

        Dim scaleData As Double = 100.0

        sp = sp * scaleData
        pv = pv * scaleData
        mv = mv * 3

        If qSP.Count > 999 Then qSP.Dequeue()
        If qPV.Count > 999 Then qPV.Dequeue()
        If qMV.Count > 999 Then qMV.Dequeue()
        qSP.Enqueue(sp)
        qPV.Enqueue(pv)
        qMV.Enqueue(mv)
        buildChart()
    End Sub

    Public Sub clearQueus()
        qSP.Clear()
        qPV.Clear()
        qMV.Clear()
    End Sub

    Public Sub buildChart()
        Dim g As Graphics = Graphics.FromImage(pBmp)
        g.Clear(Color.LightGray)
        Dim pointSP As Point() = New Point(qSP.Count - 1) {}
        Dim pointPV As Point() = New Point(qPV.Count - 1) {}
        Dim pointMV As Point() = New Point(qMV.Count - 1) {}

        For x = 0 To qSP.Count - 1
            y = 1000 - CInt(Math.Round(qSP.ElementAt(x)))
            pointSP(x) = New Point(x, y)
            y = 1000 - CInt(Math.Round(qPV.ElementAt(x)))
            pointPV(x) = New Point(x, y)
            y = 1000 - CInt(Math.Round(qMV.ElementAt(x)))
            pointMV(x) = New Point(x, y)
        Next

        If pointSP.Length > 1 Then
            g.DrawLines(pSP, pointSP)
            g.DrawLines(pPV, pointPV)
            g.DrawLines(pMV, pointMV)
        End If

        g.Dispose()
    End Sub

End Class