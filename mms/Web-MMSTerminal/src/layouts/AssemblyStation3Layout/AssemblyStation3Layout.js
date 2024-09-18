import { Col, Radio, Row, message } from "antd";
import CurrentPartProcessingTimeWidget from "components/CurrentPartProcessingTimeWidget/CurrentPartProcessingTimeWidget";
import GoodBadModal from "components/GoodBadModal/GoodBadModal";
import GoodBadWidget from "components/GoodBadWidget/GoodBadWidget";
import InputModeModal from "layouts/InputModeModal/InputModeModal";
import PartIDInputLayout from "layouts/PartIDInputLayout/PartIDInputLayout";
import PartIDModal from "layouts/PartIDModal/PartIDModal";
import UserInfoLayout from "layouts/UserInfoLayout/UserInfoLayout";
import { useEffect, useState } from "react";
import moment from "moment";
import "./AssemblyStation3Layout.css";
import { useDispatch, useSelector } from "react-redux";
import { isSpinning, setAppDataStore } from "redux/actions/appActions";
import {
  appData,
  secondToTime,
  timeToSecond,
  userData,
  factoryData,
} from "services/global";
import {
  apiCallForGetAssembly3Station,
  apiCallForPostAssembly3Station,
} from "services/apiCall";
import TextInputArrayModal from "components/TextInputArrayModal/TextInputArrayModal";

const AssemblyStation3Layout = (props) => {
  const dispatch = useDispatch();
  const { appDataStore } = useSelector((x) => x.appDataStore);
  const [showInputMode, setShowInputMode] = useState(false);
  const [showPartID, setShowPartID] = useState(false);
  const [partID, setPartID] = useState("");

  const [goodParts, setGoodParts] = useState("0");
  const [badParts, setBadParts] = useState("0");
  const [showGoodBadModal, setShowGoodBadModal] = useState(false);

  const [inputMode, setInputMode] = useState("0");
  const [processingTime, setProcessingTime] = useState("00:00:00");
  const [notes, setNotes] = useState("");

  const [showInputModal, setShowInputModal] = useState(false);

  const titles = [
    "mil_d_16791_non_ionic_detergent_lot",
    "923as829_drive_lok_pins_lot",
    "ams_s_8802_polysulfide_lot",
    "4512421_base_plate_lt",
    "4512422_abs_insert_lot",
    "4512423_steel_insert_lot",
  ];

  const [values, setValues] = useState([0.0, 0.0, 0.0, 0.0, 0.0, 0.0]);

  const [intervalId, setIntervalID] = useState(null);
  const [tick, setTick] = useState(false);
  const timer = () => setTick((t) => !t);
  const [isStarted, setIsStarted] = useState(false);

  useEffect(() => {
    setGoodParts(appData.shiftGoodParts);
    setBadParts(appData.shiftBadParts);
  }, [appDataStore.shiftGoodParts, appDataStore.shiftBadParts]);

  const updateGoodParts = (value) => {
    appData.shiftGoodParts = parseInt(value);
    dispatch(setAppDataStore(appData));
  };

  const updateBadParts = (value) => {
    appData.shiftBadParts = parseInt(value);
    dispatch(setAppDataStore(appData));
  };

  useEffect(() => {
    return () => {
      clearInterval(intervalId);
    };
  }, []);

  useEffect(() => {
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
    setNotes("");
    setProcessingTime("00:00:00");
    setIsStarted(false);
  };

  const onStartClick = () => {
    if (!isStarted) {
      // Clear Old Timer if exists
      if (intervalId) {
        clearInterval(intervalId);
      }

      setIntervalID(setInterval(timer, 1000));
      setIsStarted(true);
    }
  };

  const onStopClick = () => {
    clearInterval(intervalId);
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
    apiCallForGetAssembly3Station(param)
      .then((res) => {
        dispatch(isSpinning(false));
        let stationInfo = res["station"];
        setProcessingTime(secondToTime(stationInfo["processing_time"]));
        setValues([
          stationInfo["mil_d_16791_non_ionic_detergent_lot"],
          stationInfo["923as829_drive_lok_pins_lot"],
          stationInfo["ams_s_8802_polysulfide_lot"],
          stationInfo["4512421_base_plate_lt"],
          stationInfo["4512422_abs_insert_lot"],
          stationInfo["4512423_steel_insert_lot"],
        ]);
      })
      .catch((err) => {
        dispatch(isSpinning(false));
        //message.error("Fail to get times!");
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
      p_mil_d_16791_non_ionic_detergent_lot: values[0],
      p_923as829_drive_lok_pins_lot: values[1],
      p_ams_s_8802_polysulfide_lot: values[2],
      p_4512421_base_plate_lt: values[3],
      p_4512422_abs_insert_lot: values[4],
      p_4512423_steel_insert_lot: values[5],
    };

    dispatch(isSpinning(true));
    apiCallForPostAssembly3Station(param)
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
    <div className="assembly3-station-layout">
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
        title={"Assembly Usage"}
        subTitle={"Please input Assembly Usage."}
        span={8}
      />
      <div className="assembly3-station-title">{"SUB ASSEMBLY STATION"}</div>
      <div className="assembly3-station-top-left">
        <div className="assembly3-station-part-id">
          <PartIDInputLayout
            partID={partID}
            setPartID={onPartId}
            setShowInputMode={setShowInputMode}
          />
        </div>
        <div className="assembly3-station-user-info">
          <UserInfoLayout />
        </div>
      </div>
      <div className="assembly3-station-top-right">
        <Radio.Group
          // onChange={updateValue}
          value={inputMode}
          className="assembly3-station-option"
          onChange={(e) => {
            setInputMode(e.target.value);
          }}
        >
          <Row align={"middle"} style={{ marginTop: "10px" }}>
            <Col span={24}>
              <Radio value={"0"} style={{ color: "white" }}>
                Barcode
              </Radio>
            </Col>
            <Col span={24}>
              <Radio value={"1"} style={{ color: "white" }}>
                Manual
              </Radio>
            </Col>
          </Row>
        </Radio.Group>
      </div>

      <div className="assembly3-station-content">
        <Row>
          <Col span={12}>
            <CurrentPartProcessingTimeWidget
              time={processingTime}
              setTime={setProcessingTime}
              onStartClick={onStartClick}
              onStopClick={onStopClick}
            />
          </Col>
          <Col span={12} style={{ paddingLeft: "5px" }}>
            <Row>
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
        <div className="assembly3-station-info">
          <div className="assembly3-station-info-title">{"Assembly Info"}</div>
          <Row align={"middle"} className="assembly3-station-info-content">
            <Col
              span={24}
              style={{ cursor: "default" }}
              onClick={() => {
                setShowInputModal(true);
              }}
            >
              <div className="assembly3-station-info-button">
                {"Assembly Info Details"}
              </div>
            </Col>
          </Row>
        </div>
      </div>
    </div>
  );
};

export default AssemblyStation3Layout;
