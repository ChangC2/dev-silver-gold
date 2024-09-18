import Urls, { postRequest } from "../../common/urls";
import * as actionType from "../actionType";

const initialState = {
  cwDashboards: {},
  cwWidgets: [],
};

const reducer = (state = initialState, action) => {
  switch (action.type) {
    case actionType.cwUpdateDashboards:
      return {
        ...state,
        cwDashboards: action.data.custom_dashboards,
      };
    case actionType.cwUpdateWidgets:
      return {
        ...state,
        cwWidgets:
          action.data.custom_widgets === undefined
            ? []
            : action.data.custom_widgets,
      };
    default:
      return state;
  }
};

export default reducer;
