import { useEffect, useState } from "react";
import "./TableWidget.css";
import { ExportToCsv } from "export-to-csv";
import { Button, Select, Spin, Table } from "antd";
import { PrinterOutlined } from "@ant-design/icons";

const { Option } = Select;

const daysoftimesList = [1, 2, 3, 4, 5, 6, 7];

function PaintStationTableWidget(props) {
  const {
    paintStation,
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

    var titles = ["--- Paint ---", "", "", "", "", "", "", "", "", "", "", ""];
    csvData.push(titles);
    csvData.push(["", "", "", "", "", "", "", "", "", "", "", ""]);

    var header = [
      "Part ID",
      "Operator",
      "Finish Time",
      "Ambient Temp",
      "Ambient Humidity",
      "Ambient Dew Point",
      "Paint Booth Temp",
      "Paint Booth Humidity",
      "Paint Booth Dew Point",
      "Before",
      "After",
      "Used",
    ];
    csvData.push(header);

    selectedRowKeys.forEach((key) => {
      const index = dataSource.findIndex((item) => key === item.key);
      let record = dataSource[index];
      let row = [
        record["part_id"],
        record["operator"],
        record["time"],
        record["ambient_temp"],
        record["ambient_humidity"],
        record["ambient_dewpoint"],
        record["paintbooth_temp"],
        record["paintbooth_humidity"],
        record["paintbooth_dewpoint"],
        record["bitu_wt_before"],
        record["bitu_wt_after"],
        record["bitu_used"],
      ];

      csvData.push(row);
    });

    const options = {
      filename: `${"Paint"}`,
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
      { title: "Finish Time", dataIndex: "time", key: "time" },
      { title: "Ambient Temp", dataIndex: "ambient_temp", key: "ambient_temp" },
      {
        title: "Ambient Humidity",
        dataIndex: "ambient_humidity",
        key: "ambient_humidity",
      },
      {
        title: "Ambient Dew Point",
        dataIndex: "ambient_dewpoint",
        key: "ambient_dewpoint",
      },
      {
        title: "Paint Booth Temp",
        dataIndex: "paintbooth_temp",
        key: "paintbooth_temp",
      },
      {
        title: "Paint Booth Humidity",
        dataIndex: "paintbooth_humidity",
        key: "paintbooth_humidity",
      },
      {
        title: "Paint Booth Dew Point",
        dataIndex: "paintbooth_dewpoint",
        key: "paintbooth_dewpoint",
      },
      {
        title: "Before",
        dataIndex: "bitu_wt_before",
        key: "bitu_wt_before",
      },
      {
        title: "After",
        dataIndex: "bitu_wt_after",
        key: "bitu_wt_after",
      },
      { title: "Used", dataIndex: "bitu_used", key: "bitu_used" },
    ];
    setColumns(columns1);
    setPreviewColumns(columns1);
    setSelectedRowKeys([]);
  }, []);

  useEffect(() => {
    const ds = paintStation.map((x, index) => ({ ...x, key: index }));
    setDataSource(ds);
    setPreviewDataSource(ds);
  }, [paintStation]);

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
        className="table-widget-table-style"
        dataSource={dataSource}
        columns={columns}
        rowSelection={rowSelection}
        pagination={{ defaultPageSize: 5 }}
      />
    </div>
  );
}

export default PaintStationTableWidget;
