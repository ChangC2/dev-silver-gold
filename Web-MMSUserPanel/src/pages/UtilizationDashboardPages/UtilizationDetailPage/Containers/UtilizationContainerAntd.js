import { Pie } from "@ant-design/plots";
import { Col, Row } from "antd";
import { useEffect, useState } from "react";

function UtilizationContainerAntd(props) {
  const { additionalHstInfo, shift, shifts } = props;
  const [title, setTitle] = useState("Utilization");
  console.log(additionalHstInfo);
  useEffect(() => {
    if (shift == 0) {
      setTitle("Daily Utilization");
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
      position: "left",
      offsetX: 0,
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
    <Row className="utilization-detail-chart-container">
      <Col span={24} className="utilization-detail-chart-title">
        {title}
      </Col>
      <Col span={24} className="utilization-detail-chart-right">
        <Pie {...config} style={{ height: 190, paddingTop: 0 }} />
      </Col>
    </Row>
  );
}

export default UtilizationContainerAntd;
