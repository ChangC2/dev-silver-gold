import { Button, Col, Row } from "antd";
import Modal from "antd/lib/modal/Modal";
import { useEffect, useState } from "react";
import { useSelector } from "react-redux";
import "./RejectReasonsModal.css";
import TextArea from "antd/lib/input/TextArea";

function RejectReasonsModal(props) {
  const { showModal, setShowModal, titles, setTitles, index } = props;
  const [title, setTitle] = useState("");

  useEffect(() => {
    setTitle(titles[index]);
  }, [titles, index]);

  const onCancel = () => {
    setShowModal(false);
  };

  const onOK = () => {
    let newTitles = titles;
    newTitles[index] = title;
    setTitles(newTitles);
    setShowModal(false);
  };

  return (
    <div>
      <Modal
        centered
        open={showModal}
        className="reject-reason-title-dialog-style"
        title={null}
        onCancel={() => onCancel()}
        closable={false}
        maskClosable={false}
        destroyOnClose={true}
        header={null}
        footer={null}
      >
        <div>
          <Row align="middle" className="reject-reason-title-dialog-top">
            <Col>
              <span className="reject-reason-title-dialog-title">
                {"Edit Title"}
              </span>
            </Col>
          </Row>
          <Row className="reject-reason-title-dialog-content">
            <Col span={24} style={{ textAlign: "center" }}>
              <span className="reject-reason-title-dialog-input-desc">
                {"Please Input Title"}
              </span>
            </Col>
            <Col span={24}>
              <input
                className="reject-reason-title-dialog-input"
                value={title}
                onChange={(e) => setTitle(e.target.value)}
                style={{ outlineStyle: "none" }}
              />
            </Col>

            <Col span={12}>
              <Button
                className="reject-reason-title-dialog-button"
                style={{ marginRight: "5px" }}
                onClick={onCancel}
                type="primary"
              >
                {"Cancel"}
              </Button>
            </Col>
            <Col span={12}>
              <Button
                className="reject-reason-title-dialog-button"
                style={{ marginLeft: "5px" }}
                onClick={onOK}
                type="primary"
              >
                {"Ok"}
              </Button>
            </Col>
          </Row>
        </div>
      </Modal>
    </div>
  );
}

export default RejectReasonsModal;
