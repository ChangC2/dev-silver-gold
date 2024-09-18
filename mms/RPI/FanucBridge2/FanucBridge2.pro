QT  += \
    core \
    gui \
    network \
    websockets \
    serialbus

greaterThan(QT_MAJOR_VERSION, 4): QT += widgets

CONFIG += c++11 \
    static

# You can make your code fail to compile if it uses deprecated APIs.
# In order to do so, uncomment the following line.
#DEFINES += QT_DISABLE_DEPRECATED_BEFORE=0x060000    # disables all the APIs deprecated before Qt 6.0.0

SOURCES += \
    Global.cpp \
    appsettings.cpp \
    apputils.cpp \
    bcm2835.c \
    fanuchttpserver.cpp \
    glgdataserver.cpp \
    mmsmodbustcpserver.cpp \
    pid.cpp \
    spiComm.cpp \
    spivaluereader.cpp \
    statusreporttcpclient.cpp \
    widget.cpp \
    main.cpp

HEADERS += \
    Global.h \
    appsettings.h \
    apputils.h \
    bcm2835.h \
    fanuchttpserver.h \
    glgdataserver.h \
    include/fwlib32.h \
    mmsmodbustcpserver.h \
    pid.h \
    spiComm.h \
    spivaluereader.h \
    statusreporttcpclient.h \
    widget.h

FORMS += \
    widget.ui

LIBS += -L$$PWD/lib/ -lfwlib32-linux-armv7

# Default rules for deployment.
qnx: target.path = /tmp/$${TARGET}/bin
else: unix:!android: target.path = /opt/$${TARGET}/bin
!isEmpty(target.path): INSTALLS += target

DISTFILES += \
    assets/index.html \
    assets/script.js \
    assets/styles.css \
    lib/libfwlib32-linux-armv7.so \
    runapp.sh

# unix:!macx: LIBS += -L$$PWD/lib/ -lfwlib32-linux-armv7
# INCLUDEPATH += $$PWD/include
# DEPENDPATH += $$PWD/include

DESTDIR = $$PWD/Builds
OBJECTS_DIR = ../FanucBridge2Builds
MOC_DIR = ../FanucBridge2Builds
