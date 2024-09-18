import React, { useState } from "react";
import { Chart } from "react-google-charts";
import { useSelector } from "react-redux";
import { sizePad, sizeMobile } from "../../../../services/common/constants";
import lang from "../../../../services/lang";

function UtilizationContainerGoogle(props) {
  const { langIndex } = useSelector((x) => x.app);
  const { machineInfo, additionalHstInfo, screenSize } = props;


  let data_array = [["Task", "Hours per Day"]];
  let slice_array = [];
  additionalHstInfo.sort(function (a, b) {
    if (a.duration < b.duration) {
      return 1;
    }
    if (a.duration > b.duration) {
      return -1;
    }
    return 0;
  });
  for (var i = 0; i < additionalHstInfo.length; i++) {
    data_array.push([
      additionalHstInfo[i].status,
      additionalHstInfo[i].duration <= 0 ? 0 : additionalHstInfo[i].duration,
    ]);
    slice_array.push({
      color: additionalHstInfo[i].color,
      textStyle: {
        // color: pie_chart_slice_color_array[i]
        color: "#ffffff",
      },
    });
  }

  if (machineInfo["machine_id"] == "Makino") {
  }

  return (
    <div className="utilization-container">
      <div style={{ borderBottom: "1px solid white", textAlign: "left" }}>
        <h4 style={{ color: "#eeeeee" }}>
          {/* {lang(langIndex, "cnc_utilization")} */}
          24 Hours Utilization
        </h4>
      </div>
      {screenSize.width >= sizePad || screenSize.width < sizeMobile ? (
        <div style={{ paddingLeft: screenSize.width < sizeMobile ? 0 : 20 }}>
          <Chart
            width={"100%"}
            height={"180px"}
            fill={"transparent"}
            chartType="PieChart"
            loader={<div>{lang(langIndex, "cnc_loadingchart")}</div>}
            data={data_array}
            // slices={pie_chart_slice_color_array}
            options={{
              chartArea: { width: "100%", height: "80%" },
              legend: {
                textStyle: { color: "#eeeeee", bold: "true", fontSize: 12 },
              },
              backgroundColor: { fill: "transparent" },
              slices: slice_array,
              pieStartAngle: 0,
            }}
            rootProps={{ "data-testid": "1000" }}
          />
        </div>
      ) : (
        <div>
          <Chart
            width={"100%"}
            height={"180px"}
            fill={"transparent"}
            chartType="PieChart"
            loader={<div>{lang(langIndex, "cnc_loadingchart")}</div>}
            data={data_array}
            options={{
              chartArea: { width: "100%", height: "80%" },
              legend: {
                position: "bottom",
                textStyle: { color: "#eeeeee", fontSize: 10 },
              },
              backgroundColor: { fill: "transparent" },
              slices: slice_array,
              pieStartAngle: 0,
            }}
            rootProps={{ "data-testid": "1000" }}
          />
        </div>
      )}
    </div>
  );
}

export default UtilizationContainerGoogle;
