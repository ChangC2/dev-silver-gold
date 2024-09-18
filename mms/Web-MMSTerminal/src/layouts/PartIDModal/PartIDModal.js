import { Button, Col, Row } from "antd";
import Modal from "antd/lib/modal/Modal";
import { useEffect, useState } from "react";
import { useSelector } from "react-redux";
import "./PartIDModal.css";

function PartIDModal(props) {
  const authData = useSelector((x) => x.authService);
  const { showModal, setShowModal, partID, setPartID } = props;
  const [pID, setPID] = useState("");

  useEffect(() => {
    setPID(partID);
  }, [partID]);

  const onCancel = () => {
    setPID(partID)
    setShowModal(false);
  };

  const onOK = () => {
    setPartID(pID);
    setShowModal(false);
  };

  return (
    <div>
      <Modal
        centered
        open={showModal}
        className="part-id-dialog-style"
        title={null}
        onCancel={() => onCancel()}
        closable={false}
        maskClosable={false}
        destroyOnClose={true}
        header={null}
        footer={null}
      >
        <div>
          <Row align="middle" className="part-id-dialog-top">
            <Col>
              <span className="part-id-dialog-title">Part ID Input</span>
            </Col>
          </Row>
          <Row className="part-id-dialog-content">
            <Col span={24} style={{ textAlign: "center" }}>
              <span className="part-id-dialog-input-desc">
                Please Input Part ID
              </span>
            </Col>
            <Col span={24}>
              <input
                className="part-id-dialog-input"
                value={pID}
                onChange={(e) => setPID(e.target.value)}
                style={{ outlineStyle: "none" }}
              />
            </Col>

            <Col span={12}>
              <Button
                className="part-id-dialog-button"
                style={{ marginRight: "5px" }}
                onClick={onCancel}
                type="primary"
              >
                {"Cancel"}
              </Button>
            </Col>
            <Col span={12}>
              <Button
                className="part-id-dialog-button"
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

export default PartIDModal;
