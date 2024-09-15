import * as actionType from "../actionType";

const initialState = {
  isAuth: false,
  laborCategories: [],
};

const reducer = (state = initialState, action) => {
  switch (action.type) {
    case actionType.actSetAuthInfo:
      return {
        ...state,
        ...action.data,
        isAuth: true,
      };
    case actionType.actSetLaborInfo:
      return {
        ...state,
        ...action.data,
        isAuth: true,
      };
    case actionType.actSignOut:
      return {
        isAuth: false,
        laborCategories: [],
      };
    case actionType.authUpdateLaborCategories:
      return {
        ...state,
        laborCategories:
          action.data.labor_categories === undefined
            ? []
            : action.data.labor_categories,
      };
    default:
      return state;
  }
};
export default reducer;
