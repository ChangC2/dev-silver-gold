#ifndef APPUTILS_H
#define APPUTILS_H

#include <QString>

class AppUtils
{
public:
    AppUtils();

    static bool isValidIPAddress(const QString &ipAddress);
    static bool isValidPort(const int port);
};

#endif // APPUTILS_H
