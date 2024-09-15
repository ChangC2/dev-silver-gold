// initWiFi

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

void checkMidnight() {
  time_t currentTime = time(NULL);
  // Convert the current time to a tm struct (local time)
  tm *localTime = localtime(&currentTime);
  if (localTime == nullptr) {
    return;
  }
  time_t currentTimestamp = mktime(localTime);
  Serial.println("Current time:" + String(localTime->tm_hour) + ":" + String(localTime->tm_min) + ":" + String(localTime->tm_sec));
  if (localTime->tm_hour == 0 && !isRestartedOnMidnight) {
    Serial.println("Midnight: restarted ESP");
    ESP.restart();
  }
  if (localTime->tm_hour == 1) {
    isRestartedOnMidnight = false;
  }
}

void loadPref() {
  pref.begin("MMS", false);

  customeridstr = pref.getString("CUSTOMERID", "");
  machineidstr = pref.getString("MACHINEID", "");
  // customeridstr = String("visser");
  // machineidstr = String("NHX6300 #3");

  // // Reading Ethernet IP
  ipstr = pref.getString("New_IP", defaultEthernetIp);

  // // Reading WiFi ssid and pass
  wifissid = pref.getString("SSID", "");
  // wifissid = String("TKK0806");
  // wifissid = String("1bourbon1scotch1router");
  wifipass = pref.getString("PASS", "");
  // wifipass = String("WIFI@0642!");
  // wifipass = String("8675309j");
  pref.end();

  customer_id_str = customeridstr.c_str();
  machine_id_str = machineidstr.c_str();
  ip_str = ipstr.c_str();
  wifi_ssid = wifissid.c_str();
  wifi_pass = wifipass.c_str();

  // //////////////////////////////////////////////
  Serial.print("Connecting to ");
  Serial.println(wifi_ssid);
  Serial.print("password : ");
  Serial.println(wifi_pass);
}

void initWiFi() {
  WiFi.mode(WIFI_STA);
  WiFi.disconnect();
  WiFi.begin(wifi_ssid, wifi_pass);
  // WiFi.begin("TKK0806", "WIFI@0642!");
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

void checkWiFi() {
  if (WiFi.status() != WL_CONNECTED) {
    WiFi.mode(WIFI_STA);
    WiFi.disconnect();
    WiFi.begin(wifi_ssid, wifi_pass);
    ++wifi_cnt;
    Serial.print(".");
    Serial.print(wifi_cnt);
    if (wifi_cnt > 1000)  // Wifi connection try 20 times if connection is not established
    {
      Serial.println("WiFi connection failed with current Credentials");
      Serial.println("Check the WiFi SSID and PASSWORD again");
      wifi_cnt = 0;
      ESP.restart();
    }
  }
}

// OTA Update
void checkOTAUpdate() {
  Serial.print(F("Firmware version "));
  Serial.println(FIRMWARE_VERSION);
  HTTPClient http;
  String apiUrl = OTA_API_URL + customeridstr;
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
    float newVersion = json["mms_version"].as<float>();
    timezone = json["timezone"].as<int>();
    gmtOffset_sec = 3600 * timezone;
    syncTime();
    Serial.printf("Server Version : %.2f\n", newVersion);
    Serial.printf("Timezone : %d\n", timezone);
    if (newVersion > FIRMWARE_VERSION) {
      delay(1000);
      WiFiClient client;
      httpUpdate.rebootOnUpdate(false);  // remove automatic update
      Serial.println(F("Update start now!"));
      t_httpUpdate_return ret = httpUpdate.update(client, "www.slymms.com", 3000, "/firmwares/mms.bin");

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

//Send data to server
void postGanttData(int signal) {
  // The code to get the current machine status
  HTTPClient http;
  String apiUrl = DATA_API_URL + urlEncode(customeridstr) + String("&machine_id=") + urlEncode(machineidstr) + String("&signal=") + String(signal);
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
    cslock_cycle = json["cslock_cycle"].as<int>();
    cslock_reverse = json["cslock_reverse"].as<int>();
    if (prevSignal != 1 && signal != 1 && cslock_cycle == 1) {
      sendCSLockSignal();
    }
    prevSignal = signal;
  }
  http.end();
}
