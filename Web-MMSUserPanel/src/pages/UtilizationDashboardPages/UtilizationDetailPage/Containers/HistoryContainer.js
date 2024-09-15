import { List } from "antd";

import { useSelector } from "react-redux";
import {
  GetCustomerCurrentTime,
  GetOneMachineData,
  pad,
} from "../../../../../src/services/common/cnc_apis";
import lang from "../../../../../src/services/lang";

function HistoryContainer(props) {
  const { langIndex } = useSelector((x) => x.app);

  const {
    customer_id,
    machineInfo,
    customerInfo,
    setCurrentDate,
    setMyHst,
    setMyGanttList,
    currentDate,
    todayString,
    shifts,
    shift,
  } = props;
  const { setIsBusy } = props;
  let dateArray = [""];
  for (var i = 1; i < 30; i++) {
    var c = GetCustomerCurrentTime(customerInfo["timezone"]);
    c.setDate(c.getDate() - i);
    dateArray.push(
      pad(c.getMonth() + 1) + "/" + pad(c.getDate()) + "/" + c.getFullYear()
    );
  }

  const onSelectDate = (date) => {
    if (date === "") {
      setCurrentDate("");
      return;
    }

    const startDate = date;
    let endDate = Date.parse(date);
    endDate = new Date(endDate);
    endDate.setDate(endDate.getDate() + 1);
    endDate =
      pad(endDate.getMonth() + 1) +
      "/" +
      pad(endDate.getDate()) +
      "/" +
      pad(endDate.getFullYear());

    setIsBusy(true);
    GetOneMachineData(
      customer_id,
      customerInfo["timezone"],
      startDate,
      machineInfo["machine_id"],
      shifts[shift],
      (res) => {
        setIsBusy(false);
        if (res != null) {
          setMyGanttList([...res.data.gantt]);
          setMyHst({ ...res.data.hst });
          setCurrentDate(startDate);
        }
      }
    );
  };

  return (
    <div className="history-container">
      <div style={{ borderBottom: "1px solid white", textAlign: "left" }}>
        <h3 style={{ color: "white" }}>{lang(langIndex, "cnc_history")}</h3>
      </div>
      <div
        className="content-scrollbar-style menu-container"
        style={{ maxHeight: 180, overflow: "auto", marginTop: 3 }}
      >
        <List
          header={null}
          footer={null}
          //  bordered
          dataSource={dateArray}
          size={"small"}
          renderItem={(item) => {
            return (
              <List.Item
                className={
                  item === currentDate
                    ? "history-item history-item-selected"
                    : "history-item"
                }
                onClick={() => {
                  onSelectDate(item);
                }}
                style={{ height: 35, borderColor: "#333333" }}
              >
                {item === "" ? todayString : item}
              </List.Item>
            );
          }}
        />
      </div>
    </div>
  );
}

export default HistoryContainer;
