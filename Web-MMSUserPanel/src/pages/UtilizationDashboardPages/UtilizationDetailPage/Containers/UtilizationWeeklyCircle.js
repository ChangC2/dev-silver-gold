import React, { useState } from "react";
import { Chart } from "react-google-charts";
import { Row, Col } from 'antd';
import { useSelector } from "react-redux";
import { sizePad, sizeMobile } from "../../../../../src/services/common/constants";
import ReactApexChart from 'react-apexcharts'
import lang from "../../../../../src/services/lang";

function UtilizationWeeklyCircle(props) {
  const { langIndex } = useSelector((x) => x.app);
  const { machineInfo, additionalHstInfo, screenSize } = props;

  const series = [70]
  const options5 = {

    chart: {
      height: 350,
      type: 'radialBar',
    },
    stroke: {
      width: 0.5,
    },
    plotOptions: {
      radialBar: {
        hollow: {
          size: '70%',
        },

        track: {
          strokeWidth: '97%',
        },
        dataLabels: {
          show: true,
          name: {
            show: true,
            fontSize: '30px',
            fontWeight: 600,
            offsetY: 10
          },
          value: {
            show: false,
          }
        }
      },
    },
    labels: ['70%'],
  };

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
          Daily Utilization
        </h4>
      </div>
      {screenSize.width >= sizePad || screenSize.width < sizeMobile ? (
        <div style={{ paddingLeft: screenSize.width < sizeMobile ? 0 : 20 }}>
          <Row>
            <Col md={10}>
              <div className="util-chart-left">
                <div className="util-chart-desc-text" style={{ marginTop: 40 }}>Idle – Uncategorized </div>
                <div className="util-chart-desc-text">Idle – Machine Warning </div>
                <div className="util-chart-desc-text">Idle – Machine Alarm Offline </div>
              </div>
            </Col>
            <Col md={14}>
              <div style={{ paddingTop: 5 }}>
                <ReactApexChart options={options5} series={series} type="radialBar" height={200}></ReactApexChart>
              </div>
            </Col>
          </Row>
        </div>
      ) : (
        <div>
          <ReactApexChart options={options5} series={series} type="radialBar" height={190}></ReactApexChart>
        </div>
      )}
    </div>
  );
}

export default UtilizationWeeklyCircle;
