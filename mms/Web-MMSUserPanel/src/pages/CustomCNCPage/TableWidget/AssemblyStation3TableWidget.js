import { useEffect, useState } from "react";
import "./TableWidget.css";
import { ExportToCsv } from "export-to-csv";
import { Button, Select, Spin, Table } from "antd";
import { PrinterOutlined } from "@ant-design/icons";

const { Option } = Select;

const daysoftimesList = [1, 2, 3, 4, 5, 6, 7];

function AssemblyStation3TableWidget(props) {
  const {
    assemblyStation3,
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
      "--- Assembly Sub ---",
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
    ]);

    var header = [
      "Part ID",
      "Machine ID",
      "Operator",
      "Finish Time",
      "Processing time",
      "mil_d_16791_non_ionic_detergent_lot",
      "923as829_drive_lok_pins_lot",
      "ams_s_8802_polysulfide_lot",
      "4512421_base_plate_lt",
      "4512422_abs_insert_lot",
      "4512423_steel_insert_lot",
    ];
    csvData.push(header);

    selectedRowKeys.forEach((key) => {
      const index = dataSource.findIndex((item) => key === item.key);
      let record = dataSource[index];
      let row = [
        record["part_id"],
        record["machine_id"],
        record["operator"],
        record["time"],
        record["processing_time"],
        record["mil_d_16791_non_ionic_detergent_lot"],
        record["923as829_drive_lok_pins_lot"],
        record["ams_s_8802_polysulfide_lot"],
        record["4512421_base_plate_lt"],
        record["4512422_abs_insert_lot"],
        record["4512423_steel_insert_lot"],
      ];

      csvData.push(row);
    });

    const options = {
      filename: `${"Assembly Sub"}`,
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
      {
        title: "Processing time",
        dataIndex: "processing_time",
        key: "processing_time",
      },
      {
        title: "mil_d_16791_non_ionic_detergent_lot",
        dataIndex: "mil_d_16791_non_ionic_detergent_lot",
        key: "mil_d_16791_non_ionic_detergent_lot",
      },
      {
        title: "923as829_drive_lok_pins_lot",
        dataIndex: "923as829_drive_lok_pins_lot",
        key: "923as829_drive_lok_pins_lot",
      },
      {
        title: "ams_s_8802_polysulfide_lot",
        dataIndex: "ams_s_8802_polysulfide_lot",
        key: "ams_s_8802_polysulfide_lot",
      },
      {
        title: "4512421_base_plate_lt",
        dataIndex: "4512421_base_plate_lt",
        key: "4512421_base_plate_lt",
      },
      {
        title: "4512422_abs_insert_lot",
        dataIndex: "4512422_abs_insert_lot",
        key: "4512422_abs_insert_lot",
      },
      {
        title: "4512423_steel_insert_lot",
        dataIndex: "4512423_steel_insert_lot",
        key: "4512423_steel_insert_lot",
      },
    ];
    setColumns(columns1);
    setPreviewColumns(columns1);
    setSelectedRowKeys([]);
  }, []);

  useEffect(() => {
    const ds = assemblyStation3.map((x, index) => ({ ...x, key: index }));
    setDataSource(ds);
    setPreviewDataSource(ds);
  }, [assemblyStation3]);

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

export default AssemblyStation3TableWidget;
