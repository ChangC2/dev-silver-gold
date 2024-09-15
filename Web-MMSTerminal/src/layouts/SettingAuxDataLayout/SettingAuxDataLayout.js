import { Col, Row } from "antd";
import TextInputGroup from "components/TextInputGroup/TextInputGroup";
import { useState } from "react";
import "./SettingAuxDataLayout.css";

const SettingAuxDataLayout = (props) => {
  const { appSetting, setAppSetting } = props;

  const auxDataTitles = [
    { key: "auxData1", title: "Aux1Data" },
    { key: "auxData2", title: "Aux2Data" },
    { key: "auxData3", title: "Aux3Data" },
  ];

  const setAuxSettings = (field, value) => {
    var newSetting = { ...appSetting };
    newSetting[field] = value;
    setAppSetting(newSetting);
  };

  const auxDataTextInputs = auxDataTitles.map((dTitle) => {
    return (
      <Col span={8} key={dTitle.key}>
        <TextInputGroup
          initValue={appSetting}
          title={dTitle.title}
          field={dTitle.key}
          updateValue={setAuxSettings}
        />
      </Col>
    );
  });

  return (
    <div className="settings-auxData-layout">
      <Row align="middle">
        <Col span={24}>
          <span className="settings-auxData-title">AuxData Title Settings</span>
        </Col>
      </Row>
      <Row style={{ paddingLeft: "20px", paddingRight: "20px" }}>
        {auxDataTextInputs}
      </Row>
    </div>
  );
};

export default SettingAuxDataLayout;
