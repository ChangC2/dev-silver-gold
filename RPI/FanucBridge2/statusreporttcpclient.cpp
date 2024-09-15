#include "statusreporttcpclient.h"

// InCycle/UnCate Status Reporter TCP Client
// App use this object to report the Fanuc machine status changes
StatusReportTCPClient::StatusReportTCPClient(QObject *parent) : QObject(parent),
    m_socket(nullptr),
    m_isConnected(false){
}

void StatusReportTCPClient::connectToServer(const QString& ipAddress, quint16 port) {
    m_socket = new QTcpSocket(this);

    connect(m_socket, &QTcpSocket::connected, this, &StatusReportTCPClient::handleConnected);
    connect(m_socket, &QTcpSocket::disconnected, this, &StatusReportTCPClient::handleDisconnected);
    connect(m_socket, &QTcpSocket::errorOccurred, this, &StatusReportTCPClient::handleError);
    connect(m_socket, &QTcpSocket::readyRead, this, &StatusReportTCPClient::handleReadyRead);

    m_socket->connectToHost(ipAddress, port);
}

void StatusReportTCPClient::disconnectServer() {
    if (m_socket != NULL) {
        m_socket->disconnect();
        m_socket->close();
        delete m_socket;
        m_socket = NULL;
    }
}

bool StatusReportTCPClient::isConnected() {
    //return m_isConnected;
    if (m_socket && m_socket->state() == QAbstractSocket::ConnectedState) {
        return true;
    } else {
        return false;
    }
}

void StatusReportTCPClient::sendMessage(const QString& message) {
    if (m_socket && m_socket->state() == QAbstractSocket::ConnectedState) {
        m_socket->write(message.toUtf8());
        m_socket->flush();
        m_socket->waitForBytesWritten(500);
    }
}

void StatusReportTCPClient::handleConnected() {
    m_isConnected = true;
    emit connected();
}

void StatusReportTCPClient::handleDisconnected() {
    m_isConnected = false;
    emit disconnected();
}

void StatusReportTCPClient::handleError(QAbstractSocket::SocketError socketError) {
    emit error(socketError);
}

void StatusReportTCPClient::handleReadyRead() {
    QByteArray data = m_socket->readAll();
    QString message(data);
    emit messageReceived(message);
}
