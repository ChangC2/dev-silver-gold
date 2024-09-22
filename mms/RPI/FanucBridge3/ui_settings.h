/********************************************************************************
** Form generated from reading UI file 'settings.ui'
**
** Created by: Qt User Interface Compiler version 5.15.3
**
** WARNING! All changes made in this file will be lost when recompiling UI file!
********************************************************************************/

#ifndef UI_SETTINGS_H
#define UI_SETTINGS_H

#include <QtCore/QVariant>
#include <QtWidgets/QApplication>
#include <QtWidgets/QCheckBox>
#include <QtWidgets/QFrame>
#include <QtWidgets/QGroupBox>
#include <QtWidgets/QHBoxLayout>
#include <QtWidgets/QLabel>
#include <QtWidgets/QLineEdit>
#include <QtWidgets/QPushButton>
#include <QtWidgets/QRadioButton>
#include <QtWidgets/QWidget>

QT_BEGIN_NAMESPACE

class Ui_Settings
{
public:
    QPushButton *btnClose;
    QGroupBox *groupBox;
    QGroupBox *gBConnInfo_2;
    QHBoxLayout *horizontalLayout_2;
    QLabel *label_4;
    QLineEdit *txtAddressPressure1;
    QPushButton *btnSetPressureAddress1;
    QGroupBox *gBConnInfo_3;
    QHBoxLayout *horizontalLayout_3;
    QLabel *label_5;
    QLineEdit *txtAddressPort;
    QPushButton *btnSetPortAddress;
    QGroupBox *gBConnInfo;
    QHBoxLayout *horizontalLayout;
    QLabel *label;
    QLineEdit *txtIPAddr;
    QLabel *label_2;
    QLineEdit *txtPort;
    QPushButton *btnConnect;
    QCheckBox *chkFuanucEnable;
    QGroupBox *gBConnInfo_4;
    QHBoxLayout *horizontalLayout_4;
    QLabel *label_6;
    QLineEdit *txtAddressPressure2;
    QPushButton *btnSetPressureAddress2;
    QGroupBox *gBConnInfo_5;
    QHBoxLayout *horizontalLayout_5;
    QRadioButton *radio2Pump;
    QRadioButton *radio1Pump;
    QFrame *line;
    QLabel *label_3;

    void setupUi(QWidget *Settings)
    {
        if (Settings->objectName().isEmpty())
            Settings->setObjectName(QString::fromUtf8("Settings"));
        Settings->setWindowModality(Qt::WindowModal);
        Settings->resize(800, 596);
        Settings->setAutoFillBackground(true);
        btnClose = new QPushButton(Settings);
        btnClose->setObjectName(QString::fromUtf8("btnClose"));
        btnClose->setGeometry(QRect(30, 390, 741, 31));
        QFont font;
        font.setFamily(QString::fromUtf8("PibotoLt"));
        font.setPointSize(11);
        btnClose->setFont(font);
        groupBox = new QGroupBox(Settings);
        groupBox->setObjectName(QString::fromUtf8("groupBox"));
        groupBox->setGeometry(QRect(30, 63, 741, 321));
        gBConnInfo_2 = new QGroupBox(groupBox);
        gBConnInfo_2->setObjectName(QString::fromUtf8("gBConnInfo_2"));
        gBConnInfo_2->setGeometry(QRect(30, 79, 681, 57));
        horizontalLayout_2 = new QHBoxLayout(gBConnInfo_2);
        horizontalLayout_2->setObjectName(QString::fromUtf8("horizontalLayout_2"));
        label_4 = new QLabel(gBConnInfo_2);
        label_4->setObjectName(QString::fromUtf8("label_4"));

        horizontalLayout_2->addWidget(label_4);

        txtAddressPressure1 = new QLineEdit(gBConnInfo_2);
        txtAddressPressure1->setObjectName(QString::fromUtf8("txtAddressPressure1"));
        txtAddressPressure1->setAlignment(Qt::AlignCenter);

        horizontalLayout_2->addWidget(txtAddressPressure1);

        btnSetPressureAddress1 = new QPushButton(gBConnInfo_2);
        btnSetPressureAddress1->setObjectName(QString::fromUtf8("btnSetPressureAddress1"));

        horizontalLayout_2->addWidget(btnSetPressureAddress1);

        gBConnInfo_3 = new QGroupBox(groupBox);
        gBConnInfo_3->setObjectName(QString::fromUtf8("gBConnInfo_3"));
        gBConnInfo_3->setGeometry(QRect(30, 200, 681, 57));
        horizontalLayout_3 = new QHBoxLayout(gBConnInfo_3);
        horizontalLayout_3->setObjectName(QString::fromUtf8("horizontalLayout_3"));
        label_5 = new QLabel(gBConnInfo_3);
        label_5->setObjectName(QString::fromUtf8("label_5"));

        horizontalLayout_3->addWidget(label_5);

        txtAddressPort = new QLineEdit(gBConnInfo_3);
        txtAddressPort->setObjectName(QString::fromUtf8("txtAddressPort"));
        txtAddressPort->setAlignment(Qt::AlignCenter);

        horizontalLayout_3->addWidget(txtAddressPort);

        btnSetPortAddress = new QPushButton(gBConnInfo_3);
        btnSetPortAddress->setObjectName(QString::fromUtf8("btnSetPortAddress"));

        horizontalLayout_3->addWidget(btnSetPortAddress);

        gBConnInfo = new QGroupBox(groupBox);
        gBConnInfo->setObjectName(QString::fromUtf8("gBConnInfo"));
        gBConnInfo->setGeometry(QRect(30, 20, 681, 57));
        horizontalLayout = new QHBoxLayout(gBConnInfo);
        horizontalLayout->setObjectName(QString::fromUtf8("horizontalLayout"));
        label = new QLabel(gBConnInfo);
        label->setObjectName(QString::fromUtf8("label"));

        horizontalLayout->addWidget(label);

        txtIPAddr = new QLineEdit(gBConnInfo);
        txtIPAddr->setObjectName(QString::fromUtf8("txtIPAddr"));
        txtIPAddr->setAlignment(Qt::AlignCenter);
        txtIPAddr->setClearButtonEnabled(true);

        horizontalLayout->addWidget(txtIPAddr);

        label_2 = new QLabel(gBConnInfo);
        label_2->setObjectName(QString::fromUtf8("label_2"));

        horizontalLayout->addWidget(label_2);

        txtPort = new QLineEdit(gBConnInfo);
        txtPort->setObjectName(QString::fromUtf8("txtPort"));
        txtPort->setAlignment(Qt::AlignCenter);

        horizontalLayout->addWidget(txtPort);

        btnConnect = new QPushButton(gBConnInfo);
        btnConnect->setObjectName(QString::fromUtf8("btnConnect"));

        horizontalLayout->addWidget(btnConnect);

        chkFuanucEnable = new QCheckBox(gBConnInfo);
        chkFuanucEnable->setObjectName(QString::fromUtf8("chkFuanucEnable"));
        chkFuanucEnable->setChecked(true);

        horizontalLayout->addWidget(chkFuanucEnable);

        gBConnInfo_4 = new QGroupBox(groupBox);
        gBConnInfo_4->setObjectName(QString::fromUtf8("gBConnInfo_4"));
        gBConnInfo_4->setGeometry(QRect(30, 140, 681, 57));
        horizontalLayout_4 = new QHBoxLayout(gBConnInfo_4);
        horizontalLayout_4->setObjectName(QString::fromUtf8("horizontalLayout_4"));
        label_6 = new QLabel(gBConnInfo_4);
        label_6->setObjectName(QString::fromUtf8("label_6"));

        horizontalLayout_4->addWidget(label_6);

        txtAddressPressure2 = new QLineEdit(gBConnInfo_4);
        txtAddressPressure2->setObjectName(QString::fromUtf8("txtAddressPressure2"));
        txtAddressPressure2->setAlignment(Qt::AlignCenter);

        horizontalLayout_4->addWidget(txtAddressPressure2);

        btnSetPressureAddress2 = new QPushButton(gBConnInfo_4);
        btnSetPressureAddress2->setObjectName(QString::fromUtf8("btnSetPressureAddress2"));

        horizontalLayout_4->addWidget(btnSetPressureAddress2);

        gBConnInfo_5 = new QGroupBox(groupBox);
        gBConnInfo_5->setObjectName(QString::fromUtf8("gBConnInfo_5"));
        gBConnInfo_5->setGeometry(QRect(30, 260, 681, 51));
        horizontalLayout_5 = new QHBoxLayout(gBConnInfo_5);
        horizontalLayout_5->setObjectName(QString::fromUtf8("horizontalLayout_5"));
        radio2Pump = new QRadioButton(gBConnInfo_5);
        radio2Pump->setObjectName(QString::fromUtf8("radio2Pump"));
        radio2Pump->setChecked(true);

        horizontalLayout_5->addWidget(radio2Pump);

        radio1Pump = new QRadioButton(gBConnInfo_5);
        radio1Pump->setObjectName(QString::fromUtf8("radio1Pump"));

        horizontalLayout_5->addWidget(radio1Pump);

        line = new QFrame(Settings);
        line->setObjectName(QString::fromUtf8("line"));
        line->setGeometry(QRect(0, 40, 801, 21));
        line->setFrameShape(QFrame::HLine);
        line->setFrameShadow(QFrame::Sunken);
        label_3 = new QLabel(Settings);
        label_3->setObjectName(QString::fromUtf8("label_3"));
        label_3->setGeometry(QRect(0, 14, 800, 22));
        QFont font1;
        font1.setPointSize(15);
        font1.setBold(true);
        font1.setUnderline(false);
        font1.setWeight(75);
        label_3->setFont(font1);
        label_3->setAlignment(Qt::AlignCenter);

        retranslateUi(Settings);

        QMetaObject::connectSlotsByName(Settings);
    } // setupUi

    void retranslateUi(QWidget *Settings)
    {
        Settings->setWindowTitle(QCoreApplication::translate("Settings", "Form", nullptr));
        btnClose->setText(QCoreApplication::translate("Settings", "Close", nullptr));
        groupBox->setTitle(QString());
        gBConnInfo_2->setTitle(QString());
        label_4->setText(QCoreApplication::translate("Settings", "<html><head/><body><p>Address for Pressure Setting 1 :</p></body></html>", nullptr));
        btnSetPressureAddress1->setText(QCoreApplication::translate("Settings", "Set", nullptr));
        gBConnInfo_3->setTitle(QString());
        label_5->setText(QCoreApplication::translate("Settings", "Address for Port Setting           :", nullptr));
        btnSetPortAddress->setText(QCoreApplication::translate("Settings", "Set", nullptr));
        gBConnInfo->setTitle(QString());
        label->setText(QCoreApplication::translate("Settings", "IP :", nullptr));
        label_2->setText(QCoreApplication::translate("Settings", "Port:", nullptr));
        btnConnect->setText(QCoreApplication::translate("Settings", "Set", nullptr));
        chkFuanucEnable->setText(QCoreApplication::translate("Settings", "Enable Fanuc", nullptr));
        gBConnInfo_4->setTitle(QString());
        label_6->setText(QCoreApplication::translate("Settings", "<html><head/><body><p>Address for Pressure Setting 2 :</p></body></html>", nullptr));
        btnSetPressureAddress2->setText(QCoreApplication::translate("Settings", "Set", nullptr));
        gBConnInfo_5->setTitle(QString());
        radio2Pump->setText(QCoreApplication::translate("Settings", "2 Pump Configuration", nullptr));
        radio1Pump->setText(QCoreApplication::translate("Settings", "1 Pump Configuration", nullptr));
        label_3->setText(QCoreApplication::translate("Settings", "Settings", nullptr));
    } // retranslateUi

};

namespace Ui {
    class Settings: public Ui_Settings {};
} // namespace Ui

QT_END_NAMESPACE

#endif // UI_SETTINGS_H
