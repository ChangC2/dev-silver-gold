/********************************************************************************
** Form generated from reading UI file 'widget.ui'
**
** Created by: Qt User Interface Compiler version 5.15.2
**
** WARNING! All changes made in this file will be lost when recompiling UI file!
********************************************************************************/

#ifndef UI_WIDGET_H
#define UI_WIDGET_H

#include <QtCore/QVariant>
#include <QtGui/QIcon>
#include <QtWidgets/QApplication>
#include <QtWidgets/QCheckBox>
#include <QtWidgets/QComboBox>
#include <QtWidgets/QGroupBox>
#include <QtWidgets/QHBoxLayout>
#include <QtWidgets/QLabel>
#include <QtWidgets/QLineEdit>
#include <QtWidgets/QPushButton>
#include <QtWidgets/QSlider>
#include <QtWidgets/QTabWidget>
#include <QtWidgets/QWidget>

QT_BEGIN_NAMESPACE

class Ui_Widget
{
public:
    QPushButton *btnConnect;
    QGroupBox *gBConnInfo;
    QHBoxLayout *horizontalLayout;
    QLabel *label;
    QLineEdit *txtIPAddr;
    QLabel *label_2;
    QLineEdit *txtPort;
    QTabWidget *tabWidget;
    QWidget *tabMMS;
    QLabel *lblFanucInfo;
    QLabel *label_17;
    QLabel *lblMachineInfo;
    QLabel *label_16;
    QLabel *label_18;
    QLineEdit *txtCustomerID;
    QLabel *label_19;
    QLineEdit *txtMachineName;
    QCheckBox *chkTempInCycle;
    QWidget *tabUnitTest;
    QGroupBox *groupBox;
    QLabel *lblMacroVars;
    QPushButton *btnReadMacros;
    QGroupBox *groupBox_2;
    QLabel *lblSPIVars;
    QPushButton *pushButton;
    QWidget *tabCSV;
    QLabel *lblFanucReadIntervals;
    QLabel *label_13;
    QSlider *sliderFanucReadIntervals;
    QPushButton *btnFanucReadStartStop;
    QLabel *label_14;
    QLineEdit *txtHpX;
    QLabel *label_15;
    QLabel *lblCurrSeqNum;
    QLabel *lblModalS;
    QLabel *lblModalT;
    QLabel *lblModalF;
    QLabel *lblModalM;
    QWidget *tabPID;
    QSlider *sliderSP;
    QGroupBox *groupBox_3;
    QLineEdit *txtKp;
    QLabel *label_3;
    QLineEdit *txtKi;
    QLabel *label_4;
    QLineEdit *txtKd;
    QLabel *label_5;
    QLabel *label_6;
    QLabel *lblSPValue;
    QPushButton *btnPIDStartStop;
    QLabel *label_7;
    QSlider *sliderPIDIntervals;
    QLabel *lblPIDIntervals;
    QGroupBox *groupBox_4;
    QLabel *label_8;
    QLineEdit *txtPIDOutAddr;
    QLabel *label_9;
    QComboBox *cbPIDOoutRegType;
    QGroupBox *groupBox_5;
    QLineEdit *txtSPIAddr;
    QLabel *label_11;
    QLabel *label_12;
    QLabel *label_10;
    QLabel *lblPIDOut;

    void setupUi(QWidget *Widget)
    {
        if (Widget->objectName().isEmpty())
            Widget->setObjectName(QString::fromUtf8("Widget"));
        Widget->resize(600, 400);
        QIcon icon;
        icon.addFile(QString::fromUtf8("assets/favicon.png"), QSize(), QIcon::Normal, QIcon::Off);
        Widget->setWindowIcon(icon);
        btnConnect = new QPushButton(Widget);
        btnConnect->setObjectName(QString::fromUtf8("btnConnect"));
        btnConnect->setGeometry(QRect(440, 20, 121, 30));
        gBConnInfo = new QGroupBox(Widget);
        gBConnInfo->setObjectName(QString::fromUtf8("gBConnInfo"));
        gBConnInfo->setGeometry(QRect(10, 10, 411, 57));
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

        tabWidget = new QTabWidget(Widget);
        tabWidget->setObjectName(QString::fromUtf8("tabWidget"));
        tabWidget->setGeometry(QRect(10, 70, 581, 321));
        tabMMS = new QWidget();
        tabMMS->setObjectName(QString::fromUtf8("tabMMS"));
        lblFanucInfo = new QLabel(tabMMS);
        lblFanucInfo->setObjectName(QString::fromUtf8("lblFanucInfo"));
        lblFanucInfo->setGeometry(QRect(360, 50, 181, 91));
        QFont font;
        font.setPointSize(10);
        lblFanucInfo->setFont(font);
        lblFanucInfo->setAlignment(Qt::AlignLeading|Qt::AlignLeft|Qt::AlignTop);
        label_17 = new QLabel(tabMMS);
        label_17->setObjectName(QString::fromUtf8("label_17"));
        label_17->setGeometry(QRect(350, 20, 181, 30));
        lblMachineInfo = new QLabel(tabMMS);
        lblMachineInfo->setObjectName(QString::fromUtf8("lblMachineInfo"));
        lblMachineInfo->setGeometry(QRect(360, 180, 181, 91));
        lblMachineInfo->setFont(font);
        lblMachineInfo->setAlignment(Qt::AlignLeading|Qt::AlignLeft|Qt::AlignTop);
        label_16 = new QLabel(tabMMS);
        label_16->setObjectName(QString::fromUtf8("label_16"));
        label_16->setGeometry(QRect(350, 150, 181, 30));
        label_18 = new QLabel(tabMMS);
        label_18->setObjectName(QString::fromUtf8("label_18"));
        label_18->setGeometry(QRect(16, 30, 111, 30));
        txtCustomerID = new QLineEdit(tabMMS);
        txtCustomerID->setObjectName(QString::fromUtf8("txtCustomerID"));
        txtCustomerID->setGeometry(QRect(140, 30, 159, 30));
        txtCustomerID->setAlignment(Qt::AlignCenter);
        txtCustomerID->setClearButtonEnabled(true);
        label_19 = new QLabel(tabMMS);
        label_19->setObjectName(QString::fromUtf8("label_19"));
        label_19->setGeometry(QRect(20, 90, 121, 30));
        txtMachineName = new QLineEdit(tabMMS);
        txtMachineName->setObjectName(QString::fromUtf8("txtMachineName"));
        txtMachineName->setGeometry(QRect(140, 90, 158, 30));
        txtMachineName->setAlignment(Qt::AlignCenter);
        chkTempInCycle = new QCheckBox(tabMMS);
        chkTempInCycle->setObjectName(QString::fromUtf8("chkTempInCycle"));
        chkTempInCycle->setGeometry(QRect(20, 150, 241, 28));
        tabWidget->addTab(tabMMS, QString());
        tabUnitTest = new QWidget();
        tabUnitTest->setObjectName(QString::fromUtf8("tabUnitTest"));
        groupBox = new QGroupBox(tabUnitTest);
        groupBox->setObjectName(QString::fromUtf8("groupBox"));
        groupBox->setGeometry(QRect(20, 50, 181, 225));
        QFont font1;
        font1.setPointSize(10);
        font1.setBold(false);
        font1.setItalic(false);
        font1.setWeight(50);
        groupBox->setFont(font1);
        lblMacroVars = new QLabel(groupBox);
        lblMacroVars->setObjectName(QString::fromUtf8("lblMacroVars"));
        lblMacroVars->setGeometry(QRect(10, 30, 161, 191));
        lblMacroVars->setTextFormat(Qt::PlainText);
        lblMacroVars->setAlignment(Qt::AlignLeading|Qt::AlignLeft|Qt::AlignTop);
        btnReadMacros = new QPushButton(tabUnitTest);
        btnReadMacros->setObjectName(QString::fromUtf8("btnReadMacros"));
        btnReadMacros->setGeometry(QRect(20, 10, 181, 30));
        groupBox_2 = new QGroupBox(tabUnitTest);
        groupBox_2->setObjectName(QString::fromUtf8("groupBox_2"));
        groupBox_2->setGeometry(QRect(210, 50, 161, 225));
        groupBox_2->setFont(font1);
        lblSPIVars = new QLabel(groupBox_2);
        lblSPIVars->setObjectName(QString::fromUtf8("lblSPIVars"));
        lblSPIVars->setGeometry(QRect(10, 30, 141, 191));
        lblSPIVars->setTextFormat(Qt::PlainText);
        lblSPIVars->setAlignment(Qt::AlignLeading|Qt::AlignLeft|Qt::AlignTop);
        pushButton = new QPushButton(tabUnitTest);
        pushButton->setObjectName(QString::fromUtf8("pushButton"));
        pushButton->setGeometry(QRect(210, 10, 161, 30));
        tabWidget->addTab(tabUnitTest, QString());
        tabCSV = new QWidget();
        tabCSV->setObjectName(QString::fromUtf8("tabCSV"));
        lblFanucReadIntervals = new QLabel(tabCSV);
        lblFanucReadIntervals->setObjectName(QString::fromUtf8("lblFanucReadIntervals"));
        lblFanucReadIntervals->setGeometry(QRect(100, 20, 61, 22));
        QFont font2;
        font2.setBold(true);
        font2.setWeight(75);
        lblFanucReadIntervals->setFont(font2);
        label_13 = new QLabel(tabCSV);
        label_13->setObjectName(QString::fromUtf8("label_13"));
        label_13->setGeometry(QRect(10, 20, 81, 22));
        QFont font3;
        font3.setPointSize(10);
        font3.setBold(false);
        font3.setWeight(50);
        label_13->setFont(font3);
        sliderFanucReadIntervals = new QSlider(tabCSV);
        sliderFanucReadIntervals->setObjectName(QString::fromUtf8("sliderFanucReadIntervals"));
        sliderFanucReadIntervals->setGeometry(QRect(10, 40, 550, 20));
        sliderFanucReadIntervals->setMinimum(5);
        sliderFanucReadIntervals->setMaximum(1000);
        sliderFanucReadIntervals->setSingleStep(5);
        sliderFanucReadIntervals->setPageStep(50);
        sliderFanucReadIntervals->setOrientation(Qt::Horizontal);
        btnFanucReadStartStop = new QPushButton(tabCSV);
        btnFanucReadStartStop->setObjectName(QString::fromUtf8("btnFanucReadStartStop"));
        btnFanucReadStartStop->setGeometry(QRect(470, 65, 91, 30));
        label_14 = new QLabel(tabCSV);
        label_14->setObjectName(QString::fromUtf8("label_14"));
        label_14->setGeometry(QRect(15, 72, 101, 22));
        label_14->setFont(font);
        txtHpX = new QLineEdit(tabCSV);
        txtHpX->setObjectName(QString::fromUtf8("txtHpX"));
        txtHpX->setGeometry(QRect(120, 68, 101, 30));
        txtHpX->setAlignment(Qt::AlignCenter);
        label_15 = new QLabel(tabCSV);
        label_15->setObjectName(QString::fromUtf8("label_15"));
        label_15->setGeometry(QRect(225, 70, 211, 22));
        label_15->setFont(font);
        lblCurrSeqNum = new QLabel(tabCSV);
        lblCurrSeqNum->setObjectName(QString::fromUtf8("lblCurrSeqNum"));
        lblCurrSeqNum->setGeometry(QRect(15, 130, 231, 22));
        lblCurrSeqNum->setFont(font);
        lblModalS = new QLabel(tabCSV);
        lblModalS->setObjectName(QString::fromUtf8("lblModalS"));
        lblModalS->setGeometry(QRect(15, 160, 231, 22));
        lblModalS->setFont(font);
        lblModalT = new QLabel(tabCSV);
        lblModalT->setObjectName(QString::fromUtf8("lblModalT"));
        lblModalT->setGeometry(QRect(15, 190, 231, 22));
        lblModalT->setFont(font);
        lblModalF = new QLabel(tabCSV);
        lblModalF->setObjectName(QString::fromUtf8("lblModalF"));
        lblModalF->setGeometry(QRect(15, 220, 231, 22));
        lblModalF->setFont(font);
        lblModalM = new QLabel(tabCSV);
        lblModalM->setObjectName(QString::fromUtf8("lblModalM"));
        lblModalM->setGeometry(QRect(15, 250, 231, 22));
        lblModalM->setFont(font);
        tabWidget->addTab(tabCSV, QString());
        tabPID = new QWidget();
        tabPID->setObjectName(QString::fromUtf8("tabPID"));
        sliderSP = new QSlider(tabPID);
        sliderSP->setObjectName(QString::fromUtf8("sliderSP"));
        sliderSP->setGeometry(QRect(10, 200, 361, 20));
        sliderSP->setMaximum(200);
        sliderSP->setOrientation(Qt::Horizontal);
        groupBox_3 = new QGroupBox(tabPID);
        groupBox_3->setObjectName(QString::fromUtf8("groupBox_3"));
        groupBox_3->setGeometry(QRect(10, 10, 161, 151));
        txtKp = new QLineEdit(groupBox_3);
        txtKp->setObjectName(QString::fromUtf8("txtKp"));
        txtKp->setGeometry(QRect(40, 20, 101, 30));
        txtKp->setAlignment(Qt::AlignCenter);
        label_3 = new QLabel(groupBox_3);
        label_3->setObjectName(QString::fromUtf8("label_3"));
        label_3->setGeometry(QRect(15, 22, 21, 22));
        label_3->setFont(font);
        txtKi = new QLineEdit(groupBox_3);
        txtKi->setObjectName(QString::fromUtf8("txtKi"));
        txtKi->setGeometry(QRect(40, 60, 101, 30));
        txtKi->setAlignment(Qt::AlignCenter);
        label_4 = new QLabel(groupBox_3);
        label_4->setObjectName(QString::fromUtf8("label_4"));
        label_4->setGeometry(QRect(15, 62, 21, 22));
        label_4->setFont(font);
        txtKd = new QLineEdit(groupBox_3);
        txtKd->setObjectName(QString::fromUtf8("txtKd"));
        txtKd->setGeometry(QRect(40, 100, 101, 30));
        txtKd->setAlignment(Qt::AlignCenter);
        label_5 = new QLabel(groupBox_3);
        label_5->setObjectName(QString::fromUtf8("label_5"));
        label_5->setGeometry(QRect(15, 102, 21, 22));
        label_5->setFont(font);
        label_6 = new QLabel(tabPID);
        label_6->setObjectName(QString::fromUtf8("label_6"));
        label_6->setGeometry(QRect(10, 180, 55, 22));
        lblSPValue = new QLabel(tabPID);
        lblSPValue->setObjectName(QString::fromUtf8("lblSPValue"));
        lblSPValue->setGeometry(QRect(70, 180, 61, 22));
        lblSPValue->setFont(font2);
        btnPIDStartStop = new QPushButton(tabPID);
        btnPIDStartStop->setObjectName(QString::fromUtf8("btnPIDStartStop"));
        btnPIDStartStop->setGeometry(QRect(430, 230, 131, 30));
        label_7 = new QLabel(tabPID);
        label_7->setObjectName(QString::fromUtf8("label_7"));
        label_7->setGeometry(QRect(10, 230, 60, 22));
        label_7->setFont(font3);
        sliderPIDIntervals = new QSlider(tabPID);
        sliderPIDIntervals->setObjectName(QString::fromUtf8("sliderPIDIntervals"));
        sliderPIDIntervals->setGeometry(QRect(10, 250, 361, 20));
        sliderPIDIntervals->setMinimum(5);
        sliderPIDIntervals->setMaximum(1000);
        sliderPIDIntervals->setSingleStep(5);
        sliderPIDIntervals->setPageStep(50);
        sliderPIDIntervals->setOrientation(Qt::Horizontal);
        lblPIDIntervals = new QLabel(tabPID);
        lblPIDIntervals->setObjectName(QString::fromUtf8("lblPIDIntervals"));
        lblPIDIntervals->setGeometry(QRect(70, 230, 61, 22));
        lblPIDIntervals->setFont(font2);
        groupBox_4 = new QGroupBox(tabPID);
        groupBox_4->setObjectName(QString::fromUtf8("groupBox_4"));
        groupBox_4->setGeometry(QRect(380, 10, 195, 151));
        QFont font4;
        font4.setPointSize(11);
        font4.setBold(true);
        font4.setItalic(true);
        font4.setWeight(75);
        groupBox_4->setFont(font4);
        label_8 = new QLabel(groupBox_4);
        label_8->setObjectName(QString::fromUtf8("label_8"));
        label_8->setGeometry(QRect(15, 50, 35, 22));
        QFont font5;
        font5.setPointSize(10);
        font5.setBold(true);
        font5.setItalic(false);
        font5.setWeight(75);
        label_8->setFont(font5);
        txtPIDOutAddr = new QLineEdit(groupBox_4);
        txtPIDOutAddr->setObjectName(QString::fromUtf8("txtPIDOutAddr"));
        txtPIDOutAddr->setGeometry(QRect(65, 90, 121, 30));
        txtPIDOutAddr->setFont(font1);
        txtPIDOutAddr->setAlignment(Qt::AlignCenter);
        label_9 = new QLabel(groupBox_4);
        label_9->setObjectName(QString::fromUtf8("label_9"));
        label_9->setGeometry(QRect(10, 92, 53, 22));
        label_9->setFont(font5);
        cbPIDOoutRegType = new QComboBox(groupBox_4);
        cbPIDOoutRegType->addItem(QString());
        cbPIDOoutRegType->addItem(QString());
        cbPIDOoutRegType->addItem(QString());
        cbPIDOoutRegType->addItem(QString());
        cbPIDOoutRegType->addItem(QString());
        cbPIDOoutRegType->addItem(QString());
        cbPIDOoutRegType->addItem(QString());
        cbPIDOoutRegType->addItem(QString());
        cbPIDOoutRegType->addItem(QString());
        cbPIDOoutRegType->addItem(QString());
        cbPIDOoutRegType->addItem(QString());
        cbPIDOoutRegType->addItem(QString());
        cbPIDOoutRegType->addItem(QString());
        cbPIDOoutRegType->addItem(QString());
        cbPIDOoutRegType->setObjectName(QString::fromUtf8("cbPIDOoutRegType"));
        cbPIDOoutRegType->setGeometry(QRect(65, 50, 121, 30));
        QFont font6;
        font6.setPointSize(9);
        font6.setBold(false);
        font6.setItalic(false);
        font6.setWeight(50);
        cbPIDOoutRegType->setFont(font6);
        groupBox_5 = new QGroupBox(tabPID);
        groupBox_5->setObjectName(QString::fromUtf8("groupBox_5"));
        groupBox_5->setGeometry(QRect(175, 10, 200, 151));
        groupBox_5->setFont(font4);
        txtSPIAddr = new QLineEdit(groupBox_5);
        txtSPIAddr->setObjectName(QString::fromUtf8("txtSPIAddr"));
        txtSPIAddr->setGeometry(QRect(63, 50, 131, 30));
        txtSPIAddr->setFont(font1);
        txtSPIAddr->setAlignment(Qt::AlignCenter);
        label_11 = new QLabel(groupBox_5);
        label_11->setObjectName(QString::fromUtf8("label_11"));
        label_11->setGeometry(QRect(9, 52, 52, 22));
        label_11->setFont(font5);
        label_12 = new QLabel(groupBox_5);
        label_12->setObjectName(QString::fromUtf8("label_12"));
        label_12->setGeometry(QRect(63, 80, 131, 22));
        label_12->setFont(font5);
        label_10 = new QLabel(tabPID);
        label_10->setObjectName(QString::fromUtf8("label_10"));
        label_10->setGeometry(QRect(400, 180, 45, 22));
        label_10->setFont(font5);
        lblPIDOut = new QLabel(tabPID);
        lblPIDOut->setObjectName(QString::fromUtf8("lblPIDOut"));
        lblPIDOut->setGeometry(QRect(450, 180, 121, 22));
        lblPIDOut->setFont(font5);
        tabWidget->addTab(tabPID, QString());
        QWidget::setTabOrder(txtIPAddr, txtPort);
        QWidget::setTabOrder(txtPort, btnConnect);

        retranslateUi(Widget);

        tabWidget->setCurrentIndex(0);


        QMetaObject::connectSlotsByName(Widget);
    } // setupUi

    void retranslateUi(QWidget *Widget)
    {
        Widget->setWindowTitle(QCoreApplication::translate("Widget", "Widget", nullptr));
        btnConnect->setText(QCoreApplication::translate("Widget", "Connect", nullptr));
        gBConnInfo->setTitle(QString());
        label->setText(QCoreApplication::translate("Widget", "IP :", nullptr));
        label_2->setText(QCoreApplication::translate("Widget", "Port:", nullptr));
        lblFanucInfo->setText(QString());
        label_17->setText(QCoreApplication::translate("Widget", "Fanuc Information", nullptr));
        lblMachineInfo->setText(QString());
        label_16->setText(QCoreApplication::translate("Widget", "Current Machine Status", nullptr));
        label_18->setText(QCoreApplication::translate("Widget", "Customer ID:", nullptr));
        label_19->setText(QCoreApplication::translate("Widget", "Machine Name:", nullptr));
        chkTempInCycle->setText(QCoreApplication::translate("Widget", "Send Test In Cycle Signal", nullptr));
        tabWidget->setTabText(tabWidget->indexOf(tabMMS), QCoreApplication::translate("Widget", "MMS", nullptr));
        groupBox->setTitle(QCoreApplication::translate("Widget", "Macro Variables(100~110)", nullptr));
        lblMacroVars->setText(QCoreApplication::translate("Widget", "0\n"
"0\n"
"0\n"
"", nullptr));
        btnReadMacros->setText(QCoreApplication::translate("Widget", "Read Macros(100~110)", nullptr));
        groupBox_2->setTitle(QCoreApplication::translate("Widget", "SPI Values", nullptr));
        lblSPIVars->setText(QCoreApplication::translate("Widget", "Pow1\n"
"Pow2\n"
"Pow3", nullptr));
        pushButton->setText(QCoreApplication::translate("Widget", "Read SPI Values", nullptr));
        tabWidget->setTabText(tabWidget->indexOf(tabUnitTest), QCoreApplication::translate("Widget", "Unit Test", nullptr));
        lblFanucReadIntervals->setText(QCoreApplication::translate("Widget", "<html><head/><body><p>5 ms</p></body></html>", nullptr));
        label_13->setText(QCoreApplication::translate("Widget", "Read  Cycle:", nullptr));
        btnFanucReadStartStop->setText(QCoreApplication::translate("Widget", "Start", nullptr));
        label_14->setText(QCoreApplication::translate("Widget", "<html><head/><body><p>HP Threshold X:</p></body></html>", nullptr));
        label_15->setText(QCoreApplication::translate("Widget", "<html><head/><body><p>:Only records csv when HP &gt;= X</p></body></html>", nullptr));
        lblCurrSeqNum->setText(QCoreApplication::translate("Widget", "<html><head/><body><p>Current Sequence Number:</p></body></html>", nullptr));
        lblModalS->setText(QCoreApplication::translate("Widget", "<html><head/><body><p>S Modal:</p></body></html>", nullptr));
        lblModalT->setText(QCoreApplication::translate("Widget", "<html><head/><body><p>T Modal:</p></body></html>", nullptr));
        lblModalF->setText(QCoreApplication::translate("Widget", "<html><head/><body><p>F Modal:</p></body></html>", nullptr));
        lblModalM->setText(QCoreApplication::translate("Widget", "<html><head/><body><p>M Modal:</p></body></html>", nullptr));
        tabWidget->setTabText(tabWidget->indexOf(tabCSV), QCoreApplication::translate("Widget", "ML/AI CSV", nullptr));
        label_3->setText(QCoreApplication::translate("Widget", "<html><head/><body><p><span style=\" font-style:italic;\">k</span><span style=\" font-weight:600;\">P</span>:</p></body></html>", nullptr));
        label_4->setText(QCoreApplication::translate("Widget", "<html><head/><body><p><span style=\" font-style:italic;\">k</span><span style=\" font-weight:600;\">I</span>:</p></body></html>", nullptr));
        label_5->setText(QCoreApplication::translate("Widget", "<html><head/><body><p><span style=\" font-style:italic;\">k</span><span style=\" font-weight:600;\">D</span>:</p></body></html>", nullptr));
        label_6->setText(QCoreApplication::translate("Widget", "<html><head/><body><p><span style=\" font-size:10pt; font-weight:600;\">S</span><span style=\" font-size:10pt;\">et</span><span style=\" font-size:10pt; font-weight:600;\">P</span><span style=\" font-size:10pt;\">oint</span><span style=\" font-size:10pt; font-weight:600;\">:</span></p></body></html>", nullptr));
        lblSPValue->setText(QCoreApplication::translate("Widget", "<html><head/><body><p>0.0</p></body></html>", nullptr));
        btnPIDStartStop->setText(QCoreApplication::translate("Widget", "Start PID", nullptr));
        label_7->setText(QCoreApplication::translate("Widget", "Interval:", nullptr));
        lblPIDIntervals->setText(QCoreApplication::translate("Widget", "<html><head/><body><p>5 ms</p></body></html>", nullptr));
        groupBox_4->setTitle(QCoreApplication::translate("Widget", "PID Output", nullptr));
        label_8->setText(QCoreApplication::translate("Widget", "<html><head/><body><p><span style=\" font-weight:400;\">Type:</span></p></body></html>", nullptr));
        label_9->setText(QCoreApplication::translate("Widget", "<html><head/><body><p><span style=\" font-weight:400;\">Address</span><span style=\" font-weight:400;\">:</span></p></body></html>", nullptr));
        cbPIDOoutRegType->setItemText(0, QCoreApplication::translate("Widget", "G (Output signal from PMC to CNC)", nullptr));
        cbPIDOoutRegType->setItemText(1, QCoreApplication::translate("Widget", "F (Input Signal to PMC from CNC)", nullptr));
        cbPIDOoutRegType->setItemText(2, QCoreApplication::translate("Widget", "Y (Output signal from PMC to machine)", nullptr));
        cbPIDOoutRegType->setItemText(3, QCoreApplication::translate("Widget", "X (Input signal to PMC from machine)", nullptr));
        cbPIDOoutRegType->setItemText(4, QCoreApplication::translate("Widget", "A (Message display)", nullptr));
        cbPIDOoutRegType->setItemText(5, QCoreApplication::translate("Widget", "R (Internal/System relay)", nullptr));
        cbPIDOoutRegType->setItemText(6, QCoreApplication::translate("Widget", "T (Timer)", nullptr));
        cbPIDOoutRegType->setItemText(7, QCoreApplication::translate("Widget", "K (Keep relay)", nullptr));
        cbPIDOoutRegType->setItemText(8, QCoreApplication::translate("Widget", "C (Counter)", nullptr));
        cbPIDOoutRegType->setItemText(9, QCoreApplication::translate("Widget", "D (Data table)", nullptr));
        cbPIDOoutRegType->setItemText(10, QCoreApplication::translate("Widget", "M (Input signal from other PMC path)", nullptr));
        cbPIDOoutRegType->setItemText(11, QCoreApplication::translate("Widget", "N (Output signal to other PMC path)", nullptr));
        cbPIDOoutRegType->setItemText(12, QCoreApplication::translate("Widget", "E (Extra relay)", nullptr));
        cbPIDOoutRegType->setItemText(13, QCoreApplication::translate("Widget", "Z (System relay)", nullptr));

        groupBox_5->setTitle(QCoreApplication::translate("Widget", "SPI Reading", nullptr));
        label_11->setText(QCoreApplication::translate("Widget", "<html><head/><body><p><span style=\" font-weight:400;\">Address</span><span style=\" font-weight:400;\">:</span></p></body></html>", nullptr));
        label_12->setText(QCoreApplication::translate("Widget", "<html><head/><body><p><span style=\" font-weight:400;\">Hex Value. ex: 12AB</span></p></body></html>", nullptr));
        label_10->setText(QCoreApplication::translate("Widget", "<html><head/><body><p><span style=\" font-weight:400;\">Output:</span></p></body></html>", nullptr));
        lblPIDOut->setText(QCoreApplication::translate("Widget", "00000000", nullptr));
        tabWidget->setTabText(tabWidget->indexOf(tabPID), QCoreApplication::translate("Widget", "PID", nullptr));
    } // retranslateUi

};

namespace Ui {
    class Widget: public Ui_Widget {};
} // namespace Ui

QT_END_NAMESPACE

#endif // UI_WIDGET_H
