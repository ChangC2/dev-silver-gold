// Ethernet Webserver
void handleEthernetRoot()
{
  ethernetServer.send(200, "text/html", PAGE_MAIN);
}

void style_upload()
{
  ethernetServer.send(200, F("text/css"), STYLE);
}

void script_upload()
{
  ethernetServer.send(200, F("application/javascript"), SCRIPT);
}

void sendMachine()
{
  StaticJsonDocument<200> json;
  json["customer_id"] = String(customer_id_str);
  json["machine_id"] = String(machine_id_str);
  json["networktype"] = String(networktype_str);
  String jsonStr;
  serializeJson(json, jsonStr);
  Serial.print(jsonStr);
  Serial.println();
  ethernetServer.send(200, F("text/json"), jsonStr);
  jsonStr.clear();
}

void getMachine()
{
  Serial.println(ethernetServer.arg("plain"));
  StaticJsonDocument<200> json;
  DeserializationError error = deserializeJson(json, ethernetServer.arg("plain"));
  ethernetServer.send(200, F("text/plain"), ethernetServer.arg("plain"));
  if (!error)
  {
    machine_id_set = 1;
    customer_id_str = json["customer_id"];
    machine_id_str = json["machine_id"];
    customeridstr = String(customer_id_str);
    machineidstr = String(machine_id_str);
    Serial.println(customer_id_str);
    Serial.println(machine_id_str);
  }
}

void getIP()
{
  Serial.println(ethernetServer.arg("plain"));
  StaticJsonDocument<200> json;
  DeserializationError error = deserializeJson(json, ethernetServer.arg("plain"));
  ethernetServer.send(200, F("text/plain"), ethernetServer.arg("plain"));
  if (!error)
  {
    ip_set = 1;
    ip_str = json["ipaddress"];
    ipstr = String(ip_str);
    Serial.println(ip_str);
  }
  else
  {
    Serial.print(F("Failed"));
    Serial.println(error.f_str());
  }
}

void getNetworkType()
{
  Serial.println(ethernetServer.arg("plain"));
  StaticJsonDocument<200> json;
  DeserializationError error = deserializeJson(json, ethernetServer.arg("plain"));
  ethernetServer.send(200, F("text/plain"), ethernetServer.arg("plain"));
  if (!error)
  {
    networktype_set = 1;
    networktype_str = json["networktype"];
    networktypestr = String(networktype_str);
    Serial.println(networktype_str);
  }
}

void resetMachine()
{
  if (machine_id_set)
  {
    pref.begin("MMS", false);
    pref.putString("CUSTOMERID", customeridstr);
    pref.putString("MACHINEID", machineidstr);
    Serial.println(pref.getString("CUSTOMERID", ""));
    Serial.println(pref.getString("MACHINEID", ""));
    pref.end();
    machine_id_set = 0;
    ESP.restart();
  }
}

void resetEnthernetIP()
{
  if (ip_set)
  {
    pref.begin("MMS", false);
    pref.putString("New_IP", ipstr);
    Serial.println(pref.getString("New_IP", ""));
    pref.end();
    ip_set = 0;
    ESP.restart();
  }
}

void resetNetworkType()
{
  if (networktype_set)
  {
    pref.begin("MMS", false);
    pref.putString("NETWORKTYPE", networktypestr);
    Serial.println(pref.getString("NETWORKTYPE", ""));
    pref.end();
    networktype_set = 0;
    ESP.restart();
  }
}

void setupEthernetWebserver()
{
  // start the ethernet connection and the server:
  Ethernet.init(5); // USE_THIS_SS_PIN 5 For ESP32
  int ip_buf[4] = {};
  sscanf(ip_str, "%d.%d.%d.%d", &ip_buf[3], &ip_buf[2], &ip_buf[1], &ip_buf[0]);
  IPAddress ip(ip_buf[3], ip_buf[2], ip_buf[1], ip_buf[0]);
  Ethernet.begin(mac, ip);

  ethernetServer.sendHeader("Access-Control-Allow-Origin", "*", 1);
  ethernetServer.on(F("/"), handleEthernetRoot);
  ethernetServer.on(F("/script.js"), script_upload);
  ethernetServer.on(F("/style.css"), style_upload);
  ethernetServer.on(F("/setMachine"), getMachine);
  ethernetServer.on(F("/setEthernetIP"), getIP);
  ethernetServer.on(F("/setNetworkType"), getNetworkType);
  ethernetServer.on(F("/getMachine"), sendMachine);
  ethernetServer.begin();

  Serial.print(F("HTTP EthernetWebServer is @ IP : "));
  Serial.println(Ethernet.localIP());
}

void loopEthernetWebserver()
{
  ethernetServer.handleClient();
  resetMachine();
  resetEnthernetIP();
  resetNetworkType();
}
// Ethernet Webserver