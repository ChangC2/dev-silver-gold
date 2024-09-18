import { useEffect, useState } from "react";
import "./TableWidget.css";
import { ExportToCsv } from "export-to-csv";
import { Button, Select, Spin, Table } from "antd";
import { PrinterOutlined } from "@ant-design/icons";

const { Option } = Select;

const daysofpanelsList = [1, 2, 3, 4, 5, 6, 7];

function PanelsTableWidget(props) {
  const {
    panels,
    daysofpanels,
    setDaysOfPanels,
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

    var title1 = [
      "",
      "",
      "",
      "Panel Data Log",
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
    csvData.push(title1);

    var title2 = [
      "",
      "",
      "",
      "Panel# 1 mg",
      "",
      "",
      "Panel# 2 mg",
      "",
      "",
      "Panel# 3 mg",
      "",
      "",
      "",
      "",
    ];
    csvData.push(title2);

    var title3 = [
      "Date/Time",
      "Water Break pass/fail",
      "grade",
      "phosed",
      "striped",
      "mg",
      "phosed",
      "striped",
      "mg",
      "phosed",
      "striped",
      "mg",
      "mg average",
      "Notes",
    ];
    csvData.push(title3);

    selectedRowKeys.forEach((key) => {
      const index = dataSource.findIndex((item) => key === item.key);
      let record = dataSource[index];
      let row = [
        record["p_datetime"],
        record["p_break_pass_fail"],
        record["p_grade"],
        record["p_phose1"],
        record["p_striped1"],
        record["p_mg1"],
        record["p_phose2"],
        record["p_striped2"],
        record["p_mg2"],
        record["p_phose3"],
        record["p_striped3"],
        record["p_mg3"],
        record["p_average"],
        record["p_notes"],
      ];
      csvData.push(row);
    });

    const options = {
      filename: `${dataSource[0]["machine_id"]}_panel_data_log`,
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
      {
        title: "Date/time",
        dataIndex: "p_datetime",
        key: "p_datetime",
        width: 150,
      },
      {
        title: "Water Break pass/fail",
        width: 120,
        dataIndex: "p_break_pass_fail",
        key: "p_break_pass_fail",
        width: 120,
      },
      { title: "grade", dataIndex: "p_grade", key: "p_grade", width: 120 },

      {
        title: "panel1 phosed",
        dataIndex: "p_phose1",
        key: "p_phose1",
        width: 120,
      },
      {
        title: "panel1 striped",
        dataIndex: "p_striped1",
        key: "p_striped1",
        width: 120,
      },
      { title: "panel1 mg", dataIndex: "p_mg1", key: "p_mg1", width: 120 },

      {
        title: "panel2 phosed",
        dataIndex: "p_phose2",
        key: "p_phose2",
        width: 120,
      },
      {
        title: "panel2 striped",
        dataIndex: "p_striped2",
        key: "p_striped2",
        width: 120,
      },
      { title: "panel2 mg", dataIndex: "p_mg2", key: "p_mg2", width: 120 },

      {
        title: "panel3 phosed",
        dataIndex: "p_phose3",
        key: "p_phose3",
        width: 120,
      },
      {
        title: "panel3 striped",
        dataIndex: "p_striped3",
        key: "p_striped3",
        width: 120,
      },
      { title: "panel3 mg", dataIndex: "p_mg3", key: "p_mg3", width: 120 },

      {
        title: "mg average",
        dataIndex: "p_average",
        key: "p_average",
        width: 120,
      },
    ];
    setColumns(columns1);
    setPreviewColumns(columns1);
    setSelectedRowKeys([]);
  }, []);

  useEffect(() => {
    const ds = panels.map((x, index) => ({ ...x, key: index }));
    setDataSource(ds);
    setPreviewDataSource(ds);
  }, [panels]);

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
        Panel Data Log (
        <Select
          className="selector"
          dropdownClassName="selector-dropdown"
          style={{ width: 60, marginLeft: 5, marginRight: 5 }}
          value={daysofpanels}
          onChange={(e) => setDaysOfPanels(e)}
        >
          {daysofpanelsList.map((x) => {
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
        className="table-wrapper table-widget-table-style content-scrollbar-style"
        dataSource={dataSource}
        columns={columns}
        rowSelection={rowSelection}
        pagination={{ defaultPageSize: 5 }}
      />
    </div>
  );
}

export default PanelsTableWidget;
