// Digital Read
#define inCyclePin 32
#define warningPin 35
#define alarmPin 34
#define csLockPin 2

void setDigitalPins()
{
    pinMode(inCyclePin, INPUT);
    pinMode(warningPin, INPUT);
    pinMode(alarmPin, INPUT);
    pinMode(alarmPin, INPUT);
    pinMode(csLockPin, OUTPUT);
}

void sendCSLockSignal()
{
    // The code to get the current machine status
    Serial.println("sendCSLockSingal");
    if (cslock_reverse == 0)
    {
        digitalWrite(csLockPin, HIGH);
    }
    else
    {
        digitalWrite(csLockPin, LOW);
    }
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

    if (digitalRead(inCyclePin) == LOW && digitalRead(warningPin) == LOW && digitalRead(alarmPin) == HIGH) /// For Grob, Yellow light is not op stop, it is maint task due. Yellow and green can be on when inCycle=1
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
