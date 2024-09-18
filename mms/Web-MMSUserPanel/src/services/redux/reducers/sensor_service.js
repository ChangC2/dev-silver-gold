import * as actionType from "../actionType";

const initialState = {
  sensorList: {},
  sensorInfo: {},
};

const reducer = (state = initialState, action) => {
  let _sensorList;
  let _customerSensorList;
  switch (action.type) {
    case actionType.sensorUpdateSensorList:
      _sensorList = state.sensorList;
      _sensorList[action.data.customerId] = action.data.sensorData;
      return {
        ...state,
        sensorList: {
          ..._sensorList,
        },
      };
    case actionType.sensorUpdateSensorInfo:
      let _sensorInfo = state.sensorInfo;
      _sensorInfo[action.data.customerId] = { ...action.data.sensorInfo };
      return {
        ...state,
        sensorInfo: {
          ..._sensorInfo,
        },
      };
    case actionType.sensorAddNewSensor:
      _sensorList = state.sensorList;
      _sensorList[action.data.customerId] = [
        ..._sensorList[action.data.customerId],
        action.data.sensorInfo,
      ];
      //
      return {
        ...state,
        sensorList: {
          ..._sensorList,
        },
      };
    case actionType.sensorUpdateSensor:
      _sensorList = state.sensorList;

      _customerSensorList = _sensorList[action.data.customerId];
      try {
        let _selectedSensor = _customerSensorList.filter(
          (x) => x.sensor_id === action.data.sensorInfo.sensor_id
        )[0];
        _selectedSensor = { ...action.data.sensorInfo };

        _customerSensorList = [
          ..._customerSensorList.filter(
            (x) => x.sensor_id !== action.data.sensorInfo.sensor_id
          ),
          { ..._selectedSensor },
        ];

        _sensorList[action.data.customerId] = [..._customerSensorList];

        return {
          ...state,
          sensorList: {
            ..._sensorList,
          },
        };
      } catch (err) {}
      break;
    case actionType.sensorDeleteSensor:
      _sensorList = state.sensorList;
      _customerSensorList = _sensorList[action.data.customerId];
      try {
        _customerSensorList = [
          ..._customerSensorList.filter(
            (x) => x.sensor_id !== action.data.sensorId
          ),
        ];

        _sensorList[action.data.customerId] = [..._customerSensorList];

        return {
          ...state,
          sensorList: {
            ..._sensorList,
          },
        };
      } catch (err) {}
      break;
    default:
      return state;
  }
};

export default reducer;


