import { Col, Modal, Row } from "antd";
import { confirmable } from "react-confirm";

import domtoimage from "dom-to-image";
import { useRef } from "react";
import "./PDFPreviewModal.css";
import { sizeMobile } from "services/common/constants";
import CWTrendChart from "./CWTrendChart";
import CWBarChart from "./CWBarChart";
import CWGaugeChart from "./CWGaugeChart";
import CWPieChart from "./CWPieChart";
import CWTextInfo from "./CWTextInfo";
import CWMachineInfo from "./CWMachineInfo";
import { useEffect } from "react";

const PDFPreviewModal = (props) => {
  const { show, proceed, customWidgets, screenSize, customerInfo } = props;

  const logoRef = useRef();
  const refs = useRef([]);

  const mainUI = customWidgets.map((widget, i) => {
    {
      switch (parseInt(widget.widget_type)) {
        case 0:
          return (
            <Col
              span={
                widget.widget_size !== "0" && screenSize.width > sizeMobile
                  ? 12
                  : 24
              }
              key={`col-${widget.id}`}
            >
              <div style={{ padding: 3 }}>
                <div
                  className="cw-widget-container-pdf"
                  ref={(el) => (refs.current = [...refs.current, el])}
                >
                  <CWTrendChart
                    widget_info={widget}
                    customerInfo={customerInfo}
                    darkmode={true}
                  />
                </div>
              </div>
            </Col>
          );
        case 1:
          return (
            <Col
              span={
                widget.widget_size !== "0" && screenSize.width > sizeMobile
                  ? 12
                  : 24
              }
              key={`col-${widget.id}`}
            >
              <div style={{ padding: 3 }}>
                <div
                  className="cw-widget-container-pdf"
                  ref={(el) => (refs.current = [...refs.current, el])}
                >
                  <CWBarChart
                    widget_info={widget}
                    customerInfo={customerInfo}
                    darkmode={true}
                  />
                </div>
              </div>
            </Col>
          );
        case 2:
          return (
            <Col
              span={
                widget.widget_size !== "0" && screenSize.width > sizeMobile
                  ? 12
                  : 24
              }
              key={`col-${widget.id}`}
            >
              <div style={{ padding: 3 }}>
                <div
                  className="cw-widget-container-pdf"
                  ref={(el) => (refs.current = [...refs.current, el])}
                >
                  <CWGaugeChart
                    widget_info={widget}
                    customerInfo={customerInfo}
                    darkmode={true}
                  />
                </div>
              </div>
            </Col>
          );
        case 3:
          return (
            <Col
              span={
                widget.widget_size !== "0" && screenSize.width > sizeMobile
                  ? 12
                  : 24
              }
              key={`col-${widget.id}`}
            >
              <div style={{ padding: 3 }}>
                <div
                  className="cw-widget-container-pdf"
                  ref={(el) => (refs.current = [...refs.current, el])}
                >
                  <CWPieChart
                    widget_info={widget}
                    customerInfo={customerInfo}
                  />
                </div>
              </div>
            </Col>
          );
        case 4:
          return (
            <Col
              span={
                widget.widget_size !== "0" && screenSize.width > sizeMobile
                  ? 12
                  : 24
              }
              key={`col-${widget.id}`}
            >
              <div style={{ padding: 3 }}>
                <div
                  className="cw-widget-container-pdf"
                  ref={(el) => (refs.current = [...refs.current, el])}
                >
                  <CWTextInfo
                    widget_info={widget}
                    customerInfo={customerInfo}
                    darkmode={true}
                  />
                </div>
              </div>
            </Col>
          );
        case 5:
          return (
            <Col
              span={
                widget.widget_size !== "0" && screenSize.width > sizeMobile
                  ? 12
                  : 24
              }
              key={`col-${widget.id}`}
            >
              <div style={{ padding: 3 }}>
                <div
                  className="cw-widget-container-pdf"
                  ref={(el) => (refs.current = [...refs.current, el])}
                >
                  <CWMachineInfo
                    widget_info={widget}
                    customerInfo={customerInfo}
                    darkmode={true}
                  />
                </div>
              </div>
            </Col>
          );
        default:
          return (
            <Col
              span={
                widget.widget_size !== "0" && screenSize.width > sizeMobile
                  ? 12
                  : 24
              }
              key={`col-${widget.id}`}
            >
              <div style={{ padding: 3 }}>
                <div
                  className="cw-widget-container-pdf"
                  ref={(el) => (refs.current = [...refs.current, el])}
                >
                  <CWTrendChart
                    widget_info={widget}
                    customerInfo={customerInfo}
                    darkmode={true}
                  />
                </div>
              </div>
            </Col>
          );
      }
    }
  });

  const onClickOK = async () => {
    const widgetDatas = [];
    for (let i = 0; i < refs.current.length; i++) {
      let widgetData = await domtoimage.toJpeg(refs.current[i], {
        quality: 1,
      });
      widgetDatas.push(widgetData);
    }
    var logoData = await domtoimage.toJpeg(logoRef.current, {
      quality: 1,
    });
    proceed({ widgetDatas: widgetDatas, logoData: logoData });
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
          <div ref={logoRef} style={{ background: "white", padding: 5 }}>
            <img
              src={customerInfo.logo}
              style={{ maxHeight: 80, maxWidth: 150 }}
            />
          </div>
        </Col>
        <Col
          span={12}
          style={{ textAlign: "center", fontSize: 25, fontWeight: "bold" }}
        >
          MMS Custom Dashboard
        </Col>
        <Col span={6}></Col>
      </Row>
      <div style={{ marginTop: 20 }}>
        <Row>{mainUI}</Row>
      </div>
    </Modal>
  );
};

export default confirmable(PDFPreviewModal);
