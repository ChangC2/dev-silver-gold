#include "widget.h"
#include "ui_widget.h"
#include "QRegularExpression"
#include "QRegularExpressionValidator"
#include "QIntValidator"
#include "QDoubleValidator"
#include <QString>
#include <QMessageBox>
#include <QDebug>
#include <QException>
#include <QRandomGenerator>
#include "Global.h"
#include "apputils.h"
#include "spiComm.h"
#include "include/fwlib32.h"
#include "QtCore/qmath.h"
#include <QDateTime>

#define MMS_RELAY_SERVER_IP "www.slymms.com"
#define MMS_RELAY_SERVER_PORT 5050

#define CYCLE_MILIS_1SEC 1000 * 1
#define CYCLE_MILIS_5SECS 1000 * 5
#define CYCLE_MILIS_10SECS 1000 * 10
#define CYCLE_MILIS_20SECS 1000 * 20
#define CYCLE_MILIS_30SECS 1000 * 30
#define CYCLE_MILIS_1MIN 1000 * 60
#define CYCLE_MILIS_2MINS 1000 * 60 * 2
#define CYCLE_MILIS_3MINS 1000 * 60 * 3
#define CYCLE_MILIS_5MINS 1000 * 60 * 5
#define CYCLE_MILIS_10MINS 1000 * 60 * 10

#define TOS_MODBUS_SERVER_PORT 5150
#define TOS_GLGDATA_SERVER_PORT 5151

Widget::Widget(QWidget *parent)
    : QWidget(parent)
    , ui(new Ui::Widget)
    , bFanucConnected(false)
    , bNeedsAutoConnect(true)
{

    ui->setupUi(this);
    appSetting = new AppSettings();

    qDebug() << "S" << stfmData[0].datano << stfmData[0].type << stfmData[0].auxData.intValue;
    qDebug() << "T" << stfmData[1].datano << stfmData[1].type << stfmData[1].auxData.intValue;
    qDebug() << "F" << stfmData[2].datano << stfmData[2].type << stfmData[2].auxData.floatValue;
    qDebug() << "M" << stfmData[3].datano << stfmData[3].type << stfmData[3].auxData.intValue;

    // MMS Signal Relay Server -----------------------------------------------------------------
    connect(&signalReportClient, &StatusReportTCPClient::connected, [this]() {
       qDebug() << "Signal Server Connected!";

       signalReportClient.sendMessage("Hello, MMS!");
    });

    connect(&signalReportClient, &StatusReportTCPClient::messageReceived, [](QString message) {
       qDebug() << "Received message:" << message;
    });
    connectSignalServer();
    // -----------------------------------------------------------------------------------------/

    // GLG Data Server -------------------------------------------------------------------------
    glgDataServer.startServer(TOS_GLGDATA_SERVER_PORT);
    // -----------------------------------------------------------------------------------------/

    // Modbus Server for providing the HP2 -----------------------------------------------------
    modbusTCPServer = new MMSModbusTcpServer(this);
    modbusTCPServer->setConnectionParameter(QModbusServer::NetworkAddressParameter, "0.0.0.0");
    modbusTCPServer->setConnectionParameter(QModbusServer::NetworkPortParameter, TOS_MODBUS_SERVER_PORT);
    modbusTCPServer->setServerAddress(1);

    QModbusDataUnitMap reg;
    reg.insert(QModbusDataUnit::Coils, {QModbusDataUnit::Coils, 0, 100});
    reg.insert(QModbusDataUnit::HoldingRegisters, {QModbusDataUnit::HoldingRegisters, 0, 100});

    if (!modbusTCPServer->setMap(reg)) {
        // Failed to map the registers

        qDebug() << "Failed to init Modbus Server!";
        QMessageBox::warning(this, "Modbus", "Failed to init Modbus Server!");

        quitApp();
        return;
    } else if (!modbusTCPServer->connectDevice()) {
        // Failed to connect the device

        qDebug() << "Failed to connect Device!";
        QMessageBox::warning(this, "Modbus", "Failed to connect Device!");

        quitApp();
        return;
    } else {
        // Modbus Server Init Success!

        connect(modbusTCPServer, &QModbusServer::dataWritten, this, &Widget::handleDataWritten);

        // Set Data, This mode is trigerring the Slot too.
        //modbusTCPServer->setData(QModbusDataUnit::HoldingRegisters, 0, 10);
        //modbusTCPServer->setData(QModbusDataUnit::HoldingRegisters, 1, 15);
        //modbusTCPServer->setData(QModbusDataUnit::HoldingRegisters, 2, 20);
        //modbusTCPServer->setData(QModbusDataUnit::Coils, 0, 1);
        //modbusTCPServer->setData(QModbusDataUnit::Coils, 1, 1);
        //modbusTCPServer->setData(QModbusDataUnit::Coils, 2, 0);

        // QModbusDataUnit& holdingRegUnit = reg[QModbusDataUnit::HoldingRegisters];
        // holdingRegUnit.setValue(0, 200);
        // holdingRegUnit.setValue(1, 300);

        QModbusDataUnit& coilRegUnit = reg[QModbusDataUnit::Coils];
        coilRegUnit.setValue(4, 1);
        coilRegUnit.setValue(5, 1);
        modbusTCPServer->setMap(reg);

        QModbusDataUnit dataUnit(QModbusDataUnit::HoldingRegisters, 0, 2);
        dataUnit.setValues(floatToRegisters(0.75));
        modbusTCPServer->setData(dataUnit);
    }
    // -----------------------------------------------------------------------------------------/

    // Init Fanuc ------------------------------------------------------------------------------
#ifndef _WIN32
    if (cnc_startupprocess(0, "focas.log") != EW_OK) {
        qDebug() << "Failed to create required log file!";
        QMessageBox::warning(this, "Fanuc Settings", "Please check fanuc ip or port.");

        quitApp();
        return;
    }
#endif
    // ----------------------------------------------------------------------------------------/

    // Main Timer
    connect(&mainTimer, SIGNAL(timeout()), this, SLOT(handleMainTimeout()));

    // Start Spi Reading
    spiReader.start();

    pidObj = NULL;

    // IP, Port Inputs, apply input filters
    QRegularExpression regex("^((\\d{1,2}|1\\d{2}|2[0-4]\\d|25[0-5])\\.){3}(\\d{1,2}|1\\d{2}|2[0-4]\\d|25[0-5])$");
    QRegularExpressionValidator *validator = new QRegularExpressionValidator(regex, ui->txtIPAddr);
    ui->txtIPAddr->setValidator(validator);

    QIntValidator *portValidator = new QIntValidator(0, 65535, ui->txtPort);
    ui->txtPort->setValidator(portValidator);

    ui->txtIPAddr->setText(appSetting->getStringValue(SETTING_FANUC_IP, ""));
    ui->txtPort->setText(QString::number(appSetting->getIntValue(SETTING_FANUC_PORT, 0)));

    // MMS Settings
    ui->txtCustomerID->setText(appSetting->getStringValue(SETTING_MMS_CUSTOMERID, ""));
    ui->txtMachineName->setText(appSetting->getStringValue(SETTING_MMS_MACHINENAME, ""));

    // PID Timer
    connect(&pidTimer, SIGNAL(timeout()), this, SLOT(handlePIDTimeout()));

    // PID settings, apply input filters
    QDoubleValidator *floatValueValidator = new QDoubleValidator(this);
    ui->txtKp->setValidator(floatValueValidator);
    ui->txtKi->setValidator(floatValueValidator);
    ui->txtKd->setValidator(floatValueValidator);

    ui->txtKp->setText(QString::number(appSetting->getFloatValue(SETTING_PID_KP, 0.2)));
    ui->txtKi->setText(QString::number(appSetting->getFloatValue(SETTING_PID_KI, 0.01)));
    ui->txtKd->setText(QString::number(appSetting->getFloatValue(SETTING_PID_KD, 1)));

    // PID Interval Init Value
    int pidInterval = appSetting->getIntValue(SETTING_PID_INTERVAL, 20);
    ui->lblPIDIntervals->setText(QString::number(pidInterval) + " ms");
    ui->sliderPIDIntervals->setValue(pidInterval);

    // PID SP Init Value
    float pidSP = appSetting->getFloatValue(SETTING_PID_SP);
    ui->lblSPValue->setText(QString::number(pidSP));
    ui->sliderSP->setValue(pidSP * 10);

    // SPI Settings for PV and PID Output Registers
    QRegExpValidator *hexValidator = new QRegExpValidator(this);
    QRegExp regExp("[a-fA-F0-9]+");
    hexValidator->setRegExp(regExp);
    ui->txtSPIAddr->setValidator(hexValidator);
    ui->txtSPIAddr->setText(QString::number(appSetting->getIntValue(SETTING_PID_PV_SPI_ADDR), 16).toUpper());

    QIntValidator *intValidator = new QIntValidator(this);
    ui->txtPIDOutAddr->setValidator(intValidator);
    ui->cbPIDOoutRegType->setCurrentIndex(appSetting->getIntValue(SETTING_PID_OUT_REG_TYPE));

    int pidOutRegAddr = appSetting->getIntValue(SETTING_PID_OUT_REG_ADDR);
    if (pidOutRegAddr > 0) {
        ui->txtPIDOutAddr->setText(QString::number(pidOutRegAddr));
    }

    // Init ML/AI CSV File Operations
    // CSV Timer
    connect(&csvTimer, SIGNAL(timeout()), this, SLOT(handleCSVFileTimeout()));

    // Init Read Interval Settings
    int fanucReadInterval = appSetting->getIntValue(SETTING_FANUC_READ_INTERVAL, 20);
    ui->lblFanucReadIntervals->setText(QString::number(fanucReadInterval) + " ms");
    ui->sliderFanucReadIntervals->setValue(fanucReadInterval);

    ui->txtHpX->setValidator(floatValueValidator);
    ui->txtHpX->setText(QString::number(appSetting->getFloatValue(SETTING_FANUC_HP_X, 1.0)));

}

Widget::~Widget() {
    delete ui;
}

void Widget::quitApp() {
    QTimer::singleShot(0, []() {
        QApplication::quit();
    });
}

void Widget::closeEvent(QCloseEvent *event) {
    disconnectFanuc();

#ifndef _WIN32
    cnc_exitprocess();
#endif

    // Stop PID timer
    pidTimer.stop();

    // Stop Main Timer
    csvTimer.stop();

    // Remove SPI Interface
    spiReader.requestInterruption();
    spiReader.wait();

    // Remove PID block
    if (pidObj != NULL) {
        delete pidObj;
    }

    // Disconnect Modbus
    if (modbusTCPServer != NULL) {
        qDebug() << "Close Modbus Server!";
        modbusTCPServer->closeServer();
    }

    if (m_file.isOpen()) {
        m_file.close();
    }

    QWidget::closeEvent(event);
}


void Widget::handleDataWritten(QModbusDataUnit::RegisterType dataType, int address, int size) {
    //qDebug() << "Modbus Data:" << dataType << address << size;
}

void Widget::on_btnConnect_clicked() {

    if (bFanucConnected) {
        // Disconnect Fanuc
        disconnectFanuc();

        bNeedsAutoConnect = false;
        mainTimer.stop();
    } else {
        // Try to Connect
        QString ipAddrVal = ui->txtIPAddr->text();
        QString portVal = ui->txtPort->text();
        int portAddr = portVal.toInt();

        if(AppUtils::isValidIPAddress(ipAddrVal) && AppUtils::isValidPort(portAddr)) {
            appSetting->setValue(SETTING_FANUC_IP, ipAddrVal);
            appSetting->setValue(SETTING_FANUC_PORT, portAddr);
        } else {
            QMessageBox::warning(this, "Fanuc Settings", "Please check fanuc ip or port.");
            return;
        }

        // Remove this later
        bNeedsAutoConnect = true;
        mainTimer.start(50);

        QString customerId = ui->txtCustomerID->text().trimmed();
        QString machineName = ui->txtMachineName->text().trimmed();
        appSetting->setValue(SETTING_MMS_CUSTOMERID, customerId);
        appSetting->setValue(SETTING_MMS_MACHINENAME, machineName);
        if (!isSignalServerConnected()) {
            connectSignalServer();
        }

        // Try to connect with fanuc
        if (connectFanuc(true)) {
            //bNeedsAutoConnect = true;
            //mainTimer.start(500);
        }
    }
}

bool Widget::connectFanuc(bool isInteractiveMode) {

    reconnectTryTimeFanuc = getCurrentTimeMilis();

    QString ipAddr = appSetting->getStringValue(SETTING_FANUC_IP);
    int port = appSetting->getIntValue(SETTING_FANUC_PORT);


    int ret = 0;
// We call this module in the constructor of Widget
/*
#ifndef _WIN32
    if (cnc_startupprocess(0, "focas.log") != EW_OK) {
        fprintf(stderr, "Failed to create required log file!\n");
        QMessageBox::warning(this, "Fanuc", "Failed to create required log file!\n");

        return false;
    }
#endif
*/
    if ((ret = cnc_allclibhndl3(ipAddr.toStdString().c_str(), port, 5, &libh)) != EW_OK) {
        if (isInteractiveMode) {
            QMessageBox::warning(this, "Fanuc", QString("Failed to connect: %1").arg(ret));
        }
        return false;
    } else {
        // Get Fanuc Information
        ODBSYS odbSysInfo;
        if (cnc_sysinfo(libh, &odbSysInfo) == EW_OK) {
            fanucData.addInfo = odbSysInfo.addinfo;
            fanucData.max_axis = odbSysInfo.max_axis;
            fanucData.cnc_type = QString(odbSysInfo.cnc_type);
            fanucData.mt_type = QString(odbSysInfo.mt_type);
            fanucData.series = QString(odbSysInfo.series);
            fanucData.version = QString(odbSysInfo.version);
            fanucData.axes = QString(odbSysInfo.axes);

            // Show Current Fanuc Information
            QString cncInfo = QString("CNC Type: %1-%2\nSeries: %3\nVersion: %4\nAxies: %5")
                    .arg(fanucData.cnc_type)
                    .arg(fanucData.mt_type)
                    .arg(fanucData.series)
                    .arg(fanucData.version)
                    .arg(fanucData.axes);
            ui->lblFanucInfo->setText(cncInfo);
        }

        // Update UI
        ui->btnConnect->setText("Disconnect");

        bFanucConnected = true;

        return true;
    }
}

bool Widget::disconnectFanuc() {
    if (bFanucConnected && cnc_freelibhndl(libh) != EW_OK)
        fprintf(stderr, "Failed to free library handle!\n");
/*
#ifndef _WIN32
    cnc_exitprocess();
#endif
*/
    // Update UI
    ui->btnConnect->setText("Connect");
    bFanucConnected = false;
    return true;
}

bool Widget::isSignalServerConnected() {
    return signalReportClient.isConnected();
}

void Widget::connectSignalServer() {
    signalReportClient.disconnectServer();
    signalReportClient.connectToServer(MMS_RELAY_SERVER_IP, MMS_RELAY_SERVER_PORT);
    reconnectTryTimeSignalServer = getCurrentTimeMilis();
}

void Widget::on_btnReadMacros_clicked() {
    int ret = 0;
    // Read Macro Value
    short addrStart = 100;
    short addrEnd = 110;
    IODBMR *macror ;
    char strbuf[12] ;
    short idx ;
    macror = (IODBMR *)malloc( 1000 ) ;
    ret = cnc_rdmacror(libh, addrStart, addrEnd, 1000, macror);
    if ( !ret ) {
        QString macroValues = "";

        for ( idx = 0 ; idx <= addrEnd-addrStart ; idx++ ) {
            sprintf( &strbuf[0], "  %09ld", (long int)abs(macror->data[idx].mcr_val) );
            strncpy( &strbuf[1], &strbuf[2], 10 - macror->data[idx].dec_val ) ;
            strbuf[10-macror->data[idx].dec_val] = '.' ;

            if(macror->data[idx].dec_val <= 0)
                strbuf[strlen(strbuf)-1] = '\0';
            if(macror->data[idx].mcr_val < 0)
                strbuf[0] = '-';
            printf( "#%04ld %s\n", (long int)addrStart + idx, strbuf );

            macroValues += QString("%1 %2").arg(addrStart + idx, 4).arg(strbuf) + "\n";
        }

        ui->lblMacroVars->setText(macroValues);
    } else {
        printf( "ERROR!(%d)\n", ret ) ;
        ui->lblMacroVars->setText(QString("ERROR!(%1)\n").arg(ret));
    }

    free (macror);
}

void Widget::on_pushButton_clicked() {
    try {
        HPData hpData = spiReader.getHPData();
        float pow1 = hpData.m_hpA;
        float pow2 = hpData.m_hpB;
        float pow3 = hpData.m_hpC;

        printf( "POWER1 %f\n", pow1);
        printf( "POWER2 %f\n", pow2);
        printf( "POWER3 %f\n", pow3);

        QString spiValues = "";
        spiValues += QString("Pow1 %1\nPow2 %2\nPow3 %3").arg(pow1).arg(pow2).arg(pow3);
        ui->lblSPIVars->setText(spiValues);
    } catch (QException& e) {
        qDebug() << e.what();
    }
}

void Widget::on_sliderSP_valueChanged(int value) {
    ui->lblSPValue->setText(QString::number(value * 0.1));
    appSetting->setValue(SETTING_PID_SP, value * 0.1f);
}

void Widget::on_sliderPIDIntervals_valueChanged(int value) {
    ui->lblPIDIntervals->setText(QString::number(value) + " ms");
    appSetting->setValue(SETTING_PID_INTERVAL, value);
}

void Widget::on_btnPIDStartStop_clicked() {
    if (pidTimer.isActive()) {
        // PID is running, stop it
        pidTimer.stop();
        ui->btnPIDStartStop->setText("Start PID");
    } else {
        // PID is not running, play it

        // Check Fanuc Connection Status
        /*if (!bFanucConnected) {
            QMessageBox::warning(this, "PID", "Fanuc is not connected, please check connection!");
            return;
        }*/

        // Check PID Params
        bool convertStatus;
        QString strKp = ui->txtKp->text().trimmed();
        float kP_Val = strKp.toFloat(&convertStatus);
        if (!convertStatus) {
            QMessageBox::warning(this, "PID", "Please check Kp!");
            return;
        }
        appSetting->setValue(SETTING_PID_KP, kP_Val);

        QString strKi = ui->txtKi->text().trimmed();
        float kI_Val = strKi.toFloat(&convertStatus);
        if (!convertStatus) {
            QMessageBox::warning(this, "PID", "Please check Ki!");
            return;
        }
        appSetting->setValue(SETTING_PID_KI, kI_Val);

        QString strKd = ui->txtKd->text().trimmed();
        float kD_Val = strKd.toFloat(&convertStatus);
        if (!convertStatus) {
            QMessageBox::warning(this, "PID", "Please check Kd!");
            return;
        }
        appSetting->setValue(SETTING_PID_KD, kD_Val);


        // Check SPI address for reading the PV
        QString pidSPIAddr = ui->txtSPIAddr->text();
        int spi_Addr = pidSPIAddr.toInt(&convertStatus, 16);
        if (!convertStatus  || spi_Addr == 0) {
            QMessageBox::warning(this, "PID", "Please input the correct SPI Address!");
            return;
        } else {
            appSetting->setValue(SETTING_PID_PV_SPI_ADDR, spi_Addr);
        }

        // Save PID Output settings
        appSetting->setValue(SETTING_PID_OUT_REG_TYPE, ui->cbPIDOoutRegType->currentIndex());
        QString pidOutAddrString = ui->txtPIDOutAddr->text();
        int pidOutAddr = pidOutAddrString.toInt();

        if(pidOutAddr > 0) {
            appSetting->setValue(SETTING_PID_OUT_REG_ADDR, pidOutAddr);
        } else {
            QMessageBox::warning(this, "PID", "Please check PID Output Address!");
            return;
        }

        // Remove old and create new
        if (pidObj != NULL) {
            delete pidObj;
            pidObj = NULL;
        }
        pidObj = new PID(appSetting->getIntValue(SETTING_PID_INTERVAL) / 1000.0f,
                         20, 0,
                         appSetting->getFloatValue(SETTING_PID_KP),
                         appSetting->getFloatValue(SETTING_PID_KI),
                         appSetting->getFloatValue(SETTING_PID_KD));

        pidTimer.start(appSetting->getIntValue(SETTING_PID_INTERVAL));

        ui->btnPIDStartStop->setText("Stop PID");
    }
}

void Widget::handlePIDTimeout() {
    //qDebug() << "Timeout!";

    if (pidObj == NULL) {
        qDebug() << "No PID module inited!";
        return;
    }

    try {
        //float pvVal = commObj->readPower(appSetting->getIntValue(SETTING_PID_PV_SPI_ADDR));
        HPData hpData = spiReader.getHPData();
        float pA = hpData.m_hpA;
        float pB = hpData.m_hpB;
        float pC = hpData.m_hpC;
        float pvVal = hpData.m_hp;

        float spVal = appSetting->getFloatValue(SETTING_PID_SP);

        qDebug() << "PV:" << pvVal << "SP:" << spVal;

        if (pvVal < 0 || pvVal > 30) {
            qDebug() << "Peak!";
            return;
        }

        if(modbusTCPServer != NULL && modbusTCPServer->state() == QModbusDevice::ConnectedState) {
            QModbusDataUnit dataUnit(QModbusDataUnit::HoldingRegisters, 0, 2);

            /*
            FloatConverter converter;
            converter.floatValue = pvVal;
            QVector<quint16> dataVector;
            dataVector.append(converter.ushortArray[0]);
            dataVector.append(converter.ushortArray[1]);
            dataUnit.setValues(dataVector);
            */
            dataUnit.setValues(floatToRegisters(pvVal));
            modbusTCPServer->setData(dataUnit);
        }

        double inc = pidObj->calculate(spVal, pvVal);
        // Scale the value of 0~20 to 0~250
        double scaleValue = static_cast<double>(inc) / 20.0f;
        int ctrlValue = (int) (scaleValue * 250);
        ui->lblPIDOut->setText(QString::number(ctrlValue) + ", " + intToBinaryString(ctrlValue));

        QBitArray bits = intToBitArray(ctrlValue);
        char charPMCOut = static_cast<char>(ctrlValue);
        //charPMCOut = charPMCOut ^ 0xFF;

        QString fanucIP = appSetting->getStringValue(SETTING_FANUC_IP);
        int fanucPort = appSetting->getIntValue(SETTING_FANUC_PORT);

        int ret = 0;

#ifdef USE_SEPARATE_PID_HANDLE
        unsigned short libh;
        if ((ret = cnc_allclibhndl3(fanucIP.toStdString().c_str(), fanucPort, 5, &libh)) != EW_OK) {
            qDebug() << "PID" << QString("Error Connect: %1").arg(ret);
            return;
        }
#else
        if (!bFanucConnected) {

            // No connection with Fanuc
            qDebug() << "No Conn with Fanuc!";
            return;

            if (connectFanuc()) {
                // Update UI
                ui->btnConnect->setText("Disconnect");
            } else {
                // Couldn't connect
                qDebug() << "No Conn with Fanuc!";
                return;
            }
        }
#endif
        int address_s = appSetting->getIntValue(SETTING_PID_OUT_REG_ADDR);
        int bufCnt = 1;
        int address_e = address_s + bufCnt - 1; // We only read 1 register
        unsigned short length = 8 + address_e - address_s + 1;
        IODBPMC *pmcrng = (IODBPMC *) malloc(length);
        pmcrng->type_a = appSetting->getIntValue(SETTING_PID_OUT_REG_TYPE);
        pmcrng->type_d = 0;
        pmcrng->datano_s = address_s;
        pmcrng->datano_e = address_e;
        pmcrng->u.cdata[0] = charPMCOut;

        ret = pmc_wrpmcrng(libh, length, pmcrng);
        if (ret) {
            qDebug() << "PID" << "Error write!" << ret;
        }

        free (pmcrng);

#ifdef USE_SEPARATE_PID_HANDLE
        if (cnc_freelibhndl(libh) != EW_OK) {
            qDebug() << "PID" << "Failed to free library handle!";
        }
#endif
    } catch (QException& ex) {
        qDebug() << "Exception" << ex.what();
    }
}

void Widget::on_sliderFanucReadIntervals_valueChanged(int value) {
    ui->lblFanucReadIntervals->setText(QString::number(value) + " ms");
    appSetting->setValue(SETTING_FANUC_READ_INTERVAL, value);
}

void Widget::on_btnFanucReadStartStop_clicked() {
    if (csvTimer.isActive()) {
        // Read is running, stop it
        csvTimer.stop();
        ui->btnFanucReadStartStop->setText("Start");

        if (m_file.isOpen()) {
            m_file.close();
        }
    } else {
        bool convertStatus;
        QString strHPX = ui->txtHpX->text().trimmed();
        float hpX = strHPX.toFloat(&convertStatus);
        if (!convertStatus) {
            QMessageBox::warning(this, "HST", "Please check HP threshold value!");
            return;
        }
        appSetting->setValue(SETTING_FANUC_HP_X, hpX);

        // Check Fanuc Connection Status
        if (!bFanucConnected) {
            QMessageBox::warning(this, "HST", "Fanuc is not connected, please check connection!");
            return;
        }

        // Close Old csv File Stream
        if (m_file.isOpen()) {
            m_file.close();
        }

        // Create the new CSV file with a unique name
        QString fileName = generateUniqueFileName();
        m_file.setFileName(fileName);

        // Open the new CSV file in append mode
        if (!m_file.open(QIODevice::Append | QIODevice::Text)) {
            qDebug() << "Failed to create file!";
            QMessageBox::warning(this, "CSV", "Failed to create csv file!");
            return;
        }
        // Associate the QTextStream object with the new file
        m_stream.setDevice(&m_file);
        m_stream << "HP2,S1,S2,S3,T1,T2,T3,F1,F2,F3,M1,M2,M3,SeqNo" << "\n";

        csvTimer.start(appSetting->getIntValue(SETTING_FANUC_READ_INTERVAL));

        ui->btnFanucReadStartStop->setText("Stop");
    }
}

QString Widget::generateUniqueFileName() {
    QDateTime currentDateTime = QDateTime::currentDateTime();
    QString fileName = currentDateTime.toString("yyyy-MM-dd_hh-mm_ss") + ".csv";
    return fileName;
}

bool Widget::GetProgramInfo() {
    ODBPRO progInfo;
    int ret = 0;
    ret = cnc_rdprgnum(libh, &progInfo);
    if ( !ret ) {
        // Success to read
        fanucData.mainProgram = progInfo.data;
        fanucData.currProgram = progInfo.mdata;

        return true;
    } else {
        fanucData.mainProgram = "";
        fanucData.currProgram = "";

        return false;
    }
}

bool Widget::GetStateInfo() {
    ODBST stateInfo;
    int ret = 0;
    ret = cnc_statinfo(libh, &stateInfo);
    if ( !ret ) {
        // Success to read
        fanucData.motion = stateInfo.motion;
        fanucData.run = stateInfo.run;

        return true;
    } else {
        fanucData.motion = -1;
        fanucData.run = -1;

        return false;
    }
}

bool Widget::GetModalData() {
    // Read S Modal
    bool statusReadSModal = ReadModalValue(107, 0, &stfmData[0]);
    if (statusReadSModal) {
        ui->lblModalS->setText(QString("S Modal: %1, %2, %3").arg(stfmData[0].datano).arg(stfmData[0].type).arg(stfmData[0].auxData.intValue));
    } else {
        ui->lblModalS->setText(QString("S Modal: Error!"));
    }

    // Read T Modal
    bool statusReadTModal = ReadModalValue(108, 0, &stfmData[1]);
    if (statusReadTModal) {
        ui->lblModalT->setText(QString("T Modal: %1, %2, %3").arg(stfmData[1].datano).arg(stfmData[1].type).arg(stfmData[1].auxData.intValue));
    } else {
        ui->lblModalT->setText(QString("T Modal: Error!"));
    }

    // Read F Modal
    bool statusReadFModal = ReadModalValue(103, 0, &stfmData[2], true);
    if (statusReadFModal) {
        ui->lblModalF->setText(QString("F Modal: %1, %2, %3").arg(stfmData[2].datano).arg(stfmData[2].type).arg(stfmData[2].auxData.floatValue));
    } else {
        ui->lblModalF->setText(QString("F Modal: Error!"));
    }

    // Read M Modal
    bool statusReadMModal = ReadModalValue(106, 0, &stfmData[3]);
    if (statusReadMModal) {
        ui->lblModalM->setText(QString("M Modal: %1, %2, %3").arg(stfmData[3].datano).arg(stfmData[3].type).arg(stfmData[3].auxData.intValue));
    } else {
        ui->lblModalM->setText(QString("M Modal: Error!"));
    }

    return statusReadSModal &
            statusReadTModal &
            statusReadFModal &
            statusReadMModal;
}

bool Widget::ReadModalValue(int type, int block, STFMData *pModal, bool fCode) {

    // Check Parameters
    if (type == -1 || block == -1) {
        pModal->datano = 0;
        pModal->type = 0;
        pModal->auxData.intValue = 0;
        return false;
    }

    // Check Fanuc Connection Status
    if (!bFanucConnected) {
        return false;
    }

    // Read Modal Value
    bool bRet = false;
    int ret = 0;
    ODBMDL *modal ;
    modal = (ODBMDL *)malloc( 256 ) ;
    ret = cnc_modal(libh, type, block, modal);
    if ( !ret ) {
        bRet = true;

        if (fCode) {
            pModal->datano = pModal->datano;
            pModal->type = pModal->type;

            float gData = modal->modal.aux.aux_data;
            if ((modal->modal.aux.flag1 & 0x40) > 0) {
                gData = 1.0f * gData / qPow(10, modal->modal.aux.flag2);
            }

            pModal->auxData.floatValue = gData;
        } else {
            pModal->datano = pModal->datano;
            pModal->type = pModal->type;
            pModal->auxData.intValue = modal->modal.aux.aux_data;
        }
    } else {
        bRet = false;

        pModal->datano = -1;
        pModal->type = -1;
        pModal->auxData.intValue = ret;

        qDebug() << "Read Modal Error:" << type << ret;
    }

    free (modal);

    return bRet;
}

bool Widget::GetSequenceNumber() {
    // Check Fanuc Connection
    if (!bFanucConnected) {
        return false;
    }

    // Read Sequence Number
    ODBSEQ buf;
    int ret = 0;
    ret = cnc_rdseqnum(libh, &buf);
    if ( !ret ) {
        // Success to read
        currSequenceNum = buf.data;
        ui->lblCurrSeqNum->setText(QString("Current Sequence Number: %1").arg(currSequenceNum));

        fanucData.currSequenceNum = buf.data;
        return true;
    } else {
        // Failed to read
        currSequenceNum = 0;
        ui->lblCurrSeqNum->setText(QString("Current Sequence Number: Error(%1)").arg(ret));

        fanucData.currSequenceNum = 0;
        return false;
    }
}

void Widget::handleCSVFileTimeout() {
    try {
        // Read Values
        bool statusModal = GetModalData();
        bool statusSeqNum = GetSequenceNumber();

        // Get SPI value
        float hp2 = spiReader.getHPData().m_hp;

        // Check Data Validation
        if (statusModal && statusSeqNum && hp2 >= appSetting->getFloatValue(SETTING_FANUC_HP_X)) {
            // Make CSV Line Data : "HP2,S1,S2,S3,T1,T2,T3,F1,F2,F3,M1,M2,M3,SeqNo"
            QString lineData = QString("%1,%2,%3,%4,%5,%6,%7,%8,%9,%10,%11,%12,%13,%14")
                    .arg(hp2)
                    .arg(stfmData[0].datano).arg(stfmData[0].type).arg(stfmData[0].auxData.intValue)
                    .arg(stfmData[1].datano).arg(stfmData[1].type).arg(stfmData[1].auxData.intValue)
                    .arg(stfmData[2].datano).arg(stfmData[2].type).arg(stfmData[2].auxData.floatValue)
                    .arg(stfmData[3].datano).arg(stfmData[3].type).arg(stfmData[3].auxData.intValue)
                    .arg(currSequenceNum);
            // Print to the CSV file
            m_stream << lineData << "\n";
        }
    } catch (QException& ex) {
        qDebug() << ex.what();
    }
}

void Widget::handleMainTimeout() {
    try {
        qint64 currentTimeMilis = getCurrentTimeMilis();
        QDateTime currentDateTime = QDateTime::currentDateTime();

        // Check connection with signal relay server and try to reconnect if not connected.
        if (!isSignalServerConnected() &&
                qAbs(currentTimeMilis - reconnectTryTimeSignalServer) >= CYCLE_MILIS_30SECS) {
            connectSignalServer();
        }

        if (bFanucConnected) {
            // Read Values
            bool statusProg = GetProgramInfo();
            bool statusState = GetStateInfo();

            bool ret = statusProg & statusState;

            if (ret == false) {
                disconnectFanuc();
            }
        } else {
            fanucData.run = -1;
            //fanucData.spindleSpeed = 0;
        }

        // Check FEED FOLD Status
        bool feedFoldStatus = false;
        if (fanucData.cnc_type == "15") {
            // Series 15/15i
            // 1: HOLD
            if (fanucData.run == 1 || fanucData.run == 0) {
                feedFoldStatus = true;
            }
        } else if (fanucData.mt_type == "W" && (fanucData.cnc_type == "16" || fanucData.cnc_type == "18")) {
            // Series 16i/18i-W
            // 3: F-HOLD
            if (fanucData.run == 3 || fanucData.run == 4) {
                feedFoldStatus = true;
            }
        } else if (fanucData.cnc_type == "16" ||
                   fanucData.cnc_type == "18" ||
                   fanucData.cnc_type == "21" ||
                   fanucData.cnc_type == "0" ||
                   fanucData.cnc_type == "30" ||
                   fanucData.cnc_type == "31" ||
                   fanucData.cnc_type == "32" ||
                   fanucData.cnc_type == "PH") {
            // Series 16/18/21, 16i/18i/21i, 0i, 30i/31i/32i, Power Mate i, PMi-A, odbst
            // 2: HOLD
            if (fanucData.run == 2 || fanucData.run == 1) {
                feedFoldStatus = true;
            }
        } else {
            // No case
        }

        // Show Current Fanuc Information
        QString machineInfo = QString("RUN STATUS: %1,%2\nFeed Fold: %3\nMain Prog: %4\nCurr Prog: %5")
                .arg(fanucData.run)
                .arg(fanucData.motion)
                .arg(feedFoldStatus ? "ON" : "OFF")
                .arg(fanucData.mainProgram)
                .arg(fanucData.currProgram);
        ui->lblMachineInfo->setText(machineInfo);

        // This is for Test
        if (ui->chkTempInCycle->isChecked()) {
            fanucData.run = 3;
        }

        // Report Current Signal
        if (isSignalServerConnected()) {
            // New Signal or timeout for report
            if(fanucData.prevRun != fanucData.run ||
                    qAbs(currentTimeMilis - reportTimeSignal) >= CYCLE_MILIS_1MIN) {
                QString customerId = appSetting->getStringValue(SETTING_MMS_CUSTOMERID);
                QString machineName = appSetting->getStringValue(SETTING_MMS_MACHINENAME);

                if (!customerId.isEmpty() && !machineName.isEmpty()) {
                    QString createdAt = currentDateTime.toString("yyyy-MM-dd HH:mm:ss.zzz");
                    QString timeStamp = currentDateTime.toString("yyyy-MM-dd HH:mm:ss");

                    QString segSignal = QString("signal|||%1,%2,%3,%4,%5,%6,%7(%8)")
                            .arg(customerId)
                            .arg(machineName)
                            .arg(fanucData.run == 3 ? "INCYCLE" : "UNCATE")
                            .arg(currentTimeMilis)
                            .arg(createdAt)
                            .arg(timeStamp)
                            .arg(APP_NAME)
                            .arg(APP_VERSION);

                    qDebug()<< segSignal;

                    QString segRequestSignal = QString("requestSignal|||%1,%2")
                            .arg(customerId)
                            .arg(machineName);

                    signalReportClient.sendMessage(segSignal);
                    signalReportClient.sendMessage(segRequestSignal);

                    reportTimeSignal = currentTimeMilis;
                    fanucData.prevRun = fanucData.run;
                }
            }
        }

        // Check Fanuc Auto Reconnect Logic
        if (!bFanucConnected && bNeedsAutoConnect) {
            if (qAbs(currentTimeMilis - reconnectTryTimeFanuc) >= CYCLE_MILIS_1MIN/*CYCLE_MILIS_10SECS*/) {
                connectFanuc(false);
            }
        }

        // Check Random value to the GlG Clients
        double randomValue = QRandomGenerator::global()->generateDouble();
        randomValue *= 2.0;
        //glgDataServer.sendGLGDataToClients(randomValue);
        glgDataServer.sendGLGDataToClients(QString::number(randomValue));

    } catch (QException& ex) {
        qDebug() << ex.what();
    }
}
