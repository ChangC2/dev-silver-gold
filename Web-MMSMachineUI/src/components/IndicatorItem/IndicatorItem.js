import { Gauge } from "@ant-design/plots";
import "./IndicatorItem.css";

function IndicatorItem(props) {
  const { id, value, title, textColor, fontSize } = props;
  const color = textColor === undefined ? "#eeeeee" : textColor;
  const size = fontSize === undefined ? "18px" : fontSize;

  const config = {
    id: id,
    percent: value === null || isNaN(value) ? 0 : value,
    axis: {
      label: {
        formatter(v) {
          return Number(v) * 100;
        },
      },
      subTickLine: {
        count: 3,
      },
    },
    range: {
      ticks: [0, 100],
      color: ["l(0) 0:#F4664A 0.5:#FAAD14 1:#30BF78"],
    },
    indicator: {
      pointer: {
        style: {
          stroke: "#D0D0D0",
        },
      },
      pin: {
        style: {
          stroke: "#D0D0D0",
        },
      },
    },
    statistic: {
      content: {
        offsetY: 0,
        style: {
          fontSize: "14px",
          color: "#fff",
        },
        formatter: ({ percent }) =>
          // `${variables[variableKeyList[currentIndex]]}: ${(
          //   percent * 100
          // ).toFixed(0)}%`,
          "",
      },
    },
  };

  return (
    <div className="indicator-container">
      <Gauge {...config} className="indicator-gauge" />
      <span style={{ color: color, fontSize: size }}>
        {title} : {value === null || isNaN(value) ? 0 : parseInt(value * 100)}%
      </span>
    </div>
  );
}

export default IndicatorItem;
