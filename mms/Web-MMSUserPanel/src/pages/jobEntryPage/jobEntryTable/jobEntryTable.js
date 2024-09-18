import { useState } from "react";

import {
  Button,
  Col,
  DatePicker,
  Form,
  Input,
  InputNumber,
  message,
  Popconfirm,
  Row,
  Table,
  Upload,
} from "antd";
import "./jobEntryTable.css";

import { useSelector } from "react-redux";
import Urls, { postRequest } from "../../../services/common/urls";
import lang from "../../../services/lang";
import JobAddModal from "../jobAddModal/jobAddModal";
import JobFilesModal from "../jobFilesModel/jobFilesModal";

const EditableCell = ({
  editing,
  dataIndex,
  title,
  inputType,
  record,
  index,
  children,
  ...restProps
}) => {
  const inputNode = inputType === "number" ? <InputNumber /> : <Input />;
  return (
    <td {...restProps}>
      {editing ? (
        <Form.Item
          name={dataIndex}
          style={{
            margin: 0,
          }}
          rules={
            inputType === "datetime"
              ? [
                  {
                    pattern:
                      "[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}:[0-9]{2}",
                    required: true,
                    message: "YYYY-MM-DD HH:mm:ss ex:)2020-08-20 15:51:23",
                  },
                ]
              : []
          }
        >
          {inputNode}
        </Form.Item>
      ) : (
        children
      )}
    </td>
  );
};

function JobEntryTable(props) {
  const { langIndex } = useSelector((state) => state.app);
  const {
    entryList,
    updateEntry,
    addEntry,
    isBusy,
    setIsBusy,
    deleteEntry,
    selCustomerId,
    setSearchKey,
    setSearchDueDate,
    maxJobId,
  } = props;
  const { security_level } = props;
  const [form] = Form.useForm();
  const [data, setData] = useState(entryList);
  const [editingKey, setEditingKey] = useState("");
  const isEditing = (record) => record.key === editingKey;
  const [isShowModal, setIsShowModal] = useState(false);
  const [isShowFiles, setIsShowFiles] = useState(false);
  const [files, setFiles] = useState("");
  const [Id, setId] = useState("");

  const columns =
    security_level == 0
      ? [
          {
            title: lang(langIndex, "jobentry_id"),
            dataIndex: "Id",
            key: "Id",
          },
          {
            title: lang(langIndex, "jobentry_jobid"),
            dataIndex: "jobID",
            key: "jobID",
            filterSearch: true,
          },
          {
            title: lang(langIndex, "jobentry_customerid"),
            dataIndex: "customer",
            key: "customer",
          },
          {
            title: lang(langIndex, "jobentry_partnumber"),
            dataIndex: "partNumber",
            key: "partNumber",
            editable: true,
          },
          {
            title: lang(langIndex, "jobentry_programnumber"),
            dataIndex: "programNumber",
            key: "programNumber",
            editable: true,
          },
          {
            title: lang(langIndex, "jobentry_description"),
            dataIndex: "description",
            key: "description",
            editable: true,
          },
          {
            title: lang(langIndex, "jobentry_partspercycle"),
            dataIndex: "partsPerCycle",
            key: "partsPerCycle",
            editable: true,
          },
          {
            title: lang(langIndex, "jobentry_targetcycletime"),
            dataIndex: "targetCycleTime",
            key: "targetCycleTime",
            editable: true,
          },
          {
            title: lang(langIndex, "jobentry_qtyrequired"),
            dataIndex: "qtyRequired",
            key: "qtyRequired",
            editable: true,
          },
          {
            title: lang(langIndex, "jobentry_qtycompleted"),
            dataIndex: "qtyCompleted",
            key: "qtyCompleted",
            editable: true,
          },
          {
            title: lang(langIndex, "jobentry_orderdate"),
            dataIndex: "orderDate",
            key: "orderDate",
            editable: true,
          },
          {
            title: lang(langIndex, "jobentry_duedate"),
            dataIndex: "dueDate",
            key: "dueDate",
            editable: true,
          },
          {
            title: lang(langIndex, "jobentry_aux1data"),
            dataIndex: "aux1data",
            key: "aux1data",
            editable: true,
          },

          {
            title: lang(langIndex, "jobentry_aux2data"),
            dataIndex: "aux2data",
            key: "aux2data",
            editable: true,
          },

          {
            title: lang(langIndex, "jobentry_aux3data"),
            dataIndex: "aux3data",
            key: "aux3data",
            editable: true,
          },
        ]
      : [
          { title: lang(langIndex, "jobentry_id"), dataIndex: "Id", key: "Id" },
          {
            title: lang(langIndex, "jobentry_jobid"),
            dataIndex: "jobID",
            key: "jobID",
          },
          {
            title: lang(langIndex, "jobentry_customerid"),
            dataIndex: "customer",
            key: "customer",
          },
          {
            title: lang(langIndex, "jobentry_partnumber"),
            dataIndex: "partNumber",
            key: "partNumber",
            editable: true,
          },
          {
            title: lang(langIndex, "jobentry_programnumber"),
            dataIndex: "programNumber",
            key: "programNumber",
            editable: true,
          },
          {
            title: lang(langIndex, "jobentry_description"),
            dataIndex: "description",
            key: "description",
            editable: true,
          },
          {
            title: lang(langIndex, "jobentry_partspercycle"),
            dataIndex: "partsPerCycle",
            key: "partsPerCycle",
            editable: true,
          },
          {
            title: lang(langIndex, "jobentry_targetcycletime"),
            dataIndex: "targetCycleTime",
            key: "targetCycleTime",
            editable: true,
          },
          {
            title: lang(langIndex, "jobentry_qtyrequired"),
            dataIndex: "qtyRequired",
            key: "qtyRequired",
            editable: true,
          },
          {
            title: lang(langIndex, "jobentry_qtycompleted"),
            dataIndex: "qtyCompleted",
            key: "qtyCompleted",
            editable: true,
          },
          {
            title: lang(langIndex, "jobentry_orderdate"),
            dataIndex: "orderDate",
            key: "orderDate",
            editable: true,
          },
          {
            title: lang(langIndex, "jobentry_duedate"),
            dataIndex: "dueDate",
            key: "dueDate",
            editable: true,
          },

          {
            title: lang(langIndex, "jobentry_aux1data"),
            dataIndex: "aux1data",
            key: "aux1data",
            editable: true,
          },

          {
            title: lang(langIndex, "jobentry_aux2data"),
            dataIndex: "aux2data",
            key: "aux2data",
            editable: true,
          },

          {
            title: lang(langIndex, "jobentry_aux3data"),
            dataIndex: "aux3data",
            key: "aux3data",
            editable: true,
          },
          {
            title: "bom_item",
            dataIndex: "bom_item",
            key: "bom_item",
            editable: true,
          },
          {
            title: "bom_short_cd",
            dataIndex: "bom_short_cd",
            key: "bom_short_cd",
            editable: true,
          },
          {
            title: "bom_dim1",
            dataIndex: "bom_dim1",
            key: "bom_dim1",
            editable: true,
          },
          {
            title: "bom_dim2",
            dataIndex: "bom_dim2",
            key: "bom_dim2",
            editable: true,
          },
          {
            title: "bom_dim3",
            dataIndex: "bom_dim3",
            key: "bom_dim3",
            editable: true,
          },
          {
            title: "bom_dim4",
            dataIndex: "bom_dim4",
            key: "bom_dim4",
            editable: true,
          },
          {
            title: "bom_dim5",
            dataIndex: "bom_dim5",
            key: "bom_dim5",
            editable: true,
          },
          {
            title: "bom_dim6",
            dataIndex: "bom_dim6",
            key: "bom_dim6",
            editable: true,
          },
          {
            title: "bom_uom",
            dataIndex: "bom_uom",
            key: "bom_uom",
            editable: true,
          },

          {
            title: lang(langIndex, "jobentry_operation"),
            dataIndex: "operation",
            fixed: "right",
            render: (_, record) => {
              const editable = isEditing(record);

              return editable ? (
                <span>
                  <a
                    onClick={() => save(record.key)}
                    style={{
                      marginRight: 8,
                    }}
                  >
                    {lang(langIndex, "jobentry_save")}
                  </a>
                  <Popconfirm
                    title={lang(langIndex, "jobentry_suretocancel")}
                    onConfirm={cancel}
                  >
                    <a>{lang(langIndex, "jobentry_cancel")}</a>
                  </Popconfirm>
                </span>
              ) : (
                <div>
                  <a disabled={editingKey !== ""} onClick={() => edit(record)}>
                    {lang(langIndex, "jobentry_edit")}
                  </a>

                  <a
                    style={{ marginLeft: 10, color: "green" }}
                    onClick={() => onFiles(record)}
                  >
                    {lang(langIndex, "files")}
                  </a>

                  <Popconfirm
                    title={lang(langIndex, "jobentry_suretodelete")}
                    onConfirm={() => deleteRecord(record)}
                  >
                    <a
                      style={{ marginLeft: 10, color: "red" }}
                      disabled={editingKey !== ""}
                    >
                      {lang(langIndex, "jobentry_delete")}
                    </a>
                  </Popconfirm>
                </div>
              );
            },
          },
        ];

  const save = async (key) => {
    try {
      const row = await form.validateFields();
      updateEntry(key, row);
      setEditingKey("");
    } catch (errInfo) {}
  };

  const cancel = () => {
    setEditingKey("");
  };

  const onSearchJobId = (e) => {
    setSearchKey(e.target.value);
  };

  const onSearchDueDate = (date, dateString) => {
    setSearchDueDate(dateString);
  };

  const edit = (record) => {
    form.setFieldsValue({
      ...record,
    });
    setEditingKey(record.key);
  };

  const onFiles = (record) => {
    if (record.files == undefined) setFiles("");
    else setFiles(record.files);

    setId(record.Id);
    setIsShowFiles(true);
  };

  const deleteRecord = (record) => {
    deleteEntry(record.Id);
  };
  const mergedColumns = columns.map((col) => {
    if (!col.editable) {
      return col;
    }

    return {
      ...col,
      onCell: (record) => ({
        record,
        inputType:
          col.dataIndex === "targetCycleTime" ||
          col.dataIndex === "qtyRequired" ||
          col.dataIndex === "qtyCompleted" ||
          col.dataIndex === "partsPerCycle"
            ? "number"
            : col.dataIndex === "orderDate" || col.dataIndex === "dueDate"
            ? "datetime"
            : "text",
        dataIndex: col.dataIndex,
        title: col.title,
        editing: isEditing(record),
      }),
    };
  });
  const onClickAdd = () => {
    setIsShowModal(true);
  };

  const uploadCSV = {
    name: "file",
    action: Urls.UPLOAD_JOBENTRY_CSV,
    beforeUpload: (file) => {
      if (file.type !== "application/vnd.ms-excel") {
        message.error(`${file.name} is not a csv file`);
      }
      var formData = new FormData();
      formData.append("file", file);
      formData.append("customerID", selCustomerId);
      setIsBusy(true);
      postRequest(Urls.UPLOAD_JOBENTRY_CSV, formData, (response) => {
        setIsBusy(false);
        window.location.reload(false);
      });
      return file.type === "application/vnd.ms-excel"
        ? true
        : Upload.LIST_IGNORE;
    },
    onChange: (info) => {},
  };

  return (
    <div>
      {isShowModal && (
        <JobAddModal
          maxJobId={maxJobId}
          addEntry={addEntry}
          isShowModal={isShowModal}
          setIsShowModal={setIsShowModal}
        />
      )}

      {isShowFiles && (
        <JobFilesModal
          files={files}
          setFiles={setFiles}
          addEntry={addEntry}
          updateEntry={updateEntry}
          isShowFiles={isShowFiles}
          Id={Id}
          security_level={security_level}
          selCustomerId={selCustomerId}
          setIsShowFiles={setIsShowFiles}
        />
      )}

      <div style={{ marginTop: 5, marginBottom: 5 }}>
        <Row>
          <Col span={12}>
            <Input
              style={{ width: 150 }}
              placeholder="Search JobId"
              size="middle "
              onChange={onSearchJobId}
            />
            <DatePicker
              placeholder="Search DueDate"
              style={{ marginLeft: 15 }}
              onChange={onSearchDueDate}
            />
          </Col>
          <Col span={12} style={{ textAlign: "right" }}>
            {security_level != 0 && (
              <Button
                loading={isBusy}
                ghost
                onClick={onClickAdd}
                type="primary"
              >
                {lang(langIndex, "jobentry_addnew")}
              </Button>
            )}

            {security_level != 0 && (
              <Upload
                {...uploadCSV}
                style={{
                  marginLeft: 16,
                }}
              >
                <Button ghost type="primary">
                  {lang(langIndex, "jobentry_uploadcsv")}
                </Button>
              </Upload>
            )}
          </Col>
        </Row>
      </div>

      <Form form={form} component={false}>
        <Table
          className="entry-table-style"
          style={{ whiteSpace: "pre" }}
          components={{
            body: {
              cell: EditableCell,
            },
          }}
          // scroll={{ x: '100%' }}

          dataSource={entryList}
          // columns={columns}
          columns={mergedColumns}
          pagination={{
            pageSize: 10,
            onChange: cancel,
          }}
        />
      </Form>
    </div>
  );
}

export default JobEntryTable;
