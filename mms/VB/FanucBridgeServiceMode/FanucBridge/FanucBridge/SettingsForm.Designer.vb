<Global.Microsoft.VisualBasic.CompilerServices.DesignerGenerated()>
Partial Class SettingsForm
    Inherits System.Windows.Forms.Form

    'Form overrides dispose to clean up the component list.
    <System.Diagnostics.DebuggerNonUserCode()>
    Protected Overrides Sub Dispose(ByVal disposing As Boolean)
        Try
            If disposing AndAlso components IsNot Nothing Then
                components.Dispose()
            End If
        Finally
            MyBase.Dispose(disposing)
        End Try
    End Sub

    'Required by the Windows Form Designer
    Private components As System.ComponentModel.IContainer

    'NOTE: The following procedure is required by the Windows Form Designer
    'It can be modified using the Windows Form Designer.  
    'Do not modify it using the code editor.
    <System.Diagnostics.DebuggerStepThrough()>
    Private Sub InitializeComponent()
        Me.components = New System.ComponentModel.Container()
        Dim resources As System.ComponentModel.ComponentResourceManager = New System.ComponentModel.ComponentResourceManager(GetType(SettingsForm))
        Me.btnSave = New System.Windows.Forms.Button()
        Me.MyTooTip = New System.Windows.Forms.ToolTip(Me.components)
        Me.btnClose = New System.Windows.Forms.Button()
        Me.GroupBox4 = New System.Windows.Forms.GroupBox()
        Me.txtPort = New System.Windows.Forms.Label()
        Me.txtPortInput = New System.Windows.Forms.TextBox()
        Me.txtIpAddress = New System.Windows.Forms.Label()
        Me.txtIpAddressInput = New System.Windows.Forms.TextBox()
        Me.SaveFileDialog = New System.Windows.Forms.SaveFileDialog()
        Me.Label1 = New System.Windows.Forms.Label()
        Me.txtReadCycleTime = New System.Windows.Forms.TextBox()
        Me.GroupBox4.SuspendLayout()
        Me.SuspendLayout()
        '
        'btnSave
        '
        Me.btnSave.Location = New System.Drawing.Point(93, 202)
        Me.btnSave.Name = "btnSave"
        Me.btnSave.Size = New System.Drawing.Size(127, 27)
        Me.btnSave.TabIndex = 35
        Me.btnSave.Text = "Save Settings"
        Me.btnSave.UseVisualStyleBackColor = True
        '
        'btnClose
        '
        Me.btnClose.Location = New System.Drawing.Point(255, 202)
        Me.btnClose.Name = "btnClose"
        Me.btnClose.Size = New System.Drawing.Size(82, 27)
        Me.btnClose.TabIndex = 37
        Me.btnClose.Text = "Close"
        Me.btnClose.UseVisualStyleBackColor = True
        '
        'GroupBox4
        '
        Me.GroupBox4.Controls.Add(Me.txtReadCycleTime)
        Me.GroupBox4.Controls.Add(Me.Label1)
        Me.GroupBox4.Controls.Add(Me.txtPort)
        Me.GroupBox4.Controls.Add(Me.txtPortInput)
        Me.GroupBox4.Controls.Add(Me.txtIpAddress)
        Me.GroupBox4.Controls.Add(Me.txtIpAddressInput)
        Me.GroupBox4.Font = New System.Drawing.Font("Microsoft Sans Serif", 8.25!, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, CType(0, Byte))
        Me.GroupBox4.Location = New System.Drawing.Point(13, 12)
        Me.GroupBox4.Name = "GroupBox4"
        Me.GroupBox4.Size = New System.Drawing.Size(324, 176)
        Me.GroupBox4.TabIndex = 56
        Me.GroupBox4.TabStop = False
        Me.GroupBox4.Text = "Fanuc Settings"
        '
        'txtPort
        '
        Me.txtPort.Font = New System.Drawing.Font("Microsoft Sans Serif", 8.25!, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, CType(0, Byte))
        Me.txtPort.Location = New System.Drawing.Point(20, 74)
        Me.txtPort.Name = "txtPort"
        Me.txtPort.Size = New System.Drawing.Size(73, 17)
        Me.txtPort.TabIndex = 59
        Me.txtPort.Text = "Port"
        '
        'txtPortInput
        '
        Me.txtPortInput.Location = New System.Drawing.Point(20, 94)
        Me.txtPortInput.Name = "txtPortInput"
        Me.txtPortInput.Size = New System.Drawing.Size(283, 20)
        Me.txtPortInput.TabIndex = 58
        Me.txtPortInput.Text = "8193"
        '
        'txtIpAddress
        '
        Me.txtIpAddress.Font = New System.Drawing.Font("Microsoft Sans Serif", 8.25!, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, CType(0, Byte))
        Me.txtIpAddress.Location = New System.Drawing.Point(20, 24)
        Me.txtIpAddress.Name = "txtIpAddress"
        Me.txtIpAddress.Size = New System.Drawing.Size(139, 17)
        Me.txtIpAddress.TabIndex = 57
        Me.txtIpAddress.Text = "Fanuc IP-Address"
        '
        'txtIpAddressInput
        '
        Me.txtIpAddressInput.Location = New System.Drawing.Point(20, 44)
        Me.txtIpAddressInput.Name = "txtIpAddressInput"
        Me.txtIpAddressInput.Size = New System.Drawing.Size(283, 20)
        Me.txtIpAddressInput.TabIndex = 56
        Me.txtIpAddressInput.Text = "192.168.1.23"
        '
        'SaveFileDialog
        '
        Me.SaveFileDialog.DefaultExt = "csv"
        Me.SaveFileDialog.Filter = "Excel Files (*.csv*)|*.csv"
        Me.SaveFileDialog.Title = "Please select report file"
        '
        'Label1
        '
        Me.Label1.AutoSize = True
        Me.Label1.Font = New System.Drawing.Font("Microsoft Sans Serif", 8.25!, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, CType(0, Byte))
        Me.Label1.Location = New System.Drawing.Point(20, 125)
        Me.Label1.Name = "Label1"
        Me.Label1.Size = New System.Drawing.Size(105, 13)
        Me.Label1.TabIndex = 60
        Me.Label1.Text = "Read cycle time (ms)"
        '
        'txtReadCycleTime
        '
        Me.txtReadCycleTime.Location = New System.Drawing.Point(20, 143)
        Me.txtReadCycleTime.Name = "txtReadCycleTime"
        Me.txtReadCycleTime.Size = New System.Drawing.Size(283, 20)
        Me.txtReadCycleTime.TabIndex = 61
        Me.txtReadCycleTime.Text = "1000"
        '
        'SettingsForm
        '
        Me.AutoScaleDimensions = New System.Drawing.SizeF(6.0!, 13.0!)
        Me.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font
        Me.ClientSize = New System.Drawing.Size(351, 238)
        Me.Controls.Add(Me.GroupBox4)
        Me.Controls.Add(Me.btnClose)
        Me.Controls.Add(Me.btnSave)
        Me.FormBorderStyle = System.Windows.Forms.FormBorderStyle.FixedDialog
        Me.Icon = CType(resources.GetObject("$this.Icon"), System.Drawing.Icon)
        Me.MaximizeBox = False
        Me.MinimizeBox = False
        Me.Name = "SettingsForm"
        Me.StartPosition = System.Windows.Forms.FormStartPosition.CenterScreen
        Me.Text = "Fanuc Machine Settings"
        Me.TopMost = True
        Me.TransparencyKey = System.Drawing.Color.Lime
        Me.GroupBox4.ResumeLayout(False)
        Me.GroupBox4.PerformLayout()
        Me.ResumeLayout(False)

    End Sub
    Friend WithEvents btnSave As Button
    Friend WithEvents MyTooTip As ToolTip
    Friend WithEvents btnClose As Button
    Friend WithEvents GroupBox4 As GroupBox
    Friend WithEvents SaveFileDialog As SaveFileDialog
    Private WithEvents txtPort As Label
    Private WithEvents txtPortInput As TextBox
    Private WithEvents txtIpAddress As Label
    Private WithEvents txtIpAddressInput As TextBox
    Private WithEvents txtReadCycleTime As TextBox
    Private WithEvents Label1 As Label
End Class
