import { useEffect, useState } from "react";
import "./TableWidget.css";
import { ExportToCsv } from "export-to-csv";
import { Button, Select, Spin, Table } from "antd";
import { PrinterOutlined } from "@ant-design/icons";

const { Option } = Select;

const daysoftimesList = [1, 2, 3, 4, 5, 6, 7];

function TankTimesTableWidget(props) {
  const {
    tanktimes,
    daysoftimes,
    setDaysOfTimes,
    setPreviewDataSource,
    setPreviewColumns,
  } = props;

  const [dataSource, setDataSource] = useState([]);
  const [columns, setColumns] = useState([]);
  const [selectedRowKeys, setSelectedRowKeys] = useState([]);

  const onSelectChange = (newSelectedRowKeys) => {
    setSelectedRowKeys(newSelectedRowKeys);
  };

  const rowSelection = {
    selectedRowKeys,
    onChange: onSelectChange,
  };

  const onClickCreateExcel = async () => {
    var csvData = [];

    var titles = [
      "",
      "",
      "",
      "",
      "",
      dataSource[0]["machine_id"],
      "",
      "",
      "",
      "",
      "",
      "",
      "",
      "",
      "",
      "",
      "",
      "",
      "",
      "",
    ];
    csvData.push(titles);
    csvData.push([
      "",
      "",
      "",
      "",
      "",
      "",
      "",
      "",
      "",
      "",
      "",
      "",
      "",
      "",
      "",
      "",
      "",
      "",
      "",
      "",
    ]);

    var header = [
      "Part ID",
      "Operator",
      "RM LOT",
      "Finish Time",
      "Tank1 Time",
      "Tank1 Temp",
      "Tank2 Time",
      "Tank2 Temp",
      "Tank3 Time",
      "Tank3 Temp",
      "Tank4 Time",
      "Tank4 Temp",
      "Tank5 Time",
      "Tank5 Temp",
      "Tank6 Time",
      "Tank6 Temp",
      "Tank7 Time",
      "Tank7 Temp",
      "Tank8 Time",
      "Tank8 Temp",
    ];
    csvData.push(header);

    selectedRowKeys.forEach((key) => {
      const index = dataSource.findIndex((item) => key === item.key);
      let record = dataSource[index];
      let row = [
        record["part_id"],
        record["operator"],
        record["rm_lot"],
        record["finish_time"],
        record["v1"].split("\n")[0],
        record["v1"].split("\n").length > 1 ? record["v1"].split("\n")[1] : 0,
        record["v2"].split("\n")[0],
        record["v2"].split("\n").length > 1 ? record["v2"].split("\n")[1] : 0,
        record["v3"].split("\n")[0],
        record["v3"].split("\n").length > 1 ? record["v3"].split("\n")[1] : 0,
        record["v4"].split("\n")[0],
        record["v4"].split("\n").length > 1 ? record["v4"].split("\n")[1] : 0,
        record["v5"].split("\n")[0],
        record["v5"].split("\n").length > 1 ? record["v5"].split("\n")[1] : 0,
        record["v6"].split("\n")[0],
        record["v6"].split("\n").length > 1 ? record["v6"].split("\n")[1] : 0,
        record["v7"].split("\n")[0],
        record["v7"].split("\n").length > 1 ? record["v7"].split("\n")[1] : 0,
        record["v8"].split("\n")[0],
        record["v8"].split("\n").length > 1 ? record["v8"].split("\n")[1] : 0,
      ];

      csvData.push(row);
    });

    const options = {
      filename: `${dataSource[0]["machine_id"]}`,
      fieldSeparator: ",",
      quoteStrings: '"',
      decimalSeparator: ".",
      showLabels: true,
      showTitle: false,
      useTextFile: false,
      useBom: true,
      useKeysAsHeaders: false,
    };
    const csvExporter = new ExportToCsv(options);
    csvExporter.generateCsv(csvData);
  };

  useEffect(() => {
    const columns1 = [
      { title: "Part ID", dataIndex: "part_id", key: "part_id" },
      // { title: "Machine ID", dataIndex: "machine_id", key: "machine_id" },
      { title: "Operator", dataIndex: "operator", key: "operator" },
      { title: "RM LOT", dataIndex: "rm_lot", key: "rm_lot" },
      { title: "Finish Time", dataIndex: "finish_time", key: "finish_time" },
      { title: "Tank1", dataIndex: "v1", key: "v1" },
      { title: "Tank2", dataIndex: "v2", key: "v2" },
      { title: "Tank3", dataIndex: "v3", key: "v3" },
      { title: "Tank4", dataIndex: "v4", key: "v4" },
      { title: "Tank5", dataIndex: "v5", key: "v5" },
      { title: "Tank6", dataIndex: "v6", key: "v6" },
      { title: "Tank7", dataIndex: "v7", key: "v7" },
      { title: "Tank8", dataIndex: "v8", key: "v8" },
    ];
    setColumns(columns1);
    setPreviewColumns(columns1);
    setSelectedRowKeys([]);
  }, []);

  useEffect(() => {
    const ds = tanktimes.map((x, index) => ({ ...x, key: index }));
    setDataSource(ds);
    setPreviewDataSource(ds);
  }, [tanktimes]);

  if (dataSource.length === 0) {
    return (
      <div style={{ textAlign: "center", paddingTop: 20 }}>
        <Spin />
      </div>
    );
  }

  return (
    <div className="table-widget-table-back">
      <div className="table-widget-table-title">
        Processing Times (
        <Select
          className="selector"
          dropdownClassName="selector-dropdown"
          style={{ width: 60, marginLeft: 5, marginRight: 5 }}
          value={daysoftimes}
          onChange={(e) => setDaysOfTimes(e)}
        >
          {daysoftimesList.map((x) => {
            return (
              <Option className="page-changer-item" key={`day-${x}`} value={x}>
                {x}
              </Option>
            );
          })}
        </Select>
        Days)
      </div>
      <Button
        className="table-widget-print-button"
        ghost
        style={{ width: 100 }}
        onClick={() => {
          onClickCreateExcel();
        }}
        disabled={selectedRowKeys.length === 0}
      >
        <PrinterOutlined />
        Print
      </Button>
      <Table
        className="table-widget-table-style content-scrollbar-style"
        dataSource={dataSource}
        columns={columns}
        rowSelection={rowSelection}
        pagination={{ defaultPageSize: 5 }}
      />
    </div>
  );
}

export default TankTimesTableWidget;
