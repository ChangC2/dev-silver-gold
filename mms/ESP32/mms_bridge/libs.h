///////////////////////////////////////////////////////////////////////////////////
// To demonstrate the coexistence of this EthernetWebServer and ESP32 WebServer library
#include <Arduino.h>
#include <SPI.h>
#include <ctime>
#include <Wire.h>
#include <WiFi.h>
#include <WiFiMulti.h>
#include <ArduinoJson.h>
#include <ArduinoRS485.h> // ArduinoModbus depends on the ArduinoRS485 library
#include <ArduinoModbus.h>
#include <UrlEncode.h>
#include <painlessMesh.h>
#include <HttpClient.h>
// #include <Ethernet.h>
#include <EthernetClient.h>
#include <EthernetWebServer.h>