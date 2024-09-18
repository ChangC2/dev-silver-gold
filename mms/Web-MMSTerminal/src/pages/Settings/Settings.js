// @flow strict

import { Col, Row, message } from "antd";
import { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import "./Settings.css";

import SettingAutomaticCounterLayout from "layouts/SettingAutomaticCounterLayout/SettingAutomaticCounterLayout";
import SettingAuxDataLayout from "layouts/SettingAuxDataLayout/SettingAuxDataLayout";
import SettingCSLockLayout from "layouts/SettingCSLockLayout/SettingCSLockLayout";
import SettingCycleStopAlertLayout from "layouts/SettingCycleStopAlertLayout/SettingCycleStopAlertLayout";
import SettingDowntimeLayout from "layouts/SettingDowntimeLayout/SettingDowntimeLayout";
import SettingGanttChartLayout from "layouts/SettingGanttChartLayout/SettingGanttChartLayout";
import SettingMachineInfoLayout from "layouts/SettingMachineInfoLayout/SettingMachineInfoLayout";
import SettingProcessMonitorLayout from "layouts/SettingProcessMonitorLayout/SettingProcessMonitorLayout";
import SettingShiftTimeLayout from "layouts/SettingShiftTimeLayout/SettingShiftTimeLayout";
import SettingTempDataSourceLayout from "layouts/SettingTempDataSourceLayout/SettingTempDataSourceLayout";
import SettingTimeLayout from "layouts/SettingTimeLayout/SettingTimeLayout";
import SettingsAppInfoLayout from "layouts/SettingsAppInfoLayout/SettingsAppInfoLayout";
import SettingsInCycleSignalFromLayout from "layouts/SettingsInCycleSignalFromLayout/SettingsInCycleSignalFromLayout";
import SettingsServerInfoLayout from "layouts/SettingsServerInfoLayout/SettingsServerInfoLayout";
import SettingsTopLayout from "layouts/SettingsTopLayout/SettingsTopLayout";
import { setAppDataStore } from "redux/actions/appActions";
import { LS_ITEMS } from "services/CONSTANTS";
import { appData, isValidTime, setAppData } from "services/global";
import settingsIcon from "../../assets/icons/ic_settings.png";

function Settings(props) {
  const dispatch = useDispatch();
  const { appDataStore } = useSelector((x) => x.appDataStore);
  const { pages } = appDataStore;

  const [appSetting, setAppSetting] = useState({ ...appDataStore });

  useEffect(() => {
    setAppSetting({ ...appDataStore });
    console.log("appData=>", appData);
    console.log("appDataStore=>", appDataStore);
  }, [
    appDataStore.machineName,
    appDataStore.downtime_reason1,
    appDataStore.downtime_reason2,
    appDataStore.downtime_reason3,
    appDataStore.downtime_reason4,
    appDataStore.downtime_reason5,
    appDataStore.downtime_reason6,
    appDataStore.downtime_reason7,
    appDataStore.downtime_reason8,

    appDataStore.cslock_cycle,
    appDataStore.cslock_reverse,
    appDataStore.cslock_guest,
    appDataStore.cslock_alert,

    appDataStore.time_stop,
    appDataStore.time_production,

    appDataStore.cycle_send_alert,
    appDataStore.cycle_email1,
    appDataStore.cycle_email2,
    appDataStore.cycle_email3,

    appDataStore.automatic_part,
    appDataStore.automatic_min_time,
    appDataStore.automatic_part_per_cycle,

    appDataStore.gantt_chart_display,
    appDataStore.calc_chart_title,
    appDataStore.calc_chart_formula,
    appDataStore.calc_chart_option,
    appDataStore.calc_chart_unit,
    appDataStore.calc_chart_disp_mode,
    appDataStore.calc_chart_max_value,
  ]);

  useEffect(() => {
    //console.log("appData=>", appData);
    //console.log("appSettingFromChildSettings=>", appSetting);
  }, [appSetting]);

  const onClickSave = () => {
    // Check Time Settings Input
    if (
      !isValidTime(appSetting.time_stop) ||
      !isValidTime(appSetting.time_production)
    ) {
      message.error("Please input valid time format.");
      return;
    }

    // Check Shift PPT Settings Input
    if (appSetting.shift1_ppt === "" && !isValidTime(appSetting.shift1_ppt)) {
      message.error("Please input valid time format.");
      return;
    }
    if (appSetting.shift2_ppt === "" && !isValidTime(appSetting.shift2_ppt)) {
      message.error("Please input valid time format.");
      return;
    }
    if (appSetting.shift3_ppt === "" && !isValidTime(appSetting.shift3_ppt)) {
      message.error("Please input valid time format.");
      return;
    }

    // Check Automatic Part Counter Settings Input
    let autoPartPertCycle = parseInt(appSetting.automatic_part_per_cycle);
    if (isNaN(autoPartPertCycle) || autoPartPertCycle <= 0) {
      message.error("Please input valid auto part counter per cycle.");
      return;
    }

    let newPages = [...appData.pages];
    newPages.splice(-1);

    let newSettings = { ...appData, ...appSetting };
    newSettings.pages = newPages;

    console.log("-------------------------------");
    console.log("appData=>", { ...appData });
    console.log("appSetting=>", appSetting);
    console.log("appNewSetting=>", newSettings);

    setAppData(newSettings);
    dispatch(setAppDataStore(appData));
  };

  return (
    <div
      className={
        pages[pages.length - 1] === 2 ? "settings-page" : "display-none"
      }
    >
      <SettingsTopLayout />
      <div className="settings-content">
        <SettingsAppInfoLayout />
        <SettingsServerInfoLayout />
        <SettingsInCycleSignalFromLayout
          appSetting={appSetting}
          setAppSetting={setAppSetting}
        />
        <SettingProcessMonitorLayout
          appSetting={appSetting}
          setAppSetting={setAppSetting}
        />
        <SettingTempDataSourceLayout
          appSetting={appSetting}
          setAppSetting={setAppSetting}
        />
        <SettingMachineInfoLayout
          appSetting={appSetting}
          setAppSetting={setAppSetting}
        />
        <SettingDowntimeLayout
          appSetting={appSetting}
          setAppSetting={setAppSetting}
        />
        <SettingAuxDataLayout
          appSetting={appSetting}
          setAppSetting={setAppSetting}
        />
        <SettingCSLockLayout
          appSetting={appSetting}
          setAppSetting={setAppSetting}
        />
        <SettingTimeLayout
          appSetting={appSetting}
          setAppSetting={setAppSetting}
        />
        <SettingShiftTimeLayout
          appSetting={appSetting}
          setAppSetting={setAppSetting}
        />
        <SettingCycleStopAlertLayout
          appSetting={appSetting}
          setAppSetting={setAppSetting}
        />
        <SettingAutomaticCounterLayout
          appSetting={appSetting}
          setAppSetting={setAppSetting}
        />
        <SettingGanttChartLayout
          appSetting={appSetting}
          setAppSetting={setAppSetting}
        />
      </div>
      <Row
        align={"middle"}
        className="settings-save-button"
        onClick={onClickSave}
      >
        <Col span={4} style={{ textAlign: "left" }}>
          <img
            className="settings-save-button-icon"
            src={settingsIcon}
            alt="settings"
          />
        </Col>
        <Col span={16} style={{ textAlign: "center" }}>
          {"Save Settings"}
        </Col>
        <Col span={4}></Col>
      </Row>
    </div>
  );
}

export default Settings;
