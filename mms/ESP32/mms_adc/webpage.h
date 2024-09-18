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
          <input
            type="text"
            id="customer_id"
            name="customer_id"
            class="settings-input"
          />

          <div class="settings-label">Device ID</div>
          <input
            type="text"
            id="device_id"
            name="device_id"
            class="settings-input"
          />

          <button onclick="setMachine()" class="wifi-settings-button">
            Save
          </button>
        </div>

        <div class="title" style="margin-top: 10px">Relay</div>
        <div class="sub-container-slider">
          <div style="text-align: center">
            <div class="settings-label">Relay 1</div>
            <div>
              <label class="switch">
                <input type="checkbox" onclick="handleRelay1(this)" />
                <span class="slider round"></span>
              </label>
            </div>
          </div>

          <div style="text-align: center">
            <div class="settings-label">Relay 2</div>
            <div>
              <label class="switch">
                <input type="checkbox" onclick="handleRelay2(this)" />
                <span class="slider round"></span>
              </label>
            </div>
          </div>

          <div style="text-align: center">
            <div class="settings-label">Relay 3</div>
            <div>
              <label class="switch">
                <input type="checkbox" onclick="handleRelay3(this)" />
                <span class="slider round"></span>
              </label>
            </div>
          </div>

          <div style="text-align: center">
            <div class="settings-label">Relay 4</div>
            <div>
              <label class="switch">
                <input type="checkbox" onclick="handleRelay4(this)" />
                <span class="slider round"></span>
              </label>
            </div>
          </div>
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
          <button onclick="setEthernetIP()" class="settings-button">Save</button>
        </div>

        <div class="sub-container">
          <div class="sub-title">WIFI</div>
          <div class="settings-label">SSID</div>
          <input type="text" id="ssid" name="ssid" class="settings-input" />

          <div class="settings-label">PASS</div>
          <input type="text" id="pass" name="pass" class="settings-input" />

          <button onclick="setWifi()" class="wifi-settings-button">Save</button>
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

  .sub-container-slider {
    display: flex;
    justify-content: space-around;
    border: white 1px;
    border-radius: 8px;
    padding: 20px;
    border: 1px rgb(150, 150, 150) solid;
    background-color: #303030;
    margin-top: 10px;
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

  .btnRelay {
    padding: 4px;
    margin: 4px;
  }

  .switch {
    position: relative;
    display: inline-block;
    width: 60px;
    height: 34px;
    margin-top: 5px;
  }

  .switch input {
    opacity: 0;
    width: 0;
    height: 0;
  }

  .slider {
    position: absolute;
    cursor: pointer;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background-color: #ccc;
    -webkit-transition: 0.4s;
    transition: 0.4s;
  }

  .slider:before {
    position: absolute;
    content: "";
    height: 26px;
    width: 26px;
    left: 4px;
    bottom: 4px;
    background-color: white;
    -webkit-transition: 0.4s;
    transition: 0.4s;
  }

  input:checked + .slider {
    background-color: #2196f3;
  }

  input:focus + .slider {
    box-shadow: 0 0 1px #2196f3;
  }

  input:checked + .slider:before {
    -webkit-transform: translateX(26px);
    -ms-transform: translateX(26px);
    transform: translateX(26px);
  }

  /* Rounded sliders */
  .slider.round {
    border-radius: 34px;
  }

  .slider.round:before {
    border-radius: 50%;
  }

  .dot {
    height: 30px;
    width: 30px;
    background-color: blue;
    border-radius: 50%;
    display: inline-block;
  }
)=====";

const char SCRIPT[] PROGMEM = R"=====(
  const urlPrefix = "/";
  const apiSetMachine = "/setMachine";
  const apiSetEthernetIP = "/setEthernetIP";
  const apiSetWifi = "/setWifi";
  const apiGetMachine = "/getMachine";
  const apiSetRelay = "/setRelay";

  setTimeout(() => {
    this.getMachine();
  }, 3000);

  function getMachine() {
    this.request(apiGetMachine, "get", {}, (response) => {
      if (response) {
        document.getElementById("customer_id").value = response.customer_id;
        document.getElementById("device_id").value = response.device_id;
      }
    });
  }

  function setMachine() {
    const customer_id = document.getElementById("customer_id").value;
    const device_id = document.getElementById("device_id").value;
    const data = {
      customer_id: customer_id,
      device_id: device_id,
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

  function handleRelay1(cb) {
    console.log(cb.checked);
    const data = {
      relay1: cb.checked,
    };
    this.request(apiSetRelay, "post", data, (response) => {});
  }
  function handleRelay2(cb) {
    console.log(cb.checked);
    const data = {
      relay2: cb.checked,
    };
    this.request(apiSetRelay, "post", data, (response) => {});
  }
  function handleRelay3(cb) {
    console.log(cb.checked);
    const data = {
      relay3: cb.checked,
    };
    this.request(apiSetRelay, "post", data, (response) => {});
  }
  function handleRelay4(cb) {
    console.log(cb.checked);
    const data = {
      relay4: cb.checked,
    };
    this.request(apiSetRelay, "post", data, (response) => {});
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