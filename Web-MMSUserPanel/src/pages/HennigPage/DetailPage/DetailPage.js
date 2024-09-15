import { Button, Col, Popconfirm, Row, Select, Spin } from "antd";
import { useEffect, useState } from "react";
import { useSelector } from "react-redux";
import "./DetailPage.css";

import {
  ArrowLeftOutlined,
  CloseOutlined,
  PrinterOutlined,
  SettingOutlined,
} from "@ant-design/icons";

import moment from "moment";
import HennigTrendChart from "../../../components/HennigPageWidgets/HennigTrendChart/HennigTrendChart";
import { GetCustomerCurrentTime } from "../../../services/common/cnc_apis";
import { getDetailedHennigData } from "../../../services/common/hennig_apis";
import lang from "../../../services/lang";
import DetailPageValueContainer from "./DetailPageValueContainer/DetailPageValueContainer";
import ExportCSVDialog from "./ExportCSVDialog/ExportCSVDialog";

import HennigUpdateModal from "../HennigUpdateModal/HennigUpdateModal";
import sensorDefaultImage from "../../../assets/images/default_sensor.jpg";

const { Option } = Select;

function DetailPage(props) {
  const { langIndex } = useSelector((x) => x.app);
  const {
    hennigInfo,
    selectedSensorId,
    setSelectedSensorId,
    timezone,
    onDeleteHennig,
  } = props;

  const [tick, setTick] = useState(true);
  const [intervalId, setIntervalID] = useState(null);
  const [isReading, setIsReading] = useState(false);
  const [isShowingExportModal, setShowingExportModal] = useState(false);
  const intervalTime = 60000;

  const [hennigData, setHennigData] = useState([]);
  const [valueList1, setValueList1] = useState([]);
  const [valueList2, setValueList2] = useState([]);
  const [valueList3, setValueList3] = useState([]);
  const [valueList4, setValueList4] = useState([]);
  const [valueList5, setValueList5] = useState([]);
  const [valueList6, setValueList6] = useState([]);
  const [valueList7, setValueList7] = useState([]);
  const [valueList8, setValueList8] = useState([]);
  const [isShowUpdateModal, setIsShowUpdateModal] = useState(false);
  const [days, setDays] = useState(30);
  const [readingTime, setReadingTime] = useState("");
  const [status, setStatus] = useState("");

  const daysList = [
    1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21,
    22, 23, 24, 25, 26, 27, 28, 29, 30,
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

  console.log(hennigInfo);
  
  let value1Info = hennigInfo.for_value1.split(",");
  let value1Label = value1Info[0];
  let value1Unit = "";
  if (value1Info.length > 1) {
    value1Unit = " " + value1Info[1];
  }
  let value1Rate = 1.0;
  if (value1Info.length > 2) {
    value1Rate = parseFloat(value1Info[2]);
  }

  let value2Info = hennigInfo.for_value2.split(",");
  let value2Label = value2Info[0];
  let value2Unit = "";
  if (value2Info.length > 1) {
    value2Unit = " " + value2Info[1];
  }
  let value2Rate = 1.0;
  if (value2Info.length > 2) {
    value2Rate = parseFloat(value2Info[2]);
  }

  let value3Info = hennigInfo.for_value3.split(",");
  let value3Label = value3Info[0];
  let value3Unit = "";
  if (value3Info.length > 1) {
    value3Unit = " " + value3Info[1];
  }
  let value3Rate = 1.0;
  if (value3Info.length > 2) {
    value3Rate = parseFloat(value3Info[2]);
  }

  let value4Info = hennigInfo.for_value4.split(",");
  let value4Label = value4Info[0];
  let value4Unit = "";
  if (value4Info.length > 1) {
    value4Unit = " " + value4Info[1];
  }
  let value4Rate = 1.0;
  if (value4Info.length > 2) {
    value4Rate = parseFloat(value4Info[2]);
  }

  let value5Info = hennigInfo.for_value5.split(",");
  let value5Label = value5Info[0];
  let value5Unit = "";
  if (value5Info.length > 1) {
    value5Unit = " " + value5Info[1];
  }
  let value5Rate = 1.0;
  if (value5Info.length > 2) {
    value5Rate = parseFloat(value5Info[2]);
  }

  let value6Info = hennigInfo.for_value6.split(",");
  let value6Label = value6Info[0];
  let value6Unit = "";
  if (value6Info.length > 1) {
    value6Unit = " " + value6Info[1];
  }
  let value6Rate = 1.0;
  if (value6Info.length > 2) {
    value6Rate = parseFloat(value6Info[2]);
  }

  let value7Info = hennigInfo.for_value7.split(",");
  let value7Label = value7Info[0];
  let value7Unit = "";
  if (value7Info.length > 1) {
    value7Unit = " " + value7Info[1];
  }
  let value7Rate = 1.0;
  if (value7Info.length > 2) {
    value7Rate = parseFloat(value7Info[2]);
  }

  let value8Info = hennigInfo.for_value8.split(",");
  let value8Label = value8Info[0];
  let value8Unit = "";
  if (value8Info.length > 1) {
    value8Unit = " " + value8Info[1];
  }
  let value8Rate = 1.0;
  if (value8Info.length > 2) {
    value8Rate = parseFloat(value8Info[2]);
  }

  useEffect(() => {
    try {
      clearInterval(intervalId);
    } catch (_) {}

    setIntervalID(setInterval(timer, intervalTime));
    return () => {
      clearInterval(intervalId);
    };
  }, []);

  useEffect(() => {
    if (!isReading) {
      var curTime = GetCustomerCurrentTime(timezone);
      const startTime = moment(curTime)
        .add(-days, "days")
        .set({ hour: 0, minute: 0, second: 0 });
      const endTime = moment(curTime);
      getDetailedHennigData(
        hennigInfo.sensor_id,
        startTime.format("YYYY-MM-DD HH:mm:ss"),
        endTime.format("YYYY-MM-DD HH:mm:ss"),
        (res) => {
          if (res === null) return;
          setHennigData(res);
        }
      );
    }
  }, [tick]);

  useEffect(() => {
    setIsReading(true);
    var curTime = GetCustomerCurrentTime(timezone);
    const startTime = moment(curTime)
      .add(-days, "days")
      .set({ hour: 0, minute: 0, second: 0 });
    const endTime = moment(curTime);
    getDetailedHennigData(
      hennigInfo.sensor_id,
      startTime.format("YYYY-MM-DD HH:mm:ss"),
      endTime.format("YYYY-MM-DD HH:mm:ss"),
      (res) => {
        setIsReading(false);
        if (res === null) return;
        setHennigData(res);
      }
    );
  }, [days]);

  useEffect(() => {
    if (hennigData == undefined || hennigData["sensor_values"] == null) {
      return;
    }

    if (
      hennigData["current_value"] != null &&
      hennigData["current_value"]["reading_time"] != null
    ) {
      var ownReadTime = hennigData["current_value"]["reading_time"];
      ownReadTime = moment(ownReadTime);
      setReadingTime(ownReadTime.format("YYYY-MM-DD HH:mm"));

      var customerTime = GetCustomerCurrentTime(timezone);

      customerTime = moment(customerTime);

      var duration = moment.duration(customerTime.diff(ownReadTime));
      var minutes = duration.asMinutes();
      if (minutes <= 30) {
        setStatus(lang(langIndex, "iiot_connectiongood"));
      } else {
        setStatus(lang(langIndex, "iiot_connectionlost"));
      }
    }

    var curTime = GetCustomerCurrentTime(timezone);
    const startTime = moment(curTime)
      .add(-days, "days")
      .set({ hour: 0, minute: 0, second: 0 });
    const endTime = moment(curTime);

    const timeList = [];
    while (startTime <= endTime) {
      timeList.push(startTime.format("YYYY-MM-DD HH:mm"));
      startTime.add(1, "hours");
    }

    let chartData1 = Array();
    let chartData2 = Array();
    let chartData3 = Array();
    let chartData4 = Array();
    let chartData5 = Array();
    let chartData6 = Array();
    let chartData7 = Array();
    let chartData8 = Array();

    timeList.forEach((_time) => {
      const info = hennigData["sensor_values"].find(
        (x) =>
          x.reading_time.substring(0, 14) ===
          toCalifonia(_time).format("YYYY-MM-DD HH:mm").substring(0, 14)
      );
      chartData1.push([
        _time,
        info == undefined ? 0 : parseFloat(info.value1) * value1Rate,
      ]);
      chartData2.push([
        _time,
        info == undefined ? 0 : parseFloat(info.value2) * value2Rate,
      ]);
      chartData3.push([
        _time,
        info == undefined ? 0 : parseFloat(info.value3) * value3Rate,
      ]);
      chartData4.push([
        _time,
        info == undefined ? 0 : parseFloat(info.value4) * value4Rate,
      ]);
      chartData5.push([
        _time,
        info == undefined ? 0 : parseFloat(info.value5) * value5Rate,
      ]);
      chartData6.push([
        _time,
        info == undefined ? 0 : parseFloat(info.value6) * value6Rate,
      ]);
      chartData7.push([
        _time,
        info == undefined ? 0 : parseFloat(info.value7) * value7Rate,
      ]);
      chartData8.push([
        _time,
        info == undefined ? 0 : parseFloat(info.value8) * value8Rate,
      ]);
    });
    setValueList1(chartData1);
    setValueList2(chartData2);
    setValueList3(chartData3);
    setValueList4(chartData4);
    setValueList5(chartData5);
    setValueList6(chartData6);
    setValueList7(chartData7);
    setValueList8(chartData8);
  }, [hennigData]);

  const exportToCSV = () => {
    setShowingExportModal(true);
  };

  let value1 = 0;
  let value2 = 0;
  let value3 = 0;
  let value4 = 0;
  let value5 = 0;
  let value6 = 0;
  let value7 = 0;
  let value8 = 0;

  if (hennigData != null && hennigData["current_value"] != null) {
    let currentValues = hennigData["current_value"];
    value1 = Math.round(currentValues["value1"] * value1Rate * 100) / 100;
    value2 = Math.round(currentValues["value2"] * value2Rate * 100) / 100;
    value3 = Math.round(currentValues["value3"] * value3Rate * 100) / 100;
    value4 = Math.round(currentValues["value4"] * value4Rate * 100) / 100;
    value5 = Math.round(currentValues["value5"] * value5Rate * 100) / 100;
    value6 = Math.round(currentValues["value6"] * value6Rate * 100) / 100;
    value7 = Math.round(currentValues["value7"] * value7Rate * 100) / 100;
    value8 = Math.round(currentValues["value8"] * value8Rate * 100) / 100;
  }

  return (
    <div className="hennig-detail-page-container-style">
      <ExportCSVDialog
        isShowingExportModal={isShowingExportModal}
        setShowingExportModal={setShowingExportModal}
        sensor_id={hennigInfo.sensor_id}
      />

      {isShowUpdateModal && (
        <HennigUpdateModal
          hennigInfo={hennigInfo}
          setIsShowUpdateModal={setIsShowUpdateModal}
          isShowUpdateModal={isShowUpdateModal}
        />
      )}

      <Row>
        <Col flex={"50px"}>
          <ArrowLeftOutlined
            className="hennig-backward-button"
            onClick={() => setSelectedSensorId("")}
          />
        </Col>
        <Col flex={"auto"}>
          <span className="hennig-detail-one-hennig-name">
            {hennigInfo.sensor_name}
          </span>
        </Col>
        <Col className="delete-hennig-button">
          <div className="user-setting-button-container">
            <Button
              ghost
              onClick={() => {
                exportToCSV();
              }}
            >
              <PrinterOutlined />
              {lang(langIndex, "iiot_exporttocsv")}
            </Button>
            <span className="backspace" />
            <Button
              ghost
              onClick={() => {
                setIsShowUpdateModal(true);
              }}
            >
              <SettingOutlined />
              {lang(langIndex, "iiot_updatesetting")}
            </Button>
            <span className="backspace" />
            <Popconfirm
              onConfirm={() => {
                onDeleteHennig(hennigInfo.sensor_id);
                setSelectedSensorId("");
              }}
              okText="Yes"
              cancelText="No"
              title={lang(langIndex, "jobentry_suretodelete")}
              placement="bottomLeft"
              className="hennig-delete-popup"
            >
              <Button ghost onClick={() => {}}>
                <CloseOutlined /> Delete Sensor
              </Button>
            </Popconfirm>
          </div>
        </Col>
      </Row>
      {/* <div className="hennig-detail-current-info">{readingTime}</div> */}
      <Row>
        <Col span={24} className="hennig-detail-one-hennig-container">
          <Row align="middle">
            <Col>
              <img
                alt=""
                className="hennig-detail-page-image-style"
                src={
                  hennigInfo.image === "" || hennigInfo.image === null
                    ? sensorDefaultImage
                    : hennigInfo.image
                }
              />
            </Col>

            <Col
              flex={"auto"}
              className="hennig-detail-current-value-container"
            >
              <Row>
                <Col
                  className="hennig-detail-current-value hennig-detail-current-value-border-right"
                  span={12}
                >
                  {value1Label}
                </Col>
                <Col className="hennig-detail-current-value" span={12}>
                  {value1} {value1Unit}
                </Col>
              </Row>

              <Row className="hennig-detail-current-value-border-top">
                <Col
                  className="hennig-detail-current-value hennig-detail-current-value-border-right"
                  span={12}
                >
                  {value2Label}
                </Col>
                <Col className="hennig-detail-current-value" span={12}>
                  {value2} {value2Unit}
                </Col>
              </Row>

              {value3Label !== "" && (
                <Row className="hennig-detail-current-value-border-top">
                  <Col
                    className="hennig-detail-current-value hennig-detail-current-value-border-right"
                    span={12}
                  >
                    {value3Label}
                  </Col>
                  <Col className="hennig-detail-current-value" span={12}>
                    {value3} {value3Unit}
                  </Col>
                </Row>
              )}

              {value4Label !== "" && (
                <Row className="hennig-detail-current-value-border-top">
                  <Col
                    className="hennig-detail-current-value hennig-detail-current-value-border-right"
                    span={12}
                  >
                    {value4Label}
                  </Col>
                  <Col className="hennig-detail-current-value" span={12}>
                    {value4} {value4Unit}
                  </Col>
                </Row>
              )}

              {value5Label !== "" && (
                <Row className="hennig-detail-current-value-border-top">
                  <Col
                    className="hennig-detail-current-value hennig-detail-current-value-border-right"
                    span={12}
                  >
                    {value5Label}
                  </Col>
                  <Col className="hennig-detail-current-value" span={12}>
                    {selectedSensorId == "5002"
                      ? value5 == 0
                        ? "No Alarm"
                        : "Low Pressure Alarm"
                      : `${value5} ${value5Unit}`}
                  </Col>
                </Row>
              )}

              {value6Label !== "" && (
                <Row className="hennig-detail-current-value-border-top">
                  <Col
                    className="hennig-detail-current-value hennig-detail-current-value-border-right"
                    span={12}
                  >
                    {value6Label}
                  </Col>
                  <Col className="hennig-detail-current-value" span={12}>
                    {selectedSensorId == "5002"
                      ? value6 == 0
                        ? "No Alarm"
                        : "Alarm"
                      : `${value6} ${value6Unit}`}
                  </Col>
                </Row>
              )}

              {value7Label !== "" && (
                <Row className="hennig-detail-current-value-border-top">
                  <Col
                    className="hennig-detail-current-value hennig-detail-current-value-border-right"
                    span={12}
                  >
                    {value7Label}
                  </Col>
                  <Col className="hennig-detail-current-value" span={12}>
                    {value7} {value7Unit}
                  </Col>
                </Row>
              )}

              {value8Label !== "" && (
                <Row className="hennig-detail-current-value-border-top">
                  <Col
                    className="hennig-detail-current-value hennig-detail-current-value-border-right"
                    span={12}
                  >
                    {value8Label}
                  </Col>
                  <Col className="hennig-detail-current-value" span={12}>
                    {value8} {value8Unit}
                  </Col>
                </Row>
              )}
            </Col>
          </Row>
        </Col>
      </Row>

      <div className="detail-page-trend-container">
        <div className="hennig-trend-chart-title">
          Trend (
          <Select
            className="selector"
            dropdownClassName="selector-dropdown"
            style={{ width: 60, marginLeft: 5, marginRight: 5 }}
            value={days}
            onChange={(e) => setDays(e)}
          >
            {daysList.map((x) => {
              return (
                <Option
                  className="page-changer-item"
                  key={`day-${x}`}
                  value={x}
                >
                  {x}
                </Option>
              );
            })}
          </Select>
          Days)
        </div>

        <div className="hennig-detail-page-trend-chart">
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
            <Row>
              {value1Unit !== "" && (
                <Col span={12}>
                  <HennigTrendChart
                    hennigInfo={hennigInfo}
                    valueList={valueList1}
                    chartColor={["#da1c1c"]}
                    yAxisLabel={value1Label}
                    yAxisUnit={value1Unit}
                    screenSize={app_info.screenSize}
                  />
                </Col>
              )}

              {value2Unit !== "" && (
                <Col span={12}>
                  <HennigTrendChart
                    hennigInfo={hennigInfo}
                    valueList={valueList2}
                    chartColor={["#1093f1"]}
                    yAxisLabel={value2Label}
                    yAxisUnit={value2Unit}
                    screenSize={app_info.screenSize}
                  />
                </Col>
              )}

              {value3Unit !== "" && (
                <Col span={12}>
                  <HennigTrendChart
                    hennigInfo={hennigInfo}
                    valueList={valueList3}
                    chartColor={["#0fa30f"]}
                    yAxisLabel={value3Label}
                    yAxisUnit={value3Unit}
                    screenSize={app_info.screenSize}
                  />
                </Col>
              )}
              {value4Unit !== "" && (
                <Col span={12}>
                  <HennigTrendChart
                    hennigInfo={hennigInfo}
                    valueList={valueList4}
                    chartColor={["#02cabe"]}
                    yAxisLabel={value4Label}
                    yAxisUnit={value4Unit}
                    screenSize={app_info.screenSize}
                  />
                </Col>
              )}
              {value5Unit !== "" && (
                <Col span={12}>
                  <HennigTrendChart
                    hennigInfo={hennigInfo}
                    valueList={valueList5}
                    chartColor={["#ca02be"]}
                    yAxisLabel={value5Label}
                    yAxisUnit={value5Unit}
                    screenSize={app_info.screenSize}
                  />
                </Col>
              )}
              {value6Unit !== "" && (
                <Col span={12}>
                  <HennigTrendChart
                    hennigInfo={hennigInfo}
                    valueList={valueList6}
                    chartColor={["#02beca"]}
                    yAxisLabel={value6Label}
                    yAxisUnit={value6Unit}
                    screenSize={app_info.screenSize}
                  />
                </Col>
              )}

              {value7Unit !== "" && (
                <Col span={12}>
                  <HennigTrendChart
                    hennigInfo={hennigInfo}
                    valueList={valueList7}
                    chartColor={["#02beca"]}
                    yAxisLabel={value7Label}
                    yAxisUnit={value7Unit}
                    screenSize={app_info.screenSize}
                  />
                </Col>
              )}

              {value8Unit !== "" && (
                <Col span={12}>
                  <HennigTrendChart
                    hennigInfo={hennigInfo}
                    valueList={valueList8}
                    chartColor={["#02beca"]}
                    yAxisLabel={value8Label}
                    yAxisUnit={value8Unit}
                    screenSize={app_info.screenSize}
                  />
                </Col>
              )}
            </Row>
          )}
        </div>
      </div>
    </div>
  );
}

export default DetailPage;
