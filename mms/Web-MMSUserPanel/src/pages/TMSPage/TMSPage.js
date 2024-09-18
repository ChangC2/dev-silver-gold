import { Line } from "@ant-design/plots";
import { Col, Row, Select, Spin } from "antd";
import { useEffect, useState } from "react";
import { connect, useSelector } from "react-redux";
import { getTimeSavingList } from "../../services/common/tms_apis";
import lang from "../../services/lang";
import "./TMSPage.css";

const { Option } = Select;

function TMSPage(props) {
  const { langIndex } = useSelector((state) => state.app);
  const customerId = props.app.customer_id;
  const timeSavingList = props.tmsService.timeSavingList[customerId];
  const [isLoading, setIsLoading] = useState(true);
  const [machineId, setMachineId] = useState("");
  const [progId, setProgId] = useState("");
  const [progNum, setProgNum] = useState([]);
  const [timeSaving7, setTimeSaving7] = useState(0);
  const [timeSaving30, setTimeSaving30] = useState(0);
  const [timeSavingAll, setTimeSavingAll] = useState(0);
  const [timeSavings, setTimeSavings] = useState([]);
  const [chartData, setChartData] = useState([]);

  const [data, setData] = useState([]);
  const config = {
    data,
    xField: "time",
    yField: "value",
    seriesField: "name",
    // yAxis: {
    //   label: {
    //     formatter: (v) => `${(v / 10e8).toFixed(1)} B`,
    //   },
    // },
    legend: {
      position: "top",
    },
    smooth: true,
    // @TODO 后续会换一种动画方式
    // animation: {
    //   appear: {
    //     animation: "path-in",
    //     duration: 5000,
    //   },
    // },
    animation: false,
    slider: {
      start: 0.0,
      end: 1,
      textStyle: {
        fill: "white",
      },
    },
    // scrollbar: {
    //   type: "horizontal",
    // },
  };

  useEffect(() => {
    setIsLoading(true);
    getTimeSavingList(customerId, props.dispatch, (res) => {
      setIsLoading(false);
    });
  }, [customerId]);

  useEffect(() => {
    if (timeSavingList != undefined && timeSavingList.length > 0) {
      setMachineId(timeSavingList[0]["machine_id"]);
    }
  }, [timeSavingList]);

  useEffect(() => {
    if (timeSavingList != undefined && timeSavingList.length > 0) {
      let findVal1 = timeSavingList.find((x) => {
        return x["machine_id"] === machineId;
      });

      if (findVal1 != undefined) {
        setProgId(findVal1["progNum"]["0"]["progNum"]);
        setProgNum(findVal1["progNum"]);
      } else {
        setProgId("");
        setProgNum([]);
      }
    }
  }, [timeSavingList, machineId]);

  useEffect(() => {
    if (progNum != undefined && progNum.length > 0) {
      let findVal2 = progNum.find((x) => {
        return x["progNum"] === progId;
      });

      if (findVal2 != undefined) {
        setTimeSavings(findVal2["time_savings"]);
        setTimeSaving7(findVal2["timeSavings7"]);
        setTimeSaving30(findVal2["timeSavings30"]);
        setTimeSavingAll(findVal2["timeSavingsAll"]);
      } else {
        setTimeSavings(0);
        setTimeSaving7(0);
        setTimeSaving30(0);
        setTimeSavingAll([]);
      }
    }
  }, [timeSavingList, progNum, progId]);

  useEffect(() => {
    let _chartData = [];
    if (timeSavings != undefined && timeSavings.length > 0) {
      timeSavings.forEach((_item) => {
        const oneData = [
          _item["date"],
          parseInt(_item["learnedTime"]),
          parseInt(_item["elapsedTime"]),
          parseInt(_item["timeSavings"]),
        ];
        _chartData.push(oneData);
      });

      setChartData([
        ["Date", "Learned Time", "Elapsed Time", "Time Savings"],
        ..._chartData,
      ]);

      let chartData1 = [];
      _chartData.map((item) => {
        chartData1.push({
          time: item[0],
          name: "Learned Time",
          value: item[1],
        });
        chartData1.push({
          time: item[0],
          name: "Elapsed Time",
          value: item[2],
        });
        chartData1.push({
          time: item[0],
          name: "Time Savings",
          value: item[3],
        });
      });
      setData(chartData1);
    } else {
      setData([]);
    }
  }, [timeSavings]);

  return (
    <div>
      {isLoading && (
        <div style={{ paddingTop: 100, textAlign: "center" }}>
          <Spin tip="Loading ..." size="large" />
        </div>
      )}
      {!isLoading && timeSavingList == "" && (
        <div className="no-data">{lang(langIndex, "cnc_nodata")}</div>
      )}
      {!isLoading && timeSavingList != "" && timeSavingList != undefined && (
        <div className="time-saving-back">
          <div className="time-saving-select">
            Machine
            <Select
              className="page-changer-style"
              style={{ width: 160, marginLeft: 15, marginRight: 50 }}
              dropdownClassName="page-changer-style-dropdown"
              value={machineId}
              onChange={(e) => setMachineId(e)}
            >
              {timeSavingList.map((x) => {
                return (
                  <Option
                    className="page-changer-item"
                    key={`machine-${x["machine_id"]}`}
                    value={x["machine_id"]}
                  >
                    {x["machine_id"]}
                  </Option>
                );
              })}
            </Select>
            Program Number
            <Select
              className="page-changer-style"
              style={{ width: 80, marginLeft: 15, marginRight: 5 }}
              dropdownClassName="page-changer-style-dropdown"
              value={progId}
              onChange={(e) => setProgId(e)}
            >
              {progNum.map((x) => {
                return (
                  <Option
                    className="page-changer-item"
                    key={`prog_num-${x["progNum"]}`}
                    value={x["progNum"]}
                  >
                    {x["progNum"]}
                  </Option>
                );
              })}
            </Select>
          </div>
          <div className="time-saving-record-times-container">
            <span className="time-saving-record-times-title">
              TOS Time Savings :
            </span>
            <Row>
              <Col span={8}>
                <div className="time-saving-record-times">
                  <span className="time-saving-record-times-label">
                    Time Savings
                  </span>
                  <span className="time-saving-record-times-label">
                    Last 7 Days
                  </span>
                  <span className="time-saving-record-times-value">
                    {timeSaving7} Min
                  </span>
                </div>
              </Col>
              <Col span={8}>
                <div className="time-saving-record-times">
                  <span className="time-saving-record-times-label">
                    Time Savings
                  </span>
                  <span className="time-saving-record-times-label">
                    Last 30 Days
                  </span>
                  <span className="time-saving-record-times-value">
                    {timeSaving30} Min
                  </span>
                </div>
              </Col>
              <Col span={8}>
                <div className="time-saving-record-times">
                  <span className="time-saving-record-times-label">
                    Time Savings
                  </span>
                  <span className="time-saving-record-times-label">
                    All Time
                  </span>
                  {timeSavingAll <= 60 && (
                    <span className="time-saving-record-times-value">
                      {timeSavingAll} Min
                      {}
                    </span>
                  )}
                  {timeSavingAll > 60 &&
                    timeSavingAll - parseInt(timeSavingAll / 60) * 60 > 0 && (
                      <span className="time-saving-record-times-value">
                        {parseInt(timeSavingAll / 60)} Hrs{" "}
                        {timeSavingAll - parseInt(timeSavingAll / 60) * 60} Min
                      </span>
                    )}
                  {timeSavingAll > 60 &&
                    timeSavingAll - parseInt(timeSavingAll / 60) * 60 == 0 && (
                      <span className="time-saving-record-times-value">
                        {parseInt(timeSavingAll / 60)} Hrs{" "}
                      </span>
                    )}
                </div>
              </Col>
            </Row>
          </div>
          <Line
            {...config}
            style={{
              paddingTop: 20,
              paddingBottom: 30,
              paddingLeft: 50,
              paddingRight: 50,
              textAlign: "center",
              height: 500,
            }}
          />
        </div>
      )}
    </div>
  );
}

const mapStateToProps = (state, props) => ({
  app: state.app,
  tmsService: state.tmsService,
});
const mapDispatchToProps = (dispatch, props) => ({
  dispatch: dispatch,
});
export default connect(mapStateToProps, mapDispatchToProps)(TMSPage);
