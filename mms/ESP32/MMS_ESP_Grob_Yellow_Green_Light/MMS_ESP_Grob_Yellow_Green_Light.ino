#include "libs.h"
#include "webpage.h"
#include "vars.h"
#include "digital_read.h"
#include "common_funcs.h"
#include "ethernet_webserver_funcs.h"


void setup() {
  isRestartedOnMidnight = true;
  Serial.begin(115200);
  setDigitalPins();
  loadPref();
  initWiFi();
  checkOTAUpdate();  // OTA update and get Sensor details
  setupEthernetWebserver();
  delay(1000);
}

void loop() {
  checkWiFi();
  loopEthernetWebserver();
  passedTime = passedTime + 1;
  if (passedTime % 10 == 0) {
    checkMidnight();
    Serial.printf("%d Sec\n", (int)passedTime / 10);
  }
  if (passedTime > readingCycle * 10 && !customeridstr.isEmpty() && !machineidstr.isEmpty()) {
    int machineStatus = getMachineStatus();
    postGanttData(machineStatus);
    passedTime = 0;
  }
  delay(100);
}
