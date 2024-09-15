import { Col, Row, message } from "antd";
import InputModeModal from "layouts/InputModeModal/InputModeModal";
import PartIDInputLayout from "layouts/PartIDInputLayout/PartIDInputLayout";
import PartIDModal from "layouts/PartIDModal/PartIDModal";
import moment from "moment";
import { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { isSpinning } from "redux/actions/appActions";
import {
  apiCallForGetTankTime,
  apiCallForPostTankTime,
} from "services/apiCall";
import {
  appData,
  secondToTime,
  timeToSecond,
  userData,
  factoryData,
} from "services/global";
import TankTimeWidget from "../../components/TankTimeWidget/TankTimeWidget";
import "./TimeLoggerLayout.css";
import TextInputArrayModal from "components/TextInputArrayModal/TextInputArrayModal";
import RecordPhosphateTestInfo from "components/RecordPhosphateTestInfoModal/RecordPhosphateTestInfoModal";

const TimeLoggerLayout = (props) => {
  const defaultTimes = [
    "00:00:00",
    "00:00:00",
    "00:00:00",
    "00:00:00",
    "00:00:00",
    "00:00:00",
    "00:00:00",
    "00:00:00",
    "00:00:00",
  ];

  const titles = [
    "Oven",
    "Tank1",
    "Tank2",
    "Tank3",
    "Tank4",
    "Tank5",
    "Tank6",
    "Tank7",
    "Tank8",
  ];

  const tankTitles = [
    "Tank1",
    "Tank2",
    "Tank3",
    "Tank4",
    "Tank5",
    "Tank6",
    "Tank7",
    "Tank8",
  ];

  const [temps, setTemps] = useState([
    0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
  ]);

  const [tankTemps, setTankTemps] = useState([
    0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0,
  ]);

  const dispatch = useDispatch();

  const [showInputMode, setShowInputMode] = useState(false);
  const [showPartID, setShowPartID] = useState(false);
  const [showTextInputArrayModal, setShowTextInputArrayModal] = useState(false);
  const [showRecordPhosphateTestInfo, setShowRecordPhosphateTestInfo] =
    useState(false);

  const [rmrot, setRmrot] = useState("");
  const [partID, setPartID] = useState("");
  const [times, setTimes] = useState(defaultTimes);

  const [intervalId, setIntervalID] = useState(null);
  const [tick, setTick] = useState(false);
  const timer = () => setTick((t) => !t);

  const [selectedTankIndex, setSelectedTankIndex] = useState(1);

  const [isStarted, setIsStarted] = useState(false);

  useEffect(() => {
    return () => {
      clearInterval(intervalId);
    };
  }, []);

  useEffect(() => {
    if (isStarted) {
      let tempTimes = [...times];
      tempTimes[selectedTankIndex] = moment(
        times[selectedTankIndex],
        "HH:mm:ss"
      )
        .add(1, "seconds")
        .format("HH:mm:ss");
      setTimes(tempTimes);
    }
  }, [tick]);

  const updateTankTemps = (values) => {
    let newValues = [];
    newValues = [temps[0], ...values];
    setTemps(newValues);
    setTankTemps(values);
  };

  const onStartClick = () => {
    if (!isStarted) {
      setIntervalID(setInterval(timer, 1000));
      setIsStarted(true);
    }
  };

  const onStopClick = () => {
    clearInterval(intervalId);
    setIsStarted(false);
    if (selectedTankIndex === 0) {
      reportTimes();
    }
  };

  const reportTimes = () => {
    if (partID === "") {
      message.warn("Please input Part ID to save times.");
      return;
    }

    if (rmrot === "") {
      message.warn("Please input RM LOT# to save times.");
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

    console.log("factoryData=>", factoryData);
    console.log("appData=>", appData);

    let param = {
      customer_id: factoryData.accountId,
      machine_id: appData.machineName,
      created_at: moment().format("YYYY-MM-DD HH:mm:ss") + ".000000",
      timestamp: Date.now(),
      part_id: partID,
      date: moment().format("MM/DD/YYYY"),
      time: moment().format("HH:mm:ss"),
      ttime1: timeToSecond(times[1]),
      ttemp1: temps[1],
      ttime2: timeToSecond(times[2]),
      ttemp2: temps[2],
      ttime3: timeToSecond(times[3]),
      ttemp3: temps[3],
      ttime4: timeToSecond(times[4]),
      ttemp4: temps[4],
      ttime5: timeToSecond(times[5]),
      ttemp5: temps[5],
      ttime6: timeToSecond(times[6]),
      ttemp6: temps[6],
      ttime7: timeToSecond(times[7]),
      ttemp7: temps[7],
      ttime8: timeToSecond(times[8]),
      ttemp8: temps[8],
      toven: timeToSecond(times[0]),
      operator: userData.username_full,
      rm_lot: rmrot,
    };

    dispatch(isSpinning(true));
    apiCallForPostTankTime(param)
      .then((res) => {
        dispatch(isSpinning(false));
        message.success("Success to report times!");
      })
      .catch((err) => {
        dispatch(isSpinning(false));
        message.error("Fail to report times!");
      });
  };

  const onPartId = (value) => {
    setPartID(value);
    resetTimers();
    getPartInfo(value);
  };

  const resetTimers = () => {
    setRmrot("");
    setSelectedTankIndex(1);
    setTimes(defaultTimes);
    setIsStarted(false);
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
    apiCallForGetTankTime(param)
      .then((res) => {
        dispatch(isSpinning(false));
        let tankTimes = res["tank_time"];
        setTimes([
          secondToTime(tankTimes["toven"]),
          secondToTime(tankTimes["ttime1"]),
          secondToTime(tankTimes["ttime2"]),
          secondToTime(tankTimes["ttime3"]),
          secondToTime(tankTimes["ttime4"]),
          secondToTime(tankTimes["ttime5"]),
          secondToTime(tankTimes["ttime6"]),
          secondToTime(tankTimes["ttime7"]),
          secondToTime(tankTimes["ttime8"]),
        ]);
        setRmrot(tankTimes["rm_lot"]);
      })
      .catch((err) => {
        dispatch(isSpinning(false));
        //message.error("Fail to get times!");
      });
  };

  return (
    <div className="time-logger-layout">
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
      <TextInputArrayModal
        showModal={showTextInputArrayModal}
        setShowModal={setShowTextInputArrayModal}
        values={tankTemps}
        setValues={updateTankTemps}
        titles={tankTitles}
        title={"Temperature"}
        subTitle={"Please Input Tank Temperature(°F)"}
        span={6}
      />
      <RecordPhosphateTestInfo
        showModal={showRecordPhosphateTestInfo}
        setShowModal={setShowRecordPhosphateTestInfo}
        partID={partID}
      />
      <Row align={"middle"} className="time-logger-top-layout">
        <Col
          flex={"170px"}
          className="time-logger-top-record-test-info"
          onClick={() => {
            setShowRecordPhosphateTestInfo(true);
          }}
        >
          {"Record Test Info"}
        </Col>
        <Col flex={"auto"}>
          <Row>
            <Col span={8}>
              <PartIDInputLayout
                partID={partID}
                setPartID={onPartId}
                setShowInputMode={setShowInputMode}
              />
            </Col>
            <Col span={8}>
              <Row align={"middle"} className="time-logger-top-rmrot">
                <Col span={24}>
                  <div className="time-logger-top-rmrot-left">{"RM LOT#"}</div>
                  <div className="time-logger-top-rmrot-right">
                    <input
                      className="text-input"
                      value={rmrot}
                      onChange={(e) => setRmrot(e.target.value)}
                      style={{ outlineStyle: "none" }}
                    />
                  </div>
                </Col>
              </Row>
            </Col>
            <Col span={8}>
              <Row align={"middle"} className="time-logger-top-oven">
                <Col flex={"80px"}>{titles[0]}</Col>
                <Col
                  flex={"auto"}
                  onClick={() => {
                    setSelectedTankIndex(0);
                  }}
                  style={{
                    color: selectedTankIndex === 0 ? "red" : "white",
                    cursor: "default",
                  }}
                >
                  {times[0]}
                </Col>
              </Row>
            </Col>
          </Row>
        </Col>
      </Row>
      <div className="time-logger-content">
        <Row className="time-logger-tanktime-row">
          <Col span={12} style={{ width: "100%", height: "100%" }}>
            <TankTimeWidget
              title={titles[1]}
              times={times}
              temps={temps}
              setTimes={setTimes}
              selectedTankIndex={selectedTankIndex}
              setSelectedTankIndex={setSelectedTankIndex}
              setShowTemperatureModal={setShowTextInputArrayModal}
              index={1}
            />
          </Col>

          <Col span={12} style={{ width: "100%", height: "100%" }}>
            <TankTimeWidget
              title={titles[5]}
              times={times}
              temps={temps}
              setTimes={setTimes}
              selectedTankIndex={selectedTankIndex}
              setSelectedTankIndex={setSelectedTankIndex}
              setShowTemperatureModal={setShowTextInputArrayModal}
              index={5}
            />
          </Col>
        </Row>

        <Row className="time-logger-tanktime-row">
          <Col span={12} style={{ width: "100%", height: "100%" }}>
            <TankTimeWidget
              title={titles[2]}
              times={times}
              temps={temps}
              setTimes={setTimes}
              selectedTankIndex={selectedTankIndex}
              setSelectedTankIndex={setSelectedTankIndex}
              setShowTemperatureModal={setShowTextInputArrayModal}
              index={2}
            />
          </Col>

          <Col span={12} style={{ width: "100%", height: "100%" }}>
            <TankTimeWidget
              title={titles[6]}
              times={times}
              temps={temps}
              setTimes={setTimes}
              selectedTankIndex={selectedTankIndex}
              setSelectedTankIndex={setSelectedTankIndex}
              setShowTemperatureModal={setShowTextInputArrayModal}
              index={6}
            />
          </Col>
        </Row>

        <Row className="time-logger-tanktime-row">
          <Col span={12} style={{ width: "100%", height: "100%" }}>
            <TankTimeWidget
              title={titles[3]}
              times={times}
              temps={temps}
              setTimes={setTimes}
              selectedTankIndex={selectedTankIndex}
              setSelectedTankIndex={setSelectedTankIndex}
              setShowTemperatureModal={setShowTextInputArrayModal}
              index={3}
            />
          </Col>

          <Col span={12} style={{ width: "100%", height: "100%" }}>
            <TankTimeWidget
              title={titles[7]}
              times={times}
              temps={temps}
              setTimes={setTimes}
              selectedTankIndex={selectedTankIndex}
              setSelectedTankIndex={setSelectedTankIndex}
              setShowTemperatureModal={setShowTextInputArrayModal}
              index={7}
            />
          </Col>
        </Row>

        <Row className="time-logger-tanktime-row">
          <Col span={12} style={{ width: "100%", height: "100%" }}>
            <TankTimeWidget
              title={titles[4]}
              times={times}
              temps={temps}
              setTimes={setTimes}
              selectedTankIndex={selectedTankIndex}
              setSelectedTankIndex={setSelectedTankIndex}
              setShowTemperatureModal={setShowTextInputArrayModal}
              index={4}
            />
          </Col>

          <Col span={12} style={{ width: "100%", height: "100%" }}>
            <TankTimeWidget
              title={titles[8]}
              times={times}
              temps={temps}
              setTimes={setTimes}
              selectedTankIndex={selectedTankIndex}
              setSelectedTankIndex={setSelectedTankIndex}
              setShowTemperatureModal={setShowTextInputArrayModal}
              index={8}
            />
          </Col>
        </Row>
      </div>
      <div className="time-logger-bottom">
        <Row style={{ height: "70px" }}>
          <Col span={8}>
            <div
              className="time-logger-bottom-button"
              style={{ marginRight: "5px" }}
              onClick={() => {
                onStartClick();
              }}
            >
              {"Start"}
            </div>
          </Col>
          <Col span={8} style={{ paddingLeft: "10px" }}>
            <div
              className="time-logger-bottom-button"
              onClick={() => {
                onStopClick();
              }}
            >
              {"Stop"}
            </div>
          </Col>
          <Col span={8} style={{ paddingLeft: "10px" }}>
            <div className="time-logger-bottom-button">{"Data Log"}</div>
          </Col>
        </Row>
      </div>
    </div>
  );
};

export default TimeLoggerLayout;
