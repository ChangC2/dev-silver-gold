import { Col, Row, message } from "antd";
import CurrentPartProcessingTimeWidget from "components/CurrentPartProcessingTimeWidget/CurrentPartProcessingTimeWidget";
import GoodBadModal from "components/GoodBadModal/GoodBadModal";
import GoodBadWidget from "components/GoodBadWidget/GoodBadWidget";
import InputModeModal from "layouts/InputModeModal/InputModeModal";
import PartIDInputLayout from "layouts/PartIDInputLayout/PartIDInputLayout";
import PartIDModal from "layouts/PartIDModal/PartIDModal";
import UserInfoLayout from "layouts/UserInfoLayout/UserInfoLayout";
import { useEffect, useState } from "react";
import "./QualityLayout.css";
import RejectReasonsModal from "layouts/RejectReasonsModal/RejectReasonsModal";
import RadialInputGroup from "components/RadialInputGroup/RadialInputGroup";
import jobidEditIcon from "../../assets/icons/ic_jobid_edit.png";
import TextArea from "antd/lib/input/TextArea";
import { useDispatch, useSelector } from "react-redux";
import moment from "moment";
import { isSpinning, setAppDataStore } from "redux/actions/appActions";
import { appData, secondToTime, timeToSecond, userData, factoryData } from "services/global";
import {
  apiCallForGetQualityStation,
  apiCallForPostQualityStation,
} from "services/apiCall";

const QualityLayout = (props) => {
  const dispatch = useDispatch();
  const { appDataStore } = useSelector((x) => x.appDataStore);
  const [showInputMode, setShowInputMode] = useState(false);
  const [showPartID, setShowPartID] = useState(false);
  const [partID, setPartID] = useState("");

  const [goodParts, setGoodParts] = useState("0");
  const [badParts, setBadParts] = useState("0");
  const [showGoodBadModal, setShowGoodBadModal] = useState(false);
  const [showRejectReasons, setShowRejectReasons] = useState(false);
  const [selectedTitleIndex, setSelectedTitleIndex] = useState(0);

  const [processingTime, setProcessingTime] = useState("00:00:00");
  const [comments, setComments] = useState("");

  const [reasons, setReasons] = useState({
    reason1: 0,
    reason2: 0,
    reason3: 0,
    reason4: 0,
    reason5: 0,
    reason6: 0,
    reason7: 0,
    reason8: 0,
    reason9: 0,
    reason10: 0,
  });

  const [reasonTitles, setReasonTitles] = useState([
    "FOD in paint",
    "FOD in Bituminous",
    "Bituminous Separation",
    "Missing Phosphate",
    "Damaged Product",
    "Paperwork not completed",
    "Paperwork incorrect",
    "Heavy Coatings",
    "Rust",
    "Light Coatings",
  ]);

  const updateReasons = (field, value) => {
    message.warn("updateReasons");
    var newReasons = { ...reasons };
    newReasons[field] = value;
    setReasons({ ...newReasons });
  };

  const reasonsUI = Object.keys(reasons).map((key, index) => {
    return (
      <Col span={12} key={"key-" + index}>
        <RadialInputGroup
          initValue={reasons}
          field={key}
          updateValue={updateReasons}
          title={reasonTitles[index]}
        />
        <img
          className="reject-reasons-title-edit"
          src={jobidEditIcon}
          onClick={() => {
            setSelectedTitleIndex(index);
            setShowRejectReasons(true);
          }}
          alt="edit"
        />
      </Col>
    );
  });

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
    setComments("");
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
    apiCallForGetQualityStation(param)
      .then((res) => {
        dispatch(isSpinning(false));
        let stationInfo = res["station"];
        setProcessingTime(secondToTime(stationInfo["processing_time"]));
        setComments(stationInfo["comments"]);
        let scrapCode = stationInfo["scrap_code"].toLowerCase();
        let newReasons = { ...reasons };
        for (let i = 0; i < reasonTitles.length; i++) {
          if (scrapCode.includes(reasonTitles[i].toLowerCase())) {
            newReasons[Object.keys(newReasons)[i]] = 1;
          } else {
            newReasons[Object.keys(newReasons)[i]] = 0;
          }
        }
        setReasons(newReasons);
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

    let scrapCode = "";

    for (let i = 0; i < reasonTitles.length; i++) {
      if (reasons[Object.keys(reasons)[i]] === 1) {
        if (scrapCode === "") {
          scrapCode = reasonTitles[i];
        } else {
          scrapCode = scrapCode + ", " + reasonTitles[i];
        }
      }
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
      comments: comments,
      scrap_code: scrapCode,
    };

    dispatch(isSpinning(true));
    apiCallForPostQualityStation(param)
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
    <div className="quality-station-layout">
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
      <RejectReasonsModal
        showModal={showRejectReasons}
        setShowModal={setShowRejectReasons}
        titles={reasonTitles}
        setTitles={setReasonTitles}
        index={selectedTitleIndex}
      />
      <div className="quality-station-title">{"Quality STATION"}</div>
      <div className="quality-station-part-id">
        <PartIDInputLayout
          partID={partID}
          setPartID={onPartId}
          setShowInputMode={setShowInputMode}
        />
      </div>
      <div className="quality-station-user-info">
        <UserInfoLayout />
      </div>
      <div className="quality-station-content">
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
        <div className="quality-station-info">
          <Row className="quality-station-info-content">
            <Col span={24} className="quality-station-reject-reasons">
              {"Reject Reasons"}
            </Col>
            {reasonsUI}
            <Col span={24}>
              <TextArea
                className="reject-reasons-comment-textarea"
                rows={3}
                max
                placeholder="Add Comments Here"
                value={comments}
                onChange={(e) => {
                  setComments(e.target.value);
                }}
              />
            </Col>
          </Row>
        </div>
      </div>
    </div>
  );
};

export default QualityLayout;
