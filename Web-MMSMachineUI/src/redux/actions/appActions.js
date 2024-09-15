// You can use CONSTANTS.js file for below definitions of constants and import here.
export const IS_SPINNING = "IS_SPINNING";
export const SET_APP_DATA_STORE = "SET_APP_DATA_STORE";

export const isSpinning = (payload) => ({
  type: IS_SPINNING,
  payload,
});

export const setAppDataStore = (payload) => ({
  type: SET_APP_DATA_STORE,
  payload,
});
