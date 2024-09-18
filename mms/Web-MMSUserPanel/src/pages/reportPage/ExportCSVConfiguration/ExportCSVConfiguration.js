import React from "react";
import { useSelector } from "react-redux";
import lang from "../../../services/lang";
import "./ExportCSVConfiguration.css";
function ExportCSVConfiguration(props) {
  const { langIndex } = useSelector((x) => x.app);
  const { customerInfo, selectedEmails, setSelectedEmails, screenSize } = props;
  const {
    fromReportDate,
    setFromReportDate,
    toReportDate,
    setToReportDate,
  } = props;
  return (
    <div style={{ marginTop: 50 }}>
      <div>
        <h3 style={{ color: "#eeeeee" }}>
          {lang(langIndex, "report_exportcsv")}{" "}
        </h3>
      </div>
      <div className="configuration-setting-one-item-style"></div>
    </div>
  );
}

export default ExportCSVConfiguration;
