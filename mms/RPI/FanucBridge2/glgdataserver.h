#ifndef GLGDATASERVER_H
#define GLGDATASERVER_H

#include <QObject>
#include <QList>

//#include <QtNetwork>
#include <QtWebSockets/QWebSocketServer>
#include <QtWebSockets/QWebSocket>

class GlgDataServer : public QObject
{
    Q_OBJECT
public:
    explicit GlgDataServer(QObject *parent = nullptr);

    // Start to listen connection request
    void startServer(int port);

    // send glg plot to the clients
    void sendGLGDataToClients(const QString& message);

private slots:
    void handleNewConnection();
    void readData(const QString& message);
    void handleClientDisconnected();

private:
    // QTcpServer Mode
    //QTcpServer *tcpServer;
    //QList<QTcpSocket*> clientSockets;

    QWebSocketServer *webSocketServer;
    QList<QWebSocket*> clientSockets;

};

#endif // GLGDATASERVER_H
