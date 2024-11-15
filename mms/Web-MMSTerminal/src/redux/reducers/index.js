/**
 * We purposely added 2 reducers for the example of combineReducers method.
 * There can be only 1 or even more than 2 reducers.
 * combineReducers defines the structure of the store object.
 */
import { combineReducers } from "redux";
import { appReducer } from "./appReducer";
import { factoryReducer } from "./factoryReducer";
import { userReducer } from "./userReducer";

export const rootReducer = combineReducers({
  appDataStore: appReducer,
  factoryDataStore: factoryReducer,
  userDataStore: userReducer,
});
