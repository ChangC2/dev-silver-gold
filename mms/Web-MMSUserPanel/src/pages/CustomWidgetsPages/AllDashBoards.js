import {
  Button, Form, Input,
  Popconfirm, Table
} from "antd";
import "./AllDashboards.css";


import React, { useContext, useEffect, useRef, useState } from "react";
import { connect, useDispatch, useSelector } from "react-redux";

import "antd/dist/antd.css";
import {
  addCustomDashboard,
  deleteCustomDashboard,
  updateCustomDashboard
} from "services/common/cw_apis";

const EditableContext = React.createContext(null);

const EditableRow = ({ index, ...props }) => {
  const [form] = Form.useForm();
  return (
    <Form form={form} component={false}>
      <EditableContext.Provider value={form}>
        <tr {...props} />
      </EditableContext.Provider>
    </Form>
  );
};

const EditableCell = ({
  title,
  editable,
  children,
  dataIndex,
  record,
  handleSave,
  ...restProps
}) => {
  const [editing, setEditing] = useState(false);
  const inputRef = useRef(null);
  const form = useContext(EditableContext);
  useEffect(() => {
    if (editing) {
      inputRef.current.focus();
    }
  }, [editing]);

  const toggleEdit = () => {
    setEditing(!editing);
    form.setFieldsValue({
      [dataIndex]: record[dataIndex],
    });
  };

  const save = async () => {
    try {
      const values = await form.validateFields();
      toggleEdit();
      handleSave({ ...record, ...values });
    } catch (errInfo) {}
  };

  let childNode = children;

  if (editable) {
    childNode = editing ? (
      <Form.Item
        style={{
          margin: 0,
        }}
        name={dataIndex}
        rules={[
          {
            required: true,
            message: `${title} is required.`,
          },
        ]}
      >
        <Input ref={inputRef} onPressEnter={save} onBlur={save} />
      </Form.Item>
    ) : (
      <div
        className="editable-cell-value-wrap"
        style={{
          paddingRight: 24,
        }}
        onClick={toggleEdit}
      >
        {children}
      </div>
    );
  }

  return <td {...restProps}>{childNode}</td>;
};

const AllDashboards = (props) => {
  const dispatch = useDispatch();
  const cwService = useSelector((x) => x.cwService);
  const [dataSource, setDataSource] = useState([]);
  const [count, setCount] = useState(0);
  const { customer_id } = props;
  const customDashboardList = cwService.cwDashboards[customer_id];
  
  useEffect(() => {
    if (
      customDashboardList !== undefined
    ) {
      let ds = [];
      customDashboardList.map((dashboard) => {
        ds.push({
          key: dashboard.id,
          name: dashboard.name,
          order: dashboard.order,
        });
      });
      setDataSource(ds);
    }
  }, [customDashboardList]);

  useEffect(() => {
    setCount(dataSource.length);
  }, [dataSource]);

  const handleDelete = (key) => {
    const newData = dataSource.filter((item) => item.key !== key);
    setDataSource(newData);
    deleteCustomDashboard(
      key,
      dispatch,
      (res) => {}
    );
  };

  const defaultColumns = [
    {
      title: "Name",
      dataIndex: "name",
      width: "60%",
      editable: true,
    },
    {
      title: "Order",
      dataIndex: "order",
      width: "15%",
      align: "center",
      editable: true,
    },
    {
      title: "Operation",
      dataIndex: "operation",
      align: "center",
      render: (_, record) =>
        dataSource.length >= 1 ? (
          <Popconfirm
            title="Sure to delete?"
            onConfirm={() => handleDelete(record.key)}
          >
            <a>Delete</a>
          </Popconfirm>
        ) : null,
    },
  ];

  const handleAdd = () => {
    const newData = {
      key: count,
      name: `New Dashboard`,
      order: count + 1,
    };
    setDataSource([...dataSource, newData]);
    setCount(count + 1);

    addCustomDashboard(
      customer_id,
      newData.name,
      newData.order,
      dispatch,
      (res) => {}
    );
  };

  const handleSave = (row) => {
    const newData = [...dataSource];
    const index = newData.findIndex((item) => row.key === item.key);
    const item = newData[index];
    newData.splice(index, 1, { ...item, ...row });
    setDataSource(newData);

    updateCustomDashboard(row.key, row.name, row.order, dispatch, (res) => {
    });
  };

  const components = {
    body: {
      row: EditableRow,
      cell: EditableCell,
    },
  };
  const columns = defaultColumns.map((col) => {
    if (!col.editable) {
      return col;
    }

    return {
      ...col,
      onCell: (record) => ({
        record,
        editable: col.editable,
        dataIndex: col.dataIndex,
        title: col.title,
        handleSave,
      }),
    };
  });

  return (
    <div className="all-dashboards">
      <div className="all-dashboard-add-button">
        <Button ghost onClick={handleAdd}>
          Add New Dashboard +{" "}
        </Button>
      </div>
      <Table
        className="all-dashboard-table-style"
        components={components}
        rowClassName={() => "editable-row"}
        dataSource={dataSource}
        pagination={false}
        columns={columns}
      />
    </div>
  );
};

const mapStateToProps = (state, props) => ({
  cwService: state.cwService,
  app: state.app,
});

const mapDispatchToProps = (dispatch, props) => ({
  dispatch: dispatch,
});

export default connect(mapStateToProps, mapDispatchToProps)(AllDashboards);
