import { Col, Row, message } from "antd";
import AmbientPaintWidget from "components/AmbientPaintWidget/AmbientPaintWidget";
import CurrentPartProcessingTimeWidget from "components/CurrentPartProcessingTimeWidget/CurrentPartProcessingTimeWidget";
import GoodBadModal from "components/GoodBadModal/GoodBadModal";
import GoodBadWidget from "components/GoodBadWidget/GoodBadWidget";
import UsageWidget from "components/UsageWidget/UsageWidget";
import InputModeModal from "layouts/InputModeModal/InputModeModal";
import PartIDInputLayout from "layouts/PartIDInputLayout/PartIDInputLayout";
import PartIDModal from "layouts/PartIDModal/PartIDModal";
import UserInfoLayout from "layouts/UserInfoLayout/UserInfoLayout";
import moment from "moment";
import { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { isSpinning, setAppDataStore } from "redux/actions/appActions";
import {
  apiCallForGetPaintStation,
  apiCallForGetTankTemperature,
  apiCallForPostPaintStation,
} from "services/apiCall";
import {
  appData,
  secondToTime,
  timeToSecond,
  userData,
  factoryData,
} from "services/global";
import "./PaintStationLayout.css";
import TextInputArrayModal from "components/TextInputArrayModal/TextInputArrayModal";

const PaintStationLayout = (props) => {
  const dispatch = useDispatch();
  const { appDataStore } = useSelector((x) => x.appDataStore);
  const [showInputMode, setShowInputMode] = useState(false);
  const [showPartID, setShowPartID] = useState(false);
  const [partID, setPartID] = useState("");
  const [processingTime, setProcessingTime] = useState("00:00:00");
  const [showInputModal, setShowInputModal] = useState(false);
  const titles = [
    "Temp1",
    "Humidity1",
    "Dew Point1",
    "Temp2",
    "Humidit2",
    "Dew Point2",
  ];
  const [values, setValues] = useState([0.0, 0.0, 0.0, 0.0, 0.0, 0.0]);

  const [before, setBefore] = useState(0);
  const [after, setAfter] = useState(0);
  const [used, setUsed] = useState(0);

  const [goodParts, setGoodParts] = useState("0");
  const [badParts, setBadParts] = useState("0");
  const [showGoodBadModal, setShowGoodBadModal] = useState(false);

  const [intervalId, setIntervalID] = useState(null);
  const [tick, setTick] = useState(false);
  const timer = () => setTick((t) => !t);
  const [isStarted, setIsStarted] = useState(false);

  let timeLastTemperatureData = 0;

  useEffect(() => {
    setGoodParts(appData.shiftGoodParts);
    setBadParts(appData.shiftBadParts);
  }, [appDataStore.shiftGoodParts, appDataStore.shiftBadParts]);

  useEffect(() => {
    setUsed(before - after);
  }, [before, after]);

  const updateGoodParts = (value) => {
    appData.shiftGoodParts = parseInt(value);
    dispatch(setAppDataStore(appData));
  };

  const updateBadParts = (value) => {
    appData.shiftBadParts = parseInt(value);
    dispatch(setAppDataStore(appData));
  };

  useEffect(() => {
    getTemperatureData();
    clearInterval(intervalId);
    setIntervalID(setInterval(timer, 1000));
    return () => {
      clearInterval(intervalId);
    };
  }, []);

  useEffect(() => {
    if (timeLastTemperatureData > 3 * 60 * 1000) {
      timeLastTemperatureData = 0;
      getTemperatureData();
    } else {
      timeLastTemperatureData += 1000;
    }
    if (isStarted) {
      setProcessingTime(
        moment(processingTime, "HH:mm:ss").add(1, "seconds").format("HH:mm:ss")
      );
    }
  }, [tick]);

  const onPartId = (value) => {
    setPartID(value);
    resetTimers();
    getPartInfo(value);
  };

  const resetTimers = () => {
    setBefore("");
    setAfter("");
    setUsed("");
    setProcessingTime("00:00:00");
    setIsStarted(false);
  };

  const onStartClick = () => {
    if (!isStarted) {
      setIsStarted(true);
    }
  };

  const onStopClick = () => {
    setIsStarted(false);
    reportTimes();
  };

  const getPartInfo = (value) => {
    if (value === "") {
      return;
    }
    let param = {
      customer_id: factoryData.accountId,
      part_id: value,
    };
    dispatch(isSpinning(true));
    apiCallForGetPaintStation(param)
      .then((res) => {
        dispatch(isSpinning(false));
        let stationInfo = res["station"];
        setProcessingTime(secondToTime(stationInfo["processing_time"]));
        setBefore(stationInfo["bitu_wt_before"]);
        setAfter(stationInfo["bitu_wt_after"]);
        setUsed(stationInfo["bitu_used"]);
      })
      .catch((err) => {
        dispatch(isSpinning(false));
        message.error("Fail to get times!");
      });
  };

  const getTemperatureData = () => {
    let param = {
      customer_id: factoryData.accountId,
    };
    dispatch(isSpinning(true));
    apiCallForGetTankTemperature(param)
      .then((res) => {
        dispatch(isSpinning(false));
        let sensors = res["sensors"];
        sensors.map((sensor) => {
          let sensorName = sensor["sensor_name"];
          let newValues = [];
          if (sensorName === "Paint Booth - Internal") {
            newValues.push(sensor["value1"]);
            newValues.push(sensor["value2"]);
            newValues.push(sensor["value3"]);
          }

          if (sensorName === "Paint Booth - Ambient") {
            newValues.push(sensor["value1"]);
            newValues.push(sensor["value2"]);
            newValues.push(sensor["value3"]);
          }
          setValues(newValues);
        });
      })
      .catch((err) => {
        dispatch(isSpinning(false));
        message.error("Fail to get times!");
      });
  };

  const reportTimes = () => {
    if (partID === "") {
      message.warn("Please input Part ID to save times.");
      return;
    }

    if (factoryData.accountId === "") {
      message.warn("Please login with customer id");
      return;
    }

    if (appData.machineName === "") {
      message.warn("Please input machine name in settings page");
      return;
    }

    let param = {
      customer_id: factoryData.accountId,
      machine_id: appData.machineName,
      created_at: moment().format("YYYY-MM-DD HH:mm:ss") + ".000000",
      timestamp: Date.now(),
      operator: userData.username_full,
      part_id: partID,
      date: moment().format("MM/DD/YYYY"),
      time: moment().format("HH:mm:ss"),
      processing_time: timeToSecond(processingTime),
      bitu_wt_before: before,
      bitu_wt_after: after,
      bitu_used: used,
      ambient_temp: values[0],
      ambient_humidity: values[1],
      ambient_dewpoint: values[2],
      paintbooth_temp: values[3],
      paintbooth_humidity: values[4],
      paintbooth_dewpoint: values[5],
    };

    dispatch(isSpinning(true));
    apiCallForPostPaintStation(param)
      .then((res) => {
        dispatch(isSpinning(false));
        message.success("Success to report times!");
      })
      .catch((err) => {
        dispatch(isSpinning(false));
        message.error("Fail to report times!");
      });
  };

  return (
    <div className="paint-station-layout">
      <InputModeModal
        title={"Please Select Input Mode"}
        showModal={showInputMode}
        setShowModal={setShowInputMode}
        setShowInput={setShowPartID}
      />
      <PartIDModal
        showModal={showPartID}
        setShowModal={setShowPartID}
        partID={partID}
        setPartID={onPartId}
      />
      <GoodBadModal
        goodParts={goodParts}
        badParts={badParts}
        setGoodParts={updateGoodParts}
        setBadParts={updateBadParts}
        showModal={showGoodBadModal}
        setShowModal={setShowGoodBadModal}
      />
      <TextInputArrayModal
        showModal={showInputModal}
        setShowModal={setShowInputModal}
        values={values}
        setValues={setValues}
        titles={titles}
        title={"Temperature & Humidity & Dew Point"}
        subTitle={
          "Please input Temperatures(°F), Humidities(%) and Dew Points(°F)."
        }
        span={8}
      />
      <div className="paint-station-title">{"PAINT STATION"}</div>
      <div className="paint-station-part-id">
        <PartIDInputLayout
          partID={partID}
          setPartID={onPartId}
          setShowInputMode={setShowInputMode}
        />
      </div>
      <div className="paint-station-user-info">
        <UserInfoLayout />
      </div>
      <div className="paint-station-content">
        <Row>
          <Col span={12}>
            <CurrentPartProcessingTimeWidget
              time={processingTime}
              setTime={setProcessingTime}
              onStartClick={onStartClick}
              onStopClick={onStopClick}
            />
            <Row style={{ marginTop: "20px" }}>
              <Col
                span={12}
                onClick={() => {
                  setShowInputModal(true);
                }}
              >
                <AmbientPaintWidget
                  title={"Ambient"}
                  temp={values[0]}
                  hum={values[1]}
                  dew={values[2]}
                />
              </Col>
              <Col
                span={12}
                onClick={() => {
                  setShowInputModal(true);
                }}
              >
                <AmbientPaintWidget
                  title={"Paint Booth"}
                  temp={values[3]}
                  hum={values[4]}
                  dew={values[5]}
                />
              </Col>
            </Row>
          </Col>
          <Col span={12} style={{ paddingLeft: "5px" }}>
            <UsageWidget
              title={"Bitumen Usage"}
              unit={"oz"}
              before={before}
              setBefore={setBefore}
              after={after}
              setAfter={setAfter}
              used={used}
              setUsed={setUsed}
            />
            <Row style={{ marginTop: "20px" }}>
              <Col span={12}>
                <GoodBadWidget
                  type={0}
                  value={goodParts}
                  setValue={updateGoodParts}
                  setShowModal={setShowGoodBadModal}
                />
              </Col>
              <Col span={12}>
                <GoodBadWidget
                  type={1}
                  value={badParts}
                  setValue={updateBadParts}
                  setShowModal={setShowGoodBadModal}
                />
              </Col>
            </Row>
          </Col>
        </Row>
      </div>
    </div>
  );
};

export default PaintStationLayout;
