import moment from "moment";
import Urls, { postRequest } from "./urls";

import * as actionType from "../redux/actionType";
import { onlyUnique } from "./functions";

const ct = require("countries-and-timezones");
const timezones = ct.getAllTimezones();
const timezoneList = Object.keys(timezones).map((x) => timezones[x]);

export function ExecuteQuery(query, callback) {
  const url = Urls.EXECUTE_QUERY;
  const param = {
    query: query,
  };
  postRequest(url, param, (res) => {
    if (res.status === true) {
      callback(res.data);
    } else {
      callback(null);
    }
  });
}

export function GetAllCustomerInfo(customerString, dispatch, callback) {
  const url = Urls.GET_CUSTOMER_ALL_INFO;
  const param = {
    customer_string: customerString,
  };
  postRequest(url, param, (res) => {
    if (res === null) {
      callback(false);
    }
    dispatch({
      type: actionType.cncSetAllCustomerInfo,
      data: res.data,
    });
    callback(res.data);
  });
}

export function GetInfo(customerId, dispatch) {
  const url = Urls.GET_TABLE;
  const table = customerId + "_info";
  const param = {
    table: table,
    where: "",
  };
  postRequest(url, param, (res) => {
    updateCustomerInfo(customerId, res.data[0], dispatch);
  });
}

export function updateCustomerInfo(customerId, info, dispatch) {
  let newInfo = info;
  let timezoneInfo = timezoneList.find((x) => x.name == info.timezone_name);
  if (timezoneInfo != null) {
    newInfo.timezone = timezoneInfo.utcOffset / 60;
  }
  dispatch({
    type: actionType.cncUpdateCustomerInfo,
    data: {
      customer_id: customerId,
      customer_info: newInfo,
    },
  });
}

export function UpdateCustomerGroupInfo(customer_id, groupInfo, dispatch) {
  dispatch({
    type: actionType.cncUpdateGroupMachineInfo,
    data: {
      customer_id: customer_id,
      groupInfo: groupInfo,
    },
  });
}

export function readOperatorList(callback) {
  const url = Urls.GET_TABLE;
  const table = "user_login_barcode";
  const param = {
    table: table,
    where: "",
  };

  postRequest(url, param, (res) => {
    if (res.status === true) {
      callback(res.data);
    } else {
      callback(null);
    }
  });
}

export function getPartId(customer_id, callback) {
  const url = Urls.GET_PART_ID;
  const param = {
    customer_id: customer_id,
  };

  postRequest(url, param, (res) => {
    callback(res);
  });
}

export function getCustomDashboardTableData(customer_id, days, callback) {
  const url = Urls.GET_CUSTOM_DASHBOARD_TABLE_DATA;
  const param = {
    customer_id: customer_id,
    days: days,
  };

  postRequest(url, param, (res) => {
    callback(res);
  });
}

export function getCustomDashboardStage(
  customer_id,
  machine_id,
  days,
  callback
) {
  const url = Urls.GET_CUSTOM_DASHBOARD_STAGE;
  const param = {
    customer_id: customer_id,
    machine_id: machine_id,
    days: days,
  };
  postRequest(url, param, (res) => {
    callback(res);
  });
}

export function getTackReport(
  machineLogo,
  hstImage,
  hstDay,
  tempImage,
  tempDay,
  tableData,
  tableDay,
  callback
) {
  const url = Urls.GET_TANK_REPORT;
  const param = {
    machineLogo: machineLogo,
    hstImage: hstImage,
    hstDay: hstDay,
    tempImage: tempImage,
    tempDay: tempDay,
    tableData: tableData,
    tableDay: tableDay,
  };

  postRequest(url, param, (res) => {
    callback(res);
  });
}

export function GetMachineListData(
  customer_id,
  timezone,
  startDate,
  endDate,
  machine_id,
  dispatch
) {
  // Read gantt and hst data
  const url = Urls.GET_MACHINE_DATA;
  const param = {
    customer_id: customer_id,
    startDate: startDate,
    endDate: endDate,
    machine_id: machine_id,
    timezone: timezone,
  };

  postRequest(url, param, (res) => {
    if (res === null) return;
    dispatch({
      type: actionType.cncUpdateMachineDetailInfo,
      data: {
        customer_id: customer_id,
        machines: res.data.machines,
      },
    });
  });
}

export function GetOneMachineData(
  customer_id,
  timezone,
  startDate,
  endDate,
  machine_id,
  shiftTime,
  callback
) {
  // Read gantt and hst data
  const url = Urls.GET_MACHINE_DATA;
  const param = {
    customer_id: customer_id,
    startDate: startDate,
    endDate: endDate,
    shiftTime: shiftTime,
    machine_id: machine_id,
    timezone: timezone,
  };
  postRequest(url, param, (res) => {
    if (res === null) return;
    callback(res);
  });
}

export function GetCurrentIotData(device_id, callback) {
  // Read gantt and hst data
  const url = Urls.GET_CURRENT_IOT_DATA;
  const param = {
    device_id: device_id,
  };
  postRequest(url, param, (res) => {
    if (res === null) return;
    callback(res);
  });
}

export function GetUtilizationData(
  customer_id,
  timezone,
  startDate,
  endDate,
  machine_id,
  dispatch
) {
  // Read gantt and hst data
  const url = Urls.GET_UTILIZATION_DATA;
  const param = {
    customer_id: customer_id,
    startDate: startDate,
    endDate: endDate,
    machine_id: machine_id,
    timezone: timezone,
  };

  postRequest(url, param, (res) => {
    if (res === null) return;
    dispatch({
      type: actionType.utilizationData,
      data: {
        customer_id: customer_id,
        machines: res.data.machines,
      },
    });
  });
}

export function GetUtilizationDetailData(
  customer_id,
  timezone,
  startDate,
  endDate,
  machine_id,
  need_downtimes,
  callback
) {
  // Read gantt and hst data
  const url = Urls.GET_UTILIZATION_DETAIL_DATA;
  const param = {
    customer_id: customer_id,
    startDate,
    endDate: endDate,
    machine_id: machine_id,
    timezone: timezone,
    need_downtimes: need_downtimes,
  };
  postRequest(url, param, (res) => {
    if (res === null) return;
    callback(res);
  });
}

export function GetReportPeriodData(
  customer_id,
  machine_id,
  timezone,
  startDate,
  endDate,
  callback
) {
  const url = Urls.GET_REPORT_PERIOD_DATA;
  const param = {
    customer_id: customer_id,
    machine_id: machine_id,
    timezone: timezone,
    startDate: startDate,
    endDate: endDate,
  };
  postRequest(url, param, (res) => {
    callback(res);
  });
}

export function GetReportPeriodDataByOperator(
  customer_id,
  operator,
  timezone,
  startDate,
  endDate,
  callback
) {
  const url = Urls.GET_REPORT_PERIOD_DATA_BY_OPERATOR;
  const param = {
    customer_id: customer_id,
    operator: operator,
    timezone: timezone,
    startDate: startDate,
    endDate: endDate,
  };

  postRequest(url, param, (res) => {
    callback(res);
  });
}

export function GetHSTData(
  customer_id,
  machine_id,
  timezone,
  startDate,
  endDate,
  callback
) {
  const url = Urls.GET_REPORT_PERIOD_DATA;
  const param = {
    customer_id: customer_id,
    machine_id: machine_id,
    timezone: timezone,
    startDate: startDate,
    endDate: endDate,
  };
  postRequest(url, param, (res) => {
    callback(res);
  });
}
export function GetHstFullData(
  customer_id,
  machine_id,
  operator,
  timezone,
  startDate,
  endDate,
  callback
) {
  const url = Urls.GET_HST_FULL_DATA;
  const param = {
    customer_id: customer_id,
    machine_id: machine_id,
    operator: operator,
    timezone: timezone,
    startDate: startDate,
    endDate: endDate,
  };
  postRequest(url, param, (res) => {
    callback(res);
  });
}

export function GetJobEntryList(customerId, callback) {
  const url = Urls.GET_TABLE;
  const table = customerId + "_jobdata";
  const param = {
    table: table,
    where: "",
  };
  postRequest(url, param, (res) => {
    if (res.status === true) {
      callback(res.data);
    } else {
      callback(null);
    }
  });
}

export function GetOperatorList(customerId, callback) {
  const url = Urls.GET_OPERATOR_LIST;
  const param = {
    customer_id: customerId,
  };
  postRequest(url, param, (res) => {
    if (res != null && res.status === true) {
      callback(res.data);
    } else {
      callback(null);
    }
  });
}

export function GetMaintenanceEntry(customerId, callback) {
  const url = Urls.GET_TABLE;
  const table = customerId + "_maintenance";
  const param = {
    table: table,
    where: "",
  };

  postRequest(url, param, (res) => {
    if (res.status === true) {
      callback(res.data);
    } else {
      callback(null);
    }
  });
}
export function GetIncycleTimes(param, callback) {
  const url = Urls.GET_INCYCLE_TIMES;

  postRequest(url, param, (res) => {
    if (res.status === true) {
      callback(res.data);
    } else {
      callback(null);
    }
  });
}

export function GetUserList(customerId, callback) {
  const url = Urls.GET_TABLE;
  const table = customerId + "_user";
  const param = {
    table: table,
    where: "",
  };

  postRequest(url, param, (res) => {
    if (res.status === true) {
      callback(res.data);
    } else {
      callback(null);
    }
  });
}

export function GetMachineGroup(customerId, callback) {
  const url = Urls.GET_MACHINE_GROUP;
  const param = {
    customer_id: customerId,
  };
  postRequest(url, param, (res) => {
    if (res != null && res.status === true) {
      callback(res.data);
    } else {
      callback(null);
    }
  });
}

export function ManageMachineGroup(selCustomerId, info, callback) {
  const url = Urls.MANAGE_MACHINE_GROUP;
  const param = {
    ...info,
    customer_id: selCustomerId,
  };
  postRequest(url, param, (res) => {
    if (res != null && res.status === true) {
      callback(res.data);
    } else {
      callback(null);
    }
  });
}

export function GetMachineList(customerId, callback) {
  const url = Urls.GET_MACHINE_LIST;
  const param = {
    customer_id: customerId,
  };

  postRequest(url, param, (res) => {
    if (res.status === true) {
      callback(res.data);
    } else {
      callback(null);
    }
  });
}

export function AddEntry(param, callback) {
  const url = Urls.ADD_ENTRY;
  postRequest(url, param, (res) => {
    callback(res);
  });
}

export function AddMaintenanceEntry(param, callback) {
  const url = Urls.ADD_MAINTENANCE_ENTRY;

  postRequest(url, param, (res) => {
    callback(res);
  });
}

export function AddUser(param, callback) {
  const url = Urls.ADD_USER;
  postRequest(url, param, (res) => {
    callback(res);
  });
}

export function UpdateUser(param, callback) {
  const url = Urls.UPDATE_USER;
  postRequest(url, param, (res) => {
    callback(res);
  });
}

export function DeleteUser(param, callback) {
  const url = Urls.DELETE_USER;
  postRequest(url, param, (res) => {
    callback(res);
  });
}

export function AddMachine(param, callback) {
  const url = Urls.ADD_MACHINE;
  postRequest(url, param, (res) => {
    callback(res);
  });
}

export function GetStatus(customerId, dispatch) {
  const url = Urls.GET_TABLE;
  const table = customerId + "_status";
  const param = {
    table: table,
    where: "",
  };

  postRequest(url, param, (res) => {
    if (res != null) {
      updateMachineListInfo(customerId, res.data, dispatch);
    } else {
      updateMachineListInfo(customerId, [], dispatch);
    }
  });
}

export function updateMachineListInfo(customerId, machineListInfo, dispatch) {
  let mList = machineListInfo;
  mList.forEach(function (entry) {
    entry.key = entry.id;
  });
  mList.sort((a, b) => a.order - b.order);
  dispatch({
    type: actionType.cncUpdateMachineListInfo,
    data: {
      customer_id: customerId,
      machine_info: mList,
    },
  });
}

export function GetHst(customerId, customerDate, callback) {
  const url = Urls.GET_TABLE;
  const table = customerId + "_hst";
  const where = "(`date`= '" + customerDate + "')";
  const param = {
    table: table,
    where: where,
  };
  postRequest(url, param, (res) => {
    callback(res);
  });
}

export function GetGantt(customerId, startDate, endDate, callback) {
  const url = Urls.GET_GANTT;
  const table = customerId + "_ganttdata";
  const param = {
    table: table,
    startDate: startDate,
    endDate: endDate,
  };
  postRequest(url, param, (res) => {
    callback(res);
  });
}

export function GetCustomerCurrentTime(customerTimezone) {
  var currentTime = Math.floor(Date.now() / 1000);
  var offset = new Date().getTimezoneOffset() * 60;
  currentTime += offset;
  currentTime += parseInt(customerTimezone) * 3600;
  return new Date(currentTime * 1000);
}

export function pad(num) {
  const size = 2;
  var s = num + "";
  while (s.length < size) s = "0" + s;
  return s;
}

export function ConvertTimespanToDateBasedOnTimezone(timespan, timezone) {
  var offset = new Date().getTimezoneOffset() * 60;
  var resTime = parseInt(timespan);
  resTime += offset;
  // resTime += offset;
  resTime += parseInt(timezone) * 3600;

  return new Date(resTime * 1000);
}

export function GetTimeWithStyle(datetime) {
  var time = moment(datetime);

  return time.format("hh:mm:ss a");
}

function formattedNumber(myNumber) {
  return ("0" + myNumber).slice(-2);
}

export function getBetweenTime(from, to) {
  return humanitizeDuration(parseInt(to) - parseInt(from));
}

export function humanitizeDuration(seconds, secAvailable = true) {
  var duration = moment.duration(seconds, "seconds");
  try {
    var hour = duration.hours() + duration.days() * 24;
    var minutes = duration.minutes();
    var seconds1 = duration.seconds();

    if (secAvailable === false)
      duration =
        formattedNumber(hour) + "hours " + formattedNumber(minutes) + "minutes";
    else
      duration =
        formattedNumber(hour) +
        ":" +
        formattedNumber(minutes) +
        ":" +
        formattedNumber(seconds1);
    return duration;
  } catch (_) {
    return "";
  }
}

export function getDurationInHour(seconds) {
  var duration = moment.duration(seconds, "seconds");
  try {
    var hour = duration.asHours().toFixed(2);
    return hour;
  } catch (_) {
    return "";
  }
}

function FilterOneMachineGanttData(_ganttInfo) {
  let tmpList = [];
  // remove overrided times
  for (let i = 0; i < _ganttInfo.length - 1; i++) {
    if (_ganttInfo[i].end > _ganttInfo[i + 1].start) {
      _ganttInfo[i].end = _ganttInfo[i + 1].start;
    }
  }

  // connect continuous data
  for (let i = 0; i < _ganttInfo.length; i++) {
    var connected = false;
    if (
      i < _ganttInfo.length - 1 &&
      _ganttInfo[i].status.toUpperCase() ===
        _ganttInfo[i + 1].status.toUpperCase() &&
      _ganttInfo[i].end === _ganttInfo[i + 1].start
    ) {
      _ganttInfo[i + 1].start = _ganttInfo[i].start;
      connected = true;
    }

    if (connected === false) {
      tmpList.push(_ganttInfo[i]);
    }
  }
  return tmpList;
}

export function getUsersReport(userData, callback) {
  const url = Urls.GET_USERS_REPORT;
  const param = {
    userData: userData,
  };

  postRequest(url, param, (res) => {
    callback(res);
  });
}

//Shift
export function getShiftList(param, callback) {
  const url = Urls.GET_SHIFT_LIST;
  postRequest(url, param, (res) => {
    if (res === null) {
      callback(null);
    } else {
      if (res.status === true) {
        callback(res.data);
      } else {
        callback(null);
      }
    }
  });
}
//Shift

//Arduino Firmware
export function updateFirmware(mms_version, iot_version, callback) {
  const url = Urls.UPDATE_FIRMWARE;
  postRequest(
    url,
    {
      mms_version: mms_version,
      iot_version: iot_version,
    },
    (res) => {
      if (res === null) {
        callback(false);
      } else {
        callback(true);
      }
    }
  );
}

export function getFirmwares(callback) {
  const url = Urls.GET_FIRMWARES;
  postRequest(url, {}, (res) => {
    callback(res);
  });
}
//Arduino Firmware
