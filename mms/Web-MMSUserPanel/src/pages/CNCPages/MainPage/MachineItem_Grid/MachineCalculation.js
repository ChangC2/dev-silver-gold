import { useEffect, useState } from "react";
import { Spin } from "antd";
import { Bullet } from "@ant-design/plots";

import moment from "moment";
import { GetCustomerCurrentTime } from "services/common/cnc_apis";
import Urls, { postRequest } from "../../../../services/common/urls";

function MachineCalculation(props) {
  const {
    customer_id,
    machineInfo,
    customerInfo,
    appSetting,
    currentDate,
  } = props;

  const [calc_value, setCalc_value] = useState(0);
  const [isLoading, setIsLoading] = useState(true);
  const [calc_value_color, setCalc_value_color] = useState("#fd9321");

  let measure =
    (parseFloat(calc_value) / parseFloat(appSetting.calc_chart_max_value)) *
    100;
  if (isNaN(measure)) {
    measure = 0;
  }

  if (measure > 150) {
    measure = 150;
  }

  const data = [
    {
      title: "",
      ranges: [50, 100, 150],
      values: [measure],
      target: 100,
    },
  ];

  const config = {
    data,
    measureField: "values",
    rangeField: "ranges",
    targetField: "target",
    xField: "title",
    animation: false,
    tooltip: false,
    color: {
      range: ["#FFbcb8", "#FFe0b0", "#bfeec8"],
      measure: calc_value_color,
      target: "#ffffff",
    },
    xAxis: {
      line: null,
    },
    yAxis: {
      tickMethod: ({ max }) => {
        const interval = Math.ceil(max / 3); // 自定义刻度 ticks
        return [0, interval, interval * 2, max];
      },
      label: {
        formatter(v) {
          // let unit =
          //   appSetting.calc_chart_disp_mode == 0
          //     ? appSetting.calc_chart_unit
          //     : "%";
          return v + "%";
        },
        style: {
          fill: "#fff",
          fontSize: 12,
          textAlign: "center",
        },
      },
    },
    label: {
      measure: {
        formatter(v) {
          let unit =
            appSetting.calc_chart_disp_mode == 0
              ? appSetting.calc_chart_unit
              : "%";

          let mValue =
            appSetting.calc_chart_disp_mode == 0
              ? (v.values * parseFloat(appSetting.calc_chart_max_value)) / 100
              : v.values;
          return isNaN(mValue) ? "0 " + unit : parseFloat(mValue).toFixed(0) + " " + unit;
        },
        position: "middle",
        style: {
          fill: "#000000",
        },
      },
    },
    height: 50,
    legend: false,
  };

  const toFactory = (strDate) => {
    var arr = strDate.split(/[- :]/);
    let ret = new Date(arr[0], arr[1] - 1, arr[2], arr[3], arr[4], "00"); // Date.parse(strDate);
    ret = ret / 1000;
    ret = ret + 3600 * 8;
    const timeZone = customerInfo["timezone"];
    ret = ret + 3600 * timeZone;
    ret = new Date(ret * 1000);
    ret = moment(ret);
    return ret;
  };

  useEffect(() => {
    let factoryDate = currentDate;
    if (factoryDate == "") {
      const date = moment(GetCustomerCurrentTime(-8));
      factoryDate = toFactory(date.format("YYYY-MM-DD HH:mm:ss"));
    } else {
      const date = moment(currentDate);
      factoryDate = toFactory(date.format("YYYY-MM-DD HH:mm:ss"));
    }
    var url = Urls.GET_MACHINE_CALCULATION;

    var param = {
      factory_id: customer_id,
      machine_id: machineInfo.machine_id,
      date: factoryDate.format("YYYY-MM-DD"),
    };
    setIsLoading(true);
    postRequest(url, param, (res) => {
      setIsLoading(false);
      if (res.status === true) {
        setCalc_value(parseFloat(res.value));
      }
    });
  }, [currentDate]);

  useEffect(() => {
    if (calc_value < appSetting.calc_chart_max_value / 2) {
      setCalc_value_color("#F4664A");
    } else if (calc_value < appSetting.calc_chart_max_value) {
      setCalc_value_color("#FAAD14");
    } else {
      setCalc_value_color("#30BF78");
    }
  }, [calc_value, appSetting]);

  if (isLoading) {
    return (
      <div style={{ width: "100%", textAlign: "center" }}>
        <Spin size="medium"></Spin>
      </div>
    );
  }
  return (
    <div className="calculation-title">
      <span>{appSetting.calc_chart_title}</span>
      <Bullet
        {...config}
        style={{
          marginTop: 10,
          marginBottom: 30,
          textAlign: "center",
          height: 60,
        }}
      />
    </div>
  );
}
export default MachineCalculation;
