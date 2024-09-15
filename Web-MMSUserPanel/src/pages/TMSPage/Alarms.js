import { Button, DatePicker, Select, Spin } from "antd";
import moment from "moment";

import { useEffect, useState } from "react";
import { connect, useSelector } from "react-redux";
import {
  getAlarms,
  getAlarmsFilters,
} from "../../services/common/tms_apis";
import "./Alarms.css";
import AlarmsTable from "./AlarmsTable";

const { Option } = Select;
const { RangePicker } = DatePicker;

function Alarms(props) {
  const { langIndex } = useSelector((state) => state.app);
  const customerId = props.app.customer_id;

  const [isLoading, setIsLoading] = useState(true);

  const [machineId, setMachineId] = useState("");
  const [progNum, setProgNum] = useState("");

  const [machines, setMachines] = useState([]);
  const [progNums, setProgNums] = useState([]);
  const [startDate, setStartDate] = useState("");
  const [endDate, setEndDate] = useState("");

  const [data, setData] = useState([]);

  const onChange = (value, dateString) => {
    setStartDate(dateString[0]);
    setEndDate(dateString[1]);
  };

  const onSearch = () => {
    let param = {
      customer_id: customerId,
      machine_id: machineId,
      progNum: progNum,
      startDate: startDate,
      endDate: endDate,
    };
    getData(param);
  };

  const getData = (param) => {
    setIsLoading(true);
    getAlarms(param, (res) => {
      setIsLoading(false);
      if (res != undefined && res != null && res["status"] == true) {
        setData(res["data"]["alarms"]);
      } else {
        setData([]);
      }
    });
  };

  useEffect(() => {
    setMachineId("");
    setProgNum("");
    let eDate = moment().format("YYYY-MM-DD");
    let sDate = moment().add(-30, "days").format("YYYY-MM-DD");
    setStartDate(sDate);
    setEndDate(eDate);
    let param = {
      customer_id: customerId,
      machine_id: "",
      progNum: "",
      startDate: sDate,
      endDate: eDate,
    };
    getData(param);
    getAlarmsFilters(customerId, (res) => {
      if (res != undefined && res != null && res["status"] == true) {
        setMachines(res["data"]["machineList"]);
        setProgNums(res["data"]["progNumList"]);
      }
    });
  }, [customerId]);

  return (
    <Spin spinning={isLoading}>
      <div>
        <div className="alarms-back">
          <div className="alarms-search-filters-container">
            Machine
            <Select
              className="page-changer-style"
              style={{
                width: 130,
                marginLeft: 5,
                marginRight: 10,
                textAlign: "left",
              }}
              dropdownClassName="page-changer-style-dropdown"
              value={machineId}
              onChange={(e) => setMachineId(e)}
            >
              <Option className="page-changer-item" value="">
                All
              </Option>
              {machines.map((x) => {
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
              style={{
                width: 90,
                marginLeft: 5,
                marginRight: 5,
                textAlign: "left",
              }}
              dropdownClassName="page-changer-style-dropdown"
              value={progNum}
              onChange={(e) => setProgNum(e)}
            >
              <Option className="page-changer-item" value="">
                All
              </Option>
              {progNums.map((x) => {
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
            <RangePicker
              value={[
                startDate != "" ? moment(startDate) : "",
                endDate != "" ? moment(endDate) : "",
              ]}
              style={{
                width: 220,
                marginLeft: 5,
                backgroundColor: "transparent",
                color: "white",
              }}
              onChange={onChange}
            />
            <Button
              ghost
              onClick={onSearch}
              style={{ borderRadius: 5, marginLeft: 5, width: 85 }}
            >
              Search
            </Button>
          </div>
          <AlarmsTable data={data} />
        </div>
      </div>
    </Spin>
  );
}

const mapStateToProps = (state, props) => ({
  app: state.app,
  tmsService: state.tmsService,
});
const mapDispatchToProps = (dispatch, props) => ({
  dispatch: dispatch,
});
export default connect(mapStateToProps, mapDispatchToProps)(Alarms);
