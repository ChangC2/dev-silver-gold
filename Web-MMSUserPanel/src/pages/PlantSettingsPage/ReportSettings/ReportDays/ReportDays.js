import { useEffect, useState } from "react";
import { useSelector } from "react-redux";
import lang from "../../../../services/lang";
import DayCheck from "./DayCheck/DayCheck";
import "./ReportDays.css";

function ReportDays(props) {
  const { langIndex } = useSelector((x) => x.app);
  const { customerInfo, updateCustomerInfo } = props;
  const [reportDays, setReportDays] = useState("");
  const days = ["S", "M", "T", "W", "T", "F", "S"];

  useEffect(() => {
    
    setReportDays(customerInfo.report_days);
  }, []);

  const setChecked = (index, checked) => {
    var tmpDays = reportDays;
    if (checked) {
      tmpDays += index + "";
    } else {
      tmpDays = tmpDays.replace(index + "", "");
    }
    setReportDays(tmpDays);

    if (customerInfo.report_days == tmpDays) return;
    let newInfo = customerInfo;
    newInfo.report_days = tmpDays;
    updateCustomerInfo(newInfo);
  };

  const daysUI = days.map((day, index) => {
    const checked = reportDays.includes(index + "");
    const title = day;
    return (
      <DayCheck
        key={index}
        checked={checked}
        title={title}
        index={index}
        setChecked={setChecked}
      />
    );
  });

  return (
    <div className="customer-setting-container-style">
      <div className="app-setting-text-input-title">
        {" "}
        {lang(langIndex, "plant_reportday")}
      </div>
      <div style={{marginTop:15}}>{daysUI}</div>
    </div>
  );
}

export default ReportDays;
