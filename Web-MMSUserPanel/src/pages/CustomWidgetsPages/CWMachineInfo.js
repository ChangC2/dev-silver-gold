import { Col, Row, Select, Spin } from "antd";
import { useEffect, useState } from "react";

import { cwDataPoints } from "services/common/constants";
import { getShiftList } from "services/common/cnc_apis";
import { MACHINE_IMAGE_BASE_URL } from "services/common/urls";
import "./CWMachineInfo.css";

const { Option } = Select;

const CWMachineInfo = (props) => {
  const { widget_info, darkmode, customerInfo } = props;
  const [isLoading, setIsLoading] = useState(false);
  const [shiftList, setShiftList] = useState([]);

  const [data, setData] = useState([]);
  const [machineImage, setMachineImage] = useState("");

  const dataPoints = widget_info.data_points.split(",");

  useEffect(() => {
    setIsLoading(true);
    const param = {
      customer_id: widget_info.customer_id,
      machine: widget_info.machine,
      operator: widget_info.operator,
      jobID: widget_info.jobID,
      days: widget_info.days,
      timezone: customerInfo["timezone"],
    };

    getShiftList(param, (res) => {
      setIsLoading(false);
      if (res !== null) {
        setShiftList(res.shifts);
      }
    });
  }, [widget_info]);

  useEffect(() => {
    setChartData();
  }, [shiftList]);

  const setChartData = () => {
    let chartData = [];
    let goodParts = 0;
    let badParts = 0;
    let oee = 0;
    let availability = 0;
    let quality = 0;
    let performance = 0;
    let inCycle = 0;

    for (let i = 0; i < shiftList.length; i++) {
      const shift = shiftList[i];
      goodParts += parseInt(shift["goodParts"]);
      badParts += parseInt(shift["badParts"]);
      oee += parseInt(shift["oee"]);
      availability += parseInt(shift["availability"]);
      quality += parseInt(shift["quality"]);
      performance += parseInt(shift["performance"]);
      inCycle += parseInt(shift["inCycle"]);
    }

    for (let i = 0; i < cwDataPoints.length; i++) {
      if (dataPoints[i] === "1") {
        let dataPoint = cwDataPoints[i];
        let value = 0;
        let className = darkmode
          ? "cwmachineinfo-content-darkmode"
          : "cwmachineinfo-content";
        switch (i) {
          case 0:
            value = goodParts;
            className = "cwmachineinfo-goods";
            break;
          case 1:
            className = "cwmachineinfo-bads";
            value = badParts;
            break;
          case 2:
            value = oee;
            break;
          case 3:
            value = availability;
            break;
          case 4:
            value = quality;
            break;
          case 5:
            value = performance;
            break;
          case 6:
            dataPoint = "Total Cycle Time Today"
            let hh = parseInt(inCycle / 3600 / 1000);
            let mm = parseInt((inCycle - hh * 3600 * 1000) / 60 / 1000);
            let ss = parseInt(
              (inCycle - hh * 3600 * 1000 - mm * 60 * 1000) / 1000
            );
            if (hh > 0) {
              value = "" + hh + " Hrs  " + mm + " Min  " + ss + " Sec";
            } else {
              if (mm > 0) {
                value = "" + mm + " Min  " + ss + " Sec";
              } else {
                value = ss + " Sec";
              }
            }
            break;
          default:
            break;
        }
        chartData.push({
          data_point: dataPoint,
          value: value,
          className: className,
        });
      }
    }
    setData(chartData);
  };

  const mainUI = data.map((info) => {
    return (
      <div key={`info-${info.data_point}`} className={info.className}>
        {info.data_point}
        <span className="backspace" />:<span className="backspace" />
        {info.value}
      </div>
    );
  });

  return (
    <Spin spinning={isLoading}>
      <div>
        {!darkmode && (
          <img
            className="cwmachineinfo-logo"
            src={
              widget_info["machineImage"] == "" || widget_info["machineImage"] == undefined
                ? MACHINE_IMAGE_BASE_URL + "blank machine.png"
                : widget_info["machineImage"].includes("http")
                ? widget_info["machineImage"]
                : MACHINE_IMAGE_BASE_URL + widget_info["machineImage"]
            }
          />
        )}

        <Row className="cwmachineinfo-title-container" align="center">
          <Col
            className={
              darkmode ? "cwmachineinfo-title-darkmode" : "cwmachineinfo-title"
            }
          >
            {widget_info.machine}
          </Col>
        </Row>
        <Row className="cwmachineinfo-content-container" align="center">
          <Col>
            <div
              className={
                darkmode
                  ? "cwmachineinfo-content-darkmode"
                  : "cwmachineinfo-content"
              }
            >
              Operator
              <span className="backspace" />:<span className="backspace" />
              {widget_info.operator}
            </div>
            <div
              className={
                darkmode
                  ? "cwmachineinfo-content-darkmode"
                  : "cwmachineinfo-content"
              }
            >
              JobID
              <span className="backspace" />:<span className="backspace" />
              {widget_info.jobID}
            </div>
            {mainUI}
          </Col>
        </Row>
      </div>
    </Spin>
  );
};

export default CWMachineInfo;
