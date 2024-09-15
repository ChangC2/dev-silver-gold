#ifndef GLOBAL_H
#define GLOBAL_H

#define APP_NAME "TOSV3"
#define APP_VERSION "3.9.14"

// Gloal Settings KEY
#define SETTING_FANUC_IP "fanuc_ip"
#define SETTING_FANUC_PORT "fanuc_port"

#define SETTING_MMS_CUSTOMERID "mms_customer_id"
#define SETTING_MMS_MACHINENAME "mms_machine_name"

#define SETTING_PID_KP "pid_kp"
#define SETTING_PID_KI "pid_ki"
#define SETTING_PID_KD "pid_kd"

#define SETTING_PID_SP "pid_sp"
#define SETTING_PID_INTERVAL "pid_interval"
#define SETTING_PID_OUT_REG_TYPE "pid_outreg_type"
#define SETTING_PID_OUT_REG_ADDR "pid_outreg_addr"
#define SETTING_PID_PV_SPI_ADDR "pid_pvspi_addr"

#define SETTING_FANUC_READ_INTERVAL "fanuc_read_interval"
#define SETTING_FANUC_HP_X "fanuc_hp_x"

#include <QString>
#include <QBitArray>
#include <QtCore/qvector.h>
#include <QtCore/qglobal.h>

struct HPData {
    float m_hpA;
    float m_hpB;
    float m_hpC;

    float m_hp;

    HPData(): m_hpA(0), m_hpB(0), m_hpC(0), m_hp(0) {}
};

union AUX_DATAHolder {
    float floatValue;
    int intValue;
};

struct STFMData {
    short datano;
    short type;
    AUX_DATAHolder auxData;

    STFMData(): datano(0), type(0) {
        auxData.floatValue = 0;
    }
    STFMData(int dn, int tp, int ad) {
        datano = dn;
        type = tp;
        auxData.floatValue = ad;
    }
};

struct FanucData {
    QString mainProgram;
    QString currProgram;

    int prevRun;    // Hold previous run status to detect new Machine Status

    int motion;
    int run;

    int currSequenceNum;

    short addInfo;
    short max_axis;
    QString cnc_type;
    QString mt_type;
    QString series;
    QString version;
    QString axes;

    FanucData() {
        mainProgram = "";
        currProgram = "";

        prevRun = 0;

        motion = 0;
        run = 0;

        currSequenceNum = 0;

        addInfo = 0;
        max_axis = 0;
        cnc_type = "";
        mt_type = "";
        series = "";
        version = "";
        axes = "";
    }
};

QString intToBinaryString(int value);
QBitArray intToBitArray(int value);

union FloatConverter {
    float floatValue;
    quint16 ushortArray[2];
};

QVector<quint16> floatToRegisters(float value);
float registersToFloat(const QVector<quint16>& registers);
qint64 getCurrentTimeMilis();

#endif // GLOBAL_H




