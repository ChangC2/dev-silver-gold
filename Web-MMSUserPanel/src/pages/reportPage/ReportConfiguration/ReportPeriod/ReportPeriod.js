import React from "react";
import { DatePicker, Space, Row, Col } from "antd";
import moment from "moment";
import "./ReportPeriod.css";
import { useSelector } from "react-redux";
import lang from "../../../../services/lang";
function ReportPeriod(props) {
  const { langIndex } = useSelector((x) => x.app);
  const {
    fromReportDate,
    setFromReportDate,
    toReportDate,
    setToReportDate,
    customerInfo,
    selectedEmails,
  } = props;
  const dateFormat = "MM/DD/YYYY";

  const onChangeFromDate = (date, dateString) => {
    var fromDate = moment(date);
    var toDate = moment(toReportDate, dateFormat);
    if (fromDate > toDate) {
      toDate = fromDate;
    }
    setFromReportDate(fromDate.format(dateFormat));
    setToReportDate(toDate.format(dateFormat));
  };
  const onChangeToDate = (date, dateString) => {
    var toDate = moment(date);
    var fromDate = moment(fromReportDate, dateFormat);
    if (fromDate > toDate) {
      toDate = fromDate;
    }
    setFromReportDate(fromDate.format(dateFormat));
    setToReportDate(toDate.format(dateFormat));
  };
  return (
    <div className="period-select-container-style">
      <div style={{ marginBottom: 10 }}>
        <Row>
          <Col span={3}>
            <h3 style={{ color: "#eeeeee" }}>{lang(langIndex, "report_from")}: </h3>
          </Col>
          <Col span={20}>
            <DatePicker
              className="date-time-picker-style"
              format={dateFormat}
              value={
                fromReportDate == ""
                  ? moment({ format: dateFormat })
                  : moment(fromReportDate, dateFormat)
              }
              allowClear={false}
              onChange={onChangeFromDate}
            />
          </Col>
        </Row>
      </div>
      <div style={{ marginBottom: 10 }}>
        <Row>
          <Col span={3}>
            <h3 style={{ color: "#eeeeee" }}>{lang(langIndex, "report_to")}: </h3>
          </Col>
          <Col span={20}>
            <DatePicker
              className="date-time-picker-style"
              format={dateFormat}
              value={
                toReportDate == "" ? moment() : moment(toReportDate, dateFormat)
              }
              allowClear={false}
              onChange={onChangeToDate}
            />
          </Col>
        </Row>
      </div>
    </div>
  );
}

export default ReportPeriod;
