import * as actionType from "../actionType";
import Urls, { postRequest } from "../../common/urls";

const initialState = {
  hennigList: {},
  hennigInfo: {},
};

const reducer = (state = initialState, action) => {
  let _hennigList;
  let _customerHennigList;
  switch (action.type) {
    case actionType.hennigUpdateHennigList:
      _hennigList = state.hennigList;
      _hennigList[action.data.customerId] = action.data.hennigData;
      return {
        ...state,
        hennigList: {
          ..._hennigList,
        },
      };
    case actionType.hennigUpdateHennigInfo:
      let _hennigInfo = state.hennigInfo;
      _hennigInfo[action.data.customerId] = { ...action.data.hennigInfo };
      return {
        ...state,
        hennigInfo: {
          ..._hennigInfo,
        },
      };
    case actionType.hennigAddNewHennig:
      _hennigList = state.hennigList;
      _hennigList[action.data.customerId] = [
        ..._hennigList[action.data.customerId],
        action.data.hennigInfo,
      ];
      //
      return {
        ...state,
        hennigList: {
          ..._hennigList,
        },
      };
    case actionType.hennigUpdateHennig:
      _hennigList = state.hennigList;

      _customerHennigList = _hennigList[action.data.customerId];
      try {
        let _selectedHennig = _customerHennigList.filter(
          (x) => x.sensor_id === action.data.hennigInfo.sensor_id
        )[0];
        _selectedHennig = { ...action.data.hennigInfo };

        _customerHennigList = [
          ..._customerHennigList.filter(
            (x) => x.sensor_id !== action.data.hennigInfo.sensor_id
          ),
          { ..._selectedHennig },
        ];

        _hennigList[action.data.customerId] = [..._customerHennigList];

        return {
          ...state,
          hennigList: {
            ..._hennigList,
          },
        };
      } catch (err) {}
      break;
    case actionType.hennigDeleteHennig:
      _hennigList = state.hennigList;
      _customerHennigList = _hennigList[action.data.customerId];
      try {
        _customerHennigList = [
          ..._customerHennigList.filter(
            (x) => x.sensor_id !== action.data.sensorId
          ),
        ];

        _hennigList[action.data.customerId] = [..._customerHennigList];

        return {
          ...state,
          hennigList: {
            ..._hennigList,
          },
        };
      } catch (err) {}
      break;
    default:
      return state;
  }
};
export default reducer;