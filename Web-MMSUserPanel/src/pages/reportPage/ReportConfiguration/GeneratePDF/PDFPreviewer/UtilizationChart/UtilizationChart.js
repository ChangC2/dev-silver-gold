import React from "react";
import Chart from "react-google-charts";
import { Row, Col } from "antd";
import moment from "moment";
import "./UtilizationChart.css";

function UtilizationChart(props) {
  const { pdfData } = props;
  const { gantt } = pdfData;

  let data_array = [["Task", "Hours per Day"]];
  let slice_array = [];
  for (var i = 0; i < gantt.statusList.length; i++) {
    var status = gantt.statusList[i];
    data_array.push([status, gantt.timeList[status]]);
    slice_array.push({
      color: gantt.colorList[status],
      textStyle: {
        color: "#eeeeee",
      },
    });
  }
  let util_legend_ui = gantt.statusList.map((status, index) => {
    var percent = (
      (gantt.timeList[status] / pdfData["totalTime"]) *
      100.0
    ).toFixed(2);
    var duration = moment.duration(gantt.timeList[status], "seconds");
    var hour = Math.floor(gantt.timeList[status] / 3600.0);
    var minutes = Math.floor((gantt.timeList[status] % 3600.0) / 60.0);
    var seconds = gantt.timeList[status] % 60;
    duration = hour + ":" + minutes + ":" + seconds;

    var color = gantt.colorList[status];
    return (
      <div className="legend-info-container-style" key={"legent_info_" + index}>
        <div
          className="circle-shape-style"
          style={{ backgroundColor: color }}
        ></div>
        <div className="legentinfo-style">
          {/* {status}( {percent}%, {duration} ) */}
          {status}({duration})
        </div>
      </div>
    );
  });
  return (
    <div>
      <Row>
        <Col span={14}>
          <Chart
            className="pdf-util-pie-chart-style"
            width={"100%"}
            height={"250px"}
            fill={"transparent"}
            chartType="PieChart"
            loader={<div>Loading Chart</div>}
            data={data_array}
            options={{
              chartArea: { width: "100%", height: "90%" },
              // legend: { textStyle: { color: 'black', bold: 'true', fontSize: 12 } },
              legend: { position: "none" },
              backgroundColor: { fill: "transparent" },
              slices: slice_array,
              pieStartAngle: 0,
            }}
            rootProps={{ "data-testid": "1000" }}
          />
        </Col>
        <Col span={10}>
          <div style={{ marginBottom: 20 }}>
            <h3>Utilization: </h3>
          </div>
          {util_legend_ui}
        </Col>
      </Row>
    </div>
  );
}

export default UtilizationChart;
