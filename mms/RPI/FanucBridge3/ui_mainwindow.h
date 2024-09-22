/********************************************************************************
** Form generated from reading UI file 'mainwindow.ui'
**
** Created by: Qt User Interface Compiler version 5.15.3
**
** WARNING! All changes made in this file will be lost when recompiling UI file!
********************************************************************************/

#ifndef UI_MAINWINDOW_H
#define UI_MAINWINDOW_H

#include <QtCore/QVariant>
#include <QtGui/QIcon>
#include <QtWidgets/QApplication>
#include <QtWidgets/QFrame>
#include <QtWidgets/QGroupBox>
#include <QtWidgets/QLabel>
#include <QtWidgets/QMainWindow>
#include <QtWidgets/QMenuBar>
#include <QtWidgets/QPushButton>
#include <QtWidgets/QSlider>
#include <QtWidgets/QStatusBar>
#include <QtWidgets/QWidget>

QT_BEGIN_NAMESPACE

class Ui_MainWindow
{
public:
    QWidget *centralwidget;
    QFrame *line;
    QLabel *label;
    QPushButton *btnNav;
    QLabel *lblMachineStatusImg;
    QLabel *lblMachineStatusTitle;
    QLabel *lblConnectionStatusImg;
    QLabel *lblConnectionStatusTitle;
    QWidget *widgetPump1;
    QLabel *lblPressureSetPoint;
    QPushButton *btnPressureSetPoint1;
    QLabel *lblActualPressure;
    QPushButton *btnActualPressure1;
    QFrame *line_2;
    QLabel *label_4;
    QFrame *line_3;
    QWidget *widgetPump2;
    QLabel *lblPressureSetPoint_2;
    QPushButton *btnPressureSetPoint2;
    QLabel *lblActualPressure_2;
    QPushButton *btnActualPressure2;
    QFrame *line_4;
    QLabel *label_5;
    QFrame *line_5;
    QGroupBox *groupBox;
    QLabel *lblEstTimeToFilter;
    QLabel *label_6;
    QLabel *lblFilterLife;
    QGroupBox *groupBox_2;
    QLabel *lblFillPumpStatus;
    QLabel *label_10;
    QLabel *label_11;
    QLabel *lblTopFloatStatus;
    QLabel *label_13;
    QLabel *lblBottomFloatStatus;
    QLabel *label_15;
    QLabel *lblElapsedRunTime;
    QWidget *vSlider;
    QSlider *numberSlider;
    QLabel *label_7;
    QLabel *label_8;
    QPushButton *pushButton;
    QPushButton *pushButton_2;
    QLabel *lblSliverValue;
    QLabel *label_9;
    QWidget *vMenu;
    QLabel *lblConnectionStatusImg_2;
    QLabel *label_2;
    QPushButton *btnSettings;
    QLabel *lblConnectionStatusImg_3;
    QPushButton *btnExit;
    QLabel *label_3;
    QMenuBar *menubar;
    QStatusBar *statusbar;

    void setupUi(QMainWindow *MainWindow)
    {
        if (MainWindow->objectName().isEmpty())
            MainWindow->setObjectName(QString::fromUtf8("MainWindow"));
        MainWindow->resize(831, 753);
        QIcon icon;
        icon.addFile(QString::fromUtf8("assets/favicon.png"), QSize(), QIcon::Normal, QIcon::Off);
        MainWindow->setWindowIcon(icon);
        centralwidget = new QWidget(MainWindow);
        centralwidget->setObjectName(QString::fromUtf8("centralwidget"));
        line = new QFrame(centralwidget);
        line->setObjectName(QString::fromUtf8("line"));
        line->setGeometry(QRect(0, 48, 800, 2));
        line->setFrameShape(QFrame::HLine);
        line->setFrameShadow(QFrame::Sunken);
        label = new QLabel(centralwidget);
        label->setObjectName(QString::fromUtf8("label"));
        label->setGeometry(QRect(240, 5, 320, 40));
        label->setPixmap(QPixmap(QString::fromUtf8(":/icons/ic_logo.png")));
        label->setScaledContents(true);
        btnNav = new QPushButton(centralwidget);
        btnNav->setObjectName(QString::fromUtf8("btnNav"));
        btnNav->setGeometry(QRect(10, 12, 26, 26));
        QIcon icon1;
        icon1.addFile(QString::fromUtf8(":/icons/ic_nav.png"), QSize(), QIcon::Normal, QIcon::Off);
        btnNav->setIcon(icon1);
        btnNav->setIconSize(QSize(25, 25));
        lblMachineStatusImg = new QLabel(centralwidget);
        lblMachineStatusImg->setObjectName(QString::fromUtf8("lblMachineStatusImg"));
        lblMachineStatusImg->setGeometry(QRect(774, 8, 18, 18));
        lblMachineStatusImg->setPixmap(QPixmap(QString::fromUtf8(":/icons/ic_circle_blue.png")));
        lblMachineStatusImg->setScaledContents(true);
        lblMachineStatusTitle = new QLabel(centralwidget);
        lblMachineStatusTitle->setObjectName(QString::fromUtf8("lblMachineStatusTitle"));
        lblMachineStatusTitle->setGeometry(QRect(705, 6, 65, 20));
        QFont font;
        font.setFamily(QString::fromUtf8("Sans Serif"));
        font.setPointSize(10);
        font.setBold(false);
        font.setItalic(false);
        font.setWeight(50);
        lblMachineStatusTitle->setFont(font);
        lblMachineStatusTitle->setScaledContents(true);
        lblMachineStatusTitle->setAlignment(Qt::AlignRight|Qt::AlignTrailing|Qt::AlignVCenter);
        lblConnectionStatusImg = new QLabel(centralwidget);
        lblConnectionStatusImg->setObjectName(QString::fromUtf8("lblConnectionStatusImg"));
        lblConnectionStatusImg->setGeometry(QRect(774, 27, 18, 18));
        lblConnectionStatusImg->setPixmap(QPixmap(QString::fromUtf8(":/icons/ic_circle_green.png")));
        lblConnectionStatusImg->setScaledContents(true);
        lblConnectionStatusTitle = new QLabel(centralwidget);
        lblConnectionStatusTitle->setObjectName(QString::fromUtf8("lblConnectionStatusTitle"));
        lblConnectionStatusTitle->setGeometry(QRect(620, 25, 150, 20));
        lblConnectionStatusTitle->setFont(font);
        lblConnectionStatusTitle->setScaledContents(true);
        lblConnectionStatusTitle->setAlignment(Qt::AlignRight|Qt::AlignTrailing|Qt::AlignVCenter);
        widgetPump1 = new QWidget(centralwidget);
        widgetPump1->setObjectName(QString::fromUtf8("widgetPump1"));
        widgetPump1->setGeometry(QRect(10, 99, 461, 161));
        lblPressureSetPoint = new QLabel(widgetPump1);
        lblPressureSetPoint->setObjectName(QString::fromUtf8("lblPressureSetPoint"));
        lblPressureSetPoint->setGeometry(QRect(20, 27, 205, 30));
        QFont font1;
        font1.setFamily(QString::fromUtf8("Sans Serif"));
        font1.setPointSize(11);
        font1.setBold(false);
        font1.setItalic(false);
        font1.setWeight(50);
        lblPressureSetPoint->setFont(font1);
        lblPressureSetPoint->setScaledContents(false);
        lblPressureSetPoint->setAlignment(Qt::AlignCenter);
        btnPressureSetPoint1 = new QPushButton(widgetPump1);
        btnPressureSetPoint1->setObjectName(QString::fromUtf8("btnPressureSetPoint1"));
        btnPressureSetPoint1->setGeometry(QRect(20, 54, 205, 101));
        QFont font2;
        font2.setFamily(QString::fromUtf8("Sans Serif"));
        font2.setPointSize(25);
        font2.setBold(false);
        font2.setWeight(50);
        btnPressureSetPoint1->setFont(font2);
        lblActualPressure = new QLabel(widgetPump1);
        lblActualPressure->setObjectName(QString::fromUtf8("lblActualPressure"));
        lblActualPressure->setGeometry(QRect(254, 27, 205, 30));
        lblActualPressure->setFont(font1);
        lblActualPressure->setScaledContents(false);
        lblActualPressure->setAlignment(Qt::AlignCenter);
        btnActualPressure1 = new QPushButton(widgetPump1);
        btnActualPressure1->setObjectName(QString::fromUtf8("btnActualPressure1"));
        btnActualPressure1->setGeometry(QRect(254, 54, 205, 101));
        btnActualPressure1->setFont(font2);
        line_2 = new QFrame(widgetPump1);
        line_2->setObjectName(QString::fromUtf8("line_2"));
        line_2->setGeometry(QRect(22, 18, 171, 3));
        line_2->setAutoFillBackground(false);
        line_2->setFrameShadow(QFrame::Plain);
        line_2->setLineWidth(3);
        line_2->setMidLineWidth(3);
        line_2->setFrameShape(QFrame::HLine);
        label_4 = new QLabel(widgetPump1);
        label_4->setObjectName(QString::fromUtf8("label_4"));
        label_4->setGeometry(QRect(199, 8, 80, 22));
        QFont font3;
        font3.setPointSize(15);
        font3.setBold(true);
        font3.setWeight(75);
        label_4->setFont(font3);
        label_4->setAlignment(Qt::AlignCenter);
        line_3 = new QFrame(widgetPump1);
        line_3->setObjectName(QString::fromUtf8("line_3"));
        line_3->setGeometry(QRect(287, 18, 171, 3));
        line_3->setAutoFillBackground(false);
        line_3->setFrameShadow(QFrame::Plain);
        line_3->setLineWidth(3);
        line_3->setMidLineWidth(3);
        line_3->setFrameShape(QFrame::HLine);
        widgetPump2 = new QWidget(centralwidget);
        widgetPump2->setObjectName(QString::fromUtf8("widgetPump2"));
        widgetPump2->setGeometry(QRect(10, 269, 461, 161));
        lblPressureSetPoint_2 = new QLabel(widgetPump2);
        lblPressureSetPoint_2->setObjectName(QString::fromUtf8("lblPressureSetPoint_2"));
        lblPressureSetPoint_2->setGeometry(QRect(20, 25, 205, 30));
        lblPressureSetPoint_2->setFont(font1);
        lblPressureSetPoint_2->setScaledContents(false);
        lblPressureSetPoint_2->setAlignment(Qt::AlignCenter);
        btnPressureSetPoint2 = new QPushButton(widgetPump2);
        btnPressureSetPoint2->setObjectName(QString::fromUtf8("btnPressureSetPoint2"));
        btnPressureSetPoint2->setGeometry(QRect(20, 52, 205, 101));
        btnPressureSetPoint2->setFont(font2);
        lblActualPressure_2 = new QLabel(widgetPump2);
        lblActualPressure_2->setObjectName(QString::fromUtf8("lblActualPressure_2"));
        lblActualPressure_2->setGeometry(QRect(253, 25, 205, 30));
        lblActualPressure_2->setFont(font1);
        lblActualPressure_2->setScaledContents(false);
        lblActualPressure_2->setAlignment(Qt::AlignCenter);
        btnActualPressure2 = new QPushButton(widgetPump2);
        btnActualPressure2->setObjectName(QString::fromUtf8("btnActualPressure2"));
        btnActualPressure2->setGeometry(QRect(253, 52, 205, 101));
        btnActualPressure2->setFont(font2);
        line_4 = new QFrame(widgetPump2);
        line_4->setObjectName(QString::fromUtf8("line_4"));
        line_4->setGeometry(QRect(22, 19, 171, 3));
        line_4->setAutoFillBackground(false);
        line_4->setFrameShadow(QFrame::Plain);
        line_4->setLineWidth(3);
        line_4->setMidLineWidth(3);
        line_4->setFrameShape(QFrame::HLine);
        label_5 = new QLabel(widgetPump2);
        label_5->setObjectName(QString::fromUtf8("label_5"));
        label_5->setGeometry(QRect(199, 9, 80, 22));
        label_5->setFont(font3);
        label_5->setAlignment(Qt::AlignCenter);
        line_5 = new QFrame(widgetPump2);
        line_5->setObjectName(QString::fromUtf8("line_5"));
        line_5->setGeometry(QRect(287, 19, 171, 3));
        line_5->setAutoFillBackground(false);
        line_5->setFrameShadow(QFrame::Plain);
        line_5->setLineWidth(3);
        line_5->setMidLineWidth(3);
        line_5->setFrameShape(QFrame::HLine);
        groupBox = new QGroupBox(centralwidget);
        groupBox->setObjectName(QString::fromUtf8("groupBox"));
        groupBox->setGeometry(QRect(500, 114, 251, 141));
        lblEstTimeToFilter = new QLabel(groupBox);
        lblEstTimeToFilter->setObjectName(QString::fromUtf8("lblEstTimeToFilter"));
        lblEstTimeToFilter->setGeometry(QRect(0, 101, 251, 22));
        QFont font4;
        font4.setPointSize(10);
        font4.setBold(true);
        font4.setWeight(75);
        lblEstTimeToFilter->setFont(font4);
        lblEstTimeToFilter->setAlignment(Qt::AlignCenter);
        label_6 = new QLabel(groupBox);
        label_6->setObjectName(QString::fromUtf8("label_6"));
        label_6->setGeometry(QRect(5, 84, 241, 21));
        QFont font5;
        font5.setPointSize(10);
        label_6->setFont(font5);
        label_6->setAlignment(Qt::AlignCenter);
        lblFilterLife = new QLabel(groupBox);
        lblFilterLife->setObjectName(QString::fromUtf8("lblFilterLife"));
        lblFilterLife->setGeometry(QRect(1, 40, 251, 40));
        lblFilterLife->setFont(font2);
        lblFilterLife->setAlignment(Qt::AlignCenter);
        groupBox_2 = new QGroupBox(centralwidget);
        groupBox_2->setObjectName(QString::fromUtf8("groupBox_2"));
        groupBox_2->setGeometry(QRect(500, 269, 251, 151));
        lblFillPumpStatus = new QLabel(groupBox_2);
        lblFillPumpStatus->setObjectName(QString::fromUtf8("lblFillPumpStatus"));
        lblFillPumpStatus->setGeometry(QRect(100, 34, 81, 22));
        lblFillPumpStatus->setFont(font4);
        lblFillPumpStatus->setAlignment(Qt::AlignJustify|Qt::AlignVCenter);
        label_10 = new QLabel(groupBox_2);
        label_10->setObjectName(QString::fromUtf8("label_10"));
        label_10->setGeometry(QRect(15, 34, 81, 21));
        label_10->setFont(font5);
        label_10->setAlignment(Qt::AlignJustify|Qt::AlignVCenter);
        label_11 = new QLabel(groupBox_2);
        label_11->setObjectName(QString::fromUtf8("label_11"));
        label_11->setGeometry(QRect(15, 58, 71, 21));
        label_11->setFont(font5);
        label_11->setAlignment(Qt::AlignJustify|Qt::AlignVCenter);
        lblTopFloatStatus = new QLabel(groupBox_2);
        lblTopFloatStatus->setObjectName(QString::fromUtf8("lblTopFloatStatus"));
        lblTopFloatStatus->setGeometry(QRect(90, 58, 81, 22));
        lblTopFloatStatus->setFont(font4);
        lblTopFloatStatus->setAlignment(Qt::AlignJustify|Qt::AlignVCenter);
        label_13 = new QLabel(groupBox_2);
        label_13->setObjectName(QString::fromUtf8("label_13"));
        label_13->setGeometry(QRect(15, 83, 81, 21));
        label_13->setFont(font5);
        label_13->setAlignment(Qt::AlignJustify|Qt::AlignVCenter);
        lblBottomFloatStatus = new QLabel(groupBox_2);
        lblBottomFloatStatus->setObjectName(QString::fromUtf8("lblBottomFloatStatus"));
        lblBottomFloatStatus->setGeometry(QRect(110, 83, 81, 22));
        lblBottomFloatStatus->setFont(font4);
        lblBottomFloatStatus->setAlignment(Qt::AlignJustify|Qt::AlignVCenter);
        label_15 = new QLabel(groupBox_2);
        label_15->setObjectName(QString::fromUtf8("label_15"));
        label_15->setGeometry(QRect(15, 107, 121, 21));
        label_15->setFont(font5);
        label_15->setAlignment(Qt::AlignJustify|Qt::AlignVCenter);
        lblElapsedRunTime = new QLabel(groupBox_2);
        lblElapsedRunTime->setObjectName(QString::fromUtf8("lblElapsedRunTime"));
        lblElapsedRunTime->setGeometry(QRect(140, 107, 81, 22));
        lblElapsedRunTime->setFont(font4);
        lblElapsedRunTime->setAlignment(Qt::AlignJustify|Qt::AlignVCenter);
        vSlider = new QWidget(centralwidget);
        vSlider->setObjectName(QString::fromUtf8("vSlider"));
        vSlider->setGeometry(QRect(80, 80, 691, 321));
        vSlider->setAutoFillBackground(true);
        numberSlider = new QSlider(vSlider);
        numberSlider->setObjectName(QString::fromUtf8("numberSlider"));
        numberSlider->setGeometry(QRect(40, 110, 621, 68));
        QSizePolicy sizePolicy(QSizePolicy::Expanding, QSizePolicy::Fixed);
        sizePolicy.setHorizontalStretch(0);
        sizePolicy.setVerticalStretch(0);
        sizePolicy.setHeightForWidth(numberSlider->sizePolicy().hasHeightForWidth());
        numberSlider->setSizePolicy(sizePolicy);
        numberSlider->setSizeIncrement(QSize(0, 0));
        numberSlider->setBaseSize(QSize(0, 0));
        numberSlider->setStyleSheet(QString::fromUtf8("QSlider {\n"
"    min-height: 68px;\n"
"    max-height: 68px;\n"
"    background: #transparent;\n"
"}\n"
"\n"
".QSlider::groove:horizontal {\n"
"    border: 1px solid #262626;\n"
"    height: 5px;\n"
"    background: #393939;\n"
"    margin: 0 12px;\n"
"}\n"
"\n"
".QSlider::handle:horizontal {\n"
"    background: #22B14C;\n"
"    border: 5px solid #B5E61D;\n"
"    width: 23px;\n"
"    height: 100px;\n"
"    margin: -24px -12px;\n"
"}"));
        numberSlider->setMinimum(100);
        numberSlider->setMaximum(1000);
        numberSlider->setSingleStep(10);
        numberSlider->setPageStep(10);
        numberSlider->setOrientation(Qt::Horizontal);
        label_7 = new QLabel(vSlider);
        label_7->setObjectName(QString::fromUtf8("label_7"));
        label_7->setGeometry(QRect(40, 70, 41, 16));
        label_8 = new QLabel(vSlider);
        label_8->setObjectName(QString::fromUtf8("label_8"));
        label_8->setGeometry(QRect(620, 70, 41, 20));
        label_8->setAlignment(Qt::AlignRight|Qt::AlignTrailing|Qt::AlignVCenter);
        pushButton = new QPushButton(vSlider);
        pushButton->setObjectName(QString::fromUtf8("pushButton"));
        pushButton->setGeometry(QRect(170, 210, 131, 41));
        pushButton_2 = new QPushButton(vSlider);
        pushButton_2->setObjectName(QString::fromUtf8("pushButton_2"));
        pushButton_2->setGeometry(QRect(340, 210, 131, 41));
        lblSliverValue = new QLabel(vSlider);
        lblSliverValue->setObjectName(QString::fromUtf8("lblSliverValue"));
        lblSliverValue->setGeometry(QRect(320, 70, 61, 16));
        lblSliverValue->setAlignment(Qt::AlignCenter);
        label_9 = new QLabel(centralwidget);
        label_9->setObjectName(QString::fromUtf8("label_9"));
        label_9->setGeometry(QRect(240, 60, 320, 40));
        label_9->setPixmap(QPixmap(QString::fromUtf8(":/icons/ultraflow.png")));
        label_9->setScaledContents(true);
        vMenu = new QWidget(centralwidget);
        vMenu->setObjectName(QString::fromUtf8("vMenu"));
        vMenu->setGeometry(QRect(0, 48, 181, 401));
        vMenu->setAutoFillBackground(true);
        lblConnectionStatusImg_2 = new QLabel(vMenu);
        lblConnectionStatusImg_2->setObjectName(QString::fromUtf8("lblConnectionStatusImg_2"));
        lblConnectionStatusImg_2->setGeometry(QRect(16, 25, 14, 14));
        lblConnectionStatusImg_2->setPixmap(QPixmap(QString::fromUtf8(":/icons/ic_circle_blue.png")));
        lblConnectionStatusImg_2->setScaledContents(true);
        label_2 = new QLabel(vMenu);
        label_2->setObjectName(QString::fromUtf8("label_2"));
        label_2->setGeometry(QRect(47, 22, 68, 22));
        QFont font6;
        font6.setPointSize(12);
        font6.setBold(false);
        font6.setWeight(50);
        label_2->setFont(font6);
        btnSettings = new QPushButton(vMenu);
        btnSettings->setObjectName(QString::fromUtf8("btnSettings"));
        btnSettings->setGeometry(QRect(10, 20, 201, 31));
        btnSettings->setFlat(true);
        lblConnectionStatusImg_3 = new QLabel(vMenu);
        lblConnectionStatusImg_3->setObjectName(QString::fromUtf8("lblConnectionStatusImg_3"));
        lblConnectionStatusImg_3->setGeometry(QRect(16, 68, 14, 14));
        lblConnectionStatusImg_3->setPixmap(QPixmap(QString::fromUtf8(":/icons/ic_circle_blue.png")));
        lblConnectionStatusImg_3->setScaledContents(true);
        btnExit = new QPushButton(vMenu);
        btnExit->setObjectName(QString::fromUtf8("btnExit"));
        btnExit->setGeometry(QRect(10, 63, 201, 31));
        btnExit->setFlat(true);
        label_3 = new QLabel(vMenu);
        label_3->setObjectName(QString::fromUtf8("label_3"));
        label_3->setGeometry(QRect(47, 65, 68, 22));
        label_3->setFont(font6);
        label_2->raise();
        label_3->raise();
        lblConnectionStatusImg_2->raise();
        lblConnectionStatusImg_3->raise();
        btnExit->raise();
        btnSettings->raise();
        MainWindow->setCentralWidget(centralwidget);
        label_9->raise();
        line->raise();
        label->raise();
        btnNav->raise();
        lblMachineStatusImg->raise();
        lblMachineStatusTitle->raise();
        lblConnectionStatusImg->raise();
        lblConnectionStatusTitle->raise();
        widgetPump1->raise();
        widgetPump2->raise();
        groupBox->raise();
        groupBox_2->raise();
        vSlider->raise();
        vMenu->raise();
        menubar = new QMenuBar(MainWindow);
        menubar->setObjectName(QString::fromUtf8("menubar"));
        menubar->setGeometry(QRect(0, 0, 831, 22));
        MainWindow->setMenuBar(menubar);
        statusbar = new QStatusBar(MainWindow);
        statusbar->setObjectName(QString::fromUtf8("statusbar"));
        MainWindow->setStatusBar(statusbar);

        retranslateUi(MainWindow);

        btnPressureSetPoint1->setDefault(false);
        btnPressureSetPoint2->setDefault(false);


        QMetaObject::connectSlotsByName(MainWindow);
    } // setupUi

    void retranslateUi(QMainWindow *MainWindow)
    {
        MainWindow->setWindowTitle(QCoreApplication::translate("MainWindow", "MainWindow", nullptr));
        label->setText(QString());
        btnNav->setText(QString());
        lblMachineStatusImg->setText(QString());
        lblMachineStatusTitle->setText(QCoreApplication::translate("MainWindow", "Idle", nullptr));
        lblConnectionStatusImg->setText(QString());
        lblConnectionStatusTitle->setText(QCoreApplication::translate("MainWindow", "Connected to server", nullptr));
        lblPressureSetPoint->setText(QCoreApplication::translate("MainWindow", "Pressure Setpoint", nullptr));
        btnPressureSetPoint1->setText(QCoreApplication::translate("MainWindow", "0 psi", nullptr));
        lblActualPressure->setText(QCoreApplication::translate("MainWindow", "Actual Pressure", nullptr));
        btnActualPressure1->setText(QCoreApplication::translate("MainWindow", "0 psi", nullptr));
        label_4->setText(QCoreApplication::translate("MainWindow", "PUMP 1", nullptr));
        lblPressureSetPoint_2->setText(QCoreApplication::translate("MainWindow", "Pressure Setpoint", nullptr));
        btnPressureSetPoint2->setText(QCoreApplication::translate("MainWindow", "0 psi", nullptr));
        lblActualPressure_2->setText(QCoreApplication::translate("MainWindow", "Actual Pressure", nullptr));
        btnActualPressure2->setText(QCoreApplication::translate("MainWindow", "0 psi", nullptr));
        label_5->setText(QCoreApplication::translate("MainWindow", "PUMP 2", nullptr));
        groupBox->setTitle(QCoreApplication::translate("MainWindow", "Filter Life", nullptr));
        lblEstTimeToFilter->setText(QCoreApplication::translate("MainWindow", "10 hrs", nullptr));
        label_6->setText(QCoreApplication::translate("MainWindow", "Est. Time To Filter Change :", nullptr));
        lblFilterLife->setText(QCoreApplication::translate("MainWindow", "0%", nullptr));
        groupBox_2->setTitle(QCoreApplication::translate("MainWindow", "Diagnostics", nullptr));
        lblFillPumpStatus->setText(QCoreApplication::translate("MainWindow", "Off", nullptr));
        label_10->setText(QCoreApplication::translate("MainWindow", "Fill Pump 1 : ", nullptr));
        label_11->setText(QCoreApplication::translate("MainWindow", "Top Float :", nullptr));
        lblTopFloatStatus->setText(QCoreApplication::translate("MainWindow", "Off", nullptr));
        label_13->setText(QCoreApplication::translate("MainWindow", "Bottom Float :", nullptr));
        lblBottomFloatStatus->setText(QCoreApplication::translate("MainWindow", "Off", nullptr));
        label_15->setText(QCoreApplication::translate("MainWindow", "Elapsed Run Time :", nullptr));
        lblElapsedRunTime->setText(QCoreApplication::translate("MainWindow", "0 min", nullptr));
        label_7->setText(QCoreApplication::translate("MainWindow", "100", nullptr));
        label_8->setText(QCoreApplication::translate("MainWindow", "1000", nullptr));
        pushButton->setText(QCoreApplication::translate("MainWindow", "Cancel", nullptr));
        pushButton_2->setText(QCoreApplication::translate("MainWindow", "Ok", nullptr));
        lblSliverValue->setText(QCoreApplication::translate("MainWindow", "100", nullptr));
        label_9->setText(QString());
        lblConnectionStatusImg_2->setText(QString());
        label_2->setText(QCoreApplication::translate("MainWindow", "Settings", nullptr));
        btnSettings->setText(QString());
        lblConnectionStatusImg_3->setText(QString());
        btnExit->setText(QString());
        label_3->setText(QCoreApplication::translate("MainWindow", "Exit", nullptr));
    } // retranslateUi

};

namespace Ui {
    class MainWindow: public Ui_MainWindow {};
} // namespace Ui

QT_END_NAMESPACE

#endif // UI_MAINWINDOW_H
