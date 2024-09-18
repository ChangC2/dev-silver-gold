import { Button, Col, Row } from "antd";
import Modal from "antd/lib/modal/Modal";
import "./AlertDlg.css";

function AlertDlg(props) {
  const { title, showModal, setShowModal, onOK } = props;
  const onCancel = () => {
    setShowModal(false);
  };

  const onOKClicked = () => {
    onOK();
    setShowModal(false);
  };

  return (
    <div>
      <Modal
        centered
        open={showModal}
        className="alert-dialog-style"
        title={null}
        onCancel={() => onCancel()}
        closable={false}
        maskClosable={false}
        destroyOnClose={true}
        header={null}
        footer={null}
      >
        <div>
          <Row align="middle" className="alert-dialog-top">
            <Col>
              <span className="alert-dialog-title">Alert</span>
            </Col>
          </Row>
          <Row className="alert-dialog-content">
            <Col span={24} style={{ textAlign: "center" }}>
              <span className="alert-dialog-input-desc">
                {title}
              </span>
            </Col>
            <Col span={12}>
              <Button
                className="alert-dialog-button"
                style={{ marginRight: "5px" }}
                onClick={onCancel}
                type="primary"
              >
                {"Cancel"}
              </Button>
            </Col>
            <Col span={12}>
              <Button
                className="alert-dialog-button"
                style={{ marginLeft: "5px" }}
                onClick={onOKClicked}
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

export default AlertDlg;
