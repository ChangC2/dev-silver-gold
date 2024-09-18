import Urls, { postRequest } from "./urls";
import * as actionType from "../redux/actionType";

export function getDetailedHennigData(sensorId, startTime, endTime, callback) {
  const url = Urls.HENNIG_GET_HENNIG_DATA;
  const param = {
    sensor_id: sensorId,
    from_date: startTime,
    to_date: endTime,
  };
  postRequest(url, param, (res) => {
    if (res.status === true) {
      callback(res.data);
    } else {
      callback(null);
    }
  });
}

export function getHennigCSVData(sensorId, startTime, endTime, callback) {
  const url = Urls.HENNIG_GET_HENNIG_CSV_DATA;
  const param = {
    sensor_id: sensorId,
    from_date: startTime,
    to_date: endTime,
  };
  postRequest(url, param, (res) => {
    if (res.status === true) {
      callback(res.data);
    } else {
      callback(null);
    }
  });
}

export function getDetailedHennigDataWithFromTo(
  sensorId,
  fromDate,
  toDate,
  callback
) {
  const url = Urls.HENNIG_GET_HENNIG_DATA;
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

export function getHennigDataForGraph(fromDate, toDate, callback) {
  const url = Urls.HENNIG_GET_HENNIG_PERIOD_DATA;
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

export function getHennigList(customerId, dispatch, callback) {
  const url = Urls.HENNIG_GET_LIST;
  postRequest(url, { customer_id: customerId }, (res) => {
    if (res.status === true) {
      callback(true);
      dispatch({
        type: actionType.hennigUpdateHennigList,
        data: {
          customerId: customerId,
          hennigData: res.data,
        },
      });
    }
  });
}

export function getHennigInfo(customerId, sensorIdList, dispatch) {
  const url = Urls.HENNIG_GET_INFO_LIST;
  const param = { idList: sensorIdList };
  postRequest(url, param, (res) => {
    if (res.status === true) {
      dispatch({
        type: actionType.hennigUpdateHennigInfo,
        data: {
          customerId: customerId,
          hennigInfo: res.data,
        },
      });
    }
  });
}
export function addHennig(customerId, hennigInfo, dispatch, callback) {
  const url = Urls.HENNIG_ADD_HENNIG;
  postRequest(url, { ...hennigInfo, customer_id: customerId }, (res) => {
    if (res.status === true) {
      callback({
        status: true,
        message: `${hennigInfo.sensor_name} has successfully been added.`,
      });

      dispatch({
        type: actionType.hennigAddNewHennig,
        data: {
          customerId: customerId,
          hennigInfo: hennigInfo,
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
export function updateHennigInfo(
  customerId,
  newHennigInfo,
  dispatch,
  callback
) {
  const url = Urls.HENNIG_UPDATE_HENNIG;
  postRequest(url, { ...newHennigInfo, customer_id: customerId }, (res) => {
    if (res.status === true) {
      callback(true);
      dispatch({
        type: actionType.hennigUpdateHennig,
        data: {
          customerId: customerId,
          hennigInfo: newHennigInfo,
        },
      });
    } else {
      callback(false);
    }
  });
}

export function deleteHennig(customerId, sensorId, dispatch) {
  const url = Urls.HENNIG_DELETE_HENNIG;

  postRequest(url, { customer_id: customerId, sensor_id: sensorId }, (res) => {
    if (res.status === true) {
      dispatch({
        type: actionType.hennigDeleteHennig,
        data: {
          customerId: customerId,
          sensorId: sensorId,
        },
      });
    } else {
    }
  });
}
