#include "appsettings.h"
#include <QMetaType>
#include <QVariant>

AppSettings::AppSettings() {
    settings = new QSettings(QCoreApplication::organizationName(), QCoreApplication::applicationName());
}

int AppSettings::getIntValue(QString key, int defVal) {
    QVariant savedValue = settings->value(key);
    if (savedValue.isValid() && savedValue.canConvert<int>()) {
        return savedValue.toInt();
    } else {
        return defVal;
    }
}

long AppSettings::getLongValue(QString key, long defVal) {
    QVariant savedValue = settings->value(key);
    if (savedValue.isValid() && savedValue.canConvert<long>()) {
        return savedValue.toLongLong();
    } else {
        return defVal;
    }
}

QString AppSettings::getStringValue(QString key, QString defVal) {
    QVariant savedValue = settings->value(key);
    if (savedValue.isValid() && savedValue.canConvert<QString>()) {
        return savedValue.toString();
    } else {
        return defVal;
    }
}

float AppSettings::getFloatValue(QString key, float defVal) {
    QVariant savedValue = settings->value(key);
    if (savedValue.isValid() && savedValue.canConvert<float>()) {
        return savedValue.toFloat();
    } else {
        return defVal;
    }
}

template<typename T>
T AppSettings::getValue(QString key, T defVal) {
    QVariant savedValue = settings->value(key);
    if (savedValue.isValid() && savedValue.canConvert<T>()) {
        return savedValue.toString();
    } else {
        return defVal;
    }
}

/*
template<typename T>
void AppSettings::setValue(QString key, T val) {
    settings->setValue(key, QVariant::fromValue(val));
    settings->sync();
}
*/

void AppSettings::setValue(QString key, int val) {
    settings->setValue(key, val);
    settings->sync();
}

void AppSettings::setValue(QString key, long val) {
    settings->setValue(key, QVariant::fromValue(val));
    settings->sync();
}

void AppSettings::setValue(QString key, QString val) {
    settings->setValue(key, val);
    settings->sync();
}

void AppSettings::setValue(QString key, float val) {
    settings->setValue(key, val);
    settings->sync();
}
