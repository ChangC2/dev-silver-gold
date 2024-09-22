#ifndef SETTINGS_H
#define SETTINGS_H

#include <QWidget>
#include "appsettings.h"

namespace Ui {
class Settings;
}

class Settings : public QWidget
{
    Q_OBJECT

public:
    explicit Settings(QWidget *parent = nullptr);
    ~Settings();

signals:
    void updatePumpConfig();

protected:
    void showEvent(QShowEvent *event) override;


private slots:
    void on_btnClose_clicked();
    void on_btnConnect_clicked();
    void on_btnSetPressureAddress1_clicked();
    void on_btnSetPressureAddress2_clicked();
    void on_btnSetPortAddress_clicked();

    void on_chkFuanucEnable_stateChanged(int arg1);

    void on_radio2Pump_clicked();

    void on_radio1Pump_clicked();

private:
    Ui::Settings *ui;
    AppSettings *appSetting;
};

#endif // SETTINGS_H
