/****************************************************************************************************************************
  WiFi_Ethernet_Complex_ESP32.ino
  EthernetWebServer is a library for the Ethernet shields to run WebServer

  Based on and modified from ESP8266 https://github.com/esp8266/Arduino/releases
  Built by Khoi Hoang https://github.com/khoih-prog/EthernetWebServer
  Licensed under MIT license
 ***************************************************************************************************************************************/
///////////////////////////////////////////////////////////////////////////////////
#include "BluetoothSerial.h"

#if !defined(CONFIG_BT_ENABLED) || !defined(CONFIG_BLUEDROID_ENABLED)
#error Bluetooth is not enabled! Please run `make menuconfig` to and enable it
#endif
///////////////////////////////////////////////////////////////////////////////////
// To demonstrate the coexistence of this EthernetWebServer and ESP32 WebServer library

#include "defines.h"
#include "webpage.h"
#include "ArduinoJson.h"
#include <Preferences.h>
#include <Wire.h>

#include <WiFi.h>
#include <WebServer.h>
#include <WiFiClient.h>

#include <SPI.h>
#include <Ethernet_Generic.h>
#include <EthernetWebServer.h>

#include <HTTPClient.h>
#include <UrlEncode.h>

#include "AD7091R.h"
// AD7091R class
AD7091R8class adc;

///////////////////// DEVICE ID (Set unique for each PLC) /////////////////////////
String deviceID = "MMS_0050";
///////////////////////////////////////////////////////////////////////////////////

BluetoothSerial SerialBT;

EthernetServer ethernetServer(80);
WebServer wifiServer(80);

const int led = 13;
// Pin Assignment
int relay_pin[4] = { 2, 4, 16, 17 };  // Relay outputs pin numbers
int din_pin[4] = { 32, 35, 34, 39 };  // Digital Inputs pin numbers
int led_pin = 33;                     // User onboard LED pin number
int btn_pin = 15;                     // User onboard Button pin number
int adc_busy_pin = 25;                // ADC IC's busy indicator pin
int adc_conv_pin = 27;                // ADC IC's ADC conversion control pin

// Data variables
int adc_temp, adc_val;
int ain[4] = { 0 };            // Current inputs
int vin[4] = { 0 };            // Voltage inputs
float vin_val[4], ain_val[4];  // Real voltage and current values in float
//float vin_temp[4], ain_temp[4]; // tempary voltage and current values in float used to round.
float Vcoef = 0.0024576;
/*
 * Vout = Vref*(Vadc/4096)*4 
 * Vcoef = (Vref/4096)*4 = 4* 2.5/4096 = 10/4096 = 0.0024576062914721
*/
float Acoef = 0.005086;
/*
 * Aout = (Vref*(Vadc/4096))/120Ohm 
 * Acoef(mA unit) = 1000*(Vref/4096)/120 = 1000*2.5/(4096*120) = 2500/(491520) = 0.0050862630208333
 */
// Adc correction values
float voff[4] = { .49, .60, .52, .62 };      //analog voltage adjustment
float aoff[4] = { 1.01, 1.29, 1.03, 1.31 };  //analog current adjustment

int relay[4] = { 0 };  // Relay outputs
int din[4] = { 0 };    // Digital inputs
int adc_busy = 0;      // ADC IC's busy indicator
int adc_conv = 0;      // ADC IC's ADC conversion control
String str1 = "";
boolean stringComplete = false;
String p1;
String p2;
String p3;
String p4;
String dataString;
String input1Str;
String input2Str;
String input3Str;
String input4Str;

const int readingCycle = 5; //seconds
uint8_t passedTime = 0;

// Flash memory initialize
Preferences ip_mem, ssid_mem, pass_mem;
StaticJsonDocument<200> ip_json;
StaticJsonDocument<200> relay_json;
StaticJsonDocument<200> wifi_json;
StaticJsonDocument<200> wifiIP_json;
StaticJsonDocument<400> data_json;

int ip_buf[4] = {};
int ip_set = 0, wifi_set = 0;
const char *ip_str, *wifi_ssid, *wifi_pass, *wifi_IP_str;
String ipstr, wifissid, wifipass, wifiIPstr;
String data_str;

// WiFi connection try counter
int wifi_cnt = 0;

void handleEthernetRoot() {
  ethernetServer.send(200, "text/html", PAGE_MAIN);
}

void handleWiFiRoot() {
  wifiServer.send(200, "text/html", PAGE_WIFI);
}

void eth_getIP() {
  Serial.println(ethernetServer.arg("plain"));
  DeserializationError error = deserializeJson(ip_json, ethernetServer.arg("plain"));
  ethernetServer.send(200, F("text/plain"), ethernetServer.arg("plain"));
  if (!error) {
    ip_set = 1;
    ip_str = ip_json["ipaddress"];
    ipstr = String(ip_str);
    Serial.println(ip_str);
    sscanf(ip_str, "%d.%d.%d.%d", &ip_buf[3], &ip_buf[2], &ip_buf[1], &ip_buf[0]);
  }
}

void eth_getWiFi() {
  Serial.println(ethernetServer.arg("plain"));
  DeserializationError error = deserializeJson(wifi_json, ethernetServer.arg("plain"));

  // Serial.println("Received");

  //ethernetServer.send(200, F("text/plain"), ethernetServer.arg("plain"));
  if (!error) {
    Serial.println("Received");
    wifi_set = 1;
    wifi_ssid = wifi_json["ssid"];
    wifissid = String(wifi_ssid);
    wifi_pass = wifi_json["pass"];
    wifipass = String(wifi_pass);
    Serial.println(wifi_ssid);
    Serial.println(wifi_pass);
    // sscanf(ip_str,"%d.%d.%d.%d",&ip_buf[3], &ip_buf[2], &ip_buf[1], &ip_buf[0]);
  } else {
    Serial.print(F("Failed"));
    Serial.println(error.f_str());
  }
}

void getIP() {
  Serial.println(wifiServer.arg("plain"));
  DeserializationError error = deserializeJson(ip_json, wifiServer.arg("plain"));
  wifiServer.send(200, F("text/plain"), wifiServer.arg("plain"));
  if (!error) {
    ip_set = 1;
    ip_str = ip_json["ipaddress"];
    ipstr = String(ip_str);
    Serial.println(ip_str);
    sscanf(ip_str, "%d.%d.%d.%d", &ip_buf[3], &ip_buf[2], &ip_buf[1], &ip_buf[0]);
  }
}

void getWiFi() {
  Serial.println(wifiServer.arg("plain"));
  DeserializationError error = deserializeJson(wifi_json, wifiServer.arg("plain"));

  // Serial.println("Received");

  wifiServer.send(200, F("text/plain"), wifiServer.arg("plain"));
  if (!error) {
    Serial.println("Received");
    wifi_set = 1;
    wifi_ssid = wifi_json["ssid"];
    wifissid = String(wifi_ssid);
    wifi_pass = wifi_json["pass"];
    wifipass = String(wifi_pass);
    Serial.println(wifi_ssid);
    Serial.println(wifi_pass);
    // sscanf(ip_str,"%d.%d.%d.%d",&ip_buf[3], &ip_buf[2], &ip_buf[1], &ip_buf[0]);
  }
}

void getRelay() {
  DeserializationError error = deserializeJson(relay_json, wifiServer.arg("plain"));
  // Serial.print(message);
  Serial.println(wifiServer.arg("plain"));
  wifiServer.send(200, F("text/plain"), wifiServer.arg("plain"));
  if (!error) {
    relay[0] = relay_json["relay1"];
    relay[1] = relay_json["relay2"];
    relay[2] = relay_json["relay3"];
    relay[3] = relay_json["relay4"];
    for (int i = 0; i < 4; i++) {
      if (relay[i] == 1)
        digitalWrite(relay_pin[i], HIGH);
      else
        digitalWrite(relay_pin[i], LOW);
      //    Serial.printf("Relay %d = %d",i, relay[i]);
    }
  }
}

void getData() {
  for (int i = 0; i < 4; i++) {
    din[i] = digitalRead(din_pin[i]);
  }
  data_json["din1"] = din[0] ? true : false;
  data_json["din2"] = din[1] ? true : false;
  data_json["din3"] = din[2] ? true : false;
  data_json["din4"] = din[3] ? true : false;
  data_json["current1"] = ain_val[0];
  data_json["current2"] = ain_val[1];
  data_json["current3"] = ain_val[2];
  data_json["current4"] = ain_val[3];
  data_json["volt1"] = vin_val[0];
  data_json["volt2"] = vin_val[1];
  data_json["volt3"] = vin_val[2];
  data_json["volt4"] = vin_val[3];

  serializeJson(data_json, data_str);
  Serial.print(data_str);
  Serial.println();
  wifiServer.send(200, F("text/json"), data_str);
  data_str.clear();
}

void sendWiFiIP() {
  serializeJson(wifiIP_json, wifiIPstr);
  Serial.print(wifiIPstr);
  Serial.println();
  ethernetServer.send(200, F("text/json"), wifiIPstr);
  wifiIPstr.clear();
}

String ip2Str(IPAddress ip) {
  String s = "";
  for (int i = 0; i < 4; i++) {
    s += i ? "." + String(ip[i]) : String(ip[i]);
  }
  return s;
}

void style_upload() {
  ethernetServer.send(200, F("text/css"), STYLE);
  // ethernetServer.send(200, F("text/plain"), STYLE);
}
void script_upload() {
  ethernetServer.send(200, F("application/javascript"), SCRIPT);
  //ethernetServer.send(200, F("text/plain"), SCRIPT);
}

void initWiFi() {
  WiFi.mode(WIFI_STA);
  WiFi.begin(wifi_ssid, wifi_pass);
  while (WiFi.status() != WL_CONNECTED) {
    ++wifi_cnt;
    delay(500);
    Serial.print(".");
    Serial.print(wifi_cnt);
    if (wifi_cnt > 19) {
      Serial.println("WiFi connection failed with current Credentials");
      Serial.println("Check the WiFi SSID and PASSWORD again");
      wifi_cnt = 0;
      break;
    }
  }
  if (WiFi.status() == WL_CONNECTED) {
    Serial.println("WiFi connected");
    Serial.println("IP address: ");
    Serial.println(WiFi.localIP());
  }
}

void initAd7091() {
  pinMode(AD7091R8_CONVST, OUTPUT);  //
  delay(100);                        // Wait untill reference capatitor fully charged.
  adc.AD7091R8_SPI_Configuration();
  adc.Ad7091_soft_reset();

  adc.writeAd7091(CONFIG, 0x0271);  // Active Software Reset, BUSY output, CMOS output,
  adc.writeAd7091(ADC_CH, 0x00ff);  // ADC channel 0
  adc.writeAd7091(ADC_RES, 0x00);   // Initialize ADC result register
  adc.writeAd7091(CH0L_LIM, 0x0000);
  adc.writeAd7091(CH0H_LIM, 0x01ff);
  adc.writeAd7091(CH0_HYS, 0x01ff);

  adc.writeAd7091(CH1L_LIM, 0x0000);
  adc.writeAd7091(CH1H_LIM, 0x01ff);
  adc.writeAd7091(CH1_HYS, 0x01ff);

  adc.writeAd7091(CH2L_LIM, 0x0000);
  adc.writeAd7091(CH2H_LIM, 0x01ff);
  adc.writeAd7091(CH2_HYS, 0x01ff);

  adc.writeAd7091(CH3L_LIM, 0x0000);
  adc.writeAd7091(CH3H_LIM, 0x01ff);
  adc.writeAd7091(CH3_HYS, 0x01ff);

  Serial.println("Written Register values checking.....  ");
  adc.readAd7091(CONFIG);
  adc.readAd7091(ADC_CH);
  adc.readAd7091(CH0H_LIM);
  Serial.println("Written Register values checking.....  ");
}

void calc_ADC() {
  adc.Adc_Conv();
  adc_temp = adc.readAd7091(0);
  int ch_num = (adc_temp & 0xF000) >> 13;
  int adc_val = (adc_temp & 0x0FFF);
  Serial.printf("ADC_TEMP= %x\r\n", adc_temp);
  Serial.printf("CH_NUM = %d\r\n", ch_num);
  Serial.printf("ADC_VAL = %x\r\n", adc_val);

  switch (ch_num) {
    case 0:
      vin[3] = adc_val;
      break;
    case 1:
      ain[0] = adc_val;
      break;
    case 2:
      ain[1] = adc_val;
      break;
    case 3:
      ain[2] = adc_val;
      break;
    case 4:
      ain[3] = adc_val;
      break;
    case 5:
      vin[0] = adc_val;
      break;
    case 6:
      vin[1] = adc_val;
      break;
    case 7:
      vin[2] = adc_val;
      break;
  }
  // Calculate Vin voltage in [V]
  for (int i = 0; i < 4; i++) {
    vin_val[i] = ((vin[i] * Vcoef) - voff[i]);
    if (vin_val[i] < 0) {
      vin_val[i] = 0;
    }
    Serial.printf("Vin[%d] = %x\r\n", i, vin[i]);
    Serial.printf("Vin_val[%d] = %.2f\r\n", i + 1, vin_val[i]);
  }
  // Calculate Ain current in [mA]
  for (int i = 0; i < 4; i++) {
    ain_val[i] = ((ain[i] * Acoef) - aoff[i]);
    if (ain_val[i] < 0) {
      ain_val[i] = 0;
    }
    Serial.printf("Ain[%d] = %x\r\n", i, ain[i]);
    Serial.printf("Ain_val[%d] = %.2f\r\n", i + 1, ain_val[i]);
  }
}


void setup() {
  // Config & Init Digital Inputs
  for (int i = 0; i < 4; i++) {
    pinMode(din_pin[i], INPUT);
    // digitalWrite(din_pin[i], HIGH);
  }
  // Config & Init Relay Outputs
  for (int i = 0; i < 4; i++) {
    pinMode(relay_pin[i], OUTPUT);
    digitalWrite(relay_pin[i], LOW);
  }
  // Config & Init ADC IC Busy pin
  pinMode(adc_busy_pin, INPUT);
  // Config & Init ADC IC Conv pin
  pinMode(adc_conv_pin, OUTPUT);
  digitalWrite(adc_conv_pin, LOW);
  // Config & Init LED pin
  pinMode(led_pin, OUTPUT);
  digitalWrite(led_pin, HIGH);
  // Config & Init LED pin
  pinMode(btn_pin, INPUT);

  // I2C  interface init
  Wire.begin();

  // Open serial communications and wait for port to open:
  Serial.begin(115200);
  // Start Bluetooth:
  SerialBT.begin(deviceID);  //Bluetooth device name
  Serial.println("The device started, now you can pair it with bluetooth!");

  delay(1000);
  Serial.println("\nStarting WiFi_Ethernet_Complex_ESP32 on " + String(ARDUINO_BOARD));
  // Reading Ethernet IP
  ip_mem.begin("New_IP", false);
  ipstr = ip_mem.getString("New_IP", "192.168.1.120");
  ip_mem.end();
  // Reading WiFi ssid and pass
  ssid_mem.begin("SSID", false);
  wifissid = ssid_mem.getString("SSID", "CBCI-8F3C");
  wifissid.end();
  wifi_ssid = wifissid.c_str();
  pass_mem.begin("PASS", false);
  wifipass = pass_mem.getString("PASS", "chores5312berry");
  wifi_pass = wifipass.c_str();
  wifipass.end();

  //////////////////////////////////////////////

  Serial.print("Connecting to ");
  Serial.println(wifi_ssid);
  Serial.print("password : ");
  Serial.println(wifi_pass);

  initWiFi();
  // IPAddress test_ip(192,168,0,120);
  //wifiIP_json["wifiIP"] = ip2Str(test_ip);
  wifiIP_json["wifiIP"] = ip2Str(WiFi.localIP());
  /////////////////////////////////////////////

#ifndef USE_THIS_SS_PIN
#define USE_THIS_SS_PIN 5  // For ESP32
#endif
  ET_LOGWARN1(F("ESP32 setCsPin:"), USE_THIS_SS_PIN);
  Ethernet.init(USE_THIS_SS_PIN);

  // start the ethernet connection and the server:
  // Use DHCP dynamic IP and random mac
  uint16_t index = millis() % NUMBER_OF_MAC;

  sscanf(ipstr.c_str(), "%d.%d.%d.%d", &ip_buf[3], &ip_buf[2], &ip_buf[1], &ip_buf[0]);
  // Select the IP address according to your local network
  IPAddress ip(ip_buf[3], ip_buf[2], ip_buf[1], ip_buf[0]);
  //IPAddress ip(192,168,1,120);
  // start the ethernet connection and the server:
  Ethernet.begin(mac[index], ip);
  // Ethernet.begin(mac[index]);

  //if (Ethernet.getChip() == w5500)
  {
    Serial.print(F("Speed: "));
    Serial.print(Ethernet.speedReport());
    Serial.print(F(", Duplex: "));
    Serial.print(Ethernet.duplexReport());
    Serial.print(F(", Link status: "));
    Serial.println(Ethernet.linkReport());
  }

  ethernetServer.on(F("/"), handleEthernetRoot);
  ethernetServer.on(F("/script.js"), script_upload);
  ethernetServer.on(F("/style.css"), style_upload);
  ethernetServer.on(F("/inline"), eth_getIP);
  ethernetServer.on(F("/wifi"), eth_getWiFi);
  ethernetServer.on(F("/wifiIP"), sendWiFiIP);
  ethernetServer.begin();

  Serial.print(F("HTTP EthernetWebServer is @ IP : "));
  Serial.println(Ethernet.localIP());

  //////////////////////////////////

  wifiServer.on(F("/"), handleWiFiRoot);
  wifiServer.on(F("/inline"), getIP);
  wifiServer.on(F("/relay"), getRelay);
  wifiServer.on(F("/wifi"), getWiFi);
  wifiServer.on(F("/data"), getData);
  wifiServer.begin();

  Serial.print(F("HTTP WiFiWebServer is @ IP : "));
  Serial.println(WiFi.localIP());

  //////////////////////////////////
  /* AD7091R configuration  */
  initAd7091();
}

void loop() {
  // listen for incoming clients
  ethernetServer.handleClient();
  wifiServer.handleClient();
  // save the wifi and ethernet IP address
  if (ip_set) {
    ip_mem.begin("New_IP", false);
    ip_mem.putString("New_IP", ipstr);
    ip_mem.end();
    ip_set = 0;
    ESP.restart();
  }
  if (wifi_set) {
    // Save SSID
    ssid_mem.begin("SSID", false);
    ssid_mem.putString("SSID", wifissid);
    Serial.print("Saved SSID :  ");
    Serial.println(ssid_mem.getString("SSID", "CBCI-8F3C"));
    ssid_mem.end();
    // Save Pass
    pass_mem.begin("PASS", false);
    pass_mem.putString("PASS", wifipass);
    Serial.print("Saved PASS :  ");
    Serial.println(pass_mem.getString("PASS", "chores5312berry"));
    pass_mem.end();
    wifi_set = 0;
    ESP.restart();
  }
  calc_ADC();
  ///////////////////////////////////////////////////////////////////////////////////
  if (Serial.available()) {
    SerialBT.write(Serial.read());
  }
  while (SerialBT.available() > 0) {
    str1 = SerialBT.readString();
    p1 = getValue(str1, ',', 0);
    p2 = getValue(str1, ',', 1);
    p3 = getValue(str1, ',', 2);
    p4 = getValue(str1, ',', 3);
    Serial.print(p1);
    if (p1 == "1") {
      digitalWrite(relay_pin[0], HIGH);
    } else {
      digitalWrite(relay_pin[0], LOW);
    }
    if (p2 == "1") {
      digitalWrite(relay_pin[1], LOW);
    } else {
      digitalWrite(relay_pin[1], HIGH);
    }
    if (p3 == "1") {
      digitalWrite(relay_pin[2], HIGH);
    } else {
      digitalWrite(relay_pin[2], LOW);
    }
    if (p4 == "1") {
      digitalWrite(relay_pin[3], HIGH);
    } else {
      digitalWrite(relay_pin[3], LOW);
    }

    if (digitalRead(din_pin[0]) == LOW) {
      input1Str = String("1,");
    } else {
      input1Str = String("0,");
    }

    if (digitalRead(din_pin[1]) == LOW) {
      input2Str = String("1,");
    } else {
      input2Str = String("0,");
    }

    if (digitalRead(din_pin[2]) == LOW) {
      input3Str = String("1,");
    } else {
      input3Str = String("0,");
    }

    if (digitalRead(din_pin[3]) == LOW) {
      input4Str = String("1,");
    } else {
      input4Str = String("0,");
    }
    String test1 = String("1,");
    String test2 = String("982,");
    dataString = input1Str + input2Str + input3Str + input4Str;
    SerialBT.println(dataString);
  }

  passedTime = passedTime + 1;
  if (passedTime == 10 * readingCycle) {
    postGanttData();
    passedTime = 0;
  }
  delay(100);
  ///////////////////////////////////////////////////////////////////////////////////
}

String getValue(String data, char separator, int index) {
  int found = 0;
  int strIndex[] = {
    0, -1
  };
  int maxIndex = data.length() - 1;
  for (int i = 0; i <= maxIndex && found <= index; i++) {
    if (data.charAt(i) == separator || i == maxIndex) {
      found++;
      strIndex[0] = strIndex[1] + 1;
      strIndex[1] = (i == maxIndex) ? i + 1 : i;
    }
  }
  return found > index ? data.substring(strIndex[0], strIndex[1]) : "";

  delay(250);
}

//Send data to server
void postGanttData() {
  HTTPClient http;
  String apiUrl = String("https://api.slymms.com/api/postBufferGanttData?device_id") + urlEncode(DEVICE_ID) + String("&signals=");
  for (int i = 0; i < 4; i++) {
    din[i] = digitalRead(din_pin[i]);
  }
  apiUrl = apiUrl + String(din[0] ? "1" : "0");
  apiUrl = apiUrl + String(din[1] ? ",1" : ",0");
  apiUrl = apiUrl + String(din[2] ? ",1" : ",0");
  apiUrl = apiUrl + String(din[3] ? ",1" : ",0");
  apiUrl = apiUrl + "," + String(ain_val[0]);
  apiUrl = apiUrl + "," + String(ain_val[1]);
  apiUrl = apiUrl + "," + String(ain_val[2]);
  apiUrl = apiUrl + "," + String(ain_val[3]);
  apiUrl = apiUrl + "," + String(vin_val[0]);
  apiUrl = apiUrl + "," + String(vin_val[1]);
  apiUrl = apiUrl + "," + String(vin_val[2]);
  apiUrl = apiUrl + "," + String(vin_val[3]);
  Serial.println(apiUrl);
  http.begin(apiUrl);
  http.addHeader("Content-Type", "application/x-www-form-urlencoded");
  int httpCode = http.GET();
  Serial.println(apiUrl);
  Serial.printf("Api Status Code : %d.\n", httpCode);
  if (httpCode == HTTP_CODE_OK) {
    DynamicJsonDocument json(300);  // For ESP32/ESP8266 you'll mainly use dynamic.
    String payload = http.getString();
    Serial.println(payload);
    // DeserializationError error = deserializeJson(json, payload);
    // cslock_cycle = json["cslock_cycle"].as<int>();
    // cslock_reverse = json["cslock_reverse"].as<int>();
    // if (prevSignal != 1 && signal != 1 && cslock_cycle == 1) {
    //   sendCSLockSignal();
    // }
    // prevSignal = signal;
  }
  http.end();
}
