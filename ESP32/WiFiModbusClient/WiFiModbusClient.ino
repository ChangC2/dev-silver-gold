/*
  WiFi Modbus TCP Client Toggle

  This sketch toggles the coil of a Modbus TCP server connected
  on and off every second.

  Circuit:
   - MKR1000 or MKR WiFi 1010 board

  created 16 July 2018
  by Sandeep Mistry
*/
#include <Arduino.h>
#include <SPI.h>
#include <WiFi.h>
#include <WiFiMulti.h> // for MKR WiFi 1010
// #include <WiFi101.h> // for MKR1000

#include <ArduinoRS485.h> // ArduinoModbus depends on the ArduinoRS485 library
#include <ArduinoModbus.h>

WiFiClient wifiClient;
ModbusTCPClient modbusTCPClient(wifiClient);

const char *wifi_ssid = "TKK0806";
const char *wifi_pass = "WIFI@0642!";
IPAddress server(192, 168, 0, 53); // update with the IP Address of your Modbus server
int addressToRead = 202;

void setup() {
  //Initialize serial and wait for port to open:
  Serial.begin(115200);
  initWiFi();
}

void loop() {
  if (!modbusTCPClient.connected()) {
    // client not connected, start the Modbus TCP client
    Serial.println("Attempting to connect to Modbus TCP server");
    
    if (!modbusTCPClient.begin(server)) {
      Serial.println("Modbus TCP Client failed to connect!");
    } else {
      Serial.println("Modbus TCP Client connected");
    }
  } else {
    Serial.println(modbusTCPClient.holdingRegisterRead(addressToRead)); // holdingRegisterRead(int address)
    delay(3000);
  }
}

void initWiFi() {
  WiFi.mode(WIFI_STA);
  // WiFi.disconnect();
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
