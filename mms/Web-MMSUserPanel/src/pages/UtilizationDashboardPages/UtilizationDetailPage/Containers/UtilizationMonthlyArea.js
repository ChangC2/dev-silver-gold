import { Col, Row, Select, Spin } from "antd";
import ReactApexChart from "react-apexcharts";
import { useSelector } from "react-redux";
import React, { useState, useEffect } from "react";
import { GetCustomerCurrentTime, GetUtilizationDetailData, pad } from "services/common/cnc_apis";
const { Option } = Select;

function UtilizationMonthlyArea(props) {
  const { langIndex } = useSelector((x) => x.app);
  const { timezone, customer_id, customerInfo, machineInfo } = props;
  const [selectUI, setSelectUI] = useState("");
  const [isBusy, setIsBusy] = useState(false);
  const [monthlyUtilizations, setMonthlyUtilizations] = useState([]);
  let months = [
    "January",
    "February",
    "March",
    "April",
    "May",
    "June",
    "July",
    "August",
    "September",
    "October",
    "November",
    "December",
  ];

  function apiCallForGetMachineData(month) {
    let currentDate = GetCustomerCurrentTime(timezone);
    let startDate = new Date(currentDate.getFullYear(), month, 1);
    startDate =
      pad(startDate.getMonth() + 1) +
      "/" +
      pad(startDate.getDate()) +
      "/" +
      pad(startDate.getFullYear());
    let endDate = new Date(currentDate.getFullYear(), month + 1, 0);
    endDate =
      pad(endDate.getMonth() + 1) +
      "/" +
      pad(endDate.getDate()) +
      "/" +
      pad(endDate.getFullYear());
    setIsBusy(true);
    GetUtilizationDetailData(
      customer_id,
      customerInfo["timezone"],
      startDate,
      endDate,
      machineInfo["machine_id"],
      1,
      (res) => {
        setIsBusy(false);
        if (res != null && res.data) {
          let tmpList = [...res.data.utilities];
          setMonthlyUtilizations(tmpList);
        }
      }
    );
  }

  useEffect(() => {
    let monthUIList = months.map((month, index) => {
      return (
        <Option key={index} value={index}>
          {month}
        </Option>
      );
    });
    let currentDate = GetCustomerCurrentTime(timezone);
    apiCallForGetMachineData(currentDate.getMonth());
    setSelectUI(
      <Select
        className={"timezone-page-timezone"}
        dropdownClassName={"timezone-page-timezone-list"}
        showSearch
        style={{ width: "100%" }}
        defaultValue={months[currentDate.getMonth()]}
        filterOption={(input, option) =>
          option.children.toLowerCase().indexOf(input.toLowerCase()) >= 0
        }
        onChange={(v) => onChangeMonth(v)}
      >
        {monthUIList}
      </Select>
    );
  }, [timezone]);


  const onChangeMonth = (m) =>{
    apiCallForGetMachineData(m);
  }

  const series = [
    {
      name: "Utilization",
      data: monthlyUtilizations.map((x) => parseFloat(x.utilization)),
    },
  ];
  const options5 = {
    chart: {
      type: "area",
      toolbar: { show: false },
    },
    labels: monthlyUtilizations.map((x) => x.day),
    xaxis: {
      labels: {
        style: {
          colors: "#fff",
          cssClass: "apexcharts-xaxis-label",
        },
        formatter: function (val, timestamp) {
          return val;
        },
      },
      tickAmount: 8,
      tickPlacement: "between",
    },
    yaxis: {
      title: {
        text: "Hours “In Cycle” Per Day",
        style: {
          color: "#fff",
          fontSize: "16px",
        },
      },
      labels: {
        show: true,
        style: {
          colors: "#fff",
          cssClass: "apexcharts-yaxis-label",
        },
      },
    },
    dataLabels: {
      enabled: false,
    },
  };

  return (
    <Spin spinning={isBusy} size="large">
      <Row className="utilization-detail-chart-container">
        <Col flex={"auto"} className="utilization-detail-chart-title">
          Year Spindle Utilization
        </Col>
        <Col
          flex={"150px"}
          style={{ marginRight: 10, marginTop: 5, textAlign: "left" }}
        >
          {selectUI}
        </Col>
        <Col span={24} className="utilization-detail-chart-right">
          <ReactApexChart
            options={options5}
            series={series}
            type="area"
            height={415}
          ></ReactApexChart>
        </Col>
      </Row>
    </Spin>
  );
}

export default UtilizationMonthlyArea;
