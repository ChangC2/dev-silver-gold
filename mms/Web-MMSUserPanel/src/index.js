import React from "react";
import ReactDOM from "react-dom";
import "./index.css";

import * as serviceWorker from "./serviceWorker";
import App from "./App";
import "antd/dist/antd.css";

import { Provider } from "react-redux";

import { store, persistor } from "./services/redux/index";
import { PersistGate } from "redux-persist/integration/react";

const appVersion = "202419130326";
const scriptJs = `/static/js/main.js?version=${appVersion}`;
const linkCss = `/static/css/main.css?version=${appVersion}`;

// Dynamically inject the scripts and styles
const script = document.createElement("script");
script.src = scriptJs;
document.body.appendChild(script);

const link = document.createElement("link");
link.href = linkCss;
link.rel = "stylesheet";
document.head.appendChild(link);

ReactDOM.render(
  <Provider store={store}>
    <PersistGate persistor={persistor} loading={null}>
      <App />
    </PersistGate>
  </Provider>,
  document.getElementById("root")
);

// If you want your app to work offline and load faster, you can change
// unregister() to register() below. Note this comes with some pitfalls.
// Learn more about service workers: https://bit.ly/CRA-PWA
serviceWorker.unregister();
