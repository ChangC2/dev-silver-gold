export const PAGETYPE = {
  submenu: "submenu",
  cnc: "CNC",
  ud: "Utilization Dashboard",
  cd: "Customer Dashboards",
  cw: "Customer Widgets",
  tms: "TMS",
  tms_alarm: "TMS Alarm",
  iiot: "IIot",
  job_entry: "JOB_ENTRY",
  report: "REPORT",
  plant_setting: "PLANT_SETTING",
  cnc_maintenance: "CNC_MAINTENANCE",
  hennig_alarm: "HENNIG_ALARM",
  hennig_iot: "HENNIG_IOT",
};

export const VIEWMODE = {
  listView: 0,
  tileView: 1,
};

export const MENU_DIVIDER = "___";

export const menuReport = "menu_report";
export const sizeMobile = 768;
export const sizePad = 1100;

// Sensor
export const SENSOR_TYPE = {
  other: 0,
  temperature: 1,
  current: 2,
  vibration: 3,
  moisture: 4,
  weight: 5,
  tempAndHum: 6,
};

export const SENSOR_TITLE_BY_ID = {
  other: "Other",
  temperature: "Temperature Probe",
  current: "Current Sensor",
  vibration: "Vibration Sensor",
  moisture: "Moisture Sensor",
  weight: "Weight Sensor",
  tempAndHum: "Temperature and Humidity",
};

export const SENSOR_UNIT_BY_ID = {
  other: [""],
  temperature: ["°C", "°F", ""],
  current: ["mA", "A", ""],
  vibration: ["Hz", ""],
  moisture: ["%", ""],
  weight: ["kg", "lbs"],
  tempAndHum: ["F", "C"],
};

export const SENSOR_ALERT_FREQUENCY = {
  noAlert: 0,
  min5: 5,
  min10: 10,
  min20: 20,
  hr1: 60,
};

export const SENSOR_ALERT_FREQUENCY_TITLE = {
  noAlert: "no alert",
  min5: "5 min",
  min10: "10 min",
  min20: "20 min",
  hr1: "1 hour",
};
// Sensor

//Hennig
export const HENNIG_TYPE = {
  other: 0,
  temperature: 1,
  current: 2,
  vibration: 3,
  moisture: 4,
  weight: 5,
  tempAndHum: 6,
};
export const HENNIG_TITLE_BY_ID = {
  other: "Other",
  temperature: "Temperature Probe",
  current: "Current Sensor",
  vibration: "Vibration Sensor",
  moisture: "Moisture Sensor",
  weight: "Weight Sensor",
  tempAndHum: "Temperature and Humidity",
};
export const HENNIG_UNIT_BY_ID = {
  other: [""],
  temperature: ["°C", "°F", ""],
  current: ["mA", "A", ""],
  vibration: ["Hz", ""],
  moisture: ["%", ""],
  weight: ["kg", "lbs"],
  tempAndHum: ["F", "C"],
};
export const HENNIG_ALERT_FREQUENCY = {
  noAlert: 0,
  min5: 5,
  min10: 10,
  min20: 20,
  hr1: 60,
};
export const HENNIG_ALERT_FREQUENCY_TITLE = {
  noAlert: "no alert",
  min5: "5 min",
  min10: "10 min",
  min20: "20 min",
  hr1: "1 hour",
};
//Hennig

export const laborTimeFormats = ["24hr", "12hr"];
export const laborCategories = [
  "TOS",
  "MMS Site",
  "MMS APK",
  "Fanuc Bridge",
  "Okuma",
];

export const cwTypes = [
  "Trend Chart",
  "Bar Chart",
  "Gauge Chart",
  "Pie Chart",
  "Text Info Box",
  "Machine Info Box",
];

export const cwSizes = ["FullSize", "Half Size"];

export const cwDataPoints = [
  "Good Parts",
  "Bad Parts",
  "OEE",
  "Availability",
  "Quality",
  "Performance",
  "Total In Cycle Time",
];

// export const cwFilters

//Trello Constants
export const trelloColors = [
  "red",
  "red_dark",
  "green",
  "green_dark",
  "blue",
  "blue_dark",
  "yellow",
  "purple",
];

export const STATUS_COLORS = [
  "#ff9600",
  "#ffec00",
  "#549afc",
  "#c000db",
  "#9898db",
  "#B0E0E6",
  "#6aa786",
  "#c0a0c0",
  "#808080",
  "#ff0000",
  "#46c392",
  "#000000",
];
