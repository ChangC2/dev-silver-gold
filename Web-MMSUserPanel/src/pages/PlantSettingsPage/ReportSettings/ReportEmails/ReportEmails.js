import React, { useState, useEffect } from "react";
import "./ReportEmails.css";
import { Col, Input, Row, message } from "antd";
import OneEmail from "./OneEmail/OneEmail";
import { withRouter } from "react-router-dom";
import { useSelector } from "react-redux";
import lang from "../../../../services/lang";

function ReportEmails(props) {
  const { langIndex } = useSelector((x) => x.app);
  const { customerInfo, updateCustomerInfo } = props;
  const [emailList, setEmailList] = useState([]);
  const [typingEmail, setTypingMail] = useState("");
  useEffect(() => {
    if (customerInfo.emails == null) {
      customerInfo.emails = "";
    }
    var tmpList = customerInfo.emails.split(";");
    var mails = [];
    for (var i = 0; i < tmpList.length; i++) {
      var tmp = tmpList[i].split(":")[0];
      if (tmp != "") mails.push(tmpList[i].split(":")[0]);
    }
    setEmailList(mails);
  }, []);

  const deleteEmail = (email) => {
    var tmpList = emailList;
    tmpList = tmpList.filter((e) => e != email);
    setEmailList(tmpList);
    onUpdateReportDays(tmpList);
  };

  const addMail = () => {
    if (typingEmail == "") return;
    if (typingEmail.includes("@") && typingEmail.includes(".")) {
      var tmpList = emailList;
      tmpList.push(typingEmail);
      setEmailList(tmpList);
      setTypingMail("");
      onUpdateReportDays(tmpList);
    } else {
      message.warning("Please type valid email.");
    }
  };

  const onUpdateReportDays = (newList) => {
    var emails = newList.join(";");
    let newInfo = customerInfo;
    newInfo.emails = emails;
    updateCustomerInfo(newInfo);
  };

  const emailUI = emailList.map((email, index) => {
    return <OneEmail key={index} email={email} deleteEmail={deleteEmail} />;
  });

  return (
    <div className="customer-setting-container-style">
      <div className="app-setting-text-input-title">
        {" "}
        {lang(langIndex, "plant_reportemail")}
      </div>

      <div className="reportemails-mail-input-style"></div>

      <Row>
        <Col flex={"auto"}>
          <input
            className={"app-setting-text-input-value"}
            value={typingEmail}
            onChange={(e) => setTypingMail(e.target.value)}
            type={"email"}
          />
        </Col>
        <Col flex="50px">
          <div className="reportemails-mail-add-button-style" onClick={addMail}>
            +
          </div>
        </Col>
      </Row>
      <div className="reportemails-maillist-container-style">{emailUI}</div>
    </div>
  );
}

export default withRouter(ReportEmails);
