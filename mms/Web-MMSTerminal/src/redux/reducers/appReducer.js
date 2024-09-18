import { IS_SPINNING, SET_APP_DATA_STORE } from "redux/actions/appActions";
import { LS_ITEMS } from "services/CONSTANTS";

const initialState = {
  isSpinning: false,
  appDataStore: {
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

    auxData1: "Aux1",
    auxData2: "Aux2",
    auxData3: "Aux3",

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
  },
};

export const appReducer = (state = initialState, action) => {
  switch (action.type) {
    case IS_SPINNING:
      return {
        ...state,
        ...{ isSpinning: action.payload },
      };
    case SET_APP_DATA_STORE:
      localStorage.setItem(LS_ITEMS.appData, JSON.stringify(action.payload));
      return {
        ...state,
        ...{ appDataStore: action.payload },
      };
    default:
      return state;
  }
};
