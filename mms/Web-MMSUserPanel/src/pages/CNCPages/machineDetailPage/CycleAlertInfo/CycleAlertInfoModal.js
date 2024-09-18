import { Col, message, Row } from "antd";
import Modal from "antd/lib/modal/Modal";
import React, { useEffect, useState } from "react";
import "./CycleAlertInfo.css";
import { InfoCircleOutlined } from "@ant-design/icons";
import { useSelector } from "react-redux";
import lang from "../../../../services/lang";

function CycleAlertInfoModal(props) {
  const { langIndex } = useSelector((x) => x.app);
  const { cycleAlertInfoModal, setCycleAlertInfoModal } = props;

  const data = [
    {
      title: lang(langIndex, "metro_pcs"),
      format: lang(langIndex, "metro_pcs_format"),
    },
    {
      title: lang(langIndex, "t_mobile"),
      format: lang(langIndex, "t_mobile_format"),
    },
    {
      title: lang(langIndex, "verizon_wireless"),
      format: lang(langIndex, "verizon_wireless_format"),
    },
    { title: lang(langIndex, "at_t"), format: lang(langIndex, "at_t_format") },
    {
      title: lang(langIndex, "sprint_pcs"),
      format: lang(langIndex, "sprint_pcs_format"),
    },
    {
      title: lang(langIndex, "nextel"),
      format: lang(langIndex, "nextel_format"),
    },
    {
      title: lang(langIndex, "cricket"),
      format: lang(langIndex, "cricket_format"),
    },
    {
      title: lang(langIndex, "us_Cellular"),
      format: lang(langIndex, "us_Cellular_format"),
    },

    { title: lang(langIndex, "cingular1"), format: lang(langIndex, "cingular1_format") },
    {
      title: lang(langIndex, "cingular2"),
      format: lang(langIndex, "cingular2_format"),
    },
  ];

  const dataUI = data.map((info,index) => {
    return (
      <Row className="cycler_alert_info_row my-own-button" key={"format-key-" + index}>
        <Col span={6} className="cycler_alert_info_key">
          {info.title}
        </Col>
        <Col span={18} className="cycler_alert_info_format">
          {info.format}
        </Col>
      </Row>
    );
  });

  return (
    <div>
      <Modal
        centered
        visible={cycleAlertInfoModal}
        title={null}
        onCancel={() => setCycleAlertInfoModal(false)}
        onOk={() => setCycleAlertInfoModal(false)}
        closable={true}
        cancelButtonProps={{ style: { display: "none" } }}
        className="app-setting-dialog-style"
        destroyOnClose={true}
        width={700}
      >
        <div>
          <div>
            <div className="cycler_alert_info_title">
              <Row>
                <Col span={1}>
                  <InfoCircleOutlined />
                </Col>
                <Col span={12}>{lang(langIndex, "about_cycle_stop_alert")}</Col>
              </Row>
            </div>
            <div className="cycler_alert_info_content">
              <div>{lang(langIndex, "cycle_alert_subtitle")}</div>
              <div>
                {dataUI}
              </div>
            </div>
          </div>
        </div>
      </Modal>
    </div>
  );
}

export default CycleAlertInfoModal;
