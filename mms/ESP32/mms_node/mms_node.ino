#include "libs.h"
#include "webpage.h"
#include "vars.h"
#include "common_funcs.h"
#include "ethernet_webserver_funcs.h"
#include "digital_read.h"
#include "mesh.h"

void setup() {
  Serial.begin(115200);
  setDigitalPins();
  loadPref();
  if (strcmp(networktype_str, "mesh") == 0) {
    initMesh();
  }
  setupEthernetWebserver();
}

void loop() {
  loopEthernetWebserver();
  if (strcmp(networktype_str, "mesh") == 0) {
    mesh.update();
  }

  passedTime = passedTime + 1;
  if (passedTime % 10 == 0) {
    Serial.printf("%d Sec\n", (int)passedTime / 10);
  }

  if (strcmp(networktype_str, "ethernet") == 0) {
    if (passedTime > readingCycle * 10) {
      if (!customeridstr.isEmpty() && !machineidstr.isEmpty()) {
        int machineStatus = getMachineStatus();
        String api = "/api/postBufferGanttData?customer_id=" + urlEncode(customeridstr) + String("&machine_id=") + urlEncode(machineidstr) + String("&signal=") + String(machineStatus);
        kPath = api.c_str();
        postGanttData();
        passedTime = 0;
      }
    }
  }
  delay(100);
}
