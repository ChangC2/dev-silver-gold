import { useEffect, useState } from "react";
import "./TableWidget.css";
import { ExportToCsv } from "export-to-csv";
import { Button, Select, Spin, Table } from "antd";
import { PrinterOutlined } from "@ant-design/icons";

const { Option } = Select;

const daysoftimesList = [1, 2, 3, 4, 5, 6, 7];

function AssemblyStation1TableWidget(props) {
  const {
    assemblyStation1,
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
      dataSource[0]["machine_id"],
      "",
      "",
      "",
      "--- Assembly Usage 1 ---",
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
      "--- Assembly Usage 2 ---",
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
      "Finish Time",
      "Processing time",
      "Case number",
      "Serial",
      "Shipment",
      "Empty wt",
      "Center grav",
      "Aft ass",
      "Aft retain ring",
      "Shipping cover",
      "shipping plug",
      "End cap",
      "Lugs",
      "Retain ring",
      "Adapt ring",
      "Impact ring",
      "Total wt",
      "Ass center grav",
      "Degrease sol",
      "Corrosin prev compound",
      "Ship cover oring",
      "Oring grease",
      "Protective end cap",
      "End cap set screw",
      "Lifting lug bolt",
      "Lifting lug washer",
      "Stencil ink",
    ];
    csvData.push(header);

    selectedRowKeys.forEach((key) => {
      const index = dataSource.findIndex((item) => key === item.key);
      let record = dataSource[index];
      let row = [
        record["part_id"],
        record["operator"],
        record["time"],
        record["processing_time"],
        record["case_number"],
        record["serial"],
        record["shipment"],
        record["empty_wt"],
        record["center_grav"],
        record["aft_ass"],
        record["aft_retain_ring"],
        record["shipping_cover"],
        record["shipping_plug"],
        record["end_cap"],
        record["lugs"],
        record["retain_ring"],
        record["adapt_ring"],
        record["impact_ring"],
        record["total_wt"],
        record["ass_center_grav"],
        record["degrease_sol"],
        record["corrosin_prev_compound"],
        record["ship_cover_oring"],
        record["oring_grease"],
        record["protective_end_cap"],
        record["end_cap_set_screw"],
        record["lifting_lug_bolt"],
        record["lifting_lug_washer"],
        record["stencil_ink"],
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
      { title: "Finish Time", dataIndex: "time", key: "time" },
      {
        title: "Processing time",
        dataIndex: "processing_time",
        key: "processing_time",
      },
      { title: "Case number", dataIndex: "case_number", key: "case_number" },
      { title: "Serial", dataIndex: "serial", key: "serial" },
      { title: "Shipment", dataIndex: "shipment", key: "shipment" },
      { title: "Empty wt", dataIndex: "empty_wt", key: "empty_wt" },
      { title: "Center grav", dataIndex: "center_grav", key: "center_grav" },
      { title: "After ass", dataIndex: "aft_ass", key: "aft_ass" },
      {
        title: "After retain ring",
        dataIndex: "aft_retain_ring",
        key: "aft_retain_ring",
      },
      {
        title: "Shipping cover",
        dataIndex: "shipping_cover",
        key: "shipping_cover",
      },
      {
        title: "Shipping plug",
        dataIndex: "shipping_plug",
        key: "shipping_plug",
      },
      { title: "End cap", dataIndex: "end_cap", key: "end_cap" },
      { title: "Lugs", dataIndex: "lugs", key: "lugs" },
      { title: "Retain ring", dataIndex: "retain_ring", key: "retain_ring" },
      { title: "Adapt ring", dataIndex: "adapt_ring", key: "adapt_ring" },
      { title: "Impact ring", dataIndex: "impact_ring", key: "impact_ring" },
      { title: "Total wt", dataIndex: "total_wt", key: "total_wt" },
      {
        title: "Ass center grav",
        dataIndex: "ass_center_grav",
        key: "ass_center_grav",
      },
      { title: "Degrease sol", dataIndex: "degrease_sol", key: "degrease_sol" },
      {
        title: "Corrosin prev compound",
        dataIndex: "corrosin_prev_compound",
        key: "corrosin_prev_compound",
      },
      {
        title: "Ship cover oring",
        dataIndex: "ship_cover_oring",
        key: "ship_cover_oring",
      },
      { title: "Oring grease", dataIndex: "oring_grease", key: "oring_grease" },
      {
        title: "Protective end cap",
        dataIndex: "protective_end_cap",
        key: "protective_end_cap",
      },
      {
        title: "End cap set screw",
        dataIndex: "end_cap_set_screw",
        key: "end_cap_set_screw",
      },
      {
        title: "Lifting lug bolt",
        dataIndex: "lifting_lug_bolt",
        key: "lifting_lug_bolt",
      },
      {
        title: "Lifting lug washer",
        dataIndex: "lifting_lug_washer",
        key: "lifting_lug_washer",
      },
      { title: "Stencil ink", dataIndex: "stencil_ink", key: "stencil_ink" },
    ];
    setColumns(columns1);
    setPreviewColumns(columns1);
    setSelectedRowKeys([]);
  }, []);

  useEffect(() => {
    const ds = assemblyStation1.map((x, index) => ({ ...x, key: index }));
    setDataSource(ds);
    setPreviewDataSource(ds);
  }, [assemblyStation1]);

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

export default AssemblyStation1TableWidget;
