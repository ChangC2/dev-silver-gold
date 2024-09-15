import { Button, Col, Row, Radio, message } from "antd";
import Modal from "antd/lib/modal/Modal";
import { DatePicker, TimePicker} from 'antd';
import dayjs from 'dayjs';
import moment from 'moment';

import { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { isSpinning, setAppDataStore } from "redux/actions/appActions";
import { apiCallForRecordPhosphateTestInfo } from "services/apiCall";
import { appData, userData, factoryData } from "services/global";

import "./RecordPhosphateTestInfoModal.css";

function RecordPhosphateTestInfo(props) {

  const dispatch = useDispatch();

  const { showModal, setShowModal, partID } = props;

  const dateFormat = 'MM/DD/YYYY';
  const timeFormat = 'HH:mm:ss';
  
  // Use moment
  const currentDate = moment();
  const formattedDate = currentDate.format(dateFormat);
  const formattedTime = currentDate.format(timeFormat);
  console.log('Init Date:', formattedDate);
  console.log('Init Time:', formattedTime);

  const [rDate, setRDate] = useState(formattedDate);
  const [rTime, setRTime] = useState(formattedTime);

  useEffect(() => {
    setRDate(formattedDate)
    setRTime(formattedTime)
  });

  const handleDateChange = (date, dateString) => {
    setRDate(dateString);
    console.log('Selected Date:', dateString);
  };

  const handleTimeChange = (time, timeString) => {
    setRTime(timeString);
    console.log('Selected Time:', timeString);
  };

  const [weight, setWeight] = useState("0");
  const [optionWaterBreak, setOptionWaterBreak] = useState("-1");

  const onCancel = () => {
    setShowModal(false);
  };

  const onOK = () => {
    reportData()
  };

  const reportData = () => {

    if (factoryData.accountId === "") {
      message.warn("Please login with customer id");
      return;
    }

    if (appData.machineName === "") {
      message.warn("Please input machine name in settings page");
      return;
    }

    if (partID === "") {
      message.warn("Please input Part ID.");
      return;
    }

    let param = {
      customer_id: factoryData.accountId,
      machine_id: appData.machineName,
      operator: userData.username_full,
      part_id: partID,
      created_at: moment().format("YYYY-MM-DD HH:mm:ss") + ".000000",
      timestamp: Date.now(),
      date: rDate,
      time: rTime,
      weight: weight,
      water_break: optionWaterBreak === "0" ? "Pass" : (optionWaterBreak === "1" ? "Fail" : ""),
    };

    dispatch(isSpinning(true));
    apiCallForRecordPhosphateTestInfo(param)
      .then((res) => {
        setShowModal(false);
        dispatch(isSpinning(false));
        message.success("Success to save test info!");
      })
      .catch((err) => {
        setShowModal(false);
        dispatch(isSpinning(false));
        message.error("Fail to report data!");
      });
  };

  return (
    <div>
      <Modal
        centered
        open={showModal}
        className="record-phosphate-testinfo-dialog-style"
        title={null}
        onCancel={() => onCancel()}
        closable={false}
        maskClosable={false}
        destroyOnClose={true}
        header={null}
        footer={null}
      >
        <div>
          <Row align="middle" className="record-phosphate-testinfo-dialog-top">
            <Col>
              <span className="record-phosphate-testinfo-dialog-title">
                {"Record Phosphate Test Info"}
              </span>
            </Col>
          </Row>
          <Row className="record-phosphate-testinfo-dialog-content">
            <Col span={8}>
              <div className="record-phosphate-testinfo-input-container left-aligned">
                <div className="record-phosphate-testinfo-input-title">
                  {"Testing Date"}
                </div>
                <DatePicker defaultValue={currentDate} format={dateFormat} onChange={handleDateChange}/>
              </div>
            </Col>

            <Col span={8}>
              <div className="record-phosphate-testinfo-input-container left-aligned">
                <div className="record-phosphate-testinfo-input-title">
                  {"Time"}
                </div>
                <TimePicker defaultValue={currentDate} format={timeFormat} onChange={handleTimeChange}/>
              </div>
            </Col>

            <Col span={8}>
              <div className="record-phosphate-testinfo-input-container">
                <div className="record-phosphate-testinfo-input-title">
                  {"Weight"}
                </div>
                <input
                  className="record-phosphate-testinfo-input-value"
                  value={weight}
                  onChange={(e) => setWeight(e.target.value)}
                  type="number"
                  step="0.1"
                  min='0'
                  max='100000'
                  style={{ outlineStyle: "none" }}
                />
              </div>
            </Col>
          </Row>
          <Row className="record-phosphate-testinfo-dialog-content">
            <Col span={24}>
              <div className="record-phosphate-testinfo-input-container">
                <div className="record-phosphate-testinfo-input-title left-aligned">
                  {"Water Break"}
                </div>
                <Radio.Group
                  // onChange={updateValue}
                  value={optionWaterBreak}
                  className="app-setting-in-cyle-option"
                  onChange={(e) => {
                    setOptionWaterBreak(e.target.value);
                  }}
                >
                  <Row>
                    <Col span={12}>
                      <Radio value={"0"} style={{ color: "white" }}>
                        Pass
                      </Radio>
                    </Col>
                    <Col span={12}>
                      <Radio value={"1"} style={{ color: "white" }}>
                        Fail
                      </Radio>
                    </Col>
                  </Row>
                </Radio.Group>
              </div>
            </Col>
          </Row>
          <Row className="record-phosphate-testinfo-dialog-content">
            <Col span={12}>
              <Button
                className="record-phosphate-testinfo-dialog-button"
                style={{ marginRight: "5px" }}
                onClick={onCancel}
                type="primary"
              >
                {"Cancel"}
              </Button>
            </Col>
            <Col span={12}>
              <Button
                className="record-phosphate-testinfo-dialog-button"
                style={{ marginLeft: "5px" }}
                onClick={onOK}
                type="primary"
              >
                {"Ok"}
              </Button>
            </Col>
          </Row>
        </div>
      </Modal>
    </div>
  );
}

// Use Date() function
/*const currentDate = new Date();
const formattedDate = currentDate.toLocaleDateString('en-US', {
  year: 'numeric',
  month: '2-digit',
  day: '2-digit'
});
const hours = String(currentDate.getHours()).padStart(2, "0");
const minutes = String(currentDate.getMinutes()).padStart(2, "0");
const seconds = String(currentDate.getSeconds()).padStart(2, "0");
const formattedTime = '${hours}:${minutes}:${seconds}';
console.log('Selected Date:', formattedDate);
console.log('Selected Time:', formattedTime);*/

export default RecordPhosphateTestInfo;
