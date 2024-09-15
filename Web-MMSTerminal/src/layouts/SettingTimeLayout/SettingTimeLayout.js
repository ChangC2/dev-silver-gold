import { Col, Row } from "antd";
import TextInputGroup from "components/TextInputGroup/TextInputGroup";
import { useState } from "react";
import "./SettingTimeLayout.css";

const SettingTimeLayout = (props) => {
  const { appSetting, setAppSetting } = props;
  // const [ appSetting, setAppSetting ] = useState({
  //   time_stop: "00:01:30",
  //   time_production: "08:00:00",
  // });

  if (!appSetting.hasOwnProperty("time_stop")) {
    setAppSetting({ ...appSetting, time_stop: "00:01:30" });
  }

  if (!appSetting.hasOwnProperty("time_production")) {
    setAppSetting({ ...appSetting, time_production: "08:00:00" });
  }

  const commafy = (num) => {
    var str = num.toString().split(".");
    if (str[0].length >= 5) {
      str[0] = str[0].replace(/(\d)(?=(\d{3})+$)/g, "$1,");
    }
    if (str[1] && str[1].length >= 5) {
      str[1] = str[1].replace(/(\d{3})/g, "$1 ");
    }
    return str.join(".");
  };

  const getSecondFromTime = (time) => {
    var timeList = time.split(":");
    var hr = isNaN(parseInt(timeList[0])) ? 0 : parseInt(timeList[0]);
    var min = isNaN(parseInt(timeList[1])) ? 0 : parseInt(timeList[1]);
    var sec = isNaN(parseInt(timeList[2])) ? 0 : parseInt(timeList[2]);
    return hr * 3600 + min * 60 + sec;
  };

  const setTimeSettings = (field, value) => {
    var newSetting = { ...appSetting };
    newSetting[field] = value;
    setAppSetting({ ...newSetting });
  };

  return (
    <div className="settings-time-layout">
      <Row align="middle">
        <Col span={24}>
          <span className="settings-time-title">Time Settings</span>
        </Col>
      </Row>
      <div className="settings-time-content">
        <Row>
          <Col span={12}>
            <TextInputGroup
              initValue={appSetting}
              field="time_stop"
              updateValue={setTimeSettings}
              title={`Stop Time Limit (${commafy(
                getSecondFromTime(appSetting.time_stop)
              )} s)`}
              isCenter={true}
              input_type="time-setting"
            />
          </Col>
          <Col span={12}>
            <TextInputGroup
              initValue={appSetting}
              field="time_production"
              updateValue={setTimeSettings}
              title={"Planned Production Time"}
              isCenter={true}
              input_type="time-setting"
            />
          </Col>
        </Row>
      </div>
    </div>
  );
};

export default SettingTimeLayout;
