import { useEffect, useState } from "react";
import "./TableWidget.css";
import { ExportToCsv } from "export-to-csv";
import { Button, Select, Spin, Table } from "antd";
import { PrinterOutlined } from "@ant-design/icons";

const { Option } = Select;

const daysofstagesList = [1, 2, 3, 4, 5, 6, 7];

function StagesTableWidget(props) {
  const {
    stages,
    daysofstages,
    setDaysOfStages,
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
      "Titration Results",
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
    ];
    csvData.push(title1);
    
    var title2 = [
      "Date/Time",
      "",
      "Stage 1: pre clean Gardocien 1207",
      "",
      "",
      "",
      "",
      "",
      "",
      "",
      "",
      "Stage2 : Clean gardoclean 1207",
      "",
      "",
      "",
      "",
      "",
      "",
      "",
      "",
      "Stage 3 : rinse city water",
      "",
      "",
      "",
      "",
      "",
      "Stage 4: Activate gardolene v6513",
      "",
      "",
      "",
      "",
      "",
      "Stage 5: zinc phosphate",
      "",
      "",
      "",
      "",
      "",
      "",
      "",
      "",
      "Stage 6: rinse- city water",
      "",
      "",
      "",
      "",
      "",
      "Stage 7: seal gardolene D 6800/6",
      "",
      "",
      "",
      "",
      "",
      "Stage 8: Final rinse DI water",
      "",
      "",
      "",
      "",
      "",
      "Stage 9: Dry Manula air",
      "",
      "",
    ];
    csvData.push(title2);

    var title3 = [
      "",
      "Steps",
      "FA mls",
      "conc",
      "TA mls",
      "TA/FA",
      "conductivity",
      "Temp",
      "Time",
      "Notes",
      "",
      "FA mls",
      "conc",
      "TA mls",
      "TA/FA",
      "conductivity",
      "Temp",
      "Time",
      "Notes",
      "",
      "conductivity",
      "ph",
      "total alk",
      "time",
      "notes",
      "",
      "conductivity",
      "ph",
      "temp",
      "time",
      "notes",
      "",
      "total acid",
      "free acid",
      "gas points",
      "zinc",
      "conductivity",
      "temp",
      "time",
      "notes",
      "",
      "conductivity",
      "ph",
      "total acid",
      "time",
      "notes",
      "",
      "conductivity",
      "ph",
      "absorbance",
      "time",
      "notes",
      "",
      "conductivity",
      "ph",
      "temp",
      "time",
      "notes",
      "",
      "temp",
      "time",
      "notes",
    ];
    csvData.push(title3);

    selectedRowKeys.forEach((key) => {
      const index = dataSource.findIndex((item) => key === item.key);
      let record = dataSource[index];
      let row = [
        record["datetime"],
        "",
        record["s1_fa_mls"],
        record["s1_conc"],
        record["s1_ta_mls"],
        record["s1_ta_fa"],
        record["s1_conductivity"],
        record["s1_temp"],
        record["s1_time"],
        record["s1_notes"],
        "",
        record["s2_fa_mls"],
        record["s2_conc"],
        record["s2_ta_mls"],
        record["s2_ta_fa"],
        record["s2_conductivity"],
        record["s2_temp"],
        record["s2_time"],
        record["s2_notes"],
        "",
        record["s3_conductivity"],
        record["s3_ph"],
        record["s3_total_alk"],
        record["s3_time"],
        record["s3_notes"],
        "",
        record["s4_conductivity"],
        record["s4_ph"],
        record["s4_temp"],
        record["s4_time"],
        record["s4_notes"],
        "",
        record["s5_total_acid"],
        record["s5_free_acid"],
        record["s5_gas_points"],
        record["s5_zinc"],
        record["s5_conductivity"],
        record["s5_temp"],
        record["s5_time"],
        record["s5_notes"],
        "",
        record["s6_conductivity"],
        record["s6_ph"],
        record["s6_total_acid"],
        record["s6_time"],
        record["s6_notes"],
        "",
        record["s7_conductivity"],
        record["s7_ph"],
        record["s7_absorbance"],
        record["s7_time"],
        record["s7_notes"],
        "",
        record["s8_conductivity"],
        record["s8_ph"],
        record["s8_temp"],
        record["s8_time"],
        record["s8_notes"],
        "",
        record["s9_temp"],
        record["s9_time"],
        record["s9_notes"],
      ];
      csvData.push(row);
    });

    const options = {
      filename: `${dataSource[0]["machine_id"]}_stage_data_log`,
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
        dataIndex: "datetime",
        key: "datetime",
        width: 150,
      },
      {
        title: "stage1 fa_mls",
        dataIndex: "s1_fa_mls",
        key: "s1_fa_mls",
        width: 120,
      },
      {
        title: "stage1 conc",
        dataIndex: "s1_conc",
        key: "s1_conc",
        width: 120,
      },
      {
        title: "stage1 ta_mls",
        dataIndex: "s1_ta_mls",
        key: "s1_ta_mls",
        width: 120,
      },
      {
        title: "stage1 ta_fa",
        dataIndex: "s1_ta_fa",
        key: "s1_ta_fa",
        width: 120,
      },
      {
        title: "stage1 conductivity",
        dataIndex: "s1_conductivity",
        key: "s1_conductivity",
        width: 120,
      },
      {
        title: "stage1 temp",
        dataIndex: "s1_temp",
        key: "s1_temp",
        width: 120,
      },
      {
        title: "stage1 time",
        dataIndex: "s1_time",
        key: "s1_time",
        width: 120,
      },
      {
        title: "stage1 notes",
        dataIndex: "s1_notes",
        key: "s1_notes",
        width: 120,
      },
      {
        title: "stage2 fa_mls",
        dataIndex: "s2_fa_mls",
        key: "s2_fa_mls",
        width: 120,
      },
      {
        title: "stage2 conc",
        dataIndex: "s2_conc",
        key: "s2_conc",
        width: 120,
      },
      {
        title: "stage2 ta_mls",
        dataIndex: "s2_ta_mls",
        key: "s2_ta_mls",
        width: 120,
      },
      {
        title: "stage2 ta_fa",
        dataIndex: "s2_ta_fa",
        key: "s2_ta_fa",
        width: 120,
      },
      {
        title: "stage2 conductivity",
        dataIndex: "s2_conductivity",
        key: "s2_conductivity",
        width: 120,
      },
      {
        title: "stage2 temp",
        dataIndex: "s2_temp",
        key: "s2_temp",
        width: 120,
      },
      {
        title: "stage2 time",
        dataIndex: "s2_time",
        key: "s2_time",
        width: 120,
      },
      {
        title: "stage2 notes",
        dataIndex: "s2_notes",
        key: "s2_notes",
        width: 120,
      },
      {
        title: "stage3 conductivity",
        dataIndex: "s3_conductivity",
        key: "s3_conductivity",
        width: 120,
      },
      { title: "stage3 ph", dataIndex: "s3_ph", key: "s3_ph", width: 120 },
      {
        title: "stage3 total_alk",
        dataIndex: "s3_total_alk",
        key: "s3_total_alk",
        width: 120,
      },
      {
        title: "stage3 time",
        dataIndex: "s3_time",
        key: "s3_time",
        width: 120,
      },
      {
        title: "stage3 notes",
        dataIndex: "s3_notes",
        key: "s3_notes",
        width: 120,
      },
      {
        title: "stage4 conductivity",
        dataIndex: "s4_conductivity",
        key: "s4_conductivity",
        width: 120,
      },
      { title: "stage4 ph", dataIndex: "s4_ph", key: "s4_ph", width: 120 },
      {
        title: "stage4 temp",
        dataIndex: "s4_temp",
        key: "s4_temp",
        width: 120,
      },
      {
        title: "stage4 time",
        dataIndex: "s4_time",
        key: "s4_time",
        width: 120,
      },
      {
        title: "stage4 notes",
        dataIndex: "s4_notes",
        key: "s4_notes",
        width: 120,
      },
      {
        title: "stage5 total_acid",
        dataIndex: "s5_total_acid",
        key: "s5_total_acid",
        width: 120,
      },
      {
        title: "stage5 free_acid",
        dataIndex: "s5_free_acid",
        key: "s5_free_acid",
        width: 120,
      },
      {
        title: "stage5 gas_points",
        dataIndex: "s5_gas_points",
        key: "s5_gas_points",
        width: 120,
      },
      {
        title: "stage5 zinc",
        dataIndex: "s5_zinc",
        key: "s5_zinc",
        width: 120,
      },
      {
        title: "stage5 conductivity",
        dataIndex: "s5_conductivity",
        key: "s5_conductivity",
        width: 120,
      },
      {
        title: "stage5 temp",
        dataIndex: "s5_temp",
        key: "s5_temp",
        width: 120,
      },
      {
        title: "stage5 time",
        dataIndex: "s5_time",
        key: "s5_time",
        width: 120,
      },
      {
        title: "stage5 notes",
        dataIndex: "s5_notes",
        key: "s5_notes",
        width: 120,
      },
      {
        title: "stage6 conductivity",
        dataIndex: "s6_conductivity",
        key: "s6_conductivity",
        width: 120,
      },
      { title: "stage6 ph", dataIndex: "s6_ph", key: "s6_ph", width: 120 },
      {
        title: "stage6 total_acid",
        dataIndex: "s6_total_acid",
        key: "s6_total_acid",
        width: 120,
      },
      {
        title: "stage6 time",
        dataIndex: "s6_time",
        key: "s6_time",
        width: 120,
      },
      {
        title: "stage6 notes",
        dataIndex: "s6_notes",
        key: "s6_notes",
        width: 120,
      },
      {
        title: "stage7 conductivity",
        dataIndex: "s7_conductivity",
        key: "s7_conductivity",
        width: 120,
      },
      { title: "stage7 ph", dataIndex: "s7_ph", key: "s7_ph", width: 120 },
      {
        title: "stage7 absorbance",
        dataIndex: "s7_absorbance",
        key: "s7_absorbance",
        width: 120,
      },
      {
        title: "stage7 time",
        dataIndex: "s7_time",
        key: "s7_time",
        width: 120,
      },
      {
        title: "stage7 notes",
        dataIndex: "s7_notes",
        key: "s7_notes",
        width: 120,
      },
      {
        title: "stage8 conductivity",
        dataIndex: "s8_conductivity",
        key: "s8_conductivity",
        width: 120,
      },
      { title: "stage8 ph", dataIndex: "s8_ph", key: "s8_ph", width: 120 },
      {
        title: "stage8 temp",
        dataIndex: "s8_temp",
        key: "s8_temp",
        width: 120,
      },
      {
        title: "stage8 time",
        dataIndex: "s8_time",
        key: "s8_time",
        width: 120,
      },
      {
        title: "stage8 notes",
        dataIndex: "s8_notes",
        key: "s8_notes",
        width: 120,
      },
      {
        title: "stage9 temp",
        dataIndex: "s9_temp",
        key: "s9_temp",
        width: 120,
      },
      {
        title: "stage9 time",
        dataIndex: "s9_time",
        key: "s9_time",
        width: 120,
      },
      {
        title: "stage9 notes",
        dataIndex: "s9_notes",
        key: "s9_notes",
        width: 120,
      },
    ];
    setColumns(columns1);
    setPreviewColumns(columns1);
    setSelectedRowKeys([]);
  }, []);

  useEffect(() => {
    const ds = stages.map((x, index) => ({ ...x, key: index }));
    setDataSource(ds);
    setPreviewDataSource(ds);
  }, [stages]);

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
        Stage Data Log (
        <Select
          className="selector"
          dropdownClassName="selector-dropdown"
          style={{ width: 60, marginLeft: 5, marginRight: 5 }}
          value={daysofstages}
          onChange={(e) => setDaysOfStages(e)}
        >
          {daysofstagesList.map((x) => {
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
        style={{ width: 120 }}
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

export default StagesTableWidget;
