#ifndef WORKER_H
#define WORKER_H

#include <QObject>
#include <QTimer>
#include <QException>
#include <QDebug>
#include <QSerialPort>
#include <QSerialPortInfo>

#include "appsettings.h"

class Worker : public QObject {
    Q_OBJECT

public:
    explicit Worker(QObject *parent = nullptr);
    ~Worker();

signals:
    void resultReady();

public slots:
    void handleMainTimeout();

private:
    // Storage for read/writing the settings
    AppSettings *appSetting;
    // Fanuc Connect Status and Fanuc Handler
    unsigned short libh;
    int tryConnectionCounter = 0;
    QSerialPort serial;
    bool isReadingSerial = false;
    bool isWritingSerial = false;

private:
    bool connectFanuc();
    bool disconnectFanuc();
    int readMacro( short number );
    int readMacro2( short number );

    void readFromSerial();
    void writeToSerial();


public:
    bool bFanucConnected = false;
    int elapsedRunTime = 0;
    int systemStatus = 0, newStatus = 0, actualPressure1 = 0, actualPressure2 = 0, feedPump = 0, topFloat = 0, bottomFloat = 0,
    filterPressure1 = 0, filterPressure2 = 0, port = 0, pressureSetPoint1 = 0,  pressureSetPoint2 = 0;

public:
    void writeMacro( short number, short value );
    void writeMacro2( short number, short value );
};

#endif // WORKER_H
