import { Col, message, Row, Select } from "antd";
import Modal from "antd/lib/modal/Modal";
import { useEffect, useState } from "react";
import { useSelector } from "react-redux";
import OptionInput from "../../../../../src/components/cncPageComponents/OptionInput/OptionInput";
import RadialInput from "../../../../../src/components/cncPageComponents/RadialInput/RadialInput";
import SliderInput from "../../../../../src/components/cncPageComponents/SliderInput/SliderInput";
import TextInput from "../../../../../src/components/cncPageComponents/TextInput/TextInput";
import Urls, { postRequest } from "../../../../../src/services/common/urls";
import lang from "../../../../../src/services/lang";
import "./AppSettingModal.css";

import { InfoCircleOutlined } from "@ant-design/icons";

import CycleAlertInfoModal from "../CycleAlertInfo/CycleAlertInfoModal";
import AppSettingCalc from "./AppSettingCalc";

const { Option } = Select;

function AppSettingModal(props) {
  const { langIndex } = useSelector((x) => x.app);
  const {
    isShowAppSettingModal,
    setIsShowAppSettingModal,
    customer_id,
    security_level,
    machineInfo,
    setSetting,
  } = props;

  const [appSetting, setAppSetting] = useState({
    id: "",
    factory_id: customer_id,
    machine_id: machineInfo.machine_id,
    downtime_reason1: "Clear Chips",
    downtime_reason2: "Wait Materials",
    downtime_reason3: "Wait Tooling",
    downtime_reason4: "Break",
    downtime_reason5: "No Operator",
    downtime_reason6: "P.M.",
    downtime_reason7: "Unplanned Repair",
    downtime_reason8: "Other",
    cslock_cycle: "1",
    cslock_reverse: "0",
    cslock_guest: "0",
    cslock_alert: "1",
    time_stop: "00:00:30",
    time_production: "05:00:00",
    cycle_send_alert: "0",
    cycle_email1: "",
    cycle_email2: "",
    cycle_email3: "",
    automatic_part: "0",
    automatic_min_time: "10",
    automatic_part_per_cycle: "4",
    gantt_chart_display: "0",
  });

  const [cycleAlertInfoModal, setCycleAlertInfoModal] = useState(false);

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

  useEffect(() => {
    var url = Urls.GET_APP_SETTING;
    var param = {
      factory_id: customer_id,
      machine_id: machineInfo.machine_id,
    };

    postRequest(url, param, (res) => {
      if (res.status === true) {
        setAppSetting({
          ...res.data,
          time_production: getTimeFromSecond(res.data.time_production),
          time_stop: getTimeFromSecond(res.data.time_stop),
        });
      }
    });
  }, []);

  const setAppSettings = (field, value) => {
    var newSetting = { ...appSetting };
    newSetting[field] = value;
    setAppSetting({ ...newSetting });
  };

  const getSecondFromTime = (time) => {
    var timeList = time.split(":");
    var hr = isNaN(parseInt(timeList[0])) ? 0 : parseInt(timeList[0]);
    var min = isNaN(parseInt(timeList[1])) ? 0 : parseInt(timeList[1]);
    var sec = isNaN(parseInt(timeList[2])) ? 0 : parseInt(timeList[2]);
    return hr * 3600 + min * 60 + sec;
  };
  const formattedNumber = (myNumber) => ("0" + myNumber).slice(-2);
  const getTimeFromSecond = (second) => {
    second = parseInt(second);
    if (isNaN(second)) return "00:00:00";

    var hr = Math.floor(second / 3600);
    var min = Math.floor((second % 3600) / 60);
    var sec = second % 60;

    return `${formattedNumber(hr)}:${formattedNumber(min)}:${formattedNumber(
      sec
    )}`;
  };

  const onConfirmOK = () => {
    var validationResult = validate_info(appSetting);
    if (validationResult != true) return;
    var url = Urls.UPDATE_APP_SETTING;
    var newSetting = { ...appSetting };
    newSetting["factory_id"] = customer_id;
    newSetting["machine_id"] = machineInfo.machine_id;
    delete newSetting.id;

    newSetting.time_production = getSecondFromTime(newSetting.time_production);
    newSetting.time_stop = getSecondFromTime(newSetting.time_stop);

    var param = {
      detail: { ...newSetting },
    };
    postRequest(url, param, (res) => {
      if (res === null) return;
      if (res.status === true) {
        setSetting(appSetting);
        message.success(lang(langIndex, "msg_update_success"));
      } else {
        message.error(lang(langIndex, "msg_something_wrong"));
      }
    });

    setIsShowAppSettingModal(false);
  };

  return (
    <div>
      <CycleAlertInfoModal
        cycleAlertInfoModal={cycleAlertInfoModal}
        setCycleAlertInfoModal={setCycleAlertInfoModal}
      />

      <Modal
        centered
        visible={isShowAppSettingModal}
        title={null}
        onCancel={() => setIsShowAppSettingModal(false)}
        closable={true}
        onOk={onConfirmOK}
        className="app-setting-dialog-style"
        destroyOnClose={true}
        width={1000}
        okButtonProps={{
          style: {
            display: security_level === 0 ? "none" : "",
          },
        }}
        cancelButtonProps={{
          style: {
            display: security_level === 0 ? "none" : "",
          },
        }}
      >
        <div>
          <div className="app-setting-group-container">
            <div className="app-setting-group-title">
              {lang(langIndex, "cnc_downtime")}
            </div>
            <div className="app-setting-group-details">
              <Row>
                <Col span={6}>
                  <TextInput
                    initValue={appSetting}
                    field="downtime_reason1"
                    updateValue={setAppSettings}
                    title={lang(langIndex, "cnc_downtimereason") + "1"}
                  />
                </Col>
                <Col span={6}>
                  <TextInput
                    initValue={appSetting}
                    field="downtime_reason2"
                    updateValue={setAppSettings}
                    title={lang(langIndex, "cnc_downtimereason") + "2"}
                  />
                </Col>
                <Col span={6}>
                  <TextInput
                    initValue={appSetting}
                    field="downtime_reason3"
                    updateValue={setAppSettings}
                    title={lang(langIndex, "cnc_downtimereason") + "3"}
                  />
                </Col>
                <Col span={6}>
                  <TextInput
                    initValue={appSetting}
                    field="downtime_reason4"
                    updateValue={setAppSettings}
                    title={lang(langIndex, "cnc_downtimereason") + "4"}
                  />
                </Col>
                <Col span={6}>
                  <TextInput
                    initValue={appSetting}
                    field="downtime_reason5"
                    updateValue={setAppSettings}
                    title={lang(langIndex, "cnc_downtimereason") + "5"}
                  />
                </Col>
                <Col span={6}>
                  <TextInput
                    initValue={appSetting}
                    field="downtime_reason6"
                    updateValue={setAppSettings}
                    title={lang(langIndex, "cnc_downtimereason") + "6"}
                  />
                </Col>
                <Col span={6}>
                  <TextInput
                    initValue={appSetting}
                    field="downtime_reason7"
                    updateValue={setAppSettings}
                    title={lang(langIndex, "cnc_downtimereason") + "7"}
                  />
                </Col>
                <Col span={6}>
                  <TextInput
                    initValue={appSetting}
                    field="downtime_reason8"
                    updateValue={setAppSettings}
                    title={lang(langIndex, "cnc_downtimereason") + "8"}
                  />
                </Col>
              </Row>
            </div>
          </div>
          <div className="app-setting-group-container">
            <div className="app-setting-group-title">
              {lang(langIndex, "cnc_cslock")}
            </div>
            <div className="app-setting-group-details">
              <Row>
                <Col span={12}>
                  <RadialInput
                    initValue={appSetting}
                    field="cslock_cycle"
                    updateValue={setAppSettings}
                    title={lang(langIndex, "cnc_usecycle")}
                  />
                </Col>
                <Col span={12}>
                  <RadialInput
                    initValue={appSetting}
                    field="cslock_reverse"
                    updateValue={setAppSettings}
                    title={lang(langIndex, "cnc_reversecslock")}
                  />
                </Col>
                <Col span={12}>
                  <RadialInput
                    initValue={appSetting}
                    field="cslock_guest"
                    updateValue={setAppSettings}
                    title={lang(langIndex, "cnc_guestlock")}
                  />
                </Col>
                <Col span={12}>
                  <RadialInput
                    initValue={appSetting}
                    field="cslock_alert"
                    updateValue={setAppSettings}
                    title={lang(langIndex, "cnc_enablealert")}
                  />
                </Col>
              </Row>
            </div>
          </div>
          <div className="app-setting-group-container">
            <div className="app-setting-group-title">
              {lang(langIndex, "cnc_timesetting")}
            </div>
            <div className="app-setting-group-details">
              <Row>
                <Col span={12}>
                  <TextInput
                    initValue={appSetting}
                    field="time_stop"
                    updateValue={setAppSettings}
                    title={`${lang(langIndex, "cnc_stoptimelimit")} (${commafy(
                      getSecondFromTime(appSetting.time_stop)
                    )} s)`}
                    isCenter={true}
                    input_type="time-setting"
                  />
                </Col>
                <Col span={12}>
                  <TextInput
                    initValue={appSetting}
                    field="time_production"
                    updateValue={setAppSettings}
                    title={lang(langIndex, "cnc_plannedproducttime")}
                    isCenter={true}
                    input_type="time-setting"
                  />
                </Col>
              </Row>
            </div>
          </div>
          <div className="app-setting-group-container">
            <div className="app-setting-group-title">
              <Row>
                <Col span={6}>{lang(langIndex, "cnc_cyclealert")}</Col>
                <Col span={17}></Col>
                <Col span={1}>
                  <InfoCircleOutlined
                    className="app-setting-show_cycle_help my-own-button"
                    onClick={() => setCycleAlertInfoModal(true)}
                  />
                </Col>
              </Row>
            </div>
            <div className="app-setting-group-details">
              <Row>
                <Col span={6}>
                  <RadialInput
                    initValue={appSetting}
                    field="cycle_send_alert"
                    updateValue={setAppSettings}
                    title={lang(langIndex, "cnc_sendalert")}
                  />
                </Col>
                <Col span={6}>
                  <TextInput
                    initValue={appSetting}
                    field="cycle_email1"
                    updateValue={setAppSettings}
                    title={lang(langIndex, "cnc_alertemail") + "1"}
                    input_type="email"
                  />
                </Col>
                <Col span={6}>
                  <TextInput
                    initValue={appSetting}
                    field="cycle_email2"
                    updateValue={setAppSettings}
                    title={lang(langIndex, "cnc_alertemail") + "2"}
                    input_type="email"
                  />
                </Col>
                <Col span={6}>
                  <TextInput
                    initValue={appSetting}
                    field="cycle_email3"
                    updateValue={setAppSettings}
                    title={lang(langIndex, "cnc_alertemail") + "3"}
                    input_type="email"
                  />
                </Col>
              </Row>
            </div>
          </div>
          <div className="app-setting-group-container">
            <div className="app-setting-group-title">
              {lang(langIndex, "cnc_partcounter")}
            </div>
            <div className="app-setting-group-details">
              <Row>
                <Col span={6}>
                  <RadialInput
                    initValue={appSetting}
                    field="automatic_part"
                    updateValue={setAppSettings}
                    title={lang(langIndex, "cnc_automaticcount")}
                  />
                </Col>
                <Col span={12}>
                  <SliderInput
                    initValue={appSetting}
                    field="automatic_min_time"
                    updateValue={setAppSettings}
                    title={lang(langIndex, "cnc_minelapsed")}
                  />
                </Col>
                <Col span={6}>
                  <TextInput
                    initValue={appSetting}
                    field="automatic_part_per_cycle"
                    updateValue={setAppSettings}
                    title={lang(langIndex, "cnc_partcycle")}
                    input_type="number"
                  />
                </Col>
              </Row>
            </div>
          </div>
          <div className="app-setting-group-container">
            <div className="app-setting-group-title">
              {lang(langIndex, "cnc_ganttsetting")}
            </div>
            <div className="app-setting-group-details">
              <Row>
                <Col span={12}>
                  <OptionInput
                    span={24}
                    initValue={appSetting}
                    field="gantt_chart_display"
                    updateValue={setAppSettings}
                    title={[
                      lang(langIndex, "cnc_showgantt1"),
                      lang(langIndex, "cnc_showgantt2"),
                      lang(langIndex, "cnc_showdailygoalchart"),
                    ]}
                  />
                </Col>
                {appSetting["gantt_chart_display"] === "2" && (
                  <Col span={12}>
                    <AppSettingCalc
                      appSetting={appSetting}
                      setAppSettings={setAppSettings}
                    />
                  </Col>
                )}
              </Row>

              <div></div>
            </div>
          </div>
        </div>
      </Modal>
    </div>
  );
}

export default AppSettingModal;
