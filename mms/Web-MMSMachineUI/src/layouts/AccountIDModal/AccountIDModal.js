import { Button, Col, message, Row } from "antd";
import Modal from "antd/lib/modal/Modal";
import { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { isSpinning, setAppDataStore } from "redux/actions/appActions";
import { apiCallForLoginWithCustomerId } from "services/apiCall";

import {
  appData,
  setAppData
} from "services/global";
import "./AccountIDModal.css";

function AccountIDModal(props) {
  const dispatch = useDispatch();
  const { appDataStore } = useSelector((x) => x.appDataStore);
  const { customer_id} = appDataStore;

  const { showModal, setShowModal } = props;
  const [accountId, setAccountId] = useState("");

  useEffect(() => {
    setAccountId(customer_id);
  }, [customer_id]);

  const onCancel = () => {
    setShowModal(false);
  };

  const onOK = () => {
    if (accountId === "" || accountId === undefined) return;
    dispatch(isSpinning(true));
    apiCallForLoginWithCustomerId(accountId, "")
      .then((res) => {
        let newAppData = {
          ...appData,
          ...{ customer_id: accountId, customer_details: res },
        };
        setAppData(newAppData);
        dispatch(isSpinning(false));
        dispatch(setAppDataStore(appData));
        setShowModal(false);
      })
      .catch((err) => {
        dispatch(isSpinning(false));
        message.error(err);
      });
  };

  return (
    <div>
      <Modal
        centered
        open={showModal}
        className="account-id-dialog-style"
        title={null}
        onCancel={() => onCancel()}
        closable={false}
        maskClosable={false}
        destroyOnClose={true}
        header={null}
        footer={null}
      >
        <div>
          <Row align="middle" className="account-id-dialog-top">
            <Col>
              <span className="account-id-dialog-title">
                {"Account ID Input"}
              </span>
            </Col>
          </Row>
          <Row className="account-id-dialog-content">
            <Col span={24} style={{ textAlign: "center" }}>
              <span className="account-id-dialog-input-desc">
                {"Please Input Account ID"}
              </span>
            </Col>
            <Col span={24}>
              <input
                className="account-id-dialog-input"
                value={accountId}
                onChange={(e) => setAccountId(e.target.value)}
                style={{ outlineStyle: "none" }}
              />
            </Col>

            <Col span={12}>
              <Button
                className="account-id-dialog-button"
                style={{ marginRight: "5px" }}
                onClick={onCancel}
                type="primary"
              >
                {"Cancel"}
              </Button>
            </Col>
            <Col span={12}>
              <Button
                className="account-id-dialog-button"
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

export default AccountIDModal;
