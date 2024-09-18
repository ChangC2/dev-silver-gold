// Digital Read
#define inCyclePin 32
#define warningPin 35
#define alarmPin 34

void setDigitalPins()
{
    pinMode(inCyclePin, INPUT);
    pinMode(warningPin, INPUT);
    pinMode(alarmPin, INPUT);
}

int getMachineStatus()
{
    if (digitalRead(inCyclePin) == HIGH && digitalRead(warningPin) == HIGH && digitalRead(alarmPin) == HIGH)
    {
        return 0; // Idle Uncategorized
    }

    if (digitalRead(inCyclePin) == LOW && digitalRead(warningPin) == HIGH && digitalRead(alarmPin) == HIGH)
    {
        return 1; // In Cycle
    }

    if (digitalRead(warningPin) == LOW && digitalRead(inCyclePin) == HIGH && digitalRead(alarmPin) == HIGH)
    {
        return 2; // Idle - Machine Warning
    }

    if (digitalRead(alarmPin) == LOW && digitalRead(inCyclePin) == HIGH && digitalRead(warningPin) == HIGH)
    {
        return 3; // Idle - Machine Alarm
    }

    return 0;
}