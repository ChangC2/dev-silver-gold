#ifndef FANUCHTTPSERVER_H
#define FANUCHTTPSERVER_H

#include <QObject>
#include <QtCore>
#include <QtNetwork>

class FanucHttpServer : public QObject {
    Q_OBJECT

public:

    //FanucHttpServer();

    explicit FanucHttpServer(QObject* parent = nullptr) : QObject(parent) {
        server = new QTcpServer(this);

        connect(server, &QTcpServer::newConnection, this, &FanucHttpServer::onNewConnection);
    }

    ~FanucHttpServer() {
        delete server;
    }

    void start(int port);

    QByteArray getFileContent(QString);

private slots:
    void onNewConnection();
    void onReadyRead();

    QByteArray generateResponse(const QByteArray& requestData);

private:
    QTcpServer* server;
};

#endif // FANUCHTTPSERVER_H
