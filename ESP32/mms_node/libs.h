#include <Arduino.h>
#include <SPI.h>
#include <ctime>
#include <Wire.h>
#include <Preferences.h>
#include <ArduinoJson.h>
#include <ArduinoRS485.h> // ArduinoModbus depends on the ArduinoRS485 library
#include <ArduinoModbus.h>
#include <UrlEncode.h>
#include <painlessMesh.h>
#include <HttpClient.h>
#include <EthernetClient.h>
#include <EthernetWebServer.h>
#include <stdio.h>
#include <string.h>