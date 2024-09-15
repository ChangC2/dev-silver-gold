import { Line } from "@ant-design/plots";
import { Select, Spin } from "antd";
import moment from "moment";
import { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import {
  GetCustomerCurrentTime,
} from "../../../services/common/cnc_apis";
import {
  getSensorDataForGraph,
  getSensorList,
} from "../../../services/common/sensor_apis";
import "./TrendChartWidget.css";
const { Option } = Select;

const daysoftempList = [1, 2, 3, 4, 5, 6, 7];
const minuteStep = 60;

const PaintTrendChartWidget = (props) => {
  const {
    customer_id,
    customerInfo,
    tankRef,
    daysoftemp,
    setDaysOfTemp,
    setChartData,
    step,
    setStep,
  } = props;

  const dispatch = useDispatch();
  const sensorService = useSelector((x) => x.sensorService);

  const sensorList = sensorService.sensorList[customer_id];

  const [data, setData] = useState([]);

  const config = {
    data,
    height: 400,
    width: "100%",
    legend: {
      position: "top",
      textStyle: {
        color: "#FFFFFF",
      },
    },
    bar: { groupWidth: "75%" },
    timeline: { showRowLabels: true, showBarLabels: true },
    backgroundColor: "transparent",
    allowHtml: true,
    vAxis: {
      title: "",
      textStyle: {
        color: "#FFFFFF",
      },
      gridlines: {
        color: "#FFFFFF",
      },
      baselineColor: "#FFFFFF",
    },
    hAxis: {
      showTextEvery: step,
      slantedText: false,
      //format: "MM-DD HH:mm",
      textStyle: {
        fontSize: 14,
        color: "#FFFFFF",
      },
      gridlines: {
        color: "#FFFFFF",
      },
      baselineColor: "#FFFFFF",
    },
    xField: "time",
    yField: "value",
    seriesField: "sensor",
    legend: {
      position: "top",
    },
    smooth: true,
    animation: false,
    slider: {
      start: 0.0,
      end: 1,
      textStyle: {
        fill: "white",
      },
    },

    tooltip: {
      isHtml: true,
      customContent: (title, items) => {
        const field = items?.[0];
        let htmlStr = `<div style="margin:10px 0;font-weight:700;">${field?.data?.time}</div><div style="margin:10px 0;font-weight:400;">Part ID : ${field?.data?.part_id}</div><div class="g2-tooltip-items">`;
        items.forEach((item) => {
          htmlStr += `<div class="g2-tooltip-item" style="margin-bottom:8px;display:flex;justify-content:space-between;">
                <span  style="margin-right: 12px;color:${item.color};font-size:15px" >■</span>
                <span class="g2-tooltip-item-label" style="margin-right: auto;" > ${item.data?.sensor} : </span>
                <span  style="margin-right: 12px;" ></span>
                <span class="g2-tooltip-item-value">${item.value}</span>
              </div>`;
        });
        htmlStr += "</div>";
        return htmlStr;
      },
    },
  };

  useEffect(() => {
    getSensorList(customer_id, dispatch, (res) => {});
  }, []);

  const toFactory = (strDate) => {
    var arr = strDate.split(/[- :]/);
    let ret = new Date(arr[0], arr[1] - 1, arr[2], arr[3], arr[4], "00"); // Date.parse(strDate);
    ret = ret / 1000;
    ret = ret + 3600 * 7;
    const timeZone = customerInfo["timezone"];
    ret = ret + 3600 * timeZone;
    ret = new Date(ret * 1000);
    ret = moment(ret);
    return ret;
  };

  useEffect(() => {
    if (customerInfo === undefined) return;
    if (sensorList === undefined) return;

    const califoniaTime = GetCustomerCurrentTime(-7); // GMT - 7

    const startTime = moment(califoniaTime)
      .add(-daysoftemp, "days")
      .set({ hour: 0, minute: 0, second: 0 });

    const endTime = moment(califoniaTime);

    getSensorDataForGraph(
      startTime.format("YYYY-MM-DD HH:mm:ss"),
      endTime.format("YYYY-MM-DD HH:mm:ss"),
      (res) => {
        const timeList = [];
        while (startTime <= endTime) {
          timeList.push(startTime.format("YYYY-MM-DD HH:mm"));
          startTime.add(minuteStep, "minutes");
        }

        const sensorData = res;

        let chartData1 = Array();
        if (sensorData != undefined) {
          timeList.forEach((_time) => {
            const sensorItem = sensorData.filter(
              (x) => x[0].substring(0, 15) === _time.substring(0, 15)
            );

            const ambientSensor = sensorItem.find((x) => x[1] === "5010");
            const value1 =
              ambientSensor === undefined ? 0 : parseFloat(ambientSensor[2]);
            const value2 =
              ambientSensor === undefined ? 0 : parseFloat(ambientSensor[3]);
            const partID = ambientSensor === undefined ? "" : ambientSensor[4];

            chartData1.push({
              time: toFactory(_time).format("MM-DD HH:mm"),
              sensor: "Ambient Temp",
              value: value1,
              part_id: partID,
            });

            chartData1.push({
              time: toFactory(_time).format("MM-DD HH:mm"),
              sensor: "Ambient Humidity",
              value: value2,
              part_id: partID,
            });

            const paintboothSensor = sensorItem.find((x) => x[1] === "5009");
            const paintboothValue1 =
              paintboothSensor === undefined
                ? 0
                : parseFloat(paintboothSensor[2]);
            const paintboothValue2 =
              paintboothSensor === undefined
                ? 0
                : parseFloat(paintboothSensor[3]);

            chartData1.push({
              time: toFactory(_time).format("MM-DD HH:mm"),
              sensor: "Paint Booth Temp",
              value: paintboothValue1,
              part_id: partID,
            });

            chartData1.push({
              time: toFactory(_time).format("MM-DD HH:mm"),
              sensor: "Paint Booth Humidity",
              value: paintboothValue2,
              part_id: partID,
            });
          });
        }
        setStep(parseInt(timeList.length / 7));
        setChartData(chartData1);
        setData(chartData1);
      }
    );
  }, [customerInfo, sensorList, daysoftemp]);

  return (
    <div className="trend-chart-back">
      <div className="trend-chart-title">
        Trend(
        <Select
          className="selector"
          dropdownClassName="selector-dropdown"
          style={{ width: 60, marginLeft: 5, marginRight: 5 }}
          value={daysoftemp}
          onChange={(e) => setDaysOfTemp(e)}
        >
          {daysoftempList.map((x) => {
            return (
              <Option className="page-changer-item" key={`day-${x}`} value={x}>
                {x}
              </Option>
            );
          })}
        </Select>
        Days)
      </div>
      {sensorList === undefined || data.length == 0 ? (
        <div style={{ textAlign: "center", paddingTop: 20 }}>
          <Spin />
        </div>
      ) : (
        <div ref={tankRef}>
          <Line
            {...config}
            style={{
              paddingTop: 20,
              paddingBottom: 0,
              paddingLeft: 50,
              paddingRight: 50,
              textAlign: "center",
              height: 400,
            }}
          />
        </div>
      )}
    </div>
  );
};
export default PaintTrendChartWidget;
