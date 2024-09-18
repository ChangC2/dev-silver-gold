import { SET_FACTORY_DATA_STORE } from "redux/actions/factoryActions";
import { LS_ITEMS } from "services/CONSTANTS";

const initialState = {
  factoryDataStore: {
    accountId: "",
    customer_details: {},
    current_ganttdata: {},
    last_ganttdata: {},
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
    idle_times: [0, 0, 0, 0, 0, 0, 0, 0],
    uncat_time: 0,
    idle_status: "",
  },
};

export const factoryReducer = (state = initialState, action) => {
  switch (action.type) {
    case SET_FACTORY_DATA_STORE:
      localStorage.setItem(
        LS_ITEMS.factoryData,
        JSON.stringify(action.payload)
      );
      return {
        ...state,
        ...{ factoryDataStore: action.payload },
      };
    default:
      return state;
  }
};
