import { Col, Row, Spin } from "antd";
import moment from "moment";
import { useEffect, useState } from "react";
import { useSelector } from "react-redux";
import SimpleInfoContainer from "../../../components/HennigPageWidgets/simple_info_container/simple_info_container";
import { GetCustomerCurrentTime } from "../../../services/common/cnc_apis";
import {
  HENNIG_TITLE_BY_ID,
  HENNIG_TYPE,
} from "../../../services/common/constants";
import { getEnumKeyByValue } from "../../../services/common/functions";
import lang from "../../../services/lang";
import "./glowEffect.scss";
import "./OneItem.css";

import sensorDefaultImage from "../../../assets/images/default_sensor.jpg";

function OneItem(props) {
  const { langIndex } = useSelector((x) => x.app);
  const { hennig, hennigInfo, timezone, setSelectedSensorId } = props;
  const [hennigType, setHennigType] = useState("Other");

  useEffect(() => {
    setHennigType(
      HENNIG_TITLE_BY_ID[getEnumKeyByValue(HENNIG_TYPE, parseInt(hennig.type))]
    );
  }, [hennig]);

  if (hennigInfo == undefined) {
    return (
      <div style={{ paddingTop: 100, textAlign: "center" }}>
        <Spin tip="Loading ..." size="large" />
      </div>
    );
  }

  let value1Info = hennig.for_value1.split(",");
  let value1Label = value1Info[0];
  let value1Unit = "";
  if (value1Info.length > 1) {
    value1Unit = " " + value1Info[1];
  }
  let value1Rate = 1.0;
  if (value1Info.length > 2) {
    value1Rate = parseFloat(value1Info[2]);
  }
  let value1 = Math.round(hennigInfo.value1 * value1Rate * 100) / 100;

  let value2Info = hennig.for_value2.split(",");
  let value2Label = value2Info[0];
  let value2Unit = "";
  if (value2Info.length > 1) {
    value2Unit = " " + value2Info[1];
  }
  let value2Rate = 1.0;
  if (value2Info.length > 2) {
    value2Rate = parseFloat(value2Info[2]);
  }
  let value2 = Math.round(hennigInfo.value2 * value2Rate * 100) / 100;

  let value3Info = hennig.for_value3.split(",");
  let value3Label = value3Info[0];
  let value3Unit = "";
  if (value3Info.length > 1) {
    value3Unit = " " + value3Info[1];
  }
  let value3Rate = 1.0;
  if (value3Info.length > 2) {
    value3Rate = parseFloat(value3Info[2]);
  }
  let value3 = Math.round(hennigInfo.value3 * value3Rate * 100) / 100;

  let value4Info = hennig.for_value4.split(",");
  let value4Label = value4Info[0];
  let value4Unit = "";
  if (value4Info.length > 1) {
    value4Unit = " " + value4Info[1];
  }
  let value4Rate = 1.0;
  if (value4Info.length > 2) {
    value4Rate = parseFloat(value4Info[2]);
  }
  let value4 = Math.round(hennigInfo.value4 * value4Rate * 100) / 100;

  let value5Info = hennig.for_value5.split(",");
  let value5Label = value5Info[0];
  let value5Unit = "";
  if (value5Info.length > 1) {
    value5Unit = " " + value5Info[1];
  }
  let value5Rate = 1.0;
  if (value5Info.length > 2) {
    value5Rate = parseFloat(value5Info[2]);
  }
  let value5 = hennigInfo.value5;
  if (hennig.sensor_id == "5002") {
    value5 = value5 == 0 ? "No Alarm" : "Low Pressure Alarm";
  } else {
    value5 = Math.round(hennigInfo.value5 * value5Rate * 100) / 100;
  }

  let value6Info = hennig.for_value6.split(",");
  let value6Label = value6Info[0];
  let value6Unit = "";
  if (value6Info.length > 1) {
    value6Unit = " " + value6Info[1];
  }
  let value6Rate = 1.0;
  if (value6Info.length > 2) {
    value6Rate = parseFloat(value6Info[2]);
  }
  let value6 = hennigInfo.value6;
  if (hennig.sensor_id == "5002") {
    value6 = value6 == 0 ? "No Alarm" : "Alarm";
  } else {
    value6 = Math.round(hennigInfo.value6 * value6Rate * 100) / 100;
  }

  let value7Info = hennig.for_value7.split(",");
  let value7Label = value7Info[0];
  let value7Unit = "";
  if (value7Info.length > 1) {
    value7Unit = " " + value7Info[1];
  }
  let value7Rate = 1.0;
  if (value7Info.length > 2) {
    value7Rate = parseFloat(value7Info[2]);
  }
  let value7 = Math.round(hennigInfo.value7 * value7Rate * 100) / 100;

  let value8Info = hennig.for_value8.split(",");
  let value8Label = value8Info[0];
  let value8Unit = "";
  if (value8Info.length > 1) {
    value8Unit = " " + value8Info[1];
  }
  let value8Rate = 1.0;
  if (value8Info.length > 2) {
    value8Rate = parseFloat(value8Info[2]);
  }
  let value8 = Math.round(hennigInfo.value8 * value8Rate * 100) / 100;

  const toTimestamp = (strDate) => {
    if (strDate == undefined) return 0;
    var arr = strDate.split(/[- :]/);
    const dt = new Date(arr[0], arr[1] - 1, arr[2], arr[3], arr[4], arr[5]);
    //const dt = Date.parse(strDate);
    return dt / 1000;
  };

  var ownReadTime = hennigInfo === undefined ? "-" : hennigInfo.reading_time;
  if (ownReadTime !== "-") {
    ownReadTime = toTimestamp(ownReadTime);
    ownReadTime = ownReadTime + 3600 * 8;
    ownReadTime = ownReadTime + 3600 * timezone;
    ownReadTime = new Date(ownReadTime * 1000);
    ownReadTime = moment(ownReadTime);
  }
  var isStatusWorking = false;

  if (hennigInfo != undefined) {
    if (hennig.sensor_id == "5002" && hennigInfo.system_status != undefined) {
      isStatusWorking = hennigInfo.system_status == 1;
    } else {
      var customerTime = GetCustomerCurrentTime(timezone);
      customerTime = moment(customerTime);
      var duration = moment.duration(customerTime.diff(ownReadTime));
      var minutes = duration.asMinutes();
      if (minutes <= 30) {
        isStatusWorking = true;
      }
    }
  }

  // isStatusWorking = true;

  return (
    <div
      className="one-hennig-container"
      // onClick={() => onSelectHennig(hennigName)}
    >
      <Row>
        <Col span={20}>
          <div className="one-hennig-name">{hennig.sensor_name}</div>
          {/* <div className="one-hennig-id">Sensor ID : {hennig.sensor_id}</div> */}
        </Col>

        <Col
          span={4}
          style={{
            display: "flex",
            justifyContent: "flex-end",
            paddingRight: 10,
          }}
        >
          <span
            className={
              isStatusWorking
                ? "one-hennig-status-online"
                : "one-hennig-status-offline"
            }
          >
            {isStatusWorking ? "Online" : "Offline"}
          </span>
          <div
            className={
              isStatusWorking
                ? "one-hennig-status green-glow"
                : "one-hennig-status red-glow"
            }
          >
            &#8226;
          </div>
        </Col>
      </Row>
      <div
        onClick={() => {
          setSelectedSensorId(hennig.sensor_id);
        }}
      >
        <div className="one-hennig-image-container-style">
          <img
            alt=""
            className="one-hennig-image-style"
            src={
              hennig.image === "" || hennig.image === null
                ? sensorDefaultImage
                : hennig.image
            }
          />
        </div>

        <SimpleInfoContainer
          height={30}
          title={"Status"}
          value={isStatusWorking ? "Running" : "Not Running"}
          isStatusWorking={isStatusWorking}
          valueSize={16}
        />

        <Row>
          <Col span={24}>
            <SimpleInfoContainer
              height={30}
              title={value1Label}
              value={value1 + value1Unit}
              valueSize={17}
            />
          </Col>
        </Row>
        <Row>
          <Col span={24}>
            <SimpleInfoContainer
              height={30}
              title={value2Label}
              value={value2 + value2Unit}
              valueSize={17}
            />
          </Col>
        </Row>

        {value3Label !== "" && (
          <Row>
            <Col span={24}>
              <SimpleInfoContainer
                height={30}
                title={value3Label}
                value={value3 + value3Unit}
                valueSize={17}
              />
            </Col>
          </Row>
        )}

        {value4Label !== "" && (
          <Row>
            <Col span={24}>
              <SimpleInfoContainer
                height={30}
                title={value4Label}
                value={value4 + value4Unit}
                valueSize={17}
              />
            </Col>
          </Row>
        )}

        {value5Label !== "" && (
          <Row>
            <Col span={24}>
              <SimpleInfoContainer
                height={30}
                title={value5Label}
                value={value5 + value5Unit}
                valueSize={17}
              />
            </Col>
          </Row>
        )}

        {value6Label !== "" && (
          <Row>
            <Col span={24}>
              <SimpleInfoContainer
                height={30}
                title={value6Label}
                value={value6 + value6Unit}
                valueSize={17}
              />
            </Col>
          </Row>
        )}

        {value7Label !== "" && (
          <Row>
            <Col span={24}>
              <SimpleInfoContainer
                height={30}
                title={value7Label}
                value={value7 + value7Unit}
                valueSize={17}
              />
            </Col>
          </Row>
        )}

        {value8Label !== "" && (
          <Row>
            <Col span={24}>
              <SimpleInfoContainer
                height={30}
                title={value8Label}
                value={value8 + value8Unit}
                valueSize={17}
              />
            </Col>
          </Row>
        )}

        <div className="hennig-one-item-reading-time">
          {ownReadTime.format("MM/DD HH:mm:ss")}
        </div>
      </div>
    </div>
  );
}

export default OneItem;
