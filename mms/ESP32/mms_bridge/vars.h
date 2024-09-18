byte mac[] = {0xDE, 0xAD, 0xBE, 0xEF, 0xFE, 0x01}; // MAC Address of Ethernet Shield
String defaultEthernetIp = "192.168.1.20";

int machine_id_set = 0, ip_set = 0, wifi_set = 0;

const int readingCycle = 5;
uint8_t passedTime = 10 * readingCycle;

const char kHostname[] = "72.167.224.113";
const char* kPath;
const int kNetworkTimeout = 30 * 1000;
const int kNetworkDelay = 1000;