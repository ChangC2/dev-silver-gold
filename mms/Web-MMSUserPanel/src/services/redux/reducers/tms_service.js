import * as actionType from "../actionType";
import Urls, { postRequest } from "../../common/urls";

const initialState = {
  timeSavingList: {},
};

const reducer = (state = initialState, action) => {
  switch (action.type) {
    case actionType.timeSavingGetList:
      var _timeSavingList = state.timeSavingList;
      _timeSavingList[action.data.customerId] = action.data.timeSavingData;
      return {
        ...state,
        timeSavingList: {
          ..._timeSavingList,
        },
      };
    default:
      return state;
  }
};

export default reducer;
