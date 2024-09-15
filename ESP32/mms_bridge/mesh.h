
// ESP Mesh
#define MESH_PREFIX "Slytek_Mesh"
#define MESH_PASSWORD "Slytek_Mesh"
#define MESH_PORT 5555

Scheduler userScheduler;  // to control your personal task
painlessMesh mesh;

// User stub
void sendMessage();  // Prototype so PlatformIO doesn't complain

Task taskSendMessage(TASK_SECOND * 1, TASK_FOREVER, &sendMessage);

void sendMessage() {
}

// Needed for painless library
void receivedCallback(uint32_t from, String &msg) {
  kPath = msg.c_str();
  Serial.printf("echoNode: Received from %u msg=%s\n", from, kPath);
  postGanttData();
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
