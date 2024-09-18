import { Pie } from "@ant-design/plots";
import { useEffect, useState } from "react";
import { useSelector } from "react-redux";

function UtilizationContainerAntd(props) {
  const { langIndex } = useSelector((x) => x.app);
  const { additionalHstInfo, shift, shifts } = props;
  const [title, setTitle] = useState("Utilization");

  useEffect(() => {
    if (shift == 0) {
      setTitle("Utilization  " + "(24 Hours)");
    } else {
      if (shifts.length > shift) {
        setTitle(
          "Utilization  " +
            "(Shift " +
            shift.toString() +
            " (" +
            shifts[shift] +
            "))"
        );
      }
    }
  }, [shift, shifts]);

  additionalHstInfo.sort(function (a, b) {
    if (a.duration < b.duration) {
      return 1;
    }
    if (a.duration > b.duration) {
      return -1;
    }
    return 0;
  });

  const data = [];
  for (var i = 0; i < additionalHstInfo.length; i++) {
    data.push({
      task: additionalHstInfo[i].status,
      hours:
        additionalHstInfo[i].duration <= 0 ? 0 : additionalHstInfo[i].duration,
    });
  }

  const config = {
    appendPadding: 10,
    data,
    angleField: "hours",
    colorField: "task",
    radius: 0.9,
    // label: {
    //   type: "outer",
    //   offset: "30%",
    //   content: ({ percent }) => {
    //     if (percent > 0.05) {
    //       return `${(percent * 100).toFixed(0)}%`;
    //     } else {
    //       return "";
    //     }
    //   },
    //   style: {
    //     fontSize: 12,
    //     textAlign: "center",
    //     fill: "rgba(255, 255, 255, 1)",
    //   },
    // },
    legend: {
      layout: "vertical",
      position: "right",
      offsetX: -40,
      itemName: {
        style: (item, index) => {
          return {
            fill: "rgba(255, 255, 255, 0.9)",
          };
        },
      },
    },
    label: false,
    interactions: [
      {
        type: "element-active",
      },
    ],
    color: ({ task }) => {
      for (var i = 0; i < additionalHstInfo.length; i++) {
        if (task === additionalHstInfo[i].status) {
          return additionalHstInfo[i].color;
        }
      }
      return "#d62728";
    },
    tooltip: {
      formatter: (data) => {
        return { name: data.task, value: data.hours.toFixed(3) };
      },
    },
    animation: false,
  };

  return (
    <div className="utilization-container">
      <div style={{ borderBottom: "1px solid white", textAlign: "left" }}>
        <h4 style={{ color: "#eeeeee" }}>{title}</h4>
      </div>
      <div>
        <Pie {...config} style={{ height: 190, paddingTop: 10 }} />
      </div>
    </div>
  );
}

export default UtilizationContainerAntd;
