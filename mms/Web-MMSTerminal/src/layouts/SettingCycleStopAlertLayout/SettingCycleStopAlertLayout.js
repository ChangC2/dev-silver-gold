import { Col, Row, message } from "antd";
import RadialInputGroup from "components/RadialInputGroup/RadialInputGroup";
import TextInputGroup from "components/TextInputGroup/TextInputGroup";
import TextWithIcon from "components/TextWithIcon/TextWithIcon";
import CycleAlertInfoModal from "layouts/CycleAlertInfo/CycleAlertInfoModal";
import { useState } from "react";
import cycleStopAlertIcon from "../../assets/icons/ic_machine_name.png";
import "./SettingCycleStopAlertLayout.css";

const SettingCycleStopAlertLayout = (props) => {
  const { appSetting, setAppSetting } = props;
  // const [appSetting, setAppSetting] = useState({
  //   cycle_send_alert: "0",
  //   cycle_email1: "aa@aa.com",
  //   cycle_email2: "",
  //   cycle_email3: "",
  // });

  const [cycleAlertInfoModal, setCycleAlertInfoModal] = useState(false);

  const setCycleStopSettings = (field, value) => {
    var newSetting = { ...appSetting };
    newSetting[field] = value;
    setAppSetting({ ...newSetting });
  };

  const emailValidation = (fieldName, value) => {
    if (value === "") return true;
    if (
      /^[a-zA-Z0-9.!#$%&’*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\.[a-zA-Z0-9-]+)*$/.test(
        value
      )
    ) {
      return true;
    }
    message.error(`Please enter a valid email for ${fieldName}`);
    return false;
  };

  const timeValidation = (fieldName, value) => {
    var timeList = value.split(":");
    if (
      timeList.length != 3 ||
      isNaN(parseInt(timeList[0])) ||
      isNaN(parseInt(timeList[1])) ||
      isNaN(parseInt(timeList[2])) ||
      parseInt(timeList[1]) >= 60 ||
      parseInt(timeList[2]) >= 60
    ) {
      message.error(`Please enter a valid time for ${fieldName}`);
      return false;
    }
    return true;
  };

  const validate_info = (values) => {
    var res = true;

    res = timeValidation("Stop Time Limit", values.time_stop);
    if (res === false) return false;
    res = timeValidation("Planned Production Time", values.time_production);
    if (res === false) return false;

    res = emailValidation("Alert Email1", values.cycle_email1);
    if (res === false) return false;
    res = emailValidation("Alert Email2", values.cycle_email2);
    if (res === false) return false;
    res = emailValidation("Alert Email3", values.cycle_email3);
    if (res === false) return false;

    return res;
  };

  const onClickInfoIcon = () => {
    setCycleAlertInfoModal(true);
  };

  return (
    <div className="settings-cycle-stop-alert-layout">
      <CycleAlertInfoModal
        cycleAlertInfoModal={cycleAlertInfoModal}
        setCycleAlertInfoModal={setCycleAlertInfoModal}
      />
      <Row align="middle" className="settings-cycle-stop-alert-title">
        <Col span={24}>
          <TextWithIcon
            text={"Cyle Stop Alert"}
            icon={cycleStopAlertIcon}
            marginLeft={"30px"}
            iconSize={"35px"}
            fontSize={"21px"}
            iconBottom={"2px"}
            iconClick={onClickInfoIcon}
          />
        </Col>
      </Row>
      <div className="settings-cycle-stop-alert-content">
        <Row>
          <Col span={6}>
            <RadialInputGroup
              initValue={appSetting}
              field="cycle_send_alert"
              updateValue={setCycleStopSettings}
              title={"Send Alert"}
            />
          </Col>
          <Col span={6}>
            <TextInputGroup
              initValue={appSetting}
              field="cycle_email1"
              updateValue={setCycleStopSettings}
              title={"Alert Email 1"}
              input_type="email"
            />
          </Col>
          <Col span={6}>
            <TextInputGroup
              initValue={appSetting}
              field="cycle_email2"
              updateValue={setCycleStopSettings}
              title={"Alert Email 2"}
              input_type="email"
            />
          </Col>
          <Col span={6}>
            <TextInputGroup
              initValue={appSetting}
              field="cycle_email3"
              updateValue={setCycleStopSettings}
              title={"Alert Email 3"}
              input_type="email"
            />
          </Col>
        </Row>
      </div>
    </div>
  );
};

export default SettingCycleStopAlertLayout;
