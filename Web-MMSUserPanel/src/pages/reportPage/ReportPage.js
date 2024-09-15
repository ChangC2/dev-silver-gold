import React, { useState, useEffect } from "react";
import moment from "moment";
import { Button, Row, Col, Divider, Spin } from "antd";
import "./ReportPage.css";
import MachineContainer from "./MachineContainer/MachineContainer";
import FactoryContainer from "./FactoryContainer/FactoryContainer";
import ReportConfiguration from "./ReportConfiguration/ReportConfiguration";

import GeneratePDF from "./ReportConfiguration/GeneratePDF/GeneratePDF";
import ReportPeriod from "./ReportConfiguration/ReportPeriod/ReportPeriod";
import ExportCSVConfiguration from "./ExportCSVConfiguration/ExportCSVConfiguration";
import ExportCSVButton from "./ExportCSVConfiguration/ExportCSVButton/ExportCSVButton";
import {
  GetCustomerCurrentTime,
  GetOperatorList,
  GetStatus,
} from "../../services/common/cnc_apis";
import { connect, useSelector } from "react-redux";
import lang from "../../services/lang";

function ReportPage(props) {
  const { langIndex } = useSelector((x) => x.app);
  const { screenSize } = props.app;
  const [selectedMachine, setSelectedMachine] = useState([]);
  const [selectedOperator, setSelectedOperator] = useState([]);
  const [operatorList, setOperatorList] = useState([]);
  const [fromReportDate, setFromReportDate] = useState("");
  const [toReportDate, setToReportDate] = useState("");
  const [selectedEmails, setSelectedEmails] = useState([]);
  const [isGeneratingPDF, setIsGeneratingPDF] = useState(false);

  const customer_id = props.app.customer_id;
  useEffect(() => {
    GetStatus(customer_id, props.dispatch);
  }, [customer_id]);
  const customerInfo = props.cncService.customerInfoList[customer_id];
  const machineList = props.cncService.machineInfoList[customer_id];
  var isPageLoading = machineList == undefined;

  useEffect(() => {
    GetOperatorList(customer_id, (operatorList) => {
      if (operatorList != null && operatorList.length > 0) {
        operatorList.forEach(function (operator) {
          operator.key = operator.Id;
        });
        setOperatorList(operatorList);
      } else {
      }
    });
  }, []);

  useEffect(() => {
    var tmpEmails = customerInfo.emails.split(";");
    var emailList = [];
    for (var i = 0; i < tmpEmails.length; i++) {
      var email = tmpEmails[i].split(":")[0];
      if (email != "") {
        emailList.push(email);
      }
    }
    setSelectedEmails(emailList);
    if (customerInfo["timezone"] != undefined) {
      const timezone = customerInfo["timezone"];
      let _cusTime = GetCustomerCurrentTime(timezone);
      const dateFormat = "MM/DD/YYYY";
      let _cusTimeString = moment(_cusTime).format(dateFormat);
      setToReportDate(_cusTimeString);
      setFromReportDate(_cusTimeString);
    }
  }, [customerInfo]);

  return (
    <div className="report-container">
      {isPageLoading && (
        <div
          style={{ paddingTop: 100, textAlign: "center" }}
        >
          <Spin tip="Loading ..." size="large" />
        </div>
      )}
      {!isPageLoading && (
        <Row>
          <Col span={9}>
            <MachineContainer
              customer_id={customer_id}
              customerInfo={customerInfo}
              machineList={machineList}
              operatorList={operatorList}
              selectedMachine={selectedMachine}
              setSelectedMachine={setSelectedMachine}
              selectedOperator={selectedOperator}
              setSelectedOperator={setSelectedOperator}
            />
          </Col>
          <Col span={1} style={{ textAlign: "left" }}>
            <Divider
              type="vertical"
              style={{
                height: "100%",
                color: "#eeeeee",
                borderWidth: 2,
                borderColor: "#eeeeee",
              }}
            ></Divider>
          </Col>
          <Col span={14}>
            <div className="data-operation-page">
              <div className="data-period-container">
                <div>
                  <h3 style={{ color: "#eeeeee" }}>
                    {lang(langIndex, "report_reportperiod")}:
                  </h3>
                </div>
                <div>
                  <ReportPeriod
                    customerInfo={customerInfo}
                    fromReportDate={fromReportDate}
                    setFromReportDate={setFromReportDate}
                    toReportDate={toReportDate}
                    setToReportDate={setToReportDate}
                    selectedEmails={selectedEmails}
                  />
                </div>
              </div>

              <div className="report-section-container">
                <ReportConfiguration
                  customerInfo={customerInfo}
                  fromReportDate={fromReportDate}
                  setFromReportDate={setFromReportDate}
                  toReportDate={toReportDate}
                  setToReportDate={setToReportDate}
                  selectedEmails={selectedEmails}
                  setSelectedEmails={setSelectedEmails}
                  screenSize={screenSize}
                />
                <div style={{ marginLeft: 50 }}>
                  <GeneratePDF
                    customer_id={customer_id}
                    fromReportDate={fromReportDate}
                    toReportDate={toReportDate}
                    selectedEmails={selectedEmails}
                    selectedMachine={selectedMachine}
                    selectedOperator={selectedOperator}
                    isGeneratingPDF={isGeneratingPDF}
                    setIsGeneratingPDF={setIsGeneratingPDF}
                    customerInfo={customerInfo}
                    machineInfo={machineList.filter((x) =>
                      selectedMachine.includes(x.machine_id)
                    )}
                  />
                </div>
              </div>
              <div className="export-to-csv-section-container">
                <ExportCSVConfiguration
                  customerInfo={customerInfo}
                  fromReportDate={fromReportDate}
                  setFromReportDate={setFromReportDate}
                  toReportDate={toReportDate}
                  setToReportDate={setToReportDate}
                  selectedEmails={selectedEmails}
                  setSelectedEmails={setSelectedEmails}
                  screenSize={screenSize}
                />
                <div style={{ marginLeft: 50 }}>
                  <ExportCSVButton
                    customer_id={customer_id}
                    fromReportDate={fromReportDate}
                    toReportDate={toReportDate}
                    selectedEmails={selectedEmails}
                    selectedMachine={selectedMachine}
                    selectedOperator={selectedOperator}
                    isGeneratingPDF={isGeneratingPDF}
                    setIsGeneratingPDF={setIsGeneratingPDF}
                    customerInfo={customerInfo}
                    machineInfo={machineList.filter((x) =>
                      selectedMachine.includes(x.machine_id)
                    )}
                  />
                </div>
              </div>
            </div>
          </Col>
        </Row>
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

export default connect(mapStateToProps, mapDispatchToProps)(ReportPage);
