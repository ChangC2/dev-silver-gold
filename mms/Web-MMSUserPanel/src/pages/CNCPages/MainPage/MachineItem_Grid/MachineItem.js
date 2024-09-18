import { Avatar, Col, Row, Tooltip } from "antd";
import { useEffect, useState } from "react";
import "./MachineItem.css";

import { UserOutlined } from "@ant-design/icons";
import { MACHINE_IMAGE_BASE_URL } from "../../../../services/common/urls";

import { useSelector } from "react-redux";
import { sizeMobile } from "../../../../services/common/constants";
import Urls, { postRequest } from "../../../../services/common/urls";
import lang from "../../../../services/lang";
import "./glowEffect.scss";
import MachineCalculation from "./MachineCalculation";
import OneGantt from "./OneGantt";
import VariablesWidget from "./VariablesWidget/VariablesWidget";

import { Spin } from "antd";

function MachineItem(props) {
  const { langIndex } = useSelector((x) => x.app);
  const { machineInfo, operatorList, screenSize, partId } = props;

  const { customer_id } = props;
  const { customerInfo } = props;
  const { onClickMachine } = props;

  const [appSetting, setAppSetting] = useState({ gantt_chart_display: -1 });

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
  }, [customer_id, machineInfo]);

  let statusName = machineInfo["status"];
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

  return screenSize.width >= sizeMobile ? ( // desktop Version
    <Col span={24}>
      <div
        className={"machine-item-style border-glow"}
        style={{
          margin: 15,
          border: 2,
          borderStyle: "solid",
          borderColor: machineInfo["color"],
        }}
      >
        <Row align="middle" style={{ marginLeft: 10 }}>
          <Col span={8}>
            <Row align={"middle"} onClick={() => onClickMachine(machineInfo)}>
              <Col span={7}>
                <div>
                  <img
                    className="machine-image"
                    src={
                      machineInfo["machine_picture_url"].includes("http")
                        ? machineInfo["machine_picture_url"]
                        : MACHINE_IMAGE_BASE_URL +
                          machineInfo["machine_picture_url"]
                    }
                  />
                </div>
              </Col>
              <Col span={7}>
                {partId == undefined && (
                  <div className="metric-time-range-value">
                    {machineInfo["machine_id"]}
                  </div>
                )}

                {partId != undefined && (
                  <div
                    style={{
                      color: "white",
                      textAlign: "center",
                      fontSize: 16,
                    }}
                  >
                    <div className="metric-time-range-value">
                      Part Number <br /> {partId}
                    </div>
                  </div>
                )}
              </Col>
              <Col span={10}>
                <div style={{ textAlign: "center" }}>
                  {
                    <img
                      className="main-operator-image-style"
                      style={{
                        width: 120,
                        height: 120,
                        padding: 10,
                        marginTop: 5,
                        marginBottom: 5,
                      }}
                      src={operatorImage}
                    />
                  }
                  <div className="metric-time-range-value">{operatorName}</div>
                </div>
              </Col>
            </Row>
          </Col>
          <Col span={3}>
            <div className="metric-time-range-title">Metric Time Range</div>
            <div className="metric-time-range-value">
              {machineInfo["shift_info"]}
            </div>
          </Col>
          <Col span={4}>
            <Tooltip
              title={lang(langIndex, "cnc_click_to_check_other_variable")}
            >
              <div style={{ width: "100%" }}>
                <VariablesWidget
                  machineInfo={machineInfo}
                  langIndex={langIndex}
                  isMobile={false}
                  customer_id={customer_id}
                />
              </div>
            </Tooltip>
          </Col>
          <Col span={6}>
            {appSetting.gantt_chart_display == -1 && (
              <div style={{ width: "100%", textAlign: "center" }}>
                <Spin size="medium"></Spin>
              </div>
            )}
            {appSetting.gantt_chart_display > -1 &&
              appSetting.gantt_chart_display < 2 && (
                <OneGantt
                  ganttInfo={machineInfo["gantt"]}
                  machineInfo={machineInfo}
                  customerInfo={customerInfo}
                />
              )}
            {appSetting.gantt_chart_display == 2 && (
              <MachineCalculation
                machineInfo={machineInfo}
                customer_id={customer_id}
                customerInfo={customerInfo}
                appSetting={appSetting}
                marginTop={10}
                currentDate=""
              />
            )}
          </Col>
          <Col span={3} style={{ textAlign: "center" }}>
            {
              <div style={{ textAlign: "center" }}>
                <div className="main-machine-status-led-container-style">
                  <a
                    className={"led-one-line-style col-glow"}
                    style={{ color: machineInfo["color"] }}
                  >
                    &#8226;
                  </a>
                </div>
                <div>
                  <h6 className="main-machine-status-style">{statusName}</h6>
                </div>
              </div>
            }
          </Col>
        </Row>
      </div>
    </Col>
  ) : (
    // Mobile Version
    <Col span={24}>
      <div
        className={"machine-item-style border-glow"}
        style={{
          margin: 5,
          border: 2,
          borderStyle: "solid",
          borderColor: machineInfo["color"],
        }}
      >
        <Row align="middle" onClick={() => onClickMachine(machineInfo)}>
          <Col span={4}>
            <div>
              <img
                className="machine-image"
                src={
                  machineInfo["machine_picture_url"].includes("http")
                    ? machineInfo["machine_picture_url"]
                    : MACHINE_IMAGE_BASE_URL +
                      machineInfo["machine_picture_url"]
                }
              />
            </div>
          </Col>
          <Col span={4}>
            <h5 style={{ color: "white", textAlign: "center" }}>
              {partId == undefined ? machineInfo["machine_id"] : partId}
            </h5>
          </Col>
          <Col span={5}>
            <div style={{ textAlign: "center" }}>
              {operatorImage == "" ? (
                <Avatar icon={<UserOutlined />} />
              ) : (
                <img
                  style={{ width: 48, height: 48 }}
                  className="main-operator-image-style"
                  src={operatorImage}
                />
              )}
              <h5 style={{ color: "white", textAlign: "center" }}>
                {operatorName}
              </h5>
            </div>
          </Col>
          <Col span={6}>
            <div style={{ width: "100%" }}>
              <VariablesWidget
                machineInfo={machineInfo}
                langIndex={langIndex}
                isMobile={true}
              />
            </div>
          </Col>
          <Col span={5} style={{ textAlign: "center" }}>
            {
              <div style={{ textAlign: "center" }}>
                <div className="main-machine-status-led-container-style">
                  <a
                    style={{ color: machineInfo["color"] }}
                    className={"led-one-line-style col-glow"}
                  >
                    &#8226;
                  </a>
                </div>
                <div>
                  <h6
                    className="main-machine-status-style"
                    style={{ fontWeight: "normal" }}
                  >
                    {statusName}
                  </h6>
                </div>
              </div>
            }
          </Col>
        </Row>
      </div>
    </Col>
  );
}

export default MachineItem;
