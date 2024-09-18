import { PAGETYPE, VIEWMODE } from "../../common/constants";
import * as actionType from "../actionType";

const initialState = {
  customer_id: "",
  page_type: PAGETYPE.dashboard,
  group_id: "all",
  is_menu_collapsed: false,
  isAutoScroll: false,
  scrollSpeed: 30,
  viewMode: VIEWMODE.listView,
  screenSize: {
    width: 1000,
    height: 500,
  },
  langIndex: 0, //lang = 0: English,  =1: Chinese, =2: Spanish,
};

const reducer = (state = initialState, action) => {
  switch (action.type) {
    case actionType.setLang:
      return {
        ...state,
        langIndex: action.data,
      };
    case actionType.appOnSelectMenu:
      return {
        ...state,
        customer_id: action.data.customer_id,
        page_type: action.data.page_type,
        group_id: action.data.group_id,
      };
    case actionType.appToggleMenuCollapse:
      return {
        ...state,
        is_menu_collapsed: !state.is_menu_collapsed,
      };
    case actionType.appUpdateConfigParams:
      return {
        ...state,
        ...action.data,
      };
    default:
      return state;
  }
};
export default reducer;

export function SetLanguage(langIndex, dispatch) {
  dispatch({
    type: actionType.setLang,
    data: langIndex,
  });
}

export function onSelectMenu(customer_id, page_type, group_id, dispatch) {
  dispatch({
    type: actionType.appOnSelectMenu,
    data: {
      customer_id: customer_id,
      page_type: page_type,
      group_id: group_id === undefined ? "" : group_id,
    },
  });
}

export function ToggleMenuCollapse(dispatch) {
  dispatch({
    type: actionType.appToggleMenuCollapse,
  });
}

export function UpdateAppConfig(newConfig, dispatch) {
  dispatch({
    type: actionType.appUpdateConfigParams,
    data: newConfig,
  });
}
