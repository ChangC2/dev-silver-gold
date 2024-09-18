import { Col, Row } from "antd";
import OptionInput from "components/OptionInput/OptionInput";
import { useState } from "react";
import SettingGanttChartCalcLayout from "./SettingGanttChartCalcLayout/SettingGanttChartCalcLayout";
import "./SettingGanttChartLayout.css";

const SettingGanttChartLayout = (props) => {
  const { appSetting, setAppSetting } = props;
  
  const setGanttChartSettings = (field, value) => {
    var newSetting = { ...appSetting };
    newSetting[field] = value;
    setAppSetting({ ...newSetting });
  };

  return (
    <div className="settings-gantt-chart-layout">
      <Row align="middle">
        <Col span={24}>
          <span className="settings-gantt-chart-title">
            {"Gantt Chart Settings"}
          </span>
        </Col>
      </Row>
      <div className="settings-gantt-chart-content">
        <Row>
          <Col span={12}>
            <OptionInput
              span={24}
              initValue={appSetting}
              field="gantt_chart_display"
              updateValue={setGanttChartSettings}
              title={[
                "Show gantt data from 00:00 to 23:00",
                "Show gantt data from 00:00 to current time",
                "Show Daily Goal Chart",
              ]}
            />
          </Col>
          {appSetting["gantt_chart_display"] === "2" && (
            <Col span={12}>
              <SettingGanttChartCalcLayout
                appSetting={appSetting}
                setAppSetting={setAppSetting}
              />
            </Col>
          )}
        </Row>
      </div>
    </div>
  );
};

export default SettingGanttChartLayout;
