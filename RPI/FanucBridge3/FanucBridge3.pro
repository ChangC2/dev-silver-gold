QT  += \
    core \
    gui \
    network \
    websockets \
    serialbus \
    serialport

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
    mainwindow.cpp \
    settings.cpp \
    main.cpp \
    worker.cpp

HEADERS += \
    Global.h \
    appsettings.h \
    apputils.h \
    include/fwlib32.h \
    mainwindow.h \
    settings.h \
    worker.h

FORMS += \
    mainwindow.ui \
    settings.ui

LIBS += -L$$PWD/lib/ -lfwlib32

# Default rules for deployment.
qnx: target.path = /tmp/$${TARGET}/bin
else: unix:!android: target.path = /opt/$${TARGET}/bin
!isEmpty(target.path): INSTALLS += target

DISTFILES += \
    assets/index.html \
    assets/script.js \
    assets/styles.css \
    lib/libfwlib32.so \
    runapp.sh

# unix:!macx: LIBS += -L$$PWD/lib/ -lfwlib32
# INCLUDEPATH += $$PWD/include
# DEPENDPATH += $$PWD/include

DESTDIR = $$PWD/Builds
OBJECTS_DIR = $$PWD/OBJECTS_DIR
MOC_DIR = $$PWD/MOC_DIR

RESOURCES += \
    resources.qrc
