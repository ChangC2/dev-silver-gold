import { Button, Col, Row } from "antd";
import Modal from "antd/lib/modal/Modal";
import "./LogoutModal.css";

function LogoutModal(props) {
  const { showModal, setShowModal, onLogout } = props;
  const onCancel = () => {
    setShowModal(false);
  };

  const onOK = () => {
    onLogout();
    setShowModal(false);
  };

  return (
    <div>
      <Modal
        centered
        open={showModal}
        className="logout-dialog-style"
        title={null}
        onCancel={() => onCancel()}
        closable={false}
        maskClosable={false}
        destroyOnClose={true}
        header={null}
        footer={null}
      >
        <div>
          <Row align="middle" className="logout-dialog-top">
            <Col>
              <span className="logout-dialog-title">Alert</span>
            </Col>
          </Row>
          <Row className="logout-dialog-content">
            <Col span={24} style={{ textAlign: "center" }}>
              <span className="logout-dialog-input-desc">
                Are you sure want to logout?
              </span>
            </Col>
            <Col span={12}>
              <Button
                className="logout-dialog-button"
                style={{ marginRight: "5px" }}
                onClick={onCancel}
                type="primary"
              >
                {"Cancel"}
              </Button>
            </Col>
            <Col span={12}>
              <Button
                className="logout-dialog-button"
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

export default LogoutModal;
