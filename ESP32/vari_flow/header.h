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

#define LOOP_DELAY 100 // 10 ms
#define Led_Pin 12
#define RXD2 16
#define TXD2 17

// const char *wifi_ssid = "TP-LINK_F56E", *wifi_pass = "WIFI@0642!";
const char *wifi_ssid = "SWEBCO_GUEST", *wifi_pass = "Violin-improvising";

const int sendCycle = 10; // second
uint8_t sendDelayCounter = 0;

uint8_t readDelayCounter = 0;

int dutyCycle = 0;
int dutyStep = 1;
int dutyDelayCounter = 0;

String DATA_API_URL = String("https://slymms.com/backend/get_arduino_store_hennig_data.php?sensor_id="); // url to store data
String params[7] = {"system_status=", "&set_pressure=", "&actual_pressure=", "&hz=", "&run_time=", "&alarm=", "&lowTankAlarm="};

int freq = 5000, ledChannel = 0, resolution = 8;
int press = 150;
String dataString = "", serialPressure = "";
