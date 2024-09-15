#ifndef SPIVALUEREADER_H
#define SPIVALUEREADER_H

#include <QThread>
#include "spiComm.h"
#include "Global.h"

class SPIValueReader : public QThread
{
public:
    SPIValueReader();
    ~SPIValueReader();

    HPData getHPData();
    void readHPData();

protected:
    void run() override;

private:

    HPData m_hpData;

    spiComm* commObj;
};

#endif // SPIVALUEREADER_H
