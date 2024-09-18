#include "apputils.h"
#include <QRegularExpression>
#include <QRegularExpressionMatch>

AppUtils::AppUtils()
{

}

bool AppUtils::isValidIPAddress(const QString &ipAddress)
{
    QRegularExpression ipRegex("^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$");
    QRegularExpressionMatch match = ipRegex.match(ipAddress);
    return match.hasMatch();
}

bool AppUtils::isValidPort(const int port)
{
    if (port > 0 && port < 65536) {
        return true;
    }
    return false;
}

