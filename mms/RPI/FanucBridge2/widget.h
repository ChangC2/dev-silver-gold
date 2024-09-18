#ifndef WIDGET_H
#define WIDGET_H

#include <QWidget>
#include <QTimer>
#include <QFile>
#include <QTextStream>

#include "mmsmodbustcpserver.h"
#include "appsettings.h"
#include "spiComm.h"
#include "pid.h"
#include "spivaluereader.h"
#include "statusreporttcpclient.h"
#include "glgdataserver.h"

QT_BEGIN_NAMESPACE
namespace Ui { class Widget; }
QT_END_NAMESPACE

class Widget : public QWidget
{
    Q_OBJECT

public:
    Widget(QWidget *parent = nullptr);
    ~Widget();

protected:
    void closeEvent(QCloseEvent *event) override;

private slots:
    void handleDataWritten(QModbusDataUnit::RegisterType dataType, int address, int size);

    void on_btnConnect_clicked();

    void on_btnReadMacros_clicked();

    //void handleRequest(QHttpRequest* request, HttpResponse* response);

    void on_pushButton_clicked();

    void on_sliderSP_valueChanged(int value);

    void on_sliderPIDIntervals_valueChanged(int value);

    void on_btnPIDStartStop_clicked();

    void on_sliderFanucReadIntervals_valueChanged(int value);
    void on_btnFanucReadStartStop_clicked();


    // Timer Handlers
    void handleMainTimeout();
    void handlePIDTimeout();
    void handleCSVFileTimeout();

private:
    void quitApp();

    bool connectFanuc(bool isInteractiveMode = false);
    bool disconnectFanuc();

    bool isSignalServerConnected();
    void connectSignalServer();

    float getPID_PV();
    float getPID_SP();

    bool GetProgramInfo();
    bool GetStateInfo();
    bool GetModalData();
    bool ReadModalValue(int type, int block, STFMData *pModal, bool fCode = false);

    bool GetSequenceNumber();
    QString generateUniqueFileName();


private:
    Ui::Widget *ui;

    // Placeholder for saving the current fanuc machine data
    FanucData fanucData;

    // Storage for read/writing the settings
    AppSettings *appSetting;

    // InCycle/UnCat Signal Report Server
    StatusReportTCPClient signalReportClient;
    qint64 reconnectTryTimeSignalServer = 0;
    qint64 reportTimeSignal = 0;

    // GLG Data Server
    GlgDataServer glgDataServer;

    // Modbus Server for providing the HP2 to TOSV2
    MMSModbusTcpServer *modbusTCPServer;

    // Fanuc Connect Status and Fanuc Handler
    bool bFanucConnected = false;
    unsigned short libh;

    // Auto connect logic
    bool bNeedsAutoConnect = true;
    qint64 reconnectTryTimeFanuc = 0;

    QTimer pidTimer;
    SPIValueReader spiReader;
    PID* pidObj;

    // Main Timer
    QTimer mainTimer;

    // CSV Report Timer
    QTimer csvTimer;

    // S, T, F, M Data
    STFMData stfmData[4];

    // Current Sequence Number
    long currSequenceNum = 0;

    // CSV File Reporter
    QFile m_file;
    QTextStream m_stream;

};
#endif // WIDGET_H
