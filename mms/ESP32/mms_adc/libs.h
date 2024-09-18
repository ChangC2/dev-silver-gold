///////////////////////////////////////////////////////////////////////////////////
// To demonstrate the coexistence of this EthernetWebServer and ESP32 WebServer library
#include <Arduino.h>
#include <SPI.h>
#include <ctime>
#include <Wire.h>
#include <Preferences.h>
#include <WiFiMulti.h>
#include <HTTPClient.h>
#include <HTTPUpdate.h>
#include <ArduinoJson.h>
#include <EthernetWebServer.h>
#include <ArduinoRS485.h> // ArduinoModbus depends on the ArduinoRS485 library
#include <ArduinoModbus.h>
#include <UrlEncode.h>
#include "AD7091R.h"
