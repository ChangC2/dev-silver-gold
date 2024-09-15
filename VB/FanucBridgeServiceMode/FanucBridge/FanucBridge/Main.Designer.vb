<Global.Microsoft.VisualBasic.CompilerServices.DesignerGenerated()>
Partial Class Main
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
        Dim resources As System.ComponentModel.ComponentResourceManager = New System.ComponentModel.ComponentResourceManager(GetType(Main))
        Dim DataGridViewCellStyle1 As System.Windows.Forms.DataGridViewCellStyle = New System.Windows.Forms.DataGridViewCellStyle()
        Dim DataGridViewCellStyle2 As System.Windows.Forms.DataGridViewCellStyle = New System.Windows.Forms.DataGridViewCellStyle()
        Me.btnDisconnect = New System.Windows.Forms.Button()
        Me.btnConnect = New System.Windows.Forms.Button()
        Me.Timer1 = New System.Windows.Forms.Timer(Me.components)
        Me.notifyIcon = New System.Windows.Forms.NotifyIcon(Me.components)
        Me.lblAppVersion = New System.Windows.Forms.Label()
        Me.tabTiConn = New System.Windows.Forms.TabControl()
        Me.tabTMS = New System.Windows.Forms.TabPage()
        Me.btnAddr4OnOff = New System.Windows.Forms.Button()
        Me.btnAddr3OnOff = New System.Windows.Forms.Button()
        Me.lblRunVal = New System.Windows.Forms.Label()
        Me.lblCncType = New System.Windows.Forms.Label()
        Me.lblFeedFoldStatus = New System.Windows.Forms.Label()
        Me.lblTWriteStatus = New System.Windows.Forms.Label()
        Me.lblCurrentToolNum = New System.Windows.Forms.Label()
        Me.lblModbusSvrIP = New System.Windows.Forms.Label()
        Me.titleModbusSrvInfo = New System.Windows.Forms.Label()
        Me.Label2 = New System.Windows.Forms.Label()
        Me.Label1 = New System.Windows.Forms.Label()
        Me.gridViewPMCs = New System.Windows.Forms.DataGridView()
        Me.DataGridViewTextBoxColumn1 = New System.Windows.Forms.DataGridViewTextBoxColumn()
        Me.DataGridViewTextBoxColumn2 = New System.Windows.Forms.DataGridViewTextBoxColumn()
        Me.DataGridViewTextBoxColumn3 = New System.Windows.Forms.DataGridViewTextBoxColumn()
        Me.DataGridViewTextBoxColumn4 = New System.Windows.Forms.DataGridViewTextBoxColumn()
        Me.DataGridViewTextBoxColumn5 = New System.Windows.Forms.DataGridViewTextBoxColumn()
        Me.DataGridViewTextBoxColumn6 = New System.Windows.Forms.DataGridViewTextBoxColumn()
        Me.DataGridViewTextBoxColumn7 = New System.Windows.Forms.DataGridViewTextBoxColumn()
        Me.DataGridViewTextBoxColumn8 = New System.Windows.Forms.DataGridViewTextBoxColumn()
        Me.gridViewMacros = New System.Windows.Forms.DataGridView()
        Me.No = New System.Windows.Forms.DataGridViewTextBoxColumn()
        Me.Desc = New System.Windows.Forms.DataGridViewTextBoxColumn()
        Me.ModbusAddr = New System.Windows.Forms.DataGridViewTextBoxColumn()
        Me.ModbusVal = New System.Windows.Forms.DataGridViewTextBoxColumn()
        Me.FanucAddr = New System.Windows.Forms.DataGridViewTextBoxColumn()
        Me.FanucVal = New System.Windows.Forms.DataGridViewTextBoxColumn()
        Me.Status = New System.Windows.Forms.DataGridViewTextBoxColumn()
        Me.WStatus = New System.Windows.Forms.DataGridViewTextBoxColumn()
        Me.tabTestFunctions = New System.Windows.Forms.TabPage()
        Me.GroupBox8 = New System.Windows.Forms.GroupBox()
        Me.lblLastEditTimeInfo = New System.Windows.Forms.Label()
        Me.btnGetLastEditTimeForCurrentProgramNumber = New System.Windows.Forms.Button()
        Me.GroupBox7 = New System.Windows.Forms.GroupBox()
        Me.btnReadModalMCode = New System.Windows.Forms.Button()
        Me.btnReadModalFeedrate = New System.Windows.Forms.Button()
        Me.btnReadModalSpindle = New System.Windows.Forms.Button()
        Me.btnReadModalTool = New System.Windows.Forms.Button()
        Me.lblReadModalInfo = New System.Windows.Forms.Label()
        Me.btnReadModalData = New System.Windows.Forms.Button()
        Me.txtModalBlock = New System.Windows.Forms.TextBox()
        Me.Label10 = New System.Windows.Forms.Label()
        Me.txtModalType = New System.Windows.Forms.TextBox()
        Me.Label9 = New System.Windows.Forms.Label()
        Me.GroupBox6 = New System.Windows.Forms.GroupBox()
        Me.txtCurrProgramFileInfo = New System.Windows.Forms.TextBox()
        Me.btnReadCurrProgInfo = New System.Windows.Forms.Button()
        Me.GroupBox5 = New System.Windows.Forms.GroupBox()
        Me.txtSubFolderReqNum = New System.Windows.Forms.TextBox()
        Me.Label8 = New System.Windows.Forms.Label()
        Me.lblFilePropertiesInfo = New System.Windows.Forms.Label()
        Me.btnReadFileProperty = New System.Windows.Forms.Button()
        Me.btnGetFolderPath1 = New System.Windows.Forms.Button()
        Me.txtFolderPath1 = New System.Windows.Forms.TextBox()
        Me.Label7 = New System.Windows.Forms.Label()
        Me.txtMaxNumOfProg = New System.Windows.Forms.TextBox()
        Me.Label6 = New System.Windows.Forms.Label()
        Me.GroupBox4 = New System.Windows.Forms.GroupBox()
        Me.lblCurrProgNameInfo = New System.Windows.Forms.Label()
        Me.btnReadCurrentProgName = New System.Windows.Forms.Button()
        Me.GroupBox3 = New System.Windows.Forms.GroupBox()
        Me.lblCurrSequenceNumInfo = New System.Windows.Forms.Label()
        Me.btnReadSequenceNum = New System.Windows.Forms.Button()
        Me.GroupBox2 = New System.Windows.Forms.GroupBox()
        Me.lblCopyProgInfo = New System.Windows.Forms.Label()
        Me.txtDestNCProgNum = New System.Windows.Forms.TextBox()
        Me.txtSrcNCProgNum = New System.Windows.Forms.TextBox()
        Me.btnCopyProg = New System.Windows.Forms.Button()
        Me.Label5 = New System.Windows.Forms.Label()
        Me.Label3 = New System.Windows.Forms.Label()
        Me.GroupBox1 = New System.Windows.Forms.GroupBox()
        Me.btnRunSpeedCheck = New System.Windows.Forms.Button()
        Me.lblSpeedCheckInfo = New System.Windows.Forms.Label()
        Me.tabPIDControl = New System.Windows.Forms.TabPage()
        Me.lblPIDTestValBinary = New System.Windows.Forms.Label()
        Me.btnWriteTestPIDVal = New System.Windows.Forms.Button()
        Me.txtPIDTestVal = New System.Windows.Forms.TextBox()
        Me.Label30 = New System.Windows.Forms.Label()
        Me.GroupBox10 = New System.Windows.Forms.GroupBox()
        Me.tbAdaptiveEnableAddr = New System.Windows.Forms.TextBox()
        Me.Label28 = New System.Windows.Forms.Label()
        Me.cbBoxTypePMC = New System.Windows.Forms.ComboBox()
        Me.Label27 = New System.Windows.Forms.Label()
        Me.GroupBox9 = New System.Windows.Forms.GroupBox()
        Me.tbPLCPort = New System.Windows.Forms.TextBox()
        Me.Label26 = New System.Windows.Forms.Label()
        Me.tbPLCIP = New System.Windows.Forms.TextBox()
        Me.Label25 = New System.Windows.Forms.Label()
        Me.lblStatusPIDOutToCNC = New System.Windows.Forms.Label()
        Me.tbOMax = New System.Windows.Forms.TextBox()
        Me.Label23 = New System.Windows.Forms.Label()
        Me.tbOMin = New System.Windows.Forms.TextBox()
        Me.Label24 = New System.Windows.Forms.Label()
        Me.lblOutput = New System.Windows.Forms.Label()
        Me.Label22 = New System.Windows.Forms.Label()
        Me.progBarOut = New System.Windows.Forms.ProgressBar()
        Me.Label19 = New System.Windows.Forms.Label()
        Me.Label20 = New System.Windows.Forms.Label()
        Me.Label21 = New System.Windows.Forms.Label()
        Me.pbPIDChart = New System.Windows.Forms.PictureBox()
        Me.btnStart = New System.Windows.Forms.Button()
        Me.lblInterval = New System.Windows.Forms.Label()
        Me.Label18 = New System.Windows.Forms.Label()
        Me.trackBarInterval = New System.Windows.Forms.TrackBar()
        Me.lblSP = New System.Windows.Forms.Label()
        Me.Label13 = New System.Windows.Forms.Label()
        Me.trackBarSP = New System.Windows.Forms.TrackBar()
        Me.progBarPV = New System.Windows.Forms.ProgressBar()
        Me.lblPV = New System.Windows.Forms.Label()
        Me.Label12 = New System.Windows.Forms.Label()
        Me.tbPVMax = New System.Windows.Forms.TextBox()
        Me.Label11 = New System.Windows.Forms.Label()
        Me.tbPVMin = New System.Windows.Forms.TextBox()
        Me.Label17 = New System.Windows.Forms.Label()
        Me.tbKd = New System.Windows.Forms.TextBox()
        Me.Label14 = New System.Windows.Forms.Label()
        Me.tbKi = New System.Windows.Forms.TextBox()
        Me.Label15 = New System.Windows.Forms.Label()
        Me.tbKp = New System.Windows.Forms.TextBox()
        Me.Label16 = New System.Windows.Forms.Label()
        Me.PictureBox1 = New System.Windows.Forms.PictureBox()
        Me.lblStateInfo = New System.Windows.Forms.Label()
        Me.lblDateTime = New System.Windows.Forms.Label()
        Me.Label4 = New System.Windows.Forms.Label()
        Me.OpenFileDialog1 = New System.Windows.Forms.OpenFileDialog()
        Me.btnSettings = New System.Windows.Forms.PictureBox()
        Me.tmrChart = New System.Windows.Forms.Timer(Me.components)
        Me.tmrReadPLC = New System.Windows.Forms.Timer(Me.components)
        Me.chkAutoMonitor = New System.Windows.Forms.CheckBox()
        Me.txtSpindleTime = New System.Windows.Forms.TextBox()
        Me.lblSpindleTime = New System.Windows.Forms.Label()
        Me.Label29 = New System.Windows.Forms.Label()
        Me.txtThreshold = New System.Windows.Forms.TextBox()
        Me.lblCurrSpindleRPM = New System.Windows.Forms.Label()
        Me.lblTValues = New System.Windows.Forms.Label()
        Me.Label32 = New System.Windows.Forms.Label()
        Me.Label33 = New System.Windows.Forms.Label()
        Me.lblSValues = New System.Windows.Forms.Label()
        Me.Label35 = New System.Windows.Forms.Label()
        Me.lblFValues = New System.Windows.Forms.Label()
        Me.Label37 = New System.Windows.Forms.Label()
        Me.lblMValues = New System.Windows.Forms.Label()
        Me.lblSeqNum = New System.Windows.Forms.Label()
        Me.tabTiConn.SuspendLayout()
        Me.tabTMS.SuspendLayout()
        CType(Me.gridViewPMCs, System.ComponentModel.ISupportInitialize).BeginInit()
        CType(Me.gridViewMacros, System.ComponentModel.ISupportInitialize).BeginInit()
        Me.tabTestFunctions.SuspendLayout()
        Me.GroupBox8.SuspendLayout()
        Me.GroupBox7.SuspendLayout()
        Me.GroupBox6.SuspendLayout()
        Me.GroupBox5.SuspendLayout()
        Me.GroupBox4.SuspendLayout()
        Me.GroupBox3.SuspendLayout()
        Me.GroupBox2.SuspendLayout()
        Me.GroupBox1.SuspendLayout()
        Me.tabPIDControl.SuspendLayout()
        Me.GroupBox10.SuspendLayout()
        Me.GroupBox9.SuspendLayout()
        CType(Me.pbPIDChart, System.ComponentModel.ISupportInitialize).BeginInit()
        CType(Me.trackBarInterval, System.ComponentModel.ISupportInitialize).BeginInit()
        CType(Me.trackBarSP, System.ComponentModel.ISupportInitialize).BeginInit()
        CType(Me.PictureBox1, System.ComponentModel.ISupportInitialize).BeginInit()
        CType(Me.btnSettings, System.ComponentModel.ISupportInitialize).BeginInit()
        Me.SuspendLayout()
        '
        'btnDisconnect
        '
        Me.btnDisconnect.BackColor = System.Drawing.Color.Red
        Me.btnDisconnect.Enabled = False
        Me.btnDisconnect.Font = New System.Drawing.Font("Microsoft Sans Serif", 8.25!, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, CType(0, Byte))
        Me.btnDisconnect.ForeColor = System.Drawing.Color.FromArgb(CType(CType(255, Byte), Integer), CType(CType(255, Byte), Integer), CType(CType(192, Byte), Integer))
        Me.btnDisconnect.Location = New System.Drawing.Point(907, 4)
        Me.btnDisconnect.Name = "btnDisconnect"
        Me.btnDisconnect.Size = New System.Drawing.Size(100, 32)
        Me.btnDisconnect.TabIndex = 21
        Me.btnDisconnect.Text = "Disconnected"
        Me.btnDisconnect.UseVisualStyleBackColor = False
        '
        'btnConnect
        '
        Me.btnConnect.BackColor = System.Drawing.Color.Lime
        Me.btnConnect.Location = New System.Drawing.Point(799, 4)
        Me.btnConnect.Name = "btnConnect"
        Me.btnConnect.Size = New System.Drawing.Size(100, 32)
        Me.btnConnect.TabIndex = 20
        Me.btnConnect.Text = "Start"
        Me.btnConnect.UseVisualStyleBackColor = False
        '
        'Timer1
        '
        Me.Timer1.Interval = 200
        '
        'notifyIcon
        '
        Me.notifyIcon.BalloonTipIcon = System.Windows.Forms.ToolTipIcon.Info
        Me.notifyIcon.BalloonTipText = "Fanuc Bridge is now running."
        Me.notifyIcon.BalloonTipTitle = "Fanuc Bridge"
        Me.notifyIcon.Icon = CType(resources.GetObject("notifyIcon.Icon"), System.Drawing.Icon)
        Me.notifyIcon.Text = "NotifyIcon1"
        '
        'lblAppVersion
        '
        Me.lblAppVersion.AutoSize = True
        Me.lblAppVersion.Font = New System.Drawing.Font("Microsoft Sans Serif", 8.25!, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, CType(0, Byte))
        Me.lblAppVersion.Location = New System.Drawing.Point(16, 681)
        Me.lblAppVersion.Name = "lblAppVersion"
        Me.lblAppVersion.Size = New System.Drawing.Size(51, 13)
        Me.lblAppVersion.TabIndex = 59
        Me.lblAppVersion.Text = "V3.1.19"
        Me.lblAppVersion.TextAlign = System.Drawing.ContentAlignment.MiddleLeft
        '
        'tabTiConn
        '
        Me.tabTiConn.Controls.Add(Me.tabTMS)
        Me.tabTiConn.Controls.Add(Me.tabTestFunctions)
        Me.tabTiConn.Controls.Add(Me.tabPIDControl)
        Me.tabTiConn.Location = New System.Drawing.Point(17, 42)
        Me.tabTiConn.Name = "tabTiConn"
        Me.tabTiConn.SelectedIndex = 0
        Me.tabTiConn.Size = New System.Drawing.Size(1031, 611)
        Me.tabTiConn.TabIndex = 60
        '
        'tabTMS
        '
        Me.tabTMS.Controls.Add(Me.btnAddr4OnOff)
        Me.tabTMS.Controls.Add(Me.btnAddr3OnOff)
        Me.tabTMS.Controls.Add(Me.lblRunVal)
        Me.tabTMS.Controls.Add(Me.lblCncType)
        Me.tabTMS.Controls.Add(Me.lblFeedFoldStatus)
        Me.tabTMS.Controls.Add(Me.lblTWriteStatus)
        Me.tabTMS.Controls.Add(Me.lblCurrentToolNum)
        Me.tabTMS.Controls.Add(Me.lblModbusSvrIP)
        Me.tabTMS.Controls.Add(Me.titleModbusSrvInfo)
        Me.tabTMS.Controls.Add(Me.Label2)
        Me.tabTMS.Controls.Add(Me.Label1)
        Me.tabTMS.Controls.Add(Me.gridViewPMCs)
        Me.tabTMS.Controls.Add(Me.gridViewMacros)
        Me.tabTMS.Location = New System.Drawing.Point(4, 22)
        Me.tabTMS.Name = "tabTMS"
        Me.tabTMS.Padding = New System.Windows.Forms.Padding(3)
        Me.tabTMS.Size = New System.Drawing.Size(1023, 585)
        Me.tabTMS.TabIndex = 1
        Me.tabTMS.Text = "TMS"
        Me.tabTMS.UseVisualStyleBackColor = True
        '
        'btnAddr4OnOff
        '
        Me.btnAddr4OnOff.Location = New System.Drawing.Point(38, 297)
        Me.btnAddr4OnOff.Name = "btnAddr4OnOff"
        Me.btnAddr4OnOff.Size = New System.Drawing.Size(110, 23)
        Me.btnAddr4OnOff.TabIndex = 74
        Me.btnAddr4OnOff.Text = "Addr 4 On/Off"
        Me.btnAddr4OnOff.UseVisualStyleBackColor = True
        '
        'btnAddr3OnOff
        '
        Me.btnAddr3OnOff.Location = New System.Drawing.Point(38, 268)
        Me.btnAddr3OnOff.Name = "btnAddr3OnOff"
        Me.btnAddr3OnOff.Size = New System.Drawing.Size(110, 23)
        Me.btnAddr3OnOff.TabIndex = 73
        Me.btnAddr3OnOff.Text = "Addr 3 On/Off"
        Me.btnAddr3OnOff.UseVisualStyleBackColor = True
        '
        'lblRunVal
        '
        Me.lblRunVal.AutoSize = True
        Me.lblRunVal.Location = New System.Drawing.Point(842, 285)
        Me.lblRunVal.Name = "lblRunVal"
        Me.lblRunVal.Size = New System.Drawing.Size(72, 13)
        Me.lblRunVal.TabIndex = 72
        Me.lblRunVal.Text = "RUN VALUE:"
        '
        'lblCncType
        '
        Me.lblCncType.AutoSize = True
        Me.lblCncType.Location = New System.Drawing.Point(842, 268)
        Me.lblCncType.Name = "lblCncType"
        Me.lblCncType.Size = New System.Drawing.Size(63, 13)
        Me.lblCncType.TabIndex = 71
        Me.lblCncType.Text = "CNC TYPE:"
        '
        'lblFeedFoldStatus
        '
        Me.lblFeedFoldStatus.AutoSize = True
        Me.lblFeedFoldStatus.Location = New System.Drawing.Point(842, 302)
        Me.lblFeedFoldStatus.Name = "lblFeedFoldStatus"
        Me.lblFeedFoldStatus.Size = New System.Drawing.Size(69, 13)
        Me.lblFeedFoldStatus.TabIndex = 70
        Me.lblFeedFoldStatus.Text = "FEED FOLD:"
        '
        'lblTWriteStatus
        '
        Me.lblTWriteStatus.AutoSize = True
        Me.lblTWriteStatus.Location = New System.Drawing.Point(496, 284)
        Me.lblTWriteStatus.Name = "lblTWriteStatus"
        Me.lblTWriteStatus.Size = New System.Drawing.Size(93, 13)
        Me.lblTWriteStatus.TabIndex = 66
        Me.lblTWriteStatus.Text = "CNC Write Status:"
        '
        'lblCurrentToolNum
        '
        Me.lblCurrentToolNum.AutoSize = True
        Me.lblCurrentToolNum.Location = New System.Drawing.Point(259, 284)
        Me.lblCurrentToolNum.Name = "lblCurrentToolNum"
        Me.lblCurrentToolNum.Size = New System.Drawing.Size(102, 13)
        Me.lblCurrentToolNum.TabIndex = 65
        Me.lblCurrentToolNum.Text = "Current Tool Num: 0"
        '
        'lblModbusSvrIP
        '
        Me.lblModbusSvrIP.AutoSize = True
        Me.lblModbusSvrIP.Font = New System.Drawing.Font("Microsoft Sans Serif", 8.25!, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, CType(0, Byte))
        Me.lblModbusSvrIP.Location = New System.Drawing.Point(573, 560)
        Me.lblModbusSvrIP.Name = "lblModbusSvrIP"
        Me.lblModbusSvrIP.Size = New System.Drawing.Size(61, 13)
        Me.lblModbusSvrIP.TabIndex = 62
        Me.lblModbusSvrIP.Text = "127.0.0.1"
        '
        'titleModbusSrvInfo
        '
        Me.titleModbusSrvInfo.AutoSize = True
        Me.titleModbusSrvInfo.Font = New System.Drawing.Font("Microsoft Sans Serif", 8.25!, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, CType(0, Byte))
        Me.titleModbusSrvInfo.Location = New System.Drawing.Point(397, 560)
        Me.titleModbusSrvInfo.Name = "titleModbusSrvInfo"
        Me.titleModbusSrvInfo.Size = New System.Drawing.Size(171, 13)
        Me.titleModbusSrvInfo.TabIndex = 61
        Me.titleModbusSrvInfo.Text = "Modbus Server Information : "
        '
        'Label2
        '
        Me.Label2.AutoSize = True
        Me.Label2.Font = New System.Drawing.Font("Microsoft Sans Serif", 9.75!, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, CType(0, Byte))
        Me.Label2.Location = New System.Drawing.Point(70, 27)
        Me.Label2.Name = "Label2"
        Me.Label2.Size = New System.Drawing.Size(868, 16)
        Me.Label2.TabIndex = 31
        Me.Label2.Text = "------------------------------------------------------------------------------- N" &
    "C  Data ------------------------------------------------------------------------" &
    "-------"
        '
        'Label1
        '
        Me.Label1.AutoSize = True
        Me.Label1.Font = New System.Drawing.Font("Microsoft Sans Serif", 9.75!, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, CType(0, Byte))
        Me.Label1.Location = New System.Drawing.Point(70, 316)
        Me.Label1.Name = "Label1"
        Me.Label1.Size = New System.Drawing.Size(875, 16)
        Me.Label1.TabIndex = 30
        Me.Label1.Text = "------------------------------------------------------------------------------- P" &
    "MC Data ------------------------------------------------------------------------" &
    "-------"
        '
        'gridViewPMCs
        '
        DataGridViewCellStyle1.WrapMode = System.Windows.Forms.DataGridViewTriState.[True]
        Me.gridViewPMCs.AlternatingRowsDefaultCellStyle = DataGridViewCellStyle1
        Me.gridViewPMCs.BackgroundColor = System.Drawing.SystemColors.Control
        Me.gridViewPMCs.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize
        Me.gridViewPMCs.Columns.AddRange(New System.Windows.Forms.DataGridViewColumn() {Me.DataGridViewTextBoxColumn1, Me.DataGridViewTextBoxColumn2, Me.DataGridViewTextBoxColumn3, Me.DataGridViewTextBoxColumn4, Me.DataGridViewTextBoxColumn5, Me.DataGridViewTextBoxColumn6, Me.DataGridViewTextBoxColumn7, Me.DataGridViewTextBoxColumn8})
        Me.gridViewPMCs.EditMode = System.Windows.Forms.DataGridViewEditMode.EditOnKeystroke
        Me.gridViewPMCs.GridColor = System.Drawing.SystemColors.Control
        Me.gridViewPMCs.Location = New System.Drawing.Point(38, 337)
        Me.gridViewPMCs.Name = "gridViewPMCs"
        Me.gridViewPMCs.SelectionMode = System.Windows.Forms.DataGridViewSelectionMode.FullRowSelect
        Me.gridViewPMCs.Size = New System.Drawing.Size(942, 215)
        Me.gridViewPMCs.TabIndex = 29
        '
        'DataGridViewTextBoxColumn1
        '
        Me.DataGridViewTextBoxColumn1.AutoSizeMode = System.Windows.Forms.DataGridViewAutoSizeColumnMode.DisplayedCells
        Me.DataGridViewTextBoxColumn1.Frozen = True
        Me.DataGridViewTextBoxColumn1.HeaderText = "No"
        Me.DataGridViewTextBoxColumn1.Name = "DataGridViewTextBoxColumn1"
        Me.DataGridViewTextBoxColumn1.ReadOnly = True
        Me.DataGridViewTextBoxColumn1.SortMode = System.Windows.Forms.DataGridViewColumnSortMode.NotSortable
        Me.DataGridViewTextBoxColumn1.Width = 27
        '
        'DataGridViewTextBoxColumn2
        '
        Me.DataGridViewTextBoxColumn2.AutoSizeMode = System.Windows.Forms.DataGridViewAutoSizeColumnMode.Fill
        Me.DataGridViewTextBoxColumn2.HeaderText = "Description"
        Me.DataGridViewTextBoxColumn2.Name = "DataGridViewTextBoxColumn2"
        Me.DataGridViewTextBoxColumn2.SortMode = System.Windows.Forms.DataGridViewColumnSortMode.NotSortable
        '
        'DataGridViewTextBoxColumn3
        '
        Me.DataGridViewTextBoxColumn3.HeaderText = "Modbus Address"
        Me.DataGridViewTextBoxColumn3.Name = "DataGridViewTextBoxColumn3"
        Me.DataGridViewTextBoxColumn3.SortMode = System.Windows.Forms.DataGridViewColumnSortMode.NotSortable
        '
        'DataGridViewTextBoxColumn4
        '
        Me.DataGridViewTextBoxColumn4.HeaderText = "Modbus Value"
        Me.DataGridViewTextBoxColumn4.Name = "DataGridViewTextBoxColumn4"
        Me.DataGridViewTextBoxColumn4.ReadOnly = True
        Me.DataGridViewTextBoxColumn4.SortMode = System.Windows.Forms.DataGridViewColumnSortMode.NotSortable
        '
        'DataGridViewTextBoxColumn5
        '
        Me.DataGridViewTextBoxColumn5.HeaderText = "FanucAddress"
        Me.DataGridViewTextBoxColumn5.Name = "DataGridViewTextBoxColumn5"
        Me.DataGridViewTextBoxColumn5.SortMode = System.Windows.Forms.DataGridViewColumnSortMode.NotSortable
        '
        'DataGridViewTextBoxColumn6
        '
        Me.DataGridViewTextBoxColumn6.HeaderText = "Fanuc Value"
        Me.DataGridViewTextBoxColumn6.Name = "DataGridViewTextBoxColumn6"
        Me.DataGridViewTextBoxColumn6.ReadOnly = True
        Me.DataGridViewTextBoxColumn6.SortMode = System.Windows.Forms.DataGridViewColumnSortMode.NotSortable
        '
        'DataGridViewTextBoxColumn7
        '
        Me.DataGridViewTextBoxColumn7.HeaderText = "Read Status"
        Me.DataGridViewTextBoxColumn7.Name = "DataGridViewTextBoxColumn7"
        Me.DataGridViewTextBoxColumn7.ReadOnly = True
        Me.DataGridViewTextBoxColumn7.Width = 150
        '
        'DataGridViewTextBoxColumn8
        '
        Me.DataGridViewTextBoxColumn8.HeaderText = "Write Status"
        Me.DataGridViewTextBoxColumn8.Name = "DataGridViewTextBoxColumn8"
        Me.DataGridViewTextBoxColumn8.ReadOnly = True
        Me.DataGridViewTextBoxColumn8.Width = 150
        '
        'gridViewMacros
        '
        DataGridViewCellStyle2.WrapMode = System.Windows.Forms.DataGridViewTriState.[True]
        Me.gridViewMacros.AlternatingRowsDefaultCellStyle = DataGridViewCellStyle2
        Me.gridViewMacros.BackgroundColor = System.Drawing.SystemColors.Control
        Me.gridViewMacros.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize
        Me.gridViewMacros.Columns.AddRange(New System.Windows.Forms.DataGridViewColumn() {Me.No, Me.Desc, Me.ModbusAddr, Me.ModbusVal, Me.FanucAddr, Me.FanucVal, Me.Status, Me.WStatus})
        Me.gridViewMacros.EditMode = System.Windows.Forms.DataGridViewEditMode.EditOnKeystroke
        Me.gridViewMacros.GridColor = System.Drawing.SystemColors.Control
        Me.gridViewMacros.Location = New System.Drawing.Point(38, 46)
        Me.gridViewMacros.Name = "gridViewMacros"
        Me.gridViewMacros.SelectionMode = System.Windows.Forms.DataGridViewSelectionMode.FullRowSelect
        Me.gridViewMacros.Size = New System.Drawing.Size(942, 215)
        Me.gridViewMacros.TabIndex = 28
        '
        'No
        '
        Me.No.AutoSizeMode = System.Windows.Forms.DataGridViewAutoSizeColumnMode.DisplayedCells
        Me.No.Frozen = True
        Me.No.HeaderText = "No"
        Me.No.Name = "No"
        Me.No.ReadOnly = True
        Me.No.SortMode = System.Windows.Forms.DataGridViewColumnSortMode.NotSortable
        Me.No.Width = 27
        '
        'Desc
        '
        Me.Desc.AutoSizeMode = System.Windows.Forms.DataGridViewAutoSizeColumnMode.Fill
        Me.Desc.HeaderText = "Description"
        Me.Desc.Name = "Desc"
        Me.Desc.SortMode = System.Windows.Forms.DataGridViewColumnSortMode.NotSortable
        '
        'ModbusAddr
        '
        Me.ModbusAddr.HeaderText = "Modbus Address"
        Me.ModbusAddr.Name = "ModbusAddr"
        Me.ModbusAddr.SortMode = System.Windows.Forms.DataGridViewColumnSortMode.NotSortable
        '
        'ModbusVal
        '
        Me.ModbusVal.HeaderText = "Modbus Value"
        Me.ModbusVal.Name = "ModbusVal"
        Me.ModbusVal.ReadOnly = True
        Me.ModbusVal.SortMode = System.Windows.Forms.DataGridViewColumnSortMode.NotSortable
        '
        'FanucAddr
        '
        Me.FanucAddr.HeaderText = "FanucAddress"
        Me.FanucAddr.Name = "FanucAddr"
        Me.FanucAddr.SortMode = System.Windows.Forms.DataGridViewColumnSortMode.NotSortable
        '
        'FanucVal
        '
        Me.FanucVal.HeaderText = "Fanuc Value"
        Me.FanucVal.Name = "FanucVal"
        Me.FanucVal.ReadOnly = True
        Me.FanucVal.SortMode = System.Windows.Forms.DataGridViewColumnSortMode.NotSortable
        '
        'Status
        '
        Me.Status.HeaderText = "Read Status"
        Me.Status.Name = "Status"
        Me.Status.ReadOnly = True
        Me.Status.Width = 150
        '
        'WStatus
        '
        Me.WStatus.HeaderText = "Write Status"
        Me.WStatus.Name = "WStatus"
        Me.WStatus.Width = 150
        '
        'tabTestFunctions
        '
        Me.tabTestFunctions.Controls.Add(Me.GroupBox8)
        Me.tabTestFunctions.Controls.Add(Me.GroupBox7)
        Me.tabTestFunctions.Controls.Add(Me.GroupBox6)
        Me.tabTestFunctions.Controls.Add(Me.GroupBox5)
        Me.tabTestFunctions.Controls.Add(Me.GroupBox4)
        Me.tabTestFunctions.Controls.Add(Me.GroupBox3)
        Me.tabTestFunctions.Controls.Add(Me.GroupBox2)
        Me.tabTestFunctions.Controls.Add(Me.GroupBox1)
        Me.tabTestFunctions.Location = New System.Drawing.Point(4, 22)
        Me.tabTestFunctions.Name = "tabTestFunctions"
        Me.tabTestFunctions.Padding = New System.Windows.Forms.Padding(3)
        Me.tabTestFunctions.Size = New System.Drawing.Size(1023, 585)
        Me.tabTestFunctions.TabIndex = 2
        Me.tabTestFunctions.Text = "Test Functions"
        Me.tabTestFunctions.UseVisualStyleBackColor = True
        '
        'GroupBox8
        '
        Me.GroupBox8.Controls.Add(Me.lblLastEditTimeInfo)
        Me.GroupBox8.Controls.Add(Me.btnGetLastEditTimeForCurrentProgramNumber)
        Me.GroupBox8.Location = New System.Drawing.Point(456, 269)
        Me.GroupBox8.Name = "GroupBox8"
        Me.GroupBox8.Size = New System.Drawing.Size(537, 100)
        Me.GroupBox8.TabIndex = 80
        Me.GroupBox8.TabStop = False
        Me.GroupBox8.Text = "Get the last edit time for the current program number"
        '
        'lblLastEditTimeInfo
        '
        Me.lblLastEditTimeInfo.Anchor = CType((((System.Windows.Forms.AnchorStyles.Top Or System.Windows.Forms.AnchorStyles.Bottom) _
            Or System.Windows.Forms.AnchorStyles.Left) _
            Or System.Windows.Forms.AnchorStyles.Right), System.Windows.Forms.AnchorStyles)
        Me.lblLastEditTimeInfo.AutoSize = True
        Me.lblLastEditTimeInfo.Font = New System.Drawing.Font("Microsoft Sans Serif", 8.25!, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, CType(0, Byte))
        Me.lblLastEditTimeInfo.Location = New System.Drawing.Point(157, 44)
        Me.lblLastEditTimeInfo.Name = "lblLastEditTimeInfo"
        Me.lblLastEditTimeInfo.Size = New System.Drawing.Size(205, 13)
        Me.lblLastEditTimeInfo.TabIndex = 80
        Me.lblLastEditTimeInfo.Text = "=>                                              "
        '
        'btnGetLastEditTimeForCurrentProgramNumber
        '
        Me.btnGetLastEditTimeForCurrentProgramNumber.BackColor = System.Drawing.Color.RoyalBlue
        Me.btnGetLastEditTimeForCurrentProgramNumber.ForeColor = System.Drawing.Color.White
        Me.btnGetLastEditTimeForCurrentProgramNumber.Location = New System.Drawing.Point(25, 37)
        Me.btnGetLastEditTimeForCurrentProgramNumber.Name = "btnGetLastEditTimeForCurrentProgramNumber"
        Me.btnGetLastEditTimeForCurrentProgramNumber.Size = New System.Drawing.Size(100, 32)
        Me.btnGetLastEditTimeForCurrentProgramNumber.TabIndex = 79
        Me.btnGetLastEditTimeForCurrentProgramNumber.Text = "Read"
        Me.btnGetLastEditTimeForCurrentProgramNumber.UseVisualStyleBackColor = False
        '
        'GroupBox7
        '
        Me.GroupBox7.Controls.Add(Me.btnReadModalMCode)
        Me.GroupBox7.Controls.Add(Me.btnReadModalFeedrate)
        Me.GroupBox7.Controls.Add(Me.btnReadModalSpindle)
        Me.GroupBox7.Controls.Add(Me.btnReadModalTool)
        Me.GroupBox7.Controls.Add(Me.lblReadModalInfo)
        Me.GroupBox7.Controls.Add(Me.btnReadModalData)
        Me.GroupBox7.Controls.Add(Me.txtModalBlock)
        Me.GroupBox7.Controls.Add(Me.Label10)
        Me.GroupBox7.Controls.Add(Me.txtModalType)
        Me.GroupBox7.Controls.Add(Me.Label9)
        Me.GroupBox7.Location = New System.Drawing.Point(456, 391)
        Me.GroupBox7.Name = "GroupBox7"
        Me.GroupBox7.Size = New System.Drawing.Size(537, 180)
        Me.GroupBox7.TabIndex = 79
        Me.GroupBox7.TabStop = False
        Me.GroupBox7.Text = "Read ""Modal Data"" | CNC_MODAL"
        '
        'btnReadModalMCode
        '
        Me.btnReadModalMCode.BackColor = System.Drawing.Color.RoyalBlue
        Me.btnReadModalMCode.ForeColor = System.Drawing.Color.White
        Me.btnReadModalMCode.Location = New System.Drawing.Point(154, 132)
        Me.btnReadModalMCode.Name = "btnReadModalMCode"
        Me.btnReadModalMCode.Size = New System.Drawing.Size(31, 32)
        Me.btnReadModalMCode.TabIndex = 85
        Me.btnReadModalMCode.Text = "M"
        Me.btnReadModalMCode.UseVisualStyleBackColor = False
        '
        'btnReadModalFeedrate
        '
        Me.btnReadModalFeedrate.BackColor = System.Drawing.Color.RoyalBlue
        Me.btnReadModalFeedrate.ForeColor = System.Drawing.Color.White
        Me.btnReadModalFeedrate.Location = New System.Drawing.Point(111, 132)
        Me.btnReadModalFeedrate.Name = "btnReadModalFeedrate"
        Me.btnReadModalFeedrate.Size = New System.Drawing.Size(31, 32)
        Me.btnReadModalFeedrate.TabIndex = 84
        Me.btnReadModalFeedrate.Text = "F"
        Me.btnReadModalFeedrate.UseVisualStyleBackColor = False
        '
        'btnReadModalSpindle
        '
        Me.btnReadModalSpindle.BackColor = System.Drawing.Color.RoyalBlue
        Me.btnReadModalSpindle.ForeColor = System.Drawing.Color.White
        Me.btnReadModalSpindle.Location = New System.Drawing.Point(68, 132)
        Me.btnReadModalSpindle.Name = "btnReadModalSpindle"
        Me.btnReadModalSpindle.Size = New System.Drawing.Size(31, 32)
        Me.btnReadModalSpindle.TabIndex = 83
        Me.btnReadModalSpindle.Text = "S"
        Me.btnReadModalSpindle.UseVisualStyleBackColor = False
        '
        'btnReadModalTool
        '
        Me.btnReadModalTool.BackColor = System.Drawing.Color.RoyalBlue
        Me.btnReadModalTool.ForeColor = System.Drawing.Color.White
        Me.btnReadModalTool.Location = New System.Drawing.Point(25, 132)
        Me.btnReadModalTool.Name = "btnReadModalTool"
        Me.btnReadModalTool.Size = New System.Drawing.Size(31, 32)
        Me.btnReadModalTool.TabIndex = 82
        Me.btnReadModalTool.Text = "T"
        Me.btnReadModalTool.UseVisualStyleBackColor = False
        '
        'lblReadModalInfo
        '
        Me.lblReadModalInfo.Anchor = CType((((System.Windows.Forms.AnchorStyles.Top Or System.Windows.Forms.AnchorStyles.Bottom) _
            Or System.Windows.Forms.AnchorStyles.Left) _
            Or System.Windows.Forms.AnchorStyles.Right), System.Windows.Forms.AnchorStyles)
        Me.lblReadModalInfo.AutoSize = True
        Me.lblReadModalInfo.Font = New System.Drawing.Font("Microsoft Sans Serif", 8.25!, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, CType(0, Byte))
        Me.lblReadModalInfo.Location = New System.Drawing.Point(243, 28)
        Me.lblReadModalInfo.Name = "lblReadModalInfo"
        Me.lblReadModalInfo.Size = New System.Drawing.Size(205, 13)
        Me.lblReadModalInfo.TabIndex = 81
        Me.lblReadModalInfo.Text = "=>                                              "
        '
        'btnReadModalData
        '
        Me.btnReadModalData.BackColor = System.Drawing.Color.RoyalBlue
        Me.btnReadModalData.ForeColor = System.Drawing.Color.White
        Me.btnReadModalData.Location = New System.Drawing.Point(25, 91)
        Me.btnReadModalData.Name = "btnReadModalData"
        Me.btnReadModalData.Size = New System.Drawing.Size(161, 32)
        Me.btnReadModalData.TabIndex = 80
        Me.btnReadModalData.Text = "Read"
        Me.btnReadModalData.UseVisualStyleBackColor = False
        '
        'txtModalBlock
        '
        Me.txtModalBlock.Location = New System.Drawing.Point(63, 56)
        Me.txtModalBlock.Name = "txtModalBlock"
        Me.txtModalBlock.Size = New System.Drawing.Size(123, 20)
        Me.txtModalBlock.TabIndex = 78
        '
        'Label10
        '
        Me.Label10.AutoSize = True
        Me.Label10.Location = New System.Drawing.Point(22, 60)
        Me.Label10.Name = "Label10"
        Me.Label10.Size = New System.Drawing.Size(34, 13)
        Me.Label10.TabIndex = 77
        Me.Label10.Text = "Block"
        '
        'txtModalType
        '
        Me.txtModalType.Location = New System.Drawing.Point(63, 28)
        Me.txtModalType.Name = "txtModalType"
        Me.txtModalType.Size = New System.Drawing.Size(123, 20)
        Me.txtModalType.TabIndex = 76
        '
        'Label9
        '
        Me.Label9.AutoSize = True
        Me.Label9.Location = New System.Drawing.Point(22, 32)
        Me.Label9.Name = "Label9"
        Me.Label9.Size = New System.Drawing.Size(31, 13)
        Me.Label9.TabIndex = 75
        Me.Label9.Text = "Type"
        '
        'GroupBox6
        '
        Me.GroupBox6.Controls.Add(Me.txtCurrProgramFileInfo)
        Me.GroupBox6.Controls.Add(Me.btnReadCurrProgInfo)
        Me.GroupBox6.Location = New System.Drawing.Point(22, 473)
        Me.GroupBox6.Name = "GroupBox6"
        Me.GroupBox6.Size = New System.Drawing.Size(390, 100)
        Me.GroupBox6.TabIndex = 78
        Me.GroupBox6.TabStop = False
        Me.GroupBox6.Text = "Read current program file information : CNC_PDF_RDMAIN | PROGRAM"
        '
        'txtCurrProgramFileInfo
        '
        Me.txtCurrProgramFileInfo.Location = New System.Drawing.Point(124, 52)
        Me.txtCurrProgramFileInfo.Name = "txtCurrProgramFileInfo"
        Me.txtCurrProgramFileInfo.ReadOnly = True
        Me.txtCurrProgramFileInfo.Size = New System.Drawing.Size(258, 20)
        Me.txtCurrProgramFileInfo.TabIndex = 80
        '
        'btnReadCurrProgInfo
        '
        Me.btnReadCurrProgInfo.BackColor = System.Drawing.Color.RoyalBlue
        Me.btnReadCurrProgInfo.ForeColor = System.Drawing.Color.White
        Me.btnReadCurrProgInfo.Location = New System.Drawing.Point(17, 45)
        Me.btnReadCurrProgInfo.Name = "btnReadCurrProgInfo"
        Me.btnReadCurrProgInfo.Size = New System.Drawing.Size(100, 32)
        Me.btnReadCurrProgInfo.TabIndex = 79
        Me.btnReadCurrProgInfo.Text = "Read"
        Me.btnReadCurrProgInfo.UseVisualStyleBackColor = False
        '
        'GroupBox5
        '
        Me.GroupBox5.Controls.Add(Me.txtSubFolderReqNum)
        Me.GroupBox5.Controls.Add(Me.Label8)
        Me.GroupBox5.Controls.Add(Me.lblFilePropertiesInfo)
        Me.GroupBox5.Controls.Add(Me.btnReadFileProperty)
        Me.GroupBox5.Controls.Add(Me.btnGetFolderPath1)
        Me.GroupBox5.Controls.Add(Me.txtFolderPath1)
        Me.GroupBox5.Controls.Add(Me.Label7)
        Me.GroupBox5.Controls.Add(Me.txtMaxNumOfProg)
        Me.GroupBox5.Controls.Add(Me.Label6)
        Me.GroupBox5.Location = New System.Drawing.Point(456, 25)
        Me.GroupBox5.Name = "GroupBox5"
        Me.GroupBox5.Size = New System.Drawing.Size(537, 222)
        Me.GroupBox5.TabIndex = 77
        Me.GroupBox5.TabStop = False
        Me.GroupBox5.Text = "Read file properties (last edit date) : CNC_RDPDF_ALLDIR | PROGRAM"
        '
        'txtSubFolderReqNum
        '
        Me.txtSubFolderReqNum.Location = New System.Drawing.Point(193, 95)
        Me.txtSubFolderReqNum.Name = "txtSubFolderReqNum"
        Me.txtSubFolderReqNum.Size = New System.Drawing.Size(123, 20)
        Me.txtSubFolderReqNum.TabIndex = 81
        '
        'Label8
        '
        Me.Label8.AutoSize = True
        Me.Label8.Location = New System.Drawing.Point(22, 99)
        Me.Label8.Name = "Label8"
        Me.Label8.Size = New System.Drawing.Size(52, 13)
        Me.Label8.TabIndex = 80
        Me.Label8.Text = "Req Num"
        '
        'lblFilePropertiesInfo
        '
        Me.lblFilePropertiesInfo.Anchor = CType((((System.Windows.Forms.AnchorStyles.Top Or System.Windows.Forms.AnchorStyles.Bottom) _
            Or System.Windows.Forms.AnchorStyles.Left) _
            Or System.Windows.Forms.AnchorStyles.Right), System.Windows.Forms.AnchorStyles)
        Me.lblFilePropertiesInfo.AutoSize = True
        Me.lblFilePropertiesInfo.Font = New System.Drawing.Font("Microsoft Sans Serif", 8.25!, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, CType(0, Byte))
        Me.lblFilePropertiesInfo.Location = New System.Drawing.Point(157, 138)
        Me.lblFilePropertiesInfo.Name = "lblFilePropertiesInfo"
        Me.lblFilePropertiesInfo.Size = New System.Drawing.Size(205, 13)
        Me.lblFilePropertiesInfo.TabIndex = 79
        Me.lblFilePropertiesInfo.Text = "=>                                              "
        '
        'btnReadFileProperty
        '
        Me.btnReadFileProperty.BackColor = System.Drawing.Color.RoyalBlue
        Me.btnReadFileProperty.ForeColor = System.Drawing.Color.White
        Me.btnReadFileProperty.Location = New System.Drawing.Point(25, 128)
        Me.btnReadFileProperty.Name = "btnReadFileProperty"
        Me.btnReadFileProperty.Size = New System.Drawing.Size(100, 32)
        Me.btnReadFileProperty.TabIndex = 78
        Me.btnReadFileProperty.Text = "Read"
        Me.btnReadFileProperty.UseVisualStyleBackColor = False
        '
        'btnGetFolderPath1
        '
        Me.btnGetFolderPath1.Location = New System.Drawing.Point(485, 62)
        Me.btnGetFolderPath1.Name = "btnGetFolderPath1"
        Me.btnGetFolderPath1.Size = New System.Drawing.Size(32, 23)
        Me.btnGetFolderPath1.TabIndex = 77
        Me.btnGetFolderPath1.Text = "..."
        Me.btnGetFolderPath1.UseVisualStyleBackColor = True
        '
        'txtFolderPath1
        '
        Me.txtFolderPath1.Location = New System.Drawing.Point(90, 63)
        Me.txtFolderPath1.Name = "txtFolderPath1"
        Me.txtFolderPath1.Size = New System.Drawing.Size(389, 20)
        Me.txtFolderPath1.TabIndex = 76
        '
        'Label7
        '
        Me.Label7.AutoSize = True
        Me.Label7.Location = New System.Drawing.Point(22, 67)
        Me.Label7.Name = "Label7"
        Me.Label7.Size = New System.Drawing.Size(61, 13)
        Me.Label7.TabIndex = 75
        Me.Label7.Text = "Folder Path"
        '
        'txtMaxNumOfProg
        '
        Me.txtMaxNumOfProg.Location = New System.Drawing.Point(193, 30)
        Me.txtMaxNumOfProg.Name = "txtMaxNumOfProg"
        Me.txtMaxNumOfProg.Size = New System.Drawing.Size(123, 20)
        Me.txtMaxNumOfProg.TabIndex = 74
        '
        'Label6
        '
        Me.Label6.AutoSize = True
        Me.Label6.Location = New System.Drawing.Point(22, 34)
        Me.Label6.Name = "Label6"
        Me.Label6.Size = New System.Drawing.Size(145, 13)
        Me.Label6.TabIndex = 73
        Me.Label6.Text = "Maximum Number of Program"
        '
        'GroupBox4
        '
        Me.GroupBox4.Controls.Add(Me.lblCurrProgNameInfo)
        Me.GroupBox4.Controls.Add(Me.btnReadCurrentProgName)
        Me.GroupBox4.Location = New System.Drawing.Point(22, 373)
        Me.GroupBox4.Name = "GroupBox4"
        Me.GroupBox4.Size = New System.Drawing.Size(390, 82)
        Me.GroupBox4.TabIndex = 76
        Me.GroupBox4.TabStop = False
        Me.GroupBox4.Text = "Read Current Program Name CNC_EXEPRGNAME | PROGRAM"
        '
        'lblCurrProgNameInfo
        '
        Me.lblCurrProgNameInfo.Anchor = CType((((System.Windows.Forms.AnchorStyles.Top Or System.Windows.Forms.AnchorStyles.Bottom) _
            Or System.Windows.Forms.AnchorStyles.Left) _
            Or System.Windows.Forms.AnchorStyles.Right), System.Windows.Forms.AnchorStyles)
        Me.lblCurrProgNameInfo.AutoSize = True
        Me.lblCurrProgNameInfo.Font = New System.Drawing.Font("Microsoft Sans Serif", 8.25!, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, CType(0, Byte))
        Me.lblCurrProgNameInfo.Location = New System.Drawing.Point(123, 38)
        Me.lblCurrProgNameInfo.Name = "lblCurrProgNameInfo"
        Me.lblCurrProgNameInfo.Size = New System.Drawing.Size(205, 13)
        Me.lblCurrProgNameInfo.TabIndex = 77
        Me.lblCurrProgNameInfo.Text = "=>                                              "
        '
        'btnReadCurrentProgName
        '
        Me.btnReadCurrentProgName.BackColor = System.Drawing.Color.RoyalBlue
        Me.btnReadCurrentProgName.ForeColor = System.Drawing.Color.White
        Me.btnReadCurrentProgName.Location = New System.Drawing.Point(17, 31)
        Me.btnReadCurrentProgName.Name = "btnReadCurrentProgName"
        Me.btnReadCurrentProgName.Size = New System.Drawing.Size(100, 32)
        Me.btnReadCurrentProgName.TabIndex = 76
        Me.btnReadCurrentProgName.Text = "Read"
        Me.btnReadCurrentProgName.UseVisualStyleBackColor = False
        '
        'GroupBox3
        '
        Me.GroupBox3.Controls.Add(Me.lblCurrSequenceNumInfo)
        Me.GroupBox3.Controls.Add(Me.btnReadSequenceNum)
        Me.GroupBox3.Location = New System.Drawing.Point(22, 269)
        Me.GroupBox3.Name = "GroupBox3"
        Me.GroupBox3.Size = New System.Drawing.Size(390, 86)
        Me.GroupBox3.TabIndex = 75
        Me.GroupBox3.TabStop = False
        Me.GroupBox3.Text = "Read Current Sequence Num CNC_RDSEQNUM | PROGRAM"
        '
        'lblCurrSequenceNumInfo
        '
        Me.lblCurrSequenceNumInfo.Anchor = CType((((System.Windows.Forms.AnchorStyles.Top Or System.Windows.Forms.AnchorStyles.Bottom) _
            Or System.Windows.Forms.AnchorStyles.Left) _
            Or System.Windows.Forms.AnchorStyles.Right), System.Windows.Forms.AnchorStyles)
        Me.lblCurrSequenceNumInfo.AutoSize = True
        Me.lblCurrSequenceNumInfo.Font = New System.Drawing.Font("Microsoft Sans Serif", 8.25!, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, CType(0, Byte))
        Me.lblCurrSequenceNumInfo.Location = New System.Drawing.Point(122, 42)
        Me.lblCurrSequenceNumInfo.Name = "lblCurrSequenceNumInfo"
        Me.lblCurrSequenceNumInfo.Size = New System.Drawing.Size(205, 13)
        Me.lblCurrSequenceNumInfo.TabIndex = 75
        Me.lblCurrSequenceNumInfo.Text = "=>                                              "
        '
        'btnReadSequenceNum
        '
        Me.btnReadSequenceNum.BackColor = System.Drawing.Color.RoyalBlue
        Me.btnReadSequenceNum.ForeColor = System.Drawing.Color.White
        Me.btnReadSequenceNum.Location = New System.Drawing.Point(17, 32)
        Me.btnReadSequenceNum.Name = "btnReadSequenceNum"
        Me.btnReadSequenceNum.Size = New System.Drawing.Size(100, 32)
        Me.btnReadSequenceNum.TabIndex = 72
        Me.btnReadSequenceNum.Text = "Read"
        Me.btnReadSequenceNum.UseVisualStyleBackColor = False
        '
        'GroupBox2
        '
        Me.GroupBox2.Controls.Add(Me.lblCopyProgInfo)
        Me.GroupBox2.Controls.Add(Me.txtDestNCProgNum)
        Me.GroupBox2.Controls.Add(Me.txtSrcNCProgNum)
        Me.GroupBox2.Controls.Add(Me.btnCopyProg)
        Me.GroupBox2.Controls.Add(Me.Label5)
        Me.GroupBox2.Controls.Add(Me.Label3)
        Me.GroupBox2.Location = New System.Drawing.Point(22, 133)
        Me.GroupBox2.Name = "GroupBox2"
        Me.GroupBox2.Size = New System.Drawing.Size(390, 118)
        Me.GroupBox2.TabIndex = 74
        Me.GroupBox2.TabStop = False
        Me.GroupBox2.Text = "Copy NC Program : CNC_COPYPROG | PROGRAM"
        '
        'lblCopyProgInfo
        '
        Me.lblCopyProgInfo.Anchor = CType((((System.Windows.Forms.AnchorStyles.Top Or System.Windows.Forms.AnchorStyles.Bottom) _
            Or System.Windows.Forms.AnchorStyles.Left) _
            Or System.Windows.Forms.AnchorStyles.Right), System.Windows.Forms.AnchorStyles)
        Me.lblCopyProgInfo.AutoSize = True
        Me.lblCopyProgInfo.Font = New System.Drawing.Font("Microsoft Sans Serif", 8.25!, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, CType(0, Byte))
        Me.lblCopyProgInfo.Location = New System.Drawing.Point(122, 82)
        Me.lblCopyProgInfo.Name = "lblCopyProgInfo"
        Me.lblCopyProgInfo.Size = New System.Drawing.Size(205, 13)
        Me.lblCopyProgInfo.TabIndex = 74
        Me.lblCopyProgInfo.Text = "=>                                              "
        '
        'txtDestNCProgNum
        '
        Me.txtDestNCProgNum.Location = New System.Drawing.Point(185, 46)
        Me.txtDestNCProgNum.Name = "txtDestNCProgNum"
        Me.txtDestNCProgNum.Size = New System.Drawing.Size(123, 20)
        Me.txtDestNCProgNum.TabIndex = 73
        '
        'txtSrcNCProgNum
        '
        Me.txtSrcNCProgNum.Location = New System.Drawing.Point(185, 21)
        Me.txtSrcNCProgNum.Name = "txtSrcNCProgNum"
        Me.txtSrcNCProgNum.Size = New System.Drawing.Size(123, 20)
        Me.txtSrcNCProgNum.TabIndex = 72
        '
        'btnCopyProg
        '
        Me.btnCopyProg.BackColor = System.Drawing.Color.RoyalBlue
        Me.btnCopyProg.ForeColor = System.Drawing.Color.White
        Me.btnCopyProg.Location = New System.Drawing.Point(17, 72)
        Me.btnCopyProg.Name = "btnCopyProg"
        Me.btnCopyProg.Size = New System.Drawing.Size(100, 32)
        Me.btnCopyProg.TabIndex = 71
        Me.btnCopyProg.Text = "Copy Program"
        Me.btnCopyProg.UseVisualStyleBackColor = False
        '
        'Label5
        '
        Me.Label5.AutoSize = True
        Me.Label5.Location = New System.Drawing.Point(14, 50)
        Me.Label5.Name = "Label5"
        Me.Label5.Size = New System.Drawing.Size(160, 13)
        Me.Label5.TabIndex = 1
        Me.Label5.Text = "Destination NC Program Number"
        '
        'Label3
        '
        Me.Label3.AutoSize = True
        Me.Label3.Location = New System.Drawing.Point(14, 25)
        Me.Label3.Name = "Label3"
        Me.Label3.Size = New System.Drawing.Size(141, 13)
        Me.Label3.TabIndex = 0
        Me.Label3.Text = "Source NC Program Number"
        '
        'GroupBox1
        '
        Me.GroupBox1.Controls.Add(Me.btnRunSpeedCheck)
        Me.GroupBox1.Controls.Add(Me.lblSpeedCheckInfo)
        Me.GroupBox1.Location = New System.Drawing.Point(22, 25)
        Me.GroupBox1.Name = "GroupBox1"
        Me.GroupBox1.Size = New System.Drawing.Size(390, 90)
        Me.GroupBox1.TabIndex = 73
        Me.GroupBox1.TabStop = False
        Me.GroupBox1.Text = "Check Macro Write Speed"
        '
        'btnRunSpeedCheck
        '
        Me.btnRunSpeedCheck.BackColor = System.Drawing.Color.RoyalBlue
        Me.btnRunSpeedCheck.ForeColor = System.Drawing.Color.White
        Me.btnRunSpeedCheck.Location = New System.Drawing.Point(17, 21)
        Me.btnRunSpeedCheck.Name = "btnRunSpeedCheck"
        Me.btnRunSpeedCheck.Size = New System.Drawing.Size(100, 32)
        Me.btnRunSpeedCheck.TabIndex = 70
        Me.btnRunSpeedCheck.Text = "Check Speed"
        Me.btnRunSpeedCheck.UseVisualStyleBackColor = False
        '
        'lblSpeedCheckInfo
        '
        Me.lblSpeedCheckInfo.Anchor = CType((((System.Windows.Forms.AnchorStyles.Top Or System.Windows.Forms.AnchorStyles.Bottom) _
            Or System.Windows.Forms.AnchorStyles.Left) _
            Or System.Windows.Forms.AnchorStyles.Right), System.Windows.Forms.AnchorStyles)
        Me.lblSpeedCheckInfo.AutoSize = True
        Me.lblSpeedCheckInfo.Font = New System.Drawing.Font("Microsoft Sans Serif", 8.25!, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, CType(0, Byte))
        Me.lblSpeedCheckInfo.Location = New System.Drawing.Point(123, 25)
        Me.lblSpeedCheckInfo.Name = "lblSpeedCheckInfo"
        Me.lblSpeedCheckInfo.Size = New System.Drawing.Size(205, 13)
        Me.lblSpeedCheckInfo.TabIndex = 71
        Me.lblSpeedCheckInfo.Text = "=>                                              "
        '
        'tabPIDControl
        '
        Me.tabPIDControl.Controls.Add(Me.lblPIDTestValBinary)
        Me.tabPIDControl.Controls.Add(Me.btnWriteTestPIDVal)
        Me.tabPIDControl.Controls.Add(Me.txtPIDTestVal)
        Me.tabPIDControl.Controls.Add(Me.Label30)
        Me.tabPIDControl.Controls.Add(Me.GroupBox10)
        Me.tabPIDControl.Controls.Add(Me.GroupBox9)
        Me.tabPIDControl.Controls.Add(Me.lblStatusPIDOutToCNC)
        Me.tabPIDControl.Controls.Add(Me.tbOMax)
        Me.tabPIDControl.Controls.Add(Me.Label23)
        Me.tabPIDControl.Controls.Add(Me.tbOMin)
        Me.tabPIDControl.Controls.Add(Me.Label24)
        Me.tabPIDControl.Controls.Add(Me.lblOutput)
        Me.tabPIDControl.Controls.Add(Me.Label22)
        Me.tabPIDControl.Controls.Add(Me.progBarOut)
        Me.tabPIDControl.Controls.Add(Me.Label19)
        Me.tabPIDControl.Controls.Add(Me.Label20)
        Me.tabPIDControl.Controls.Add(Me.Label21)
        Me.tabPIDControl.Controls.Add(Me.pbPIDChart)
        Me.tabPIDControl.Controls.Add(Me.btnStart)
        Me.tabPIDControl.Controls.Add(Me.lblInterval)
        Me.tabPIDControl.Controls.Add(Me.Label18)
        Me.tabPIDControl.Controls.Add(Me.trackBarInterval)
        Me.tabPIDControl.Controls.Add(Me.lblSP)
        Me.tabPIDControl.Controls.Add(Me.Label13)
        Me.tabPIDControl.Controls.Add(Me.trackBarSP)
        Me.tabPIDControl.Controls.Add(Me.progBarPV)
        Me.tabPIDControl.Controls.Add(Me.lblPV)
        Me.tabPIDControl.Controls.Add(Me.Label12)
        Me.tabPIDControl.Controls.Add(Me.tbPVMax)
        Me.tabPIDControl.Controls.Add(Me.Label11)
        Me.tabPIDControl.Controls.Add(Me.tbPVMin)
        Me.tabPIDControl.Controls.Add(Me.Label17)
        Me.tabPIDControl.Controls.Add(Me.tbKd)
        Me.tabPIDControl.Controls.Add(Me.Label14)
        Me.tabPIDControl.Controls.Add(Me.tbKi)
        Me.tabPIDControl.Controls.Add(Me.Label15)
        Me.tabPIDControl.Controls.Add(Me.tbKp)
        Me.tabPIDControl.Controls.Add(Me.Label16)
        Me.tabPIDControl.Controls.Add(Me.PictureBox1)
        Me.tabPIDControl.Location = New System.Drawing.Point(4, 22)
        Me.tabPIDControl.Name = "tabPIDControl"
        Me.tabPIDControl.Padding = New System.Windows.Forms.Padding(3)
        Me.tabPIDControl.Size = New System.Drawing.Size(1023, 585)
        Me.tabPIDControl.TabIndex = 3
        Me.tabPIDControl.Text = "PID Control"
        Me.tabPIDControl.UseVisualStyleBackColor = True
        '
        'lblPIDTestValBinary
        '
        Me.lblPIDTestValBinary.AutoSize = True
        Me.lblPIDTestValBinary.Location = New System.Drawing.Point(514, 35)
        Me.lblPIDTestValBinary.Name = "lblPIDTestValBinary"
        Me.lblPIDTestValBinary.Size = New System.Drawing.Size(13, 13)
        Me.lblPIDTestValBinary.TabIndex = 98
        Me.lblPIDTestValBinary.Text = "0"
        '
        'btnWriteTestPIDVal
        '
        Me.btnWriteTestPIDVal.Location = New System.Drawing.Point(401, 30)
        Me.btnWriteTestPIDVal.Name = "btnWriteTestPIDVal"
        Me.btnWriteTestPIDVal.Size = New System.Drawing.Size(88, 24)
        Me.btnWriteTestPIDVal.TabIndex = 97
        Me.btnWriteTestPIDVal.Text = "Write"
        Me.btnWriteTestPIDVal.UseVisualStyleBackColor = True
        '
        'txtPIDTestVal
        '
        Me.txtPIDTestVal.Location = New System.Drawing.Point(285, 32)
        Me.txtPIDTestVal.Name = "txtPIDTestVal"
        Me.txtPIDTestVal.Size = New System.Drawing.Size(100, 20)
        Me.txtPIDTestVal.TabIndex = 96
        '
        'Label30
        '
        Me.Label30.AutoSize = True
        Me.Label30.Location = New System.Drawing.Point(221, 35)
        Me.Label30.Name = "Label30"
        Me.Label30.Size = New System.Drawing.Size(58, 13)
        Me.Label30.TabIndex = 95
        Me.Label30.Text = "Test Value"
        '
        'GroupBox10
        '
        Me.GroupBox10.Controls.Add(Me.tbAdaptiveEnableAddr)
        Me.GroupBox10.Controls.Add(Me.Label28)
        Me.GroupBox10.Controls.Add(Me.cbBoxTypePMC)
        Me.GroupBox10.Controls.Add(Me.Label27)
        Me.GroupBox10.Font = New System.Drawing.Font("Microsoft Sans Serif", 8.25!, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, CType(0, Byte))
        Me.GroupBox10.Location = New System.Drawing.Point(152, 184)
        Me.GroupBox10.Name = "GroupBox10"
        Me.GroupBox10.Size = New System.Drawing.Size(285, 85)
        Me.GroupBox10.TabIndex = 94
        Me.GroupBox10.TabStop = False
        Me.GroupBox10.Text = "Adaptive Enable"
        '
        'tbAdaptiveEnableAddr
        '
        Me.tbAdaptiveEnableAddr.Location = New System.Drawing.Point(86, 54)
        Me.tbAdaptiveEnableAddr.Name = "tbAdaptiveEnableAddr"
        Me.tbAdaptiveEnableAddr.Size = New System.Drawing.Size(181, 20)
        Me.tbAdaptiveEnableAddr.TabIndex = 4
        '
        'Label28
        '
        Me.Label28.AutoSize = True
        Me.Label28.Font = New System.Drawing.Font("Microsoft Sans Serif", 8.25!, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, CType(0, Byte))
        Me.Label28.Location = New System.Drawing.Point(12, 57)
        Me.Label28.Name = "Label28"
        Me.Label28.Size = New System.Drawing.Size(45, 13)
        Me.Label28.TabIndex = 3
        Me.Label28.Text = "Address"
        '
        'cbBoxTypePMC
        '
        Me.cbBoxTypePMC.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList
        Me.cbBoxTypePMC.FormattingEnabled = True
        Me.cbBoxTypePMC.Items.AddRange(New Object() {"G (Output signal from PMC to CNC)", "F (Input signal to PMC from CNC)", "Y (Output signal from PMC to machine)", "X (Input signal to PMC from machine)", "A (Message display)", "R (Internal/System relay)", "T (Timer)", "K (Keep relay)", "C (Counter)", "D (Data table)", "M (Input signal from other PMC path)", "N (Output signal to other PMC path)", "E (Extra relay)", "Z (System relay)"})
        Me.cbBoxTypePMC.Location = New System.Drawing.Point(86, 23)
        Me.cbBoxTypePMC.Name = "cbBoxTypePMC"
        Me.cbBoxTypePMC.Size = New System.Drawing.Size(181, 21)
        Me.cbBoxTypePMC.TabIndex = 2
        '
        'Label27
        '
        Me.Label27.AutoSize = True
        Me.Label27.Font = New System.Drawing.Font("Microsoft Sans Serif", 8.25!, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, CType(0, Byte))
        Me.Label27.Location = New System.Drawing.Point(12, 27)
        Me.Label27.Name = "Label27"
        Me.Label27.Size = New System.Drawing.Size(68, 13)
        Me.Label27.TabIndex = 1
        Me.Label27.Text = "Kind Of PMC"
        '
        'GroupBox9
        '
        Me.GroupBox9.Controls.Add(Me.tbPLCPort)
        Me.GroupBox9.Controls.Add(Me.Label26)
        Me.GroupBox9.Controls.Add(Me.tbPLCIP)
        Me.GroupBox9.Controls.Add(Me.Label25)
        Me.GroupBox9.Font = New System.Drawing.Font("Microsoft Sans Serif", 8.25!, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, CType(0, Byte))
        Me.GroupBox9.Location = New System.Drawing.Point(152, 85)
        Me.GroupBox9.Name = "GroupBox9"
        Me.GroupBox9.Size = New System.Drawing.Size(285, 84)
        Me.GroupBox9.TabIndex = 93
        Me.GroupBox9.TabStop = False
        Me.GroupBox9.Text = "PLC Settings"
        '
        'tbPLCPort
        '
        Me.tbPLCPort.Location = New System.Drawing.Point(87, 52)
        Me.tbPLCPort.Name = "tbPLCPort"
        Me.tbPLCPort.Size = New System.Drawing.Size(180, 20)
        Me.tbPLCPort.TabIndex = 3
        '
        'Label26
        '
        Me.Label26.AutoSize = True
        Me.Label26.Font = New System.Drawing.Font("Microsoft Sans Serif", 8.25!, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, CType(0, Byte))
        Me.Label26.Location = New System.Drawing.Point(18, 56)
        Me.Label26.Name = "Label26"
        Me.Label26.Size = New System.Drawing.Size(26, 13)
        Me.Label26.TabIndex = 2
        Me.Label26.Text = "Port"
        '
        'tbPLCIP
        '
        Me.tbPLCIP.Location = New System.Drawing.Point(87, 22)
        Me.tbPLCIP.Name = "tbPLCIP"
        Me.tbPLCIP.Size = New System.Drawing.Size(180, 20)
        Me.tbPLCIP.TabIndex = 1
        '
        'Label25
        '
        Me.Label25.AutoSize = True
        Me.Label25.Font = New System.Drawing.Font("Microsoft Sans Serif", 8.25!, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, CType(0, Byte))
        Me.Label25.Location = New System.Drawing.Point(18, 26)
        Me.Label25.Name = "Label25"
        Me.Label25.Size = New System.Drawing.Size(17, 13)
        Me.Label25.TabIndex = 0
        Me.Label25.Text = "IP"
        '
        'lblStatusPIDOutToCNC
        '
        Me.lblStatusPIDOutToCNC.AutoSize = True
        Me.lblStatusPIDOutToCNC.Location = New System.Drawing.Point(674, 35)
        Me.lblStatusPIDOutToCNC.Name = "lblStatusPIDOutToCNC"
        Me.lblStatusPIDOutToCNC.Size = New System.Drawing.Size(16, 13)
        Me.lblStatusPIDOutToCNC.TabIndex = 92
        Me.lblStatusPIDOutToCNC.Text = "---"
        '
        'tbOMax
        '
        Me.tbOMax.Location = New System.Drawing.Point(58, 355)
        Me.tbOMax.Name = "tbOMax"
        Me.tbOMax.ReadOnly = True
        Me.tbOMax.Size = New System.Drawing.Size(70, 20)
        Me.tbOMax.TabIndex = 91
        Me.tbOMax.Text = "250"
        Me.tbOMax.TextAlign = System.Windows.Forms.HorizontalAlignment.Center
        '
        'Label23
        '
        Me.Label23.AutoSize = True
        Me.Label23.Location = New System.Drawing.Point(12, 359)
        Me.Label23.Name = "Label23"
        Me.Label23.Size = New System.Drawing.Size(47, 13)
        Me.Label23.TabIndex = 90
        Me.Label23.Text = "Out.Max"
        '
        'tbOMin
        '
        Me.tbOMin.Location = New System.Drawing.Point(58, 328)
        Me.tbOMin.Name = "tbOMin"
        Me.tbOMin.ReadOnly = True
        Me.tbOMin.Size = New System.Drawing.Size(70, 20)
        Me.tbOMin.TabIndex = 89
        Me.tbOMin.Text = "0"
        Me.tbOMin.TextAlign = System.Windows.Forms.HorizontalAlignment.Center
        '
        'Label24
        '
        Me.Label24.AutoSize = True
        Me.Label24.Location = New System.Drawing.Point(12, 332)
        Me.Label24.Name = "Label24"
        Me.Label24.Size = New System.Drawing.Size(44, 13)
        Me.Label24.TabIndex = 88
        Me.Label24.Text = "Out.Min"
        '
        'lblOutput
        '
        Me.lblOutput.AutoSize = True
        Me.lblOutput.Location = New System.Drawing.Point(226, 416)
        Me.lblOutput.Name = "lblOutput"
        Me.lblOutput.Size = New System.Drawing.Size(16, 13)
        Me.lblOutput.TabIndex = 87
        Me.lblOutput.Text = "---"
        '
        'Label22
        '
        Me.Label22.AutoSize = True
        Me.Label22.Location = New System.Drawing.Point(156, 416)
        Me.Label22.Name = "Label22"
        Me.Label22.Size = New System.Drawing.Size(62, 13)
        Me.Label22.TabIndex = 86
        Me.Label22.Text = "MV (output)"
        '
        'progBarOut
        '
        Me.progBarOut.Location = New System.Drawing.Point(152, 435)
        Me.progBarOut.Maximum = 300
        Me.progBarOut.Name = "progBarOut"
        Me.progBarOut.Size = New System.Drawing.Size(285, 19)
        Me.progBarOut.Style = System.Windows.Forms.ProgressBarStyle.Continuous
        Me.progBarOut.TabIndex = 85
        '
        'Label19
        '
        Me.Label19.Anchor = CType((System.Windows.Forms.AnchorStyles.Top Or System.Windows.Forms.AnchorStyles.Right), System.Windows.Forms.AnchorStyles)
        Me.Label19.AutoSize = True
        Me.Label19.ForeColor = System.Drawing.Color.Red
        Me.Label19.Location = New System.Drawing.Point(945, 72)
        Me.Label19.Name = "Label19"
        Me.Label19.Size = New System.Drawing.Size(64, 13)
        Me.Label19.TabIndex = 84
        Me.Label19.Text = "MV (Output)"
        '
        'Label20
        '
        Me.Label20.Anchor = CType((System.Windows.Forms.AnchorStyles.Top Or System.Windows.Forms.AnchorStyles.Right), System.Windows.Forms.AnchorStyles)
        Me.Label20.AutoSize = True
        Me.Label20.ForeColor = System.Drawing.Color.Blue
        Me.Label20.Location = New System.Drawing.Point(841, 72)
        Me.Label20.Name = "Label20"
        Me.Label20.Size = New System.Drawing.Size(98, 13)
        Me.Label20.TabIndex = 83
        Me.Label20.Text = "PV (Provess Value)"
        '
        'Label21
        '
        Me.Label21.Anchor = CType((System.Windows.Forms.AnchorStyles.Top Or System.Windows.Forms.AnchorStyles.Right), System.Windows.Forms.AnchorStyles)
        Me.Label21.AutoSize = True
        Me.Label21.Font = New System.Drawing.Font("Microsoft Sans Serif", 8.25!, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, CType(0, Byte))
        Me.Label21.ForeColor = System.Drawing.Color.Green
        Me.Label21.Location = New System.Drawing.Point(766, 72)
        Me.Label21.Name = "Label21"
        Me.Label21.Size = New System.Drawing.Size(69, 13)
        Me.Label21.TabIndex = 82
        Me.Label21.Text = "SP (Setpoint)"
        '
        'pbPIDChart
        '
        Me.pbPIDChart.Anchor = CType((((System.Windows.Forms.AnchorStyles.Top Or System.Windows.Forms.AnchorStyles.Bottom) _
            Or System.Windows.Forms.AnchorStyles.Left) _
            Or System.Windows.Forms.AnchorStyles.Right), System.Windows.Forms.AnchorStyles)
        Me.pbPIDChart.BorderStyle = System.Windows.Forms.BorderStyle.Fixed3D
        Me.pbPIDChart.Location = New System.Drawing.Point(469, 88)
        Me.pbPIDChart.Name = "pbPIDChart"
        Me.pbPIDChart.Size = New System.Drawing.Size(540, 441)
        Me.pbPIDChart.SizeMode = System.Windows.Forms.PictureBoxSizeMode.StretchImage
        Me.pbPIDChart.TabIndex = 81
        Me.pbPIDChart.TabStop = False
        '
        'btnStart
        '
        Me.btnStart.Anchor = CType((System.Windows.Forms.AnchorStyles.Top Or System.Windows.Forms.AnchorStyles.Right), System.Windows.Forms.AnchorStyles)
        Me.btnStart.Location = New System.Drawing.Point(217, 551)
        Me.btnStart.Name = "btnStart"
        Me.btnStart.Size = New System.Drawing.Size(124, 30)
        Me.btnStart.TabIndex = 80
        Me.btnStart.Text = "Start"
        Me.btnStart.UseVisualStyleBackColor = True
        '
        'lblInterval
        '
        Me.lblInterval.AutoSize = True
        Me.lblInterval.Location = New System.Drawing.Point(235, 472)
        Me.lblInterval.Name = "lblInterval"
        Me.lblInterval.Size = New System.Drawing.Size(16, 13)
        Me.lblInterval.TabIndex = 43
        Me.lblInterval.Text = "---"
        '
        'Label18
        '
        Me.Label18.AutoSize = True
        Me.Label18.Location = New System.Drawing.Point(156, 472)
        Me.Label18.Name = "Label18"
        Me.Label18.Size = New System.Drawing.Size(64, 13)
        Me.Label18.TabIndex = 42
        Me.Label18.Text = "Interval (ms)"
        '
        'trackBarInterval
        '
        Me.trackBarInterval.Location = New System.Drawing.Point(152, 488)
        Me.trackBarInterval.Maximum = 1000
        Me.trackBarInterval.Minimum = 1
        Me.trackBarInterval.Name = "trackBarInterval"
        Me.trackBarInterval.Size = New System.Drawing.Size(286, 45)
        Me.trackBarInterval.TabIndex = 41
        Me.trackBarInterval.TickFrequency = 50
        Me.trackBarInterval.Value = 100
        '
        'lblSP
        '
        Me.lblSP.AutoSize = True
        Me.lblSP.Location = New System.Drawing.Point(240, 338)
        Me.lblSP.Name = "lblSP"
        Me.lblSP.Size = New System.Drawing.Size(16, 13)
        Me.lblSP.TabIndex = 40
        Me.lblSP.Text = "---"
        '
        'Label13
        '
        Me.Label13.AutoSize = True
        Me.Label13.Location = New System.Drawing.Point(157, 338)
        Me.Label13.Name = "Label13"
        Me.Label13.Size = New System.Drawing.Size(69, 13)
        Me.Label13.TabIndex = 39
        Me.Label13.Text = "SP (Setpoint)"
        '
        'trackBarSP
        '
        Me.trackBarSP.Enabled = False
        Me.trackBarSP.Location = New System.Drawing.Point(152, 354)
        Me.trackBarSP.Maximum = 100
        Me.trackBarSP.Name = "trackBarSP"
        Me.trackBarSP.Size = New System.Drawing.Size(286, 45)
        Me.trackBarSP.TabIndex = 38
        Me.trackBarSP.TickFrequency = 200
        '
        'progBarPV
        '
        Me.progBarPV.Location = New System.Drawing.Point(152, 299)
        Me.progBarPV.Maximum = 200
        Me.progBarPV.Name = "progBarPV"
        Me.progBarPV.Size = New System.Drawing.Size(286, 19)
        Me.progBarPV.Style = System.Windows.Forms.ProgressBarStyle.Continuous
        Me.progBarPV.TabIndex = 37
        '
        'lblPV
        '
        Me.lblPV.AutoSize = True
        Me.lblPV.Location = New System.Drawing.Point(262, 283)
        Me.lblPV.Name = "lblPV"
        Me.lblPV.Size = New System.Drawing.Size(16, 13)
        Me.lblPV.TabIndex = 36
        Me.lblPV.Text = "---"
        '
        'Label12
        '
        Me.Label12.AutoSize = True
        Me.Label12.Location = New System.Drawing.Point(158, 283)
        Me.Label12.Name = "Label12"
        Me.Label12.Size = New System.Drawing.Size(98, 13)
        Me.Label12.TabIndex = 35
        Me.Label12.Text = "PV (Process Value)"
        '
        'tbPVMax
        '
        Me.tbPVMax.Location = New System.Drawing.Point(58, 279)
        Me.tbPVMax.Name = "tbPVMax"
        Me.tbPVMax.Size = New System.Drawing.Size(70, 20)
        Me.tbPVMax.TabIndex = 34
        Me.tbPVMax.Text = "10"
        Me.tbPVMax.TextAlign = System.Windows.Forms.HorizontalAlignment.Center
        '
        'Label11
        '
        Me.Label11.AutoSize = True
        Me.Label11.Location = New System.Drawing.Point(12, 283)
        Me.Label11.Name = "Label11"
        Me.Label11.Size = New System.Drawing.Size(44, 13)
        Me.Label11.TabIndex = 33
        Me.Label11.Text = "PV.Max"
        '
        'tbPVMin
        '
        Me.tbPVMin.Location = New System.Drawing.Point(58, 252)
        Me.tbPVMin.Name = "tbPVMin"
        Me.tbPVMin.Size = New System.Drawing.Size(70, 20)
        Me.tbPVMin.TabIndex = 32
        Me.tbPVMin.Text = "0"
        Me.tbPVMin.TextAlign = System.Windows.Forms.HorizontalAlignment.Center
        '
        'Label17
        '
        Me.Label17.AutoSize = True
        Me.Label17.Location = New System.Drawing.Point(12, 256)
        Me.Label17.Name = "Label17"
        Me.Label17.Size = New System.Drawing.Size(41, 13)
        Me.Label17.TabIndex = 31
        Me.Label17.Text = "PV.Min"
        '
        'tbKd
        '
        Me.tbKd.Location = New System.Drawing.Point(58, 201)
        Me.tbKd.Name = "tbKd"
        Me.tbKd.Size = New System.Drawing.Size(70, 20)
        Me.tbKd.TabIndex = 27
        Me.tbKd.Text = "1"
        Me.tbKd.TextAlign = System.Windows.Forms.HorizontalAlignment.Center
        '
        'Label14
        '
        Me.Label14.AutoSize = True
        Me.Label14.Location = New System.Drawing.Point(12, 204)
        Me.Label14.Name = "Label14"
        Me.Label14.Size = New System.Drawing.Size(20, 13)
        Me.Label14.TabIndex = 26
        Me.Label14.Text = "Kd"
        '
        'tbKi
        '
        Me.tbKi.Location = New System.Drawing.Point(58, 175)
        Me.tbKi.Name = "tbKi"
        Me.tbKi.Size = New System.Drawing.Size(70, 20)
        Me.tbKi.TabIndex = 25
        Me.tbKi.Text = "0.01"
        Me.tbKi.TextAlign = System.Windows.Forms.HorizontalAlignment.Center
        '
        'Label15
        '
        Me.Label15.AutoSize = True
        Me.Label15.Location = New System.Drawing.Point(12, 178)
        Me.Label15.Name = "Label15"
        Me.Label15.Size = New System.Drawing.Size(16, 13)
        Me.Label15.TabIndex = 24
        Me.Label15.Text = "Ki"
        '
        'tbKp
        '
        Me.tbKp.Location = New System.Drawing.Point(58, 149)
        Me.tbKp.Name = "tbKp"
        Me.tbKp.Size = New System.Drawing.Size(70, 20)
        Me.tbKp.TabIndex = 23
        Me.tbKp.Text = "0.2"
        Me.tbKp.TextAlign = System.Windows.Forms.HorizontalAlignment.Center
        '
        'Label16
        '
        Me.Label16.AutoSize = True
        Me.Label16.Location = New System.Drawing.Point(12, 152)
        Me.Label16.Name = "Label16"
        Me.Label16.Size = New System.Drawing.Size(20, 13)
        Me.Label16.TabIndex = 22
        Me.Label16.Text = "Kp"
        '
        'PictureBox1
        '
        Me.PictureBox1.Image = Global.FanucBridge.My.Resources.Resources.pid
        Me.PictureBox1.Location = New System.Drawing.Point(6, 6)
        Me.PictureBox1.Name = "PictureBox1"
        Me.PictureBox1.Size = New System.Drawing.Size(146, 68)
        Me.PictureBox1.SizeMode = System.Windows.Forms.PictureBoxSizeMode.Zoom
        Me.PictureBox1.TabIndex = 0
        Me.PictureBox1.TabStop = False
        '
        'lblStateInfo
        '
        Me.lblStateInfo.Anchor = CType((((System.Windows.Forms.AnchorStyles.Top Or System.Windows.Forms.AnchorStyles.Bottom) _
            Or System.Windows.Forms.AnchorStyles.Left) _
            Or System.Windows.Forms.AnchorStyles.Right), System.Windows.Forms.AnchorStyles)
        Me.lblStateInfo.AutoSize = True
        Me.lblStateInfo.Font = New System.Drawing.Font("Microsoft Sans Serif", 8.25!, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, CType(0, Byte))
        Me.lblStateInfo.Location = New System.Drawing.Point(100, 16)
        Me.lblStateInfo.Name = "lblStateInfo"
        Me.lblStateInfo.Size = New System.Drawing.Size(205, 13)
        Me.lblStateInfo.TabIndex = 68
        Me.lblStateInfo.Text = "=>                                              "
        Me.lblStateInfo.Visible = False
        '
        'lblDateTime
        '
        Me.lblDateTime.AutoSize = True
        Me.lblDateTime.Font = New System.Drawing.Font("Microsoft Sans Serif", 9.75!, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, CType(0, Byte))
        Me.lblDateTime.ForeColor = System.Drawing.Color.FromArgb(CType(CType(30, Byte), Integer), CType(CType(30, Byte), Integer), CType(CType(30, Byte), Integer))
        Me.lblDateTime.Location = New System.Drawing.Point(888, 679)
        Me.lblDateTime.Name = "lblDateTime"
        Me.lblDateTime.Size = New System.Drawing.Size(151, 16)
        Me.lblDateTime.TabIndex = 61
        Me.lblDateTime.Text = "Tue Dec 15, 20 02:45"
        Me.lblDateTime.TextAlign = System.Drawing.ContentAlignment.TopRight
        '
        'Label4
        '
        Me.Label4.AutoSize = True
        Me.Label4.Font = New System.Drawing.Font("Tahoma", 8.25!, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, CType(0, Byte))
        Me.Label4.Location = New System.Drawing.Point(22, 16)
        Me.Label4.Name = "Label4"
        Me.Label4.Size = New System.Drawing.Size(70, 13)
        Me.Label4.TabIndex = 69
        Me.Label4.Text = "StatusInfo:"
        Me.Label4.Visible = False
        '
        'OpenFileDialog1
        '
        Me.OpenFileDialog1.FileName = "OpenFileDialog1"
        '
        'btnSettings
        '
        Me.btnSettings.Image = Global.FanucBridge.My.Resources.Resources.settings
        Me.btnSettings.InitialImage = Nothing
        Me.btnSettings.Location = New System.Drawing.Point(1016, 4)
        Me.btnSettings.Name = "btnSettings"
        Me.btnSettings.Size = New System.Drawing.Size(32, 32)
        Me.btnSettings.SizeMode = System.Windows.Forms.PictureBoxSizeMode.Zoom
        Me.btnSettings.TabIndex = 45
        Me.btnSettings.TabStop = False
        '
        'tmrChart
        '
        Me.tmrChart.Enabled = True
        Me.tmrChart.Interval = 37
        '
        'tmrReadPLC
        '
        Me.tmrReadPLC.Interval = 25
        '
        'chkAutoMonitor
        '
        Me.chkAutoMonitor.AutoSize = True
        Me.chkAutoMonitor.Location = New System.Drawing.Point(313, 14)
        Me.chkAutoMonitor.Name = "chkAutoMonitor"
        Me.chkAutoMonitor.Size = New System.Drawing.Size(86, 17)
        Me.chkAutoMonitor.TabIndex = 70
        Me.chkAutoMonitor.Text = "Auto Monitor"
        Me.chkAutoMonitor.UseVisualStyleBackColor = True
        '
        'txtSpindleTime
        '
        Me.txtSpindleTime.Location = New System.Drawing.Point(575, 11)
        Me.txtSpindleTime.Name = "txtSpindleTime"
        Me.txtSpindleTime.Size = New System.Drawing.Size(47, 20)
        Me.txtSpindleTime.TabIndex = 71
        '
        'lblSpindleTime
        '
        Me.lblSpindleTime.AutoSize = True
        Me.lblSpindleTime.Location = New System.Drawing.Point(525, 15)
        Me.lblSpindleTime.Name = "lblSpindleTime"
        Me.lblSpindleTime.Size = New System.Drawing.Size(49, 13)
        Me.lblSpindleTime.TabIndex = 72
        Me.lblSpindleTime.Text = "Time(ms)"
        '
        'Label29
        '
        Me.Label29.AutoSize = True
        Me.Label29.Location = New System.Drawing.Point(404, 15)
        Me.Label29.Name = "Label29"
        Me.Label29.Size = New System.Drawing.Size(56, 13)
        Me.Label29.TabIndex = 73
        Me.Label29.Text = "Speed TH"
        '
        'txtThreshold
        '
        Me.txtThreshold.Location = New System.Drawing.Point(462, 11)
        Me.txtThreshold.Name = "txtThreshold"
        Me.txtThreshold.Size = New System.Drawing.Size(48, 20)
        Me.txtThreshold.TabIndex = 74
        '
        'lblCurrSpindleRPM
        '
        Me.lblCurrSpindleRPM.AutoSize = True
        Me.lblCurrSpindleRPM.Location = New System.Drawing.Point(634, 15)
        Me.lblCurrSpindleRPM.Name = "lblCurrSpindleRPM"
        Me.lblCurrSpindleRPM.Size = New System.Drawing.Size(86, 13)
        Me.lblCurrSpindleRPM.TabIndex = 75
        Me.lblCurrSpindleRPM.Text = "Curr Speed(rpm):"
        '
        'lblTValues
        '
        Me.lblTValues.AutoSize = True
        Me.lblTValues.Location = New System.Drawing.Point(395, 671)
        Me.lblTValues.Name = "lblTValues"
        Me.lblTValues.Size = New System.Drawing.Size(50, 39)
        Me.lblTValues.TabIndex = 76
        Me.lblTValues.Text = "DataNo: " & Global.Microsoft.VisualBasic.ChrW(13) & Global.Microsoft.VisualBasic.ChrW(10) & "Type: " & Global.Microsoft.VisualBasic.ChrW(13) & Global.Microsoft.VisualBasic.ChrW(10) & "G_Data: "
        '
        'Label32
        '
        Me.Label32.AutoSize = True
        Me.Label32.Font = New System.Drawing.Font("Microsoft Sans Serif", 9.75!, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, CType(0, Byte))
        Me.Label32.Location = New System.Drawing.Point(406, 652)
        Me.Label32.Name = "Label32"
        Me.Label32.Size = New System.Drawing.Size(18, 16)
        Me.Label32.TabIndex = 77
        Me.Label32.Text = "T"
        '
        'Label33
        '
        Me.Label33.AutoSize = True
        Me.Label33.Font = New System.Drawing.Font("Microsoft Sans Serif", 9.75!, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, CType(0, Byte))
        Me.Label33.Location = New System.Drawing.Point(518, 652)
        Me.Label33.Name = "Label33"
        Me.Label33.Size = New System.Drawing.Size(18, 16)
        Me.Label33.TabIndex = 79
        Me.Label33.Text = "S"
        '
        'lblSValues
        '
        Me.lblSValues.AutoSize = True
        Me.lblSValues.Location = New System.Drawing.Point(507, 671)
        Me.lblSValues.Name = "lblSValues"
        Me.lblSValues.Size = New System.Drawing.Size(50, 39)
        Me.lblSValues.TabIndex = 78
        Me.lblSValues.Text = "DataNo: " & Global.Microsoft.VisualBasic.ChrW(13) & Global.Microsoft.VisualBasic.ChrW(10) & "Type: " & Global.Microsoft.VisualBasic.ChrW(13) & Global.Microsoft.VisualBasic.ChrW(10) & "G_Data: "
        '
        'Label35
        '
        Me.Label35.AutoSize = True
        Me.Label35.Font = New System.Drawing.Font("Microsoft Sans Serif", 9.75!, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, CType(0, Byte))
        Me.Label35.Location = New System.Drawing.Point(634, 652)
        Me.Label35.Name = "Label35"
        Me.Label35.Size = New System.Drawing.Size(17, 16)
        Me.Label35.TabIndex = 81
        Me.Label35.Text = "F"
        '
        'lblFValues
        '
        Me.lblFValues.AutoSize = True
        Me.lblFValues.Location = New System.Drawing.Point(623, 671)
        Me.lblFValues.Name = "lblFValues"
        Me.lblFValues.Size = New System.Drawing.Size(50, 39)
        Me.lblFValues.TabIndex = 80
        Me.lblFValues.Text = "DataNo: " & Global.Microsoft.VisualBasic.ChrW(13) & Global.Microsoft.VisualBasic.ChrW(10) & "Type: " & Global.Microsoft.VisualBasic.ChrW(13) & Global.Microsoft.VisualBasic.ChrW(10) & "G_Data: "
        '
        'Label37
        '
        Me.Label37.AutoSize = True
        Me.Label37.Font = New System.Drawing.Font("Microsoft Sans Serif", 9.75!, System.Drawing.FontStyle.Bold, System.Drawing.GraphicsUnit.Point, CType(0, Byte))
        Me.Label37.Location = New System.Drawing.Point(748, 652)
        Me.Label37.Name = "Label37"
        Me.Label37.Size = New System.Drawing.Size(20, 16)
        Me.Label37.TabIndex = 83
        Me.Label37.Text = "M"
        '
        'lblMValues
        '
        Me.lblMValues.AutoSize = True
        Me.lblMValues.Location = New System.Drawing.Point(737, 671)
        Me.lblMValues.Name = "lblMValues"
        Me.lblMValues.Size = New System.Drawing.Size(50, 39)
        Me.lblMValues.TabIndex = 82
        Me.lblMValues.Text = "DataNo: " & Global.Microsoft.VisualBasic.ChrW(13) & Global.Microsoft.VisualBasic.ChrW(10) & "Type: " & Global.Microsoft.VisualBasic.ChrW(13) & Global.Microsoft.VisualBasic.ChrW(10) & "G_Data: "
        '
        'lblSeqNum
        '
        Me.lblSeqNum.AutoSize = True
        Me.lblSeqNum.Font = New System.Drawing.Font("Microsoft Sans Serif", 8.25!, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, CType(0, Byte))
        Me.lblSeqNum.Location = New System.Drawing.Point(265, 679)
        Me.lblSeqNum.Name = "lblSeqNum"
        Me.lblSeqNum.Size = New System.Drawing.Size(51, 13)
        Me.lblSeqNum.TabIndex = 84
        Me.lblSeqNum.Text = "SeqNum:"
        '
        'Main
        '
        Me.AutoScaleDimensions = New System.Drawing.SizeF(6.0!, 13.0!)
        Me.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font
        Me.ClientSize = New System.Drawing.Size(1056, 711)
        Me.Controls.Add(Me.lblSeqNum)
        Me.Controls.Add(Me.Label37)
        Me.Controls.Add(Me.lblMValues)
        Me.Controls.Add(Me.Label35)
        Me.Controls.Add(Me.lblFValues)
        Me.Controls.Add(Me.Label33)
        Me.Controls.Add(Me.lblSValues)
        Me.Controls.Add(Me.Label32)
        Me.Controls.Add(Me.lblTValues)
        Me.Controls.Add(Me.lblCurrSpindleRPM)
        Me.Controls.Add(Me.txtThreshold)
        Me.Controls.Add(Me.Label29)
        Me.Controls.Add(Me.lblSpindleTime)
        Me.Controls.Add(Me.txtSpindleTime)
        Me.Controls.Add(Me.chkAutoMonitor)
        Me.Controls.Add(Me.Label4)
        Me.Controls.Add(Me.lblDateTime)
        Me.Controls.Add(Me.tabTiConn)
        Me.Controls.Add(Me.lblAppVersion)
        Me.Controls.Add(Me.btnSettings)
        Me.Controls.Add(Me.btnDisconnect)
        Me.Controls.Add(Me.btnConnect)
        Me.Controls.Add(Me.lblStateInfo)
        Me.FormBorderStyle = System.Windows.Forms.FormBorderStyle.FixedSingle
        Me.Icon = CType(resources.GetObject("$this.Icon"), System.Drawing.Icon)
        Me.MaximizeBox = False
        Me.Name = "Main"
        Me.StartPosition = System.Windows.Forms.FormStartPosition.CenterScreen
        Me.Text = "FanucBridge Settings V3.1.19"
        Me.tabTiConn.ResumeLayout(False)
        Me.tabTMS.ResumeLayout(False)
        Me.tabTMS.PerformLayout()
        CType(Me.gridViewPMCs, System.ComponentModel.ISupportInitialize).EndInit()
        CType(Me.gridViewMacros, System.ComponentModel.ISupportInitialize).EndInit()
        Me.tabTestFunctions.ResumeLayout(False)
        Me.GroupBox8.ResumeLayout(False)
        Me.GroupBox8.PerformLayout()
        Me.GroupBox7.ResumeLayout(False)
        Me.GroupBox7.PerformLayout()
        Me.GroupBox6.ResumeLayout(False)
        Me.GroupBox6.PerformLayout()
        Me.GroupBox5.ResumeLayout(False)
        Me.GroupBox5.PerformLayout()
        Me.GroupBox4.ResumeLayout(False)
        Me.GroupBox4.PerformLayout()
        Me.GroupBox3.ResumeLayout(False)
        Me.GroupBox3.PerformLayout()
        Me.GroupBox2.ResumeLayout(False)
        Me.GroupBox2.PerformLayout()
        Me.GroupBox1.ResumeLayout(False)
        Me.GroupBox1.PerformLayout()
        Me.tabPIDControl.ResumeLayout(False)
        Me.tabPIDControl.PerformLayout()
        Me.GroupBox10.ResumeLayout(False)
        Me.GroupBox10.PerformLayout()
        Me.GroupBox9.ResumeLayout(False)
        Me.GroupBox9.PerformLayout()
        CType(Me.pbPIDChart, System.ComponentModel.ISupportInitialize).EndInit()
        CType(Me.trackBarInterval, System.ComponentModel.ISupportInitialize).EndInit()
        CType(Me.trackBarSP, System.ComponentModel.ISupportInitialize).EndInit()
        CType(Me.PictureBox1, System.ComponentModel.ISupportInitialize).EndInit()
        CType(Me.btnSettings, System.ComponentModel.ISupportInitialize).EndInit()
        Me.ResumeLayout(False)
        Me.PerformLayout()

    End Sub
    Private WithEvents btnDisconnect As Button
    Private WithEvents btnConnect As Button
    Private WithEvents txtPort As Label
    Private WithEvents txtPortInput As TextBox
    Private WithEvents txtIpAddress As Label
    Private WithEvents txtIpAddressInput As TextBox
    Friend WithEvents Timer1 As Timer
    Friend WithEvents btnSettings As PictureBox
    Friend WithEvents notifyIcon As NotifyIcon
    Friend WithEvents lblAppVersion As Label
    Friend WithEvents tabTiConn As TabControl
    Friend WithEvents tabTMS As TabPage
    Friend WithEvents lblModbusSvrIP As Label
    Friend WithEvents titleModbusSrvInfo As Label
    Friend WithEvents Label2 As Label
    Friend WithEvents Label1 As Label
    Friend WithEvents gridViewPMCs As DataGridView
    Friend WithEvents DataGridViewTextBoxColumn1 As DataGridViewTextBoxColumn
    Friend WithEvents DataGridViewTextBoxColumn2 As DataGridViewTextBoxColumn
    Friend WithEvents DataGridViewTextBoxColumn3 As DataGridViewTextBoxColumn
    Friend WithEvents DataGridViewTextBoxColumn4 As DataGridViewTextBoxColumn
    Friend WithEvents DataGridViewTextBoxColumn5 As DataGridViewTextBoxColumn
    Friend WithEvents DataGridViewTextBoxColumn6 As DataGridViewTextBoxColumn
    Friend WithEvents DataGridViewTextBoxColumn7 As DataGridViewTextBoxColumn
    Friend WithEvents DataGridViewTextBoxColumn8 As DataGridViewTextBoxColumn
    Friend WithEvents gridViewMacros As DataGridView
    Friend WithEvents No As DataGridViewTextBoxColumn
    Friend WithEvents Desc As DataGridViewTextBoxColumn
    Friend WithEvents ModbusAddr As DataGridViewTextBoxColumn
    Friend WithEvents ModbusVal As DataGridViewTextBoxColumn
    Friend WithEvents FanucAddr As DataGridViewTextBoxColumn
    Friend WithEvents FanucVal As DataGridViewTextBoxColumn
    Friend WithEvents Status As DataGridViewTextBoxColumn
    Friend WithEvents WStatus As DataGridViewTextBoxColumn
    Friend WithEvents lblDateTime As Label
    Friend WithEvents lblStateInfo As Label

    Friend WithEvents Label4 As Label
    Friend WithEvents lblTWriteStatus As Label
    Friend WithEvents lblCurrentToolNum As Label
    Private WithEvents btnRunSpeedCheck As Button
    Friend WithEvents lblSpeedCheckInfo As Label
    Friend WithEvents tabTestFunctions As TabPage
    Friend WithEvents GroupBox1 As GroupBox
    Friend WithEvents GroupBox2 As GroupBox
    Friend WithEvents lblCopyProgInfo As Label
    Friend WithEvents txtDestNCProgNum As TextBox
    Friend WithEvents txtSrcNCProgNum As TextBox
    Private WithEvents btnCopyProg As Button
    Friend WithEvents Label5 As Label
    Friend WithEvents Label3 As Label
    Friend WithEvents GroupBox3 As GroupBox
    Friend WithEvents lblCurrSequenceNumInfo As Label
    Private WithEvents btnReadSequenceNum As Button
    Friend WithEvents GroupBox4 As GroupBox
    Friend WithEvents lblCurrProgNameInfo As Label
    Private WithEvents btnReadCurrentProgName As Button
    Friend WithEvents GroupBox5 As GroupBox
    Friend WithEvents txtMaxNumOfProg As TextBox
    Friend WithEvents Label6 As Label
    Friend WithEvents btnGetFolderPath1 As Button
    Friend WithEvents txtFolderPath1 As TextBox
    Friend WithEvents Label7 As Label
    Private WithEvents btnReadFileProperty As Button
    Friend WithEvents lblFilePropertiesInfo As Label
    Friend WithEvents OpenFileDialog1 As OpenFileDialog
    Friend WithEvents GroupBox6 As GroupBox
    Friend WithEvents txtCurrProgramFileInfo As TextBox
    Private WithEvents btnReadCurrProgInfo As Button
    Friend WithEvents txtSubFolderReqNum As TextBox
    Friend WithEvents Label8 As Label
    Friend WithEvents GroupBox7 As GroupBox
    Friend WithEvents lblReadModalInfo As Label
    Private WithEvents btnReadModalData As Button
    Friend WithEvents txtModalBlock As TextBox
    Friend WithEvents Label10 As Label
    Friend WithEvents txtModalType As TextBox
    Friend WithEvents Label9 As Label
    Private WithEvents btnReadModalMCode As Button
    Private WithEvents btnReadModalFeedrate As Button
    Private WithEvents btnReadModalSpindle As Button
    Private WithEvents btnReadModalTool As Button
    Friend WithEvents GroupBox8 As GroupBox
    Friend WithEvents lblLastEditTimeInfo As Label
    Private WithEvents btnGetLastEditTimeForCurrentProgramNumber As Button
    Friend WithEvents tabPIDControl As TabPage
    Friend WithEvents PictureBox1 As PictureBox
    Private WithEvents tbKd As TextBox
    Private WithEvents Label14 As Label
    Private WithEvents tbKi As TextBox
    Private WithEvents Label15 As Label
    Private WithEvents tbKp As TextBox
    Private WithEvents Label16 As Label
    Private WithEvents tbPVMin As TextBox
    Private WithEvents Label17 As Label
    Private WithEvents tbPVMax As TextBox
    Private WithEvents Label11 As Label
    Private WithEvents lblInterval As Label
    Private WithEvents Label18 As Label
    Private WithEvents trackBarInterval As TrackBar
    Private WithEvents lblSP As Label
    Private WithEvents Label13 As Label
    Private WithEvents trackBarSP As TrackBar
    Private WithEvents progBarPV As ProgressBar
    Private WithEvents lblPV As Label
    Private WithEvents Label12 As Label
    Private WithEvents btnStart As Button
    Private WithEvents Label19 As Label
    Private WithEvents Label20 As Label
    Private WithEvents Label21 As Label
    Private WithEvents pbPIDChart As PictureBox
    Private WithEvents tmrChart As Timer
    Private WithEvents lblOutput As Label
    Private WithEvents Label22 As Label
    Private WithEvents progBarOut As ProgressBar
    Private WithEvents tbOMax As TextBox
    Private WithEvents Label23 As Label
    Private WithEvents tbOMin As TextBox
    Private WithEvents Label24 As Label
    Friend WithEvents lblStatusPIDOutToCNC As Label
    Friend WithEvents GroupBox9 As GroupBox
    Friend WithEvents Label25 As Label
    Friend WithEvents tbPLCPort As TextBox
    Friend WithEvents Label26 As Label
    Friend WithEvents tbPLCIP As TextBox
    Friend WithEvents tmrReadPLC As Timer
    Friend WithEvents GroupBox10 As GroupBox
    Friend WithEvents Label27 As Label
    Friend WithEvents cbBoxTypePMC As ComboBox
    Friend WithEvents Label28 As Label
    Friend WithEvents tbAdaptiveEnableAddr As TextBox
    Friend WithEvents lblFeedFoldStatus As Label
    Friend WithEvents lblCncType As Label
    Friend WithEvents lblRunVal As Label
    Friend WithEvents chkAutoMonitor As CheckBox
    Friend WithEvents txtSpindleTime As TextBox
    Friend WithEvents lblSpindleTime As Label
    Friend WithEvents Label29 As Label
    Friend WithEvents txtThreshold As TextBox
    Friend WithEvents lblCurrSpindleRPM As Label
    Friend WithEvents lblPIDTestValBinary As Label
    Friend WithEvents btnWriteTestPIDVal As Button
    Friend WithEvents txtPIDTestVal As TextBox
    Friend WithEvents Label30 As Label
    Friend WithEvents btnAddr4OnOff As Button
    Friend WithEvents btnAddr3OnOff As Button
    Friend WithEvents lblTValues As Label
    Friend WithEvents Label32 As Label
    Friend WithEvents Label33 As Label
    Friend WithEvents lblSValues As Label
    Friend WithEvents Label35 As Label
    Friend WithEvents lblFValues As Label
    Friend WithEvents Label37 As Label
    Friend WithEvents lblMValues As Label
    Friend WithEvents lblSeqNum As Label
End Class
