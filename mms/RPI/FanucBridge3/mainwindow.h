#ifndef MAINWINDOW_H
#define MAINWINDOW_H

#include <QMainWindow>
#include "settings.h"
#include <QThread>

#include "appsettings.h"
#include "worker.h"

namespace Ui {
class MainWindow;
}

class MainWindow : public QMainWindow
{
    Q_OBJECT

public:
    explicit MainWindow(QWidget *parent = nullptr);
    ~MainWindow();
signals:
    void handleMainTimeout();

protected:
    void showEvent(QShowEvent *event) override;

private slots:
    void on_btnNav_clicked();

    // Timer Handlers    
    void on_btnExit_clicked();
    void on_btnSettings_clicked();

    void on_btnPressureSetPoint1_clicked();

    void on_btnPressureSetPoint2_clicked();

    void showDataToUI();
    void on_pushButton_clicked();
    void on_pushButton_2_clicked();

    void on_numberSlider_valueChanged(int value);

    void updatedPumpConfig();

private:
    void setConnectionStatus(int status);
    void setMachineStatus(int status);
    void quitApp();


private:
    Ui::MainWindow *ui;
    Settings *settingsWidget;

    // Storage for read/writing the settings
    AppSettings *appSetting;
    Worker *worker;
    QThread workerThread;
    int inputType = 0;

};

#endif // MAINWINDOW_H
