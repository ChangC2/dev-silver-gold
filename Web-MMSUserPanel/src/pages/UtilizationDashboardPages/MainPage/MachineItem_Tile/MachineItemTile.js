
import { Avatar, Col, Row } from "antd";
import "./MachineItemTile.css";

import { UserOutlined } from "@ant-design/icons";
import GaugeChart from "react-gauge-chart/dist/GaugeChart";
import { MACHINE_IMAGE_BASE_URL } from "../../../../../src/services/common/urls";

import { useSelector } from "react-redux";
import { sizePad } from "../../../../../src/services/common/constants";
import lang from "../../../../../src/services/lang";

function MachineItemTile(props) {
  const { langIndex } = useSelector((x) => x.app);
  const { machineInfo, operatorList, statusList, screenSize } = props;
  const { hstList } = props;
  const { ganttList } = props;
  const { onClickMachine } = props;

  let ledClassName = "col-000000";
  let hstInfo = undefined;
  let ganttInfo = undefined;
  let operatorName = "Guest";
  let operatorImage = "";
  let statusName = "Offline";
  if (hstList.length > 0) {
    hstInfo = hstList.find(
      (hst) => hst["machine_id"] === machineInfo["machine_id"]
    );
  }
  if (ganttList !== undefined && ganttList.length > 0) {
    ganttInfo = ganttList.filter(
      (gantt) => gantt["machine_id"] === machineInfo["machine_id"]
    );

    if (
      ganttInfo !== undefined &&
      ganttInfo.length > 0 &&
      ganttInfo[ganttInfo.length - 1] !== null
    ) {
      var color = ganttInfo[ganttInfo.length - 1]["color"];
      ledClassName = "col-" + color.replace("#", "");
    }
  }
  if (statusList.length > 0) {
    try {
      var tmp = statusList.filter(
        (x) =>
          x.machine_id.toLowerCase().trim() ===
          machineInfo["machine_id"].toLowerCase().trim()
      );

      if (tmp.length > 0) {
        var tmpStatus = tmp[0];
        if (tmpStatus.status.toLowerCase().trim() === "offline") {
          statusName = lang(langIndex, "cnc_offline");
          ledClassName = "col-000000";
        }
        if (operatorList.length > 0) {
          var tmpOperator = operatorList.filter(
            (x) =>
              x.username_full.toLowerCase().trim() ===
              tmpStatus["Operator"].toLowerCase().trim()
          );
          if (tmpOperator.length > 0) {
            operatorImage = tmpOperator[0].user_picture;
            operatorName = tmpOperator[0].username;
          }
        }
      }
    } catch (err) { }
  }

  // compare last gantt and current time, and set the status of machine
  if (ganttInfo !== undefined && ganttInfo.length > 0) {
    // get last gantt data
    var lastGanttTime = ganttInfo[ganttInfo.length - 1].end;
    var currentTime = Math.round(new Date().getTime() / 1000);
    if (currentTime - lastGanttTime > 60 * 5) {
      statusName = lang(langIndex, "cnc_offline");
      ledClassName = "col-000000";
    }
  }



  return screenSize.width >= sizePad ? (
    <Col span={4}>
      <div className="machine-item-style">
        <Row align="middle" onClick={() => onClickMachine(machineInfo)}>
          <Col span={24} style={{ textAlign: "center" }}>
            <div style={{ height: 100 }}>
              <img
                className="machine-image-tile"
                src={
                  machineInfo["machine_picture_url"].includes("http")
                    ? machineInfo["machine_picture_url"]
                    : MACHINE_IMAGE_BASE_URL +
                    machineInfo["machine_picture_url"]
                }
                style={{ maxHeight: 100 }}
              />
            </div>
          </Col>
          <Col span={24}>
            <h4 style={{ color: "white", textAlign: "center" }}>
              {machineInfo["machine_id"]}
            </h4>
          </Col>
          <Col span={12}>
            <div style={{ textAlign: "center" }}>
              {operatorImage === "" ? (
                <Avatar size={"middle"} icon={<UserOutlined />} />
              ) : (
                <img
                  style={{ width: 30, height: 30 }}
                  className="main-tile-operator-image-style"
                  src={operatorImage}
                />
              )}
            </div>
          </Col>
          <Col span={12}>
            <h4 style={{ color: "white", textAlign: "center" }}>
              {/* {machineInfo['Operator']} */}
              {operatorName}
            </h4>
          </Col>
          <Col span={18} style={{ marginTop: 10 }}>
            <div style={{ width: "100%" }}>
              <GaugeChart
                id={"gauge_" + machineInfo["id"]}
                nrOfLevels={20}
                colors={[
                  "#ff0000",
                  "#ff0000",
                  "#ff0000",
                  "#FFFF00",
                  "#FFFF00",
                  "#00ff00",
                  "#00ff00",
                ]}
                arcWidth={0.3}
                percent={
                  hstInfo !== undefined
                    ? parseFloat(hstInfo["Utilization"]) / 100.0
                    : 0
                }
              />
            </div>
          </Col>
          <Col span={6} style={{ textAlign: "center" }}>
            <a className={"led-one-tile-style " + ledClassName}>&#8226;</a>
          </Col>
        </Row>
      </div>
    </Col>
  ) : (
    <Col span={8}>
      <div className="machine-item-style">
        <Row align="middle" onClick={() => onClickMachine(machineInfo)}>
          <Col span={24} style={{ textAlign: "center" }}>
            <div style={{ height: 70 }}>
              <img
                className="machine-image-tile"
                src={
                  machineInfo["machine_picture_url"].includes("http")
                    ? machineInfo["machine_picture_url"]
                    : MACHINE_IMAGE_BASE_URL +
                    machineInfo["machine_picture_url"]
                }
                style={{ maxHeight: 70 }}
              />
            </div>
          </Col>
          <Col span={24}>
            <h4 style={{ color: "white", textAlign: "center" }}>
              {machineInfo["machine_id"]}
            </h4>
          </Col>
          <Col span={12}>
            <div style={{ textAlign: "center" }}>
              {operatorImage === "" ? (
                <Avatar size={"middle"} icon={<UserOutlined />} />
              ) : (
                <img
                  style={{ width: 30, height: 30 }}
                  className="main-tile-operator-image-style"
                  src={operatorImage}
                />
              )}
            </div>
          </Col>
          <Col span={12}>
            <h4 style={{ color: "white", textAlign: "center" }}>
              {/* {machineInfo['Operator']} */}
              {operatorName}
            </h4>
          </Col>


          <Col span={18} style={{ marginTop: 10 }}>
            <div style={{ width: "100%" }}>
              <GaugeChart
                id={"gauge_" + machineInfo["id"]}
                nrOfLevels={20}
                colors={[
                  "#ff0000",
                  "#ff0000",
                  "#ff0000",
                  "#FFFF00",
                  "#FFFF00",
                  "#00ff00",
                  "#00ff00",
                ]}
                arcWidth={0.3}
                percent={
                  hstInfo !== undefined
                    ? parseFloat(hstInfo["Utilization"]) / 100.0
                    : 0
                }
              />
            </div>
          </Col>
          <Col span={6} style={{ textAlign: "center" }}>
            <a className={"led-one-tile-style " + ledClassName}>&#8226;</a>
          </Col>
        </Row>
      </div>
    </Col>
  );
}

export default MachineItemTile;
