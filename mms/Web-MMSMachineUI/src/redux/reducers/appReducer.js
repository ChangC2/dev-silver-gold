import { IS_SPINNING, SET_APP_DATA_STORE } from "redux/actions/appActions";
import { LS_ITEMS } from "services/CONSTANTS";

const initialState = {
  isSpinning: false,
  appDataStore: {
    width: 1024,
    height: 768,

    customer_id: "",
    customer_details: {},
    machine_id: "",
    machine_picture_url: "",
    machine_status: [
      "Clear Chips",
      "Wait Materials",
      "Wait Tooling",
      "Break",
      "No Operator",
      "P.M",
      "Unplanned Repair",
      "Other",
      "Idle-Uncategorized",
      "In Cycle",
      "Offline",
    ],
    current_ganttdata: {},
    last_ganttdata: {},
    time_stop: "00:01:30",
  },
};

export const appReducer = (state = initialState, action) => {
  switch (action.type) {
    case IS_SPINNING:
      return {
        ...state,
        ...{ isSpinning: action.payload },
      };
    case SET_APP_DATA_STORE:
      localStorage.setItem(LS_ITEMS.appData, JSON.stringify(action.payload));
      return {
        ...state,
        ...{ appDataStore: action.payload },
      };
    default:
      return state;
  }
};
