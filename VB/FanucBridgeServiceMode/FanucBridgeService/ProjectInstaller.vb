Imports System.Configuration.Install
Imports System.Reflection
Imports System.ServiceProcess

Public Class ProjectInstaller

    Public Sub New()
        MyBase.New()

        'This call is required by the Component Designer.
        InitializeComponent()

    End Sub

    'Private Sub ProjectInstaller_BeforeInstall(sender As Object, e As InstallEventArgs) Handles MyBase.BeforeInstall
    'Try
    '    Dim installer As New ServiceInstaller()
    '    installer.Context = New InstallContext()
    '    installer.Context.Parameters.Add("assemblypath", Context.Parameters("assemblypath"))
    '    installer.ServiceName = ServiceInstaller1.ServiceName ' Replace with your actual service name
    '    installer.Uninstall(Nothing)
    'Catch ex As Exception
    '    ' Handle any errors here
    'End Try
    'End Sub

    Private Sub ProjectInstaller_AfterInstall(sender As Object, e As InstallEventArgs) Handles MyBase.AfterInstall
        Dim sc As New ServiceController()
        sc.ServiceName = ServiceInstaller1.ServiceName

        If sc.Status = ServiceControllerStatus.Stopped Then
            Try
                ' Start the service, and wait until its status is "Running".
                sc.Start()
                sc.WaitForStatus(ServiceControllerStatus.Running)

                ' TODO: log status of service here: sc.Status
            Catch ex As Exception
                ' TODO: log an error here: "Could not start service: ex.Message"
                Throw
            End Try
        End If

        'Using serviceController As New ServiceController(ServiceInstaller1.ServiceName)
        '    Try
        '        If serviceController.Status <> ServiceControllerStatus.Running Then
        '            serviceController.Start()
        '            serviceController.WaitForStatus(ServiceControllerStatus.Running, TimeSpan.FromSeconds(10))
        '        Else
        '            serviceController.Stop()
        '            serviceController.WaitForStatus(ServiceControllerStatus.Running, TimeSpan.FromSeconds(10))
        '            serviceController.Start()
        '            serviceController.WaitForStatus(ServiceControllerStatus.Running, TimeSpan.FromSeconds(10))
        '        End If
        '    Catch ex As Exception
        '        Throw
        '    End Try

        'End Using

    End Sub

    Private Function ServiceIsInstalled(serviceName As String) As Boolean
        Dim sc As New ServiceController(serviceName)

        Try
            ' Check if the service exists
            Dim status As ServiceControllerStatus = sc.Status
            Return True
        Catch ex As InvalidOperationException
            Return False
        End Try
    End Function

    Private Sub UninstallService(serviceName As String)

        Using service As New ServiceController(serviceName)
            If service.Status <> ServiceControllerStatus.Stopped Then
                service.Stop()
                service.WaitForStatus(ServiceControllerStatus.Stopped, TimeSpan.FromSeconds(10))
            End If

            Using installer As New TransactedInstaller()
                installer.Installers.Add(New ServiceInstaller() With {.ServiceName = serviceName})
                installer.Uninstall(Nothing)
            End Using
        End Using
    End Sub

End Class
