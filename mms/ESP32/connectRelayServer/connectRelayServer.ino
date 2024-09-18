#include <WiFi.h>
#include <WiFiClient.h>
#include <ctime>


// Wi-Fi Credentials
const char* ssid = "1bourbon1scotch1router";
const char* password = "8675309j";

// constants for getting time from NTPClient
const char* ntpServer = "pool.ntp.org";
const long  gmtOffset_sec = -25200;   // UTC -7 = Denver time
const int   daylightOffset_sec = 3600;

// Machine Setting
const char* factoryId = "visser";
const char* machineId = "NHX6300 #1";

// Relay Server info
const char* serverDomain = "www.slymms.com";
const int serverPort = 5050;

WiFiClient client;
long long lastReportTimeMilis = 0;
int prevRunningStatus = 0;     // 0 means Uncategorized, 1 means In cycle
int delayNum;

long long getUnixTimestamp() {
  long long unixSec = time(nullptr);
  long long timestampMilis = unixSec * 1000 + millis() % 1000;
  return timestampMilis;
}

void setup() {
  Serial.begin(115200);
  delay(2000);

  WiFi.begin(ssid, password);

  while (WiFi.status() != WL_CONNECTED) {
    delay(1000);
    Serial.println("Connecting to WiFi...");
  }

  Serial.println("Connected to WiFi!");

  //init and get the time
  configTime(gmtOffset_sec, daylightOffset_sec, ntpServer);

  Serial.println("Waiting for time syncronization...");
  while(!time(nullptr)) {
    delay(500);
  }

  time_t currentTime = time(nullptr);
  long long unixTimeStampMillis = static_cast<long long>(currentTime) * 1000;

  Serial.println("Syncronized time:" + String(unixTimeStampMillis));
}

void loop() {
  
  // The code to get the current machine status
  int currRunningStatus = 0;
  if (currRunningStatus == 1){
    currRunningStatus = 0;
  }
  if (currRunningStatus == 0){
    currRunningStatus = 1;
  }
  // After read value, if Incycle, then put currRunningStatus = 1, otherwise, currRunningStatus = 0;
  // ...


  // If the client is not connected, then try to connect
  if (!client.connected()) {
    // Resolve the server domain name to an IP address
    IPAddress serverIP;
    if (WiFi.hostByName(serverDomain, serverIP)) {
      Serial.print("Server IP: ");
      Serial.println(serverIP);
      
      // Connect to the server using the IP address
      if (client.connect(serverIP, serverPort)) {
        Serial.println("Connected to server");
      } else {
        Serial.println("Connection failed");
      }
    } else {
      Serial.println("DNS resolution failed");
    }
  }

  // If the client is connected, then send the signal to the relay server
  if (client.connected()) {
    struct tm new_ts;
    getLocalTime(&new_ts);

    long long currTimestampMils = getUnixTimestamp();

    // Check If it's time to report.
    // New running status                      or          1 minutes passed in the same status 
    if (currRunningStatus != prevRunningStatus || currTimestampMils - lastReportTimeMilis > 60000) {
      
      int milliseconds = currTimestampMils % 1000;

      char formattedCreatedAt[30];
      sprintf(formattedCreatedAt, "%04d-%02d-%02d %02d:%02d:%02d.%03d",
          new_ts.tm_year + 1900,
          new_ts.tm_mon + 1,
          new_ts.tm_mday,
          new_ts.tm_hour,
          new_ts.tm_min,
          new_ts.tm_sec,
          milliseconds);

      char formattedTimeStamp[20];
      sprintf(formattedTimeStamp, "%04d-%02d-%02d %02d:%02d:%02d",
          new_ts.tm_year + 1900,
          new_ts.tm_mon + 1,
          new_ts.tm_mday,
          new_ts.tm_hour,
          new_ts.tm_min,
          new_ts.tm_sec);

      char strSignal[150];
      //"signal|||visser,Machine7,UNCATE,1695675623962,2023-09-26 05:00:23.962,2023-09-26 05:00:23,TOSV3(3.9.14)"
      sprintf(strSignal, "signal|||%s,%s,%s,%lld,%s,%s,ESP32(3.9.28)",
          factoryId, 
          machineId,
          currRunningStatus == 1 ? "INCYCLE" : "UNCATE",
          currTimestampMils,
          formattedCreatedAt,
          formattedTimeStamp);

      client.println(strSignal);
      Serial.println(strSignal);
      client.flush();

      // Save the last report status
      lastReportTimeMilis = currTimestampMils;
      prevRunningStatus = currRunningStatus;
    }
  }
  delayNum = random(10000, 120000);
  delay(delayNum);
  Serial.println(delayNum);
  delay(1000);
}
