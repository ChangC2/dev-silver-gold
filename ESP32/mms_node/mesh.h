
// ESP Mesh
#define MESH_PREFIX "Slytek_Mesh1"
#define MESH_PASSWORD "Slytek_Mesh"
#define MESH_PORT 5555

Scheduler userScheduler;  // to control your personal task
painlessMesh mesh;

// User stub
void sendMessage();  // Prototype so PlatformIO doesn't complain

Task taskSendMessage(TASK_SECOND * 1, TASK_FOREVER, &sendMessage);

void sendMessage() {
  if (!customeridstr.isEmpty() && !machineidstr.isEmpty()) {
    int machineStatus = getMachineStatus();
    String api = "/api/postBufferGanttData?customer_id=" + urlEncode(customeridstr) + String("&machine_id=") + urlEncode(machineidstr) + String("&signal=") + String(machineStatus);
    Serial.printf("Send Message Time %u", mesh.getNodeTime());
    Serial.println();
    Serial.println(api);
    mesh.sendBroadcast(api);
  }
  taskSendMessage.setInterval(TASK_SECOND * 5);
}

// Needed for painless library
void receivedCallback(uint32_t from, String &msg) {
  Serial.printf("echoNode: Received from %u msg=%s\n", from, msg.c_str());
  // mesh.sendBroadcast(apiUrl);
}

void newConnectionCallback(uint32_t nodeId) {
  Serial.printf("--> New Connection, nodeId = %u\n", nodeId);
}

void changedConnectionCallback() {
  Serial.printf("Changed connections\n");
}

void nodeTimeAdjustedCallback(int32_t offset) {
  Serial.printf("Adjusted time %u. Offset = %d\n", mesh.getNodeTime(), offset);
}

void initMesh() {

  mesh.setDebugMsgTypes(ERROR | STARTUP | CONNECTION);  // set before init() so that you can see startup messages

  mesh.init(MESH_PREFIX, MESH_PASSWORD, &userScheduler, MESH_PORT);
  mesh.onReceive(&receivedCallback);
  mesh.onNewConnection(&newConnectionCallback);
  mesh.onChangedConnections(&changedConnectionCallback);
  mesh.onNodeTimeAdjusted(&nodeTimeAdjustedCallback);

  userScheduler.addTask(taskSendMessage);
  taskSendMessage.enable();
}
