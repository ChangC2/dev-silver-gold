import { SET_FACTORY_DATA_STORE } from "redux/actions/factoryActions";
import { LS_ITEMS } from "services/CONSTANTS";

const initialState = {
  factoryDataStore: {
    accountId: "",
    customer_details: {},
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
