import { Col, Row, Select, Spin } from "antd";
import { useEffect, useState } from "react";

import { cwDataPoints } from "services/common/constants";
import { getShiftList } from "services/common/cnc_apis";
import "./CWTextInfo.css";

const { Option } = Select;

const CWTextInfo = (props) => {
  const { widget_info, darkmode, customerInfo } = props;
  const [isLoading, setIsLoading] = useState(false);
  const [shiftList, setShiftList] = useState([]);

  const [data, setData] = useState([]);

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
        const dataPoint = cwDataPoints[i];
        let value = 0;
        let className = darkmode
          ? "cwtextinfo-content-darkmode"
          : "cwtextinfo-content";
        switch (i) {
          case 0:
            value = goodParts;
            className = "cwtextinfo-goods";
            break;
          case 1:
            className = "cwtextinfo-bads";
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
      <Row className="cwtextinfo-title-container" align="center">
        <Col
          className={
            darkmode ? "cwtextinfo-title-darkmode" : "cwtextinfo-title"
          }
        >
          {widget_info.machine}
        </Col>
      </Row>
      <Row className="cwtextinfo-content-container" align="center">
        <Col>{mainUI}</Col>
      </Row>
    </Spin>
  );
};

export default CWTextInfo;
