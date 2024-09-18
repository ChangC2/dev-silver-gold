import { Button, Col, Modal, Row, TimePicker, message } from "antd";
import moment from "moment";
import { useEffect, useState } from "react";
import { useSelector } from "react-redux";
import lang from "../../../services/lang";
import "./ShiftSettings.css";
import { SettingOutlined } from "@ant-design/icons";

function ShiftSettings(props) {
  const { langIndex } = useSelector((x) => x.app);
  const { showModal, setShowModal, customerInfo, updateCustomerInfo } = props;

  const [shift1_start, setShift1_start] = useState();
  const [shift1_end, setShift1_end] = useState();
  const [shift2_start, setShift2_start] = useState();
  const [shift2_end, setShift2_end] = useState();
  const [shift3_start, setShift3_start] = useState();
  const [shift3_end, setShift3_end] = useState();

  const format = "HH:mm";

  useEffect(() => {
    if (customerInfo.shift1_start !== "") {
      setShift1_start(moment(customerInfo.shift1_start, format));
    }
    if (customerInfo.shift1_end !== "") {
      setShift1_end(moment(customerInfo.shift1_end, format));
    }
    if (customerInfo.shift2_start !== "") {
      setShift2_start(moment(customerInfo.shift2_start, format));
    }
    if (customerInfo.shift2_end !== "") {
      setShift2_end(moment(customerInfo.shift2_end, format));
    }
    if (customerInfo.shift3_start !== "") {
      setShift3_start(moment(customerInfo.shift3_start, format));
    }
    if (customerInfo.shift3_end !== "") {
      setShift3_end(moment(customerInfo.shift3_end, format));
    }

    if (customerInfo.shift1_end == "00:00") {
      setShift1_start(null);
      setShift1_end(null);
    }
    if (customerInfo.shift2_end == "00:00") {
      setShift2_start(null);
      setShift2_end(null);
    }
    if (customerInfo.shift3_end == "00:00") {
      setShift3_start(null);
      setShift3_end(null);
    }
  }, [customerInfo]);

  const onShift1StartChange = (time) => {
    setShift1_start(time);

    setShift1_end(null);
    setShift2_start(null);
    setShift2_end(null);
    setShift3_start(null);
    setShift3_end(null);
  };

  const onShift1EndChange = (time) => {
    setShift1_end(time);
    if (time < shift1_start || shift1_start === null) {
      setShift1_end(null);
    }

    setShift2_start(null);
    setShift2_end(null);
    setShift3_start(null);
    setShift3_end(null);
  };

  const onShift2StartChange = (time) => {
    setShift2_start(time);
    if (time < shift1_end || shift1_end === null) {
      setShift2_start(null);
    }

    setShift2_end(null);
    setShift3_start(null);
    setShift3_end(null);
  };

  const onShift2EndChange = (time) => {
    setShift2_end(time);
    if ((time < shift2_start && time > shift1_start) || shift2_start === null) {
      setShift2_end(null);
    }

    setShift3_start(null);
    setShift3_end(null);
  };

  const onShift3StartChange = (time) => {
    setShift3_start(time);
    if (time < shift2_end || shift2_end === null) {
      setShift3_start(null);
    }
    setShift3_end(null);
  };

  const onShift3EndChange = (time) => {
    setShift3_end(time);
    if ((time < shift3_start && time > shift1_start) || shift3_start === null) {
      setShift3_end(null);
    }
  };

  const onUpdateShift = () => {
    if (shift1_start == null && shift2_start == null && shift3_start == null) {
      message.error("Invalid shift time");
      return;
    }

    if (shift1_start != null && shift1_end == null) {
      message.error("Invalid shift time");
      return;
    }
    if (shift2_start != null && shift2_end == null) {
      message.error("Invalid shift time");
      return;
    }
    if (shift3_start != null && shift3_end == null) {
      message.error("Invalid shift time");
      return;
    }

    let newInfo = customerInfo;
    newInfo.shift1_start =
      shift1_start != null ? shift1_start.format(format) : "";
    newInfo.shift1_end = shift1_end != null ? shift1_end.format(format) : "";
    newInfo.shift2_start =
      shift2_start != null ? shift2_start.format(format) : "";
    newInfo.shift2_end = shift2_end != null ? shift2_end.format(format) : "";
    newInfo.shift3_start =
      shift3_start != null ? shift3_start.format(format) : "";
    newInfo.shift3_end = shift3_end != null ? shift3_end.format(format) : "";
    updateCustomerInfo(newInfo);
  };

  const onCancel = () => {
    setShowModal(false);
  };

  return (
    <div>
      <Modal
        centered
        visible={showModal}
        title={null}
        onCancel={() => onCancel()}
        onOk={() => onUpdateShift()}
        okText={lang(langIndex, "plant_update")}
        closable={true}
        className="customer-setting-dialog-style"
        destroyOnClose={true}
        width={700}
      >
        <div className="customer-setting-page-title">
          <SettingOutlined />
          <span style={{ marginLeft: 10 }} />
          {lang(langIndex, "shift_settings")}
        </div>
        <div className="customer-setting-container-style">
          <div className="app-setting-text-input-title">
            {" "}
            {lang(langIndex, "shift")} 1
          </div>
          <Row style={{ marginTop: 5, marginBottom: 15 }}>
            <Col span={11}>
              <TimePicker
                className="shift-setting-time-picker-style"
                value={shift1_start}
                style={{}}
                onChange={onShift1StartChange}
                format={format}
                placeholder="Start Time"
              />
            </Col>
            <Col
              className="shift-title-style"
              span={1}
              style={{ textAlign: "center" }}
            >
              ~
            </Col>
            <Col span={11}>
              <TimePicker
                className="shift-setting-time-picker-style"
                value={shift1_end}
                onChange={onShift1EndChange}
                format={format}
                placeholder="End Time"
              />
            </Col>
          </Row>

          <div className="app-setting-text-input-title">
            {" "}
            {lang(langIndex, "shift")} 2
          </div>
          <Row style={{ marginTop: 5, marginBottom: 15 }}>
            <Col span={11}>
              <TimePicker
                className="shift-setting-time-picker-style"
                value={shift2_start}
                onChange={onShift2StartChange}
                format={format}
                placeholder="Start Time"
              />
            </Col>
            <Col
              className="shift-title-style"
              span={1}
              style={{ textAlign: "center" }}
            >
              ~
            </Col>
            <Col span={11}>
              <TimePicker
                className="shift-setting-time-picker-style"
                value={shift2_end}
                onChange={onShift2EndChange}
                format={format}
                placeholder="End Time"
              />
            </Col>
          </Row>

          <div className="app-setting-text-input-title">
            {" "}
            {lang(langIndex, "shift")} 3
          </div>
          <Row style={{ marginTop: 5, marginBottom: 15 }}>
            <Col span={11}>
              <TimePicker
                className="shift-setting-time-picker-style"
                value={shift3_start}
                onChange={onShift3StartChange}
                format={format}
                placeholder="Start Time"
              />
            </Col>
            <Col
              className="shift-title-style"
              span={1}
              style={{ textAlign: "center" }}
            >
              ~
            </Col>
            <Col span={11}>
              <TimePicker
                className="shift-setting-time-picker-style"
                value={shift3_end}
                onChange={onShift3EndChange}
                format={format}
                placeholder="End Time"
              />
            </Col>
          </Row>
        </div>
      </Modal>
    </div>
  );
}

export default ShiftSettings;
