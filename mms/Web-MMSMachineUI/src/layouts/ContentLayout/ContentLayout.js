import { Col, Row, Spin, message } from "antd";
import { useState } from "react";
import { useDispatch, useSelector } from "react-redux";

import DashboardChartLayout from "layouts/DashboardChartLayout/DashboardChartLayout";
import DashboardOEELayout from "layouts/DashboardOEELayout/DashboardOEELayout";
import DashboardUtilizationLayout from "layouts/DashboardUtilizationLayout/DashboardUtilizationLayout";
import { useEffect } from "react";
import { GetCustomerCurrentTime, isValid, pad } from "services/global";
import { isSpinning } from "redux/actions/appActions";
import MachineInfoLayout from "layouts/MachineInfoLayout/MachineInfoLayout";
import { GetOneMachineData } from "services/apiCall";
import { SYS_INTERVAL } from "services/CONSTANTS";
import "./ContentLayout.css";
import FactoryInfoLayout from "layouts/FactoryInfoLayout/FactoryInfoLayout";

const ContentLayout = (props) => {
  const dispatch = useDispatch();
  const { appDataStore } = useSelector((x) => x.appDataStore);
  const { customer_id, machine_id, customer_details } = appDataStore;

  const timezone =
    isValid(customer_details) && isValid(customer_details["timezone"])
      ? customer_details["timezone"]
      : 0;

  const [hstInfo, setHstInfo] = useState({});
  const [ganttInfo, setGanttInfo] = useState([]);
  const [additionalHstInfo, setAdditionalHstInfo] = useState([]);

  const [shifts, setShifts] = useState(["00:00-24:00", "-", "-", "-"]);
  const [shiftStart, setShiftStart] = useState(0);
  const [shiftEnd, setShiftEnd] = useState(0);
  const [shift, setShift] = useState(0);

  const [intervalId, setIntervalID] = useState(null);
  const [tick, setTick] = useState(false);
  const timer = () => setTick((t) => !t);
  useEffect(() => {}, [tick]);

  useEffect(() => {
    clearInterval(intervalId);
    setIntervalID(setInterval(timer, SYS_INTERVAL));
    return () => {
      clearInterval(intervalId);
    };
  }, []);

  useEffect(() => {
    apiCallForGetMachineData(false);
  }, [tick]);

  useEffect(() => {
    apiCallForGetMachineData(true);
  }, [customer_id, machine_id, shift]);

  const apiCallForGetMachineData = (isLoading) => {
    if (!isValid(customer_id) || !isValid(machine_id)) {
      setGanttInfo([]);
      setHstInfo({});
      setShifts(["00:00-24:00", "-", "-", "-"]);
      setShiftStart(0);
      setShiftEnd(0);
    } else {
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

      if (isLoading) {
        dispatch(isSpinning(true));
      }

      GetOneMachineData(
        customer_id,
        timezone,
        startDate,
        endDate,
        machine_id,
        shifts[shift],
        (res) => {
          if (isLoading) {
            dispatch(isSpinning(false));
          }
          if (res.data.machines.length > 0) {
            let _ganttInfo = [...res.data.machines[0].gantt];
            setGanttInfo(_ganttInfo);
            setHstInfo({ ...res.data.machines[0] });

            var _additionalHst = [];
            for (var i = 0; i < _ganttInfo.length; i++) {
              if (
                _ganttInfo[i].status != "Offline" &&
                _ganttInfo[i].start <= res.data.shift_end / 1000 &&
                _ganttInfo[i].end >= res.data.shift_start / 1000
              ) {
                if (
                  _additionalHst.length === 0 ||
                  _additionalHst.filter(
                    (x) => x.status === _ganttInfo[i].status
                  ).length === 0
                ) {
                  _additionalHst.push({
                    status: _ganttInfo[i].status,
                    color: _ganttInfo[i].color,
                    duration: parseFloat(
                      parseFloat(_ganttInfo[i].end) -
                        parseFloat(_ganttInfo[i].start)
                    ),
                  });
                } else {
                  var _hst = _additionalHst.filter(
                    (x) => x.status === _ganttInfo[i].status
                  )[0];
                  _hst.duration += parseFloat(
                    parseFloat(_ganttInfo[i].end) -
                      parseFloat(_ganttInfo[i].start)
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
                (res.data.shift_end - res.data.shift_start) / 1000 / 3600 -
                _ganttInfo[_ganttInfo.length - 1].end / 3600 -
                onlineTime;

              _additionalHst.push({
                status: "Offline",
                color: "#000000",
                duration: offlineTime,
              });
            }

            setAdditionalHstInfo([..._additionalHst]);
            setShifts(["00:00-24:00", ...res.data.shifts]);
            setShiftStart(res.data.shift_start);
            setShiftEnd(res.data.shift_end);
          }
        }
      );
    }
  };

  return (
    <div className="content-layout">
      <Row>
        <Col span={12}>
          <FactoryInfoLayout />
        </Col>
        <Col span={12}>
          <MachineInfoLayout />
        </Col>
      </Row>
      <div className="content-machine-details">
        <DashboardOEELayout
          hstInfo={hstInfo}
          setShift={setShift}
          shift={shift}
          shifts={shifts}
        />
        <DashboardChartLayout
          ganttInfo={ganttInfo}
          shift={shift}
          shifts={shifts}
        />
        <DashboardUtilizationLayout
          additionalHstInfo={additionalHstInfo}
          shift={shift}
          shifts={shifts}
        />
      </div>
    </div>
  );
};

export default ContentLayout;
