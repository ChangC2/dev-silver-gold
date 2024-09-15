// @flow strict

import { Col, Popconfirm, Row, Table } from "antd";
import React from "react";
import { MACHINE_IMAGE_BASE_URL } from "../../../../services/common/urls";
import lang from "../../../../services/lang";
import "./GroupTableWidget.css";
function GroupTableWidget(props) {
  const {
    machineList,
    machineGroupList,
    langIndex,
    onClickEditGroup,
    onClickDeleteGroup,
  } = props;
  const editRecord = (info) => {
    onClickEditGroup(info);
  };

  const columns = [
    { title: "Id", dataIndex: "id", key: "id" },
    {
      title: "Group Name",
      dataIndex: "name",
      key: "name",
    },

    {
      title: lang(langIndex, "maintenance_machine"),
      dataIndex: "machine_list",
      key: "machine_list",
      render: (machine_list, group) => {
        return machine_list.split(",").map((machine_id) => {
          const machine = machineList.find((x) => x.machine_id == machine_id);
          if (machine == undefined) return <div></div>;
          return (
            <div key={`machine-group-${group.id}-${machine_id}`}>
              <Row align={"middle"}>
                <Col>
                  <img
                    style={{ maxWidth: 50 }}
                    src={MACHINE_IMAGE_BASE_URL + machine.machine_picture_url}
                  />
                </Col>
                <Col>{machine_id}</Col>
              </Row>
            </div>
          );
        });
      },
    },
    {
      title: lang(langIndex, "jobentry_operation"),
      dataIndex: "operation",
      render: (_, record) => {
        return (
          <Row gutter={16}>
            <Col>
              <a
                style={{ display: "inline-block" }}
                onClick={() => editRecord(record)}
              >
                {lang(langIndex, "jobentry_edit")}
              </a>
            </Col>
            <Col>
              <Popconfirm
                title={lang(langIndex, "jobentry_suretodelete")}
                onConfirm={() => onClickDeleteGroup(record)}
              >
                <a style={{ display: "inline-block", color: "red" }}>
                  {lang(langIndex, "jobentry_delete")}
                </a>
              </Popconfirm>
            </Col>
          </Row>
        );
      },
    },
  ];
  return (
    <div>
      <Table
        className="machine-table-style"
        dataSource={machineGroupList.map((x) => ({ ...x, key: x.id }))}
        columns={columns}
      />
    </div>
  );
}

export default GroupTableWidget;
