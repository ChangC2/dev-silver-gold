import { applyMiddleware, compose, createStore } from "redux";
import thunk from "redux-thunk";
import { rootReducer } from "./reducers";
import { persistStore, persistReducer } from "redux-persist";
import storage from "redux-persist/lib/storage";

// applying persist redux
const rootPersistConfig = {
  key: "root",
  storage: storage,
  whitelist: ["userDataStore"],
};

const persistedReducer = persistReducer(rootPersistConfig, rootReducer);

// devtools for debugging in dev environment.
const devTools =
  // eslint-disable-next-line no-undef
  process.env.NODE_ENV !== "production" && window.__REDUX_DEVTOOLS_EXTENSION__
    ? window.__REDUX_DEVTOOLS_EXTENSION__ &&
      window.__REDUX_DEVTOOLS_EXTENSION__()
    : (a) => a;

export const store = createStore(
  persistedReducer,
  compose(applyMiddleware(thunk), devTools)
);

export const persistor = persistStore(store);
