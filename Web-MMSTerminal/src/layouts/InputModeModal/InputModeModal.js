import { Button, Col, Row } from "antd";
import Modal from "antd/lib/modal/Modal";
import RadialInput from "components/RadialInput/RadialInput";
import { useState } from "react";
import "./InputModeModal.css";

function InputModeModal(props) {
  const { showModal, setShowModal, title, setShowInput, onLogoutJobID } = props;
  const [status, setStatus] = useState(false);

  const onCancel = () => {
    setShowModal(false);
  };

  const onManualInput = () => {
    setShowInput(true);
    setShowModal(false);
  };

  const onScanID = () => {
    setShowInput(true);
    setShowModal(false);
  };

  return (
    <div>
      <Modal
        centered
        open={showModal}
        className="input-mode-dialog-style"
        title={null}
        onCancel={() => onCancel()}
        closable={false}
        maskClosable={false}
        destroyOnClose={true}
        header={null}
        footer={null}
      >
        <div>
          <Row align="middle" className="input-mode-dialog-top">
            <Col>
              <span className="input-mode-dialog-title">{title}</span>
            </Col>
          </Row>
          <Row className="input-mode-dialog-content">
            <Col span={24}>
              <Button
                className="input-mode-dialog-button"
                onClick={onManualInput}
                type="primary"
              >
                {"Manual Input"}
              </Button>
            </Col>
            <Col span={24}>
              <Button
                className="input-mode-dialog-button"
                onClick={onScanID}
                type="primary"
              >
                {"Scan ID"}
              </Button>
            </Col>

            {onLogoutJobID !== undefined && (
              <Col span={24}>
                <Button
                  className="input-mode-dialog-button"
                  onClick={onScanID}
                  type="primary"
                >
                  {"Log Out of Job"}
                </Button>
              </Col>
            )}

            {onLogoutJobID !== undefined && (
              <Col span={24}>
                <div className="input-mode-dialog-status-left">
                  <RadialInput
                    value={status}
                    title="Setup Status"
                    setValue={setStatus}
                  />
                </div>
                <div className="input-mode-dialog-status-right">
                  <Button
                    className="input-mode-dialog-status-button"
                    onClick={() => {}}
                    type="primary"
                  >
                    {"Update"}
                  </Button>
                </div>
              </Col>
            )}

            <Col span={24}>
              <Button
                className="input-mode-dialog-button"
                style={{ marginRight: "5px" }}
                onClick={onCancel}
                type="primary"
              >
                {"Cancel"}
              </Button>
            </Col>
          </Row>
        </div>
      </Modal>
    </div>
  );
}

export default InputModeModal;
