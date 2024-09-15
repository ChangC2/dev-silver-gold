/*
  Ethernet Modbus TCP Client Toggle

  This sketch toggles the coil of a Modbus TCP server connected
  on and off every second.

  Circuit:
   - Any Arduino MKR Board
   - MKR ETH Shield

  created 16 July 2018
  by Sandeep Mistry
*/

#include <Arduino.h>
#include <SPI.h>
#include <Ethernet.h>

#include <ArduinoRS485.h> // ArduinoModbus depends on the ArduinoRS485 library
#include <ArduinoModbus.h>

// Enter a MAC address for your controller below.
// Newer Ethernet shields have a MAC address printed on a sticker on the shield
// The IP address will be dependent on your local network:
byte mac[] = {
  0xDE, 0xAD, 0xBE, 0xEF, 0xFE, 0xED
};
IPAddress ip(192, 168, 1, 177);

EthernetClient ethClient;
ModbusTCPClient modbusTCPClient(ethClient);

IPAddress server(192, 168, 1, 50); // update with the IP Address of your Modbus server
int addressToRead = 201;

void setup() {
  //Initialize serial and wait for port to open:
  Serial.begin(115200);
  delay(1000);
  Ethernet.init(5);  // USE_THIS_SS_PIN 5 For ESP32
  IPAddress ip(192, 168, 1, 20);
  Ethernet.begin(mac, ip);
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
