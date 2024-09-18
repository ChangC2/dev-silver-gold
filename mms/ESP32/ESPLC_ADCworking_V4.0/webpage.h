const char PAGE_MAIN[] PROGMEM = R"=====(
 <html>
 <head>
     <title>ESP32 ETHERNET SERVER</title>
     <link rel='stylesheet' type='text/css' href='style.css'>
 </head>
 <body>
     <h1>ESP32 ETHERNET SERVER</h1>
     <div class='container'>
         <div class='card ethernet'>
             <h5>Ethernet</h5>
             <div class='in_cont'><input type='text' id='ipaddress' name='ipaddress' /><button id='ipsetting'
                     onclick='getIpAddress()'>IP SETTING</button></div>
         </div>
         <div class='card wifi'>
             <h5>Wifi</h5>
             <div class='in_cont'>IP&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type='text' id='wifiIP' name='wifiIP'
                     disabled /></div>
             <div class='in_cont'>SSID<input type='text' id='ssid' name='ssid' /></div>
             <div class='in_cont'>PASS<input type='text' id='pass' name='pass' /></div>
             <div class='in_cont'><button onclick='handleWifi()'>submit</button></div>
         </div>
     </div>
     <script type='application/javascript' src='script.js'></script>
 </body>
 </html>
)=====";

const char STYLE[] PROGMEM = R"=====(
html,
body {
    margin: 0;
    padding: 0;
}
body {
    background: #eee;
    text-align: center;
    font-family: sans-serif;
}
.container {
    padding: 10px;
    display: flex;
    justify-content: space-between;
    background-color: gray;
}
.card {
    display: flex;
    flex-direction: column;
    justify-content: center;
    width: 100%;
    margin: 10px;
    padding: 10px;
    background-color: green;
}
.in-container {
    display: flex;
    flex-direction: row;
    justify-content: center;
    align-items: center;
    margin: 4px;
}
)=====";


const char SCRIPT[] PROGMEM = R"=====(
        console.log('Hello: ');
        const urlPrefix = '/';
        const wifiURL = '/wifi';
        const ipURL = '/inline';
        const dataURL = '/wifiIP';

        setInterval(() => {
            console.log('getDatas: ');
            this.getDatas();
        }, 2000);

        function getDatas() {
            this.request(dataURL, 'get', {}, (response) => {
                console.log('getDatas response: ', response, typeof response);
                if (response) {
                    document.getElementById('wifiIP').value = response.wifiIP;
                }
            });
        }
        function getIpAddress() {
            const ipaddress = document.getElementById('ipaddress').value;
            const data = { ipaddress };
            this.request(ipURL, 'post', data, (response) => {
            });
        }
        function handleWifi() {
            const ssid = document.getElementById('ssid').value;
            const pass = document.getElementById('pass').value;
            const data = {
                ssid: ssid,
                pass: pass
            };
            this.request(wifiURL, 'post', data, (response) => {
            });
        }
        function request(url, method, data, callback) {
            const jsonData = JSON.stringify(data);
            let xhr = new XMLHttpRequest();
            xhr.open(method, url, true)
            xhr.setRequestHeader('Content-type', 'application/json; charset=UTF-8');
            switch (method) {
                case 'get':
                    xhr.send();
                    break;
                case 'post':
                    xhr.send(jsonData);
                    break;
                default:
                    break;
            }
            xhr.onload = function () {
                if (xhr.status === 200) {
                    callback(JSON.parse(xhr.response));
                } else { callback(); }
            }
        }  
)=====";

const char PAGE_WIFI[] PROGMEM = R"=====(
<html>

<head>
    <title>ESP32 WEB SERVER</title>

    <style>
        html,
        body {
            margin: 0;
            padding: 0;
        }

        body {
            background: #eee;
            text-align: center;
            font-family: sans-serif;
        }

        .container {
            padding: 10px;
            display: flex;
            justify-content: space-between;
            background-color: gray;
        }

        .card {
            display: flex;
            flex-direction: column;
            justify-content: center;
            width: 100%;
            margin: 10px;
            padding: 10px;
            background-color: green;
        }

        .analog {
            display: flex;
            flex-direction: column;
        }

        .current {
            background-color: blue;
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
            -webkit-transition: .4s;
            transition: .4s;
        }

        .slider:before {
            position: absolute;
            content: '';
            height: 26px;
            width: 26px;
            left: 4px;
            bottom: 4px;
            background-color: white;
            -webkit-transition: .4s;
            transition: .4s;
        }

        input:checked+.slider {
            background-color: #2196F3;
        }

        input:focus+.slider {
            box-shadow: 0 0 1px #2196F3;
        }

        input:checked+.slider:before {
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
    </style>
</head>

<body>
    <h1>ESP32 WEB SERVER</h1>
    <input type='text' id='ipaddress' class='form-control' name='ipaddress' />
    <button id='ipsetting' onclick='getIpAddress()'>IP SETTING</button>

    <div class='container'>
        <div class='card relay'>
            <h5>Relay</h5>
            <div style='display: flex; flex-direction: row; justify-content: center; align-items: center; margin: 4px;'>
                <!-- Rounded switch -->
                Relay1
                <label class='switch'>
                    <input type='checkbox' onclick='handleRelay1(this)'>
                    <span class='slider round'></span>
                </label>
            </div>
            <div class=''
                style='display: flex; flex-direction: row; justify-content: center; align-items: center; margin: 4px;'>
                <!-- Rounded switch -->
                Relay2
                <label class='switch'>
                    <input type='checkbox' onclick='handleRelay2(this)'>
                    <span class='slider round'></span>
                </label>
            </div>
            <div class=''
                style='display: flex; flex-direction: row; justify-content: center; align-items: center; margin: 4px;'>
                <!-- Rounded switch -->
                Relay3
                <label class='switch'>
                    <input type='checkbox' onclick='handleRelay3(this)'>
                    <span class='slider round'></span>
                </label>
            </div>
            <div class=''
                style='display: flex; flex-direction: row; justify-content: center; align-items: center; margin: 4px;'>
                <!-- Rounded switch -->
                Relay4
                <label class='switch'>
                    <input type='checkbox' onclick='handleRelay4(this)'>
                    <span class='slider round'></span>
                </label>
            </div>
        </div>
        <div class='card analog'>
            <h5>Analog</h5>
            <div style='display: flex; flex-direction: row; justify-content: space-between;'>
                <div class='card current'>
                    <h5>Current</h5>
                    <div style='display: flex; flex-direction: row; align-items: center; margin: 4px;'>
                        CH1
                        <input type='text' id='current1' class='form-control' name='current1' disabled />
                    </div>
                    <div style='display: flex; flex-direction: row; align-items: center; margin: 4px;'>
                        CH2
                        <input type='text' id='current2' class='form-control' name='current2' disabled />
                    </div>
                    <div style='display: flex; flex-direction: row; align-items: center; margin: 4px;'>
                        CH3
                        <input type='text' id='current3' class='form-control' name='current3' disabled />
                    </div>
                    <div style='display: flex; flex-direction: row; align-items: center; margin: 4px;'>
                        CH4
                        <input type='text' id='current4' class='form-control' name='current4' disabled />
                    </div>
                </div>
                <div class='card current'>
                    <h5>Voltage</h5>
                    <div style='display: flex; flex-direction: row; align-items: center; margin: 4px;'>
                        CH1
                        <input type='text' id='volt1' class='form-control' name='volt1' disabled />
                    </div>
                    <div style='display: flex; flex-direction: row; align-items: center; margin: 4px;'>
                        CH2
                        <input type='text' id='volt2' class='form-control' name='volt2' disabled />
                    </div>
                    <div style='display: flex; flex-direction: row; align-items: center; margin: 4px;'>
                        CH3
                        <input type='text' id='volt3' class='form-control' name='volt3' disabled />
                    </div>
                    <div style='display: flex; flex-direction: row; align-items: center; margin: 4px;'>
                        CH4
                        <input type='text' id='volt4' class='form-control' name='volt4' disabled />
                    </div>
                </div>
            </div>
        </div>
        <div class='card digital'>
            <h5>Digital</h5>
            <div style='display: flex; justify-content: center; align-items: center; margin: 4px;'>
                CH1<div class='dot' id='din1'></div>
            </div>
            <div style='display: flex; justify-content: center; align-items: center; margin: 4px;'>
                CH2<div class='dot' id='din2'></div>
            </div>
            <div style='display: flex; justify-content: center; align-items: center; margin: 4px;'>
                CH3<div class='dot' id='din3'></div>
            </div>
            <div style='display: flex; justify-content: center; align-items: center; margin: 4px;'>
                CH4<div class='dot' id='din4'></div>
            </div>
        </div>
        <div class='card wifi'>
            <h5>Wifi</h5>
            <div style='display: flex; flex-direction: row; justify-content: center; align-items: center; margin: 4px;'>
                SSID
                <input type='text' id='ssid' class='form-control' name='ch1' />
            </div>
            <div style='display: flex; flex-direction: row; justify-content: center; align-items: center; margin: 4px;'>
                PASS
                <input type='text' id='pass' class='form-control' name='ch1' />
            </div>
            <div style='display: flex; flex-direction: row; justify-content: center; align-items: center; margin: 4px;'>
                <button onclick='handleWifi()'>submit</button>
            </div>
        </div>
    </div>
    <script type='text/javascript'>
        const urlPrefix = '/';
        const relayURL = '/relay';
        const dataURL = '/data';
        const wifiURL = '/wifi';
        const ipURL = '/inline';

        setInterval(() => {
          this.getDatas();
        }, 2000);

        function getDatas() {
            this.request(dataURL, 'get', {}, (response) => {
              console.log('response: ', response, typeof response);
              if (response) {
                document.getElementById('current1').value = response.current1;
                document.getElementById('current2').value = response.current2;
                document.getElementById('current3').value = response.current3;
                document.getElementById('current4').value = response.current4;
                document.getElementById('volt1').value = response.volt1;
                document.getElementById('volt2').value = response.volt2;
                document.getElementById('volt3').value = response.volt3;
                document.getElementById('volt4').value = response.volt4;
                document.getElementById('din1').style.backgroundColor = response.din1 ? 'red' : 'blue';
                document.getElementById('din2').style.backgroundColor = response.din2 ? 'red' : 'blue';
                document.getElementById('din3').style.backgroundColor = response.din3 ? 'red' : 'blue';
                document.getElementById('din4').style.backgroundColor = response.din4 ? 'red' : 'blue';
            }
          });
        }
        function getIpAddress() {
            const ipaddress = document.getElementById('ipaddress').value;
            const data = {
                idaddress
            };
            this.request(ipURL, 'post', data, (response) => {              
            });
        }
        function handleRelay1(cb) {
            console.log(cb.checked);
            const data = {
                relay1: cb.checked
            };
            this.request(relayURL, 'post', data, (response) => {});
        }
        function handleRelay2(cb) {
            console.log(cb.checked);
            const data = {
                relay2: cb.checked
            };
            this.request(relayURL, 'post', data, (response) => {});
        }
        function handleRelay3(cb) {
            console.log(cb.checked);
            const data = {
                relay3: cb.checked
            };
            this.request(relayURL, 'post', data, (response) => {});
        }
        function handleRelay4(cb) {
            console.log(cb.checked);
            const data = {
                relay4: cb.checked
            };
            this.request(relayURL, 'post', data, (response) => {});
        }
        function handleWifi() {
            const ssid = document.getElementById('ssid').value;
            const pass = document.getElementById('pass').value;
            const data = {
                ssid: ssid,
                pass: pass
            };
            this.request(wifiURL, 'post', data, (response) => {});
        }
        function request(url, method, data, callback) {
            const jsonData = JSON.stringify(data);
            let xhr = new XMLHttpRequest();
            xhr.open(method, url, true)
            xhr.setRequestHeader('Content-type', 'application/json; charset=UTF-8');
            switch (method) {
                case 'get':
                    xhr.send();
                    break;
                case 'post':
                    xhr.send(jsonData);
                    break;
                default:
                    break;
            }
            xhr.onload = function () {
                if (xhr.status === 200) {
                    console.log('success', xhr.response);
                    const response = JSON.parse(xhr.response);
                    callback(response);
                } else {
                    console.log('Error', xhr.responseText);
                    callback();
                }
            }
        }
    </script>
</body>
</html>
)=====";
