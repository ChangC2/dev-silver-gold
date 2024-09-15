const char PAGE_MAIN[] PROGMEM = R"=====(
  <html>
    <head>
      <title>SLYTRACKER</title>
      <link rel="stylesheet" type="text/css" href="style.css" />
      <script type="application/javascript" src="script.js"></script>
    </head>
    <body>
      <div class="container">
        <div class="title">Settings</div>
        <div class="sub-container">
          <div class="settings-label">Customer ID</div>
          <input type="text" id="customer_id" name="customer_id" class="settings-input" />

          <div class="settings-label">Machine ID</div>
          <input type="text" id="machine_id" name="machine_id" class="settings-input" />

          <button onclick="setMachine()" class="wifi-settings-button">
            Save
          </button>
        </div>

        <div class="sub-container">
          <div class="sub-title">Ethernet</div>
          <input
            type="text"
            id="ipaddress"
            name="ipaddress"
            placeholder="IP Address"
            class="settings-input"
          />
          <button
            onclick="setEthernetIP()"
            class="settings-button"
          >
            Save
          </button>
        </div>

        <div class="sub-container">
          <div class="sub-title">WIFI</div>
          <div class="settings-label">SSID</div>
          <input type="text" id="ssid" name="ssid" class="settings-input" />

          <div class="settings-label">PASS</div>
          <input type="text" id="pass" name="pass" class="settings-input" />

          <button onclick="setWifi()" class="wifi-settings-button">
            Save
          </button>
        </div>
      </div>
    </body>
  </html>
)=====";

const char STYLE[] PROGMEM = R"=====(
  html,
  body {
    margin: 0;
    padding: 0;
    font-family: sans-serif;
    background-color: rgb(42, 44, 44);
  }

  .container {
    position: absolute;
    top: 10%;
    left: 50%;
    margin-left: -200px;
    width: 400px;
    padding: 20px;
    border: 1px white solid;
    background-color: #1e1e1e;
    border-radius: 8px;
  }

  .title {
    display: block;
    color: white;
    font-size: 21px;
  }

  .sub-container {
    display: block;
    border: white 1px;
    border-radius: 8px;
    padding: 20px;
    border: 1px rgb(150, 150, 150) solid;
    background-color: #303030;
    margin-top: 10px;
  }

  .sub-title {
    display: block;
    color: rgb(255, 255, 255);
    font-size: 16px;
  }

  .settings-label {
    color: rgb(255, 255, 255);
    font-size: 14px;
    width: 80px;
    display: inline-block;
    margin-top: 10px;
  }

  .settings-input {
    width: 270px;
    height: 32px;
    color: white;
    font-size: 14px;
    border: 1px white solid;
    border-radius: 5px;
    background-color: transparent;
    padding: 0 10px 0 10px;
    margin-top: 10px;
  }

  .settings-input:disabled {
    border: 1px rgb(150, 150, 150) solid;
    background-color: rgb(63, 63, 63);
  }

  .settings-button {
    width: 80px;
    height: 32px;
    color: white;
    font-size: 13px;
    border-radius: 5px;
    background-color: #1890ff;
    border: 0px;
  }

  .wifi-settings-button {
    width: 100%;
    height: 32px;
    color: white;
    font-size: 13px;
    border-radius: 5px;
    background-color: #1890ff;
    margin-top: 10px;
    border: 0px;
  }
)=====";

const char SCRIPT[] PROGMEM = R"=====(
  const urlPrefix = "/";
  const apiSetMachine = "/setMachine";
  const apiSetEthernetIP = "/setEthernetIP";
  const apiSetWifi = "/setWifi";
  const apiGetMachine = "/getMachine";

  setTimeout(() => {
    this.getMachine();
  }, 3000);

  function getMachine() {
    this.request(apiGetMachine, "get", {}, (response) => {
      if (response) {
        document.getElementById("customer_id").value = response.customer_id;
        document.getElementById("machine_id").value = response.machine_id;
      }
    });
  }

  function setMachine() {
    const customer_id = document.getElementById("customer_id").value;
    const machine_id = document.getElementById("machine_id").value;
    const data = {
      customer_id: customer_id,
      machine_id: machine_id,
    };
    this.request(apiSetMachine, "post", data, (response) => {});
  }

  function setEthernetIP() {
    const ipaddress = document.getElementById("ipaddress").value;
    const data = { ipaddress };
    this.request(apiSetEthernetIP, "post", data, (response) => {});
  }

  function setWifi() {
    const ssid = document.getElementById("ssid").value;
    const pass = document.getElementById("pass").value;
    const data = {
      ssid: ssid,
      pass: pass,
    };
    this.request(apiSetWifi, "post", data, (response) => {});
  }

  function request(url, method, data, callback) {
    const jsonData = JSON.stringify(data);
    let xhr = new XMLHttpRequest();
    xhr.open(method, url, true);
    xhr.setRequestHeader("Content-type", "application/json; charset=UTF-8");
    switch (method) {
      case "get":
        xhr.send();
        break;
      case "post":
        xhr.send(jsonData);
        break;
      default:
        break;
    }
    xhr.onload = function () {
      if (xhr.status === 200) {
        callback(JSON.parse(xhr.response));
      } else {
        callback();
      }
    };
  }
)=====";