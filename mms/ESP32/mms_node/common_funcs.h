void loadPref() {
  pref.begin("MMS", false);

  customeridstr = pref.getString("CUSTOMERID", "");
  machineidstr = pref.getString("MACHINEID", "");
  ipstr = pref.getString("New_IP", defaultEthernetIp);
  networktypestr = pref.getString("NETWORKTYPE", "");

  // customeridstr = String("visser");
  // machineidstr = String("NHX6300 #3");
  // ipstr = "10.0.0.85";
  // networktypestr = "ethernet";
  // // Reading Ethernet IP

  pref.end();

  customer_id_str = customeridstr.c_str();
  machine_id_str = machineidstr.c_str();
  networktype_str = networktypestr.c_str();
  ip_str = ipstr.c_str();
  // //////////////////////////////////////////////
}

void setupEthernet() {
  Ethernet.init(5);  // USE_THIS_SS_PIN 5 For ESP32
  int ip_buf[4] = {};
  sscanf(ip_str, "%d.%d.%d.%d", &ip_buf[3], &ip_buf[2], &ip_buf[1], &ip_buf[0]);
  IPAddress ip(ip_buf[3], ip_buf[2], ip_buf[1], ip_buf[0]);
  Ethernet.begin(mac, ip);
  Serial.println(Ethernet.localIP());
}

void postGanttData() {
  // The code to get the current machine status
  int err = 0;

  EthernetClient c;
  HttpClient http(c);

  err = http.get(kHostname, kPath);
  if (err == 0) {
    err = http.responseStatusCode();
    if (err >= 0) {
      Serial.print("Api status code: ");
      Serial.println(err);
      err = http.skipResponseHeaders();
      if (err >= 0) {
        int bodyLen = http.contentLength();
        unsigned long timeoutStart = millis();
        char c;
        while ((http.connected() || http.available()) && ((millis() - timeoutStart) < kNetworkTimeout)) {
          if (http.available()) {
            c = http.read();
            Serial.print(c);
            bodyLen--;
            timeoutStart = millis();
          } else {
            delay(kNetworkDelay);
          }
        }
        Serial.println();
      } else {
        Serial.print("Failed to skip response headers: ");
        Serial.println(err);
      }
    } else {
      Serial.print("Getting response failed: ");
      Serial.println(err);
    }
  } else {
    Serial.print("Connect failed: ");
    Serial.println(err);
  }
  http.stop();
}
