
// Ethernet Webserver
EthernetWebServer ethernetServer(80);

// Flash memory initialize
Preferences pref;

// // WiFi connection try counter

String customeridstr, machineidstr, ipstr, wifissid, wifipass;
const char *customer_id_str, *machine_id_str, *ip_str, *wifi_ssid, *wifi_pass, *wifi_IP_str;

byte mac[] = { 0xDE, 0xAD, 0xBE, 0xEF, 0xFE, 0x01 };  // MAC Address of Ethernet Shield
String defaultEthernetIp = "192.168.1.20";

int machine_id_set = 0, ip_set = 0, wifi_set = 0;

const int readingCycle = 5;
uint8_t passedTime = 10 * readingCycle;

String DATA_API_URL = String("https://api.slymms.com/api/postBufferGanttData?customer_id=");  // url to store data

#define FIRMWARE_VERSION 3  // to avoid updating infinitely

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

