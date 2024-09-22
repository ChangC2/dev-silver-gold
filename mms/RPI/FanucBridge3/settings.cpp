#include "settings.h"
#include "ui_settings.h"
#include "Global.h"
#include "apputils.h"
#include <QMessageBox>
#include "QRegularExpression"
#include "QRegularExpressionValidator"
#include "QIntValidator"
#include <QSerialPort>
#include <QSerialPortInfo>
#include <QDebug>

Settings::Settings(QWidget *parent) :
    QWidget(parent),
    ui(new Ui::Settings)
{
    ui->setupUi(this);
    appSetting = new AppSettings();

    // IP, Port Inputs, apply input filters
    QRegularExpression regex("^((\\d{1,2}|1\\d{2}|2[0-4]\\d|25[0-5])\\.){3}(\\d{1,2}|1\\d{2}|2[0-4]\\d|25[0-5])$");
    QRegularExpressionValidator *validator = new QRegularExpressionValidator(regex, ui->txtIPAddr);
    ui->txtIPAddr->setValidator(validator);

    QIntValidator *portValidator = new QIntValidator(0, 65535, ui->txtPort);
    ui->txtPort->setValidator(portValidator);
}


Settings::~Settings()
{
    delete ui;
}

void Settings::showEvent(QShowEvent *event)
{
    ui->txtIPAddr->setText(appSetting->getStringValue(SETTING_FANUC_IP, ""));
    ui->txtPort->setText(QString::number(appSetting->getIntValue(SETTING_FANUC_PORT, 0)));

    ui->txtAddressPressure1->setText(QString::number(appSetting->getIntValue(SETTING_PRESSURE_ADDRESS1, 0)));
    ui->txtAddressPressure2->setText(QString::number(appSetting->getIntValue(SETTING_PRESSURE_ADDRESS2, 0)));
    ui->txtAddressPort->setText(QString::number(appSetting->getIntValue(SETTING_PORT_ADDRESS, 0)));

    ui->chkFuanucEnable->setChecked(appSetting->getIntValue(SETTING_FANUC_ENABLE, 0));
    ui->txtIPAddr->setEnabled(appSetting->getIntValue(SETTING_FANUC_ENABLE, 0));
    ui->txtPort->setEnabled(appSetting->getIntValue(SETTING_FANUC_ENABLE, 0));
    ui->btnConnect->setEnabled(appSetting->getIntValue(SETTING_FANUC_ENABLE, 0));

    if (appSetting->getIntValue(SETTING_PUMP_CONFIG, 0) == 1){
        ui->radio1Pump->setChecked(true);
    }else{
        ui->radio2Pump->setChecked(true);
    }

    QWidget::showEvent(event);
}


void Settings::on_btnClose_clicked()
{
    hide();
}


void Settings::on_btnConnect_clicked()
{
    // Try to Connect
    QString ipAddrVal = ui->txtIPAddr->text();
    QString portVal = ui->txtPort->text();
    int portAddr = portVal.toInt();

    if(AppUtils::isValidIPAddress(ipAddrVal) && AppUtils::isValidPort(portAddr)) {
        appSetting->setValue(SETTING_FANUC_IP, ipAddrVal);
        appSetting->setValue(SETTING_FANUC_PORT, portAddr);
    } else {
        QMessageBox::warning(this, "Fanuc Settings", "Invalid fanuc ip or port.");
        return;
    }
}


void Settings::on_btnSetPressureAddress1_clicked()
{
    QString pressureVal = ui->txtAddressPressure1->text();
    int pressureAddr = pressureVal.toInt();
    appSetting->setValue(SETTING_PRESSURE_ADDRESS1, pressureAddr);
}

void Settings::on_btnSetPressureAddress2_clicked()
{
    QString pressureVal = ui->txtAddressPressure2->text();
    int pressureAddr = pressureVal.toInt();
    appSetting->setValue(SETTING_PRESSURE_ADDRESS2, pressureAddr);
}

void Settings::on_btnSetPortAddress_clicked()
{
    QString portVal = ui->txtAddressPort->text();
    int portAddr = portVal.toInt();
    appSetting->setValue(SETTING_PORT_ADDRESS, portAddr);
}

void Settings::on_chkFuanucEnable_stateChanged(int arg1)
{
    appSetting->setValue(SETTING_FANUC_ENABLE, arg1);
    ui->txtIPAddr->setEnabled(arg1);
    ui->txtPort->setEnabled(arg1);
    ui->btnConnect->setEnabled(arg1);
}

void Settings::on_radio2Pump_clicked()
{
    appSetting->setValue(SETTING_PUMP_CONFIG, 2);
    emit updatePumpConfig();
}


void Settings::on_radio1Pump_clicked()
{
    appSetting->setValue(SETTING_PUMP_CONFIG, 1);
    emit updatePumpConfig();
}


