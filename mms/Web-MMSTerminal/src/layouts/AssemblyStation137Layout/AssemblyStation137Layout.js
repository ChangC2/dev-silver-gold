import { Col, Radio, Row, message } from "antd";
import CurrentPartProcessingTimeWidget from "components/CurrentPartProcessingTimeWidget/CurrentPartProcessingTimeWidget";
import GoodBadModal from "components/GoodBadModal/GoodBadModal";
import GoodBadWidget from "components/GoodBadWidget/GoodBadWidget";
import InputModeModal from "layouts/InputModeModal/InputModeModal";
import PartIDInputLayout from "layouts/PartIDInputLayout/PartIDInputLayout";
import PartIDModal from "layouts/PartIDModal/PartIDModal";
import UserInfoLayout from "layouts/UserInfoLayout/UserInfoLayout";
import { useState } from "react";
import moment from "moment";
import "./AssemblyStation137Layout.css";
import { isSpinning, setAppDataStore } from "redux/actions/appActions";
import { useDispatch, useSelector } from "react-redux";
import { appData, secondToTime, timeToSecond, userData, factoryData } from "services/global";
import { useEffect } from "react";
import {
  apiCallForGetAssembly1Station,
  apiCallForPostAssembly1Station,
} from "services/apiCall";
import TextInputArrayModal from "components/TextInputArrayModal/TextInputArrayModal";

const AssemblyStation137Layout = (props) => {
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

  const [showInputModal1, setShowInputModal1] = useState(false);
  const [showInputModal2, setShowInputModal2] = useState(false);

  const titles1 = [
    "case_number",
    "serial",
    "shipment",
    "empty_wt",
    "center_grav",
    "aft_ass",
    "aft_retain_ring",
    "shipping_cover",
    "shipping_plug",
    "end_cap",
    "lugs",
    "retain_ring",
    "adapt_ring",
    "impact_ring",
    "total_wt",
    "ass_center_grav",
  ];
  const [values1, setValues1] = useState([
    "0.0", "0.0", "0.0", "0.0", "0.0", "0.0", "0.0", "0.0", "0.0", "0.0", "0.0", "0.0", "0.0", "0.0", "0.0",
    "0.0",
  ]);

  const titles2 = [
    "degrease_sol",
    "corrosin_prev_compound",
    "ship_cover_oring",
    "Oring_grease",
    "protective_end_cap",
    "end_cap_set_screw",
    "lifting_lug_bolt",
    "lifting_lug_washer",
    "stencil_ink",
  ];
  const [values2, setValues2] = useState([
    "0.0", "0.0", "0.0", "0.0", "0.0", "0.0", "0.0", "0.0", "0.0",
  ]);

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

    const updatedValue1 = values1.map(() => "0.0");
    setValues1(updatedValue1);

    const updateValue2 = values2.map(() => "0.0");
    setValues2(updateValue2);
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
    apiCallForGetAssembly1Station(param)
      .then((res) => {
        dispatch(isSpinning(false));
        let stationInfo = res["station"];
        setProcessingTime(secondToTime(stationInfo["processing_time"]));
        setValues1([
          stationInfo["case_number"],
          stationInfo["serial"],
          stationInfo["shipment"],
          stationInfo["empty_wt"],
          stationInfo["center_grav"],
          stationInfo["aft_ass"],
          stationInfo["aft_retain_ring"],
          stationInfo["shipping_cover"],
          stationInfo["shipping_plug"],
          stationInfo["end_cap"],
          stationInfo["lugs"],
          stationInfo["retain_ring"],
          stationInfo["adapt_ring"],
          stationInfo["impact_ring"],
          stationInfo["total_wt"],
          stationInfo["ass_center_grav"],
        ]);

        setValues2([
          stationInfo["degrease_sol"],
          stationInfo["corrosin_prev_compound"],
          stationInfo["ship_cover_oring"],
          stationInfo["oring_grease"],
          stationInfo["protective_end_cap"],
          stationInfo["end_cap_set_screw"],
          stationInfo["lifting_lug_bolt"],
          stationInfo["lifting_lug_washer"],
          stationInfo["stencil_ink"],
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
      case_number: values1[0],
      serial: values1[1],
      shipment: values1[2],
      empty_wt: values1[3],
      center_grav: values1[4],
      aft_ass: values1[5],
      aft_retain_ring: values1[6],
      shipping_cover: values1[7],
      shipping_plug: values1[8],
      end_cap: values1[9],
      lugs: values1[10],
      retain_ring: values1[11],
      adapt_ring: values1[12],
      impact_ring: values1[13],
      total_wt: values1[14],
      ass_center_grav: values1[15],
      degrease_sol: values2[0],
      corrosin_prev_compound: values2[1],
      ship_cover_oring: values2[2],
      oring_grease: values2[3],
      protective_end_cap: values2[4],
      end_cap_set_screw: values2[5],
      lifting_lug_bolt: values2[6],
      lifting_lug_washer: values2[7],
      stencil_ink: values2[8],
    };

    dispatch(isSpinning(true));
    apiCallForPostAssembly1Station(param)
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
    <div className="assembly137-station-layout">
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
        showModal={showInputModal1}
        setShowModal={setShowInputModal1}
        values={values1}
        setValues={setValues1}
        titles={titles1}
        title={"Assembly Usage 1"}
        subTitle={"Please input Assembly Usage 1."}
        span={6}
      />
      <TextInputArrayModal
        showModal={showInputModal2}
        setShowModal={setShowInputModal2}
        values={values2}
        setValues={setValues2}
        titles={titles2}
        title={"Assembly Usage 2"}
        subTitle={"Please input Assembly Usage 2."}
        span={8}
      />
      <div className="assembly137-station-title">
        {"ASSEMBLY STATION - BLU 137"}
      </div>
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
      <div className="assembly137-station-content">
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
        <div className="assembly137-station-info">
          <div className="assembly137-station-info-title">
            {"Assembly Info"}
          </div>
          <Row align={"middle"} className="assembly137-station-info-content">
            <Col
              span={12}
              onClick={() => {
                setShowInputModal1(true);
              }}
            >
              <div
                className="assembly137-station-info-button"
                style={{ float: "right" }}
              >
                {"Assembly Usage1"}
              </div>
            </Col>
            <Col
              span={12}
              onClick={() => {
                setShowInputModal2(true);
              }}
            >
              <div className="assembly137-station-info-button">
                {"Assembly Usage2"}
              </div>
            </Col>
          </Row>
        </div>
      </div>
    </div>
  );
};

export default AssemblyStation137Layout;
