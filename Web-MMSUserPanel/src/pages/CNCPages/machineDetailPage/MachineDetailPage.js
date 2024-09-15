import {
  ArrowLeftOutlined,
  SettingOutlined,
  ToolOutlined,
  UserOutlined,
  VideoCameraOutlined,
} from "@ant-design/icons";
import { Avatar, Col, Row, Spin } from "antd";
import { useEffect, useState } from "react";

import { useSelector } from "react-redux";
import {
  ConvertTimespanToDateBasedOnTimezone,
  GetCustomerCurrentTime,
  GetOneMachineData,
  GetTimeWithStyle,
  pad,
} from "../../../services/common/cnc_apis";
import { sizeMobile } from "../../../services/common/constants";
import Urls, {
  MACHINE_IMAGE_BASE_URL,
  postRequest,
} from "../../../services/common/urls";
import lang from "../../../services/lang";
import MachineCalculation from "../MainPage/MachineItem_Grid/MachineCalculation";
import AppSettingModal from "./AppSettingModal/AppSettingModal";
import DetailGanttModal from "./Containers/DetailGanttModal";
import HistoryContainer from "./Containers/HistoryContainer";
import IndicatorContainer from "./Containers/IndicatorContainer";
import TimelineContainer from "./Containers/TimelineContainer";
import UtilizationContainer from "./Containers/UtilizationContainerAntd";
import InstallConfigModal from "./InstallConfig/InstallConfigModal";
import "./MachineDetailPage.css";

function MachineDetailPage(props) {
  const { langIndex } = useSelector((x) => x.app);
  const {
    onCloseMachine,
    operatorList,
    screenSize,
    security_level,
    customer_id,
    customerInfo,
    machineInfo,
  } = props;

  const timezone = customerInfo["timezone"];
  const customerTime = GetCustomerCurrentTime(timezone);

  const [currentDate, setCurrentDate] = useState("");

  const [
    isMachineDetailPageContentLoading,
    setIsMachineDetailPageContentLoading,
  ] = useState(false);
  const [showDetailGanttModal, setShowDetailGanttModal] = useState(false);

  const [myHst, setMyHst] = useState({});
  const [myGanttList, setMyGanttList] = useState([]);

  const [hstInfo, setHstInfo] = useState({});
  const [ganttInfo, setGanttInfo] = useState([]);
  const [additionalHstInfo, setAdditionalHstInfo] = useState([]);
  const [shifts, setShifts] = useState(["00:00-24:00", "", "", ""]);
  const [shiftStart, setShiftStart] = useState(0);
  const [shiftEnd, setShiftEnd] = useState(0);
  const [shift, setShift] = useState(0);

  const [isShowAppSettingModal, setIsShowAppSettingModal] = useState(false);
  const [isShowInstallConfigModal, setIsShowInstallConfigModal] =
    useState(false);

  const [appSetting, setAppSetting] = useState({ gantt_chart_display: -1 });

  const todayString = lang(langIndex, "cnc_today");

  const openAppSettingDialog = () => {
    setIsShowAppSettingModal(true);
  };

  const openInstallConfigDialog = () => {
    setIsShowInstallConfigModal(true);
  };

  useEffect(() => {
    var url = Urls.GET_APP_SETTING;
    var param = {
      factory_id: customer_id,
      machine_id: machineInfo.machine_id,
    };
    postRequest(url, param, (res) => {
      if (res.status === true) {
        setAppSetting(res.data);
      } else {
        setAppSetting({ gantt_chart_display: 0 });
      }
    });
  }, []);

  useEffect(() => {
    apiCallForGetMachineData();
  }, [customer_id, machineInfo, shift, currentDate]);

  useEffect(() => {
    var useData = extractMachineData(myHst, myGanttList);
    console.log("MachineDetails", useData);
    setHstInfo({ ...useData["hstInfo"] });
    setGanttInfo([...useData["ganttInfo"]]);
    setAdditionalHstInfo([...useData["additionalHstInfo"]]);
  }, [myHst, myGanttList, shift, currentDate, shiftStart, shiftEnd]);

  function extractHstFromGantt(_ganttInfo) {
    var _additionalHst = [];
    for (var i = 0; i < _ganttInfo.length; i++) {
      if (
        _ganttInfo[i].status != "Offline" &&
        _ganttInfo[i].start <= shiftEnd / 1000 &&
        _ganttInfo[i].end >= shiftStart / 1000
      ) {
        if (
          _additionalHst.length === 0 ||
          _additionalHst.filter((x) => x.status === _ganttInfo[i].status)
            .length === 0
        ) {
          _additionalHst.push({
            status: _ganttInfo[i].status,
            color: _ganttInfo[i].color,
            duration: parseFloat(
              parseFloat(_ganttInfo[i].end) - parseFloat(_ganttInfo[i].start)
            ),
          });
        } else {
          var _hst = _additionalHst.filter(
            (x) => x.status === _ganttInfo[i].status
          )[0];
          _hst.duration += parseFloat(
            parseFloat(_ganttInfo[i].end) - parseFloat(_ganttInfo[i].start)
          );
        }
      }
    }

    for (var i = 0; i < _additionalHst.length; i++) {
      _additionalHst[i].duration = _additionalHst[i].duration / 3600;
    }

    // calculate offline time
    if (_ganttInfo.length > 0) {
      var offlineTime = 0;
      const onlineTime = _additionalHst
        .map((item) => item.duration)
        .reduce((a, b) => a + b, 0);

      offlineTime =
        (shiftEnd - shiftStart) / 1000 / 3600 -
        _ganttInfo[_ganttInfo.length - 1].end / 3600 -
        onlineTime;
      offlineTime = Math.round(offlineTime * 10) / 10;
      if (offlineTime > 0) {
        _additionalHst.push({
          status: lang(langIndex, "cnc_offline"),
          color: "#000000",
          duration: offlineTime,
        });
      }
    }
    return _additionalHst;
  }

  function extractMachineData(tmpHst, tmpGanttList) {
    let _hstInfo = tmpHst;
    let _ganttInfo = tmpGanttList.filter(
      (gantt) => gantt["machine_id"] === machineInfo["machine_id"]
    );
    return {
      ganttInfo: _ganttInfo,
      hstInfo: _hstInfo,
      additionalHstInfo: extractHstFromGantt(_ganttInfo),
    };
  }

  function apiCallForGetMachineData() {
    let startDate = GetCustomerCurrentTime(timezone);
    if (currentDate !== undefined && currentDate !== "") {
      startDate = new Date(currentDate);
    }
    startDate =
      pad(startDate.getMonth() + 1) +
      "/" +
      pad(startDate.getDate()) +
      "/" +
      pad(startDate.getFullYear());

    let endDate = GetCustomerCurrentTime(timezone);
    if (currentDate !== undefined && currentDate !== "") {
      endDate = new Date(currentDate);
    }
    endDate.setDate(endDate.getDate() + 1);
    endDate =
      pad(endDate.getMonth() + 1) +
      "/" +
      pad(endDate.getDate()) +
      "/" +
      pad(endDate.getFullYear());

    setIsMachineDetailPageContentLoading(true);
    GetOneMachineData(
      customer_id,
      customerInfo["timezone"],
      startDate,
      endDate,
      machineInfo["machine_id"],
      shifts[shift],
      (res) => {
        setIsMachineDetailPageContentLoading(false);
        if (res != null && res.data.machines.length > 0) {
          setMyGanttList([...res.data.machines[0].gantt]);
          setMyHst({ ...res.data.machines[0] });
          setShifts(["00:00-24:00", ...res.data.shifts]);
          setShiftStart(res.data.shift_start);
          setShiftEnd(res.data.shift_end);
        }
      }
    );
  }

  let operatorName = lang(langIndex, "cnc_guest");
  let operatorImage = "";
  // read status data and set status of machine and operator name and operator photo
  if (operatorList.length > 0) {
    var mOperator = operatorList.find(
      (x) =>
        x.username_full != null &&
        machineInfo["Operator"] != null &&
        x.username_full.toLowerCase().trim() ==
          machineInfo["Operator"].toLowerCase().trim()
    );
    operatorImage =
      mOperator == undefined
        ? "https://slymms.com/backend/images/photo/blank.jpg"
        : mOperator.user_picture;
    operatorName = mOperator == undefined ? "guest" : mOperator.username_full;
    if (operatorName.toLowerCase() === "guest") {
      operatorName = lang(langIndex, "cnc_guest");
    }
  }

  var usingGantt = myGanttList;

  usingGantt = usingGantt.filter(
    (x) => x.machine_id === machineInfo.machine_id
  );

  var lastGantt =
    usingGantt.length === 0 ? {} : usingGantt[usingGantt.length - 1];

  return screenSize.width >= sizeMobile ? (
    <div className="machine-detail-container">
      {isShowAppSettingModal && (
        <AppSettingModal
          isShowAppSettingModal={isShowAppSettingModal}
          setIsShowAppSettingModal={setIsShowAppSettingModal}
          customer_id={customer_id}
          security_level={security_level}
          machineInfo={machineInfo}
          setSetting={setAppSetting}
        />
      )}
      {isShowInstallConfigModal && (
        <InstallConfigModal
          isShowInstallConfigModal={isShowInstallConfigModal}
          setIsShowInstallConfigModal={setIsShowInstallConfigModal}
          customer_id={customer_id}
          security_level={security_level}
          machineInfo={machineInfo}
        />
      )}
      {showDetailGanttModal && (
        <DetailGanttModal
          showDetailGanttModal={showDetailGanttModal}
          setShowDetailGanttModal={setShowDetailGanttModal}
          ganttInfo={ganttInfo}
          machineInfo={machineInfo}
          customerInfo={customerInfo}
          currentDate={currentDate}
          security_level={security_level}
          myGanttList={myGanttList}
          setMyGanttList={setMyGanttList}
          customer_id={customer_id}
        />
      )}
      <Spin spinning={isMachineDetailPageContentLoading} size="large">
        <Row justify="center" style={{ width: "100%" }}>
          <Col className="detail-content-container">
            <Row justify="center" className="content-table" align="top">
              <Col
                span={18}
                className="machine-detail-header-back"
                style={{ textAlign: "left" }}
              >
                <Row justify="center" align="middle">
                  <Col span={3}>
                    <ArrowLeftOutlined
                      className="backward-button"
                      onClick={() => onCloseMachine("")}
                    />
                  </Col>
                  <Col span={12} className="machine-detail-header-name">
                    {machineInfo["machine_id"]}
                  </Col>
                  <Col span={6} className="machine-detail-header-date">
                    {currentDate === "" ? todayString : currentDate}
                  </Col>

                  <Col span={2} style={{ textAlign: "right", paddingRight: 5 }}>
                    {security_level == 4 && (
                      <ToolOutlined
                        className="app-setting-button"
                        onClick={openInstallConfigDialog}
                      />
                    )}
                  </Col>
                  <Col span={1} style={{ textAlign: "center" }}>
                    {security_level == 4 && (
                      <SettingOutlined
                        className="app-setting-button"
                        onClick={openAppSettingDialog}
                      />
                    )}
                  </Col>
                </Row>
              </Col>
              <Col span={6}>
                <div className="machine-detail-customer-container">
                  <div className="user-name-container">
                    {operatorImage === "" ? (
                      <Avatar
                        className="detail-operator-image-style"
                        size={50}
                        icon={<UserOutlined />}
                      />
                    ) : (
                      <img
                        style={{ width: 50, height: 50 }}
                        className="detail-operator-image-style"
                        src={operatorImage}
                      />
                    )}
                    <h4 className="operator-name-style">{operatorName}</h4>
                  </div>
                  {/*  */}
                </div>
              </Col>

              <Col span={18}>
                <div className="machine-detail-indicator-container">
                  <IndicatorContainer
                    hstInfo={hstInfo}
                    machineInfo={machineInfo}
                    screenSize={screenSize}
                    lastGantt={lastGantt}
                    setShift={setShift}
                    shift={shift}
                    shifts={shifts}
                  />
                </div>
              </Col>

              <Col span={6}>
                <div className="machine-detail-img-container">
                  <div className="machine-image-container">
                    {machineInfo != "" && (
                      <img
                        style={{ maxWidth: "100%", height: 150 }}
                        src={
                          machineInfo["machine_picture_url"].includes("http")
                            ? machineInfo["machine_picture_url"]
                            : MACHINE_IMAGE_BASE_URL +
                              machineInfo["machine_picture_url"]
                        }
                      />
                    )}
                  </div>
                </div>
                <div className="machine-detail-camera-container">
                  <div className="intro-video-container">
                    <VideoCameraOutlined
                      size={100}
                      style={{ fontSize: 100, color: "white" }}
                    />
                  </div>
                </div>
              </Col>

              <Col span={18}>
                <Row>
                  <Col span={10}>
                    <div className="machine-detail-utilization-container">
                      <UtilizationContainer
                        hstInfo={hstInfo}
                        machineInfo={machineInfo}
                        additionalHstInfo={additionalHstInfo}
                        screenSize={screenSize}
                        shift={shift}
                        shifts={shifts}
                      />
                    </div>
                  </Col>
                  <Col span={14}>
                    <div className="machine-detail-timeline-container">
                      <TimelineContainer
                        ganttInfo={ganttInfo}
                        machineInfo={machineInfo}
                        customerInfo={customerInfo}
                        setShowDetailGanttModal={setShowDetailGanttModal}
                        currentDate={currentDate}
                        screenSize={screenSize}
                        customer_id={customer_id}
                        security_level={security_level}
                        myGanttList={myGanttList}
                        setMyGanttList={setMyGanttList}
                        shift={shift}
                        shifts={shifts}
                      />
                    </div>
                  </Col>
                </Row>
              </Col>
              <Col span={6}>
                <div className="machine-detail-history-container">
                  <HistoryContainer
                    machineInfo={machineInfo}
                    customerInfo={customerInfo}
                    customer_id={customer_id}
                    // extractMachineData={extractMachineData}
                    setIsMachineDetailPageContentLoading={
                      setIsMachineDetailPageContentLoading
                    }
                    setCurrentDate={setCurrentDate}
                    setMyHst={setMyHst}
                    setMyGanttList={setMyGanttList}
                    currentDate={currentDate}
                    todayString={todayString}
                    shifts={shifts}
                    shift={shift}
                    setShiftStart={setShiftStart}
                    setShiftEnd={setShiftEnd}
                  />
                </div>
              </Col>
              <Col span={18}>
                <Row>
                  <Col span={appSetting.gantt_chart_display == 2 ? 8 : 24}>
                    <div className="machine-detail-text-container">
                      <h4
                        style={{
                          color: "#eeeeee",
                          marginTop:
                            appSetting.gantt_chart_display == 2 ? 0 : 15,
                        }}
                      >
                        {lang(langIndex, "cnc_lastupdate")}:
                        {lastGantt["end"] === "undefined"
                          ? ""
                          : " " +
                            GetTimeWithStyle(
                              ConvertTimespanToDateBasedOnTimezone(
                                lastGantt["end"],
                                customerInfo["timezone"]
                              )
                            )}
                      </h4>
                      <h4 style={{ color: "#eeeeee" }}>
                        {lang(langIndex, "cnc_interface")}:{" "}
                        {lastGantt["interface"] === "undefined"
                          ? ""
                          : lastGantt["interface"]}
                      </h4>
                      <h4 style={{ color: "#eeeeee" }}>
                        {lang(langIndex, "cnc_version")}:{" "}
                        {machineInfo["app_version"] === null
                          ? ""
                          : " Ver " + machineInfo["app_version"]}
                      </h4>
                    </div>
                  </Col>
                  <Col span={16}>
                    {appSetting.gantt_chart_display == 2 && (
                      <div className="machine-detail-timeline-container">
                        <MachineCalculation
                          machineInfo={machineInfo}
                          customer_id={customer_id}
                          customerInfo={customerInfo}
                          appSetting={appSetting}
                          currentDate={currentDate}
                        />
                      </div>
                    )}
                  </Col>
                </Row>
              </Col>
              <Col span={6}></Col>
            </Row>
          </Col>
        </Row>
      </Spin>
    </div>
  ) : (
    // mobile version
    <div className="machine-detail-container">
      {showDetailGanttModal && (
        <DetailGanttModal
          showDetailGanttModal={showDetailGanttModal}
          setShowDetailGanttModal={setShowDetailGanttModal}
          ganttInfo={ganttInfo}
          machineInfo={machineInfo}
          customerInfo={customerInfo}
          currentDate={currentDate}
          customer_id={customer_id}
        />
      )}
      <Spin spinning={isMachineDetailPageContentLoading} size="large">
        <Row justify="center" style={{ width: "100%" }}>
          <Col className="detail-content-container">
            <Row justify="center" className="content-table" align="top">
              <Col span={24} style={{ textAlign: "left" }}>
                <Row justify="center">
                  <Col span={3}>
                    <ArrowLeftOutlined
                      className="backward-button"
                      onClick={() => onCloseMachine("")}
                    />
                  </Col>
                  <Col span={12} style={{ textAlign: "center" }}>
                    <h4 style={{ color: "#eeeeee" }}>
                      {machineInfo["machine_id"]}
                    </h4>
                  </Col>
                  <Col span={9}>
                    <div className="dateContainer">
                      <h4 style={{ color: "#eeeeee" }}>
                        {currentDate === "" ? todayString : currentDate}
                      </h4>
                    </div>
                  </Col>
                </Row>
              </Col>
              <Col span={24}>
                <Row>
                  <Col span={24}>
                    <div className="machine-detail-user-info-mobile">
                      <div>
                        {operatorImage === "" ? (
                          <Avatar
                            className="detail-operator-image-style"
                            size={50}
                            icon={<UserOutlined />}
                          />
                        ) : (
                          <img
                            style={{ width: 50, height: 50 }}
                            className="detail-operator-image-style"
                            src={operatorImage}
                          />
                        )}
                      </div>
                      <h4 className="operator-name-style">{operatorName}</h4>
                    </div>
                  </Col>
                  <Col span={12}>
                    <div className="info-container user-machine-info-container-mobile">
                      <div className="machine-image-container-mobile">
                        {machineInfo != "" && (
                          <img
                            src={
                              machineInfo["machine_picture_url"].includes(
                                "http"
                              )
                                ? machineInfo["machine_picture_url"]
                                : MACHINE_IMAGE_BASE_URL +
                                  machineInfo["machine_picture_url"]
                            }
                          />
                        )}
                      </div>
                    </div>
                  </Col>
                  <Col span={12}>
                    <div className="info-container user-machine-info-container-mobile">
                      <VideoCameraOutlined
                        size={60}
                        style={{ fontSize: 60, color: "white", marginTop: 18 }}
                      />
                    </div>
                  </Col>
                </Row>
              </Col>

              <Col span={24}>
                <div className="info-container">
                  <IndicatorContainer
                    hstInfo={hstInfo}
                    machineInfo={machineInfo}
                    screenSize={screenSize}
                    lastGantt={lastGantt}
                    setShift={setShift}
                    shift={shift}
                    shifts={shifts}
                  />
                </div>
              </Col>

              <Col span={24}>
                <div className="info-container">
                  <UtilizationContainer
                    hstInfo={hstInfo}
                    machineInfo={machineInfo}
                    additionalHstInfo={additionalHstInfo}
                    screenSize={screenSize}
                    shift={shift}
                    shifts={shifts}
                  />
                </div>
              </Col>
              <Col span={24}>
                <div className="info-container">
                  <TimelineContainer
                    ganttInfo={ganttInfo}
                    machineInfo={machineInfo}
                    customerInfo={customerInfo}
                    setShowDetailGanttModal={setShowDetailGanttModal}
                    currentDate={currentDate}
                    screenSize={screenSize}
                    customer_id={customer_id}
                    security_level={security_level}
                    myGanttList={myGanttList}
                    setMyGanttList={setMyGanttList}
                    shift={shift}
                    shifts={shifts}
                  />
                </div>
              </Col>

              <Col span={24}>
                {appSetting.gantt_chart_display == 2 && (
                  <div className="info-container">
                    <MachineCalculation
                      machineInfo={machineInfo}
                      customer_id={customer_id}
                      customerInfo={customerInfo}
                      appSetting={appSetting}
                      currentDate={currentDate}
                    />
                  </div>
                )}
              </Col>

              <Col span={24}>
                <div className="info-container">
                  <HistoryContainer
                    machineInfo={machineInfo}
                    customerInfo={customerInfo}
                    customer_id={customer_id}
                    setIsMachineDetailPageContentLoading={
                      setIsMachineDetailPageContentLoading
                    }
                    setCurrentDate={setCurrentDate}
                    setMyHst={setMyHst}
                    setMyGanttList={setMyGanttList}
                    currentDate={currentDate}
                    todayString={todayString}
                    shifts={shifts}
                    shift={shift}
                  />
                </div>
              </Col>

              <Col span={24} style={{ marginLeft: 10 }}>
                <Row>
                  <h4 style={{ color: "#eeeeee" }}>
                    {lang(langIndex, "cnc_lastupdate")}:
                    {lastGantt["end"] === "undefined"
                      ? ""
                      : " " +
                        GetTimeWithStyle(
                          ConvertTimespanToDateBasedOnTimezone(
                            lastGantt["end"],
                            customerInfo["timezone"]
                          )
                        )}
                  </h4>
                </Row>
                <Row>
                  <h4 style={{ color: "#eeeeee" }}>
                    {lang(langIndex, "cnc_interface")}:{" "}
                    {lastGantt["interface"] === "undefined"
                      ? ""
                      : lastGantt["interface"]}
                  </h4>
                </Row>
                <Row>
                  <h4 style={{ color: "#eeeeee" }}>
                    {lang(langIndex, "cnc_version")}:{" "}
                    {machineInfo["app_version"] === null
                      ? ""
                      : " Ver " + machineInfo["app_version"]}
                  </h4>
                </Row>
              </Col>
            </Row>
          </Col>
        </Row>
      </Spin>
    </div>
  );
}

export default MachineDetailPage;
