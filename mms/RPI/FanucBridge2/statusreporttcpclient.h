#ifndef STATUSREPORTTCPCLIENT_H
#define STATUSREPORTTCPCLIENT_H

#include <QObject>
#include <QTcpSocket>

// InCycle/UnCate Status Reporter TCP Client
// App use this object to report the Fanuc machine status changes

class StatusReportTCPClient : public QObject
{
    Q_OBJECT

public:
    explicit StatusReportTCPClient(QObject *parent = nullptr);
    void connectToServer(const QString& ipAddress, quint16 port);
    void disconnectServer();
    bool isConnected();

signals:
    void connected();
    void disconnected();
    void error(QTcpSocket::SocketError socketError);
    void messageReceived(QString message);

public slots:
    void sendMessage(const QString& message);

private slots:
    void handleConnected();
    void handleDisconnected();
    void handleError(QAbstractSocket::SocketError socketError);
    void handleReadyRead();

private:
    QTcpSocket* m_socket;

    bool m_isConnected;
};

#endif // STATUSREPORTTCPCLIENT_H
