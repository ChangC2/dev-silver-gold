import { useEffect, useState } from "react";
import "./TableWidget.css";
import { ExportToCsv } from "export-to-csv";
import { Button, Select, Spin, Table } from "antd";
import { PrinterOutlined } from "@ant-design/icons";

const { Option } = Select;

const daysoftimesList = [1, 2, 3, 4, 5, 6, 7];

function Blu136_assemblyTableWidget(props) {
  const {
    blu136_assembly,
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
      "-- Assembly Usage 1 --",
      "",
      "",
      "",
      "",
      "",
      "",
      "",
      "",
      "-- Assembly Usage 2 --",
      "",
      "",
      "",
      "",
      "",
      "",
      "",
      "",
      "-- Assembly Usage 3 --",
      "",
      "",
      "",
      "",
      "",
      "",
      "",
      "-- Tail Fairing --",
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
      "Base plat #",
      "Fwd fuze line",
      "Aft fuz line",
      "Fit chrg tube",
      "Washer lock iternal tooth",
      "Retainer fuz liner aft",
      "Nut fit charge tube",
      "Fwd chrg tube",
      "Aft charg tube",
      "Cap shipping",
      "Flat washer",
      "Hex head bolt",
      "Lug shipping",
      "Charge tube plug",
      "Screw cap hex head",
      "Washer lock sprg",
      "Bituminous",
      "Ring small",
      "Ring rubber",
      "Set screw",
      "Marking stencil",
      "Gun oil",
      "Corrision resistanct grease",
      "Silicone lubricant",
      "Degreasing solvent",
      "Shipping plugs",
      "Job #",
      "Screw lot",
      "Threadlock lot",
      "Set Screw lot",
      "Ams lot",
      "Two part polysulfie sealant",
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
        record["base_plat11"],
        record["20199266_fwd_fuze_line"],
        record["20199367_aft_fuz_line"],
        record["1265394_fit_chrg_tube"],
        record["1252629_washer_lock_iternal_tooth2"],
        record["4902493_retainer_fuz_liner_aft"],
        record["1123646_nut_fit_charg_tube2"],
        record["20199361_fwd_chrg_tube"],
        record["20199361_030_aft_charg_tube"],
        record["4512424_cap_shipping"],
        record["nas1149f0832p_flat_washer8"],
        record["nas568_41_hex_head_bolt8"],
        record["x20173251_lug_shipping2"],
        record["20199362_charge_tube_plug"],
        record["nasm90725_31_screw_cap_hex_head2"],
        record["ms35338_45_washer_lock_sprg4"],
        record["mil_dtl_450_bituminous"],
        record["as3582_236_o_ring_small2"],
        record["923as694_o_ring_rubber"],
        record["ms51964_69_set_screw1"],
        record["a_a_208_ink_marking_stencil"],
        record["mil_prf_63460_gun_oil"],
        record["mil_prf_16173_corrision_resistant_grease"],
        record["sae_as8660_silicone_lubricant"],
        record["mil_prf_680_degreasing_solvent"],
        record["shipping_plugs2"],
        record["job_at"],
        record["screw_lot6"],
        record["threadlock_271_lot"],
        record["set_screw_lot_6"],
        record["ams_s_8802_lot"],
        record["two_part_polysulfie_sealant"],
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
      { title: "base_plat11", dataIndex: "base_plat11", key: "base_plat11" },
      {
        title: "20199266_fwd_fuze_line",
        dataIndex: "20199266_fwd_fuze_line",
        key: "20199266_fwd_fuze_line",
      },
      {
        title: "20199367_aft_fuz_line",
        dataIndex: "20199367_aft_fuz_line",
        key: "20199367_aft_fuz_line",
      },
      {
        title: "1265394_fit_chrg_tube",
        dataIndex: "1265394_fit_chrg_tube",
        key: "1265394_fit_chrg_tube",
      },
      {
        title: "1252629_washer_lock_iternal_tooth2",
        dataIndex: "1252629_washer_lock_iternal_tooth2",
        key: "1252629_washer_lock_iternal_tooth2",
      },
      {
        title: "4902493_retainer_fuz_liner_aft",
        dataIndex: "4902493_retainer_fuz_liner_aft",
        key: "4902493_retainer_fuz_liner_aft",
      },
      {
        title: "1123646_nut_fit_charg_tube2",
        dataIndex: "1123646_nut_fit_charg_tube2",
        key: "1123646_nut_fit_charg_tube2",
      },
      {
        title: "20199361_fwd_chrg_tube",
        dataIndex: "20199361_fwd_chrg_tube",
        key: "20199361_fwd_chrg_tube",
      },
      {
        title: "20199361_030_aft_charg_tube",
        dataIndex: "20199361_030_aft_charg_tube",
        key: "20199361_030_aft_charg_tube",
      },
      {
        title: "4512424_cap_shipping",
        dataIndex: "4512424_cap_shipping",
        key: "4512424_cap_shipping",
      },
      {
        title: "nas1149f0832p_flat_washer8",
        dataIndex: "nas1149f0832p_flat_washer8",
        key: "nas1149f0832p_flat_washer8",
      },
      {
        title: "nas568_41_hex_head_bolt8",
        dataIndex: "nas568_41_hex_head_bolt8",
        key: "nas568_41_hex_head_bolt8",
      },
      {
        title: "x20173251_lug_shipping2",
        dataIndex: "x20173251_lug_shipping2",
        key: "x20173251_lug_shipping2",
      },
      {
        title: "20199362_charge_tube_plug",
        dataIndex: "20199362_charge_tube_plug",
        key: "20199362_charge_tube_plug",
      },
      {
        title: "nasm90725_31_screw_cap_hex_head2",
        dataIndex: "nasm90725_31_screw_cap_hex_head2",
        key: "nasm90725_31_screw_cap_hex_head2",
      },
      {
        title: "ms35338_45_washer_lock_sprg4",
        dataIndex: "ms35338_45_washer_lock_sprg4",
        key: "ms35338_45_washer_lock_sprg4",
      },

      {
        title: "mil_dtl_450_bituminous",
        dataIndex: "mil_dtl_450_bituminous",
        key: "mil_dtl_450_bituminous",
      },
      {
        title: "as3582_236_o_ring_small2",
        dataIndex: "as3582_236_o_ring_small2",
        key: "as3582_236_o_ring_small2",
      },
      {
        title: "923as694_o_ring_rubber",
        dataIndex: "923as694_o_ring_rubber",
        key: "923as694_o_ring_rubber",
      },
      {
        title: "ms51964_69_set_screw1",
        dataIndex: "ms51964_69_set_screw1",
        key: "ms51964_69_set_screw1",
      },
      {
        title: "a_a_208_ink_marking_stencil",
        dataIndex: "a_a_208_ink_marking_stencil",
        key: "a_a_208_ink_marking_stencil",
      },
      {
        title: "mil_prf_63460_gun_oil",
        dataIndex: "mil_prf_63460_gun_oil",
        key: "mil_prf_63460_gun_oil",
      },
      {
        title: "mil_prf_16173_corrision_resistant_grease",
        dataIndex: "mil_prf_16173_corrision_resistant_grease",
        key: "mil_prf_16173_corrision_resistant_grease",
      },
      {
        title: "sae_as8660_silicone_lubricant",
        dataIndex: "sae_as8660_silicone_lubricant",
        key: "sae_as8660_silicone_lubricant",
      },
      {
        title: "mil_prf_680_degreasing_solvent",
        dataIndex: "mil_prf_680_degreasing_solvent",
        key: "mil_prf_680_degreasing_solvent",
      },
      {
        title: "shipping_plugs2",
        dataIndex: "shipping_plugs2",
        key: "shipping_plugs2",
      },
      { title: "job_at", dataIndex: "job_at", key: "job_at" },
      { title: "screw_lot6", dataIndex: "screw_lot6", key: "screw_lot6" },
      {
        title: "threadlock_271_lot",
        dataIndex: "threadlock_271_lot",
        key: "threadlock_271_lot",
      },
      {
        title: "set_screw_lot_6",
        dataIndex: "set_screw_lot_6",
        key: "set_screw_lot_6",
      },
      {
        title: "ams_s_8802_lot",
        dataIndex: "ams_s_8802_lot",
        key: "ams_s_8802_lot",
      },
      {
        title: "two_part_polysulfie_sealant",
        dataIndex: "two_part_polysulfie_sealant",
        key: "two_part_polysulfie_sealant",
      },
    ];
    setColumns(columns1);
    setPreviewColumns(columns1);
    setSelectedRowKeys([]);
  }, []);

  useEffect(() => {
    const ds = blu136_assembly.map((x, index) => ({ ...x, key: index }));
    setDataSource(ds);
    setPreviewDataSource(ds);
  }, [blu136_assembly]);

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

export default Blu136_assemblyTableWidget;
