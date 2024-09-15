import Urls, { postRequest } from "./urls";

import * as actionType from "../redux/actionType";

export function getDetailedSensorData(sensorId, currentDate, callback) {
  const url = Urls.SENSOR_GET_SENSOR_DATA;
  const param = {
    sensor_id: sensorId,
    read_date: currentDate,
  };
  postRequest(url, param, (res) => {
    if (res.status === true) {
      callback(res.data);
    } else {
      callback(null);
    }
  });
}

export function getDetailedSensorDataWithFromTo(
  sensorId,
  fromDate,
  toDate,
  callback
) {
  const url = Urls.SENSOR_GET_SENSOR_DATA;
  const param = {
    sensor_id: sensorId,
    from_date: fromDate,
    to_date: toDate,
  };

  postRequest(url, param, (res) => {
    if (res === null) {
      callback(null);
    } else {
      if (res.status === true) {
        callback(res.data);
      } else {
        callback();
      }
    }
  });
}

export function getSensorDataForGraph(fromDate, toDate, callback) {
  const url = Urls.SENSOR_GET_SENSOR_PERIOD_DATA;
  const param = {
    from_date: fromDate,
    to_date: toDate,
  };
  postRequest(url, param, (res) => {
    if (res === null) {
      callback(null);
    } else {
      if (res.status === true) {
        callback(res.data);
      } else {
        callback();
      }
    }
  });
}

export function getSensorList(customerId, dispatch, callback) {
  const url = Urls.SENSOR_GET_LIST;
  postRequest(url, { customer_id: customerId }, (res) => {
    if (res.status === true) {
      callback(true);

      dispatch({
        type: actionType.sensorUpdateSensorList,
        data: {
          customerId: customerId,
          sensorData: res.data,
        },
      });
    }
  });
}

export function getSensorInfo(customerId, sensorIdList, dispatch) {
  const url = Urls.SENSOR_GET_INFO_LIST;
  const param = { idList: sensorIdList };
  postRequest(url, param, (res) => {
    if (res.status === true) {
      dispatch({
        type: actionType.sensorUpdateSensorInfo,
        data: {
          customerId: customerId,
          sensorInfo: res.data,
        },
      });
    }
  });
}

export function addSensor(customerId, sensorInfo, dispatch, callback) {
  const url = Urls.SENSOR_ADD_SENSOR;

  postRequest(url, { ...sensorInfo, customer_id: customerId }, (res) => {
    //
    if (res.status === true) {
      callback({
        status: true,
        message: `${sensorInfo.sensor_name} has successfully been added.`,
      });

      dispatch({
        type: actionType.sensorAddNewSensor,
        data: {
          customerId: customerId,
          sensorInfo: sensorInfo,
        },
      });
    } else {
      callback({
        status: false,
        message: `${res.message}`,
      });
    }
  });
}

export function updateSensorInfo(
  customerId,
  newSensorInfo,
  dispatch,
  callback
) {
  const url = Urls.SENSOR_UPDATE_SENSOR;

  postRequest(url, { ...newSensorInfo, customer_id: customerId }, (res) => {
    if (res.status === true) {
      callback(true);
      dispatch({
        type: actionType.sensorUpdateSensor,
        data: {
          customerId: customerId,
          sensorInfo: newSensorInfo,
        },
      });
    } else {
      callback(false);
    }
  });
}

export function deleteSensor(customerId, sensorId, dispatch) {
  const url = Urls.SENSOR_DELETE_SENSOR;

  postRequest(url, { customer_id: customerId, sensor_id: sensorId }, (res) => {
    if (res.status === true) {
      dispatch({
        type: actionType.sensorDeleteSensor,
        data: {
          customerId: customerId,
          sensorId: sensorId,
        },
      });
    } else {
    }
  });
}
