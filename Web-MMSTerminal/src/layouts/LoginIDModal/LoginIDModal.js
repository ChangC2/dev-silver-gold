import { Button, Col, message, Row } from "antd";
import Modal from "antd/lib/modal/Modal";
import { useState } from "react";
import { useDispatch } from "react-redux";
import { isSpinning } from "redux/actions/appActions";
import { setUserDataStore } from "redux/actions/userActions";
import { apiCallForLogin } from "services/apiCall";
import { LS_ITEMS} from "services/CONSTANTS";
import "./LoginIDModal.css";
import { setUserData, userData } from "services/global";

function LoginIDModal(props) {
  const dispatch = useDispatch();

  const { showModal, setShowModal } = props;
  const [userID, setUserID] = useState("");

  const onCancel = () => {
    setUserID("");
    setShowModal(false);
  };

  const onOK = () => {
    if (userID === "" || userID === undefined) return;
    dispatch(isSpinning(true));
    apiCallForLogin(userID, "")
      .then((res) => {
        setUserID("");
        setShowModal(false);
        dispatch(isSpinning(false));
        setUserData(res);
        dispatch(setUserDataStore(userData));
      })
      .catch((err) => {
        dispatch(isSpinning(false));
        message.error("Login Fail. Please try again.");
      });
  };

  return (
    <div>
      <Modal
        centered
        open={showModal}
        className="login-id-dialog-style"
        title={null}
        onCancel={() => onCancel()}
        closable={false}
        maskClosable={false}
        destroyOnClose={true}
        header={null}
        footer={null}
      >
        <div>
          <Row align="middle" className="login-id-dialog-top">
            <Col>
              <span className="login-id-dialog-title">Login ID Input</span>
            </Col>
          </Row>
          <Row className="login-id-dialog-content">
            <Col span={24} style={{ textAlign: "center" }}>
              <span className="login-id-dialog-input-desc">
                Please Input Login ID
              </span>
            </Col>
            <Col span={24}>
              <input
                className="login-id-dialog-input"
                value={userID}
                onChange={(e) => setUserID(e.target.value)}
                style={{ outlineStyle: "none" }}
              />
            </Col>

            <Col span={12}>
              <Button
                className="login-id-dialog-button"
                style={{ marginRight: "5px" }}
                onClick={onCancel}
                type="primary"
              >
                {"Cancel"}
              </Button>
            </Col>
            <Col span={12}>
              <Button
                className="login-id-dialog-button"
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

export default LoginIDModal;
