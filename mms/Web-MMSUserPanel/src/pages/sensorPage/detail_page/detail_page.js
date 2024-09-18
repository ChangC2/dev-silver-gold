import { Button, Col, DatePicker, Popconfirm, Row, Spin } from "antd";
import { useEffect, useState } from "react";
import { useSelector } from "react-redux";
import "./detail_page.css";

import {
  ArrowLeftOutlined,
  CloseOutlined,
  DoubleLeftOutlined,
  DoubleRightOutlined,
} from "@ant-design/icons";

import moment from "moment";
import SensorTrendChart from "../../../components/sensorPageWidgets/sensor_trend_chart/sensor_trend_chart";
import { GetCustomerCurrentTime } from "../../../services/common/cnc_apis";
import { getDetailedSensorData } from "../../../services/common/sensor_apis";
import { getEnumKeyByValue } from "../../../services/common/functions";
import {
  SENSOR_TITLE_BY_ID,
  SENSOR_TYPE,
} from "../../../services/common/constants";

import lang from "../../../services/lang";
import AlertSettingContainer from "./alertSettingContainer/alertSettingContainer";
import DetailPageValueContainer from "./detailPageValueContainer/detailPageValueContainer";
import ExportCSVDialog from "./exportCSVDialog/ExportCSVDialog";

import imgTempSensor from "../../../assets/images/temp_sensor.png";
import imgWeightSensor from "../../../assets/images/weight_sensor.png";
import imgTempHumSensor from "../../../assets/images/temp_hum_sensor.png";

function DetailPage(props) {
  const { langIndex } = useSelector((x) => x.app);
  const { sensorInfo, setSelectedSensorId, timezone, onDeleteSensor } = props;

  const [tick, setTick] = useState(true);
  const [intervalId, setIntervalID] = useState(null);
  const [currentTime, setCurrentTime] = useState(null);
  const [isReading, setIsReading] = useState(false);
  const [isShowingExportModal, setShowingExportModal] = useState(false);
  const intervalTime = 60000;

  const [sensorData, setSensorData] = useState([]);
  const [valueList, setValueList] = useState([]);

  const defaultImages = [
    imgTempSensor,
    imgTempSensor,
    imgTempSensor,
    imgTempSensor,
    imgTempSensor,
    imgWeightSensor,
    imgTempHumSensor,
  ];

  const timer = () => {
    setTick((t) => !t);
  };

  const app_info = useSelector((state) => state.app);
  const customer_info = useSelector(
    (state) => state.cncService["customerInfoList"][app_info["customer_id"]]
  );

  const toCalifonia = (strDate) => {
    var arr = strDate.split(/[- :]/);
    let ret = new Date(arr[0], arr[1] - 1, arr[2], arr[3], arr[4], "00"); // Date.parse(strDate);
    ret = ret / 1000;
    ret = ret - 3600 * 8;
    ret = ret - 3600 * timezone;
    ret = new Date(ret * 1000);
    ret = moment(ret);
    return ret;
  };

  useEffect(() => {
    try {
      clearInterval(intervalId);
    } catch (_) {}

    const timezone = customer_info["timezone"];
    var curTime = GetCustomerCurrentTime(timezone);
    setCurrentTime(moment(curTime));

    setIntervalID(setInterval(timer, intervalTime));
    return () => {
      clearInterval(intervalId);
    };
  }, []);

  useEffect(() => {
    if (!isReading && currentTime != null) {
      var curTime = currentTime.format("YYYY-MM-DD");
      getDetailedSensorData(sensorInfo.sensor_id, curTime, (res) => {
        if (res === null) return;
        setSensorData(res);
      });
    }
  }, [tick]);

  useEffect(() => {
    if (currentTime != null) {
      setIsReading(true);
      var curTime = currentTime.format("YYYY-MM-DD");
      getDetailedSensorData(sensorInfo.sensor_id, curTime, (res) => {
        setIsReading(false);
        if (res === null) return;
        setSensorData(res);
      });
    }
  }, [currentTime]);

  useEffect(() => {
    if (sensorData == undefined || currentTime == undefined) {
      return;
    }

    const startTime = moment(currentTime).set({
      hour: 0,
      minute: 0,
      second: 0,
    });

    const endTime = moment(currentTime).set({
      hour: 23,
      minute: 0,
      second: 0,
    });

    const timeList = [];
    while (startTime <= endTime) {
      timeList.push(startTime.format("HH:mm"));
      startTime.add(1, "hours");
    }
    let chartDataArray = Array();
    let chartData = Array();

    timeList.forEach((_time) => {
      const info = sensorData.find(
        (x) =>
          x.reading_time.substring(0, 14) ===
          toCalifonia(
            endTime.format("YYYY-MM-DD").substring(0, 10) + " " + _time
          )
            .format("YYYY-MM-DD HH:mm")
            .substring(0, 14)
      );

      let oneItem = [
        _time,
        info == undefined ? 0 : parseFloat(info.value1),
        info == undefined ? 0 : parseFloat(info.value2),
        info == undefined ? 0 : parseFloat(info.value3),
      ];
      chartData.push(oneItem);
    });

    setValueList(chartData);
  }, [currentTime, sensorData]);

  const exportToCSV = () => {
    setShowingExportModal(true);
  };

  var ownReadTime = "-";
  var isStatusWorking = false;
  if (sensorData.length > 0) {
    ownReadTime = sensorData[sensorData.length - 1].reading_time;
    ownReadTime = moment(ownReadTime);

    var customerTime = GetCustomerCurrentTime(timezone);

    customerTime = moment(customerTime);

    var duration = moment.duration(customerTime.diff(ownReadTime));
    var minutes = duration.asMinutes();
    if (minutes <= 30) {
      isStatusWorking = true;
      ownReadTime = lang(langIndex, "iiot_connectiongood");
    } else {
      isStatusWorking = false;
      ownReadTime = lang(langIndex, "iiot_connectionlost");
    }
  }
  var unit =
    sensorInfo === undefined || sensorInfo.unit === undefined
      ? ""
      : sensorInfo.unit;

  const updateSetting = (
    <div style={{ fontSize: 16 }}>
      <div>
        <div className="sensor-detail-one-sensor-status-title">
          {lang(langIndex, "iiot_connectionstatus")}:
        </div>
        <div
          className="sensor-detail-one-sensor-status-value"
          style={{ color: isStatusWorking ? "#eeeeee" : "#888888" }}
        >
          {ownReadTime}
        </div>
      </div>
      <div>
        <div className="sensor-detail-one-sensor-info-title">
          {lang(langIndex, "iiot_sn")}:
        </div>
        <div className="sensor-detail-one-sensor-info-value">
          {sensorInfo.sensor_id}
        </div>
      </div>
    </div>
  );

  return (
    <div className="sensor-detail-page-container-style">
      <ExportCSVDialog
        isShowingExportModal={isShowingExportModal}
        setShowingExportModal={setShowingExportModal}
        sensor_id={sensorInfo.sensor_id}
      />

      <Row>
        <Col span={4}>
          <ArrowLeftOutlined
            className="sensor-backward-button"
            onClick={() => setSelectedSensorId("")}
          />
        </Col>
        <Col span={20} className="delete-sensor-button">
          <Popconfirm
            onConfirm={() => {
              onDeleteSensor(sensorInfo.sensor_id);
              setSelectedSensorId("");
            }}
            okText="Yes"
            cancelText="No"
            title={lang(langIndex, "jobentry_suretodelete")}
            placement="bottomLeft"
            className="sensor-delete-popup"
          >
            <span ghost style={{ textAlign: "right" }}>
              <CloseOutlined /> Delete Sensor
            </span>
          </Popconfirm>
        </Col>

        <Col span={24} className="sensor-detail-one-sensor-container">
          <Row align="center">
            <Col>
              <img
                alt=""
                className="sensor-detail-page-image-style"
                src={
                  sensorInfo.image === ""
                    ? defaultImages[parseInt(sensorInfo.type)]
                    : sensorInfo.image
                }
              />
            </Col>
            <Col flex={"auto"} style={{ marginLeft: 20 }}>
              <div className="sensor-detail-one-sensor-text">
                {lang(langIndex, "iiot_name")} : {sensorInfo.sensor_name}
              </div>
              <div className="sensor-detail-one-sensor-text">
                {lang(langIndex, "iiot_type")} :{" "}
                {
                  SENSOR_TITLE_BY_ID[
                    getEnumKeyByValue(SENSOR_TYPE, sensorInfo.type)
                  ]
                }
              </div>

              <div className="sensor-detail-one-sensor-text">
                {lang(langIndex, "iiot_location_c")} : {sensorInfo.location}
              </div>
            </Col>
          </Row>
        </Col>
      </Row>

      <Row>
        <Col className="chart-description-container" span={24}>
          <Row style={{ fontSize: 16 }}>
            <Col flex={"170px"}> </Col>
            <Col flex={"auto"}>
              <Row>
                <Col xs={0} sm={8}></Col>
                <Col xs={0} sm={8} style={{ textAlign: "center" }}>
                  Max
                </Col>
                <Col xs={0} sm={8} style={{ textAlign: "center" }}>
                  Min
                </Col>
              </Row>
            </Col>
          </Row>

          <DetailPageValueContainer
            title={
              sensorInfo.for_value1 === ""
                ? `- ${lang(langIndex, "iiot_valuedescription")}1`
                : `- ${sensorInfo.for_value1}`
            }
            valueList={sensorData.map((x) => parseFloat(x.value1))}
            unit={unit}
            color={"#da1c1c"}
          />
          <DetailPageValueContainer
            title={
              sensorInfo.for_value2 === ""
                ? `- ${lang(langIndex, "iiot_valuedescription")}2`
                : `- ${sensorInfo.for_value2}`
            }
            valueList={sensorData.map((x) => parseFloat(x.value2))}
            unit={unit}
            color={"#1093f1"}
          />
          <DetailPageValueContainer
            title={
              sensorInfo.for_value3 === ""
                ? `- ${lang(langIndex, "iiot_valuedescription")}3`
                : `- ${sensorInfo.for_value3}`
            }
            valueList={sensorData.map((x) => parseFloat(x.value3))}
            unit={unit}
            color={"#0fa30f"}
          />
        </Col>
      </Row>

      <div className="detail-page-trend-container">
        <div className="detail-page-trend-title">
          {lang(langIndex, "iiot_trendchart")}
        </div>
        <div className="detail-page-trend-chart">
          {isReading ? (
            <Spin spinning={isReading} size="large">
              <div
                style={{
                  width: "100%",
                  textAlign: "center",
                  fontSize: 17,
                  color: "#eeeeee",
                  background: "transparent",
                  padding: 10,
                }}
              >
                {lang(langIndex, "loading_sensor_data")}
              </div>
            </Spin>
          ) : (
            <SensorTrendChart
              valueList={valueList}
              chartColor={["#da1c1c", "#1093f1", "#0fa30f"]}
              screenSize={app_info.screenSize}
            />
          )}
        </div>
      </div>

      <Row style={{ marginTop: 10 }}>
        <Col md={9}>{updateSetting}</Col>
        <Col md={6}>
          <div style={{ width: "100%", textAlign: "center" }}>
            <button className="detail-page-export-button" onClick={exportToCSV}>
              {lang(langIndex, "iiot_exporttocsv")}
            </button>
          </div>
        </Col>
        <Col md={9} style={{ textAlign: "right" }}>
          <div>
            <Button
              type="ghost"
              style={{ color: "white" }}
              onClick={() =>
                setCurrentTime(moment(currentTime.add(-1, "days")))
              }
              icon={<DoubleLeftOutlined />}
            />
            <DatePicker
              className="detail-page-one-date"
              // showTime={{ format: 'HH:mm' }}
              // format="YYYY-MM-DD HH:mm"
              format="MM/DD/YYYY"
              onChange={(value) => setCurrentTime(value)}
              value={currentTime === "" ? moment() : currentTime}
            />
            <Button
              type="ghost"
              style={{ color: "white" }}
              onClick={() => setCurrentTime(moment(currentTime.add(1, "days")))}
              icon={<DoubleRightOutlined />}
            />
          </div>
        </Col>
      </Row>
      <div>
        <AlertSettingContainer sensorInfo={sensorInfo} />
      </div>
    </div>
  );
}

export default DetailPage;
