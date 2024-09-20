#include "worker.h"
#include "Global.h"
#include "apputils.h"
#include "include/fwlib32.h"

#include <QString>
#include <QMessageBox>
#include <QDebug>
#include <QtMath>


Worker::Worker(QObject *parent) : QObject(parent) {
    appSetting = new AppSettings();
    // Configure your specific settings
    serial.setPortName("/dev/ttyAMA0"); // Change this to your serial device
    serial.setBaudRate(QSerialPort::Baud9600);
    serial.setDataBits(QSerialPort::Data8);
    serial.setParity(QSerialPort::NoParity);
    serial.setStopBits(QSerialPort::OneStop);
    serial.setFlowControl(QSerialPort::NoFlowControl);
    if (!serial.isOpen()){
        serial.open(QIODevice::ReadWrite);
    }
}

Worker::~Worker()
{
    if (bFanucConnected) {
        disconnectFanuc();
    }
    if (serial.isOpen()){
        serial.close();
    }
}
void Worker::handleMainTimeout() {
    if (systemStatus == 1 && newStatus == 1){
        elapsedRunTime++;
    }else if (newStatus == 1){
        elapsedRunTime = 0;
    }
    systemStatus = newStatus;

    try {
        if (appSetting->getIntValue(SETTING_FANUC_ENABLE, 0) == 0){
            if (bFanucConnected) {
                disconnectFanuc();
            }
        }else{
            if (bFanucConnected) {
                pressureSetPoint1 =  readMacro(appSetting->getIntValue(SETTING_PRESSURE_ADDRESS1, 0));
                pressureSetPoint1 = pressureSetPoint1 > 1000 ? 1000 : pressureSetPoint1;
                pressureSetPoint1 = pressureSetPoint1 < 100 ? 100 : pressureSetPoint1;
                pressureSetPoint2 =  readMacro(appSetting->getIntValue(SETTING_PRESSURE_ADDRESS2, 0));
                pressureSetPoint2 = pressureSetPoint2 > 1000 ? 1000 : pressureSetPoint2;
                pressureSetPoint2 = pressureSetPoint2 < 100 ? 100 : pressureSetPoint2;
                port =  readMacro(appSetting->getIntValue(SETTING_PORT_ADDRESS, 0));
            }else{
                if (tryConnectionCounter == 0){
                    tryConnectionCounter = 10;
                    bFanucConnected = connectFanuc();
                }else{
                    tryConnectionCounter--;
                }
            }
        }
    } catch (QException& ex) { }

    try {
        if (!isReadingSerial && !isWritingSerial){
            readFromSerial();
        }
    }  catch (QException& ex) { }

    emit resultReady();
}


int Worker::readMacro( short number )
{
    try {
        ODBM macro ;
        char strbuf[12] ;
        short ret ;
        ret = cnc_rdmacro(libh, number, 10, &macro ) ;
        if ( !ret ) {
            sprintf( &strbuf[0], "  %09ld", abs(macro.mcr_val) );
            //qDebug() << QString("Read Macro: %1, %2").arg(macro.mcr_val).arg(macro.dec_val);
            strncpy( &strbuf[1], &strbuf[2], 10 - macro.dec_val ) ;
            strbuf[10-macro.dec_val] = '.' ;
            if(macro.dec_val <= 0) strbuf[strlen(strbuf)-1] = '\0';
            if(macro.mcr_val < 0) strbuf[0] = '-';
            QString strVal = QString::fromUtf8(strbuf);
            bool convertOk;
            int doubleVal =(int) strVal.replace(" ", "").replace("\"","").toDouble(&convertOk);
            //qDebug() << QString("Read Macro: %1.").arg(doubleVal);
            return convertOk ? doubleVal : 0 ;
        }
        else{
            bFanucConnected = connectFanuc();
            return 0 ;
        }

    } catch (QException& ex) {
        bFanucConnected = connectFanuc();
        return 0 ;
    }
}

int Worker::readMacro2( short number )
{
    IODBMR *macror ;
    char strbuf[12] ;
    short ret ;
    int doubleVal = 0;
    macror = (IODBMR *)malloc( 1000 ) ;
    ret = cnc_rdmacror(libh, number, number, 1000, macror ) ;
    if ( !ret ){
        sprintf( &strbuf[0], "  %09ld",
                abs(macror->data[0].mcr_val) );
        strncpy( &strbuf[1], &strbuf[2],
                10 - macror->data[0].dec_val ) ;
        strbuf[10-macror->data[0].dec_val] = '.' ;
        if(macror->data[0].dec_val <= 0)
            strbuf[strlen(strbuf)-1] = '\0';
        if(macror->data[0].mcr_val < 0) strbuf[0] = '-';
        //        qDebug() << QString("Read2 Macro: %1, %2").arg(macror->data[0].mcr_val).arg(macror->data[0].dec_val);
        QString strVal = QString::fromUtf8(strbuf);
        bool convertOk;
        doubleVal =(int) strVal.replace(" ", "").replace("\"","").toDouble(&convertOk);
        doubleVal =  convertOk ? doubleVal : 0 ;
        //        qDebug() << QString("Read2 Macro: %1.").arg(doubleVal);
        doubleVal = doubleVal > 1000 ? 1000 : doubleVal;
        doubleVal = doubleVal < 100 ? 100 : doubleVal;
    }
    else
        printf( "ERROR!(%d)\n", ret ) ;
    free( macror ) ;
    return doubleVal ;
}


void Worker::writeMacro(short number, short value)
{
    QString ipAddr = appSetting->getStringValue(SETTING_FANUC_IP);
    int port = appSetting->getIntValue(SETTING_FANUC_PORT);
    if ((cnc_allclibhndl3(ipAddr.toStdString().c_str(), port, 5, &libh)) != EW_OK) {
        return;
    }
    short dec_val = 0;
    long mcr_val = value * qPow(10, dec_val);
    try {
        int ret = cnc_wrmacro( libh, number, 10, mcr_val, dec_val);
        qDebug() << QString("Write Macro %1, %2, %3").arg(mcr_val).arg(dec_val).arg(ret);
    } catch (QException& ex) {
        qDebug() << "Reconnect on exception" ;
        bFanucConnected = connectFanuc(); // Reconnect on exceptionx
    }
}

void Worker::writeMacro2(short number, short value)
{
    QString ipAddr = appSetting->getStringValue(SETTING_FANUC_IP);
    int port = appSetting->getIntValue(SETTING_FANUC_PORT);
    if ((cnc_allclibhndl3(ipAddr.toStdString().c_str(), port, 5, &libh)) != EW_OK) {
        return;
    }
    IODBMR *macror ;
    short ret;
    macror = (IODBMR *)malloc( 8+8*number ) ;
    macror->datano_s = number ;
    macror->datano_e = number;
    short dec_val = 0;
    macror->data[0].mcr_val = value * qPow(10, dec_val) ;
    macror->data[0].dec_val = dec_val ;
    ret = cnc_wrmacror( libh, 8+8*1, macror ) ;
    qDebug() << QString("Write Macro %1, %2, %3").arg(macror->data[0].mcr_val).arg(dec_val).arg(ret);
    free( macror ) ;
}



bool Worker::connectFanuc() {
    QString ipAddr = appSetting->getStringValue(SETTING_FANUC_IP);
    int port = appSetting->getIntValue(SETTING_FANUC_PORT);


    // Init Fanuc ------------------------------------------------------------------------------
#ifndef _WIN32
    int ret = cnc_startupprocess(0, "focas.log"); //EW_OK = 0, no problem
    if (ret != EW_OK) {
        return false;
    }
#endif
    // ----------------------------------------------------------------------------------------/
    if ((ret = cnc_allclibhndl3(ipAddr.toStdString().c_str(), port, 5, &libh)) != EW_OK) {
        //        qDebug() << QString("Failed to connect: %1. Will try to connect after 10 seconds").arg(ret);
        return false;
    }
    tryConnectionCounter = 0;
    return true;
}

bool Worker::disconnectFanuc() {
    bFanucConnected = false;
    if (cnc_freelibhndl(libh) != EW_OK){
        return false;
    }
    return true;
}


void Worker::readFromSerial()
{
    if (!serial.isOpen()){
        serial.open(QIODevice::ReadWrite);
    }
    isReadingSerial = true;
    QByteArray readData = serial.readAll();
    while (serial.waitForReadyRead(100))
        readData.append(serial.readAll());

    if (serial.error() == QSerialPort::ReadError) {
        qDebug() << "Failed to read from port:" << serial.errorString();
    } else if (serial.error() == QSerialPort::TimeoutError && readData.isEmpty()) {
        qDebug() << "No data was currently available for reading from port" << serial.portName();
    }else{
        QString plcData = QString::fromUtf8(readData);
        qDebug() << "ReadFromSerial:" << plcData;
        plcData = plcData.replace(" ", "");
        QStringList list = plcData.split(",");
        if (list.size() > 7){
            newStatus = list.at(0).toInt();
            actualPressure1 = list.at(1).toInt();
            actualPressure2 = list.at(2).toInt();
            feedPump = list.at(3).toInt();
            topFloat = list.at(4).toInt();
            bottomFloat = list.at(5).toInt();
            filterPressure1 = list.at(6).toInt();
            filterPressure2 = list.at(7).toInt();
            writeToSerial();
        }
    }
    isReadingSerial = false;
}

void Worker::writeToSerial() {
    if (!serial.isOpen()){
        serial.open(QIODevice::ReadWrite);
    }
    isWritingSerial = true;
    QString serialSettings = QString("%1,%2,%3")
            .arg(pressureSetPoint1)
            .arg(pressureSetPoint2)
            .arg(port);
    qDebug() << "WriteToSerial:" << serialSettings;
    QByteArray writeData = serialSettings.toUtf8();
    qint64 bytesWritten = serial.write(writeData);

    if (bytesWritten == -1) {
        qDebug() << "Failed to write data:" << serial.errorString();
    } else if (bytesWritten != writeData.size()) {
        qDebug() << "Failed to write all the data to port." << serial.errorString();
    } else if (!serial.waitForBytesWritten(5000)) {
        qDebug() << "Timeout when writing data:" << serial.errorString();
    }
    isWritingSerial = false;
}
