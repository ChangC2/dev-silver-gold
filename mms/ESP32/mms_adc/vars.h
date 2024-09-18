
#define FIRMWARE_VERSION 3  // to avoid updating infinitely

// Ethernet Webserver
EthernetWebServer ethernetServer(80);

// Flash memory initialize
Preferences pref;

// // WiFi connection try counter

String customeridstr, deviceidstr, ipstr, wifissid, wifipass;
const char *customer_id_str, *device_id_str, *ip_str, *wifi_ssid, *wifi_pass, *wifi_IP_str;

byte mac[] = { 0xDE, 0xAD, 0xBE, 0xEF, 0xFE, 0x01 };  // MAC Address of Ethernet Shield
String defaultEthernetIp = "192.168.1.20";

int device_id_set = 0, ip_set = 0, wifi_set = 0;

const int readingCycle = 5;
uint8_t passedTime = 10 * readingCycle;

String DATA_API_URL = String("https://api.slymms.com/api/postBufferGanttDataV2?device_id=");  // url to store data

// constants for getting time from NTPClient
int timezone = 0;

String OTA_API_URL = String("https://slymms.com/backend/get_arduino_farmware_version.php?customer_id=");  // url to check fireware version

#define CSLOCK_LIMIT_TIME 5
int cslock_cycle = 1;
int cslock_reverse = 0;
int prevSignal = 1;

// constants for getting time from NTPClient
const char *ntpServer = "pool.ntp.org";
long gmtOffset_sec = -3600 * 7; // UTC -7 = Denver time
const int daylightOffset_sec = 3600;
bool isRestartedOnMidnight = false;

int wifi_cnt = 0;

AD7091R8class adc;

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

