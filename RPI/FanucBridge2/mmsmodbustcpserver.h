#ifndef MMSMODBUSTCPSERVER_H
#define MMSMODBUSTCPSERVER_H

#include <QModbusTcpServer>

class MMSModbusTcpServer: public QModbusTcpServer {
public:
    MMSModbusTcpServer(QObject* parent);

    void closeServer();
};

#endif // MMSMODBUSTCPSERVER_H
