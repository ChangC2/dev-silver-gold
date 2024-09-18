import { useSelector } from "react-redux";
import "./SettingTempDataSourceLayout.css";
import { PlayCircleFilled } from "@ant-design/icons";
import { Col, Radio, Row, message } from "antd";
import { useState } from "react";
import { Parser } from "html-to-react";
import TextInput from "components/TextInput/TextInput";
import RadialInput from "components/RadialInput/RadialInput";

const SettingTempDataSourceLayout = (props) => {
  const { appSetting, setAppSetting } = props;

  const TempDataSources = [
    { value: 0, title: "Manual Entry" },
    { value: 1, title: "llot Link" },
    { value: 2, title: "PLC" },
  ];

  const tempDataSourceOptions = TempDataSources.map((tdSource) => {
    return (
      <Col
        className="app-setting-temp_data_source-option-item"
        span={8}
        key={TempDataSources.indexOf(tdSource)}
      >
        <Radio value={tdSource.value} style={{ color: "white" }}>
          {tdSource.title}
        </Radio>
      </Col>
    );
  });

  const setTemperatureDataSource = (value) => {
    var newSetting = { ...appSetting, temperatureDataSource: value };
    //newSetting[field] = value;
    //setAppSetting({ ...newSetting });
    setAppSetting(newSetting);
  };

  return (
    <div className="settings-temp_data_source-layout">
      <Row align="middle">
        <Col span={24}>
          <span className="settings-temp_data_source-title">
            Temperature Data Source
          </span>
        </Col>
      </Row>

      <Radio.Group
        // onChange={updateValue}
        value={appSetting.temperatureDataSource}
        className="app-setting-temp_data_source-option"
        onChange={(e) => {
          setTemperatureDataSource(e.target.value);
        }}
      >
        <Row>{tempDataSourceOptions}</Row>
      </Radio.Group>
    </div>
  );
};

export default SettingTempDataSourceLayout;
