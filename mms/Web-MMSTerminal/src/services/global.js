// User Data
let userData = {
  id: "",
  username: "",
  password: "",
  username_full: "UnAttended",
  user_picture: "",
  security_level: "",
  customer_id: "",
};
export function setUserData(value) {
  if (value === undefined || Object.keys(value).length === 0) return;

  userData = value;
}
export { userData };

// Factory Data
let factoryData = {
  accountId: "",
  customer_details: {},
};
export function setFactoryData(value) {
  if (value === undefined || Object.keys(value).length === 0) return;

  factoryData.accountId =
    value.accountId === undefined || value.accountId === null
      ? ""
      : value.accountId;

  factoryData.customer_details =
    value.customer_details === undefined || value.customer_details === null
      ? {}
      : value.customer_details;
}
export { factoryData };

// App Settings
let appData = {
  pages: [0],
  width: 1024,
  height: 768,
  machineName: "",
  shiftGoodParts: 0,
  shiftBadParts: 0,
  jobId: "",

  inCycleSignalFrom: 0,
  process: 1,
  temperatureDataSource: 0,

  downtime_reason1: "Clear Chips",
  downtime_reason2: "Wait Materials",
  downtime_reason3: "Wait Tooling",
  downtime_reason4: "Break",
  downtime_reason5: "No Operator",
  downtime_reason6: "P.M.",
  downtime_reason7: "Unplanned Repair",
  downtime_reason8: "Other",

  auxData1: "Aux1Data",
  auxData2: "Aux2Data",
  auxData3: "Aux3Data",

  cslock_cycle: "1",
  cslock_reverse: "0",
  cslock_guest: "0",
  cslock_alert: "1",

  time_stop: "00:01:30",
  time_production: "08:00:00",

  shift1_ppt: "",
  shift2_ppt: "",
  shift3_ppt: "",

  cycle_send_alert: "0",
  cycle_email1: "",
  cycle_email2: "",
  cycle_email3: "",

  automatic_part: "0",
  automatic_min_time: "10",
  automatic_part_per_cycle: "1",

  gantt_chart_display: "0",

  calc_chart_title: "",
  calc_chart_formula: "72",
  calc_chart_option: "0",
  calc_chart_unit: "",
  calc_chart_disp_mode: "0",
  calc_chart_max_value: "100",

  collapsed: false,
  currentRoute: "/",
};

export function setAppData(value) {
  if (value === undefined || Object.keys(value).length === 0) return;

  appData.pages =
    value.pages === undefined || value.pages === null ? [0] : value.pages;

  appData.machineName =
    value.machineName === undefined || value.machineName === null
      ? ""
      : value.machineName;
  appData.shiftGoodParts =
    value.shiftGoodParts === undefined || value.shiftGoodParts === null
      ? 0
      : value.shiftGoodParts;
  appData.shiftBadParts =
    value.shiftBadParts === undefined || value.shiftBadParts === null
      ? 0
      : value.shiftBadParts;
  appData.jobId =
    value.jobId === undefined || value.jobId === null ? "" : value.jobId;

  // InCycle Signal Setting
  appData.inCycleSignalFrom =
    value.inCycleSignalFrom === undefined || value.inCycleSignalFrom === null
      ? 0
      : value.inCycleSignalFrom;
  // Process Monitor Setting
  appData.process =
    value.process === undefined || value.process === null ? 0 : value.process;

  // Temperature Source Setting
  appData.temperatureDataSource =
    value.temperatureDataSource === undefined ||
    value.temperatureDataSource === null
      ? 0
      : value.temperatureDataSource;

  // Downtime reason settings
  appData.downtime_reason1 =
    value.downtime_reason1 === undefined || value.downtime_reason1 === null
      ? "Clear Chips"
      : value.downtime_reason1;
  appData.downtime_reason2 =
    value.downtime_reason2 === undefined || value.downtime_reason2 === null
      ? "Wait Materials"
      : value.downtime_reason2;
  appData.downtime_reason3 =
    value.downtime_reason3 === undefined || value.downtime_reason3 === null
      ? "Wait Tooling"
      : value.downtime_reason3;
  appData.downtime_reason4 =
    value.downtime_reason4 === undefined || value.downtime_reason4 === null
      ? "Break"
      : value.downtime_reason4;
  appData.downtime_reason5 =
    value.downtime_reason5 === undefined || value.downtime_reason5 === null
      ? "No Operator"
      : value.downtime_reason5;
  appData.downtime_reason6 =
    value.downtime_reason6 === undefined || value.downtime_reason6 === null
      ? "P.M."
      : value.downtime_reason6;
  appData.downtime_reason7 =
    value.downtime_reason7 === undefined || value.downtime_reason7 === null
      ? "Unplanned Repair"
      : value.downtime_reason7;
  appData.downtime_reason8 =
    value.downtime_reason8 === undefined || value.downtime_reason8 === null
      ? "Other"
      : value.downtime_reason8;

  // AuxData Settings
  appData.auxData1 =
    value.auxData1 === undefined || value.auxData1 === null
      ? ""
      : value.auxData1;
  appData.auxData2 =
    value.auxData2 === undefined || value.auxData2 === null
      ? ""
      : value.auxData2;
  appData.auxData3 =
    value.auxData3 === undefined || value.auxData3 === null
      ? ""
      : value.auxData3;

  // CSLock Settings
  appData.cslock_cycle =
    value.cslock_cycle === undefined || value.cslock_cycle === null
      ? "0"
      : value.cslock_cycle;
  appData.cslock_reverse =
    value.cslock_reverse === undefined || value.cslock_reverse === null
      ? "0"
      : value.cslock_reverse;
  appData.cslock_guest =
    value.cslock_guest === undefined || value.cslock_guest === null
      ? "0"
      : value.cslock_guest;
  appData.cslock_alert =
    value.cslock_alert === undefined || value.cslock_alert === null
      ? "0"
      : value.cslock_alert;

  // Time Settings
  appData.time_stop =
    value.time_stop === undefined || value.time_stop === null
      ? "00:01:30"
      : value.time_stop;
  appData.time_production =
    value.time_production === undefined || value.time_production === null
      ? "08:00:00"
      : value.time_production;

  // Shift PPT Time Setting
  appData.shift1_ppt =
    value.shift1_ppt === undefined || value.shift1_ppt === null
      ? ""
      : value.shift1_ppt;
  appData.shift2_ppt =
    value.shift2_ppt === undefined || value.shift2_ppt === null
      ? ""
      : value.shift2_ppt;
  appData.shift3_ppt =
    value.shift3_ppt === undefined || value.shift3_ppt === null
      ? ""
      : value.shift3_ppt;

  // Cycle Stop Alert eSettings
  appData.cycle_send_alert =
    value.cycle_send_alert === undefined || value.cycle_send_alert == null
      ? "0"
      : value.cycle_send_alert;
  appData.cycle_email1 =
    value.cycle_email1 === undefined || value.cycle_email1 == null
      ? ""
      : value.cycle_email1;
  appData.cycle_email2 =
    value.cycle_email2 === undefined || value.cycle_email2 == null
      ? ""
      : value.cycle_email2;
  appData.cycle_email3 =
    value.cycle_email3 === undefined || value.cycle_email3 == null
      ? ""
      : value.cycle_email3;

  // Automatic part counter Settings
  appData.automatic_part =
    value.automatic_part === undefined || value.automatic_part === null
      ? "0"
      : value.automatic_part;
  appData.automatic_min_time =
    value.automatic_min_time === undefined || value.automatic_min_time === null
      ? "10"
      : value.automatic_min_time;
  appData.automatic_part_per_cycle =
    value.automatic_part_per_cycle === undefined ||
    value.automatic_part_per_cycle === null
      ? "1"
      : value.automatic_part_per_cycle;

  // Gantt Chart Setting
  appData.gantt_chart_display =
    value.gantt_chart_display === undefined ||
    value.gantt_chart_display === null
      ? "0"
      : value.gantt_chart_display;
  appData.calc_chart_title =
    value.calc_chart_title === undefined || value.calc_chart_title === null
      ? ""
      : value.calc_chart_title;
  appData.calc_chart_formula =
    value.calc_chart_formula === undefined || value.calc_chart_formula === null
      ? "72"
      : value.calc_chart_formula;
  appData.calc_chart_option =
    value.calc_chart_option === undefined || value.calc_chart_option === null
      ? "0"
      : value.calc_chart_option;
  appData.calc_chart_unit =
    value.calc_chart_unit === undefined || value.calc_chart_unit === null
      ? ""
      : value.calc_chart_unit;
  appData.calc_chart_disp_mode =
    value.calc_chart_disp_mode === undefined ||
    value.calc_chart_disp_mode === null
      ? "0"
      : value.calc_chart_disp_mode;
  appData.calc_chart_max_value =
    value.calc_chart_max_value === undefined ||
    value.calc_chart_max_value === null
      ? "100"
      : value.calc_chart_max_value;
}
export { appData };

let shiftSettingInfo = {
  shift1_time: "",
  shift2_time: "",
  shift3_time: "",
};
export { shiftSettingInfo };

export const validateEmail = (email) => {
  var pattern = new RegExp(
    /^(("[\w-\s]+")|([\w-]+(?:\.[\w-]+)*)|("[\w-\s]+")([\w-]+(?:\.[\w-]+)*))(@((?:[\w-]+\.)*\w[\w-]{0,66})\.([a-z]{2,6}(?:\.[a-z]{2})?)$)|(@\[?((25[0-5]\.|2[0-4][0-9]\.|1[0-9]{2}\.|[0-9]{1,2}\.))((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[0-9]{1,2})\.){2}(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[0-9]{1,2})\]?$)/i
  );
  if (!pattern.test(email)) {
    return false;
  } else {
    return true;
  }
};

export const validatePassword = (password) => {
  if (password === undefined) {
    return false;
  }

  if (password.length < 6) {
    return false;
  }
  return true;
};

export const getLSString = (key) => {
  if (
    localStorage.getItem(key) === null ||
    localStorage.getItem(key) === undefined
  ) {
    return "";
  } else {
    return localStorage.getItem(key);
  }
};

export const getLSNumber = (key) => {
  if (
    localStorage.getItem(key) === null ||
    localStorage.getItem(key) === undefined
  ) {
    return 0;
  } else {
    return localStorage.getItem(key);
  }
};

export const getLSJson = (key) => {
  if (
    localStorage.getItem(key) === null ||
    localStorage.getItem(key) === undefined
  ) {
    return {};
  } else {
    return JSON.parse(localStorage.getItem(key));
  }
};

export const timeToSecond = (time) => {
  var a = time.split(":"); // split it at the colons
  var seconds = +a[0] * 60 * 60 + +a[1] * 60 + +a[2];
  return seconds * 1000;
};

export const secondToTime = (second) => {
  var sec_num = Math.floor(parseInt(second, 10) / 1000); // don't forget the second param
  var hours = Math.floor(sec_num / 3600);
  var minutes = Math.floor((sec_num - hours * 3600) / 60);
  var seconds = sec_num - hours * 3600 - minutes * 60;

  if (hours < 10) {
    hours = "0" + hours;
  }
  if (minutes < 10) {
    minutes = "0" + minutes;
  }
  if (seconds < 10) {
    seconds = "0" + seconds;
  }
  return hours + ":" + minutes + ":" + seconds;
};

export const isValidTime = (time) => {
  // Check if the time matches the pattern HH:MM:SS (2 digits for hours, minutes, and seconds)
  const timePattern = /^\d{2}:\d{2}:\d{2}$/;
  if (!timePattern.test(time)) {
    return false;
  }

  // Split the time into hours, minutes, and seconds
  const [hours, minutes, seconds] = time.split(":");

  // Convert the hours, minutes, and seconds to numbers
  const hoursNum = parseInt(hours, 10);
  const minutesNum = parseInt(minutes, 10);
  const secondsNum = parseInt(seconds, 10);

  // Check if the hours are between 0 and 23
  if (hoursNum < 0 || hoursNum > 23) {
    return false;
  }

  // Check if the minutes are between 0 and 59
  if (minutesNum < 0 || minutesNum > 59) {
    return false;
  }

  // Check if the seconds are between 0 and 59
  if (secondsNum < 0 || secondsNum > 59) {
    return false;
  }

  // If all checks passed, the time is valid
  return true;
};
