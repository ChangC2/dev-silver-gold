import React from "react";
import { useEffect } from "react";
import {
  GetCustomerCurrentTime,
  GetHstFullData,
} from "../../../services/common/cnc_apis";
import "./MachineHstGraphWidget.css";
import moment from "moment";
import { Spin, Select } from "antd";
import { Column } from "@ant-design/plots";
import { useState } from "react";
const { Option } = Select;

const daysofhstList = [
  5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25,
  26, 27, 28, 29, 30,
];

const MachineHstGraphWidget = (props) => {
  const {
    machine_id,
    customer_id,
    customerInfo,
    throughPutRef,
    daysofhst,
    setDaysOfHst,
    hstData,
    setHstData,
    period,
    setPeriod,
    setChartData,
  } = props;

  const [data, setData] = useState([]);

  const [totalGoods, setTotalGoods] = useState(0);
  const [totalBads, setTotalBads] = useState(0);

  useEffect(() => {
    if (period == undefined || hstData == undefined) {
      return;
    }

    let chartData1 = [];
    period.map((day) => {
      const hst = hstData.find((x) => x["date"] == day);
      chartData1.push({
        date: day.substring(0, 5),
        type: "Good Parts",
        value: hst == undefined ? 0 : parseInt(hst["goodParts"]),
      });
      chartData1.push({
        date: day.substring(0, 5),
        type: "Bad Parts",
        value: hst == undefined ? 0 : parseInt(hst["badParts"]),
      });
    });

    let tGoods = 0;
    let tBads = 0;
    for (let i = 0; i < hstData.length; i++) {
      tGoods += parseInt(hstData[i].goodParts);
      tBads += parseInt(hstData[i].badParts);
    }
    setTotalGoods(tGoods);
    setTotalBads(tBads);
    setData(chartData1);
    setChartData(chartData1);
  }, [period, hstData]);

  useEffect(() => {
    const timeZone = customerInfo["timezone"];
    const customerCurrentTime = GetCustomerCurrentTime(timeZone);
    const operator = "";
    const startDate = moment(customerCurrentTime).add(-daysofhst, "days");
    const endDate = moment(customerCurrentTime).add(1, "days");
    GetHstFullData(
      customer_id,
      machine_id,
      operator,
      timeZone,
      startDate.format("YYYY/MM/DD"),
      endDate.format("YYYY/MM/DD"),
      (res) => {
        const { status, message, data } = res;
        if (status == false) {
          message.error(message);
          return;
        }
        const daysofhst = endDate.diff(startDate, "days");
        const dayList = [];
        for (let i = 0; i < daysofhst; i++) {
          dayList.push(startDate.format("MM/DD/YYYY"));
          startDate.add(1, "days");
        }
        setPeriod(dayList);
        setHstData(data);
      }
    );
  }, [daysofhst, customer_id, machine_id]);

  if (hstData == undefined) {
    return (
      <div style={{ paddingTop: 30, textAlign: "center" }}>
        <Spin />
      </div>
    );
  }
  const config = {
    data,
    // theme: theme,
    xField: "date",
    yField: "value",
    seriesField: "type",
    isGroup: true,
    colorField: "type", // or seriesField in some cases
    color: ["#2ca02c", "#d62728"],
    columnStyle: {
      radius: [0, 0, 0, 0],
    },
    slider: {
      start: 0.0,
      end: 1,
      textStyle: {
        fill: "white",
      },
    },
    legend: {
      offsetY: -8,
    },
    // scrollbar: {
    //   type: 'horizontal',
    // },
  };

  return (
    <div className="machine-hst-gragh-back">
      <div className="machine-hst-title">
        Trailing
        <Select
          className="selector"
          dropdownClassName="selector-dropdown"
          style={{ width: 60, marginLeft: 15, marginRight: 5 }}
          value={daysofhst}
          onChange={(e) => setDaysOfHst(e)}
        >
          {daysofhstList.map((x) => {
            return (
              <Option className="page-changer-item" key={`day-${x}`} value={x}>
                {x}
              </Option>
            );
          })}
        </Select>
        Days Throughput
        <span className="backspace" />
        <span className="backspace" />
        <span className="backspace" />
        <span className="backspace" />
        <span className="backspace" />
        <span className="machine-hst-gragh-total-goods">
          Total Good Parts : {totalGoods}
        </span>
        <span className="backspace" />
        <span className="backspace" />
        <span className="backspace" />
        <span className="machine-hst-gragh-total-bads">
          Total Bad Parts : {totalBads}
        </span>
      </div>
      <div ref={throughPutRef}>
        <Column
          {...config}
          style={{
            paddingTop: 20,
            paddingBottom: 20,
            paddingLeft: 50,
            paddingRight: 50,
            textAlign: "center",
            height: 350,
          }}
        />
      </div>
    </div>
  );
};

export default MachineHstGraphWidget;
