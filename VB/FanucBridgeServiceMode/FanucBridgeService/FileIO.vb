Imports System.IO

Public Class FileIO
    Public Shared Sub WriteToFile(strToWrite As String)
        If String.IsNullOrEmpty(strToWrite) Then
            Return
        End If

        'Dim logFileName As String = "C:\FanucBridgeServiceLog(" + Date.Now.ToString("yyyyMMdd") + ").txt"
        Dim logFileName As String = AppDomain.CurrentDomain.BaseDirectory + Path.DirectorySeparatorChar + "FanucBridgeServiceLog(" + Date.Now.ToString("yyyyMMdd") + ").txt"

        Try
            Dim stream As IO.StreamWriter = New IO.StreamWriter(logFileName, True)

            Dim lineLog As String = GetCurrentDateTimeString() & " " & strToWrite

            stream.WriteLine(lineLog)
            stream.Flush()
            stream.Close()
        Catch ex As Exception
        End Try
    End Sub

    Public Shared Sub WriteToFile0(strToWrite As String)

        If String.IsNullOrEmpty(strToWrite) Then
            Return
        End If

        Dim logFileName As String = "FanucBridgeServiceLog.txt"
        Try
            Dim stream As IO.StreamWriter = New IO.StreamWriter(AppDomain.CurrentDomain.BaseDirectory + Path.DirectorySeparatorChar + logFileName, True)

            Dim lineLog As String = GetCurrentDateTimeString() & " " & strToWrite

            stream.WriteLine(lineLog)
            stream.Flush()
            stream.Close()
        Catch ex As Exception
        End Try
    End Sub

End Class
