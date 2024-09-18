import { Button, message } from "antd";
import { ExportToCsv } from "export-to-csv";
import { useState } from "react";
import { useSelector } from "react-redux";
import {
  GetReportPeriodData,
  GetReportPeriodDataByOperator,
} from "../../../../services/common/cnc_apis";
import Urls, { postRequest } from "../../../../services/common/urls";
import lang from "../../../../services/lang";
import "./ExportCSVButton.css";

function ExportCSVButton(props) {
  const { langIndex } = useSelector((x) => x.app);
  const {
    customer_id,
    fromReportDate,
    toReportDate,
    selectedMachine,
    selectedOperator,
    customerInfo,
    machineInfo,
    selectedEmails,
  } = props;
  const [isGeneratingCSV, setIsGeneratingCSV] = useState(false);

  const onSendAsEmail = (csvData) => {
    if (csvData.length == 0) return;
    var mailData = [];
    mailData.push(Object.keys(csvData[0]));
    csvData.forEach((item) => {
      mailData.push(Object.values(item));
    });

    var url = Urls.CSV_SENDER;
    var param = {
      csvData: mailData,
      emailList: selectedEmails,
    };
    postRequest(url, param, (res) => {});
  };

  const onGenerateCSV = () => {
    setIsGeneratingCSV(true);
    if (selectedMachine != null && selectedMachine.length > 0) {
      GetReportPeriodData(
        customer_id,
        selectedMachine,
        customerInfo["timezone"],
        fromReportDate,
        toReportDate,
        (res) => {
          if (res.status === false) {
            message.error(lang(langIndex, "msg_something_wrong"));
            setIsGeneratingCSV(false);
          } else {
            if (res.data["oee"].length === 0) {
              message.error(lang(langIndex, "msg_no_data_print"));
              setIsGeneratingCSV(false);
              return;
            }
            exportToCSV({ ...res.data });
          }
        }
      );
    } else {
      GetReportPeriodDataByOperator(
        customer_id,
        selectedOperator,
        customerInfo["timezone"],
        fromReportDate,
        toReportDate,
        (res) => {
          setIsGeneratingCSV(false);
          if (res == null) {
            message.error("No Data");
            return;
          }
          if (res.status === false) {
            message.error("No Data");
          } else {
            if (res.data["oee_o"].length === 0) {
              message.error(lang(langIndex, "msg_no_data_print"));
              return;
            }
            exportToCSV({ ...res.data });
          }
        }
      );
    }
  };

  const exportToCSV = (data) => {
    var no = 1;
    const hstData = data.oee_o;
    const appSettings = data.appSettings;
    var key1 =
      appSettings.length > 0 && appSettings[0].downtime_reason1 !== ""
        ? appSettings[0].downtime_reason1
        : "Downtime Reason1";

    var key2 =
      appSettings.length > 0 && appSettings[0].downtime_reason2 !== ""
        ? appSettings[0].downtime_reason2
        : "Downtime Reason2";
    var key3 =
      appSettings.length > 0 && appSettings[0].downtime_reason3 !== ""
        ? appSettings[0].downtime_reason3
        : "Downtime Reason3";
    var key4 =
      appSettings.length > 0 && appSettings[0].downtime_reason4 !== ""
        ? appSettings[0].downtime_reason4
        : "Downtime Reason4";
    var key5 =
      appSettings.length > 0 && appSettings[0].downtime_reason5 !== ""
        ? appSettings[0].downtime_reason5
        : "Downtime Reason5";
    var key6 =
      appSettings.length > 0 && appSettings[0].downtime_reason6 !== ""
        ? appSettings[0].downtime_reason6
        : "Downtime Reason6";
    var key7 =
      appSettings.length > 0 && appSettings[0].downtime_reason7 !== ""
        ? appSettings[0].downtime_reason7
        : "Downtime Reason7";
    var key8 =
      appSettings.length > 0 && appSettings[0].downtime_reason8 !== ""
        ? appSettings[0].downtime_reason8
        : "Downtime Reason8";

    var csvData = [];
    for (var i = 0; i < hstData.length; i++) {
      var hsv = hstData[i];
      // var item = {
      //   no: no,
      //   date: i > 0 && hsv.date === hstData[i - 1].date ? "" : hsv.date,
      //   machine:
      //     i > 0 && hsv.machine_id === hstData[i - 1].machine_id
      //       ? ""
      //       : hsv.machine_id,
      //   Operator: hsv.Operator,
      //   oee: `${hsv.oee / 100}`,
      //  // utilization: `${0}`,
      //   availability: `${hsv.availability / 100}`,
      //   quality: `${
      //     parseFloat(hsv.goodParts) + parseFloat(hsv.badParts) == 0
      //       ? 0
      //       : (
      //           parseFloat(hsv.goodParts) /
      //           (parseFloat(hsv.goodParts) + parseFloat(hsv.badParts))
      //         ).toFixed(2)
      //   }`,
      //   performance: `${hsv.performance / 100}`,
      //   goodParts: "",
      //   badParts: "",
      // };

      var item = {
        no: no,
        date: hsv.date,
        machine: hsv.machine_id,
        Operator: hsv.Operator,
        oee: `${hsv.oee / 100}`,
        // utilization: `${0}`,
        availability: `${hsv.availability / 100}`,
        quality: `${
          parseFloat(hsv.goodParts) + parseFloat(hsv.badParts) == 0
            ? 0
            : (
                parseFloat(hsv.goodParts) /
                (parseFloat(hsv.goodParts) + parseFloat(hsv.badParts))
              ).toFixed(2)
        }`,
        performance: `${hsv.performance / 100}`,
        goodParts: "",
        badParts: "",
      };

      item["Operator goodParts"] = hsv.goodParts;
      item["Operator badParts"] = hsv.badParts;

      if (customer_id == "sm_ks") item["Sq inches"] = hsv.sq_inches;
      item["Orders"] = "Total";

      item[key1] = Math.round((Number(hsv.r1t) / 60000 / 60) * 100) / 100;
      item[key2] = Math.round((Number(hsv.r2t) / 60000 / 60) * 100) / 100;
      item[key3] = Math.round((Number(hsv.r3t) / 60000 / 60) * 100) / 100;
      item[key4] = Math.round((Number(hsv.r4t) / 60000 / 60) * 100) / 100;
      item[key5] = Math.round((Number(hsv.r5t) / 60000 / 60) * 100) / 100;
      item[key6] = Math.round((Number(hsv.r6t) / 60000 / 60) * 100) / 100;
      item[key7] = Math.round((Number(hsv.r7t) / 60000 / 60) * 100) / 100;
      item[key8] = Math.round((Number(hsv.r8t) / 60000 / 60) * 100) / 100;

      item["Idle - Uncategorized"] =
        Math.round((Number(hsv.uncat) / 60000 / 60) * 100) / 100;
      item["Total Downtime"] =
        Math.round((Number(hsv.downtimes) / 60000 / 60) * 100) / 100;
      item["Total uptime"] =
        Math.round((Number(hsv.inCycle) / 60000 / 60) * 100) / 100;
      no++;
      csvData.push({ ...item });

      var subItems = data.oee.filter(
        (h) =>
          h.date === hsv.date &&
          h.machine_id === hsv.machine_id &&
          h.Operator === hsv.Operator
      );

      for (var j = 0; j < subItems.length; j++) {
        var sHsv = subItems[j];
        var subItem = {
          no: no,
          date: hsv.date,
          machine: hsv.machine_id,
          Operator: hsv.Operator,
          oee: `${sHsv.oee / 100}`,
          //utilization: `${0}`,
          availability: `${sHsv.availability / 100}`,
          quality: `${sHsv.quality / 100}`,
          performance: `${sHsv.performance / 100}`,
          goodParts: sHsv.goodParts,
          badParts: sHsv.badParts,
        };
        subItem["Operator goodParts"] = "";
        subItem["Operator badParts"] = "";

        if (customer_id == "sm_ks") subItem["Sq inches"] = sHsv.sq_inches;
        subItem["Orders"] = sHsv.Orders;

        subItem[key1] = Math.round((Number(sHsv.r1t) / 60000 / 60) * 100) / 100;
        subItem[key2] = Math.round((Number(sHsv.r2t) / 60000 / 60) * 100) / 100;
        subItem[key3] = Math.round((Number(sHsv.r3t) / 60000 / 60) * 100) / 100;
        subItem[key4] = Math.round((Number(sHsv.r4t) / 60000 / 60) * 100) / 100;
        subItem[key5] = Math.round((Number(sHsv.r5t) / 60000 / 60) * 100) / 100;
        subItem[key6] = Math.round((Number(sHsv.r6t) / 60000 / 60) * 100) / 100;
        subItem[key7] = Math.round((Number(sHsv.r7t) / 60000 / 60) * 100) / 100;
        subItem[key8] = Math.round((Number(sHsv.r8t) / 60000 / 60) * 100) / 100;

        subItem["Idle - Uncategorized"] =
          Math.round((Number(sHsv.uncat) / 60000 / 60) * 100) / 100;

        subItem["Total Downtime"] =
          Math.round((Number(sHsv.downtimes) / 60000 / 60) * 100) / 100;
        subItem["Total uptime"] =
          Math.round((Number(sHsv.inCycle) / 60000 / 60) * 100) / 100;
        no++;
        csvData.push({ ...subItem });
      }
    }

    var title = "";
    for (var i = 0; i < selectedMachine.length; i++) {
      if (i === 0) {
        title = title + selectedMachine[i];
      } else {
        title = title + "_" + selectedMachine[i];
      }
    }

    const options = {
      filename: `${title} ${fromReportDate}-${toReportDate}`,
      fieldSeparator: ",",
      quoteStrings: '"',
      decimalSeparator: ".",
      showLabels: true,
      showTitle: true,
      title: `${title} ${fromReportDate}-${toReportDate}`,
      useTextFile: false,
      useBom: true,
      useKeysAsHeaders: true,
    };
    const csvExporter = new ExportToCsv(options);
    csvExporter.generateCsv(csvData);
    onSendAsEmail(csvData);
    setIsGeneratingCSV(false);
  };

  return (
    <div>
      <Button
        type="primary"
        disabled={selectedMachine.length === 0 && selectedOperator.length === 0}
        loading={isGeneratingCSV}
        onClick={onGenerateCSV}
      >
        {lang(langIndex, "report_generatecsv")}
      </Button>
    </div>
  );
}

export default ExportCSVButton;
