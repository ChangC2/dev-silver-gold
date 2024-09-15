#include "mainwindow.h"

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

int main(int argc, char *argv[])
{
    QApplication app(argc, argv);
    MainWindow window;
    window.setWindowTitle("Fanuc Bridge3");
    int width = 800;
    int height = 480;
    window.resize(width, height);
    window.move(0, 0);
    window.show();
    return app.exec();
}


