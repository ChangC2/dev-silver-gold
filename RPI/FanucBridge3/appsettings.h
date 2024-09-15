#ifndef APPSETTINGS_H
#define APPSETTINGS_H

#include <QCoreApplication>
#include <QSettings>

class AppSettings
{

private:

    template<typename T>
    T getValue(QString key, T defVal);

public:
    AppSettings();
    /*static AppSettings* getInstance()
    {
        if (_instance == NULL) {
            _instance = new AppSettings();
        }

        return _instance;
    }*/

    int getIntValue(QString key, int defVal = 0);
    long getLongValue(QString key, long defVal = 0);
    QString getStringValue(QString key, QString defVal = "");
    float getFloatValue(QString key, float defVal = 0);

    //template<typename T>
    //void setValue(QString key, T val);

    void setValue(QString key, int val);
    void setValue(QString key, long val);
    void setValue(QString key, QString val);
    void setValue(QString key, float val);

private:
    static AppSettings* _instance;
    QSettings* settings;
};

#endif // APPSETTINGS_H
