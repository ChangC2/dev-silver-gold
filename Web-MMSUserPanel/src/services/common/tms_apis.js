import Urls, { postRequest } from "./urls";

import * as actionType from "../redux/actionType";

export function getTimeSavingList(customerId, dispatch, callback) {
  const url = Urls.GET_TIME_SAVINGS;
  postRequest(url, { customer_id: customerId }, (res) => {
    callback(true);
    var data = [];
    if (res !== undefined && res.status === true) {
      data = res.data;
    }
    dispatch({
      type: actionType.timeSavingGetList,
      data: {
        customerId: customerId,
        timeSavingData: data,
      },
    });
  });
}

export function getAlarmsFilters(customerId, callback) {
  const url = Urls.GET_ALARMS_FILTERS;
  postRequest(url, { customer_id: customerId }, (res) => {
    callback(res);
  });
}

export function getAlarms(param, callback) {
  const url = Urls.GET_ALARMS;
  postRequest(url, param, (res) => {
    callback(res);
  });
}
