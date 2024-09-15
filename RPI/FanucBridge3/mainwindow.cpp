#include "mainwindow.h"
#include "ui_mainwindow.h"
#include <QPixmap>
#include "QRegularExpression"
#include "QRegularExpressionValidator"
#include "QIntValidator"
#include "QDoubleValidator"
#include <QString>
#include <QMessageBox>
#include <QDebug>
#include <QException>
#include <QRandomGenerator>
#include "Global.h"
#include "apputils.h"
#include "QtCore/qmath.h"
#include <QDateTime>
#include <QCoreApplication>
#include "QInputDialog"


MainWindow::MainWindow(QWidget *parent) :
    QMainWindow(parent),
    ui(new Ui::MainWindow)
{
    ui->setupUi(this);
    setAttribute(Qt::WA_AcceptTouchEvents);
    setWindowFlags(Qt::FramelessWindowHint);
    ui->btnNav->setStyleSheet("QPushButton {"
                              "   border: none;"
                              "   background-color: transparent;"
                              "}");

    appSetting = new AppSettings();

    QString customerID = QString("hennig");
    QString machineName = QString("CDN");
    appSetting->setValue(SETTING_MMS_CUSTOMERID, customerID);
    appSetting->setValue(SETTING_MMS_MACHINENAME, machineName);

    setConnectionStatus(1);

    ui->vMenu->hide();
    settingsWidget = new Settings(this);
    settingsWidget->hide();

    worker = new Worker();
    worker->moveToThread(&workerThread);

    connect(&workerThread, &QThread::finished, worker, &QObject::deleteLater);
    connect(worker, &Worker::resultReady, this, &MainWindow::showDataToUI);
    connect(this, &MainWindow::handleMainTimeout, worker, &Worker::handleMainTimeout);

    // Start the worker thread
    workerThread.start();

    // Trigger the worker to handle the timeout periodically
    QTimer *timer = new QTimer(this);
    connect(timer, &QTimer::timeout, this, [=]() {
        emit handleMainTimeout();
    });
    timer->start(1000); // Adjust the interval as needed

    ui->lblFillPumpStatus->setStyleSheet("QLabel { color : red; }");
    ui->lblTopFloatStatus->setStyleSheet("QLabel { color : red; }");
    ui->lblBottomFloatStatus->setStyleSheet("QLabel { color : red; }");
    ui->vSlider->hide();
    ui->numberSlider->setSingleStep(10);
}

void MainWindow::setConnectionStatus(int status){
    if (status == 1){ // Connected
        ui->lblConnectionStatusTitle->setText("Connected to server");
        ui->lblConnectionStatusTitle->setStyleSheet("QLabel {color:#019a74;}");
        QPixmap icStatus(":/icons/ic_circle_green.png");
        ui->lblConnectionStatusImg->setPixmap(icStatus);
    }else{ // Disconnected
        ui->lblConnectionStatusTitle->setText("Disconnected to server");
        ui->lblConnectionStatusTitle->setStyleSheet("QLabel {color:#d10000;}");
        QPixmap icStatus(":/icons/ic_circle_red.png");
        ui->lblConnectionStatusImg->setPixmap(icStatus);
    }
}

void MainWindow::setMachineStatus(int status){
    if (status == 0){ // Idle
        ui->lblMachineStatusTitle->setText("Idle");
        ui->lblMachineStatusTitle->setStyleSheet("QLabel {color:#012daa;}");
        QPixmap icStatus(":/icons/ic_circle_blue.png");
        ui->lblMachineStatusImg->setPixmap(icStatus);
    }else if (status == 1){ // In Cycle
        ui->lblMachineStatusTitle->setText("In Cycle");
        ui->lblMachineStatusTitle->setStyleSheet("QLabel {color:#019a74;}");
        QPixmap icStatus(":/icons/ic_circle_green.png");
        ui->lblMachineStatusImg->setPixmap(icStatus);
    }else{ // Alarm
        ui->lblMachineStatusTitle->setText("Alarm");
        ui->lblMachineStatusTitle->setStyleSheet("QLabel {color:#d10000;}");
        QPixmap icStatus(":/icons/ic_circle_red.png");
        ui->lblMachineStatusImg->setPixmap(icStatus);
    }
}

MainWindow::~MainWindow()
{
    workerThread.quit();
    //    workerThread.wait();
    delete ui;
    delete settingsWidget;
}

void MainWindow::on_btnNav_clicked()
{
    if (ui->vMenu->isVisible()){
        ui->vMenu->hide();
    }else{
        ui->vMenu->show();
    }
}

void MainWindow::quitApp() {
    QCoreApplication::quit();
}

void MainWindow::showDataToUI(){
    ui->btnPressureSetPoint1->setText(QString("%1 psi").arg(worker->pressureSetPoint1));
    ui->btnActualPressure1->setText(QString("%1 psi").arg(worker->actualPressure1));
    ui->btnPressureSetPoint2->setText(QString("%1 psi").arg(worker->pressureSetPoint2));

    setMachineStatus(worker->systemStatus);
    ui->btnActualPressure1->setText(QString("%1 psi").arg(worker->actualPressure1));
    ui->btnActualPressure2->setText(QString("%1 psi").arg(worker->actualPressure2));
    ui->lblFilterLife->setText(QString("%1 %").arg(worker->filterPressure1));
    ui->lblFillPumpStatus->setText(worker->feedPump == 1 ? "On" : "Off");
    ui->lblFillPumpStatus->setStyleSheet(worker->feedPump == 1 ? "QLabel { color : green; }" : "QLabel { color : red; }");
    ui->lblTopFloatStatus->setText(worker->topFloat == 1 ? "On" : "Off");
    ui->lblTopFloatStatus->setStyleSheet(worker->topFloat == 1 ? "QLabel { color : green; }" : "QLabel { color : red; }");
    ui->lblBottomFloatStatus->setText(worker->bottomFloat == 1 ? "On" : "Off");
    ui->lblBottomFloatStatus->setStyleSheet(worker->bottomFloat == 1 ? "QLabel { color : green; }" : "QLabel { color : red; }");
    ui->lblElapsedRunTime->setText(QString("%1 min").arg(QString::number((double)worker->elapsedRunTime / 60.0, 'f', 1)));
}

void MainWindow::on_btnExit_clicked()
{
    quitApp();
    return;
}

void MainWindow::on_btnSettings_clicked()
{
    settingsWidget->show();
    ui->vMenu->hide();
}

void MainWindow::on_btnPressureSetPoint1_clicked()
{
    inputType = 0;
    ui->vSlider->show();
    ui->numberSlider->setValue(worker->pressureSetPoint1);
}

void MainWindow::on_btnPressureSetPoint2_clicked()
{
    inputType = 1;
    ui->vSlider->show();
    ui->numberSlider->setValue(worker->pressureSetPoint2);
}

void MainWindow::on_pushButton_clicked()
{
    ui->vSlider->hide();
}

void MainWindow::on_pushButton_2_clicked()
{
    ui->vSlider->hide();
    if (inputType == 0){
        int number = ui->numberSlider->value();
        ui->btnPressureSetPoint1->setText(QString("%1 psi").arg(number));
        worker->pressureSetPoint1 = number;
        if (worker->bFanucConnected){
            worker->writeMacro(appSetting->getIntValue(SETTING_PRESSURE_ADDRESS1, 0), number);
        }
    }
    if (inputType == 1){
        int number = ui->numberSlider->value();
        ui->btnPressureSetPoint2->setText(QString("%1 psi").arg(number));
        worker->pressureSetPoint2 = number;
        if (worker->bFanucConnected)
            worker->writeMacro2(appSetting->getIntValue(SETTING_PRESSURE_ADDRESS2, 0), number);
    }
}

void MainWindow::on_numberSlider_valueChanged(int value)
{
    ui->lblSliverValue->setText(QString::number(value));
}
