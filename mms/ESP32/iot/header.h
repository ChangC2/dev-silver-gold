///////////////////////////////////////////////////////////////////////////////////
// To demonstrate the coexistence of this EthernetWebServer and ESP32 WebServer library

#include <Arduino.h>
#include <SPI.h>
#include <ctime>
#include <Wire.h>
#include <Preferences.h>
#include <WiFi.h>
#include <WiFiMulti.h>
#include <HTTPClient.h>
#include <HTTPUpdate.h>
#include <ArduinoJson.h>
#include <EthernetWebServer.h>
#include <ArduinoRS485.h> // ArduinoModbus depends on the ArduinoRS485 library
#include <ArduinoModbus.h>

EthernetWebServer ethernetServer(80);

// Flash memory initialize
Preferences pref;

String keystr, ipstr, wifissid, wifipass;
const char *key_str, *ip_str, *wifi_ssid, *wifi_pass, *wifi_IP_str;

byte mac[] = {0xDE, 0xAD, 0xBE, 0xEF, 0xFE, 0x01}; // MAC Address of Ethernet Shield
String defaultEthernetIp = "192.168.1.20";

int key_set = 0, ip_set = 0, wifi_set = 0;

EthernetClient ethClient;
ModbusTCPClient modbusTCPClient(ethClient);

String modbusIP = "";
String hreg = "0,1,2,3,4,5,6,7";
const char *modbus_ip_str, *hreg_str;
const int readingCycle = 10;
uint8_t passedTime = 10 * readingCycle;

String customer_id = "hennig";
String DATA_API_URL = String("https://slymms.com/backend/get_arduino_store_hennig_data.php?sensor_id=");                                       // url to store data
String OTA_API_URL = String("https://slymms.com/backend/get_arduino_farmware_version.php?customer_id=") + customer_id + String("&sensor_id="); // url to check fireware version

#define FIRMWARE_VERSION 3 // to avoid updating infinitely

// constants for getting time from NTPClient
const char *ntpServer = "pool.ntp.org";
const long gmtOffset_sec = -3600 * 7; // UTC -7 = Denver time
const int daylightOffset_sec = 3600;
bool isRestartedOnMidnight = false;
int wifi_cnt = 0;