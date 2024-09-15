#include "BluetoothSerial.h"

#if !defined(CONFIG_BT_ENABLED) || !defined(CONFIG_BLUEDROID_ENABLED)
#error Bluetooth is not enabled! Please run `make menuconfig` to and enable it
#endif

///////////////////// DEVICE ID (Set unique for each PLC) /////////////////////////
String deviceID = "MMS_8888";
///////////////////////////////////////////////////////////////////////////////////

BluetoothSerial SerialBT;

int rly1 = 2;
int rly2 = 4;
int rly3 = 16;
int rly4 = 17;
int i1 = 32;
int i2 = 35;
int i3 = 34;
int i4 = 39;
String str1 = "";
boolean stringComplete = false;
String p1;
String p2;
String p3;
String p4;
String dataString;
String input1Str;
String input2Str;
String input3Str;
String input4Str;

void setup() {
  Serial.begin(115200);
  SerialBT.begin(deviceID);  //Bluetooth device name
  Serial.println("The device started, now you can pair it with bluetooth!");
  pinMode(rly1, OUTPUT);
  pinMode(rly2, OUTPUT);
  pinMode(rly3, OUTPUT);
  pinMode(rly4, OUTPUT);
  pinMode(i1, INPUT);
  pinMode(i2, INPUT);
  pinMode(i3, INPUT);
  pinMode(i4, INPUT);
  //digitalWrite(rly2, HIGH);
  //inputString.reserve(200);
}

void loop() {
  if (Serial.available()) {
    SerialBT.write(Serial.read());
  }
  while (SerialBT.available() > 0) {
    str1 = SerialBT.readString();
    p1 = getValue(str1, ',', 0);
    p2 = getValue(str1, ',', 1);
    p3 = getValue(str1, ',', 2);
    p4 = getValue(str1, ',', 3);
    Serial.println(p1);
    if (p1 == "1") {
      digitalWrite(rly1, HIGH);
    } else {
      digitalWrite(rly1, LOW);
    }
    if (p2 == "1") {
      digitalWrite(rly2, LOW);
    } else {
      digitalWrite(rly2, HIGH);
    }
    if (p3 == "1") {
      digitalWrite(rly3, HIGH);
    } else {
      digitalWrite(rly3, LOW);
    }
    if (p4 == "1") {
      digitalWrite(rly4, HIGH);
    } else {
      digitalWrite(rly4, LOW);
    }

    if (digitalRead(i1) == LOW) {
      input1Str = String("1,");
    } else {
      input1Str = String("0,");
    }

    if (digitalRead(i2) == LOW) {
      input2Str = String("1,");
    } else {
      input2Str = String("0,");
    }

    if (digitalRead(i3) == LOW) {
      input3Str = String("1,");
    } else {
      input3Str = String("0,");
    }

    if (digitalRead(i4) == LOW) {
      input4Str = String("1,");
    } else {
      input4Str = String("0,");
    }
    String test1 = String("1,");
    String test2 = String("982,");
    dataString = input1Str + input2Str + input3Str + input4Str;
    SerialBT.println(dataString);
  }
}

String getValue(String data, char separator, int index) {
  int found = 0;
  int strIndex[] = {
    0, -1
  };
  int maxIndex = data.length() - 1;
  for (int i = 0; i <= maxIndex && found <= index; i++) {
    if (data.charAt(i) == separator || i == maxIndex) {
      found++;
      strIndex[0] = strIndex[1] + 1;
      strIndex[1] = (i == maxIndex) ? i + 1 : i;
    }
  }
  return found > index ? data.substring(strIndex[0], strIndex[1]) : "";
}
