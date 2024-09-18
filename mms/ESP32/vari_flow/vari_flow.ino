#include "header.h"
#include "webpage.h"

void initWiFi() {
  WiFi.mode(WIFI_STA);
  WiFi.disconnect();
  WiFi.begin(wifi_ssid, wifi_pass);
  int wifi_cnt = 0;
  while (WiFi.status() != WL_CONNECTED) {
    ++wifi_cnt;
    delay(500);
    Serial.print(".");
    Serial.print(wifi_cnt);
    if (wifi_cnt > 19)  // Wifi connection try 20 times if connection is not established
    {
      Serial.println("WiFi connection failed with current Credentials");
      Serial.println("Check the WiFi SSID and PASSWORD again");
      wifi_cnt = 0;
      break;
    }
  }

  if (WiFi.status() == WL_CONNECTED) {
    Serial.print("WiFi IP: ");
    Serial.println(WiFi.localIP());
  }
}

void setup() {
  ledcSetup(ledChannel, freq, resolution);
  ledcAttachPin(Led_Pin, ledChannel);
  Serial.begin(9600, SERIAL_7E2);  // Faduino Connection
  // Serial.begin(115200);  // Faduino Connection
  // Serial2.begin(9600, SERIAL_7E2); // Machine CNC Connection

  initWiFi();
}

void loop() {
  readDelayCounter++;
  if (readDelayCounter == 100 / LOOP_DELAY)  // run per 100 ms
  {
    if (Serial.available() > 0) {
      dataString = Serial.readString();  // Read data string from Faduino
      Serial.print("dataString : ");
      Serial.println(dataString);
    }

    if (Serial2.available()) {
      serialPressure = Serial2.readString();  // Read serial pressure from CNC
      Serial.print("serialPressure : ");
      Serial.println(serialPressure);
    }
    readDelayCounter = 0;
  }

  sendDelayCounter++;
  if (sendDelayCounter == sendCycle * (1000 / LOOP_DELAY))  // run per sendCylce
  {
    sendData();
    sendDelayCounter = 0;
  }

  ledcWrite(ledChannel, dutyCycle);
  dutyCycle += dutyStep;

  if (dutyCycle == 255) {
    dutyStep = 0;
    dutyDelayCounter++;
    if (dutyDelayCounter == 500 / LOOP_DELAY)  // Delay 500ms when dutyCycle = 255
    {
      dutyStep = -1;
      dutyDelayCounter = 0;
    }
  }

  if (dutyCycle == 0) {
    dutyStep = 0;
    dutyDelayCounter++;
    if (dutyDelayCounter == 500 / LOOP_DELAY)  // Delay 500(10 * 50) ms when dutyCycle = 0
    {
      dutyStep = 1;
      dutyDelayCounter = 0;
    }
  }

  delay(LOOP_DELAY);
}

void sendData() {
  // 0: status, 1: setPressure, 2: actualPressure, 3: hz, 4: runTime, 5: alarm, 6: lowTankAlarm
  HTTPClient http;
  String apiUrl = "https://slymms.com/backend/get_arduino_store_vari_flow.php?";
  for (int i = 0; i < 7; i++) {
    apiUrl = apiUrl + params[i] + getValue(dataString, ',', i);
    // apiUrl = apiUrl + params[i] + String(random(10));  // Random Value
  }
  http.begin(apiUrl);
  http.addHeader("Content-Type", "application/x-www-form-urlencoded");
  int httpCode = http.GET();
  Serial.println(apiUrl);
  Serial.printf("Api Status Code : %d.\n", httpCode);
  http.end();
}

String getValue(String data, char separator, int index) {
  int found = 0;
  int strIndex[] = { 0, -1 };
  int maxIndex = data.length() - 1;

  for (int i = 0; i <= maxIndex && found <= index; i++) {
    if (data.charAt(i) == separator || i == maxIndex) {
      found++;
      strIndex[0] = strIndex[1] + 1;
      strIndex[1] = (i == maxIndex) ? i + 1 : i;
    }
  }
  return found > index ? data.substring(strIndex[0], strIndex[1]) : "0";
}
