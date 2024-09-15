#include "glgdataserver.h"
#include <QDebug>

GlgDataServer::GlgDataServer(QObject *parent) : QObject(parent) {
    /*
    // Create a server instance
    tcpServer = new QTcpServer(this);
    // Connect the newConnection signal to a slot
    connect(tcpServer, &QTcpServer::newConnection, this, &GlgDataServer::handleNewConnection);
    */

    webSocketServer = new QWebSocketServer("TOS3GLGDataServer", QWebSocketServer::NonSecureMode, this);
    connect(webSocketServer, &QWebSocketServer::newConnection, this, &GlgDataServer::handleNewConnection);
}

void GlgDataServer::startServer(int port) {
    // Start listening on a specific port
    /*if (tcpServer->listen(QHostAddress::Any, port)) {
        qDebug() << "GLG Data Server started, Listening on port " << port;
    } else {
        qDebug() << "GLG Data Server failed to start!";
    }*/

    if (webSocketServer->listen(QHostAddress::Any, port)) {
        qDebug() << "GLG Data Server started, Listening on port " << port;
    } else {
        qDebug() << "GLG Data Server failed to start!";
    }
}

void GlgDataServer::sendGLGDataToClients(const QString &message) {
    // Send a message to all connected clients
    /*
    for (auto& socket : clientSockets) {
        socket->write(message.toUtf8());
        socket->waitForBytesWritten();
    }
    */
    for (auto& socket : clientSockets) {
        socket->sendTextMessage(message.toUtf8());
    }
}

void GlgDataServer::handleNewConnection() {

    /*
    qDebug() << "New connection!";

    // Accept incoming connection
    QTcpSocket *clientSocket = tcpServer->nextPendingConnection();

    // Read data from the client
    connect(clientSocket, &QTcpSocket::readyRead, this, &GlgDataServer::readData);

    // Disconnect the client once it is disconnected
    connect(clientSocket, &QTcpSocket::disconnected, this, &GlgDataServer::handleClientDisconnected);

    // Save the client socket
    clientSockets.append(clientSocket);
    */

    qDebug() << "New connection!";

    // Accept incoming connection
    QWebSocket *clientSocket = webSocketServer->nextPendingConnection();

    // Read data from the client
    connect(clientSocket, &QWebSocket::textMessageReceived, this, &GlgDataServer::readData);

    // Disconnect the client once it is disconnected
    connect(clientSocket, &QWebSocket::disconnected, this, &GlgDataServer::handleClientDisconnected);

    // Save the client socket
    clientSockets.append(clientSocket);
}

void GlgDataServer::readData(const QString& message) {
    /*
    // Get the socket that triggered the signal
    QTcpSocket *socket = qobject_cast<QTcpSocket *>(sender());

    // Read data from the socket
    QByteArray data = socket->readAll();
    qDebug() << "GLG Req:" << data;

    // Echo the data back to the client
    //socket->write(data);
    */

    // Get the socket that triggered the signal
    QWebSocket *socket = qobject_cast<QWebSocket *>(sender());
    qDebug() << "GLG Req:" << message;
    // Echo the data back to the client
    //socket->sendTextMessage(data);
}

void GlgDataServer::handleClientDisconnected() {
    /*
    // Get the socket that triggered the signal
    QTcpSocket *disconnectedSocket = qobject_cast<QTcpSocket *>(sender());

    // Remove the disconnected socket from the list
    clientSockets.removeAll(disconnectedSocket);

    // Delete the socket
    disconnectedSocket->deleteLater();

    qDebug() << "Glg Client closed, current clients number is " << clientSockets.size();
    */

    // Get the socket that triggered the signal
    QWebSocket *disconnectedSocket = qobject_cast<QWebSocket *>(sender());

    // Remove the disconnected socket from the list
    clientSockets.removeAll(disconnectedSocket);

    // Delete the socket
    disconnectedSocket->deleteLater();

    qDebug() << "Glg Client closed, current clients number is " << clientSockets.size();
}

