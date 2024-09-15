#include "fanuchttpserver.h"

/*FanucHttpServer::FanucHttpServer()
{

}*/

void FanucHttpServer::start(int port) {
    if (server->listen(QHostAddress::Any, port)) {
        qDebug() << "Server started on port" << port;
    } else {
        qDebug() << "Failed to start server";
    }
}


void FanucHttpServer::onNewConnection() {
    while (server->hasPendingConnections()) {
        QTcpSocket* socket = server->nextPendingConnection();
        connect(socket, &QTcpSocket::readyRead, this, &FanucHttpServer::onReadyRead);
    }
}

void FanucHttpServer::onReadyRead() {
    QTcpSocket* socket = static_cast<QTcpSocket*>(sender());

    QByteArray requestData = socket->readAll();

    // Parse the request to get the method, path, headers, etc.
    // You can use third-party libraries like QHttpParser for this or implement your own parsing logic

    // Process the request and generate a response
    QByteArray responseData = generateResponse(requestData);

    // Send the response back to the client
    socket->write(responseData);
    socket->flush();
    socket->waitForBytesWritten();
    socket->close();
}

QByteArray FanucHttpServer::generateResponse(const QByteArray& requestData) {
    // Here, you can process the request, generate a response based on the requested path, method, etc.
    QString requestMethod;
    QString requestPath;
    QTextStream stream(requestData);
    if (!stream.atEnd()) {
        QString firstLine = stream.readLine();
        QStringList parts = firstLine.split(" ");
        if (parts.size() >= 2) {
            requestMethod = parts[0];
            requestPath = parts[1];
        }
    }

    qDebug() << "--------------------------";
    qDebug() << "MP1" << requestMethod << requestPath;

    QString requestString(requestData);
    qDebug() << "MP2" << requestString;
    QStringList requestLines = requestString.split("\r\n", Qt::SkipEmptyParts);
    QStringList requestInfo = requestLines[0].split(" ");
    qDebug() << "MP3" << requestInfo[0] << requestInfo[1];
    qDebug() << "--------------------------";

    // For simplicity, let's just return a simple "Hello, World!" response
    /*QByteArray responseData = "HTTP/1.1 200 OK\r\n"
                              "Content-Type: text/plain\r\n"
                              "Content-Length: 13\r\n"
                              "Connection: close\r\n"
                              "\r\n"
                              "Hello, World!";*/

    if (requestPath == "/") {

        QByteArray htmlContent = getFileContent("index.html");
        // Set the HTTP response headers
        QByteArray responseData =
                "HTTP/1.1 200 OK\r\n"
                "Content-Type: text/html\r\n"
                "Content-Length: " + QByteArray::number(htmlContent.size()) + "\r\n"
                "\r\n" + htmlContent;

        return responseData;
    } else if(requestPath.endsWith("styles.css")) {
        QByteArray cssContent = getFileContent("styles.css");
        // Set the HTTP response headers
        QByteArray responseData =
                "HTTP/1.1 200 OK\r\n"
                "Content-Type: text/css\r\n"
                "Content-Length: " + QByteArray::number(cssContent.size()) + "\r\n"
                "\r\n" + cssContent;

        return responseData;
    } else if(requestPath.endsWith("script.js")) {
        QByteArray jsContent = getFileContent("script.js");
        // Set the HTTP response headers
        QByteArray responseData =
                "HTTP/1.1 200 OK\r\n"
                "Content-Type: application/javascript\r\n"
                "Content-Length: " + QByteArray::number(jsContent.size()) + "\r\n"
                "\r\n" + jsContent;

        return responseData;
    } else if(requestPath.endsWith("favicon.ico")) {
        QByteArray iconContent = getFileContent("favicon.ico");
        // Set the HTTP response headers
        QByteArray responseData =
                "HTTP/1.1 200 OK\r\n"
                "Content-Type: image/x-icon\r\n"
                "Content-Length: " + QByteArray::number(iconContent.size()) + "\r\n"
                "\r\n" + iconContent;

        return responseData;
    } else if(requestPath.endsWith(".html")) {
        QByteArray imageContent = getFileContent(requestPath);
        // Set the HTTP response headers
        QByteArray responseData =
                "HTTP/1.1 200 OK\r\n"
                "Content-Type: text/html\r\n"
                "Content-Length: " + QByteArray::number(imageContent.size()) + "\r\n"
                "\r\n" + imageContent;

        return responseData;
    } else if(requestPath.endsWith(".css")) {
        QByteArray imageContent = getFileContent(requestPath);
        // Set the HTTP response headers
        QByteArray responseData =
                "HTTP/1.1 200 OK\r\n"
                "Content-Type: text/css\r\n"
                "Content-Length: " + QByteArray::number(imageContent.size()) + "\r\n"
                "\r\n" + imageContent;

        return responseData;
    } else if(requestPath.endsWith(".js")) {
        QByteArray imageContent = getFileContent(requestPath);
        // Set the HTTP response headers
        QByteArray responseData =
                "HTTP/1.1 200 OK\r\n"
                "Content-Type: application/javascript\r\n"
                "Content-Length: " + QByteArray::number(imageContent.size()) + "\r\n"
                "\r\n" + imageContent;

        return responseData;
    } else if(requestPath.endsWith(".png") || requestPath.endsWith(".jpg") || requestPath.endsWith(".gif")) {
        QByteArray imageContent = getFileContent(requestPath);
        // Set the HTTP response headers
        QByteArray responseData =
                "HTTP/1.1 200 OK\r\n"
                "Content-Type: image/png\r\n"
                "Content-Length: " + QByteArray::number(imageContent.size()) + "\r\n"
                "\r\n" + imageContent;

        return responseData;
    } else if(requestPath.endsWith(".g")) {
        QByteArray imageContent = getFileContent(requestPath);
        // Set the HTTP response headers
        QByteArray responseData =
                "HTTP/1.1 200 OK\r\n"
                "Content-Type: application/octet-stream\r\n"
                "Content-Length: " + QByteArray::number(imageContent.size()) + "\r\n"
                "\r\n" + imageContent;

        return responseData;
    } else if(requestMethod == "POST") {
        int bodyStartIndex = requestString.indexOf("\r\n\r\n") + 4;

        QString contentTypeHeader;
        for (const QString& line : requestLines) {
            if (line.startsWith("Content-Type:")) {
                contentTypeHeader = line.mid(QString("Content-Type:").length()).trimmed();
                break;
            }
        }

        QMap<QString, QString> formData;

        // Process the request based on the Content-Type
        if (contentTypeHeader.startsWith("multipart/form-data")) {
            // Extract the boundary from the content type header
            QString boundary;
            int pos = contentTypeHeader.indexOf("boundary=");
            if (pos != -1) {
                boundary = contentTypeHeader.mid(pos + QString("boundary=").length());
            }

            // Split the request body into individual parts based on the boundary
            QStringList parts = requestString.mid(bodyStartIndex).split("--" + boundary, Qt::SkipEmptyParts);
            if (!parts.isEmpty()) {
                //parts.removeLast(); // Remove the initial boundary
            }

            // Loop through the parts and extract the form data parameters
            foreach (const QString& part, parts) {
                // Find the name and value of each form field
                QRegularExpression re("name=\"([^\"]*)\"\r\n\r\n(.*)\r\n");
                QRegularExpressionMatch match = re.match(part);
                if (match.hasMatch()) {
                    QString paramName = match.captured(1);
                    QString paramValue = match.captured(2);

                    formData.insert(paramName, paramValue);
                }
            }
        }

        // Print params
        if (requestPath == "/api/updateSettings") {
            qDebug() << "updateSettings :" << formData.value("IP") << formData.value("Port") << formData.value("Val1");
        }

        // Generate a response indicating success
        QJsonObject responseObject;
        responseObject["status"] = false;
        responseObject["message"] = "No result";

        QJsonDocument responseDoc(responseObject);
        QByteArray jsonResponse = responseDoc.toJson();

        return "HTTP/1.1 200 OK\r\n"
               "Content-Type: application/json\r\n"
               "Content-Length: " + QByteArray::number(jsonResponse.size()) + "\r\n"
               "Connection: close\r\n"
               "\r\n" + jsonResponse;
    }

    // If No response value, then return the Page not found content
    QByteArray noData =
            "HTTP/1.1 200 Not Found\r\n"
            "Content-Type: text/plain\r\n"
            "Content-Length: 15\r\n"
            "Connection: close\r\n"
            "\r\n"
            "Page not found.";
    return noData;

}

QByteArray FanucHttpServer::getFileContent(QString filePath) {

    // Read the contents of the HTML file
    /*QUrl htmlUrl("assets/index.html");
    QFile htmlFile(htmlUrl.isLocalFile() ? htmlUrl.toLocalFile() : htmlUrl.toString());
    if (!htmlFile.open(QIODevice::ReadOnly | QIODevice::Text)) {
        qDebug() << "Failed to open HTML file";
        return "";
    }
    QByteArray htmlContent = htmlFile.readAll();
    htmlFile.close();*/

    QString executableDir = QCoreApplication::applicationDirPath();
    QString fileAbsolutPath = executableDir + "/assets/" + filePath;
    QFile file(fileAbsolutPath);
    qDebug() << "File Path:" << fileAbsolutPath;
    if (!file.open(QIODevice::ReadOnly)) {
        qDebug() << "Failed to open HTML file" << fileAbsolutPath;
        return "";
    }
    QByteArray fileContent = file.readAll();
    file.close();

    return fileContent;
}
