
// Ethernet Webserver
EthernetWebServer ethernetServer(80);

// Flash memory initialize
Preferences pref;

String customeridstr, machineidstr, ipstr, networktypestr;
const char *customer_id_str, *machine_id_str, *ip_str, *networktype_str;

byte mac[] = {0xDE, 0xAD, 0xBE, 0xEF, 0xFE, 0x01}; // MAC Address of Ethernet Shield
String defaultEthernetIp = "10.0.0.85";

int machine_id_set = 0, ip_set = 0, networktype_set = 0;

const int readingCycle = 10;
uint8_t passedTime = 10 * readingCycle;

const char kHostname[] = "72.167.224.113";
const char* kPath;
const int kNetworkTimeout = 30 * 1000;
const int kNetworkDelay = 1000;