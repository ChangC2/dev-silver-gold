#include "Global.h"
#include <QDateTime>
#include <QDebug>

QString intToBinaryString(int value) {
    QString binaryString = QString::number(value, 2);

    while (binaryString.length() < 8) {
        binaryString.prepend('0');
    }

    return binaryString;
}

QBitArray intToBitArray(int value) {
    QBitArray bitArray(8);
    for (int i = 0; i < 8; i++) {
        bitArray.setBit(i, (value & (1 << i)));
    }

    return bitArray;
}

QVector<quint16> floatToRegisters(float value) {
    QVector<quint16> registers(2);
    quint32 intValue = *reinterpret_cast<quint32*>(&value);
    registers[0] = static_cast<quint16>(intValue);
    registers[1] = static_cast<quint16>(intValue >> 16);
    return registers;
}

float registersToFloat(const QVector<quint16>& registers) {
    quint32 intValue = (static_cast<quint32>(registers[1]) << 16) | static_cast<quint32>(registers[0]);
    return *reinterpret_cast<float*>(&intValue);
}

qint64 getCurrentTimeMilis() {
    qint64 timeStamp1 = QDateTime::currentDateTimeUtc().toMSecsSinceEpoch();
    // qint64 timeStamp2 = QDateTime::currentDateTime().toMSecsSinceEpoch();
    // Two values are same.
    // qDebug() << timeStamp1 << timeStamp2;
    return timeStamp1;
}
