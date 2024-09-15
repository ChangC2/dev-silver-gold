#include "header.h"
#include "webpage.h"

long long getUnixTimestamp() {
  long long unixSec = time(nullptr);
  long long timestampMilis = unixSec * 1000 + millis() % 1000;
  return timestampMilis;
}

void syncTime() {
  // init and get the time
  configTime(gmtOffset_sec, daylightOffset_sec, ntpServer);
  Serial.println("Waiting for time syncronization...");
  while (!time(nullptr)) {
    delay(500);
  }
  time_t currentTime = time(nullptr);
  long long unixTimeStampMillis = static_cast<long long>(currentTime) * 1000;
  Serial.println("Syncronized time:" + String(unixTimeStampMillis));
}

// Ethernet Web Server
void handleEthernetRoot() {
  ethernetServer.send(200, "text/html", PAGE_MAIN);
}

void style_upload() {
  ethernetServer.send(200, F("text/css"), STYLE);
}

void script_upload() {
  ethernetServer.send(200, F("application/javascript"), SCRIPT);
}

void sendKey() {
  StaticJsonDocument<200> json;
  json["sensor_key"] = String(key_str);
  String jsonStr;
  serializeJson(json, jsonStr);
  Serial.print(jsonStr);
  Serial.println();
  ethernetServer.send(200, F("text/json"), jsonStr);
  jsonStr.clear();
}

void getKey() {
  Serial.println(ethernetServer.arg("plain"));
  StaticJsonDocument<200> json;
  DeserializationError error = deserializeJson(json, ethernetServer.arg("plain"));
  ethernetServer.send(200, F("text/plain"), ethernetServer.arg("plain"));
  if (!error) {
    key_set = 1;
    key_str = json["sensor_key"];
    keystr = String(key_str);
    Serial.println(key_str);
  } else {
    Serial.print(F("Failed"));
    Serial.println(error.f_str());
  }
}

void getIP() {
  Serial.println(ethernetServer.arg("plain"));
  StaticJsonDocument<200> json;
  DeserializationError error = deserializeJson(json, ethernetServer.arg("plain"));
  ethernetServer.send(200, F("text/plain"), ethernetServer.arg("plain"));
  if (!error) {
    ip_set = 1;
    ip_str = json["ipaddress"];
    ipstr = String(ip_str);
    Serial.println(ip_str);
  }
}

void getWiFi() {
  Serial.println(ethernetServer.arg("plain"));
  StaticJsonDocument<200> json;
  DeserializationError error = deserializeJson(json, ethernetServer.arg("plain"));
  if (!error) {
    wifi_set = 1;
    wifi_ssid = json["ssid"];
    wifissid = String(wifi_ssid);
    wifi_pass = json["pass"];
    wifipass = String(wifi_pass);
    Serial.println(wifi_ssid);
    Serial.println(wifi_pass);
  }
}
// Ethernet Web Server

void initWiFi() {
  WiFi.mode(WIFI_STA);
  WiFi.disconnect();
  WiFi.begin(wifi_ssid, wifi_pass);
  wifi_cnt = 0;
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

void checkWiFi()
{
  if (WiFi.status() != WL_CONNECTED)
  {
    WiFi.mode(WIFI_STA);
    WiFi.disconnect();
    WiFi.begin(wifi_ssid, wifi_pass);
    ++wifi_cnt;
    Serial.print(".");
    Serial.print(wifi_cnt);
    if (wifi_cnt > 1000) // Wifi connection try 20 times if connection is not established
    {
      Serial.println("WiFi connection failed with current Credentials");
      Serial.println("Check the WiFi SSID and PASSWORD again");
      wifi_cnt = 0;
      ESP.restart();
    }
  }
}

void checkMidnight()
{
  time_t currentTime = time(NULL);
  // Convert the current time to a tm struct (local time)
  tm *localTime = localtime(&currentTime);
  if (localTime == nullptr)
  {
    return;
  }
  time_t currentTimestamp = mktime(localTime);
  Serial.println("Current time:" + String(localTime->tm_hour) + ":" + String(localTime->tm_min) + ":" + String(localTime->tm_sec));
  if (localTime->tm_hour == 17 && !isRestartedOnMidnight)
  {
    Serial.println("Midnight: restarted ESP");
    ESP.restart();
  }
  if (localTime->tm_hour == 1)
  {
    isRestartedOnMidnight = false;
  }
}

void setup() {
  // ESP.restart();
  Serial.begin(115200);
  Serial.print(F("Firmware version "));
  Serial.println(FIRMWARE_VERSION);
  delay(1000);

  // // Reading Ethernet IP
  pref.begin("MMS", false);

  keystr = pref.getString("KEY", "");
  // keystr = String("5001");

  // // Reading Ethernet IP
  ipstr = pref.getString("New_IP", defaultEthernetIp);

  // // Reading WiFi ssid and pass
  wifissid = pref.getString("SSID", "");
  // wifissid = String("TP-LINK_F56E");
  // wifissid = String("1bourbon1scotch1router");
  wifipass = pref.getString("PASS", "");
  // wifipass = String("WIFI@0642!");
  // wifipass = String("8675309j");
  pref.end();

  key_str = keystr.c_str();
  ip_str = ipstr.c_str();
  wifi_ssid = wifissid.c_str();
  wifi_pass = wifipass.c_str();

  // //////////////////////////////////////////////
  Serial.print("Connecting to ");
  Serial.println(wifi_ssid);
  Serial.print("password : ");
  Serial.println(wifi_pass);

  initWiFi();

  checkOTAUpdate();  // OTA update and get Sensor details

  syncTime();

  // start the ethernet connection and the server:
  Ethernet.init(5);  // USE_THIS_SS_PIN 5 For ESP32
  int ip_buf[4] = {};
  sscanf(ip_str, "%d.%d.%d.%d", &ip_buf[3], &ip_buf[2], &ip_buf[1], &ip_buf[0]);
  IPAddress ip(ip_buf[3], ip_buf[2], ip_buf[1], ip_buf[0]);
  Ethernet.begin(mac, ip);

  ethernetServer.sendHeader("Access-Control-Allow-Origin", "*", 1);
  ethernetServer.on(F("/"), handleEthernetRoot);
  ethernetServer.on(F("/script.js"), script_upload);
  ethernetServer.on(F("/style.css"), style_upload);
  ethernetServer.on(F("/setKey"), getKey);
  ethernetServer.on(F("/setEthernetIP"), getIP);
  ethernetServer.on(F("/setWifi"), getWiFi);
  ethernetServer.on(F("/getKey"), sendKey);
  ethernetServer.begin();

  Serial.print(F("HTTP EthernetWebServer is @ IP : "));
  Serial.println(Ethernet.localIP());
}

void loop() {
  checkWiFi();
  ethernetServer.handleClient();
  resetKey();
  resetEnthernetIP();
  resetWifi();
  passedTime = passedTime + 1;
  if (passedTime % 10 == 0) {
    checkMidnight();
    Serial.printf("%d Sec, ", (int)passedTime / 10);
  }
  // run per 10 seconds
  if (passedTime > readingCycle * 10 && !modbusIP.isEmpty()) {
    Serial.println();
    readModbus();
    // readRandom();
    passedTime = 0;
  }
  // run per 10 seconds

  delay(100);
}

void resetKey() {
  if (key_set) {
    pref.begin("MMS", false);
    pref.putString("KEY", keystr);
    Serial.println(pref.getString("KEY", ""));
    pref.end();
    key_set = 0;
    ESP.restart();
  }
}

void resetEnthernetIP() {
  if (ip_set) {
    pref.begin("MMS", false);
    pref.putString("New_IP", ipstr);
    Serial.println(pref.getString("New_IP", ""));
    pref.end();
    ip_set = 0;
    ESP.restart();
  }
}

void resetWifi() {
  if (wifi_set) {
    // Save SSID
    pref.begin("MMS", false);
    Serial.println(wifissid);

    pref.putString("SSID", wifissid);
    Serial.println(pref.getString("SSID", ""));
    // Save Pass
    pref.putString("PASS", wifipass);
    Serial.println(pref.getString("PASS", ""));

    pref.end();
    wifi_set = 0;
    ESP.restart();
  }
}

void checkOTAUpdate() {
  HTTPClient http;
  String apiUrl = OTA_API_URL + keystr;
  http.begin(apiUrl);
  http.addHeader("Content-Type", "application/x-www-form-urlencoded");
  int httpCode = http.GET();
  Serial.println(apiUrl);
  Serial.printf("Api Status Code : %d.\n", httpCode);
  if (httpCode == HTTP_CODE_OK) {
    DynamicJsonDocument json(300);  // For ESP32/ESP8266 you'll mainly use dynamic.
    String payload = http.getString();
    Serial.println(payload);
    DeserializationError error = deserializeJson(json, payload);
    float newVersion = json["iot_version"].as<float>();
    modbusIP = json["modbus_ip"].as<String>();
    hreg = json["hreg"].as<String>();
    Serial.printf("Server Version : %.2f\n", newVersion);
    Serial.printf("Modbus IP : %s\n", modbusIP);
    Serial.printf("Modbus HREG : %s\n", hreg);
    if (newVersion > FIRMWARE_VERSION) {
      delay(1000);
      WiFiClient client;
      httpUpdate.rebootOnUpdate(false);  // remove automatic update
      Serial.println(F("Update start now!"));
      t_httpUpdate_return ret = httpUpdate.update(client, "www.slymms.com", 3000, "/firmwares/iot.bin");

      switch (ret) {
        case HTTP_UPDATE_FAILED:
          Serial.printf("HTTP_UPDATE_FAILD Error (%d): %s\n", httpUpdate.getLastError(), httpUpdate.getLastErrorString().c_str());
          Serial.println(F("Retry in 10secs!"));
          delay(10000);  // Wait 10secs
          break;

        case HTTP_UPDATE_NO_UPDATES:
          Serial.println("HTTP_UPDATE_NO_UPDATES");
          break;

        case HTTP_UPDATE_OK:
          Serial.println("HTTP_UPDATE_OK");
          delay(1000);  // Wait a second and restart
          ESP.restart();
          break;
      }
    }
  }
  http.end();
}

void readRandom() {
  uint16_t res[8] = {};
  for (int i = 0; i < 8; i++) {
    res[i] = random(10);
  }
  HTTPClient http;
  String apiUrl = DATA_API_URL + keystr;
  apiUrl = apiUrl + String("&data_1=") + String(res[0]);
  apiUrl = apiUrl + String("&data_2=") + String(res[1]);
  apiUrl = apiUrl + String("&data_3=") + String(res[2]);
  apiUrl = apiUrl + String("&data_4=") + String(res[3]);
  apiUrl = apiUrl + String("&data_5=") + String(res[4]);
  apiUrl = apiUrl + String("&data_6=") + String(res[5]);
  apiUrl = apiUrl + String("&data_7=") + String(res[6]);
  apiUrl = apiUrl + String("&data_8=") + String(res[7]);
  apiUrl = apiUrl + String("&created_at=") + String(time(nullptr));
  http.begin(apiUrl);
  http.addHeader("Content-Type", "application/x-www-form-urlencoded");
  int httpCode = http.GET();
  Serial.println(apiUrl);
  Serial.printf("Api Status Code : %d.\n", httpCode);
  http.end();
}

void readModbus() {

  if (!modbusTCPClient.connected()) {
    // client not connected, start the Modbus TCP client
    modbus_ip_str = modbusIP.c_str();
    Serial.println(modbus_ip_str);
    int ip_buf[4] = {};
    sscanf(modbus_ip_str, "%d.%d.%d.%d", &ip_buf[3], &ip_buf[2], &ip_buf[1], &ip_buf[0]);
    IPAddress ip(ip_buf[3], ip_buf[2], ip_buf[1], ip_buf[0]);
    Serial.printf("Attempting to connect to Modbus TCP server IP : %d.%d.%d.%d\n", ip_buf[3], ip_buf[2], ip_buf[1], ip_buf[0]);
    if (!modbusTCPClient.begin(ip, 502)) {
      Serial.println("Modbus TCP Client failed to connect!");
    } else {
      Serial.println("Modbus TCP Client connected");
    }
  } else {
    uint16_t res[8] = {};
    int REG[8] = {};  // Modbus HREG Offset
    hreg_str = hreg.c_str();
    sscanf(hreg_str, "%d,%d,%d,%d,%d,%d,%d,%d", &REG[0], &REG[1], &REG[2], &REG[3], &REG[4], &REG[5], &REG[6], &REG[7]);
    Serial.printf("Attempting to read the hreg : %d.%d.%d.%d.%d.%d.%d.%d\n", REG[0], REG[1], REG[2], REG[3], &REG[4], &REG[5], &REG[6], &REG[7]);
    // client connected
    for (int i = 0; i < 8; i++) {
      res[i] = modbusTCPClient.holdingRegisterRead(REG[i]);
    }
    HTTPClient http;
    String apiUrl = DATA_API_URL + keystr;
    apiUrl = apiUrl + String("&data_1=") + String(res[0]);
    apiUrl = apiUrl + String("&data_2=") + String(res[1]);
    apiUrl = apiUrl + String("&data_3=") + String(res[2]);
    apiUrl = apiUrl + String("&data_4=") + String(res[3]);
    apiUrl = apiUrl + String("&data_5=") + String(res[4]);
    apiUrl = apiUrl + String("&data_6=") + String(res[5]);
    apiUrl = apiUrl + String("&data_7=") + String(res[6]);
    apiUrl = apiUrl + String("&data_8=") + String(res[7]);
    apiUrl = apiUrl + String("&created_at=") + String(time(nullptr));
    http.begin(apiUrl);
    http.addHeader("Content-Type", "application/x-www-form-urlencoded");
    int httpCode = http.GET();
    Serial.println(apiUrl);
    Serial.printf("Api Status Code : %d.\n", httpCode);
    http.end();
  }
}
