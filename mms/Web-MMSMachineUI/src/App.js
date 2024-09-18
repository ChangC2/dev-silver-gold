// Handle console logs
import "utils/dropConsole";
// antd framework style
import "antd/dist/antd.less";
// ROUTER
import { RouterConfig } from "navigation/RouterConfig";
import { BrowserRouter } from "react-router-dom";

import { ProvideAuth } from "navigation/ProvideAuth";
import "./App.css";
// Redux
import { Provider } from "react-redux";

import axios from "axios";
import { PersistGate } from "redux-persist/integration/react";
import { persistor, store } from "./redux/store";

store.subscribe(listener);

function select(state) {
  const { token } = state.userDataStore;
  if (token === undefined || token === "") return "";
  return token;
}

function listener() {
  let token = select(store.getState());

  axios.defaults.headers.common["Content-Type"] =
    "application/json; charset=UTF-8";
  if (token === "") {
    delete axios.defaults.headers.common["Authorization"];
  } else {
    axios.defaults.headers.common["Authorization"] = "Bearer " + token;
  }
}

function App() {
  return (
    <Provider store={store}>
      <PersistGate persistor={persistor} loading={null}>
        <ProvideAuth>
          <BrowserRouter>
            <RouterConfig />
          </BrowserRouter>
        </ProvideAuth>
      </PersistGate>
    </Provider>
  );
}

export default App;
