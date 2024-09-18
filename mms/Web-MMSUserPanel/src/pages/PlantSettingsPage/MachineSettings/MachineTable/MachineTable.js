import { Table } from "antd";
import {
  EditOutlined,
  ArrowUpOutlined,
  ArrowDownOutlined,
} from "@ant-design/icons";
import { useSelector } from "react-redux";
import { MACHINE_IMAGE_BASE_URL } from "../../../../services/common/urls";
import lang from "../../../../services/lang";
import "./MachineTable.css";
import { useEffect, useState } from "react";

function MachineTable(props) {
  const { langIndex } = useSelector((x) => x.app);
  const { machineList, onClickEditRecord, moveMachine } = props;
  const [columns, setColumns] = useState([]);
  useEffect(() => {
    setColumns([
      { title: "Order", dataIndex: "order", key: "order" },
      {
        title: lang(langIndex, "plant_machineid"),
        dataIndex: "machine_id",
        key: "machine_id",
      },
      {
        title: lang(langIndex, "plant_picture"),
        dataIndex: "machine_picture_url",
        key: "machine_picture_url",
        render: (machine_picture_url) => {
          return (
            <img
              alt=""
              className="machine-picture-style"
              src={
                machine_picture_url.includes("http")
                  ? machine_picture_url
                  : MACHINE_IMAGE_BASE_URL + machine_picture_url
              }
            />
          );
        },
      },
      {
        title: lang(langIndex, "jobentry_operation"),
        dataIndex: "operation",
        render: (_, record) => {
          return (
            <div>
              <a
                onClick={() => moveMachine(record, -1)}
                style={{ opacity: record.order == 1 ? 0.4 : 1 }}
              >
                <ArrowUpOutlined />
              </a>
              {"       "}
              <a
                onClick={() => moveMachine(record, 1)}
                style={{
                  opacity: record.order == machineList.length ? 0.4 : 1,
                }}
              >
                <ArrowDownOutlined />
              </a>
              {"       "}
              <a onClick={() => editRecord(record)}>
                <EditOutlined />
              </a>
            </div>
          );
        },
      },
    ]);
  }, [machineList]);

  const editRecord = (record) => {
    onClickEditRecord(record);
  };
  return (
    <div>
      <Table
        className="user-table-style"
        dataSource={machineList}
        columns={columns}
        pagination={{ defaultPageSize: 5 }}
      />
    </div>
  );
}

export default MachineTable;
