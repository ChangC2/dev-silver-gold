import { Col, Row, Spin } from "antd";
import BottomLayout from "layouts/BottomLayout/BottomLayout";
import ContentLayout from "layouts/ContentLayout/ContentLayout";
import TopLayout from "layouts/TopLayout/TopLayout";
import MachineStatus from "pages/MachineStatus/MachineStatus";
import { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { setAppDataStore } from "redux/actions/appActions";
import { setUserDataStore } from "redux/actions/userActions";
import {
  apiCallForGetAppSetting,
  apiCallForGetBufferGanttData,
  apiCallForPostBufferGanttData,
} from "services/apiCall";
import { LS_ITEMS, STATUS_COLORS, SYS_INTERVAL } from "services/CONSTANTS";
import {
  appData,
  getLSJson,
  isNull,
  isValid,
  secondToTime,
  setAppData,
  setUserData,
  userData,
} from "services/global";
import "./MainLayout.css";

const MainLayout = (props) => {
  const dispatch = useDispatch();
  const { appDataStore } = useSelector((x) => x.appDataStore);
  const { customer_id, machine_id, machine_status, current_ganttdata } =
    appDataStore;

  const [showDowntimeReasonView, setShowDowntimeReasonView] = useState(false);

  const [intervalId, setIntervalID] = useState(null);
  const [tick, setTick] = useState(false);
  const timer = () => setTick((t) => !t);
  useEffect(() => {
    apiCallForGetBufferGanttData(customer_id, machine_id)
      .then((res) => {
        if (isValid(res.buffer_data) && isValid(res.last_data)) {
          let oneGantt = res.buffer_data.find(
            (x) => x.machine_id == machine_id && x.customer_id == customer_id
          );

          let newAppData = {
            ...appData,
            ...{ current_ganttdata: oneGantt, last_ganttdata: res.last_data },
          };
          setAppData(newAppData);
          dispatch(setAppDataStore(newAppData));
        }
      })
      .catch((err) => {});
  }, [tick, machine_id]);

  useEffect(() => {
    loadLSUserData();
    loadLSAppData();

    clearInterval(intervalId);
    setIntervalID(setInterval(timer, SYS_INTERVAL));
    return () => {
      clearInterval(intervalId);
    };
  }, []);

  useEffect(() => {
    if (isValid(customer_id) && isValid(machine_id)) {
      apiCallForGetAppSetting(customer_id, machine_id)
        .then((res) => {
          let newAppData = {
            ...appData,
            ...{
              machine_picture_url: res["machine_picture_url"],
              machine_status: [
                res.downtime_reason1,
                res.downtime_reason2,
                res.downtime_reason3,
                res.downtime_reason4,
                res.downtime_reason5,
                res.downtime_reason6,
                res.downtime_reason7,
                res.downtime_reason8,
                "Idle-Uncategorized",
                "In Cycle",
                "Offline",
              ],
              time_stop: secondToTime(res["time_stop"] * 1000),
            },
          };
          setAppData(newAppData);
          dispatch(setAppDataStore(newAppData));
        })
        .catch((err) => {});
    }
  }, [customer_id, machine_id]);

  // Load User Data
  const loadLSUserData = () => {
    setUserData(getLSJson(LS_ITEMS.userData));
    dispatch(setUserDataStore(userData));
  };

  // Load App Data
  const loadLSAppData = () => {
    setAppData(getLSJson(LS_ITEMS.appData));
    dispatch(setAppDataStore(appData));
  };

  const updateMachineStatus = (newStatus) => {
    if (appData.last_ganttdata.status != newStatus) {
      let currentTimestamp = Math.floor(new Date().getTime() / 1000);
      let newAppData = {
        ...appData,
        last_ganttdata: {
          ...appData.last_ganttdata,
          status: newStatus,
          color:
            STATUS_COLORS[
              machine_status.findIndex((x) => x == newStatus) > 0
                ? machine_status.findIndex((x) => x == newStatus)
                : 0
            ],
          start: currentTimestamp,
        },
      };
      setAppData(newAppData);
      dispatch(setAppDataStore(appData));
    }
    apiCallForPostBufferGanttData(
      customer_id,
      machine_id,
      newStatus,
      STATUS_COLORS[
        machine_status.findIndex((x) => x == newStatus) > 0
          ? machine_status.findIndex((x) => x == newStatus)
          : 0
      ]
    )
      .then((res) => {})
      .catch((err) => {});
  };

  if (isNull(current_ganttdata)) {
    return (
      <Row className="dashboard-machine-info-layout" align={"middle"}>
        <Col span={24} style={{ textAlign: "center" }}>
          <Spin size={"large"} spinning={isNull(current_ganttdata)} />
        </Col>
      </Row>
    );
  }

  return (
    <div>
      <div className={showDowntimeReasonView ? "display-none" : "main-layout"}>
        <TopLayout />
        <ContentLayout />
        <BottomLayout
          setShowDowntimeReasonView={setShowDowntimeReasonView}
          updateMachineStatus={updateMachineStatus}
        />
      </div>
      {showDowntimeReasonView && (
        <MachineStatus
          setShowDowntimeReasonView={setShowDowntimeReasonView}
          updateMachineStatus={updateMachineStatus}
        />
      )}
    </div>
  );
};

export default MainLayout;
