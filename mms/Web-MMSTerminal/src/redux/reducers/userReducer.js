import { SET_FROM_URL, SET_USER_DATA_STORE } from "redux/actions/userActions";
import { LS_ITEMS } from "services/CONSTANTS";

const initialState = {
  fromUrl: undefined,
  userDataStore: {
    id: "",
    username: "",
    password: "",
    username_full: "UnAttended",
    user_picture: "",
    security_level: "",
    customer_id: "",
  },
};

export const userReducer = (state = initialState, action) => {
  switch (action.type) {
    case SET_USER_DATA_STORE:
      localStorage.setItem(LS_ITEMS.userData, JSON.stringify(action.payload));
      return {
        ...state,
        ...{ userDataStore: action.payload },
      };
    case SET_FROM_URL:
      return {
        ...state,
        ...{ fromUrl: action.payload },
      };
    default:
      return state;
  }
};
