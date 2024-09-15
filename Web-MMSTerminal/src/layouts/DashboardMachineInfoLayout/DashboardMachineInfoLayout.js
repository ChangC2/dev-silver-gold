import { Col, Row, Spin, message } from "antd";
import { Parser } from "html-to-react";
import InputModeModal from "layouts/InputModeModal/InputModeModal";
import { useEffect, useState } from "react";
import { useSelector } from "react-redux";
import { apiCallForGetJobData } from "services/apiCall";
import { appData, factoryData } from "services/global";
import jobAttachsIcon from "../../assets/icons/ic_job_attachs.png";
import jobGuideIcon from "../../assets/icons/ic_job_guides.png";
import jobidEditIcon from "../../assets/icons/ic_jobid_edit.png";
import "./DashboardMachineInfoLayout.css";
import JobIDModal from "./JobIDModal/JobIDModal";

const DashboardMachineInfoLayout = (props) => {
  const [isSpinning, setIsSpinning] = useState(false);

  const [showInputMode, setShowInputMode] = useState(false);
  const [showJobIDInput, setShowJobIDInput] = useState(false);
  const { appDataStore } = useSelector((x) => x.appDataStore);
  const { factoryDataStore } = useSelector((x) => x.factoryDataStore);

  const [html, setHTML] = useState(
    "Customer : <b></b><br>" +
      "Part Number : <b></b><br>" +
      "Program Number : <b></b><br>" +
      "Description : <b></b><br>" +
      "Parts Per Cycle : <b></b><br>" +
      "Target Cycle Time : <b></b><br>" +
      "Qty Required : <b></b><br>" +
      "Qty Good Completed : <b></b><br>" +
      "Aux1 : <b></b><br>" +
      "Aux2 : <b></b><br>" +
      "Aux3 : <b></b><br>"
  );

  const setHTMLString = (jobData) => {
    if (jobData !== undefined && jobData.length > 0) {
      let htmlString =
        "Customer : <b>" +
        (jobData[0]["customer"] === null ? "" : jobData[0]["customer"]) +
        "</b><br>" +
        "Part Number : <b>" +
        (jobData[0]["partNumber"] === null ? "" : jobData[0]["partNumber"]) +
        "</b><br>" +
        "Program Number : <b>" +
        (jobData[0]["programNumber"] === null
          ? ""
          : jobData[0]["programNumber"]) +
        "</b><br>" +
        "Description : <b>" +
        (jobData[0]["description"] === null ? "" : jobData[0]["description"]) +
        "</b><br>" +
        "Parts Per Cycle : <b>" +
        (jobData[0]["partsPerCycle"] === null
          ? "0"
          : jobData[0]["partsPerCycle"]) +
        "</b><br>" +
        "Target Cycle Time : <b>" +
        (jobData[0]["targetCycleTime"] === null
          ? "00:00:00"
          : jobData[0]["targetCycleTime"]) +
        "</b><br>" +
        "Qty Required : <b>" +
        (jobData[0]["qtyRequired"] === null ? "" : jobData[0]["qtyRequired"]) +
        "</b><br>" +
        "Qty Good Completed : <b>" +
        (jobData[0]["qtyGoodCompleted"] === null
          ? ""
          : jobData[0]["qtyGoodCompleted"]) +
        "</b><br>" +
        "Aux1 : <b>" +
        (jobData[0]["aux1data"] === null ? "" : jobData[0]["aux1data"]) +
        "</b><br>" +
        "Aux2 : <b>" +
        (jobData[0]["aux2data"] === null ? "" : jobData[0]["aux2data"]) +
        "</b><br>" +
        "Aux3 : <b>" +
        (jobData[0]["aux3data"] === null ? "" : jobData[0]["aux3data"]) +
        "</b><br>";

      setHTML(htmlString);
    }
  };

  const onLogoutJobID = () => {
    message.info("onLogoutJobID Clicked");
  };

  const onJobAttachs = () => {
    message.info("onJobAttachs Clicked");
  };

  const onJobGuides = () => {
    message.info("onJobGhides Clicked");
  };

  useEffect(() => {
    if (factoryData.accountId === "" || appData.jobId === "") return;
    setIsSpinning(true);
    apiCallForGetJobData(factoryData.accountId, appData.jobId)
      .then((res) => {
        setHTMLString(res);
        setIsSpinning(false);
      })
      .catch((err) => {
        setIsSpinning(false);
        message.error(err);
      });
  }, [factoryDataStore.accountId, appDataStore.jobId]);

  if (isSpinning) {
    return (
      <Row className="dashboard-machine-info-layout" align={"middle"}>
        <Col span={24} style={{ textAlign: "center" }}>
          <Spin size={"large"} spinning={isSpinning} />
        </Col>
      </Row>
    );
  }

  return (
    <div className="dashboard-machine-info-layout">
      <InputModeModal
        title={"Please Select Job ID Input Mode"}
        showModal={showInputMode}
        setShowModal={setShowInputMode}
        setShowInput={setShowJobIDInput}
        onLogoutJobID={onLogoutJobID}
      />
      <JobIDModal showModal={showJobIDInput} setShowModal={setShowJobIDInput} />
      <Row align="middle" className="dashboard-machine-info-top">
        <Col span={20}>
          <span className="dashboard-machine-info-jobid">
            {"Job ID : "}
            {appDataStore.jobId}
          </span>
        </Col>
        <Col span={4} style={{ textAlign: "right" }}>
          <img
            className="dashboard-machine-info-jobid-edit"
            src={jobidEditIcon}
            onClick={() => setShowInputMode(true)}
            alt="edit"
          />
        </Col>
      </Row>
      <div className="dashboard-machine-info-content">
        {Parser().parse(html)}
      </div>
      <img
        className="dashboard-machine-info-job-guides"
        src={jobGuideIcon}
        onClick={() => onJobGuides()}
        alt="guides"
      />
      <img
        className="dashboard-machine-info-job-attaches"
        src={jobAttachsIcon}
        onClick={() => onJobAttachs()}
        alt="attaches"
      />
    </div>
  );
};

export default DashboardMachineInfoLayout;
