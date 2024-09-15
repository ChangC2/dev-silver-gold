import { useSelector } from "react-redux";
import "./SettingShiftTimeLayout.css";
import { Col, Radio, Row, message } from "antd";
import { useState } from "react";
import TextInputGroup from "components/TextInputGroup/TextInputGroup";
import { shiftSettingInfo } from "services/global";

const SettingShiftTimeLayout = (props) => {
  const { appSetting, setAppSetting } = props;
  // const [appSetting, setAppSetting] = useState({
  //   shift1_ppt: "00:00:30",
  //   shift2_ppt: "05:00:00",
  //   shift3_ppt: "05:00:00",
  // });

  const setShiftPPTSettings = (field, value) => {
    var newSetting = { ...appSetting };
    newSetting[field] = value;
    setAppSetting({ ...newSetting });
  };

  return (
    <div className="settings-shift-time-layout">
      <Row align="middle">
        <Col span={24}>
          <span className="settings-shift-time-title">
            Shifts Planned Production Time Settings
          </span>
        </Col>
      </Row>
      <div className="settings-shift-time-content">
        <Row>
          <Col span={8}>
            <TextInputGroup
              initValue={appSetting}
              field="shift1_ppt"
              updateValue={setShiftPPTSettings}
              title={
                shiftSettingInfo[0] === "" || shiftSettingInfo[0] === undefined
                  ? "Shift1"
                  : `Shift1 (${shiftSettingInfo[0]})`
              }
              isCenter={true}
              input_type="time-setting"
            />
          </Col>
          <Col span={8}>
            <TextInputGroup
              initValue={appSetting}
              field="shift2_ppt"
              updateValue={setShiftPPTSettings}
              title={
                shiftSettingInfo[1] === "" || shiftSettingInfo[1] === undefined
                  ? "Shift2"
                  : `Shift2 (${shiftSettingInfo[1]})`
              }
              isCenter={true}
              input_type="time-setting"
            />
          </Col>
          <Col span={8}>
            <TextInputGroup
              initValue={appSetting}
              field="shift3_ppt"
              updateValue={setShiftPPTSettings}
              title={
                shiftSettingInfo[2] === "" || shiftSettingInfo[2] === undefined
                  ? "Shift3"
                  : `Shift3 (${shiftSettingInfo[2]})`
              }
              isCenter={true}
              input_type="time-setting"
            />
          </Col>
        </Row>
      </div>
    </div>
  );
};

export default SettingShiftTimeLayout;
