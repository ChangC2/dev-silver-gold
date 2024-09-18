import { combineReducers } from "redux";
import { appReducer } from "./appReducer";
import { factoryReducer } from "./factoryReducer";
import { userReducer } from "./userReducer";

export const rootReducer = combineReducers({
  appDataStore: appReducer,
  factoryDataStore: factoryReducer,
  userDataStore: userReducer,
});
