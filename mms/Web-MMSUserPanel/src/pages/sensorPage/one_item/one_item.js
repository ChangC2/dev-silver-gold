import { Col, Row } from "antd";
import moment from "moment";
import { useEffect, useState } from "react";
import { useSelector } from "react-redux";
import SimpleInfoContainer from "../../../components/sensorPageWidgets/simple_info_container/simple_info_container";
import { GetCustomerCurrentTime } from "../../../services/common/cnc_apis";
import {
  SENSOR_TITLE_BY_ID,
  SENSOR_TYPE,
} from "../../../services/common/constants";
import { getEnumKeyByValue } from "../../../services/common/functions";
import lang from "../../../services/lang";
import "./glowEffect.scss";
import "./one_item.css";

import imgTempSensor from "../../../assets/images/temp_sensor.png";
import imgWeightSensor from "../../../assets/images/weight_sensor.png";
import imgTempHumSensor from "../../../assets/images/temp_hum_sensor.png";

function OneItem(props) {
  const { langIndex } = useSelector((x) => x.app);
  const { sensor, sensorInfo, timezone, setSelectedSensorId } = props;
  const [sensorType, setSensorType] = useState("Other");

  const toTimestamp = (strDate) => {
    if (strDate == undefined) return 0;
    var arr = strDate.split(/[- :]/);
    const dt = new Date(arr[0], arr[1] - 1, arr[2], arr[3], arr[4], arr[5]); 
    //const dt = Date.parse(strDate);
    return dt / 1000;
  };;

  var ownReadTime = sensorInfo === undefined ? "-" : sensorInfo.reading_time;
  var isStatusWorking = false;

  if (ownReadTime !== "-") {
    ownReadTime = toTimestamp(ownReadTime);
    ownReadTime = ownReadTime + 3600 * 8;
    ownReadTime = ownReadTime + 3600 * timezone;
    ownReadTime = new Date(ownReadTime * 1000);
    ownReadTime = moment(ownReadTime);
    
    var customerTime = GetCustomerCurrentTime(timezone);
    customerTime = moment(customerTime);

    var duration = moment.duration(customerTime.diff(ownReadTime));
    var minutes = duration.asMinutes();
    if (minutes <= 30) {
      isStatusWorking = true;
    }
  }

  var unit =
    sensorInfo === undefined ||
    sensorInfo === false ||
    sensor.unit === undefined
      ? ""
      : sensor.unit;

  const defaultImages = [
    imgTempSensor,
    imgTempSensor,
    imgTempSensor,
    imgTempSensor,
    imgTempSensor,
    imgWeightSensor,
    imgTempHumSensor,
  ];

  useEffect(() => {
    setSensorType(
      SENSOR_TITLE_BY_ID[getEnumKeyByValue(SENSOR_TYPE, parseInt(sensor.type))]
    );
  }, [sensor]);

  return (
    <div
      className="one-sensor-container"
      // onClick={() => onSelectSensor(sensorName)}
    >
      <Row>
        <Col span={20}>
          <div className="one-sensor-name">{sensor.sensor_name}</div>
          <div className="one-sensor-id">Sensor ID : {sensor.sensor_id}</div>
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
          setSelectedSensorId(sensor.sensor_id);
        }}
      >
        <div className="one-sensor-image-container-style">
          <img
            alt=""
            className="one-sensor-image-style"
            src={
              sensor.image === ""
                ? defaultImages[parseInt(sensor.type)]
                : sensor.image
            }
          />
        </div>

        <SimpleInfoContainer
          className="sensor-locaion-container"
          height={25}
          title={lang(langIndex, "iiot_location")}
          value={sensor.location}
        />

        <SimpleInfoContainer
          height={25}
          title={lang(langIndex, "iiot_type")}
          value={sensorType}
          valueSize={14}
        />

        <Row>
          <Col span={24}>
            <SimpleInfoContainer
              height={25}
              title={
                sensor.for_value1 === ""
                  ? sensor.type === "" + SENSOR_TYPE.tempAndHum
                    ? "Temp"
                    : `${lang(langIndex, "iiot_value")}1`
                  : sensor.for_value1
              }
              value={
                sensorInfo === undefined || sensorInfo === false
                  ? "-"
                  : isStatusWorking
                  ? sensorInfo.value1 + unit
                  : "0.0 " + unit
              }
              valueSize={15}
            />
          </Col>
        </Row>
        <Row>
          <Col span={24}>
            <SimpleInfoContainer
              height={25}
              title={
                sensor.for_value2 === ""
                  ? sensor.type === "" + SENSOR_TYPE.tempAndHum
                    ? "Humidity"
                    : `${lang(langIndex, "iiot_value")}2`
                  : sensor.for_value2
              }
              value={
                sensorInfo === undefined || sensorInfo === false
                  ? "-"
                  : isStatusWorking
                  ? sensor.type === "" + SENSOR_TYPE.tempAndHum
                    ? sensorInfo.value2 + "%"
                    : sensorInfo.value2 + unit
                  : sensor.type === "" + SENSOR_TYPE.tempAndHum
                  ? "0.0 %"
                  : "0.0 " + unit
              }
              valueSize={15}
            />
          </Col>
        </Row>
        <Row>
          <Col span={24}>
            <SimpleInfoContainer
              height={25}
              title={
                sensor.for_value3 === ""
                  ? `${lang(langIndex, "iiot_value")}3`
                  : sensor.for_value3
              }
              value={
                sensorInfo === undefined || sensorInfo === false
                  ? "-"
                  : isStatusWorking
                  ? sensorInfo.value3 + unit
                  : "0.0 " + unit
              }
              valueSize={15}
            />
          </Col>
        </Row>

        <SimpleInfoContainer
          height={25}
          title={lang(langIndex, "iiot_updatetime")}
          value={
            sensorInfo === undefined || sensorInfo === false
              ? "-"
              : ownReadTime.format("MM/DD HH:mm:ss")
          }
          valueSize={14}
        />
      </div>
    </div>
  );
}

export default OneItem;
