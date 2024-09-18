import { Spin } from "antd";
import { useEffect, useState } from "react";
import { connect, useSelector } from "react-redux";
import "./UtilizationDashboardPage.css";

import { UpdateAppConfig } from "../../../src/services/redux/reducers/app";

import {
  GetStatus,
  GetCustomerCurrentTime,
  GetUtilizationData,
  pad,
  readOperatorList,
} from "../../../src/services/common/cnc_apis";

import { CSSTransition } from "react-transition-group";
import UtilizationDetailPage from "./UtilizationDetailPage/UtilizationDetailPage";
import MachineItemContainer from "./MainPage/MachineItemContainer";

function UtilizationDashboardPage(props) {
  const appData = useSelector((state) => state.app);
  const authData = useSelector((x) => x.authService);
  const { security_level } = authData;
  const { langIndex } = appData;
  const { screenSize } = props.app;

  const customer_id = props.app.customer_id;
  const group_id = props.app.group_id;
  const viewMode = props.app.viewMode;

  const customerInfo = props.cncService.customerInfoList[customer_id];
  const machineList = props.cncService.utilizationMachineList[customer_id];

  var isPageLoading = machineList === undefined;

  const [selectedMachine, setSelectedmachine] = useState("");
  const [operatorList, setOperatorList] = useState([]);

  const [tick, setTick] = useState(true);
  const [intervalId, setIntervalID] = useState(null);
  const intervalTime = 60000;

  const timezone = customerInfo["timezone"];

  let startDate = GetCustomerCurrentTime(timezone);
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

  useEffect(() => {
    GetStatus(customer_id, props.dispatch);
  }, [customer_id]);

  const onSetAutoScroll = (val) => {
    UpdateAppConfig({ isAutoScroll: val }, props.dispatch);
  };

  const timer = () => {
    setTick((t) => !t);
  };

  const onClickMachine = (machine) => {
    onSetAutoScroll(false);
    setSelectedmachine(machine);
  };

  useEffect(() => {
    setSelectedmachine("");

    try {
      clearInterval(intervalId);
    } catch (_) { }

    readOperatorList((res) => {
      setOperatorList(res);
      GetUtilizationData(
        customer_id,
        timezone,
        startDate,
        endDate,
        "",
        props.dispatch
      );
      setIntervalID(setInterval(timer, intervalTime));
    });

    return () => {
      clearInterval(intervalId);
    };
  }, [customer_id]);

  useEffect(() => {
    GetUtilizationData(
      customer_id,
      timezone,
      startDate,
      endDate,
      "",
      props.dispatch
    );
  }, [tick]);

  return (
    <div>
      {isPageLoading && (
        <div style={{ paddingTop: 100, textAlign: "center" }}>
          <Spin tip="Loading ..." size="large" />
        </div>
      )}
      {!isPageLoading && (
        <div>
          {selectedMachine == "" && (
            <MachineItemContainer
              machineList={machineList}
              customer_id={customer_id}
              group_id={group_id}
              customerInfo={customerInfo}
              onClickMachine={onClickMachine}
              viewMode={viewMode}
              operatorList={operatorList}
              screenSize={screenSize}
            />
          )}

          <CSSTransition
            in={selectedMachine !== ""}
            timeout={1000}
            classNames="pageSliderLeft"
            unmountOnExit={true}
          >
            <UtilizationDetailPage
              customer_id={customer_id}
              customerInfo={customerInfo}
              machineInfo={selectedMachine}
              security_level={security_level}
              onCloseMachine={setSelectedmachine}
              operatorList={operatorList}
              screenSize={screenSize}
              dismissible
            />
          </CSSTransition>
        </div>
      )}
    </div>
  );
}

const mapStateToProps = (state, props) => ({
  cncService: state.cncService,
  app: state.app,
});

const mapDispatchToProps = (dispatch, props) => ({
  dispatch: dispatch,
});

export default connect(mapStateToProps, mapDispatchToProps)(UtilizationDashboardPage);
