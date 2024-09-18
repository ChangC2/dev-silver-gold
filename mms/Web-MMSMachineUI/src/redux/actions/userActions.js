// You can use CONSTANTS.js file for below definitions of constants and import here.
export const SET_USER_DATA_STORE = "SET_USER_DATA_STORE";
export const SET_FROM_URL = "set-from-url";

export const setUserDataStore = (payload) => ({
  type: SET_USER_DATA_STORE,
  payload,
});

export const setFromUrl = (url) => ({
  type: SET_FROM_URL,
  payload: url,
});
