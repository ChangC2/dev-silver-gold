import { Col, Radio, Row, message } from "antd";
import { useState } from "react";
import "./SettingsInCycleSignalFromLayout.css";

const SettingsInCycleSignalFromLayout = (props) => {
  const { appSetting, setAppSetting } = props;

  const setInCycleSignalFrom = (value) => {
    var newSetting = { ...appSetting, inCycleSignalFrom: value };
    setAppSetting(newSetting);
  };

  return (
    <div className="settings-in-cyle-layout">
      <Row align="middle">
        <Col span={24}>
          <span className="settings-in-cyle-title">In Cycle Signal From</span>
        </Col>
      </Row>

      <Radio.Group
        // onChange={updateValue}
        value={appSetting.inCycleSignalFrom}
        className="app-setting-in-cyle-option"
        onChange={(e) => {
          console.log("Selected Switch:", e.target.value);
          setInCycleSignalFrom(e.target.value);
        }}
      >
        <Row>
          <Col span={12}>
            <Radio value={0} style={{ color: "white" }}>
              PLC
            </Radio>
          </Col>
          <Col span={12}>
            <Radio value={1} style={{ color: "white" }}>
              Process Monitor
            </Radio>
          </Col>
        </Row>
      </Radio.Group>
    </div>
  );
};

export default SettingsInCycleSignalFromLayout;
