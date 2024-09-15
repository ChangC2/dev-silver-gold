import { Popconfirm, Table, Typography } from "antd";
import { useSelector } from "react-redux";
import {
  getDurationInHour
} from "../../../../../src/services/common/cnc_apis";
import {
  MACHINE_IMAGE_BASE_URL
} from "../../../../../src/services/common/urls";
import lang from "../../../../../src/services/lang";
import "./maintenanceTable.css";

const { Text } = Typography;
function MaintenanceTable(props) {

  const { setRecordId, setFiles, setIsShowFiles } = props;

  const { langIndex } = useSelector((x) => x.app);
  const {
    machineList,
    matintenanceEntryList,
    onClickEditRecord,
    onDeleteRecord,
  } = props;



  const onFiles = (record) => {
    setRecordId(record.id);
    if (record.files == undefined)
      setFiles('');
    else
      setFiles(record.files);

    setIsShowFiles(true);
  };



  const columns = [
    { title: "Id", dataIndex: "id", key: "id", width: 50 },
    {
      title: lang(langIndex, "maintenance_machine"),
      dataIndex: "machine_id",
      key: "machine_id",
      width: 100,
      render: (machine_id) => {
        var machine = machineList.filter((x) => x.machine_id == machine_id)[0];
        if (machine == undefined) return <div></div>;
        return (
          <img
            className="machine-picture-style"
            src={
              machine["machine_picture_url"] == "blank machine.png"
                ? MACHINE_IMAGE_BASE_URL + machine["machine_picture_url"]
                : machine["machine_picture_url"].includes("http")
                  ? machine["machine_picture_url"]
                  : MACHINE_IMAGE_BASE_URL + machine["machine_picture_url"]
            }
          />
        );
      },
    },
    {
      title: lang(langIndex, "maintenance_taskname"),
      dataIndex: "task_name",
      key: "task_name",
    },
    {
      title: lang(langIndex, "maintenance_taskcategory"),
      dataIndex: "task_category",
      key: "task_category",
    },
    {
      title: lang(langIndex, "maintenance_taskpicture"),
      dataIndex: "picture",
      key: "picture",
      width: 100,
      render: (picture) => {
        return <img className="machine-picture-style" src={
          picture.includes("http")
            ? picture
            : MACHINE_IMAGE_BASE_URL + picture
        } />;
      },
    },
    {
      title: lang(langIndex, "maintenance_taskinstruction"),
      dataIndex: "task_instruction",
      key: "task_instruction",
      width: "20%",
      render: (task_instruction) => {
        return (
          <Text style={{ whiteSpace: "pre-wrap", color: "#eeeeee" }}>
            {task_instruction}
          </Text>
        );
      },
    },
    {
      title: lang(langIndex, "maintenance_frequency"),
      dataIndex: "frequency",
      key: "frequency",
      render: (frequency) => {
        return Math.round(frequency);
      },
    },
    {
      title: lang(langIndex, "maintenance_cycle"),
      dataIndex: "interlock",
      key: "interlock",
      render: (interlock) => {
        return interlock == 1 ? "Yes" : "No";
      },
    },
    {
      title: lang(langIndex, "maintenance_timetonext"),
      dataIndex: "completed_hours",
      key: "completed_hours",
      render: (completed_hours, data) => {
        // if (data.is_finished == 1) return 'finished';
        if (completed_hours == undefined || completed_hours == 0) return "";
        // if (completed_hours < 0) return 'finished';
        var allTime = data.frequency;
        var hourText = getDurationInHour(
          completed_hours > 0 ? completed_hours : -completed_hours
        );
        if (completed_hours < 0) {
          return <div style={{ color: "red" }}>Overdue by: {hourText} Hrs</div>;
        }
        if (allTime * 0.2 >= hourText) {
          return <div style={{ color: "yellow" }}>Due In: {hourText} Hrs</div>;
        } else {
          return <div style={{ color: "#76CB66" }}>Due In: {hourText} Hrs</div>;
        }
      },
    },
    // {
    //     title: 'Note', dataIndex: 'note', key: 'note', width: '20%',
    //     render: (note) => {
    //         return <Text style={{ whiteSpace: 'pre-wrap', color: '#eeeeee' }}>{note}</Text>
    //     },
    // },
    {
      title: lang(langIndex, "jobentry_operation"),
      dataIndex: "operation",
      width: 150,
      fixed: "right",
      render: (_, record) => {
        return (
          <div>
            <a onClick={() => editRecord(record)}>{lang(langIndex, "jobentry_edit")}</a>
            <a style={{ marginLeft: 10, color: "green" }} onClick={() => onFiles(record)}>
              {lang(langIndex, "files")}
            </a>
            <Popconfirm
              title={lang(langIndex, 'jobentry_suretodelete')}
              onConfirm={() => onDeleteRecord(record)}
            >
              <a style={{ marginLeft: 10, color: "red" }}>
                {lang(langIndex, "jobentry_delete")}
              </a>
            </Popconfirm>
          </div>
        );
      },
    },
  ];
  const editRecord = (record) => {
    onClickEditRecord(record);
  };


  return (
    <div style={{ marginRight: 0 }}>
      <Table
        className="maintenance-table-style"
        dataSource={matintenanceEntryList}
        columns={columns}
        scroll={{ x: 1300 }}
      />
    </div>
  );
}

export default MaintenanceTable;
