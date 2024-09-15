import { Col, Row } from "antd";
import RadialInputGroup from "components/RadialInputGroup/RadialInputGroup";
import { useState } from "react";
import "./SettingCSLockLayout.css";

const SettingCSLockLayout = (props) => {
  const { appSetting, setAppSetting } = props;

  const setCSLockSettings = (field, value) => {
    var newSetting = { ...appSetting };
    newSetting[field] = value;
    setAppSetting({ ...newSetting });
  };

  return (
    <div className="settings-cslock-layout">
      <Row align="middle">
        <Col span={24}>
          <span className="settings-cslock-title">CSLock Settings</span>
        </Col>
      </Row>
      <div className="settings-cslock-content">
        <Row>
          <Col span={12}>
            <RadialInputGroup
              initValue={appSetting}
              field="cslock_cycle"
              updateValue={setCSLockSettings}
              title={"Use Cycle Start Interlock"}
            />
          </Col>
          <Col span={12}>
            <RadialInputGroup
              initValue={appSetting}
              field="cslock_reverse"
              updateValue={setCSLockSettings}
              title={"CSLock Normally Open"}
            />
          </Col>
          <Col span={12}>
            <RadialInputGroup
              initValue={appSetting}
              field="cslock_guest"
              updateValue={setCSLockSettings}
              title={"Quest Lock"}
            />
          </Col>
          <Col span={12}>
            <RadialInputGroup
              initValue={appSetting}
              field="cslock_alert"
              updateValue={setCSLockSettings}
              title={"Enable Alert Sount"}
            />
          </Col>
        </Row>
      </div>
    </div>
  );
};

export default SettingCSLockLayout;
