// Digital Read
#define din1 32
#define din2 35
#define din3 34
#define din4 39
#define csLockPin 2

void setDigitalPins()
{
    pinMode(din1, INPUT);
    pinMode(din2, INPUT);
    pinMode(din3, INPUT);
    pinMode(din4, INPUT);
    pinMode(csLockPin, OUTPUT);
}

void sendCSLockSignal()
{
    // The code to get the current device status
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
    if (digitalRead(din1) == HIGH && digitalRead(din2) == HIGH && digitalRead(din3) == HIGH)
    {
        return 0; // Idle Uncategorized
    }

    if (digitalRead(din1) == LOW && digitalRead(din2) == HIGH && digitalRead(din3) == HIGH)
    {
        return 1; // In Cycle
    }

    if (digitalRead(din2) == LOW && digitalRead(din1) == HIGH && digitalRead(din3) == HIGH)
    {
        return 2; // Idle - Machine Warning
    }

    if (digitalRead(din3) == LOW && digitalRead(din1) == HIGH && digitalRead(din2) == HIGH)
    {
        return 3; // Idle - Machine Alarm
    }

    return 0;
}


