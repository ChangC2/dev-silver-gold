import { Col, Modal, Row } from "antd";

import { confirmable } from "react-confirm";

import domtoimage from "dom-to-image";
import { useRef } from "react";
import "./CustomCNCPDFPreviewModal.css";
import TrendPreviewWidget from "./TrendPreviewWidget";
import MachineHstGraphPreviewWidget from "./MachineHstGraphPreviewWidget";
import TankTimesPreviewTable from "./TankTimesPreviewTable";

const CustomCNCPDFPreviewModal = (props) => {
  const {
    show,
    proceed,
    customer_id,
    machine_id,
    daysoftimes,
    daysofhst,
    daysoftemp,
    customerInfo,
    chartData,
    chartHstData,
    step,
    dataSource,
    columns,
  } = props;

  const logoRef = useRef();
  const throughPutRef = useRef();
  const tankRef = useRef();

  const onClickOK = async () => {
    var hstUrl = await domtoimage.toJpeg(throughPutRef.current, {
      quality: 1,
    });
    var tempUrl = await domtoimage.toJpeg(tankRef.current, {
      quality: 1,
    });
    var logoUrl = await domtoimage.toJpeg(logoRef.current, {
      quality: 1,
    });
    proceed({ hstUrl: hstUrl, tempUrl: tempUrl, logoUrl: logoUrl });
  };

  const onClickCancel = () => {
    proceed(false);
  };

  return (
    <Modal
      width={1200}
      height={900}
      visible={show}
      closable={false}
      onOk={onClickOK}
      onCancel={onClickCancel}
      okText="Print"
    >
      <Row align="middle" style={{ width: "100%" }}>
        <Col span={6}>
          <div ref={logoRef} style={{ background: "white", padding: 5 }}></div>
        </Col>
        <Col
          span={12}
          style={{ textAlign: "center", fontSize: 30, fontWeight: "bold" }}
        >
          MMS Custom Report
        </Col>
        <Col span={6}></Col>
      </Row>

      <div>
        <MachineHstGraphPreviewWidget
          machine_id={machine_id}
          customer_id={customer_id}
          customerInfo={customerInfo}
          daysofhst={daysofhst}
          throughPutRef={throughPutRef}
          chartData={chartHstData}
        />

        <TrendPreviewWidget
          customer_id={customer_id}
          customerInfo={customerInfo}
          chartData={chartData}
          daysoftemp={daysoftemp}
          tankRef={tankRef}
          step={step}
        />

        <TankTimesPreviewTable
          columns={columns}
          dataSource={dataSource}
          daysoftimes={daysoftimes}
        />
      </div>
    </Modal>
  );
};

export default confirmable(CustomCNCPDFPreviewModal);
