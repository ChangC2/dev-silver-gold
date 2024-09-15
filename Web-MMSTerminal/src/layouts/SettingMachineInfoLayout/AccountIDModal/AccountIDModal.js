import { Button, Col, message, Row } from "antd";
import Modal from "antd/lib/modal/Modal";
import { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { isSpinning } from "redux/actions/appActions";
import { apiCallForLoginWithCustomerId } from "services/apiCall";

import { factoryData, setFactoryData } from "services/global";
import { setFactoryDataStore } from "redux/actions/factoryActions";
import "./AccountIDModal.css";

function AccountIDModal(props) {
  const dispatch = useDispatch();
  const { factoryDataStore } = useSelector((x) => x.factoryDataStore);

  const { showModal, setShowModal } = props;
  const [accountId, setAccountId] = useState("");

  useEffect(() => {
    setAccountId(factoryDataStore.accountId);
  }, [factoryDataStore.accountId]);

  const onCancel = () => {
    //setAccountId("");
    setShowModal(false);
  };

  const onOK = () => {
    if (accountId === "" || accountId === undefined) return;
    dispatch(isSpinning(true));
    apiCallForLoginWithCustomerId(accountId, "")
      .then((res) => {
        let newSettings = { ...factoryDataStore, ...{accountId: accountId, customer_details: res}};

        setFactoryData(newSettings)
        dispatch(isSpinning(false));
        dispatch(setFactoryDataStore(factoryData));
        //setAccountId("");
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
