import { Col, Row } from "antd";
import TextInputGroup from "components/TextInputGroup/TextInputGroup";
import { useState } from "react";
import "./SettingDowntimeLayout.css";

const SettingDowntimeLayout = (props) => {
  const { appSetting, setAppSetting } = props;

  const downtimeSettingsTitles = [
    { key: "downtime_reason1", title: "Downtune Reason1" },
    { key: "downtime_reason2", title: "Downtune Reason2" },
    { key: "downtime_reason3", title: "Downtune Reason3" },
    { key: "downtime_reason4", title: "Downtune Reason4" },
    { key: "downtime_reason5", title: "Downtune Reason5" },
    { key: "downtime_reason6", title: "Downtune Reason6" },
    { key: "downtime_reason7", title: "Downtune Reason7" },
    { key: "downtime_reason8", title: "Downtune Reason8" },
  ];

  const setDowntimeTitle = (field, value) => {
    var newSetting = { ...appSetting, [field]: value };
    //newSetting[field] = value;
    //setAppSetting({ ...newSetting });
    setAppSetting(newSetting);
  };

  const downtimeSettingsTextInputs = downtimeSettingsTitles.map((dTitle) => {
    return (
      <Col className="app-setting-downtime-item" span={6} key={dTitle.key}>
        <TextInputGroup
          initValue={appSetting}
          title={dTitle.title}
          field={dTitle.key}
          updateValue={setDowntimeTitle}
        />
      </Col>
    );
  });

  return (
    <div className="settings-downtime-layout">
      <Row align="middle">
        <Col span={24}>
          <span className="settings-downtime-title">Downtime Settings</span>
        </Col>
      </Row>
      <Row style={{ paddingLeft: "20px", paddingRight: "20px" }}>
        {downtimeSettingsTextInputs}
      </Row>
    </div>
  );
};

export default SettingDowntimeLayout;
