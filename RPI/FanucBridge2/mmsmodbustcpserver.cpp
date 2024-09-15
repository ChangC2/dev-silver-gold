#include "mmsmodbustcpserver.h"

MMSModbusTcpServer::MMSModbusTcpServer(QObject* parent) : QModbusTcpServer(parent) {
}

void MMSModbusTcpServer::closeServer() {
    disconnectDevice();
    close();
}
