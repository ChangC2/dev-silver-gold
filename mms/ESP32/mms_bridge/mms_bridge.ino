#include "libs.h"
#include "vars.h"
#include "common_funcs.h"
#include "digital_read.h"
#include "mesh.h"

void setup() {
  Serial.begin(115200);
  setDigitalPins();
  initMesh();
  // checkOTAUpdate();  // OTA update and get Sensor details
  Ethernet.init(5);
  while (Ethernet.begin(mac) != 1) {
    Serial.println("Error getting IP address via DHCP, trying again...");
    delay(15000);
  }
}

void loop() {
  mesh.update();

  // passedTime = passedTime + 1;
  // if (passedTime % 10 == 0) {
  //   Serial.printf("%d Sec\n", (int)passedTime / 10);
  // }
  // if (passedTime > readingCycle * 10) {
  //   kPath = "/api/postBufferGanttData?customer_id=visser&machine_id=NHX6300%20%233&signal=1";
  //   postGanttData();
  //   passedTime = 0;
  // }
  // delay(100);
}
