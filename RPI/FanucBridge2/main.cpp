#include "widget.h"

#include <QApplication>
#include <QDesktopWidget>
#include <QFile>
#include <QTextStream>
#include <QTcpServer>
#include <QTcpSocket>
#include <QUrl>
#include <QtCore>
#include <QtNetwork>
#include <QtSerialBus/QModbusTcpServer>
#include "fanuchttpserver.h"

int main(int argc, char *argv[])
{
    QApplication app(argc, argv);

    // Using QHttpServer Way
    FanucHttpServer httpServer;
    httpServer.start(8080);

    // Open the main window
    Widget window;
    window.setWindowTitle("Fanuc Bridge2");
    int width = 600;
    int height = 400;
    window.resize(width, height);

    QRect screenGeometry = QApplication::desktop()->screen()->rect();
    int x = (screenGeometry.width()-window.width()) / 2;
    int y = (screenGeometry.height()-window.height()) / 2;
    window.move(x, y);
    window.show();
    return app.exec();
}
