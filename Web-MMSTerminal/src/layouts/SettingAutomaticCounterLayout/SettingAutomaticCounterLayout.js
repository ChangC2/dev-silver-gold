import { Col, Row } from "antd";
import RadialInputGroup from "components/RadialInputGroup/RadialInputGroup";
import SliderInput from "components/SliderInput/SliderInput";
import TextInputGroup from "components/TextInputGroup/TextInputGroup";
import { useState } from "react";
import "./SettingAutomaticCounterLayout.css";

const SettingAutomaticCounterLayout = (props) => {
  const { appSetting, setAppSetting } = props;
  // const [appSetting, setAppSetting] = useState({
  //   automatic_part: "0",
  //   automatic_min_time: "10",
  //   automatic_part_per_cycle: "1",
  // });

  const setAppSettings = (field, value) => {
    var newSetting = { ...appSetting };
    newSetting[field] = value;
    setAppSetting({ ...newSetting });
  };

  return (
    <div className="settings-automatic-parts-counter-layout">
      <Row align="middle">
        <Col span={24}>
          <span className="settings-automatic-parts-counter-title">
            {"Automatic Parts Counter"}
          </span>
        </Col>
      </Row>
      <div className="settings-automatic-parts-counter-content">
        <Row>
          <Col span={6}>
            <RadialInputGroup
              initValue={appSetting}
              field="automatic_part"
              updateValue={setAppSettings}
              title={"Automatic Count"}
            />
          </Col>
          <Col span={12}>
            <SliderInput
              initValue={appSetting}
              field="automatic_min_time"
              updateValue={setAppSettings}
              title={`Min Elapsed Cycle Time(${appSetting.automatic_min_time}s)`}
            />
          </Col>
          <Col span={6}>
            <TextInputGroup
              initValue={appSetting}
              field="automatic_part_per_cycle"
              updateValue={setAppSettings}
              title={"Parts per cycle"}
              input_type="number"
            />
          </Col>
        </Row>
      </div>
    </div>
  );
};

export default SettingAutomaticCounterLayout;
