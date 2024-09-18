import moment from "moment";
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
  if (value === null || value === undefined || Object.keys(value).length === 0)
    return;
  userData = value;
}
export { userData };

// App Settings
let appData = {
  width: 1024,
  height: 768,

  customer_id: "",
  customer_details: {},

  machine_id: "",
  machine_picture_url: "",
  machine_status: [
    "Clear Chips",
    "Wait Materials",
    "Wait Tooling",
    "Break",
    "No Operator",
    "P.M",
    "Unplanned Repair",
    "Other",
    "Idle-Uncategorized",
    "In Cycle",
    "Offline",
  ],
  current_ganttdata: {},
  last_ganttdata: {},

  time_stop: "00:01:30",
};

export function setAppData(value) {
  appData.customer_id = isNull(value.customer_id) ? "" : value.customer_id;
  appData.customer_details = isNull(value.customer_details)
    ? {}
    : value.customer_details;
  appData.machine_id = isNull(value.machine_id) ? "" : value.machine_id;
  appData.machine_picture_url = isNull(value.machine_picture_url)
    ? ""
    : value.machine_picture_url;
  appData.machine_status = isNull(value.machine_status)
    ? [
        "Clear Chips",
        "Wait Materials",
        "Wait Tooling",
        "Break",
        "No Operator",
        "P.M",
        "Unplanned Repair",
        "Other",
        "Idle-Uncategorized",
        "In Cycle",
        "Offline",
      ]
    : [...value.machine_status];
  appData.current_ganttdata = isNull(value.current_ganttdata)
    ? {}
    : { ...value.current_ganttdata };

  let currentTimestamp = Math.floor(new Date().getTime() / 1000);
  appData.last_ganttdata = isNull(value.last_ganttdata)
    ? { status: "Idle Uncategorized", color: "#ff0000", start: currentTimestamp }
    : { ...value.last_ganttdata };

  appData.time_stop = isNull(value.time_stop) ? "00:01:30" : value.time_stop;
}
export { appData };

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
  var seconds = 0;
  if (a.length == 2) {
    seconds = +a[1] * 60 + +a[2];
  } else if (a.length == 3) {
    seconds = +a[0] * 60 * 60 + +a[1] * 60 + +a[2];
  } else {
    seconds = +a[0];
  }
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

export function onlyUnique(value, index, self) {
  return self.indexOf(value) === index;
}

export function ConvertTimestampToDateBasedOnTimezone(timestamp, timezone) {
  var offset = new Date().getTimezoneOffset() * 60;
  var resTime = parseInt(timestamp);
  resTime += offset;
  // resTime += offset;
  resTime += parseInt(timezone) * 3600;

  return new Date(resTime * 1000);
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

function formattedNumber(myNumber) {
  return ("0" + myNumber).slice(-2);
}

export function GetTimeWithStyle(datetime) {
  var time = moment(datetime);

  return time.format("hh:mm:ss a");
}

export function isNull(value) {
  return value === undefined || value === null ? true : false;
}

export function isValid(value) {
  return value === undefined || value === null || value === "" || value === NaN
    ? false
    : true;
}

export function ConvertTimespanToDateBasedOnTimezone(timespan, timezone) {
  var offset = new Date().getTimezoneOffset() * 60;
  var resTime = parseInt(timespan);
  resTime += offset;
  // resTime += offset;
  resTime += parseInt(timezone) * 3600;

  return new Date(resTime * 1000);
}
