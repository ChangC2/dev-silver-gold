import { ArrowLeftOutlined, UserOutlined } from "@ant-design/icons";
import { Avatar, Col, Row, Spin } from "antd";
import { useEffect, useState } from "react";

import { useSelector } from "react-redux";
import {
  GetCustomerCurrentTime,
  GetOneMachineData,
  GetUtilizationDetailData,
  pad,
} from "../../../../src/services/common/cnc_apis";
import { sizeMobile } from "../../../../src/services/common/constants";
import Urls, {
  MACHINE_IMAGE_BASE_URL,
  postRequest,
} from "../../../../src/services/common/urls";
import lang from "../../../../src/services/lang";
import AppSettingModal from "./AppSettingModal/AppSettingModal";
import DetailGanttModal from "./Containers/DetailGanttModal";
import TimelineContainer from "./Containers/TimelineContainer";
import UtilizationContainer from "./Containers/UtilizationContainerAntd";
import UtilizationMonthly from "./Containers/UtilizationMonthly";
import UtilizationMonthlyArea from "./Containers/UtilizationMonthlyArea";
import UtilizationWeekly from "./Containers/UtilizationWeeklyBar";
import UtilizationWeeklyDown from "./Containers/UtilizationWeeklyDown";
import InstallConfigModal from "./InstallConfig/InstallConfigModal";
import "./UtilizationDetailPage.css";

function UtilizationDetailPage(props) {
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
  const [monthlyUtilizations, setMonthlyUtilizations] = useState([]);
  const [downtimes, setDowntimes] = useState([]);
  const [weeklyUtilizations, setWeeklyUtilizations] = useState([]);
  const [weeklyTotal, setWeeklyTotal] = useState(0);
  const [monthlyTotal, setMonthlyTotal] = useState(0);

  const [isBusy, setIsBusy] = useState(false);
  const [showDetailGanttModal, setShowDetailGanttModal] = useState(false);

  const [myHst, setMyHst] = useState({});
  const [myGanttList, setMyGanttList] = useState([]);
  const [myLastGant, setMyLastGant] = useState({});

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
    apiCallForGetOneMachineData();
    apiCallForGetMachineData();
  }, [customer_id, machineInfo, shift, currentDate]);

  function apiCallForGetOneMachineData() {
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

    setIsBusy(true);
    GetOneMachineData(
      customer_id,
      customerInfo["timezone"],
      startDate,
      endDate,
      machineInfo["machine_id"],
      shifts[shift],
      (res) => {
        setIsBusy(false);
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

  useEffect(() => {
    var useData = extractMachineData(myHst, myGanttList);
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
    startDate.setDate(startDate.getDate() - 30);
    startDate =
      pad(startDate.getMonth() + 1) +
      "/" +
      pad(startDate.getDate()) +
      "/" +
      pad(startDate.getFullYear());

    let endDate = GetCustomerCurrentTime(timezone);
    endDate.setDate(endDate.getDate() + 1);
    endDate =
      pad(endDate.getMonth() + 1) +
      "/" +
      pad(endDate.getDate()) +
      "/" +
      pad(endDate.getFullYear());

    setIsBusy(true);
    GetUtilizationDetailData(
      customer_id,
      customerInfo["timezone"],
      startDate,
      endDate,
      machineInfo["machine_id"],
      1,
      (res) => {
        setIsBusy(false);
        if (res != null && res.data) {
          let tmpList = [...res.data.utilities];
          let weekTmpList = [...res.data.utilities];
          let tmpTotal = 0;
          tmpList.map((x, index) => {
            tmpTotal += parseFloat(x.utilization);
          });
          setMonthlyTotal(parseFloat(tmpTotal).toFixed(1));
          setMonthlyUtilizations(tmpList);
          if (weekTmpList.length > 7) {
            weekTmpList = weekTmpList.splice(weekTmpList.length - 7, 7);
          }
          tmpTotal = 0;
          weekTmpList.map((x, index) => {
            tmpTotal += parseFloat(x.utilization);
          });
          setWeeklyTotal(parseFloat(tmpTotal).toFixed(1));
          setWeeklyUtilizations(weekTmpList);
          setDowntimes([...res.data.downtimes]);
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

  return screenSize.width >= sizeMobile ? (
    <div className="utilization-detail-machine-detail-container">
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
      <Spin spinning={isBusy} size="large">
        <Row justify="center" style={{ width: "100%" }}>
          <Col span={24}>
            <div className="util-detail-header-back">
              <ArrowLeftOutlined
                className="backward-button"
                onClick={() => onCloseMachine("")}
              />
            </div>
            <Row justify="center" gutter={[10, 10]} style={{ marginTop: 10 }}>
              <Col md={9}>
                <div className="machine-detail-utilization-container">
                  <Row className="utilization-detail-chart-container">
                    <Col span={24} className="utilization-detail-chart-title">
                      {machineInfo["machine_id"]}
                    </Col>
                    <Col span={24} className="utilization-detail-chart-right">
                      {machineInfo != "" && (
                        <img
                          style={{ maxWidth: "100%", height: 170, margin: 10 }}
                          src={
                            machineInfo["machine_picture_url"].includes("http")
                              ? machineInfo["machine_picture_url"]
                              : MACHINE_IMAGE_BASE_URL +
                                machineInfo["machine_picture_url"]
                          }
                        />
                      )}
                    </Col>
                  </Row>
                </div>
              </Col>
              <Col md={15}>
                <div className="machine-detail-utilization-container">
                  <Row className="utilization-detail-chart-container">
                    <Col span={24} className="utilization-detail-chart-title">
                      Current Status
                    </Col>
                    <Col span={24} className="utilization-detail-status-left">
                      <Row justify="center">
                        <Col span={12}>
                          <div
                            className="utilization-detail-status-title"
                            style={{ color: myHst?.color }}
                          >
                            {myHst?.status}
                          </div>
                          <div className="utilization-detail-status-subtitle">
                            Job ID: {myLastGant?.job_id}
                          </div>
                          <div className="utilization-detail-status-subtitle">
                            Program Number: {myLastGant?.current_program}
                          </div>
                          <div
                            className="machine-hst-gragh-total-goods"
                            style={{ marginTop: 3 }}
                          >
                            Total Good Parts: {myHst?.goodParts}
                          </div>
                          <div
                            className="machine-hst-gragh-total-bads"
                            style={{ marginTop: 3 }}
                          >
                            Total Bad Parts: {myHst?.badParts}
                          </div>
                        </Col>
                        <Col span={12}>
                          <div style={{ textAlign: "center" }}>
                            {operatorImage === "" ? (
                              <Avatar
                                className="util-avatar"
                                size={120}
                                icon={<UserOutlined />}
                              />
                            ) : (
                              <img
                                className="util-avatar"
                                src={operatorImage}
                              />
                            )}
                          </div>
                          <div
                            className="util-item-name"
                            style={{ marginTop: 5 }}
                          >
                            {operatorName}
                          </div>
                        </Col>
                      </Row>
                    </Col>
                  </Row>
                </div>
              </Col>
            </Row>
            <Row justify="center" gutter={[10, 10]}>
              <Col md={9}>
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
              <Col md={15}>
                <div className="util-detail-timeline-container">
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
            <Row justify="center" gutter={[10, 10]}>
              <Col md={9}>
                <div className="machine-detail-utilization-container">
                  <UtilizationWeekly
                    hstInfo={hstInfo}
                    machineInfo={machineInfo}
                    additionalHstInfo={additionalHstInfo}
                    screenSize={screenSize}
                    shift={shift}
                    shifts={shifts}
                    weeklyTotal={weeklyTotal}
                    weeklyUtilizations={weeklyUtilizations}
                  />
                </div>
                <div className="machine-detail-utilization-container">
                  <UtilizationMonthly
                    hstInfo={hstInfo}
                    machineInfo={machineInfo}
                    additionalHstInfo={additionalHstInfo}
                    screenSize={screenSize}
                    shift={shift}
                    shifts={shifts}
                    monthlyTotal={monthlyTotal}
                    monthlyUtilizations={monthlyUtilizations}
                  />
                </div>
              </Col>
              <Col md={15}>
                <div className="util-detail-timeline-container">
                  <UtilizationWeeklyDown
                    hstInfo={hstInfo}
                    machineInfo={machineInfo}
                    additionalHstInfo={additionalHstInfo}
                    screenSize={screenSize}
                    shift={shift}
                    shifts={shifts}
                    downtimes={downtimes}
                  />
                </div>
              </Col>
            </Row>
            <Row>
              <Col span={24}>
                <div className="util-detail-timeline-container">
                  <UtilizationMonthlyArea
                    machineInfo={machineInfo}
                    customer_id={customer_id}
                    customerInfo={customerInfo}
                    timezone={timezone}
                  />
                </div>
              </Col>
            </Row>
          </Col>
        </Row>
      </Spin>
    </div>
  ) : (
    // mobile version
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
      <Spin spinning={isBusy} size="large">
        <Row justify="center" style={{ width: "100%" }}>
          <Col>
            <div className="util-detail-header-back">
              <ArrowLeftOutlined
                className="backward-button"
                onClick={() => onCloseMachine("")}
              />
            </div>
            <div
              className="util-rect-round-item"
              style={{ textAlign: "center" }}
            >
              <p className="util-device-name">{machineInfo["machine_id"]}</p>
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
            <div className="util-rect-round-item" style={{ marginTop: 10 }}>
              <p className="util-item-title">Current Status</p>
              <Row justify="center">
                <Col md={10} sm={12}>
                  {myHst?.status === "Offline" ? (
                    <div className="util-item-offline-status">
                      {myHst?.status}
                    </div>
                  ) : (
                    <div
                      className="util-item-status"
                      style={{ color: myHst?.color }}
                    >
                      {myHst?.status}
                    </div>
                  )}
                  <div className="utilization-detail-status-subtitle">
                    Job ID: {myLastGant?.job_id}
                  </div>
                  <div className="utilization-detail-status-subtitle">
                    Program Number: {myLastGant?.current_program}
                  </div>
                  <div className="machine-hst-gragh-total-goods">
                    Total Good Parts: {myHst?.goodParts}
                  </div>
                  <div className="machine-hst-gragh-total-bads">
                    Total Bad Parts: {myHst?.badParts}
                  </div>
                </Col>
                <Col md={14} sm={12}>
                  <div style={{ textAlign: "center" }}>
                    {operatorImage === "" ? (
                      <Avatar
                        className="util-avatar"
                        size={120}
                        icon={<UserOutlined />}
                      />
                    ) : (
                      <img className="util-avatar" src={operatorImage} />
                    )}
                  </div>
                  <div className="util-item-name" style={{ marginTop: 5 }}>
                    {operatorName}
                  </div>
                </Col>
              </Row>
            </div>
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
            <div className="util-detail-timeline-container">
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
            <div className="machine-detail-utilization-container">
              <UtilizationWeekly
                hstInfo={hstInfo}
                machineInfo={machineInfo}
                additionalHstInfo={additionalHstInfo}
                screenSize={screenSize}
                shift={shift}
                shifts={shifts}
                weeklyTotal={weeklyTotal}
                weeklyUtilizations={weeklyUtilizations}
              />
            </div>
            <div className="machine-detail-utilization-container">
              <UtilizationMonthly
                hstInfo={hstInfo}
                machineInfo={machineInfo}
                additionalHstInfo={additionalHstInfo}
                screenSize={screenSize}
                shift={shift}
                shifts={shifts}
                monthlyTotal={monthlyTotal}
                monthlyUtilizations={monthlyUtilizations}
              />
            </div>
            <div className="util-detail-timeline-container">
              <UtilizationWeeklyDown
                hstInfo={hstInfo}
                machineInfo={machineInfo}
                additionalHstInfo={additionalHstInfo}
                screenSize={screenSize}
                shift={shift}
                shifts={shifts}
                downtimes={downtimes}
              />
            </div>
            <div className="util-detail-timeline-container">
              <UtilizationMonthlyArea
                hstInfo={hstInfo}
                machineInfo={machineInfo}
                additionalHstInfo={additionalHstInfo}
                screenSize={screenSize}
                shift={shift}
                shifts={shifts}
                monthlyTotal={monthlyTotal}
                monthlyUtilizations={monthlyUtilizations}
              />
            </div>
          </Col>
        </Row>
      </Spin>
    </div>
  );
}

export default UtilizationDetailPage;
