import { Table } from "antd";
import "./AlarmsTable.css";



function AlarmsTable(props) {
  const { data } = props;

  const columns = [
    { title: "Machine ID", dataIndex: "machine_id", key: "machine_id" },
    { title: "Date", dataIndex: "date", key: "date" },
    { title: "Time", dataIndex: "time", key: "time" },
    { title: "Program Number", dataIndex: "progNum", key: "progNum" },
    { title: "Tool", dataIndex: "tool", key: "tool" },
    { title: "Section", dataIndex: "section", key: "section" },
    { title: "Channel", dataIndex: "channel", key: "channel" },
    { title: "Alarm Type", dataIndex: "alarmType", key: "alarmType" },
    {
      title: "Elapsed Monitor Time",
      dataIndex: "elapsedTime",
      key: "elapsedTime",
    },
  ];


  return (
    <div>
      <Table
        className="alarms-table-style"
        dataSource={data.map((x, index) => ({ ...x, key: index }))}
        columns={columns}
      />
    </div>
  );
}

export default AlarmsTable;
