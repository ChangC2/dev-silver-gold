import { combineReducers, createStore } from "redux";
import { persistReducer, persistStore } from "redux-persist";
import storage from "redux-persist/lib/storage";
import app from "./reducers/app";
import authService from "./reducers/auth_service";
import cncService from "./reducers/cnc_service";
import cwService from "./reducers/cw_service";
import sensorService from "./reducers/sensor_service";
import hennigService from "./reducers/hennig_service";
import tmsService from "./reducers/tms_service";

const rootReducer = combineReducers({
  app: app,
  authService: authService,
  sensorService: sensorService,
  hennigService: hennigService,
  cncService: cncService,
  tmsService: tmsService,
  cwService: cwService,
});

// applying persist redux
const rootPersistConfig = {
  key: "root",
  storage: storage,
  blacklist: [
    "sensorService",
    "hennigService",
    "cncService",
    "tmsService",
    "cwService",
  ],
};

const devTools =
  window.__REDUX_DEVTOOLS_EXTENSION__ && window.__REDUX_DEVTOOLS_EXTENSION__();

const persistedReducer = persistReducer(rootPersistConfig, rootReducer);

export const store = createStore(persistedReducer, devTools);
export const persistor = persistStore(store);

const Todo = () => {
  return { store, persistor };
};
export default Todo;
