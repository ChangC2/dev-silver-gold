#include "spivaluereader.h"
#include <QDebug>
#include <QException>

SPIValueReader::SPIValueReader() {
    m_hpData.m_hpA = 0;
    m_hpData.m_hpB = 0;
    m_hpData.m_hpC = 0;
    m_hpData.m_hp = 0;

    commObj = NULL;
}

SPIValueReader::~SPIValueReader() {
    // Remove SPI Interface
    if (commObj != NULL) {
        delete commObj;
    }
}

HPData SPIValueReader::getHPData() {
    return m_hpData;
}

void SPIValueReader::readHPData() {

    try {
        if (commObj == NULL) {
            qDebug() << "SPI Reading started2!";
            commObj = new spiComm();
        }

        //float pvVal = commObj->readPower(appSetting->getIntValue(SETTING_PID_PV_SPI_ADDR));
        float pA = commObj->readPower(R_AWATT_REGISTER);
        float pB = commObj->readPower(R_BWATT_REGISTER);
        float pC = commObj->readPower(R_CWATT_REGISTER);
        float pvVal = (pA + pB + pC) / 745.7;

        if (pvVal < 0 || pvVal > 30) {
            qDebug() << "Peak!" << pvVal;
        } else {
            m_hpData.m_hpA = pA;
            m_hpData.m_hpB = pB;
            m_hpData.m_hpC = pC;

            m_hpData.m_hp = pvVal;

            //qDebug() << "HP:" << pvVal;
        }
    } catch (QException& ex) {
        qDebug() << "Exception" << ex.what();
    }
}

void SPIValueReader::run() {
    while (!isInterruptionRequested()) {

        readHPData();

        sleep(10);
    }
}
