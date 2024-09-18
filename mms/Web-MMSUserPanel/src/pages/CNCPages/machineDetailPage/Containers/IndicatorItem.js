import { Gauge } from "@ant-design/plots";

function IndicatorItem(props) {
  const { id, value, title, textColor, isForReport } = props;
  const color = textColor === undefined ? "#eeeeee" : textColor;

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
    <div style={{ textAlign: "center" }}>
      <Gauge
        {...config}
        className={
          title === "OEE"
            ? "machine-detail-oee-container"
            : "machine-detail-gauge-container"
        }
      />

      {textColor === undefined ? (
        title === "OEE" ? (
          <h3 style={{ color: color }}>
            {title} :{" "}
            {value === null || isNaN(value) ? 0 : parseInt(value * 100)}%
          </h3>
        ) : (
          <h4 style={{ color: color }}>
            {title} :{" "}
            {value === null || isNaN(value) ? 0 : parseInt(value * 100)}%
          </h4>
        )
      ) : (
        <div>
          {isForReport && (
            <div style={{ fontSize: 12, lineHeight: 0.75 }}>
              {value === null || isNaN(value) ? 0 : parseInt(value * 100)}%
            </div>
          )}
          <h2 style={{ color: color }}>{title}</h2>
        </div>
      )}
    </div>
  );
}

export default IndicatorItem;
